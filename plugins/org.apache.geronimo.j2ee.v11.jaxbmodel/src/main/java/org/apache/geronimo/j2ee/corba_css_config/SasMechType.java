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
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="description" type="{http://www.openejb.org/xml/ns/corba-css-config-2.0}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;group ref="{http://www.openejb.org/xml/ns/corba-css-config-2.0}ittGroup"/>
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
    "ittAbsent",
    "ittAnonymous",
    "ittPrincipalNameStatic",
    "ittPrincipalNameDynamic"
})
public class SasMechType {

    protected List<DescriptionType> description;
    @XmlElement(name = "ITTAbsent")
    protected ITTAbsentType ittAbsent;
    @XmlElement(name = "ITTAnonymous")
    protected ITTAnonymousType ittAnonymous;
    @XmlElement(name = "ITTPrincipalNameStatic")
    protected ITTPrincipalNameStaticType ittPrincipalNameStatic;
    @XmlElement(name = "ITTPrincipalNameDynamic")
    protected ITTPrincipalNameDynamicType ittPrincipalNameDynamic;

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
     * Gets the value of the ittAbsent property.
     * 
     * @return
     *     possible object is
     *     {@link ITTAbsentType }
     *     
     */
    public ITTAbsentType getITTAbsent() {
        return ittAbsent;
    }

    /**
     * Sets the value of the ittAbsent property.
     * 
     * @param value
     *     allowed object is
     *     {@link ITTAbsentType }
     *     
     */
    public void setITTAbsent(ITTAbsentType value) {
        this.ittAbsent = value;
    }

    /**
     * Gets the value of the ittAnonymous property.
     * 
     * @return
     *     possible object is
     *     {@link ITTAnonymousType }
     *     
     */
    public ITTAnonymousType getITTAnonymous() {
        return ittAnonymous;
    }

    /**
     * Sets the value of the ittAnonymous property.
     * 
     * @param value
     *     allowed object is
     *     {@link ITTAnonymousType }
     *     
     */
    public void setITTAnonymous(ITTAnonymousType value) {
        this.ittAnonymous = value;
    }

    /**
     * Gets the value of the ittPrincipalNameStatic property.
     * 
     * @return
     *     possible object is
     *     {@link ITTPrincipalNameStaticType }
     *     
     */
    public ITTPrincipalNameStaticType getITTPrincipalNameStatic() {
        return ittPrincipalNameStatic;
    }

    /**
     * Sets the value of the ittPrincipalNameStatic property.
     * 
     * @param value
     *     allowed object is
     *     {@link ITTPrincipalNameStaticType }
     *     
     */
    public void setITTPrincipalNameStatic(ITTPrincipalNameStaticType value) {
        this.ittPrincipalNameStatic = value;
    }

    /**
     * Gets the value of the ittPrincipalNameDynamic property.
     * 
     * @return
     *     possible object is
     *     {@link ITTPrincipalNameDynamicType }
     *     
     */
    public ITTPrincipalNameDynamicType getITTPrincipalNameDynamic() {
        return ittPrincipalNameDynamic;
    }

    /**
     * Sets the value of the ittPrincipalNameDynamic property.
     * 
     * @param value
     *     allowed object is
     *     {@link ITTPrincipalNameDynamicType }
     *     
     */
    public void setITTPrincipalNameDynamic(ITTPrincipalNameDynamicType value) {
        this.ittPrincipalNameDynamic = value;
    }

}
