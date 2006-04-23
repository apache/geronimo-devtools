/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v11.core;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.directory.NoSuchAttributeException;

import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.gbean.AbstractNameQuery;
import org.apache.geronimo.gbean.GBeanData;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.config.PersistentConfigurationList;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.core.DeploymentUtils;
import org.apache.geronimo.st.core.GenericGeronimoServerBehaviour;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.st.core.IGeronimoServer;
import org.apache.geronimo.st.core.PingThread;
import org.apache.geronimo.st.core.UpdateServerStateTask;
import org.apache.geronimo.st.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.core.commands.IDeploymentCommand;
import org.apache.geronimo.st.core.commands.TargetModuleIdNotFoundException;
import org.apache.geronimo.st.v11.core.internal.EclipseAwareConfigurationStore;
import org.apache.geronimo.st.v11.core.internal.Messages;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.apache.geronimo.system.jmx.KernelDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jst.server.generic.core.internal.CorePlugin;
import org.eclipse.jst.server.generic.core.internal.GenericServerCoreMessages;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.util.SocketUtil;

public class GeronimoServerBehaviour extends GenericGeronimoServerBehaviour {

	private static final String ATTR_STOP = "stop-server";

	private static final int TIMER_TASK_INTERVAL = 10;

	private IProgressMonitor _monitor = null;

	private Kernel kernel = null;

	private Timer timer = null;
	PingThread pingThread;

	public GeronimoServerBehaviour() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#stop(boolean)
	 */
	public synchronized void stop(boolean force) {

		Trace.trace(Trace.INFO, "--> stop()");

		if (getServer().getServerState() != IServer.STATE_STOPPED) {
			setServerState(IServer.STATE_STOPPING);
			if (kernel != null) {
				kernel.shutdown();
			}
		}

		GeronimoConnectionFactory.getInstance().destroy(getServer());
		kernel = null;

		// kill the process
		super.stop(true);

		Trace.trace(Trace.INFO, "<-- stop()");
	}

