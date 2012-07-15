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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.deployment.Pattern;


/**
 * <p>Java class for login-module-refType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="login-module-refType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://geronimo.apache.org/xml/ns/loginconfig-2.0}abstract-login-moduleType">
 *       &lt;sequence>
 *         &lt;element name="pattern" type="{http://geronimo.apache.org/xml/ns/deployment-1.2}patternType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "login-module-refType", propOrder = {
    "pattern"
})
public class LoginModuleRef
    extends AbstractLoginModule
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(required = true)
    protected Pattern pattern;

    /**
     * Gets the value of the pattern property.
     * 
     * @return
     *     possible object is
     *     {@link Pattern }
     *     
     */
    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Sets the value of the pattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pattern }
     *     
     */
    public void setPattern(Pattern value) {
        this.pattern = value;
    }

}
