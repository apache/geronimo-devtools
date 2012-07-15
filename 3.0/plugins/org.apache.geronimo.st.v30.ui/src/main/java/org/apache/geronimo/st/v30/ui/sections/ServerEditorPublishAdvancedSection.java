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
import org.apache.geronimo.st.v30.ui.commands.SetNoRedeployCommand;
import org.apache.geronimo.st.v30.ui.commands.SetNoRedeployFilePatternCommand;
import org.apache.geronimo.st.v30.ui.commands.SetPublishTimeoutCommand;
import org.apache.geronimo.st.v30.ui.commands.SetRefreshOSGiBundleCommand;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.apache.geronimo.st.v30.ui.wizards.ListEditorWizard;
import org.eclipse.jface.wizard.Wizard;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class ServerEditorPublishAdvancedSection extends AbstractServerEditorSection {

    private Spinner publishTimeout;
    private Button refreshOSGiBundle;
    private Button noRedeploy;
    private ListEditor includesEditor;
    private ListEditor excludesEditor;

    public ServerEditorPublishAdvancedSection() {
        super();
    }

    public void createSection(Composite parent) {
        super.createSection(parent);

        FormToolkit toolkit = getFormToolkit(parent.getDisplay());

        Section section = toolkit.createSection(parent,
                ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
                        | ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
                        | ExpandableComposite.FOCUS_TITLE);

        section.setText(Messages.editorSectionPublishAdvancedTitle);
        section.setDescription(Messages.editorSectionPublishAdvancedDescription);
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

        createLabel(composite, Messages.publishingTimeout, toolkit);

        publishTimeout = new Spinner(composite, SWT.BORDER);
        publishTimeout.setMinimum(0);
        publishTimeout.setIncrement(5);
        publishTimeout.setMaximum(900);
        publishTimeout.setSelection((int) gs.getPublishTimeout() / 1000);

        publishTimeout.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                execute(new SetPublishTimeoutCommand(server, publishTimeout.getSelection() * 1000));
            }
        });
                
        refreshOSGiBundle = toolkit.createButton(composite, Messages.refreshOSGiBundle, SWT.CHECK);
        refreshOSGiBundle.setSelection(gs.isRefreshOSGiBundle());
        refreshOSGiBundle.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                execute(new SetRefreshOSGiBundleCommand(server, refreshOSGiBundle.getSelection()));
            }
        });
        Label refreshOSGiLabel = toolkit.createLabel(composite, Messages.refreshOSGiBundleDescription, SWT.WRAP);
        refreshOSGiLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        GridData refreshOSGiGridData = new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 5);
        refreshOSGiGridData.widthHint = 350;
        refreshOSGiLabel.setLayoutData(refreshOSGiGridData);
        
        includesEditor = (new ListEditor(toolkit) {
            public void modified() {
                execute(new SetNoRedeployFilePatternCommand(server, true, includesEditor.getItems()));
            }
            public Wizard getWizard() {
                return (new ListEditorWizard(this) {
                    public String getWizardPageLabel() {
                        return Messages.filePattern;
                    }
                    
                    public String getAddWizardWindowTitle() {
                        return Messages.wizardNewTitle_IncludeFilePattern;
                    }

                    public String getEditWizardWindowTitle() {
                        return Messages.wizardEditTitle_IncludeFilePattern;
                    }

                    public String getWizardPageDescription() {
                        return Messages.wizardPageDescription_IncludeFilePattern;
                    }

                    public String getWizardPageTitle() {
                        return Messages.wizardPageTitle_IncludeFilePattern;
                    }                    
                });
            }
        });
        includesEditor.setTitle(Messages.includeFilePatterns);
        includesEditor.setDefaultValues(GeronimoServerDelegate.DEFAULT_NOREDEPLOY_INCLUDE_PATTERNS);
        
        excludesEditor = (new ListEditor(toolkit) {
            public void modified() {
                execute(new SetNoRedeployFilePatternCommand(server, false, excludesEditor.getItems()));
            }
            public Wizard getWizard() {
                return (new ListEditorWizard(this) {
                    public String getWizardPageLabel() {
                        return Messages.filePattern;
                    }
                    
                    public String getAddWizardWindowTitle() {
                        return Messages.wizardNewTitle_ExcludeFilePattern;
                    }
                    
                    public String getEditWizardWindowTitle() {
                        return Messages.wizardEditTitle_ExcludeFilePattern;
                    }     
                    
                    public String getWizardPageDescription() {
                        return Messages.wizardPageDescription_ExcludeFilePattern;
                    }

                    public String getWizardPageTitle() {
                        return Messages.wizardPageTitle_ExcludeFilePattern;
                    }
                });
            }
        });
        excludesEditor.setTitle(Messages.excludeFilePatterns);
        excludesEditor.setDefaultValues(GeronimoServerDelegate.DEFAULT_NOREDEPLOY_EXCLUDE_PATTERNS);
        
        noRedeploy = toolkit.createButton(composite, Messages.noRedeployOption, SWT.CHECK);
        noRedeploy.setEnabled(!(server.getServerType().supportsRemoteHosts() && !SocketUtil.isLocalhost(server.getHost())));
        noRedeploy.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                execute(new SetNoRedeployCommand(server, noRedeploy.getSelection()));                
                includesEditor.setEnabled(noRedeploy.getSelection());
                excludesEditor.setEnabled(noRedeploy.getSelection());
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }

        });
        noRedeploy.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

        Label noRedeployLabel = toolkit.createLabel(composite, Messages.noRedeployOptionDescription, SWT.WRAP);
        noRedeployLabel.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        GridData noRedeployGridData = new GridData(SWT.LEFT, SWT.CENTER, true, true, 2, 5);
        noRedeployGridData.widthHint = 350;
        noRedeployLabel.setLayoutData(noRedeployGridData);
        
        includesEditor.createClient(composite);
        includesEditor.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        
        excludesEditor.createClient(composite);
        excludesEditor.getComposite().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        
        if (gs != null) {
            noRedeploy.setSelection(gs.isNoRedeploy());
            includesEditor.setEnabled(noRedeploy.getSelection());
            excludesEditor.setEnabled(noRedeploy.getSelection());
            
            includesEditor.setItems(gs.getNoRedeployFilePatterns(true));
            excludesEditor.setItems(gs.getNoRedeployFilePatterns(false));
        }
    }

}
