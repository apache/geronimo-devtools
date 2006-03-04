/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.core.internal;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.enterprise.deploy.shared.ModuleType;

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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.jst.j2ee.internal.deployables.J2EEFlexProjDeployable;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.wst.common.componentcore.ArtifactEdit;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;
import org.openejb.xml.ns.openejb.jar.JarPackage;
import org.openejb.xml.ns.openejb.jar.OpenejbJarType;
import org.openejb.xml.ns.openejb.jar.util.JarResourceFactoryImpl;

public class GeronimoUtils {

	public static final String WEB_PLAN_NAME = "geronimo-web.xml";

	public static final String OPENEJB_PLAN_NAME = "openejb-jar.xml";

	public static final String APP_PLAN_NAME = "geronimo-application.xml";

	public static final String CONNECTOR_PLAN_NAME = "geronimo-connector.xml";

	public static String getConfigId(IModule module) {

		if (isWebModule(module)) {
			WebAppType plan = getWebDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(WebPackage.eINSTANCE
							.getWebAppType_ConfigId())) {
				return plan.getConfigId();
			}
		} else if (isEjbJarModule(module)) {
			OpenejbJarType plan = getOpenEjbDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(JarPackage.eINSTANCE
							.getOpenejbJarType_ConfigId())) {
				return plan.getConfigId();
			}
		} else if (isEarModule(module)) {
			ApplicationType plan = getApplicationDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(ApplicationPackage.eINSTANCE
							.getApplicationType_ConfigId())) {
				return plan.getConfigId();
			}
		} else if (isRARModule(module)) {
			ConnectorType plan = getConnectorDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(ConnectorPackage.eINSTANCE
							.getConnectorType_ConfigId())) {
				return plan.getConfigId();
			}
		}

		return getId(module);
	}

	public static boolean isWebModule(IModule module) {
		return "jst.web".equals(module.getModuleType().getId());
	}

	public static boolean isEjbJarModule(IModule module) {
		return "jst.ejb".equals(module.getModuleType().getId());
	}

	public static boolean isEarModule(IModule module) {
		return "jst.ear".equals(module.getModuleType().getId());
	}

	public static boolean isRARModule(IModule module) {
		return "jst.connnector".equals(module.getModuleType().getId());
	}

	public static ModuleType getJSR88ModuleType(IModule module) {
		if (isWebModule(module)) {
			return ModuleType.WAR;
		} else if (isEjbJarModule(module)) {
			return ModuleType.EJB;
		} else if (isEarModule(module)) {
			return ModuleType.EAR;
		} else if (isRARModule(module)) {
			return ModuleType.RAR;
		}
		Trace.trace(Trace.SEVERE, "getJSR88ModuleType = null");
		return null;
	}

	public static String getContextRoot(IModule module) {
		String contextRoot = null;

		WebAppType deploymentPlan = getWebDeploymentPlan(module);
		if (deploymentPlan != null)
			contextRoot = deploymentPlan.getContextRoot();

		if (contextRoot == null) {
			J2EEFlexProjDeployable j2eeModule = (J2EEFlexProjDeployable) module
					.loadAdapter(J2EEFlexProjDeployable.class, null);
			contextRoot = ((IWebModule) j2eeModule).getContextRoot();
		}

		if (contextRoot == null)
			contextRoot = getId(module);

		return contextRoot;
	}

	public static String getId(IModule module) {
		// use the module ID
		String moduleId = module.getId();

		J2EEFlexProjDeployable j2eeModule = (J2EEFlexProjDeployable) module
				.loadAdapter(J2EEFlexProjDeployable.class, null);
		if (j2eeModule != null) {
			// j2eeFlex
			ArtifactEdit edit = null;
			try {
				edit = ArtifactEdit.getArtifactEditForRead(j2eeModule
						.getProject());
				XMIResource res = (XMIResource) edit.getContentModelRoot()
						.eResource();
				moduleId = res.getID(edit.getContentModelRoot());
			} finally {
				if (edit != null)
					edit.dispose();
			}
		}

		if (moduleId != null && moduleId.length() > 0)
			return moduleId;

		// ...but if there is no defined module ID, pick the best alternative

		IPath moduleLocation = new Path(j2eeModule.getURI(module));
		if (moduleLocation != null) {
			moduleId = moduleLocation.removeFileExtension().lastSegment();
		}

		if (j2eeModule instanceof IWebModule) {
			// A better choice is to use the context root
			// For wars most appservers use the module name
			// as the context root
			String contextRoot = ((IWebModule) j2eeModule).getContextRoot();
			if (contextRoot.charAt(0) == '/')
				moduleId = contextRoot.substring(1);
		}

		return moduleId;
	}

	public static ApplicationType getApplicationDeploymentPlan(
			IVirtualComponent comp) {
		IFile dpPlan = getApplicationDeploymentPlanFile(comp);
		return getApplicationDeploymentPlan(dpPlan);
	}

	public static WebAppType getWebDeploymentPlan(IVirtualComponent comp) {
		IFile dpPlan = getWebDeploymentPlanFile(comp);
		return getWebDeploymentPlan(dpPlan);
	}

	public static OpenejbJarType getOpenEjbDeploymentPlan(IVirtualComponent comp) {
		IFile dpPlan = getOpenEjbDeploymentPlanFile(comp);
		return getOpenEjbDeploymentPlan(dpPlan);
	}

	public static ConnectorType getConnectorDeploymentPlan(
			IVirtualComponent comp) {
		IFile dpPlan = getConnectorDeploymentPlanFile(comp);
		return getConnectorDeploymentPlan(dpPlan);
	}

	public static ApplicationType getApplicationDeploymentPlan(IFile file) {
		if (file.getName().equals(APP_PLAN_NAME) && file.exists()) {
			ResourceSet resourceSet = new ResourceSetImpl();
			registerAppFactoryAndPackage(resourceSet);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((org.apache.geronimo.xml.ns.j2ee.application.DocumentRoot) resource
						.getContents().get(0)).getApplication();
			}
		}
		return null;
	}

	public static WebAppType getWebDeploymentPlan(IFile file) {
		if (file.getName().equals(WEB_PLAN_NAME) && file.exists()) {
			ResourceSet resourceSet = new ResourceSetImpl();
			registerWebFactoryAndPackage(resourceSet);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((DocumentRoot) resource.getContents().get(0))
						.getWebApp();
			}
		}
		return null;
	}

	public static OpenejbJarType getOpenEjbDeploymentPlan(IFile file) {
		if (file.getName().equals(OPENEJB_PLAN_NAME) && file.exists()) {
			ResourceSet resourceSet = new ResourceSetImpl();
			registerEjbFactoryAndPackage(resourceSet);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((org.openejb.xml.ns.openejb.jar.DocumentRoot) resource
						.getContents().get(0)).getOpenejbJar();
			}
		}
		return null;
	}

	public static ConnectorType getConnectorDeploymentPlan(IFile file) {
		if (file.getName().equals(CONNECTOR_PLAN_NAME) && file.exists()) {
			ResourceSet resourceSet = new ResourceSetImpl();
			registerConnectorFactoryAndPackage(resourceSet);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((org.apache.geronimo.xml.ns.j2ee.connector.DocumentRoot) resource
						.getContents().get(0)).getConnector();
			}
		}
		return null;
	}

	public static IFile getWebDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
				.getProjectRelativePath().append("WEB-INF").append(
						WEB_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IFile getOpenEjbDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
				.getProjectRelativePath().append("META-INF").append(
						OPENEJB_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IFile getApplicationDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
				.getProjectRelativePath().append("META-INF").append(
						APP_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IFile getConnectorDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
				.getProjectRelativePath().append("META-INF").append(
						CONNECTOR_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static WebAppType getWebDeploymentPlan(IModule module) {
		return getWebDeploymentPlan(getVirtualComponent(module));
	}

	public static ApplicationType getApplicationDeploymentPlan(IModule module) {
		return getApplicationDeploymentPlan(getVirtualComponent(module));
	}

	public static OpenejbJarType getOpenEjbDeploymentPlan(IModule module) {
		return getOpenEjbDeploymentPlan(getVirtualComponent(module));
	}

	public static ConnectorType getConnectorDeploymentPlan(IModule module) {
		return getConnectorDeploymentPlan(getVirtualComponent(module));
	}

	private static IVirtualComponent getVirtualComponent(IModule module) {
		return ComponentCore.createComponent(module.getProject());
	}

	private static Resource load(IFile dpFile, ResourceSet resourceSet) {
		try {

			URI uri = URI.createPlatformResourceURI(dpFile.getFullPath()
					.toString());

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

	/**
	 * Register the appropriate resource factory to handle all file extentions.
	 * Register the package to ensure it is available during loading.
	 * 
	 * @param resourceSet
	 */
	public static void registerWebFactoryAndPackage(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new WebResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(WebPackage.eNS_URI,
				WebPackage.eINSTANCE);
	}

	/**
	 * Register the appropriate resource factory to handle all file extentions.
	 * Register the package to ensure it is available during loading.
	 * 
	 * @param resourceSet
	 */
	public static void registerEjbFactoryAndPackage(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new JarResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(JarPackage.eNS_URI,
				JarPackage.eINSTANCE);
	}

	/**
	 * Register the appropriate resource factory to handle all file extentions.
	 * Register the package to ensure it is available during loading.
	 * 
	 * @param resourceSet
	 */
	public static void registerAppFactoryAndPackage(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new ApplicationResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ApplicationPackage.eNS_URI,
				ApplicationPackage.eINSTANCE);
	}

	/**
	 * Register the appropriate resource factory to handle all file extentions.
	 * Register the package to ensure it is available during loading.
	 * 
	 * @param resourceSet
	 */
	public static void registerConnectorFactoryAndPackage(
			ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new ConnectorResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ConnectorPackage.eNS_URI,
				ConnectorPackage.eINSTANCE);
	}

}