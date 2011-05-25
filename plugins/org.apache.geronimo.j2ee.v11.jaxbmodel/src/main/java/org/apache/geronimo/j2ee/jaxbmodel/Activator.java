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

package org.apache.geronimo.j2ee.jaxbmodel;

import org.apache.geronimo.j2ee.jaxbmodel.internal.Trace;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class Activator extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.apache.geronimo.j2ee.v11.jaxbmodel";
    public static boolean console;
    public static boolean logOperations;
    public static boolean traceOperations;
    
    static {
    	try {
    		console = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.j2ee.v11.jaxbmodel/console"));
    		logOperations = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.j2ee.v11.jaxbmodel/log/operations"));
    		traceOperations = Boolean.parseBoolean(Platform.getDebugOption("org.apache.geronimo.j2ee.v11.jaxbmodel/trace/operations"));
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
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);   
        this.setDebugging(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
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
