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
package org.apache.geronimo.st.v11.core.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

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

import org.apache.geronimo.st.v11.core.Activator;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.apache.geronimo.st.core.jaxb.IJAXBUtilsProvider;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @version $Rev: 726047 $ $Date: 2008-12-12 23:35:00 +0800 (Fri, 12 Dec 2008) $
 */
public class JAXB11Utils implements IJAXBUtilsProvider{

    // JAXBContext instantiation is costly - must be done only once!
    private static final JAXBContext jaxbContext = newJAXBContext();
    private static final MarshallerListener marshallerListener = new MarshallerListener();
    //private static JAXB11Utils _instance = new JAXB11Utils();
    
    
    private static JAXBContext newJAXBContext() {
        try {
            return JAXBContext.newInstance( 
                    "org.apache.geronimo.xml.ns.deployment_1:" +
                    "org.apache.geronimo.xml.ns.j2ee.application_1:" +
                    "org.apache.geronimo.xml.ns.j2ee.application_client_1:" +
                    "org.apache.geronimo.xml.ns.j2ee.connector_1:" +
                    "org.apache.geronimo.xml.ns.j2ee.web_1:" +
                    "org.apache.geronimo.xml.ns.naming_1:" +
                    "org.apache.geronimo.xml.ns.security_1:" +
                    "org.openejb.xml.ns.openejb_jar_2:"+
                    "org.openejb.xml.ns.pkgen_2:"+
                    "org.openejb.xml.ns.corba_css_config_2:"+
                    "org.openejb.xml.ns.corba_tss_config_2:", Activator.class.getClassLoader() );
        } catch (JAXBException e) {
            Trace.tracePoint("JAXBException", "JAXBContext.newInstance");
            e.printStackTrace();
        }
        return null;
    }
    
  /*  public JAXB11Utils(){
    }
    
    public static JAXB11Utils getInstance(){
    	return _instance;
    }*/

    
    public void marshalDeploymentPlan(JAXBElement jaxbElement, IFile file) throws Exception {
        try {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setListener(marshallerListener);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
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
            NamespacePrefix.processPrefix(doc);

            xformer.transform(new DOMSource(doc), out);
            ByteArrayInputStream inBuffer = new ByteArrayInputStream(outBuffer.toByteArray());
            if(file.exists()) {
                file.setContents(inBuffer, true, false, null);
            } else {
                prepareFolder(file.getParent());
                file.create(inBuffer, true, null);
            }
        } catch (JAXBException jaxbException) {
            Trace.tracePoint("JAXBException", "JAXB11Utils.marshalDeploymentPlan()", file.getFullPath());
            throw jaxbException;
        } catch (CoreException coreException) {
            Trace.tracePoint("CoreException", "JAXB11Utils.marshalDeploymentPlan()", file.getFullPath());
            throw coreException;
        } catch (ParserConfigurationException e) {
        	Trace.tracePoint("ParserConfigurationException", "JAXB11Utils.marshalDeploymentPlan()", file.getFullPath());
        	throw e;
		} catch (TransformerConfigurationException e) {
			Trace.tracePoint("TransformerConfigurationException", "JAXB11Utils.marshalDeploymentPlan()", file.getFullPath());
			throw e;
		} catch (UnsupportedEncodingException e) {
			Trace.tracePoint("UnsupportedEncodingException", "JAXB11Utils.marshalDeploymentPlan()", file.getFullPath());
			throw e;
		} catch (TransformerException e) {
			Trace.tracePoint("TransformerException", "JAXB11Utils.marshalDeploymentPlan()", file.getFullPath());
			throw e;
		}
    }

    public JAXBElement unmarshalFilterDeploymentPlan(IFile file) throws Exception {
        try {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            SAXSource source = new SAXSource(null, new InputSource(file.getContents()));
            JAXBElement plan = (JAXBElement) unmarshaller.unmarshal(source);
            return plan;
        } catch (JAXBException e) {
            Trace.tracePoint("JAXBException", "JAXB11Utils.unmarshalFilterDeploymentPlan()", file.getFullPath());
            throw e;
        } catch (CoreException e) {
            Trace.tracePoint("CoreException", "JAXB11Utils.unmarshalFilterDeploymentPlan()", file.getFullPath());
            throw e;
        } catch (ParserConfigurationException e) {
            Trace.tracePoint("ParserConfigurationException", "JAXB11Utils.unmarshalFilterDeploymentPlan()", file.getFullPath());
            throw e;
        } catch (SAXException e) {
            Trace.tracePoint("SAXException", "JAXB11Utils.unmarshalFilterDeploymentPlan()", file.getFullPath());
            throw e;
        }
    }

    private void prepareFolder(IContainer folder) throws CoreException {
        if (folder.exists() || !(folder instanceof IFolder)) {
            return;
        }
        // prepare the upper level folders recursively
        prepareFolder(folder.getParent());
        ((IFolder) folder).create(true, true, null);
    }

    public Object getValue( Object element, String name ) throws Exception {
        try {
            if (String.class.isInstance(element))
                return (String)element;
            Method method = element.getClass().getMethod( "get" + name, null);
            return method.invoke(element, null);
        } catch ( Exception e ) {
            throw e;
        }
    }
    
    public void setValue( Object element, String name, Object value ) throws Exception {
        try {
            Method[] methods = element.getClass().getMethods();
            for ( Method method: methods) {
                if ( method.getName().equals( "set" + name ) ) {
                    method.invoke( element, value );
                    return;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        System.out.println( "============== No such method set" + name + " in class " + element.getClass().getName() );
    }

	public JAXBContext getJAXBContext() {
	    return jaxbContext;
    }

	public void marshalPlugin(JAXBElement jaxbElement, OutputStream outputStream)
            throws Exception {
	    
    }

	public JAXBElement unmarshalPlugin(InputStream inputStream) {
	    return null;
    }
}