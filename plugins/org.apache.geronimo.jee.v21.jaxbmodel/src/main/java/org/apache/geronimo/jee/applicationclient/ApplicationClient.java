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

package org.apache.geronimo.jee.applicationclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.deployment.AbstractService;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.security.SubjectInfo;


/**
 * <p>Java class for application-clientType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="application-clientType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}client-environment"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}server-environment"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}gbean-ref" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}ejb-ref" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}service-ref" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}resource-ref" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}resource-env-ref" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}message-destination" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/security-2.0}default-subject" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="realm-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="callback-handler" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="resource" type="{http://geronimo.apache.org/xml/ns/j2ee/application-client-2.0}resourceType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}service" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "application-clientType", propOrder = {
    "clientEnvironment",
    "serverEnvironment",
    "gbeanRef",
    "ejbRef",
    "serviceRef",
    "resourceRef",
    "resourceEnvRef",
    "messageDestination",
    "defaultSubject",
    "realmName",
    "callbackHandler",
    "resource",
    "service"
})
public class ApplicationClient
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "client-environment", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", required = true)
    protected Environment clientEnvironment;
    @XmlElement(name = "server-environment", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", required = true)
    protected Environment serverEnvironment;
    @XmlElement(name = "gbean-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<GbeanRef> gbeanRef;
    @XmlElement(name = "ejb-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<EjbRef> ejbRef;
    @XmlElement(name = "service-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<ServiceRef> serviceRef;
    @XmlElement(name = "resource-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<ResourceRef> resourceRef;
    @XmlElement(name = "resource-env-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<ResourceEnvRef> resourceEnvRef;
    @XmlElement(name = "message-destination", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<MessageDestination> messageDestination;
    @XmlElement(name = "default-subject", namespace = "http://geronimo.apache.org/xml/ns/security-2.0")
    protected SubjectInfo defaultSubject;
    @XmlElement(name = "realm-name")
    protected String realmName;
    @XmlElement(name = "callback-handler")
    protected String callbackHandler;
    protected List<Resource> resource;
    @XmlElementRef(name = "service", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractService>> service;

    /**
     * 
     *                         Reference to client-environment element defined in
     *                         imported "geronimo-module-1.2.xsd"
     *                     
     * 
     * @return
     *     possible object is
     *     {@link Environment}
     *     
     */
    public Environment getClientEnvironment() {
        return clientEnvironment;
    }

    /**
     * 
     *                         Reference to client-environment element defined in
     *                         imported "geronimo-module-1.2.xsd"
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link Environment}
     *     
     */
    public void setClientEnvironment(Environment value) {
        this.clientEnvironment = value;
        this.clientEnvironment.getModuleId().setArtifactId(value.getModuleId().getArtifactId()+"ClientSide");
    }

    /**
     * 
     *                         Reference to server-environment element defined in
     *                         imported "geronimo-module-1.2.xsd"
     *                     
     * 
     * @return
     *     possible object is
     *     {@link Environment}
     *     
     */
    public Environment getServerEnvironment() {
        return serverEnvironment;
    }

    /**
     * 
     *                         Reference to server-environment element defined in
     *                         imported "geronimo-module-1.2.xsd"
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link Environment}
     *     
     */
    public void setServerEnvironment(Environment value) {
        this.serverEnvironment = value;
        this.serverEnvironment.getModuleId().setArtifactId(value.getModuleId().getArtifactId()+"ServerSide");
    }

    /**
     * 
     *                         Reference to gbean-ref element defined in imported
     *                         "geronimo-naming-1.2.xsd"
     *                     Gets the value of the gbeanRef property.
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
     * {@link GbeanRef}
     * 
     * 
     */
    public List<GbeanRef> getGbeanRef() {
        if (gbeanRef == null) {
            gbeanRef = new ArrayList<GbeanRef>();
        }
        return this.gbeanRef;
    }

    /**
     * 
     *                         Reference to ejb-ref element defined in imported
     *                         "geronimo-naming-1.2.xsd"
     *                     Gets the value of the ejbRef property.
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
     * 
     *                         Reference to service-ref element defined in imported
     *                         "geronimo-naming-1.2.xsd"
     *                     Gets the value of the serviceRef property.
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
     * 
     *                         Reference to resource-ref element defined in imported
     *                         "geronimo-naming-1.2.xsd"
     *                     Gets the value of the resourceRef property.
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
     * 
     *                         Reference to resource-env-ref element defined in
     *                         imported "geronimo-naming-1.2.xsd"
     *                     Gets the value of the resourceEnvRef property.
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
     * 
     *                         Reference to default-subject element defined in
     *                         imported "geronimo-security-2.0.xsd"
     *                         This is the subject run under if you are not logged in.
     *                     
     * 
     * @return
     *     possible object is
     *     {@link SubjectInfo}
     *     
     */
    public SubjectInfo getDefaultSubject() {
        return defaultSubject;
    }

    /**
     * 
     *                         Reference to default-subject element defined in
     *                         imported "geronimo-security-2.0.xsd"
     *                         This is the subject run under if you are not logged in.
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectInfo}
     *     
     */
    public void setDefaultSubject(SubjectInfo value) {
        this.defaultSubject = value;
    }

    /**
     * Gets the value of the realmName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRealmName() {
        return realmName;
    }

    /**
     * Sets the value of the realmName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRealmName(String value) {
        if (value == null || value.length() == 0)
            this.realmName = null;
        else
            this.realmName = value;
    }

    /**
     * Gets the value of the callbackHandler property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCallbackHandler() {
        return callbackHandler;
    }

    /**
     * Sets the value of the callbackHandler property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCallbackHandler(String value) {
        if (value == null || value.length() == 0)
            this.callbackHandler = null;
        else
            this.callbackHandler = value;
    }

    /**
     * Gets the value of the resource property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resource property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResource().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Resource }
     * 
     * 
     */
    public List<Resource> getResource() {
        if (resource == null) {
            resource = new ArrayList<Resource>();
        }
        return this.resource;
    }

    /**
     * 
     *                         Reference to service element defined in imported
     *                         "geronimo-module-1.2.xsd"
     *                     Gets the value of the service property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the service property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractService}{@code >}
     * {@link JAXBElement }{@code <}{@link Gbean}{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractService>> getService() {
        if (service == null) {
            service = new ArrayList<JAXBElement<? extends AbstractService>>();
        }
        return this.service;
    }

}
