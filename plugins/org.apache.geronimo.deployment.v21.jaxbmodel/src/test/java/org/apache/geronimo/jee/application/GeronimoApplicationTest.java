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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.geronimo.jee.deployment.Module;
import org.custommonkey.xmlunit.Diff;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

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
 * </ol>
 * 
 * 
 * @version $Rev$ $Date$  
 */
public class GeronimoApplicationTest extends TestCase {

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
                         "application/geronimo-application-expected-3.xml");
    }


    private void unmarshallAndMarshall(String fileExample, String fileExpected) throws Exception {

        // 
        // Create unmarshaller and marshaller
        // 
        JAXBContext jAXBContext = JAXBContext.newInstance(Application.class,Module.class);
        Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
        Marshaller marshaller = jAXBContext.createMarshaller();
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
        Object jAXBElement = unmarshaller.unmarshal(new InputSource(new ByteArrayInputStream(example.getBytes())));

        // 
        // Marshall the output of the unmarshall
        // 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(jAXBElement, baos);
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
            System.out.println("[Example XML] " + '\n' + example + '\n');
            System.out.println("[Expected XML] " + '\n' + expected + '\n');
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
            throw e;            
        }

    }

    private  void convertNamespace(String fileExample,String fileExpected) throws Exception {

        // 
        // Create unmarshaller and marshaller
        // 
        JAXBContext jAXBContext = JAXBContext.newInstance(Application.class,Module.class);
        Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
        Marshaller marshaller = jAXBContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

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
        SAXSource source = new SAXSource(xmlFilter, new InputSource(new ByteArrayInputStream(expected.getBytes())));
        Object jAXBElement = unmarshaller.unmarshal(source);

        // 
        // Marshall the output of the unmarshall
        // 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(jAXBElement, baos);
        byte[] bytes = baos.toByteArray();
        String actual = new String(bytes);

        // 
        // Compare actual and expected
        // 
        try {
            Diff myDiff = new Diff(expected, actual);
            assertTrue("Files are similar " + myDiff, myDiff.similar());
        } catch (AssertionFailedError e) {
            System.out.println("[Example XML] " + '\n' + example + '\n');
            System.out.println("[Expected XML] " + '\n' + expected + '\n');
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
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

    public static class NamespaceFilter extends XMLFilterImpl {

        public NamespaceFilter(XMLReader xmlReader) {
            super(xmlReader);
        }

        public void startElement(String uri, String localName, String qname, Attributes atts) throws SAXException {
            if (uri.equals("http://geronimo.apache.org/xml/ns/j2ee/application-1.2")) {
                uri = "http://geronimo.apache.org/xml/ns/j2ee/application-2.0";
            }
            else if (uri.equals("http://geronimo.apache.org/xml/ns/deployment-1.1")) {
                uri = "http://geronimo.apache.org/xml/ns/deployment-1.2";
            }
            super.startElement(uri, localName, qname, atts);
        }
    }

}
