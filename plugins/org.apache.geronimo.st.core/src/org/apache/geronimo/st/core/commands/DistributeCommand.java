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
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.internal.impl.WorkbenchComponentImpl;
import org.eclipse.wst.server.core.IModule;

class DistributeCommand extends AbstractDeploymentCommand {

	boolean inPlace;

	Target[] targets;

	public DistributeCommand(IModule module, DeploymentManager dm, Target[] targets, boolean inPlace) {
		super(dm, module);
		this.inPlace = inPlace;
		this.targets = targets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus execute(IProgressMonitor monitor) {
		if (targets == null)
			targets = getDeploymentManager().getTargets();

		File file = null;

		if (inPlace) {
			StructureEdit moduleCore = StructureEdit.getStructureEditForRead(getModule().getProject());
			try {
				WorkbenchComponent component = moduleCore.getComponent();
				IPath loc = ((WorkbenchComponentImpl) component).getDefaultSourceRoot();
				file = getModule().getProject().findMember(loc).getLocation().toFile();
			} finally {
				if (moduleCore != null)
					moduleCore.dispose();
			}
		} else {
			file = DeploymentUtils.createJarFile(getModule());
		}
		
		Trace.trace(Trace.INFO, "Target: " + targets[0]);
		Trace.trace(Trace.INFO, "File: " + file.getAbsolutePath());

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
