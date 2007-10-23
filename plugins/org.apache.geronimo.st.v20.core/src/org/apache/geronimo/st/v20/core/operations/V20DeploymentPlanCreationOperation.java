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
package org.apache.geronimo.st.v20.core.operations;

import org.apache.geronimo.st.core.GeronimoSchemaNS;
import org.apache.geronimo.st.core.operations.DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v20.core.DeploymentPlanInstallConfig;
import org.apache.geronimo.st.v20.core.GeronimoV20Utils;
import org.apache.geronimo.xml.ns.deployment.ArtifactType;
import org.apache.geronimo.xml.ns.deployment.DependenciesType;
import org.apache.geronimo.xml.ns.deployment.DependencyType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.apache.geronimo.xml.ns.deployment.DocumentRoot;
import org.apache.geronimo.xml.ns.deployment.EnvironmentType;
import org.apache.geronimo.xml.ns.deployment.ModuleType;
import org.apache.geronimo.xml.ns.deployment.util.DeploymentResourceFactoryImpl;
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

/**
 * @version $Rev: 509704 $ $Date: 2007-02-20 13:42:24 -0500 (Tue, 20 Feb 2007) $
 */
public class V20DeploymentPlanCreationOperation extends DeploymentPlanCreationOperation {

	DeploymentPlanInstallConfig cfg;

	public V20DeploymentPlanCreationOperation(IDataModel model, Object config) {
		super(model, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoApplicationDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createGeronimoApplicationDeploymentPlan(IFile dpFile) {
		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString(), false);

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV20Utils.register(resourceSet, new ApplicationResourceFactoryImpl(), ApplicationPackage.eINSTANCE, ApplicationPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.apache.geronimo.xml.ns.j2ee.application.DocumentRoot documentRoot = ApplicationFactory.eINSTANCE.createDocumentRoot();
		ApplicationType root = ApplicationFactory.eINSTANCE.createApplicationType();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_APP_NS_1_1);
		map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);

		root.setApplicationName(getProject().getName());
		root.setEnvironment(getConfigEnvironment());

		documentRoot.setApplication(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoWebDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createGeronimoWebDeploymentPlan(IFile dpFile) {

		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString(), false);

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV20Utils.register(resourceSet, new WebResourceFactoryImpl(), WebPackage.eINSTANCE, WebPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.apache.geronimo.xml.ns.j2ee.web.DocumentRoot documentRoot = WebFactory.eINSTANCE.createDocumentRoot();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_WEB_NS_1_1);
		map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS_1_1);
		map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);

		WebAppType root = WebFactory.eINSTANCE.createWebAppType();

		root.setEnvironment(getConfigEnvironment());
		root.setContextRoot("/" + getProject().getName());
		// root.setContextPriorityClassloader(false); //TODO Replace this with
		// inverse-classloading

		documentRoot.setWebApp(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createOpenEjbDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createOpenEjbDeploymentPlan(IFile dpFile) {
		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString(), false);

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV20Utils.register(resourceSet, new JarResourceFactoryImpl(), JarPackage.eINSTANCE, JarPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.openejb.xml.ns.openejb.jar.DocumentRoot documentRoot = JarFactory.eINSTANCE.createDocumentRoot();
		OpenejbJarType root = JarFactory.eINSTANCE.createOpenejbJarType();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_OPENEJB_NS_2_1);
		map.put("sec", GeronimoSchemaNS.GERONIMO_SECURITY_NS_1_1);
		map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);
		map.put("pkgen", GeronimoSchemaNS.GERONIMO_PKGEN_NS_2_0);

		root.setEnvironment(getConfigEnvironment());
		root.setEnterpriseBeans(JarFactory.eINSTANCE.createEnterpriseBeansType());

		documentRoot.setOpenejbJar(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createConnectorDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject createConnectorDeploymentPlan(IFile dpFile) {
		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString(), false);

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV20Utils.register(resourceSet, new ConnectorResourceFactoryImpl(), ConnectorPackage.eINSTANCE, ConnectorPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		org.apache.geronimo.xml.ns.j2ee.connector.DocumentRoot documentRoot = ConnectorFactory.eINSTANCE.createDocumentRoot();
		ConnectorType root = ConnectorFactory.eINSTANCE.createConnectorType();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("", GeronimoSchemaNS.GERONIMO_CONNECTOR_NS_1_1);
		map.put("nam", GeronimoSchemaNS.GERONIMO_NAMING_NS_1_1);
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);

		root.setEnvironment(getConfigEnvironment());
		documentRoot.setConnector(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	public EObject createServiceDeploymentPlan(IFile dpFile) {
		URI uri = URI.createPlatformResourceURI(dpFile.getFullPath().toString(), false);

		ResourceSet resourceSet = new ResourceSetImpl();
		GeronimoV20Utils.register(resourceSet, new DeploymentResourceFactoryImpl(), DeploymentPackage.eINSTANCE, DeploymentPackage.eNS_URI);

		Resource resource = resourceSet.createResource(uri);
		DocumentRoot documentRoot = DeploymentFactory.eINSTANCE.createDocumentRoot();
		ModuleType root = DeploymentFactory.eINSTANCE.createModuleType();

		EMap map = documentRoot.getXMLNSPrefixMap();
		map.put("sys", GeronimoSchemaNS.GERONIMO_DEPLOYMENT_NS_1_1);

		root.setEnvironment(getConfigEnvironment());
		documentRoot.setModule(root);
		resource.getContents().add(documentRoot);

		save(resource);

		return root;
	}

	public EnvironmentType getConfigEnvironment() {
		if (config != null && config instanceof DeploymentPlanInstallConfig) {
			cfg = (DeploymentPlanInstallConfig) config;
		}

		String groupId = cfg != null && hasValue(cfg.getGroupId()) ? cfg.getGroupId()
				: "default";
		String artifactId = cfg != null && hasValue(cfg.getArtifactId()) ? cfg.getArtifactId()
				: getProject().getName();
		String version = cfg != null && hasValue(cfg.getVersion()) ? cfg.getVersion()
				: "1.0";
		String type = cfg != null && hasValue(cfg.getType()) ? cfg.getType()
				: "car";

		ArtifactType artifact = createArtifactType(groupId, artifactId, version, type);
		EnvironmentType env = DeploymentFactory.eINSTANCE.createEnvironmentType();
		env.setModuleId(artifact);

		if (cfg != null && cfg.isSharedLib()) {
			DependenciesType dt = DeploymentFactory.eINSTANCE.createDependenciesType();
			ArtifactType sharedLib = createDependencyType("org.apache.geronimo.configs", "sharedlib", null, "car");
			dt.getDependency().add(sharedLib);
			env.setDependencies(dt);
		}

		return env;
	}

	public static ArtifactType createArtifactType(String groupId, String artifactId, String version, String type) {
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

	public static ArtifactType createDependencyType(String groupId, String artifactId, String version, String type) {
		DependencyType artifact = DeploymentFactory.eINSTANCE.createDependencyType();
		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		return artifact;
	}

	private static boolean hasValue(String attribute) {
		return attribute != null && attribute.trim().length() != 0;
	}

}
