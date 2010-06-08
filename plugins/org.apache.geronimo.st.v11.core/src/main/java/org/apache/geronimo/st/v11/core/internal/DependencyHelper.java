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

package org.apache.geronimo.st.v11.core.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.application.ApplicationType;
import org.apache.geronimo.j2ee.applicationclient.ApplicationClientType;
import org.apache.geronimo.j2ee.connector.ConnectorType;
import org.apache.geronimo.j2ee.deployment.ArtifactType;
import org.apache.geronimo.j2ee.deployment.DependenciesType;
import org.apache.geronimo.j2ee.deployment.DependencyType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.deployment.ObjectFactory;
import org.apache.geronimo.j2ee.openejb_jar.OpenejbJarType;
import org.apache.geronimo.j2ee.web.WebAppType;
import org.apache.geronimo.st.v11.core.DeploymentUtils;
import org.apache.geronimo.st.v11.core.GeronimoUtils;
import org.apache.geronimo.st.core.jaxb.JAXBUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;

/**
 * <b>DependencyTypeHelper</b> is a helper class with various methods to aid in the discovery of
 * inter-dependencies between modules being deployed from the GEP to the Geronimo server. It
 * performs the following capabilities:
 * <ol>
 *      <li>Discovery of dependencies between modules<p>
 *      <li>Provides the proper publishing ordering of the modules based on the discovered
 *          dependencies<p>
 *      <li><b>TODO:</b> Query the server searching for missing dependencies
 * </ol>
 * 
 * @version $Rev: 817996 $ $Date: 2009-09-23 16:04:12 +0800 (Wed, 23 Sep 2009) $
 */
public class DependencyHelper {

    private DependencyManager dm = new DependencyManager();
    private ObjectFactory deploymentFactory = new ObjectFactory();
    private List inputModules = new ArrayList();
    private List inputDeltaKind = new ArrayList();
    private List reorderedModules = new ArrayList();
    private List reorderedKinds  = new ArrayList();
    private List<JAXBElement> inputJAXBElements = new ArrayList();
    private List<JAXBElement> reorderedJAXBElements = new ArrayList();


