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
package org.apache.geronimo.devtools;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoBuilder;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.kernel.repository.WritableListableRepository;
import org.apache.geronimo.system.configuration.RepositoryConfigurationStore;

public class EclipseAwareConfigurationStore extends RepositoryConfigurationStore {

	private static final Log log = LogFactory.getLog(EclipseAwareConfigurationStore.class);

	public EclipseAwareConfigurationStore(WritableListableRepository repository) {

		super(repository);
	}

	public EclipseAwareConfigurationStore(Kernel kernel, String objectName, WritableListableRepository repository) {
		super(kernel, objectName, repository);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.system.configuration.RepositoryConfigurationStore#resolve(org.apache.geronimo.kernel.repository.Artifact,
	 *      java.lang.String, java.lang.String)
	 */
	public Set resolve(Artifact artifact, String module, String path) {

		log.debug("--> EclipseAwareConfigurationStore.resolve()");

		Set result = Collections.EMPTY_SET;
		JMXConnector connector = null;

		try {
			
			Map env = new HashMap();
			env.put(JMXConnectorServerFactory.PROTOCOL_PROVIDER_CLASS_LOADER, this.getClass().getClassLoader());

			JMXServiceURL address = new JMXServiceURL("hessian", null, 8090, "/hessian");
			connector = JMXConnectorFactory.connect(address, env);
			MBeanServerConnection connection = connector.getMBeanServerConnection();
			ObjectName on = ObjectName.getInstance("ConfigStoreResolver:name=resolver");

			String configId = getConfigId(artifact);
			log.debug("Resolving: " + configId + " " + module + " " + path);

			result = (Set) connection.invoke(on, "resolve", new Object[] { configId, module, path }, new String[] { "java.lang.String", "java.lang.String", "java.lang.String" });

			log.debug("Resolved to: " + result);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connector != null)
				try {
					connector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
	
	private String getConfigId(Artifact artifact) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(artifact.getGroupId());
		buffer.append("/");
		buffer.append(artifact.getArtifactId());
		buffer.append("/");
		buffer.append(artifact.getVersion());
		buffer.append("/");
		buffer.append(artifact.getType());
		return buffer.toString();
	}

	public static final GBeanInfo GBEAN_INFO;

	static {
		GBeanInfoBuilder builder = GBeanInfoBuilder.createStatic(EclipseAwareConfigurationStore.class, "ConfigurationStore");
		builder.addAttribute("kernel", Kernel.class, false);
		builder.addAttribute("objectName", String.class, false);
		builder.addReference("Repository", WritableListableRepository.class, "Repository");
		builder.setConstructor(new String[] { "kernel", "objectName", "Repository" });
		GBEAN_INFO = builder.getBeanInfo();
	}

	public static GBeanInfo getGBeanInfo() {
		return GBEAN_INFO;
	}

}
