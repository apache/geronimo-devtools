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

import java.net.URI;

import javax.enterprise.deploy.spi.factories.DeploymentFactory;
import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.core.GeronimoSchemaNS;
import org.apache.geronimo.st.core.operations.DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v20.core.DeploymentPlanInstallConfig;
import org.apache.geronimo.st.v20.core.GeronimoV20Utils;
import org.apache.geronimo.st.v20.core.internal.Trace;
import org.apache.geronimo.xml.ns.deployment_1.ArtifactType;
import org.apache.geronimo.xml.ns.deployment_1.DependenciesType;
import org.apache.geronimo.xml.ns.deployment_1.DependencyType;
import org.apache.geronimo.xml.ns.deployment_1.EnvironmentType;
import org.apache.geronimo.xml.ns.j2ee.application_2.ApplicationType;
import org.apache.geronimo.xml.ns.j2ee.application_2.ModuleType;
import org.apache.geronimo.xml.ns.j2ee.connector_1.ConnectorType;
import org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2.GeronimoEjbJarType;
import org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2.OpenejbJarType;
import org.apache.geronimo.xml.ns.j2ee.web_2_0.WebAppType;
import org.eclipse.core.internal.resources.Resource;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * @version $Rev: 509704 $ $Date: 2007-02-20 13:42:24 -0500 (Tue, 20 Feb 2007) $
 */
public class V20DeploymentPlanCreationOperation extends DeploymentPlanCreationOperation {

	DeploymentPlanInstallConfig cfg;

	public V20DeploymentPlanCreationOperation(IDataModel model, Object config) {
		super(model, config);
  		Trace.trace("Constructor Entry/Exit", "V20DeploymentPlanCreationOperation", model, config);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoApplicationDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoApplicationDeploymentPlan(IFile dpFile) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", dpFile);
		
		org.apache.geronimo.xml.ns.j2ee.application_2.ObjectFactory applicationFactory = new org.apache.geronimo.xml.ns.j2ee.application_2.ObjectFactory();
		ApplicationType application = applicationFactory.createApplicationType();
		application.setApplicationName(getProject().getName());
		application.setEnvironment(getConfigEnvironment());
		
/*
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
*/
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", root);
		return applicationFactory.createApplication(application);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoWebDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoWebDeploymentPlan(IFile dpFile) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", dpFile);
		org.apache.geronimo.xml.ns.j2ee.web_2_0.ObjectFactory webFactory = new org.apache.geronimo.xml.ns.j2ee.web_2_0.ObjectFactory();
		WebAppType web = webFactory.createWebAppType();
/*
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
*/
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", root);
		return webFactory.createWebApp(web);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createOpenEjbDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createOpenEjbDeploymentPlan(IFile dpFile) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", dpFile);
		org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2.ObjectFactory ejbFactory = new org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2.ObjectFactory();
		GeronimoEjbJarType ejbjar = ejbFactory.createGeronimoEjbJarType();
/*
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
*/
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", root);
		return ejbFactory.createEjbJar(ejbjar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createConnectorDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createConnectorDeploymentPlan(IFile dpFile) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.createConnectorDeploymentPlan", dpFile);
		org.apache.geronimo.xml.ns.j2ee.connector_1.ObjectFactory connectorFactory = new org.apache.geronimo.xml.ns.j2ee.connector_1.ObjectFactory();
		ConnectorType connector = connectorFactory.createConnectorType();
		return connectorFactory.createConnector(connector);
/*		
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
*/
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.createConnectorDeploymentPlan", root);
//		return root;
	}

	public JAXBElement createServiceDeploymentPlan(IFile dpFile) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.createServiceDeploymentPlan", dpFile);
		org.apache.geronimo.xml.ns.deployment_1.ObjectFactory serviceFactory = new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory();
		org.apache.geronimo.xml.ns.deployment_1.ModuleType module = serviceFactory.createModuleType();
		return serviceFactory.createModule(module);
/*
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
*/
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.createServiceDeploymentPlan", root);
//		return root;
	}

	public EnvironmentType getConfigEnvironment() {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.getConfigEnvironment");
		
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
		org.apache.geronimo.xml.ns.deployment_1.ObjectFactory serviceFactory = new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory();
		
		EnvironmentType env = serviceFactory.createEnvironmentType();
		env.setModuleId(artifact);

		if (cfg != null && cfg.isSharedLib()) {
			DependenciesType dt = serviceFactory.createDependenciesType();
			DependencyType sharedLib = createDependencyType("org.apache.geronimo.configs", "sharedlib", null, "car");
			dt.getDependency().add(sharedLib);
			env.setDependencies(dt);
		}

//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.getConfigEnvironment", env);
		return env;
	}

	public static ArtifactType createArtifactType(String groupId, String artifactId, String version, String type) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.createArtifactType", groupId, artifactId, version, type);
		org.apache.geronimo.xml.ns.deployment_1.ObjectFactory serviceFactory = new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory();
		ArtifactType artifact = serviceFactory.createArtifactType();
		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.createArtifactType", artifact);
		return artifact;
	}

	public static DependencyType createDependencyType(String groupId, String artifactId, String version, String type) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.createDependencyType", groupId, artifactId, version, type);
		org.apache.geronimo.xml.ns.deployment_1.ObjectFactory serviceFactory = new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory();
		DependencyType artifact = serviceFactory.createDependencyType();
		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.createDependencyType", artifact);
		return artifact;
	}

	private static boolean hasValue(String attribute) {
//		Trace.trace("Entry", "V20DeploymentPlanCreationOperation.hasValue", attribute);
//		Trace.trace("Exit", "V20DeploymentPlanCreationOperation.hasValue", (attribute != null && attribute.trim().length() != 0) );
		
		return attribute != null && attribute.trim().length() != 0;
	}

}
