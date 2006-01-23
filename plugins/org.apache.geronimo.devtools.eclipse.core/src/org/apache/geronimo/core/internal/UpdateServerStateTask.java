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
package org.apache.geronimo.core.internal;

import java.util.TimerTask;

import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.internal.Server;

public class UpdateServerStateTask extends TimerTask {

	GeronimoServerBehaviour delegate;

	public UpdateServerStateTask(GeronimoServerBehaviour delegate) {
		super();
		this.delegate = delegate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.TimerTask#run()
	 */
	public void run() {
		synchronized (delegate) {
			Trace.trace(Trace.INFO, "--> UpdateServerStateTask.run()");
			int currentState = delegate.getServer().getServerState();
			Server server = (Server) delegate.getServer();
			try {
				switch (currentState) {
				case IServer.STATE_STOPPED:
					if (delegate.isKernelAlive())
						updateFromStopped(server);
					break;
				case IServer.STATE_STARTING:
					if (delegate.isKernelAlive())
						updateFromStarting(server);
					else {
						server.setServerState(IServer.STATE_STOPPED);
					}
					break;
				case IServer.STATE_STARTED:
					updateFromStarted(server);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Trace.trace(Trace.INFO, "<-- UpdateServerStateTask.run()");
		}
	}

	private void updateFromStopped(Server server) {
		if (delegate.isKernelFullyStarted()) {
			server.setServerState(IServer.STATE_STARTED);
		} else {
			server.setServerState(IServer.STATE_STARTING);
		}
	}

	private void updateFromStarting(Server server) {
		if (delegate.isKernelFullyStarted())
			server.setServerState(IServer.STATE_STARTED);
	}

	private void updateFromStarted(Server server) {
		if (!delegate.isKernelAlive()) {
			server.setServerState(IServer.STATE_STOPPED);
		}
	}

}
