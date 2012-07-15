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


package org.apache.geronimo.j2ee.naming;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.xml.ns.naming_1 package. 
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

    private final static QName _ResourceAdapter_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "resource-adapter");
    private final static QName _ResourceRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "resource-ref");
    private final static QName _ResourceEnvRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "resource-env-ref");
    private final static QName _ServiceRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "service-ref");
    private final static QName _EjbRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "ejb-ref");
    private final static QName _WebContainer_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "web-container");
    private final static QName _CmpConnectionFactory_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "cmp-connection-factory");
    private final static QName _MessageDestination_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "message-destination");
    private final static QName _Workmanager_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "workmanager");
    private final static QName _GbeanRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.1", "gbean-ref");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.xml.ns.naming_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResourceRefType }
     * 
     */
    public ResourceRefType createResourceRefType() {
        return new ResourceRefType();
    }

    /**
     * Create an instance of {@link EjbLocalRefType }
     * 
     */
    public EjbLocalRefType createEjbLocalRefType() {
        return new EjbLocalRefType();
    }

    /**
     * Create an instance of {@link PortCompletionType }
     * 
     */
    public PortCompletionType createPortCompletionType() {
        return new PortCompletionType();
    }

    /**
     * Create an instance of {@link ResourceLocatorType }
     * 
     */
    public ResourceLocatorType createResourceLocatorType() {
        return new ResourceLocatorType();
    }

    /**
     * Create an instance of {@link EjbRefType }
     * 
     */
    public EjbRefType createEjbRefType() {
        return new EjbRefType();
    }

    /**
     * Create an instance of {@link PatternType }
     * 
     */
    public PatternType createPatternType() {
        return new PatternType();
    }

    /**
     * Create an instance of {@link GbeanLocatorType }
     * 
     */
    public GbeanLocatorType createGbeanLocatorType() {
        return new GbeanLocatorType();
    }

    /**
     * Create an instance of {@link GbeanRefType }
     * 
     */
    public GbeanRefType createGbeanRefType() {
        return new GbeanRefType();
    }

    /**
     * Create an instance of {@link ResourceEnvRefType }
     * 
     */
    public ResourceEnvRefType createResourceEnvRefType() {
        return new ResourceEnvRefType();
    }

    /**
     * Create an instance of {@link ServiceRefType }
     * 
     */
    public ServiceRefType createServiceRefType() {
        return new ServiceRefType();
    }

    /**
     * Create an instance of {@link ServiceCompletionType }
     * 
     */
    public ServiceCompletionType createServiceCompletionType() {
        return new ServiceCompletionType();
    }

    /**
     * Create an instance of {@link PortType }
     * 
     */
    public PortType createPortType() {
        return new PortType();
    }

    /**
     * Create an instance of {@link MessageDestinationType }
     * 
     */
    public MessageDestinationType createMessageDestinationType() {
        return new MessageDestinationType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceLocatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "resource-adapter")
    public JAXBElement<ResourceLocatorType> createResourceAdapter(ResourceLocatorType value) {
        return new JAXBElement<ResourceLocatorType>(_ResourceAdapter_QNAME, ResourceLocatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "resource-ref")
    public JAXBElement<ResourceRefType> createResourceRef(ResourceRefType value) {
        return new JAXBElement<ResourceRefType>(_ResourceRef_QNAME, ResourceRefType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceEnvRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "resource-env-ref")
    public JAXBElement<ResourceEnvRefType> createResourceEnvRef(ResourceEnvRefType value) {
        return new JAXBElement<ResourceEnvRefType>(_ResourceEnvRef_QNAME, ResourceEnvRefType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "service-ref")
    public JAXBElement<ServiceRefType> createServiceRef(ServiceRefType value) {
        return new JAXBElement<ServiceRefType>(_ServiceRef_QNAME, ServiceRefType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EjbRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "ejb-ref")
    public JAXBElement<EjbRefType> createEjbRef(EjbRefType value) {
        return new JAXBElement<EjbRefType>(_EjbRef_QNAME, EjbRefType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanLocatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "web-container")
    public JAXBElement<GbeanLocatorType> createWebContainer(GbeanLocatorType value) {
        return new JAXBElement<GbeanLocatorType>(_WebContainer_QNAME, GbeanLocatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceLocatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "cmp-connection-factory")
    public JAXBElement<ResourceLocatorType> createCmpConnectionFactory(ResourceLocatorType value) {
        return new JAXBElement<ResourceLocatorType>(_CmpConnectionFactory_QNAME, ResourceLocatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageDestinationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "message-destination")
    public JAXBElement<MessageDestinationType> createMessageDestination(MessageDestinationType value) {
        return new JAXBElement<MessageDestinationType>(_MessageDestination_QNAME, MessageDestinationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanLocatorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "workmanager")
    public JAXBElement<GbeanLocatorType> createWorkmanager(GbeanLocatorType value) {
        return new JAXBElement<GbeanLocatorType>(_Workmanager_QNAME, GbeanLocatorType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.1", name = "gbean-ref")
    public JAXBElement<GbeanRefType> createGbeanRef(GbeanRefType value) {
        return new JAXBElement<GbeanRefType>(_GbeanRef_QNAME, GbeanRefType.class, null, value);
    }

}
