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
package org.apache.geronimo.jee.jaspi;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.jee.jaspi package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ServerAuthModule_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "serverAuthModule");
    private final static QName _Jaspi_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "jaspi");
    private final static QName _ServerAuthContext_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "serverAuthContext");
    private final static QName _ConfigProvider_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "configProvider");
    private final static QName _ClientAuthConfig_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "clientAuthConfig");
    private final static QName _ClientAuthContext_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "clientAuthContext");
    private final static QName _ServerAuthConfig_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "serverAuthConfig");
    private final static QName _ClientAuthModule_QNAME = new QName("http://geronimo.apache.org/xml/ns/geronimo-jaspi", "clientAuthModule");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.jee.jaspi
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServerAuthContext }
     * 
     */
    public ServerAuthContext createServerAuthContext() {
        return new ServerAuthContext();
    }

    /**
     * Create an instance of {@link ServerAuthConfig }
     * 
     */
    public ServerAuthConfig createServerAuthConfig() {
        return new ServerAuthConfig();
    }

    /**
     * Create an instance of {@link TargetPolicy }
     * 
     */
    public TargetPolicy createTargetPolicy() {
        return new TargetPolicy();
    }

    /**
     * Create an instance of {@link ClientAuthConfig }
     * 
     */
    public ClientAuthConfig createClientAuthConfig() {
        return new ClientAuthConfig();
    }

    /**
     * Create an instance of {@link Jaspi }
     * 
     */
    public Jaspi createJaspi() {
        return new Jaspi();
    }

    /**
     * Create an instance of {@link ClientAuthContext }
     * 
     */
    public ClientAuthContext createClientAuthContext() {
        return new ClientAuthContext();
    }

    /**
     * Create an instance of {@link MessagePolicy }
     * 
     */
    public MessagePolicy createMessagePolicy() {
        return new MessagePolicy();
    }

    /**
     * Create an instance of {@link AuthModule }
     * 
     */
    public AuthModule createAuthModule() {
        return new AuthModule();
    }

    /**
     * Create an instance of {@link Target }
     * 
     */
    public Target createTarget() {
        return new Target();
    }

    /**
     * Create an instance of {@link ConfigProvider }
     * 
     */
    public ConfigProvider createConfigProvider() {
        return new ConfigProvider();
    }

    /**
     * Create an instance of {@link ProtectionPolicy }
     * 
     */
    public ProtectionPolicy createProtectionPolicy() {
        return new ProtectionPolicy();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthModule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "serverAuthModule")
    public JAXBElement<AuthModule> createServerAuthModule(AuthModule value) {
        return new JAXBElement<AuthModule>(_ServerAuthModule_QNAME, AuthModule.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Jaspi }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "jaspi")
    public JAXBElement<Jaspi> createJaspi(Jaspi value) {
        return new JAXBElement<Jaspi>(_Jaspi_QNAME, Jaspi.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServerAuthContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "serverAuthContext")
    public JAXBElement<ServerAuthContext> createServerAuthContext(ServerAuthContext value) {
        return new JAXBElement<ServerAuthContext>(_ServerAuthContext_QNAME, ServerAuthContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigProvider }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "configProvider")
    public JAXBElement<ConfigProvider> createConfigProvider(ConfigProvider value) {
        return new JAXBElement<ConfigProvider>(_ConfigProvider_QNAME, ConfigProvider.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientAuthConfig }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "clientAuthConfig")
    public JAXBElement<ClientAuthConfig> createClientAuthConfig(ClientAuthConfig value) {
        return new JAXBElement<ClientAuthConfig>(_ClientAuthConfig_QNAME, ClientAuthConfig.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientAuthContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "clientAuthContext")
    public JAXBElement<ClientAuthContext> createClientAuthContext(ClientAuthContext value) {
        return new JAXBElement<ClientAuthContext>(_ClientAuthContext_QNAME, ClientAuthContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServerAuthConfig }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "serverAuthConfig")
    public JAXBElement<ServerAuthConfig> createServerAuthConfig(ServerAuthConfig value) {
        return new JAXBElement<ServerAuthConfig>(_ServerAuthConfig_QNAME, ServerAuthConfig.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthModule }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/geronimo-jaspi", name = "clientAuthModule")
    public JAXBElement<AuthModule> createClientAuthModule(AuthModule value) {
        return new JAXBElement<AuthModule>(_ClientAuthModule_QNAME, AuthModule.class, null, value);
    }

}
