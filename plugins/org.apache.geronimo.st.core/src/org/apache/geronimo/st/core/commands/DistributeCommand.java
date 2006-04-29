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
package org.apache.geronimo.st.core.commands;

import java.io.File;

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;
import org.apache.geronimo.st.core.DeploymentUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;

class DistributeCommand extends AbstractDeploymentCommand {
	
	boolean inPlace;
	
	public DistributeCommand(IModule module, DeploymentManager dm, boolean inPlace) {
		super(dm, module);
		this.inPlace = inPlace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus execute(IProgressMonitor monitor) {
		Target[] targets = getDeploymentManager().getTargets();
		File file = null;
		
		if(inPlace) {
			file = getModule().getProject().getLocation().toFile();
		} else {
			file = DeploymentUtils.createJarFile(getModule());
		}
		
		return new DeploymentCmdStatus(Status.OK_STATUS, getDeploymentManager().distribute(targets, file, null));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#getCommandType()
	 */
	public CommandType getCommandType() {
		return CommandType.DISTRIBUTE;
	}
	

}
