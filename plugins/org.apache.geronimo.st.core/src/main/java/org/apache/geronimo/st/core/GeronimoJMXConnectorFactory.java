/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.core;

import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.geronimo.st.core.internal.Trace;

/**
 * Create the JMX connector according to the server type, if the connector is already existed, will return the existing one immediately.
 *
 * @version $Rev$ $Date$
 */
public class GeronimoJMXConnectorFactory {
    private static final String JMX_REMOTE_CREDENTIALS_KEY = "jmx.remote.credentials";
    
    private static final String JAVA_NAMING_FACTORY_INITIAL_KEY = "java.naming.factory.initial";
    private static final String JAVA_NAMING_FACTORY_INITIAL_VALUE = "com.sun.jndi.rmi.registry.RegistryContextFactory";
    
    private static final String JAVA_NAMING_FACTORY_URL_PKGS_KEY = "java.naming.factory.url.pkgs";
    private static final String JAVA_NAMING_FACTORY_URL_PKGS_VALUE = "org.apache.geronimo.naming";
    
    private static final String JAVA_NAMING_PROVIDER_URL_KEY = "java.naming.provider.url";
    
    private static final String JMX_REMOTE_DEFAULT_CLASS_LOADER_KEY = "jmx.remote.default.class.loader";
    
    public GeronimoJMXConnectorFactory() {
    };
    
    /**
     * 
     * @param connectorInfo
     * @param classloader
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static JMXConnector create(JMXConnectorInfo connectorInfo, ClassLoader classloader) throws Exception {
        Trace.trace(Trace.INFO, "Start to get the MBeanServer connector", Activator.traceCore);
        if(connectorInfo == null) throw new IllegalArgumentException("The connector info could not be null");
        
        JMXConnector jmxConnector = null;
        Map env = new HashMap();
        
        env.put(JMX_REMOTE_CREDENTIALS_KEY, new String[] {connectorInfo.userName, connectorInfo.password});
        env.put(JAVA_NAMING_FACTORY_INITIAL_KEY, JAVA_NAMING_FACTORY_INITIAL_VALUE);
        env.put(JAVA_NAMING_FACTORY_URL_PKGS_KEY, JAVA_NAMING_FACTORY_URL_PKGS_VALUE);
        env.put(JAVA_NAMING_PROVIDER_URL_KEY, "rmi://" + connectorInfo.host + ":" + connectorInfo.port);
        env.put(JMX_REMOTE_DEFAULT_CLASS_LOADER_KEY, classloader);
        
        String url = getJMXServiceURL(connectorInfo.host, connectorInfo.port);
        
        JMXServiceURL address = new JMXServiceURL(url);
        try {
            jmxConnector = JMXConnectorFactory.connect(address, env);
        } catch (SecurityException se) {
            //FIXME once GERONIMO-3467 JIRA is fixed
            Thread.sleep(10000);
            jmxConnector = JMXConnectorFactory.connect(address, env);
        }
        
        Trace.trace(Trace.INFO, "Get the MBeanServer connector successfully", Activator.traceCore);
        return jmxConnector;
            
            
    }
    
    public static class JMXConnectorInfo {
        private final String userName;
        private final String password;
        private final String host;
        private final String port;
        
        public JMXConnectorInfo(String userName, String password, String host, String port) {
            super();
            this.userName = userName;
            this.password = password;
            this.host = host;
            this.port = port;
        }
        

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public String getHost() {
            return host;
        }

        public String getPort() {
            return port;
        }
        
    }
    protected static String getJMXServiceURL(String host, String port) {
        return "service:jmx:rmi://" + host + "/jndi/rmi://" + host + ":" + port + "/JMXConnector";
    }
    
    
    
}
