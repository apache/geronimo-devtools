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

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.st.core.internal.Messages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.server.core.RuntimeClasspathProviderDelegate;
import org.eclipse.wst.server.core.IRuntime;

/**
 * @version $Rev$ $Date$
 */
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
		} else if(version.equals("1.2")) {
			return Messages.target12runtime;
		}
		return Messages.target12runtime;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.server.core.RuntimeClasspathProviderDelegate#resolveClasspathContainer(org.eclipse.wst.server.core.IRuntime)
	 */
	public IClasspathEntry[] resolveClasspathContainer(IRuntime runtime) {
		return getServerClassPathEntry(runtime);
	}

	public IClasspathEntry[] getServerClassPathEntry(IRuntime runtime) {
		
		List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
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
		} else {
			IPath specPath = runtime.getLocation().append("repository/org/apache/geronimo/specs/");
			addLibraryEntries(list, specPath.toFile(), true);
            //
            // Add spec jars that are not in the specs directory (per Geronimo configs jee-specs)
            //
            if (!version.startsWith("1")) {
            	IPath javaMailSpec = runtime.getLocation().append("repository/org/apache/geronimo/javamail/geronimo-javamail_1.4_mail/");
            	IPath jaxbApiSpec  = runtime.getLocation().append("repository/javax/xml/bind/jaxb-api/");
            	IPath jabxImplSpec = runtime.getLocation().append("repository/com/sun/xml/bind/jaxb-impl/");
            	IPath jaxwsApiSpec = runtime.getLocation().append("repository/org/apache/axis2/axis2-jaxws-api/");
            	IPath saajApiSpec  = runtime.getLocation().append("repository/org/apache/axis2/axis2-saaj-api/");
            	IPath jstlSpec     = runtime.getLocation().append("repository/jstl/jstl/");
            	IPath myfacesSpec  = runtime.getLocation().append("repository/org/apache/myfaces/core/myfaces-api/");
            	IPath jdbcSpec     = runtime.getLocation().append("repository/org/apache/geronimo/modules/geronimo-jdbc/");
            	addLibraryEntries(list, javaMailSpec.toFile(), true);
            	addLibraryEntries(list, jaxbApiSpec.toFile(),  true);
            	addLibraryEntries(list, jabxImplSpec.toFile(), true);
            	addLibraryEntries(list, jaxwsApiSpec.toFile(), true);
            	addLibraryEntries(list, saajApiSpec.toFile(),  true);
            	addLibraryEntries(list, jstlSpec.toFile(),     true);
            	addLibraryEntries(list, myfacesSpec.toFile(),  true);
            	addLibraryEntries(list, jdbcSpec.toFile(),     true);
            }
		}
		
		return (IClasspathEntry[])list.toArray(new IClasspathEntry[list.size()]);
	}
}
