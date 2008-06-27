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

package org.apache.geronimo.jee.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.application.AbstractSecurity;
import org.apache.geronimo.jee.deployment.AbstractService;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.naming.AbstractNamingEntry;
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.GbeanLocator;
import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.persistence.Persistence;


/**
 * <p>Java class for web-app complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="web-app">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}environment" minOccurs="0"/>
 *         &lt;element name="context-root" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="work-dir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}web-container" minOccurs="0"/>
 *         &lt;element name="container-config" type="{http://geronimo.apache.org/xml/ns/j2ee/web-2.0.1}container-configType" minOccurs="0"/>
 *         &lt;group ref="{http://geronimo.apache.org/xml/ns/naming-1.2}jndiEnvironmentRefsGroup"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}message-destination" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="security-realm-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}security" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}service" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{http://java.sun.com/xml/ns/persistence}persistence"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "web-appType", propOrder = {
    "environment",
    "contextRoot",
    "workDir",
    "webContainer",
    "containerConfig",
    "abstractNamingEntry",
    "ejbRef",
    "ejbLocalRef",
    "serviceRef",
    "resourceRef",
    "resourceEnvRef",
    "messageDestination",
    "securityRealmName",
    "security",
    "serviceOrPersistence"
})
public class WebApp
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2")
    protected Environment environment;
    @XmlElement(name = "context-root")
    protected String contextRoot;
    @XmlElement(name = "work-dir")
    protected String workDir;
    @XmlElement(name = "web-container", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected GbeanLocator webContainer;
    @XmlElement(name = "container-config")
    protected ContainerConfig containerConfig;
    @XmlElementRef(name = "abstract-naming-entry", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractNamingEntry>> abstractNamingEntry;
    @XmlElement(name = "ejb-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<EjbRef> ejbRef;
    @XmlElement(name = "ejb-local-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<EjbLocalRef> ejbLocalRef;
    @XmlElement(name = "service-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<ServiceRef> serviceRef;
    @XmlElement(name = "resource-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<ResourceRef> resourceRef;
    @XmlElement(name = "resource-env-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<ResourceEnvRef> resourceEnvRef;
    @XmlElement(name = "message-destination", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<MessageDestination> messageDestination;
    @XmlElement(name = "security-realm-name")
    protected String securityRealmName;
    @XmlElementRef(name = "security", namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractSecurity> security;
    @XmlElementRefs({
        @XmlElementRef(name = "service", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "persistence", namespace = "http://java.sun.com/xml/ns/persistence", type = Persistence.class)
    })
    protected List<Serializable> serviceOrPersistence;

    /**
     *                          
     *                         This is the first part of the URL used to access the web application.
     *                         For example context-root of "Sample-App" will have URL of 
     *                         http://host:port/Sample-App" and a context-root of "/" would be make this the default web application to the server.
     * 
     *                         If the web application is packaged as an EAR that can use application context
     *                         in the "application.xml". This element is necessary unless you want context root to default to the WAR 
     *                         name.
     *                     
     * 
     * @return
     *     possible object is
     *     {@link Environment}
     *     
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     *                          
     *                         This is the first part of the URL used to access the web application.
     *                         For example context-root of "Sample-App" will have URL of 
     *                         http://host:port/Sample-App" and a context-root of "/" would be make this the default web application to the server.
     * 
     *                         If the web application is packaged as an EAR that can use application context
     *                         in the "application.xml". This element is necessary unless you want context root to default to the WAR 
     *                         name.
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link Environment}
     *     
     */
    public void setEnvironment(Environment value) {
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
        if (value == null || value.length() == 0)
            this.contextRoot = null;
        else
            this.contextRoot = value;
    }

    /**
     * Gets the value of the workDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkDir() {
        return workDir;
    }

    /**
     * Sets the value of the workDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkDir(String value) {
        if (value == null || value.length() == 0)
            this.workDir = null;
        else
            this.workDir = value;
    }

    /**
     * Gets the value of the webContainer property.
     * 
     * @return
     *     possible object is
     *     {@link GbeanLocator}
     *     
     */
    public GbeanLocator getWebContainer() {
        return webContainer;
    }

    /**
     * Sets the value of the webContainer property.
     * 
     * @param value
     *     allowed object is
     *     {@link GbeanLocator}
     *     
     */
    public void setWebContainer(GbeanLocator value) {
        this.webContainer = value;
    }

    /**
     * Gets the value of the containerConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ContainerConfig}
     *     
     */
    public ContainerConfig getContainerConfig() {
        return containerConfig;
    }

    /**
     * Sets the value of the containerConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContainerConfig}
     *     
     */
    public void setContainerConfig(ContainerConfig value) {
        this.containerConfig = value;
    }

    /**
     * Gets the value of the abstractNamingEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractNamingEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractNamingEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link GbeanRef}{@code >}
     * {@link JAXBElement }{@code <}{@link PersistenceContextRef}{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractNamingEntry}{@code >}
     * {@link JAXBElement }{@code <}{@link PersistenceUnitRef}{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractNamingEntry>> getAbstractNamingEntry() {
        if (abstractNamingEntry == null) {
            abstractNamingEntry = new ArrayList<JAXBElement<? extends AbstractNamingEntry>>();
        }
        return this.abstractNamingEntry;
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
     * {@link EjbRef}
     * 
     * 
     */
    public List<EjbRef> getEjbRef() {
        if (ejbRef == null) {
            ejbRef = new ArrayList<EjbRef>();
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
     * {@link EjbLocalRef}
     * 
     * 
     */
    public List<EjbLocalRef> getEjbLocalRef() {
        if (ejbLocalRef == null) {
            ejbLocalRef = new ArrayList<EjbLocalRef>();
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
     * {@link ServiceRef}
     * 
     * 
     */
    public List<ServiceRef> getServiceRef() {
        if (serviceRef == null) {
            serviceRef = new ArrayList<ServiceRef>();
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
     * {@link ResourceRef}
     * 
     * 
     */
    public List<ResourceRef> getResourceRef() {
        if (resourceRef == null) {
            resourceRef = new ArrayList<ResourceRef>();
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
     * {@link ResourceEnvRef}
     * 
     * 
     */
    public List<ResourceEnvRef> getResourceEnvRef() {
        if (resourceEnvRef == null) {
            resourceEnvRef = new ArrayList<ResourceEnvRef>();
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
     * {@link MessageDestination}
     * 
     * 
     */
    public List<MessageDestination> getMessageDestination() {
        if (messageDestination == null) {
            messageDestination = new ArrayList<MessageDestination>();
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
        if (value == null || value.length() == 0)
            this.securityRealmName = null;
        else
            this.securityRealmName = value;
    }

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity}{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security}{@code >}
     *     
     */
    public JAXBElement<? extends AbstractSecurity> getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity}{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security}{@code >}
     *     
     */
    public void setSecurity(JAXBElement<? extends AbstractSecurity> value) {
        this.security = ((JAXBElement<? extends AbstractSecurity> ) value);
    }

    /**
     * Gets the value of the serviceOrPersistence property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceOrPersistence property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceOrPersistence().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractService}{@code >}
     * {@link JAXBElement }{@code <}{@link Gbean}{@code >}
     * {@link Persistence }
     * 
     * 
     */
    public List<Serializable> getServiceOrPersistence() {
        if (serviceOrPersistence == null) {
            serviceOrPersistence = new ArrayList<Serializable>();
        }
        return this.serviceOrPersistence;
    }

}
