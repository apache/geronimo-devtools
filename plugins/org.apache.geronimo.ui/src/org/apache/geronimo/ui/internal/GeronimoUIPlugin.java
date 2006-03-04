/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.ui.internal;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class GeronimoUIPlugin extends AbstractUIPlugin {
	protected static final String PLUGIN_ID = "org.apache.geronimo.ui";

	private static final String ICONS_DIRECTORY = "icons/";

	private static String iconLocation;

	private static GeronimoUIPlugin singleton;

	protected Map imageDescriptors = new HashMap();

	public static final String IMG_WIZ_GERONIMO = "gServer";

	/**
	 * The constructor.
	 */
	public GeronimoUIPlugin() {
		super();
		singleton = this;
	}

	/**
	 * Returns the singleton instance of this plugin.
	 * 
	 * @return org.apache.geronimo.ui.internal.GeronimoUIPlugin
	 */
	public static GeronimoUIPlugin getInstance() {
		return singleton;
	}

	public static String getIconLocation() {
		if (iconLocation == null) {
			try {
				iconLocation = Platform.resolve(
						GeronimoUIPlugin.getInstance().getBundle()
								.getEntry("/")).getPath()
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
		return getInstance().getImageRegistry().get(key);
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
			getInstance().getImageRegistry();
			return (ImageDescriptor) getInstance().imageDescriptors.get(key);
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
	}

	private void registerImage(ImageRegistry registry, String key,
			String partialURL) {

		URL iconsURL = singleton.getBundle().getEntry(ICONS_DIRECTORY);

		try {
			ImageDescriptor id = ImageDescriptor.createFromURL(new URL(
					iconsURL, partialURL));
			registry.put(key, id);
			imageDescriptors.put(key, id);
		} catch (Exception e) {
			Trace.trace(Trace.WARNING, "Error registering image", e);
		}
	}
}