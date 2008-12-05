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
package org.apache.geronimo.st.v21.ui.actions;

import org.apache.geronimo.st.v21.core.operations.GeronimoServerPluginManager;
import org.apache.geronimo.st.ui.internal.Trace;
import org.apache.geronimo.st.v21.ui.wizards.ServerCustomAssemblyWizard;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionDelegate;

/**
 * @version $Rev$ $Date$
 */
public class LaunchGeronimoServerAssemblyAction implements IActionDelegate {

    private GeronimoServerPluginManager customAssembly;

    private String serverPrefix;

    public LaunchGeronimoServerAssemblyAction() {
        super();
        IExtensionRegistry reg = Platform.getExtensionRegistry();
        IConfigurationElement[] extensions = reg
                .getConfigurationElementsFor("org.apache.geronimo.st.ui.actionURLs");
        for (IConfigurationElement element : extensions) {
            Trace.trace(Trace.INFO, element.getName() + " = "
                    + element.getValue() + ".");
            if (element.getName().equals("server_prefix")) {
                serverPrefix = element.getValue();
                Trace
                        .trace(Trace.INFO, "server_prefix = " + serverPrefix
                                + ".");
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {

        // bring up new dialog
        ServerCustomAssemblyWizard wizard = new ServerCustomAssemblyWizard (customAssembly);
        WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
        dialog.open();
        if (dialog.getReturnCode() == Dialog.OK) {

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {

        customAssembly = new GeronimoServerPluginManager();
        boolean enable = customAssembly.serverChanged (((StructuredSelection) selection).getFirstElement(), serverPrefix);

        action.setEnabled(enable);
    }
}
