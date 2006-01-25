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

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;

import org.apache.geronimo.core.GeronimoConnectionFactory;
import org.apache.geronimo.core.internal.GeronimoPlugin;
import org.apache.geronimo.core.internal.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

/**
 * 
 * This class provides static factory methods that return "synchronized" wrapper
 * instances of JSR-88 deployment commands.
 * 
 */
public class DeploymentCommandFactory {

	private DeploymentCommandFactory() {
	}

	/**
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createDistributeCommand(IModule module,
			IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new DistributeCommand(module,
				getDeploymentManager(server)));
	}

	/**
	 * @param ids
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createStartCommand(TargetModuleID[] ids,
			IModule module, IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new StartCommand(ids, module,
				getDeploymentManager(server)));
	}

	/**
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createStopCommand(IModule module,
			IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new StopCommand(module,
				getDeploymentManager(server)));
	}

	/**
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createRedeployCommand(IModule module,
			IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new RedeployCommand(module,
				getDeploymentManager(server)));
	}

	/**
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createUndeployCommand(IModule module,
			IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new UndeployCommand(module,
				getDeploymentManager(server)));
	}

	/**
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static DeploymentManager getDeploymentManager(IServer server)
			throws CoreException {
		try {
			return GeronimoConnectionFactory.getInstance()
					.getDeploymentManager(server);
		} catch (DeploymentManagerCreationException e) {
			throw new CoreException(
					new Status(IStatus.ERROR, GeronimoPlugin.PLUGIN_ID, 0,
							Messages.DM_CONNECTION_FAIL, e));
		}
	}

}
