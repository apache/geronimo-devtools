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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.geronimo.crypto.EncryptionManager;
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

    public static final List<String> DEFAULT_NOREDEPLOY_INCLUDE_PATTERNS = 
        Arrays.asList("**/*.html", "**/*.xhtml", "**/*.css", "**/*.js", "**/*.jsp", "**/*.jspx", "**/*.gif", "**/*.jpg", "**/*.png", "**/*.swt", "**/*.properties", "**/*.xml");
    
    public static final List<String> DEFAULT_NOREDEPLOY_EXCLUDE_PATTERNS = 
        Arrays.asList("WEB-INF/geronimo-*.xml", "WEB-INF/web.xml");
    
    public static final String PROPERTY_ADMIN_ID = "adminID";

    public static final String PROPERTY_ADMIN_PW = "adminPassword";

    public static final String PROPERTY_RMI_PORT = "RMIRegistry";

    public static final String PROPERTY_HTTP_PORT = "WebConnector";

    public static final String PROPERTY_CLEAN_OSGI_BUNDLE_CACHE = "cleanOSGiBundleCache";
    
    public static final String PROPERTY_REFRESH_OSGI_BUNDLE = "refreshOSGiBundle";

    public static final String PROPERTY_LOG_LEVEL = "logLevel";
    
    public static final String PROPERTY_KARAF_SHELL = "karafShell";
    
    public static final String PROPERTY_PROGRAM_ARGS = "ProgramArgs";
    
    public static final String PROPERTY_VM_ARGS = "VMArgs";
    
    public static final String PROPERTY_PING_DELAY = "pingDelay";
    
    public static final String PROPERTY_PING_INTERVAL = "pingInterval";
    
    public static final String PROPERTY_MAX_PINGS = "maxPings";
    
    public static final String PROPERTY_PUBLISH_TIMEOUT = "publishTimeout";
    
    public static final String PROPERTY_IN_PLACE_SHARED_LIB = "inPlaceSharedLib";
    
    public static final String PROPERTY_NOREDEPLOY = "noRedeploy";
    
    public static final String PROPERTY_NOREDEPLOY_INCLUDE_PATTERNS = "noRedeploy.includePatterns";
    
    public static final String PROPERTY_NOREDEPLOY_EXCLUDE_PATTERNS = "noRedeploy.excludePatterns";
    
    public static final String PROPERTY_RUN_FROM_WORKSPACE = "runFromWorkspace";

    public static final String PROPERTY_SELECT_CLASSPATH_CONTAINERS = "selectClasspathContainers";

    public static final String PROPERTY_CLASSPATH_CONTAINERS = "classpathContainers";

    public static final String CONSOLE_INFO = "--long";

    public static final String CONSOLE_DEBUG = "-vv";
    
    public static final String CLEAN_OSGI_BUNDLE_CACHE = "--clean";
    
    public static final String DISABLE_KARAF_SHELL = "-Dkaraf.startLocalConsole=false";
    
    public static final String ENABLE_KARAF_SHELL = "-Dkaraf.startLocalConsole=true";
    
    // required to avoid error start, and work nicely on windows, when Karaf shell is enabled.
    public static final String JLINE_UNSUPPORTED_TERMINAL = "-Djline.terminal=jline.UnsupportedTerminal";

    private boolean suspendArgUpdates;

    /**
     * Determines whether the specified module modifications can be made to the server at this time
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#canModifyModules(org.eclipse.wst.server.core.IModule[],
     *      org.eclipse.wst.server.core.IModule[])
     */
    public IStatus canModifyModules(IModule[] add, IModule[] remove) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.canModifyModules", add, remove);

        if (add != null) {
            for (int i = 0; i < add.length; i++) {
                IModule module = add[i];
                if (module.getProject() != null) {
                    IStatus status = FacetUtil.verifyFacets(module.getProject(), getServer());
                    if (status != null && !status.isOK()) {
                        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.canModifyModules", status);
                        return status;
                    }
                }
            }
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.canModifyModules", Status.OK_STATUS);
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
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.modifyModules", add, remove, monitor);

        // TODO servermodule.info should be pushed to here and set as instance
        // property

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.modifyModules");
    }


    /**
     * Return the parent module(s) of this module
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#getRootModules(org.eclipse.wst.server.core.IModule)
     */
    public IModule[] getRootModules(IModule module) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getRootModules", module);

        IModule[] rootModule = new IModule[] { module };
        IStatus status = canModifyModules(rootModule, null);
        if (status != null && !status.isOK()) {
            throw new CoreException(status);
        }
        IModule[] modules = doGetParentModules(module);
        if (modules.length == 0) {
            modules = rootModule;
        }
            
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getRootModules", modules);
        return modules;
    }


    /**
     * Return the child module(s) of this module
     * 
     * @see org.eclipse.wst.server.core.model.ServerDelegate#getChildModules(org.eclipse.wst.server.core.IModule[])
     */
    public IModule[] getChildModules(IModule[] module) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getChildModules", module);

        if (module == null) {
            Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getChildModules", null);
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
                            Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getChildModules", modules);
                            return modules;
                        }
                    }
                }
                else if (IModuleConstants.JST_WEB_MODULE.equals(moduleType.getId())) {
                    IWebModule webModule = (IWebModule) module[0].loadAdapter(IWebModule.class, null);
                    if (webModule != null) {
                        IModule[] modules = webModule.getModules();
                        if (modules != null) {
                            Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getChildModules", modules);
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
                                Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getChildModules", modules);
                                return modules;
                            }
                        }
                    }
                    else if (OsgiConstants.COMPOSITE_BUNDLE.equals(moduleType.getId())) {
                        FlatComponentDeployable composite = (FlatComponentDeployable) module[0].loadAdapter(FlatComponentDeployable.class,  null);
                        if (composite != null) {
                            IModule[] modules = composite.getModules();
                            if (modules != null) {
                                Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getChildModules", modules);
                                return modules;
                            }
                        }
                    }
                }
            }
        }
       
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getChildModules", new IModule[] {});
        return new IModule[] {};
    }


    /**
     * Return an array of ServerPorts associated with this server
     *
     * @see org.eclipse.wst.server.core.model.ServerDelegate#getServerPorts()
     */
    public ServerPort[] getServerPorts() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getServerPorts");

        List<ServerPort> ports = new ArrayList<ServerPort>();
        ports.add(new ServerPort(PROPERTY_HTTP_PORT, "Web Connector", Integer.parseInt(getHTTPPort()), "http"));
        ports.add(new ServerPort(PROPERTY_RMI_PORT, "RMI Naming", Integer.parseInt(getRMINamingPort()), "rmi"));

        ServerPort[] serverPorts = ports.toArray(new ServerPort[ports.size()]);
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getServerPorts;", serverPorts);
        return serverPorts;
    }

    public String getContextPath(IModule module) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getContextPath", module);

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
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerDelegate.getContextPath", contextRoot);        
        return contextRoot;
    }
    
    /**
     * Return the base URL of this module on the server
     * 
     * @see org.eclipse.wst.server.core.model.IURLProvider#getModuleRootURL(org.eclipse.wst.server.core.IModule)
     */
    public URL getModuleRootURL(IModule module) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getModuleRootURL", module);

        if (module == null) {
            return null;        
        }
        
        String contextRoot = getContextPath(module);
                
        if (contextRoot != null) {
            try {
                String host = getServer().getHost();
                StringBuilder urlSB = new StringBuilder("http://");
                urlSB.append(host);
                int port = Integer.parseInt(getHTTPPort());
                port = ServerMonitorManager.getInstance().getMonitoredPort(getServer(), port, "web");
                if (port != 80) {
                    urlSB.append(":").append(port);
                }

                String moduleId = contextRoot;
                if (!moduleId.startsWith("/")) {
                    urlSB.append("/");
                }
                urlSB.append(moduleId);

                if (!moduleId.endsWith("/")) {
                    urlSB.append("/");
                }
                String url = urlSB.toString();
                URL moduleURL = new URL(url);
                Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getModuleRootURL", moduleURL);
                return moduleURL;
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, "Could not get root URL", e, Activator.logCore);
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
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.setDefaults", monitor);
        suspendArgUpdates();
        setAdminID("system");
        setAdminPassword("manager");
        setHTTPPort("8080");
        setRMINamingPort("1099");
        setConsoleLogLevel(CONSOLE_INFO);
        setCleanOSGiBundleCache(false);
        setRefreshOSGiBundle(false);
        setKarafShell(false);
        setPingDelay(new Integer(10000));
        setMaxPings(new Integer(40));
        setPingInterval(new Integer(5000));
        setPublishTimeout(900000);
        setInPlaceSharedLib(false);
        setRunFromWorkspace(false);
        setSelectClasspathContainers(false);
        resumeArgUpdates();
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerDelegate.setDefaults", monitor);
    }

    

    @Override
    public void saveConfiguration(IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Enter", Activator.traceCore, "GeronimoServerDelegate.v30.saveConfiguration", monitor);
        super.saveConfiguration(monitor);
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerDelegate.v30.saveConfiguration", monitor);
    }
    
    

    @Override
    public void configurationChanged() {
        Trace.tracePoint("Enter", Activator.traceCore, "GeronimoServerDelegate.v30.configurationChanged");
        super.configurationChanged();
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerDelegate.v30.configurationChanged");
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
        String password = getInstanceProperty(PROPERTY_ADMIN_PW);
        return password == null ? null : (String) EncryptionManager.decrypt(password);
    }
    
    public void setAdminPassword(String value) {
        String password = value == null ? null : EncryptionManager.encrypt(value);
        setInstanceProperty(PROPERTY_ADMIN_PW, password);
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
        updateProgramArgsFromProperties();
    }
    

    //
    // CLEAR_OSGI_BUNDLE_CACHE
    //
    public boolean isCleanOSGiBundleCache() {
        String enable = getInstanceProperty(PROPERTY_CLEAN_OSGI_BUNDLE_CACHE);
        return Boolean.valueOf(enable);
    }

    public void setCleanOSGiBundleCache(boolean value) {
        setInstanceProperty(PROPERTY_CLEAN_OSGI_BUNDLE_CACHE, Boolean.toString(value));
        updateProgramArgsFromProperties();
    }
    
    //
    // REFRESH_OSGI_BUNDLE
    //
    public boolean isRefreshOSGiBundle() {
        String enable = getInstanceProperty(PROPERTY_REFRESH_OSGI_BUNDLE);
        return Boolean.valueOf(enable);
    }
    
    public void setRefreshOSGiBundle(boolean value) {
        setInstanceProperty(PROPERTY_REFRESH_OSGI_BUNDLE, Boolean.toString(value));
    }
    
    
    /**
     * remove args no longer specified, add newly specified args, and only specify them once.
     */
    private void updateProgramArgsFromProperties() {
        if (isSuspendArgUpdates()) {
            return;
        }
        Set<String> parmsSet = getProgramArgsSet();
        Set<String> parmsNotSet = getProgramArgsNotSet();
        String existingProgArgs = getProgramArgs();
        
        String programArgs = updateProgramArgsFromProperties(existingProgArgs, parmsSet, parmsNotSet);
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerDelegate.updateProgramArgsFromProperties", programArgs); 

        setProgramArgs(programArgs);
    }
    
    
    /**
     * remove args no longer specified, add newly specified args, and only specify them once.
     */
    private void updateVMArgsFromProperties() {
        if (isSuspendArgUpdates()) {
            return;
        }
        Set<String> parmsSet = getVMArgsSet();
        Set<String> parmsNotSet = getVMArgsNotSet();
        String existingVMArgs = getVMArgs();
        
        String vmArgs = updateVMArgsFromProperties(existingVMArgs, parmsSet, parmsNotSet);
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerDelegate.updateVMArgsFromProperties", vmArgs); 

        setVMArgs(vmArgs);
    }

    private String updateProgramArgsFromProperties(String existingProgramArgs, Set<String> parmsSet, Set<String> parmsNotSet) {
        Set<String> parmsSeen = new HashSet<String>(parmsSet.size());
        List<String> parms;
        if (existingProgramArgs == null) {
            parms = new ArrayList<String>(parmsSet.size());
        } else {
            parms = new ArrayList<String>(Arrays.asList(existingProgramArgs.split("\\s+")));
        }
        // remove notSet and duplicate set parameters from the list
        for (ListIterator<String> iterator = parms.listIterator(); iterator.hasNext();) {
            String parm = iterator.next();
            if (parmsNotSet.contains(parm) || parmsSeen.contains(parm)) {
                iterator.remove();
                continue;
            }
            if (parmsSet.contains(parm)) {
                parmsSet.remove(parm);
                parmsSeen.add(parm);
            }
        }
        StringBuilder sb = new StringBuilder();
        // add new parms to front
        for (String parm : parmsSet) {
            addParm(sb, parm);
        }
        // valid existing parms
        for (String parm : parms) {
            addParm(sb, parm);
        }
        return sb.toString();
    }

    public void updatePropertiesFromProgramArgs(String existingProgramArgs) {
        boolean cleanOSGiCache = false;
        boolean infoConsole = false;
        boolean debugConsole = false;
        List<String> parms;
        if (existingProgramArgs == null) {
            parms = new ArrayList<String>(0);
        } else {
            parms = new ArrayList<String>(Arrays.asList(existingProgramArgs.split("\\s+")));
        }
        for (String parm : parms) {
            if (parm.equals(CLEAN_OSGI_BUNDLE_CACHE)) {
                cleanOSGiCache = true;
            }
            if (parm.equals(CONSOLE_DEBUG)) {
                debugConsole = true;
            }
            if (parm.equals(CONSOLE_INFO)) {
                infoConsole = true;
            }
        }
        suspendArgUpdates();
        setCleanOSGiBundleCache(cleanOSGiCache);
        if (debugConsole) {
            setConsoleLogLevel(CONSOLE_DEBUG);
        } else {
            if (infoConsole) {
                setConsoleLogLevel(CONSOLE_INFO);
            } else {
                setConsoleLogLevel("");
            }
        }
        resumeArgUpdates();
    }

    private void suspendArgUpdates() {
        suspendArgUpdates = true;
    }
    
    private void resumeArgUpdates() {
        suspendArgUpdates = false;
    }
    
    private boolean isSuspendArgUpdates() {
        return suspendArgUpdates;
    }

    /**
     * A parameter. can include spaces inside single or double quotes.
     */
    static final Pattern PARAMETER_PATTERN = Pattern.compile("(?:\"[^\"]*\"|'[^']*'|\\S)+");

    private String updateVMArgsFromProperties(String existingVMArgs, Set<String> parmsSet, Set<String> parmsNotSet) {
        Set<String> parmsSeen = new HashSet<String>(parmsSet.size());
        List<String> parms = new ArrayList<String>(parmsSet.size());
        int lastSystemPropertyFoundIndex = -1;
        if (existingVMArgs == null) {
            // TODO could be a problem
        } else {
            // deal with quoted parms that may contain spaces
            // the regex for a parameter is: (?:"[^"]*"|'[^']*'|\S)+
            // TODO what if there is an unmatched quote??
            Matcher matcher = PARAMETER_PATTERN.matcher(existingVMArgs);
            while (matcher.find()) {
                String parm = matcher.group();
                if (parmsNotSet.contains(parm) || parmsSeen.contains(parm)) {
                    // ignore this parm
                    continue;
                }
                parms.add(parm);
                if (parm.startsWith("-D")) {
                    lastSystemPropertyFoundIndex = parms.size();
                }
                if (parmsSet.contains(parm)) {
                    parmsSet.remove(parm);
                    parmsSeen.add(parm);
                }
            }
        }
        if (lastSystemPropertyFoundIndex < 0) {
            lastSystemPropertyFoundIndex = parms.size();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parms.size(); i++) {
            addParm(sb, parms.get(i));
            if (i > lastSystemPropertyFoundIndex) {
                // add new parms here after last -D parm
                for (String parm : parmsSet) {
                    addParm(sb, parm);
                }
                lastSystemPropertyFoundIndex = -1;
            }
        }
        if (lastSystemPropertyFoundIndex >= 0) {
            // add new parms to end
            for (String parm : parmsSet) {
                addParm(sb, parm);
            }
        }
        return sb.toString();
    }
    
    public void updatePropertiesFromVMArgs(String existingVMArgs) {
        boolean karafShell = false;
        if (existingVMArgs == null) {
            // TODO could be a problem
        } else {
            // deal with quoted parms that may contain spaces
            // the regex for a parameter is: (?:\S|"[^"]*"|'[^']*')+
            // TODO what if there is an umatched quote??
            Matcher matcher = PARAMETER_PATTERN.matcher(existingVMArgs);
            while (matcher.find()) {
                String parm = matcher.group();
                if (parm.equals(ENABLE_KARAF_SHELL)) {
                    karafShell = true;
                }
            }
        }
        suspendArgUpdates();
        setKarafShell(karafShell);
        resumeArgUpdates();
    }

    private void addParm(StringBuilder sb, String parm) {
        if (sb.length() > 0) {
            sb.append(" ");
        }
        sb.append(parm);
    }

    public String getCleanOSGiBundleCacheArgs() {
        return isCleanOSGiBundleCache() ? CLEAN_OSGI_BUNDLE_CACHE : "";
    }

    
    
    // 
    // PROPERTY_KARAF_SHELL
    // 
    public boolean isKarafShell() {
        return getAttribute(PROPERTY_KARAF_SHELL, false);
    }
    public void setKarafShell(boolean enable) {
        setAttribute(PROPERTY_KARAF_SHELL, enable);
        updateVMArgsFromProperties();
    }
    
    public List<String> getKarafShellArgs(boolean enable) {
        if (enable)
            return Arrays.asList(ENABLE_KARAF_SHELL, JLINE_UNSUPPORTED_TERMINAL);
        else
            return Arrays.asList(DISABLE_KARAF_SHELL);
    }

    public String getKarafShellArgs() {
        StringBuilder sb = new StringBuilder();
        for (String parm : getKarafShellArgs(isKarafShell())) {
            addParm(sb, parm);
        }
        return sb.toString();
    }

    public Set<String> getProgramArgsSet() {
        Set<String> parms = new HashSet<String>(2);
        parms.add(getConsoleLogLevel());
        if (isCleanOSGiBundleCache()) {
            parms.add(CLEAN_OSGI_BUNDLE_CACHE);
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
        if (isCleanOSGiBundleCache()) {
        } else {
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
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getVMArgs", value);
        setInstanceProperty(PROPERTY_VM_ARGS, value);
    }

    public Set<String> getVMArgsSet() {
        return new HashSet<String>(getKarafShellArgs(isKarafShell()));
    }

    public Set<String> getVMArgsNotSet() {
        return new HashSet<String>(getKarafShellArgs(!isKarafShell()));
    }

    // 
    // PROPERTY_PROGRAM_ARGS
    // 
    public String getProgramArgs() {
        return getInstanceProperty(PROPERTY_PROGRAM_ARGS);
    }
    public void setProgramArgs(String value) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getProgramArgs", value);
        setInstanceProperty(PROPERTY_PROGRAM_ARGS, value);
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
    // PROPERTY_NOREDEPLOY
    //
    public boolean isNoRedeploy() {
        return getAttribute(PROPERTY_NOREDEPLOY, false);
    }
    public void setNoRedeploy(boolean enable){
        setAttribute(PROPERTY_NOREDEPLOY, enable);
    }

    public List<String> getNoRedeployFilePatternsAsList(boolean includePatterns) {
        String propertyName = (includePatterns) ? PROPERTY_NOREDEPLOY_INCLUDE_PATTERNS : PROPERTY_NOREDEPLOY_EXCLUDE_PATTERNS;
        List<String> defaults = (includePatterns) ? DEFAULT_NOREDEPLOY_INCLUDE_PATTERNS : DEFAULT_NOREDEPLOY_EXCLUDE_PATTERNS;
        List<String> patterns = getAttribute(propertyName, defaults);
        return patterns;
    }
    
    public String[] getNoRedeployFilePatterns(boolean includePatterns) {
        List<String> patterns = getNoRedeployFilePatternsAsList(includePatterns);
        return patterns.toArray(new String [patterns.size()]);
    }
    
    public void setNoRedeployFilePatterns(boolean includePatterns, String[] patterns) {
        String propertyName = (includePatterns) ? PROPERTY_NOREDEPLOY_INCLUDE_PATTERNS : PROPERTY_NOREDEPLOY_EXCLUDE_PATTERNS;
        setAttribute(propertyName, Arrays.asList(patterns));
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
        try {
            setServerInstanceProperties(map);
        } catch (Exception e) {
            // TODO WTF? Need to figure out why this fails...  seems to fail before setDefaults is called.
            Trace.trace(Trace.INFO, "GeronimoServerDelegate.setInstanceProperty(name = " + name + ", value = " + value + " )", e, Activator.traceCore);
        }
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
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.v30.doGetParentModules", module);

        ArrayList<IModule> parents = new ArrayList<IModule>();
        parents.addAll(getApplicationModules(module));

        // also check to see if the module is a utility module for a stand-alone
        // web module
        for (IModule war : J2EEUtil.getWebModules(module, null)) {
            if (getApplicationModules(war).isEmpty()) {
                parents.add(war);
            }
        }
        IModule[] modules = (IModule[]) parents.toArray(new IModule[parents.size()]);
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.v30.doGetParentModules", modules);
        return modules;
    }


    /**
     * Return all applications this module is contained in
     * 
     * @param module
     * 
     * @return 
     */
    private List<IModule> getApplicationModules(IModule module) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerDelegate.getApplicationModules", module);

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

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerDelegate.getApplicationModules", list);
        return list;
    }
}
