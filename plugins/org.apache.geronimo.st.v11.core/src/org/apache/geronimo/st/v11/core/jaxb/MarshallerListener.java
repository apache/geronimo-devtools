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

import javax.xml.bind.Marshaller;

import org.apache.geronimo.xml.ns.j2ee.web_1.WebAppType;
import org.apache.geronimo.xml.ns.naming_1.GbeanLocatorType;
import org.apache.geronimo.xml.ns.naming_1.PatternType;
import org.apache.geronimo.xml.ns.naming_1.ResourceLocatorType;
import org.apache.geronimo.xml.ns.security_1.SecurityType;
import org.openejb.xml.ns.openejb_jar_2.EjbRelationshipRoleType;
import org.openejb.xml.ns.openejb_jar_2.OpenejbJarType;
import org.openejb.xml.ns.openejb_jar_2.RelationshipsType;

public class MarshallerListener extends Marshaller.Listener{

    @Override
    public void beforeMarshal(Object source) {
        if (source instanceof SecurityType) {
            SecurityType security = (SecurityType)source;
            if (security.getRoleMappings() != null && security.getRoleMappings().getRole().size() == 0) {
                security.setRoleMappings(null);
            } 
        } else if (source instanceof WebAppType) {
            WebAppType webapp = (WebAppType)source;
            GbeanLocatorType gbeanlocator = webapp.getWebContainer();
            if (gbeanlocator != null && isEmpty(gbeanlocator.getGbeanLink()) && isEmpty(gbeanlocator.getPattern())) {
                webapp.setWebContainer(null);
            }
        } else if (source instanceof OpenejbJarType) {
        	OpenejbJarType openejb = (OpenejbJarType)source;
            ResourceLocatorType locator = openejb.getCmpConnectionFactory();
            if (locator != null && isEmpty(locator.getResourceLink()) && isEmpty(locator.getUrl()) && isEmpty(locator.getPattern())) {
                openejb.setCmpConnectionFactory(null);
            }
            RelationshipsType relationships = openejb.getRelationships();
            if (relationships != null && relationships.getEjbRelation().size() == 0) {
                openejb.setRelationships(null);
            }
        }else if (source instanceof EjbRelationshipRoleType) {
        	EjbRelationshipRoleType role = (EjbRelationshipRoleType)source;
            if (role.getRoleMapping() != null && role.getRoleMapping().getCmrFieldMapping().size() == 0) {
                role.setRoleMapping(null);
            }
        }
    }
        
    private boolean isEmpty(PatternType pattern) {
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
