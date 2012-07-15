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

import org.apache.geronimo.st.v30.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.v30.core.base.Bundle;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.ObjectPluginAction;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.internal.Server;
import org.eclipse.wst.server.ui.internal.view.servers.ModuleServer;

@SuppressWarnings("restriction")
public class ChangeBundleStartLevelAction implements IObjectActionDelegate {
    private Server server;
    private IModule[] module;

    @Override
    public void run(IAction arg0) {
        ObjectPluginAction action = (ObjectPluginAction) arg0;
        TreeSelection selection = (TreeSelection) action.getSelection();
        ModuleServer ms = (ModuleServer) selection.getFirstElement();
        module = ms.getModule();
        server = (Server) ms.getServer();

        Bundle bundle = getBundle();
        InputDialog diag = new ChangeStartLevelDiag(null, Messages.changeOSGIBundleStartLevel,
                Messages.changeOSGIBundleStartLevelDesc, bundle.getStartLevel() + "", null);
        diag.open();

    }

    @Override
    public void selectionChanged(IAction arg0, ISelection arg1) {
    }

    @Override
    public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
    }

    private class ChangeStartLevelDiag extends InputDialog {

        public ChangeStartLevelDiag(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue,
                IInputValidator validator) {
            super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
        }

        @Override
        protected void cancelPressed() {
            super.cancelPressed();
        }

        @Override
        protected void okPressed() {
            String value = this.getValue();
            try {
                int level = Integer.parseInt(value);
                Bundle bundle = getBundle();
                bundle.setStartLevel(level);
                super.okPressed();

            } catch (Exception e) {
                this.setErrorMessage(Messages.changeOSGIBundleStartLevelDescOnError);
            }

            publish(module);
        }

        private void publish(IModule[] module) {
            GeronimoServerBehaviourDelegate delegate = (GeronimoServerBehaviourDelegate) server
                    .getAdapter(GeronimoServerBehaviourDelegate.class);
            try {
                delegate.getOsgiModuleHandler().doChanged(module[0], null);
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logActions);
            }
        }

    }

    private Bundle getBundle() throws RuntimeException {
        Bundle bundle = null;
        GeronimoServerBehaviourDelegate delegate = (GeronimoServerBehaviourDelegate) server
                .getAdapter(GeronimoServerBehaviourDelegate.class);
        try {
            bundle = delegate.getOsgiModuleHandler().getBundleInfo(module[0]);
            if (bundle == null)
                throw new NullPointerException("the bundle is inexisted");

            return bundle;
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logActions);
            throw new RuntimeException(e);
        }
    }

}
