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

package org.apache.geronimo.st.core.internal;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.xml.ns.deployment.ArtifactType;
import org.apache.geronimo.xml.ns.deployment.DependenciesType;
import org.apache.geronimo.xml.ns.deployment.DependencyType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.EnvironmentType;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationPackage;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationType;
import org.apache.geronimo.xml.ns.j2ee.application.util.ApplicationResourceFactoryImpl;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorPackage;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorType;
import org.apache.geronimo.xml.ns.j2ee.connector.util.ConnectorResourceFactoryImpl;
import org.apache.geronimo.xml.ns.j2ee.web.DocumentRoot;
import org.apache.geronimo.xml.ns.j2ee.web.WebAppType;
import org.apache.geronimo.xml.ns.j2ee.web.WebPackage;
import org.apache.geronimo.xml.ns.j2ee.web.util.WebResourceFactoryImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.model.ServerBehaviourDelegate;
import org.openejb.xml.ns.openejb.jar.JarPackage;
import org.openejb.xml.ns.openejb.jar.OpenejbJarType;
import org.openejb.xml.ns.openejb.jar.util.JarResourceFactoryImpl;

/**
 * <b>DependencyHelper</b> is a helper class with various methods to aid in the discovery of
 * inter-dependencies between modules being deployed from the GEP to the Geronimo server. It
 * performs the following capabilities:
 * <ol>
 *      <li>Discovery of dependencies between modules<p>
 *      <li>Provides the proper publishing ordering of the modules based on the discovered
 *          dependencies<p>
 *      <li><b>TODO:</b> Query the server searching for missing dependencies
 * </ol>
 * 
 * @version $Rev$ $Date$
 */
public class DependencyHelper {

    private DependencyManager dm = new DependencyManager();
    private List inputModules = new ArrayList();
    private List inputDeltaKind = new ArrayList();
    private List reorderedModules = new ArrayList();
    private List reorderedKinds  = new ArrayList();


    /**
     * Reorder the publish order of the modules based on any discovered dependencies
     * 
     * @param modules   Modules to be published to the Geronimo server
     * @param deltaKind Publish kind constant for each module
     * 
     * @return List of reordered modules and deltaKind (or input if no change)
     */
    public List reorderModules( List modules, List deltaKind ) {
        Trace.tracePoint("Entry", "DependencyHelper.reorderModules", modules, deltaKind);

        if (modules.size() == 0) {
            List reorderedLists = new ArrayList(2);
            reorderedLists.add(modules);
            reorderedLists.add(deltaKind);
            Trace.tracePoint("Exit ", "DependencyHelper.reorderModules", reorderedLists);
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
                EnvironmentType environment = getEnvironment(module[0]);
                if (environment != null) {
                    ArtifactType child = environment.getModuleId();
                    DependenciesType dependencies = environment.getDependencies();
                    if (dependencies != null) {
                        List<DependencyType> depList = dependencies.getDependency();
                        for ( DependencyType dep : depList) {
                            ArtifactType parent = createArtifactType( dep.getGroupId(), 
                                            						  dep.getArtifactId(), 
                                            						  dep.getVersion(), 
                                            						  dep.getType() );
                            dm.addDependency( child, parent );
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

        Trace.tracePoint("Exit ", "DependencyHelper.reorderModules", reorderedLists);
        return reorderedLists;
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
     * Process the parents for a given artifact. The terminatingArtifact parameter will be used as
     * the terminating condition to ensure there will not be an infinite loop (i.e., if
     * terminatingArtifact is encountered again there is a circular dependency).
     * 
     * @param parents
     * @param terminatingArtifact
     */
    private void processParents(Set parents, ArtifactType terminatingArtifact) {
        Trace.tracePoint("Enter", "DependencyHelper.processParents", parents, terminatingArtifact );

        if (parents == null) {
            Trace.tracePoint("Exit ", "DependencyHelper.processParents", null);
            return;
        }
        for (Iterator ii = parents.iterator(); ii.hasNext();) {
            ArtifactType artifact = (ArtifactType)ii.next();
            if (dm.getParents(artifact).size() > 0 && !artifact.equals(terminatingArtifact) &&
               !dm.getParents(artifact).contains(artifact) && !dm.getChildren(artifact).contains(artifact)) {
                // Keep processing parents (as long as no circular dependencies)
                processParents(dm.getParents(artifact), terminatingArtifact);
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

        Trace.tracePoint("Exit ", "DependencyHelper.processParents");
    }


    /**
     * Returns the Environment for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return Environment
     */
    private EnvironmentType getEnvironment(IModule module) {
        Trace.tracePoint("Enter", "DependencyHelper.getEnvironment", module);

        EnvironmentType environment = null;
        if (GeronimoUtils.isWebModule(module)) {
            WebAppType plan = getWebDeploymentPlan(module);
            if (plan != null)
                environment = plan.getEnvironment();
        }
        else if (GeronimoUtils.isEjbJarModule(module)) {
            OpenejbJarType plan = getOpenEjbDeploymentPlan(module);
            if (plan != null)
                environment = plan.getEnvironment();
        }
        else if (GeronimoUtils.isEarModule(module)) {
            ApplicationType plan = getApplicationDeploymentPlan(module);
            if (plan != null)
                environment = plan.getEnvironment();
        }
        else if (GeronimoUtils.isRARModule(module)) {
            ConnectorType plan = getConnectorDeploymentPlan(module);
            if (plan != null)
                environment = plan.getEnvironment();
        }

        Trace.tracePoint("Exit ", "DependencyHelper.getEnvironment", environment);
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
        Trace.tracePoint("Enter", "DependencyHelper.getModule", artifact);

        for (int ii=0; ii<inputModules.size(); ii++) {
            IModule[] module = (IModule[]) inputModules.get(ii);
            int moduleDeltaKind = ((Integer)inputDeltaKind.get(ii)).intValue();
            EnvironmentType environment = getEnvironment(module[0]);
            if (environment != null) {
                ArtifactType moduleArtifact = environment.getModuleId();
                if (artifact.equals(moduleArtifact)) {
                    Trace.tracePoint("Exit ", "DependencyHelper.getModule", module);
                    return module;
                }
            }
        }

        Trace.tracePoint("Exit ", "DependencyHelper.getModule", null);
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
        Trace.tracePoint("Enter", "DependencyHelper.getDeltaKind", artifact);

        for (int ii=0; ii<inputModules.size(); ii++) {
            IModule[] module = (IModule[]) inputModules.get(ii);
            int moduleDeltaKind = ((Integer)inputDeltaKind.get(ii)).intValue();
            EnvironmentType environment = getEnvironment(module[0]);
            if (environment != null) {
                ArtifactType moduleArtifact = environment.getModuleId();
                if (artifact.equals(moduleArtifact)) {
                    Trace.tracePoint("Exit ", "DependencyHelper.getDeltaKind", moduleDeltaKind);
                    return moduleDeltaKind;
                }
            }
        }
        Trace.tracePoint("Exit ", "DependencyHelper.getDeltaKind", 0);
        return 0;
    }
    
    /**
     * Returns the WebApp for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return WebApp
     */
    private WebAppType getWebDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyHelper.getWebDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getWebDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.WEB_PLAN_NAME) && file.exists()) {
        	ResourceSet resourceSet = new ResourceSetImpl();
			register(resourceSet, new WebResourceFactoryImpl(), WebPackage.eINSTANCE, WebPackage.eNS_URI);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((DocumentRoot) resource.getContents().get(0)).getWebApp();
			}
        }
       
        Trace.tracePoint("Exit ", "DependencyHelper.getWebDeploymentPlan", null);
        return null;
    }


    /**
     * Returns the OpenEjbJar for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return OpenEjbJar
     */
    private OpenejbJarType getOpenEjbDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyHelper.getOpenEjbDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getOpenEjbDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.OPENEJB_PLAN_NAME) && file.exists()) {
        	ResourceSet resourceSet = new ResourceSetImpl();
			register(resourceSet, new JarResourceFactoryImpl(), JarPackage.eINSTANCE, JarPackage.eNS_URI);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((org.openejb.xml.ns.openejb.jar.DocumentRoot) resource.getContents().get(0)).getOpenejbJar();
			}
        }

