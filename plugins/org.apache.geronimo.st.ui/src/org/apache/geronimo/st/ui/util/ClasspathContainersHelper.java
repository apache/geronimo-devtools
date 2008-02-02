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
package org.apache.geronimo.st.ui.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.geronimo.st.ui.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
    // Query the workspace for the set of classpath containers
    //
    public static List<String> queryWorkspace() {
        Trace.tracePoint("ENTRY", "ClasspathContainersHelper.queryWorkspace");

        ArrayList<String> containers = new ArrayList<String>();

        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

        for ( IProject project : projects ) {

            try {
                if ( project.getNature(JavaCore.NATURE_ID) !=null ) {

                    IJavaProject javaProject = JavaCore.create(project);
                    IClasspathEntry[] cp = javaProject.getRawClasspath();

                    for ( IClasspathEntry cpEntry : cp ) {
                        int kind = cpEntry.getEntryKind();
                        if (kind == IClasspathEntry.CPE_CONTAINER) {
                            addEntry( containers, cpEntry.getPath().toString());
                        }
                    }
                }
            }
            catch ( CoreException e ) {
                e.printStackTrace();
            }
        }

        Trace.tracePoint("ENTRY", "ClasspathContainersHelper.queryWorkspace", containers);
        return containers;
    }


    //
    // Ensure no duplicates
    //
    public static void addEntry( List<String> containers, String container ) {

        if ( containers.indexOf( container ) < 0 ) {
            containers.add( container );
        }
    }
}