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
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.Port;
import org.apache.geronimo.jee.naming.PortCompletion;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceLocator;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceCompletion;
import org.apache.geronimo.jee.naming.ServiceRef;
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
 * <strong>OpenEjbJarTest</strong> is used to test various JAXB operations on
 * the following Geronimo-specific XML file supported by the GEP: 
 * 
 * <ul>
 *      <li>openejb-jar.xml
 * </ul>
 * 
 * <p>The following JAXB operations are performed: 
 * <ol>
 *      <li>Unmarshalling and marshalling sequence
 *      <li>Namespace conversion (TODO)
 *      <li>Element conversion (TODO)
 *      <li>Create XML with all fields
 * </ol>
 * 
 * 
 * @version $Rev$ $Date$
 */
public class OpenEjbJarTest extends TestCase {

    /*------------------------------------------------------------------------*\
    |                                                                          |
    |  Testcase(s)                                                             | 
    |                                                                          |
    \*------------------------------------------------------------------------*/
    public void testUnmarshallAndMarshall() throws Exception {
        unmarshallAndMarshall("openejb/openejb-jar-example-1.xml", 
                              "openejb/openejb-jar-expected-1.xml");
        unmarshallAndMarshall("openejb/openejb-jar-example-2.xml", 
                              "openejb/openejb-jar-expected-2.xml");
    }

    public void testConvertNamespace() throws Exception {
        convertNamespace("openejb/openejb-jar-example-3.xml", 
                         "openejb/openejb-jar-expected-3.xml");
        convertNamespace("openejb/openejb-jar-example-4.xml", 
                         "openejb/openejb-jar-expected-3.xml");
    }

