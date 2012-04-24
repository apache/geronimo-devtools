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
package org.apache.geronimo.st.v30.ui.sections;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v30.ui.commands.SetKarafShellCommand;
import org.apache.geronimo.st.v30.ui.commands.SetKarafShellKeepAliveCommand;
import org.apache.geronimo.st.v30.ui.commands.SetKarafShellPortCommand;
import org.apache.geronimo.st.v30.ui.commands.SetKarafShellTimeoutCommand;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
public class ServerEditorKarafShellSection extends AbstractServerEditorSection {
    Button enable;
    
    Text timeout;
    Text keepAlive;
    Text port;
        
    public ServerEditorKarafShellSection() {
        super();
    }

    public void createSection(Composite parent) {
        super.createSection(parent);

        FormToolkit toolkit = getFormToolkit(parent.getDisplay());

        Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE
                | ExpandableComposite.EXPANDED
                | ExpandableComposite.TITLE_BAR
                | Section.DESCRIPTION | ExpandableComposite.FOCUS_TITLE);

        section.setText(Messages.editorSectionKarafShellTitle);
        section.setDescription(Messages.editorSectionKarafShellDescription);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);
        
        Composite subComp0 = toolkit.createComposite(composite);
        layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        subComp0.setLayout(layout);
        subComp0.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        Composite subComp1 = toolkit.createComposite(composite);
        layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        subComp1.setLayout(layout);
        subComp1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        enable = toolkit.createButton(subComp0, Messages.enableKarafShell, SWT.CHECK);

        final GeronimoServerDelegate gsdCopy = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        
        boolean karafShell = gsdCopy.isKarafShell();
        enable.setSelection(karafShell);

        enable.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                int serverState = server.getOriginal().getServerState();
                if(serverState != IServer.STATE_STOPPED && serverState != IServer.STATE_STOPPING) {
                    if(MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), Messages.karafShellChangeEnableProblem, 
                            Messages.karafShellMustChangeBeforeServerStart + "\r\n\r\n" + Messages.wantToContinue)) {
                        executeAndEnableWidgets();
                    } else {
                        enable.setSelection(! enable.getSelection());
                    }
                } else {
                    executeAndEnableWidgets();
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
            
            private void executeAndEnableWidgets() {
                execute(new SetKarafShellCommand(server, enable.getSelection()));
                timeout.setEnabled(enable.getSelection());
                keepAlive.setEnabled(enable.getSelection());
                port.setEnabled(enable.getSelection());
            }

        });
        // create timeout field
        createLabel(subComp1, Messages.karafShellTimeout, toolkit);
        timeout = toolkit.createText(subComp1, Integer.toString(gsdCopy.getKarafShellTimeout()), SWT.BORDER);
        timeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        timeout.setToolTipText(Messages.karafShellTimeout);
        timeout.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Integer value = Integer.valueOf(timeout.getText());
                execute(new SetKarafShellTimeoutCommand(server, timeout, value));
            }
        });
        // create keep alive field
        createLabel(subComp1, Messages.karafShellkeepAlive, toolkit);
        keepAlive = toolkit.createText(subComp1, Integer.toString(gsdCopy.getKarafShellKeepAlive()), SWT.BORDER);
        keepAlive.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        keepAlive.setToolTipText(Messages.karafShellkeepAlive);
        keepAlive.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Integer value = Integer.valueOf(keepAlive.getText());
                execute(new SetKarafShellKeepAliveCommand(server, keepAlive, value));
            }
        });
        // create port field
        createLabel(subComp1, Messages.karafShellPort, toolkit);
        port = toolkit.createText(subComp1, Integer.toString(gsdCopy.getKarafShellPort()), SWT.BORDER);
        port.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        port.setToolTipText(Messages.karafShellPort);
        port.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Integer value = Integer.valueOf(port.getText());
                execute(new SetKarafShellPortCommand(server, port, value));
            }
        });
    }
}
