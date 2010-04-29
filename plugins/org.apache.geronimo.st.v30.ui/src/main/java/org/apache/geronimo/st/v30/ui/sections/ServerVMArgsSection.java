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

import org.apache.geronimo.st.v30.ui.commands.SetVMArgsCommand;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public class ServerVMArgsSection extends AbstractServerEditorSection {
    
    protected Text fVMArgumentsText;
    private Button fPgrmArgVariableButton;

    public ServerVMArgsSection() {
        super();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.server.ui.editor.ServerEditorSection#createSection(org.eclipse.swt.widgets.Composite)
     */
    public void createSection(Composite parent) {
        super.createSection(parent);
        FormToolkit toolkit = getFormToolkit(parent.getDisplay());
        
        Section section = toolkit.createSection(parent,
                ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
                        | ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
                        | ExpandableComposite.FOCUS_TITLE);
        section.setText(Messages.editorSectionVMArgsTitle);
        section.setDescription(Messages.editorSectionVMArgsDescription);
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
        
        fVMArgumentsText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 75;
        gd.widthHint = 100;
        fVMArgumentsText.setLayoutData(gd);
        fVMArgumentsText.setText(getArgs());
        fVMArgumentsText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                execute(new SetVMArgsCommand(server, getAttributeValueFrom(fVMArgumentsText)));
            }
        });
        
        
        fPgrmArgVariableButton = SWTFactory.createPushButton(composite, LauncherMessages.VMArgumentsBlock_4, null);
        fPgrmArgVariableButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        fPgrmArgVariableButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
                dialog.open();
                String variable = dialog.getVariableExpression();
                if (variable != null) {
                    fVMArgumentsText.insert(variable);
                }
            }
            public void widgetDefaultSelected(SelectionEvent e) {
            }
            
        });
    }
    
    protected String getAttributeValueFrom(Text text) {
        String content = text.getText().trim();
        if (content.length() > 0) {
            return content;
        }
        return null;
    }
    
    private String getArgs() {
        return gs != null && gs.getVMArgs() != null ? gs.getVMArgs() : "";
    }
}
