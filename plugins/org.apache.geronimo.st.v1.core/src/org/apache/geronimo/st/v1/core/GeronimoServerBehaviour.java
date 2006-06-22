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
package org.apache.geronimo.st.v1.core;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.deploy.spi.Target;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.directory.NoSuchAttributeException;

import org.apache.geronimo.gbean.GBeanData;
import org.apache.geronimo.gbean.GBeanQuery;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.InternalKernelException;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.config.Configuration;
import org.apache.geronimo.kernel.config.PersistentConfigurationList;
import org.apache.geronimo.kernel.jmx.KernelDelegate;
import org.apache.geronimo.st.core.GenericGeronimoServerBehaviour;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.v1.core.internal.Trace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.IModulePublishHelper;

public class GeronimoServerBehaviour extends GenericGeronimoServerBehaviour implements IModulePublishHelper {

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

	private Kernel getKernel() throws SecurityException {

		if (kernel == null) {
			Map map = new HashMap();
			String user = getGeronimoServer().getAdminID();
			String password = getGeronimoServer().getAdminPassword();
			map.put("jmx.remote.credentials", new String[] { user, password });
			try {
				String url = getGeronimoServer().getJMXServiceURL();
				if (url == null)
					return null;
				JMXServiceURL address = new JMXServiceURL(url);
				try {
					JMXConnector jmxConnector = JMXConnectorFactory.connect(address, map);
					MBeanServerConnection mbServerConnection = jmxConnector.getMBeanServerConnection();
					kernel = new KernelDelegate(mbServerConnection);
					Trace.trace(Trace.INFO, "Connected to kernel: " + url);
				} catch (SecurityException e) {
					throw e;
				} catch (Exception e) {
					Trace.trace(Trace.WARNING, "Kernel connection failed." + url);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return kernel;
	}

	/* (non-Javadoc)
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

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.IGeronimoServerBehavior#isFullyStarted()
	 */
	public boolean isFullyStarted() {
		if (isKernelAlive()) {
			Set configLists = kernel.listGBeans(new GBeanQuery(null, PersistentConfigurationList.class.getName()));
			if (!configLists.isEmpty()) {
				ObjectName on = (ObjectName) configLists.toArray()[0];
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
	 * @see org.apache.geronimo.st.core.GenericGeronimoServerBehaviour#getConfigId(org.eclipse.wst.server.core.IModule)
	 */
	public String getConfigId(IModule module) {
		return GeronimoV1Utils.getConfigId(module);
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.IGeronimoServerBehavior#getTargets()
	 */
	public Target[] getTargets() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.wst.server.core.internal.IModulePublishHelper#getPublishDirectory(org.eclipse.wst.server.core.IModule[])
	 */
	public IPath getPublishDirectory(IModule[] module) {
		if(module == null || module.length == 0)
			return null;
		
		try {
			IModule workingModule = module[0];
			String configId = getConfigId(workingModule);
			ObjectName on = Configuration.getConfigurationObjectName(URI.create(configId));
			GBeanData data = kernel.getGBeanData(on);
			URL url = (URL) data.getAttribute("baseURL");
			IPath modulePath = new Path(url.getFile());
			if(module.length == 2) {
				workingModule = module[1];
				modulePath = modulePath.append(workingModule.getName());
			} 
			
			if(GeronimoUtils.isWebModule(workingModule)) {
				modulePath = modulePath.addFileExtension(".war");
			} else if(GeronimoUtils.isEjbJarModule(workingModule)) {
				modulePath = modulePath.addFileExtension(".jar");
			} else if(GeronimoUtils.isRARModule(workingModule)) {
				modulePath = modulePath.addFileExtension(".rar");
			} else if(GeronimoUtils.isEarModule(workingModule)) {
				modulePath = modulePath.addFileExtension(".ear");
			}
			
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (GBeanNotFoundException e) {
			e.printStackTrace();
		} catch (InternalKernelException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.GenericGeronimoServerBehaviour#getContextClassLoader()
	 */
	protected ClassLoader getContextClassLoader() {
		return Kernel.class.getClassLoader();
	}
}