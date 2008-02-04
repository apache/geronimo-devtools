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
import java.util.Collections;
import java.util.List;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 *
 * Helper class for ClasspathContainers support
 *
 */
public class ClasspathContainersHelper {

    //
    // Query the workspace for the list of classpath containers and return as a 
    // sorted List<String> for display and for WTP to ultimately persist as server 
    // instance properties in servers.xml
    // 
    public static List<String> queryWorkspace() {
        Trace.tracePoint("ENTRY", "ClasspathContainersHelper.queryWorkspace");

        ArrayList<String> containers = new ArrayList<String>();

        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

        for ( IProject project : projects ) {

            try {

                if ( project.getNature(JavaCore.NATURE_ID) != null ) {

                    IJavaProject javaProject = JavaCore.create(project);
                    IClasspathEntry[] cp = javaProject.getRawClasspath();

                    for ( IClasspathEntry cpEntry : cp ) {

                        if (cpEntry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                            addEntry( containers, cpEntry.getPath().toString());
                        }
                    }
                }
            }
            catch ( CoreException e ) {
                e.printStackTrace();
            }
        }

        Collections.sort( containers );
        Trace.tracePoint("EXIT", "ClasspathContainersHelper.queryWorkspace", containers);
        return containers;
    }


    //
    // Query the workspace to find the classapth entries for a specific classpath container,
    // 
    public static List<IClasspathEntry> queryWorkspace( String containerPath ) {
        Trace.tracePoint("ENTRY", "ClasspathContainersHelper.queryWorkspace", containerPath );

        List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();

        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

        for ( IProject project : projects ) {

            try {

                if ( project.getNature(JavaCore.NATURE_ID) !=null ) {

                    IJavaProject javaProject = JavaCore.create(project);
                    IClasspathEntry[] cp = javaProject.getRawClasspath();

                    for ( IClasspathEntry cpEntry : cp ) {

                        if (cpEntry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {

                            if ( cpEntry.getPath().toString().equals( containerPath )) {

                                IClasspathContainer classpathContainer = JavaCore.getClasspathContainer(cpEntry.getPath(), javaProject);
                                IClasspathEntry[] containerEntries = classpathContainer.getClasspathEntries();

                                for (int ii = 0; ii  < containerEntries.length; ii++) {
                                    classpathEntries.add(containerEntries[ii]);
                                }
                            }
                        }
                    }
                }
            }
            catch ( CoreException e ) {
                e.printStackTrace();
            }
        }

        Trace.tracePoint("EXIT", "ClasspathContainersHelper.queryWorkspace", classpathEntries);
        return classpathEntries;
    }


    //
    // Ensure no duplicates in the list
    //
    private static void addEntry( List<String> containers, String container ) {

        if ( containers.indexOf( container ) < 0 ) {
            containers.add( container );
        }
    }
}