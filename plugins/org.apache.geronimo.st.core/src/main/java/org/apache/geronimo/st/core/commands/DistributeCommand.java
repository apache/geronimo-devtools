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
package org.apache.geronimo.st.core.commands;

import java.io.File;

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
class DistributeCommand extends DeployCommand {

	Target[] targets;

	public DistributeCommand(IServer server, IModule module, Target[] targets) {
		super(server, module);
		this.targets = targets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus execute(IProgressMonitor monitor) throws CoreException {

		DeploymentManager dm = getDeploymentManager();

		if (targets == null)
			targets = dm.getTargets();

        Trace.trace(Trace.INFO, "Available targets: ");
        for ( int ii=0; ii<targets.length; ii++ ) {
            Trace.trace(Trace.INFO, "--> Target[" + ii + "]: " + targets[ii]);
        }

        //
        // Geronimo 2.1 now supports clustering which will likely result in multiple targets getting 
        // returned from the deployment manager. In our case though we have to ensure that only the 
        // first target is used, which is the default configuration store and which is explicitly 
        // configured by users. Thus, we will distribute the project to the defaultTarget only.
        //
        Target[] defaultTarget = new Target[1];
        defaultTarget[0] = targets[0];

        Trace.trace(Trace.INFO, "Using default target:");
        Trace.trace(Trace.INFO, "--> " + defaultTarget[0]);

		File file = getTargetFile();
		return new DeploymentCmdStatus(Status.OK_STATUS, dm.distribute( defaultTarget, file, null));
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
