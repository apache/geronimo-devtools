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


package org.apache.geronimo.j2ee.applicationclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.j2ee.connector.ConnectorType;


/**
 * <p>Java class for resourceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resourceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="external-rar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="internal-rar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}connector"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resourceType", propOrder = {
    "externalRar",
    "internalRar",
    "connector"
})
public class ResourceType {

    @XmlElement(name = "external-rar")
    protected String externalRar;
    @XmlElement(name = "internal-rar")
    protected String internalRar;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/j2ee/connector-1.1", required = true)
    protected ConnectorType connector;

    /**
     * Gets the value of the externalRar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalRar() {
        return externalRar;
    }

    /**
     * Sets the value of the externalRar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalRar(String value) {
        this.externalRar = value;
    }

    /**
     * Gets the value of the internalRar property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalRar() {
        return internalRar;
    }

    /**
     * Sets the value of the internalRar property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalRar(String value) {
        this.internalRar = value;
    }

    /**
     * Gets the value of the connector property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectorType }
     *     
     */
    public ConnectorType getConnector() {
        return connector;
    }

    /**
     * Sets the value of the connector property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectorType }
     *     
     */
    public void setConnector(ConnectorType value) {
        this.connector = value;
    }

}
