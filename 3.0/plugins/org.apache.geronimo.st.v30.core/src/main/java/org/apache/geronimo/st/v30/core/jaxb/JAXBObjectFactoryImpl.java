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
package org.apache.geronimo.st.v30.core.jaxb;

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
import org.apache.geronimo.jee.naming.EjbLocalRef;
import org.apache.geronimo.jee.naming.EjbRef;
import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.Port;
import org.apache.geronimo.jee.naming.PortCompletion;
import org.apache.geronimo.jee.naming.Property;
import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.naming.ServiceCompletion;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.apache.geronimo.jee.openejb.EjbRelation;
import org.apache.geronimo.jee.openejb.EjbRelationshipRole;
import org.apache.geronimo.jee.openejb.Relationships;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.DistinguishedName;
import org.apache.geronimo.jee.security.LoginDomainPrincipal;
import org.apache.geronimo.jee.security.Principal;
import org.apache.geronimo.jee.security.RealmPrincipal;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.RoleMappings;
import org.apache.geronimo.jee.security.Security;
import org.apache.geronimo.osgi.blueprint.Targument;
import org.apache.geronimo.osgi.blueprint.Tbean;
import org.apache.geronimo.osgi.blueprint.Tblueprint;
import org.apache.geronimo.osgi.blueprint.Tcollection;
import org.apache.geronimo.osgi.blueprint.Tdescription;
import org.apache.geronimo.osgi.blueprint.TinlinedBean;
import org.apache.geronimo.osgi.blueprint.TinlinedReference;
import org.apache.geronimo.osgi.blueprint.TinlinedReferenceList;
import org.apache.geronimo.osgi.blueprint.TinlinedService;
import org.apache.geronimo.osgi.blueprint.Tinterfaces;
import org.apache.geronimo.osgi.blueprint.Tkey;
import org.apache.geronimo.osgi.blueprint.Tmap;
import org.apache.geronimo.osgi.blueprint.TmapEntry;
import org.apache.geronimo.osgi.blueprint.Tnull;
import org.apache.geronimo.osgi.blueprint.Tprop;
import org.apache.geronimo.osgi.blueprint.Tref;
import org.apache.geronimo.osgi.blueprint.Treference;
import org.apache.geronimo.osgi.blueprint.TreferenceList;
import org.apache.geronimo.osgi.blueprint.TreferenceListener;
import org.apache.geronimo.osgi.blueprint.Tservice;
import org.apache.geronimo.osgi.blueprint.TserviceProperties;
import org.apache.geronimo.osgi.blueprint.TservicePropertyEntry;
import org.apache.geronimo.osgi.blueprint.TserviceReference;
import org.apache.geronimo.osgi.blueprint.TserviceUse;
import org.apache.geronimo.osgi.blueprint.TtypeConverters;
import org.apache.geronimo.osgi.blueprint.TtypedCollection;
import org.apache.geronimo.osgi.blueprint.Tvalue;

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
    
    public Object createBlueprintElement(Class type) {
        if ( type.equals( Targument.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTargument();
        } else if ( type.equals( Tbean.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTbean();
        } else if ( type.equals( Tblueprint.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTblueprint();
        } else if ( type.equals( Tcollection.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTcollection();
        } else if ( type.equals( Tdescription.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTdescription();
        } else if ( type.equals( TinlinedBean.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTinlinedBean();
        } else if ( type.equals( TinlinedReference.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTinlinedReference();
        } else if ( type.equals( TinlinedReferenceList.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTinlinedReferenceList();
        } else if ( type.equals( TinlinedService.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTinlinedService();
        } else if ( type.equals( Tinterfaces.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTinterfaces();
        } else if ( type.equals( Tkey.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTkey();
        } else if ( type.equals( Tmap.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTmap();
        } else if ( type.equals( TmapEntry.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTmapEntry();
        } else if ( type.equals( Tnull.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTnull();
        } else if ( type.equals( Tprop.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTprop();
        } else if ( type.equals( Tref.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTref();
        } else if ( type.equals( Treference.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTreference();
        } else if ( type.equals( TreferenceList.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTreferenceList();
        } else if ( type.equals( TreferenceListener.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTreferenceListener();
        } else if ( type.equals( Tservice.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTservice();
        } else if ( type.equals( TserviceProperties.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTserviceProperties();
        } else if ( type.equals( TservicePropertyEntry.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTservicePropertyEntry();
        } else if ( type.equals( TserviceReference.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTserviceReference();
        } else if ( type.equals( TtypeConverters.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTtypeConverters();
        } else if ( type.equals( TtypedCollection.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTtypedCollection();
        } else if ( type.equals( Tvalue.class ) ) {
            return (new org.apache.geronimo.osgi.blueprint.ObjectFactory()).createTvalue();
        } 
        
        return null;
    }

}
