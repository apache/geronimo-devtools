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

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.st.core.internal.Messages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.server.core.RuntimeClasspathProviderDelegate;
import org.eclipse.wst.server.core.IRuntime;

public class GeronimoServerRuntimeTargetHandler extends RuntimeClasspathProviderDelegate {
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.server.core.RuntimeClasspathProviderDelegate#getClasspathContainerLabel(org.eclipse.wst.server.core.IRuntime)
	 */
	public String getClasspathContainerLabel(IRuntime runtime) {
		String version = runtime.getRuntimeType().getVersion();
		if(version.equals("1.0")) {
			return Messages.target10runtime;
		} else if(version.equals("1.1")) {
			return Messages.target11runtime;
		}
		return Messages.target11runtime;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.server.core.RuntimeClasspathProviderDelegate#resolveClasspathContainer(org.eclipse.wst.server.core.IRuntime)
	 */
	public IClasspathEntry[] resolveClasspathContainer(IRuntime runtime) {
		return getServerClassPathEntry(runtime);
	}

	public IClasspathEntry[] getServerClassPathEntry(IRuntime runtime) {
		
		List list = new ArrayList();
		IPath path = runtime.getLocation().append("lib");
		addLibraryEntries(list, path.toFile(), true);
		
		String version = runtime.getRuntimeType().getVersion();
		
		if(version.equals("1.0")) {
			IPath specPath = runtime.getLocation().append("repository/org.apache.geronimo.specs/jars/");
			addLibraryEntries(list, specPath.toFile(), false);
		} else if(version.equals("1.1")) {
			IPath specPath = runtime.getLocation().append("repository/org/apache/geronimo/specs/geronimo-j2ee_1.4_spec/1.1/geronimo-j2ee_1.4_spec-1.1.jar");
			//hack to add servlet/jsp spec jars to little G
			if(!specPath.toFile().exists()) {
				IPath servletSpec =runtime.getLocation().append("repository/org/apache/geronimo/specs/geronimo-servlet_2.4_spec/1.0.1/geronimo-servlet_2.4_spec-1.0.1.jar");
				IPath jspSpec = runtime.getLocation().append("repository/org/apache/geronimo/specs/geronimo-jsp_2.0_spec/1.0.1/geronimo-jsp_2.0_spec-1.0.1.jar");
				list.add(JavaCore.newLibraryEntry(servletSpec, null, null));
				list.add(JavaCore.newLibraryEntry(jspSpec, null, null));
			} else {
				list.add(JavaCore.newLibraryEntry(specPath, null, null));
			}
		}
		
		return (IClasspathEntry[])list.toArray(new IClasspathEntry[list.size()]);
	}
}
