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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.directory.NoSuchAttributeException;

import org.apache.geronimo.deployment.plugin.jmx.ExtendedDeploymentManager;
import org.apache.geronimo.deployment.plugin.jmx.JMXDeploymentManager;
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
import org.apache.geronimo.st.core.GeronimoJMXConnectorFactory;
import org.apache.geronimo.st.core.GeronimoJMXConnectorFactory.JMXConnectorInfo;
import org.apache.geronimo.st.v30.core.UpdateServerStateTask;
import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.internal.DependencyHelper;
import org.apache.geronimo.st.v30.core.internal.Messages;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.operations.ISharedLibEntryCreationDataModelProperties;
import org.apache.geronimo.st.v30.core.operations.SharedLibEntryCreationOperation;
import org.apache.geronimo.st.v30.core.operations.SharedLibEntryDataModelProvider;
import org.apache.geronimo.st.v30.core.osgi.AriesHelper;
import org.apache.geronimo.st.v30.core.osgi.OSGiModuleHandler;
import org.apache.geronimo.system.jmx.KernelDelegate;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.DefaultSourceContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.launching.RuntimeClasspathEntry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.internal.IModulePublishHelper;
import org.eclipse.wst.server.core.internal.ProgressUtil;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;
import org.eclipse.wst.server.core.util.PublishHelper;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServerBehaviourDelegate extends ServerBehaviourDelegate implements IGeronimoServerBehavior, IModulePublishHelper {

    public static final int TIMER_TASK_INTERVAL = 20;
    
    public static final int TIMER_TASK_DELAY = 20;

    private Kernel kernel = null;
    
    protected Timer stateTimer = null;
    
    protected SynchronizeProjectOnServerTask synchronizerTask = null;

    protected PingThread pingThread;

    protected transient IDebugEventSetListener processListener;

    public static final String ERROR_SETUP_LAUNCH_CONFIGURATION = "errorInSetupLaunchConfiguration";

    private PublishStateListener publishStateListener;
    
    private Set<IProject> knownSourceProjects = null; 
    
    private DefaultModuleHandler defaultModuleHandler;
    private OSGiModuleHandler osgiModuleHandler;

    protected ClassLoader getContextClassLoader() {
        return Kernel.class.getClassLoader();
    }
    
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
    public synchronized void stop(final boolean force) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.stop", force);

        IServer server = getServer();
                
        if (server.getServerState() != IServer.STATE_STOPPED) {
            setServerState(IServer.STATE_STOPPING);
        }
        
        // stop threads
        stopPingThread();
        stopSynchronizeProjectOnServerTask();
        
        // request shutdown
        stopKernel();
        
        // wait for shutdown
        if (!waitForStopped(60 * 1000) || force) {
            ILaunch launch = server.getLaunch();
            if (launch != null) {
                Trace.trace(Trace.INFO, "Killing the geronimo server process", Activator.traceCore); //$NON-NLS-1$
                try {
                    launch.terminate();
                } catch (Exception e) {
                    Trace.trace(Trace.ERROR, "Error killing the geronimo server process", e, Activator.logCore); //$NON-NLS-1$
                }
            }
        }
        
        GeronimoConnectionFactory.getInstance().destroy(server);
        
        stopImpl(); 
                                                           
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.stop");
    }

    private boolean waitForStopped(long timeout) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.waitForStopped", timeout);
        
        long started = System.currentTimeMillis();
        boolean stopped = false;
        try {
            while (System.currentTimeMillis() - started < timeout) {
                if (isKernelAlive()) {
                    Thread.sleep(500);
                } else {
                    stopped = true;
                    break;
                }
            }
        } catch (InterruptedException e) {
            // ignore
        }
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.waitForStopped", stopped);
        return stopped;
    }
    
    private void setStatus(IModule[] module, IStatus status, MultiStatus multiStatus) {
        if (status.isOK()) {
            setModulePublishState(module, IServer.PUBLISH_STATE_NONE);
            setModuleStatus(module, null);
        } else {
            multiStatus.add(status);
            setModuleStatus(module, status);
            setModulePublishState(module, IServer.PUBLISH_STATE_UNKNOWN);
        }
    }
    
    void setKnownSourceProjects(Set<IProject> knownSourceProjects) {
        this.knownSourceProjects = knownSourceProjects;
    }
    
    boolean hasKnownSourceProject(List<IModule[]> moduleList) {
        if (knownSourceProjects != null) {
            for (IModule[] modules : moduleList) {
                for (IModule module : modules) {
                    IProject project = module.getProject();
                    if (project != null && !knownSourceProjects.contains(project)) {
                        Trace.trace(Trace.INFO, "Project " + project.getName() + " is not source lookup list.", Activator.traceCore); //$NON-NLS-1$
                        return false;
                    }
                }
            }            
        }
        return true;
    }
    
    void resetSourceLookupList() {
        Trace.trace(Trace.INFO, "Resetting source lookup list.", Activator.traceCore); //$NON-NLS-1$
        
        // reset DefaultSourceContainer - that will force Eclipse to re-compute the source paths
        AbstractSourceLookupDirector locator = (AbstractSourceLookupDirector) getServer().getLaunch().getSourceLocator();
        ISourceContainer[] oldContainers = locator.getSourceContainers();
        ISourceContainer[] newContainers = new ISourceContainer[oldContainers.length];
        System.arraycopy(oldContainers, 0, newContainers, 0, oldContainers.length);
        DefaultSourceContainer newDefaultContainer = new DefaultSourceContainer();
        for (int i = 0; i < newContainers.length; i++) {
            if (newDefaultContainer.getType().equals(newContainers[i].getType())) {
                newContainers[i] = newDefaultContainer;
                break;
            }
        }
        locator.setSourceContainers(newContainers);
        
        // reset knownSourceProjects as they will be set once Eclipse re-computes the source paths
        knownSourceProjects = null;
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
        Map<String, ModuleDeltaList> publishMap = new LinkedHashMap<String, ModuleDeltaList>();
        for (int i = 0; i < modules.size(); i++) {
            IModule[] module = (IModule[]) modules.get(i);
            Integer moduleDeltaKind = (Integer) deltaKind.get(i);
            IModule rootModule = module[0];
            
            ModuleDeltaList list = publishMap.get(rootModule.getId());
            if (list == null) {
                list = new ModuleDeltaList(rootModule);
                publishMap.put(rootModule.getId(), list);
            }
            
            if (module.length == 1) {
                list.setRootModuleDelta(moduleDeltaKind.intValue());
            } else {
                list.addChildModule(module, moduleDeltaKind.intValue());
            }           
        }
        
        // Reset source code lookup list - see GERONIMODEVTOOLS-763 for details.
        if (ILaunchManager.DEBUG_MODE.equals(getServer().getMode()) && !hasKnownSourceProject(modules)) {
            resetSourceLookupList();
        }
        
        if(status.isOK()) {
            if (modules == null)
                return;
            
            int size = modules.size();
            if (size == 0)
                return;
            
            if (monitor.isCanceled())
                return;
            
            // phase 1: see if the modified contents can copied/replaced 
            if (getServerDelegate().isNoRedeploy() && !isRemote()) {
                Iterator<ModuleDeltaList> iterator = publishMap.values().iterator();
                while (iterator.hasNext()) {
                    ModuleDeltaList moduleList = iterator.next();
                    IModule[] rootModule = moduleList.getRootModule();
                    if (GeronimoUtils.isEBAModule(rootModule[0]) || GeronimoUtils.isEarModule(rootModule[0])) {
                        if (moduleList.hasChangedChildModulesOnly(true)) {
                            boolean replacementPossible = true;
                            Map<IModule[], IStatus> statusMap = new HashMap<IModule[], IStatus>();   
                            
                            for (ModuleDelta moduleDelta : moduleList.getChildModules()) {
                                IModule bundleModule = moduleDelta.module[1];
                                if (moduleDelta.delta == CHANGED && (GeronimoUtils.isWebModule(bundleModule) || GeronimoUtils.isBundleModule(bundleModule))) {
                                    // try to do replacement
                                    status = tryFileReplace(moduleDelta.module);
                                    if (status == null) {
                                        // replacement was not possible
                                        replacementPossible = false;
                                        break;
                                    } else {
                                        statusMap.put(moduleDelta.module, status);
                                    }
                                } else {
                                    statusMap.put(moduleDelta.module, Status.OK_STATUS);
                                }
                            }
                            
                            if (replacementPossible) {
                                // replacement was possible for all changed child modules - remove it from publish list
                                iterator.remove();
                                
                                statusMap.put(rootModule, Status.OK_STATUS);
                                for (Map.Entry<IModule[], IStatus> entry : statusMap.entrySet()) {
                                    setStatus(entry.getKey(), entry.getValue(), multi);
                                }
                            } else {
                                // replacement was not possible for at least one child module - redeploy the module
                            }                            
                        }
                    } else if (GeronimoUtils.isWebModule(rootModule[0]) || GeronimoUtils.isBundleModule(rootModule[0])) {
                        if (moduleList.getEffectiveRootDelta() == CHANGED) {
                            // contents changed - try to do replacement
                            status = tryFileReplace(rootModule);
                            if (status != null) {
                                // replacement was possible - remove it from publish list
                                iterator.remove();

                                setStatus(rootModule, status, multi);
                            } else {
                                // replacement was not possible - redeploy the module
                            }
                        }
                    }                                          
                }
            }   
                    
            // phase 2: re-deploy the modules
            boolean refreshOSGiBundle = getServerDelegate().isRefreshOSGiBundle();
            for (ModuleDeltaList moduleList : publishMap.values()) {  
                IModule[] rootModule = moduleList.getRootModule();
                if (refreshOSGiBundle && GeronimoUtils.isEBAModule(rootModule[0]) && moduleList.hasChangedChildModulesOnly(false)) {
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
        
    private IStatus refreshBundles(IModule ebaModule, List<IModule[]> bundleModules, IProgressMonitor monitor) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundles", ebaModule, bundleModules, monitor);

        String configId = null;
        try {
            configId = DeploymentUtils.getConfigId(getServer(), ebaModule);
        } catch (CoreException e) {
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.bind(Messages.REFRESH_NO_CONFIGURATION_FAIL, ebaModule.getProject().getName()), e);
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
                setStatus(bundleModule, status, multiStatus);
            }
        } catch (Exception e) {
            multiStatus.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.REFRESH_FAIL, e));
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
            String symbolicName = AriesHelper.getSymbolicName(bundleModule);
            Long bundleId = bundleMap.get(symbolicName);
            
            if (bundleId == null) {
                return new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.bind(Messages.REFRESH_NO_BUNDLE_FAIL,
                        new String[] {bundleModule.getProject().getName(), ebaModule.getProject().getName()}));
            }
            
            ExtendedDeploymentManager dm = (ExtendedDeploymentManager) DeploymentCommandFactory.getDeploymentManager(getServer());
            /*
             * Try class hot swap first and if it fails fallback to regular bundle update.
             */
            if (!refreshBundleClasses(dm, ebaModule, bundleModule, ebaName, bundleId)) {
                File file = DeploymentUtils.getTargetFile(getServer(), bundleModule);
                dm.updateEBAContent(ebaName, bundleId, file);
            }
        } catch (Exception e) {
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.REFRESH_FAIL, e);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundle");

        return Status.OK_STATUS;
    }

    private boolean refreshBundleClasses(ExtendedDeploymentManager dm, IModule ebaModule, IModule bundleModule, AbstractName ebaName, long bundleId) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundleClasses", ebaModule, bundleModule, ebaName, bundleId);    
        // check if class hot swap is supported
        if (!dm.isRedefineClassesSupported()) {
            Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundleClasses", "Class redefinition is not supported");        
            return false;
        }
        // ensure only classes have changed
        IModuleResourceDelta[] delta = getPublishedResourceDelta(new IModule[] { ebaModule, bundleModule });
        IModuleResource[] classResources = DeploymentUtils.getChangedClassResources(delta);
        if (classResources == null) {
            Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundleClasses", "Non-class resource modifications found");
            return false;
        }
        // create temp. zip with the changes
        File changeSetFile = DeploymentUtils.createChangeSetFile(classResources);
        if (changeSetFile == null) {
            Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundleClasses", "Error creating file with resource modifications");
            return false;
        }
        // get document base for the module if it is expanded
        String documentBase = getServerDelegate().isNoRedeploy() ? getWebModuleDocumentBase(bundleModule) : null;
        // see if the classes can be hot swapped - update archive if module is not expanded
        if (!dm.hotSwapEBAContent(ebaName, bundleId, changeSetFile, documentBase == null)) {
            Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundleClasses", "Bundle class hot swap cannot be preformed");
            changeSetFile.delete();
            return false;
        } else {
            changeSetFile.delete();
        }
        if (documentBase != null) {
            PublishHelper publishHelper = new PublishHelper(getTempDirectory().toFile());   
            IStatus[] statusArray = publishHelper.publishFull(classResources, new Path(documentBase), null);
            if (statusArray != null) {
                // XXX: in case of an error should we return false to force full re-deploy?
                for (IStatus status : statusArray) {
                    if (!status.isOK()) {
                        Trace.trace(Trace.WARNING, "Error publishing changes: " + status.getMessage(), status.getException(), Activator.traceCore);
                    }
                }
            }
        }
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.refreshBundleClasses", "Bundle class hot swap was succesfully preformed", documentBase);
        return true;
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

        public boolean hasChangedChildModulesOnly(boolean allChangedAllowed) {
            int changed = getChangedChildModulesOnly();
            if (changed > 0) {
                if (allChangedAllowed) {
                    return true;
                } else {                    
                    if (children.size() == 1) {                
                        // special case: always return true if module only has one child module                        
                        return true;
                    } else {
                        return (changed < children.size());
                    }
                }
            }
            return false;
        }
        
        /*
         * Returns number of "changed" child modules. 
         * Returns -1 if a single "added" or "removed" child module is found or a root module is modified.
         */
        public int getChangedChildModulesOnly() {
            if (root.delta == NO_CHANGE) {
                int changed = 0;
                for (ModuleDelta child : children) {
                    if (child.delta == ADDED || child.delta == REMOVED) {
                        return -1;
                    } else if (child.delta == CHANGED) {
                        changed++;
                    }
                }
                return changed;          
            }
            return -1;
        }
    }

    /*
     * This method is used to invoke DependencyHelper of different version
     */
    protected List getOrderedModules(IServer server, List modules, List deltaKind) {
        DependencyHelper dh = new DependencyHelper();
        List list = dh.reorderModules(this.getServer(),modules, deltaKind);
        return list;
   }

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
    
    @Override
    public void publishStart(IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishStart", monitor);
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.publishStart");
    }
    
    @Override
    public void publishFinish(IProgressMonitor monitor) throws CoreException {
        doPublishFinish(monitor);
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
        
        defaultModuleHandler = new DefaultModuleHandler(this);
        osgiModuleHandler = new OSGiModuleHandler(this);
        
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
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.dispose");
        stopUpdateServerStateTask();
        stopSynchronizeProjectOnServerTask();
        if (publishStateListener != null) {
            getServer().removeServerListener(publishStateListener);
        }
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.dispose");
    }

    public String getRuntimeClass() {
        return "org.apache.geronimo.cli.daemon.DaemonCLI";
    }

    public void setServerStarted() {
        setServerState(IServer.STATE_STARTED);
        startSynchronizeProjectOnServerTask();
    }

    public void setServerStopped() {
        setServerState(IServer.STATE_STOPPED);
        resetModuleState();
        stopSynchronizeProjectOnServerTask();
        if (defaultModuleHandler != null) {
            defaultModuleHandler.serverStopped();
        }
        if (osgiModuleHandler != null) {
            osgiModuleHandler.serverStopped();
        }
        GeronimoConnectionFactory.getInstance().destroy(getServer());
    }

    public IGeronimoServer getGeronimoServer() {
        return (IGeronimoServer) getServer().loadAdapter(IGeronimoServer.class, null);
    }

    protected void stopImpl() {
        if (processListener != null) {
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
    
    protected AbstractModuleHandler getModuleHandler(IModule module) {
        return (GeronimoUtils.isBundleModule(module) || GeronimoUtils.isFragmentBundleModule(module)) ? osgiModuleHandler : defaultModuleHandler;
    }
    
    protected void invokeCommand(int deltaKind, IModule module, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.invokeCommand", deltaKindToString(deltaKind), module.getName());
        AbstractModuleHandler moduleHandler = getModuleHandler(module);
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader cl = getContextClassLoader();
            if (cl != null)
                Thread.currentThread().setContextClassLoader(cl);
            switch (deltaKind) {
            case ADDED: {
                moduleHandler.doAdded(module, monitor);
                break;
            }
            case CHANGED: {
                moduleHandler.doChanged(module, monitor);
                break;
            }
            case REMOVED: {
                moduleHandler.doRemoved(module, monitor);
                break;
            }
            case NO_CHANGE: {
                moduleHandler.doNoChange(module, monitor);
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

    private String getWebModuleDocumentBase(IModule webModule) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.getWebModuleDocumentBase", webModule);
        
        if (webModule.isExternal()) {
            Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.getWebModuleDocumentBase", "External module");
            return null;
        }
                
        String contextPath = getServerDelegate().getContextPath(webModule);
        if (contextPath == null) {
            Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.getWebModuleDocumentBase", "Context path is null");
            return null;
        }

        String documentBase = getWebModuleDocumentBase(contextPath);
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.getWebModuleDocumentBase", contextPath, documentBase);
        return documentBase;        
    }
    
    private IStatus tryFileReplace(IModule[] module) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.tryFileReplace", module.toString());
        
        IModule webModule = module[module.length - 1];        
        String documentBase = getWebModuleDocumentBase(webModule);
        if (documentBase == null ) {
            Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.tryFileReplace", "Document base is null");
            return null;
        }

        List<IModuleResourceDelta> modifiedFiles = findModifiedFiles(module);
        if (modifiedFiles == null) {
            Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.tryFileReplace", "Some modified files cannot be replaced");
            return null;
        }
        Trace.trace(Trace.INFO, "Modified files: " + modifiedFiles, Activator.logCore);

        IStatus status = findAndReplaceFiles(webModule, modifiedFiles, documentBase);
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.tryFileReplace", status);
        return status;
    }
        
    private List<IModuleResourceDelta> findModifiedFiles(IModule[] module) {
        IModuleResourceDelta[] deltaArray = getPublishedResourceDelta(module);

        GeronimoServerDelegate delegate = getServerDelegate();
        List<String> includes = delegate.getNoRedeployFilePatternsAsList(true);
        List<String> excludes = delegate.getNoRedeployFilePatternsAsList(false);
        
        List<IModuleResourceDelta> modifiedFiles = new ArrayList<IModuleResourceDelta>();
        for (IModuleResourceDelta delta : deltaArray) {
            List<IModuleResourceDelta> files = DeploymentUtils.getAffectedFiles(delta, includes, excludes);
            // if null then some other files were changed that we cannot just copy/replace.
            if (files == null) {
                return null;
            } else {
                modifiedFiles.addAll(files);
            }
        }
        return modifiedFiles;
    }
    
    /*
     * This method is used to replace updated files without redeploying the entire module. 
     */
    private IStatus findAndReplaceFiles(IModule module, List<IModuleResourceDelta> modifiedFiles, String documentBase) {
        Trace.trace(Trace.INFO, "Replacing updated files for " + module.getName() + " module.", Activator.logCore);
        
        String ch = File.separator;
        byte[] buffer = new byte[10 * 1024];
        int bytesRead;
        
        for (IModuleResourceDelta deltaModule : modifiedFiles) {
            IModuleFile moduleFile = (IModuleFile) deltaModule.getModuleResource();

            StringBuilder target = new StringBuilder(documentBase);
            target.append(ch);
            String relativePath = moduleFile.getModuleRelativePath().toOSString();
            if (relativePath != null && relativePath.length() != 0) {
                target.append(relativePath);
                target.append(ch);
            } 
            target.append(moduleFile.getName());
            
            File file = new File(target.toString());
            if(! file.isAbsolute()) {
                file = getServerResource(IGeronimoServerBehavior.VAR_CATALINA_DIR + target.toString()).toFile();
            }
            
            switch (deltaModule.getKind()) {
            case IModuleResourceDelta.REMOVED:
                if (file.exists()) {
                    file.delete();
                }
                break;
            case IModuleResourceDelta.ADDED:
            case IModuleResourceDelta.CHANGED:
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    if (!parentFile.mkdirs()) {
                        Trace.trace(Trace.ERROR, "Cannot create target directory: " + parentFile, Activator.logCore);
                        return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot create target directory", null);
                    }
                }
                String rootFolder = GeronimoUtils.getVirtualComponent(module).getRootFolder().getProjectRelativePath().toOSString();
                String sourceFile = module.getProject().getFile(rootFolder + ch + moduleFile.getModuleRelativePath() + ch + moduleFile.getName()).getLocation().toString();
                
                FileInputStream in = null;
                FileOutputStream out = null;
                try {
                    in = new FileInputStream(sourceFile);
                    out = new FileOutputStream(file);
                    
                    while ((bytesRead = in.read(buffer)) > 0) {
                        out.write(buffer, 0, bytesRead);
                    }
                } catch (FileNotFoundException e) {
                    Trace.trace(Trace.ERROR, "Cannot find file to copy: " + sourceFile, e, Activator.logCore);
                    return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot find file " + sourceFile, e);
                } catch (IOException e) {
                    Trace.trace(Trace.ERROR, "Cannot copy file: " + sourceFile, e, Activator.logCore);
                    return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot copy file " + sourceFile, e);
                } finally {
                    if (in != null) {
                        try { in.close(); } catch (IOException ignore) {}
                    }
                    if (out != null) {
                        try { out.close(); } catch (IOException ignore) {}
                    }
                }
                break;
            }                    
        }

        return Status.OK_STATUS;
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
        if (newProcess == null) {
            return;
        }

        if (processListener != null) {
            DebugPlugin.getDefault().removeDebugEventListener(processListener);
        }
        
        processListener = new IDebugEventSetListener() {
            public void handleDebugEvents(DebugEvent[] events) {
                if (events != null) {
                    int size = events.length;
                    for (int i = 0; i < size; i++) {
                        if (newProcess.equals(events[i].getSource()) && events[i].getKind() == DebugEvent.TERMINATE) {
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
    
    protected Kernel getKernel() throws SecurityException {
        if (kernel == null) {
            try {
                MBeanServerConnection connection = getServerConnection();
                if (connection != null)
                    kernel = new KernelDelegate(connection);
            } catch (SecurityException e) {
                throw e;
            } catch (Exception e) {
                Trace.trace(Trace.INFO, "Kernel connection failed. "
                        + e.getMessage(), Activator.traceCore);
            }
        }
        return kernel;
    }
    
    private void stopKernel() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.stopKernel");
        try {
            MBeanServerConnection connection = getServerConnection();
            connection.invoke(getFrameworkMBean(connection), "stopBundle",
                     new Object[] { 0 }, new String[] { long.class.getName() });
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error while requesting server shutdown", e, Activator.traceCore);
        }
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.stopKernel");
    }

    public boolean isKernelAlive() {
        try {
            return getKernel() != null && kernel.isRunning();
        } catch (SecurityException e) {
            Trace.trace(Trace.ERROR, "Invalid username and/or password.", e, Activator.logCore);

            pingThread.interrupt();
            if (getServer().getServerState() != IServer.STATE_STOPPED) {
                forceStopJob(true,e);
            }
        } catch (Exception e) {
            Trace.trace(Trace.WARNING, "Geronimo Server may have been terminated manually outside of workspace.", e, Activator.logCore);
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

        new Job(jobName) {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                MultiStatus multiStatus = new  MultiStatus(Activator.PLUGIN_ID, 0, jobStartingName, null);
                multiStatus.add(new Status(IStatus.ERROR,Activator.PLUGIN_ID,0,"Invalid username and/or password.",e));
                try {
                    stop(true);
                } catch (Exception e){
                    multiStatus.add(new Status(IStatus.ERROR,Activator.PLUGIN_ID,0,"Failed to stop server",e));
                }
            
                return multiStatus;
            }
        }.schedule();
        
    }
    
    public boolean isFullyStarted() {
        if (isKernelAlive()) {
            AbstractNameQuery query = new AbstractNameQuery(PersistentConfigurationList.class.getName());
            Set<AbstractName> configLists = kernel.listGBeans(query);configLists.toArray()[0].getClass().getClassLoader();
            if (!configLists.isEmpty()) {kernel.getClass().getClassLoader();
                AbstractName on = (AbstractName) configLists.toArray()[0];AbstractName.class.getClassLoader();
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
                Trace.trace(Trace.INFO, "configLists is empty", Activator.traceCore);
            }
        }
        return false;
    }
    
    
    @Override
    public IPath getServerResource(String path) {
        IPath serverRoot = getServer().getRuntime().getLocation();
        return serverRoot.append(path);
    }

    public void startUpdateServerStateTask() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.startUpdateServerStateTask", getServer().getName());

        stateTimer = new Timer(true);
        stateTimer.schedule(new UpdateServerStateTask(this, getServer()), 0, TIMER_TASK_INTERVAL * 1000);

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.startUpdateServerStateTask");
    }
    
    public void startSynchronizeProjectOnServerTask() {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.startSynchronizeProjectOnServerTask", getServer().getName());

        if (synchronizerTask != null) {
            synchronizerTask.stop();
        }
        
        synchronizerTask = new SynchronizeProjectOnServerTask(this, getServer());
        synchronizerTask.start();
        
        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.startSynchronizeProjectOnServerTask");
    }

    public void stopUpdateServerStateTask() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stopUpdateServerStateTask", Activator.traceCore);

        if (stateTimer != null) {
            stateTimer.cancel();
            stateTimer = null;
        }

        Trace.tracePoint("Exit ", "GeronimoServerBehaviourDelegate.stopUpdateServerStateTask", Activator.traceCore);
    }
    
    public void stopSynchronizeProjectOnServerTask() {
        Trace.tracePoint("Entry", "GeronimoServerBehaviourDelegate.stopSynchronizeProjectOnServerTask", Activator.traceCore);
        
        if (synchronizerTask != null) {
            synchronizerTask.stop();
            synchronizerTask = null;
        }

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

    private Kernel getDeploymentManagerKernel() {
        GeronimoConnectionFactory connectionFactory = GeronimoConnectionFactory.getInstance();
        try {
            JMXDeploymentManager manager =
                (JMXDeploymentManager) connectionFactory.getDeploymentManager(getServer());
            return manager.getKernel();
        } catch (DeploymentManagerCreationException e) {
            Trace.trace(Trace.WARNING, "Error getting kernel from deployment manager", e, Activator.logCore);
            return null;
        }
    }
    
    public MBeanServerConnection getServerConnection() throws Exception {
        String host = getServer().getHost();
        String user = getGeronimoServer().getAdminID();
        String password = getGeronimoServer().getAdminPassword();
        String port = getGeronimoServer().getRMINamingPort();
        JMXConnectorInfo connectorInfo = new JMXConnectorInfo(user, password, host, port);
        
        // Using the classloader that loads the current's class as the default classloader when creating the JMXConnector
        JMXConnector jmxConnector = GeronimoJMXConnectorFactory.create(connectorInfo, this.getClass().getClassLoader());

        return jmxConnector.getMBeanServerConnection();
    }
    
    public ObjectName getMBean(MBeanServerConnection connection, String mbeanName, String name) throws Exception {
        Set<ObjectName> objectNameSet =
            connection.queryNames(new ObjectName(mbeanName), null);
        if (objectNameSet.isEmpty()) {
            throw new Exception(Messages.bind(Messages.mBeanNotFound, name));
        } else if (objectNameSet.size() == 1) {
            return objectNameSet.iterator().next();
        } else {
            throw new Exception(Messages.bind(Messages.multipleMBeansFound, name));
        }
    }
    
    private ObjectName getFrameworkMBean(MBeanServerConnection connection) throws Exception {
        return getMBean(connection, "osgi.core:type=framework,*", "Framework");
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
            getModuleHandler(module[0]).doStartModule(module, monitor);
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
            getModuleHandler(module[0]).doStopModule(module, monitor);
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
            getModuleHandler(module[0]).doRestartModule(module, monitor);
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
    
    // TODO: this can be cached 
    public String getWebModuleDocumentBase(String contextPath) {
        Kernel kernel = getDeploymentManagerKernel();
        if (kernel == null) {
            Trace.trace(Trace.WARNING, "Error getting web module document base - no kernel", null, Activator.logCore);
            return null;
        }
        Map<String, String> map = Collections.singletonMap("j2eeType", "WebModule");
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        AbstractNameQuery query = new AbstractNameQuery(null, map, Collections.EMPTY_SET);
        Set<AbstractName> webModuleNames = kernel.listGBeans(query);
        for (AbstractName name : webModuleNames) {
            try {
                String moduleContextPath = (String) kernel.getAttribute(name, "contextPath");
                if (contextPath.equals(moduleContextPath)) {
                    String docBase = (String) kernel.getAttribute(name, "docBase");
                    return (docBase != null && docBase.length() > 0) ? docBase : null;
                }
            } catch (GBeanNotFoundException e) {
                // ignore
            } catch (NoSuchAttributeException e) {
                // ignore
            } catch (Exception e) {
                Trace.trace(Trace.WARNING, "Error getting web module document base", e, Activator.logCore);
            }
        }
        return null;
    }

    public OSGiModuleHandler getOsgiModuleHandler() {
        return osgiModuleHandler;
    }
    
    public boolean isPublished(IModule[] module) {
        return super.hasBeenPublished(module);
    }
}
