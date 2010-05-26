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
package org.apache.geronimo.jee.jaspi;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for configProvider complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configProvider">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageLayer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="appContext" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *             &lt;element name="properties" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="clientAuthConfig" type="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}clientAuthConfig" maxOccurs="unbounded" minOccurs="0"/>
 *             &lt;element name="serverAuthConfig" type="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}serverAuthConfig" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element name="persistent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="classLoaderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configProvider", propOrder = {
    "messageLayer",
    "appContext",
    "description",
    "className",
    "properties",
    "clientAuthConfig",
    "serverAuthConfig",
    "persistent",
    "classLoaderName"
})
public class ConfigProvider {

    protected String messageLayer;
    protected String appContext;
    protected String description;
    protected String className;
    protected String properties;
    protected List<ClientAuthConfig> clientAuthConfig;
    protected List<ServerAuthConfig> serverAuthConfig;
    protected Boolean persistent;
    protected String classLoaderName;

    /**
     * Gets the value of the messageLayer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageLayer() {
        return messageLayer;
    }

    /**
     * Sets the value of the messageLayer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageLayer(String value) {
        this.messageLayer = value;
    }

    /**
     * Gets the value of the appContext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppContext() {
        return appContext;
    }

    /**
     * Sets the value of the appContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppContext(String value) {
        this.appContext = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProperties(String value) {
        this.properties = value;
    }

    /**
     * Gets the value of the clientAuthConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clientAuthConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClientAuthConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ClientAuthConfig }
     * 
     * 
     */
    public List<ClientAuthConfig> getClientAuthConfig() {
        if (clientAuthConfig == null) {
            clientAuthConfig = new ArrayList<ClientAuthConfig>();
        }
        return this.clientAuthConfig;
    }

    /**
     * Gets the value of the serverAuthConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serverAuthConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServerAuthConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServerAuthConfig }
     * 
     * 
     */
    public List<ServerAuthConfig> getServerAuthConfig() {
        if (serverAuthConfig == null) {
            serverAuthConfig = new ArrayList<ServerAuthConfig>();
        }
        return this.serverAuthConfig;
    }

    /**
     * Gets the value of the persistent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPersistent() {
        return persistent;
    }

    /**
     * Sets the value of the persistent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPersistent(Boolean value) {
        this.persistent = value;
    }

    /**
     * Gets the value of the classLoaderName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassLoaderName() {
        return classLoaderName;
    }

    /**
     * Sets the value of the classLoaderName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassLoaderName(String value) {
        this.classLoaderName = value;
    }

}
