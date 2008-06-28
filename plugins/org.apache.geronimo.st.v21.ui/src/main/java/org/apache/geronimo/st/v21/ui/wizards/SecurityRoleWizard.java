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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.st.core.jaxb.JAXBUtils;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard.DynamicWizardPage;
import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.RoleMappings;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.Security;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class SecurityRoleWizard extends AbstractTableWizard {

    public SecurityRoleWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
        return new String[] { "RoleName", "Description" };
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_SecurityRole;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_SecurityRole;
    }

    public String getWizardFirstPageTitle() {
        return CommonMessages.wizardPageTitle_SecurityRole;
    }

    public String getWizardFirstPageDescription() {
        return CommonMessages.wizardPageDescription_SecurityRole;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        SecurityRoleWizardPage page = new SecurityRoleWizardPage("Page0");
        page.setImageDescriptor(descriptor);
        addPage(page);
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class SecurityRoleWizardPage extends DynamicWizardPage {
        public SecurityRoleWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            for (int i = 0; i < section.getTableColumnNames().length; i++) {
                Label label = new Label(composite, SWT.LEFT);
                String columnName = section.getTableColumnNames()[i];
                if (!columnName.endsWith(":"))
                    columnName = columnName.concat(":");
                label.setText(columnName);
                GridData data = new GridData();
                data.horizontalAlignment = GridData.FILL;
                label.setLayoutData(data);

                Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
                data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                        | GridData.VERTICAL_ALIGN_FILL);
                data.grabExcessHorizontalSpace = true;
                data.widthHint = 100;
                text.setLayoutData(data);
                if (eObject != null) {
                    if (i == 1) {
                        // get the description
                        Role role = (Role) eObject;
                        String value = role.getDescription().get(0).getValue();
                        if (value != null) {
                            text.setText(value);
                        }                        
                    }
                    else
                    {
                        String value = (String) JAXBUtils.getValue(eObject,getTableColumnEAttributes()[i]);
                        if (value != null) {
                            text.setText(value);
                        }
                    }
                }
                textEntries[i] = text;
            }

            doCustom(composite);
            setControl(composite);
            textEntries[0].setFocus();
        }
    }
    
    public boolean performFinish() {
        DynamicWizardPage page = (DynamicWizardPage) getPages()[0];

        if (eObject == null) {
            eObject = getEFactory().create(Role.class);
            JAXBElement plan = section.getPlan();

            Security security = JAXBModelUtils.getSecurity(plan);
            if (security == null) {
                security = (Security)getEFactory().create(Security.class);
                JAXBModelUtils.setSecurity(plan, security);
            }

            RoleMappings roleMappings = security.getRoleMappings();
            if (roleMappings == null) {
                roleMappings = (RoleMappings)getEFactory().create(RoleMappings.class);
                security.setRoleMappings(roleMappings);
            }

            roleMappings.getRole().add((Role)eObject);
        }

        // NOTE!! this replaces the call to processEAttributes (page);
        String value = page.getTextEntry(0).getText();
        String attribute = getTableColumnEAttributes()[0];
        JAXBUtils.setValue(eObject, attribute, value);

        Description type = null;
        Role role = (Role) eObject;
        if (role.getDescription().isEmpty()) {
            type = (Description)getEFactory().create(Description.class);
            role.getDescription().add(type);
        } else {
            type = (Description) role.getDescription().get(0);
        }
        type.setValue (page.getTextEntry(1).getText());

        if (section.getTableViewer().getInput() == section.getPlan()) {
            section.getTableViewer().setInput(section.getInput());
        }

        return true;
    }
}
