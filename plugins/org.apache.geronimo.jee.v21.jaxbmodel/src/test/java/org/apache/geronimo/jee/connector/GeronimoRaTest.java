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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.geronimo.jee.common.NamespaceFilter;
import org.apache.geronimo.jee.common.NamespacePrefixMapperImpl;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.naming.GbeanLocator;
import org.apache.geronimo.jee.naming.Pattern;
import org.custommonkey.xmlunit.Diff;
import org.xml.sax.InputSource;

/**
 * <strong>GeronimoRaTest</strong> is used to test various JAXB operations on
 * the following Geronimo-specific XML file supported by the GEP: 
 * 
 * <ul>
 *      <li>geronimo-ra.xml
 * </ul>
 * 
 * <p>The following JAXB operations are performed: 
 * <ol>
 *      <li>Unmarshalling and marshalling sequence
 *      <li>Namespace conversion
 *      <li>Element conversion (TODO)
 *      <li>Create XML with all fields
 * </ol>
 * 
 * 
 * @version $Rev$ $Date$
 */
public class GeronimoRaTest extends TestCase {

    /*------------------------------------------------------------------------*\
    |                                                                          |
    |  Testcase(s)                                                             | 
    |                                                                          |
    \*------------------------------------------------------------------------*/
    public void testUnmarshallAndMarshall() throws Exception {
        unmarshallAndMarshall("connector/geronimo-ra-example-1.xml", 
                              "connector/geronimo-ra-expected-1.xml");
        unmarshallAndMarshall("connector/geronimo-ra-example-2.xml", 
                              "connector/geronimo-ra-expected-2.xml");
        unmarshallAndMarshall("connector/geronimo-ra-example-3.xml", 
                              "connector/geronimo-ra-expected-3.xml");
    }

    public void testConvertNamespace() throws Exception {
        convertNamespace("connector/geronimo-ra-example-4.xml",
                         "connector/geronimo-ra-expected-1.xml");
        convertNamespace("connector/geronimo-ra-example-5.xml", 
                         "connector/geronimo-ra-expected-2.xml");
        convertNamespace("connector/geronimo-ra-example-6.xml", 
                         "connector/geronimo-ra-expected-3.xml");
    }

    public void testCompleteXML() throws Exception {
        buildFullXMLFromScratch("connector/geronimo-ra-expected-11.xml");
    }

