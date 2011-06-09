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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.geronimo.deployment.plugin.jmx.ExtendedDeploymentManager;
import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.v30.core.UpdateServerStateTask;
import org.apache.geronimo.st.v30.core.commands.DeploymentCmdStatus;
import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.commands.IDeploymentCommand;
import org.apache.geronimo.st.v30.core.internal.Messages;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.operations.ISharedLibEntryCreationDataModelProperties;
import org.apache.geronimo.st.v30.core.operations.SharedLibEntryCreationOperation;
import org.apache.geronimo.st.v30.core.operations.SharedLibEntryDataModelProvider;
import org.apache.geronimo.st.v30.core.osgi.AriesHelper;
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
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
abstract public class GeronimoServerBehaviourDelegate extends ServerBehaviourDelegate implements IGeronimoServerBehavior {

    public static final int TIMER_TASK_INTERVAL = 20;
    
    public static final int TIMER_TASK_DELAY = 20;

    protected Timer stateTimer = null;
    
    protected Timer synchronizerTimer = null;

    protected PingThread pingThread;

    protected transient IProcess process;

    protected transient IDebugEventSetListener processListener;

    public static final String ERROR_SETUP_LAUNCH_CONFIGURATION = "errorInSetupLaunchConfiguration";

    abstract protected ClassLoader getContextClassLoader();

    private PublishStateListener publishStateListener;
    
