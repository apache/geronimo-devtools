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
package org.apache.geronimo.st.v30.core;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.geronimo.st.v30.core.GeronimoUtils;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.osgi.AriesHelper;
import org.apache.geronimo.st.v30.core.osgi.OsgiConstants;
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
import org.eclipse.wst.web.internal.deployables.FlatComponentDeployable;


/**
 * <b>GeronimoServerDelegate</b> contains the properties for the Geronimo server that are persisted across
 * workbench sessions. Also, various methods are invoked by the web server core framework at deployment time to 
 * determine whether workspace modules can be deployed, and if so, properly collapses child modules into their 
 * parents. 
 * 
 * @see org.apache.geronimo.st.v30.core.GeronimoServer
 * 
 * @version $Rev$ $Date$
 */
abstract public class GeronimoServerDelegate extends ServerDelegate implements IGeronimoServer {

    public static final String PROPERTY_ADMIN_ID = "adminID";

    public static final String PROPERTY_ADMIN_PW = "adminPassword";

    public static final String PROPERTY_RMI_PORT = "RMIRegistry";

    public static final String PROPERTY_HTTP_PORT = "WebConnector";

    public static final String PROPERTY_CLEAN_OSGI_BUNDLE_CACHE = "cleanOSGiBundleCache";

    public static final String PROPERTY_LOG_LEVEL = "logLevel";
    
    public static final String PROPERTY_VM_ARGS = "VMArgs";
    
    public static final String PROPERTY_PING_DELAY = "pingDelay";
    
    public static final String PROPERTY_PING_INTERVAL = "pingInterval";
    
    public static final String PROPERTY_MAX_PINGS = "maxPings";
    
    public static final String PROPERTY_PUBLISH_TIMEOUT = "publishTimeout";
    
    public static final String PROPERTY_IN_PLACE_SHARED_LIB = "inPlaceSharedLib";
    
    public static final String PROPERTY_NOT_REDEPLOY_JSP_FILES = "notRedeployJSPFiles";
    
    public static final String PROPERTY_RUN_FROM_WORKSPACE = "runFromWorkspace";

    public static final String PROPERTY_SELECT_CLASSPATH_CONTAINERS = "selectClasspathContainers";

    public static final String PROPERTY_CLASSPATH_CONTAINERS = "classpathContainers";

    public static final String CONSOLE_INFO = "--long";

    public static final String CONSOLE_DEBUG = "-vv";
    
    public static final String CLEAN_OSGI_BUNDLE_CACHE = "--clean";

    /**
     * Determines whether the specified module modifications can be made to the server at this time
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#canModifyModules(org.eclipse.wst.server.core.IModule[],
     *      org.eclipse.wst.server.core.IModule[])
     */
    public IStatus canModifyModules(IModule[] add, IModule[] remove) {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.canModifyModules", add, remove);

        if (add != null) {
            for (int i = 0; i < add.length; i++) {
                IModule module = add[i];
                if (module.getProject() != null) {
                    IStatus status = FacetUtil.verifyFacets(module.getProject(), getServer());
                    if (status != null && !status.isOK()) {
                        Trace.tracePoint("Exit ", "GeronimoServerDelegate.canModifyModules", status);
                        return status;
                    }
                }
            }
        }

        Trace.tracePoint("Exit ", "GeronimoServerDelegate.canModifyModules", Status.OK_STATUS);
        return Status.OK_STATUS;
    }


