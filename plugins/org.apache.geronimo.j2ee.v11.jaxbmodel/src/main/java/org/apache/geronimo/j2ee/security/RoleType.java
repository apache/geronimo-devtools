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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for roleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="roleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://geronimo.apache.org/xml/ns/security-1.1}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="realm-principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}realmPrincipalType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="login-domain-principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}loginDomainPrincipalType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="principal" type="{http://geronimo.apache.org/xml/ns/security-1.1}principalType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="distinguished-name" type="{http://geronimo.apache.org/xml/ns/security-1.1}distinguishedNameType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="role-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleType", propOrder = {
    "description",
    "realmPrincipal",
    "loginDomainPrincipal",
    "principal",
    "distinguishedName"
})
public class RoleType {

    protected List<DescriptionType> description;
    @XmlElement(name = "realm-principal")
    protected List<RealmPrincipalType> realmPrincipal;
    @XmlElement(name = "login-domain-principal")
    protected List<LoginDomainPrincipalType> loginDomainPrincipal;
    protected List<PrincipalType> principal;
    @XmlElement(name = "distinguished-name")
    protected List<DistinguishedNameType> distinguishedName;
    @XmlAttribute(name = "role-name", required = true)
    protected String roleName;

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
     * Gets the value of the realmPrincipal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realmPrincipal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealmPrincipal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RealmPrincipalType }
     * 
     * 
     */
    public List<RealmPrincipalType> getRealmPrincipal() {
        if (realmPrincipal == null) {
            realmPrincipal = new ArrayList<RealmPrincipalType>();
        }
        return this.realmPrincipal;
    }

    /**
     * Gets the value of the loginDomainPrincipal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the loginDomainPrincipal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLoginDomainPrincipal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LoginDomainPrincipalType }
     * 
     * 
     */
    public List<LoginDomainPrincipalType> getLoginDomainPrincipal() {
        if (loginDomainPrincipal == null) {
            loginDomainPrincipal = new ArrayList<LoginDomainPrincipalType>();
        }
        return this.loginDomainPrincipal;
    }

    /**
     * Gets the value of the principal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the principal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrincipal().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PrincipalType }
     * 
     * 
     */
    public List<PrincipalType> getPrincipal() {
        if (principal == null) {
            principal = new ArrayList<PrincipalType>();
        }
        return this.principal;
    }

    /**
     * Gets the value of the distinguishedName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the distinguishedName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDistinguishedName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DistinguishedNameType }
     * 
     * 
     */
    public List<DistinguishedNameType> getDistinguishedName() {
        if (distinguishedName == null) {
            distinguishedName = new ArrayList<DistinguishedNameType>();
        }
        return this.distinguishedName;
    }

    /**
     * Gets the value of the roleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the value of the roleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRoleName(String value) {
        this.roleName = value;
    }

}
