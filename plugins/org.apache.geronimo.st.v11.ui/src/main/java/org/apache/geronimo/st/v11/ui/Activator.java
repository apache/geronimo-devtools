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
package org.apache.geronimo.st.v11.ui;

import org.apache.geronimo.st.v11.ui.internal.Trace;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.apache.geronimo.st.v11.ui";
    public static boolean console;
    public static boolean logWizards;
    public static boolean logEditors;
    public static boolean logPages;
    public static boolean logSections;

    public static boolean traceWizards;
    public static boolean traceEditors;
    public static boolean tracePages;
    public static boolean traceSections;

    static {
        try {
            console = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/console"));

            logWizards = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/wizards"));
            logEditors = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/editors"));
            logPages = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/pages"));
            logSections = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/sections"));

            traceWizards = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/wizards"));
            traceEditors = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/editors"));
            tracePages = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/pages"));
            traceSections = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/sections"));
        } catch (NumberFormatException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), true);
        } catch (NullPointerException e) {
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

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.apache.geronimo.st.v11.ui", path);
	}
}
