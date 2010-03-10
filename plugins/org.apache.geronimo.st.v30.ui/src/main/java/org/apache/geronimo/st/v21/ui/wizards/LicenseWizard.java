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

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.apache.geronimo.system.plugin.model.LicenseType;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class LicenseWizard extends AbstractWizard {

    protected LicenseType license;
    
    protected Text licenseName;
    
    protected Combo osiApproved;
    
    public LicenseWizard (LicenseType oldLicense) {
        super();
        license = oldLicense;
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_License;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_License;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(new MessageDestWizardPage("Page0"));
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class MessageDestWizardPage extends AbstractWizardPage {
        public MessageDestWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel (composite, CommonMessages.license);
            licenseName = createTextField (composite, "");
            createLabel (composite, CommonMessages.osiApproved);
            String[] values = {"true", "false"};
            osiApproved = createCombo (composite, values, false);

            if (license != null) {
                licenseName.setText(license.getValue());
                osiApproved.setText(String.valueOf(license.isOsiApproved()));
            }
            setControl(composite);
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_License;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_License;
        }
    }
    
    public boolean performFinish() {
        license = new LicenseType();
        license.setValue(licenseName.getText());
        license.setOsiApproved(Boolean.parseBoolean(osiApproved.getText()));

        return true;
    }
    
    public LicenseType getLicense() {
        return license;
    }
}
