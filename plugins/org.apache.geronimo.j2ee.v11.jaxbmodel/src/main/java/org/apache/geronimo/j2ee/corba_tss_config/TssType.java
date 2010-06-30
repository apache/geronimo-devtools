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
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.j2ee.security.DefaultPrincipalType;


/**
 * <p>Java class for tssType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tssType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="default-principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}default-principalType" minOccurs="0"/>
 *         &lt;group ref="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}transportMechGroup" minOccurs="0"/>
 *         &lt;element name="compoundSecMechTypeList" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="compoundSecMech" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}compoundSecMechType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="stateful" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="inherit" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tssType", propOrder = {
    "description",
    "defaultPrincipal",
    "ssl",
    "seciop",
    "compoundSecMechTypeList"
})
public class TssType {

    protected List<DescriptionType> description;
    @XmlElement(name = "default-principal")
    protected DefaultPrincipalType defaultPrincipal;
    @XmlElement(name = "SSL")
    protected SSLType ssl;
    @XmlElement(name = "SECIOP")
    protected SECIOPType seciop;
    protected TssType.CompoundSecMechTypeList compoundSecMechTypeList;
    @XmlAttribute
    protected Boolean inherit;

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
     * Gets the value of the defaultPrincipal property.
     * 
     * @return
     *     possible object is
     *     {@link DefaultPrincipalType }
     *     
     */
    public DefaultPrincipalType getDefaultPrincipal() {
        return defaultPrincipal;
    }

    /**
     * Sets the value of the defaultPrincipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefaultPrincipalType }
     *     
     */
    public void setDefaultPrincipal(DefaultPrincipalType value) {
        this.defaultPrincipal = value;
    }

    /**
     * Gets the value of the ssl property.
     * 
     * @return
     *     possible object is
     *     {@link SSLType }
     *     
     */
    public SSLType getSSL() {
        return ssl;
    }

    /**
     * Sets the value of the ssl property.
     * 
     * @param value
     *     allowed object is
     *     {@link SSLType }
     *     
     */
    public void setSSL(SSLType value) {
        this.ssl = value;
    }

    /**
     * Gets the value of the seciop property.
     * 
     * @return
     *     possible object is
     *     {@link SECIOPType }
     *     
     */
    public SECIOPType getSECIOP() {
        return seciop;
    }

    /**
     * Sets the value of the seciop property.
     * 
     * @param value
     *     allowed object is
     *     {@link SECIOPType }
     *     
     */
    public void setSECIOP(SECIOPType value) {
        this.seciop = value;
    }

    /**
     * Gets the value of the compoundSecMechTypeList property.
     * 
     * @return
     *     possible object is
     *     {@link TssType.CompoundSecMechTypeList }
     *     
     */
    public TssType.CompoundSecMechTypeList getCompoundSecMechTypeList() {
        return compoundSecMechTypeList;
    }

    /**
     * Sets the value of the compoundSecMechTypeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TssType.CompoundSecMechTypeList }
     *     
     */
    public void setCompoundSecMechTypeList(TssType.CompoundSecMechTypeList value) {
        this.compoundSecMechTypeList = value;
    }

    /**
     * Gets the value of the inherit property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInherit() {
        if (inherit == null) {
            return false;
        } else {
            return inherit;
        }
    }

    /**
     * Sets the value of the inherit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInherit(Boolean value) {
        this.inherit = value;
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
     *         &lt;element name="compoundSecMech" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}compoundSecMechType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *       &lt;attribute name="stateful" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "compoundSecMech"
    })
    public static class CompoundSecMechTypeList {

        @XmlElement(required = true)
        protected List<CompoundSecMechType> compoundSecMech;
        @XmlAttribute
        protected Boolean stateful;

        /**
         * Gets the value of the compoundSecMech property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the compoundSecMech property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCompoundSecMech().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CompoundSecMechType }
         * 
         * 
         */
        public List<CompoundSecMechType> getCompoundSecMech() {
            if (compoundSecMech == null) {
                compoundSecMech = new ArrayList<CompoundSecMechType>();
            }
            return this.compoundSecMech;
        }

        /**
         * Gets the value of the stateful property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public boolean isStateful() {
            if (stateful == null) {
                return false;
            } else {
                return stateful;
            }
        }

        /**
         * Sets the value of the stateful property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setStateful(Boolean value) {
            this.stateful = value;
        }

    }

}
