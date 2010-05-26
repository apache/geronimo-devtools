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

package org.apache.geronimo.jee.application;

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
import org.apache.geronimo.jee.deployment.Pattern;
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
 * <strong>GeronimoApplicationTest</strong> is used to test various JAXB 
 * operations on the following Geronimo-specific XML file supported by the GEP:
 * 
 * <ul>
 *      <li>geronimo-application.xml
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
public class GeronimoApplicationTest extends TestCase {

    /*------------------------------------------------------------------------*\
    |                                                                          |
    |  Testcase(s)                                                             | 
    |                                                                          |
    \*------------------------------------------------------------------------*/
    public void testUnmarshallAndMarshall() throws Exception {
        unmarshallAndMarshall("application/geronimo-application-example-1.xml", 
                              "application/geronimo-application-expected-1.xml");
        unmarshallAndMarshall("application/geronimo-application-example-2.xml", 
                              "application/geronimo-application-expected-2.xml");
        unmarshallAndMarshall("application/geronimo-application-example-3.xml", 
                              "application/geronimo-application-expected-3.xml");
    }

    public void testConvertNamespace() throws Exception {
        convertNamespace("application/geronimo-application-example-4.xml",
                         "application/geronimo-application-expected-1.xml");
        convertNamespace("application/geronimo-application-example-5.xml", 
                         "application/geronimo-application-expected-2.xml");
        convertNamespace("application/geronimo-application-example-6.xml", 
                         "application/geronimo-application-expected-6.xml");
    }

    public void testCompleteXML() throws Exception {
        buildFullXMLFromScratch("application/geronimo-application-expected-11.xml");
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
                                    "org.apache.geronimo.jee.application:" +
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
                                    "org.apache.geronimo.jee.application:" +
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
        }
        catch (AssertionFailedError e) {
            System.out.println("[Example XML: " + fileExample + "] " + '\n' + example + '\n');
            System.out.println("[Expected XML: " + fileExpected + "] " + '\n' + expected + '\n');
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
            throw e;            
        }
    }

    private void buildFullXMLFromScratch (String fileExpected) throws Exception {
        org.apache.geronimo.jee.application.ObjectFactory applicationFactory = new org.apache.geronimo.jee.application.ObjectFactory();
        org.apache.geronimo.jee.deployment.ObjectFactory deploymentFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
        org.apache.geronimo.jee.security.ObjectFactory securityFactory = new org.apache.geronimo.jee.security.ObjectFactory();
        Application application = applicationFactory.createApplication();

        // hard code everything to come up with a large XML with everything in it
        application.setApplicationName("test-app-name");

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
        application.setEnvironment(environment);

        // set the Module
        Module module = applicationFactory.createModule();
        Path path = applicationFactory.createPath();
        path.setId("module-altdd-path-id");
        path.setValue("module-altdd-path-value");
        module.setAltDd(path);
//        module.setAny("module-any"); //TODO
        path = applicationFactory.createPath();
        path.setId("module-connector-path-id");
        path.setValue("module-connector-path-value");
        module.setConnector(path);
        path = applicationFactory.createPath();
        path.setId("module-ejb-path-id");
        path.setValue("module-ejb-path-value");
        module.setEjb(path);
        path = applicationFactory.createPath();
        path.setId("module-java-path-id");
        path.setValue("module-java-path-value");
        module.setJava(path);
        path = applicationFactory.createPath();
        path.setId("module-web-path-id");
        path.setValue("module-web-path-value");
        module.setWeb(path);
        application.getModule().add(module);

        // set the Ext Module
        ExtModule extModule = applicationFactory.createExtModule();
        extModule.setInternalPath("extmodule-internalpath");
//        module.setAny("module-any"); //TODO
        path = applicationFactory.createPath();
        path.setId("extmodule-connector-path-id");
        path.setValue("extmodule-connector-path-value");
        extModule.setConnector(path);
        path = applicationFactory.createPath();
        path.setId("extmodule-ejb-path-id");
        path.setValue("extmodule-ejb-path-value");
        extModule.setEjb(path);
        path = applicationFactory.createPath();
        path.setId("extmodule-java-path-id");
        path.setValue("extmodule-java-path-value");
        extModule.setJava(path);
        path = applicationFactory.createPath();
        path.setId("extmodule-web-path-id");
        path.setValue("extmodule-web-path-value");
        extModule.setWeb(path);
        Pattern pattern = deploymentFactory.createPattern();
        pattern.setArtifactId("extmodule-externalpath-artifactid");
        pattern.setCustomFoo("extmodule-externalpath-customfoo");
        pattern.setGroupId("extmodule-externalpath-groupid");
        pattern.setModule("extmodule-externalpath-module");
        pattern.setType("extmodule-externalpath-type");
        pattern.setVersion("extmodule-externalpath-version");
        extModule.setExternalPath(pattern);
        application.getExtModule().add(extModule);

        // set the Service
        Gbean gbean = deploymentFactory.createGbean();
        gbean.setClazz("gbean-class");
        gbean.setName("gbean-name");
        application.getService().add(deploymentFactory.createGbean(gbean));

        // set the Security
        Security security = securityFactory.createSecurity();
        security.setDefaultRole ("security-role");
        security.setDoasCurrentCaller(true);
        security.setUseContextHandler(true);
        pattern = deploymentFactory.createPattern();
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
        application.setSecurity(applicationFactory.createSecurity(security));
 
        JAXBElement<Application> jaxbElement = applicationFactory.createApplication(application);
        
        // 
        // Marshall the output of the unmarshall
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                "org.apache.geronimo.jee.application:" +
                "org.apache.geronimo.jee.security:" +
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
