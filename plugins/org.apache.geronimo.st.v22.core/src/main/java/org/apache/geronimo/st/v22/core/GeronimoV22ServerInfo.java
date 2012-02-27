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
package org.apache.geronimo.st.v22.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import org.apache.geronimo.st.v21.core.Activator;
import org.apache.geronimo.st.v21.core.IGeronimoServerInfo;
import org.apache.geronimo.st.v21.core.internal.Trace;
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
public class GeronimoV22ServerInfo implements IGeronimoServerInfo{
    static final long serialVersionUID = 1L;

    private ArrayList<Kernel> kernels;

    private ArrayList<Pattern> deployedEJBs;
    private ArrayList<String> securityRealms;
    private ArrayList<Pattern> jmsConnectionFactories;
    private ArrayList<Pattern> jmsDestinations;
    private ArrayList<Pattern> jdbcConnectionPools;
    private ArrayList<Pattern> javaMailResources;
    private ArrayList<org.apache.geronimo.jee.deployment.Pattern> credentialStores;
    private HashMap<org.apache.geronimo.jee.deployment.Pattern,HashMap<String,ArrayList<String>>> credentialStoreAttributes;
    private ArrayList<Dependency> commonLibs;


    private static GeronimoV22ServerInfo instance = new GeronimoV22ServerInfo();

    public static GeronimoV22ServerInfo getInstance() {
        return instance;
    }

    public ArrayList<Pattern> getDeployedEjbs() {
        return deployedEJBs;
    }

    public ArrayList<String> getSecurityRealms() {
        return securityRealms;
    }

    public ArrayList<Pattern> getJmsConnectionFactories() {
        return jmsConnectionFactories;
    }

    public ArrayList<Pattern> getJmsDestinations() {
        return jmsDestinations;
    }

    public ArrayList<Pattern> getJdbcConnectionPools() {
        return jdbcConnectionPools;
    }

    public ArrayList<Pattern> getJavaMailSessions() {
        return javaMailResources;
    }

    public ArrayList<org.apache.geronimo.jee.deployment.Pattern> getDeployedCredentialStores() {
        return credentialStores;
    }
    
    public HashMap<org.apache.geronimo.jee.deployment.Pattern,HashMap<String,ArrayList<String>>> getDeployedCredentialStoreAttributes() {
        return credentialStoreAttributes;
    }

