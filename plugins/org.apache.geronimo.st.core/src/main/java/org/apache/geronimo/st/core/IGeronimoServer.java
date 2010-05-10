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
package org.apache.geronimo.st.core;

import java.util.List;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;

import org.apache.geronimo.st.core.operations.IGeronimoServerPluginManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.model.IURLProvider;

/**
 * @version $Rev$ $Date$
 */
public interface IGeronimoServer extends IURLProvider {

	public String getAdminID();

	public String getAdminPassword();

	public String getRMINamingPort();

	public String getDeployerURL();

	public String getJMXServiceURL();

	public IPath getJSR88DeployerJar();
	
	public DeploymentFactory getDeploymentFactory();

	public void configureDeploymentManager(DeploymentManager dm);

	public IGeronimoVersionHandler getVersionHandler();
	
	public boolean isNotRedeployJSPFiles();	
	
	public boolean isInPlaceSharedLib();
	
	public boolean isRunFromWorkspace();
	
	public boolean isSelectClasspathContainers();
	
	public List<String> getClasspathContainers();
	
	public int getPingDelay();
	
	public int getPingInterval();
	
	public int getMaxPings();
	
	public long getPublishTimeout();
	
	public IGeronimoServerPluginManager getServerPluginManager();

}
