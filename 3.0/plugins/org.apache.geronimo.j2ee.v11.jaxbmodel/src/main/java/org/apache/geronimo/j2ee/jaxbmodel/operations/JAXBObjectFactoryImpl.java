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
package org.apache.geronimo.j2ee.jaxbmodel.operations;

import org.apache.geronimo.j2ee.application.ExtModuleType;
import org.apache.geronimo.j2ee.application.ModuleType;
import org.apache.geronimo.j2ee.application.PathType;
import org.apache.geronimo.j2ee.connector.AdminobjectInstanceType;
import org.apache.geronimo.j2ee.connector.AdminobjectType;
import org.apache.geronimo.j2ee.connector.ConfigPropertySettingType;
import org.apache.geronimo.j2ee.deployment.ArtifactType;
import org.apache.geronimo.j2ee.deployment.AttributeType;
import org.apache.geronimo.j2ee.deployment.ClassFilterType;
import org.apache.geronimo.j2ee.deployment.DependenciesType;
import org.apache.geronimo.j2ee.deployment.DependencyType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.deployment.GbeanType;
import org.apache.geronimo.j2ee.deployment.ReferenceType;
import org.apache.geronimo.j2ee.deployment.javabean.PropertyType;
import org.apache.geronimo.j2ee.naming.EjbLocalRefType;
import org.apache.geronimo.j2ee.naming.EjbRefType;
import org.apache.geronimo.j2ee.naming.GbeanRefType;
import org.apache.geronimo.j2ee.naming.MessageDestinationType;
import org.apache.geronimo.j2ee.naming.PatternType;
import org.apache.geronimo.j2ee.naming.PortCompletionType;
import org.apache.geronimo.j2ee.naming.PortType;
import org.apache.geronimo.j2ee.naming.ResourceEnvRefType;
import org.apache.geronimo.j2ee.naming.ResourceRefType;
import org.apache.geronimo.j2ee.naming.ServiceCompletionType;
import org.apache.geronimo.j2ee.naming.ServiceRefType;
import org.apache.geronimo.j2ee.openejb_jar.EjbRelationType;
import org.apache.geronimo.j2ee.openejb_jar.EjbRelationshipRoleType;
import org.apache.geronimo.j2ee.openejb_jar.RelationshipsType;
import org.apache.geronimo.j2ee.security.DescriptionType;
import org.apache.geronimo.j2ee.security.DistinguishedNameType;
import org.apache.geronimo.j2ee.security.LoginDomainPrincipalType;
import org.apache.geronimo.j2ee.security.PrincipalType;
import org.apache.geronimo.j2ee.security.RealmPrincipalType;
import org.apache.geronimo.j2ee.security.RoleMappingsType;
import org.apache.geronimo.j2ee.security.RoleType;
import org.apache.geronimo.j2ee.security.SecurityType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;


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
        if ( type.equals( ResourceRefType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createResourceRefType();
        } else if ( type.equals( ResourceEnvRefType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createResourceEnvRefType();
        } else if ( type.equals( EjbRefType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createEjbRefType();
        } else if ( type.equals( GbeanRefType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createGbeanRefType();
        } else if ( type.equals( MessageDestinationType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createMessageDestinationType();
        } else if ( type.equals( PatternType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createPatternType();
        } else if ( type.equals( PortType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createPortType();
        } else if ( type.equals( PortCompletionType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createPortCompletionType();
        } else if ( type.equals( PropertyType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.javabean.ObjectFactory()).createPropertyType();
        } else if ( type.equals( ServiceCompletionType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createServiceCompletionType();
        } else if ( type.equals( ServiceRefType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createServiceRefType();
        } else if ( type.equals( EjbLocalRefType.class ) ) {
            return (new org.apache.geronimo.j2ee.naming.ObjectFactory()).createEjbLocalRefType();
        } else if ( type.equals( SecurityType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createSecurityType();
        } else if ( type.equals( RoleMappingsType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createRoleMappingsType();
        } else if ( type.equals( DescriptionType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createDescriptionType();
        } else if ( type.equals( RoleType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createRoleType();
        } else if ( type.equals( DistinguishedNameType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createDistinguishedNameType();
        } else if ( type.equals( PrincipalType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createPrincipalType();
        } else if ( type.equals( LoginDomainPrincipalType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createLoginDomainPrincipalType();
        } else if ( type.equals( RealmPrincipalType.class ) ) {
            return (new org.apache.geronimo.j2ee.security.ObjectFactory()).createRealmPrincipalType();
        } else if ( type.equals( GbeanType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createGbeanType();
        } else if ( type.equals( ArtifactType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createArtifactType();
        } else if ( type.equals( ClassFilterType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createClassFilterType();
        } else if ( type.equals( DependenciesType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createDependenciesType();
        } else if ( type.equals( DependencyType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createDependencyType();
        } else if ( type.equals( EnvironmentType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createEnvironmentType();
        } else if ( type.equals( org.apache.geronimo.j2ee.deployment.PatternType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createPatternType();
        } else if ( type.equals( AttributeType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createAttributeType();
        } else if ( type.equals( ReferenceType.class ) ) {
            return (new org.apache.geronimo.j2ee.deployment.ObjectFactory()).createReferenceType();
        } else if ( type.equals( ExtModuleType.class ) ) {
            return (new org.apache.geronimo.j2ee.application.ObjectFactory()).createExtModuleType();
        } else if ( type.equals( ModuleType.class ) ) {
            return (new org.apache.geronimo.j2ee.application.ObjectFactory()).createModuleType();
        } else if ( type.equals( PathType.class ) ) {
            return (new org.apache.geronimo.j2ee.application.ObjectFactory()).createPathType();
        } else if ( type.equals( EjbRelationType.class ) ) {
            return (new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory()).createEjbRelationType();
        } else if ( type.equals( EjbRelationshipRoleType.class ) ) {
            return (new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory()).createEjbRelationshipRoleType();
        } else if ( type.equals( EjbRelationshipRoleType.RelationshipRoleSource.class ) ) {
            return (new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory()).createEjbRelationshipRoleTypeRelationshipRoleSource();
        } else if ( type.equals( EjbRelationshipRoleType.CmrField.class ) ) {
            return (new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory()).createEjbRelationshipRoleTypeCmrField();
        } else if ( type.equals( EjbRelationshipRoleType.RoleMapping.CmrFieldMapping.class ) ) {
            return (new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory()).createEjbRelationshipRoleTypeRoleMappingCmrFieldMapping();
        } else if ( type.equals( EjbRelationshipRoleType.RoleMapping.class ) ) {
            return (new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory()).createEjbRelationshipRoleTypeRoleMapping();
        } else if ( type.equals( RelationshipsType.class ) ) {
            return (new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory()).createRelationshipsType();
        } else if ( type.equals( AdminobjectType.class ) ) {
            return (new org.apache.geronimo.j2ee.connector.ObjectFactory()).createAdminobjectType();
        } else if ( type.equals( AdminobjectInstanceType.class ) ) {
            return (new org.apache.geronimo.j2ee.connector.ObjectFactory()).createAdminobjectInstanceType();
        } else if ( type.equals( ConfigPropertySettingType.class ) ) {
            return (new org.apache.geronimo.j2ee.connector.ObjectFactory()).createConfigPropertySettingType();
        }
        
        return null;
    }

}
