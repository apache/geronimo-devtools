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

package org.apache.geronimo.jee.jetty;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.geronimo.jee.jaspi.AuthModule;
import org.apache.geronimo.jee.jaspi.ConfigProvider;
import org.apache.geronimo.jee.jaspi.ServerAuthConfig;
import org.apache.geronimo.jee.jaspi.ServerAuthContext;


/**
 * <p>Java class for authenticationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="authenticationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice minOccurs="0">
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}configProvider"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}serverAuthConfig"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}serverAuthContext"/>
 *         &lt;element ref="{http://geronimo.apache.org/xml/ns/geronimo-jaspi}serverAuthModule"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticationType", propOrder = {
    "configProvider",
    "serverAuthConfig",
    "serverAuthContext",
    "serverAuthModule"
})
public class Authentication
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi")
    protected ConfigProvider configProvider;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi")
    protected ServerAuthConfig serverAuthConfig;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi")
    protected ServerAuthContext serverAuthContext;
    @XmlElement(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi")
    protected AuthModule serverAuthModule;

    /**
     * Gets the value of the configProvider property.
     * 
     * @return
     *     possible object is
     *     {@link ConfigProviderType }
     *     
     */
    public ConfigProvider getConfigProvider() {
        return configProvider;
    }

    /**
     * Sets the value of the configProvider property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigProviderType }
     *     
     */
    public void setConfigProvider(ConfigProvider value) {
        this.configProvider = value;
    }

    /**
     * Gets the value of the serverAuthConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ServerAuthConfigType }
     *     
     */
    public ServerAuthConfig getServerAuthConfig() {
        return serverAuthConfig;
    }

    /**
     * Sets the value of the serverAuthConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerAuthConfigType }
     *     
     */
    public void setServerAuthConfig(ServerAuthConfig value) {
        this.serverAuthConfig = value;
    }

    /**
     * Gets the value of the serverAuthContext property.
     * 
     * @return
     *     possible object is
     *     {@link ServerAuthContextType }
     *     
     */
    public ServerAuthContext getServerAuthContext() {
        return serverAuthContext;
    }

    /**
     * Sets the value of the serverAuthContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerAuthContextType }
     *     
     */
    public void setServerAuthContext(ServerAuthContext value) {
        this.serverAuthContext = value;
    }

    /**
     * Gets the value of the serverAuthModule property.
     * 
     * @return
     *     possible object is
     *     {@link AuthModuleType }
     *     
     */
    public AuthModule getServerAuthModule() {
        return serverAuthModule;
    }

    /**
     * Sets the value of the serverAuthModule property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthModuleType }
     *     
     */
    public void setServerAuthModule(AuthModule value) {
        this.serverAuthModule = value;
    }

}
