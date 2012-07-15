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
 * <p>Java class for adminobject-instanceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adminobject-instanceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="message-destination-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="config-property-setting" type="{http://geronimo.apache.org/xml/ns/j2ee/connector-1.1}config-property-settingType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adminobject-instanceType", propOrder = {
    "messageDestinationName",
    "configPropertySetting"
})
public class AdminobjectInstanceType {

    @XmlElement(name = "message-destination-name", required = true)
    protected String messageDestinationName;
    @XmlElement(name = "config-property-setting")
    protected List<ConfigPropertySettingType> configPropertySetting;

    /**
     * Gets the value of the messageDestinationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageDestinationName() {
        return messageDestinationName;
    }

    /**
     * Sets the value of the messageDestinationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageDestinationName(String value) {
        this.messageDestinationName = value;
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

}
