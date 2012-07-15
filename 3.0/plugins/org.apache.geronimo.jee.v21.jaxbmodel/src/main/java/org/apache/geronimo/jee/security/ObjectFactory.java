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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.apache.geronimo.jee.deployment.Pattern;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the <strong>org.apache.geronimo.jee.security</strong> package.
 * 
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 * 
 * @version $Rev$ $Date$
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CredentialStore_QNAME = new QName("http://geronimo.apache.org/xml/ns/security-2.0", "credential-store");
    private final static QName _Security_QNAME = new QName("http://geronimo.apache.org/xml/ns/security-2.0", "security");
    private final static QName _DefaultSubject_QNAME = new QName("http://geronimo.apache.org/xml/ns/security-2.0", "default-subject");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema 
     * derived classes for package: org.apache.geronimo.jee.security
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LoginDomainPrincipal }
     * 
     */
    public LoginDomainPrincipal createLoginDomainPrincipal() {
        return new LoginDomainPrincipal();
    }

    /**
     * Create an instance of {@link NamedUsernamePasswordCredential }
     * 
     */
    public NamedUsernamePasswordCredential createNamedUsernamePasswordCredential() {
        return new NamedUsernamePasswordCredential();
    }

    /**
     * Create an instance of {@link RoleMappings }
     * 
     */
    public RoleMappings createRoleMappings() {
        return new RoleMappings();
    }

    /**
     * Create an instance of {@link Security }
     * 
     */
    public Security createSecurity() {
        return new Security();
    }

    /**
     * Create an instance of {@link Role }
     * 
     */
    public Role createRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link SubjectInfo }
     * 
     */
    public SubjectInfo createSubjectInfo() {
        return new SubjectInfo();
    }

    /**
     * Create an instance of {@link DistinguishedName }
     * 
     */
    public DistinguishedName createDistinguishedName() {
        return new DistinguishedName();
    }

    /**
     * Create an instance of {@link Description }
     * 
     */
    public Description createDescription() {
        return new Description();
    }

    /**
     * Create an instance of {@link Principal }
     * 
     */
    public Principal createPrincipal() {
        return new Principal();
    }

    /**
     * Create an instance of {@link RealmPrincipal }
     * 
     */
    public RealmPrincipal createRealmPrincipal() {
        return new RealmPrincipal();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pattern }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/security-2.0", name = "credential-store")
    public JAXBElement<Pattern> createCredentialStore(Pattern value) {
        return new JAXBElement<Pattern>(_CredentialStore_QNAME, Pattern.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Security }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/security-2.0", name = "security", substitutionHeadNamespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", substitutionHeadName = "security")
    public JAXBElement<Security> createSecurity(Security value) {
        return new JAXBElement<Security>(_Security_QNAME, Security.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/security-2.0", name = "default-subject")
    public JAXBElement<SubjectInfo> createDefaultSubject(SubjectInfo value) {
        return new JAXBElement<SubjectInfo>(_DefaultSubject_QNAME, SubjectInfo.class, null, value);
    }

}