    /**
     * Modify the list of modules already associated with the server
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#modifyModules(org.eclipse.wst.server.core.IModule[],
     *      org.eclipse.wst.server.core.IModule[],
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public void modifyModules(IModule[] add, IModule[] remove, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.modifyModules", add, remove, monitor);

        // TODO servermodule.info should be pushed to here and set as instance
        // property

        Trace.tracePoint("Exit ", "GeronimoServerDelegate.modifyModules");
    }


    /**
     * Return the parent module(s) of this module
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#getRootModules(org.eclipse.wst.server.core.IModule)
     */
    public IModule[] getRootModules(IModule module) throws CoreException {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.getRootModules", module);

        IStatus status = canModifyModules(new IModule[] { module }, null);
        if (status != null && !status.isOK())
            throw new CoreException(status);
        IModule[] modules = doGetParentModules(module);
        if (modules.length > 0) {
            Trace.tracePoint("Exit ", "GeronimoServerDelegate.getRootModules", modules);
            return modules;
        }

        Trace.tracePoint("Exit ", "GeronimoServerDelegate.getRootModules", new IModule[] { module });
        return new IModule[] { module };
    }


    /**
     * Return the child module(s) of this module
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#getChildModules(org.eclipse.wst.server.core.IModule[])
     */
    public IModule[] getChildModules(IModule[] module) {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.getChildModules", module);

        if (module == null) {
            Trace.tracePoint("Exit ", "GeronimoServerDelegate.getChildModules", null);
            return null;
        }

        if (module.length == 1 && module[0] != null) {
            IModuleType moduleType = module[0].getModuleType();
            if (moduleType != null) {
                if (IModuleConstants.JST_EAR_MODULE.equals(moduleType.getId())) {
                    IEnterpriseApplication enterpriseApplication = (IEnterpriseApplication) module[0].loadAdapter(IEnterpriseApplication.class, null);
                    if (enterpriseApplication != null) {
                        IModule[] modules = enterpriseApplication.getModules();
                        if (modules != null) {
                            Trace.tracePoint("Exit ", "GeronimoServerDelegate.getChildModules", modules);
                            return modules;
                        }
                    }
                }
                else if (IModuleConstants.JST_WEB_MODULE.equals(moduleType.getId())) {
                    IWebModule webModule = (IWebModule) module[0].loadAdapter(IWebModule.class, null);
                    if (webModule != null) {
                        IModule[] modules = webModule.getModules();
                        if (modules != null) {
                            Trace.tracePoint("Exit ", "GeronimoServerDelegate.getChildModules", modules);
                            return modules;
                        }
                    }
                }

                if (AriesHelper.isAriesInstalled()) {
                    if (OsgiConstants.APPLICATION.equals(moduleType.getId())) {
                        FlatComponentDeployable application = (FlatComponentDeployable) module[0].loadAdapter(FlatComponentDeployable.class,  null);
                        if (application != null) {
                            IModule[] modules = application.getModules();
                            if (modules != null) {
                                Trace.tracePoint("Exit ", "GeronimoServerDelegate.getChildModules", modules);
                                return modules;
                            }
                        }
                    }
                    else if (OsgiConstants.COMPOSITE_BUNDLE.equals(moduleType.getId())) {
                        FlatComponentDeployable composite = (FlatComponentDeployable) module[0].loadAdapter(FlatComponentDeployable.class,  null);
                        if (composite != null) {
                            IModule[] modules = composite.getModules();
                            if (modules != null) {
                                Trace.tracePoint("Exit ", "GeronimoServerDelegate.getChildModules", modules);
                                return modules;
                            }
                        }
                    }
                }
            }
        }

        Trace.tracePoint("Exit ", "GeronimoServerDelegate.getChildModules", new IModule[] {});
        return new IModule[] {};
    }


    /**
     * Return an array of ServerPorts associated with this server
     *
     * @see org.eclipse.wst.server.core.model.ServerDelegate#getServerPorts()
     */
    public ServerPort[] getServerPorts() {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.getServerPorts");

        List<ServerPort> ports = new ArrayList<ServerPort>();
        ports.add(new ServerPort(PROPERTY_HTTP_PORT, "Web Connector", Integer.parseInt(getHTTPPort()), "http"));
        ports.add(new ServerPort(PROPERTY_RMI_PORT, "RMI Naming", Integer.parseInt(getRMINamingPort()), "rmi"));

        Trace.tracePoint("Entry", "GeronimoServerDelegate.getServerPorts;", (ServerPort[]) ports.toArray(new ServerPort[ports.size()]));
        return (ServerPort[]) ports.toArray(new ServerPort[ports.size()]);
    }


