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
package org.apache.geronimo.st.ui.commands;

import org.apache.geronimo.st.core.GeronimoServerDelegate;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServerWorkingCopy;

/**
 * @version $Rev$ $Date$
 */
public class SetConsoleLogLevelCommand extends ServerCommand {

	String value;

	String oldValue;

	/**
	 * @param server
	 * @param name
	 */
	public SetConsoleLogLevelCommand(IServerWorkingCopy server, String value) {
		super(server, "SetConsoleLogLevelCommand");
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.internal.command.ServerCommand#execute()
	 */
	public void execute() {
		GeronimoServerDelegate gs = getGeronimoServer();
		oldValue = gs.getConsoleLogLevel();
		gs.setConsoleLogLevel(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.internal.command.ServerCommand#undo()
	 */
	public void undo() {
		getGeronimoServer().setConsoleLogLevel(oldValue);
	}

	private GeronimoServerDelegate getGeronimoServer() {
		GeronimoServerDelegate gs = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
		if (gs == null) {
			gs = (GeronimoServerDelegate) server.loadAdapter(GeronimoServerDelegate.class, new NullProgressMonitor());
		}
		return gs;
	}
}
