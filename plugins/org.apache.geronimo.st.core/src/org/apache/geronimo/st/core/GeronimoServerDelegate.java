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
package org.apache.geronimo.st.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.server.core.FacetUtil;
import org.eclipse.jst.server.core.IEnterpriseApplication;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.jst.server.core.internal.J2EEUtil;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IModuleType;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.internal.ServerMonitorManager;
import org.eclipse.wst.server.core.model.ServerDelegate;

/**
 * @version $Rev$ $Date$
 */
abstract public class GeronimoServerDelegate extends ServerDelegate implements IGeronimoServer {

	public static final String PROPERTY_ADMIN_ID = "adminID";

	public static final String PROPERTY_ADMIN_PW = "adminPassword";

	public static final String PROPERTY_RMI_PORT = "RMIRegistry";

	public static final String PROPERTY_HTTP_PORT = "WebConnector";

	public static final String PROPERTY_LOG_LEVEL = "logLevel";
	
	public static final String PROPERTY_VM_ARGS = "VMArgs";
	
	public static final String PROPERTY_PING_DELAY = "pingDelay";
	
	public static final String PROPERTY_PING_INTERVAL = "pingInterval";
	
	public static final String PROPERTY_MAX_PINGS = "maxPings";
	
	public static final String PROPERTY_PUBLISH_TIMEOUT = "publishTimeout";
	
	public static final String PROPERTY_IN_PLACE_SHARED_LIB = "inPlaceSharedLib";
	
	public static final String PROPERTY_RUN_FROM_WORKSPACE = "runFromWorkspace";

	public static final String CONSOLE_INFO = "--long";

	public static final String CONSOLE_DEBUG = "-vv";

