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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for compoundSecMechType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="compoundSecMechType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;group ref="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}asMechGroup" minOccurs="0"/>
 *         &lt;element name="sasMech" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}sasMechType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "compoundSecMechType", propOrder = {
    "description",
    "gssup",
    "sasMech"
})
public class CompoundSecMechType {

    protected List<DescriptionType> description;
    @XmlElement(name = "GSSUP")
    protected GSSUPType gssup;
    protected SasMechType sasMech;

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
     * Gets the value of the gssup property.
     * 
     * @return
     *     possible object is
     *     {@link GSSUPType }
     *     
     */
    public GSSUPType getGSSUP() {
        return gssup;
    }

    /**
     * Sets the value of the gssup property.
     * 
     * @param value
     *     allowed object is
     *     {@link GSSUPType }
     *     
     */
    public void setGSSUP(GSSUPType value) {
        this.gssup = value;
    }

    /**
     * Gets the value of the sasMech property.
     * 
     * @return
     *     possible object is
     *     {@link SasMechType }
     *     
     */
    public SasMechType getSasMech() {
        return sasMech;
    }

    /**
     * Sets the value of the sasMech property.
     * 
     * @param value
     *     allowed object is
     *     {@link SasMechType }
     *     
     */
    public void setSasMech(SasMechType value) {
        this.sasMech = value;
    }

}
