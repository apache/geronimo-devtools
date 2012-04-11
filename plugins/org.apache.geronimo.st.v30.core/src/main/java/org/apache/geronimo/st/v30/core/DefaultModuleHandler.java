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

import java.util.Arrays;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;

import org.apache.geronimo.deployment.plugin.jmx.ExtendedDeploymentManager;
import org.apache.geronimo.kernel.management.State;
import org.apache.geronimo.st.v30.core.base.Artifact;
import org.apache.geronimo.st.v30.core.base.ModuleSet;
import org.apache.geronimo.st.v30.core.commands.DeploymentCmdStatus;
import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.commands.IDeploymentCommand;
import org.apache.geronimo.st.v30.core.internal.Messages;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

public class DefaultModuleHandler extends AbstractModuleHandler {
    
    private ModuleArtifactMapper mapper;
    
    public DefaultModuleHandler(GeronimoServerBehaviourDelegate serverDelegate) {
        super(serverDelegate);
        mapper = ModuleArtifactMapper.getInstance();
    }

    public void doAdded(IModule module, IProgressMonitor monitor) throws Exception {
        doAdded(module, null, monitor);
    }
    
    public void doAdded(IModule module, String configId, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DefaultModuleHandler.doAdded", module.getName(), configId);
        
        configId = getLastKnowConfigurationId(module, configId);
        if (configId == null) {
            IStatus status;
            TargetModuleID[] ids;
            
            ModuleSet<Artifact> artifacts = mapper.getServerArtifacts(getServer());
            if (artifacts != null) {
                synchronized (artifacts) {
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
                setModuleState(new IModule [] { module }, IServer.STATE_STARTED);
            }
        } else {
            //either (1) a configuration with the same module id exists already on the server
            //or (2) the module now has a different configId and the configuration on the server using
            //the old id as specified in the project-configId map should be uninstalled.
            doChanged(module, configId, monitor);
        }
        
        Trace.tracePoint("Exit", Activator.traceCore, "DefaultModuleHandler.doAdded");
    }
    
    public void doChanged(IModule module, IProgressMonitor monitor) throws Exception {
        doChanged(module, null, monitor);
    }
    
    public void doChanged(IModule module, String configId, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DefaultModuleHandler.doChanged", module.getName(), configId);
        
        configId = getLastKnowConfigurationId(module, configId);
        if(configId != null) {
            String moduleConfigId = getConfigId(module);
            if (moduleConfigId.equals(configId)) {
                IStatus status = reDeploy(module, monitor);                
                if (!status.isOK()) {
                    doFail(status, Messages.REDEPLOY_FAIL);
                } else {
                    setModuleState(new IModule [] { module }, IServer.STATE_STARTED);
                    mapper.addArtifactEntry(getServer(), module, moduleConfigId);
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
        
        Trace.tracePoint("Exit", Activator.traceCore, "DefaultModuleHandler.doChanged");
    }
    
    public void doNoChange(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DefaultModuleHandler.doNoChange", module.getName());
        
        DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(getServer());
        String configId = DeploymentUtils.getLastKnownConfigurationId(module, getServer());
        if (configId != null) {
            IModule[] rootModule = new IModule[] { module };
            int state = doGetModuleState( (ExtendedDeploymentManager) dm, configId);
            setModuleState(rootModule, state == -1 ? IServer.STATE_UNKNOWN : state);
            mapper.addArtifactEntry(getServer(), module, configId);
        } else {
            doAdded(module, null, monitor);
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "DefaultModuleHandler.doNoChange");
    }
    
    public void doRemoved(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DefaultModuleHandler.doRemoved", module.getName());

        ModuleSet<Artifact> artifacts = mapper.getServerArtifacts(getServer());
        if (artifacts != null) {
            synchronized (artifacts) {
                try {
                    _doRemove(module, monitor);
                } finally {
                    // remove the mapping - even if module failed to undeploy
                    mapper.removeArtifactEntry(getServer(), module);
                }
            }    
        } else {
            _doRemove(module, monitor);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "DefaultModuleHandler.doRemoved");
    }
    
    public void doStartModule(IModule[] module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DefaultModuleHandler.doStartModule", Arrays.asList(module));
        
        start(module[0], monitor);

        Trace.tracePoint("Exit ", Activator.traceCore, "DefaultModuleHandler.doStartModule");
    }
    
    public void doStopModule(IModule[] module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DefaultModuleHandler.doStopModule", Arrays.asList(module));
        
        stop(module[0], monitor);

        Trace.tracePoint("Exit ", Activator.traceCore, "DefaultModuleHandler.doStopModule");
    }
    
    public void doRestartModule(IModule[] module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DefaultModuleHandler.doRestartModule", Arrays.asList(module));

        stop(module[0], monitor);            
        start(module[0], monitor);

        Trace.tracePoint("Exit ", Activator.traceCore, "DefaultModuleHandler.doRestartModule");
    }
    
    private void _doRemove(IModule module, IProgressMonitor monitor) throws Exception {
        IStatus status = unDeploy(module, monitor);
        if (!status.isOK()) {
            doFail(status, Messages.UNDEPLOY_FAIL);
        }
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
    
    public String getConfigId(IModule module) throws Exception {
        return serverDelegate.getConfigId(module);
    }

    private String getLastKnowConfigurationId(IModule module, String configId) throws Exception {
        Trace.tracePoint("Entry ", Activator.traceCore, "GeronimoServerBehaviourDelegate.getLastKnowConfigurationId", module.getName(), configId);

        //use the correct configId, second from the .metadata, then from the plan
        configId = configId != null ? configId : DeploymentUtils.getLastKnownConfigurationId(module, getServer());

        Trace.tracePoint("Exit ", Activator.traceCore, "GeronimoServerBehaviourDelegate.getLastKnowConfigurationId", configId);
        return configId;
    }
    
    private TargetModuleID[] updateServerModuleConfigIDMap(IModule module, IStatus status) {
        TargetModuleID[] ids = ((DeploymentCmdStatus) status).getResultTargetModuleIDs();        
        mapper.addArtifactEntry(getServer(), module, ids[0].getModuleID());
        return ids;
    }

    @Override
    public int getModuleState(IModule module) throws Exception {
        ExtendedDeploymentManager dm = (ExtendedDeploymentManager) DeploymentCommandFactory.getDeploymentManager(getServer());
        String configID = ModuleArtifactMapper.getInstance().resolveArtifact(getServer(), module);
        return doGetModuleState(dm, configID);
    }
    
    private int doGetModuleState(ExtendedDeploymentManager dm, String configID) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoServerBehaviourDelegate.doGetModuleState", configID);
        
        int moduleState = IServer.STATE_UNKNOWN;
        if (configID != null) {
            try {
                State state = dm.getModulesState(org.apache.geronimo.kernel.repository.Artifact.create(configID));
                if (state == null) {
                    moduleState = -1;
                } else if (state == State.RUNNING) {
                    moduleState = IServer.STATE_STARTED;
                } else if (state == State.STOPPED) {
                    moduleState = IServer.STATE_STOPPED;
                }
            } catch (Exception e) {
                moduleState = IServer.STATE_UNKNOWN;
                Trace.trace(Trace.ERROR, "getModuleState() failed", e, Activator.traceCore);
            }
        }
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoServerBehaviourDelegate.doGetModuleState", configID, moduleState);
        return moduleState;
    }
    
}
