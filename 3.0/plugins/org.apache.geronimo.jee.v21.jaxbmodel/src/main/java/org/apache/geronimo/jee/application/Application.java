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

package org.apache.geronimo.jee.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.deployment.AbstractService;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;


/**
 * 
 *                 The complex type for root element, it defines the elements of
 *                 root element for Geronimo enterprise application deployment
 *                 plan. Basically it is a sequence of elements environment,
 *                 module, ext-module, security, and services.
 *             
 * 
 * <p>Java class for applicationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="applicationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}environment"/>
 *         &lt;element name="module" type="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}moduleType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ext-module" type="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}ext-moduleType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}security" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}service" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="application-name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "applicationType", propOrder = {
    "environment",
    "module",
    "extModule",
    "security",
    "service"
})
public class Application
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", required = true)
    protected Environment environment;
    protected List<Module> module;
    @XmlElement(name = "ext-module")
    protected List<ExtModule> extModule;
    @XmlElementRef(name = "security", namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", type = JAXBElement.class)
    protected JAXBElement<? extends AbstractSecurity> security;
    @XmlElementRef(name = "service", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractService>> service;
    @XmlAttribute(name = "application-name")
    protected java.lang.String applicationName;

    /**
     * 
     *                         Reference to environment element defined in imported
     *                         "geronimo-module-1.2.xsd"
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
     *                         Reference to environment element defined in imported
     *                         "geronimo-module-1.2.xsd"
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
     * Gets the value of the module property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the module property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Module }
     * 
     * 
     */
    public List<Module> getModule() {
        if (module == null) {
            module = new ArrayList<Module>();
        }
        return this.module;
    }

    /**
     * Gets the value of the extModule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the extModule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExtModule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExtModule }
     * 
     * 
     */
    public List<ExtModule> getExtModule() {
        if (extModule == null) {
            extModule = new ArrayList<ExtModule>();
        }
        return this.extModule;
    }

    /**
     * 
     *                         Reference to security element defined in this schema. If
     *                         this optional element is present, all web and EJB
     *                         modules must make the appropriate access checks as
     *                         outlined in the JACC spec. This element groups the
     *                         security role mapping settings for the application.
     *                     
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security}{@code >}
     *     
     */
    public JAXBElement<? extends AbstractSecurity> getSecurity() {
        return security;
    }

    /**
     * 
     *                         Reference to security element defined in this schema. If
     *                         this optional element is present, all web and EJB
     *                         modules must make the appropriate access checks as
     *                         outlined in the JACC spec. This element groups the
     *                         security role mapping settings for the application.
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AbstractSecurity }{@code >}
     *     {@link JAXBElement }{@code <}{@link org.apache.geronimo.jee.security.Security}{@code >}
     *     
     */
    public void setSecurity(JAXBElement<? extends AbstractSecurity> value) {
        this.security = ((JAXBElement<? extends AbstractSecurity> ) value);
    }

    /**
     * 
     *                         Reference to service element defined in imported
     *                         "geronimo-module-1.2.xsd".
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
     * {@link JAXBElement }{@code <}{@link Gbean}{@code >}
     * {@link JAXBElement }{@code <}{@link AbstractService}{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractService>> getService() {
        if (service == null) {
            service = new ArrayList<JAXBElement<? extends AbstractService>>();
        }
        return this.service;
    }

    /**
     * Gets the value of the applicationName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the value of the applicationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setApplicationName(java.lang.String value) {
        if (value == null || value.length() == 0)
            this.applicationName = null;
        else
            this.applicationName = value;
    }

}
