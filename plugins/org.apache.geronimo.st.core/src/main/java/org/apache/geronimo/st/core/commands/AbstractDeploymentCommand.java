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

import org.apache.geronimo.st.core.IGeronimoServer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
abstract class AbstractDeploymentCommand implements IDeploymentCommand {
	
	private IServer server;

	private IModule module;
	
	private long timeout;

	public AbstractDeploymentCommand(IServer server, IModule module) {
		super();
		this.server = server;
		this.module = module;
		timeout = getGeronimoServer().getPublishTimeout();
	}

	public DeploymentManager getDeploymentManager() throws CoreException {
		return DeploymentCommandFactory.getDeploymentManager(server);
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.commands.IDeploymentCommand#getModule()
	 */
	public IModule getModule() {
		return module;
	}

	public IServer getServer() {
		return server;
	}
	
	public IGeronimoServer getGeronimoServer() {
		return (IGeronimoServer) getServer().getAdapter(IGeronimoServer.class);
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.commands.IDeploymentCommand#getTimeout()
	 */
	public long getTimeout() {
		return timeout;
	}
}
