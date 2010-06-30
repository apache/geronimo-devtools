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


package org.apache.geronimo.j2ee.security;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


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
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://geronimo.apache.org/xml/ns/security-1.1}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="default-principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}default-principalType"/>
 *         &lt;element name="role-mappings" type="{http://geronimo.apache.org/xml/ns/security-1.1}role-mappingsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="default-role" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="doas-current-caller" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="use-context-handler" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "securityType", propOrder = {
    "description",
    "defaultPrincipal",
    "roleMappings"
})
public class SecurityType {

    protected List<DescriptionType> description;
    @XmlElement(name = "default-principal", required = true)
    protected DefaultPrincipalType defaultPrincipal;
    @XmlElement(name = "role-mappings")
    protected RoleMappingsType roleMappings;
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
     * {@link DescriptionType }
     * 
     * 
     */
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    /**
     * Gets the value of the defaultPrincipal property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultPrincipalType }
     *     
     */
    public DefaultPrincipalType getDefaultPrincipal() {
        return defaultPrincipal;
    }

    /**
     * Sets the value of the defaultPrincipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultPrincipalType }
     *     
     */
    public void setDefaultPrincipal(DefaultPrincipalType value) {
        this.defaultPrincipal = value;
    }

    /**
     * Gets the value of the roleMappings property.
     * 
     * @return
     *     possible object is
     *     {@link RoleMappingsType }
     *     
     */
    public RoleMappingsType getRoleMappings() {
        return roleMappings;
    }

    /**
     * Sets the value of the roleMappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleMappingsType }
     *     
     */
    public void setRoleMappings(RoleMappingsType value) {
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
