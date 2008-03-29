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

package org.apache.geronimo.jee.naming;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the <strong>org.apache.geronimo.jee.naming</strong> package. 
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

    private final static QName _ResourceEnvRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "resource-env-ref");
    private final static QName _ResourceRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "resource-ref");
    private final static QName _PersistenceContextRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "persistence-context-ref");
    private final static QName _AbstractNamingEntry_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "abstract-naming-entry");
    private final static QName _MessageDestination_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "message-destination");
    private final static QName _PersistenceUnitRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "persistence-unit-ref");
    private final static QName _ResourceAdapter_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "resource-adapter");
    private final static QName _EjbLocalRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "ejb-local-ref");
    private final static QName _Workmanager_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "workmanager");
    private final static QName _ServiceRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "service-ref");
    private final static QName _CmpConnectionFactory_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "cmp-connection-factory");
    private final static QName _GbeanRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "gbean-ref");
    private final static QName _EjbRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "ejb-ref");
    private final static QName _WebContainer_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "web-container");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema 
     * derived classes for package: org.apache.geronimo.jee.naming
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MessageDestination }
     * 
     */
    public MessageDestination createMessageDestination() {
        return new MessageDestination();
    }

    /**
     * Create an instance of {@link EjbLocalRef }
     * 
     */
    public EjbLocalRef createEjbLocalRef() {
        return new EjbLocalRef();
    }

    /**
     * Create an instance of {@link EjbRef }
     * 
     */
    public EjbRef createEjbRef() {
        return new EjbRef();
    }

    /**
     * Create an instance of {@link PersistenceContextRef }
     * 
     */
    public PersistenceContextRef createPersistenceContextRef() {
        return new PersistenceContextRef();
    }

    /**
     * Create an instance of {@link ResourceEnvRef }
     * 
     */
    public ResourceEnvRef createResourceEnvRef() {
        return new ResourceEnvRef();
    }

    /**
     * Create an instance of {@link Pattern }
     * 
     */
    public Pattern createPattern() {
        return new Pattern();
    }

    /**
     * Create an instance of {@link PersistenceUnitRef }
     * 
     */
    public PersistenceUnitRef createPersistenceUnitRef() {
        return new PersistenceUnitRef();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link ServiceRef }
     * 
     */
    public ServiceRef createServiceRef() {
        return new ServiceRef();
    }

    /**
     * Create an instance of {@link GbeanRef }
     * 
     */
    public GbeanRef createGbeanRef() {
        return new GbeanRef();
    }

    /**
     * Create an instance of {@link ResourceRef }
     * 
     */
    public ResourceRef createResourceRef() {
        return new ResourceRef();
    }

    /**
     * Create an instance of {@link Port }
     * 
     */
    public Port createPort() {
        return new Port();
    }

    /**
     * Create an instance of {@link GbeanLocator }
     * 
     */
    public GbeanLocator createGbeanLocator() {
        return new GbeanLocator();
    }

    /**
     * Create an instance of {@link ServiceCompletion }
     * 
     */
    public ServiceCompletion createServiceCompletion() {
        return new ServiceCompletion();
    }

    /**
     * Create an instance of {@link ResourceLocator }
     * 
     */
    public ResourceLocator createResourceLocator() {
        return new ResourceLocator();
    }

    /**
     * Create an instance of {@link PortCompletion }
     * 
     */
    public PortCompletion createPortCompletion() {
        return new PortCompletion();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceEnvRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "resource-env-ref")
    public JAXBElement<ResourceEnvRef> createResourceEnvRef(ResourceEnvRef value) {
        return new JAXBElement<ResourceEnvRef>(_ResourceEnvRef_QNAME, ResourceEnvRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "resource-ref")
    public JAXBElement<ResourceRef> createResourceRef(ResourceRef value) {
        return new JAXBElement<ResourceRef>(_ResourceRef_QNAME, ResourceRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistenceContextRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "persistence-context-ref", substitutionHeadNamespace = "http://geronimo.apache.org/xml/ns/naming-1.2", substitutionHeadName = "abstract-naming-entry")
    public JAXBElement<PersistenceContextRef> createPersistenceContextRef(PersistenceContextRef value) {
        return new JAXBElement<PersistenceContextRef>(_PersistenceContextRef_QNAME, PersistenceContextRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractNamingEntry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "abstract-naming-entry")
    public JAXBElement<AbstractNamingEntry> createAbstractNamingEntry(AbstractNamingEntry value) {
        return new JAXBElement<AbstractNamingEntry>(_AbstractNamingEntry_QNAME, AbstractNamingEntry.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageDestination }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "message-destination")
    public JAXBElement<MessageDestination> createMessageDestination(MessageDestination value) {
        return new JAXBElement<MessageDestination>(_MessageDestination_QNAME, MessageDestination.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersistenceUnitRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "persistence-unit-ref", substitutionHeadNamespace = "http://geronimo.apache.org/xml/ns/naming-1.2", substitutionHeadName = "abstract-naming-entry")
    public JAXBElement<PersistenceUnitRef> createPersistenceUnitRef(PersistenceUnitRef value) {
        return new JAXBElement<PersistenceUnitRef>(_PersistenceUnitRef_QNAME, PersistenceUnitRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceLocator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "resource-adapter")
    public JAXBElement<ResourceLocator> createResourceAdapter(ResourceLocator value) {
        return new JAXBElement<ResourceLocator>(_ResourceAdapter_QNAME, ResourceLocator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EjbLocalRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "ejb-local-ref")
    public JAXBElement<EjbLocalRef> createEjbLocalRef(EjbLocalRef value) {
        return new JAXBElement<EjbLocalRef>(_EjbLocalRef_QNAME, EjbLocalRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanLocator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "workmanager")
    public JAXBElement<GbeanLocator> createWorkmanager(GbeanLocator value) {
        return new JAXBElement<GbeanLocator>(_Workmanager_QNAME, GbeanLocator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "service-ref")
    public JAXBElement<ServiceRef> createServiceRef(ServiceRef value) {
        return new JAXBElement<ServiceRef>(_ServiceRef_QNAME, ServiceRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceLocator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "cmp-connection-factory")
    public JAXBElement<ResourceLocator> createCmpConnectionFactory(ResourceLocator value) {
        return new JAXBElement<ResourceLocator>(_CmpConnectionFactory_QNAME, ResourceLocator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "gbean-ref", substitutionHeadNamespace = "http://geronimo.apache.org/xml/ns/naming-1.2", substitutionHeadName = "abstract-naming-entry")
    public JAXBElement<GbeanRef> createGbeanRef(GbeanRef value) {
        return new JAXBElement<GbeanRef>(_GbeanRef_QNAME, GbeanRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EjbRef }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "ejb-ref")
    public JAXBElement<EjbRef> createEjbRef(EjbRef value) {
        return new JAXBElement<EjbRef>(_EjbRef_QNAME, EjbRef.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanLocator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "web-container")
    public JAXBElement<GbeanLocator> createWebContainer(GbeanLocator value) {
        return new JAXBElement<GbeanLocator>(_WebContainer_QNAME, GbeanLocator.class, null, value);
    }

}