        Trace.tracePoint("Exit ", "DependencyHelper.getOpenEjbDeploymentPlan", null);
        return null;
    }


    /**
     * Returns the Application for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return Application
     */
    private ApplicationType getApplicationDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyHelper.getApplicationDeploymentPlan", module);

        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getApplicationDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.APP_PLAN_NAME) && file.exists()) {
        	ResourceSet resourceSet = new ResourceSetImpl();
			register(resourceSet, new ApplicationResourceFactoryImpl(), ApplicationPackage.eINSTANCE, ApplicationPackage.eNS_URI);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((org.apache.geronimo.xml.ns.j2ee.application.DocumentRoot) resource.getContents().get(0)).getApplication();
			}
        }
        
        Trace.tracePoint("Exit ", "DependencyHelper.getApplicationDeploymentPlan", null);
        return null;
    }


    /**
     * Returns the Connector for the given IModule
     * 
     * @param module IModule to be published
     * 
     * @return Application
     */
    private ConnectorType getConnectorDeploymentPlan(IModule module) {
        Trace.tracePoint("Enter", "DependencyHelper.getConnectorDeploymentPlan", module);
        
        IVirtualComponent comp = GeronimoUtils.getVirtualComponent(module);
        IFile file = GeronimoUtils.getConnectorDeploymentPlanFile(comp);
        if (file.getName().equals(GeronimoUtils.CONNECTOR_PLAN_NAME) && file.exists()) {
            ResourceSet resourceSet = new ResourceSetImpl();
			register(resourceSet, new ConnectorResourceFactoryImpl(), ConnectorPackage.eINSTANCE, ConnectorPackage.eNS_URI);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				Trace.tracePoint("Exit ", "DependencyHelper.getConnectorDeploymentPlan");
				return ((org.apache.geronimo.xml.ns.j2ee.connector.DocumentRoot) resource.getContents().get(0)).getConnector();
			}
		}

        Trace.tracePoint("Exit ", "DependencyHelper.getConnectorDeploymentPlan", null);
        return null;
    }
    
    
	private static Resource load(IFile dpFile, ResourceSet resourceSet) {
		try {

			URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString(), false);

			Resource resource = resourceSet.createResource(uri);
			if (!resource.isLoaded()) {
				resource.load(null);
			}
			return resource;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private static void register(ResourceSet resourceSet,
		Resource.Factory factory, EPackage pkg, String nsUri) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, factory);
		resourceSet.getPackageRegistry().put(nsUri, pkg);
	}
 
	
	private static ArtifactType createArtifactType(String groupId, String artifactId, String version, String type) {
		ArtifactType artifact = DeploymentFactory.eINSTANCE.createArtifactType();
		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		return artifact;
	}
}