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

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.application.ApplicationType;
import org.apache.geronimo.j2ee.applicationclient.ApplicationClientType;
import org.apache.geronimo.j2ee.connector.ConnectorType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.openejb_jar.OpenejbJarType;
import org.apache.geronimo.j2ee.openejb_jar.RelationshipsType;
import org.apache.geronimo.j2ee.security.SecurityType;
import org.apache.geronimo.j2ee.web.WebAppType;

/**
 * @version $Rev$ $Date$
 */
public class JAXBModelUtils {
	
    
    public static SecurityType getSecurity (JAXBElement element) {
        Object plan = element;
        if (WebAppType.class.isInstance (plan)) {
            if (((WebAppType)plan).getSecurity() == null) {
                setSecurity (element, new org.apache.geronimo.j2ee.security.ObjectFactory().createSecurityType());
            }
            SecurityType security = (SecurityType)((WebAppType)plan).getSecurity();
            if (security.getRoleMappings() == null) {
                security.setRoleMappings (new org.apache.geronimo.j2ee.security.ObjectFactory().createRoleMappingsType());
            }
            return security;
        } else if (ApplicationType.class.isInstance (plan)) {
            if (((ApplicationType)plan).getSecurity() == null) {
                setSecurity (element, new org.apache.geronimo.j2ee.security.ObjectFactory().createSecurityType());
            }
            SecurityType security = (SecurityType)((ApplicationType)plan).getSecurity();
            if (security.getRoleMappings() == null) {
                security.setRoleMappings (new org.apache.geronimo.j2ee.security.ObjectFactory().createRoleMappingsType());
            }
            return security;
        } else if (OpenejbJarType.class.isInstance (plan)) {
            if (((OpenejbJarType)plan).getSecurity() == null) {
                setSecurity (element, new org.apache.geronimo.j2ee.security.ObjectFactory().createSecurityType());
            }
            SecurityType security = (SecurityType)((OpenejbJarType)plan).getSecurity();
            if (security.getRoleMappings() == null) {
                security.setRoleMappings (new org.apache.geronimo.j2ee.security.ObjectFactory().createRoleMappingsType());
            }
            return security;
        }
        return null;
    }
    
    public static void setSecurity (JAXBElement element, SecurityType security) {
        Object plan = element;
        if (WebAppType.class.isInstance (plan)) {
            ((WebAppType)plan).setSecurity((new org.apache.geronimo.j2ee.security.ObjectFactory()).createSecurity( security ).getValue() );
        } else if (ApplicationType.class.isInstance(plan)) {
            ((ApplicationType)plan).setSecurity((new org.apache.geronimo.j2ee.security.ObjectFactory()).createSecurity( security ).getValue() );
        } else if (OpenejbJarType.class.isInstance(plan)) {
            ((OpenejbJarType)plan).setSecurity((new org.apache.geronimo.j2ee.security.ObjectFactory()).createSecurity( security ).getValue() );
        }
    }

    public static EnvironmentType getEnvironment(JAXBElement element) {
        return getEnvironment (element, true);
    }

    public static EnvironmentType getEnvironment(JAXBElement element, boolean serverEnvironment) {
        Object plan = element;
        if (serverEnvironment == true) {
            if (WebAppType.class.isInstance (plan)) {
                return ((WebAppType)plan).getEnvironment();
            } else if (ApplicationType.class.isInstance (plan)) {
                return ((ApplicationType)plan).getEnvironment();
            } else if (OpenejbJarType.class.isInstance (plan)) {
                return ((OpenejbJarType)plan).getEnvironment();
            } else if (ConnectorType.class.isInstance (plan)) {
                return ((ConnectorType)plan).getEnvironment();
            } else if (ApplicationClientType.class.isInstance (plan)) {
                return ((ApplicationClientType)plan).getServerEnvironment();
            }
        } else {
            if (ApplicationClientType.class.isInstance (plan)) {
                return ((ApplicationClientType)plan).getClientEnvironment();
            }
        }
        return null;
    }

    public static void setEnvironment (JAXBElement element, EnvironmentType environment) {
        setEnvironment (element, environment, true);
    }

    public static void setEnvironment (JAXBElement element, EnvironmentType environment, boolean serverEnvironment) {
        Object plan = element;
        if (serverEnvironment == true) {
            if (WebAppType.class.isInstance (plan)) {
                ((WebAppType)plan).setEnvironment (environment);
            } else if (ApplicationType.class.isInstance (plan)) {
                ((ApplicationType)plan).setEnvironment (environment);
            } else if (OpenejbJarType.class.isInstance (plan)) {
                ((OpenejbJarType)plan).setEnvironment (environment);
            } else if (ConnectorType.class.isInstance (plan)) {
                ((ConnectorType)plan).setEnvironment (environment);
            } else if (ApplicationClientType.class.isInstance (plan)) {
                ((ApplicationClientType)plan).setServerEnvironment (environment);
            }
        } else {
            if (ApplicationClientType.class.isInstance (plan)) {
                ((ApplicationClientType)plan).setClientEnvironment (environment);
            }
        }
    }

    public static List getGbeans (JAXBElement element) {
        Object plan = element;
        if (WebAppType.class.isInstance (plan)) {
            return ((WebAppType)plan).getGbean();
        } else if (ApplicationType.class.isInstance (plan)) {
            return ((ApplicationType)plan).getGbean();
        } else if (OpenejbJarType.class.isInstance (plan)) {
            return ((OpenejbJarType)plan).getGbean();
        } else if (ConnectorType.class.isInstance (plan)) {
            return ((ConnectorType)plan).getGbean();
        } else if (ApplicationClientType.class.isInstance (plan)) {
            return ((ApplicationClientType)plan).getGbean();
        }
        return null;
    }

    public static List getGbeanRefs (JAXBElement element) {
        Object plan = element;
        if (ApplicationClientType.class.isInstance (plan)) {
            return ((ApplicationClientType)plan).getGbeanRef();
        } else if (WebAppType.class.isInstance (plan)) {
            return ((WebAppType)plan).getGbeanRef();
        }
        return null;
    }

    public static List getServiceRefs (JAXBElement element) {
        Object plan = element;
        if (WebAppType.class.isInstance (plan)) {
            return ((WebAppType)plan).getServiceRef();
        }
        return null;
    }

    public static List getEjbRelationships (JAXBElement element) {
        Object plan = element;
        if (OpenejbJarType.class.isInstance (plan)) {
            return ((OpenejbJarType)plan).getRelationships() == null ? null : ((OpenejbJarType)plan).getRelationships().getEjbRelation();
        }
        return null;
    }
    
    public static void setEjbRelationships (JAXBElement element, RelationshipsType relationships ) {
        Object plan = element;
        if (OpenejbJarType.class.isInstance (plan)) {
            ((OpenejbJarType)plan).setRelationships(relationships);
        }
    }
    
    public static List getMessageDestinations (JAXBElement element) {
        Object plan = element;
        if (WebAppType.class.isInstance (plan)) {
            return ((WebAppType)plan).getMessageDestination();
        } else if (OpenejbJarType.class.isInstance (plan)) {
            return ((OpenejbJarType)plan).getMessageDestination();
        } else if (ApplicationClientType.class.isInstance (plan)) {
            return ((ApplicationClientType)plan).getMessageDestination();
        }
        return null;
    }
}