    /**
     * Return the base URL of this module on the server
     * 
     * @see org.eclipse.wst.server.core.model.IURLProvider#getModuleRootURL(org.eclipse.wst.server.core.IModule)
     */
    public URL getModuleRootURL(IModule module) {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.getModuleRootURL", module);

        if (module == null) {
            return null;        
        }
        
        String contextRoot = null;
        
        if (GeronimoUtils.isBundleModule(module)) {
            // bundle - might be WAB
            Manifest manifest = GeronimoUtils.getBundleManifest(module);
            if (manifest != null) {
                contextRoot = manifest.getMainAttributes().getValue("Web-ContextPath");
            }
        } else if (module.loadAdapter(IWebModule.class, null) != null) {
            // regular WAR file
            List<IModule> parents = getApplicationModules(module);
            if (parents.isEmpty()) {
                // standalone module - get context-root from geronimo-web.xml
                contextRoot = GeronimoUtils.getContextRoot(module, true);
            } else if (parents.size() == 1) {
                // one parent - get context-root from EAR descriptor
                contextRoot = GeronimoUtils.getContextRoot(module, false);
            }
        }
        
        if (contextRoot != null) {
            try {
                String host = getServer().getHost();
                String url = "http://" + host;
                int port = Integer.parseInt(getHTTPPort());
                port = ServerMonitorManager.getInstance().getMonitoredPort(getServer(), port, "web");
                if (port != 80) {
                    url += ":" + port;
                }

                String moduleId = contextRoot;
                if (!moduleId.startsWith("/")) {
                    url += "/";
                }
                url += moduleId;

                if (!url.endsWith("/")) {
                    url += "/";
                }

                Trace.tracePoint("Exit ", "GeronimoServerDelegate.getModuleRootURL", new URL(url));
                return new URL(url);
            } catch (Exception e) {
                Trace.trace(Trace.SEVERE, "Could not get root URL", e);
                return null;
            }
        }
        
        return null;        
    }


