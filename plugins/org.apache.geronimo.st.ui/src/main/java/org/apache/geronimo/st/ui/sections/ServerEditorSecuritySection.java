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
package org.apache.geronimo.st.ui.sections;

import org.apache.geronimo.st.core.IGeronimoServer;
import org.apache.geronimo.st.core.operations.GeronimoAccountManager;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.commands.SetPasswordCommand;
import org.apache.geronimo.st.ui.commands.SetUsernameCommand;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.internal.Trace;
import org.apache.geronimo.st.ui.wizards.ManageAccountWizard;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class ServerEditorSecuritySection extends AbstractServerEditorSection {

	Text username;

	Text password;
	
	Button manageAccountButton;

	public ServerEditorSecuritySection() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.editor.ServerEditorSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	public void createSection(Composite parent) {
		super.createSection(parent);

		FormToolkit toolkit = getFormToolkit(parent.getDisplay());

		Section section = toolkit.createSection(parent,
				ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
						| ExpandableComposite.TITLE_BAR 
						| ExpandableComposite.FOCUS_TITLE);

		section.setText(Messages.editorSectionSecurityTitle);
	//	section.setDescription(Messages.editorSectionSecurityDescription);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Composite textComposite = toolkit.createComposite(section);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        textComposite.setLayout(gridLayout);
        textComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setDescriptionControl(textComposite);
        //text
        Label label = toolkit.createLabel(textComposite, Messages.editorSectionSecurityDescription);
        
		// Button for managing account
        Button manageAccountButton = toolkit.createButton(textComposite, CommonMessages.manageAccount, SWT.PUSH);
        GridData buttonData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        manageAccountButton.setLayoutData(buttonData);
        manageAccountButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                // if the server is started and local host, then we can bring up the dialog
                if (!isLocalHost())
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), CommonMessages.errorOpenWizard, CommonMessages.isNotLocalHost);
                else 
                {
                    GeronimoAccountManager manager = new GeronimoAccountManager(server.getRuntime());
                    try {
                        manager.init();
                    } catch (Exception e1) {
                        MessageDialog.openError(Display.getCurrent().getActiveShell(), CommonMessages.errorOpenWizard, CommonMessages.cannotRead);
                        Trace.trace(Trace.SEVERE, "Properties file containing user information can't be read!", e1);
                        return;
                    }
                    ManageAccountWizard wizard = new ManageAccountWizard(manager);
                    WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                    dialog.open();
                    if (dialog.getReturnCode() == Dialog.OK) {
                    }
                } 
            }

        });
		
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

		// ------- Label and text field for the username -------
		createLabel(composite, Messages.username, toolkit);
		username = toolkit.createText(composite, getUserName(), SWT.BORDER);
		username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		username.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				execute(new SetUsernameCommand(server, username.getText()));
			}
		});

		// ------- Label and text field for the password -------
		createLabel(composite, Messages.password, toolkit);

		password = toolkit.createText(composite, getPassword(), SWT.BORDER | SWT.PASSWORD);
		password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		password.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				execute(new SetPasswordCommand(server, password.getText()));
			}
		});
	}

	private String getUserName() {
		if (gs != null) {
			return gs.getAdminID();
		}
		return "";
	}

	private String getPassword() {
		if (gs != null) {
			return gs.getAdminPassword();
		}
		return "";
	}
	
	private boolean isLocalHost(){
	    return !(server.getServerType().supportsRemoteHosts()
                && !SocketUtil.isLocalhost(server.getHost()));
	}
	
    private boolean isServerRunning () {
        if (gs == null || gs.getServer() == null)
            return false;
        
        return gs.getServer().getServerState() == IServer.STATE_STARTED;
    }
}
