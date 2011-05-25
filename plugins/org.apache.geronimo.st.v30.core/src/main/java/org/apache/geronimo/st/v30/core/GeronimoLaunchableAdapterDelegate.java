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

import java.net.URL;

import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.server.core.Servlet;
import org.eclipse.wst.server.core.IModuleArtifact;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IURLProvider;
import org.eclipse.wst.server.core.model.LaunchableAdapterDelegate;
import org.eclipse.wst.server.core.model.ServerDelegate;
import org.eclipse.wst.server.core.util.HttpLaunchable;
import org.eclipse.wst.server.core.util.WebResource;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoLaunchableAdapterDelegate extends LaunchableAdapterDelegate {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.LaunchableAdapterDelegate#getLaunchable(org.eclipse.wst.server.core.IServer,
     *      org.eclipse.wst.server.core.IModuleArtifact)
     */
    public Object getLaunchable(IServer server, IModuleArtifact moduleArtifact) throws CoreException {

        GeronimoServerDelegate delegate = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        if (delegate == null)
            delegate = (GeronimoServerDelegate) server.loadAdapter(GeronimoServerDelegate.class, new NullProgressMonitor());
        if (delegate == null)
            return null;

        if ((moduleArtifact instanceof Servlet)
                || (moduleArtifact instanceof WebResource))
            return getHttpLaunchable(moduleArtifact, delegate);

        return null;
    }

    private Object getHttpLaunchable(IModuleArtifact moduleObject, ServerDelegate delegate) throws CoreException {
        URL url = ((IURLProvider) delegate).getModuleRootURL(moduleObject.getModule());
        try {
            if (moduleObject instanceof Servlet) {
                Servlet servlet = (Servlet) moduleObject;
                if (servlet.getAlias() != null) {
                    String path = servlet.getAlias();
                    if (path.startsWith("/"))
                        path = path.substring(1);
                    url = new URL(url, path);
                } else
                    url = new URL(url, "servlet/"
                            + servlet.getServletClassName());
            } else if (moduleObject instanceof WebResource) {
                WebResource resource = (WebResource) moduleObject;
                String path = resource.getPath().toString();
                if (path != null && path.startsWith("/") && path.length() > 0)
                    path = path.substring(1);
                if (path != null && path.length() > 0)
                    url = new URL(url, path);
            }
            return new HttpLaunchable(url);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error getting URL for " + moduleObject, e, Activator.logCore);
            throw new CoreException (new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Error getting URL for " + moduleObject,e));
        }
    }

}
