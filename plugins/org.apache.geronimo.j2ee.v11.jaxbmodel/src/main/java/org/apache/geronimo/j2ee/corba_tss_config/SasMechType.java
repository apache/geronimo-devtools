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


package org.apache.geronimo.j2ee.corba_tss_config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sasMechType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sasMechType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="serviceConfigurationList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;group ref="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}serviceConfigurationGroup" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="identityTokenTypes" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}identityTokenTypeList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sasMechType", propOrder = {
    "description",
    "serviceConfigurationList",
    "identityTokenTypes"
})
public class SasMechType {

    protected List<DescriptionType> description;
    protected SasMechType.ServiceConfigurationList serviceConfigurationList;
    @XmlElement(required = true)
    protected IdentityTokenTypeList identityTokenTypes;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DescriptionType }
     * 
     * 
     */
    public List<DescriptionType> getDescription() {
        if (description == null) {
            description = new ArrayList<DescriptionType>();
        }
        return this.description;
    }

    /**
     * Gets the value of the serviceConfigurationList property.
     * 
     * @return
     *     possible object is
     *     {@link SasMechType.ServiceConfigurationList }
     *     
     */
    public SasMechType.ServiceConfigurationList getServiceConfigurationList() {
        return serviceConfigurationList;
    }

    /**
     * Sets the value of the serviceConfigurationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SasMechType.ServiceConfigurationList }
     *     
     */
    public void setServiceConfigurationList(SasMechType.ServiceConfigurationList value) {
        this.serviceConfigurationList = value;
    }

    /**
     * Gets the value of the identityTokenTypes property.
     * 
     * @return
     *     possible object is
     *     {@link IdentityTokenTypeList }
     *     
     */
    public IdentityTokenTypeList getIdentityTokenTypes() {
        return identityTokenTypes;
    }

    /**
     * Sets the value of the identityTokenTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentityTokenTypeList }
     *     
     */
    public void setIdentityTokenTypes(IdentityTokenTypeList value) {
        this.identityTokenTypes = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;group ref="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}serviceConfigurationGroup" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "serviceConfigurationGroup"
    })
    public static class ServiceConfigurationList {

        @XmlElements({
            @XmlElement(name = "generalName", type = GeneralNameType.class),
            @XmlElement(name = "gssExportedName", type = GssExportedNameType.class)
        })
        protected List<Object> serviceConfigurationGroup;
        @XmlAttribute
        protected Boolean required;

        /**
         * Gets the value of the serviceConfigurationGroup property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the serviceConfigurationGroup property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getServiceConfigurationGroup().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GeneralNameType }
         * {@link GssExportedNameType }
         * 
         * 
         */
        public List<Object> getServiceConfigurationGroup() {
            if (serviceConfigurationGroup == null) {
                serviceConfigurationGroup = new ArrayList<Object>();
            }
            return this.serviceConfigurationGroup;
        }

        /**
         * Gets the value of the required property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isRequired() {
            if (required == null) {
                return false;
            } else {
                return required;
            }
        }

        /**
         * Sets the value of the required property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setRequired(Boolean value) {
            this.required = value;
        }

    }

}