	public abstract String getContextRoot(IModule module);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerDelegate#canModifyModules(org.eclipse.wst.server.core.IModule[],
	 *      org.eclipse.wst.server.core.IModule[])
	 */
	public IStatus canModifyModules(IModule[] add, IModule[] remove) {
		for (int i = 0; i < add.length; i++) {
			IModule module = add[i];
			if (module.getProject() != null) {
				IStatus status = FacetUtil.verifyFacets(module.getProject(), getServer());
				if (status != null && !status.isOK())
					return status;
			}
		}
		return Status.OK_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerDelegate#modifyModules(org.eclipse.wst.server.core.IModule[],
	 *      org.eclipse.wst.server.core.IModule[],
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void modifyModules(IModule[] add, IModule[] remove, IProgressMonitor monitor) throws CoreException {
		// TODO servermodule.info should be pushed to here and set as instance
		// property
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerDelegate#getRootModules(org.eclipse.wst.server.core.IModule)
	 */
	public IModule[] getRootModules(IModule module) throws CoreException {
		IStatus status = canModifyModules(new IModule[] { module }, null);
		if (status != null && !status.isOK())
			throw new CoreException(status);
		IModule[] childs = doGetParentModules(module);
		if (childs.length > 0)
			return childs;
		return new IModule[] { module };
	}

	private IModule[] doGetParentModules(IModule module) {
		ArrayList<IModule> parents = new ArrayList<IModule>();
		parents.addAll(getApplicationModules(module));

		// also check to see if the module is a utility module for a stand-alone
		// web module
		IModule[] wars = J2EEUtil.getWebModules(module, null);
		for (int i = 0; i < wars.length; i++) {
			if (getApplicationModules(wars[i]).isEmpty()) {
				parents.add(wars[i]);
			}
		}
		return (IModule[]) parents.toArray(new IModule[parents.size()]);
	}

	private List<IModule> getApplicationModules(IModule module) {
		IModule[] ears = ServerUtil.getModules(IModuleConstants.JST_EAR_MODULE);
		ArrayList<IModule> list = new ArrayList<IModule>();
		for (int i = 0; i < ears.length; i++) {
			IEnterpriseApplication ear = (IEnterpriseApplication) ears[i].loadAdapter(IEnterpriseApplication.class, null);
			IModule[] childs = ear.getModules();
			for (int j = 0; j < childs.length; j++) {
				if (childs[j].equals(module))
					list.add(ears[i]);
			}
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerDelegate#getChildModules(org.eclipse.wst.server.core.IModule[])
	 */
	public IModule[] getChildModules(IModule[] module) {
		if (module == null)
			return null;

		if (module.length == 1 && module[0] != null) {
			IModuleType moduleType = module[0].getModuleType();
			if (moduleType != null) {
				if (IModuleConstants.JST_EAR_MODULE.equals(moduleType.getId())) {
					IEnterpriseApplication enterpriseApplication = (IEnterpriseApplication) module[0].loadAdapter(IEnterpriseApplication.class, null);
					if (enterpriseApplication != null) {
						IModule[] modules = enterpriseApplication.getModules();
						if (modules != null)
							return modules;
					}
				} else if (IModuleConstants.JST_WEB_MODULE.equals(moduleType.getId())) {
					IWebModule webModule = (IWebModule) module[0].loadAdapter(IWebModule.class, null);
					if (webModule != null) {
						IModule[] modules = webModule.getModules();
						if (modules != null)
							return modules;
					}
				}
			}
		}

		return new IModule[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.ServerDelegate#getServerPorts()
	 */
	public ServerPort[] getServerPorts() {
		List<ServerPort> ports = new ArrayList<ServerPort>();
		ports.add(new ServerPort(PROPERTY_HTTP_PORT, "Web Connector", Integer.parseInt(getHTTPPort()), "http"));
		ports.add(new ServerPort(PROPERTY_RMI_PORT, "RMI Naming", Integer.parseInt(getRMINamingPort()), "rmi"));
		return (ServerPort[]) ports.toArray(new ServerPort[ports.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.IURLProvider#getModuleRootURL(org.eclipse.wst.server.core.IModule)
	 */
	public URL getModuleRootURL(IModule module) {
		try {
			if (module == null
					|| module.loadAdapter(IWebModule.class, null) == null)
				return null;

			String host = getServer().getHost();
			String url = "http://" + host;
			int port = 0;

			port = Integer.parseInt(getHTTPPort());
			port = ServerMonitorManager.getInstance().getMonitoredPort(getServer(), port, "web");
			if (port != 80)
				url += ":" + port;

			String moduleId = getContextRoot(module);
			if (!moduleId.startsWith("/"))
				url += "/";
			url += moduleId;

			if (!url.endsWith("/"))
				url += "/";

			return new URL(url);
		} catch (Exception e) {
			Trace.trace(Trace.SEVERE, "Could not get root URL", e);
			return null;
		}

	}

	public String getAdminID() {
		return getInstanceProperty(PROPERTY_ADMIN_ID);
	}

	public String getAdminPassword() {
		return getInstanceProperty(PROPERTY_ADMIN_PW);
	}

	public String getRMINamingPort() {
		return getInstanceProperty(PROPERTY_RMI_PORT);
	}

	public String getHTTPPort() {
		return getInstanceProperty(PROPERTY_HTTP_PORT);
	}

	public String getConsoleLogLevel() {
		return getInstanceProperty(PROPERTY_LOG_LEVEL);
	}
	
	public String getVMArgs() {
		return getInstanceProperty(PROPERTY_VM_ARGS);
	}

	public void setAdminID(String value) {
		setInstanceProperty(PROPERTY_ADMIN_ID, value);
	}

	public void setAdminPassword(String value) {
		setInstanceProperty(PROPERTY_ADMIN_PW, value);
	}

	public void setRMINamingPort(String value) {
		setInstanceProperty(PROPERTY_RMI_PORT, value);
	}

	public void setHTTPPort(String value) {
		setInstanceProperty(PROPERTY_HTTP_PORT, value);
	}

	public void setConsoleLogLevel(String value) {
		setInstanceProperty(PROPERTY_LOG_LEVEL, value);
	}
	
	public void setVMArgs(String value) {
		setInstanceProperty(PROPERTY_VM_ARGS, value);
	}
	
	public int getPingDelay() {
		String pingDelay = getInstanceProperty(PROPERTY_PING_DELAY);
		return Integer.parseInt(pingDelay);
	}
	
	public int getPingInterval() {
		String pingInterval = getInstanceProperty(PROPERTY_PING_INTERVAL);
		return Integer.parseInt(pingInterval);
	}
	
	public int getMaxPings() {
		String maxPings = getInstanceProperty(PROPERTY_MAX_PINGS);
		return Integer.parseInt(maxPings);
	}
	
	public long getPublishTimeout() {
		String timeout = getInstanceProperty(PROPERTY_PUBLISH_TIMEOUT);
		return Long.parseLong(timeout);
	}
	
	public void setPingDelay(Integer delay) {
		setInstanceProperty(PROPERTY_PING_DELAY, delay.toString());
	}
	
	public void setPingInterval(Integer interval) {
		setInstanceProperty(PROPERTY_PING_INTERVAL, interval.toString());
	}
	
	public void setMaxPings(Integer maxPings) {
		setInstanceProperty(PROPERTY_MAX_PINGS, maxPings.toString());
	}
	
	public void setPublishTimeout(long timeout) {
		setInstanceProperty(PROPERTY_PUBLISH_TIMEOUT, Long.toString(timeout));
	}
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.IGeronimoServer#isInPlace()
	 */
	public boolean isInPlaceSharedLib() {
		return getAttribute(PROPERTY_IN_PLACE_SHARED_LIB, false);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.IGeronimoServer#isRunFromWorkspace()
	 */
	public boolean isRunFromWorkspace() {
		return getAttribute(PROPERTY_RUN_FROM_WORKSPACE, false);
	}
	
	public void setInPlaceSharedLib(boolean enable) {
		setAttribute(PROPERTY_IN_PLACE_SHARED_LIB, enable);
	}
	
	public void setRunFromWorkspace(boolean enable) {
		setAttribute(PROPERTY_RUN_FROM_WORKSPACE, enable);
	}
	
	public String discoverDeploymentFactoryClassName(IPath jarPath) {
		try {
			JarFile deployerJar = new JarFile(jarPath.toFile());
			Manifest manifestFile = deployerJar.getManifest();
			Attributes attributes = manifestFile.getMainAttributes();
			return attributes.getValue("J2EE-DeploymentFactory-Implementation-Class");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.server.core.model.ServerDelegate#setDefaults(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void setDefaults(IProgressMonitor monitor) {
		setAdminID("system");
		setAdminPassword("manager");
		setHTTPPort("8080");
		setRMINamingPort("1099");
		setConsoleLogLevel(CONSOLE_INFO);
		setPingDelay(new Integer(10000));
		setMaxPings(new Integer(40));
		setPingInterval(new Integer(5000));
		setPublishTimeout(900000);
		setInPlaceSharedLib(false);
		setRunFromWorkspace(false);
	}

	public String getInstanceProperty(String name) {
		return (String) getServerInstanceProperties().get(name);
	}

	public void setInstanceProperty(String name, String value) {
		Map map = getServerInstanceProperties();
		map.put(name, value);
		setServerInstanceProperties(map);
	}

	public Map getServerInstanceProperties() {
		return getAttribute(GeronimoRuntimeDelegate.SERVER_INSTANCE_PROPERTIES, new HashMap());
	}

	public void setServerInstanceProperties(Map map) {
		setAttribute(GeronimoRuntimeDelegate.SERVER_INSTANCE_PROPERTIES, map);
	}
}
