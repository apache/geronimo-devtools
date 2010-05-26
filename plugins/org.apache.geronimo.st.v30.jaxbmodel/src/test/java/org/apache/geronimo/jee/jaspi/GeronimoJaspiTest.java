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
package org.apache.geronimo.jee.jaspi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.custommonkey.xmlunit.Diff;


/**
 * <strong>GeronimoJaspiTest</strong> is used to test various JAXB 
 * operations on the following OSGi-specific XML file supported by the GEP:
 * 
 * <ul>
 *      <li>geronimo-jaspi.xml
 * </ul>
 * 
 * <p>The following JAXB operations are performed: 
 * <ol>
 *      <li>Create XML with all fields
 * </ol>
 * 
 */
public class GeronimoJaspiTest extends TestCase {

    /*----------------------------------------------------------------------------------------------------------------*\
    |                                                                                                                  |
    |  Testcase(s)                                                                                                     | 
    |                                                                                                                  |
    \*----------------------------------------------------------------------------------------------------------------*/
    public void testCompleteXML() throws Exception {
        buildFullXMLFromScratch("jaspi/geronimo-jaspi-expected.xml");
    }


    /*----------------------------------------------------------------------------------------------------------------*\
    |                                                                                                                  |
    |  Private method(s)                                                                                               | 
    |                                                                                                                  |
    \*----------------------------------------------------------------------------------------------------------------*/
    private void buildFullXMLFromScratch (String fileExpected) throws Exception {

        // 
        // Create the jaspi factory
        // 
        ObjectFactory jaspiFactory = new ObjectFactory();
        Jaspi jaspi = jaspiFactory.createJaspi();

        // 
        // Create numerous objects so they can be used throughout
        // 
        AuthModule authModule = jaspiFactory.createAuthModule();
        ClientAuthConfig clientAuthConfig = jaspiFactory.createClientAuthConfig();
        ClientAuthContext clientAuthContext = jaspiFactory.createClientAuthContext();
        ConfigProvider configProvider = jaspiFactory.createConfigProvider();
        MessagePolicy requestPolicy = jaspiFactory.createMessagePolicy();
        MessagePolicy responsePolicy = jaspiFactory.createMessagePolicy();
        ProtectionPolicy protectionPolicy = jaspiFactory.createProtectionPolicy();
        ServerAuthConfig serverAuthConfig = jaspiFactory.createServerAuthConfig();
        ServerAuthContext serverAuthContext = jaspiFactory.createServerAuthContext();
        Target target = jaspiFactory.createTarget();
        TargetPolicy targetPolicy = jaspiFactory.createTargetPolicy();

        // 
        // ProtectionPolicy
        // 
        protectionPolicy.setClassName("PROTECTION_POLICY_CLASS_NAME");

        // 
        // Target
        // 
        target.setClassName("TARGET_CLASS_NAME");

        // 
        // TargetPolicy
        // 
        targetPolicy.setProtectionPolicy(protectionPolicy);
        targetPolicy.getTarget().add(target);

        // 
        // RequestPolicy
        // 
        requestPolicy.setMandatory(true);
        requestPolicy.getTargetPolicy().add(targetPolicy);

        // 
        // ResponsePolicy
        // 
        responsePolicy.setMandatory(true);
        responsePolicy.getTargetPolicy().add(targetPolicy);

        // 
        // AuthModule
        // 
        authModule.setClassLoaderName("AUTH_MODULE_CLASS_LOADER_NAME");
        authModule.setClassName("AUTH_MODULE_CLASS_NAME");
        authModule.setOptions("AUTH_MODULE_OPTIONS");
        authModule.setRequestPolicy(requestPolicy);
        authModule.setResponsePolicy(responsePolicy);


        // 
        // ClientAuthContext
        // 
        clientAuthContext.setAppContext("CLIENT_AUTH_CONTEXT_APP_CONTEXT");
        clientAuthContext.setAuthenticationContextID("CLIENT_AUTH_CONTEXT_AUTHENTICATION_CONTEXT_ID");
        clientAuthContext.setMessageLayer("CLIENT_AUTH_CONTEXT_MESSAGE_LAYER");
        clientAuthContext.getClientAuthModule().add(authModule);

        // 
        // ClientAuthConfig
        // 
        clientAuthConfig.setAppContext("CLIENT_AUTH_CONFIG_APP_CONTEXT");
        clientAuthConfig.setAuthenticationContextID("CLIENT_AUTH_CONFIG_AUTHENTICATION_CONTEXT_ID");
        clientAuthConfig.setMessageLayer("CLIENT_AUTH_CONFIG_MESSAGE_LAYER");
        clientAuthConfig.setProtected(true);
        clientAuthConfig.getClientAuthContext().add(clientAuthContext);

        // 
        // ConfigProvider
        // 
        configProvider.setAppContext("CONFIG_PROVIDER_APP_CONTEXT");
        configProvider.setClassLoaderName("CONFIG_PROVIDER_CLASS_LOADER_NAME");
        configProvider.setClassName("CONFIG_PROVIDER_CLASS_NAME");
        configProvider.setDescription("CONFIG_PROVIDER_DESCRIPTION");
        configProvider.setMessageLayer("CONFIG_PROVIDER_MESSAGE_LAYER");
        configProvider.setPersistent(true);
        configProvider.setProperties("CONFIG_PROVIDER_PROPERTIES");
        configProvider.getClientAuthConfig().add(clientAuthConfig);
        configProvider.getServerAuthConfig().add(serverAuthConfig);
        jaspi.getConfigProvider().add(configProvider);

        // 
        // ServerAuthConfig
        // 
        serverAuthConfig.setAppContext("SERVER_AUTH_CONFIG_APP_CONTEXT");
        serverAuthConfig.setAuthenticationContextID("SERVER_AUTH_CONFIG_AUTHENTICATION_CONTEXT_ID");
        serverAuthConfig.setMessageLayer("SERVER_AUTH_CONFIG_MESSAGE_LAYER");
        serverAuthConfig.setProtected(true);
        serverAuthConfig.getServerAuthContext().add(serverAuthContext);

        // 
        // ServerAuthContext
        // 
        serverAuthContext.setAppContext("SERVER_AUTH_CONTEXT_APP_CONTEXT");
        serverAuthContext.setAuthenticationContextID("SERVER_AUTH_CONTEXT_AUTHENTICATION_CONTEXT_ID");
        serverAuthContext.setMessageLayer("SERVER_AUTH_CONTEXT_MESSAGE_LAYER");
        serverAuthContext.getServerAuthModule().add(authModule);

        //
        // Finally, create the jaspi XML
        // 
        JAXBElement<Jaspi> jaxbElement = jaspiFactory.createJaspi(jaspi);
        
        //
        // Marshall the Jaspi so that it can be compared with the expected file
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                                    "org.apache.geronimo.jee.jaspi", getClass().getClassLoader() );
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
