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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.geronimo.deployment.plugin.factories.BaseDeploymentFactory;
import org.apache.geronimo.st.core.commands.DeploymentCmdStatus;
import org.apache.geronimo.st.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.core.commands.IDeploymentCommand;
import org.apache.geronimo.st.core.internal.DependencyHelper;
import org.apache.geronimo.st.core.internal.Messages;
import org.apache.geronimo.st.core.internal.Trace;
import org.apache.geronimo.st.core.operations.ISharedLibEntryCreationDataModelProperties;
import org.apache.geronimo.st.core.operations.SharedLibEntryCreationOperation;
import org.apache.geronimo.st.core.operations.SharedLibEntryDataModelProvider;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.launching.RuntimeClasspathEntry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.internal.ProgressUtil;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
abstract public class GeronimoServerBehaviourDelegate extends ServerBehaviourDelegate implements IGeronimoServerBehavior {

    public static final int TIMER_TASK_INTERVAL = 20;
    
    public static final int TIMER_TASK_DELAY = 20;

    protected IProgressMonitor _monitor;

    protected Timer timer = null;

    protected PingThread pingThread;

    protected transient IProcess process;

    protected transient IDebugEventSetListener processListener;

    public static final String ERROR_SETUP_LAUNCH_CONFIGURATION = "errorInSetupLaunchConfiguration";

