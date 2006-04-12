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

import java.net.URL;

import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.jst.server.generic.core.internal.GenericServer;
import org.eclipse.jst.server.generic.core.internal.Trace;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.internal.ServerMonitorManager;

public abstract class GenericGeronimoServer extends GenericServer implements
		IGeronimoServer {

	public static final String PROPERTY_ADMIN_ID = "adminID";

	public static final String PROPERTY_ADMIN_PW = "adminPassword";

	public static final String PROPERTY_RMI_PORT = "rmiport";

	public static final String PROPERTY_HTTP_PORT = "port";

	public abstract String getContextRoot(IModule module);

	public URL getModuleRootURL(IModule module) {
		try {
			if (module == null
					|| module.loadAdapter(IWebModule.class, null) == null)
				return null;

			String host = getServer().getHost();
			String url = "http://" + host;
			int port = 0;

			port = getHttpPort();
			port = ServerMonitorManager.getInstance().getMonitoredPort(getServer(), port, "web");
			if (port != 80)
				url += ":" + port;

			String moduleId = getContextRoot(module);
			if (!moduleId.startsWith("/"))
				url += "/";
			url += moduleId;

			if (!url.endsWith("/"))
				url += "/";

			return new URL(url);
		} catch (Exception e) {
			Trace.trace("Could not get root URL", e);
			return null;
		}

	}

	public String getAdminID() {
		return (String) getServerInstanceProperties().get(PROPERTY_ADMIN_ID);
	}

	public String getAdminPassword() {
		return (String) getServerInstanceProperties().get(PROPERTY_ADMIN_PW);
	}

	public String getRMINamingPort() {
		return (String) getServerInstanceProperties().get(PROPERTY_RMI_PORT);
	}

	public String getHTTPPort() {
		return (String) getServerInstanceProperties().get(PROPERTY_HTTP_PORT);
	}

	public void setAdminID(String value) {
		getServerInstanceProperties().put(PROPERTY_ADMIN_ID, value);
	}

	public void setAdminPassword(String value) {
		getServerInstanceProperties().put(PROPERTY_ADMIN_PW, value);
	}

	public void setRMINamingPort(String value) {
		getServerInstanceProperties().put(PROPERTY_RMI_PORT, value);
	}

	public void setHTTPPort(String value) {
		getServerInstanceProperties().put(PROPERTY_HTTP_PORT, value);
	}

}
