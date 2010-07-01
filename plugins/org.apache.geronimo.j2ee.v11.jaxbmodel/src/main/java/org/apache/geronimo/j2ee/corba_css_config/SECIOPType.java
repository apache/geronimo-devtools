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


package org.apache.geronimo.j2ee.corba_css_config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SECIOPType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SECIOPType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.openejb.org/xml/ns/corba-css-config-2.0}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="supports" type="{http://www.openejb.org/xml/ns/corba-css-config-2.0}associationOptionList"/>
 *         &lt;element name="requires" type="{http://www.openejb.org/xml/ns/corba-css-config-2.0}associationOptionList"/>
 *         &lt;group ref="{http://www.openejb.org/xml/ns/corba-css-config-2.0}trustGroup"/>
 *       &lt;/sequence>
 *       &lt;attribute name="mechOID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="targetName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SECIOPType", propOrder = {
    "description",
    "supports",
    "requires",
    "trustEveryone",
    "trustNoone",
    "trustList"
})
public class SECIOPType {

    protected List<DescriptionType> description;
    @XmlList
    @XmlElement(required = true)
    protected List<AssociationOption> supports;
    @XmlList
    @XmlElement(required = true)
    protected List<AssociationOption> requires;
    protected TrustEveryoneType trustEveryone;
    protected TrustNooneType trustNoone;
    protected org.apache.geronimo.j2ee.corba_css_config.SSLType.TrustList trustList;
    @XmlAttribute
    protected String mechOID;
    @XmlAttribute
    protected String targetName;

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
     * Gets the value of the supports property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supports property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupports().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssociationOption }
     * 
     * 
     */
    public List<AssociationOption> getSupports() {
        if (supports == null) {
            supports = new ArrayList<AssociationOption>();
        }
        return this.supports;
    }

    /**
     * Gets the value of the requires property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requires property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequires().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AssociationOption }
     * 
     * 
     */
    public List<AssociationOption> getRequires() {
        if (requires == null) {
            requires = new ArrayList<AssociationOption>();
        }
        return this.requires;
    }

    /**
     * Gets the value of the trustEveryone property.
     * 
     * @return
     *     possible object is
     *     {@link TrustEveryoneType }
     *     
     */
    public TrustEveryoneType getTrustEveryone() {
        return trustEveryone;
    }

    /**
     * Sets the value of the trustEveryone property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrustEveryoneType }
     *     
     */
    public void setTrustEveryone(TrustEveryoneType value) {
        this.trustEveryone = value;
    }

    /**
     * Gets the value of the trustNoone property.
     * 
     * @return
     *     possible object is
     *     {@link TrustNooneType }
     *     
     */
    public TrustNooneType getTrustNoone() {
        return trustNoone;
    }

    /**
     * Sets the value of the trustNoone property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrustNooneType }
     *     
     */
    public void setTrustNoone(TrustNooneType value) {
        this.trustNoone = value;
    }

    /**
     * Gets the value of the trustList property.
     * 
     * @return
     *     possible object is
     *     {@link org.apache.geronimo.j2ee.corba_css_config.SSLType.TrustList }
     *     
     */
    public org.apache.geronimo.j2ee.corba_css_config.SSLType.TrustList getTrustList() {
        return trustList;
    }

    /**
     * Sets the value of the trustList property.
     * 
     * @param value
     *     allowed object is
     *     {@link org.apache.geronimo.j2ee.corba_css_config.SSLType.TrustList }
     *     
     */
    public void setTrustList(org.apache.geronimo.j2ee.corba_css_config.SSLType.TrustList value) {
        this.trustList = value;
    }

    /**
     * Gets the value of the mechOID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMechOID() {
        return mechOID;
    }

    /**
     * Sets the value of the mechOID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMechOID(String value) {
        this.mechOID = value;
    }

    /**
     * Gets the value of the targetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * Sets the value of the targetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetName(String value) {
        this.targetName = value;
    }

}
