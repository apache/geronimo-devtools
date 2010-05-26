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

package org.apache.geronimo.jee.web;

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
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.GbeanLocator;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.Port;
import org.apache.geronimo.jee.naming.PortCompletion;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceCompletion;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.persistence.Persistence;
import org.apache.geronimo.jee.persistence.PersistenceUnitTransaction;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.DistinguishedName;
import org.apache.geronimo.jee.security.LoginDomainPrincipal;
import org.apache.geronimo.jee.security.Principal;
import org.apache.geronimo.jee.security.RealmPrincipal;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.RoleMappings;
import org.apache.geronimo.jee.security.Security;
import org.apache.geronimo.jee.security.SubjectInfo;
import org.custommonkey.xmlunit.Diff;
import org.xml.sax.InputSource;

/**
 * <strong>GeronimoWebTest</strong> is used to test various JAXB operations on
 * the following Geronimo-specific XML file supported by the GEP: 
 * 
 * <ul>
 *      <li>geronimo-web.xml
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
public class GeronimoWebTest extends TestCase {

    /*------------------------------------------------------------------------*\
    |                                                                          |
    |  Testcase(s)                                                             | 
    |                                                                          |
    \*------------------------------------------------------------------------*/
    public void testUnmarshallAndMarshall() throws Exception {
        unmarshallAndMarshall("web/geronimo-web-example-1.xml", 
                              "web/geronimo-web-expected-1.xml");
        unmarshallAndMarshall("web/geronimo-web-example-2.xml", 
                              "web/geronimo-web-expected-2.xml");
        unmarshallAndMarshall("web/geronimo-web-example-3.xml", 
                              "web/geronimo-web-expected-3.xml");
    }

    public void testConvertNamespace() throws Exception {
        convertNamespace("web/geronimo-web-example-4.xml",
                         "web/geronimo-web-expected-4.xml");
        convertNamespace("web/geronimo-web-example-5.xml", 
                         "web/geronimo-web-expected-5.xml");
        convertNamespace("web/geronimo-web-example-6.xml", 
                         "web/geronimo-web-expected-6.xml");
    }

    public void testCompleteXML() throws Exception {
        buildFullXMLFromScratch("web/geronimo-web-expected-11.xml");
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
                                    "org.apache.geronimo.jee.web:" +
                                    "org.apache.geronimo.jee.application:" +
                                    "org.apache.geronimo.jee.deployment:" +
                                    "org.apache.geronimo.jee.naming:" + 
                                    "org.apache.geronimo.jee.security", getClass().getClassLoader() );
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
                                    "org.apache.geronimo.jee.web:" +
                                    "org.apache.geronimo.jee.application:" +
                                    "org.apache.geronimo.jee.deployment:" +
                                    "org.apache.geronimo.jee.naming:" + 
                                    "org.apache.geronimo.jee.security", getClass().getClassLoader() );
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
        org.apache.geronimo.jee.web.ObjectFactory webFactory = new org.apache.geronimo.jee.web.ObjectFactory();
        org.apache.geronimo.jee.deployment.ObjectFactory deploymentFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
        org.apache.geronimo.jee.naming.ObjectFactory namingFactory = new org.apache.geronimo.jee.naming.ObjectFactory();
        org.apache.geronimo.jee.persistence.ObjectFactory persistenceFactory = new org.apache.geronimo.jee.persistence.ObjectFactory();
        org.apache.geronimo.jee.security.ObjectFactory securityFactory = new org.apache.geronimo.jee.security.ObjectFactory();
        WebApp webApp = webFactory.createWebApp();

        webApp.setContextRoot("contextroot");
        webApp.setSecurityRealmName("securityrealmname");
        webApp.setWorkDir("workdir");

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
        webApp.setEnvironment(environment);

        // set the Container Config TODO
        //ContainerConfig containerConfig = webFactory.createContainerConfig();
        //containerConfig.getAny().add("containerconfig-any");
        //webApp.setContainerConfig(containerConfig);

        // set the GBean Locator
        GbeanLocator gbeanLocator = namingFactory.createGbeanLocator();
        gbeanLocator.setGbeanLink("gbeanlocator-gbeanlink");
        org.apache.geronimo.jee.naming.Pattern namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("gbeanlocator-pattern-artifactid");
        namingPattern.setGroupId("gbeanlocator-pattern-groupid");
        namingPattern.setModule("gbeanlocator-pattern-module");
        namingPattern.setName("gbeanlocator-pattern-name");
        namingPattern.setVersion("gbeanlocator-pattern-version");
        gbeanLocator.setPattern(namingPattern);
        webApp.setWebContainer(gbeanLocator);

        // set the Abstract Naming Entry
        PersistenceUnitRef persistenceUnitRef = namingFactory.createPersistenceUnitRef();
        persistenceUnitRef.setPersistenceUnitName("persistenceunitref-unitname");
        persistenceUnitRef.setPersistenceUnitRefName("persistenceunitref-unitrefname");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("persistenceunitref-pattern-artifactid");
        namingPattern.setGroupId("persistenceunitref-pattern-groupid");
        namingPattern.setModule("persistenceunitref-pattern-module");
        namingPattern.setName("persistenceunitref-pattern-name");
        namingPattern.setVersion("persistenceunitref-pattern-version");
        persistenceUnitRef.setPattern(namingPattern);
        webApp.getAbstractNamingEntry().add(namingFactory.createAbstractNamingEntry(persistenceUnitRef));

        // set the EJB Local Ref
        EjbLocalRef ejbLocalRef = namingFactory.createEjbLocalRef();
        ejbLocalRef.setRefName("ejblocalref-refname");
        ejbLocalRef.setEjbLink("ejblocalref-ejblink");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("ejblocalref-pattern-artifactid");
        namingPattern.setGroupId("ejblocalref-pattern-groupid");
        namingPattern.setModule("ejblocalref-pattern-module");
        namingPattern.setName("ejblocalref-pattern-name");
        namingPattern.setVersion("ejblocalref-pattern-version");
        ejbLocalRef.setPattern(namingPattern);
        webApp.getEjbLocalRef().add(ejbLocalRef);

        // set the EJB Ref
        EjbRef ejbRef = namingFactory.createEjbRef();
        ejbRef.setName("ejbref-name");
        ejbRef.setRefName("ejbref-refname");
        ejbRef.setCssLink("ejbref-csslink");
        ejbRef.setEjbLink("ejbref-ejblink");
        ejbRef.setNsCorbaloc("ejbref-nscorbaloc");
        namingPattern = namingFactory.createPattern();
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
        webApp.getEjbRef().add(ejbRef);
        
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
        webApp.getMessageDestination().add(messageDestination);

        // set the Resource Env Ref
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
        webApp.getResourceEnvRef().add(resourceEnvRef);

        // set the Resource Ref
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
        webApp.getResourceRef().add(resourceRef);

        // set the Service or Persistence
        Persistence persistence = persistenceFactory.createPersistence();
        persistence.setVersion("persistence-version");
        Persistence.PersistenceUnit persistenceUnit = persistenceFactory.createPersistencePersistenceUnit();
        persistenceUnit.setName("persistence-name");
        persistenceUnit.setDescription("persistence-description");
        persistenceUnit.setExcludeUnlistedClasses(true);
        persistenceUnit.setJtaDataSource("persistence-jtadatasource");
        persistenceUnit.setNonJtaDataSource("persistence-nonjtadatasource");
        persistenceUnit.setProvider("persistence-provider");
        Persistence.PersistenceUnit.Properties properties = persistenceFactory.createPersistencePersistenceUnitProperties();
        Persistence.PersistenceUnit.Properties.Property property = persistenceFactory.createPersistencePersistenceUnitPropertiesProperty();
        property.setName("persistence-property-name");
        property.setValue("persistence-property-value");
        properties.getProperty().add(property);
        persistenceUnit.setProperties(properties);
        persistenceUnit.setTransactionType(PersistenceUnitTransaction.RESOURCE_LOCAL);
        persistenceUnit.getClazz().add("persistence-clazz");
        persistenceUnit.getJarFile().add("persistence-jarfile");
        persistenceUnit.getMappingFile().add("persistence-mappingfile");
        persistence.getPersistenceUnit().add(persistenceUnit);
        webApp.getServiceOrPersistence().add(persistence);

        // set the Service Ref
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
        webApp.getServiceRef().add(serviceRef);

        // set the Security
        Security security = securityFactory.createSecurity();
        security.setDefaultRole ("security-role");
        security.setDoasCurrentCaller(true);
        security.setUseContextHandler(true);
        org.apache.geronimo.jee.deployment.Pattern pattern = deploymentFactory.createPattern();
        pattern.setArtifactId("security-credentialstoreref-artifactid");
        pattern.setCustomFoo("security-credentialstoreref-customfoo");
        pattern.setGroupId("security-credentialstoreref-groupid");
        pattern.setModule("security-credentialstoreref-module");
        pattern.setType("security-credentialstoreref-type");
        pattern.setVersion("security-credentialstoreref-version");
        security.setCredentialStoreRef(pattern);
        SubjectInfo subject = securityFactory.createSubjectInfo();
        subject.setId("security-subjectinfo-id");
        subject.setRealm("security-subjectinfo-realm");
        Description description = securityFactory.createDescription();
        description.setValue("security-subjectinfo-description");
        subject.getDescription().add(description);
        security.setDefaultSubject(subject);
        RoleMappings roleMappings = securityFactory.createRoleMappings();
        Role role = securityFactory.createRole();
        role.setRoleName("security-role");
        SubjectInfo subjectInfo = securityFactory.createSubjectInfo();
        subjectInfo.setId("security-role-subjectinfo-id");
        subjectInfo.setRealm("security-role-subjectinfo-realm");
        description = securityFactory.createDescription();
        description.setValue("security-role-subjectinfo-description");
        subjectInfo.getDescription().add(description);
        role.setRunAsSubject(subjectInfo);
        description = securityFactory.createDescription();
        description.setValue("security-role-description");
        role.getDescription().add(description);
        DistinguishedName distinguishedName = securityFactory.createDistinguishedName();
        distinguishedName.setName("security-role-distinguishedname");
        description = securityFactory.createDescription();
        description.setValue("security-role-distinguished-name-description");
        distinguishedName.getDescription().add(description);
        role.getDistinguishedName().add(distinguishedName);
        LoginDomainPrincipal loginDomainPrincipal = securityFactory.createLoginDomainPrincipal();
        loginDomainPrincipal.setDomainName("security-role-logindomainprincipal-domainname");
        loginDomainPrincipal.setName("security-role-logindomainprincipal-name");
        loginDomainPrincipal.setClazz("security-role-logindomainprincipal-class");
        description = securityFactory.createDescription();
        description.setValue("security-role-logindomainprincipal-description");
        loginDomainPrincipal.getDescription().add(description);
        role.getLoginDomainPrincipal().add(loginDomainPrincipal);
        Principal principal = securityFactory.createPrincipal();
        principal.setName("security-role-principal-name");
        principal.setClazz("security-role-principal-class");
        description = securityFactory.createDescription();
        description.setValue("security-role-principal-description");
        principal.getDescription().add(description);
        role.getPrincipal().add(principal);
        RealmPrincipal realmPrincipal = securityFactory.createRealmPrincipal();
        realmPrincipal.setName("security-role-realmprincipal-name");
        realmPrincipal.setClazz("security-role-realmprincipal-class");
        description = securityFactory.createDescription();
        description.setValue("security-role-realmprincipal-description");
        realmPrincipal.getDescription().add(description);
        role.getRealmPrincipal().add(realmPrincipal);
        roleMappings.getRole().add(role);
        security.setRoleMappings(roleMappings);
        webApp.setSecurity(securityFactory.createSecurity(security));

        JAXBElement<WebApp> jaxbElement = webFactory.createWebApp(webApp);
        
        // 
        // Marshall the output of the unmarshall
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                "org.apache.geronimo.jee.web:" +
                "org.apache.geronimo.jee.application:" +
                "org.apache.geronimo.jee.deployment:" +
                "org.apache.geronimo.jee.naming:" + 
                "org.apache.geronimo.jee.security", getClass().getClassLoader() );
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
