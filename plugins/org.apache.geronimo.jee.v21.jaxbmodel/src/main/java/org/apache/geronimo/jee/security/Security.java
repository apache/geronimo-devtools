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

package org.apache.geronimo.jee.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.application.AbstractSecurity;
import org.apache.geronimo.jee.deployment.Pattern;


/**
 * 
 *                 Security entries
 * 
 *                 If this element is present, all web and EJB modules MUST make the
 *                 appropriate access checks as outlined in the JACC spec.
 *             
 * 
 * <p>Java class for securityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="securityType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}abstract-securityType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://geronimo.apache.org/xml/ns/security-2.0}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="credential-store-ref" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}patternType" minOccurs="0"/>
 *         &lt;element name="default-subject" type="{http://geronimo.apache.org/xml/ns/security-2.0}subject-infoType" minOccurs="0"/>
 *         &lt;element name="role-mappings" type="{http://geronimo.apache.org/xml/ns/security-2.0}role-mappingsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="default-role" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="doas-current-caller" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="use-context-handler" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "securityType", propOrder = {
    "description",
    "credentialStoreRef",
    "defaultSubject",
    "roleMappings"
})
public class Security
    extends AbstractSecurity
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected List<Description> description;
    @XmlElement(name = "credential-store-ref")
    protected Pattern credentialStoreRef;
    @XmlElement(name = "default-subject")
    protected SubjectInfo defaultSubject;
    @XmlElement(name = "role-mappings")
    protected RoleMappings roleMappings;
    @XmlAttribute(name = "default-role")
    protected String defaultRole;
    @XmlAttribute(name = "doas-current-caller")
    protected Boolean doasCurrentCaller;
    @XmlAttribute(name = "use-context-handler")
    protected Boolean useContextHandler;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Description }
     * 
     * 
     */
    public List<Description> getDescription() {
        if (description == null) {
            description = new ArrayList<Description>();
        }
        return this.description;
    }

    /**
     * Gets the value of the credentialStoreRef property.
     * 
     * @return
     *     possible object is
     *     {@link Pattern }
     *     
     */
    public Pattern getCredentialStoreRef() {
        return credentialStoreRef;
    }

    /**
     * Sets the value of the credentialStoreRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pattern }
     *     
     */
    public void setCredentialStoreRef(Pattern value) {
        this.credentialStoreRef = value;
    }

    /**
     * Gets the value of the defaultSubject property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectInfo }
     *     
     */
    public SubjectInfo getDefaultSubject() {
        return defaultSubject;
    }

    /**
     * Sets the value of the defaultSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectInfo }
     *     
     */
    public void setDefaultSubject(SubjectInfo value) {
        this.defaultSubject = value;
    }

    /**
     * Gets the value of the roleMappings property.
     * 
     * @return
     *     possible object is
     *     {@link RoleMappings }
     *     
     */
    public RoleMappings getRoleMappings() {
        return roleMappings;
    }

    /**
     * Sets the value of the roleMappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleMappings }
     *     
     */
    public void setRoleMappings(RoleMappings value) {
        this.roleMappings = value;
    }

    /**
     * Gets the value of the defaultRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefaultRole() {
        return defaultRole;
    }

    /**
     * Sets the value of the defaultRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefaultRole(String value) {
        if (value == null || value.length() == 0)
            this.defaultRole = null;
        else
            this.defaultRole = value;
    }

    /**
     * Gets the value of the doasCurrentCaller property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDoasCurrentCaller() {
        if (doasCurrentCaller == null) {
            return false;
        } else {
            return doasCurrentCaller;
        }
    }

    /**
     * Sets the value of the doasCurrentCaller property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDoasCurrentCaller(Boolean value) {
        this.doasCurrentCaller = value;
    }

    /**
     * Gets the value of the useContextHandler property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUseContextHandler() {
        if (useContextHandler == null) {
            return false;
        } else {
            return useContextHandler;
        }
    }

    /**
     * Sets the value of the useContextHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseContextHandler(Boolean value) {
        this.useContextHandler = value;
    }

}
