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
package org.apache.geronimo.st.v11.core;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;

import org.apache.geronimo.crypto.EncryptionManager;
import org.apache.geronimo.deployment.plugin.factories.DeploymentFactoryImpl;
import org.apache.geronimo.deployment.plugin.jmx.JMXDeploymentManager;
import org.apache.geronimo.st.core.GeronimoServerDelegate;
import org.apache.geronimo.st.core.IGeronimoVersionHandler;
import org.apache.geronimo.st.core.operations.IGeronimoServerPluginManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServer extends GeronimoServerDelegate {

	private static IGeronimoVersionHandler versionHandler = null;
	
	private static DeploymentFactory deploymentFactory;
	
	static {
		deploymentFactory = new DeploymentFactoryImpl();
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.core.GenericGeronimoServer#getContextRoot(org.eclipse.wst.server.core.IModule)
	 */
	public String getContextRoot(IModule module) throws Exception {
		return GeronimoV11Utils.getContextRoot(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServer#getDeployerURL()
	 */
	public String getDeployerURL() {
		return "deployer:geronimo:jmx://" + getServer().getHost() + ":" + getRMINamingPort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServer#getJMXServiceURL()
	 */
	public String getJMXServiceURL() {
		String host = getServer().getHost();
		return "service:jmx:rmi://" + host + "/jndi/rmi://" + host + ":" + getRMINamingPort() + "/JMXConnector";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServer#getJSR88DeployerJar()
	 */
	public IPath getJSR88DeployerJar() {
		return getServer().getRuntime().getLocation().append("/lib/geronimo-deploy-jsr88-1.1.jar");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServer#getDeploymentFactory()
	 */
	public DeploymentFactory getDeploymentFactory() {
		return deploymentFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServer#configureDeploymentManager(javax.enterprise.deploy.spi.DeploymentManager)
	 */
	public void configureDeploymentManager(DeploymentManager dm) {
		((JMXDeploymentManager) dm).setLogConfiguration(true, true);
		boolean enableInPlace = SocketUtil.isLocalhost(getServer().getHost()) && isRunFromWorkspace();
		setInPlaceDeployment(dm, enableInPlace);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoServer#getVersionHandler()
	 */
	public IGeronimoVersionHandler getVersionHandler() {
		if (versionHandler == null)
			versionHandler = new GeronimoV11VersionHandler();
		return versionHandler;
	}

	public void setInPlaceDeployment(DeploymentManager dm, boolean enable) {
		((JMXDeploymentManager) dm).setInPlace(enable);
	}
	
	 public boolean isNotRedeployJSPFiles() {
	        return getAttribute(PROPERTY_NOT_REDEPLOY_JSP_FILES,false);
	}

    @Override
    public IGeronimoServerPluginManager getServerPluginManager() {
        //Currently not support server plugin for 1.1 adapter
        return null;
    }
    
    public String encrypt(String value) {
        return value == null ? null : EncryptionManager.encrypt(value);
    }
    
    public String decrypt(String value) {
        return value == null ? null : (String) EncryptionManager.decrypt(value);
    }

}