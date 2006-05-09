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
package org.apache.geronimo.st.jmxagent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

public class JMXAgent {

	private static JMXAgent INSTANCE = null;

	private static MBeanServer server = null;

	private static JMXServiceURL url = null;

	private JMXConnectorServer connectorServer = null;

	private static int DEFAULT_PORT = 8090;

	private JMXAgent() {

	}

	public static JMXAgent getInstance() {
		if (INSTANCE == null)
			INSTANCE = new JMXAgent();
		return INSTANCE;
	}

	private void init() {
		try {
			url = new JMXServiceURL("hessian", null, DEFAULT_PORT, "/hessian");
			loadBean(ConfigurationStoreResolver.class.getName(), "ConfigStoreResolver:name=resolver");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 
	}

	public void start() throws IOException {
		if (connectorServer == null) {
			init();
			Map env = new HashMap();
			env.put(JMXConnectorServerFactory.PROTOCOL_PROVIDER_CLASS_LOADER, this.getClass().getClassLoader());
			connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, env, getServer());
		}

		if (!connectorServer.isActive()) {
			connectorServer.start();
		}
	}

	public void stop() throws IOException {
		if (connectorServer != null)
			connectorServer.stop();
	}

	public void loadBean(String className, String objectName) {
		try {
			getServer().registerMBean(new ConfigurationStoreResolver(), new ObjectName(objectName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MBeanServer getServer() {
		if (server == null)
			server = MBeanServerFactory.createMBeanServer();
		return server;
	}
}
