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


package org.apache.geronimo.j2ee.pkgen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openejb.xml.ns.pkgen_2 package. 
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

    private final static QName _KeyGenerator_QNAME = new QName("http://www.openejb.org/xml/ns/pkgen-2.0", "key-generator");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openejb.xml.ns.pkgen_2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DatabaseGeneratedType }
     * 
     */
    public DatabaseGeneratedType createDatabaseGeneratedType() {
        return new DatabaseGeneratedType();
    }

    /**
     * Create an instance of {@link SqlGeneratorType }
     * 
     */
    public SqlGeneratorType createSqlGeneratorType() {
        return new SqlGeneratorType();
    }

    /**
     * Create an instance of {@link SequenceTableType }
     * 
     */
    public SequenceTableType createSequenceTableType() {
        return new SequenceTableType();
    }

    /**
     * Create an instance of {@link CustomGeneratorType }
     * 
     */
    public CustomGeneratorType createCustomGeneratorType() {
        return new CustomGeneratorType();
    }

    /**
     * Create an instance of {@link KeyGeneratorType }
     * 
     */
    public KeyGeneratorType createKeyGeneratorType() {
        return new KeyGeneratorType();
    }

    /**
     * Create an instance of {@link AutoIncrementTableType }
     * 
     */
    public AutoIncrementTableType createAutoIncrementTableType() {
        return new AutoIncrementTableType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyGeneratorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.openejb.org/xml/ns/pkgen-2.0", name = "key-generator")
    public JAXBElement<KeyGeneratorType> createKeyGenerator(KeyGeneratorType value) {
        return new JAXBElement<KeyGeneratorType>(_KeyGenerator_QNAME, KeyGeneratorType.class, null, value);
    }

}
