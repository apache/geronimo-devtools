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

package org.apache.geronimo.jee.applicationclient;

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
import org.apache.geronimo.jee.connector.Adminobject;
import org.apache.geronimo.jee.connector.AdminobjectInstance;
import org.apache.geronimo.jee.connector.ConfigPropertySetting;
import org.apache.geronimo.jee.connector.ConnectionDefinition;
import org.apache.geronimo.jee.connector.ConnectiondefinitionInstance;
import org.apache.geronimo.jee.connector.Connectionmanager;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.jee.connector.Empty;
import org.apache.geronimo.jee.connector.OutboundResourceadapter;
import org.apache.geronimo.jee.connector.Partitionedpool;
import org.apache.geronimo.jee.connector.Resourceadapter;
import org.apache.geronimo.jee.connector.ResourceadapterInstance;
import org.apache.geronimo.jee.connector.Singlepool;
import org.apache.geronimo.jee.connector.Xatransaction;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.GbeanLocator;
import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.Port;
import org.apache.geronimo.jee.naming.PortCompletion;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceCompletion;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.SubjectInfo;
import org.custommonkey.xmlunit.Diff;
import org.xml.sax.InputSource;

/**
 * <strong>GeronimoApplicationClientTest</strong> is used to test various JAXB 
 * operations on the following Geronimo-specific XML file supported by the GEP:
 * 
 * <ul>
 *      <li>geronimo-application-client.xml
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
public class GeronimoApplicationClientTest extends TestCase {

    /*------------------------------------------------------------------------*\
    |                                                                          |
    |  Testcase(s)                                                             | 
    |                                                                          |
    \*------------------------------------------------------------------------*/
    public void testUnmarshallAndMarshall() throws Exception {
        unmarshallAndMarshall("applicationclient/geronimo-application-client-example-1.xml", 
                              "applicationclient/geronimo-application-client-expected-1.xml");
        unmarshallAndMarshall("applicationclient/geronimo-application-client-example-2.xml", 
                              "applicationclient/geronimo-application-client-expected-2.xml");
        unmarshallAndMarshall("applicationclient/geronimo-application-client-example-3.xml", 
                              "applicationclient/geronimo-application-client-expected-3.xml");
    }

    public void testConvertNamespace() throws Exception {
        convertNamespace("applicationclient/geronimo-application-client-example-4.xml",
                         "applicationclient/geronimo-application-client-expected-1.xml");
        convertNamespace("applicationclient/geronimo-application-client-example-5.xml", 
                         "applicationclient/geronimo-application-client-expected-2.xml");
        convertNamespace("applicationclient/geronimo-application-client-example-6.xml", 
                         "applicationclient/geronimo-application-client-expected-3.xml");
    }

    public void testCompleteXML() throws Exception {
        buildFullXMLFromScratch("applicationclient/geronimo-application-client-expected-11.xml");
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
                                    "org.apache.geronimo.jee.applicationclient:" +
                                    "org.apache.geronimo.jee.naming:" +
                                    "org.apache.geronimo.jee.deployment", getClass().getClassLoader() );
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
                                    "org.apache.geronimo.jee.applicationclient:" +
                                    "org.apache.geronimo.jee.naming:" +
                                    "org.apache.geronimo.jee.deployment", getClass().getClassLoader() );
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
        } catch (AssertionFailedError e) {
            System.out.println("[Example XML: " + fileExample + "] " + '\n' + example + '\n');
            System.out.println("[Expected XML: " + fileExpected + "] " + '\n' + expected + '\n');
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
            throw e;            
        }
    }

    private void buildFullXMLFromScratch (String fileExpected) throws Exception {
        org.apache.geronimo.jee.applicationclient.ObjectFactory applicationClientFactory = new org.apache.geronimo.jee.applicationclient.ObjectFactory();
        org.apache.geronimo.jee.deployment.ObjectFactory deploymentFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
        org.apache.geronimo.jee.security.ObjectFactory securityFactory = new org.apache.geronimo.jee.security.ObjectFactory();
        org.apache.geronimo.jee.naming.ObjectFactory namingFactory = new org.apache.geronimo.jee.naming.ObjectFactory();
        org.apache.geronimo.jee.connector.ObjectFactory connectorFactory = new org.apache.geronimo.jee.connector.ObjectFactory();
        ApplicationClient applicationClient = applicationClientFactory.createApplicationClient();

        applicationClient.setCallbackHandler("callbackhandler");
        applicationClient.setRealmName("realmname");

        // set the Client Environment
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
        applicationClient.setClientEnvironment(environment);

        // set the Subject Info
        SubjectInfo subject = securityFactory.createSubjectInfo();
        subject.setId("subjectinfo-id");
        subject.setRealm("subjectinfo-realm");
        Description description = securityFactory.createDescription();
        description.setValue("subjectinfo-description");
        subject.getDescription().add(description);
        applicationClient.setDefaultSubject(subject);

        // set the EJB Ref
        EjbRef ejbRef = namingFactory.createEjbRef();
        ejbRef.setName("ejbref-name");
        ejbRef.setRefName("ejbref-refname");
        ejbRef.setCssLink("ejbref-csslink");
        ejbRef.setEjbLink("ejbref-ejblink");
        ejbRef.setNsCorbaloc("ejbref-nscorbaloc");
        org.apache.geronimo.jee.naming.Pattern namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("ejbref-css-artifactid");
        namingPattern.setGroupId("ejbref-css-groupid");
        namingPattern.setModule("ejbref-css-module");
        namingPattern.setName("ejbref-css-name");
        namingPattern.setVersion("ejbref-css-version");
        ejbRef.setCss(namingPattern);
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("ejbref-pattern-artifactid");
        namingPattern.setGroupId("ejbref-pattern-groupid");
        namingPattern.setModule("ejbref-pattern-module");
        namingPattern.setName("ejbref-pattern-name");
        namingPattern.setVersion("ejbref-pattern-version");
        ejbRef.setPattern(namingPattern);
        applicationClient.getEjbRef().add(ejbRef);

        // set the GBean Ref
        GbeanRef gbeanRef = namingFactory.createGbeanRef();
        gbeanRef.setRefName("gbeanref-name");
        gbeanRef.getRefType().add("gbeanref-reftype");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("gbeanref-pattern-artifactid");
        namingPattern.setGroupId("gbeanref-pattern-groupid");
        namingPattern.setModule("gbeanref-pattern-module");
        namingPattern.setName("gbeanref-pattern-name");
        namingPattern.setVersion("gbeanref-pattern-version");
        gbeanRef.getPattern().add(namingPattern);
        applicationClient.getGbeanRef().add(gbeanRef);

        // set the Message Destination
        MessageDestination messageDestination = namingFactory.createMessageDestination();
        messageDestination.setAdminObjectLink("messagedestination-adminobjectlink");
        messageDestination.setAdminObjectModule("messagedestination-adminobjectmodule");
        messageDestination.setMessageDestinationName("messagedestination-name");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedestination-pattern-artifactid");
        namingPattern.setGroupId("messagedestination-pattern-groupid");
        namingPattern.setModule("messagedestination-pattern-module");
        namingPattern.setName("messagedestination-pattern-name");
        namingPattern.setVersion("messagedestination-pattern-version");
        messageDestination.setPattern(namingPattern);
        applicationClient.getMessageDestination().add(messageDestination);

        // set the Resource
        Resource resource = applicationClientFactory.createResource();
        resource.setInternalRar("resource-internalrar");
        Connector connector = connectorFactory.createConnector();
        Adminobject adminobject = connectorFactory.createAdminobject();
        adminobject.setAdminobjectClass("resource-connector-adminobject-adminobjectclass");
        adminobject.setAdminobjectInterface("resource-connector-adminobject-adminobjectinterface");
        AdminobjectInstance adminobjectInstance = connectorFactory.createAdminobjectInstance();
        adminobjectInstance.setMessageDestinationName("resource-connector-adminobject-instance-messagedestinationname");
        ConfigPropertySetting configPropertySetting = connectorFactory.createConfigPropertySetting();
        configPropertySetting.setName("resource-connector-adminobject-instance-name");
        configPropertySetting.setValue("resource-connector-adminobject-instance-value");
        adminobjectInstance.getConfigPropertySetting().add(configPropertySetting);
        adminobject.getAdminobjectInstance().add(adminobjectInstance);
        connector.getAdminobject().add(adminobject);
        environment = deploymentFactory.createEnvironment();
        artifact = deploymentFactory.createArtifact();
        artifact.setGroupId("org.apache.geronimo.testsuite");
        artifact.setArtifactId("agent-ear");
        artifact.setVersion("2.2-SNAPSHOT");
        artifact.setType("ear");
        environment.setModuleId(artifact);
        dependencies = deploymentFactory.createDependencies();
        dependency = deploymentFactory.createDependency();
        dependency.setGroupId("org.apache.geronimo.testsuite");
        dependency.setArtifactId("agent-ds");
        dependency.setVersion("2.2-SNAPSHOT");
        dependency.setType("car");
        dependencies.getDependency().add(dependency);
        environment.setDependencies(dependencies);
        connector.setEnvironment(environment);
        Resourceadapter resourceadapter = connectorFactory.createResourceadapter();
        ResourceadapterInstance resourceadapterInstance = connectorFactory.createResourceadapterInstance();
        resourceadapterInstance.setResourceadapterName("resource-connector-resourceadapter-name");
        GbeanLocator gbeanLocator = namingFactory.createGbeanLocator();
        gbeanLocator.setGbeanLink("connector-resourceadapter-workmanager-gbeanlink");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("connector-resourceadapter-workmanager-pattern-artifactid");
        namingPattern.setGroupId("connector-resourceadapter-workmanager-pattern-groupid");
        namingPattern.setModule("connector-resourceadapter-workmanager-pattern-module");
        namingPattern.setName("connector-resourceadapter-workmanager-pattern-name");
        namingPattern.setVersion("connector-resourceadapter-workmanager-pattern-version");
        gbeanLocator.setPattern(namingPattern);
        resourceadapterInstance.setWorkmanager(gbeanLocator);
        configPropertySetting = connectorFactory.createConfigPropertySetting();
        configPropertySetting.setName("resource-connector-resourceadapter-instance-name");
        configPropertySetting.setValue("resource-connector-resourceadapter-instance-value");        
        resourceadapterInstance.getConfigPropertySetting().add(configPropertySetting);
        resourceadapter.setResourceadapterInstance(resourceadapterInstance);
        OutboundResourceadapter outboundResourceadapter = connectorFactory.createOutboundResourceadapter();
        ConnectionDefinition connectionDefinition = connectorFactory.createConnectionDefinition();
        connectionDefinition.setConnectionfactoryInterface("resource-connector-resourceadapter-outbound-connectiondefinition-interface");
        ConnectiondefinitionInstance connectionDefinitionInstance = connectorFactory.createConnectiondefinitionInstance();
        connectionDefinitionInstance.setName("resource-connector-resourceadapter-outbound-connectiondefinition-instance-name");
        connectionDefinitionInstance.getImplementedInterface().add("resource-connector-resourceadapter-outbound-connectiondefinition-instance-implementedinterface");
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
        configPropertySetting.setName("resource-connector-resourceadapter-outbound-connectiondefinition-instance-name");
        configPropertySetting.setValue("resource-connector-resourceadapter-outbound-connectiondefinition-instance-value");
        connectionDefinitionInstance.getConfigPropertySetting().add(configPropertySetting);
        connectionDefinition.getConnectiondefinitionInstance().add(connectionDefinitionInstance);
        outboundResourceadapter.getConnectionDefinition().add(connectionDefinition);
        resourceadapter.setOutboundResourceadapter(outboundResourceadapter);
        connector.getResourceadapter().add(resourceadapter);
        Gbean gbean = deploymentFactory.createGbean();
        gbean.setClazz("resource-connector-gbean-class");
        gbean.setName("resource-connector-gbean-name");
        connector.getService().add(deploymentFactory.createGbean(gbean));
        resource.setConnector(connector);
        org.apache.geronimo.jee.deployment.Pattern deploymentPattern = deploymentFactory.createPattern();
        deploymentPattern.setArtifactId("resource-externalrar-artifactid");
        deploymentPattern.setGroupId("resource-externalrar-groupid");
        deploymentPattern.setModule("resource-externalrar-module");
        deploymentPattern.setVersion("resource-externalrar-version");
        deploymentPattern.setCustomFoo("resource-externalrar-customfoo");
        deploymentPattern.setType("resource-externalrar-type");
        resource.setExternalRar(deploymentPattern);
        applicationClient.getResource().add(resource);

        // set the resource env ref
        ResourceEnvRef resourceEnvRef = namingFactory.createResourceEnvRef();
        resourceEnvRef.setAdminObjectLink("resourceenvref-adminobjectlink");
        resourceEnvRef.setAdminObjectModule("resourceenvref-adminobjectmodule");
        resourceEnvRef.setMessageDestinationLink("resourceenvref-messagedestinationlink");
        resourceEnvRef.setRefName("resourceenvref-refname");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("resourceenvref-pattern-artifactid");
        namingPattern.setGroupId("resourceenvref-pattern-groupid");
        namingPattern.setModule("resourceenvref-pattern-module");
        namingPattern.setName("resourceenvref-pattern-name");
        namingPattern.setVersion("resourceenvref-pattern-version");
        resourceEnvRef.setPattern(namingPattern);
        applicationClient.getResourceEnvRef().add(resourceEnvRef);

        // set the resource ref
        ResourceRef resourceRef = namingFactory.createResourceRef();
        resourceRef.setRefName("resourceref-refname");
        resourceRef.setResourceLink("resourceref-resourcelink");
        resourceRef.setUrl("resourceref-url");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("resourceref-pattern-artifactid");
        namingPattern.setGroupId("resourceref-pattern-groupid");
        namingPattern.setModule("resourcref-pattern-module");
        namingPattern.setName("resourceref-pattern-name");
        namingPattern.setVersion("resourceref-pattern-version");
        resourceRef.setPattern(namingPattern);
        applicationClient.getResourceRef().add(resourceRef);

        // set the Server Environment
        environment = deploymentFactory.createEnvironment();
        artifact = deploymentFactory.createArtifact();
        artifact.setGroupId("org.apache.geronimo.testsuite");
        artifact.setArtifactId("agent-ear");
        artifact.setVersion("2.2-SNAPSHOT");
        artifact.setType("ear");
        environment.setModuleId(artifact);
        dependencies = deploymentFactory.createDependencies();
        dependency = deploymentFactory.createDependency();
        dependency.setGroupId("org.apache.geronimo.testsuite");
        dependency.setArtifactId("agent-ds");
        dependency.setVersion("2.2-SNAPSHOT");
        dependency.setType("car");
        dependencies.getDependency().add(dependency);
        environment.setDependencies(dependencies);
        applicationClient.setServerEnvironment(environment);

        // add the service
        gbean = deploymentFactory.createGbean();
        gbean.setClazz("service-gbean-class");
        gbean.setName("service-gbean-name");
        applicationClient.getService().add(deploymentFactory.createGbean(gbean));

        // add the service ref
        ServiceRef serviceRef = namingFactory.createServiceRef();
        serviceRef.setServiceRefName("serviceref-refname");
        ServiceCompletion serviceCompletion = namingFactory.createServiceCompletion();
        serviceCompletion.setServiceName("serviceref-servicecompletion-servicename");
        PortCompletion portCompletion = namingFactory.createPortCompletion();
        portCompletion.setBindingName("serviceref-servicecompletion-portcompletion-bindingname");
        Port port = namingFactory.createPort();
        port.setCredentialsName("serviceref-servicecompletion-portcompletion-port-credentialsname");
        port.setHost("serviceref-servicecompletion-portcompletion-port-host");
        port.setPort(new Integer(0));
        port.setPortName("serviceref-servicecompletion-portcompletion-port-portname");
        port.setProtocol("serviceref-servicecompletion-portcompletion-port-protocol");
        port.setUri("serviceref-servicecompletion-portcompletion-port-uri");
        portCompletion.setPort(port);
        serviceCompletion.getPortCompletion().add(portCompletion);
        serviceRef.setServiceCompletion(serviceCompletion);
        port = namingFactory.createPort();
        port.setCredentialsName("serviceref-port-credentialsname");
        port.setHost("serviceref-port-host");
        port.setPort(new Integer(0));
        port.setPortName("serviceref-port-portname");
        port.setProtocol("serviceref-port-protocol");
        port.setUri("serviceref-port-uri");
        serviceRef.getPort().add(port);
        applicationClient.getServiceRef().add(serviceRef);
        
        JAXBElement<ApplicationClient> jaxbElement = applicationClientFactory.createApplicationClient(applicationClient);
        
        // 
        // Marshall the output of the unmarshall
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                "org.apache.geronimo.jee.applicationclient:" +
                "org.apache.geronimo.jee.naming:" +
                "org.apache.geronimo.jee.deployment", getClass().getClassLoader() );
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
