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

package org.apache.geronimo.st.v21.core.plugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.geronimo.jaxbmodel.common.operations.NamespaceFilter;
import org.apache.geronimo.jee.jaxbmodel.operations.MarshallerListener;
import org.apache.geronimo.st.v21.core.Activator;
import org.apache.geronimo.st.v21.core.internal.Trace;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
 * This class is used to handle operations on packages org.apache.geronimo.system.plugin.model.
 * Since this package is version specific, the utils class is not included in v21.jaxbmodel 
 * 
 *
 */
public class JAXB21PluginUtils {

    private static final JAXBContext jaxbPluginContext = newJAXBPluginContext();
    private static final MarshallerListener marshallerListener = new MarshallerListener();
    
    public static void marshalPlugin(JAXBElement jaxbElement, OutputStream outputStream) throws Exception {
        try {
            Marshaller marshaller = jaxbPluginContext.createMarshaller();
            marshaller.setListener(marshallerListener);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument(); 

            marshaller.marshal(jaxbElement, doc);

            TransformerFactory xf = TransformerFactory.newInstance();
            try {
                xf.setAttribute("indent-number", new Integer(4));
            } catch (IllegalArgumentException iae) {
                //ignore this. http://forums.sun.com/thread.jspa?threadID=562510&messageID=2841867
            }
            Transformer xformer = xf.newTransformer();
            xformer.setOutputProperty(OutputKeys.METHOD, "xml");
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            xformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4"); 

            ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
            Result out = new StreamResult(new OutputStreamWriter(outBuffer,"UTF-8"));

            xformer.transform(new DOMSource(doc), out);
            ByteArrayInputStream inBuffer = new ByteArrayInputStream(outBuffer.toByteArray());
            outputStream.write(outBuffer.toByteArray());
        } catch (JAXBException jaxbException) {
            Trace.tracePoint("JAXBException", Activator.logPlugin, "JAXBUtils.marshalDeploymentPlan()");
            throw jaxbException;
        } catch (IOException coreException) {
            Trace.tracePoint("IOException", Activator.logPlugin, "JAXBUtils.marshalDeploymentPlan()");
            throw coreException;
        } catch (ParserConfigurationException e) {
            Trace.tracePoint("ParserConfigurationException", Activator.logPlugin, "JAXBUtils.marshalDeploymentPlan()");
            throw e;
        } catch (TransformerConfigurationException e) {
            Trace.tracePoint("TransformerConfigurationException", Activator.logPlugin, "JAXBUtils.marshalDeploymentPlan()");
            throw e;
        } catch (TransformerException e) {
            Trace.tracePoint("TransformerException", Activator.logPlugin, "JAXBUtils.marshalDeploymentPlan()");
            throw e;
        }
    }

    public static JAXBElement unmarshalPlugin(InputStream inputStream) {
        try {
            Unmarshaller unmarshaller = jaxbPluginContext.createUnmarshaller();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            NamespaceFilter xmlFilter = new NamespaceFilter(parser.getXMLReader());
            SAXSource source = new SAXSource(xmlFilter, new InputSource(inputStream));
            JAXBElement plan = (JAXBElement) unmarshaller.unmarshal(source);
            return plan;
        } catch (JAXBException e) {
            Trace.tracePoint("JAXBException", Activator.logPlugin, "JAXBUtils.unmarshalFilterDeploymentPlan()");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            Trace.tracePoint("ParserConfigurationException", Activator.logPlugin, "JAXBUtils.unmarshalFilterDeploymentPlan()");
            e.printStackTrace();
        } catch (SAXException e) {
            Trace.tracePoint("SAXException", Activator.logPlugin, "JAXBUtils.unmarshalFilterDeploymentPlan()");
            e.printStackTrace();
        }
        return null;
    }
    
    private static JAXBContext newJAXBPluginContext() {
        try {
            return JAXBContext.newInstance( 
                    "org.apache.geronimo.system.plugin.model", Activator.class.getClassLoader() );
        } catch (JAXBException e) {
            Trace.tracePoint("JAXBException", Activator.logPlugin, "JAXBContext.newInstance");
            e.printStackTrace();
        }
        return null;
    }
}
