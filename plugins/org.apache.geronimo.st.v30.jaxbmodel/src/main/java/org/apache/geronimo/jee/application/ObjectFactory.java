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

package org.apache.geronimo.jee.application;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.xml.ns.j2ee.application_2 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 *  definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Security_QNAME = new QName("http://geronimo.apache.org/xml/ns/j2ee/application-2.0", "security");
    private final static QName _Clustering_QNAME = new QName("http://geronimo.apache.org/xml/ns/j2ee/application-2.0", "clustering");
    private final static QName _Application_QNAME = new QName("http://geronimo.apache.org/xml/ns/j2ee/application-2.0", "application");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.xml.ns.j2ee.application_2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Module }
     * 
     */
    public Module createModule() {
        return new Module();
    }

    /**
     * Create an instance of {@link String }
     * 
     */
    public String createString() {
        return new String();
    }

    /**
     * Create an instance of {@link ExtModule }
     * 
     */
    public ExtModule createExtModule() {
        return new ExtModule();
    }

    /**
     * Create an instance of {@link Path }
     * 
     */
    public Path createPath() {
        return new Path();
    }

    /**
     * Create an instance of {@link Application }
     * 
     */
    public Application createApplication() {
        return new Application();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractSecurity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", name = "security")
    public JAXBElement<AbstractSecurity> createSecurity(AbstractSecurity value) {
        return new JAXBElement<AbstractSecurity>(_Security_QNAME, AbstractSecurity.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractClustering }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", name = "clustering")
    public JAXBElement<AbstractClustering> createClustering(AbstractClustering value) {
        return new JAXBElement<AbstractClustering>(_Clustering_QNAME, AbstractClustering.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Application }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0", name = "application")
    public JAXBElement<Application> createApplication(Application value) {
        return new JAXBElement<Application>(_Application_QNAME, Application.class, null, value);
    }

}
