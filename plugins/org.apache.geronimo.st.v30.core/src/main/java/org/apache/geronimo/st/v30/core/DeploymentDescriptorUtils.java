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

import org.apache.geronimo.st.v30.core.descriptor.AbstractDeploymentDescriptor;
import org.apache.geronimo.st.v30.core.descriptor.ApplicationJ2EEDeploymentDescriptor;
import org.apache.geronimo.st.v30.core.descriptor.ApplicationJavaEEDeploymentDescriptor;
import org.apache.geronimo.st.v30.core.descriptor.EjbJ2EEDeploymentDescriptor;
import org.apache.geronimo.st.v30.core.descriptor.EjbJavaEEDeploymentDescriptor;
import org.apache.geronimo.st.v30.core.descriptor.WebJ2EEDeploymentDescriptor;
import org.apache.geronimo.st.v30.core.descriptor.WebJavaEEDeploymentDescriptor;
import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;

/**
 * @version $Rev$ $Date$
 */
public class DeploymentDescriptorUtils {
    static final long serialVersionUID = 1L;

    public static AbstractDeploymentDescriptor getDeploymentDescriptor(IProject project) {
        IModelProvider mp = ModelProviderManager.getModelProvider(project);
        Object obj = mp.getModelObject();
        if (obj instanceof org.eclipse.jst.j2ee.webapplication.WebApp) {
            return (new WebJ2EEDeploymentDescriptor((org.eclipse.jst.j2ee.webapplication.WebApp) obj));
        }
        else if (obj instanceof org.eclipse.jst.javaee.web.WebApp) {
            return (new WebJavaEEDeploymentDescriptor((org.eclipse.jst.javaee.web.WebApp) obj));
        }
        else if (obj instanceof org.eclipse.jst.j2ee.application.Application) {
            return (new ApplicationJ2EEDeploymentDescriptor((org.eclipse.jst.j2ee.application.Application) obj));
        }
        else if (obj instanceof org.eclipse.jst.javaee.application.Application) {
            return (new ApplicationJavaEEDeploymentDescriptor((org.eclipse.jst.javaee.application.Application) obj));
        }
        else if (obj instanceof org.eclipse.jst.j2ee.ejb.EJBJar) {
            return (new EjbJ2EEDeploymentDescriptor((org.eclipse.jst.j2ee.ejb.EJBJar) obj));
        }
        else if (obj instanceof org.eclipse.jst.javaee.ejb.EJBJar) {
            return (new EjbJavaEEDeploymentDescriptor((org.eclipse.jst.javaee.ejb.EJBJar) obj));
        }
        return null;
    }

}
