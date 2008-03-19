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

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class PingThread extends Thread {

	private int pingDelay;

	private int pingInterval;

	private int maxPings;

	private IGeronimoServerBehavior geronimoServer;

	private IServer server;

	public PingThread(IGeronimoServerBehavior geronimoServer, IServer server) {
		super();
		this.geronimoServer = geronimoServer;
		this.server = server;
		this.setDaemon(true);
		loadPingDelay();
		loadPingInterval();
		loadMaxPings();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		if (!SocketUtil.isLocalhost(server.getHost())) {
			return;
		}

		try {
			sleep(pingDelay);
		} catch (InterruptedException e) {
			// ignore
		}

		for (int tries = maxPings; tries > 0; tries--) {
			
			if(server.getServerState() == IServer.STATE_STOPPED || server.getServerState() == IServer.STATE_STOPPING) {
				return;
			}

			ClassLoader old = Thread.currentThread().getContextClassLoader();
			try {
				ClassLoader cl = ((GeronimoServerBehaviourDelegate) geronimoServer).getContextClassLoader();
				Thread.currentThread().setContextClassLoader(cl);
				if (geronimoServer.isFullyStarted()) {
					Trace.trace(Trace.INFO, "Ping: success");
					geronimoServer.setServerStarted();
					return;
				}
			} finally {
				Thread.currentThread().setContextClassLoader(old);
			}

			Trace.trace(Trace.INFO, "Ping: fail");

			try {
				sleep(pingInterval);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		Trace.trace(Trace.SEVERE, "Ping: Can't ping for server startup.");
		server.stop(false);
	}
	
	public void loadPingDelay() {
		pingDelay = getGeronimoServer().getPingDelay();
	}
	
	public void loadPingInterval() {
		pingInterval = getGeronimoServer().getPingInterval();
	}
	
	public void loadMaxPings() {
		maxPings = getGeronimoServer().getMaxPings();
	}
	
	private IGeronimoServer getGeronimoServer() {
		IGeronimoServer gServer = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
		if(gServer == null) {
			gServer = (IGeronimoServer) server.loadAdapter(IGeronimoServer.class, null);
		}
		return gServer;
	}
}
