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

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.st.v30.core.internal.Messages;
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
        return Messages.targetRuntime;
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

        //TODO: review list here when server 3.0 is ready
        IPath javaMailSpec    = runtime.getLocation().append("repository/org/apache/geronimo/javamail/geronimo-javamail_1.4_mail/");
        IPath jstlSpec        = runtime.getLocation().append("repository/org/apache/geronimo/bundles/jstl/");
        IPath myfacesSpec     = runtime.getLocation().append("repository/org/apache/myfaces/core/myfaces-api/");
        IPath myfacesImplSpec = runtime.getLocation().append("repository/org/apache/myfaces/core/myfaces-impl/");
        IPath jdbcSpec        = runtime.getLocation().append("repository/org/apache/geronimo/framework/geronimo-jdbc/");
        addLibraryEntries(list, javaMailSpec.toFile(),      true);
        addLibraryEntries(list, jstlSpec.toFile(),          true);
        addLibraryEntries(list, myfacesSpec.toFile(),       true);
        addLibraryEntries(list, myfacesImplSpec.toFile(),   true);
        addLibraryEntries(list, jdbcSpec.toFile(),          true);

        return(IClasspathEntry[])list.toArray(new IClasspathEntry[list.size()]);
    }
}
