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
package org.apache.geronimo.st.v30.ui.actions;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.internal.Trace;
import org.apache.geronimo.st.v30.ui.view.KarafShellSSHTerminalView;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date: 2011-05-25 04:16:27 -0400 (Wed, 25 May 2011)
 */
public class LaunchGeronimoShellAction implements IActionDelegate {

    private IServer server;

    private String serverPrefix;

    public LaunchGeronimoShellAction() {
        IExtensionRegistry reg = Platform.getExtensionRegistry();
        IConfigurationElement[] extensions = reg
                .getConfigurationElementsFor("org.apache.geronimo.st.v30.ui.actionURLs");
        for (IConfigurationElement element : extensions) {
            Trace.trace(Trace.INFO, element.getName() + " = " + element.getValue() + ".", Activator.traceActions);
            if (element.getName().equals("server_prefix")) {
                serverPrefix = element.getValue();
                Trace.trace(Trace.INFO, "server_prefix = " + serverPrefix + ".", Activator.traceActions);
            }
        }
    }

    public void run(IAction action) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                IWorkbenchWindow activeWKBench = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                IWorkbenchPage activePage = getActivePage(activeWKBench);
                try {
                    KarafShellSSHTerminalView terminalView = (KarafShellSSHTerminalView) activePage.showView(KarafShellSSHTerminalView.VIEW_ID);
                    GeronimoServerDelegate serverDelegate = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
                    terminalView.doConnect(serverDelegate);
                } catch (Exception e) {
                    Trace.trace(Trace.ERROR, "Error starting Geronimo shell terminal", e, Activator.logCommands);
                }
            }
        });
    }

    private IWorkbenchPage getActivePage(IWorkbenchWindow activeWKBench) {
        IWorkbenchPage activePage = activeWKBench.getActivePage();
        try {
            while (activePage == null) {
                Thread.sleep(100);
                activePage = activeWKBench.getActivePage();
            }
        } catch (InterruptedException e) {
            // ignore
        }
        return activePage;
    }

    public void selectionChanged(IAction action, ISelection selection) {
        server = (IServer) ((StructuredSelection) selection).getFirstElement();
        boolean enable = server != null
                && server.getServerType().getId().startsWith(serverPrefix)
                && (server.getServerState() == IServer.STATE_STARTED || server.getServerState() == IServer.STATE_STARTING)
                && isShellEnabled(server);
        action.setEnabled(enable);
    }

    private boolean isShellEnabled(IServer server) {
        ILaunch launch = server.getLaunch();
        if (launch != null) {
            IProcess[] processes = launch.getProcesses();
            if (processes != null && processes.length > 0) {
                GeronimoServerDelegate delegate = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
                return delegate.isKarafShell();
            }
        }
        return true;
    }
}
