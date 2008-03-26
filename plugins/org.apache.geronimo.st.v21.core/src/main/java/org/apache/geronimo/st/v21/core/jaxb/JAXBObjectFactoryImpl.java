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

import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.naming.ObjectFactory;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.RoleMappings;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.Security;

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
		if ( type.equals( ResourceRef.class ) ) {
			return (new ObjectFactory()).createResourceRef();
		} else if ( type.equals( Security.class ) ) {
			return (new org.apache.geronimo.jee.security.ObjectFactory()).createSecurity();
		} else if ( type.equals( RoleMappings.class ) ) {
			return (new org.apache.geronimo.jee.security.ObjectFactory()).createRoleMappings();
		} else if ( type.equals( Description.class ) ) {
			return (new org.apache.geronimo.jee.security.ObjectFactory()).createDescription();
		} else if ( type.equals( Role.class ) ) {
			return (new org.apache.geronimo.jee.security.ObjectFactory()).createRole();
		} else if ( type.equals( Gbean.class ) ) {
			return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createGbean();
		} else if ( type.equals( Artifact.class ) ) {
			return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createArtifact();
		} else if ( type.equals( Dependencies.class ) ) {
			return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createDependencies();
		} else if ( type.equals( Dependency.class ) ) {
			return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createDependency();
		}
		
		return null;
	}

}
