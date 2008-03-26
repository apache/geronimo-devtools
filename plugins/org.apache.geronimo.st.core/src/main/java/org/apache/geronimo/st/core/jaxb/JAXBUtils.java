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
package org.apache.geronimo.st.core.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.geronimo.st.core.Activator;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * @version $Rev$ $Date$
 */
public class JAXBUtils {

	// JAXBContext instantiation is costly - must be done only once!
	private static final JAXBContext jaxbContext = newJAXBContext();
	private static JAXBContext newJAXBContext() {
		try {
            return JAXBContext.newInstance( 
            		"org.apache.geronimo.jee.connector:" +
                    "org.apache.geronimo.jee.openejb:" +
                    "org.apache.geronimo.jee.web:" +
                    "org.apache.geronimo.jee.application:" +
                    "org.apache.geronimo.jee.deployment:" +
                    "org.apache.geronimo.jee.naming:" +
                    "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
		} catch (JAXBException e) {
			Trace.tracePoint("JAXBException", "JAXBContext.newInstance");
			e.printStackTrace();
		}
		return null;
	}

	public static void marshalDeploymentPlan(JAXBElement jaxbElement, IFile file) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			marshaller.marshal(jaxbElement, buffer);
			file.setContents(new ByteArrayInputStream(buffer.toByteArray()), IFile.FORCE, null);
		} catch (JAXBException jaxbException) {
			Trace.tracePoint("JAXBException", "JAXBUtils.marshallToIFile()", file.getFullPath());
			jaxbException.printStackTrace();
		} catch (CoreException coreException) {
			Trace.tracePoint("CoreException", "JAXBUtils.marshallToIFile()", file.getFullPath());
			coreException.printStackTrace();
		}
	}

	public static JAXBElement unmarshalDeploymentPlan(IFile file) {
		try {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			JAXBElement plan = (JAXBElement) unmarshaller.unmarshal(file.getContents());
			return plan;
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object getValue( Object element, String name ) {
		try {
			Method method = element.getClass().getMethod( "get" + name, null);
			return method.invoke(element, null);
		} catch ( NoSuchMethodException e ) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setValue( Object element, String name, Object value ) {
		try {
			Method[] methods = element.getClass().getMethods();
			for ( Method method: methods) {
				if ( method.getName().equals( "set" + name ) ) {
					method.invoke( element, value );
					return;
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println( "============== No such method get" + name + " in class " + element.getClass().getName() );
	}
}
