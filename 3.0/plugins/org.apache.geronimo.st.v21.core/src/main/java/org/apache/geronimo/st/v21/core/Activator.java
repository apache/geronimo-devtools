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
package org.apache.geronimo.st.v21.core;

import org.apache.geronimo.st.v21.core.internal.Trace;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 *
 * @version $Rev$ $Date$
 */
public class Activator extends Plugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.apache.geronimo.st.v21.core";

    // The shared instance
    private static Activator plugin;
    /* trace and log switchers */
    public static boolean console;
    public static boolean logCore;
    public static boolean logInternal;
    public static boolean logOperations;
    public static boolean logPlugin;

    public static boolean traceCore;
    public static boolean traceInternal;
    public static boolean traceOperations;
    public static boolean tracePlugin;
    /* end here */
    static {
        try {
            console = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/console"));
            logCore = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/core"));
            logInternal = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/internal"));
            logOperations = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/operations"));
            logPlugin = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/plugin"));

            traceCore = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/core"));
            traceInternal = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/internal"));
            traceOperations = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/operations"));
            tracePlugin = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/plugin"));
        } catch (NumberFormatException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), true);
        } catch (NullPointerException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), true);
        }

    }

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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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
	
}
