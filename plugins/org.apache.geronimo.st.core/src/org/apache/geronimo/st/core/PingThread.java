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

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.util.SocketUtil;

public class PingThread extends Thread {

	private static final int PING_DELAY = 10000;

	private static final int PING_INTERVAL = 5000;

	private static final int MAX_PINGS = 40;

	private IGeronimoServerBehavior geronimoServer;

	private IServer server;

	public PingThread(IGeronimoServerBehavior geronimoServer, IServer server) {
		super();
		this.geronimoServer = geronimoServer;
		this.server = server;
		this.setDaemon(true);
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
			sleep(PING_DELAY);
		} catch (InterruptedException e) {
			// ignore
		}

		for (int tries = MAX_PINGS; tries > 0; tries--) {
			
			if(server.getServerState() == IServer.STATE_STOPPED) 
				interrupt();

			ClassLoader old = Thread.currentThread().getContextClassLoader();
			try {
				ClassLoader cl = ((GenericGeronimoServerBehaviour) geronimoServer).getContextClassLoader();
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
				sleep(PING_INTERVAL);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		Trace.trace(Trace.SEVERE, "Ping: Can't ping for server startup.");
		server.stop(false);
	}
}
