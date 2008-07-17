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
package org.apache.geronimo.st.v21.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.management.MBeanServerConnection;

import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.gbean.AbstractNameQuery;
import org.apache.geronimo.gbean.GAttributeInfo;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.naming.Pattern;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.InternalKernelException;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.NoSuchAttributeException;
import org.apache.geronimo.kernel.NoSuchOperationException;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.v21.core.internal.Trace;
import org.apache.geronimo.system.jmx.KernelDelegate;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServerInfo {
    static final long serialVersionUID = 1L;

    private ArrayList<Kernel> kernels;

    // singleton class
    private GeronimoServerInfo() {
    }

    private static GeronimoServerInfo instance = new GeronimoServerInfo();

    public static GeronimoServerInfo getInstance() {
        return instance;
    }

    public List<Pattern> getDeployedEJBs() {
        ArrayList<Pattern> statelessSessionBeans = getByType("StatelessSessionBean");
        ArrayList<Pattern> statefulSessionBeans = getByType("StatefulSessionBean");
        ArrayList<Pattern> messageDrivenBeans = getByType("MessageDrivenBean");
        ArrayList<Pattern> entityBeans = getByType("EntityBean");
        ArrayList<Pattern> deployedEJBs = new ArrayList<Pattern>();
        deployedEJBs.addAll(statelessSessionBeans);
        deployedEJBs.addAll(statefulSessionBeans);
        deployedEJBs.addAll(messageDrivenBeans);
        deployedEJBs.addAll(entityBeans);
        return deployedEJBs;
    }

    public List<String> getSecurityRealms() {
        ArrayList<String> securityRealms = new ArrayList<String>();
        Map m = null;
        m = Collections.singletonMap("j2eeType", "SecurityRealm");
        AbstractNameQuery query = new AbstractNameQuery(null, m, Collections.EMPTY_SET);
        updateKernels();
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                String name = (String) abstractName.getName().get("name");
                if (!securityRealms.contains(name)) {
                    securityRealms.add(name);
                }
            }
        }
        return securityRealms;
    }

    public List<Pattern> getJMSConnectionFactories() {
        String[] requiredInterfaces = new String[] { "javax.jms.ConnectionFactory",
                "javax.jms.QueueConnectionFactory", "javax.jms.TopicConnectionFactory", };
        String attribute = "implementedInterfaces";
        ArrayList<Pattern> jmsConnectionFactories = getByTypeAttributeValues(
                "JCAManagedConnectionFactory", attribute, requiredInterfaces);
        return jmsConnectionFactories;
    }

    public List<Pattern> getJMSDestinations() {
        String[] requiredInterfaces = new String[] { "javax.jms.Queue", "javax.jms.Topic" };
        String attribute = "adminObjectInterface";
        ArrayList<Pattern> jmsDestinations = getByTypeAttributeValues("JCAAdminObject", attribute,
                requiredInterfaces);
        return jmsDestinations;
    }

    public List<Pattern> getJDBCConnectionPools() {
        String[] acceptedValues = new String[] { "javax.sql.DataSource" };
        String attribute = "connectionFactoryInterface";
        ArrayList<Pattern> jdbcConnectionPools = getByTypeAttributeValues(
                "JCAManagedConnectionFactory", attribute, acceptedValues);
        return jdbcConnectionPools;

    }

    public List<Pattern> getJavaMailSessions() {
        ArrayList<Pattern> javaMailResources = getByType("JavaMailResource");
        return javaMailResources;
    }

    public List<org.apache.geronimo.jee.deployment.Pattern> getDeployedCredentialStores() {
        ArrayList<org.apache.geronimo.jee.deployment.Pattern> credentialStores = new ArrayList<org.apache.geronimo.jee.deployment.Pattern>();
        Map map = Collections.singletonMap("j2eeType", "GBean");
        AbstractNameQuery query = new AbstractNameQuery(null, map, Collections.EMPTY_SET);
        updateKernels();
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                try {
                    GBeanInfo info = kernels.get(i).getGBeanInfo(abstractName);
                    GAttributeInfo attribInfo = info.getAttribute("credentialStore");
                    if (attribInfo != null) {
                        Artifact artifact = abstractName.getArtifact();
                        Object name = abstractName.getName().get("name");
                        org.apache.geronimo.jee.deployment.Pattern pattern = new org.apache.geronimo.jee.deployment.Pattern();
                        pattern.setArtifactId(artifact.getArtifactId());
                        pattern.setGroupId(artifact.getGroupId());
                        pattern.setType(artifact.getType());
                        pattern.setVersion(artifact.getVersion().toString());
                        pattern.setCustomFoo((String) name);
                        if (!credentialStores.contains(pattern)) {
                            credentialStores.add(pattern);
                        }
                    }
                } catch (GBeanNotFoundException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return credentialStores;
    }

    public List<Dependency> getCommonLibs() {
        List<Object> artifacts = null;
        ArrayList<Dependency> dependencies = new ArrayList<Dependency>();
        Map map = Collections.singletonMap("j2eeType", "Repository");
        AbstractNameQuery query = new AbstractNameQuery(null, map, Collections.EMPTY_SET);
        updateKernels();
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                try {
                    GBeanInfo info = kernels.get(i).getGBeanInfo(abstractName);
                    Object value = kernels.get(i).invoke(abstractName, "list");
                    if (value instanceof TreeSet) {
                        artifacts = Arrays.asList(((TreeSet) value).toArray());
                    }
                } catch (GBeanNotFoundException e) {
                    Trace.trace(Trace.WARNING, "GBean Not Found. " + e.getMessage());
                } catch (NoSuchOperationException e) {
                    Trace.trace(Trace.WARNING, "The operation cant invoked. " + e.getMessage());
                } catch (InternalKernelException e) {
                    throw e;
                } catch (Exception e) {
                    Trace.trace(Trace.WARNING, "Kernel connection failed.  " + e.getMessage());
                }

            }
        }
        if (artifacts != null) {
            for (int i = 0; i < artifacts.size(); i++) {
                Dependency dependency = new Dependency();
                dependency.setArtifactId(((Artifact) artifacts.get(i)).getArtifactId());
                dependency.setGroupId(((Artifact) artifacts.get(i)).getGroupId());
                dependency.setVersion(((Artifact) artifacts.get(i)).getVersion().toString());
                dependency.setType(((Artifact) artifacts.get(i)).getType());
                if (!dependencies.contains(dependency)) {
                    dependencies.add(dependency);
                }
            }
        }
        return dependencies;
    }

    protected ArrayList<Pattern> getByTypeAttributeValues(String type, String attribute,
            String[] acceptedValues) {
        ArrayList<Pattern> result = new ArrayList<Pattern>();
        Map map = Collections.singletonMap("j2eeType", type);
        AbstractNameQuery query = new AbstractNameQuery(null, map, Collections.EMPTY_SET);
        updateKernels();
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                try {
                    Object value = kernels.get(i).getAttribute(abstractName, attribute);
                    if (value != null) {
                        if (value instanceof String[]) {
                            List<String> interfaces = Arrays.asList((String[]) value);
                            for (int j = 0; j < acceptedValues.length; j++) {
                                if (interfaces.contains(acceptedValues[j])) {
                                    Pattern pattern = new Pattern();
                                    Artifact artifact = abstractName.getArtifact();
                                    pattern.setArtifactId(artifact.getArtifactId());
                                    pattern.setGroupId(artifact.getGroupId());
                                    pattern.setVersion(artifact.getVersion().toString());
                                    pattern.setName((String) abstractName.getName().get("name"));
                                    if (!result.contains(pattern)) {
                                        result.add(pattern);
                                    }
                                    break;
                                }
                            }
                        }
                        if (value instanceof String) {
                            String interfaces = (String) value;
                            for (int j = 0; j < acceptedValues.length; j++) {
                                if (interfaces.contains(acceptedValues[j])) {
                                    Pattern pattern = new Pattern();
                                    Artifact artifact = abstractName.getArtifact();
                                    pattern.setArtifactId(artifact.getArtifactId());
                                    pattern.setGroupId(artifact.getGroupId());
                                    pattern.setVersion(artifact.getVersion().toString());
                                    pattern.setName((String) abstractName.getName().get("name"));
                                    if (!result.contains(pattern)) {
                                        result.add(pattern);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (GBeanNotFoundException e) {
                } catch (NoSuchAttributeException e) {
                } catch (Exception e) {
                    Trace.trace(Trace.WARNING, "Kernel connection failed. " + e.getMessage());
                }
            }
        }
        return result;
    }

    protected ArrayList<Pattern> getByType(String type) {
        ArrayList<Pattern> result = new ArrayList<Pattern>();
        Map map = Collections.singletonMap("j2eeType", type);
        AbstractNameQuery query = new AbstractNameQuery(null, map, Collections.EMPTY_SET);
        updateKernels();
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                Pattern pattern = new Pattern();
                Artifact artifact = abstractName.getArtifact();
                pattern.setArtifactId(artifact.getArtifactId());
                pattern.setGroupId(artifact.getGroupId());
                pattern.setVersion(artifact.getVersion().toString());
                pattern.setName((String) abstractName.getName().get("name"));
                if (!result.contains(pattern)) {
                    result.add(pattern);
                }
            }
        }
        return result;
    }

    protected void updateKernels() {
        kernels = new ArrayList<Kernel>();
        IServer[] runningServers = ServerCore.getServers();
        for (int i = 0; i < runningServers.length; i++) {
            try {
                GeronimoServerBehaviourDelegate delegate = (GeronimoServerBehaviourDelegate) runningServers[i]
                        .getAdapter(GeronimoServerBehaviourDelegate.class);
                if (delegate != null) {
                    MBeanServerConnection connection = delegate.getServerConnection();
                    if (connection != null) {
                        kernels.add(new KernelDelegate(connection));
                    }
                }
            } catch (SecurityException e) {
            } catch (Exception e) {
                Trace.trace(Trace.WARNING, "Kernel connection failed. " + e.getMessage());
            }
        }
    }

    protected void printNamingPatternList(List<Pattern> patternList) {
        for (int i = 0; i < patternList.size(); i++) {
            Pattern pattern = patternList.get(i);
            System.out.println("ArtifactID:" + pattern.getArtifactId() + " GroupID:"
                    + pattern.getGroupId() + " Module:" + pattern.getModule() + " Version:"
                    + pattern.getVersion() + " Name:" + pattern.getName());
        }
    }

    protected void printDeploymentPatternList(List<org.apache.geronimo.jee.deployment.Pattern> patternList) {
        for (int i = 0; i < patternList.size(); i++) {
            org.apache.geronimo.jee.deployment.Pattern pattern = patternList.get(i);
            System.out.println("ArtifactID:" + pattern.getArtifactId() + " GroupID:"
                    + pattern.getGroupId() + " Module:" + pattern.getVersion() + " Version:"
                    + pattern.getType() + " Name:" + pattern.getCustomFoo());
        }
    }

    protected void printDependencies(List<Dependency> dependencyList) {
        for (int i = 0; i < dependencyList.size(); i++) {
            Dependency dependency = dependencyList.get(i);
            System.out.println("ArtifactID:" + dependency.getArtifactId() + " GroupID:"
                    + dependency.getGroupId() + " Type:" + dependency.getType() + " Version:"
                    + dependency.getVersion());
        }
    }

    public void testGeronimoServerInfo() {
        System.out.println("EJB Modules: \n");
        List<Pattern> ejbModules = getDeployedEJBs();
        printNamingPatternList(ejbModules);
        System.out.println("\n\nSecurity Realms: \n");
        List<String> securityRealms = getSecurityRealms();
        System.out.println(securityRealms.toString());
        System.out.println("\n\nJMS Connection Factories: \n");
        List<Pattern> jmsConnectionFactories = getJMSConnectionFactories();
        printNamingPatternList(jmsConnectionFactories);
        System.out.println("\n\nJMS Destinations: \n");
        List<Pattern> jmsDestinations = getJMSDestinations();
        printNamingPatternList(jmsDestinations);
        System.out.println("\n\nJDBC Connection Pools: \n");
        List<Pattern> jdbcConnectionPools = getJDBCConnectionPools();
        printNamingPatternList(jdbcConnectionPools);
        System.out.println("\n\nJava Mail Resources: \n");
        List<Pattern> javaMailResources = getJavaMailSessions();
        printNamingPatternList(javaMailResources);
        System.out.println("\n\nCredential Stores: \n");
        List<org.apache.geronimo.jee.deployment.Pattern> credentialStores = getDeployedCredentialStores();
        printDeploymentPatternList(credentialStores);
        System.out.println("\n\nCommon Libs: \n");
        List<Dependency> dependencies = getCommonLibs();
        printDependencies(dependencies);
    }

}
