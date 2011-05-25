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

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerLifecycleListener;
import org.eclipse.wst.server.core.ServerCore;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 *
 * @version $Rev$ $Date$
 */
public class Activator extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.apache.geronimo.st.core";
    /* trace and log switchers */
    public static boolean console;
    public static boolean logCore;
    public static boolean logCommands;
    public static boolean logDesc;
    public static boolean logInternal;
    public static boolean logOperations;
    
    public static boolean traceCore;
    public static boolean traceCommands;
    public static boolean traceDesc;
    public static boolean traceInternal;
    public static boolean traceOperations;
    /* end here */
    static {
    	try {
    		console = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/console"));
    		logCore = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/log/core"));
    		logCommands = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/log/commands"));
    		logDesc = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/log/descriptor"));
    		logInternal = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/log/internal"));
    		logOperations = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/log/operations"));
    		
    		traceCore = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/trace/core"));
    		traceCommands = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/trace/commands"));
    		traceDesc = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/trace/descriptor"));
    		traceInternal = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/trace/internal"));
    		traceOperations = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.st.core/trace/operations"));
    	} catch(NumberFormatException e) {
    		Trace.trace(Trace.ERROR, e.getMessage(), true);
    	} catch(NullPointerException e) {
    		Trace.trace(Trace.ERROR, e.getMessage(), true);
    	}

    }
    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
        plugin = this;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        ServerCore.addServerLifecycleListener(new IServerLifecycleListener() {
            public void serverAdded(IServer server) {
                triggerStartUpdateServerTask(server);
            }

            public void serverChanged(IServer server) {

            }

            public void serverRemoved(IServer server) {
            }
        });
        IServer[] servers = ServerCore.getServers();
        for(int i = 0; i < servers.length; i++) {
            triggerStartUpdateServerTask(servers[i]);
        }
    }


    /** 
     * <b>triggerStartUpdateServerTask</b> is invoked from:
     * <ul> 
     * <li>The WTP ResourceManager after a new server has been defined (via the WTP NewServerWizard)
     * <li>When a server lifecycle listener has been added (see above)
     * </ul>
     * 
     * @param server 
     */
    private void triggerStartUpdateServerTask(IServer server) {
        GeronimoServerBehaviourDelegate delegate = (GeronimoServerBehaviourDelegate) server.getAdapter(GeronimoServerBehaviourDelegate.class);
        if (delegate == null) {
            delegate = (GeronimoServerBehaviourDelegate) server.loadAdapter(GeronimoServerBehaviourDelegate.class, null);
        }
        if (delegate != null) {
            delegate.startUpdateServerStateTask();
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        IServer[] servers = ServerCore.getServers();
        for(int i = 0; i < servers.length; i++) {
            GeronimoServerBehaviourDelegate delegate = (GeronimoServerBehaviourDelegate) servers[i].getAdapter(GeronimoServerBehaviourDelegate.class);
            if(delegate != null) {
                delegate.stopUpdateServerStateTask();
            }
        }
        ModuleArtifactMapper.getInstance().save();
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    public static void log(int severity, String message, Throwable throwable) {
        if (plugin != null && plugin.getLog() != null) {
            plugin.getLog().log(new Status(severity, PLUGIN_ID, 0, message, throwable));
        }
    }

}
