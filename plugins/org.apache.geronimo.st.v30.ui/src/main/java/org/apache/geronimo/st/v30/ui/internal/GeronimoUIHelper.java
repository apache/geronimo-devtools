/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v30.ui.internal;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.deployment.XmlAttributeType;
import org.apache.geronimo.jee.loginconfig.LoginConfig;
import org.apache.geronimo.jee.loginconfig.LoginModule;

/**
 * 
 *
 * @version $Rev$ $Date$
 */
public class GeronimoUIHelper {
    @SuppressWarnings("unchecked")
    public static LoginModule getLoginModule(JAXBElement<?> ele) {
        Object loginObject = ((XmlAttributeType) ele.getValue()).getAny();
        LoginConfig loginConfig = null;
        if(loginObject instanceof JAXBElement) {
            loginConfig = (LoginConfig) ((JAXBElement<LoginConfig>) loginObject).getValue();
        } else if (loginObject instanceof LoginConfig) {
            loginConfig = (LoginConfig) loginObject;
        }
        if(loginConfig != null) {
            LoginModule loginModule = (LoginModule) ((LoginConfig)loginConfig).getLoginModuleRefOrLoginModule().get(0);
            return loginModule;
        }
        return null;
    }
}
