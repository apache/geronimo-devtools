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

package org.apache.geronimo.jee.tomcatconfig;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tomcat-configType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tomcat-configType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="host" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cross-context" type="{http://geronimo.apache.org/xml/ns/web/tomcat/config-1.0}emptyType" minOccurs="0"/>
 *         &lt;element name="disable-cookies" type="{http://geronimo.apache.org/xml/ns/web/tomcat/config-1.0}emptyType" minOccurs="0"/>
 *         &lt;element name="valve-chain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listener-chain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tomcat-realm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="manager" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cluster" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tomcat-configType", propOrder = {
    "host",
    "crossContext",
    "disableCookies",
    "valveChain",
    "listenerChain",
    "tomcatRealm",
    "manager",
    "cluster"
})
public class TomcatConfig
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected String host;
    @XmlElement(name = "cross-context")
    protected Empty crossContext;
    @XmlElement(name = "disable-cookies")
    protected Empty disableCookies;
    @XmlElement(name = "valve-chain")
    protected String valveChain;
    @XmlElement(name = "listener-chain")
    protected String listenerChain;
    @XmlElement(name = "tomcat-realm")
    protected String tomcatRealm;
    protected String manager;
    protected String cluster;

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the value of the host property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHost(String value) {
        this.host = value;
    }

    /**
     * Gets the value of the crossContext property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getCrossContext() {
        return crossContext;
    }

    /**
     * Sets the value of the crossContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setCrossContext(Empty value) {
        this.crossContext = value;
    }

    /**
     * Gets the value of the disableCookies property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getDisableCookies() {
        return disableCookies;
    }

    /**
     * Sets the value of the disableCookies property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setDisableCookies(Empty value) {
        this.disableCookies = value;
    }

    /**
     * Gets the value of the valveChain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValveChain() {
        return valveChain;
    }

    /**
     * Sets the value of the valveChain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValveChain(String value) {
        this.valveChain = value;
    }

    /**
     * Gets the value of the listenerChain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListenerChain() {
        return listenerChain;
    }

    /**
     * Sets the value of the listenerChain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListenerChain(String value) {
        this.listenerChain = value;
    }

    /**
     * Gets the value of the tomcatRealm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTomcatRealm() {
        return tomcatRealm;
    }

    /**
     * Sets the value of the tomcatRealm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTomcatRealm(String value) {
        this.tomcatRealm = value;
    }

    /**
     * Gets the value of the manager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManager() {
        return manager;
    }

    /**
     * Sets the value of the manager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManager(String value) {
        this.manager = value;
    }

    /**
     * Gets the value of the cluster property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCluster() {
        return cluster;
    }

    /**
     * Sets the value of the cluster property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCluster(String value) {
        this.cluster = value;
    }

}
