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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.geronimo.core.GeronimoConnectionFactory;
import org.apache.geronimo.core.commands.DeploymentCmdStatus;
import org.apache.geronimo.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.core.commands.IDeploymentCommand;
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
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jst.server.generic.core.internal.GenericServerBehaviour;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

public class GeronimoServerBehaviour extends GenericServerBehaviour {

	private static final int MAX_TRIES = 15;

	private IProgressMonitor _monitor = null;

	private Kernel kernel = null;

	public GeronimoServerBehaviour() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#stop(boolean)
	 */
	public void stop(boolean force) {

		if (getKernel() != null) {
			// lets shutdown the kernel so shutdown messages are displayed in
			// the console view
			kernel.shutdown();
		}

		GeronimoConnectionFactory.getInstance().destroy(getServer());
		kernel = null;

		// kill the process
		super.stop(true);
	}

	private String getJMXServiceURL() {
		String host = getServer().getHost();
		return "service:jmx:rmi://" + host + "/jndi/rmi://" + host + ":"
				+ getRMINamingPort() + "/JMXConnector";
	}

	private Kernel getKernel() {

		int tries = MAX_TRIES;

		if (kernel == null) {

			Map map = new HashMap();
			map.put("jmx.remote.credentials", new String[] { getUserName(),
					getPassword() });
			try {
				String url = getJMXServiceURL();
				Trace.trace(Trace.INFO, url);
				JMXServiceURL address = new JMXServiceURL(url);
				do {
					try {

						JMXConnector jmxConnector = JMXConnectorFactory
								.connect(address, map);
						MBeanServerConnection mbServerConnection = jmxConnector
								.getMBeanServerConnection();
						kernel = new KernelDelegate(mbServerConnection);
						Trace.trace(Trace.INFO, "Connected to kernel.");
						break;
					} catch (Exception e) {
						Thread.sleep(3000);
						tries--;
						if (tries != 0) {
							Trace
									.trace(Trace.WARNING,
											"Couldn't connect to kernel.  Trying again...");
						} else {
							Trace.trace(Trace.SEVERE,
									"Connection to Geronimo kernel failed.", e);
						}
					}
				} while (tries > 0);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return kernel;
	}

	protected void setServerStarted() {

		boolean started = false;

		GBeanQuery query = new GBeanQuery(null,
				PersistentConfigurationList.class.getName());

		for (int tries = MAX_TRIES; tries > 0 && !started; tries--) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
			}
			Set configLists = getKernel().listGBeans(query);
			if (!configLists.isEmpty()) {
				ObjectName on = (ObjectName) configLists.toArray()[0];
				try {
					Boolean b = (Boolean) getKernel().getAttribute(on,
							"kernelFullyStarted");
					if (b.booleanValue()) {
						Trace.trace(Trace.INFO, "kernelFullyStarted = true");
						setServerState(IServer.STATE_STARTED);
						started = true;
					} else {
						Trace.trace(Trace.INFO, "kernelFullyStarted = false");
					}
				} catch (GBeanNotFoundException e) {
				} catch (NoSuchAttributeException e) {
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Trace.trace(Trace.INFO, "configList is Empty");
			}
		}
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

		Trace.trace(Trace.INFO, "publishModule()" + module + " "
				+ module.length + " deltaKind=" + deltaKind);

		_monitor = monitor;

		if (deltaKind == NO_CHANGE) {
			Trace.trace(Trace.INFO,
					"deltaKind = NO_CHANGE, returning out of publishModule()");
			return;
		}

		if (module.length == 1) {
			invokeCommand(deltaKind, module[0]);
		}
	}

	private void invokeCommand(int deltaKind, IModule module)
			throws CoreException {

		Trace.trace(Trace.INFO, "calling invokeComand()" + module);

		try {
			switch (deltaKind) {
			case ADDED: {
				Trace.trace(Trace.INFO, "calling doDeploy()");
				doDeploy(module);
				break;
			}
			case CHANGED: {
				Trace.trace(Trace.INFO, "calling doRedeploy()");
				doRedeploy(module);
				break;
			}
			case REMOVED: {
				Trace.trace(Trace.INFO, "calling doUndeploy()");
				doUndeploy(module);
				break;
			}
			default:
				throw new IllegalArgumentException();
			}
		} catch (DeploymentManagerCreationException e) {
			e.printStackTrace();
			throw new CoreException(new Status(IStatus.ERROR,
					GeronimoPlugin.PLUGIN_ID, 0, e.getMessage(), e));
		}
	}

	private void doDeploy(IModule module) throws CoreException {
		IDeploymentCommand op = DeploymentCommandFactory
				.createDistributeCommand(module, getServer());
		IStatus status = op.execute(_monitor);

		if (!status.isOK()) {
			doFail(status, Messages.DISTRIBUTE_FAIL);
		}

		if (status instanceof DeploymentCmdStatus) {
			TargetModuleID[] ids = ((DeploymentCmdStatus) status).getResultTargetModuleIDs();
 
			op = DeploymentCommandFactory.createStartCommand(ids, module, getServer());

			status = op.execute(_monitor);

			if (!status.isOK()) {
				doFail(status, Messages.START_FAIL);
			}
		}
	}

	private void doRedeploy(IModule module) throws CoreException {
		IDeploymentCommand op = DeploymentCommandFactory.createRedeployCommand(
				module, getServer());

		IStatus status = op.execute(_monitor);

		if (!status.isOK()) {
			doFail(status, Messages.REDEPLOY_FAIL);
		}
	}

	private void doUndeploy(IModule module) throws CoreException,
			DeploymentManagerCreationException {
		IDeploymentCommand op = DeploymentCommandFactory.createStopCommand(
				module, getServer());

		IStatus status = op.execute(_monitor);

		if (!status.isOK()) {
			doFail(status, Messages.STOP_FAIL);
		}

		op = DeploymentCommandFactory
				.createUndeployCommand(module, getServer());

		status = op.execute(_monitor);

		if (!status.isOK()) {
			doFail(status, Messages.UNDEPLOY_FAIL);
		}
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

}