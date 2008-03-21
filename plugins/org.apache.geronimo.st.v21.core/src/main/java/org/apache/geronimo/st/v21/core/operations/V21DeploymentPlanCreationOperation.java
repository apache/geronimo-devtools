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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.geronimo.st.core.operations.DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v21.core.Activator;
import org.apache.geronimo.st.v21.core.DeploymentPlanInstallConfig;
import org.apache.geronimo.st.v21.core.internal.Trace;
import org.apache.geronimo.xml.ns.deployment_1.ArtifactType;
import org.apache.geronimo.xml.ns.deployment_1.DependenciesType;
import org.apache.geronimo.xml.ns.deployment_1.DependencyType;
import org.apache.geronimo.xml.ns.deployment_1.EnvironmentType;
import org.apache.geronimo.xml.ns.j2ee.application_2.ApplicationType;
import org.apache.geronimo.xml.ns.j2ee.connector_1.ConnectorType;
import org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2.GeronimoEjbJarType;
import org.apache.geronimo.xml.ns.j2ee.web_2_0.WebAppType;
import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;


/**
 * <strong>V21DeploymentPlanCreationOperation</strong>
 * is invoked when projects are created the are to be deployment on the 2.1
 * version of the Geronimo server. One of these Geronimo-specific deployment
 * plans is created as a result is inserted into the user's Eclipse workspace in
 * the appropriate folder:
 * 
 * <ol>
 *      <li>geronimo-application.xml
 *      <li>geronimo-web.xml
 *      <li>openejb-jar.xml
 *      <li>geronimo-ra.xml
 * </ol>
 * 
 * Remaining TODO Task(s):
 * 
 * <ol>
 *      <li>Use the Eclipse framework IResource interface to create the files
 *      above -- otherwise the user will have to refresh their workspace for the
 *      contents of the file to display, and an "Out of sync" message will be
 *      displayed.
 *      <li>JUnit testcases
 *      <li>How to invoke Service Deployment Plan
 *      <li>How to invoke App Client Deployment Plan
 * </ol>
 * 
 * @version $Rev: 509704 $ $Date: 2007-02-20 13:42:24 -0500 (Tue, 20 Feb 2007) $
 */
public class V21DeploymentPlanCreationOperation extends DeploymentPlanCreationOperation {

	DeploymentPlanInstallConfig cfg;

	public V21DeploymentPlanCreationOperation(IDataModel model, Object config) {
		super(model, config);
  		Trace.tracePoint("Constructor Entry/Exit", "V21DeploymentPlanCreationOperation", model, config);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoApplicationDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoApplicationDeploymentPlan(IFile dpFile) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", dpFile);
		
		org.apache.geronimo.xml.ns.j2ee.application_2.ObjectFactory applicationFactory = new org.apache.geronimo.xml.ns.j2ee.application_2.ObjectFactory();
		ApplicationType application = applicationFactory.createApplicationType();
		
		application.setApplicationName(getProject().getName());
		application.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.xml.ns.j2ee.web_2_0:org.apache.geronimo.xml.ns.j2ee.application_2:org.apache.geronimo.xml.ns.deployment_1:org.apache.geronimo.xml.ns.naming_1:org.apache.geronimo.xml.ns.security_2", Activator.class.getClassLoader() );
			jaxbElement = applicationFactory.createApplication(application);
			Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(jaxbElement, new FileOutputStream( new File( dpFile.getLocationURI().toURL().getFile() )));
		}
		catch( JAXBException jaxbException ) {
			Trace.tracePoint("JAXBException", "V21DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", dpFile.getFullPath() );
			jaxbException.printStackTrace();
		}
		catch( FileNotFoundException fileNotFoundException ) {
			Trace.tracePoint("FileNotFoundException", "V21DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", dpFile.getFullPath() );
			fileNotFoundException.printStackTrace();
		}
		catch( MalformedURLException malformedURLException ) {
			Trace.tracePoint("MalformedURLException", "V21DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", dpFile.getFullPath() );
			malformedURLException.printStackTrace();
		}
		
		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createGeronimoApplicationDeploymentPlan", applicationFactory.createApplication(application));
		return applicationFactory.createApplication(application);
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createGeronimoWebDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createGeronimoWebDeploymentPlan(IFile dpFile) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", dpFile, dpFile.getFullPath() );

