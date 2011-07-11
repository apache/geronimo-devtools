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
package org.apache.geronimo.st.v30.core.osgi;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.geronimo.deployment.plugin.jmx.RemoteDeploymentManager;
import org.apache.geronimo.st.v30.core.AbstractModuleHandler;
import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.DeploymentUtils;
import org.apache.geronimo.st.v30.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.v30.core.GeronimoUtils;
import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.internal.Messages;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

public class OSGiModuleHandler extends AbstractModuleHandler {
    
    private Map<String, Long> bundleMap;
    
    public OSGiModuleHandler(GeronimoServerBehaviourDelegate serverDelegate) {
        super(serverDelegate);
        bundleMap = Collections.synchronizedMap(new HashMap<String, Long>());
    }

    @Override
    public void serverStopped() {
        bundleMap.clear();
    }
    
    private long getBundleId(IModule module) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.getBundleId", module);
        Long id = bundleMap.get(module.getId());
        if (id == null && module.getProject() != null) {
            RemoteDeploymentManager dm = (RemoteDeploymentManager) DeploymentCommandFactory.getDeploymentManager(this.getServer());
            AriesHelper.BundleInfo bundleInfo = AriesHelper.getBundleInfo(module.getProject());
            id = dm.getBundleId(bundleInfo.getSymbolicName(), bundleInfo.getVersion().toString());
            if (id != -1) {
                bundleMap.put(module.getId(), id);
            }
        }
        Trace.tracePoint("Exit", Activator.traceCore, "OSGiBundleHandler.getBundleId", id);
        return id;
    }
    
    public void doAdded(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.doAdded", module.getName());
        
        long bundleId = getBundleId(module);
        if (bundleId == -1) {
            IStatus status = distributeBundle(module);
            if (!status.isOK()) {
                doFail(status, Messages.DISTRIBUTE_FAIL);
            }
            
            bundleId = getBundleId(module);
            if (GeronimoUtils.isFragmentBundleModule(module)) {
                setModuleState(new IModule [] { module }, IServer.STATE_STOPPED);
            } else {
                startBundle(bundleId, module);
            }
        } else {
            doChanged(module, monitor);
        }
                
        Trace.tracePoint("Exit", Activator.traceCore, "OSGiBundleHandler.doAdded", bundleId);
    }
    
    public void doChanged(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.doChanged", module.getName());
        
        long bundleId = getBundleId(module);
        if (bundleId != -1) {
            doRemoved(module, monitor);
        }
        
        doAdded(module, monitor);       

        Trace.tracePoint("Exit", Activator.traceCore, "OSGiBundleHandler.doChanged", bundleId);
    }
    
    public void doNoChange(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.doNoChange", module.getName());
        
        long bundleId = getBundleId(module);
        if (bundleId != -1) {
            try {
                String state = getBundleState(bundleId);
                if ("active".equalsIgnoreCase(state)) {
                    setModuleState(new IModule [] {module}, IServer.STATE_STARTED);
                } else if ("resolved".equalsIgnoreCase(state)) {
                    setModuleState(new IModule [] {module}, IServer.STATE_STOPPED);
                } else {
                    setModuleState(new IModule [] {module}, IServer.STATE_UNKNOWN);
                }
            } catch (Exception e) {
                setModuleState(new IModule [] {module}, IServer.STATE_UNKNOWN);
            }
        } else {
            doAdded(module, monitor);
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "OSGiBundleHandler.doNoChange", bundleId);
    }
    
    public void doRemoved(IModule module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.doRemoved", module.getName());

        long bundleId = getBundleId(module);
        if (bundleId != -1) {
            IStatus status = _unInstallBundle(bundleId);
            bundleMap.remove(module.getId());
            if (!status.isOK()) {
                doFail(status, Messages.UNDEPLOY_FAIL);
            }
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "OSGiBundleHandler.doRemoved", bundleId);
    }
    
    public void doStartModule(IModule[] module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.doStartModule", Arrays.asList(module));
        
        long bundleId = getBundleId(module[0]);
        if (bundleId != -1) {
            startBundle(bundleId, module[0]);
        } else {
            throw new Exception(Messages.START_FAIL);
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "OSGiBundleHandler.doStartModule");
    }
    
    public void doStopModule(IModule[] module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.doStopModule", Arrays.asList(module));
        
        long bundleId = getBundleId(module[0]);
        if (bundleId != -1) {
            stopBundle(bundleId, module[0]);
        } else {
            throw new Exception(Messages.STOP_FAIL);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "OSGiBundleHandler.doStopModule");
    }
    
    public void doRestartModule(IModule[] module, IProgressMonitor monitor) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "OSGiBundleHandler.doRestartModule", Arrays.asList(module));

        long bundleId = getBundleId(module[0]);
        if (bundleId != -1) {
            stopBundle(bundleId, module[0]);
            startBundle(bundleId, module[0]);
        } else {
            throw new Exception(Messages.RESTART_OSGIBUNDLE_FAIL);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "OSGiBundleHandler.doRestartModule");
    }
        
    private IStatus distributeBundle(IModule module) throws Exception {
        RemoteDeploymentManager rDm = (RemoteDeploymentManager)DeploymentCommandFactory.getDeploymentManager(this.getServer());
        try {
            /* Get target file */
            File f = DeploymentUtils.getTargetFile(getServer(), module);
            if (f == null) {
                throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 
                        Messages.bind(Messages.moduleExportError, module.getProject().getName())));     
            }
            /* end here */
            long bundleId = rDm.recordInstall(f, null, OsgiConstants.BUNDLE_DEFAULT_START_LEVEL);
            
            bundleMap.put(module.getId(), bundleId);
            
            return Status.OK_STATUS;
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error installing " + module.getName() + " bundle", e, Activator.logCore);
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error installing " + module.getName() + " bundle", e);
        }
    }
     
    private void startBundle(long bundleId, IModule module) throws CoreException {
        IStatus status = _startBundle(bundleId);            
        if (!status.isOK()) {
            doFail(status, Messages.START_FAIL);
        } else {
            setModuleState(new IModule [] { module }, IServer.STATE_STARTED);
        }
    }
    
    private void stopBundle(long bundleId, IModule module) throws CoreException {
        IStatus status = _stopBundle(bundleId);            
        if (!status.isOK()) {
            doFail(status, Messages.STOP_FAIL);
        } else {
            setModuleState(new IModule [] { module }, IServer.STATE_STOPPED);
        }
    }
    
    private IStatus _startBundle(long bundleId) {
        try {
            MBeanServerConnection connection = getServerConnection();
            connection.invoke(getFrameworkMBean(connection), "startBundle",
                    new Object[] { bundleId }, new String[] { long.class.getName() });
            return Status.OK_STATUS;
        } catch(Exception e) {
            Trace.trace(Trace.ERROR, "Error starting bundle " + bundleId, e, Activator.logCore);
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not start bundle", e);
        }        
    }
    
    private IStatus _stopBundle(long bundleId) {
        try {
            MBeanServerConnection connection = getServerConnection();
            connection.invoke(getFrameworkMBean(connection), "stopBundle",
                    new Object[] { bundleId }, new String[] { long.class.getName() });
            return Status.OK_STATUS;
        } catch(Exception e) {
            Trace.trace(Trace.ERROR, "Error stopping bundle " + bundleId, e, Activator.logCore);
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not stop bundle", e);
        }
    }
    
    private String getBundleState(long bundleId) throws Exception {
        MBeanServerConnection connection = getServerConnection();
        return (String) connection.invoke(getBundleStateMBean(connection), "getState",
                new Object[] { bundleId }, new String[] { long.class.getName() });
    }
    
    private IStatus _unInstallBundle(long bundleId) throws CoreException {
        RemoteDeploymentManager rDm = (RemoteDeploymentManager)DeploymentCommandFactory.getDeploymentManager(this.getServer());
        try {
            rDm.eraseUninstall(bundleId);
            return Status.OK_STATUS;
        } catch(Exception e) {
            Trace.trace(Trace.ERROR, "Error uninstalling bundle", e, Activator.logCore);
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error uninstalling bundle", e);
        }
    }
    
    private ObjectName getFrameworkMBean(MBeanServerConnection connection) throws Exception {
        return serverDelegate.getMBean(connection, "osgi.core:type=framework,*", "Framework");
    }
    
    private ObjectName getBundleStateMBean(MBeanServerConnection connection) throws Exception {
        return serverDelegate.getMBean(connection, "osgi.core:type=bundleState,*", "BundleState");
    }
    
    private MBeanServerConnection getServerConnection() throws Exception {
        return serverDelegate.getServerConnection();
    }
}