    abstract protected ClassLoader getContextClassLoader();


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#setupLaunchConfiguration(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public void setupLaunchConfiguration(ILaunchConfigurationWorkingCopy wc, IProgressMonitor monitor) throws CoreException {
        if (isRemote())// No launch for remote servers.
            return;

        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, getRuntimeClass());

        GeronimoRuntimeDelegate runtime = getRuntimeDelegate();

        IVMInstall vmInstall = runtime.getVMInstall();
        if (vmInstall != null)
            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_JRE_CONTAINER_PATH, JavaRuntime.newJREContainerPath(vmInstall).toPortableString());

        String existingProgArgs = null;
        wc.setAttribute(ERROR_SETUP_LAUNCH_CONFIGURATION, (String)null);
        
        try{
            setupLaunchClasspath(wc, vmInstall);
            existingProgArgs = wc.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, (String) null);
        }catch (CoreException e){
            // Throwing a CoreException at this time will not accomplish anything useful as WTP will 
            // will essentially ignore it. Instead set a flag in the configuration that can 
            // subsequently be checked when an attempt is made to launch the server in 
            // GeronimoLaunchConfigurationDelegate.launch(). At that point a CoreException will be
            // thrown that WTP will handle properly and will display an error dialog which is 
            // exactly what we want the GEP user to see.
            wc.setAttribute(ERROR_SETUP_LAUNCH_CONFIGURATION, e.getMessage());
        }
        String serverProgArgs = getServerDelegate().getConsoleLogLevel();
        if (existingProgArgs == null || existingProgArgs.indexOf(serverProgArgs) < 0) {
            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, serverProgArgs);
        }
        
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, getServerDelegate().getVMArgs());
    }


    /**
     * @param launch
     * @param launchMode
     * @param monitor
     * @throws CoreException
     */
    synchronized protected void setupLaunch(ILaunch launch, String launchMode, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.setupLaunch", launch, launchMode, monitor); 

        if (!SocketUtil.isLocalhost(getServer().getHost()))
            return;

        ServerPort[] ports = getServer().getServerPorts(null);
        for (int i = 0; i < ports.length; i++) {
            ServerPort sp = ports[i];
            if (SocketUtil.isPortInUse(ports[i].getPort(), 5))
                throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, Messages.bind(Messages.errorPortInUse, Integer.toString(sp.getPort()), sp.getName()), null));
        }

        stopUpdateServerStateTask();
        setServerState(IServer.STATE_STARTING);
        setMode(launchMode);

        IServerListener listener = new IServerListener() {
            public void serverChanged(ServerEvent event) {
                int eventKind = event.getKind();
                if ((eventKind & ServerEvent.STATE_CHANGE) != 0) {
                    int state = event.getServer().getServerState();
                    if (state == IServer.STATE_STARTED
                            || state == IServer.STATE_STOPPED) {
                        GeronimoServerBehaviourDelegate.this.getServer().removeServerListener(this);
                        startUpdateServerStateTask();
                    }
                }
            }
        };

        getServer().addServerListener(listener);

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.setupLaunch");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#stop(boolean)
     */
    synchronized public void stop(final boolean force) {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stop", force);

        stopPingThread();
        if (getServer().getServerState() != IServer.STATE_STOPPED) {
            setServerState(IServer.STATE_STOPPING);
            stopKernel();
        }
        GeronimoConnectionFactory.getInstance().destroy(getServer());
        if (force) {
            terminate();
            return;
        }
        int state = getServer().getServerState();
        if (state == IServer.STATE_STOPPED)
            return;
        if (state == IServer.STATE_STARTING || state == IServer.STATE_STOPPING)
            terminate();
                                                   
        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.stop");
    }

    /* 
     * Override this method to be able to process in-place shared lib entries and restart the shared lib configuration for all projects prior
     * to publishing each IModule.
     * 
     * This overridden method also fixes WTP Bugzilla 123676 to prevent duplicate repdeloys if both parent and child modules have deltas.
     * 
     * (non-Javadoc)
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#publishModules(int, java.util.List, java.util.List, org.eclipse.core.runtime.MultiStatus, org.eclipse.core.runtime.IProgressMonitor)
     */
    protected void publishModules(int kind, List modules, List deltaKind, MultiStatus multi, IProgressMonitor monitor) {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.publishModules", deltaKindToString(kind), Arrays.asList(modules).toString(), Arrays.asList(deltaKind).toString(), multi, monitor);

        // 
        // WTP publishes modules in reverse alphabetical order which does not account for possible 
        // dependencies between modules. If necessary reorder the publish order of the modules 
        // based on any discovered dependencies. 
        //
        if (modules != null && modules.size() > 0) {
            DependencyHelper dh = new DependencyHelper();
            List list = dh.reorderModules(modules, deltaKind);
            modules = (List) list.get(0);
            deltaKind = (List) list.get(1);
        }

        IStatus status = Status.OK_STATUS;
        if (modules != null && modules.size() > 0 && getGeronimoServer().isInPlaceSharedLib()) {
            List rootModules = new ArrayList<IModule>();
            for(int i = 0; i < modules.size(); i++) {
                IModule[] module = (IModule[]) modules.get(i);
                if(!rootModules.contains(module[0])) {
                    rootModules.add(module[0]);
                }
            }
            IModule[] toProcess = (IModule[])rootModules.toArray(new IModule[rootModules.size()]);
            status = updateSharedLib(toProcess, ProgressUtil.getSubMonitorFor(monitor, 1000));
        }
        if(status.isOK()) {
            if (modules == null)
                return;
            
            int size = modules.size();
            if (size == 0)
                return;
            
            if (monitor.isCanceled())
                return;
            
            List rootModulesPublished = new ArrayList<IModule>();
            for (int i = 0; i < size; i++) {
                IModule[] module = (IModule[]) modules.get(i);
                int moduleDeltaKind = ((Integer)deltaKind.get(i)).intValue();
                //has the root of this module been published already?
                if(!rootModulesPublished.contains(module[0])) {
                    status = publishModule(kind, module, moduleDeltaKind, ProgressUtil.getSubMonitorFor(monitor, 3000));
                    if (status != null && !status.isOK())
                        multi.add(status);
                    //cache published root modules to compare against to prevent dup redeploys
                    if(moduleDeltaKind != NO_CHANGE) {
                        rootModulesPublished.add(module[0]);
                    }
                } else {
                    setModulePublishState(module, IServer.PUBLISH_STATE_NONE);
                    Trace.trace(Trace.INFO, "root module for " + Arrays.asList(module).toString() + " already published.  Skipping.");
                }   
            }
        } else {
            multi.add(status);
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.publishModules");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#publishModule(int,
     *      int, org.eclipse.wst.server.core.IModule[],
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public void publishModule(int kind, int deltaKind, IModule[] module, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.publishModule", deltaKindToString(kind), deltaKindToString(deltaKind), Arrays.asList(module).toString(), monitor);

        _monitor = monitor;

        setModulePublishState(module, IServer.PUBLISH_STATE_NONE);
        try {
            //NO_CHANGE need if app is associated but not started and no delta
            if (deltaKind == NO_CHANGE && module.length == 1) {
                invokeCommand(deltaKind, module[0]);
            } else if (deltaKind == CHANGED || deltaKind == ADDED || deltaKind == REMOVED) {
                invokeCommand(deltaKind, module[0]);
            } 
        } 
        catch (CoreException e) {
            //
            // Set the module publish state to UNKNOWN so that WTP will not display "Synchronized"
            // for the module and server. What we should see in the Eclipse Server View in this case
            // is nothing for the module, but "Republish" for the server. This is a very subtle
            // distinction but the only indication to the GEP end-user that their module was not 
            // successfully deployed.
            //
            setModulePublishState(module, IServer.PUBLISH_STATE_UNKNOWN);
            throw e;
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.publishModule");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#publishFinish(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void publishFinish(IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.publishFinish", monitor);

        IModule[] modules = this.getServer().getModules();
        boolean allpublished = true;
        for (int i = 0; i < modules.length; i++) {
            if (this.getServer().getModulePublishState(new IModule[] { modules[i] }) != IServer.PUBLISH_STATE_NONE)
                allpublished = false;
        }
        if (allpublished)
            setServerPublishState(IServer.PUBLISH_STATE_NONE);

        GeronimoConnectionFactory.getInstance().destroy(getServer());

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.publishFinish");
    }


    /**
     * Initializes the Geronimo server delegate. This method is called by the server core framework 
     * to give delegates a chance to do their own initialization. As such, the GEP proper should 
     * never call this method.
     * 
     * @param monitor a progress monitor, or <code>null</code> if progress reporting and cancellation 
     * are not desired
     */
    protected void initialize(IProgressMonitor monitor) {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.initialize", monitor);
        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.initialize");
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#dispose()
     */
    public void dispose() {
        stopUpdateServerStateTask();
    }

    public String getRuntimeClass() {
        return "org.apache.geronimo.system.main.Daemon";
    }

    public void setServerStarted() {
        setServerState(IServer.STATE_STARTED);
    }

    public void setServerStopped() {
        setServerState(IServer.STATE_STOPPED);
    }

    public IGeronimoServer getGeronimoServer() {
        return (IGeronimoServer) getServer().loadAdapter(IGeronimoServer.class, null);
    }


    protected void terminate() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.terminate");

        if (getServer().getServerState() == IServer.STATE_STOPPED)
            return;

        try {
            setServerState(IServer.STATE_STOPPING);
            Trace.trace(Trace.INFO, "Killing the geronimo server process"); //$NON-NLS-1$
            if (process != null && !process.isTerminated()) {
                process.terminate();

            }
            stopImpl();
        } catch (Exception e) {
            Trace.trace(Trace.SEVERE, "Error killing the geronimo server process", e); //$NON-NLS-1$
            // 
            // WTP does not allow a CoreException to be thrown in this case 
            // 
            throw new RuntimeException(Messages.STOP_FAIL);
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.terminate");
    }


    protected void stopImpl() {
        if (process != null) {
            process = null;
            DebugPlugin.getDefault().removeDebugEventListener(processListener);
            processListener = null;
        }
        setServerState(IServer.STATE_STOPPED);
    }

    protected void invokeCommand(int deltaKind, IModule module) throws CoreException {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.invokeCommand", deltaKindToString(deltaKind), module.getName());
        
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader cl = getContextClassLoader();
            if (cl != null)
                Thread.currentThread().setContextClassLoader(cl);
            switch (deltaKind) {
            case ADDED: {
                doAdded(module, null);
                break;
            }
            case CHANGED: {
                doChanged(module, null);
                break;
            }
            case REMOVED: {
                doRemoved(module);
                break;
            }
            case NO_CHANGE: {
                doNoChange(module);
                break;
            }
            default:
                throw new IllegalArgumentException();
            }
        } catch (CoreException e) {
            GeronimoUtils.displayEclipseErrorLog();
            throw e;
        } catch (Exception e) {
            GeronimoUtils.displayEclipseErrorLog();
            e.printStackTrace();
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.invokeCommand");
    }   

    /**
     * @param module
     * @param configId the forced configId to process this method, passed in when this method is invoked from doChanged()
     * @throws Exception
     */
    protected void doAdded(IModule module, String configId) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.doAdded", module.getName(), configId);
        
        configId = getLastKnowConfigurationId(module, configId);
        if (configId == null) {
            IStatus status = distribute(module);
            if (!status.isOK()) {
                doFail(status, Messages.DISTRIBUTE_FAIL);
            }

            TargetModuleID[] ids = updateServerModuleConfigIDMap(module, status);

            status = start(ids);
            if (!status.isOK()) {
                doFail(status, Messages.START_FAIL);
            }
        } else {
            //either (1) a configuration with the same module id exists already on the server
            //or (2) the module now has a different configId and the configuration on the server using
            //the old id as specified in the project-configId map should be uninstalled.
            doChanged(module, configId);
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.doAdded");
    }

    /**
     * @param module
     * @param configId the forced configId to process this method, passed in when invoked from doAdded()
     * @throws Exception
     */
    protected void doChanged(IModule module, String configId) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.doChanged", module.getName(), configId);
        
        configId = getLastKnowConfigurationId(module, configId);
        if(configId != null) {
            String moduleConfigId = getConfigId(module);
            if(moduleConfigId.equals(configId)) {
                IStatus status = reDeploy(module);
                if (!status.isOK()) {
                    doFail(status, Messages.REDEPLOY_FAIL);
                }
            } else {
                //different configIds from what needs to be undeployed to what will be deployed
                doRemoved(module);
                doAdded(module, null);
            }
        } else {
            //The checked configuration no longer exists on the server
            doAdded(module, configId);
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.doChanged");
    }

    private String getLastKnowConfigurationId(IModule module, String configId) throws CoreException {
        Trace.tracePoint("Entry ", "GeronimoServerBehaviourDelegate.getLastKnowConfigurationId", module.getName(), configId);

        //use the correct configId, second from the .metadata, then from the plan
        configId = configId != null ? configId : DeploymentUtils.getLastKnownConfigurationId(module, getServer());

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.getLastKnowConfigurationId", configId);
        return configId;
    }

    protected void doRemoved(IModule module) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.doRemoved", module.getName());

        IStatus status = unDeploy(module);
        if (!status.isOK()) {
            doFail(status, Messages.UNDEPLOY_FAIL);
        }
        
        ModuleArtifactMapper.getInstance().removeEntry(getServer(), module.getProject());

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.doRemoved");
    }
    
    protected void doNoChange(IModule module) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.doNoChange", module.getName());
        
        if(DeploymentUtils.getLastKnownConfigurationId(module, getServer()) != null) {
            start(module);
        } else {
            doAdded(module, null);
        }
        
        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.doNoChange");
    }

    protected void doRestart(IModule module) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.doRestart", module.getName());
        
        IStatus status = stop(module);
        if (!status.isOK()) {
            doFail(status, Messages.STOP_FAIL);
        }

        status = start(module);
        if (!status.isOK()) {
            doFail(status, Messages.START_FAIL);
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.doRestart");
    }
    
    private TargetModuleID[] updateServerModuleConfigIDMap(IModule module, IStatus status) {
        TargetModuleID[] ids = ((DeploymentCmdStatus) status).getResultTargetModuleIDs();
        ModuleArtifactMapper mapper = ModuleArtifactMapper.getInstance();
        mapper.addEntry(getServer(), module.getProject(), ids[0].getModuleID());
        return ids;
    }

    protected void doFail(IStatus status, String message) throws CoreException {
        MultiStatus ms = new MultiStatus(Activator.PLUGIN_ID, 0, message, null);
        ms.addAll(status);
        GeronimoUtils.displayEclipseErrorLog();
        throw new CoreException(ms);
    }

    protected IStatus distribute(IModule module) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createDistributeCommand(module, getServer());
        return cmd.execute(_monitor);
    }

    protected IStatus start(IModule module) throws Exception {
        TargetModuleID id = DeploymentUtils.getTargetModuleID(getServer(), module);
        IDeploymentCommand cmd = DeploymentCommandFactory.createStartCommand(new TargetModuleID[] { id }, module, getServer());
        return cmd.execute(_monitor);
    }
    
    protected IStatus start(TargetModuleID[] ids) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createStartCommand(ids, null, getServer());
        return cmd.execute(_monitor);
    }

    protected IStatus stop(IModule module) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createStopCommand(module, getServer());
        return cmd.execute(_monitor);
    }

    protected IStatus unDeploy(IModule module) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createUndeployCommand(module, getServer());
        return cmd.execute(_monitor);
    }

    protected IStatus reDeploy(IModule module) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createRedeployCommand(module, getServer());
        return cmd.execute(_monitor);
    }

    public Map getServerInstanceProperties() {
        return getRuntimeDelegate().getServerInstanceProperties();
    }

    protected GeronimoRuntimeDelegate getRuntimeDelegate() {
        GeronimoRuntimeDelegate rd = (GeronimoRuntimeDelegate) getServer().getRuntime().getAdapter(GeronimoRuntimeDelegate.class);
        if (rd == null)
            rd = (GeronimoRuntimeDelegate) getServer().getRuntime().loadAdapter(GeronimoRuntimeDelegate.class, new NullProgressMonitor());
        return rd;
    }

    protected GeronimoServerDelegate getServerDelegate() {
        GeronimoServerDelegate sd = (GeronimoServerDelegate) getServer().getAdapter(GeronimoServerDelegate.class);
        if (sd == null)
            sd = (GeronimoServerDelegate) getServer().loadAdapter(GeronimoServerDelegate.class, new NullProgressMonitor());
        return sd;
    }

    protected boolean isRemote() {
        return getServer().getServerType().supportsRemoteHosts()
                && !SocketUtil.isLocalhost(getServer().getHost());
    }

    protected void setupLaunchClasspath(ILaunchConfigurationWorkingCopy wc, IVMInstall vmInstall) throws CoreException {
        List<IRuntimeClasspathEntry> cp = new ArrayList<IRuntimeClasspathEntry>();
        IPath serverJar = getServer().getRuntime().getLocation().append("/bin/server.jar");
        cp.add(JavaRuntime.newArchiveRuntimeClasspathEntry(serverJar));
        // merge existing classpath with server classpath
        IRuntimeClasspathEntry[] existingCps = JavaRuntime.computeUnresolvedRuntimeClasspath(wc);

        for (int i = 0; i < existingCps.length; i++) {
            Trace.trace(Trace.INFO, "cpentry: " + cp );
            if (cp.contains(existingCps[i]) == false) {
                cp.add(existingCps[i]);
            }
        }

        //
        // Add classpath entries from any selected classpath containers
        //
        if ( getGeronimoServer().isSelectClasspathContainers()) {
            List<String> containers = getGeronimoServer().getClasspathContainers();
            for ( String containerPath : containers ) {
                List<IClasspathEntry> cpes = ClasspathContainersHelper.queryWorkspace( containerPath );
                for ( IClasspathEntry cpe : cpes ) {
                    RuntimeClasspathEntry rcpe = new RuntimeClasspathEntry( cpe );
                    Trace.trace(Trace.INFO, "Classpath Container Entry: " + rcpe );
                    if (cp.contains(rcpe) == false) {
                        cp.add( rcpe );
                    }
                }
            }
        }

        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, convertCPEntryToMemento(cp));
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
    }

    private List convertCPEntryToMemento(List cpEntryList) {
        List<String> list = new ArrayList<String>(cpEntryList.size());
        Iterator iterator = cpEntryList.iterator();
        while (iterator.hasNext()) {
            IRuntimeClasspathEntry entry = (IRuntimeClasspathEntry) iterator.next();
            try {
                list.add(entry.getMemento());
            } catch (CoreException e) {
                Trace.trace(Trace.SEVERE, "Could not resolve classpath entry: "
                        + entry, e);
            }
        }
        return list;
    }

    public void setProcess(final IProcess newProcess) {
        if (process != null)
            return;

        process = newProcess;
        if (processListener != null)
            DebugPlugin.getDefault().removeDebugEventListener(processListener);
        if (newProcess == null)
            return;

        processListener = new IDebugEventSetListener() {
            public void handleDebugEvents(DebugEvent[] events) {
                if (events != null) {
                    int size = events.length;
                    for (int i = 0; i < size; i++) {
                        if (process != null
                                && process.equals(events[i].getSource())
                                && events[i].getKind() == DebugEvent.TERMINATE) {
                            DebugPlugin.getDefault().removeDebugEventListener(this);
                            stopImpl();
                        }
                    }
                }
            }
        };
        DebugPlugin.getDefault().addDebugEventListener(processListener);
    }

    protected void startPingThread() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.startPingThread");

        pingThread = new PingThread(this, getServer());
        pingThread.start();

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.startPingThread");
    }
    
    protected void stopPingThread() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stopPingThread");

        if (pingThread != null) {
            pingThread.interrupt();
            pingThread = null;
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.stopPingThread");
    }
    
    protected abstract void stopKernel();

    public void startUpdateServerStateTask() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.startUpdateServerStateTask", getServer().getName());

        timer = new Timer(true);
        timer.schedule(new UpdateServerStateTask(this, getServer()), TIMER_TASK_DELAY * 1000, TIMER_TASK_INTERVAL * 1000);

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.startUpdateServerStateTask");
    }

    public void stopUpdateServerStateTask() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stopUpdateServerStateTask", getServer().getName());

        if (timer != null)
            timer.cancel();

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.stopUpdateServerStateTask");
    }

    protected IPath getModulePath(IModule[] module, URL baseURL) {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.getModulePath", Arrays.asList(module).toString(), baseURL);

        IPath modulePath = new Path(baseURL.getFile());

        if (module.length == 2) {
            IModule workingModule = module[module.length - 1];
            modulePath = modulePath.append(workingModule.getName());
            if (GeronimoUtils.isWebModule(workingModule)) {
                modulePath = modulePath.addFileExtension("war");
            } else if (GeronimoUtils.isEjbJarModule(workingModule)) {
                modulePath = modulePath.addFileExtension("jar");
            } else if (GeronimoUtils.isRARModule(workingModule)) {
                modulePath = modulePath.addFileExtension("rar");
            } else if (GeronimoUtils.isEarModule(workingModule)) {
                modulePath = modulePath.addFileExtension("ear");
            } else if (GeronimoUtils.isAppClientModule(workingModule)) {
                modulePath = modulePath.addFileExtension("jar");
            }
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.getModulePath", modulePath);
        return modulePath;
    }

    public MBeanServerConnection getServerConnection() throws Exception {
        Map map = new HashMap();
        String user = getGeronimoServer().getAdminID();
        String password = getGeronimoServer().getAdminPassword();
        String port = getGeronimoServer().getRMINamingPort();
        map.put("jmx.remote.credentials", new String[] { user, password });
        map.put("java.naming.factory.initial", "com.sun.jndi.rmi.registry.RegistryContextFactory");
        map.put("java.naming.factory.url.pkgs", "org.apache.geronimo.naming");
        map.put("java.naming.provider.url", "rmi://" + getServer().getHost()
                + ":" + port);
        map.put(JMXConnectorFactory.DEFAULT_CLASS_LOADER, BaseDeploymentFactory.class.getClassLoader());

        String url = getGeronimoServer().getJMXServiceURL();
        if (url != null) {
            try {
                JMXServiceURL address = new JMXServiceURL(url);
                JMXConnector jmxConnector;
                try {
                    jmxConnector = JMXConnectorFactory.connect(address, map);
                } catch (SecurityException se) {
                    //FIXME once GERONIMO-3467 JIRA is fixed
                    Thread.sleep(10000);
                    jmxConnector = JMXConnectorFactory.connect(address, map);
                }
                MBeanServerConnection connection = jmxConnector.getMBeanServerConnection();
                Trace.trace(Trace.INFO, "Connected to kernel. " + url);
                return connection;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    
    public Target[] getTargets() {
        return null;
    }
    
    public static String deltaKindToString(int kind) {
        switch(kind) {
        case NO_CHANGE:
            return "NO_CHANGE";
        case ADDED:
            return "ADDED";
        case CHANGED:
            return "CHANGED";
        case REMOVED:
            return "REMOVED";
        }
        return Integer.toString(kind);
    }
    
    public String getConfigId(IModule module) {
        return getGeronimoServer().getVersionHandler().getConfigID(module);
    }
    
    private IStatus updateSharedLib(IModule[] module, IProgressMonitor monitor) {
        IDataModel model = DataModelFactory.createDataModel(new SharedLibEntryDataModelProvider());
        model.setProperty(ISharedLibEntryCreationDataModelProperties.MODULES, module);
        model.setProperty(ISharedLibEntryCreationDataModelProperties.SERVER, getServer());
        IDataModelOperation op = new SharedLibEntryCreationOperation(model);
        try {
            op.execute(monitor, null);
        } catch (ExecutionException e) {
            GeronimoUtils.displayEclipseErrorLog();
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, e.getMessage(), e.getCause());
        }
        return Status.OK_STATUS;
    }
    
}
