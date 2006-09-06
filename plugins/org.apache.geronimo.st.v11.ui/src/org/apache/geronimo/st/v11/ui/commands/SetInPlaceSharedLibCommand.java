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
package org.apache.geronimo.st.v11.ui.commands;

import org.apache.geronimo.st.ui.commands.ServerCommand;
import org.apache.geronimo.st.v11.core.GeronimoServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;

public class SetInPlaceSharedLibCommand extends ServerCommand {

	boolean value;

	boolean oldValue;

	/**
	 * @param server
	 * @param name
	 */
	public SetInPlaceSharedLibCommand(IServerWorkingCopy server, boolean value) {
		super(server, "SetInPlaceSharedLibCommand");
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.ui.commands.ServerCommand#execute()
	 */
	public void execute() {
		GeronimoServer gs = (GeronimoServer) server.getAdapter(GeronimoServer.class);
		oldValue = gs.isInPlaceSharedLib();
		gs.setInPlaceSharedLib(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.ui.commands.ServerCommand#undo()
	 */
	public void undo() {
		GeronimoServer gs = (GeronimoServer) server.getAdapter(GeronimoServer.class);
		gs.setInPlaceSharedLib(oldValue);
	}

}
