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
package org.apache.geronimo.st.v11.core.operations;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.application.ApplicationType;
import org.apache.geronimo.j2ee.application.ObjectFactory;
import org.apache.geronimo.j2ee.applicationclient.ApplicationClientType;
import org.apache.geronimo.j2ee.connector.ResourceadapterType;
import org.apache.geronimo.j2ee.deployment.ArtifactType;
import org.apache.geronimo.j2ee.deployment.DependenciesType;
import org.apache.geronimo.j2ee.deployment.DependencyType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.j2ee.deployment.ServiceType;
import org.apache.geronimo.j2ee.openejb_jar.OpenejbJarType;
import org.apache.geronimo.j2ee.web.WebAppType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils;
import org.apache.geronimo.st.core.DeploymentPlanInstallConfig;
import org.apache.geronimo.st.core.operations.DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * @version $Rev$ $Date$
 */
public class V11DeploymentPlanCreationOperation extends DeploymentPlanCreationOperation {

	DeploymentPlanInstallConfig cfg;

	public V11DeploymentPlanCreationOperation(IDataModel model, Object config) {
		super(model, config);
		Trace.tracePoint("Constructor", "V11DeploymentPlanCreationOperation", model, config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoApplicationDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoApplicationDeploymentPlan(IFile dpFile) throws Exception {
		Trace.tracePoint("Entry",
				"V11DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", dpFile);

		ObjectFactory applicationFactory = new ObjectFactory();
		ApplicationType application = applicationFactory.createApplicationType();

		application.setApplicationName(getProject().getName());
		application.setEnvironment(getConfigEnvironment());

		JAXBElement jaxbElement = applicationFactory.createApplication(application);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan",
				applicationFactory.createApplication(application));
		return applicationFactory.createApplication(application);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoWebDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoWebDeploymentPlan(IFile dpFile) throws Exception {
  		Trace.tracePoint("Entry", "V11DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan",
				dpFile, dpFile.getFullPath());

  		org.apache.geronimo.j2ee.web.ObjectFactory webFactory = new org.apache.geronimo.j2ee.web.ObjectFactory();
		WebAppType web = webFactory.createWebAppType();

		web.setContextRoot("/" + getProject().getName());
		web.setEnvironment(getConfigEnvironment());

		JAXBElement jaxbElement = webFactory.createWebApp(web);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", jaxbElement);
		return jaxbElement;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createOpenEjbDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createOpenEjbDeploymentPlan(IFile dpFile) throws Exception {
  		Trace.tracePoint("Entry", "V11DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", dpFile);

  		org.apache.geronimo.j2ee.openejb_jar.ObjectFactory ejbFactory = new org.apache.geronimo.j2ee.openejb_jar.ObjectFactory();
		OpenejbJarType ejbJar = ejbFactory.createOpenejbJarType();

		ejbJar.setEnvironment(getConfigEnvironment());

		JAXBElement jaxbElement = ejbFactory.createOpenejbJar(ejbJar);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", jaxbElement);
		return jaxbElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createConnectorDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createConnectorDeploymentPlan(IFile dpFile) throws Exception {
		Trace.tracePoint("Entry", "V11DeploymentPlanCreationOperation.createConnectorDeploymentPlan", dpFile);

		org.apache.geronimo.j2ee.connector.ObjectFactory connectorFactory = new org.apache.geronimo.j2ee.connector.ObjectFactory();
		org.apache.geronimo.j2ee.connector.ConnectorType connector = connectorFactory.createConnectorType();
		ResourceadapterType resourceadapter = connectorFactory.createResourceadapterType();
	
		connector.setEnvironment(getConfigEnvironment());
		connector.getResourceadapter().add(resourceadapter);

		JAXBElement jaxbElement = connectorFactory.createConnector(connector);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createConnectorDeploymentPlan", jaxbElement);
		return jaxbElement;
	}
	
	public JAXBElement createGeronimoApplicationClientDeploymentPlan(IFile dpFile) throws Exception {
		Trace.tracePoint("Entry","V11DeploymentPlanCreationOperation.createGeronimoApplicationClientDeploymentPlan", dpFile);

		org.apache.geronimo.j2ee.applicationclient.ObjectFactory applicationClientFactory = new org.apache.geronimo.j2ee.applicationclient.ObjectFactory();
		ApplicationClientType applicationClient = applicationClientFactory.createApplicationClientType();

        applicationClient.setServerEnvironment(getConfigEnvironment());
        applicationClient.setClientEnvironment(getConfigEnvironment());
        
		JAXBElement jaxbElement = applicationClientFactory.createApplicationClient(applicationClient);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createGeronimoApplicationClientDeploymentPlan", applicationClientFactory.createApplicationClient(applicationClient));
		return applicationClientFactory.createApplicationClient(applicationClient);
	}

	public JAXBElement createServiceDeploymentPlan(IFile dpFile) throws Exception {
		Trace.tracePoint("Entry","V11DeploymentPlanCreationOperation.createServiceDeploymentPlan", dpFile);

		org.apache.geronimo.j2ee.deployment.ObjectFactory artifactFactory = new org.apache.geronimo.j2ee.deployment.ObjectFactory();
		ServiceType service = artifactFactory.createServiceType();

        
		JAXBElement jaxbElement = artifactFactory.createService(service);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createServiceDeploymentPlan", artifactFactory.createService(service));
		return artifactFactory.createService(service);
	}

	public EnvironmentType getConfigEnvironment() {
        Trace.tracePoint("Entry", "V11DeploymentPlanCreationOperation.getConfigEnvironment");
		
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

		ArtifactType artifact = createArtifact(groupId, artifactId, version, type);
		org.apache.geronimo.j2ee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.j2ee.deployment.ObjectFactory();
   
		EnvironmentType env = serviceFactory.createEnvironmentType();
		env.setModuleId(artifact);

		if (cfg != null && cfg.isSharedLib()) {
			DependenciesType dt = serviceFactory.createDependenciesType();
			DependencyType sharedLib = createDependency("org.apache.geronimo.configs", "sharedlib", null, "car");
			dt.getDependency().add(sharedLib);
			env.setDependencies(dt);
		}

   	    Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.getConfigEnvironment", env);
		return env;
	}

	private static boolean hasValue(String attribute) {
		return attribute != null && attribute.trim().length() != 0;
	}
	
	public static ArtifactType createArtifact(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", "V11DeploymentPlanCreationOperation.createArtifact", groupId, artifactId, version, type);

  		org.apache.geronimo.j2ee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.j2ee.deployment.ObjectFactory();
		ArtifactType artifact = serviceFactory.createArtifactType();

		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		
  		Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createArtifact", artifact);
		return artifact;
	}
	
	public static DependencyType createDependency(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", "V11DeploymentPlanCreationOperation.createDependency", groupId, artifactId, version, type);

  		org.apache.geronimo.j2ee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.j2ee.deployment.ObjectFactory();
		DependencyType dependency = serviceFactory.createDependencyType();
		if (groupId != null)
			dependency.setGroupId(groupId);
		if (artifactId != null)
			dependency.setArtifactId(artifactId);
		if (version != null)
			dependency.setVersion(version);
		dependency.setType(type);
		
        Trace.tracePoint("Exit ", "V11DeploymentPlanCreationOperation.createDependency", dependency);
		return dependency;
	}


}
