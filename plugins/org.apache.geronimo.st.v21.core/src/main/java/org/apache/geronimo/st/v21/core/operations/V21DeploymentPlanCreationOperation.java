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
package org.apache.geronimo.st.v21.core.operations;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils;
import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.applicationclient.ApplicationClient;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.jee.connector.Resourceadapter;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.openejb.OpenejbJar;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.st.core.operations.DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v21.core.Activator;
import org.apache.geronimo.st.core.DeploymentPlanInstallConfig;
import org.apache.geronimo.st.v21.core.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;


/**
 * <strong>V21DeploymentPlanCreationOperation</strong>
 * is invoked when projects are created that are to be deployment on the 2.1
 * version of the Geronimo server. One of these Geronimo-specific deployment
 * plans is created as a result and is inserted into the user's Eclipse workspace in
 * the appropriate folder:
 * 
 * <ol>
 *      <li>geronimo-application.xml
 *      <li>geronimo-application-client.xml
 *      <li>geronimo-ra.xml
 *      <li>geronimo-service.xml
 *      <li>geronimo-web.xml
 *      <li>openejb-jar.xml
 * </ol>
 * 
 * @version $Rev$ $Date$
 */
public class V21DeploymentPlanCreationOperation extends DeploymentPlanCreationOperation {

	DeploymentPlanInstallConfig cfg;

	public V21DeploymentPlanCreationOperation(IDataModel model, Object config) {
		super(model, config);
  		Trace.tracePoint("Constructor", Activator.traceOperations, "V21DeploymentPlanCreationOperation", model, config);
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoApplicationDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoApplicationDeploymentPlan(IFile dpFile) throws Exception {
		Trace.tracePoint("Entry", Activator.traceOperations,
				"V21DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", dpFile);

		org.apache.geronimo.jee.application.ObjectFactory applicationFactory = new org.apache.geronimo.jee.application.ObjectFactory();
		Application application = applicationFactory.createApplication();

		application.setApplicationName(getProject().getName());
		application.setEnvironment(getConfigEnvironment());

		JAXBElement jaxbElement = applicationFactory.createApplication(application);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan",
				applicationFactory.createApplication(application));
		return applicationFactory.createApplication(application);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoWebDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoWebDeploymentPlan(IFile dpFile) throws Exception {
  		Trace.tracePoint("Entry", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan",
				dpFile, dpFile.getFullPath());
		org.apache.geronimo.jee.web.ObjectFactory webFactory = new org.apache.geronimo.jee.web.ObjectFactory();
		WebApp web = webFactory.createWebApp();

		web.setContextRoot("/" + getProject().getName());
		web.setEnvironment(getConfigEnvironment());

		JAXBElement jaxbElement = webFactory.createWebApp(web);
		try {
			JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);
		} catch(Exception e) {
			Trace.trace(Trace.ERROR, "marshalDeploymentPlan exception", e, Activator.logOperations);
		}

		Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", jaxbElement);
		return jaxbElement;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createOpenEjbDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createOpenEjbDeploymentPlan(IFile dpFile) throws Exception {
  		Trace.tracePoint("Entry", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", dpFile);

		org.apache.geronimo.jee.openejb.ObjectFactory ejbFactory = new org.apache.geronimo.jee.openejb.ObjectFactory();
		OpenejbJar ejbJar = ejbFactory.createOpenejbJar();

		ejbJar.setEnvironment(getConfigEnvironment());

		JAXBElement jaxbElement = ejbFactory.createOpenejbJar(ejbJar);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", jaxbElement);
		return jaxbElement;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createConnectorDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createConnectorDeploymentPlan(IFile dpFile) throws Exception {
  		Trace.tracePoint("Entry", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createConnectorDeploymentPlan", dpFile);

		org.apache.geronimo.jee.connector.ObjectFactory connectorFactory = new org.apache.geronimo.jee.connector.ObjectFactory();
		Connector connector = connectorFactory.createConnector();
		Resourceadapter resourceadapter = connectorFactory.createResourceadapter();
	
		connector.setEnvironment(getConfigEnvironment());
		connector.getResourceadapter().add(resourceadapter);

		JAXBElement jaxbElement = connectorFactory.createConnector(connector);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createConnectorDeploymentPlan", jaxbElement);
		return jaxbElement;
	}

		
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoApplicationDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoApplicationClientDeploymentPlan(IFile dpFile) throws Exception {
		Trace.tracePoint("Entry", Activator.traceOperations,"V21DeploymentPlanCreationOperation.createGeronimoApplicationClientDeploymentPlan", dpFile);

		org.apache.geronimo.jee.applicationclient.ObjectFactory applicationClientFactory = new org.apache.geronimo.jee.applicationclient.ObjectFactory();
		ApplicationClient applicationClient = applicationClientFactory.createApplicationClient();

        applicationClient.setServerEnvironment(getConfigEnvironment());
        applicationClient.setClientEnvironment(getConfigEnvironment());
        
		JAXBElement jaxbElement = applicationClientFactory.createApplicationClient(applicationClient);
		JAXBUtils.marshalDeploymentPlan(jaxbElement, dpFile);

		Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createGeronimoApplicationClientDeploymentPlan", applicationClientFactory.createApplicationClient(applicationClient));
		return applicationClientFactory.createApplicationClient(applicationClient);
	}

	
	public Environment getConfigEnvironment() {
        Trace.tracePoint("Entry", Activator.traceOperations, "V21DeploymentPlanCreationOperation.getConfigEnvironment");
		
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

		Artifact artifact = createArtifact(groupId, artifactId, version, type);
		org.apache.geronimo.jee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
   
		Environment env = serviceFactory.createEnvironment();
		env.setModuleId(artifact);

		if (cfg != null && cfg.isSharedLib()) {
			Dependencies dt = serviceFactory.createDependencies();
			Dependency sharedLib = createDependency("org.apache.geronimo.configs", "sharedlib", null, "car");
			dt.getDependency().add(sharedLib);
			env.setDependencies(dt);
		}

   	    Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.getConfigEnvironment", env);
		return env;
	}

	
	public static Artifact createArtifact(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createArtifact", groupId, artifactId, version, type);

		org.apache.geronimo.jee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
		Artifact artifact = serviceFactory.createArtifact();

		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		
  		Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createArtifact", artifact);
		return artifact;
	}

	
	public static Dependency createDependency(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createDependency", groupId, artifactId, version, type);

		org.apache.geronimo.jee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
		Dependency dependency = serviceFactory.createDependency();
		if (groupId != null)
			dependency.setGroupId(groupId);
		if (artifactId != null)
			dependency.setArtifactId(artifactId);
		if (version != null)
			dependency.setVersion(version);
		dependency.setType(type);
		
        Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.createDependency", dependency);
		return dependency;
	}

	
	private static boolean hasValue(String attribute) {
  		Trace.tracePoint("Entry", Activator.traceOperations, "V21DeploymentPlanCreationOperation.hasValue", attribute);
  		Trace.tracePoint("Exit ", Activator.traceOperations, "V21DeploymentPlanCreationOperation.hasValue", (attribute != null && attribute.trim().length() != 0) );
		
		return attribute != null && attribute.trim().length() != 0;
	}

}
