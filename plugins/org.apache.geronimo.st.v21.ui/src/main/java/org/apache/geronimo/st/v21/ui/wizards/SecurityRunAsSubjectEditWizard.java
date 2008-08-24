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
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class SecurityRunAsSubjectEditWizard extends AbstractWizard {
    protected Text realm;
    protected Text id;

    public SecurityRunAsSubjectEditWizard(AbstractTableSection section) {
        super(section);
    }

    private SubjectInfo getSubjectInfo() {
        return ((Role)eObject).getRunAsSubject();
    }

    public class SecurityRunAsSubjectEditWizardPage extends AbstractWizardPage {
        public SecurityRunAsSubjectEditWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel(composite, "Realm");
            realm = createTextFeild(composite, "");
            realm.setText(getSubjectInfo().getRealm());
            createLabel(composite, "Id");
            id = createTextFeild(composite, "");
            id.setText(getSubjectInfo().getId());
            setControl(composite);
        }
    }

    @Override
    public void addPages() {
        addPage(new SecurityRunAsSubjectEditWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        if (isEmpty(realm.getText()) || isEmpty(id.getText())) {
            return false;
        }
        getSubjectInfo().setRealm(realm.getText());
        getSubjectInfo().setId(id.getText());
        return true;
    }

    @Override
    protected String getWizardWindowTitle() {
        return "Edit Run-As Subject";
    }

    @Override
    protected String getWizardPageTitle() {
        return "Edit Security Run-As Subject";
    }

    @Override
    protected String getWizardPageDescription() {
        return "Specify details for this run-as-subject";
    }
}
