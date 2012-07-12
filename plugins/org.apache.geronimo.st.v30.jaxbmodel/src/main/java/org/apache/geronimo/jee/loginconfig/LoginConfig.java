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

package org.apache.geronimo.jee.loginconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 Defines the list of login modules for a login configuration
 *                 represented by a GenericSecurityRealm
 *             
 * 
 * <p>Java class for login-configType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="login-configType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="login-module-ref" type="{http://geronimo.apache.org/xml/ns/loginconfig-2.0}login-module-refType"/>
 *           &lt;element name="login-module" type="{http://geronimo.apache.org/xml/ns/loginconfig-2.0}login-moduleType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "login-configType", propOrder = {
    "loginModuleRefOrLoginModule"
})
public class LoginConfig
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElements({
        @XmlElement(name = "login-module", type = LoginModule.class),
        @XmlElement(name = "login-module-ref", type = LoginModuleRef.class)
    })
    protected List<AbstractLoginModule> loginModuleRefOrLoginModule;

    /**
     * Gets the value of the loginModuleRefOrLoginModule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the loginModuleRefOrLoginModule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLoginModuleRefOrLoginModule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LoginModule }
     * {@link LoginModuleRef }
     * 
     * 
     */
    public List<AbstractLoginModule> getLoginModuleRefOrLoginModule() {
        if (loginModuleRefOrLoginModule == null) {
            loginModuleRefOrLoginModule = new ArrayList<AbstractLoginModule>();
        }
        return this.loginModuleRefOrLoginModule;
    }

}
