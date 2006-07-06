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
package org.apache.geronimo.st.ui.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.geronimo.st.core.GenericGeronimoServer;
import org.apache.geronimo.st.ui.internal.Messages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.wst.server.core.IServer;

public class LaunchGeronimoConsoleAction implements IActionDelegate {

	public static final String G_SERVER_PREFIX = "org.apache.geronimo";

	private IServer server;

	public LaunchGeronimoConsoleAction() {
		super();
	}

	public URL getConsoleUrl() throws MalformedURLException {
		if (server != null) {
			GenericGeronimoServer gs = (GenericGeronimoServer) server.getAdapter(GenericGeronimoServer.class);
			return new URL("http://" + server.getHost() + ":"
					+ gs.getHTTPPort() + "/console/");
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {

		try {
			int style = IWorkbenchBrowserSupport.AS_EDITOR
					| IWorkbenchBrowserSupport.STATUS;
			IWebBrowser browser = WorkbenchBrowserSupport.getInstance().createBrowser(style, "console", Messages.console, Messages.consoleTooltip);
			URL url = getConsoleUrl();
			if (url != null)
				browser.openURL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

		server = (IServer) ((StructuredSelection) selection).getFirstElement();

		boolean enable = server != null
				&& server.getServerType().getId().startsWith(G_SERVER_PREFIX)
				&& server.getServerState() == IServer.STATE_STARTED;

		action.setEnabled(enable);

	}

}
