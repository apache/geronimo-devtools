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
package org.apache.geronimo.st.ui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.geronimo.st.ui.internal.Trace;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 *
 * @version $Rev$ $Date$
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "org.apache.geronimo.st.ui";
    public static boolean console;
    public static boolean logUi;
    public static boolean logActions;
    public static boolean logCommands;
    public static boolean logInternal;
    public static boolean logWizards;
    public static boolean logEditors;
    public static boolean logPages;
    public static boolean logSections;

    public static boolean traceUi;
    public static boolean traceActions;
    public static boolean traceCommands;
    public static boolean traceInternal;
    public static boolean traceWizards;
    public static boolean traceEditors;
    public static boolean tracePages;
    public static boolean traceSections;

    static {
        try {
            console = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/console"));

            logUi = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/ui"));
            logActions = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/actions"));
            logCommands = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/commands"));
            logInternal = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/internal"));
            logWizards = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/wizards"));
            logEditors = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/editors"));
            logPages = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/pages"));
            logSections = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/log/sections"));

            traceUi = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/ui"));
            traceActions = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/actions"));
            traceCommands = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/commands"));
            traceInternal = Boolean.parseBoolean(Platform.getDebugOption(PLUGIN_ID + "/trace/internal"));
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
	
	private static String iconLocation;

	private Map imageDescriptors = new HashMap();

	public static final String ICONS_DIRECTORY = "icons/";
	public static final String IMG_WIZ_GERONIMO = "gServer";
	public static final String IMG_PORT = "port";

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

	public static String getIconLocation() {
		if (iconLocation == null) {
			try {
				iconLocation = FileLocator.resolve(plugin.getBundle().getEntry("/")).getPath()
						+ ICONS_DIRECTORY;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return iconLocation;
	}

	/**
	 * Return the image with the given key from the image registry.
	 * 
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.jface.parts.IImage
	 */
	public static Image getImage(String key) {
		return plugin.getImageRegistry().get(key);
	}

	/**
	 * Return the image with the given key from the image registry.
	 * 
	 * @param key
	 *            java.lang.String
	 * @return org.eclipse.jface.parts.IImage
	 */
	public static ImageDescriptor getImageDescriptor(String key) {
		try {
			plugin.getImageRegistry();
			return (ImageDescriptor) plugin.imageDescriptors.get(key);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
	 */
	protected void initializeImageRegistry(ImageRegistry reg) {
		registerImage(reg, IMG_WIZ_GERONIMO, "g_server.gif");
		registerImage(reg, IMG_PORT, "obj16/port.gif");
	}

	private void registerImage(ImageRegistry registry, String key,
			String partialURL) {

		URL iconsURL = plugin.getBundle().getEntry(ICONS_DIRECTORY);

		try { 
			ImageDescriptor id = ImageDescriptor.createFromURL(new URL(iconsURL, partialURL));
			registry.put(key, id);
			imageDescriptors.put(key, id);
		} catch (Exception e) {
			Trace.trace(Trace.WARNING, "Error registering image", e, Activator.logUi);
		}
	}

	public static void log(int statusLevel, String s, Throwable t) {
		plugin.getLog().log(new Status(statusLevel, PLUGIN_ID, 0, s, t));
	}

}
