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
package org.apache.geronimo.st.v21.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.geronimo.st.v21.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServerInfoManager {
	
	private static Map<String, IGeronimoServerInfo> providers = new HashMap<String, IGeronimoServerInfo>();

	static {
		loadExtensionPoints();
	}

	private static synchronized void loadExtensionPoints() {
		Trace.tracePoint("ENTRY", Activator.traceCore, "GeronimoServerInfo.loadExtensionPoints");

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] cf = registry.getConfigurationElementsFor(
				Activator.PLUGIN_ID, "geronimoinfo");
		for (int i = 0; i < cf.length; i++) {
			IConfigurationElement element = cf[i];
			if ("provider".equals(element.getName())) {
				try {
					IGeronimoServerInfo provider = (IGeronimoServerInfo) element
							.createExecutableExtension("class");
					String versions = element.getAttribute("versions");
					String[] versionArray = versions.split(",");
					for (int j = 0; j < versionArray.length; j++) {
						providers.put(versionArray[j], provider);
					}
				} catch (CoreException e) {
					Trace.tracePoint("CoreException",
							"GeronimoServerInfo.loadExtensionPoints", Activator.logCore);
					e.printStackTrace();
				}
			}
		}

		Trace.tracePoint("EXIT", Activator.traceCore, "GeronimoServerInfo.loadExtensionPoints");
	}
	 

    public static IGeronimoServerInfo getProvider(String version) {
        return providers.get(version);
    }

  

}
