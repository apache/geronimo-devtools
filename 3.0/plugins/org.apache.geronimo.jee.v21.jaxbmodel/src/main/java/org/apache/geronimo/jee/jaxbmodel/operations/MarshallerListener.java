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

import javax.xml.bind.Marshaller;

import org.apache.geronimo.jee.naming.GbeanLocator;
import org.apache.geronimo.jee.naming.Pattern;
import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.ResourceLocator;
import org.apache.geronimo.jee.openejb.EjbRelationshipRole;
import org.apache.geronimo.jee.openejb.OpenejbJar;
import org.apache.geronimo.jee.openejb.Relationships;
import org.apache.geronimo.jee.security.Security;
import org.apache.geronimo.jee.web.WebApp;

public class MarshallerListener extends Marshaller.Listener{

    @Override
    public void beforeMarshal(Object source) {
        if (source instanceof Security) {
            Security security = (Security)source;
            if (security.getRoleMappings() != null && security.getRoleMappings().getRole().size() == 0) {
                security.setRoleMappings(null);
            } 
        } else if (source instanceof WebApp) {
            WebApp webapp = (WebApp)source;
            GbeanLocator gbeanlocator = webapp.getWebContainer();
            if (gbeanlocator != null && isEmpty(gbeanlocator.getGbeanLink()) && isEmpty(gbeanlocator.getPattern())) {
                webapp.setWebContainer(null);
            }
        } else if (source instanceof OpenejbJar) {
            OpenejbJar openejb = (OpenejbJar)source;
            ResourceLocator locator = openejb.getCmpConnectionFactory();
            if (locator != null && isEmpty(locator.getResourceLink()) && isEmpty(locator.getUrl()) && isEmpty(locator.getPattern())) {
                openejb.setCmpConnectionFactory(null);
            }
            Relationships relationships = openejb.getRelationships();
            if (relationships != null && relationships.getEjbRelation().size() == 0) {
                openejb.setRelationships(null);
            }
        } else if (source instanceof PersistenceContextRef) {
            PersistenceContextRef contextRef = (PersistenceContextRef)source;
            if (contextRef.getPattern() != null && isEmpty(contextRef.getPattern())) {
                contextRef.setPattern(null);
            }
        } else if (source instanceof EjbRelationshipRole) {
            EjbRelationshipRole role = (EjbRelationshipRole)source;
            if (role.getRoleMapping() != null && role.getRoleMapping().getCmrFieldMapping().size() == 0) {
                role.setRoleMapping(null);
            }
        }
    }
        
    private boolean isEmpty(Pattern pattern) {
        if ( pattern == null ) {
            return true;
        }
        if ( isEmpty(pattern.getGroupId()) && isEmpty(pattern.getArtifactId()) &&
                isEmpty(pattern.getModule()) && isEmpty(pattern.getName()) &&
                isEmpty(pattern.getVersion()) ) {
            return true;
        }
        return false;
    }
    
    private boolean isEmpty(String value) {
        
        return (value == null || value.trim().equals(""));
    }

}