    public void testCompleteXML() throws Exception {
        buildFullXMLFromScratch("openejb/openejb-jar-expected-11.xml");
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
                                    "org.apache.geronimo.jee.openejb:" +
                                    "org.apache.geronimo.jee.application:" +
                                    "org.apache.geronimo.jee.deployment:" +
                                    "org.apache.geronimo.jee.naming", getClass().getClassLoader() );
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
                                    "org.apache.geronimo.jee.openejb:" +
                                    "org.apache.geronimo.jee.application:" +
                                    "org.apache.geronimo.jee.deployment:" +
                                    "org.apache.geronimo.jee.naming", getClass().getClassLoader() );
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

        org.apache.geronimo.jee.openejb.ObjectFactory openejbFactory = new org.apache.geronimo.jee.openejb.ObjectFactory();
        org.apache.geronimo.jee.deployment.ObjectFactory deploymentFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
        org.apache.geronimo.jee.naming.ObjectFactory namingFactory = new org.apache.geronimo.jee.naming.ObjectFactory();
        org.apache.geronimo.jee.security.ObjectFactory securityFactory = new org.apache.geronimo.jee.security.ObjectFactory();
        OpenejbJar openEjbJar = openejbFactory.createOpenejbJar();

        openEjbJar.setDbSyntaxFactory("dbsyntaxfactory");
        openEjbJar.setEjbQlCompilerFactory("ejbqlcompilerfactory");
        Empty empty = openejbFactory.createEmpty();
        openEjbJar.setEnforceForeignKeyConstraints(empty);

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
        openEjbJar.setEnvironment (environment);

        // set the Cmp Connection Factory
        ResourceLocator resourceLocator = namingFactory.createResourceLocator();
        resourceLocator.setUrl("resourcelocator-url");
        resourceLocator.setResourceLink("resourcelocator-resourcelink");
        org.apache.geronimo.jee.naming.Pattern namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("resourcelocator-pattern-artifactid");
        namingPattern.setGroupId("resourcelocator-pattern-groupid");
        namingPattern.setModule("resourcelocator-pattern-module");
        namingPattern.setName("resourcelocator-pattern-name");
        namingPattern.setVersion("resourcelocator-pattern-version");
        resourceLocator.setPattern(namingPattern);
        openEjbJar.setCmpConnectionFactory(resourceLocator);

        // set the  Enterprise Bean 
        OpenejbJar.EnterpriseBeans enterpriseBeans = openejbFactory.createOpenejbJarEnterpriseBeans();
        MessageDrivenBean messageDrivenBean = openejbFactory.createMessageDrivenBean();
        messageDrivenBean.setEjbName("messagedrivenbean-ejbname");
        messageDrivenBean.setId("messagedrivenbean-id");
        PersistenceUnitRef persistenceUnitRef = namingFactory.createPersistenceUnitRef();
        persistenceUnitRef.setPersistenceUnitName("messagedrivenbean-persistenceunitref-unitname");
        persistenceUnitRef.setPersistenceUnitRefName("messagedrivenbean-persistenceunitref-unitrefname");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedrivenbean-persistenceunitref-pattern-artifactid");
        namingPattern.setGroupId("messagedrivenbean-persistenceunitref-pattern-groupid");
        namingPattern.setModule("messagedrivenbean-persistenceunitref-pattern-module");
        namingPattern.setName("messagedrivenbean-persistenceunitref-pattern-name");
        namingPattern.setVersion("messagedrivenbean-persistenceunitref-pattern-version");
        persistenceUnitRef.setPattern(namingPattern);
        messageDrivenBean.getAbstractNamingEntry().add(namingFactory.createAbstractNamingEntry(persistenceUnitRef));
        ActivationConfig activationConfig = openejbFactory.createActivationConfig();
        ActivationConfigProperty activationConfigProperty = openejbFactory.createActivationConfigProperty();
        activationConfigProperty.setActivationConfigPropertyName("messagedrivenbean-activationconfigproperty-name");
        activationConfigProperty.setActivationConfigPropertyValue("messagedrivenbean-activationconfigproperty-value");
        activationConfig.getActivationConfigProperty().add(activationConfigProperty);
        activationConfig.getDescription().add("messagedrivenbean-activationconfig-description");
        messageDrivenBean.setActivationConfig(activationConfig);
        EjbLocalRef ejbLocalRef = namingFactory.createEjbLocalRef();
        ejbLocalRef.setRefName("messagedrivenbean-ejblocalref-refname");
        ejbLocalRef.setEjbLink("messagedrivenbean-ejblocalref-ejblink");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedrivenbean-ejblocalref-pattern-artifactid");
        namingPattern.setGroupId("messagedrivenbean-ejblocalref-pattern-groupid");
        namingPattern.setModule("messagedrivenbean-ejblocalref-pattern-module");
        namingPattern.setName("messagedrivenbean-ejblocalref-pattern-name");
        namingPattern.setVersion("messagedrivenbean-ejblocalref-pattern-version");
        ejbLocalRef.setPattern(namingPattern);
        messageDrivenBean.getEjbLocalRef().add(ejbLocalRef);
        EjbRef ejbRef = namingFactory.createEjbRef();
        ejbRef.setName("messagedrivenbean-ejbref-name");
        ejbRef.setRefName("messagedrivenbean-ejbref-refname");
        ejbRef.setCssLink("messagedrivenbean-ejbref-csslink");
        ejbRef.setEjbLink("messagedrivenbean-ejbref-ejblink");
        ejbRef.setNsCorbaloc("messagedrivenbean-ejbref-nscorbaloc");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedrivenbean-ejbref-css-artifactid");
        namingPattern.setGroupId("messagedrivenbean-ejbref-css-groupid");
        namingPattern.setModule("messagedrivenbean-ejbref-css-module");
        namingPattern.setName("messagedrivenbean-ejbref-css-name");
        namingPattern.setVersion("messagedrivenbean-ejbref-css-version");
        ejbRef.setCss(namingPattern);
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedrivenbean-ejbref-pattern-artifactid");
        namingPattern.setGroupId("messagedrivenbean-ejbref-pattern-groupid");
        namingPattern.setModule("messagedrivenbean-ejbref-pattern-module");
        namingPattern.setName("messagedrivenbean-ejbref-pattern-name");
        namingPattern.setVersion("messagedrivenbean-ejbref-pattern-version");
        ejbRef.setPattern(namingPattern);
        messageDrivenBean.getEjbRef().add(ejbRef);
        resourceLocator = namingFactory.createResourceLocator();
        resourceLocator.setUrl("messagedrivenbean-resourcelocator-url");
        resourceLocator.setResourceLink("messagedrivenbean-resourcelocator-resourcelink");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedrivenbean-resourcelocator-pattern-artifactid");
        namingPattern.setGroupId("messagedrivenbean-resourcelocator-pattern-groupid");
        namingPattern.setModule("messagedrivenbean-resourcelocator-pattern-module");
        namingPattern.setName("messagedrivenbean-resourcelocator-pattern-name");
        namingPattern.setVersion("messagedrivenbean-resourcelocator-pattern-version");
        resourceLocator.setPattern(namingPattern);
        messageDrivenBean.setResourceAdapter(resourceLocator);
        ResourceEnvRef resourceEnvRef = namingFactory.createResourceEnvRef();
        resourceEnvRef.setAdminObjectLink("messagedrivenbean-resourceenvref-adminobjectlink");
        resourceEnvRef.setAdminObjectModule("messagedrivenbean-resourceenvref-adminobjectmodule");
        resourceEnvRef.setMessageDestinationLink("messagedrivenbean-resourceenvref-messagedestinationlink");
        resourceEnvRef.setRefName("messagedrivenbean-resourceenvref-refname");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedrivenbean-resourceenvref-pattern-artifactid");
        namingPattern.setGroupId("messagedrivenbean-resourceenvref-pattern-groupid");
        namingPattern.setModule("messagedrivenbean-resourceenvref-pattern-module");
        namingPattern.setName("messagedrivenbean-resourceenvref-pattern-name");
        namingPattern.setVersion("messagedrivenbean-resourceenvref-pattern-version");
        resourceEnvRef.setPattern(namingPattern);
        messageDrivenBean.getResourceEnvRef().add(resourceEnvRef);
        ResourceRef resourceRef = namingFactory.createResourceRef();
        resourceRef.setRefName("messagedrivenbean-resourceref-refname");
        resourceRef.setResourceLink("messagedrivenbean-resourceref-resourcelink");
        resourceRef.setUrl("messagedrivenbean-resourceref-url");
        namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("messagedrivenbean-resourceref-pattern-artifactid");
        namingPattern.setGroupId("messagedrivenbean-resourceref-pattern-groupid");
        namingPattern.setModule("messagedrivenbean-resourcref-pattern-module");
        namingPattern.setName("messagedrivenbean-resourceref-pattern-name");
        namingPattern.setVersion("messagedrivenbean-resourceref-pattern-version");
        resourceRef.setPattern(namingPattern);
        messageDrivenBean.getResourceRef().add(resourceRef);
        ServiceRef serviceRef = namingFactory.createServiceRef();
        serviceRef.setServiceRefName("messagedrivenbean-serviceref-refname");
        ServiceCompletion serviceCompletion = namingFactory.createServiceCompletion();
        serviceCompletion.setServiceName("messagedrivenbean-serviceref-servicecompletion-servicename");
        PortCompletion portCompletion = namingFactory.createPortCompletion();
        portCompletion.setBindingName("messagedrivenbean-serviceref-servicecompletion-portcompletion-bindingname");
        Port port = namingFactory.createPort();
        port.setCredentialsName("messagedrivenbean-serviceref-servicecompletion-portcompletion-port-credentialsname");
        port.setHost("messagedrivenbean-serviceref-servicecompletion-portcompletion-port-host");
        port.setPort(new Integer(0));
        port.setPortName("messagedrivenbean-serviceref-servicecompletion-portcompletion-port-portname");
        port.setProtocol("messagedrivenbean-serviceref-servicecompletion-portcompletion-port-protocol");
        port.setUri("messagedrivenbean-serviceref-servicecompletion-portcompletion-port-uri");
        portCompletion.setPort(port);
        serviceCompletion.getPortCompletion().add(portCompletion);
        serviceRef.setServiceCompletion(serviceCompletion);
        port = namingFactory.createPort();
        port.setCredentialsName("messagedrivenbean-serviceref-port-credentialsname");
        port.setHost("messagedrivenbean-serviceref-port-host");
        port.setPort(new Integer(0));
        port.setPortName("messagedrivenbean-serviceref-port-portname");
        port.setProtocol("messagedrivenbean-serviceref-port-protocol");
        port.setUri("messagedrivenbean-serviceref-port-uri");
        serviceRef.getPort().add(port);
        messageDrivenBean.getServiceRef().add(serviceRef);
        enterpriseBeans.getSessionOrEntityOrMessageDriven().add(messageDrivenBean);
        openEjbJar.setEnterpriseBeans(enterpriseBeans);

        // set the Relationships
        Relationships relationships = openejbFactory.createRelationships();
        EjbRelation ejbRelation = openejbFactory.createEjbRelation();
        ejbRelation.setEjbRelationName("ejbrelation-ejbrelationname");
        ejbRelation.setManyToManyTableName("ejbrelation-manytomanytablename");
        EjbRelationshipRole ejbRelationshipRole = openejbFactory.createEjbRelationshipRole();
        ejbRelationshipRole.setEjbRelationshipRoleName("ejbrelation-role-name");
        EjbRelationshipRole.CmrField cmrField = openejbFactory.createEjbRelationshipRoleCmrField();
        cmrField.setCmrFieldName("ejbrelation-role-cmrfieldname");
        ejbRelationshipRole.setCmrField(cmrField);
        //TODO ejbRelationshipRole.setForeignKeyColumnOnSource(value);
        EjbRelationshipRole.RelationshipRoleSource relationshipRoleSource = openejbFactory.createEjbRelationshipRoleRelationshipRoleSource();
        relationshipRoleSource.setEjbName("ejbrelation-role-ejbname");
        ejbRelationshipRole.setRelationshipRoleSource(relationshipRoleSource);
        EjbRelationshipRole.RoleMapping roleMapping = openejbFactory.createEjbRelationshipRoleRoleMapping();
        EjbRelationshipRole.RoleMapping.CmrFieldMapping cmrFieldMapping = openejbFactory.createEjbRelationshipRoleRoleMappingCmrFieldMapping();
        cmrFieldMapping.setKeyColumn("ejbrelation-role-mapping-keycolumn");
        cmrFieldMapping.setForeignKeyColumn("ejbrelation-role-mapping-foreignkeycolumn");
        roleMapping.getCmrFieldMapping().add(cmrFieldMapping);
        ejbRelationshipRole.setRoleMapping(roleMapping);
        ejbRelation.getEjbRelationshipRole().add(ejbRelationshipRole);
        relationships.getEjbRelation().add(ejbRelation);
        openEjbJar.setRelationships(relationships);

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
        openEjbJar.getMessageDestination().add(messageDestination);

        // set the Service
        Gbean gbean = deploymentFactory.createGbean();
        gbean.setClazz("gbean-class");
        gbean.setName("gbean-name");
        openEjbJar.getService().add(deploymentFactory.createGbean(gbean));

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
        openEjbJar.setSecurity(securityFactory.createSecurity(security));

        JAXBElement<OpenejbJar> jaxbElement = openejbFactory.createOpenejbJar(openEjbJar);
        
        // 
        // Marshall the output of the unmarshall
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                "org.apache.geronimo.jee.openejb:" +
                "org.apache.geronimo.jee.application:" +
                "org.apache.geronimo.jee.deployment:" +
                "org.apache.geronimo.jee.security:" +
                "org.apache.geronimo.jee.naming", getClass().getClassLoader() );
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
