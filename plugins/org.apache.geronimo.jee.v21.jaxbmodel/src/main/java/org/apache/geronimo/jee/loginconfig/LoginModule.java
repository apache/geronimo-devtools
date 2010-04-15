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


package org.apache.geronimo.jee.loginconfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.geronimo.jee.deployment.XmlAttributeType;;


/**
 * <p>Java class for login-moduleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="login-moduleType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://geronimo.apache.org/xml/ns/loginconfig-2.0}abstract-login-moduleType">
 *       &lt;sequence>
 *         &lt;element name="login-domain-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="login-module-class" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="option" type="{http://geronimo.apache.org/xml/ns/loginconfig-2.0}optionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="xml-option" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}xml-attributeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "login-moduleType", propOrder = {
    "loginDomainName",
    "loginModuleClass",
    "option",
    "xmlOption"
})
public class LoginModule
    extends AbstractLoginModule
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "login-domain-name", required = true)
    protected String loginDomainName;
    @XmlElement(name = "login-module-class", required = true)
    protected String loginModuleClass;
    protected List<Option> option;
    @XmlElement(name = "xml-option")
    protected List<XmlAttributeType> xmlOption;

    /**
     * Gets the value of the loginDomainName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginDomainName() {
        return loginDomainName;
    }

    /**
     * Sets the value of the loginDomainName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginDomainName(String value) {
        this.loginDomainName = value;
    }

    /**
     * Gets the value of the loginModuleClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginModuleClass() {
        return loginModuleClass;
    }

    /**
     * Sets the value of the loginModuleClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginModuleClass(String value) {
        this.loginModuleClass = value;
    }

    /**
     * Gets the value of the option property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the option property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOption().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Option }
     * 
     * 
     */
    public List<Option> getOption() {
        if (option == null) {
            option = new ArrayList<Option>();
        }
        return this.option;
    }

    /**
     * Gets the value of the xmlOption property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the xmlOption property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getXmlOption().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlAttributeType }
     * 
     * 
     */
    public List<XmlAttributeType> getXmlOption() {
        if (xmlOption == null) {
            xmlOption = new ArrayList<XmlAttributeType>();
        }
        return this.xmlOption;
    }

}
