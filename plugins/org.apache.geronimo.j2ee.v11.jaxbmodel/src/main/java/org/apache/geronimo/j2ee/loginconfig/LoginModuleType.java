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


package org.apache.geronimo.j2ee.loginconfig;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for login-moduleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="login-moduleType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://geronimo.apache.org/xml/ns/loginconfig-1.1}abstract-login-moduleType">
 *       &lt;sequence>
 *         &lt;element name="login-domain-name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="login-module-class" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="option" type="{http://geronimo.apache.org/xml/ns/loginconfig-1.1}optionType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="server-side" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
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
    "option"
})
public class LoginModuleType
    extends AbstractLoginModuleType
{

    @XmlElement(name = "login-domain-name", required = true)
    protected String loginDomainName;
    @XmlElement(name = "login-module-class", required = true)
    protected String loginModuleClass;
    protected List<OptionType> option;
    @XmlAttribute(name = "server-side", required = true)
    protected boolean serverSide;

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
     * {@link OptionType }
     * 
     * 
     */
    public List<OptionType> getOption() {
        if (option == null) {
            option = new ArrayList<OptionType>();
        }
        return this.option;
    }

    /**
     * Gets the value of the serverSide property.
     * 
     */
    public boolean isServerSide() {
        return serverSide;
    }

    /**
     * Sets the value of the serverSide property.
     * 
     */
    public void setServerSide(boolean value) {
        this.serverSide = value;
    }

}
