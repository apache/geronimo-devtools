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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

public abstract class AbstractModuleHandler {
    
    protected GeronimoServerBehaviourDelegate serverDelegate;
    
    public AbstractModuleHandler(GeronimoServerBehaviourDelegate serverDelegate) {
        this.serverDelegate = serverDelegate;
    }
    
    abstract public void doAdded(IModule module, IProgressMonitor monitor) throws Exception;
        
    abstract public void doChanged(IModule module, IProgressMonitor monitor) throws Exception;
    
    abstract public void doNoChange(IModule module, IProgressMonitor monitor) throws Exception;
        
    abstract public void doRemoved(IModule module, IProgressMonitor monitor) throws Exception;

    abstract public void doStartModule(IModule[] module, IProgressMonitor monitor) throws Exception;
    
    abstract public void doStopModule(IModule[] module, IProgressMonitor monitor) throws Exception;
    
    abstract public void doRestartModule(IModule[] module, IProgressMonitor monitor) throws Exception;

    public void serverStopped() {        
    }
    
    protected void doFail(IStatus status, String message) throws CoreException {
        MultiStatus ms = new MultiStatus(Activator.PLUGIN_ID, 0, message, null);
        ms.addAll(status);
        throw new CoreException(ms);
    }
    
    public void setModuleState(IModule[] module, int state) {
        serverDelegate.setModulesState(module, state);
    }
    
    public IServer getServer() {
        return serverDelegate.getServer();
    }
}
