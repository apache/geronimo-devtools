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

import java.util.TimerTask;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.Server;

public class UpdateServerStateTask extends TimerTask {

	private IGeronimoServerBehavior delegate;

	private IServer server;

	public UpdateServerStateTask(IGeronimoServerBehavior delegate,
			IServer server) {
		super();
		this.delegate = delegate;
		this.server = server;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void run() {
		synchronized (delegate) {
			Trace.trace(Trace.INFO, "--> UpdateServerStateTask.run() "
					+ server.getName());

			// Only start the task if the connection URL is unqiue for all
			// g-servers or if other servers that are different instances but do
			// share the same connection URL's are stopped
			IGeronimoServer thisServer = (IGeronimoServer) this.server
					.loadAdapter(IGeronimoServer.class, null);

			IServer[] allServers = ServerCore.getServers();
			boolean allUnique = true;
			boolean allNonUniqueStopped = true;
			for (int i = 0; i < allServers.length; i++) {
				IServer server = allServers[i];
				IGeronimoServer gs = (IGeronimoServer) server.loadAdapter(
						IGeronimoServer.class, null);
				if (gs != null && !this.server.getId().equals(server.getId())) {
					if (gs.getJMXServiceURL().equals(
							thisServer.getJMXServiceURL())) {
						allUnique = false;
						if (!server.getRuntime().getLocation().equals(
								this.server.getRuntime().getLocation())
								&& server.getServerState() != IServer.STATE_STOPPED) {
							allNonUniqueStopped = false;
						}
					}
				}
			}

			Trace.trace(Trace.INFO, "allUnique = " + allUnique
					+ ", allNonUniqueStopped = " + allNonUniqueStopped);

			if (allUnique || allNonUniqueStopped) {
				Trace.trace(Trace.INFO, "updating state...");
				updateServerState();
			}

			Trace.trace(Trace.INFO, "<-- UpdateServerStateTask.run() "
					+ server.getName());
		}
	}

	private void updateServerState() {
		try {
			switch (server.getServerState()) {
			case IServer.STATE_STOPPED:
				if (delegate.isKernelAlive())
					updateFromStopped();
				break;
			case IServer.STATE_STARTING:
				if (delegate.isKernelAlive())
					updateFromStarting();
				else
					delegate.setServerStopped();
				break;
			case IServer.STATE_STARTED:
				updateFromStarted();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateFromStopped() {
		if (isFullyStarted()) {
			delegate.setServerStarted();
		} else {
			((Server) server).setServerState(IServer.STATE_STARTING);
		}
	}

	private void updateFromStarting() {
		if (isFullyStarted())
			delegate.setServerStarted();
	}

	private void updateFromStarted() {
		if (!isFullyStarted())
			delegate.setServerStopped();
	}

	private boolean isFullyStarted() {
		ClassLoader old = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					((GenericGeronimoServerBehaviour) delegate)
							.getContextClassLoader());
			return delegate.isFullyStarted();
		} finally {
			Thread.currentThread().setContextClassLoader(old);
		}
	}
}
