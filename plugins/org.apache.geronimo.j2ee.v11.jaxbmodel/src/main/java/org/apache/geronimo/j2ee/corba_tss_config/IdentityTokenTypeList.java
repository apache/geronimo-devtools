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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for identityTokenTypeList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="identityTokenTypeList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="ITTAbsent" type="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}ITTAbsentType"/>
 *           &lt;group ref="{http://www.openejb.org/xml/ns/corba-tss-config-2.0}ittGroup"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "identityTokenTypeList", propOrder = {
    "ittAbsent",
    "ittAnonymous",
    "ittPrincipalNameGSSUP",
    "ittDistinguishedName",
    "ittx509CertChain"
})
public class IdentityTokenTypeList {

    @XmlElement(name = "ITTAbsent")
    protected ITTAbsentType ittAbsent;
    @XmlElement(name = "ITTAnonymous")
    protected ITTAnonymousType ittAnonymous;
    @XmlElement(name = "ITTPrincipalNameGSSUP")
    protected ITTPrincipalNameGSSUPType ittPrincipalNameGSSUP;
    @XmlElement(name = "ITTDistinguishedName")
    protected ITTDistinguishedNameType ittDistinguishedName;
    @XmlElement(name = "ITTX509CertChain")
    protected ITTX509CertChainType ittx509CertChain;

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
     * Gets the value of the ittPrincipalNameGSSUP property.
     * 
     * @return
     *     possible object is
     *     {@link ITTPrincipalNameGSSUPType }
     *     
     */
    public ITTPrincipalNameGSSUPType getITTPrincipalNameGSSUP() {
        return ittPrincipalNameGSSUP;
    }

    /**
     * Sets the value of the ittPrincipalNameGSSUP property.
     * 
     * @param value
     *     allowed object is
     *     {@link ITTPrincipalNameGSSUPType }
     *     
     */
    public void setITTPrincipalNameGSSUP(ITTPrincipalNameGSSUPType value) {
        this.ittPrincipalNameGSSUP = value;
    }

    /**
     * Gets the value of the ittDistinguishedName property.
     * 
     * @return
     *     possible object is
     *     {@link ITTDistinguishedNameType }
     *     
     */
    public ITTDistinguishedNameType getITTDistinguishedName() {
        return ittDistinguishedName;
    }

    /**
     * Sets the value of the ittDistinguishedName property.
     * 
     * @param value
     *     allowed object is
     *     {@link ITTDistinguishedNameType }
     *     
     */
    public void setITTDistinguishedName(ITTDistinguishedNameType value) {
        this.ittDistinguishedName = value;
    }

    /**
     * Gets the value of the ittx509CertChain property.
     * 
     * @return
     *     possible object is
     *     {@link ITTX509CertChainType }
     *     
     */
    public ITTX509CertChainType getITTX509CertChain() {
        return ittx509CertChain;
    }

    /**
     * Sets the value of the ittx509CertChain property.
     * 
     * @param value
     *     allowed object is
     *     {@link ITTX509CertChainType }
     *     
     */
    public void setITTX509CertChain(ITTX509CertChainType value) {
        this.ittx509CertChain = value;
    }

}
