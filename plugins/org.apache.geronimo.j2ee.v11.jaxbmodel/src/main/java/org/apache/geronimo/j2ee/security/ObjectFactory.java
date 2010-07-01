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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.xml.ns.security_1 package. 
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

    private final static QName _Security_QNAME = new QName("http://geronimo.apache.org/xml/ns/security-1.1", "security");
    private final static QName _DefaultPrincipal_QNAME = new QName("http://geronimo.apache.org/xml/ns/security-1.1", "default-principal");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.xml.ns.security_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RealmPrincipalType }
     * 
     */
    public RealmPrincipalType createRealmPrincipalType() {
        return new RealmPrincipalType();
    }

    /**
     * Create an instance of {@link SecurityType }
     * 
     */
    public SecurityType createSecurityType() {
        return new SecurityType();
    }

    /**
     * Create an instance of {@link RoleMappingsType }
     * 
     */
    public RoleMappingsType createRoleMappingsType() {
        return new RoleMappingsType();
    }

    /**
     * Create an instance of {@link DescriptionType }
     * 
     */
    public DescriptionType createDescriptionType() {
        return new DescriptionType();
    }

    /**
     * Create an instance of {@link DefaultPrincipalType }
     * 
     */
    public DefaultPrincipalType createDefaultPrincipalType() {
        return new DefaultPrincipalType();
    }

    /**
     * Create an instance of {@link DistinguishedNameType }
     * 
     */
    public DistinguishedNameType createDistinguishedNameType() {
        return new DistinguishedNameType();
    }

    /**
     * Create an instance of {@link LoginDomainPrincipalType }
     * 
     */
    public LoginDomainPrincipalType createLoginDomainPrincipalType() {
        return new LoginDomainPrincipalType();
    }

    /**
     * Create an instance of {@link NamedUsernamePasswordCredentialType }
     * 
     */
    public NamedUsernamePasswordCredentialType createNamedUsernamePasswordCredentialType() {
        return new NamedUsernamePasswordCredentialType();
    }

    /**
     * Create an instance of {@link RoleType }
     * 
     */
    public RoleType createRoleType() {
        return new RoleType();
    }

    /**
     * Create an instance of {@link PrincipalType }
     * 
     */
    public PrincipalType createPrincipalType() {
        return new PrincipalType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecurityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/security-1.1", name = "security")
    public JAXBElement<SecurityType> createSecurity(SecurityType value) {
        return new JAXBElement<SecurityType>(_Security_QNAME, SecurityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefaultPrincipalType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/security-1.1", name = "default-principal")
    public JAXBElement<DefaultPrincipalType> createDefaultPrincipal(DefaultPrincipalType value) {
        return new JAXBElement<DefaultPrincipalType>(_DefaultPrincipal_QNAME, DefaultPrincipalType.class, null, value);
    }

}
