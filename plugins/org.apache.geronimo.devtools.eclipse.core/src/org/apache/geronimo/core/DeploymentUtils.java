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
package org.apache.geronimo.core;

import java.io.File;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;

import org.apache.geronimo.core.commands.TargetModuleIdNotFoundException;
import org.apache.geronimo.core.internal.GeronimoPlugin;
import org.apache.geronimo.core.internal.GeronimoUtils;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.j2ee.application.internal.operations.AppClientComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.application.internal.operations.EARComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.application.internal.operations.J2EEComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.EJBComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.jca.operations.ConnectorComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebComponentExportDataModelProvider;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.server.core.IModule;

public class DeploymentUtils {

	public static final IPath STATE_LOC = GeronimoPlugin.getInstance()
			.getStateLocation();

	private DeploymentUtils() {
	}

	public static File createJarFile(IModule module) {
		IDataModel model = getExportDataModel(module);

		if (model != null) {

			IVirtualComponent comp = ComponentCore.createComponent(module
					.getProject());

			model.setProperty(
					J2EEComponentExportDataModelProvider.PROJECT_NAME, module
							.getProject());
			model.setProperty(
					J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION,
					STATE_LOC.append(module.getName()) + ".zip");
			model.setProperty(J2EEComponentExportDataModelProvider.COMPONENT,
					comp);
			model.setBooleanProperty(
					J2EEComponentExportDataModelProvider.OVERWRITE_EXISTING,
					true);

			if (model != null) {
				try {
					model.getDefaultOperation().execute(null, null);
					return new File(
							model
									.getStringProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION));
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
			return DataModelFactory
					.createDataModel(new WebComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_EJB_MODULE.equals(type)) {
			return DataModelFactory
					.createDataModel(new EJBComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_EAR_MODULE.equals(type)) {
			return DataModelFactory
					.createDataModel(new EARComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_CONNECTOR_MODULE.equals(type)) {
			return DataModelFactory
					.createDataModel(new ConnectorComponentExportDataModelProvider());
		} else if (IModuleConstants.JST_APPCLIENT_MODULE.equals(type)) {
			DataModelFactory
					.createDataModel(new AppClientComponentExportDataModelProvider());
		}
		return null;
	}

	public static TargetModuleID getTargetModuleID(IModule module,
			DeploymentManager dm) throws TargetModuleIdNotFoundException {

		String configId = GeronimoUtils.getConfigId(module);
		ModuleType moduleType = GeronimoUtils.getJSR88ModuleType(module);

		try {
			TargetModuleID ids[] = dm.getAvailableModules(moduleType, dm
					.getTargets());
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i].getModuleID().equals(configId)) {
						return ids[i];
					}
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (TargetException e) {
			e.printStackTrace();
		}

		throw new TargetModuleIdNotFoundException(
				"Could not find TargetModuleID for module " + module.getName()
						+ " with configId " + configId);
	}

	public static boolean configurationExists(IModule module,
			DeploymentManager dm) {
		try {
			return getTargetModuleID(module, dm) != null;
		} catch (TargetModuleIdNotFoundException e) {
			return false;
		}
	}

}
