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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.application.ApplicationType;
import org.apache.geronimo.j2ee.connector.ConnectorType;
import org.apache.geronimo.j2ee.deployment.ArtifactType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.openejb_jar.OpenejbJarType;
import org.apache.geronimo.j2ee.web.WebAppType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils;
import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;
import org.apache.geronimo.st.core.Activator;
/**
 * @version $Rev$ $Date$
 */
public class GeronimoV11Utils extends GeronimoUtils {

	public static JAXBElement getDeploymentPlan(IFile file) throws Exception {
		Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV11Utils.getDeploymentPlan", file);

		if (!file.exists()) {
			return null;
		}

		if (file.getName().equals(GeronimoUtils.APP_PLAN_NAME))
			return getApplicationDeploymentPlan(file);
		else if (file.getName().equals(GeronimoUtils.OPENEJB_PLAN_NAME))
			return getOpenEjbDeploymentPlan(file);
		else if (file.getName().equals(GeronimoUtils.WEB_PLAN_NAME))
			return getWebDeploymentPlan(file);
		else if (file.getName().equals(GeronimoUtils.CONNECTOR_PLAN_NAME))
			return getConnectorDeploymentPlan(file);
		else if (file.getName().equals(GeronimoUtils.APP_CLIENT_PLAN_NAME))
			return getApplicationClientDeploymentPlan(file);

		Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV11Utils.getDeploymentPlan", null);
		return null;
	}

	// public static String getConfigId2(IModule module) {
	//		
	// IFile planFile = null;
	// IVirtualComponent comp =
	// ComponentCore.createComponent(module.getProject());
	// if (isWebModule(module)) {
	// planFile = GeronimoUtils.getWebDeploymentPlanFile(comp);
	// } else if (isEjbJarModule(module)) {
	// planFile = GeronimoUtils.getOpenEjbDeploymentPlanFile(comp);
	// } else if (isEarModule(module)) {
	// planFile = GeronimoUtils.getApplicationDeploymentPlanFile(comp);
	// } else if (isRARModule(module)) {
	// planFile = GeronimoUtils.getConnectorDeploymentPlanFile(comp);
	// }
	//		
	// if(planFile != null) {
	// try {
	// XmlObject xmlObject =
	// XmlBeansUtil.parse(planFile.getLocation().toFile());
	// XmlCursor cursor = xmlObject.newCursor();
	// cursor.toFirstChild();
	// xmlObject = cursor.getObject();
	// XmlObject result[] =
	// xmlObject.selectChildren(QNameSet.singleton(EnvironmentDocument.type.getDocumentElementName()));
	// if(result != null && result.length > 0) {
	// org.apache.geronimo.deployment.xbeans.EnvironmentType env =
	// (org.apache.geronimo.deployment.xbeans.EnvironmentType)
	// result[0].changeType(org.apache.geronimo.deployment.xbeans.EnvironmentType.type);
	// org.apache.geronimo.deployment.xbeans.ArtifactType moduleId =
	// env.getModuleId();
	// return getQualifiedConfigID(moduleId);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (XmlException e) {
	// e.printStackTrace();
	// }
	// }
	//		
	// return null;
	// }

	public static String getConfigId(IModule module) throws Exception {

		EnvironmentType environment = null;
		if (isWebModule(module)) {
			JAXBElement<WebAppType> webapptype=getWebDeploymentPlan(module);
			WebAppType plan = webapptype.getValue();
			if (plan != null)
				environment = plan.getEnvironment();
		} else if (isEjbJarModule(module)) {
			OpenejbJarType plan = getOpenEjbDeploymentPlan(module).getValue();
			// if (plan != null)
			// environment = plan.getEnvironment();
		} else if (isEarModule(module)) {
			ApplicationType plan = getApplicationDeploymentPlan(module).getValue();
			if (plan != null)
				environment = plan.getEnvironment();
		} else if (isRARModule(module)) {
			ConnectorType plan = getConnectorDeploymentPlan(module).getValue();
			if (plan != null)
				environment = plan.getEnvironment();
		}
		if (environment != null
	            && environment.getModuleId() != null) {
	            Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV11Utils.getConfigId", getQualifiedConfigID(environment.getModuleId()));
	            return getQualifiedConfigID(environment.getModuleId());
	        }

		return getId(module);
	}

//	public static String getQualifiedConfigID(ArtifactType artifact) {
//		return getQualifiedConfigID(artifact.getGroupId(), artifact
//				.getArtifactId(), artifact.getVersion(), artifact.getType());
//	}

	public static String getQualifiedConfigID(
			ArtifactType artifact) {
		return getQualifiedConfigID(artifact.getGroupId(), artifact
				.getArtifactId(), artifact.getVersion(), artifact.getType());
	}

