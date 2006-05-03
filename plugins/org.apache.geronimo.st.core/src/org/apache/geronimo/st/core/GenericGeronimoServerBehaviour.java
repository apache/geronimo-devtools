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
package org.apache.geronimo.st.core;

import java.util.Arrays;
import java.util.Map;
import java.util.Timer;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;

import org.apache.geronimo.st.core.commands.DeploymentCmdStatus;
import org.apache.geronimo.st.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.core.commands.IDeploymentCommand;
import org.apache.geronimo.st.core.commands.TargetModuleIdNotFoundException;
import org.apache.geronimo.st.core.internal.Messages;
import org.apache.geronimo.st.core.internal.Trace;
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
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.util.SocketUtil;

abstract public class GenericGeronimoServerBehaviour extends
		GenericServerBehaviour implements IGeronimoServerBehavior {
	
	public static final String ATTR_STOP = "stop-server";

	public static final int TIMER_TASK_INTERVAL = 10;

	protected IProgressMonitor _monitor;

	protected Timer timer = null;

	protected PingThread pingThread;

	public void setServerStarted() {
		setServerState(IServer.STATE_STARTED);
	}

	public void setServerStopped() {
		setServerState(IServer.STATE_STOPPED);
	}

	public IGeronimoServer getGeronimoServer() {
		return (IGeronimoServer) getServer().loadAdapter(IGeronimoServer.class,
				null);
	}

	abstract public String getConfigId(IModule module);

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

	protected void invokeCommand(int deltaKind, IModule module)
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

	protected void doDeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doDeploy() " + module.toString());

		DeploymentManager dm = DeploymentCommandFactory
				.getDeploymentManager(getServer());

		if (!DeploymentUtils.configurationExists(module, dm)) {
			IStatus status = distribute(module);
			if (!status.isOK()) {
				doFail(status, Messages.DISTRIBUTE_FAIL);
			}
			
			TargetModuleID[] ids = ((DeploymentCmdStatus) status).getResultTargetModuleIDs();
			ModuleArtifactMapper mapper = ModuleArtifactMapper.getInstance();
			mapper.addEntry(getServer(), module.getProject(), ids[0].getModuleID());

			status = start(module);
			if (!status.isOK()) {
				doFail(status, Messages.START_FAIL);
			}
		} else {
			String id = getConfigId(module);
			String message = id
					+ "already exists.  Existing configuration will be overwritten.";
			Activator.log(Status.ERROR, message, null);
			doRedeploy(module);
		}

		Trace.trace(Trace.INFO, "<< doDeploy() " + module.toString());
	}

	protected void doRedeploy(IModule module) throws Exception {
		Trace.trace(Trace.INFO, ">> doRedeploy() " + module.toString());

		try {
			IStatus status = reDeploy(module);
			if (!status.isOK()) {
				doFail(status, Messages.REDEPLOY_FAIL);
			}
		} catch (TargetModuleIdNotFoundException e) {
			Activator.log(Status.WARNING,
					"Module may have been uninstalled outside the workspace.",
					e);
			doDeploy(module);
		}

		Trace.trace(Trace.INFO, "<< doRedeploy() " + module.toString());
	}

	protected void doUndeploy(IModule module) throws Exception {
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

	protected void doRestart(IModule module) throws Exception {
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

	protected void doFail(IStatus status, String message) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
				0, message, new Exception(status.getMessage())));
	}

	protected IStatus distribute(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory
				.createDistributeCommand(module, getServer(), getGeronimoServer().isTestEnvironment());
		return cmd.execute(_monitor);
	}

	protected IStatus start(IModule module) throws Exception {
		TargetModuleID id = DeploymentUtils.getTargetModuleID(module,
				DeploymentCommandFactory.getDeploymentManager(getServer()));
		IDeploymentCommand cmd = DeploymentCommandFactory.createStartCommand(
				new TargetModuleID[] { id }, module, getServer());
		return cmd.execute(_monitor);
	}

	protected IStatus stop(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory.createStopCommand(
				module, getServer());
		return cmd.execute(_monitor);
	}

	protected IStatus unDeploy(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory
				.createUndeployCommand(module, getServer());
		return cmd.execute(_monitor);
	}

	protected IStatus reDeploy(IModule module) throws Exception {
		IDeploymentCommand cmd = DeploymentCommandFactory
				.createRedeployCommand(module, getServer(), getGeronimoServer().isTestEnvironment());
		return cmd.execute(_monitor);
	}

	public Map getServerInstanceProperties() {
		return getRuntimeDelegate().getServerInstanceProperties();
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
		Trace.trace(Trace.INFO, "--> GeronimoServerBehavior.setupLaunch()");

		if ("true".equals(launch.getLaunchConfiguration().getAttribute(
				ATTR_STOP, "false")))
			return;

		if (!SocketUtil.isLocalhost(getServer().getHost()))
			return;

		ServerPort[] ports = getServer().getServerPorts(null);
		for (int i = 0; i < ports.length; i++) {
			ServerPort sp = ports[i];
			if (SocketUtil.isPortInUse(ports[i].getPort(), 5))
				throw new CoreException(new Status(IStatus.ERROR,
						CorePlugin.PLUGIN_ID, 0, GenericServerCoreMessages
								.bind(GenericServerCoreMessages.errorPortInUse,
										Integer.toString(sp.getPort()), sp
												.getName()), null));
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
						GenericGeronimoServerBehaviour.this.getServer()
								.removeServerListener(this);
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

	protected void startUpdateServerStateTask() {
		Trace.trace(Trace.INFO, "startUpdateServerStateTask() "
				+ getServer().getName());
		timer = new Timer(true);
		timer.schedule(new UpdateServerStateTask(this, getServer()), 10000,
				TIMER_TASK_INTERVAL * 1000);
	}

	protected void stopUpdateServerStateTask() {
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
