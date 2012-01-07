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
 * @version $Rev$ $Date$
 */
package org.apache.geronimo.st.v30.core;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;

public class SynchronizeProjectOnServerTask extends TimerTask {

    private GeronimoServerBehaviourDelegate delegate;

    private IServer server;
    
    public SynchronizeProjectOnServerTask(GeronimoServerBehaviourDelegate delegate, IServer server) {
        this.delegate = delegate;
        this.server = server;
    }

    @Override
    public void run() {
        Trace.tracePoint("Entry", Activator.traceCore, "SynchronizeProjectOnServerTask.run");

        if (canUpdateState() && !delegate.isPublishing()) {

            List<IModule> removedModules = new ArrayList<IModule>();

            IModule[] modules = server.getModules();

            try {
                for (IModule module : modules) {
                    IModule[] rootModule = new IModule[] { module };
                    if (!delegate.isPublished(rootModule)) {
                        Trace.trace(Trace.INFO, "SynchronizeProjectOnServerTask: Ignoring non-published module: " + module, Activator.traceCore);
                        continue;
                    }
                    int state = delegate.getModuleHandler(module).getModuleState(module);
                    if (state == -1) {
                        removedModules.add(module);
                    } else {
                        delegate.setModulesState(rootModule, state);
                    }
                }

                if (!removedModules.isEmpty()) {
                    if (!delegate.isPublishing()) {
                        removeModules(removedModules);
                    }
                } else {
                    Trace.trace(Trace.INFO, "SynchronizeProjectOnServerTask: no configuration is removed outside eclipse on server: " + server.getId(), Activator.traceCore);
                }

            } catch (Exception e) {
                Trace.trace(Trace.WARNING, "Error in SynchronizeProjectOnServerTask.run", e, Activator.logCore);
            }
        }

        Trace.tracePoint("Exit", Activator.traceCore, "SynchronizeProjectOnServerTask.run");
    }
   
    private void removeModules(List<IModule> removedModules) {
        Trace.tracePoint("Entry ", Activator.traceCore, "SynchronizeProjectOnServerTask.removeModules", removedModules);

        IModule[] remove = new IModule[removedModules.size()];
        removedModules.toArray(remove);

        IServerWorkingCopy wc = server.createWorkingCopy();
        IProgressMonitor monitor = new NullProgressMonitor();

        try {
            wc.modifyModules(null, remove, monitor);
            server = wc.save(true, monitor);
        } catch (CoreException e) {
            Trace.trace(Trace.WARNING, "Could not remove module in SynchronizeProjectOnServerTask", e, Activator.logCore);
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "SynchronizeProjectOnServerTask.removeModules");
    }

    private boolean canUpdateState() {
        if (server.getServerState() != IServer.STATE_STARTED) {
            return false;
        }

        IGeronimoServer thisServer = (IGeronimoServer) this.server.loadAdapter(IGeronimoServer.class, null);
        IServer[] allServers = ServerCore.getServers();
        for (int i = 0; i < allServers.length; i++) {
            IServer server = allServers[i];
            IGeronimoServer gs = (IGeronimoServer) server.loadAdapter(IGeronimoServer.class, null);
            if (gs != null && !this.server.getId().equals(server.getId())) {
                if (isSameConnectionURL(gs, thisServer)) {
                    if (!isSameRuntimeLocation(server) && server.getServerState() != IServer.STATE_STOPPED) {
                        Trace.trace(Trace.WARNING, server.getId() + " Cannot update server state.  URL conflict between multiple servers.", Activator.logCore);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isSameRuntimeLocation(IServer server) {
        return server.getRuntime().getLocation().equals(this.server.getRuntime().getLocation());
    }

    private boolean isSameConnectionURL(IGeronimoServer server, IGeronimoServer thisServer) {
        return server.getJMXServiceURL().equals(thisServer.getJMXServiceURL());
    }

}
