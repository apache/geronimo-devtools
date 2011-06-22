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
package org.apache.geronimo.st.v30.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.debug.core.sourcelookup.containers.ExternalArchiveSourceContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoSourcePathComputerDelegate implements ISourcePathComputerDelegate {
    
    private Set<String> additionalSrcPathComputerIds = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate#computeSourceContainers(org.eclipse.debug.core.ILaunchConfiguration,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public ISourceContainer[] computeSourceContainers(ILaunchConfiguration configuration, IProgressMonitor monitor) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoSourcePathComputerDelegate.computeSourceContainers", configuration, monitor);
        
        IServer server = ServerUtil.getServer(configuration);
        IModule[] modules = server.getModules();

        List<IJavaProject> javaProjectList = new ArrayList<IJavaProject>();
        // populate list of java projects and their source folders
        processModules(modules, javaProjectList, server, monitor);

        // create a ProjectRuntime classpath entry for each JavaProject
        IRuntimeClasspathEntry[] projectEntries = new IRuntimeClasspathEntry[javaProjectList.size()];
        for (int i = 0; i < javaProjectList.size(); i++) {
            projectEntries[i] = JavaRuntime.newProjectRuntimeClasspathEntry(javaProjectList.get(i));
        }

        // combine unresolved entries and project entries
        IRuntimeClasspathEntry[] unresolvedEntries = JavaRuntime.computeUnresolvedSourceLookupPath(configuration);
        IRuntimeClasspathEntry[] entries = new IRuntimeClasspathEntry[projectEntries.length + unresolvedEntries.length];
        System.arraycopy(unresolvedEntries, 0, entries, 0, unresolvedEntries.length);
        System.arraycopy(projectEntries, 0, entries, unresolvedEntries.length, projectEntries.length);

        IRuntimeClasspathEntry[] resolved = JavaRuntime.resolveSourceLookupPath(entries, configuration);
        ISourceContainer[] defaultContainers = JavaRuntime.getSourceContainers(resolved);
               
        Set<ISourceContainer> allContainers = new HashSet<ISourceContainer>();
        // add project source containers
        addAll(allContainers, defaultContainers);
        
        // add additional source containers
        ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
        for (String id : getAdditionalSrcPathComputers()) {
            ISourcePathComputer computer = mgr.getSourcePathComputer(id);
            ISourceContainer[] jsc = computer.computeSourceContainers(configuration, monitor);
            addAll(allContainers, jsc);
        }
        
        // add source containers for Geronimo Runtime
        ISourceContainer[] runtimeContainers = processServer(server);
        addAll(allContainers, runtimeContainers);

        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoSourcePathComputerDelegate.computeSourceContainers", toStringList(allContainers));
        
        return allContainers.toArray(new ISourceContainer[allContainers.size()]);
    }
    
    private static void addAll(Set<ISourceContainer> allContainers, ISourceContainer[] containers) {
        if (containers != null) {
            for (ISourceContainer container : containers) {
                allContainers.add(container);
            }
        }
    }
    
    private static List<String> toStringList(Set<ISourceContainer> allContainers) {
        List<String> list = new ArrayList<String>(allContainers.size());
        for (ISourceContainer container : allContainers) {
            list.add(container.getName());
        }
        return list;
    }
    
    private ISourceContainer[] processServer(IServer server) {
        IRuntime runtime = server.getRuntime();
        IGeronimoRuntime gRuntime = (IGeronimoRuntime) runtime.getAdapter(IGeronimoRuntime.class);
        if (gRuntime != null) {
            IPath sourcePath = gRuntime.getRuntimeSourceLocation();
            if (sourcePath != null) {
                File file = sourcePath.toFile();
                if (file.isFile()) {
                    ExternalArchiveSourceContainer sourceContainer = new ExternalArchiveSourceContainer(file.getAbsolutePath(), true);
                    return new ISourceContainer[] { sourceContainer };
                } else if (file.isDirectory()) {
                    // TODO implement me using DirectorySourceContainer
                }
            }
        }
        return new ISourceContainer[] {};
    }

    private void processModules(IModule[] modules, List<IJavaProject> javaProjectList, IServer server, IProgressMonitor monitor) {
        for (int i = 0; i < modules.length; i++) {
            IProject project = modules[i].getProject();

            IModule[] childModules = server.getChildModules(new IModule[] { modules[i] }, monitor);
            if (childModules != null && childModules.length > 0) {
                processModules(childModules, javaProjectList, server, monitor);
            }

            if (project != null) {
                //process referenced projects for shared lib
                try {
                    IProject[] referencedProjects = project.getReferencedProjects();
                    for(int j = 0; j < referencedProjects.length; j++) {
                        processJavaProject(javaProjectList, referencedProjects[j]);
                    }
                } catch (CoreException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                processJavaProject(javaProjectList, project);
            }
        }
    }

    private void processJavaProject(List<IJavaProject> javaProjectList, IProject project) {
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
    
    private synchronized void init() {
        if (additionalSrcPathComputerIds == null) {
            additionalSrcPathComputerIds = new HashSet<String>();
            IExtensionPoint extensionPoint= Platform.getExtensionRegistry().getExtensionPoint(Activator.PLUGIN_ID, "sourcePathComputerMapping");
            IConfigurationElement[] extensions = extensionPoint.getConfigurationElements();
            for (int i = 0; i < extensions.length; i++) {
                String id = extensions[i].getAttribute("id");
                additionalSrcPathComputerIds.add(id);
            }
        }
    }

    public Set<String> getAdditionalSrcPathComputers() {
        init();
        return additionalSrcPathComputerIds;
    }

}