		org.apache.geronimo.xml.ns.j2ee.web_2_0.ObjectFactory webFactory = new org.apache.geronimo.xml.ns.j2ee.web_2_0.ObjectFactory();
		WebAppType web = webFactory.createWebAppType();
		
		web.setContextRoot( "/" + getProject().getName() );
		web.setEnvironment(	getConfigEnvironment() );
		
		JAXBElement jaxbElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.xml.ns.j2ee.web_2_0:org.apache.geronimo.xml.ns.j2ee.application_2:org.apache.geronimo.xml.ns.deployment_1:org.apache.geronimo.xml.ns.naming_1:org.apache.geronimo.xml.ns.security_2", Activator.class.getClassLoader() );
			jaxbElement = webFactory.createWebApp(web);
			Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(jaxbElement, new FileOutputStream( new File( dpFile.getLocationURI().toURL().getFile() )));
		}
		catch( JAXBException jaxbException ) {
			Trace.tracePoint("JAXBException", "V21DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", dpFile.getFullPath() );
			jaxbException.printStackTrace();
		}
		catch( FileNotFoundException fileNotFoundException ) {
			Trace.tracePoint("FileNotFoundException", "V21DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", dpFile.getFullPath() );
			fileNotFoundException.printStackTrace();
		}
		catch( MalformedURLException malformedURLException ) {
			Trace.tracePoint("MalformedURLException", "V21DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", dpFile.getFullPath() );
			malformedURLException.printStackTrace();
		}
	
		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createGeronimoWebDeploymentPlan", jaxbElement);
		return jaxbElement;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createOpenEjbDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createOpenEjbDeploymentPlan(IFile dpFile) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", dpFile);

		org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2.ObjectFactory ejbFactory = new org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2.ObjectFactory();
		GeronimoEjbJarType ejbJar = ejbFactory.createGeronimoEjbJarType();

		ejbJar.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.xml.ns.j2ee.ejb.openejb_2:org.apache.geronimo.xml.ns.j2ee.web_2_0:org.apache.geronimo.xml.ns.j2ee.application_2:org.apache.geronimo.xml.ns.deployment_1:org.apache.geronimo.xml.ns.naming_1:org.apache.geronimo.xml.ns.security_2", Activator.class.getClassLoader() );
			jaxbElement = ejbFactory.createEjbJar(ejbJar);
			Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(jaxbElement, new FileOutputStream( new File( dpFile.getLocationURI().toURL().getFile() )));
		}
		catch( JAXBException jaxbException ) {
			Trace.tracePoint("JAXBException", "V21DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", dpFile.getFullPath() );
			jaxbException.printStackTrace();
		}
		catch( FileNotFoundException fileNotFoundException ) {
			Trace.tracePoint("FileNotFoundException", "V21DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", dpFile.getFullPath() );
			fileNotFoundException.printStackTrace();
		}
		catch( MalformedURLException malformedURLException ) {
			Trace.tracePoint("MalformedURLException", "V21DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", dpFile.getFullPath() );
			malformedURLException.printStackTrace();
		}
		
		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createOpenEjbDeploymentPlan", jaxbElement);
		return jaxbElement;
		
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.operations.IDeploymentPlanCreationOp#createConnectorDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public JAXBElement createConnectorDeploymentPlan(IFile dpFile) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createConnectorDeploymentPlan", dpFile);
		
		org.apache.geronimo.xml.ns.j2ee.connector_1.ObjectFactory connectorFactory = new org.apache.geronimo.xml.ns.j2ee.connector_1.ObjectFactory();
		ConnectorType connector = connectorFactory.createConnectorType();
		
		connector.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.xml.ns.j2ee.connector_1:org.apache.geronimo.xml.ns.j2ee.web_2_0:org.apache.geronimo.xml.ns.j2ee.application_2:org.apache.geronimo.xml.ns.deployment_1:org.apache.geronimo.xml.ns.naming_1:org.apache.geronimo.xml.ns.security_2", Activator.class.getClassLoader() );
			jaxbElement = connectorFactory.createConnector(connector);
			Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(jaxbElement, new FileOutputStream( new File( dpFile.getLocationURI().toURL().getFile() )));
		}
		catch( JAXBException jaxbException ) {
			Trace.tracePoint("JAXBException", "V21DeploymentPlanCreationOperation.createConnectorDeploymentPlan", dpFile.getFullPath() );
			jaxbException.printStackTrace();
		}
		catch( FileNotFoundException fileNotFoundException ) {
			Trace.tracePoint("FileNotFoundException", "V21DeploymentPlanCreationOperation.createConnectorDeploymentPlan", dpFile.getFullPath() );
			fileNotFoundException.printStackTrace();
		}
		catch( MalformedURLException malformedURLException ) {
			Trace.tracePoint("MalformedURLException", "V21DeploymentPlanCreationOperation.createConnectorDeploymentPlan", dpFile.getFullPath() );
			malformedURLException.printStackTrace();
		}
		
		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createConnectorDeploymentPlan", jaxbElement);
		return jaxbElement;

	}

		
	public JAXBElement createServiceDeploymentPlan(IFile dpFile) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createServiceDeploymentPlan", dpFile);

		org.apache.geronimo.xml.ns.deployment_1.ObjectFactory serviceFactory = new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory();
		org.apache.geronimo.xml.ns.deployment_1.ModuleType module = serviceFactory.createModuleType();
		
		module.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.xml.ns.j2ee.web_2_0:org.apache.geronimo.xml.ns.j2ee.application_2:org.apache.geronimo.xml.ns.deployment_1:org.apache.geronimo.xml.ns.naming_1:org.apache.geronimo.xml.ns.security_2", Activator.class.getClassLoader() );
			jaxbElement = serviceFactory.createModule(module);
			Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(jaxbElement, new FileOutputStream( new File( dpFile.getLocationURI().toURL().getFile() )));
		}
		catch( JAXBException jaxbException ) {
			Trace.tracePoint("JAXBException", "V21DeploymentPlanCreationOperation.createServiceDeploymentPlan", dpFile.getFullPath() );
			jaxbException.printStackTrace();
		}
		catch( FileNotFoundException fileNotFoundException ) {
			Trace.tracePoint("FileNotFoundException", "V21DeploymentPlanCreationOperation.createServiceDeploymentPlan", dpFile.getFullPath() );
			fileNotFoundException.printStackTrace();
		}
		catch( MalformedURLException malformedURLException ) {
			Trace.tracePoint("MalformedURLException", "V21DeploymentPlanCreationOperation.createServiceDeploymentPlan", dpFile.getFullPath() );
			malformedURLException.printStackTrace();
		}
		
		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createServiceDeploymentPlan", jaxbElement);
		return jaxbElement;

	}

	
	public EnvironmentType getConfigEnvironment() {
        Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.getConfigEnvironment");
		
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

   	    Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.getConfigEnvironment", env);
		return env;
	}

	
	public static ArtifactType createArtifactType(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createArtifactType", groupId, artifactId, version, type);

		org.apache.geronimo.xml.ns.deployment_1.ObjectFactory serviceFactory = new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory();
		ArtifactType artifact = serviceFactory.createArtifactType();

		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		
  		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createArtifactType", artifact);
		return artifact;
	}

	
	public static DependencyType createDependencyType(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createDependencyType", groupId, artifactId, version, type);

		org.apache.geronimo.xml.ns.deployment_1.ObjectFactory serviceFactory = new org.apache.geronimo.xml.ns.deployment_1.ObjectFactory();
		DependencyType dependency = serviceFactory.createDependencyType();
		if (groupId != null)
			dependency.setGroupId(groupId);
		if (artifactId != null)
			dependency.setArtifactId(artifactId);
		if (version != null)
			dependency.setVersion(version);
		dependency.setType(type);
		
        Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createDependencyType", dependency);
		return dependency;
	}

	
	private static boolean hasValue(String attribute) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.hasValue", attribute);
  		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.hasValue", (attribute != null && attribute.trim().length() != 0) );
		
		return attribute != null && attribute.trim().length() != 0;
	}

}
