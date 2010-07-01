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


package org.apache.geronimo.j2ee.connector;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.xml.ns.j2ee.connector_1 package. 
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

    private final static QName _Connector_QNAME = new QName("http://geronimo.apache.org/xml/ns/j2ee/connector-1.1", "connector");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.xml.ns.j2ee.connector_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConnectorType }
     * 
     */
    public ConnectorType createConnectorType() {
        return new ConnectorType();
    }

    /**
     * Create an instance of {@link ConnectionDefinitionType }
     * 
     */
    public ConnectionDefinitionType createConnectionDefinitionType() {
        return new ConnectionDefinitionType();
    }

    /**
     * Create an instance of {@link PartitionedpoolType }
     * 
     */
    public PartitionedpoolType createPartitionedpoolType() {
        return new PartitionedpoolType();
    }

    /**
     * Create an instance of {@link AdminobjectInstanceType }
     * 
     */
    public AdminobjectInstanceType createAdminobjectInstanceType() {
        return new AdminobjectInstanceType();
    }

    /**
     * Create an instance of {@link AdminobjectType }
     * 
     */
    public AdminobjectType createAdminobjectType() {
        return new AdminobjectType();
    }

    /**
     * Create an instance of {@link ResourceadapterType }
     * 
     */
    public ResourceadapterType createResourceadapterType() {
        return new ResourceadapterType();
    }

    /**
     * Create an instance of {@link OutboundResourceadapterType }
     * 
     */
    public OutboundResourceadapterType createOutboundResourceadapterType() {
        return new OutboundResourceadapterType();
    }

    /**
     * Create an instance of {@link ConnectiondefinitionInstanceType }
     * 
     */
    public ConnectiondefinitionInstanceType createConnectiondefinitionInstanceType() {
        return new ConnectiondefinitionInstanceType();
    }

    /**
     * Create an instance of {@link ConfigPropertySettingType }
     * 
     */
    public ConfigPropertySettingType createConfigPropertySettingType() {
        return new ConfigPropertySettingType();
    }

    /**
     * Create an instance of {@link ResourceadapterInstanceType }
     * 
     */
    public ResourceadapterInstanceType createResourceadapterInstanceType() {
        return new ResourceadapterInstanceType();
    }

    /**
     * Create an instance of {@link DescriptionType }
     * 
     */
    public DescriptionType createDescriptionType() {
        return new DescriptionType();
    }

    /**
     * Create an instance of {@link EmptyType }
     * 
     */
    public EmptyType createEmptyType() {
        return new EmptyType();
    }

    /**
     * Create an instance of {@link XatransactionType }
     * 
     */
    public XatransactionType createXatransactionType() {
        return new XatransactionType();
    }

    /**
     * Create an instance of {@link SinglepoolType }
     * 
     */
    public SinglepoolType createSinglepoolType() {
        return new SinglepoolType();
    }

    /**
     * Create an instance of {@link ConnectionmanagerType }
     * 
     */
    public ConnectionmanagerType createConnectionmanagerType() {
        return new ConnectionmanagerType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConnectorType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/j2ee/connector-1.1", name = "connector")
    public JAXBElement<ConnectorType> createConnector(ConnectorType value) {
        return new JAXBElement<ConnectorType>(_Connector_QNAME, ConnectorType.class, null, value);
    }

}
