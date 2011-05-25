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
package org.apache.geronimo.st.v21.ui.sections;

import org.apache.geronimo.st.core.operations.IGeronimoServerPluginManager;
import org.apache.geronimo.st.v21.core.internal.Trace;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractServerEditorSection;
import org.apache.geronimo.st.v21.core.operations.GeronimoServerV21PluginManager;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.ServerCustomAssemblyWizard;
import org.apache.geronimo.st.v21.ui.wizards.ServerPluginManagerDialog;
import org.apache.geronimo.st.v21.ui.wizards.ServerPluginManagerWizard;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
public class ServerPluginSection extends AbstractServerEditorSection {
    
    private Button customAssemblyButton;
    private Button pluginManagerButton;

    public ServerPluginSection() {
        super();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.server.ui.editor.ServerEditorSection#createSection(org.eclipse.swt.widgets.Composite)
     */
    public void createSection(Composite parent) {
        super.createSection(parent);
    
        Trace.tracePoint("Entry", Activator.traceSections, "ServerPluginSection.createSection", parent);
    
        FormToolkit toolkit = getFormToolkit(parent.getDisplay());
        
        Section section = toolkit.createSection(parent,
                ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
                        | ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
                        | ExpandableComposite.FOCUS_TITLE);
        section.setText(CommonMessages.plugin);
        section.setDescription(CommonMessages.pluginActions);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);

        customAssemblyButton = toolkit.createButton(composite, CommonMessages.createCustomAssembly, SWT.PUSH);
        customAssemblyButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        customAssemblyButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                // if the server is started, then we can bring up the dialog
                if (isServerRunning()) {
                	IGeronimoServerPluginManager pluginManager = gs.getServerPluginManager();
                    ServerCustomAssemblyWizard wizard = new ServerCustomAssemblyWizard (pluginManager);
                    WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                    dialog.open();
                    if (dialog.getReturnCode() == Dialog.OK) {
                    }
                } else {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), CommonMessages.errorOpenWizard, CommonMessages.serverNotStarted);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        pluginManagerButton = toolkit.createButton(composite, CommonMessages.convertAppsToPlugins, SWT.PUSH);
        pluginManagerButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        pluginManagerButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                // if the server is started, then we can bring up the dialog
                if (isServerRunning()) {
                    GeronimoServerV21PluginManager pluginManager = new GeronimoServerV21PluginManager (gs.getServer());
                    ServerPluginManagerWizard wizard = new ServerPluginManagerWizard (pluginManager);
                    ServerPluginManagerDialog dialog = new ServerPluginManagerDialog(Display.getCurrent().getActiveShell(), wizard);
                    dialog.open();
                    if (dialog.getReturnCode() == Dialog.OK) {
                    }
                } else {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), CommonMessages.errorOpenWizard, CommonMessages.serverNotStarted);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    
        Trace.tracePoint("Exit", Activator.traceSections, "ServerPluginSection.createSection");
    }

    private boolean isServerRunning () {
        if (gs == null || gs.getServer() == null)
            return false;
        
        return gs.getServer().getServerState() == IServer.STATE_STARTED;
    }
}
