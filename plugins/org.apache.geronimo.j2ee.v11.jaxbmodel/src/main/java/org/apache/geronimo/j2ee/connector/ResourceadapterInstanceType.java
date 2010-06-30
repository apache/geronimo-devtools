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

import org.apache.geronimo.j2ee.naming.GbeanLocatorType;


/**
 * 
 *                 The resourceadapter instance info is put in a separate optional element
 *                 to provide easier more consistent support for 1.0 adapters.
 *             
 * 
 * <p>Java class for resourceadapter-instanceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resourceadapter-instanceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resourceadapter-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="config-property-setting" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}config-property-settingType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/naming-1.1}workmanager"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resourceadapter-instanceType", propOrder = {
    "resourceadapterName",
    "configPropertySetting",
    "workmanager"
})
public class ResourceadapterInstanceType {

    @XmlElement(name = "resourceadapter-name", required = true)
    protected String resourceadapterName;
    @XmlElement(name = "config-property-setting")
    protected List<ConfigPropertySettingType> configPropertySetting;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", required = true)
    protected GbeanLocatorType workmanager;

    /**
     * Gets the value of the resourceadapterName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceadapterName() {
        return resourceadapterName;
    }

    /**
     * Sets the value of the resourceadapterName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceadapterName(String value) {
        this.resourceadapterName = value;
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
     * Gets the value of the workmanager property.
     * 
     * @return
     *     possible object is
     *     {@link GbeanLocatorType }
     *     
     */
    public GbeanLocatorType getWorkmanager() {
        return workmanager;
    }

    /**
     * Sets the value of the workmanager property.
     * 
     * @param value
     *     allowed object is
     *     {@link GbeanLocatorType }
     *     
     */
    public void setWorkmanager(GbeanLocatorType value) {
        this.workmanager = value;
    }

}