    /**
     * Reorder the publish order of the modules based on any discovered dependencies
     * 
     * @param modules   Modules to be published to the Geronimo server
     * @param deltaKind Publish kind constant for each module
     * 
     * @return List of reordered modules and deltaKind (or input if no change)
     */
    public List reorderModules(IServer server, List modules, List deltaKind ) {
        Trace.tracePoint("Entry", "DependencyTypeHelper.reorderModules", modules, deltaKind);

        if (modules.size() == 0) {
            List reorderedLists = new ArrayList(2);
            reorderedLists.add(modules);
            reorderedLists.add(deltaKind);
            Trace.tracePoint("Exit ", "DependencyTypeHelper.reorderModules", reorderedLists);
            return reorderedLists;
        }

        inputModules = modules;
        inputDeltaKind = deltaKind;

        // 
        // Iterate through all the modules and register the dependencies
        // 
        for (int ii=0; ii<modules.size(); ii++) {
            IModule[] module = (IModule[]) modules.get(ii);
            int moduleDeltaKind = ((Integer)deltaKind.get(ii)).intValue();
            if (moduleDeltaKind != ServerBehaviourDelegate.REMOVED) {
            	//GERONIMODEVTOOLS-361
            	for (IModule singleModule:module){
            		EnvironmentType environment = getEnvironment(singleModule);
	                if (environment != null) {
	                    ArtifactType child = environment.getModuleId();
	                    DependenciesType dependencies = environment.getDependencies();
	                    if (dependencies != null) {
	                        List<DependencyType> depList = dependencies.getDependency();
	                        for ( DependencyType dep : depList) {
	                            ArtifactType parent = deploymentFactory.createArtifactType();
	                            parent.setGroupId( dep.getGroupId() );
	                            parent.setArtifactId( dep.getArtifactId() );
	                            parent.setVersion( dep.getVersion() );
	                            parent.setType( dep.getType() );
	                            
	                            StringBuilder configId = new StringBuilder();
	                            if (dep.getGroupId()!=null)
	                            	configId.append(dep.getGroupId());
	                            configId.append("/");
	                            if (dep.getArtifactId()!=null)
	                            	configId.append(dep.getArtifactId());
	                            configId.append("/");
	                            if (dep.getVersion()!=null)
	                            	configId.append(dep.getVersion());
	                            configId.append("/");
	                            if (dep.getType()!=null)
	                            	configId.append(dep.getType());
	                            
	                            if (!DeploymentUtils.isInstalledModule(server,configId.toString()))
	                               	dm.addDependency(child, parent );
	                        }
	                    }
	                }
            	}
            }
        }

        // 
        // Iterate through all the modules again and reorder as necessary
        // 
        for (int ii=0; ii<modules.size(); ii++) {
            IModule[] module = (IModule[]) modules.get(ii);
            int moduleDeltaKind = ((Integer)deltaKind.get(ii)).intValue();
            if (module!=null && !reorderedModules.contains(module)) {
                // Not already moved 
                if (moduleDeltaKind == ServerBehaviourDelegate.REMOVED) {
                    // Move module if going to be removed 
                    reorderedModules.add(module);
                    reorderedKinds.add(moduleDeltaKind);
                }
                else {
                    EnvironmentType environment = getEnvironment(module[0]);
                    if (environment != null) {
                        ArtifactType artifact = environment.getModuleId();
                        if (artifact == null) {
                            // Move if null (nothing can be done)
                            if (!reorderedModules.contains(module)) {
                                reorderedModules.add(module);
                                reorderedKinds.add(moduleDeltaKind);
                            }
                        }
                        else if (dm.getParents(artifact).contains(artifact) ||  
                                 dm.getChildren(artifact).contains(artifact)) {
                            // Move if a tight circular dependency (nothing can be done)
                            if (!reorderedModules.contains(module)) {
                                reorderedModules.add(module);
                                reorderedKinds.add(moduleDeltaKind);
                            }
                        }
                        else if (dm.getParents(artifact).size() == 0) {
                            // Move if no parents (nothing to do)
                            if (!reorderedModules.contains(module)) {
                                reorderedModules.add(module);
                                reorderedKinds.add(moduleDeltaKind);
                            }
                        }
                        else if (dm.getParents(artifact).size() > 0) {
                            // Move parents first
                            processParents(dm.getParents(artifact), artifact);
                            // Move self 
                            if (!reorderedModules.contains(module)) {
                                reorderedModules.add(module);
                                reorderedKinds.add(moduleDeltaKind);
                            }
                        }
                    }else {
                    	//no environment defined, do just as no parents
                        if (!reorderedModules.contains(module)) {
                            reorderedModules.add(module);
                            reorderedKinds.add(moduleDeltaKind);
                        }
                    }
                }
            }
        }

        // 
        // Ensure return lists are exactly the same size as the input lists 
        // 
        assert reorderedModules.size() == modules.size();
        assert reorderedKinds.size() == deltaKind.size();

        // 
        // Return List of lists
        // 
        List reorderedLists = new ArrayList(2);
        reorderedLists.add(reorderedModules);
        reorderedLists.add(reorderedKinds);

        Trace.tracePoint("Exit ", "DependencyTypeHelper.reorderModules", reorderedLists);
        return reorderedLists;
    }


    /**
     * Reorder the list of JAXBElements based on any discovered dependencies
     * 
     * @param jaxbElements
     * 
     * @return List of JAXBElements (or input if no change)
     */
    public List<JAXBElement> reorderJAXBElements( List<JAXBElement> jaxbElements ) {
        Trace.tracePoint("Entry", "DependencyTypeHelper.reorderModules", jaxbElements);

        if (jaxbElements.size() == 0) {
            Trace.tracePoint("Exit ", "DependencyTypeHelper.reorderModules", jaxbElements);
            return jaxbElements;
        }

        inputJAXBElements = jaxbElements;

        // 
        // Iterate through all the JAXBElements and register the dependencies
        // 
        for (JAXBElement jaxbElement : jaxbElements) {
            EnvironmentType environment = getEnvironment(jaxbElement);
            if (environment != null) {
                ArtifactType child = environment.getModuleId();
                if (child != null) {
                    DependenciesType dependencies = environment.getDependencies();
                    if (dependencies != null) {
                        List<DependencyType> depList = dependencies.getDependency();
                        if (depList != null) {
                            for ( DependencyType dep : depList) {
                                ArtifactType parent = deploymentFactory.createArtifactType();
                                parent.setGroupId( dep.getGroupId() );
                                parent.setArtifactId( dep.getArtifactId() );
                                parent.setVersion( dep.getVersion() );
                                parent.setType( dep.getType() );
                                dm.addDependency( child, parent );
                            }
                        }
                    }
                }
            }
        }

        // 
        // Iterate through all the JAXBElements again and reorder as necessary
        // 
        for (JAXBElement jaxbElement : jaxbElements) {
            if (!reorderedJAXBElements.contains(jaxbElement)) {
                // Not already moved
                EnvironmentType environment = getEnvironment(jaxbElement);
                if (environment != null) {
                    ArtifactType artifact = environment.getModuleId();
                    if (artifact == null) {
                        // Move if null (nothing can be done)
                        if (!reorderedJAXBElements.contains(jaxbElement)) {
                            reorderedJAXBElements.add(jaxbElement);
                        }
                    }
                    else if (dm.getParents(artifact).contains(artifact) ||  
                             dm.getChildren(artifact).contains(artifact)) {
                        // Move if a tight circular dependency (nothing can be done)
                        if (!reorderedJAXBElements.contains(jaxbElement)) {
                            reorderedJAXBElements.add(jaxbElement);
                        }
                    }
                    else if (dm.getParents(artifact).size() == 0) {
                        // Move if no parents (nothing to do)
                        if (!reorderedJAXBElements.contains(jaxbElement)) {
                            reorderedJAXBElements.add(jaxbElement);
                        }
                    }
                    else if (dm.getParents(artifact).size() > 0) {
                        // Move parents first
                        processJaxbParents(dm.getParents(artifact), artifact);
                        // Move self 
                        if (!reorderedJAXBElements.contains(jaxbElement)) {
                            reorderedJAXBElements.add(jaxbElement);
                        }
                    }
                }
            }
        }

        // 
        // Ensure return list is exactly the same size as the input list 
        // 
        assert reorderedJAXBElements.size() == jaxbElements.size();

        // 
        // Return List of JAXBElements
        // 
        Trace.tracePoint("Exit ", "DependencyTypeHelper.reorderModules", reorderedJAXBElements);
        return reorderedJAXBElements;
    }


