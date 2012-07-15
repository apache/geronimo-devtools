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


package org.apache.geronimo.j2ee.deployment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.xml.ns.deployment_1 package. 
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

    private final static QName _Environment_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "environment");
    private final static QName _ClientEnvironment_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "client-environment");
    private final static QName _Module_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "module");
    private final static QName _Gbean_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "gbean");
    private final static QName _Service_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "service");
    private final static QName _ServerEnvironment_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "server-environment");
    private final static QName _GbeanTypeXmlReference_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "xml-reference");
    private final static QName _GbeanTypeAttribute_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "attribute");
    private final static QName _GbeanTypeDependency_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "dependency");
    private final static QName _GbeanTypeReferences_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "references");
    private final static QName _GbeanTypeReference_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "reference");
    private final static QName _GbeanTypeXmlAttribute_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.1", "xml-attribute");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.xml.ns.deployment_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArtifactType }
     * 
     */
    public ArtifactType createArtifactType() {
        return new ArtifactType();
    }

    /**
     * Create an instance of {@link PatternType }
     * 
     */
    public PatternType createPatternType() {
        return new PatternType();
    }

    /**
     * Create an instance of {@link ClassFilterType }
     * 
     */
    public ClassFilterType createClassFilterType() {
        return new ClassFilterType();
    }

    /**
     * Create an instance of {@link DependencyType }
     * 
     */
    public DependencyType createDependencyType() {
        return new DependencyType();
    }

    /**
     * Create an instance of {@link ReferencesType }
     * 
     */
    public ReferencesType createReferencesType() {
        return new ReferencesType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link EnvironmentType }
     * 
     */
    public EnvironmentType createEnvironmentType() {
        return new EnvironmentType();
    }

    /**
     * Create an instance of {@link GbeanType }
     * 
     */
    public GbeanType createGbeanType() {
        return new GbeanType();
    }

    /**
     * Create an instance of {@link EmptyType }
     * 
     */
    public EmptyType createEmptyType() {
        return new EmptyType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link ModuleType }
     * 
     */
    public ModuleType createModuleType() {
        return new ModuleType();
    }

    /**
     * Create an instance of {@link DependenciesType }
     * 
     */
    public DependenciesType createDependenciesType() {
        return new DependenciesType();
    }

    /**
     * Create an instance of {@link XmlAttributeType }
     * 
     */
    public XmlAttributeType createXmlAttributeType() {
        return new XmlAttributeType();
    }

    /**
     * Create an instance of {@link ServiceType }
     * 
     */
    public ServiceType createServiceType() {
        return new ServiceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnvironmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "environment")
    public JAXBElement<EnvironmentType> createEnvironment(EnvironmentType value) {
        return new JAXBElement<EnvironmentType>(_Environment_QNAME, EnvironmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnvironmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "client-environment")
    public JAXBElement<EnvironmentType> createClientEnvironment(EnvironmentType value) {
        return new JAXBElement<EnvironmentType>(_ClientEnvironment_QNAME, EnvironmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ModuleType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "module")
    public JAXBElement<ModuleType> createModule(ModuleType value) {
        return new JAXBElement<ModuleType>(_Module_QNAME, ModuleType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "gbean")
    public JAXBElement<GbeanType> createGbean(GbeanType value) {
        return new JAXBElement<GbeanType>(_Gbean_QNAME, GbeanType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "service")
    public JAXBElement<ServiceType> createService(ServiceType value) {
        return new JAXBElement<ServiceType>(_Service_QNAME, ServiceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnvironmentType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "server-environment")
    public JAXBElement<EnvironmentType> createServerEnvironment(EnvironmentType value) {
        return new JAXBElement<EnvironmentType>(_ServerEnvironment_QNAME, EnvironmentType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlAttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "xml-reference", scope = GbeanType.class)
    public JAXBElement<XmlAttributeType> createGbeanTypeXmlReference(XmlAttributeType value) {
        return new JAXBElement<XmlAttributeType>(_GbeanTypeXmlReference_QNAME, XmlAttributeType.class, GbeanType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "attribute", scope = GbeanType.class)
    public JAXBElement<AttributeType> createGbeanTypeAttribute(AttributeType value) {
        return new JAXBElement<AttributeType>(_GbeanTypeAttribute_QNAME, AttributeType.class, GbeanType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PatternType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "dependency", scope = GbeanType.class)
    public JAXBElement<PatternType> createGbeanTypeDependency(PatternType value) {
        return new JAXBElement<PatternType>(_GbeanTypeDependency_QNAME, PatternType.class, GbeanType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferencesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "references", scope = GbeanType.class)
    public JAXBElement<ReferencesType> createGbeanTypeReferences(ReferencesType value) {
        return new JAXBElement<ReferencesType>(_GbeanTypeReferences_QNAME, ReferencesType.class, GbeanType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReferenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "reference", scope = GbeanType.class)
    public JAXBElement<ReferenceType> createGbeanTypeReference(ReferenceType value) {
        return new JAXBElement<ReferenceType>(_GbeanTypeReference_QNAME, ReferenceType.class, GbeanType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlAttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.1", name = "xml-attribute", scope = GbeanType.class)
    public JAXBElement<XmlAttributeType> createGbeanTypeXmlAttribute(XmlAttributeType value) {
        return new JAXBElement<XmlAttributeType>(_GbeanTypeXmlAttribute_QNAME, XmlAttributeType.class, GbeanType.class, value);
    }

}
