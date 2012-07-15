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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;


/**
 * 
 *                 Mirrors the moduleType defined by application_1_4.xsd and adds
 *                 an optional alt-dd element defining a Geronimo specific
 *                 deployment descriptor for J2EE connector, ejb, web, or java
 *                 client modules.
 *             
 * 
 * <p>Java class for moduleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="moduleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="connector" type="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}pathType"/>
 *           &lt;element name="ejb" type="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}pathType"/>
 *           &lt;element name="java" type="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}pathType"/>
 *           &lt;element name="web" type="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}pathType"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="alt-dd" type="{http://geronimo.apache.org/xml/ns/j2ee/application-2.0}pathType"/>
 *           &lt;any/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "moduleType", propOrder = {
    "connector",
    "ejb",
    "java",
    "web",
    "altDd",
    "any"
})
public class Module
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected Path connector;
    protected Path ejb;
    protected Path java;
    protected Path web;
    @XmlElement(name = "alt-dd")
    protected Path altDd;
    @XmlAnyElement(lax = true)
    protected Object any;

    /**
     * Gets the value of the connector property.
     * 
     * @return
     *     possible object is
     *     {@link Path }
     *     
     */
    public Path getConnector() {
        return connector;
    }

    /**
     * Sets the value of the connector property.
     * 
     * @param value
     *     allowed object is
     *     {@link Path }
     *     
     */
    public void setConnector(Path value) {
        this.connector = value;
    }

    /**
     * Gets the value of the ejb property.
     * 
     * @return
     *     possible object is
     *     {@link Path }
     *     
     */
    public Path getEjb() {
        return ejb;
    }

    /**
     * Sets the value of the ejb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Path }
     *     
     */
    public void setEjb(Path value) {
        this.ejb = value;
    }

    /**
     * Gets the value of the java property.
     * 
     * @return
     *     possible object is
     *     {@link Path }
     *     
     */
    public Path getJava() {
        return java;
    }

    /**
     * Sets the value of the java property.
     * 
     * @param value
     *     allowed object is
     *     {@link Path }
     *     
     */
    public void setJava(Path value) {
        this.java = value;
    }

    /**
     * Gets the value of the web property.
     * 
     * @return
     *     possible object is
     *     {@link Path }
     *     
     */
    public Path getWeb() {
        return web;
    }

    /**
     * Sets the value of the web property.
     * 
     * @param value
     *     allowed object is
     *     {@link Path }
     *     
     */
    public void setWeb(Path value) {
        this.web = value;
    }

    /**
     * Gets the value of the altDd property.
     * 
     * @return
     *     possible object is
     *     {@link Path }
     *     
     */
    public Path getAltDd() {
        return altDd;
    }

    /**
     * Sets the value of the altDd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Path }
     *     
     */
    public void setAltDd(Path value) {
        if (value.getValue().length() > 0)
            this.altDd = value;
        else
            this.altDd = null;
    }

    /**
     * Gets the value of the any property.
     * 
     * @return
     *     possible object is
     *     {@link Element }
     *     {@link Object }
     *     
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *     allowed object is
     *     {@link Element }
     *     {@link Object }
     *     
     */
    public void setAny(Object value) {
        this.any = value;
    }

}