	public static String getQualifiedConfigID(String groupId,
			String artifactId, String version, String type) {
		return groupId + "/" + artifactId + "/" + version + "/" + type;
	}

	public static String getContextRoot(IModule module) throws Exception {
		String contextRoot = null;

		WebAppType deploymentPlan = getWebDeploymentPlan(module).getValue();
		if (deploymentPlan != null)
			contextRoot = deploymentPlan.getContextRoot();

		if (contextRoot == null)
			contextRoot = GeronimoUtils.getContextRoot(module);

		return contextRoot;
	}

	public static JAXBElement<WebAppType> getWebDeploymentPlan(IModule module)
			throws Exception {
		return getWebDeploymentPlan(getVirtualComponent(module));
	}

	public static JAXBElement<ApplicationType> getApplicationDeploymentPlan(
			IModule module) throws Exception {
		return getApplicationDeploymentPlan(getVirtualComponent(module));
	}

	public static JAXBElement<OpenejbJarType> getOpenEjbDeploymentPlan(
			IModule module) throws Exception {
		return getOpenEjbDeploymentPlan(getVirtualComponent(module));
	}

	public static JAXBElement<ConnectorType> getConnectorDeploymentPlan(
			IModule module) throws Exception {
		return getConnectorDeploymentPlan(getVirtualComponent(module));
	}

	public static JAXBElement getApplicationDeploymentPlan(
			IVirtualComponent comp) throws Exception {
		return getApplicationDeploymentPlan(getApplicationDeploymentPlanFile(comp));
	}

	public static JAXBElement getApplicationClientDeploymentPlan(IFile file)
			throws Exception {
		Trace.tracePoint("ENTRY", Activator.traceCore,
				"GeronimoV11Utils.getApplicationClientDeploymentPlan", file);

		if (file.getName().equals(APP_CLIENT_PLAN_NAME) && file.exists()) {
			return JAXBUtils.unmarshalFilterDeploymentPlan(file);
		}

		Trace.tracePoint("EXIT", Activator.traceCore,
				"GeronimoV11Utils.getApplicationClientDeploymentPlan", null);
		return null;
	}

	public static JAXBElement getWebDeploymentPlan(IVirtualComponent comp)
			throws Exception {
		return getWebDeploymentPlan(getWebDeploymentPlanFile(comp));
	}

	public static JAXBElement getOpenEjbDeploymentPlan(IVirtualComponent comp)
			throws Exception {
		return getOpenEjbDeploymentPlan(getOpenEjbDeploymentPlanFile(comp));
	}

	public static JAXBElement getConnectorDeploymentPlan(IVirtualComponent comp)
			throws Exception {
		return getConnectorDeploymentPlan(getConnectorDeploymentPlanFile(comp));
	}

	public static JAXBElement getApplicationDeploymentPlan(IFile file) throws Exception {
		Trace.tracePoint("ENTRY", Activator.traceCore,
				"GeronimoV11Utils.getApplicationClientDeploymentPlan", file);

		if (file.getName().equals(APP_PLAN_NAME) && file.exists()) {
			return JAXBUtils.unmarshalFilterDeploymentPlan(file);
		}

		Trace.tracePoint("EXIT", Activator.traceCore,
				"GeronimoV11Utils.getApplicationClientDeploymentPlan", null);
		return null;
	}

	public static JAXBElement getWebDeploymentPlan(IFile file) throws Exception {
		Trace
				.tracePoint("ENTRY", Activator.traceCore, "GeronimoV11Utils.getWebDeploymentPlan",
						file);

		if (file.getName().equals(WEB_PLAN_NAME) && file.exists()) {
			return JAXBUtils.unmarshalFilterDeploymentPlan(file);
		}

		Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV11Utils.getWebDeploymentPlan", null);
		return null;
	}

	public static JAXBElement getOpenEjbDeploymentPlan(IFile file)
			throws Exception {
		Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoV11Utils.getOpenEjbDeploymentPlan",
				file);

		if (file.getName().equals(OPENEJB_PLAN_NAME) && file.exists()) {
			return JAXBUtils.unmarshalFilterDeploymentPlan(file);
		}

		Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV11Utils.getOpenEjbDeploymentPlan",
				null);
		return null;
	}

	public static JAXBElement getConnectorDeploymentPlan(IFile file)
			throws Exception {
		Trace.tracePoint("ENTRY", Activator.traceCore,
				"GeronimoV11Utils.getConnectorDeploymentPlan", file);

		if (file.getName().equals(CONNECTOR_PLAN_NAME) && file.exists()) {
			return JAXBUtils.unmarshalFilterDeploymentPlan(file);
		}

		Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoV11Utils.getConnectorDeploymentPlan",
				null);
		return null;
	}

}
