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

package org.apache.geronimo.jee.pkgen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.openejb.xml.ns.pkgen_2 package. 
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

    private final static QName _KeyGenerator_QNAME = new QName("http://openejb.apache.org/xml/ns/pkgen-2.1", "key-generator");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.openejb.xml.ns.pkgen_2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DatabaseGenerated}
     * 
     */
    public DatabaseGenerated createDatabaseGenerated() {
        return new DatabaseGenerated();
    }

    /**
     * Create an instance of {@link Empty}
     * 
     */
    public Empty createEmpty() {
        return new Empty();
    }

    /**
     * Create an instance of {@link KeyGenerator}
     * 
     */
    public KeyGenerator createKeyGenerator() {
        return new KeyGenerator();
    }

    /**
     * Create an instance of {@link SequenceTable}
     * 
     */
    public SequenceTable createSequenceTable() {
        return new SequenceTable();
    }

    /**
     * Create an instance of {@link AutoIncrementTable}
     * 
     */
    public AutoIncrementTable createAutoIncrementTable() {
        return new AutoIncrementTable();
    }

    /**
     * Create an instance of {@link CustomGenerator}
     * 
     */
    public CustomGenerator createCustomGenerator() {
        return new CustomGenerator();
    }

    /**
     * Create an instance of {@link SqlGenerator}
     * 
     */
    public SqlGenerator createSqlGenerator() {
        return new SqlGenerator();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link KeyGenerator  }{@code >}} 
     * 
     */
    @XmlElementDecl(namespace = "http://openejb.apache.org/xml/ns/pkgen-2.1", name = "key-generator")
    public JAXBElement<KeyGenerator> createKeyGenerator(KeyGenerator value) {
        return new JAXBElement<KeyGenerator>(_KeyGenerator_QNAME, KeyGenerator.class, null, value);
    }

}
