/**
 *  Copyright 2006 The Apache Software Foundation
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;

public class GeronimoSourcePathComputerDelegate implements ISourcePathComputerDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate#computeSourceContainers(org.eclipse.debug.core.ILaunchConfiguration,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor) throws CoreException {

		IServer server = ServerUtil.getServer(configuration);
		IModule[] modules = server.getModules();

		List javaProjectList = new ArrayList();
		// populate list of java projects and their source folders
		processModules(modules, javaProjectList, server, monitor);

		// create a ProjectRuntime classpath entry for each JavaProject
		IRuntimeClasspathEntry[] projectEntries = new IRuntimeClasspathEntry[javaProjectList.size()];
		for (int i = 0; i < javaProjectList.size(); i++) {
			projectEntries[i] = JavaRuntime.newProjectRuntimeClasspathEntry((IJavaProject) javaProjectList.get(i));
		}

		// combine unresolved entries and project entries
		IRuntimeClasspathEntry[] unresolvedEntries = JavaRuntime.computeUnresolvedSourceLookupPath(configuration);
		IRuntimeClasspathEntry[] entries = new IRuntimeClasspathEntry[projectEntries.length + unresolvedEntries.length];
		System.arraycopy(unresolvedEntries, 0, entries, 0, unresolvedEntries.length);
		System.arraycopy(projectEntries, 0, entries, unresolvedEntries.length, projectEntries.length);

		IRuntimeClasspathEntry[] resolved = JavaRuntime.resolveSourceLookupPath(entries, configuration);
		ISourceContainer[] javaSourceContainers = JavaRuntime.getSourceContainers(resolved);

		// TODO support resolving from geronimo source distribution

		return javaSourceContainers;
	}

	private void processModules(IModule[] modules, List javaProjectList, IServer server, IProgressMonitor monitor) {
		for (int i = 0; i < modules.length; i++) {
			IProject project = modules[i].getProject();

			IModule[] childModules = server.getChildModules(new IModule[] { modules[i] }, monitor);
			if (childModules != null && childModules.length > 0) {
				processModules(childModules, javaProjectList, server, monitor);
			}

			if (project != null) {
				try {
					if (project.hasNature(JavaCore.NATURE_ID)) {
						IJavaProject javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
						if (!javaProjectList.contains(javaProject)) {
							javaProjectList.add(javaProject);
						}
					}
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

}
