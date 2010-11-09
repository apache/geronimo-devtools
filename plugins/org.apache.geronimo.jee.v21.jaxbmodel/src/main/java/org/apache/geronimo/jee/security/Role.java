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

package org.apache.geronimo.jee.security;

import java.io.Serializable;
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
 *         &lt;element name="description" type="{http://geronimo.apache.org/xml/ns/security-2.0}descriptionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="run-as-subject" type="{http://geronimo.apache.org/xml/ns/security-2.0}subject-infoType" minOccurs="0"/>
 *         &lt;element name="realm-principal" type="{http://geronimo.apache.org/xml/ns/security-2.0}realmPrincipalType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="login-domain-principal" type="{http://geronimo.apache.org/xml/ns/security-2.0}loginDomainPrincipalType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="principal" type="{http://geronimo.apache.org/xml/ns/security-2.0}principalType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="distinguished-name" type="{http://geronimo.apache.org/xml/ns/security-2.0}distinguishedNameType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="role-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleType", propOrder = {
    "description",
    "runAsSubject",
    "realmPrincipal",
    "loginDomainPrincipal",
    "principal",
    "distinguishedName"
})
public class Role
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    protected List<Description> description;
    @XmlElement(name = "run-as-subject")
    protected SubjectInfo runAsSubject;
    @XmlElement(name = "realm-principal")
    protected List<RealmPrincipal> realmPrincipal;
    @XmlElement(name = "login-domain-principal")
    protected List<LoginDomainPrincipal> loginDomainPrincipal;
    protected List<Principal> principal;
    @XmlElement(name = "distinguished-name")
    protected List<DistinguishedName> distinguishedName;
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
     * {@link Description }
     * 
     * 
     */
    public List<Description> getDescription() {
        if (description == null) {
            description = new ArrayList<Description>();
        }
        return this.description;
    }

    /**
     * Gets the value of the runAsSubject property.
     * 
     * @return
     *     possible object is
     *     {@link SubjectInfo }
     *     
     */
    public SubjectInfo getRunAsSubject() {
        return runAsSubject;
    }

    /**
     * Sets the value of the runAsSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubjectInfo }
     *     
     */
    public void setRunAsSubject(SubjectInfo value) {
        this.runAsSubject = value;
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
     * {@link RealmPrincipal }
     * 
     * 
     */
    public List<RealmPrincipal> getRealmPrincipal() {
        if (realmPrincipal == null) {
            realmPrincipal = new ArrayList<RealmPrincipal>();
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
     * {@link LoginDomainPrincipal }
     * 
     * 
     */
    public List<LoginDomainPrincipal> getLoginDomainPrincipal() {
        if (loginDomainPrincipal == null) {
            loginDomainPrincipal = new ArrayList<LoginDomainPrincipal>();
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
     * {@link Principal }
     * 
     * 
     */
    public List<Principal> getPrincipal() {
        if (principal == null) {
            principal = new ArrayList<Principal>();
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
     * {@link DistinguishedName }
     * 
     * 
     */
    public List<DistinguishedName> getDistinguishedName() {
        if (distinguishedName == null) {
            distinguishedName = new ArrayList<DistinguishedName>();
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