	private Kernel getKernel() throws SecurityException {

		if (kernel == null) {
			Map map = new HashMap();
			String user = getGeronimoServer().getAdminID();
			String password = getGeronimoServer().getAdminPassword();
			map.put("jmx.remote.credentials", new String[] { user, password });
			try {
				String url = getGeronimoServer().getJMXServiceURL();
				Trace.trace(Trace.INFO, "URL = " + url);
				if (url == null)
					return null;
				JMXServiceURL address = new JMXServiceURL(url);
				try {
					JMXConnector jmxConnector = JMXConnectorFactory.connect(address, map);
					MBeanServerConnection mbServerConnection = jmxConnector.getMBeanServerConnection();
					kernel = new KernelDelegate(mbServerConnection);
					Trace.trace(Trace.INFO, "Connected to kernel.");
				} catch (SecurityException e) {
					throw e;
				} catch (Exception e) {
					Trace.trace(Trace.WARNING, "Kernel connection failed.");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return kernel;
	}
	
	public void installRepo() {
		try {
			AbstractName abstractName = kernel.getNaming().createRootName(new Artifact("eclipse", "eclipse", "1.0", "car"), "EclipseConfigStore", "ConfigurationStore");
			GBeanData mockGBean = new GBeanData(abstractName, EclipseAwareConfigurationStore.getGBeanInfo());
			AbstractNameQuery query = new AbstractNameQuery("geronimo/rmi-naming/1.1-SNAPSHOT/car");
			mockGBean.addDependency(query);
			kernel.loadGBean(mockGBean, getKernel().getClass().getClassLoader());
			kernel.startGBean(mockGBean.getAbstractName());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public boolean isKernelAlive() {
		try {
			return getKernel() != null && kernel.isRunning();
		} catch (SecurityException e) {
			Activator.log(Status.ERROR, "Invalid username and/or password.", e);
			pingThread.interrupt();
			if (getServer().getServerState() != IServer.STATE_STOPPED) {
				stop(true);
			}
		} catch (Exception e) {
			Activator.log(Status.WARNING, "Geronimo Server may have been terminated manually outside of workspace.", e);
			kernel = null;
		}
		return false;
	}

	public boolean isFullyStarted() {
		if (isKernelAlive()) {
			AbstractNameQuery query = new AbstractNameQuery(PersistentConfigurationList.class.getName());
			Set configLists = kernel.listGBeans(query);
			if (!configLists.isEmpty()) {
				AbstractName on = (AbstractName) configLists.toArray()[0];
				try {
					Boolean b = (Boolean) kernel.getAttribute(on, "kernelFullyStarted");
					return b.booleanValue();
				} catch (GBeanNotFoundException e) {
					// ignore
				} catch (NoSuchAttributeException e) {
					// ignore
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Trace.trace(Trace.INFO, "configLists is empty");
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#publishModule(int,
	 *      int, org.eclipse.wst.server.core.IModule[],
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void publishModule(int kind, int deltaKind, IModule[] module,
			IProgressMonitor monitor) throws CoreException {
		
		//installRepo();
		
		Trace.trace(Trace.INFO, ">> publishModule(), deltaKind = " + deltaKind);
		Trace.trace(Trace.INFO, Arrays.asList(module).toString());
		_monitor = monitor;

		if (module.length == 1 && (deltaKind == ADDED || deltaKind == REMOVED)) {
			invokeCommand(deltaKind, module[0]);
		} else if (deltaKind == CHANGED) {
			// TODO This case is flawed due to WTP Bugzilla 123676
			invokeCommand(deltaKind, module[0]);
		}

		setModulePublishState(module, IServer.PUBLISH_STATE_NONE);

		Trace.trace(Trace.INFO, "<< publishModule()");
	}

	private void invokeCommand(int deltaKind, IModule module)
			throws CoreException {
		try {
			switch (deltaKind) {
			case ADDED: {
				doDeploy(module);
				break;
			}
			case CHANGED: {
				doRedeploy(module);
				break;
			}
			case REMOVED: {
				doUndeploy(module);
				break;
			}
			default:
				throw new IllegalArgumentException();
			}
		} catch (CoreException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doDeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doDeploy() " + module.toString());

		DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(getServer());

		if (!DeploymentUtils.configurationExists(module, dm)) {
			IStatus status = distribute(module);
			if (!status.isOK()) {
				doFail(status, Messages.DISTRIBUTE_FAIL);
			}

			status = start(module);
			if (!status.isOK()) {
				doFail(status, Messages.START_FAIL);
			}
		} else {
			String id = GeronimoV11Utils.getConfigId(module);
			String message = id
					+ "already exists.  Existing configuration will be overwritten.";
			Activator.log(Status.ERROR, message, null);
			doRedeploy(module);
		}

		Trace.trace(Trace.INFO, "<< doDeploy() " + module.toString());
	}

	private void doRedeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doRedeploy() " + module.toString());

		try {
			IStatus status = reDeploy(module);
			if (!status.isOK()) {
				doFail(status, Messages.REDEPLOY_FAIL);
			}
		} catch (TargetModuleIdNotFoundException e) {
			Activator.log(Status.WARNING, "Module may have been uninstalled outside the workspace.", e);
			doDeploy(module);
		}

		Trace.trace(Trace.INFO, "<< doRedeploy() " + module.toString());
	}

	private void doUndeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doUndeploy() " + module.toString());

		IStatus status = stop(module);
		if (!status.isOK()) {
			doFail(status, Messages.STOP_FAIL);
		}

		status = unDeploy(module);
		if (!status.isOK()) {
			doFail(status, Messages.UNDEPLOY_FAIL);
		}

		Trace.trace(Trace.INFO, "<< doUndeploy()" + module.toString());
	}

	private void doRestart(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doRestart() " + module.toString());

		IStatus status = stop(module);
		if (!status.isOK()) {
			doFail(status, Messages.STOP_FAIL);
		}

		status = start(module);
		if (!status.isOK()) {
			doFail(status, Messages.START_FAIL);
		}

		Trace.trace(Trace.INFO, ">> doRestart() " + module.toString());
	}

	private void doFail(IStatus status, String message) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, message, new Exception(status.getMessage())));
	}

	private IStatus distribute(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory.createDistributeCommand(module, getServer());
		return cmd.execute(_monitor);
	}

	private IStatus start(IModule module) throws Exception {
		TargetModuleID id = DeploymentUtils.getTargetModuleID(module, DeploymentCommandFactory.getDeploymentManager(getServer()));
		IDeploymentCommand cmd = DeploymentCommandFactory.createStartCommand(new TargetModuleID[] { id }, module, getServer());
		return cmd.execute(_monitor);
	}

	private IStatus stop(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory.createStopCommand(module, getServer());
		return cmd.execute(_monitor);
	}

	private IStatus unDeploy(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory.createUndeployCommand(module, getServer());
		return cmd.execute(_monitor);
	}

	private IStatus reDeploy(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory.createRedeployCommand(module, getServer());
		return cmd.execute(_monitor);
	}

	public Map getServerInstanceProperties() {
		return getRuntimeDelegate().getServerInstanceProperties();
	}

	public IGeronimoServer getGeronimoServer() {
		return (IGeronimoServer) getServer().loadAdapter(IGeronimoServer.class, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#setupLaunchConfiguration(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void setupLaunchConfiguration(
			ILaunchConfigurationWorkingCopy workingCopy,
			IProgressMonitor monitor) throws CoreException {
		String defaultArgs = getServerDefinition().getResolver().resolveProperties(getServerDefinition().getStart().getProgramArgumentsAsString());
		String existingPrgArgs = workingCopy.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, defaultArgs);
		super.setupLaunchConfiguration(workingCopy, monitor);
		if (existingPrgArgs != null) {
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, existingPrgArgs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour#setupLaunch(org.eclipse.debug.core.ILaunch,
	 *      java.lang.String, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void setupLaunch(ILaunch launch, String launchMode,
			IProgressMonitor monitor) throws CoreException {
		Trace.trace(Trace.INFO, "--> GeronimoServerBehavior.setupLaunch()");

		if ("true".equals(launch.getLaunchConfiguration().getAttribute(ATTR_STOP, "false")))
			return;

		if (!SocketUtil.isLocalhost(getServer().getHost()))
			return;

		ServerPort[] ports = getServer().getServerPorts(null);
		for (int i = 0; i < ports.length; i++) {
			ServerPort sp = ports[i];
			if (SocketUtil.isPortInUse(ports[i].getPort(), 5))
				throw new CoreException(new Status(IStatus.ERROR, CorePlugin.PLUGIN_ID, 0, GenericServerCoreMessages.bind(GenericServerCoreMessages.errorPortInUse, Integer.toString(sp.getPort()), sp.getName()), null));
		}

		stopUpdateServerStateTask();
		setServerState(IServer.STATE_STARTING);
		setMode(launchMode);

		IServerListener listener = new IServerListener() {
			public void serverChanged(ServerEvent event) {
				int eventKind = event.getKind();
				if (eventKind == (ServerEvent.SERVER_CHANGE | ServerEvent.STATE_CHANGE)) {
					IServer server = event.getServer();
					int state = server.getServerState();
					if (state == IServer.STATE_STARTED
							|| state == IServer.STATE_STOPPED) {
						GeronimoServerBehaviour.this.getServer().removeServerListener(this);
						startUpdateServerStateTask();
					}
				}
			}
		};

		getServer().addServerListener(listener);
		pingThread = new PingThread(this, getServer());
		pingThread.start();
		Trace.trace(Trace.INFO, "<-- GeronimoServerBehavior.setupLaunch()");
	}

	private void startUpdateServerStateTask() {
		Trace.trace(Trace.INFO, "startUpdateServerStateTask() "
				+ getServer().getName());
		timer = new Timer(true);
		timer.schedule(new UpdateServerStateTask(this, getServer()), 10000, TIMER_TASK_INTERVAL * 1000);
	}

	private void stopUpdateServerStateTask() {
		Trace.trace(Trace.INFO, "stopUpdateServerStateTask() "
				+ getServer().getName());
		if (timer != null)
			timer.cancel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#initialize(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected void initialize(IProgressMonitor monitor) {
		Trace.trace(Trace.INFO, "GeronimoServerBehavior.initialize()");
		startUpdateServerStateTask();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#dispose()
	 */
	public void dispose() {
		stopUpdateServerStateTask();
	}

}