    /*------------------------------------------------------------------------*\
    |                                                                          |
    |  Private method(s)                                                       | 
    |                                                                          |
    \*------------------------------------------------------------------------*/
    private void unmarshallAndMarshall(String fileExample, String fileExpected) throws Exception {

        // 
        // Create unmarshaller and marshaller
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                                    "org.apache.geronimo.jee.connector:" +
                                    "org.apache.geronimo.jee.deployment:", getClass().getClassLoader() );
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl());

        // 
        // Read example and expected XML files
        // 
        InputStream exampleInputStream = this.getClass().getClassLoader().getResourceAsStream(fileExample);
        InputStream expectedInputStream = this.getClass().getClassLoader().getResourceAsStream(fileExpected);
        String example = readContent(exampleInputStream);
        String expected = readContent(expectedInputStream);

        // 
        // Unmarshall the example file
        // 
        // Note: Use InputSource (instead of InputStream) to prevent 
        //       "org.xml.sax.SAXParseException: Premature end of file." exceptions
        // 
        Object jaxbElement = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(example.getBytes())));

        // 
        // Marshall the output of the unmarshall
        // 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(jaxbElement, baos);
        byte[] bytes = baos.toByteArray();
        String actual = new String(bytes);

        // 
        // Compare actual and expected
        // 
        try {
            Diff myDiff = new Diff(expected, actual);
            assertTrue("Files are similar " + myDiff, myDiff.similar());
        }
        catch (AssertionFailedError e) {
            System.out.println("[Example XML: " + fileExample + "] " + '\n' + example + '\n');
            System.out.println("[Expected XML: " + fileExpected + "] " + '\n' + expected + '\n');
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
            throw e;            
        }

    }


    private void convertNamespace(String fileExample, String fileExpected) throws Exception {

        // 
        // Create unmarshaller and marshaller
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                                    "org.apache.geronimo.jee.connector:" +
                                    "org.apache.geronimo.jee.deployment:", getClass().getClassLoader() );
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl());

        // 
        // Create SAXParser
        // 
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        SAXParser parser = factory.newSAXParser();

        // 
        // Create NamespaceFilter to filter for v1.1 namespaces
        // 
        NamespaceFilter xmlFilter = new NamespaceFilter(parser.getXMLReader());

        // 
        // Read example and expected XML files
        // 
        InputStream exampleInputStream = this.getClass().getClassLoader().getResourceAsStream(fileExample);
        InputStream expectedInputStream = this.getClass().getClassLoader().getResourceAsStream(fileExpected);
        String example = readContent(exampleInputStream);
        String expected = readContent(expectedInputStream);

        // 
        // Unmarshall the example file
        // 
        SAXSource source = new SAXSource(xmlFilter, new InputSource(new ByteArrayInputStream(example.getBytes())));
        Object jaxbElement = unmarshaller.unmarshal(source);

        // 
        // Marshall the output of the unmarshall
        // 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(jaxbElement, baos);
        byte[] bytes = baos.toByteArray();
        String actual = new String(bytes);

        // 
        // Compare actual and expected
        // 
        try {
            Diff myDiff = new Diff(expected, actual);
            assertTrue("Files are similar " + myDiff, myDiff.similar());
        }
        catch (AssertionFailedError e) {
            System.out.println("[Example XML: " + fileExample + "] " + '\n' + example + '\n');
            System.out.println("[Expected XML: " + fileExpected + "] " + '\n' + expected + '\n');
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
            throw e;            
        }
    }

    private void buildFullXMLFromScratch (String fileExpected) throws Exception {
        org.apache.geronimo.jee.connector.ObjectFactory connectorFactory = new org.apache.geronimo.jee.connector.ObjectFactory();
        org.apache.geronimo.jee.deployment.ObjectFactory deploymentFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
        org.apache.geronimo.jee.naming.ObjectFactory namingFactory = new org.apache.geronimo.jee.naming.ObjectFactory();
        Connector connector = connectorFactory.createConnector();
 
        // set the Environment
        Environment environment = deploymentFactory.createEnvironment();
        Artifact artifact = deploymentFactory.createArtifact();
        artifact.setGroupId("org.apache.geronimo.testsuite");
        artifact.setArtifactId("agent-ear");
        artifact.setVersion("2.2-SNAPSHOT");
        artifact.setType("ear");
        environment.setModuleId(artifact);
        Dependencies dependencies = deploymentFactory.createDependencies();
        Dependency dependency = deploymentFactory.createDependency();
        dependency.setGroupId("org.apache.geronimo.testsuite");
        dependency.setArtifactId("agent-ds");
        dependency.setVersion("2.2-SNAPSHOT");
        dependency.setType("car");
        dependencies.getDependency().add(dependency);
        environment.setDependencies(dependencies);
        connector.setEnvironment (environment);

        // set the Admin Object
        Adminobject adminobject = connectorFactory.createAdminobject();
        adminobject.setAdminobjectClass("adminobject-adminobjectclass");
        adminobject.setAdminobjectInterface("adminobject-adminobjectinterface");
        AdminobjectInstance adminobjectInstance = connectorFactory.createAdminobjectInstance();
        adminobjectInstance.setMessageDestinationName("adminobject-instance-messagedestinationname");
        ConfigPropertySetting configPropertySetting = connectorFactory.createConfigPropertySetting();
        configPropertySetting.setName("adminobject-instance-name");
        configPropertySetting.setValue("adminobject-instance-value");
        adminobjectInstance.getConfigPropertySetting().add(configPropertySetting);
        adminobject.getAdminobjectInstance().add(adminobjectInstance);
        connector.getAdminobject().add(adminobject);

        // set the Resource Adapter
        Resourceadapter resourceadapter = connectorFactory.createResourceadapter();
        ResourceadapterInstance resourceadapterInstance = connectorFactory.createResourceadapterInstance();
        resourceadapterInstance.setResourceadapterName("resourceadapter-name");
        GbeanLocator gbeanLocator = namingFactory.createGbeanLocator();
        gbeanLocator.setGbeanLink("connector-resourceadapter-workmanager-gbeanlink");
        Pattern namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("connector-resourceadapter-workmanager-pattern-artifactid");
        namingPattern.setGroupId("connector-resourceadapter-workmanager-pattern-groupid");
        namingPattern.setModule("connector-resourceadapter-workmanager-pattern-module");
        namingPattern.setName("connector-resourceadapter-workmanager-pattern-name");
        namingPattern.setVersion("connector-resourceadapter-workmanager-pattern-version");
        gbeanLocator.setPattern(namingPattern);
        resourceadapterInstance.setWorkmanager(gbeanLocator);
        configPropertySetting = connectorFactory.createConfigPropertySetting();
        configPropertySetting.setName("resourceadapter-instance-name");
        configPropertySetting.setValue("resourceadapter-instance-value");        
        resourceadapterInstance.getConfigPropertySetting().add(configPropertySetting);
        resourceadapter.setResourceadapterInstance(resourceadapterInstance);
        OutboundResourceadapter outboundResourceadapter = connectorFactory.createOutboundResourceadapter();
        ConnectionDefinition connectionDefinition = connectorFactory.createConnectionDefinition();
        connectionDefinition.setConnectionfactoryInterface("resourceadapter-outbound-connectiondefinition-interface");
        ConnectiondefinitionInstance connectionDefinitionInstance = connectorFactory.createConnectiondefinitionInstance();
        connectionDefinitionInstance.setName("resourceadapter-outbound-connectiondefinition-instance-name");
        connectionDefinitionInstance.getImplementedInterface().add("resourceadapter-outbound-connectiondefinition-instance-implementedinterface");
        Connectionmanager connectionManager = connectorFactory.createConnectionmanager();
        Empty empty = connectorFactory.createEmpty();
        connectionManager.setContainerManagedSecurity(empty);
        connectionManager.setLocalTransaction(empty);
        connectionManager.setNoPool(empty);
        connectionManager.setNoTransaction(empty);
        connectionManager.setTransactionLog(empty);
        Partitionedpool partitionedPool = connectorFactory.createPartitionedpool();
        partitionedPool.setBlockingTimeoutMilliseconds(new Integer(0));
        partitionedPool.setIdleTimeoutMinutes(new Integer(0));
        partitionedPool.setMatchAll(empty);
        partitionedPool.setMatchOne(empty);
        partitionedPool.setMaxSize(new Integer(0));
        partitionedPool.setMinSize(new Integer(0));
        partitionedPool.setPartitionByConnectionrequestinfo(empty);
        partitionedPool.setPartitionBySubject(empty);
        partitionedPool.setSelectOneAssumeMatch(empty);
        connectionManager.setPartitionedPool(partitionedPool);
        Singlepool singlePool = connectorFactory.createSinglepool();
        singlePool.setBlockingTimeoutMilliseconds(new Integer(0));
        singlePool.setIdleTimeoutMinutes(new Integer(0));
        singlePool.setMatchAll(empty);
        singlePool.setMatchOne(empty);
        singlePool.setMaxSize(new Integer(0));
        singlePool.setMinSize(new Integer(0));
        singlePool.setSelectOneAssumeMatch(empty);
        connectionManager.setPartitionedPool(partitionedPool);
        connectionManager.setSinglePool(singlePool);
        Xatransaction xaTransaction = connectorFactory.createXatransaction();
        xaTransaction.setThreadCaching(empty);
        xaTransaction.setTransactionCaching(empty);
        connectionManager.setXaTransaction(xaTransaction);
        connectionDefinitionInstance.setConnectionmanager(connectionManager);
        configPropertySetting = connectorFactory.createConfigPropertySetting();
        configPropertySetting.setName("resourceadapter-outbound-connectiondefinition-instance-name");
        configPropertySetting.setValue("resourceadapter-outbound-connectiondefinition-instance-value");
        connectionDefinitionInstance.getConfigPropertySetting().add(configPropertySetting);
        connectionDefinition.getConnectiondefinitionInstance().add(connectionDefinitionInstance);
        outboundResourceadapter.getConnectionDefinition().add(connectionDefinition);
        resourceadapter.setOutboundResourceadapter(outboundResourceadapter);
        connector.getResourceadapter().add(resourceadapter);

        // set the Service
        Gbean gbean = deploymentFactory.createGbean();
        gbean.setClazz("gbean-class");
        gbean.setName("gbean-name");
        connector.getService().add(deploymentFactory.createGbean(gbean));

        JAXBElement<Connector> jaxbElement = connectorFactory.createConnector(connector);
        
        // 
        // Marshall the output of the unmarshall
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                "org.apache.geronimo.jee.connector:" +
                "org.apache.geronimo.jee.deployment:", getClass().getClassLoader() );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(jaxbElement, baos);
        String actual = new String(baos.toByteArray());

        InputStream expectedInputStream = this.getClass().getClassLoader().getResourceAsStream(fileExpected);
        String expected = readContent(expectedInputStream);

        try {
            Diff myDiff = new Diff(expected, actual);
            assertTrue("Files are similar " + myDiff, myDiff.similar());
        }
        catch (AssertionFailedError e) {
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
            System.out.println("[Expected XML: " + fileExpected + "]\n" + expected + '\n');
            throw e;            
        }
    }

    private String readContent(InputStream in) throws IOException {
        StringBuffer sb = new StringBuffer();
        in = new BufferedInputStream(in);
        int i = in.read();
        while (i != -1) {
            sb.append((char) i);
            i = in.read();
        }
        String content = sb.toString();
        return content;
    }
}
