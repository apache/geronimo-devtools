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
package org.apache.geronimo.st.v21.core.jaxb;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.security.Security;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.st.v21.core.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * @version $Rev$ $Date$
 */
public class JAXBModelUtils {
	
	public static Security getSecurity(JAXBElement element) {
		Object plan = element.getValue();
		if ( WebApp.class.isInstance( plan ) ) {
			return ((WebApp)plan).getSecurity() == null ? null : (Security)((WebApp)plan).getSecurity().getValue();
		}
		return null;
	}
	
	public static void setSecurity(JAXBElement element, Security security) {
		Object plan = element.getValue();
		if ( WebApp.class.isInstance( plan ) ) {
			((WebApp)plan).setSecurity((new org.apache.geronimo.jee.security.ObjectFactory()).createSecurity( security ) );
		}
	}
	
	public static Environment getEnvironment(JAXBElement element) {
		Object plan = element.getValue();
		if ( WebApp.class.isInstance( plan ) ) {
			System.out.println( "Element : " + ((WebApp)plan).getEnvironment() );
			return ((WebApp)plan).getEnvironment() == null ? null : ((WebApp)plan).getEnvironment();
		}
		return null;
	}
	
	public static List getServiceOrPersistence(JAXBElement element) {
		Object plan = element.getValue();
		if ( WebApp.class.isInstance( plan ) ) {
			return ((WebApp)plan).getServiceOrPersistence() == null ? null : ((WebApp)plan).getServiceOrPersistence();
		}
		return null;
	}
	
	public static List getGbeans(JAXBElement element) {
		Object plan = element.getValue();
		if ( WebApp.class.isInstance( plan ) ) {
//			return ((WebApp)plan).getGbeans() == null ? null : ((WebApp)plan).getGbeans();
		}
		return null;
	}

}
