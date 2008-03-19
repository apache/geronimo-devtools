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

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;

import org.apache.geronimo.st.core.Activator;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.st.core.IGeronimoServerBehavior;
import org.apache.geronimo.st.core.internal.Messages;
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
 * @version $Rev$ $Date$
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
	public static IDeploymentCommand createDistributeCommand(IModule module, IServer server) throws CoreException {
		IGeronimoServerBehavior gs = (IGeronimoServerBehavior) server.loadAdapter(IGeronimoServerBehavior.class, null);
		Target[] targets = gs.getTargets();
		return new SynchronizedDeploymentOp(new DistributeCommand(server, module, targets));
	}

	/**
	 * @param ids
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createStartCommand(TargetModuleID[] ids, IModule module, IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new StartCommand(server, ids, module));
	}

	/**
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createStopCommand(IModule module, IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new StopCommand(server, module));
	}

	/**
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createRedeployCommand(IModule module, IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new RedeployCommand(server, module));
	}

	/**
	 * @param module
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static IDeploymentCommand createUndeployCommand(IModule module, IServer server) throws CoreException {
		return new SynchronizedDeploymentOp(new UndeployCommand(server, module));
	}

	/**
	 * @param server
	 * @return
	 * @throws CoreException
	 */
	public static DeploymentManager getDeploymentManager(IServer server) throws CoreException {
		try {
			DeploymentManager dm = GeronimoConnectionFactory.getInstance().getDeploymentManager(server);
			if (dm == null) {
				throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, Messages.DM_CONNECTION_FAIL, null));
			}
			return dm;
		} catch (DeploymentManagerCreationException e) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, Messages.DM_CONNECTION_FAIL, e));
		}
	}

}
