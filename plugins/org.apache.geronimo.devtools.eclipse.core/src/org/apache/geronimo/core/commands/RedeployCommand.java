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

import java.io.File;

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;

import org.apache.geronimo.core.DeploymentUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;

class RedeployCommand extends AbstractDeploymentCommand {

	public RedeployCommand(IModule module, DeploymentManager dm) {
		super(dm, module);
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus execute(IProgressMonitor monitor) {
		TargetModuleID id = getTargetModuleID(getModule());
		if (id != null) {
			File jarFile = DeploymentUtils.createJarFile(getModule());
			return new DeploymentCmdStatus(Status.OK_STATUS,
					getDeploymentManager().redeploy(
							new TargetModuleID[] { id }, jarFile, null));
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#getCommandType()
	 */
	public CommandType getCommandType() {
		return CommandType.REDEPLOY;
	}

}
