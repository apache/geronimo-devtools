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


package org.apache.geronimo.j2ee.web;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.deployment.GbeanType;
import org.apache.geronimo.j2ee.naming.EjbLocalRefType;
import org.apache.geronimo.j2ee.naming.EjbRefType;
import org.apache.geronimo.j2ee.naming.GbeanLocatorType;
import org.apache.geronimo.j2ee.naming.GbeanRefType;
import org.apache.geronimo.j2ee.naming.MessageDestinationType;
import org.apache.geronimo.j2ee.naming.ResourceEnvRefType;
import org.apache.geronimo.j2ee.naming.ResourceRefType;
import org.apache.geronimo.j2ee.naming.ServiceRefType;
import org.apache.geronimo.j2ee.security.SecurityType;


/**
 * <p>Java class for web-appType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="web-appType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.1}environment" minOccurs="0"/>
 *         &lt;element name="context-root" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.1}web-container" minOccurs="0"/>
 *         &lt;element name="container-config" type="{http://geronimo.apache.org/xml/ns/j2ee/web-1.1}container-configType" minOccurs="0"/>
 *         &lt;group ref="{http://geronimo.apache.org/xml/ns/naming-1.1}jndiEnvironmentRefsGroup"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.1}message-destination" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="security-realm-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element ref="{http://geronimo.apache.org/xml/ns/security-1.1}security" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.1}gbean" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "web-appType", propOrder = {
    "environment",
    "contextRoot",
    "webContainer",
    "containerConfig",
    "gbeanRef",
    "ejbRef",
    "ejbLocalRef",
    "serviceRef",
    "resourceRef",
    "resourceEnvRef",
    "messageDestination",
    "securityRealmName",
    "security",
    "gbean"
})
public class WebAppType {

    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1")
    protected EnvironmentType environment;
    @XmlElement(name = "context-root")
    protected String contextRoot;
    @XmlElement(name = "web-container", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected GbeanLocatorType webContainer;
    @XmlElement(name = "container-config")
    protected ContainerConfigType containerConfig;
    @XmlElement(name = "gbean-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected List<GbeanRefType> gbeanRef;
    @XmlElement(name = "ejb-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected List<EjbRefType> ejbRef;
    @XmlElement(name = "ejb-local-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected List<EjbLocalRefType> ejbLocalRef;
    @XmlElement(name = "service-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected List<ServiceRefType> serviceRef;
    @XmlElement(name = "resource-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected List<ResourceRefType> resourceRef;
    @XmlElement(name = "resource-env-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected List<ResourceEnvRefType> resourceEnvRef;
    @XmlElement(name = "message-destination", namespace = "http://geronimo.apache.org/xml/ns/naming-1.1")
    protected List<MessageDestinationType> messageDestination;
    @XmlElement(name = "security-realm-name")
    protected String securityRealmName;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/security-1.1")
    protected SecurityType security;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1")
    protected List<GbeanType> gbean;

    /**
     * Gets the value of the environment property.
     * 
     * @return
     *     possible object is
     *     {@link EnvironmentType }
     *     
     */
    public EnvironmentType getEnvironment() {
        return environment;
    }

    /**
     * Sets the value of the environment property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnvironmentType }
     *     
     */
    public void setEnvironment(EnvironmentType value) {
        this.environment = value;
    }

    /**
     * Gets the value of the contextRoot property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContextRoot() {
        return contextRoot;
    }

    /**
     * Sets the value of the contextRoot property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContextRoot(String value) {
        this.contextRoot = value;
    }

    /**
     * Gets the value of the webContainer property.
     * 
     * @return
     *     possible object is
     *     {@link GbeanLocatorType }
     *     
     */
    public GbeanLocatorType getWebContainer() {
        return webContainer;
    }

    /**
     * Sets the value of the webContainer property.
     * 
     * @param value
     *     allowed object is
     *     {@link GbeanLocatorType }
     *     
     */
    public void setWebContainer(GbeanLocatorType value) {
        this.webContainer = value;
    }

    /**
     * Gets the value of the containerConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerConfigType }
     *     
     */
    public ContainerConfigType getContainerConfig() {
        return containerConfig;
    }

    /**
     * Sets the value of the containerConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerConfigType }
     *     
     */
    public void setContainerConfig(ContainerConfigType value) {
        this.containerConfig = value;
    }

    /**
     * Gets the value of the gbeanRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gbeanRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGbeanRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GbeanRefType }
     * 
     * 
     */
    public List<GbeanRefType> getGbeanRef() {
        if (gbeanRef == null) {
            gbeanRef = new ArrayList<GbeanRefType>();
        }
        return this.gbeanRef;
    }

    /**
     * Gets the value of the ejbRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ejbRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEjbRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EjbRefType }
     * 
     * 
     */
    public List<EjbRefType> getEjbRef() {
        if (ejbRef == null) {
            ejbRef = new ArrayList<EjbRefType>();
        }
        return this.ejbRef;
    }

    /**
     * Gets the value of the ejbLocalRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ejbLocalRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEjbLocalRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EjbLocalRefType }
     * 
     * 
     */
    public List<EjbLocalRefType> getEjbLocalRef() {
        if (ejbLocalRef == null) {
            ejbLocalRef = new ArrayList<EjbLocalRefType>();
        }
        return this.ejbLocalRef;
    }

    /**
     * Gets the value of the serviceRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceRefType }
     * 
     * 
     */
    public List<ServiceRefType> getServiceRef() {
        if (serviceRef == null) {
            serviceRef = new ArrayList<ServiceRefType>();
        }
        return this.serviceRef;
    }

    /**
     * Gets the value of the resourceRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceRefType }
     * 
     * 
     */
    public List<ResourceRefType> getResourceRef() {
        if (resourceRef == null) {
            resourceRef = new ArrayList<ResourceRefType>();
        }
        return this.resourceRef;
    }

    /**
     * Gets the value of the resourceEnvRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceEnvRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceEnvRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResourceEnvRefType }
     * 
     * 
     */
    public List<ResourceEnvRefType> getResourceEnvRef() {
        if (resourceEnvRef == null) {
            resourceEnvRef = new ArrayList<ResourceEnvRefType>();
        }
        return this.resourceEnvRef;
    }

    /**
     * Gets the value of the messageDestination property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageDestination property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageDestination().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageDestinationType }
     * 
     * 
     */
    public List<MessageDestinationType> getMessageDestination() {
        if (messageDestination == null) {
            messageDestination = new ArrayList<MessageDestinationType>();
        }
        return this.messageDestination;
    }

    /**
     * Gets the value of the securityRealmName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityRealmName() {
        return securityRealmName;
    }

    /**
     * Sets the value of the securityRealmName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityRealmName(String value) {
        this.securityRealmName = value;
    }

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityType }
     *     
     */
    public SecurityType getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityType }
     *     
     */
    public void setSecurity(SecurityType value) {
        this.security = value;
    }

    /**
     * Gets the value of the gbean property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gbean property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGbean().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GbeanType }
     * 
     * 
     */
    public List<GbeanType> getGbean() {
        if (gbean == null) {
            gbean = new ArrayList<GbeanType>();
        }
        return this.gbean;
    }

}
