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
package org.apache.geronimo.st.v21.ui.wizards;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.application.ExtModule;
import org.apache.geronimo.jee.application.Path;
import org.apache.geronimo.jee.deployment.Pattern;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/*
 * @version $Rev$ $Date$
 */
public class ExtModuleWizard extends AbstractTableWizard {

    public ExtModuleWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
        return new String[] { "ModuleType", "Path", "InternalPath",
                "GroupId", "ArtifactId", "Version", "Type" };
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_ExtModule;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_ExtModule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(new ModuleWizardPage("Page0"));
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class ModuleWizardPage extends AbstractTableWizardPage {
        protected Button[] buttonList = new Button[6];
        
        public ModuleWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            GridData data;
            ExtModule extModule = (ExtModule)eObject;
            
            // First we need a set of radio buttons to determine what kind of module we are
            // dealing with.
            Group group = new Group (composite, SWT.NONE);
            Button button = new Button (group, SWT.RADIO);
            button.setText(CommonMessages.connector);
            buttonList[0] = button;
            button = new Button (group, SWT.RADIO);
            button.setText(CommonMessages.ejb);
            buttonList[1] = button;
            button = new Button (group, SWT.RADIO);
            button.setText(CommonMessages.java);
            buttonList[2] = button;
            button = new Button (group, SWT.RADIO);
            button.setText(CommonMessages.web);
            buttonList[3] = button;
            GridLayout gridLayout = new GridLayout();
            gridLayout.numColumns = 4;
            group.setLayout(gridLayout);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            data.horizontalSpan = 2;
            group.setLayoutData(data);
            
            Group group2 = new Group (composite, SWT.NONE);
            for (int i = 1; i < section.getTableColumnNames().length; i++) {
                if (i == 2) {
                    button = new Button (group2, SWT.RADIO);
                    button.setText(CommonMessages.internalPath);
                    buttonList[4] = button;
                    data = new GridData();
                    data.horizontalAlignment = GridData.FILL;
                    data.horizontalSpan = 2;
                    button.setLayoutData(data);
                }
                if (i == 3) {
                    button = new Button (group2, SWT.RADIO);
                    button.setText(CommonMessages.externalPath);
                    buttonList[5] = button;
                    data = new GridData();
                    data.horizontalAlignment = GridData.FILL;
                    data.horizontalSpan = 2;
                    button.setLayoutData(data);
                }
                Text text;

                if (i == 1) {
                    Label label = new Label(group, SWT.LEFT);
                    String columnName = section.getTableColumnNames()[i];
                    if (!columnName.endsWith(":"))
                        columnName = columnName.concat(":");
                    label.setText(columnName);
                    data = new GridData();
                    data.horizontalAlignment = GridData.FILL;
                    label.setLayoutData(data);

                    text = new Text(group, SWT.SINGLE | SWT.BORDER);
                    data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                            | GridData.VERTICAL_ALIGN_FILL);
                    data.grabExcessHorizontalSpace = true;
                    data.widthHint = 100;
                    data.horizontalSpan = 3;
                    text.setLayoutData(data);
                }
                else {
                    Label label = new Label(group2, SWT.LEFT);
                    String columnName = section.getTableColumnNames()[i];
                    if (!columnName.endsWith(":"))
                        columnName = columnName.concat(":");
                    label.setText(columnName);
                    data = new GridData();
                    data.horizontalAlignment = GridData.FILL;
                    label.setLayoutData(data);

                    text = new Text(group2, SWT.SINGLE | SWT.BORDER);
                    data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                            | GridData.VERTICAL_ALIGN_FILL);
                    data.grabExcessHorizontalSpace = true;
                    data.widthHint = 100;
                    text.setLayoutData(data);
                }

