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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for authModule complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="authModule">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="classLoaderName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestPolicy" type="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}messagePolicy" minOccurs="0"/>
 *         &lt;element name="responsePolicy" type="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}messagePolicy" minOccurs="0"/>
 *         &lt;element name="options" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authModule", propOrder = {
    "className",
    "classLoaderName",
    "requestPolicy",
    "responsePolicy",
    "options"
})
public class AuthModule {

    @XmlElement(required = true)
    protected String className;
    protected String classLoaderName;
    protected MessagePolicy requestPolicy;
    protected MessagePolicy responsePolicy;
    protected String options;

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

    /**
     * Gets the value of the requestPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link MessagePolicy }
     *     
     */
    public MessagePolicy getRequestPolicy() {
        return requestPolicy;
    }

    /**
     * Sets the value of the requestPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessagePolicy }
     *     
     */
    public void setRequestPolicy(MessagePolicy value) {
        this.requestPolicy = value;
    }

    /**
     * Gets the value of the responsePolicy property.
     * 
     * @return
     *     possible object is
     *     {@link MessagePolicy }
     *     
     */
    public MessagePolicy getResponsePolicy() {
        return responsePolicy;
    }

    /**
     * Sets the value of the responsePolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessagePolicy }
     *     
     */
    public void setResponsePolicy(MessagePolicy value) {
        this.responsePolicy = value;
    }

    /**
     * Gets the value of the options property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptions() {
        return options;
    }

    /**
     * Sets the value of the options property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptions(String value) {
        this.options = value;
    }

}
