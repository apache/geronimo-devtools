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

package org.apache.geronimo.jee.openejb;

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

import org.apache.geronimo.jee.application.AbstractClustering;
import org.apache.geronimo.jee.application.AbstractSecurity;
import org.apache.geronimo.jee.deployment.AbstractService;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.naming.AbstractNamingEntry;
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.EnvEntry;
import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.persistence.Persistence;
import org.apache.geronimo.jee.security.SecurityRef;


/**
 * <p>Java class for geronimo-ejb-jarType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="geronimo-ejb-jarType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}environment" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}clustering" minOccurs="0"/>
 *         &lt;element name="openejb-jar" type="{http://geronimo.apache.org/xml/ns/j2ee/ejb/openejb-2.0}openejb-jarType" minOccurs="0"/>
 *         &lt;group ref="{http://geronimo.apache.org/xml/ns/naming-1.2}jndiEnvironmentRefsGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.2}message-destination" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="tss-link" type="{http://geronimo.apache.org/xml/ns/j2ee/ejb/openejb-2.0}tss-linkType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="web-service-binding" type="{http://geronimo.apache.org/xml/ns/j2ee/ejb/openejb-2.0}web-service-bindingType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}security" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}service"/>
 *           &lt;element ref="{http://java.sun.com/xml/ns/persistence}persistence"/>
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
@XmlType(name = "geronimo-ejb-jarType", propOrder = {
    "environment",
    "clustering",
    "openejbJar",
    "jndiEnvironmentRefsGroup",
    "messageDestination",
    "tssLink",
    "webServiceBinding",
    "security",
    "serviceOrPersistence"
})
public class GeronimoEjbJar
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2")
    protected Environment environment;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0")
    protected AbstractClustering clustering;
    @XmlElement(name = "openejb-jar")
    protected OpenejbJar openejbJar;
    @XmlElementRefs({
        @XmlElementRef(name = "ejb-local-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "env-entry", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "resource-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "resource-env-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "abstract-naming-entry", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "service-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "ejb-ref", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> jndiEnvironmentRefsGroup;
    @XmlElement(name = "message-destination", namespace = "http://geronimo.apache.org/xml/ns/naming-1.2")
    protected List<MessageDestination> messageDestination;
    @XmlElement(name = "tss-link")
    protected List<TssLink> tssLink;
    @XmlElement(name = "web-service-binding")
    protected List<WebServiceBinding> webServiceBinding;
    @XmlElementRef(name = "security", namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractSecurity> security;
    @XmlElementRefs({
        @XmlElementRef(name = "service", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", type = JAXBElement.class),
        @XmlElementRef(name = "persistence", namespace = "http://java.sun.com/xml/ns/persistence", type = Persistence.class)
    })
    protected List<Serializable> serviceOrPersistence;

    /**
     * Gets the value of the environment property.
     * 
     * @return
     *     possible object is
     *     {@link Environment }
     *     
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Sets the value of the environment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Environment }
     *     
     */
    public void setEnvironment(Environment value) {
        this.environment = value;
    }

    /**
     * 
     *                         Reference to abstract clustering element defined in
     *                         imported "geronimo-application-2.0.xsd"
     *                     
     * 
     * @return
     *     possible object is
     *     {@link AbstractClustering }
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
     *     {@link AbstractClustering }
     *     
     */
    public void setClustering(AbstractClustering value) {
        this.clustering = value;
    }

    /**
     * Gets the value of the openejbJar property.
     * 
     * @return
     *     possible object is
     *     {@link OpenejbJar }
     *     
     */
    public OpenejbJar getOpenejbJar() {
        return openejbJar;
    }

    /**
     * Sets the value of the openejbJar property.
     * 
     * @param value
     *     allowed object is
     *     {@link OpenejbJar }
     *     
     */
    public void setOpenejbJar(OpenejbJar value) {
        this.openejbJar = value;
    }

    /**
     * Gets the value of the jndiEnvironmentRefsGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jndiEnvironmentRefsGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJndiEnvironmentRefsGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link EjbLocalRef }{@code >}
     * {@link JAXBElement }{@code <}{@link EnvEntry }{@code >}
     * {@link JAXBElement }{@code <}{@link ResourceRef }{@code >}
     * {@link JAXBElement }{@code <}{@link ResourceEnvRef }{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractNamingEntry }{@code >}
     * {@link JAXBElement }{@code <}{@link PersistenceUnitRef }{@code >}
     * {@link JAXBElement }{@code <}{@link PersistenceContextRef }{@code >}
     * {@link JAXBElement }{@code <}{@link ServiceRef }{@code >}
     * {@link JAXBElement }{@code <}{@link GbeanRef }{@code >}
     * {@link JAXBElement }{@code <}{@link EjbRef }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getJndiEnvironmentRefsGroup() {
        if (jndiEnvironmentRefsGroup == null) {
            jndiEnvironmentRefsGroup = new ArrayList<JAXBElement<?>>();
        }
        return this.jndiEnvironmentRefsGroup;
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
     * {@link MessageDestination }
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
     * Gets the value of the tssLink property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tssLink property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTssLink().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TssLink }
     * 
     * 
     */
    public List<TssLink> getTssLink() {
        if (tssLink == null) {
            tssLink = new ArrayList<TssLink>();
        }
        return this.tssLink;
    }

    /**
     * Gets the value of the webServiceBinding property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the webServiceBinding property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebServiceBinding().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WebServiceBinding }
     * 
     * 
     */
    public List<WebServiceBinding> getWebServiceBinding() {
        if (webServiceBinding == null) {
            webServiceBinding = new ArrayList<WebServiceBinding>();
        }
        return this.webServiceBinding;
    }

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.subject_info.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.Security.ns.security_1.SecurityType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity }{@code >}
     *     {@link JAXBElement }{@code <}{@link SecurityRef }{@code >}
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
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.subject_info.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.Security.ns.security_1.SecurityType }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security }{@code >}
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity }{@code >}
     *     {@link JAXBElement }{@code <}{@link SecurityRef }{@code >}
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
     * {@link Persistence }
     * {@link JAXBElement }{@code <}{@link AbstractService }{@code >}
     * {@link JAXBElement }{@code <}{@link Gbean }{@code >}
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