                if (extModule != null) {
                    if (i == 1) {
                        if (extModule.getConnector() != null) {
                            text.setText(extModule.getConnector().getValue());
                            buttonList[0].setSelection(true);
                        }
                        else if (extModule.getEjb() != null) {
                            text.setText(extModule.getEjb().getValue());
                            buttonList[1].setSelection(true);
                        }
                        else if (extModule.getJava() != null) {
                            text.setText(extModule.getJava().getValue());
                            buttonList[2].setSelection(true);
                        }
                        else if (extModule.getWeb() != null) {
                            text.setText(extModule.getWeb().getValue());
                            buttonList[3].setSelection(true);
                        }                        
                    }
                    else if (i == 2 && extModule.getInternalPath() != null) {
                        text.setText(extModule.getInternalPath());
                    }
                    else if (i > 2 && extModule.getExternalPath() != null) {
                        Pattern pattern = extModule.getExternalPath();
                        String value;
						try {
							value = (String) JAXBUtils.getValue(pattern,getTableColumnEAttributes()[i]);
						} catch (Exception e1) {
							value = e1.getMessage();
						}
                        if (value != null) {
                            text.setText(value);
                        }
                    }
                }
                textEntries[i - 1] = text;
            }
            gridLayout = new GridLayout();
            gridLayout.numColumns = 2;
            group2.setLayout(gridLayout);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessHorizontalSpace = true;
            data.horizontalSpan = 2;
            group2.setLayoutData(data);

            buttonList[4].addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    if (buttonList[4].getSelection()) {
                       toggle();
                    }
                }
            });
            buttonList[5].addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    if (buttonList[5].getSelection()) {
                        toggle();
                    }
                }
            });

            if (extModule == null) {
                buttonList[0].setSelection(true);
                buttonList[4].setSelection(true);
            }
            else if (extModule.getInternalPath() != null) {
                buttonList[4].setSelection(true);
            }
            else {
                buttonList[5].setSelection(true);
            }
            toggle();

            doCustom(composite);
            setControl(composite);
            textEntries[0].setFocus();
        }
        
        private void toggle () {
            textEntries[1].setEnabled(buttonList[4].getSelection());
            textEntries[2].setEnabled(buttonList[5].getSelection());
            textEntries[3].setEnabled(buttonList[5].getSelection());
            textEntries[4].setEnabled(buttonList[5].getSelection());
            textEntries[5].setEnabled(buttonList[5].getSelection());
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_ExtModule;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_ExtModule;
        }
    }
    
    public boolean performFinish() {
        ModuleWizardPage page = (ModuleWizardPage) getPages()[0];
        Path path;
        ExtModule extModule;

        if (eObject == null) {
            eObject = getEFactory().create(ExtModule.class);
            JAXBElement plan = section.getPlan();
            extModule = (ExtModule)eObject;
            //This is the extModule field in plan(Application Type)
            List<ExtModule> extModuleList = ((Application)plan.getValue()).getExtModule();
            extModuleList.add(extModule);
        }
        else {
            extModule = (ExtModule)eObject;
            extModule.setConnector(null);
            extModule.setEjb(null);
            extModule.setJava(null);
            extModule.setWeb(null);
            extModule.setExternalPath(null);
        }

        // NOTE!! this replaces the call to processEAttributes (page);
        path = (Path)getEFactory().create(Path.class);
        path.setValue(page.getTextEntry(0).getText());

        if (((ModuleWizardPage)page).buttonList[0].getSelection())
            extModule.setConnector(path);
        else if (((ModuleWizardPage)page).buttonList[1].getSelection())
            extModule.setEjb(path);
        else if (((ModuleWizardPage)page).buttonList[2].getSelection())
            extModule.setJava(path);
        else if (((ModuleWizardPage)page).buttonList[3].getSelection())
            extModule.setWeb(path);

        if (page.buttonList[4].getSelection()) {
            extModule.setInternalPath (page.getTextEntry(1).getText());
            extModule.setExternalPath(null);
        }
        else if (page.buttonList[5].getSelection()) {
            Pattern pattern = (Pattern)getEFactory().create(Pattern.class);
            extModule.setExternalPath(pattern);
            for (int i = 2; i < 6; i++) {
                String value = page.getTextEntry(i).getText();
                String attribute = getTableColumnEAttributes()[i + 1];
                try {
					JAXBUtils.setValue(pattern, attribute, value);
				} catch (Exception e) {
					MessageDialog.openError(Display.getCurrent().getActiveShell(),"Error", e.getMessage());
				}
            }
            extModule.setInternalPath(null);
        }

        if (section.getViewer().getInput() == section.getPlan()) {
            section.getViewer().setInput(section.getInput());
        }

        return true;
    }
}