    /**
     *
     */
    public void close() {
        dm.close();
    }


    /*--------------------------------------------------------------------------------------------*\
    |                                                                                              |
    |  Private method(s)                                                                           | 
    |                                                                                              |
    \*--------------------------------------------------------------------------------------------*/

    /**
     * Process the parents for a given artifact. The terminatingArtifactType parameter will be used as
     * the terminating condition to ensure there will not be an infinite loop (i.e., if
     * terminatingArtifactType is encountered again there is a circular dependency).
     * 
     * @param parents
     * @param terminatingArtifactType
     */
    private void processParents(Set parents, ArtifactType terminatingArtifactType) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.processParents", parents, terminatingArtifactType );

        if (parents == null) {
            Trace.tracePoint("Exit ", "DependencyTypeHelper.processParents", null);
            return;
        }
        for (Iterator ii = parents.iterator(); ii.hasNext();) {
            ArtifactType artifact = (ArtifactType)ii.next();
            if (dm.getParents(artifact).size() > 0 && !artifact.equals(terminatingArtifactType) &&
                !dm.getParents(artifact).contains(artifact) && !dm.getChildren(artifact).contains(artifact)) {
                // Keep processing parents (as long as no circular dependencies)
                processParents(dm.getParents(artifact), terminatingArtifactType);
                // Move self 
                IModule[] module = getModule(artifact);
                int moduleDeltaKind = getDeltaKind(artifact);
                if (module!=null && !reorderedModules.contains(module)) {
                    reorderedModules.add(module);
                    reorderedKinds.add(moduleDeltaKind);
                }
            }
            else {
                // Move parent
                IModule[] module = getModule(artifact);
                int moduleDeltaKind = getDeltaKind(artifact);
                if (module!=null && !reorderedModules.contains(module)) {
                    reorderedModules.add(module);
                    reorderedKinds.add(moduleDeltaKind);
                }
            }
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.processParents");
    }


    /**
     * Returns the EnvironmentType for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return EnvironmentType
     */
    private EnvironmentType getEnvironment(IModule module) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getEnvironment", module);

        EnvironmentType environment = null;
        if (GeronimoUtils.isWebModule(module)) {
            if (getWebDeploymentPlan(module) != null) {
                WebAppType plan = getWebDeploymentPlan(module).getValue();
                if (plan != null)
                    environment = plan.getEnvironment();
            }
        }
        else if (GeronimoUtils.isEjbJarModule(module)) {
            if (getOpenEjbDeploymentPlan(module) != null) {
                OpenejbJarType plan = getOpenEjbDeploymentPlan(module).getValue();
                if (plan != null)
                    environment = plan.getEnvironment();
            }
        }
        else if (GeronimoUtils.isEarModule(module)) {
            if (getApplicationTypeDeploymentPlan(module) != null) {
                ApplicationType plan = getApplicationTypeDeploymentPlan(module).getValue();
                if (plan != null)
                    environment = plan.getEnvironment();
            }
        }
        else if (GeronimoUtils.isRARModule(module)) {
            if (getConnectorTypeDeploymentPlan(module) != null) {
                ConnectorType plan = getConnectorTypeDeploymentPlan(module).getValue();
                if (plan != null)
                    environment = plan.getEnvironment();
            }
        }else if (GeronimoUtils.isAppClientModule(module)) {
            if (getAppClientDeploymentPlan(module) != null) {
                ApplicationClientType plan = getAppClientDeploymentPlan(module).getValue();
                if (plan != null)
                    environment = plan.getServerEnvironment();
            }
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getEnvironment", environment);
        return environment;
    }


