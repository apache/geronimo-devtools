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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.deployment.Pattern;
import org.apache.geronimo.jee.connector.Connector;


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
 *           &lt;element name="external-rar" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}patternType"/>
 *           &lt;element name="internal-rar" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.2}connector"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resourceType", propOrder = {
    "externalRar",
    "internalRar",
    "connector"
})
public class Resource
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "external-rar")
    protected Pattern externalRar;
    @XmlElement(name = "internal-rar")
    protected String internalRar;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/j2ee/connector-1.2", required = true)
    protected Connector connector;

    /**
     * Gets the value of the externalRar property.
     * 
     * @return
     *     possible object is
     *     {@link Pattern}
     *     
     */
    public Pattern getExternalRar() {
        return externalRar;
    }

    /**
     * Sets the value of the externalRar property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pattern}
     *     
     */
    public void setExternalRar(Pattern value) {
        this.externalRar = value;
    }

    /**
     * Gets the value of the internalRar property.
     * 
     * @return
     *     possible object is
     *     {@link String}
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
     *     {@link String}
     *     
     */
    public void setInternalRar(String value) {
        this.internalRar = value;
    }

    /**
     * 
     *                         This element contains the contents of the Resource Group
     *                         deployment plan. The content is no different that the
     *                         content of the deployment plan would have been for the
     *                         server-wide or application-scoped resource group.
     *                     
     * 
     * @return
     *     possible object is
     *     {@link Connector}
     *     
     */
    public Connector getConnector() {
        return connector;
    }

    /**
     * 
     *                         This element contains the contents of the Resource Group
     *                         deployment plan. The content is no different that the
     *                         content of the deployment plan would have been for the
     *                         server-wide or application-scoped resource group.
     *                     
     * 
     * @param value
     *     allowed object is
     *     {@link Connector}
     *     
     */
    public void setConnector(Connector value) {
        this.connector = value;
    }

}
