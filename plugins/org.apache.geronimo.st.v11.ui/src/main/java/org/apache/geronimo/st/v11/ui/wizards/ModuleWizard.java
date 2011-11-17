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
package org.apache.geronimo.st.v11.ui.wizards;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.application.ApplicationType;
import org.apache.geronimo.j2ee.application.ModuleType;
import org.apache.geronimo.j2ee.application.PathType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/*
 * @version $Rev$ $Date$
 */
public class ModuleWizard extends AbstractTableWizard {

    public ModuleWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
        return new String[] { "ModuleType", "Path", "AltDd" };
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_Module;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_Module;
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
        protected Button[] buttonList = new Button[4];
        
        public ModuleWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            GridData data;
            ModuleType module = (ModuleType)eObject;
            
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
            FillLayout fillLayout = new FillLayout();
            fillLayout.type = SWT.HORIZONTAL;
            group.setLayout(fillLayout);
            data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            data.horizontalSpan = 2;
            group.setLayoutData(data);
            
            for (int i = 1; i < section.getTableColumnNames().length; i++) {
                Label label = new Label(composite, SWT.LEFT);
                String columnName = section.getTableColumnNames()[i];
                if (!columnName.endsWith(":"))
                    columnName = columnName.concat(":");
                label.setText(columnName);
                data = new GridData();
                data.horizontalAlignment = GridData.FILL;
                label.setLayoutData(data);

                Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
                data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                        | GridData.VERTICAL_ALIGN_FILL);
                data.grabExcessHorizontalSpace = true;
                data.widthHint = 100;
                text.setLayoutData(data);
                if (module != null) {
                    if (i == 1) {
                        if (module.getConnector() != null) {
                            text.setText(module.getConnector().getValue());
                            buttonList[0].setSelection(true);
                        }
                        else if (module.getEjb() != null) {
                            text.setText(module.getEjb().getValue());
                            buttonList[1].setSelection(true);
                        }
                        else if (module.getJava() != null) {
                            text.setText(module.getJava().getValue());
                            buttonList[2].setSelection(true);
                        }
                        else if (module.getWeb() != null) {
                            text.setText(module.getWeb().getValue());
                            buttonList[3].setSelection(true);
                        }                        
                    }
                    else if (i == 2 && module.getAltDd() != null) {
                        text.setText(module.getAltDd().getValue());
                    }
                }
                textEntries[i - 1] = text;
            }

            doCustom(composite);
            setControl(composite);
            textEntries[0].setFocus();
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_Module;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_Module;
        }
    }

    public boolean performFinish() {
        AbstractTableWizardPage page = (AbstractTableWizardPage) getPages()[0];
        PathType path;
        ModuleType module;

        if (eObject == null) {
            eObject = getEFactory().create(ModuleType.class);
            JAXBElement plan = section.getPlan();

            module = (ModuleType)eObject;

            List moduleList = ((ApplicationType)plan.getValue()).getModule();
            if (moduleList == null) {
                moduleList = (List)getEFactory().create(ModuleType.class);
            }
            moduleList.add(eObject);
        }
        else {
            module = (ModuleType)eObject;
            module.setConnector(null);
            module.setEjb(null);
            module.setJava(null);
            module.setWeb(null);
        }

        // NOTE!! this replaces the call to processEAttributes (page);
        path = (PathType)getEFactory().create(PathType.class);
        path.setValue(page.getTextEntry(0).getText());

        if (((ModuleWizardPage)page).buttonList[0].getSelection())
            module.setConnector(path);
        else if (((ModuleWizardPage)page).buttonList[1].getSelection())
            module.setEjb(path);
        else if (((ModuleWizardPage)page).buttonList[2].getSelection())
            module.setJava(path);
        else if (((ModuleWizardPage)page).buttonList[3].getSelection())
            module.setWeb(path);
        
        String altDD = page.getTextEntry(1).getText();
        path = (PathType)getEFactory().create(PathType.class);
        path.setValue(altDD);
        module.setAltDd(path);
        
        if (section.getViewer().getInput() == section.getPlan()) {
            section.getViewer().setInput(section.getInput());
        }

        return true;
    }
}
