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

package org.apache.geronimo.devtools.helloworld;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @version $Rev$ $Date$
 */
public class HelloWorldTestPlugin extends AbstractUIPlugin {
	
	private static HelloWorldTestPlugin plugin;
	
	public HelloWorldTestPlugin() {
		super();
		plugin = this;
	}
	
	public static HelloWorldTestPlugin getDefault() {
		return plugin;
	}
	
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
	
	public static void enableAutobuild(boolean enable) throws CoreException {
		// disable auto build
		IWorkspace workspace= HelloWorldTestPlugin.getWorkspace();
		IWorkspaceDescription desc= workspace.getDescription();
		desc.setAutoBuilding(enable);
		workspace.setDescription(desc);
	}
	
	public File getFileInPlugin(IPath path) {
		try {
			Bundle bundle = getDefault().getBundle();
			URL installURL= new URL(bundle.getEntry("/"), path.toString());
			URL localURL= FileLocator.toFileURL(installURL);
			return new File(localURL.getFile());
		} catch (IOException e) {
			return null;
		}
	}
}
