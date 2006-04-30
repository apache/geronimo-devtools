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
package org.apache.geronimo.st.v11.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.geronimo.kernel.config.IOUtil;
import org.apache.geronimo.st.jmxagent.Activator;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.server.core.IServer;

import org.eclipse.wst.server.core.IServerLifecycleListener;
import org.eclipse.wst.server.core.util.SocketUtil;

public class ServerLifeCycleListener implements IServerLifecycleListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.IServerLifecycleListener#serverAdded(org.eclipse.wst.server.core.IServer)
	 */
	public void serverAdded(IServer server) {
		Trace.trace(Trace.INFO, "--> ServerLifeCycleListener.serverAdded()");
		copyJars(server);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.IServerLifecycleListener#serverChanged(org.eclipse.wst.server.core.IServer)
	 */
	public void serverChanged(IServer server) {
		Trace.trace(Trace.INFO, "--> ServerLifeCycleListener.serverChanged()");
		copyJars(server);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.core.IServerLifecycleListener#serverRemoved(org.eclipse.wst.server.core.IServer)
	 */
	public void serverRemoved(IServer server) {

	}

	private void copyToSharedLib(IServer server, String path) {
		IPath sharedLib = server.getRuntime().getLocation().append(
				new Path("/var/shared"));

		IPath destFile = sharedLib.append(path);

		if (!destFile.toFile().exists()) {
			try {
				URL url = Platform.resolve(Activator.getDefault().getBundle()
						.getEntry(path));
				Trace.trace(Trace.INFO, "copying " + path + " to shared lib");
				IOUtil.copyFile(new File(url.getFile()), destFile.toFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isSupportedServer(IServer server) {
		return "1.1".equals(server.getServerType().getRuntimeType()
				.getVersion())
				&& SocketUtil.isLocalhost(server.getHost());
	}

	private void copyJars(IServer server) {
		if (isSupportedServer(server)) {
			copyToSharedLib(server, "lib/hessian-3.0.8.jar");
			copyToSharedLib(server, "lib/mx4j-tools-3.0.1.jar");
		}
	}
}