    /**
     * Initialize this server with default values when a new server is created
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#setDefaults(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void setDefaults(IProgressMonitor monitor) {
        setAdminID("system");
        setAdminPassword("manager");
        setHTTPPort("8080");
        setRMINamingPort("1099");
        setConsoleLogLevel(CONSOLE_INFO);
        setCleanOSGiBundleCache("");
        setPingDelay(new Integer(10000));
        setMaxPings(new Integer(40));
        setPingInterval(new Integer(5000));
        setPublishTimeout(900000);
        setInPlaceSharedLib(false);
        setRunFromWorkspace(false);
        setSelectClasspathContainers(false);
    }


    // 
    // PROPERTY_ADMIN_ID 
    // 
    public String getAdminID() {
        return getInstanceProperty(PROPERTY_ADMIN_ID);
    }
    public void setAdminID(String value) {
        setInstanceProperty(PROPERTY_ADMIN_ID, value);
    }


    // 
    // PROPERTY_ADMIN_PW 
    // 
    public String getAdminPassword() {
        return getInstanceProperty(PROPERTY_ADMIN_PW);
    }
    public void setAdminPassword(String value) {
        setInstanceProperty(PROPERTY_ADMIN_PW, value);
    }


    // 
    // PROPERTY_RMI_PORT 
    // 
    public String getRMINamingPort() {
        return getInstanceProperty(PROPERTY_RMI_PORT);
    }
    public void setRMINamingPort(String value) {
        setInstanceProperty(PROPERTY_RMI_PORT, value);
    }


    // 
    // PROPERTY_HTTP_PORT 
    // 
    public String getHTTPPort() {
        return getInstanceProperty(PROPERTY_HTTP_PORT);
    }
    public void setHTTPPort(String value) {
        setInstanceProperty(PROPERTY_HTTP_PORT, value);
    }


    // 
    // PROPERTY_LOG_LEVEL
    // 
    public String getConsoleLogLevel() {
        return getInstanceProperty(PROPERTY_LOG_LEVEL);
    }
    public void setConsoleLogLevel(String value) {
        setInstanceProperty(PROPERTY_LOG_LEVEL, value);
    }
    
    
    public String getCleanOSGiBundleCache() {
        return getInstanceProperty(PROPERTY_CLEAN_OSGI_BUNDLE_CACHE);
    }

    public void setCleanOSGiBundleCache(String value) {
        setInstanceProperty(PROPERTY_CLEAN_OSGI_BUNDLE_CACHE, value);
    }

    public Set<String> getProgramArgs() {
        Set<String> parms = new HashSet<String>(2);
        parms.add(getConsoleLogLevel());
        String clean = getCleanOSGiBundleCache();
        if (clean.length() > 0) {
            parms.add(clean);
        }
        return parms;
    }

    public Set<String> getProgramArgsNotSet() {
        Set<String> notParms = new HashSet<String>(2);
        String logLevel = getConsoleLogLevel();
        if (logLevel.equals(CONSOLE_INFO)) {
            notParms.add(CONSOLE_DEBUG);
        } else {
            notParms.add(CONSOLE_INFO);
        }
        String clean = getCleanOSGiBundleCache();
        if (clean.equals("")) {
            notParms.add(CLEAN_OSGI_BUNDLE_CACHE);
        }
        return notParms;
    }

    // 
    // PROPERTY_VM_ARGS
    // 
    public String getVMArgs() {
        return getInstanceProperty(PROPERTY_VM_ARGS);
    }
    public void setVMArgs(String value) {
        setInstanceProperty(PROPERTY_VM_ARGS, value);
    }
    

    // 
    // PROPERTY_PING_DELAY
    // 
    public int getPingDelay() {
        String pingDelay = getInstanceProperty(PROPERTY_PING_DELAY);
        return Integer.parseInt(pingDelay);
    }
    public void setPingDelay(Integer delay) {
        setInstanceProperty(PROPERTY_PING_DELAY, delay.toString());
    }
    
    
    // 
    // PROPERTY_PING_INTERVAL
    // 
    public int getPingInterval() {
        String pingInterval = getInstanceProperty(PROPERTY_PING_INTERVAL);
        return Integer.parseInt(pingInterval);
    }
    public void setPingInterval(Integer interval) {
        setInstanceProperty(PROPERTY_PING_INTERVAL, interval.toString());
    }
    
    
    // 
    // PROPERTY_MAX_PINGS
    // 
    public int getMaxPings() {
        String maxPings = getInstanceProperty(PROPERTY_MAX_PINGS);
        return Integer.parseInt(maxPings);
    }
    public void setMaxPings(Integer maxPings) {
        setInstanceProperty(PROPERTY_MAX_PINGS, maxPings.toString());
    }
    
    
    // 
    // PROPERTY_PUBLISH_TIMEOUT
    // 
    public long getPublishTimeout() {
        String timeout = getInstanceProperty(PROPERTY_PUBLISH_TIMEOUT);
        return Long.parseLong(timeout);
    }
    public void setPublishTimeout(long timeout) {
        setInstanceProperty(PROPERTY_PUBLISH_TIMEOUT, Long.toString(timeout));
    }
    
    
    // 
    // PROPERTY_IN_PLACE_SHARED_LIB
    // 
    public boolean isInPlaceSharedLib() {
        return getAttribute(PROPERTY_IN_PLACE_SHARED_LIB, false);
    }
    public void setInPlaceSharedLib(boolean enable) {
        setAttribute(PROPERTY_IN_PLACE_SHARED_LIB, enable);
    }

    
    // 
    // PROPERTY_RUN_FROM_WORKSPACE
    // 
    public boolean isRunFromWorkspace() {
        return getAttribute(PROPERTY_RUN_FROM_WORKSPACE, false);
    }
    public void setRunFromWorkspace(boolean enable) {
        setAttribute(PROPERTY_RUN_FROM_WORKSPACE, enable);
    }
    
    
    //
    // PROPERTY_SELECT_CLASSPATH_CONTAINERS
    //
    public boolean isSelectClasspathContainers() {
        return getAttribute(PROPERTY_SELECT_CLASSPATH_CONTAINERS, false);
    }
    public void setSelectClasspathContainers(boolean enable) {
        setAttribute(PROPERTY_SELECT_CLASSPATH_CONTAINERS, enable);
    }
    
    
    //
    // PROPERTY_CLASSPATH_CONTAINERS
    //
    public List<String> getClasspathContainers() {
        return getAttribute(PROPERTY_CLASSPATH_CONTAINERS, new ArrayList<String>() );
    }
    public void setClasspathContainers( List<String> list ) {
        setAttribute(PROPERTY_CLASSPATH_CONTAINERS, list);
    }
    
    
    //
    // PROPERTY_NOT_REDEPLOY_JSP_FILES
    //
    public boolean isNotRedeployJSPFiles() {
        return getAttribute(PROPERTY_NOT_REDEPLOY_JSP_FILES, false);
    }
    public void setNotRedeployJSPFiles(boolean enable){
        setAttribute(PROPERTY_NOT_REDEPLOY_JSP_FILES, enable);
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


    /**
     * Return all parent module(s) of this module
     * 
     * @param module
     * 
     * @return 
     */
    private IModule[] doGetParentModules(IModule module) {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.doGetParentModules", module);

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

        Trace.tracePoint("Exit ", "GeronimoServerDelegate.doGetParentModules", (IModule[]) parents.toArray(new IModule[parents.size()]));
        return (IModule[]) parents.toArray(new IModule[parents.size()]);
    }


