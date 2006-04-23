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
package org.apache.geronimo.st.v1.core;

import org.apache.geronimo.st.core.GeronimoUtils;
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;
import org.openejb.xml.ns.openejb.jar.JarPackage;
import org.openejb.xml.ns.openejb.jar.OpenejbJarType;
import org.openejb.xml.ns.openejb.jar.util.JarResourceFactoryImpl;

public class GeronimoV1Utils extends GeronimoUtils {

	public static String getConfigId(IModule module) {

		if (isWebModule(module)) {
			WebAppType plan = getWebDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(WebPackage.eINSTANCE.getWebAppType_ConfigId())) {
				return plan.getConfigId();
			}
		} else if (isEjbJarModule(module)) {
			OpenejbJarType plan = getOpenEjbDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(JarPackage.eINSTANCE.getOpenejbJarType_ConfigId())) {
				return plan.getConfigId();
			}
		} else if (isEarModule(module)) {
			ApplicationType plan = getApplicationDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(ApplicationPackage.eINSTANCE.getApplicationType_ConfigId())) {
				return plan.getConfigId();
			}
		} else if (isRARModule(module)) {
			ConnectorType plan = getConnectorDeploymentPlan(module);
			if (plan != null
					&& plan.eIsSet(ConnectorPackage.eINSTANCE.getConnectorType_ConfigId())) {
				return plan.getConfigId();
			}
		}

		return getId(module);
	}

	public static String getContextRoot(IModule module) {
		String contextRoot = null;

		WebAppType deploymentPlan = getWebDeploymentPlan(module);
		if (deploymentPlan != null)
			contextRoot = deploymentPlan.getContextRoot();

		if (contextRoot == null)
			contextRoot = GeronimoUtils.getContextRoot(module);
			
		return contextRoot;
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

	public static ApplicationType getApplicationDeploymentPlan(
			IVirtualComponent comp) {
		return getApplicationDeploymentPlan(getApplicationDeploymentPlanFile(comp));
	}

	public static WebAppType getWebDeploymentPlan(IVirtualComponent comp) {
		return getWebDeploymentPlan(getWebDeploymentPlanFile(comp));
	}

	public static OpenejbJarType getOpenEjbDeploymentPlan(IVirtualComponent comp) {
		return getOpenEjbDeploymentPlan(getOpenEjbDeploymentPlanFile(comp));
	}

	public static ConnectorType getConnectorDeploymentPlan(
			IVirtualComponent comp) {
		return getConnectorDeploymentPlan(getConnectorDeploymentPlanFile(comp));
	}

	public static ApplicationType getApplicationDeploymentPlan(IFile file) {
		if (file.getName().equals(APP_PLAN_NAME) && file.exists()) {
			ResourceSet resourceSet = new ResourceSetImpl();
			registerAppFactoryAndPackage(resourceSet);
			Resource resource = load(file, resourceSet);
			if (resource != null) {
				return ((org.apache.geronimo.xml.ns.j2ee.application.DocumentRoot) resource.getContents().get(0)).getApplication();
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
				return ((DocumentRoot) resource.getContents().get(0)).getWebApp();
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
				return ((org.openejb.xml.ns.openejb.jar.DocumentRoot) resource.getContents().get(0)).getOpenejbJar();
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
				return ((org.apache.geronimo.xml.ns.j2ee.connector.DocumentRoot) resource.getContents().get(0)).getConnector();
			}
		}
		return null;
	}

	public static void registerWebFactoryAndPackage(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new WebResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(WebPackage.eNS_URI, WebPackage.eINSTANCE);
	}

	public static void registerEjbFactoryAndPackage(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new JarResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(JarPackage.eNS_URI, JarPackage.eINSTANCE);
	}

	public static void registerAppFactoryAndPackage(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ApplicationResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ApplicationPackage.eNS_URI, ApplicationPackage.eINSTANCE);
	}

	public static void registerConnectorFactoryAndPackage(
			ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ConnectorResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ConnectorPackage.eNS_URI, ConnectorPackage.eINSTANCE);
	}
}
