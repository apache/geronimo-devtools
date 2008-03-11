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
package org.apache.geronimo.st.v20.core.jaxb;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.xml.ns.deployment_1.EnvironmentType;
import org.apache.geronimo.xml.ns.j2ee.web_2_0.WebAppType;
import org.apache.geronimo.xml.ns.security_2.SecurityType;

/**
 * @version $Rev$ $Date$
 */
public class JAXBModelUtils {
	
	public static SecurityType getSecurityType(JAXBElement element) {
		Object plan = element.getValue();
		if ( WebAppType.class.isInstance( plan ) ) {
			return ((WebAppType)plan).getSecurity() == null ? null : (SecurityType)((WebAppType)plan).getSecurity().getValue();
		}
		return null;
	}
	
	public static void setSecurityType(JAXBElement element, SecurityType security) {
		Object plan = element.getValue();
		if ( WebAppType.class.isInstance( plan ) ) {
			((WebAppType)plan).setSecurity((new org.apache.geronimo.xml.ns.security_2.ObjectFactory()).createSecurity( security ) );
		}
	}
	
	public static EnvironmentType getEnvironmentType(JAXBElement element) {
		Object plan = element.getValue();
		if ( WebAppType.class.isInstance( plan ) ) {
			return ((WebAppType)plan).getEnvironment() == null ? null : ((WebAppType)plan).getEnvironment();
		}
		return null;
	}
	
	public static List getServiceOrPersistence(JAXBElement element) {
		Object plan = element.getValue();
		if ( WebAppType.class.isInstance( plan ) ) {
			return ((WebAppType)plan).getServiceOrPersistence() == null ? null : ((WebAppType)plan).getServiceOrPersistence();
		}
		return null;
	}
}
