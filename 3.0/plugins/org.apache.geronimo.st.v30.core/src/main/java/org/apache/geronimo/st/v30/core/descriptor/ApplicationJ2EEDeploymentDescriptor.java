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

import org.eclipse.jst.j2ee.application.Application;

/**
 * @version $Rev$ $Date$
 */
public class ApplicationJ2EEDeploymentDescriptor extends AbstractDeploymentDescriptor implements
        ApplicationDeploymentDescriptor {

    HashMap<String, String> requiredInfo;

    public ApplicationJ2EEDeploymentDescriptor(Application app) {
        super(app);
        requiredInfo = new HashMap<String, String>();
        requiredInfo.put("class", "org.eclipse.jst.j2ee.application.Application");
        requiredInfo.put("nameGetter", "getName");
    }

    public List<String> getSecurityRoles() {
        requiredInfo.put("infoGetter", "getSecurityRoles");
        requiredInfo.put("implClass", "org.eclipse.jst.j2ee.common.internal.impl.SecurityRoleImpl");
        requiredInfo.put("nameGetter", "getRoleName");
        return getDeploymentDescriptorInfo(requiredInfo);
    }
}