    /**
     * Return the IModule[] for a given artifact
     * 
     * @param artifact
     * 
     * @return IModule[]
     */
    private IModule[] getModule(ArtifactType artifact) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getModule", artifact);

        for (int ii=0; ii<inputModules.size(); ii++) {
            IModule[] module = (IModule[]) inputModules.get(ii);
            int moduleDeltaKind = ((Integer)inputDeltaKind.get(ii)).intValue();
            EnvironmentType environment = getEnvironment(module[0]);
            if (environment != null) {
                ArtifactType moduleArtifactType = environment.getModuleId();
                if (artifact.equals(moduleArtifactType)) {
                    Trace.tracePoint("Exit ", "DependencyTypeHelper.getModule", module);
                    return module;
                }
            }
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getModule", null);
        return null;
    }


    /**
     * Return the deltaKind array index for a given artifact
     * 
     * @param artifact
     * 
     * @return int
     */
    private int getDeltaKind(ArtifactType artifact) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getDeltaKind", artifact);

        for (int ii=0; ii<inputModules.size(); ii++) {
            IModule[] module = (IModule[]) inputModules.get(ii);
            int moduleDeltaKind = ((Integer)inputDeltaKind.get(ii)).intValue();
            EnvironmentType environment = getEnvironment(module[0]);
            if (environment != null) {
                ArtifactType moduleArtifactType = environment.getModuleId();
                if (artifact.equals(moduleArtifactType)) {
                    Trace.tracePoint("Exit ", "DependencyTypeHelper.getDeltaKind", moduleDeltaKind);
                    return moduleDeltaKind;
                }
            }
        }
        Trace.tracePoint("Exit ", "DependencyTypeHelper.getDeltaKind", 0);
        return 0;
    }


    /**
     * Returns the WebAppType for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return WebAppType
     */
    private JAXBElement<WebAppType> getWebDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getWebDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getWebDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.WEB_PLAN_NAME) && file.exists()) {
            try {
				Trace.tracePoint("Exit ", "DependencyTypeHelper.getWebDeploymentPlan", JAXBUtils.unmarshalFilterDeploymentPlan(file));
				 return JAXBUtils.unmarshalFilterDeploymentPlan(file);
			} catch (Exception e) {
				//ignore it, just indicate error by returning null
			}
           
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getWebDeploymentPlan", null);
        return null;
    }


    /**
     * Returns the OpenEjbJar for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return OpenEjbJar
     */
    private JAXBElement<OpenejbJarType> getOpenEjbDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getOpenEjbDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getOpenEjbDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.OPENEJB_PLAN_NAME) && file.exists()) {
            try {
				Trace.tracePoint("Exit ", "DependencyTypeHelper.getOpenEjbDeploymentPlan", JAXBUtils.unmarshalFilterDeploymentPlan(file));
			} catch (Exception e) {
				//ignore it, just indicate error by returning null
			}
            try {
				return JAXBUtils.unmarshalFilterDeploymentPlan(file);
			} catch (Exception e) {
				//ignore it, just indicate error by returning null
			}
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getOpenEjbDeploymentPlan", null);
        return null;
    }

    /**
     * Returns the ApplicationTypeClient for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return ApplicationTypeClient
     */
    private JAXBElement<ApplicationClientType> getAppClientDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getWebDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getApplicationClientDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.APP_CLIENT_PLAN_NAME) && file.exists()) {
            try {
				Trace.tracePoint("Exit ", "DependencyTypeHelper.getWebDeploymentPlan", JAXBUtils.unmarshalFilterDeploymentPlan(file));
				 return JAXBUtils.unmarshalFilterDeploymentPlan(file);
			} catch (Exception e) {
				//ignore it, just indicate error by returning null
			}
           
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getWebDeploymentPlan", null);
        return null;
    }
    
    /**
     * Returns the ApplicationType for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return ApplicationType
     */
    private JAXBElement<ApplicationType> getApplicationTypeDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getApplicationTypeDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getApplicationDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.APP_PLAN_NAME) && file.exists()) {
            try {
				Trace.tracePoint("Exit ", "DependencyTypeHelper.getApplicationTypeDeploymentPlan", JAXBUtils.unmarshalFilterDeploymentPlan(file));
				  return JAXBUtils.unmarshalFilterDeploymentPlan(file);
			} catch (Exception e) {
				//ignore it, just indicate error by returning null
			}
          
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getApplicationTypeDeploymentPlan", null);
        return null;
    }


    /**
     * Returns the ConnectorType for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return ApplicationType
     */
    private JAXBElement<ConnectorType> getConnectorTypeDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getConnectorTypeDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getConnectorDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.CONNECTOR_PLAN_NAME) && file.exists()) {
            try {
				Trace.tracePoint("Exit ", "DependencyTypeHelper.getConnectorTypeDeploymentPlan", JAXBUtils.unmarshalFilterDeploymentPlan(file));
				 return JAXBUtils.unmarshalFilterDeploymentPlan(file);
			} catch (Exception e) {
				//ignore it, just indicate error by returning null
			}
           
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getConnectorTypeDeploymentPlan", null);
        return null;
    }


    /**
     * Process the parents for a given artifact. The terminatingArtifactType parameter will be used as
     * the terminating condition to ensure there will not be an infinite loop (i.e., if
     * terminatingArtifactType is encountered again there is a circular dependency).
     * 
     * @param parents
     * @param terminatingArtifactType
     */
    private void processJaxbParents(Set parents, ArtifactType terminatingArtifactType) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.processJaxbParents", parents, terminatingArtifactType );

        if (parents == null) {
            Trace.tracePoint("Exit ", "DependencyTypeHelper.processJaxbParents", null);
            return;
        }
        for (Iterator ii = parents.iterator(); ii.hasNext();) {
            ArtifactType artifact = (ArtifactType)ii.next();
            if (dm.getParents(artifact).size() > 0 && !artifact.equals(terminatingArtifactType) &&
                !dm.getParents(artifact).contains(artifact) && !dm.getChildren(artifact).contains(artifact)) {
                // Keep processing parents (as long as no circular dependencies)
                processJaxbParents(dm.getParents(artifact), terminatingArtifactType);
                // Move self 
                JAXBElement jaxbElement = getJaxbElement(artifact);
                if (jaxbElement != null) {
                    if (!reorderedJAXBElements.contains(jaxbElement)) {
                        reorderedJAXBElements.add(jaxbElement);
                    }
                }
            }
            else {
                // Move parent
                JAXBElement jaxbElement = getJaxbElement(artifact);
                if (jaxbElement != null) {
                    if (!reorderedJAXBElements.contains(jaxbElement)) {
                        reorderedJAXBElements.add(jaxbElement);
                    }
                }
            }
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.processJaxbParents");
    }


    /**
     * Returns the EnvironmentType for the given JAXBElement plan
     * 
     * @param jaxbElement JAXBElement plan
     * 
     * @return EnvironmentType
     */
    private EnvironmentType getEnvironment(JAXBElement jaxbElement) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getEnvironment", jaxbElement);

        EnvironmentType environment = null;
        Object plan = jaxbElement.getValue();
        if (plan != null) {
            if (WebAppType.class.isInstance(plan)) {
                environment = ((WebAppType)plan).getEnvironment();
            }
            else if (OpenejbJarType.class.isInstance(plan)) {
                environment = ((OpenejbJarType)plan).getEnvironment();
            }
            else if (ApplicationType.class.isInstance(plan)) {
                environment = ((ApplicationType)plan).getEnvironment();
            }
            else if (ConnectorType.class.isInstance(plan)) {
                environment = ((ConnectorType)plan).getEnvironment();
            }
        }

        Trace.tracePoint("Exit ", "DependencyTypeHelper.getEnvironment", environment);
        return environment;
    }


    /**
     * Return the JAXBElement for a given artifact
     * 
     * @param artifact
     * 
     * @return JAXBElement
     */
    private JAXBElement getJaxbElement(ArtifactType artifact) {
        Trace.tracePoint("Enter", "DependencyTypeHelper.getJaxbElement", artifact);

        for (JAXBElement jaxbElement : inputJAXBElements) {
            EnvironmentType environment = getEnvironment(jaxbElement);
            if (environment != null) {
                ArtifactType jaxbArtifactType = environment.getModuleId();
                if (artifact.equals(jaxbArtifactType)) {
                    Trace.tracePoint("Exit ", "DependencyTypeHelper.getJaxbElement", jaxbElement);
                    return jaxbElement;
                }
            }
        }

        // TODO: Query the server searching for missing dependencies
        Trace.tracePoint("Exit ", "DependencyTypeHelper.getJaxbElement", null);
        return null;
    }
}
