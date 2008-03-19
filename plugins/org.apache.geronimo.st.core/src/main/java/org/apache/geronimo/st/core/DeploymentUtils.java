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

import java.io.File;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;

import org.apache.geronimo.st.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.core.commands.TargetModuleIdNotFoundException;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.j2ee.application.internal.operations.AppClientComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.application.internal.operations.EARComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.application.internal.operations.J2EEComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.EJBComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.jca.operations.ConnectorComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebComponentExportDataModelProvider;
import org.eclipse.jst.server.core.IEnterpriseApplication;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.jst.server.core.PublishUtil;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.util.ProjectModule;

/**
 * @version $Rev$ $Date$
 */
public class DeploymentUtils {

	public static final IPath STATE_LOC = Activator.getDefault().getStateLocation();

	private DeploymentUtils() {
	}
	
	public static IPath generateExplodedConfiguration(IModule module, IPath outputPath) {
		
		IPath output = outputPath.append(module.getName() + getModuleExtension(module));
		try {
			IModuleResource[] resources = getModuleResources(module);
			PublishUtil.publishSmart(resources, output, new NullProgressMonitor());
			if(GeronimoUtils.isEarModule(module)) {
				IEnterpriseApplication application = (IEnterpriseApplication) module.loadAdapter(IEnterpriseApplication.class, null);
				if( application != null ){
					IModule[] children = application.getModules();
					 for (int i = 0; i < children.length; i++) {
						 	IModule child = children[i];
							IPath childPath = output.append(child.getName() + getModuleExtension(child));
							IModuleResource[] childResources = getModuleResources(child);
							PublishUtil.publishSmart(childResources, childPath, new NullProgressMonitor());
							if(GeronimoUtils.isWebModule(child)) {
								IWebModule webModule = (IWebModule) module.loadAdapter(IWebModule.class, null);
								IModule[] libs = webModule.getModules();
								IPath webLibPath = childPath.append("WEB-INF").append("lib");
								for(int j = 0; j < libs.length; j++) {
									IModule lib = libs[j];
									IModuleResource[] libResources = getModuleResources(lib);
									PublishUtil.publishSmart(libResources, webLibPath.append(lib.getName() + getModuleExtension(lib)), new NullProgressMonitor());
								}
							}
					 }
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return output;
	}
	
	public static IModuleResource[] getModuleResources(IModule module) throws CoreException {
		ProjectModule pm = (ProjectModule)module.loadAdapter(ProjectModule.class, null);
		if (pm != null) {
			return pm.members();
		}
		return null;
	}
	
	private static String getModuleExtension(IModule module) {
		if(GeronimoUtils.isEarModule(module)) {
			return ".ear";
		}else if(GeronimoUtils.isWebModule(module)) {
			return ".war";
		}else if(GeronimoUtils.isRARModule(module)) {
			return ".rar";
		}
		return ".jar";
	}

	public static File createJarFile(IModule module, IPath outputPath) {
		IDataModel model = getExportDataModel(module);

		if (model != null) {

			IVirtualComponent comp = ComponentCore.createComponent(module.getProject());

			model.setProperty(J2EEComponentExportDataModelProvider.PROJECT_NAME, module.getProject());
			model.setProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION, outputPath.append(module.getName())
					+ ".zip");
			model.setProperty(J2EEComponentExportDataModelProvider.COMPONENT, comp);
			model.setBooleanProperty(J2EEComponentExportDataModelProvider.OVERWRITE_EXISTING, true);
			model.setBooleanProperty(J2EEComponentExportDataModelProvider.RUN_BUILD, false);

			if (model != null) {
				try {
					model.getDefaultOperation().execute(null, null);
					return new File(model.getStringProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION));
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public static IDataModel getExportDataModel(IModule module) {
		String type = module.getModuleType().getId();
		if (IModuleConstants.JST_WEB_MODULE.equals(type)) {
			return DataModelFactory.createDataModel(new WebComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_EJB_MODULE.equals(type)) {
			return DataModelFactory.createDataModel(new EJBComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_EAR_MODULE.equals(type)) {
			return DataModelFactory.createDataModel(new EARComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_CONNECTOR_MODULE.equals(type)) {
			return DataModelFactory.createDataModel(new ConnectorComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_APPCLIENT_MODULE.equals(type)) {
			DataModelFactory.createDataModel(new AppClientComponentExportDataModelProvider());
		}
		return null;
	}
	
	public static TargetModuleID getTargetModuleID(IServer server, IModule module) throws TargetModuleIdNotFoundException {
		String configId = ModuleArtifactMapper.getInstance().resolve(server, module);
		if(configId == null) {
			throw new TargetModuleIdNotFoundException("Could not do a local TargetModuleID lookup for module " + module.getName());
		}
		
		IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
		return gs.getVersionHandler().createTargetModuleId(configId);
	}

	public static TargetModuleID getTargetModuleID(DeploymentManager dm, String configId) throws TargetModuleIdNotFoundException {

		try {
			TargetModuleID ids[] = dm.getAvailableModules(null, dm.getTargets());
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].getModuleID().equals(configId)) {
						Trace.trace(Trace.INFO, "Found configuration " + configId +  " on server.");
						return ids[i];
					}
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TargetException e) {
			e.printStackTrace();
		}

		throw new TargetModuleIdNotFoundException("Could not find TargetModuleID with configId " + configId);
	}

	/**
	 * This method determines the last known config id for an IModule that has been deployed to the server.  The
	 * configId from the plan cannot be used as the project may have been previously deployed with a different
	 * configId.  In which case the lookup is done through the ModuleArtifactMapper first.
	 * 
	 * @param module
	 * @param server
	 * @return For a given module associated with a given server, this method returns the last known configuration id.
	 * @throws CoreException
	 */
	public static String getLastKnownConfigurationId(IModule module, IServer server) throws CoreException {
		
		IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
		String currentId = gs.getVersionHandler().getConfigID(module);
		String publishedId = ModuleArtifactMapper.getInstance().resolve(server, module);
		String query = publishedId != null ? publishedId : currentId;
		
		Trace.trace(Trace.INFO, "currentConfigId = " + currentId + " previousConfigId = " + publishedId);
		
		DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(server);
		
		try {
			getTargetModuleID(dm, query);
			return query;
		} catch (TargetModuleIdNotFoundException e) {
			Trace.trace(Trace.INFO, e.getMessage());
		}
		
		if(query != currentId) {
			try {
				getTargetModuleID(dm, currentId);
				return currentId;
			} catch (TargetModuleIdNotFoundException e) {
				Trace.trace(Trace.INFO, e.getMessage());
			}
		}
		
		return null;
	}

}
