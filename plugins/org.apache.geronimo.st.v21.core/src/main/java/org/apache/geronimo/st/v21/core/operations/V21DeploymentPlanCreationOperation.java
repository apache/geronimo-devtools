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

import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.jee.deployment.Artifact;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.openejb.GeronimoEjbJar;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.st.core.operations.DeploymentPlanCreationOperation;
import org.apache.geronimo.st.v21.core.Activator;
import org.apache.geronimo.st.v21.core.DeploymentPlanInstallConfig;
import org.apache.geronimo.st.v21.core.internal.Trace;
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
 *      <li>JAXBElement warning messages
 *      <li>How to invoke Service Deployment Plan
 *      <li>How to invoke App Client Deployment Plan
 *      <li>Consolidate marshalDeploymentPlan and unmarshalDeploymentPlan into
 *      JAXBModelUtils for each specific Geronimo-specific deployment plan
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
		
		org.apache.geronimo.jee.application.ObjectFactory applicationFactory = new org.apache.geronimo.jee.application.ObjectFactory();
		Application application = applicationFactory.createApplication();
		
		application.setApplicationName(getProject().getName());
		application.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
		
            // 
            // Create JAXBContext with a context path containing the packages of all the
            // classes for the geronimo-application.xml file
            // 
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.web:" +
															   "org.apache.geronimo.jee.application:" +
															   "org.apache.geronimo.jee.deployment:" +
															   "org.apache.geronimo.jee.naming:" +
															   "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
		
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

		org.apache.geronimo.jee.web.ObjectFactory webFactory = new org.apache.geronimo.jee.web.ObjectFactory();
		WebApp web = webFactory.createWebApp();
		
		web.setContextRoot( "/" + getProject().getName() );
		web.setEnvironment(	getConfigEnvironment() );
		
		JAXBElement jaxbElement = null;
		try {
		
            // 
            // Create JAXBContext with a context path containing the packages of all the
            // classes for the geronimo-web.xml file
            // 
          	JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.web:" +
          													   "org.apache.geronimo.jee.naming:" +
          													   "org.apache.geronimo.jee.application:" +
          													   "org.apache.geronimo.jee.deployment", Activator.class.getClassLoader() );
		
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

		org.apache.geronimo.jee.openejb.ObjectFactory ejbFactory = new org.apache.geronimo.jee.openejb.ObjectFactory();
		GeronimoEjbJar ejbJar = ejbFactory.createGeronimoEjbJar();

		ejbJar.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
		
            // 
            // Create JAXBContext with a context path containing the packages of all the
            // classes for the openejb-jar.xml
            // 
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.openejb:" +
                                                               "org.apache.geronimo.jee.web:" +									
                                                               "org.apache.geronimo.jee.application:" +
															   "org.apache.geronimo.jee.deployment:" +
															   "org.apache.geronimo.jee.naming:" +
															   "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
		
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
		
		org.apache.geronimo.jee.connector.ObjectFactory connectorFactory = new org.apache.geronimo.jee.connector.ObjectFactory();
		Connector connector = connectorFactory.createConnector();
		
		connector.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
		
            // 
            // Create JAXBContext with a context path containing the packages of all the
            // classes for the geronimo-ra.xml
            // 
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.connector:" +
                                                               "org.apache.geronimo.jee.web:" +			
              												   "org.apache.geronimo.jee.application:" +
															   "org.apache.geronimo.jee.deployment:" +
															   "org.apache.geronimo.jee.naming:" +
															   "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
		
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

		org.apache.geronimo.jee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
		org.apache.geronimo.jee.deployment.Module module = serviceFactory.createModule();
		
		module.setEnvironment(getConfigEnvironment());
		
		JAXBElement jaxbElement = null;
		try {
		
			JAXBContext jaxbContext = JAXBContext.newInstance( "org.apache.geronimo.jee.web:" + 
															   "org.apache.geronimo.jee.application:" +
															   "org.apache.geronimo.jee.deployment:" +
															   "org.apache.geronimo.jee.naming:" +
															   "org.apache.geronimo.jee.security", Activator.class.getClassLoader() );
		
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

	
	public Environment getConfigEnvironment() {
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

   	    Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.getConfigEnvironment", env);
		return env;
	}

	
	public static Artifact createArtifact(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createArtifact", groupId, artifactId, version, type);

		org.apache.geronimo.jee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
		Artifact artifact = serviceFactory.createArtifact();

		if (groupId != null)
			artifact.setGroupId(groupId);
		if (artifactId != null)
			artifact.setArtifactId(artifactId);
		if (version != null)
			artifact.setVersion(version);
		artifact.setType(type);
		
  		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createArtifact", artifact);
		return artifact;
	}

	
	public static Dependency createDependency(String groupId, String artifactId, String version, String type) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.createDependency", groupId, artifactId, version, type);

		org.apache.geronimo.jee.deployment.ObjectFactory serviceFactory = new org.apache.geronimo.jee.deployment.ObjectFactory();
		Dependency dependency = serviceFactory.createDependency();
		if (groupId != null)
			dependency.setGroupId(groupId);
		if (artifactId != null)
			dependency.setArtifactId(artifactId);
		if (version != null)
			dependency.setVersion(version);
		dependency.setType(type);
		
        Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.createDependency", dependency);
		return dependency;
	}

	
	private static boolean hasValue(String attribute) {
  		Trace.tracePoint("Entry", "V21DeploymentPlanCreationOperation.hasValue", attribute);
  		Trace.tracePoint("Exit ", "V21DeploymentPlanCreationOperation.hasValue", (attribute != null && attribute.trim().length() != 0) );
		
		return attribute != null && attribute.trim().length() != 0;
	}

}
