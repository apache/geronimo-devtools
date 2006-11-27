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

import java.util.TimerTask;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.internal.Server;

/**
 * @version $Rev$ $Date$
 */
public class UpdateServerStateTask extends TimerTask {

	private IGeronimoServerBehavior delegate;

	private IServer server;

	public UpdateServerStateTask(IGeronimoServerBehavior delegate, IServer server) {
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
		if (canUpdateState()) {
			updateServerState();
		}
	}

	/**
	 * Returns true if either the connection URL is unique for all g-servers or
	 * if all other servers that share the same connection URL but have
	 * different runtime locations are stopped.
	 * 
	 * @return true if ok to update state
	 */
	private boolean canUpdateState() {
		IGeronimoServer thisServer = (IGeronimoServer) this.server.loadAdapter(IGeronimoServer.class, null);
		IServer[] allServers = ServerCore.getServers();
		for (int i = 0; i < allServers.length; i++) {
			IServer server = allServers[i];
			IGeronimoServer gs = (IGeronimoServer) server.loadAdapter(IGeronimoServer.class, null);
			if (gs != null && !this.server.getId().equals(server.getId())) {
				if (isSameConnectionURL(gs, thisServer)) {
					if (!isSameRuntimeLocation(server) && server.getServerState() != IServer.STATE_STOPPED) {
						Trace.trace(Trace.WARNING, server.getId() + " Cannot update server state.  URL conflict between multiple servers.");
						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean isSameRuntimeLocation(IServer server) {
		return server.getRuntime().getLocation().equals(this.server.getRuntime().getLocation());
	}

	private boolean isSameConnectionURL(IGeronimoServer server, IGeronimoServer thisServer) {
		return server.getJMXServiceURL().equals(thisServer.getJMXServiceURL());
	}

	private void updateServerState() {
		Trace.trace(Trace.INFO, ">> " + server.getId() + " Updating Server State.");
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
		Trace.trace(Trace.INFO, "<< " + server.getId() + " Updating Server State.");
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
			Thread.currentThread().setContextClassLoader(((GeronimoServerBehaviourDelegate) delegate).getContextClassLoader());
			return delegate.isFullyStarted();
		} finally {
			Thread.currentThread().setContextClassLoader(old);
		}
	}
}
