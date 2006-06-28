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

import org.apache.geronimo.st.core.internal.Messages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.server.generic.core.internal.GenericServerRuntime;
import org.eclipse.osgi.util.NLS;

public class GenericGeronimoServerRuntime extends GenericServerRuntime implements IGeronimoRuntime {

	public static final int NO_IMAGE = 0;

	public static final int INCORRECT_VERSION = 1;

	public static final int PARTIAL_IMAGE = 2;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.model.RuntimeDelegate#validate()
	 */
	public IStatus validate() {
		IStatus status = super.validate();

		if (!status.isOK()) {
			return status;
		}

		IPath runtimeLoc = getRuntime().getLocation();

		// check for server file structure
		int count = 0;
		count = runtimeLoc.append("bin/server.jar").toFile().exists() ? ++count : count;
		count = runtimeLoc.append("bin/deployer.jar").toFile().exists() ? ++count : count;
		count = runtimeLoc.append("lib").toFile().exists() ? ++count : ++count;
		count = runtimeLoc.append("repository").toFile().exists() ? ++count : count;

		if (count == 0)
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, NO_IMAGE, null, null);

		if (count < 4) {
			// part of a server image was found, don't let install happen
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, PARTIAL_IMAGE, Messages.missingContent, null);
		}

		String detectedVersion = detectVersion();
		if (detectedVersion == null)
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID, INCORRECT_VERSION, Messages.noVersion, null);

		if (!detectedVersion.startsWith(getRuntime().getRuntimeType().getVersion())) {
			String message = NLS.bind(Messages.incorrectVersion, new String[] {
					getRuntime().getRuntimeType().getVersion(),
					detectedVersion });
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, INCORRECT_VERSION, message, null);
		}

		return Status.OK_STATUS;
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
			if (systemjarURL != null) {
				URLClassLoader cl = new URLClassLoader(new URL[] { systemjarURL });
				try {
					Class clazz = cl.loadClass("org.apache.geronimo.system.serverinfo.ServerConstants");
					Method method = clazz.getMethod("getVersion", new Class[] {});
					return (String) method.invoke(null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public String getInstallableTomcatRuntimeId() {
		String version = getRuntime().getRuntimeType().getVersion();
		if("1.0".equals(version)) {
			return "org.apache.geronimo.runtime.tomcat.10";
		} else if("1.1".equals(version)) {
			return "org.apache.geronimo.runtime.tomcat.11";
		}
		return null;
	}
	
	public String getInstallableJettyTomcatId() {
		String version = getRuntime().getRuntimeType().getVersion();
		if("1.0".equals(version)) {
			return "org.apache.geronimo.runtime.jetty.10";
		} else if("1.1".equals(version)) {
			return "org.apache.geronimo.runtime.jetty.11";
		}
		return null;
	}
}
