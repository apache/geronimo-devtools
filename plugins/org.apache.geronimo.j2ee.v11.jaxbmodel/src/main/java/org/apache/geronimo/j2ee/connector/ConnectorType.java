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


package org.apache.geronimo.j2ee.connector;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.deployment.GbeanType;


/**
 * 
 *                 The connectorType defines a resource adapter.
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
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/deployment-1.1}environment" minOccurs="0"/>
 *         &lt;element name="resourceadapter" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}resourceadapterType" maxOccurs="unbounded"/>
 *         &lt;element name="adminobject" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}adminobjectType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "connectorType", propOrder = {
    "environment",
    "resourceadapter",
    "adminobject",
    "gbean"
})
public class ConnectorType {

    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1")
    protected EnvironmentType environment;
    @XmlElement(required = true)
    protected List<ResourceadapterType> resourceadapter;
    protected List<AdminobjectType> adminobject;
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
     * {@link ResourceadapterType }
     * 
     * 
     */
    public List<ResourceadapterType> getResourceadapter() {
        if (resourceadapter == null) {
            resourceadapter = new ArrayList<ResourceadapterType>();
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
     * {@link AdminobjectType }
     * 
     * 
     */
    public List<AdminobjectType> getAdminobject() {
        if (adminobject == null) {
            adminobject = new ArrayList<AdminobjectType>();
        }
        return this.adminobject;
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
