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
package org.apache.geronimo.core.commands;

import javax.enterprise.deploy.shared.ModuleType;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;

import org.apache.geronimo.core.internal.GeronimoUtils;
import org.eclipse.wst.server.core.IModule;

abstract class AbstractDeploymentCommand implements IDeploymentCommand {

	private DeploymentManager dm;

	private IModule module;

	public AbstractDeploymentCommand(DeploymentManager dm, IModule module) {
		super();
		this.dm = dm;
		this.module = module;
	}

	public DeploymentManager getDeploymentManager() {
		return dm;
	}

	public IModule getModule() {
		return module;
	}

	public TargetModuleID getTargetModuleID(IModule module)
			throws TargetModuleIdNotFoundException {

		String configId = GeronimoUtils.getConfigId(module);
		ModuleType moduleType = GeronimoUtils.getJSR88ModuleType(module);

		try {
			TargetModuleID ids[] = dm.getAvailableModules(moduleType, dm.getTargets());
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

}