    /**
     * Return all applications this module is contained in
     * 
     * @param module
     * 
     * @return 
     */
    private List<IModule> getApplicationModules(IModule module) {
        Trace.tracePoint("Entry", "GeronimoServerDelegate.getApplicationModules", module);

        ArrayList<IModule> list = new ArrayList<IModule>();

        IModule[] ears = ServerUtil.getModules(IModuleConstants.JST_EAR_MODULE);
        for (int i = 0; i < ears.length; i++) {
            IEnterpriseApplication ear = (IEnterpriseApplication) ears[i].loadAdapter(IEnterpriseApplication.class, null);
            IModule[] modules = ear.getModules();
            for (int j = 0; j < modules.length; j++) {
                if (modules[j].equals(module))
                    list.add(ears[i]);
            }
        }

        if (AriesHelper.isAriesInstalled()) {
            IModule[] applications = ServerUtil.getModules(OsgiConstants.APPLICATION);
            for (int i = 0; i < applications.length; i++) {
                FlatComponentDeployable application = (FlatComponentDeployable) applications[i].loadAdapter(FlatComponentDeployable.class,  null);
                IModule[] modules = application.getModules();
                for (int j = 0; j < modules.length; j++) {
                    if (modules[j].equals(module))
                        list.add(applications[i]);
                }
            }
  
            IModule[] composites = ServerUtil.getModules(OsgiConstants.COMPOSITE_BUNDLE);
            for (int i = 0; i < composites.length; i++) {
                FlatComponentDeployable composite = (FlatComponentDeployable) composites[i].loadAdapter(FlatComponentDeployable.class,  null);
                IModule[] modules = composite.getModules();
                for (int j = 0; j < modules.length; j++) {
                    if (modules[j].equals(module))
                        list.add(composites[i]);
                }
            }
        }

        Trace.tracePoint("Exit ", "GeronimoServerDelegate.getApplicationModules", list);
        return list;
    }
}
