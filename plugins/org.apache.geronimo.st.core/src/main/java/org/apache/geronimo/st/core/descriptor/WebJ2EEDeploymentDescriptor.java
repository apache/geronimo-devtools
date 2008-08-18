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
package org.apache.geronimo.st.core.descriptor;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jst.j2ee.webapplication.WebApp;

/**
 * WebJ2EEDeploymentDescriptor
 * 
 * @version $Rev$ $Date$
 */
public class WebJ2EEDeploymentDescriptor extends AbstractDeploymentDescriptor implements
        WebDeploymentDescriptor {

    HashMap<String, String> requiredInfo;

    public WebJ2EEDeploymentDescriptor(WebApp webApp) {
        super(webApp);
        requiredInfo = new HashMap<String, String>();
        requiredInfo.put("class", "org.eclipse.jst.j2ee.webapplication.WebApp");
        requiredInfo.put("nameGetter", "getName");
    }

    public List<String> getEjbLocalRefs() {
        requiredInfo.put("infoGetter", "getEjbLocalRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.EJBLocalRefImpl");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getEjbRefs() {
        requiredInfo.put("infoGetter", "getEjbRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.EjbRefImpl");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getMessageDestinations() {
        requiredInfo.put("infoGetter", "getMessageDestinations");
        requiredInfo
                .put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.MessageDestinationImpl");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getResourceEnvRefs() {
        requiredInfo.put("infoGetter", "getResourceEnvRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.ResourceEnvRefImpl");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getResourceRefs() {
        requiredInfo.put("infoGetter", "getResourceRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.ResourceRefImpl");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getSecurityRoles() {
        requiredInfo.put("infoGetter", "getSecurityRoles");
        requiredInfo.put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.SecurityRoleImpl");
        requiredInfo.put("nameGetter", "getRoleName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    /*public List<String> getServiceRefs() {
        requiredInfo.put("infoGetter", "getServiceRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.EjbRefImpl");
        return getDeploymentDescriptorInfo(requiredInfo);
    }*/

}
