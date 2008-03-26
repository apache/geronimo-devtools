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

package org.apache.geronimo.jee.openejb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceRef;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.jee.openejb package. 
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

    private final static QName _GeronimoEjbJarEjbLocalRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "ejb-local-ref");
    private final static QName _GeronimoEjbJarResourceEnvRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "resource-env-ref");
    private final static QName _GeronimoEjbJarResourceRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "resource-ref");
    private final static QName _GeronimoEjbJarEjbRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "ejb-ref");
    private final static QName _GeronimoEjbJarServiceRef_QNAME = new QName("http://geronimo.apache.org/xml/ns/naming-1.2", "service-ref");
    private final static QName _EjbJar_QNAME = new QName("http://geronimo.apache.org/xml/ns/j2ee/ejb/openejb-2.0", "ejb-jar");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema 
     * derived classes for package: org.apache.geronimo.jee.openejb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Empty }
     * 
     */
    public Empty createEmpty() {
        return new Empty();
    }

    /**
     * Create an instance of {@link TssLink }
     * 
     */
    public TssLink createTssLink() {
        return new TssLink();
    }

    /**
     * Create an instance of {@link GeronimoEjbJar }
     * 
     */
    public GeronimoEjbJar createGeronimoEjbJar() {
        return new GeronimoEjbJar();
    }

    /**
     * Create an instance of {@link WebServiceBinding }
     * 
     */
    public WebServiceBinding createWebServiceBinding() {
        return new WebServiceBinding();
    }

    /**
     * Create an instance of {@link WebServiceSecurity }
     * 
     */
    public WebServiceSecurity createWebServiceSecurity() {
        return new WebServiceSecurity();
    }

    /**
     * Create an instance of {@link OpenejbJar }
     * 
     */
    public OpenejbJar createOpenejbJar() {
        return new OpenejbJar();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EjbLocalRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "ejb-local-ref", scope = GeronimoEjbJar.class)
    public JAXBElement<EjbLocalRef> createGeronimoEjbJarEjbLocalRef(EjbLocalRef value) {
        return new JAXBElement<EjbLocalRef>(_GeronimoEjbJarEjbLocalRef_QNAME, EjbLocalRef.class, GeronimoEjbJar.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceEnvRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "resource-env-ref", scope = GeronimoEjbJar.class)
    public JAXBElement<ResourceEnvRef> createGeronimoEjbJarResourceEnvRef(ResourceEnvRef value) {
        return new JAXBElement<ResourceEnvRef>(_GeronimoEjbJarResourceEnvRef_QNAME, ResourceEnvRef.class, GeronimoEjbJar.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResourceRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "resource-ref", scope = GeronimoEjbJar.class)
    public JAXBElement<ResourceRef> createGeronimoEjbJarResourceRef(ResourceRef value) {
        return new JAXBElement<ResourceRef>(_GeronimoEjbJarResourceRef_QNAME, ResourceRef.class, GeronimoEjbJar.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EjbRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "ejb-ref", scope = GeronimoEjbJar.class)
    public JAXBElement<EjbRef> createGeronimoEjbJarEjbRef(EjbRef value) {
        return new JAXBElement<EjbRef>(_GeronimoEjbJarEjbRef_QNAME, EjbRef.class, GeronimoEjbJar.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceRefType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/naming-1.2", name = "service-ref", scope = GeronimoEjbJar.class)
    public JAXBElement<ServiceRef> createGeronimoEjbJarServiceRef(ServiceRef value) {
        return new JAXBElement<ServiceRef>(_GeronimoEjbJarServiceRef_QNAME, ServiceRef.class, GeronimoEjbJar.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GeronimoEjbJar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/j2ee/ejb/openejb-2.0", name = "ejb-jar")
    public JAXBElement<GeronimoEjbJar> createEjbJar(GeronimoEjbJar value) {
        return new JAXBElement<GeronimoEjbJar>(_EjbJar_QNAME, GeronimoEjbJar.class, null, value);
    }

}
