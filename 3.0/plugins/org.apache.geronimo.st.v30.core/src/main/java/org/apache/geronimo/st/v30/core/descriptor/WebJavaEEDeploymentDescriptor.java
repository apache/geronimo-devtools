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
package org.apache.geronimo.st.v30.core.descriptor;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jst.javaee.web.WebApp;

/**
 * @version $Rev$ $Date$
 */
public class WebJavaEEDeploymentDescriptor extends AbstractDeploymentDescriptor implements
        WebDeploymentDescriptor {

    HashMap<String, String> requiredInfo;

    public WebJavaEEDeploymentDescriptor(WebApp webApp) {
        super(webApp);
        requiredInfo = new HashMap<String, String>();
        requiredInfo.put("class", "org.eclipse.jst.javaee.web.WebApp");
    }

    public List<String> getEjbLocalRefs() {
        requiredInfo.put("infoGetter", "getEjbLocalRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.javaee.core.internal.impl.EjbLocalRefImpl");
        requiredInfo.put("nameGetter", "getEjbRefName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getEjbRefs() {
        requiredInfo.put("infoGetter", "getEjbRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.javaee.core.internal.impl.EjbRefImpl");
        requiredInfo.put("nameGetter", "getEjbRefName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getMessageDestinations() {
        requiredInfo.put("infoGetter", "getMessageDestinations");
        requiredInfo
                .put("implClass", "org.eclipse.jst.javaee.core.internal.impl.MessageDestinationImpl");
        requiredInfo.put("nameGetter", "getMessageDestinationName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getResourceEnvRefs() {
        requiredInfo.put("infoGetter", "getResourceEnvRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.javaee.core.internal.impl.ResourceEnvRefImpl");
        requiredInfo.put("nameGetter", "getResourceEnvRefName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getResourceRefs() {
        requiredInfo.put("infoGetter", "getResourceRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.javaee.core.internal.impl.ResourceRefImpl");
        requiredInfo.put("nameGetter", "getResRefName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    public List<String> getSecurityRoles() {
        requiredInfo.put("infoGetter", "getSecurityRoles");
        requiredInfo.put("implClass", "org.eclipse.jst.javaee.core.internal.impl.SecurityRoleImpl");
        requiredInfo.put("nameGetter", "getRoleName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }

    /*public List<String> getServiceRefs() {
        requiredInfo.put("infoGetter", "getServiceRefs");
        requiredInfo.put("implClass", "org.eclipse.jst.javaee.core.internal.impl.ServiceRefImpl");
        requiredInfo.put("nameGetter", "getServiceRefName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }*/

}
