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
package org.apache.geronimo.st.ui.actions;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.internal.Trace;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date: 2006-11-05 17:47:11 -0500 (Sun, 05 Nov 2006) $
 */
public class LaunchGeronimoSupportAction implements IActionDelegate {
    
    private String serverPrefix;
    
    private String supportURL;
    
    private IServer server;
    
    public LaunchGeronimoSupportAction() {
        super();
        IExtensionRegistry reg = Platform.getExtensionRegistry();
        IConfigurationElement[] extensions = reg
                .getConfigurationElementsFor("org.apache.geronimo.st.ui.actionURLs");
        for (IConfigurationElement element : extensions) {
            Trace.trace(Trace.INFO, element.getName() + " = "
                    + element.getValue() + ".", Activator.traceActions);
            if (element.getName().equals("server_prefix")) {
                serverPrefix = element.getValue();
                Trace
                        .trace(Trace.INFO, "server_prefix = " + serverPrefix
                                + ".", Activator.traceActions);
            } else if (element.getName().equals("action_URL")
                    && element
                            .getAttribute("class")
                            .equals(
                                    "org.apache.geronimo.st.ui.actions.LaunchGeronimoSupportAction")) {
                supportURL = element.getAttribute("URL");
                Trace.trace(Trace.INFO, "support URL = " + supportURL + ".", Activator.traceActions);
            }
        }
    }

    public URL getConsoleUrl() throws MalformedURLException {
        if (server != null) {
            return new URL(supportURL);
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
            int style = IWorkbenchBrowserSupport.AS_EXTERNAL
                    | IWorkbenchBrowserSupport.STATUS;
            IWebBrowser browser = WorkbenchBrowserSupport.getInstance()
                    .createBrowser(
                            style,
                            "supportWebPage",
                            supportURL,
                            Messages.bind(Messages.supportWebPageTooltip, server
                                    .getName()));
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
                         && server.getServerType().getId().startsWith(serverPrefix);

        action.setEnabled(enable);

    }

}
