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
package org.apache.geronimo.ui.commands;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.internal.Server;
import org.eclipse.wst.server.ui.internal.command.ServerCommand;

public class SetConsoleLogLevelCommand extends ServerCommand {

	public static final String NONE = "--long";

	public static final String INFO = "-v";

	public static final String DEBUG = "-vv";

	protected String value;

	protected String oldValue;

	private ILaunchConfigurationWorkingCopy wc = null;

	/**
	 * @param server
	 * @param name
	 */
	public SetConsoleLogLevelCommand(IServerWorkingCopy server, String value) {
		super(server, value);
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.internal.command.ServerCommand#execute()
	 */
	public void execute() {
		try {
			oldValue = getCurrentValue();
			if (oldValue != value) {
				getLaunchConfiguration()
						.setAttribute(
								IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
								value);
				getLaunchConfiguration().doSave();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentValue() throws CoreException {
		return getLaunchConfiguration().getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
				NONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.internal.command.ServerCommand#undo()
	 */
	public void undo() {
		try {
			getLaunchConfiguration().setAttribute(
					IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
					oldValue);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private ILaunchConfigurationWorkingCopy getLaunchConfiguration()
			throws CoreException {
		if (wc == null) {
			Server s = (Server) server.getAdapter(Server.class);
			ILaunchConfiguration launchConfig = s.getLaunchConfiguration(true,
					null);
			wc = launchConfig.getWorkingCopy();
		}
		return wc;
	}

}
