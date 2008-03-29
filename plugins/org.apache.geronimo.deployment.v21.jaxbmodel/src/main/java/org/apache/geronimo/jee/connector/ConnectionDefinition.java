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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 The connection-definitionType defines a set of connection
 *                 interfaces and classes pertaining to a particular connection
 *                 type. This also includes configurable properties for
 *                 ManagedConnectionFactory instances that may be produced out of
 *                 this set.
 * 
 *                 The name element uniquely identifies this instance
 *             
 * 
 * <p>Java class for connection-definitionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="connection-definitionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="connectionfactory-interface" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}fully-qualified-classType"/>
 *         &lt;element name="connectiondefinition-instance" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}connectiondefinition-instanceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "connection-definitionType", propOrder = {
    "connectionfactoryInterface",
    "connectiondefinitionInstance"
})
public class ConnectionDefinition
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "connectionfactory-interface", required = true)
    protected String connectionfactoryInterface;
    @XmlElement(name = "connectiondefinition-instance")
    protected List<ConnectiondefinitionInstance> connectiondefinitionInstance;

    /**
     * Gets the value of the connectionfactoryInterface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConnectionfactoryInterface() {
        return connectionfactoryInterface;
    }

    /**
     * Sets the value of the connectionfactoryInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConnectionfactoryInterface(String value) {
        this.connectionfactoryInterface = value;
    }

    /**
     * Gets the value of the connectiondefinitionInstance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the connectiondefinitionInstance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConnectiondefinitionInstance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConnectiondefinitionInstance }
     * 
     * 
     */
    public List<ConnectiondefinitionInstance> getConnectiondefinitionInstance() {
        if (connectiondefinitionInstance == null) {
            connectiondefinitionInstance = new ArrayList<ConnectiondefinitionInstance>();
        }
        return this.connectiondefinitionInstance;
    }

}
