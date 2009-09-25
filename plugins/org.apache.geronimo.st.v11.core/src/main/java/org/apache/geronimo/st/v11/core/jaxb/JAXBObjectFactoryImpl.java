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

import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.xml.ns.deployment.javabean_1.PropertyType;
import org.apache.geronimo.xml.ns.deployment_1.ArtifactType;
import org.apache.geronimo.xml.ns.deployment_1.AttributeType;
import org.apache.geronimo.xml.ns.deployment_1.ClassFilterType;
import org.apache.geronimo.xml.ns.deployment_1.DependenciesType;
import org.apache.geronimo.xml.ns.deployment_1.DependencyType;
import org.apache.geronimo.xml.ns.deployment_1.EnvironmentType;
import org.apache.geronimo.xml.ns.deployment_1.GbeanType;
import org.apache.geronimo.xml.ns.deployment_1.ReferenceType;
import org.apache.geronimo.xml.ns.j2ee.application_1.ExtModuleType;
import org.apache.geronimo.xml.ns.j2ee.application_1.ModuleType;
import org.apache.geronimo.xml.ns.j2ee.application_1.PathType;
import org.apache.geronimo.xml.ns.j2ee.connector_1.AdminobjectInstanceType;
import org.apache.geronimo.xml.ns.j2ee.connector_1.AdminobjectType;
import org.apache.geronimo.xml.ns.j2ee.connector_1.ConfigPropertySettingType;
import org.apache.geronimo.xml.ns.naming_1.EjbLocalRefType;
import org.apache.geronimo.xml.ns.naming_1.EjbRefType;
import org.apache.geronimo.xml.ns.naming_1.GbeanRefType;
import org.apache.geronimo.xml.ns.naming_1.MessageDestinationType;
import org.apache.geronimo.xml.ns.naming_1.PatternType;
import org.apache.geronimo.xml.ns.naming_1.PortCompletionType;
import org.apache.geronimo.xml.ns.naming_1.PortType;
import org.apache.geronimo.xml.ns.naming_1.ResourceEnvRefType;
import org.apache.geronimo.xml.ns.naming_1.ResourceRefType;
import org.apache.geronimo.xml.ns.naming_1.ServiceCompletionType;
import org.apache.geronimo.xml.ns.naming_1.ServiceRefType;
import org.apache.geronimo.xml.ns.security_1.DescriptionType;
import org.apache.geronimo.xml.ns.security_1.DistinguishedNameType;
import org.apache.geronimo.xml.ns.security_1.LoginDomainPrincipalType;
import org.apache.geronimo.xml.ns.security_1.PrincipalType;
import org.apache.geronimo.xml.ns.security_1.RealmPrincipalType;
import org.apache.geronimo.xml.ns.security_1.RoleMappingsType;
import org.apache.geronimo.xml.ns.security_1.RoleType;
import org.apache.geronimo.xml.ns.security_1.SecurityType;
import org.openejb.xml.ns.openejb_jar_2.EjbRelationType;
import org.openejb.xml.ns.openejb_jar_2.EjbRelationshipRoleType;
import org.openejb.xml.ns.openejb_jar_2.RelationshipsType;