    public ArrayList<Dependency> getCommonLibs() {
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
                            + e.getMessage(), Activator.logCore);
                }
            }
        }
    }

    private void updateDeployedEJBs() {
        deployedEJBs = new ArrayList<Pattern>();
        ArrayList<Pattern> statelessSessionBeans = getByType("StatelessSessionBean");
        ArrayList<Pattern> statefulSessionBeans = getByType("StatefulSessionBean");
        ArrayList<Pattern> messageDrivenBeans = getByType("MessageDrivenBean");
        ArrayList<Pattern> entityBeans = getByType("EntityBean");
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
            try {
                for (Iterator it = beans.iterator(); it.hasNext();) {
                    AbstractName abstractName = (AbstractName) it.next();
                    String name = (String) abstractName.getName().get("name");
                    if (!securityRealms.contains(name)) {
                        securityRealms.add(name);
                    }
                } 
            }  catch(ClassCastException e) {
                // Just ignore as could be 2.1's AbstractName even in 2.2
                Trace.trace(Trace.INFO, "The class is not match: "  + e.getMessage(), Activator.logCore);
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
        credentialStores = new ArrayList<org.apache.geronimo.jee.deployment.Pattern>();
        credentialStoreAttributes = new HashMap<org.apache.geronimo.jee.deployment.Pattern,HashMap<String,ArrayList<String>>> ();
        Map map = Collections.singletonMap("j2eeType", "GBean");
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
        for (int i = 0; i < kernels.size(); i++) {
        	Kernel kernel = (Kernel)kernels.get(i);
            Set beans = kernel.listGBeans(query);
            try {
                for (Iterator it = beans.iterator(); it.hasNext();) {
                    AbstractName abstractName = (AbstractName) it.next();
                    try {
                        GBeanInfo info = kernel.getGBeanInfo(abstractName);
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

                            // update attributes of credentialStore
                            Map attributeMap = (Map) kernel.getAttribute(abstractName, "credentialStore");
                            if (attributeMap != null) {
                                HashMap<String, ArrayList<String>> realmMap = new HashMap<String, ArrayList<String>>();
                                for (Object obj : attributeMap.keySet()) {
                                    String realmName = (String) obj;
                                    Map idMap = (Map) attributeMap.get(obj);
                                    ArrayList<String> idList = new ArrayList<String>();
                                    idList.addAll(idMap.keySet());

                                    realmMap.put(realmName, idList);
                                }
                                credentialStoreAttributes.put(pattern, realmMap);
                            }
                        }
                    } catch (GBeanNotFoundException e) {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch(ClassCastException e) {
                // Just ignore as could be 2.1's AbstractName even in 2.2
                Trace.trace(Trace.INFO, "The class is not match: "  + e.getMessage(), Activator.logCore);
            }
        }
    }
    

    private void updateCommonLibs() {
        List<Object> artifacts = null;
        commonLibs = new ArrayList<Dependency>();
        Map map = Collections.singletonMap("j2eeType", "Repository");
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            try {
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
                                + e.getMessage(), Activator.logCore);
                    } catch (NoSuchOperationException e) {
                        Trace.trace(Trace.WARNING, "The operation cant invoked. "
                                + e.getMessage(), Activator.logCore);
                    } catch (InternalKernelException e) {
                        throw e;
                    } catch (Exception e) {
                        Trace.trace(Trace.WARNING, "Kernel connection failed.  "
                                + e.getMessage(), Activator.logCore);
                    }
                    
                }
            }  catch(ClassCastException e) {
                // Just ignore as could be 2.1's AbstractName even in 2.2
                Trace.trace(Trace.INFO, "The class is not match: "  + e.getMessage(), Activator.logCore);
            }
        }
        if (artifacts != null) {
            for (int i = 0; i < artifacts.size(); i++) {
                Dependency dependency = new Dependency();
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

    private ArrayList<Pattern> getByTypeAttributeValues(String type,
            String attribute, String[] acceptedValues) {
        ArrayList<Pattern> result = new ArrayList<Pattern>();
        Map map = Collections.singletonMap("j2eeType", type);
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            try {
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
                                        Pattern pattern = new Pattern();
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
                                        Pattern pattern = new Pattern();
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
                                + e.getMessage(), Activator.logCore);
                    }
                }
            } catch(ClassCastException e) {
                // Just ignore as could be 2.1's AbstractName even in 2.2
                Trace.trace(Trace.INFO, "The class is not match: "  + e.getMessage(), Activator.logCore);
            }
        }
        return result;
    }

    private ArrayList<Pattern> getByType(String type) {
        ArrayList<Pattern> result = new ArrayList<Pattern>();
        Map map = Collections.singletonMap("j2eeType", type);
        AbstractNameQuery query = new AbstractNameQuery(null, map,
                Collections.EMPTY_SET);
        for (int i = 0; i < kernels.size(); i++) {
            Set beans = kernels.get(i).listGBeans(query);
            try {
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
            } catch(ClassCastException e) {
                // Just ignore as could be 2.1's AbstractName even in 2.2
                Trace.trace(Trace.INFO, "The class is not match: "  + e.getMessage(), Activator.logCore);
            }
        }
        return result;
    }

    protected void printNamingPatternList(List<Pattern> patternList) {
        for (int i = 0; i < patternList.size(); i++) {
            Pattern pattern = patternList.get(i);
            System.out.println("ArtifactID:" + pattern.getArtifactId()
                    + " GroupID:" + pattern.getGroupId() + " Module:"
                    + pattern.getModule() + " Version:" + pattern.getVersion()
                    + " Name:" + pattern.getName());
        }
    }

    protected void printDeploymentPatternList(
            List<org.apache.geronimo.jee.deployment.Pattern> patternList) {
        for (int i = 0; i < patternList.size(); i++) {
            org.apache.geronimo.jee.deployment.Pattern pattern = patternList
                    .get(i);
            System.out.println("ArtifactID:" + pattern.getArtifactId()
                    + " GroupID:" + pattern.getGroupId() + " Module:"
                    + pattern.getVersion() + " Version:" + pattern.getType()
                    + " Name:" + pattern.getCustomFoo());
        }
    }

    protected void printDependencies(List<Dependency> dependencyList) {
        for (int i = 0; i < dependencyList.size(); i++) {
            Dependency dependency = dependencyList.get(i);
            System.out.println("ArtifactID:" + dependency.getArtifactId()
                    + " GroupID:" + dependency.getGroupId() + " Type:"
                    + dependency.getType() + " Version:"
                    + dependency.getVersion());
        }
    }

    public void printServerInfo() {
        System.out.println("EJB Modules: \n");
        List<Pattern> ejbModules = getDeployedEjbs();
        printNamingPatternList(ejbModules);
        System.out.println("\n\nSecurity Realms: \n");
        List<String> securityRealms = getSecurityRealms();
        System.out.println(securityRealms.toString());
        System.out.println("\n\nJMS Connection Factories: \n");
        List<Pattern> jmsConnectionFactories = getJmsConnectionFactories();
        printNamingPatternList(jmsConnectionFactories);
        System.out.println("\n\nJMS Destinations: \n");
        List<Pattern> jmsDestinations = getJmsDestinations();
        printNamingPatternList(jmsDestinations);
        System.out.println("\n\nJDBC Connection Pools: \n");
        List<Pattern> jdbcConnectionPools = getJdbcConnectionPools();
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
