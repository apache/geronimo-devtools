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

package org.apache.geronimo.jee.deployment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the <strong>org.apache.geronimo.jee.deployment</strong> package.
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

    private final static QName _GbeanReferences_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "references");
    private final static QName _GbeanXmlReference_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "xml-reference");
    private final static QName _GbeanDependency_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "dependency");
    private final static QName _GbeanAttribute_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "attribute");
    private final static QName _GbeanReference_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "reference");
    private final static QName _GbeanXmlAttribute_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "xml-attribute");
    private final static QName _Dependencies_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "dependencies");
    private final static QName _Module_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "module");
    private final static QName _Environment_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "environment");
    private final static QName _ServerEnvironment_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "server-environment");
    private final static QName _Service_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "service");
    private final static QName _Gbean_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "gbean");
    private final static QName _ClientEnvironment_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment-1.2", "client-environment");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema 
     * derived classes for package: org.apache.geronimo.jee.deployment
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Gbean }
     * 
     */
    public Gbean createGbean() {
        return new Gbean();
    }

    /**
     * Create an instance of {@link Module }
     * 
     */
    public Module createModule() {
        return new Module();
    }

    /**
     * Create an instance of {@link XmlAttributeType }
     * 
     */
    public XmlAttributeType createXmlAttributeType() {
        return new XmlAttributeType();
    }

    /**
     * Create an instance of {@link Pattern }
     * 
     */
    public Pattern createPattern() {
        return new Pattern();
    }

    /**
     * Create an instance of {@link Environment }
     * 
     */
    public Environment createEnvironment() {
        return new Environment();
    }

    /**
     * Create an instance of {@link Dependencies }
     * 
     */
    public Dependencies createDependencies() {
        return new Dependencies();
    }

    /**
     * Create an instance of {@link Empty }
     * 
     */
    public Empty createEmpty() {
        return new Empty();
    }

    /**
     * Create an instance of {@link Reference }
     * 
     */
    public Reference createReference() {
        return new Reference();
    }

    /**
     * Create an instance of {@link Artifact }
     * 
     */
    public Artifact createArtifact() {
        return new Artifact();
    }

    /**
     * Create an instance of {@link Dependency }
     * 
     */
    public Dependency createDependency() {
        return new Dependency();
    }

    /**
     * Create an instance of {@link References }
     * 
     */
    public References createReferences() {
        return new References();
    }

    /**
     * Create an instance of {@link ClassFilter }
     * 
     */
    public ClassFilter createClassFilter() {
        return new ClassFilter();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link References }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "references", scope = Gbean.class)
    public JAXBElement<References> createGbeanReferences(References value) {
        return new JAXBElement<References>(_GbeanReferences_QNAME, References.class, Gbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlAttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "xml-reference", scope = Gbean.class)
    public JAXBElement<XmlAttributeType> createGbeanXmlReference(XmlAttributeType value) {
        return new JAXBElement<XmlAttributeType>(_GbeanXmlReference_QNAME, XmlAttributeType.class, Gbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Pattern }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "dependency", scope = Gbean.class)
    public JAXBElement<Pattern> createGbeanDependency(Pattern value) {
        return new JAXBElement<Pattern>(_GbeanDependency_QNAME, Pattern.class, Gbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Attribute }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "attribute", scope = Gbean.class)
    public JAXBElement<Attribute> createGbeanAttribute(Attribute value) {
        return new JAXBElement<Attribute>(_GbeanAttribute_QNAME, Attribute.class, Gbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Reference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "reference", scope = Gbean.class)
    public JAXBElement<Reference> createGbeanReference(Reference value) {
        return new JAXBElement<Reference>(_GbeanReference_QNAME, Reference.class, Gbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XmlAttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "xml-attribute", scope = Gbean.class)
    public JAXBElement<XmlAttributeType> createGbeanXmlAttribute(XmlAttributeType value) {
        return new JAXBElement<XmlAttributeType>(_GbeanXmlAttribute_QNAME, XmlAttributeType.class, Gbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Dependencies }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "dependencies")
    public JAXBElement<Dependencies> createDependencies(Dependencies value) {
        return new JAXBElement<Dependencies>(_Dependencies_QNAME, Dependencies.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Module }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "module")
    public JAXBElement<Module> createModule(Module value) {
        return new JAXBElement<Module>(_Module_QNAME, Module.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Environment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "environment")
    public JAXBElement<Environment> createEnvironment(Environment value) {
        return new JAXBElement<Environment>(_Environment_QNAME, Environment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Environment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "server-environment")
    public JAXBElement<Environment> createServerEnvironment(Environment value) {
        return new JAXBElement<Environment>(_ServerEnvironment_QNAME, Environment.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "service")
    public JAXBElement<AbstractService> createService(AbstractService value) {
        return new JAXBElement<AbstractService>(_Service_QNAME, AbstractService.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Gbean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "gbean", substitutionHeadNamespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", substitutionHeadName = "service")
    public JAXBElement<Gbean> createGbean(Gbean value) {
        return new JAXBElement<Gbean>(_Gbean_QNAME, Gbean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Environment }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment-1.2", name = "client-environment")
    public JAXBElement<Environment> createClientEnvironment(Environment value) {
        return new JAXBElement<Environment>(_ClientEnvironment_QNAME, Environment.class, null, value);
    }

}
