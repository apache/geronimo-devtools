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


package org.apache.geronimo.j2ee.security;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for default-principalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="default-principalType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://geronimo.apache.org/xml/ns/security-1.1}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}principalType"/>
 *           &lt;element name="login-domain-principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}loginDomainPrincipalType"/>
 *           &lt;element name="realm-principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}realmPrincipalType"/>
 *         &lt;/choice>
 *         &lt;element name="named-username-password-credential" type="{http://geronimo.apache.org/xml/ns/security-1.1}named-username-password-credentialType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "default-principalType", propOrder = {
    "description",
    "principal",
    "loginDomainPrincipal",
    "realmPrincipal",
    "namedUsernamePasswordCredential"
})
public class DefaultPrincipalType {

    protected List<DescriptionType> description;
    protected PrincipalType principal;
    @XmlElement(name = "login-domain-principal")
    protected LoginDomainPrincipalType loginDomainPrincipal;
    @XmlElement(name = "realm-principal")
    protected RealmPrincipalType realmPrincipal;
    @XmlElement(name = "named-username-password-credential")
    protected List<NamedUsernamePasswordCredentialType> namedUsernamePasswordCredential;

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
     * Gets the value of the principal property.
     * 
     * @return
     *     possible object is
     *     {@link PrincipalType }
     *     
     */
    public PrincipalType getPrincipal() {
        return principal;
    }

    /**
     * Sets the value of the principal property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrincipalType }
     *     
     */
    public void setPrincipal(PrincipalType value) {
        this.principal = value;
    }

    /**
     * Gets the value of the loginDomainPrincipal property.
     * 
     * @return
     *     possible object is
     *     {@link LoginDomainPrincipalType }
     *     
     */
    public LoginDomainPrincipalType getLoginDomainPrincipal() {
        return loginDomainPrincipal;
    }

    /**
     * Sets the value of the loginDomainPrincipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoginDomainPrincipalType }
     *     
     */
    public void setLoginDomainPrincipal(LoginDomainPrincipalType value) {
        this.loginDomainPrincipal = value;
    }

    /**
     * Gets the value of the realmPrincipal property.
     * 
     * @return
     *     possible object is
     *     {@link RealmPrincipalType }
     *     
     */
    public RealmPrincipalType getRealmPrincipal() {
        return realmPrincipal;
    }

    /**
     * Sets the value of the realmPrincipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link RealmPrincipalType }
     *     
     */
    public void setRealmPrincipal(RealmPrincipalType value) {
        this.realmPrincipal = value;
    }

    /**
     * Gets the value of the namedUsernamePasswordCredential property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the namedUsernamePasswordCredential property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNamedUsernamePasswordCredential().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NamedUsernamePasswordCredentialType }
     * 
     * 
     */
    public List<NamedUsernamePasswordCredentialType> getNamedUsernamePasswordCredential() {
        if (namedUsernamePasswordCredential == null) {
            namedUsernamePasswordCredential = new ArrayList<NamedUsernamePasswordCredentialType>();
        }
        return this.namedUsernamePasswordCredential;
    }

}
