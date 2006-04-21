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
package org.apache.geronimo.st.core;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.server.generic.core.internal.GenericServerRuntime;

public class GenericGeronimoServerRuntime extends GenericServerRuntime
		implements IGeronimoRuntime {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.RuntimeDelegate#validate()
	 */
	public IStatus validate() {
		String version = detectVersion();
		if (!getRuntime().getRuntimeType().getVersion().equals(version))
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, null, null);
		return super.validate();
	}

	public String detectVersion() {
		File libDir = getRuntime().getLocation().append("lib").toFile();
		if (libDir.exists()) {
			File[] libs = libDir.listFiles();
			URL systemjarURL = null;
			for (int i = 0; i < libs.length; i++) {
				if (libs[i].getName().startsWith("geronimo-system")) {
					try {
						systemjarURL = libs[i].toURL();
						break;
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			}
			URLClassLoader cl = new URLClassLoader(new URL[] { systemjarURL });
			try {
				Class clazz = cl.loadClass("org.apache.geronimo.system.serverinfo.ServerConstants");
				Method method = clazz.getMethod("getVersion", new Class[] {});
				return (String) method.invoke(null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