/**
 * @version $Rev: 705268 $ $Date: 2008-10-16 23:54:29 +0800 (Thu, 16 Oct 2008) $
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
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createResourceRefType();
        } else if ( type.equals( ResourceEnvRefType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createResourceEnvRefType();
        } else if ( type.equals( EjbRefType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createEjbRefType();
        } else if ( type.equals( GbeanRefType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createGbeanRefType();
        } else if ( type.equals( MessageDestinationType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createMessageDestinationType();
        } else if ( type.equals( PatternType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createPatternType();
        } else if ( type.equals( PortType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createPortType();
        } else if ( type.equals( PortCompletionType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createPortCompletionType();
        } else if ( type.equals( PropertyType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment.javabean_1.ObjectFactory()).createPropertyType();
        } else if ( type.equals( ServiceCompletionType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createServiceCompletionType();
        } else if ( type.equals( ServiceRefType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createServiceRefType();
        } else if ( type.equals( EjbLocalRefType.class ) ) {
            return (new org.apache.geronimo.xml.ns.naming_1.ObjectFactory()).createEjbLocalRefType();
        } else if ( type.equals( SecurityType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createSecurityType();
        } else if ( type.equals( RoleMappingsType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createRoleMappingsType();
        } else if ( type.equals( DescriptionType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createDescriptionType();
        } else if ( type.equals( RoleType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createRoleType();
        } else if ( type.equals( DistinguishedNameType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createDistinguishedNameType();
        } else if ( type.equals( PrincipalType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createPrincipalType();
        } else if ( type.equals( LoginDomainPrincipalType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createLoginDomainPrincipalType();
        } else if ( type.equals( RealmPrincipalType.class ) ) {
            return (new org.apache.geronimo.xml.ns.security_1.ObjectFactory()).createRealmPrincipalType();
        } else if ( type.equals( GbeanType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createGbeanType();
        } else if ( type.equals( ArtifactType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createArtifactType();
        } else if ( type.equals( ClassFilterType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createClassFilterType();
        } else if ( type.equals( DependenciesType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createDependenciesType();
        } else if ( type.equals( DependencyType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createDependencyType();
        } else if ( type.equals( EnvironmentType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createEnvironmentType();
        } else if ( type.equals( org.apache.geronimo.xml.ns.deployment_1.PatternType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createPatternType();
        } else if ( type.equals( AttributeType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createAttributeType();
        } else if ( type.equals( ReferenceType.class ) ) {
            return (new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory()).createReferenceType();
        } else if ( type.equals( ExtModuleType.class ) ) {
            return (new org.apache.geronimo.xml.ns.j2ee.application_1.ObjectFactory()).createExtModuleType();
        } else if ( type.equals( ModuleType.class ) ) {
            return (new org.apache.geronimo.xml.ns.j2ee.application_1.ObjectFactory()).createModuleType();
        } else if ( type.equals( PathType.class ) ) {
            return (new org.apache.geronimo.xml.ns.j2ee.application_1.ObjectFactory()).createPathType();
        } else if ( type.equals( EjbRelationType.class ) ) {
            return (new org.openejb.xml.ns.openejb_jar_2.ObjectFactory()).createEjbRelationType();
        } else if ( type.equals( EjbRelationshipRoleType.class ) ) {
            return (new org.openejb.xml.ns.openejb_jar_2.ObjectFactory()).createEjbRelationshipRoleType();
        } else if ( type.equals( EjbRelationshipRoleType.RelationshipRoleSource.class ) ) {
            return (new org.openejb.xml.ns.openejb_jar_2.ObjectFactory()).createEjbRelationshipRoleTypeRelationshipRoleSource();
        } else if ( type.equals( EjbRelationshipRoleType.CmrField.class ) ) {
            return (new org.openejb.xml.ns.openejb_jar_2.ObjectFactory()).createEjbRelationshipRoleTypeCmrField();
        } else if ( type.equals( EjbRelationshipRoleType.RoleMapping.CmrFieldMapping.class ) ) {
            return (new org.openejb.xml.ns.openejb_jar_2.ObjectFactory()).createEjbRelationshipRoleTypeRoleMappingCmrFieldMapping();
        } else if ( type.equals( EjbRelationshipRoleType.RoleMapping.class ) ) {
            return (new org.openejb.xml.ns.openejb_jar_2.ObjectFactory()).createEjbRelationshipRoleTypeRoleMapping();
        } else if ( type.equals( RelationshipsType.class ) ) {
            return (new org.openejb.xml.ns.openejb_jar_2.ObjectFactory()).createRelationshipsType();
        } else if ( type.equals( AdminobjectType.class ) ) {
            return (new org.apache.geronimo.xml.ns.j2ee.connector_1.ObjectFactory()).createAdminobjectType();
        } else if ( type.equals( AdminobjectInstanceType.class ) ) {
            return (new org.apache.geronimo.xml.ns.j2ee.connector_1.ObjectFactory()).createAdminobjectInstanceType();
        } else if ( type.equals( ConfigPropertySettingType.class ) ) {
            return (new org.apache.geronimo.xml.ns.j2ee.connector_1.ObjectFactory()).createConfigPropertySettingType();
        }
        
        return null;
    }

}
