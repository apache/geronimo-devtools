/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.st.v20.core;

import java.net.URL;
import java.util.List;
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
import org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.v20.core.internal.Trace;
import org.apache.geronimo.st.v21.core.internal.DependencyHelper;
import org.apache.geronimo.system.jmx.KernelDelegate;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.IModulePublishHelper;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServerBehaviour extends GeronimoServerBehaviourDelegate implements IModulePublishHelper {

	private Kernel kernel = null;

	public GeronimoServerBehaviour() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate#stopKernel()
	 */
	protected void stopKernel() {
		if (kernel != null) {
			kernel.shutdown();
			kernel = null;
		}
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
	 * @see org.apache.geronimo.st.v21.core.IGeronimoServerBehavior#isKernelAlive()
	 */
	public boolean isKernelAlive() {
		try {
			return getKernel() != null && kernel.isRunning();
		} catch (SecurityException e) {
			Trace.trace(Trace.ERROR, "Invalid username and/or password.", e);

			pingThread.interrupt();
			if (getServer().getServerState() != IServer.STATE_STOPPED) {
				forceStopJob(true,e);

			}
		} catch (Exception e) {
			Activator.log(Status.WARNING, "Geronimo Server may have been terminated manually outside of workspace.", e);
			kernel = null;
		}
		return false;
	}
	
	private void forceStopJob(boolean b, final SecurityException e) {
		/* 
		 *
		 * Currently, there is another Status is returned by StartJob in Server. 
		 * The message doesn't contain reason for the exception. 
		 * So this job is created to show a message(Invalid username and/or password) to user.
		 *  
		 * TODO: Need a method to remove the error message thrown by StartJob in Server.
		 * 
		 */
		
		String jobName = NLS.bind(org.eclipse.wst.server.core.internal.Messages.errorStartFailed, getServer().getName());						
		
		//This message has different variable names in WTP 3.0 and 3.1, so we define it here instead of using that in WTP
		final String jobStartingName =  NLS.bind("Starting {0}", getServer().getName());

		new Job(jobName){

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				MultiStatus multiStatus = new  MultiStatus(Activator.PLUGIN_ID, 0, jobStartingName, null);
				multiStatus.add(new Status(IStatus.ERROR,Activator.PLUGIN_ID,0,"Invalid username and/or password.",e));
				try{
					GeronimoServerBehaviour.this.stop(true);
				}catch (Exception e){
					multiStatus.add(new Status(IStatus.ERROR,Activator.PLUGIN_ID,0,"Failed to stop server",e));
				}
			
				return multiStatus;
			}
		}.schedule();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.v21.core.IGeronimoServerBehavior#isFullyStarted()
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
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate#getRuntimeClass()
	 */
	public String getRuntimeClass() {
		return "org.apache.geronimo.cli.daemon.DaemonCLI";
	}

	public IPath getPublishDirectory(IModule[] module) {
		if (module == null || module.length == 0)
			return null;

		if (getGeronimoServer().isRunFromWorkspace()) {
			// TODO fix me, see if project root, component root, or output
			// container should be returned
			return module[module.length - 1].getProject().getLocation();
		} else {
			ClassLoader old = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(getContextClassLoader());
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
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Thread.currentThread().setContextClassLoader(old);
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

	@Override
    protected List getOrderedModules(IServer server, List modules,
            List deltaKind) {
		 DependencyHelper dh = new DependencyHelper();
         List list = dh.reorderModules(this.getServer(),modules, deltaKind);
         return list;
    }
}