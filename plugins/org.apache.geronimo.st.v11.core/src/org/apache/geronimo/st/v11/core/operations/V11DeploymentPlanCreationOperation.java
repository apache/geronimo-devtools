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
package org.apache.geronimo.st.v11.core.operations;

import org.apache.geronimo.st.core.GeronimoSchemaNS;
import org.apache.geronimo.st.core.operations.DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v11.core.GeronimoV11Utils;
import org.apache.geronimo.xml.ns.deployment.ArtifactType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.EnvironmentType;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationFactory;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationPackage;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationType;
import org.apache.geronimo.xml.ns.j2ee.application.util.ApplicationResourceFactoryImpl;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorFactory;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorPackage;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorType;
import org.apache.geronimo.xml.ns.j2ee.connector.util.ConnectorResourceFactoryImpl;
import org.apache.geronimo.xml.ns.j2ee.web.WebAppType;
import org.apache.geronimo.xml.ns.j2ee.web.WebFactory;
import org.apache.geronimo.xml.ns.j2ee.web.WebPackage;
import org.apache.geronimo.xml.ns.j2ee.web.util.WebResourceFactoryImpl;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.openejb.xml.ns.openejb.jar.JarFactory;
import org.openejb.xml.ns.openejb.jar.JarPackage;
import org.openejb.xml.ns.openejb.jar.OpenejbJarType;
import org.openejb.xml.ns.openejb.jar.util.JarResourceFactoryImpl;

public class V11DeploymentPlanCreationOperation extends
		DeploymentPlanCreationOperation {

	public V11DeploymentPlanCreationOperation(IDataModel model) {
		super(model);
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoApplicationDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createGeronimoApplicationDeploymentPlan(IFile dpFile) {
		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV11Utils.register(resourceSet, new ApplicationResourceFactoryImpl(), ApplicationPackage.eINSTANCE, ApplicationPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.apache.geronimo.xml.ns.j2ee.application.DocumentRoot documentRoot = ApplicationFactory.eINSTANCE.createDocumentRoot();
		ApplicationType root = ApplicationFactory.eINSTANCE.createApplicationType();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_APP_NS_1_1);
		map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);

		root.setApplicationName(getComponentName());
		root.setEnvironment(getConfigEnvironment("default", getComponentName(), "1.0", "car"));

		documentRoot.setApplication(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoWebDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createGeronimoWebDeploymentPlan(IFile dpFile) {

		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV11Utils.register(resourceSet, new WebResourceFactoryImpl(), WebPackage.eINSTANCE, WebPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.apache.geronimo.xml.ns.j2ee.web.DocumentRoot documentRoot = WebFactory.eINSTANCE.createDocumentRoot();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_WEB_NS_1_1);
		map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS_1_1);
		map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);

		WebAppType root = WebFactory.eINSTANCE.createWebAppType();

		root.setEnvironment(getConfigEnvironment("default", getComponentName(), "1.0", "car"));
		root.setContextRoot("/" + getComponentName());
		//root.setContextPriorityClassloader(false); //TODO Replace this with inverse-classloading

		documentRoot.setWebApp(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createOpenEjbDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createOpenEjbDeploymentPlan(IFile dpFile) {
		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV11Utils.register(resourceSet, new JarResourceFactoryImpl(), JarPackage.eINSTANCE, JarPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.openejb.xml.ns.openejb.jar.DocumentRoot documentRoot = JarFactory.eINSTANCE.createDocumentRoot();
		OpenejbJarType root = JarFactory.eINSTANCE.createOpenejbJarType();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_OPENEJB_NS_2_1);
		map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS_1_1);
		map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);
		map.put("pkgen", GeronimoSchemaNS.GERONIMO_PKGEN_NS_2_0);

		root.setEnvironment(getConfigEnvironment("default", getComponentName(), "1.0", "car"));
		root.setEnterpriseBeans(JarFactory.eINSTANCE.createEnterpriseBeansType());

		documentRoot.setOpenejbJar(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createConnectorDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createConnectorDeploymentPlan(IFile dpFile) {
		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString());

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV11Utils.register(resourceSet, new ConnectorResourceFactoryImpl(), ConnectorPackage.eINSTANCE, ConnectorPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.apache.geronimo.xml.ns.j2ee.connector.DocumentRoot documentRoot = ConnectorFactory.eINSTANCE.createDocumentRoot();
		ConnectorType root = ConnectorFactory.eINSTANCE.createConnectorType();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_CONNECTOR_NS_1_1);
		map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);

		root.setEnvironment(getConfigEnvironment("default", getComponentName(), "1.0", "car"));
		documentRoot.setConnector(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}
	
	public EnvironmentType getConfigEnvironment(String groupId, String artifactId, String version, String type) {
		ArtifactType artifact = DeploymentFactory.eINSTANCE.createArtifactType();
		artifact.setGroupId(groupId);
		artifact.setArtifactId(artifactId);
		artifact.setVersion(version);
		artifact.setType(type);
		EnvironmentType env = DeploymentFactory.eINSTANCE.createEnvironmentType();
		env.setModuleId(artifact);
		return env;
	}

}
