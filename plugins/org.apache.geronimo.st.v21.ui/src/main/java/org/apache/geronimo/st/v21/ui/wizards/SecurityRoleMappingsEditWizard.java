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

import org.apache.geronimo.jee.security.DistinguishedName;
import org.apache.geronimo.jee.security.LoginDomainPrincipal;
import org.apache.geronimo.jee.security.Principal;
import org.apache.geronimo.jee.security.RealmPrincipal;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class SecurityRoleMappingsEditWizard extends AbstractWizard {
    protected Text name;
    protected Combo clazz;
    protected Text domainName;
    protected Combo realmName;

    public SecurityRoleMappingsEditWizard(AbstractTreeSection section) {
        super(section);
    }

    public Object getSelectedObject() {
        return eObject;
    }

    public class SecurityRoleMappingsEditWizardPage extends AbstractWizardPage {
        public SecurityRoleMappingsEditWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel(composite, "Name");
            name = createTextFeild(composite, "");
            name.setFocus();
            if (getSelectedObject() instanceof DistinguishedName) {
                name.setText(((DistinguishedName) getSelectedObject()).getName());
            }
            if (getSelectedObject() instanceof Principal) {
                name.setText(((Principal) getSelectedObject()).getName());
                createLabel(composite, "Clazz");
                clazz = createCombo(composite, SecurityRoleMappingsAddWizard.PRINCIPAL_CLASSES, true);
                clazz.setText(((Principal) getSelectedObject()).getClazz());
            }
            if (getSelectedObject() instanceof LoginDomainPrincipal) {
                createLabel(composite, "DomainName");
                domainName = createTextFeild(composite, "");
                domainName.setText(((LoginDomainPrincipal) getSelectedObject()).getDomainName());
            }
            if (getSelectedObject() instanceof RealmPrincipal) {
                createLabel(composite, "RealmName");
                realmName = createCombo(composite, SecurityRoleMappingsAddWizard.getSecurityRealms(), true);
                realmName.setText(((RealmPrincipal) getSelectedObject()).getRealmName());
            }
            setControl(composite);
        }
    }

    @Override
    public void addPages() {
        addPage(new SecurityRoleMappingsEditWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        if (isEmpty(name.getText())) {
            return false;
        }
        if (getSelectedObject() instanceof DistinguishedName) {
            ((DistinguishedName) getSelectedObject()).setName(name.getText());
        }
        if (getSelectedObject() instanceof Principal) {
            ((Principal) getSelectedObject()).setName(name.getText());
            if (isEmpty(clazz.getText())) {
                return false;
            }
            ((Principal) getSelectedObject()).setClazz(clazz.getText());
        }
        if (getSelectedObject() instanceof LoginDomainPrincipal) {
            if (isEmpty(domainName.getText())) {
                return false;
            }
            ((LoginDomainPrincipal) getSelectedObject()).setDomainName(domainName.getText());
        }
        if (getSelectedObject() instanceof RealmPrincipal) {
            if (isEmpty(realmName.getText())) {
                return false;
            }
            ((RealmPrincipal) getSelectedObject()).setRealmName(realmName.getText());
        }
        return true;
    }

    @Override
    protected String getWizardWindowTitle() {
        return "Edit Role Mapping";
        //return CommonMessages.wizardEditTitle_SecurityRole;
    }

    @Override
    protected String getWizardPageTitle() {
        return "Edit Security Role Mapping";
        //return CommonMessages.wizardNewTitle_SecurityRole;
    }

    @Override
    protected String getWizardPageDescription() {
        return "Provide details for this role mapping";
        //return CommonMessages.wizardPageDescription_SecurityRole;
    }
}
