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
package org.apache.geronimo.jee.jaxbmodel.operations;

import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.jee.application.ExtModule;
import org.apache.geronimo.jee.application.Module;
import org.apache.geronimo.jee.application.Path;
import org.apache.geronimo.jee.connector.Adminobject;
import org.apache.geronimo.jee.connector.AdminobjectInstance;
import org.apache.geronimo.jee.connector.ConfigPropertySetting;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Attribute;
import org.apache.geronimo.jee.deployment.ClassFilter;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.deployment.Reference;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.Port;
import org.apache.geronimo.jee.naming.PortCompletion;
import org.apache.geronimo.jee.naming.Property;
import org.apache.geronimo.jee.naming.ServiceCompletion;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.openejb.EjbRelation;
import org.apache.geronimo.jee.openejb.EjbRelationshipRole;
import org.apache.geronimo.jee.openejb.Relationships;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.DistinguishedName;
import org.apache.geronimo.jee.security.LoginDomainPrincipal;
import org.apache.geronimo.jee.security.Principal;
import org.apache.geronimo.jee.security.RealmPrincipal;
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
        if ( type.equals( ResourceRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createResourceRef();
        } else if ( type.equals( ResourceEnvRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createResourceEnvRef();
        } else if ( type.equals( EjbRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createEjbRef();
        } else if ( type.equals( GbeanRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createGbeanRef();
        } else if ( type.equals( PersistenceContextRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createPersistenceContextRef();
        } else if ( type.equals( PersistenceUnitRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createPersistenceUnitRef();
        } else if ( type.equals( MessageDestination.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createMessageDestination();
        } else if ( type.equals( org.apache.geronimo.jee.naming.Pattern.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createPattern();
        } else if ( type.equals( Port.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createPort();
        } else if ( type.equals( PortCompletion.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createPortCompletion();
        } else if ( type.equals( Property.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createProperty();
        } else if ( type.equals( ServiceCompletion.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createServiceCompletion();
        } else if ( type.equals( ServiceRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createServiceRef();
        } else if ( type.equals( EjbLocalRef.class ) ) {
            return (new org.apache.geronimo.jee.naming.ObjectFactory()).createEjbLocalRef();
        } else if ( type.equals( Security.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createSecurity();
        } else if ( type.equals( RoleMappings.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createRoleMappings();
        } else if ( type.equals( Description.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createDescription();
        } else if ( type.equals( Role.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createRole();
        } else if ( type.equals( DistinguishedName.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createDistinguishedName();
        } else if ( type.equals( Principal.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createPrincipal();
        } else if ( type.equals( LoginDomainPrincipal.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createLoginDomainPrincipal();
        } else if ( type.equals( RealmPrincipal.class ) ) {
            return (new org.apache.geronimo.jee.security.ObjectFactory()).createRealmPrincipal();
        } else if ( type.equals( Gbean.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createGbean();
        } else if ( type.equals( Artifact.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createArtifact();
        } else if ( type.equals( ClassFilter.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createClassFilter();
        } else if ( type.equals( Dependencies.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createDependencies();
        } else if ( type.equals( Dependency.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createDependency();
        } else if ( type.equals( Environment.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createEnvironment();
        } else if ( type.equals( org.apache.geronimo.jee.deployment.Pattern.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createPattern();
        } else if ( type.equals( Attribute.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createAttribute();
        } else if ( type.equals( Reference.class ) ) {
            return (new org.apache.geronimo.jee.deployment.ObjectFactory()).createReference();
        } else if ( type.equals( ExtModule.class ) ) {
            return (new org.apache.geronimo.jee.application.ObjectFactory()).createExtModule();
        } else if ( type.equals( Module.class ) ) {
            return (new org.apache.geronimo.jee.application.ObjectFactory()).createModule();
        } else if ( type.equals( Path.class ) ) {
            return (new org.apache.geronimo.jee.application.ObjectFactory()).createPath();
        } else if ( type.equals( EjbRelation.class ) ) {
            return (new org.apache.geronimo.jee.openejb.ObjectFactory()).createEjbRelation();
        } else if ( type.equals( EjbRelationshipRole.class ) ) {
            return (new org.apache.geronimo.jee.openejb.ObjectFactory()).createEjbRelationshipRole();
        } else if ( type.equals( EjbRelationshipRole.RelationshipRoleSource.class ) ) {
            return (new org.apache.geronimo.jee.openejb.ObjectFactory()).createEjbRelationshipRoleRelationshipRoleSource();
        } else if ( type.equals( EjbRelationshipRole.CmrField.class ) ) {
            return (new org.apache.geronimo.jee.openejb.ObjectFactory()).createEjbRelationshipRoleCmrField();
        } else if ( type.equals( EjbRelationshipRole.RoleMapping.CmrFieldMapping.class ) ) {
            return (new org.apache.geronimo.jee.openejb.ObjectFactory()).createEjbRelationshipRoleRoleMappingCmrFieldMapping();
        } else if ( type.equals( EjbRelationshipRole.RoleMapping.class ) ) {
            return (new org.apache.geronimo.jee.openejb.ObjectFactory()).createEjbRelationshipRoleRoleMapping();
        } else if ( type.equals( Relationships.class ) ) {
            return (new org.apache.geronimo.jee.openejb.ObjectFactory()).createRelationships();
        } else if ( type.equals( Adminobject.class ) ) {
            return (new org.apache.geronimo.jee.connector.ObjectFactory()).createAdminobject();
        } else if ( type.equals( AdminobjectInstance.class ) ) {
            return (new org.apache.geronimo.jee.connector.ObjectFactory()).createAdminobjectInstance();
        } else if ( type.equals( ConfigPropertySetting.class ) ) {
            return (new org.apache.geronimo.jee.connector.ObjectFactory()).createConfigPropertySetting();
        }
        
        return null;
    }

}
