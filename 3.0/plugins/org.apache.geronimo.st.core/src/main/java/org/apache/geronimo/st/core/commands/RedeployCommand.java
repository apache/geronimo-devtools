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

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.TargetModuleID;

import org.apache.geronimo.st.core.Activator;
import org.apache.geronimo.st.core.DeploymentUtils;
import org.apache.geronimo.st.core.GeronimoUtils;
import org.apache.geronimo.st.core.IGeronimoServer;
import org.apache.geronimo.st.core.ModuleArtifactMapper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
class RedeployCommand extends DeployCommand {

	public RedeployCommand(IServer server, IModule module) {
		super(server, module);
	}

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.core.commands.IDeploymentCommand#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    public IStatus execute(IProgressMonitor monitor) throws TargetModuleIdNotFoundException, CoreException {
        String configId = ModuleArtifactMapper.getInstance().resolve(getServer(), getModule());
        
        if(configId == null) {
            IGeronimoServer gs = (IGeronimoServer) getServer().getAdapter(IGeronimoServer.class);
            try {
				configId = gs.getVersionHandler().getConfigID(getModule());
			} catch (Exception e) {
				throw new CoreException(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Module config Id not found for redeployment",e));
			}
        }
        
        TargetModuleID[] ids = null;
        if(configId != null) {
            TargetModuleID id = DeploymentUtils.getTargetModuleID(getDeploymentManager(), configId);
            ids = new TargetModuleID[] {id};
        }
        else {
            throw new CoreException(new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Module config Id not found for redeployment"));
        }
        
        return new DeploymentCmdStatus(Status.OK_STATUS, getDeploymentManager().redeploy(ids, getTargetFile(), GeronimoUtils.getDeploymentPlanFile(getModule()).getLocation().toFile()));
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
