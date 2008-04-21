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

package org.apache.geronimo.jee.connector;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the <strong>org.apache.geronimo.jee.connector</strong> package.
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

    private final static QName _Connector_QNAME = new QName("http://geronimo.apache.org/xml/ns/j2ee/connector-1.2", "connector");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema 
     * derived classes for package: org.apache.geronimo.jee.connector
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Connector }
     * 
     */
    public Connector createConnector() {
        return new Connector();
    }

    /**
     * Create an instance of {@link Resourceadapter }
     * 
     */
    public Resourceadapter createResourceadapter() {
        return new Resourceadapter();
    }

    /**
     * Create an instance of {@link Connectionmanager }
     * 
     */
    public Connectionmanager createConnectionmanager() {
        return new Connectionmanager();
    }

    /**
     * Create an instance of {@link Singlepool }
     * 
     */
    public Singlepool createSinglepool() {
        return new Singlepool();
    }

    /**
     * Create an instance of {@link Adminobject }
     * 
     */
    public Adminobject createAdminobject() {
        return new Adminobject();
    }

    /**
     * Create an instance of {@link OutboundResourceadapter }
     * 
     */
    public OutboundResourceadapter createOutboundResourceadapter() {
        return new OutboundResourceadapter();
    }

    /**
     * Create an instance of {@link ResourceadapterInstance }
     * 
     */
    public ResourceadapterInstance createResourceadapterInstance() {
        return new ResourceadapterInstance();
    }

    /**
     * Create an instance of {@link Xatransaction }
     * 
     */
    public Xatransaction createXatransaction() {
        return new Xatransaction();
    }

    /**
     * Create an instance of {@link Description }
     * 
     */
    public Description createDescription() {
        return new Description();
    }

    /**
     * Create an instance of {@link ConnectionDefinition }
     * 
     */
    public ConnectionDefinition createConnectionDefinition() {
        return new ConnectionDefinition();
    }

    /**
     * Create an instance of {@link ConnectiondefinitionInstance }
     * 
     */
    public ConnectiondefinitionInstance createConnectiondefinitionInstance() {
        return new ConnectiondefinitionInstance();
    }

    /**
     * Create an instance of {@link ConfigPropertySetting }
     * 
     */
    public ConfigPropertySetting createConfigPropertySetting() {
        return new ConfigPropertySetting();
    }

    /**
     * Create an instance of {@link Partitionedpool }
     * 
     */
    public Partitionedpool createPartitionedpool() {
        return new Partitionedpool();
    }

    /**
     * Create an instance of {@link AdminobjectInstance }
     * 
     */
    public AdminobjectInstance createAdminobjectInstance() {
        return new AdminobjectInstance();
    }

    /**
     * Create an instance of {@link Empty }
     * 
     */
    public Empty createEmpty() {
        return new Empty();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Connector }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/j2ee/connector-1.2", name = "connector")
    public JAXBElement<Connector> createConnector(Connector value) {
        return new JAXBElement<Connector>(_Connector_QNAME, Connector.class, null, value);
    }

}
