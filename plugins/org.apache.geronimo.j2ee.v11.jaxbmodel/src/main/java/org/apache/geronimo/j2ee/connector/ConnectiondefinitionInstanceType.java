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


/**
 * <p>Java class for connectiondefinition-instanceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="connectiondefinition-instanceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="implemented-interface" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}fully-qualified-classType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="config-property-setting" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}config-property-settingType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="connectionmanager" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}connectionmanagerType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "connectiondefinition-instanceType", propOrder = {
    "name",
    "implementedInterface",
    "configPropertySetting",
    "connectionmanager"
})
public class ConnectiondefinitionInstanceType {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(name = "implemented-interface")
    protected List<String> implementedInterface;
    @XmlElement(name = "config-property-setting")
    protected List<ConfigPropertySettingType> configPropertySetting;
    @XmlElement(required = true)
    protected ConnectionmanagerType connectionmanager;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the implementedInterface property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the implementedInterface property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImplementedInterface().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getImplementedInterface() {
        if (implementedInterface == null) {
            implementedInterface = new ArrayList<String>();
        }
        return this.implementedInterface;
    }

    /**
     * Gets the value of the configPropertySetting property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configPropertySetting property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigPropertySetting().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigPropertySettingType }
     * 
     * 
     */
    public List<ConfigPropertySettingType> getConfigPropertySetting() {
        if (configPropertySetting == null) {
            configPropertySetting = new ArrayList<ConfigPropertySettingType>();
        }
        return this.configPropertySetting;
    }

    /**
     * Gets the value of the connectionmanager property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionmanagerType }
     *     
     */
    public ConnectionmanagerType getConnectionmanager() {
        return connectionmanager;
    }

    /**
     * Sets the value of the connectionmanager property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionmanagerType }
     *     
     */
    public void setConnectionmanager(ConnectionmanagerType value) {
        this.connectionmanager = value;
    }

}
