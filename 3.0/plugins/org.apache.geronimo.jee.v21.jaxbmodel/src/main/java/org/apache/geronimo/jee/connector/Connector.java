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

package org.apache.geronimo.jee.connector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.deployment.AbstractService;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;


/**
 * 
 *                 The connector element is the root element of the deployment
 *                 descriptor for the resource adapter. Note that the sub-elements
 *                 of this element should be as in the given order in a sequence.
 *                 It includes geronimo specific information for the resource
 *                 adapter library.
 *             
 * 
 * <p>Java class for connectorType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="connectorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.2}environment" minOccurs="0"/>
 *         &lt;element name="resourceadapter" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}resourceadapterType" maxOccurs="unbounded"/>
 *         &lt;element name="adminobject" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}adminobjectType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "connectorType", propOrder = {
    "environment",
    "resourceadapter",
    "adminobject",
    "service"
})
public class Connector
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2")
    protected Environment environment;
    @XmlElement(required = true)
    protected List<Resourceadapter> resourceadapter;
    protected List<Adminobject> adminobject;
    @XmlElementRef(name = "service", namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractService>> service;

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
     * Gets the value of the resourceadapter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceadapter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceadapter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Resourceadapter }
     * 
     * 
     */
    public List<Resourceadapter> getResourceadapter() {
        if (resourceadapter == null) {
            resourceadapter = new ArrayList<Resourceadapter>();
        }
        return this.resourceadapter;
    }

    /**
     * Gets the value of the adminobject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the adminobject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAdminobject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Adminobject }
     * 
     * 
     */
    public List<Adminobject> getAdminobject() {
        if (adminobject == null) {
            adminobject = new ArrayList<Adminobject>();
        }
        return this.adminobject;
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

}
