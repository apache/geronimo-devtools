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
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.geronimo.jee.deployment.Pattern;
import org.w3c.dom.Element;


/**
 * 
 *                 It is used to define modules included in this application
 *                 externally and is not a part of the archive. It defines optional
 *                 internal-path or external-path to module/repository element
 *                 being referenced.
 *             
 * 
 * <p>Java class for ext-moduleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ext-moduleType">
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
 *           &lt;element name="internal-path" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *           &lt;element name="external-path" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}patternType"/>
 *         &lt;/choice>
 *         &lt;any/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ext-moduleType", propOrder = {
    "connector",
    "ejb",
    "java",
    "web",
    "internalPath",
    "externalPath",
    "any"
})
public class ExtModule
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected Path connector;
    protected Path ejb;
    protected Path java;
    protected Path web;
    @XmlElement(name = "internal-path")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected java.lang.String internalPath;
    @XmlElement(name = "external-path")
    protected Pattern externalPath;
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
     * Gets the value of the internalPath property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getInternalPath() {
        return internalPath;
    }

    /**
     * Sets the value of the internalPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setInternalPath(java.lang.String value) {
        if (value == null || value.length() == 0)
            this.internalPath = null;
        else
            this.internalPath = value;
    }

    /**
     * Gets the value of the externalPath property.
     * 
     * @return
     *     possible object is
     *     {@link Pattern }
     *     
     */
    public Pattern getExternalPath() {
        return externalPath;
    }

    /**
     * Sets the value of the externalPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pattern }
     *     
     */
    public void setExternalPath(Pattern value) {
        this.externalPath = value;
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
