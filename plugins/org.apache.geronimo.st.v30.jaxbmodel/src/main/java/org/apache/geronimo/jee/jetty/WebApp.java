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

package org.apache.geronimo.jee.jetty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import org.apache.geronimo.jee.deployment.AbstractService;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.application.AbstractClustering;
import org.apache.geronimo.jee.application.AbstractSecurity;
import org.apache.geronimo.jee.naming.AbstractNamingEntry;
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.EnvEntry;
import org.apache.geronimo.jee.naming.GbeanLocator;
import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.security.SecurityRef;


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
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}environment" minOccurs="0"/>
 *         &lt;element name="context-root" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="work-dir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}clustering" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}web-container" minOccurs="0"/>
 *         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="virtual-host" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="session-manager" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="compact-path" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;group ref="{http://geronimo.apache.org/xml/ns/naming-1.2}jndiEnvironmentRefsGroup"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}message-destination" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="security-realm-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="authentication" type="{http://geronimo.apache.org/xml/ns/j2ee/web/jetty-2.0.2}authenticationType" minOccurs="0"/>
 *           &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}security" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}service"/>
 *           &lt;any/>
 *         &lt;/choice>
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
    "workDir",
    "clustering",
    "webContainer",
    "host",
    "virtualHost",
    "sessionManager",
    "compactPath",
    "abstractNamingEntry",
    "envEntry",
    "ejbRef",
    "ejbLocalRef",
    "serviceRef",
    "resourceRef",
    "resourceEnvRef",
    "messageDestination",
    "securityRealmName",
    "authentication",
    "security",
    "serviceOrAny"
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
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0")
    protected AbstractClustering clustering;
    @XmlElement(name = "web-container", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected GbeanLocator webContainer;
    protected List<String> host;
    @XmlElement(name = "virtual-host")
    protected List<String> virtualHost;
    @XmlElement(name = "session-manager")
    protected String sessionManager;
    @XmlElement(name = "compact-path")
    protected Boolean compactPath;
    @XmlElementRef(name = "abstract-naming-entry", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractNamingEntry>> abstractNamingEntry;
    @XmlElement(name = "env-entry", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<EnvEntry> envEntry;
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
    protected Authentication authentication;
    @XmlElementRef(name = "security", namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractSecurity> security;
    @XmlElementRef(name = "service", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", type = JAXBElement.class)
    @XmlAnyElement(lax = true)
    protected List<Object> serviceOrAny;

    /**
     * 
     *                         Reference to environment element defined in imported
     *                         "geronimo-module-1.2.xsd"
     *                     
     * 
     * @return
     *     possible object is
     *     {@link EnvironmentType }
     *     
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * 
     *                         Reference to environment element defined in imported
     *                         "geronimo-module-1.2.xsd"
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link EnvironmentType }
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
        this.workDir = value;
    }

    /**
     * 
     *                         Reference to abstract clustering element defined in
     *                         imported "geronimo-application-2.0.xsd"
     *                     
     * 
     * @return
     *     possible object is
     *     {@link AbstractClusteringType }
     *     
     */
    public AbstractClustering getClustering() {
        return clustering;
    }

    /**
     * 
     *                         Reference to abstract clustering element defined in
     *                         imported "geronimo-application-2.0.xsd"
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link AbstractClusteringType }
     *     
     */
    public void setClustering(AbstractClustering value) {
        this.clustering = value;
    }

    /**
     * 
     *                         Reference to web-container element defined in imported
     *                         "geronimo-naming-1.2.xsd"
     *                     
     * 
     * @return
     *     possible object is
     *     {@link GbeanLocatorType }
     *     
     */
    public GbeanLocator getWebContainer() {
        return webContainer;
    }

    /**
     * 
     *                         Reference to web-container element defined in imported
     *                         "geronimo-naming-1.2.xsd"
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link GbeanLocatorType }
     *     
     */
    public void setWebContainer(GbeanLocator value) {
        this.webContainer = value;
    }

    /**
     * Gets the value of the host property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the host property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHost().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getHost() {
        if (host == null) {
            host = new ArrayList<String>();
        }
        return this.host;
    }

    /**
     * Gets the value of the virtualHost property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the virtualHost property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVirtualHost().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getVirtualHost() {
        if (virtualHost == null) {
            virtualHost = new ArrayList<String>();
        }
        return this.virtualHost;
    }

    /**
     * Gets the value of the sessionManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSessionManager() {
        return sessionManager;
    }

    /**
     * Sets the value of the sessionManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSessionManager(String value) {
        this.sessionManager = value;
    }

    /**
     * Gets the value of the compactPath property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCompactPath() {
        return compactPath;
    }

    /**
     * Sets the value of the compactPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCompactPath(Boolean value) {
        this.compactPath = value;
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
     * {@link JAXBElement }{@code <}{@link PersistenceUnitRefType }{@code >}
     * {@link JAXBElement }{@code <}{@link GbeanRefType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractNamingEntryType }{@code >}
     * {@link JAXBElement }{@code <}{@link PersistenceContextRefType }{@code >}
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
     * Gets the value of the envEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the envEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnvEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnvEntryType }
     * 
     * 
     */
    public List<EnvEntry> getEnvEntry() {
        if (envEntry == null) {
            envEntry = new ArrayList<EnvEntry>();
        }
        return this.envEntry;
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
     * {@link EjbLocalRefType }
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
     * {@link ServiceRefType }
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
     * {@link ResourceRefType }
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
     * {@link ResourceEnvRefType }
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
     * 
     *                         Reference to message-destination element defined in
     *                         imported "geronimo-naming-1.2.xsd"
     *                     Gets the value of the messageDestination property.
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
        this.securityRealmName = value;
    }

    /**
     * Gets the value of the authentication property.
     * 
     * @return
     *     possible object is
     *     {@link Authentication }
     *     
     */
    public Authentication getAuthentication() {
        return authentication;
    }

    /**
     * Sets the value of the authentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link Authentication }
     *     
     */
    public void setAuthentication(Authentication value) {
        this.authentication = value;
    }

    /**
     * 
     *                             Reference to security element defined in imported
     *                             "geronimo-security-1.2.xsd"
     *                         
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurityType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link SecurityRefType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.subject_info.Security }{@code >}
     *     
     */
    public JAXBElement<? extends AbstractSecurity> getSecurity() {
        return security;
    }

    /**
     * 
     *                             Reference to security element defined in imported
     *                             "geronimo-security-1.2.xsd"
     *                         
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurityType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link SecurityRefType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.subject_info.Security }{@code >}
     *     
     */
    public void setSecurity(JAXBElement<? extends AbstractSecurity> value) {
        this.security = ((JAXBElement<? extends AbstractSecurity> ) value);
    }

    /**
     * Gets the value of the serviceOrAny property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceOrAny property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceOrAny().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link GbeanType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractService }{@code >}
     * {@link Object }
     * 
     * 
     */
    public List<Object> getServiceOrAny() {
        if (serviceOrAny == null) {
            serviceOrAny = new ArrayList<Object>();
        }
        return this.serviceOrAny;
    }

}
