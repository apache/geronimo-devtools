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

package org.apache.geronimo.jee.javabean;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.xml.ns.deployment.javabean_1 package. 
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

    private final static QName _Javabean_QNAME = new QName("http://geronimo.apache.org/xml/ns/deployment/javabean-1.0", "javabean");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.xml.ns.deployment.javabean_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Javabean }
     * 
     */
    public Javabean createJavabean() {
        return new Javabean();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link BeanProperty }
     * 
     */
    public BeanProperty createBeanProperty() {
        return new BeanProperty();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Javabean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/deployment/javabean-1.0", name = "javabean")
    public JAXBElement<Javabean> createJavabean(Javabean value) {
        return new JAXBElement<Javabean>(_Javabean_QNAME, Javabean.class, null, value);
    }

}
