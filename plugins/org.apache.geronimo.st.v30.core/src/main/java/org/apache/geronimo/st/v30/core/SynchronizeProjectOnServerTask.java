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

/**
 * @version $Rev: 817996 $ $Date: 2009-09-23 16:04:12 +0800 (Wed, 23 Sep 2009) $
 */
package org.apache.geronimo.st.v30.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.TreeSet;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;

import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.ModuleFactory;
import org.eclipse.wst.server.core.internal.ServerPlugin;

public class SynchronizeProjectOnServerTask extends TimerTask {

    private IGeronimoServerBehavior delegate;

    private IServer server;

    public SynchronizeProjectOnServerTask(IGeronimoServerBehavior delegate, IServer server) {
        super();
        this.delegate = delegate;
        this.server = server;
    }

    @Override
    public void run() {

        Trace.tracePoint("Entry ", "SynchronizeProjectOnServerTask.run");
        
        if (canUpdateState()) {            
           
            try {
                HashMap projectsOnServer = ModuleArtifactMapper.getInstance().getServerArtifactsMap(server);
                
                if(projectsOnServer!=null && projectsOnServer.size() != 0) {
                    
                    synchronized (projectsOnServer) {                        
                        Iterator projectsIterator = projectsOnServer.keySet().iterator();
                        TreeSet<String> removedConfigIds = new TreeSet<String>();
                        List<IProject> removedProjects = new ArrayList<IProject>();
                        List<IModule> removedModules = new ArrayList<IModule>();
                        
                        DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(server);
                        TargetModuleID[] ids = dm.getAvailableModules(null, dm.getTargets());
                        
                        for ( ; projectsIterator.hasNext(); ) {
                            String projectName = (String) projectsIterator.next();
                            String configID = (String) projectsOnServer.get(projectName);
                            if (!isInstalledModule(ids, configID)) {
                                removedConfigIds.add(configID);
                                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
                                removedProjects.add(project);
                                IModule[] modules = getModules(project);
                                for (IModule module : modules) {
                                    removedModules.add(module);
                                }
                            }
                        }
                        
                        if (removedConfigIds.size() != 0 && removedModules.size() != 0) {
                            IModule[] removedModules2 = new IModule[removedModules.size()];
                            removedModules.toArray(removedModules2);
                            removeModules(removedModules2, removedProjects);
                        } else {
                            Trace.trace(Trace.INFO, "SynchronizeProjectOnServerTask: no configuration is removed outside eclipse on server: " + this.server.getId());
                        }
                        
                    }                    
                    
                } else {
                    Trace.trace(Trace.INFO, "SynchronizeProjectOnServerTask: no project has been deployed on server: " + this.server.getId());
                }                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        Trace.tracePoint("Exist ", "SynchronizeProjectOnServerTask.run");
    }

    private boolean isInstalledModule(TargetModuleID[] ids, String configId) {
        
        if(ids == null) {           
            return false;
        }
        
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                if (ids[i].getModuleID().equals(configId)) {                    
                    return true;
                }
            }
        }
            
        return false;
    }
      
    private void removeModules(IModule[] remove, List<IProject> projects) {
        
        Trace.tracePoint("Entry ", "SynchronizeProjectOnServerTask.removeModules", remove, projects);
        
        IServerWorkingCopy wc = server.createWorkingCopy();
        IProgressMonitor monitor = new NullProgressMonitor(); 
        
        try {
            wc.modifyModules(null, remove, monitor);
            server = wc.save(true, monitor); 

            if (projects != null) {
                for (IProject project : projects) {
                    ModuleArtifactMapper.getInstance().removeEntry(this.server, project);
                }
            }           
        } catch (CoreException e) {
            Trace.trace(Trace.WARNING, "Could not remove module in SynchronizeProjectOnServerTask", e);
        }
        
        Trace.tracePoint("Exist ", "SynchronizeProjectOnServerTask.removeModules");
    }

    private IModule[] getModules(IProject project) {
        ModuleFactory[] factories = ServerPlugin.getModuleFactories();
        if (factories != null) {
            for (ModuleFactory factory : factories) {
                IModule[] modules = factory.getModules(project, new NullProgressMonitor());
                if (modules != null && modules.length != 0) {
                    return modules;
                }
            }
        }
        return new IModule [0];
    }
    
    private boolean canUpdateState() {
        
        Trace.tracePoint("Entry ", "SynchronizeProjectOnServerTask.canUpdateState");
        
        boolean flag = true;
        
        if (server.getServerState()!=IServer.STATE_STARTED) {
            flag = false;            
        }

        IGeronimoServer thisServer = (IGeronimoServer) this.server.loadAdapter(IGeronimoServer.class, null);
        IServer[] allServers = ServerCore.getServers();
        for (int i = 0; i < allServers.length; i++) {
            IServer server = allServers[i];
            IGeronimoServer gs = (IGeronimoServer) server.loadAdapter(IGeronimoServer.class, null);
            if (gs != null && !this.server.getId().equals(server.getId())) {
                if (isSameConnectionURL(gs, thisServer)) {
                    if (!isSameRuntimeLocation(server) && server.getServerState() != IServer.STATE_STOPPED) {
                        Trace.trace(Trace.WARNING, server.getId()
                                + " Cannot update server state.  URL conflict between multiple servers.");
                        flag = false;
                    }
                }
            }
        }
        
        Trace.tracePoint("Exist ", "SynchronizeProjectOnServerTask.canUpdateState", flag);      
        return flag;
    }

    private boolean isSameRuntimeLocation(IServer server) {
        return server.getRuntime().getLocation().equals(this.server.getRuntime().getLocation());
    }

    private boolean isSameConnectionURL(IGeronimoServer server, IGeronimoServer thisServer) {
        return server.getJMXServiceURL().equals(thisServer.getJMXServiceURL());
    }

}