    private Lock publishLock = new ReentrantLock();

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
        if (vmInstall != null) {
            wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_JRE_CONTAINER_PATH,
                    JavaRuntime.newJREContainerPath(vmInstall).toPortableString());
        }
        
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
        GeronimoServerDelegate gsd = getServerDelegate();
        String programArgs = gsd.getProgramArgs();
        Trace.tracePoint("GeronimoServerBehaviourDelegate.v30", Activator.traceCore, "setupLaunchConfiguration serverProgramArgs", programArgs);
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, programArgs);
        
        /*
        programArgs = getServerProgramArgs(existingProgArgs, getServerDelegate());
        Trace.tracePoint("GeronimoServerBehaviourDelegate.v30", "setupLaunchConfiguration serverProgramArgs",
                programArgs);
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, programArgs);
        */

        String vmArgs = gsd.getVMArgs();
        Trace.tracePoint("GeronimoServerBehaviourDelegate.v30", Activator.traceCore, "setupLaunchConfiguration serverVMArgs", vmArgs);
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmArgs);
    }


    /**
     * @param launch
     * @param launchMode
     * @param monitor
     * @throws CoreException
     */
    synchronized protected void setupLaunch(ILaunch launch, String launchMode, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.setupLaunch", launch, launchMode, monitor); 

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

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.setupLaunch");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#stop(boolean)
     */
    synchronized public void stop(final boolean force) {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stop", Activator.traceCore);

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
                                                   
        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.stop", Activator.traceCore);
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
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishModules", publishKindToString(kind), Arrays.asList(modules), Arrays.asList(deltaKind), multi, monitor);

        // 
        // WTP publishes modules in reverse alphabetical order which does not account for possible 
        // dependencies between modules. If necessary reorder the publish order of the modules 
        // based on any discovered dependencies. 
        //
        if (modules != null && modules.size() > 0) {
            List list = getOrderedModules(this.getServer(),modules, deltaKind);
            modules = (List) list.get(0);
            deltaKind = (List) list.get(1);
            // trace output
            for (int i = 0; i < modules.size(); i++) {
                IModule[] module = (IModule[]) modules.get(i);
                Trace.trace(Trace.INFO, i + " " + Arrays.asList(module).toString() + " "
                        + deltaKindToString(((Integer) deltaKind.get(i)).intValue()), Activator.traceCore);
            }
        }

        IStatus status = Status.OK_STATUS;
        if (modules != null && modules.size() > 0 && getGeronimoServer().isInPlaceSharedLib()) {
            List<IModule> rootModules = new ArrayList<IModule>();
            for(int i = 0; i < modules.size(); i++) {
                IModule[] module = (IModule[]) modules.get(i);
                if(!rootModules.contains(module[0])) {
                    rootModules.add(module[0]);
                }
            }
            IModule[] toProcess = (IModule[])rootModules.toArray(new IModule[rootModules.size()]);
            status = updateSharedLib(toProcess, ProgressUtil.getSubMonitorFor(monitor, 1000));
        }

        /* 
         * Build a map of root modules that need to be published. This is to ensure that
         * we avoid redeploys and it guarantees that publishModule() is called once per
         * deployed application. 
         */
        Map<IModule, ModuleDeltaList> publishMap = new LinkedHashMap<IModule, ModuleDeltaList>();
        for (int i = 0; i < modules.size(); i++) {
            IModule[] module = (IModule[]) modules.get(i);
            Integer moduleDeltaKind = (Integer) deltaKind.get(i);
            IModule rootModule = module[0];
            
            ModuleDeltaList list = publishMap.get(rootModule);
            if (list == null) {
                list = new ModuleDeltaList(rootModule);
                publishMap.put(rootModule, list);
            }
            
            if (module.length == 1) {
                list.setRootModuleDelta(moduleDeltaKind.intValue());
            } else {
                list.addChildModule(module, moduleDeltaKind.intValue());
            }
        }
        
        if(status.isOK()) {
            if (modules == null)
                return;
            
            int size = modules.size();
            if (size == 0)
                return;
            
            if (monitor.isCanceled())
                return;
            
            boolean refreshOSGiBundle = getServerDelegate().isRefreshOSGiBundle();
            for (ModuleDeltaList moduleList : publishMap.values()) {  
                IModule[] rootModule = moduleList.getRootModule();
                if (refreshOSGiBundle && GeronimoUtils.isEBAModule(rootModule[0]) && moduleList.hasChangedChildModulesOnly()) {
                    List<IModule[]> changedModules = new ArrayList<IModule[]>();
                    List<IModule[]> unChangedModules = new ArrayList<IModule[]>();
                    for (ModuleDelta moduleDelta : moduleList.getChildModules()) {
                        if (moduleDelta.delta == CHANGED) {
                            changedModules.add(moduleDelta.module);
                        } else {
                            unChangedModules.add(moduleDelta.module);
                        }
                    }
                    status = refreshBundles(rootModule[0], changedModules, ProgressUtil.getSubMonitorFor(monitor, 3000));
                    if (status != null && !status.isOK()) {
                        multi.add(status);
                    }
                    unChangedModules.add(rootModule);
                    for (IModule[] module : unChangedModules) {                   
                        setModulePublishState(module, IServer.PUBLISH_STATE_NONE);
                        setModuleStatus(module, null);
                    }
                } else {
                    status = publishModule(kind, rootModule, moduleList.getEffectiveRootDelta(), ProgressUtil.getSubMonitorFor(monitor, 3000));
                    if (status != null && !status.isOK()) {
                        multi.add(status);
                    } else {
                        for (ModuleDelta moduleDelta : moduleList.getChildModules()) {
                            setModulePublishState(moduleDelta.module, IServer.PUBLISH_STATE_NONE);
                            setModuleStatus(moduleDelta.module, null);
                        }
                    }
                }
            }

        } else {
            multi.add(status);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishModules");
    }
    
    protected IStatus refreshBundles(IModule ebaModule, List<IModule[]> bundleModules, IProgressMonitor monitor) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundles", ebaModule, bundleModules, monitor);

        String configId = ModuleArtifactMapper.getInstance().resolveArtifact(getServer(), ebaModule);
        if (configId == null) {
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.bind(Messages.REFRESH_NO_CONFIGURATION_FAIL, ebaModule.getProject().getName()));
        }
        
        if (monitor.isCanceled()) {
            return Status.CANCEL_STATUS;
        }
        
        MultiStatus multiStatus = new MultiStatus(Activator.PLUGIN_ID, 0, "", null);
        
        try {
            ExtendedDeploymentManager dm = (ExtendedDeploymentManager) DeploymentCommandFactory.getDeploymentManager(getServer());
            AbstractName ebaName = dm.getApplicationGBeanName(Artifact.create(configId));
            long[] bundleIds = dm.getEBAContentBundleIds(ebaName);

            Map<String, Long> bundleMap = new HashMap<String, Long>();
            for (long bundleId : bundleIds) {
                String symbolicName = dm.getEBAContentBundleSymbolicName(ebaName, bundleId);
                if (symbolicName != null) {
                    bundleMap.put(symbolicName, bundleId);
                }
            }
            
            for (IModule[] bundleModule : bundleModules) {
                if (monitor.isCanceled()) {
                    return Status.CANCEL_STATUS;
                }
                
                IStatus status = refreshBundle(ebaModule, bundleModule[1], ebaName, bundleMap);
                if (status.isOK()) {
                    setModulePublishState(bundleModule, IServer.PUBLISH_STATE_NONE);
                    setModuleStatus(bundleModule, null);
                } else {
                    multiStatus.add(status);
                    setModuleStatus(bundleModule, status);
                    setModulePublishState(bundleModule, IServer.PUBLISH_STATE_UNKNOWN);
                }
            }
        } catch (Exception e) {
            multiStatus.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, Messages.REFRESH_FAIL, e));
        }
        
        IStatus status;
        if (multiStatus.isOK()) {
            status = Status.OK_STATUS;
        } else {
            status = multiStatus;
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundles", status);
        
        return status;
    }
    
    private IStatus refreshBundle(IModule ebaModule, IModule bundleModule, AbstractName ebaName, Map<String, Long> bundleMap) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundle", ebaModule, bundleModule, ebaName, bundleMap);

        try {
            File file = DeploymentUtils.getTargetFile(getServer(), bundleModule);
            if (file == null) {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.bind(Messages.moduleExportError, bundleModule.getProject().getName()));
            }
            
            String symbolicName = AriesHelper.getSymbolicName(bundleModule);
            Long bundleId = bundleMap.get(symbolicName);
            
            if (bundleId == null) {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.bind(Messages.REFRESH_NO_BUNDLE_FAIL,
                        new String[] {bundleModule.getProject().getName(), ebaModule.getProject().getName()}));
            }
            
            ExtendedDeploymentManager dm = (ExtendedDeploymentManager) DeploymentCommandFactory.getDeploymentManager(getServer());
            dm.updateEBAContent(ebaName, bundleId, file);
        } catch (Exception e) {
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, Messages.REFRESH_FAIL, e);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundle");

        return Status.OK_STATUS;
    }

    private static class ModuleDelta {
        private final IModule[] module;
        private int delta = NO_CHANGE;
        
        public ModuleDelta(IModule[] module, int delta) {
            this.module = module;
            this.delta = delta;
        }
    }
    
    private static class ModuleDeltaList {       
        
        private ModuleDelta root;
        private List<ModuleDelta> children;
        
        public ModuleDeltaList(IModule rootModule) {
            this.root = new ModuleDelta(new IModule [] {rootModule}, NO_CHANGE);
            this.children = new ArrayList<ModuleDelta>();
        }

        public IModule[] getRootModule() {
            return root.module;
        }
        
        public int getEffectiveRootDelta() {
            if (root.delta == NO_CHANGE) {
                for (ModuleDelta child : children) {
                    if (child.delta == ADDED || child.delta == REMOVED || child.delta == CHANGED) {
                        return CHANGED;
                    }
                }
            }
            return root.delta;
        }
        
        public void setRootModuleDelta(int moduleDelta) {
            root.delta = moduleDelta;
        }
        
        public void addChildModule(IModule[] module, int moduleDelta) {
            children.add(new ModuleDelta(module, moduleDelta));
        }
        
        public List<ModuleDelta> getChildModules() {
            return children;
        }

        public boolean hasChangedChildModulesOnly() {
            if (root.delta == NO_CHANGE) {
                int changed = 0;
                for (ModuleDelta child : children) {
                    if (child.delta == ADDED || child.delta == REMOVED) {
                        return false;
                    } else if (child.delta == CHANGED) {
                        changed++;
                    }
                }
                return (changed > 0);          
            }
            return false;
        }
        
    }

    /*
     * This method is used to invoke DependencyHelper of different version
     */
    abstract protected List getOrderedModules(IServer server, List modules, List deltaKind);


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#publishModule(int,
     *      int, org.eclipse.wst.server.core.IModule[],
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public void publishModule(int kind, int deltaKind, IModule[] module, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishModule", publishKindToString(kind), deltaKindToString(deltaKind), Arrays.asList(module), monitor);

        try {
            //NO_CHANGE need if app is associated but not started and no delta
            if (deltaKind == NO_CHANGE && module.length == 1) {
                invokeCommand(deltaKind, module[0], monitor);
            }
            else if (deltaKind == CHANGED || deltaKind == ADDED || deltaKind == REMOVED) {
                invokeCommand(deltaKind, module[0], monitor);
            }
            setModuleStatus(module, null);
            setModulePublishState(module, IServer.PUBLISH_STATE_NONE);
        } 
        catch (CoreException e) {
            //
            // Set the parent module publish state to UNKNOWN so that WTP will display "Republish" instead
            // "Synchronized" for the server state, and set the module status to an error message 
            // for the GEP end-user to see. 
            //            
            setModuleStatus(module, new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error publishing module to server"));
            setModulePublishState(module, IServer.PUBLISH_STATE_UNKNOWN);
            setModuleState(module, IServer.STATE_UNKNOWN);
            throw e;
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishModule");
    }

    public Lock getPublishLock() {
        return publishLock;
    }
    
    @Override
    public void publishStart(IProgressMonitor monitor) throws CoreException {
        doPublishStart(monitor);
        try {
            if (!publishLock.tryLock(30, TimeUnit.SECONDS)) {
                throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Unable to obtain publish lock"));
            }
        } catch (InterruptedException e) {
            // ignore
        }
    }
    
    private void doPublishStart(IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishStart", monitor);
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishStart");
    }
    
    @Override
    public void publishFinish(IProgressMonitor monitor) throws CoreException {
        try {
            doPublishFinish(monitor);
        } finally {
            publishLock.unlock();
        }
    }
    
    private void doPublishFinish(IProgressMonitor monitor) throws CoreException  {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishFinish", monitor);

        IModule[] modules = this.getServer().getModules();
        boolean allpublished = true;
        for (int i = 0; i < modules.length; i++) {
            int state = getServer().getModulePublishState(new IModule[] { modules[i] });
            if (state != IServer.PUBLISH_STATE_NONE) {
                allpublished = false;
                break;
            }
        }
        if (allpublished) {           
            setServerPublishState(IServer.PUBLISH_STATE_NONE);
            setServerStatus(null);
        } else {
            setServerPublishState(IServer.PUBLISH_STATE_UNKNOWN);
            setServerStatus(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error publishing one or more modules to server"));
        }

        GeronimoConnectionFactory.getInstance().destroy(getServer());
        
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishFinish");
    }


    /**
     * Initializes the Geronimo server delegate. This method is called by the server core framework 
     * to give delegates a chance to do their own initialization. As such, the GEP proper should 
     * never call this method.
     * 
     * @param monitor a progress monitor, or <code>null</code> if progress reporting and cancellation 
     * are not desired
     */
    @Override
    protected void initialize(IProgressMonitor monitor) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.initialize", monitor);
               
        publishStateListener = new PublishStateListener();
        getServer().addServerListener(publishStateListener, ServerEvent.MODULE_CHANGE | ServerEvent.PUBLISH_STATE_CHANGE);
        
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.initialize");
    }

    /*
     * GERONIMODEVTOOLS-715: Update parent module publish state to "publish" if a child 
     * publish state was changed to "publish". This is because GEP right now is redeploying the
     * entire application instead of the individual bundle/module that has changed. Once that is
     * supported this listener can be removed. 
     */
    private class PublishStateListener implements IServerListener {
        public void serverChanged(ServerEvent event) {
            if (event.getPublishState() == IServer.PUBLISH_STATE_INCREMENTAL ||
                event.getPublishState() == IServer.PUBLISH_STATE_FULL) {  
                // reset server status in case it was set
                setServerStatus(null);
                
                IModule[] modules = event.getModule();
                if (modules.length > 1) {
                    if (getServer().getServerState() == IServer.STATE_STARTED) {
                        setModulePublishState(event.getModule(), IServer.PUBLISH_STATE_NONE);
                        setModuleStatus(event.getModule(), new Status(IStatus.OK, Activator.PLUGIN_ID, Messages.moduleModified));
                    } else {
                        setModulePublishState(event.getModule(), IServer.PUBLISH_STATE_UNKNOWN);
                        setModuleStatus(event.getModule(), null);
                    }

                    IModule[] newModules = new IModule[modules.length - 1];                    
                    System.arraycopy(modules, 0, newModules, 0, newModules.length);
                    
                    // update parent module publish state to "publish"
                    setModulePublishState(newModules, event.getPublishState());
                    // reset parent module status message
                    setModuleStatus(newModules, null);
                }
            }
            
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.ServerBehaviourDelegate#dispose()
     */
    public void dispose() {
        stopUpdateServerStateTask();
        stopSynchronizeProjectOnServerTask();
        if (publishStateListener != null) {
            getServer().removeServerListener(publishStateListener);
        }
    }

    public abstract String getRuntimeClass();

    public void setServerStarted() {
        setServerState(IServer.STATE_STARTED);
    }

    public void setServerStopped() {
        setServerState(IServer.STATE_STOPPED);
        resetModuleState();
    }

    public IGeronimoServer getGeronimoServer() {
        return (IGeronimoServer) getServer().loadAdapter(IGeronimoServer.class, null);
    }


    protected void terminate() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.terminate");

        if (getServer().getServerState() == IServer.STATE_STOPPED)
            return;

        try {
            setServerState(IServer.STATE_STOPPING);
            Trace.trace(Trace.INFO, "Killing the geronimo server process", Activator.traceCore); //$NON-NLS-1$
            if (process != null && !process.isTerminated()) {
                process.terminate();

            }
            stopImpl();
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error killing the geronimo server process", e, Activator.logCore); //$NON-NLS-1$
            // 
            // WTP does not allow a CoreException to be thrown in this case 
            // 
            throw new RuntimeException(Messages.STOP_FAIL);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.terminate");
    }

    protected void stopImpl() {
        if (process != null) {
            process = null;
            DebugPlugin.getDefault().removeDebugEventListener(processListener);
            processListener = null;
        }
        setServerState(IServer.STATE_STOPPED);
        resetModuleState();
    }

    private void resetModuleState() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.resetModuleState");

        IModule[] modules = getServer().getModules();
        for (int i = 0; i < modules.length; i++) {
            IModule[] module = new IModule[] { modules[i] };
            setModuleState(module, IServer.STATE_STOPPED);
        }        

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.resetModuleState");
    }
    
    protected void invokeCommand(int deltaKind, IModule module, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.invokeCommand", deltaKindToString(deltaKind), module.getName());
        
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader cl = getContextClassLoader();
            if (cl != null)
                Thread.currentThread().setContextClassLoader(cl);
            switch (deltaKind) {
            case ADDED: {
                doAdded(module, null, monitor);
                break;
            }
            case CHANGED: {
                doChanged(module, null, monitor);
                break;
            }
            case REMOVED: {
                doRemoved(module, monitor);
                break;
            }
            case NO_CHANGE: {
                doNoChange(module, monitor);
                break;
            }
            default:
                throw new IllegalArgumentException();
            }
        } catch (CoreException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.invokeCommand");
    }   

    /**
     * @param module
     * @param configId the forced configId to process this method, passed in when this method is invoked from doChanged()
     * @throws Exception
     */
    protected void doAdded(IModule module, String configId, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.doAdded", module.getName(), configId);
        
        configId = getLastKnowConfigurationId(module, configId);
        
        IStatus status;
        TargetModuleID[] ids;
        
        if (configId == null) {
            HashMap artifactsMap = ModuleArtifactMapper.getInstance().getServerArtifactsMap(getServer());
            if (artifactsMap != null) {
                synchronized (artifactsMap) {
                    status = distribute(module, monitor);
                    if (!status.isOK()) {
                        doFail(status, Messages.DISTRIBUTE_FAIL);
                    }

                    ids = updateServerModuleConfigIDMap(module, status);
                } 
            } else {             
                status = distribute(module, monitor);
                if (!status.isOK()) {
                    doFail(status, Messages.DISTRIBUTE_FAIL);
                }

                ids = updateServerModuleConfigIDMap(module, status);
            }

            status = start(ids, monitor);
            if (!status.isOK()) {
                doFail(status, Messages.START_FAIL);
            } else {
                // record the bundle project and bundle ID map to ModuleArtifactMapper since 
                // starting EBA module leads to the included OSGi bundle project being re-installed 
                // into geronimo server cache folder
                addServerModuleBundleIDMap(module);
                setModuleState(new IModule [] { module }, IServer.STATE_STARTED);
            }
        } else {
            //either (1) a configuration with the same module id exists already on the server
            //or (2) the module now has a different configId and the configuration on the server using
            //the old id as specified in the project-configId map should be uninstalled.
            doChanged(module, configId, monitor);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.doAdded");
    }
    /**
     * @param module
     * @param configId the forced configId to process this method, passed in when invoked from doAdded()
     * @throws Exception
     */
    protected void doChanged(IModule module, String configId, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.doChanged", module.getName(), configId);
        
        configId = getLastKnowConfigurationId(module, configId);
        if(configId != null) {
            String moduleConfigId = getConfigId(module);
            if(moduleConfigId.equals(configId)) {
                
                if (this.getServerDelegate().isNotRedeployJSPFiles()&&!this.isRemote() && GeronimoUtils.isWebModule(module)
                        && !module.isExternal()) {
                    // if only jsp files changed, no redeploy needed
                    if (findAndReplaceJspFiles(module, configId))
                        return;
                }
                
                IStatus status = reDeploy(module, monitor);
                if (!status.isOK()) {
                    doFail(status, Messages.REDEPLOY_FAIL);
                } else {
                    setModuleState(new IModule [] { module }, IServer.STATE_STARTED);
                    // record the bundle project and bundle ID map to ModuleArtifactMapper since 
                    // starting EBA module leads to the included OSGi bundle project being re-installed 
                    // into geronimo server cache folder
                    addServerModuleBundleIDMap(module);
                }
            } else {
                //different configIds from what needs to be undeployed to what will be deployed
                doRemoved(module, monitor);
                doAdded(module, null, monitor);
            }
        } else {
            //The checked configuration no longer exists on the server
            doAdded(module, configId, monitor);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.doChanged");
    }

    
    
    /*
     * This method is used to replace updated JSP files without deploy it. 
     */
    private boolean findAndReplaceJspFiles(IModule module, String configId)
            throws CoreException {
        IModule[] modules = { module };
        IModuleResourceDelta[] deltaArray = this
                .getPublishedResourceDelta(modules);

        // get repository position
        String ch = File.separator;
        String repositoryLocation = this.getRuntimeDelegate().getRuntime()
                .getLocation().toOSString()
                + ch + "repository" + ch;
        // Suppose directory structure of deployed module is
        // "repository/[groupID]/[artifactId]/[version]/[artifactId]-[version].[artifactType]"
        // configId contains the groupID,artifactId,version,artifactType
        String[] segments = configId.split("/");
        // groupId may contains "." as separator
        String groupId = null;
        if (segments[0]!=null)
            groupId=segments[0].replace(".", "/");
        String moduleTargetPath = repositoryLocation.concat(groupId)
                        .concat(ch).concat(segments[1]).concat(ch).concat(segments[2])
                        .concat(ch).concat(segments[1]).concat("-").concat(segments[2])
                        .concat(".").concat(segments[3]);

        List<IModuleResourceDelta> jspFiles = new ArrayList<IModuleResourceDelta>();
        for (IModuleResourceDelta delta : deltaArray) {
            List<IModuleResourceDelta> partJspFiles = DeploymentUtils.getAffectedJSPFiles(delta);
            // if not only Jsp files found, need to redeploy the module, so return false;
            if (partJspFiles == null)
                return false;
            else
                jspFiles.addAll(partJspFiles);
        }
        for (IModuleResourceDelta deltaModule : jspFiles) {
            IModuleFile moduleFile = (IModuleFile) deltaModule.getModuleResource();

                String target;
            String relativePath = moduleFile.getModuleRelativePath().toOSString();
            if (relativePath != null && relativePath.length() != 0) {
                target = moduleTargetPath.concat(ch).concat(relativePath).concat(ch).concat(moduleFile.getName());
            } else
                target = moduleTargetPath.concat(ch).concat(moduleFile.getName());

            File file = new File(target);
            switch (deltaModule.getKind()) {
            case IModuleResourceDelta.REMOVED:
                if (file.exists())
                    file.delete();
                break;
            case IModuleResourceDelta.ADDED:
            case IModuleResourceDelta.CHANGED:
                if (!file.exists())
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        Trace.trace(Trace.ERROR, "can't create file " + file, e, Activator.logCore);
                        throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "can't create file "
                                + file, e));
                    }

                String rootFolder = GeronimoUtils.getVirtualComponent(module).getRootFolder().getProjectRelativePath()
                        .toOSString();
                String sourceFile = module.getProject()
                        .getFile(rootFolder + ch + moduleFile.getModuleRelativePath() + ch + moduleFile.getName())
                        .getLocation().toString();
                try {

                    FileInputStream in = new FileInputStream(sourceFile);
                    FileOutputStream out = new FileOutputStream(file);
                    FileChannel inChannel = in.getChannel();
                    FileChannel outChannel = out.getChannel();
                    MappedByteBuffer mappedBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
                    outChannel.write(mappedBuffer);

                    inChannel.close();
                    outChannel.close();
                } catch (FileNotFoundException e) {
                    Trace.trace(Trace.ERROR, "can't find file " + sourceFile, e, Activator.logCore);
                    throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "can't find file "
                            + sourceFile, e));
                } catch (IOException e) {
                    Trace.trace(Trace.ERROR, "can't copy file " + sourceFile, e, Activator.logCore);
                    throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "can't copy file "
                            + sourceFile, e));
                    }
                    break;
                }
            }

        return true;

    }


    private String getLastKnowConfigurationId(IModule module, String configId) throws Exception {
        Trace.tracePoint("Entry ", Activator.traceCore, "GeronimoServerBehaviourDelegate.getLastKnowConfigurationId", module.getName(), configId);

        //use the correct configId, second from the .metadata, then from the plan
        configId = configId != null ? configId : DeploymentUtils.getLastKnownConfigurationId(module, getServer());

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.getLastKnowConfigurationId", configId);
        return configId;
    }

    protected void doRemoved(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.doRemoved", module.getName());

        HashMap artifactsMap = ModuleArtifactMapper.getInstance().getServerArtifactsMap(getServer());
        if (artifactsMap != null) {
            synchronized (artifactsMap) {
                _doRemove(module, monitor);
            }    
        } else {
            _doRemove(module, monitor);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.doRemoved");
    }
    
    private void _doRemove(IModule module, IProgressMonitor monitor) throws Exception {
        IStatus status = unDeploy(module, monitor);
        if (!status.isOK()) {
            doFail(status, Messages.UNDEPLOY_FAIL);
        }

        ModuleArtifactMapper.getInstance().removeArtifactBundleEntry(getServer(), module);
    }
    
    protected void doNoChange(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.doNoChange", module.getName());
        
        if(DeploymentUtils.getLastKnownConfigurationId(module, getServer()) != null) {
            IStatus status = start(module, monitor);
            if (status!=null && status.isOK()) {
                // record the bundle project and bundle ID map to ModuleArtifactMapper since 
                // starting EBA module leads to the included OSGi bundle project being re-installed 
                // into geronimo server cache folder
                addServerModuleBundleIDMap(module);
            }
        } else {
            doAdded(module, null, monitor);
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.doNoChange");
    }

    protected void doRestart(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.doRestart", module.getName());
        
        IStatus status = stop(module, monitor);
        if (!status.isOK()) {
            doFail(status, Messages.STOP_FAIL);
        }

        status = start(module, monitor);
        if (!status.isOK()) {
            doFail(status, Messages.START_FAIL);
        } else {
            // record the bundle project and bundle ID map to ModuleArtifactMapper since 
            // starting EBA module leads to the included OSGi bundle project being re-installed 
            // into geronimo server cache folder
            addServerModuleBundleIDMap(module);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.doRestart");
    }
    
    private TargetModuleID[] updateServerModuleConfigIDMap(IModule module, IStatus status) {
        TargetModuleID[] ids = ((DeploymentCmdStatus) status).getResultTargetModuleIDs();
        ModuleArtifactMapper mapper = ModuleArtifactMapper.getInstance();
        mapper.addArtifactEntry(getServer(), module, ids[0].getModuleID());
        return ids;
    }
    
    private void addServerModuleBundleIDMap(IModule module) {
        ModuleArtifactMapper mapper = ModuleArtifactMapper.getInstance();
        mapper.addBundleEntry(getServer(), module);
    }
    
    private void removeServerModuleBundleIDMap(IModule module) {
        ModuleArtifactMapper mapper = ModuleArtifactMapper.getInstance();
        mapper.removeBundleEntry(getServer(), module);
    }

    protected void doFail(IStatus status, String message) throws CoreException {
        MultiStatus ms = new MultiStatus(Activator.PLUGIN_ID, 0, message, null);
        ms.addAll(status);
        throw new CoreException(ms);
    }

    protected IStatus distribute(IModule module, IProgressMonitor monitor) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createDistributeCommand(module, getServer());
        return cmd.execute(monitor);
    }

    protected IStatus start(IModule module, IProgressMonitor monitor) throws Exception {
        TargetModuleID id = DeploymentUtils.getTargetModuleID(getServer(), module);
        IDeploymentCommand cmd = DeploymentCommandFactory.createStartCommand(new TargetModuleID[] { id }, module, getServer());
        IStatus status = cmd.execute(monitor);
        if (status.isOK()) {
            setModuleState(new IModule [] { module }, IServer.STATE_STARTED);
        }
        return status;
    }
    
    protected IStatus start(TargetModuleID[] ids, IProgressMonitor monitor) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createStartCommand(ids, null, getServer());
        return cmd.execute(monitor);
    }

    protected IStatus stop(IModule module, IProgressMonitor monitor) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createStopCommand(module, getServer());
        IStatus status = cmd.execute(monitor);
        if (status.isOK()) {
            setModuleState(new IModule [] { module }, IServer.STATE_STOPPED);
        }
        return status;
    }

    protected IStatus unDeploy(IModule module, IProgressMonitor monitor) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createUndeployCommand(module, getServer());
        return cmd.execute(monitor);
    }

    protected IStatus reDeploy(IModule module, IProgressMonitor monitor) throws Exception {
        IDeploymentCommand cmd = DeploymentCommandFactory.createRedeployCommand(module, getServer());
        return cmd.execute(monitor);
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
        
        String version = getServer().getRuntime().getRuntimeType().getVersion();
        
        if (version.startsWith("3")) {
            //get required jar file
            IPath libPath = getServer().getRuntime().getLocation().append("/lib");
            for (String jarFile: libPath.toFile().list()){
                IPath serverJar = libPath.append("/"+jarFile);
                cp.add(JavaRuntime.newArchiveRuntimeClasspathEntry(serverJar));
            }
            
        }else{
             //for 1.1,2.0,2.1,2.2 
             IPath serverJar = getServer().getRuntime().getLocation().append("/bin/server.jar");
             cp.add(JavaRuntime.newArchiveRuntimeClasspathEntry(serverJar));
        }
        // merge existing classpath with server classpath
        IRuntimeClasspathEntry[] existingCps = JavaRuntime.computeUnresolvedRuntimeClasspath(wc);

        for (int i = 0; i < existingCps.length; i++) {
            Trace.trace(Trace.INFO, "cpentry: " + cp , Activator.traceCore);
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
                    Trace.trace(Trace.INFO, "Classpath Container Entry: " + rcpe , Activator.traceCore);
                    if (cp.contains(rcpe) == false) {
                        cp.add( rcpe );
                    }
                }
            }
        }

        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, convertCPEntryToMemento(cp));
        wc.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
    }

    private List<String> convertCPEntryToMemento(List<IRuntimeClasspathEntry> cpEntryList) {
        List<String> list = new ArrayList<String>(cpEntryList.size());
        Iterator<IRuntimeClasspathEntry> iterator = cpEntryList.iterator();
        while (iterator.hasNext()) {
            IRuntimeClasspathEntry entry = iterator.next();
            try {
                list.add(entry.getMemento());
            } catch (CoreException e) {
                Trace.trace(Trace.ERROR, "Could not resolve classpath entry: "
                        + entry, e, Activator.logCore);
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
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.startPingThread");

        pingThread = new PingThread(this, getServer());
        pingThread.start();

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.startPingThread");
    }
    
    protected void stopPingThread() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.stopPingThread");

        if (pingThread != null) {
            pingThread.interrupt();
            pingThread = null;
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.stopPingThread");
    }
    
    protected abstract void stopKernel();

    public void startUpdateServerStateTask() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.startUpdateServerStateTask", getServer().getName());

        stateTimer = new Timer(true);
        stateTimer.schedule(new UpdateServerStateTask(this, getServer()), 0, TIMER_TASK_INTERVAL * 1000);

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.startUpdateServerStateTask");
    }
    
    public void startSynchronizeProjectOnServerTask() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.startSynchronizeProjectOnServerTask", getServer().getName());

        if (synchronizerTimer != null) {
            synchronizerTimer.cancel();
        }
        
        synchronizerTimer = new Timer(true);
        synchronizerTimer.schedule(new SynchronizeProjectOnServerTask(this, getServer()), 0, TIMER_TASK_INTERVAL * 1000);
        
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.startSynchronizeProjectOnServerTask");
    }

    public void stopUpdateServerStateTask() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stopUpdateServerStateTask", Activator.traceCore);

        if (stateTimer != null)
            stateTimer.cancel();

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.stopUpdateServerStateTask", Activator.traceCore);
    }
    
    public void stopSynchronizeProjectOnServerTask() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stopSynchronizeProjectOnServerTask", Activator.traceCore);
        
        if (synchronizerTimer != null)
            synchronizerTimer.cancel(); 

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.stopSynchronizeProjectOnServerTask", Activator.traceCore);
    }

    protected IPath getModulePath(IModule[] module, URL baseURL) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.getModulePath", Arrays.asList(module), baseURL);

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

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.getModulePath", modulePath);
        return modulePath;
    }

    public MBeanServerConnection getServerConnection() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String user = getGeronimoServer().getAdminID();
        String password = getGeronimoServer().getAdminPassword();
        String port = getGeronimoServer().getRMINamingPort();
        map.put("jmx.remote.credentials", new String[] { user, password });
        map.put("java.naming.factory.initial", "com.sun.jndi.rmi.registry.RegistryContextFactory");
        map.put("java.naming.factory.url.pkgs", "org.apache.geronimo.naming");
        map.put("java.naming.provider.url", "rmi://" + getServer().getHost()
                + ":" + port);

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
                Trace.trace(Trace.INFO, "Connected to kernel. " + url, Activator.traceCore);
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
    
    public static String publishKindToString(int kind) {
        switch(kind) {
        case IServer.PUBLISH_AUTO:
            return "Auto";
        case IServer.PUBLISH_CLEAN:
            return "Clean";
        case IServer.PUBLISH_FULL:
            return "Full";
        case IServer.PUBLISH_INCREMENTAL:
            return "Incremental";
        }
        return Integer.toString(kind);
    }
    
    public String getConfigId(IModule module) throws Exception {
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
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, e.getMessage(), e.getCause());
        }
        return Status.OK_STATUS;
    }
        
    @Override
    public void startModule(IModule[] module, IProgressMonitor monitor) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.startModule", Arrays.asList(module));
        try {
            IStatus status = start(module[0], monitor);
            if (status!=null && status.isOK()) {
                for (IModule m : module) {
                    // record the bundle project and bundle ID map to ModuleArtifactMapper since 
                    // starting EBA module leads to the included OSGi bundle project being re-installed 
                    // into geronimo server cache folder
                    addServerModuleBundleIDMap(m);
                }                
            }
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error starting module " + module[0].getName(), e, Activator.logCore);
            throw new RuntimeException("Error starting module " + module[0].getName(), e);
        }
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.startModule");
    }
    
    @Override
    public void stopModule(IModule[] module, IProgressMonitor monitor) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.stopModule", Arrays.asList(module));
        try {
            IStatus status = stop(module[0], monitor);
            // remove bundle project and bundle ID map from ModuleArtifactMapper if it is EBA module, 
            // since restarting EBA module will lead to the included OSGi bundle project being re-installed 
            // into geronimo server cache folder
            if (status != null && status.isOK()) {
                for (IModule m : module) {
                    removeServerModuleBundleIDMap(m);
                }                
            }
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error stopping module " + module[0].getName(), e, Activator.logCore);
            throw new RuntimeException("Error stopping module " + module[0].getName(), e);
        }
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.stopModule");
    }
    
    @Override
    public void restartModule(IModule[] module, IProgressMonitor monitor) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.restartModule", Arrays.asList(module));
        try {
            IStatus status = stop(module[0], monitor);            
            // remove bundle project and bundle ID map from ModuleArtifactMapper if it is EBA module, 
            // since restarting EBA module will lead to the included OSGi bundle project being re-installed 
            // into geronimo server cache folder
            if (status != null && status.isOK()) {
                for (IModule m : module) {
                    removeServerModuleBundleIDMap(m);
                }                
            }
            
            status = start(module[0], monitor);
            if (status!=null && status.isOK()) {
                for (IModule m : module) {
                    // record the bundle project and bundle ID map to ModuleArtifactMapper since 
                    // starting EBA module leads to the included OSGi bundle project being re-installed 
                    // into geronimo server cache folder
                    addServerModuleBundleIDMap(m);
                }                
            }
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error restarting module " + module[0].getName(), e, Activator.logCore);
            throw new RuntimeException("Error restarting module " + module[0].getName(), e);
        }
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.restartModule");
    }
    
    @Override
    public boolean canControlModule(IModule[] module) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.canControlModule", Arrays.asList(module));
        // Enable start/stop for top-level modules only 
        if (module.length == 1) {
            if (GeronimoUtils.isFragmentBundleModule(module[0])) {
                // fragment bundles cannot be started/stopped
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
     }
    
    public void setModulesState(IModule[] module, int state) {
        setModuleState(module, state);
    }
}
