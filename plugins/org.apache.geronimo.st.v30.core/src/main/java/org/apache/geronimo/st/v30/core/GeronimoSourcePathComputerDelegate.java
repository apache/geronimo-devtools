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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.sourcelookup.containers.PackageFragmentRootSourceContainer;
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

        Set<IProject> projectList = new HashSet<IProject>();
        Set<IJavaProject> javaProjectList = new HashSet<IJavaProject>();
        
        // collect all referenced projects
        processModules(modules, projectList, javaProjectList, server, monitor);
               
        Trace.trace(Trace.INFO, "GeronimoSourcePathComputerDelegate: projects found: " + toString(javaProjectList), Activator.traceCore);
        
        Set<ISourceContainer> allContainers = new LinkedHashSet<ISourceContainer>();
        
        // add default source containers
        addDefaultSourceContainers(javaProjectList, configuration, allContainers);
        
        // add fragment source containers
        addFragmentSourceContainers(javaProjectList, allContainers);
        
        // add additional source containers
        addAdditionalSourceContainers(configuration, monitor, allContainers);
        
        // add source containers for Geronimo Runtime
        addServerSourceContainers(server, allContainers);

        // set known source project list
        GeronimoServerBehaviourDelegate delegate = getDelegate(server);
        delegate.setKnownSourceProjects(projectList);
        
        ISourceContainer[] sourceContainers = new ISourceContainer[allContainers.size()];
        allContainers.toArray(sourceContainers);
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoSourcePathComputerDelegate.computeSourceContainers", toString(sourceContainers));
        
        return sourceContainers;
    }

    private void addAdditionalSourceContainers(ILaunchConfiguration configuration, 
                                               IProgressMonitor monitor,
                                               Set<ISourceContainer> allContainers) throws CoreException {
        ILaunchManager mgr = DebugPlugin.getDefault().getLaunchManager();
        for (String id : getAdditionalSrcPathComputers()) {
            ISourcePathComputer computer = mgr.getSourcePathComputer(id);
            ISourceContainer[] jsc = computer.computeSourceContainers(configuration, monitor);
            addAll(allContainers, jsc);
        }
    }

    private void addDefaultSourceContainers(Set<IJavaProject> projectList, 
                                            ILaunchConfiguration configuration, 
                                            Collection<ISourceContainer> allContainers) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoSourcePathComputerDelegate.addDefaultSourceContainers");
        
        List<IRuntimeClasspathEntry> projectEntriesList = new ArrayList<IRuntimeClasspathEntry>(projectList.size());
        
        // create a ProjectRuntime classpath entry for each JavaProject
        for (IJavaProject project : projectList) {
            projectEntriesList.add(JavaRuntime.newProjectRuntimeClasspathEntry(project));            
        }
        
        // add in unresolved entries
        IRuntimeClasspathEntry[] unresolvedEntries = JavaRuntime.computeUnresolvedSourceLookupPath(configuration);        
        if (unresolvedEntries != null) {
            for (IRuntimeClasspathEntry unresolvedEntry : unresolvedEntries) {
                projectEntriesList.add(unresolvedEntry);
            }
        }
        
        IRuntimeClasspathEntry[] projectEntries = new IRuntimeClasspathEntry[projectEntriesList.size()];
        projectEntriesList.toArray(projectEntries);

        IRuntimeClasspathEntry[] resolved = JavaRuntime.resolveSourceLookupPath(projectEntries, configuration);
        ISourceContainer[] defaultContainers = JavaRuntime.getSourceContainers(resolved);
        
        addAll(allContainers, defaultContainers);
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoSourcePathComputerDelegate.addDefaultSourceContainers", toString(defaultContainers));        
    }
    
    private void addFragmentSourceContainers(Collection<IJavaProject> projectList, 
                                             Collection<ISourceContainer> allContainers) throws JavaModelException {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoSourcePathComputerDelegate.addFragmentSourceContainers");
        
        Set<IPath> processed = new HashSet<IPath>();
        for (IJavaProject project : projectList) {
            IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
            if (roots != null) {
                for (IPackageFragmentRoot root : roots) {
                    if (root.isExternal()) {
                        IPath path = root.getPath();
                        if (processed.contains(path)) {
                            continue;
                        }
                        processed.add(path);
                    }
                    if (allContainers.add(new PackageFragmentRootSourceContainer(root))) {
                        Trace.trace(Trace.INFO, "GeronimoSourcePathComputerDelegate: Added fragment source container: " + root.getPath(), Activator.traceCore);
                    }
                }
            }          
        }  
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoSourcePathComputerDelegate.addFragmentSourceContainers");       
    }
    
    private void addServerSourceContainers(IServer server, Set<ISourceContainer> allContainers) {
        IRuntime runtime = server.getRuntime();
        IGeronimoRuntime gRuntime = (IGeronimoRuntime) runtime.getAdapter(IGeronimoRuntime.class);
        if (gRuntime != null) {
            IPath sourcePath = gRuntime.getRuntimeSourceLocation();
            if (sourcePath != null) {
                File file = sourcePath.toFile();
                if (file.isFile()) {
                    ExternalArchiveSourceContainer sourceContainer = new ExternalArchiveSourceContainer(file.getAbsolutePath(), true);
                    allContainers.add(sourceContainer);
                } else if (file.isDirectory()) {
                    // TODO implement me using DirectorySourceContainer
                }
            }
        }
    }

    private void processModules(IModule[] modules, Set<IProject> projects, Set<IJavaProject> javaProjects, IServer server, IProgressMonitor monitor) {
        for (int i = 0; i < modules.length; i++) {
            IProject project = modules[i].getProject();

            IModule[] childModules = server.getChildModules(new IModule[] { modules[i] }, monitor);
            if (childModules != null && childModules.length > 0) {
                processModules(childModules, projects, javaProjects, server, monitor);
            }

            if (project != null) {
                processProject(projects, javaProjects, project);
            }
        }
    }

    private void processProject(Set<IProject> projects, Set<IJavaProject> javaProjects, IProject project) {
        projects.add(project);

        IProject[] referencedProjects = getReferencedProjects(project);
        if (referencedProjects != null) {
            for (int j = 0; j < referencedProjects.length; j++) {
                processProject(projects, javaProjects, referencedProjects[j]);
            }
        }

        IJavaProject javaProject = getJavaProject(project);
        if (javaProject != null) {
            javaProjects.add(javaProject);
        }
    }
    
    private static IProject[] getReferencedProjects(IProject project) {
        try {
            return project.getReferencedProjects();
        } catch (CoreException e) {
            // ignore
            return null;
        }
    }
    
    private static IJavaProject getJavaProject(IProject project) {
        try {
            if (project.hasNature(JavaCore.NATURE_ID)) {
                return (IJavaProject) project.getNature(JavaCore.NATURE_ID);
            }
        } catch (CoreException e) {
            // ignore
        }
        return null;
    }
    
    private static void addAll(Collection<ISourceContainer> allContainers, ISourceContainer[] containers) {
        if (containers != null) {
            for (ISourceContainer container : containers) {
                allContainers.add(container);
            }
        }
    }
    
    private static String toString(ISourceContainer[] containers) {
        if (containers != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < containers.length; i++) {
                builder.append(containers[i].getName());
                if (i + 1 < containers.length) {
                    builder.append(", ");
                }                
            }
            return builder.toString();
        }
        return null;
    }
    
    private static String toString(Collection<IJavaProject> projects) {
        if (projects != null) {
            StringBuilder builder = new StringBuilder();
            Iterator<IJavaProject> iterator = projects.iterator();
            while (iterator.hasNext()) {
                builder.append(iterator.next().getProject().getName());
                if (iterator.hasNext()) {
                    builder.append(", ");
                }
            }
            return builder.toString();
        }
        return null;
    }
    
    private  GeronimoServerBehaviourDelegate getDelegate(IServer server) {  
        GeronimoServerBehaviourDelegate delegate = 
            (GeronimoServerBehaviourDelegate) server.loadAdapter(GeronimoServerBehaviourDelegate.class, null);
        return delegate;
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
