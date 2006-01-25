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
package org.apache.geronimo.core.internal;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.geronimo.core.DeploymentUtils;
import org.apache.geronimo.core.GeronimoConnectionFactory;
import org.apache.geronimo.core.commands.DeploymentCmdStatus;
import org.apache.geronimo.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.core.commands.IDeploymentCommand;
import org.apache.geronimo.core.commands.TargetModuleIdNotFoundException;
import org.apache.geronimo.gbean.GBeanQuery;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.NoSuchAttributeException;
import org.apache.geronimo.kernel.config.PersistentConfigurationList;
import org.apache.geronimo.kernel.jmx.KernelDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jst.server.generic.core.internal.CorePlugin;
import org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour;
import org.eclipse.jst.server.generic.core.internal.GenericServerCoreMessages;
import org.eclipse.jst.server.generic.core.internal.PingThread;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.util.SocketUtil;

public class GeronimoServerBehaviour extends GenericServerBehaviour {

	private static final String ATTR_STOP = "stop-server";

	private static final int MAX_TRIES = 30;

	private static final int TIMER_TASK_INTERVAL = 10;

	private IProgressMonitor _monitor = null;

	private Kernel kernel = null;

	private Timer timer = null;

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
			if (getKernel() != null) {
				kernel.shutdown();
			}
		}

		GeronimoConnectionFactory.getInstance().destroy(getServer());
		kernel = null;

		// kill the process
		super.stop(true);

		Trace.trace(Trace.INFO, "<-- stop()");
	}

	private String getJMXServiceURL() {

		String host = getServer().getHost();
		String rmiPort = getRMINamingPort();

		if (host != null && rmiPort != null) {
			return "service:jmx:rmi://" + host + "/jndi/rmi://" + host + ":"
					+ rmiPort + "/JMXConnector";
		}

		return null;

	}

	private Kernel getKernel() {

		if (kernel == null) {
			Map map = new HashMap();
			map.put("jmx.remote.credentials", new String[] { getUserName(),
					getPassword() });
			try {
				String url = getJMXServiceURL();
				Trace.trace(Trace.INFO, "URL = " + url);
				if (url == null)
					return null;
				JMXServiceURL address = new JMXServiceURL(url);
				try {
					JMXConnector jmxConnector = JMXConnectorFactory.connect(
							address, map);
					MBeanServerConnection mbServerConnection = jmxConnector
							.getMBeanServerConnection();
					kernel = new KernelDelegate(mbServerConnection);
					Trace.trace(Trace.INFO, "Connected to kernel.");
				} catch (Exception e) {
					Trace.trace(Trace.WARNING, "Kernel connection failed.");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return kernel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour#setServerStarted()
	 */
	protected void setServerStarted() {
		Trace.trace(Trace.INFO, "--> setServerStarted()");
		for (int tries = MAX_TRIES; tries > 0; tries--) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// ignore
			}
			boolean isFullyStarted = isKernelFullyStarted();
			Trace.trace(Trace.INFO, "kernelFullyStarted = " + isFullyStarted);
			if (isFullyStarted) {
				setServerState(IServer.STATE_STARTED);
				break;
			}
		}
		Trace.trace(Trace.INFO, "<-- setServerStarted()");
	}

	protected boolean isKernelAlive() {
		try {
			return getKernel() != null && kernel.isRunning();
		} catch (Exception e) {
			GeronimoPlugin
					.log(
							Status.WARNING,
							"Geronimo Server may have been terminated manually outside of workspace.",
							e);
			kernel = null;
		}
		return false;
	}

	protected boolean isKernelFullyStarted() {
		if (isKernelAlive()) {
			Set configLists = kernel.listGBeans(new GBeanQuery(null,
					PersistentConfigurationList.class.getName()));
			if (!configLists.isEmpty()) {
				ObjectName on = (ObjectName) configLists.toArray()[0];
				try {
					Boolean b = (Boolean) kernel.getAttribute(on,
							"kernelFullyStarted");
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

	private void deploy(IModule module) throws Exception {
		DeploymentManager dm = DeploymentCommandFactory
				.getDeploymentManager(getServer());
		if (!DeploymentUtils.configurationExists(module, dm)) {
			doDeploy(module);
		} else {
			GeronimoPlugin
					.log(
							Status.WARNING,
							"Configuration with id "
									+ GeronimoUtils.getConfigId(module)
									+ "already exists.  Existing configuration will be overwritten with redeploy.",
							null);
			doRedeploy(module);
		}
	}

	private void doDeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doDeploy() " + module.toString());

		IDeploymentCommand cmd = DeploymentCommandFactory
				.createDistributeCommand(module, getServer());

		IStatus status = cmd.execute(_monitor);

		if (!status.isOK()) {
			doFail(status, Messages.DISTRIBUTE_FAIL);
		}

		if (status instanceof DeploymentCmdStatus) {
			TargetModuleID[] ids = ((DeploymentCmdStatus) status)
					.getResultTargetModuleIDs();
			cmd = DeploymentCommandFactory.createStartCommand(ids, module,
					getServer());
			status = cmd.execute(_monitor);

			if (!status.isOK()) {
				doFail(status, Messages.START_FAIL);
			}
		}

		Trace.trace(Trace.INFO, "<< doDeploy() " + module.toString());
	}

	private void doRedeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doRedeploy() " + module.toString());

		IDeploymentCommand cmd = DeploymentCommandFactory
				.createRedeployCommand(module, getServer());
		try {
			IStatus status = cmd.execute(_monitor);
			if (!status.isOK()) {
				doFail(status, Messages.REDEPLOY_FAIL);
			}
		} catch (TargetModuleIdNotFoundException e) {
			GeronimoPlugin.log(Status.WARNING,
					"Module may have been uninstalled outside the workspace.",
					e);
			doDeploy(module);
		}

		Trace.trace(Trace.INFO, "<< doRedeploy() " + module.toString());
	}

	private void doUndeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doUndeploy() " + module.toString());

		IDeploymentCommand cmd = DeploymentCommandFactory.createStopCommand(
				module, getServer());

		IStatus status = cmd.execute(_monitor);

		if (!status.isOK()) {
			doFail(status, Messages.STOP_FAIL);
		}

		cmd = DeploymentCommandFactory.createUndeployCommand(module,
				getServer());
		status = cmd.execute(_monitor);

		if (!status.isOK()) {
			doFail(status, Messages.UNDEPLOY_FAIL);
		}

		Trace.trace(Trace.INFO, "<< doUndeploy()" + module.toString());
	}

	private void doFail(IStatus status, String message) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR,
				GeronimoPlugin.PLUGIN_ID, 0, message, new Exception(status
						.getMessage())));
	}

	public Map getServerInstanceProperties() {
		return getRuntimeDelegate().getServerInstanceProperties();
	}

	public String getUserName() {
		return GeronimoConnectionFactory.getInstance().getUserName(getServer());
	}

	public String getPassword() {
		return GeronimoConnectionFactory.getInstance().getPassword(getServer());
	}

	public String getRMINamingPort() {
		return GeronimoConnectionFactory.getInstance().getRMINamingPort(
				getServer());
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
		String defaultArgs = getServerDefinition().getResolver()
				.resolveProperties(
						getServerDefinition().getStart()
								.getProgramArgumentsAsString());
		String existingPrgArgs = workingCopy.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
				defaultArgs);
		super.setupLaunchConfiguration(workingCopy, monitor);
		if (existingPrgArgs != null) {
			workingCopy.setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
					existingPrgArgs);
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
		if ("true".equals(launch.getLaunchConfiguration().getAttribute(ATTR_STOP, "false"))) //$NON-NLS-1$ //$NON-NLS-2$
			return;

		String host = getServer().getHost();
		ServerPort[] ports = getServer().getServerPorts(null);
		ServerPort sp = null;
		if (SocketUtil.isLocalhost(host)) {
			for (int i = 0; i < ports.length; i++) {
				sp = ports[i];
				if (SocketUtil.isPortInUse(ports[i].getPort(), 5))
					throw new CoreException(new Status(IStatus.ERROR,
							CorePlugin.PLUGIN_ID, 0,
							GenericServerCoreMessages.bind(
									GenericServerCoreMessages.errorPortInUse,
									Integer.toString(sp.getPort()), sp
											.getName()), null));
			}
		}

		stopPing();
		setServerState(IServer.STATE_STARTING);
		setMode(launchMode);

		IServerListener listener = new IServerListener() {
			public void serverChanged(ServerEvent event) {
				int eventKind = event.getKind();
				System.out.println("eventKind = " + eventKind);
				if (eventKind == (ServerEvent.SERVER_CHANGE | ServerEvent.STATE_CHANGE)) {
					IServer server = event.getServer();
					int state = server.getServerState();
					if (state == IServer.STATE_STARTED
							|| state == IServer.STATE_STOPPED) {
						GeronimoServerBehaviour.this.getServer()
								.removeServerListener(this);
						startPing();
					}
				}
			}
		};

		getServer().addServerListener(listener);

		// ping server to check for startup
		try {
			String url = "http://" + host;
			int port = sp.getPort();
			if (port != 80)
				url += ":" + port;
			ping = new PingThread(getServer(), url, this);
		} catch (Exception e) {
			Trace.trace(Trace.SEVERE, "Can't ping for server startup.");
		}
	}

	private void startPing() {
		Trace.trace(Trace.INFO, "startPing()");
		timer = new Timer(true);
		timer.schedule(new UpdateServerStateTask(this), 10000,
				TIMER_TASK_INTERVAL * 1000);
	}

	private void stopPing() {
		Trace.trace(Trace.INFO, "stopPing()");
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
		startPing();
	}

}