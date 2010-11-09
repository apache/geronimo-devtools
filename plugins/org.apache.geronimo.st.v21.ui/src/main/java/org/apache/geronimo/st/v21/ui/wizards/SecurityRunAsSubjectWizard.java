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

import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.SubjectInfo;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.apache.geronimo.st.v21.ui.sections.SecurityAdvancedSection;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 *  @version $Rev$ $Date$
 */
public class SecurityRunAsSubjectWizard extends AbstractWizard {
    protected Combo role;
    protected Text realm;
    protected Text id;
    protected AbstractTableSection section;

    public SecurityRunAsSubjectWizard(AbstractTableSection section) {
        super();
        this.section = section;
    }

    public class SecurityRunAsSubjectAddWizardPage extends AbstractWizardPage {
        public SecurityRunAsSubjectAddWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel(composite, CommonMessages.securityRunAsSubjectRole);
            role = createCombo(composite, ((SecurityAdvancedSection) section).getRolesWithoutRunAsSubject(), false);
            createLabel(composite, CommonMessages.securityRunAsSubjectRealm);
            realm = createTextField(composite, "");
            createLabel(composite, CommonMessages.securityRunAsSubjectId);
            id = createTextField(composite, "");
            if (eObject != null) {
                //TODO  role.setText (((SecurityAdvancedSection) section).);
                role.setEnabled(false);
                realm.setText (((Role)eObject).getRunAsSubject().getRealm());
                id.setText(((Role)eObject).getRunAsSubject().getId());
            }
            setControl(composite);
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_RunAsSubject;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_RunAsSubject;
        }
    }

    @Override
    public void addPages() {
        addPage(new SecurityRunAsSubjectAddWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        if (isEmpty(realm.getText()) || isEmpty(id.getText())) {
            return false;
        }
        if (eObject == null) {
            SubjectInfo subjectInfo = new SubjectInfo();
            subjectInfo.setRealm(realm.getText());
            subjectInfo.setId(id.getText());
            ((SecurityAdvancedSection) section).getRole(role.getText()).setRunAsSubject(subjectInfo);
            ((SecurityAdvancedSection) section).activateAddButton();
        }
        else {
            ((Role)eObject).getRunAsSubject().setRealm(realm.getText());
            ((Role)eObject).getRunAsSubject().setId(id.getText());
        }

        return true;
    }

    @Override
    protected String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_RunAsSubject;
    }

    @Override
    protected String getEditWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_RunAsSubject;
    }
}
