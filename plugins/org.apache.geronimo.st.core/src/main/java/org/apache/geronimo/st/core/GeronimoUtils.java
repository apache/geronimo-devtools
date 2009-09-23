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
package org.apache.geronimo.st.core;

import javax.enterprise.deploy.shared.ModuleType;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.internal.deployables.J2EEFlexProjDeployable;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.wst.common.componentcore.ArtifactEdit;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoUtils {

	public static final String WEB_PLAN_NAME = "geronimo-web.xml";

	public static final String OPENEJB_PLAN_NAME = "openejb-jar.xml";

	public static final String APP_PLAN_NAME = "geronimo-application.xml";

	public static final String APP_CLIENT_PLAN_NAME = "geronimo-application-client.xml";

	public static final String CONNECTOR_PLAN_NAME = "geronimo-ra.xml";
	
	public static final String SERVICE_PLAN_NAME = "geronimo-service.xml";

	public static boolean isWebModule(IModule module) {
		return "jst.web".equals(module.getModuleType().getId());
	}

	public static boolean isEjbJarModule(IModule module) {
		return "jst.ejb".equals(module.getModuleType().getId());
	}

	public static boolean isEarModule(IModule module) {
		return "jst.ear".equals(module.getModuleType().getId());
	}

	public static boolean isAppClientModule(IModule module) {
		return "jst.appclient".equals(module.getModuleType().getId());
	}

	public static boolean isRARModule(IModule module) {
		return "jst.connector".equals(module.getModuleType().getId());
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
		} else {
			Trace.trace(Trace.SEVERE, "getJSR88ModuleType = null");
			return null;
		}
	}
	
	public static IFile getDeploymentPlanFile(IModule module) {
		IProject project = module.getProject();
		IVirtualComponent comp = ComponentCore.createComponent(project);
		String type = J2EEProjectUtilities.getJ2EEProjectType(project);
		if (IModuleConstants.JST_WEB_MODULE.equals(type)) {
			return getWebDeploymentPlanFile(comp);
		} else if (IModuleConstants.JST_EJB_MODULE.equals(type)) {
			return getOpenEjbDeploymentPlanFile(comp);
		} else if (IModuleConstants.JST_EAR_MODULE.equals(type)) {
			return getApplicationDeploymentPlanFile(comp);
		} else if (IModuleConstants.JST_APPCLIENT_MODULE.equals(type)) {
			return getApplicationClientDeploymentPlanFile(comp);
		} else if (IModuleConstants.JST_CONNECTOR_MODULE.equals(type)) {
			return getConnectorDeploymentPlanFile(comp);
		}
		return null;
	}
	
	public static String getQualifiedConfigID(String groupId, String artifactId, String version, String type) {
		return groupId + "/" + artifactId + "/" + version + "/" + type;
	}

	public static IFile getWebDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder().getProjectRelativePath().append("WEB-INF").append(WEB_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IFile getOpenEjbDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder().getProjectRelativePath().append("META-INF").append(OPENEJB_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IFile getApplicationDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder().getProjectRelativePath().append("META-INF").append(APP_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IFile getApplicationClientDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder().getProjectRelativePath().append("META-INF").append(APP_CLIENT_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IFile getConnectorDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder().getProjectRelativePath().append("META-INF").append(CONNECTOR_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

	public static IVirtualComponent getVirtualComponent(IModule module) {
		return ComponentCore.createComponent(module.getProject());
	}

	public static String getContextRoot(IModule module) throws Exception{
		String contextRoot = null;

		J2EEFlexProjDeployable j2eeModule = (J2EEFlexProjDeployable) module.loadAdapter(J2EEFlexProjDeployable.class, null);
		contextRoot = ((IWebModule) j2eeModule).getContextRoot();

		if (contextRoot == null)
			contextRoot = getId(module);

		return contextRoot;
	}

	public static String getId(IModule module) {
		// use the module ID
		String moduleId = module.getId();

		J2EEFlexProjDeployable j2eeModule = (J2EEFlexProjDeployable) module.loadAdapter(J2EEFlexProjDeployable.class, null);
		if (j2eeModule != null) {
			// j2eeFlex
			ArtifactEdit edit = null;
			try {
				edit = ArtifactEdit.getArtifactEditForRead(j2eeModule.getProject());
				//TODO JAXB Refactoring - Can below two lines be removed without harm?
				//XMIResource res = (XMIResource) edit.getContentModelRoot().eResource();
				//moduleId = res.getID(edit.getContentModelRoot());
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

	public static IFile getServiceDeploymentPlanFile(IVirtualComponent comp) {
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder().getProjectRelativePath().append("META-INF").append(SERVICE_PLAN_NAME);
		return comp.getProject().getFile(deployPlanPath);
	}

/*
//TODO JAXB Refactoring - Can this function be removed without harm?
	public static Resource load(IFile dpFile, ResourceSet resourceSet) {
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
*/	
/*
//TODO JAXB Refactoring - Can this function be removed without harm?
	public static void register(ResourceSet resourceSet,
			Resource.Factory factory, EPackage pkg, String nsUri) {
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, factory);
		resourceSet.getPackageRegistry().put(nsUri, pkg);
	}
*/
}
