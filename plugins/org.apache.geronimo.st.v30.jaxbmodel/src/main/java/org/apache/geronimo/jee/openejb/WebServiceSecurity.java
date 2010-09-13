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

package org.apache.geronimo.jee.openejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for web-service-securityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="web-service-securityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="security-realm-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="realm-name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transport-guarantee" type="{http://geronimo.apache.org/xml/ns/j2ee/ejb/openejb-2.0}transport-guaranteeType"/>
 *         &lt;element name="auth-method" type="{http://geronimo.apache.org/xml/ns/j2ee/ejb/openejb-2.0}auth-methodType"/>
 *         &lt;element name="http-method" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "web-service-securityType", propOrder = {
    "securityRealmName",
    "realmName",
    "transportGuarantee",
    "authMethod",
    "httpMethod"
})
public class WebServiceSecurity
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "security-realm-name", required = true)
    protected String securityRealmName;
    @XmlElement(name = "realm-name")
    protected String realmName;
    @XmlElement(name = "transport-guarantee", required = true)
    protected TransportGuarantee transportGuarantee;
    @XmlElement(name = "auth-method", required = true)
    protected AuthMethod authMethod;
    @XmlElement(name = "http-method")
    protected List<String> httpMethod;

    /**
     * Gets the value of the securityRealmName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityRealmName() {
        return securityRealmName;
    }

    /**
     * Sets the value of the securityRealmName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityRealmName(String value) {
        this.securityRealmName = value;
    }

    /**
     * Gets the value of the realmName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRealmName() {
        return realmName;
    }

    /**
     * Sets the value of the realmName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRealmName(String value) {
        this.realmName = value;
    }

    /**
     * Gets the value of the transportGuarantee property.
     * 
     * @return
     *     possible object is
     *     {@link TransportGuarantee }
     *     
     */
    public TransportGuarantee getTransportGuarantee() {
        return transportGuarantee;
    }

    /**
     * Sets the value of the transportGuarantee property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransportGuarantee }
     *     
     */
    public void setTransportGuarantee(TransportGuarantee value) {
        this.transportGuarantee = value;
    }

    /**
     * Gets the value of the authMethod property.
     * 
     * @return
     *     possible object is
     *     {@link AuthMethod }
     *     
     */
    public AuthMethod getAuthMethod() {
        return authMethod;
    }

    /**
     * Sets the value of the authMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthMethod }
     *     
     */
    public void setAuthMethod(AuthMethod value) {
        this.authMethod = value;
    }

    /**
     * Gets the value of the httpMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the httpMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHttpMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getHttpMethod() {
        if (httpMethod == null) {
            httpMethod = new ArrayList<String>();
        }
        return this.httpMethod;
    }

}
