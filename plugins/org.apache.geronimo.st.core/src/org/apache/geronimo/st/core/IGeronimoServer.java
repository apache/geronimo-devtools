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
package org.apache.geronimo.st.core;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;

import org.eclipse.core.runtime.IPath;

public interface IGeronimoServer {

	public String getAdminID();

	public String getAdminPassword();

	public String getRMINamingPort();

	public String getDeployerURL();

	public String getJMXServiceURL();

	public IPath getJSR88DeployerJar();
	
	public DeploymentFactory getDeploymentFactory();

	public void configureDeploymentManager(DeploymentManager dm);

	public IGeronimoVersionHandler getVersionHandler();

}
