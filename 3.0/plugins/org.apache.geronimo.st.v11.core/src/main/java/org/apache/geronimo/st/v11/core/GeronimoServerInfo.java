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
package org.apache.geronimo.st.v11.core;

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
import org.apache.geronimo.j2ee.deployment.DependencyType;
import org.apache.geronimo.j2ee.naming.PatternType;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.InternalKernelException;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.NoSuchAttributeException;
import org.apache.geronimo.kernel.NoSuchOperationException;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.apache.geronimo.system.jmx.KernelDelegate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServerInfo {
    static final long serialVersionUID = 1L;

    private ArrayList<Kernel> kernels;

    private ArrayList<PatternType> deployedEJBs;
    private ArrayList<String> securityRealms;
    private ArrayList<PatternType> jmsConnectionFactories;
    private ArrayList<PatternType> jmsDestinations;
    private ArrayList<PatternType> jdbcConnectionPools;
    private ArrayList<PatternType> javaMailResources;
    private ArrayList<org.apache.geronimo.j2ee.deployment.PatternType> credentialStores;
    private ArrayList<DependencyType> commonLibs;

    // singleton class
    private GeronimoServerInfo() {
    }

    private static GeronimoServerInfo instance = new GeronimoServerInfo();

    public static GeronimoServerInfo getInstance() {
        return instance;
    }

    public ArrayList<PatternType> getDeployedEjbs() {
        return deployedEJBs;
    }

    public ArrayList<String> getSecurityRealms() {
        return securityRealms;
    }

    public ArrayList<PatternType> getJmsConnectionFactories() {
        return jmsConnectionFactories;
    }

    public ArrayList<PatternType> getJmsDestinations() {
        return jmsDestinations;
    }

    public ArrayList<PatternType> getJdbcConnectionPools() {
        return jdbcConnectionPools;
    }

    public ArrayList<PatternType> getJavaMailSessions() {
        return javaMailResources;
    }

    public ArrayList<org.apache.geronimo.j2ee.deployment.PatternType> getDeployedCredentialStores() {
        return credentialStores;
    }

    public ArrayList<DependencyType> getCommonLibs() {
        return commonLibs;
    }

    public void updateInfo() {
        Job job = new Job("Getting Dynamic Information from Server"){
            protected IStatus run(IProgressMonitor arg0) {
                updateKernels();
                updateDeployedEJBs();
                updateSecurityRealms();
                updateJmsConnectionFactories();
                updateJmsDestinations();
                updateJdbcConnectionPools();
                updateJavaMailSessions();
                updateDeployedCredentialStores();
                updateCommonLibs();
                return Status.OK_STATUS;
            }
        };
        job.setPriority(Job.SHORT);
        job.schedule();
    }

    private void updateKernels() {
        kernels = new ArrayList<Kernel>();
        IServer[] servers = ServerCore.getServers();
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getServerState() == IServer.STATE_STARTED) {
                try {
                    GeronimoServerBehaviourDelegate delegate = (GeronimoServerBehaviourDelegate) servers[i]
                            .getAdapter(GeronimoServerBehaviourDelegate.class);
                    if (delegate != null) {
                        MBeanServerConnection connection = delegate
                                .getServerConnection();
                        if (connection != null) {
                            kernels.add(new KernelDelegate(connection));
                        }
                    }
                } catch (SecurityException e) {
                } catch (Exception e) {
                    Trace.trace(Trace.WARNING, "Kernel connection failed. "
                            + e.getMessage());
                }
            }
        }
    }

    private void updateDeployedEJBs() {
        deployedEJBs = new ArrayList<PatternType>();
        ArrayList<PatternType> statelessSessionBeans = getByType("StatelessSessionBean");
        ArrayList<PatternType> statefulSessionBeans = getByType("StatefulSessionBean");
        ArrayList<PatternType> messageDrivenBeans = getByType("MessageDrivenBean");
        ArrayList<PatternType> entityBeans = getByType("EntityBean");
        deployedEJBs.addAll(statelessSessionBeans);
        deployedEJBs.addAll(statefulSessionBeans);
        deployedEJBs.addAll(messageDrivenBeans);
        deployedEJBs.addAll(entityBeans);
    }

    private void updateSecurityRealms() {
        securityRealms = new ArrayList<String>();
        Map map = Collections.singletonMap("j2eeType", "SecurityRealm");
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
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
    }

    private void updateJmsConnectionFactories() {
        String[] requiredInterfaces = new String[] {
                "javax.jms.ConnectionFactory",
                "javax.jms.QueueConnectionFactory",
                "javax.jms.TopicConnectionFactory", };
        String attribute = "implementedInterfaces";
        jmsConnectionFactories = getByTypeAttributeValues(
                "JCAManagedConnectionFactory", attribute, requiredInterfaces);
    }

    private void updateJmsDestinations() {
        String[] requiredInterfaces = new String[] { "javax.jms.Queue",
                "javax.jms.Topic" };
        String attribute = "adminObjectInterface";
        jmsDestinations = getByTypeAttributeValues("JCAAdminObject", attribute,
                requiredInterfaces);
    }

    private void updateJdbcConnectionPools() {
        String[] acceptedValues = new String[] { "javax.sql.DataSource" };
        String attribute = "connectionFactoryInterface";
        jdbcConnectionPools = getByTypeAttributeValues(
                "JCAManagedConnectionFactory", attribute, acceptedValues);

    }

    private void updateJavaMailSessions() {
        javaMailResources = getByType("JavaMailResource");
    }

    private void updateDeployedCredentialStores() {
        credentialStores = new ArrayList<org.apache.geronimo.j2ee.deployment.PatternType>();
        Map map = Collections.singletonMap("j2eeType", "GBean");
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                try {
                    GBeanInfo info = kernels.get(i).getGBeanInfo(abstractName);
                    GAttributeInfo attribInfo = info
                            .getAttribute("credentialStore");
                    if (attribInfo != null) {
                        Artifact artifact = abstractName.getArtifact();
                        Object name = abstractName.getName().get("name");
                        org.apache.geronimo.j2ee.deployment.PatternType pattern = new org.apache.geronimo.j2ee.deployment.PatternType();
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
    }

    private void updateCommonLibs() {
        List<Object> artifacts = null;
        commonLibs = new ArrayList<DependencyType>();
        Map map = Collections.singletonMap("j2eeType", "Repository");
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
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
                    Trace.trace(Trace.WARNING, "GBean Not Found. "
                            + e.getMessage());
                } catch (NoSuchOperationException e) {
                    Trace.trace(Trace.WARNING, "The operation cant invoked. "
                            + e.getMessage());
                } catch (InternalKernelException e) {
                    throw e;
                } catch (Exception e) {
                    Trace.trace(Trace.WARNING, "Kernel connection failed.  "
                            + e.getMessage());
                }

            }
        }
        if (artifacts != null) {
            for (int i = 0; i < artifacts.size(); i++) {
                DependencyType dependency = new DependencyType();
                dependency.setArtifactId(((Artifact) artifacts.get(i))
                        .getArtifactId());
                dependency.setGroupId(((Artifact) artifacts.get(i))
                        .getGroupId());
                dependency.setVersion(((Artifact) artifacts.get(i))
                        .getVersion().toString());
                dependency.setType(((Artifact) artifacts.get(i)).getType());
                if (!commonLibs.contains(dependency)) {
                    commonLibs.add(dependency);
                }
            }
        }
    }

    private ArrayList<PatternType> getByTypeAttributeValues(String type,
            String attribute, String[] acceptedValues) {
        ArrayList<PatternType> result = new ArrayList<PatternType>();
        Map map = Collections.singletonMap("j2eeType", type);
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                try {
                    Object value = kernels.get(i).getAttribute(abstractName,
                            attribute);
                    if (value != null) {
                        if (value instanceof String[]) {
                            List<String> interfaces = Arrays
                                    .asList((String[]) value);
                            for (int j = 0; j < acceptedValues.length; j++) {
                                if (interfaces.contains(acceptedValues[j])) {
                                	PatternType pattern = new PatternType();
                                    Artifact artifact = abstractName
                                            .getArtifact();
                                    pattern.setArtifactId(artifact
                                            .getArtifactId());
                                    pattern.setGroupId(artifact.getGroupId());
                                    pattern.setVersion(artifact.getVersion()
                                            .toString());
                                    pattern.setName((String) abstractName
                                            .getName().get("name"));
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
                                	PatternType pattern = new PatternType();
                                    Artifact artifact = abstractName
                                            .getArtifact();
                                    pattern.setArtifactId(artifact
                                            .getArtifactId());
                                    pattern.setGroupId(artifact.getGroupId());
                                    pattern.setVersion(artifact.getVersion()
                                            .toString());
                                    pattern.setName((String) abstractName
                                            .getName().get("name"));
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
                    Trace.trace(Trace.WARNING, "Kernel connection failed. "
                            + e.getMessage());
                }
            }
        }
        return result;
    }

    private ArrayList<PatternType> getByType(String type) {
        ArrayList<PatternType> result = new ArrayList<PatternType>();
        Map map = Collections.singletonMap("j2eeType", type);
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            for (Iterator it = beans.iterator(); it.hasNext();) {
                AbstractName abstractName = (AbstractName) it.next();
                PatternType pattern = new PatternType();
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

    protected void printNamingPatternList(List<PatternType> patternList) {
        for (int i = 0; i < patternList.size(); i++) {
        	PatternType pattern = patternList.get(i);
            System.out.println("ArtifactID:" + pattern.getArtifactId()
                    + " GroupID:" + pattern.getGroupId() + " Module:"
                    + pattern.getModule() + " Version:" + pattern.getVersion()
                    + " Name:" + pattern.getName());
        }
    }

    protected void printDeploymentPatternList(
            List<org.apache.geronimo.j2ee.deployment.PatternType> patternList) {
        for (int i = 0; i < patternList.size(); i++) {
        	org.apache.geronimo.j2ee.deployment.PatternType pattern = patternList
                    .get(i);
            System.out.println("ArtifactID:" + pattern.getArtifactId()
                    + " GroupID:" + pattern.getGroupId() + " Module:"
                    + pattern.getVersion() + " Version:" + pattern.getType()
                    + " Name:" + pattern.getCustomFoo());
        }
    }

    protected void printDependencies(List<DependencyType> dependencyList) {
        for (int i = 0; i < dependencyList.size(); i++) {
        	DependencyType dependency = dependencyList.get(i);
            System.out.println("ArtifactID:" + dependency.getArtifactId()
                    + " GroupID:" + dependency.getGroupId() + " Type:"
                    + dependency.getType() + " Version:"
                    + dependency.getVersion());
        }
    }

    public void printServerInfo() {
        System.out.println("EJB Modules: \n");
        List<PatternType> ejbModules = getDeployedEjbs();
        printNamingPatternList(ejbModules);
        System.out.println("\n\nSecurity Realms: \n");
        List<String> securityRealms = getSecurityRealms();
        System.out.println(securityRealms.toString());
        System.out.println("\n\nJMS Connection Factories: \n");
        List<PatternType> jmsConnectionFactories = getJmsConnectionFactories();
        printNamingPatternList(jmsConnectionFactories);
        System.out.println("\n\nJMS Destinations: \n");
        List<PatternType> jmsDestinations = getJmsDestinations();
        printNamingPatternList(jmsDestinations);
        System.out.println("\n\nJDBC Connection Pools: \n");
        List<PatternType> jdbcConnectionPools = getJdbcConnectionPools();
        printNamingPatternList(jdbcConnectionPools);
        System.out.println("\n\nJava Mail Resources: \n");
        List<PatternType> javaMailResources = getJavaMailSessions();
        printNamingPatternList(javaMailResources);
        System.out.println("\n\nCredential Stores: \n");
        List<org.apache.geronimo.j2ee.deployment.PatternType> credentialStores = getDeployedCredentialStores();
        printDeploymentPatternList(credentialStores);
        System.out.println("\n\nCommon Libs: \n");
        List<DependencyType> dependencies = getCommonLibs();
        printDependencies(dependencies);
    }

}
