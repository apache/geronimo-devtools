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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;

public class GeronimoSourcePathComputerDelegate implements ISourcePathComputerDelegate {
	
	private HashSet additionalSrcPathComputerIds = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate#computeSourceContainers(org.eclipse.debug.core.ILaunchConfiguration,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor) throws CoreException {
		Trace.trace(Trace.INFO, ">> GeronimoSourcePathComputerDelegate.computeSourceContainers()");
		
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
		ISourceContainer[] defaultContainers = JavaRuntime.getSourceContainers(resolved);
		
		HashSet allContainers = new HashSet(Arrays.asList(defaultContainers));
		Iterator i = getAdditionalSrcPathComputers().iterator();
		ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
		Trace.trace(Trace.INFO, "Total # of unique source containers: " + allContainers.size());
		while(i.hasNext()) {
			ISourcePathComputer computer = mgr.getSourcePathComputer((String) i.next());
			Trace.trace(Trace.INFO, "Invoking Source Path Computer " +  computer.getId());
			ISourceContainer[] jsc = computer.computeSourceContainers(configuration, monitor);
			if(jsc != null) {
				Trace.trace(Trace.INFO, "Additional Source Containers returned ...");
				for(int j = 0; j < jsc.length; j++) {
					Trace.trace(Trace.INFO, jsc[j].getName());
				}
			}
			allContainers.addAll(Arrays.asList(jsc));
			Trace.trace(Trace.INFO, "Number # of unique source containers: " + allContainers.size());
		}
		
		// TODO support resolving from geronimo source distribution

		return (ISourceContainer[])allContainers.toArray(new ISourceContainer[allContainers.size()]);
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
	
	private synchronized void init() {
		if(additionalSrcPathComputerIds == null) {
			additionalSrcPathComputerIds = new HashSet();
			IExtensionPoint extensionPoint= Platform.getExtensionRegistry().getExtensionPoint(Activator.PLUGIN_ID, "sourcePathComputerMapping");
			IConfigurationElement[] extensions = extensionPoint.getConfigurationElements();
			for (int i = 0; i < extensions.length; i++) {
				String id = extensions[i].getAttribute("id");
				Trace.trace(Trace.INFO, "Found extension point " +  id);
				additionalSrcPathComputerIds.add(id);
			}
		}
	}

	public HashSet getAdditionalSrcPathComputers() {
		init();
		return additionalSrcPathComputerIds;
	}

}
