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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

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

    public void testCompleteXML() throws Exception {
//      buildFullXMLFromScratch("openejb/openejb-jar-expected-11.xml");
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

    private void buildFullXMLFromScratch (String fileExpected) throws Exception {
/*
        org.apache.geronimo.jee.openejb.ObjectFactory openejbFactory = new org.apache.geronimo.jee.openejb.ObjectFactory();
        org.apache.geronimo.jee.deployment.ObjectFactory deploymentFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
        org.apache.geronimo.jee.naming.ObjectFactory namingFactory = new org.apache.geronimo.jee.naming.ObjectFactory();
        org.apache.geronimo.jee.persistence.ObjectFactory persistenceFactory = new org.apache.geronimo.jee.persistence.ObjectFactory();
        org.apache.geronimo.jee.security.ObjectFactory securityFactory = new org.apache.geronimo.jee.security.ObjectFactory();
        GeronimoEjbJar geronimoEjbJar = openejbFactory.createGeronimoEjbJar();

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
        geronimoEjbJar.setEnvironment (environment);
        
        // set the Open EJB Jar TODO
        //OpenejbJar openejbJar = openejbFactory.createOpenejbJar();
        //openejbJar.setAny("openejbjar-any");
        //geronimoEjbJar.setOpenejbJar(openejbJar);

        // set the JNDI Environment Refs Group
        PersistenceUnitRef persistenceUnitRef = namingFactory.createPersistenceUnitRef();
        persistenceUnitRef.setPersistenceUnitName("jndienvironmentrefsgroup-persistenceunitref-unitname");
        persistenceUnitRef.setPersistenceUnitRefName("jndienvironmentrefsgroup-persistenceunitref-unitrefname");
        org.apache.geronimo.jee.naming.Pattern namingPattern = namingFactory.createPattern();
        namingPattern.setArtifactId("jndienvironmentrefsgroup-persistenceunitref-pattern-artifactid");
        namingPattern.setGroupId("jndienvironmentrefsgroup-persistenceunitref-pattern-groupid");
        namingPattern.setModule("jndienvironmentrefsgroup-persistenceunitref-pattern-module");
        namingPattern.setName("jndienvironmentrefsgroup-persistenceunitref-pattern-name");
        namingPattern.setVersion("jndienvironmentrefsgroup-persistenceunitref-pattern-version");
        persistenceUnitRef.setPattern(namingPattern);
        geronimoEjbJar.getJndiEnvironmentRefsGroup().add(namingFactory.createAbstractNamingEntry(persistenceUnitRef));

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
        geronimoEjbJar.getMessageDestination().add(messageDestination);

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
        geronimoEjbJar.getServiceOrPersistence().add(persistence);

        // set the TSS Link
        TssLink tssLink = openejbFactory.createTssLink();
        tssLink.setEjbName("tsslink-ejbname");
        tssLink.setTssName("tsslink-tssname");
        tssLink.getJndiName().add("tssname-jndiname");
        geronimoEjbJar.getTssLink().add(tssLink);

        // set the Web Service Binding
        WebServiceBinding webServiceBinding = openejbFactory.createWebServiceBinding();
        webServiceBinding.setEjbName("webservicebinding-ejbname");
        webServiceBinding.setWebServiceAddress("webservicebinding-webserviceaddress");
        webServiceBinding.getWebServiceVirtualHost().add("webservicebinding-webservicevirtualhost");
        WebServiceSecurity webServiceSecurity = openejbFactory.createWebServiceSecurity();
        webServiceSecurity.setRealmName("webservicebinding-webservicesecurity-realmname");
        webServiceSecurity.setSecurityRealmName("webservicebinding-webservicesecurity-securityrealmname");
        webServiceSecurity.setAuthMethod(AuthMethod.BASIC);
        webServiceSecurity.setTransportGuarantee(TransportGuarantee.INTEGRAL);
        webServiceBinding.setWebServiceSecurity(webServiceSecurity);
        geronimoEjbJar.getWebServiceBinding().add(webServiceBinding);

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
        geronimoEjbJar.setSecurity(securityFactory.createSecurity(security));

        JAXBElement<GeronimoEjbJar> jaxbElement = openejbFactory.createEjbJar(geronimoEjbJar);
        
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
            System.out.println("Actual XML:\n" + actual + '\n');
            System.out.println("[Expected XML: " + fileExpected + "]\n" + expected + '\n');
            throw e;            
        }
*/
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
