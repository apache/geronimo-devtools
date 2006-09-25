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

import java.net.URL;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.naming.directory.NoSuchAttributeException;

import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.gbean.AbstractNameQuery;
import org.apache.geronimo.gbean.GBeanData;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.InternalKernelException;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.config.Configuration;
import org.apache.geronimo.kernel.config.InvalidConfigException;
import org.apache.geronimo.kernel.config.PersistentConfigurationList;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.core.Activator;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.core.operations.ISharedLibEntryCreationDataModelProperties;
import org.apache.geronimo.st.core.operations.SharedLibEntryCreationOperation;
import org.apache.geronimo.st.core.operations.SharedLibEntryDataModelProvider;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.apache.geronimo.system.jmx.KernelDelegate;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.IModulePublishHelper;

public class GeronimoServerBehaviour extends GeronimoServerBehaviourDelegate implements IModulePublishHelper {

	private Kernel kernel = null;

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

	/**
	 * @return
	 * @throws SecurityException
	 */
	protected Kernel getKernel() throws SecurityException {
		if (kernel == null) {
			try {
				MBeanServerConnection connection = getServerConnection();
				if (connection != null)
					kernel = new KernelDelegate(connection);
			} catch (SecurityException e) {
				throw e;
			} catch (Exception e) {
				Trace.trace(Trace.WARNING, "Kernel connection failed. "
						+ e.getMessage());
			}
		}
		return kernel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServerBehavior#isKernelAlive()
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServerBehavior#isFullyStarted()
	 */
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
	 * @see org.apache.geronimo.st.core.GenericGeronimoServerBehaviour#getConfigId(org.eclipse.wst.server.core.IModule)
	 */
	public String getConfigId(IModule module) {
		return GeronimoV11Utils.getConfigId(module);
	}

	public IPath getPublishDirectory(IModule[] module) {
		if (module == null || module.length == 0)
			return null;

		if (getGeronimoServer().isRunFromWorkspace()) {
			// TODO fix me, see if project root, component root, or output
			// container should be returned
			return module[module.length - 1].getProject().getLocation();
		} else {
			try {
				String configId = getConfigId(module[0]);
				Artifact artifact = Artifact.create(configId);
				AbstractName name = Configuration.getConfigurationAbstractName(artifact);
				GBeanData data = kernel.getGBeanData(name);
				URL url = (URL) data.getAttribute("baseURL");
				return getModulePath(module, url);
			} catch (InvalidConfigException e) {
				e.printStackTrace();
			} catch (GBeanNotFoundException e) {
				e.printStackTrace();
			} catch (InternalKernelException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.GenericGeronimoServerBehaviour#getContextClassLoader()
	 */
	protected ClassLoader getContextClassLoader() {
		return Kernel.class.getClassLoader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate#doDeploy(org.eclipse.wst.server.core.IModule)
	 */
	protected void doDeploy(IModule module) throws Exception {
		updateSharedLib(module);
		super.doDeploy(module);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate#doRedeploy(org.eclipse.wst.server.core.IModule)
	 */
	protected void doRedeploy(IModule module) throws Exception {
		super.unDeploy(module);
		updateSharedLib(module);
		super.doDeploy((module));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate#doUndeploy(org.eclipse.wst.server.core.IModule)
	 */
	protected void doUndeploy(IModule module) throws Exception {
		super.doUndeploy(module);
		updateSharedLib(module);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate#doNoChange(org.eclipse.wst.server.core.IModule)
	 */
	protected void doNoChange(IModule module) throws Exception {
		updateSharedLib(module);
		super.doNoChange(module);
	}

	private void updateSharedLib(IModule module) throws CoreException {
		if(isRemote() || !getGeronimoServer().isInPlaceSharedLib()) {
			return;
		}
		
		IDataModel model = DataModelFactory.createDataModel(new SharedLibEntryDataModelProvider());
		model.setProperty(ISharedLibEntryCreationDataModelProperties.MODULE, module);
		model.setProperty(ISharedLibEntryCreationDataModelProperties.SERVER, getServer());
		IDataModelOperation op = new SharedLibEntryCreationOperation(model);
		try {
			op.execute(new NullProgressMonitor(), null);
		} catch (ExecutionException e) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, e.getMessage(), e.getCause()));
		}
	}
}