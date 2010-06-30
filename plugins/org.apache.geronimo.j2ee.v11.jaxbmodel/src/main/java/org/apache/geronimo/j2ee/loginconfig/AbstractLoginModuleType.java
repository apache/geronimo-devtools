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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for abstract-login-moduleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="abstract-login-moduleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="control-flag" use="required" type="{http://geronimo.apache.org/xml/ns/loginconfig-1.1}control-flagType" />
 *       &lt;attribute name="wrap-principals" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstract-login-moduleType")
public abstract class AbstractLoginModuleType {

    @XmlAttribute(name = "control-flag", required = true)
    protected ControlFlagType controlFlag;
    @XmlAttribute(name = "wrap-principals")
    protected Boolean wrapPrincipals;

    /**
     * Gets the value of the controlFlag property.
     * 
     * @return
     *     possible object is
     *     {@link ControlFlagType }
     *     
     */
    public ControlFlagType getControlFlag() {
        return controlFlag;
    }

    /**
     * Sets the value of the controlFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlFlagType }
     *     
     */
    public void setControlFlag(ControlFlagType value) {
        this.controlFlag = value;
    }

    /**
     * Gets the value of the wrapPrincipals property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWrapPrincipals() {
        return wrapPrincipals;
    }

    /**
     * Sets the value of the wrapPrincipals property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWrapPrincipals(Boolean value) {
        this.wrapPrincipals = value;
    }

}
