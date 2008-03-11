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

import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.xml.ns.deployment_1.GbeanType;
import org.apache.geronimo.xml.ns.naming_1.ObjectFactory;
import org.apache.geronimo.xml.ns.naming_1.ResourceRefType;
import org.apache.geronimo.xml.ns.security_2.DescriptionType;
import org.apache.geronimo.xml.ns.security_2.RoleMappingsType;
import org.apache.geronimo.xml.ns.security_2.RoleType;
import org.apache.geronimo.xml.ns.security_2.SecurityType;

/**
 * @version $Rev$ $Date$
 */
public class JAXBObjectFactoryImpl implements JAXBObjectFactory {

	private static JAXBObjectFactoryImpl instance = new JAXBObjectFactoryImpl();
	
	private JAXBObjectFactoryImpl() {
		
	}
	
	public static JAXBObjectFactoryImpl getInstance() {
		return instance;
	}
	
	public Object create(Class type) {
		System.out.println( type );
		if ( type.equals( ResourceRefType.class ) ) {
			return (new ObjectFactory()).createResourceRefType();
		} else if ( type.equals( SecurityType.class ) ) {
			return (new org.apache.geronimo.xml.ns.security_2.ObjectFactory()).createSecurityType();
		} else if ( type.equals( RoleMappingsType.class ) ) {
			return (new org.apache.geronimo.xml.ns.security_2.ObjectFactory()).createRoleMappingsType();
		} else if ( type.equals( DescriptionType.class ) ) {
			return (new org.apache.geronimo.xml.ns.security_2.ObjectFactory()).createDescriptionType();
		} else if ( type.equals( RoleType.class ) ) {
			return (new org.apache.geronimo.xml.ns.security_2.ObjectFactory()).createRoleType();
		} else if ( type.equals( GbeanType.class ) ) {
			return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createGbeanType();
		}
		
		return null;
	}

}
