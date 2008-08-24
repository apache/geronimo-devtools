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

import org.apache.geronimo.jee.security.DistinguishedName;
import org.apache.geronimo.jee.security.LoginDomainPrincipal;
import org.apache.geronimo.jee.security.Principal;
import org.apache.geronimo.jee.security.RealmPrincipal;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.apache.geronimo.st.v21.core.GeronimoServerInfo;
import org.apache.geronimo.st.v21.ui.sections.SecurityRoleMappingsSection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class SecurityRoleMappingsAddWizard extends AbstractWizard {
    public static final String[] PRINCIPAL_CLASSES = {
            "org.apache.geronimo.security.realm.providers.GeronimoGroupPrincipal",
            "org.apache.geronimo.security.realm.providers.GeronimoUserPrincipal" };

    protected Combo type;
    protected Text name;
    protected Combo clazz;
    protected Text domainName;
    protected Combo realmName;

    public SecurityRoleMappingsAddWizard(AbstractTreeSection section) {
        super(section);
    }

    public String[] getPrincipalTypes() {
        return new String[] { "DistinguishedName", "Principal", "LoginDomainPrincipal", "RealmPrincipal" };
    }

    protected static String[] getSecurityRealms() {
        List<String> securityRealms = GeronimoServerInfo.getInstance().getSecurityRealms();
        return securityRealms.toArray(new String[securityRealms.size()]);
    }

    public class SecurityRoleMappingsAddWizardPage extends AbstractWizardPage {
        protected Label nameLabel, clazzLabel, domainNameLabel, realmNameLabel;

        public SecurityRoleMappingsAddWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel(composite, "Type");
            type = createCombo(composite, getPrincipalTypes(), false);
            nameLabel = createLabel(composite, "Name");
            name = createTextFeild(composite, "");
            name.setFocus();
            clazzLabel = createLabel(composite, "Clazz");
            clazz = createCombo(composite, PRINCIPAL_CLASSES, true);
            clazz.select(0);
            domainNameLabel = createLabel(composite, "DomainName");
            domainName = createTextFeild(composite, "");
            realmNameLabel = createLabel(composite, "RealmName");
            realmName = createCombo(composite, getSecurityRealms(), true);

            type.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    switch (type.getSelectionIndex()) {
                    case 0:
                        showFeilds(false, false, false);
                        break;
                    case 1:
                        showFeilds(true, false, false);
                        break;
                    case 2:
                        showFeilds(true, true, false);
                        break;
                    case 3:
                        showFeilds(true, true, true);
                    }
                }
            });
            type.select(1);
            showFeilds(true, false, false);
            setControl(composite);
        }

        private void showFeilds(boolean clazzVisibility, boolean domainNameVisibility, boolean realmNameVisibility) {
            clazzLabel.setVisible(clazzVisibility);
            clazz.setVisible(clazzVisibility);
            domainNameLabel.setVisible(domainNameVisibility);
            domainName.setVisible(domainNameVisibility);
            realmNameLabel.setVisible(realmNameVisibility);
            realmName.setVisible(realmNameVisibility);
        }
    }

    @Override
    public void addPages() {
        addPage(new SecurityRoleMappingsAddWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        Role role = ((SecurityRoleMappingsSection) section).getSelectedRoleDuringAdd();
        switch (type.getSelectionIndex()) {
        case 0:
            if (isEmpty(name.getText())) {
                return false;
            }
            DistinguishedName distinguishedName = new DistinguishedName();
            distinguishedName.setName(name.getText());
            role.getDistinguishedName().add(distinguishedName);
            break;
        case 1:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText())) {
                return false;
            }
            Principal principal = new Principal();
            principal.setName(name.getText());
            principal.setClazz(clazz.getText());
            role.getPrincipal().add(principal);
            break;
        case 2:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText()) || isEmpty(domainName.getText())) {
                return false;
            }
            LoginDomainPrincipal loginDomainPrincipal = new LoginDomainPrincipal();
            loginDomainPrincipal.setName(name.getText());
            loginDomainPrincipal.setClazz(clazz.getText());
            loginDomainPrincipal.setDomainName(domainName.getText());
            role.getLoginDomainPrincipal().add(loginDomainPrincipal);
            break;
        case 3:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText()) || isEmpty(domainName.getText())
                    || isEmpty(realmName.getText())) {
                return false;
            }
            RealmPrincipal realmPrincipal = new RealmPrincipal();
            realmPrincipal.setName(name.getText());
            realmPrincipal.setClazz(clazz.getText());
            realmPrincipal.setDomainName(domainName.getText());
            realmPrincipal.setRealmName(realmName.getText());
            role.getRealmPrincipal().add(realmPrincipal);
            break;
        }
        return true;
    }

    @Override
    protected String getWizardWindowTitle() {
        return "Add Role Mapping";
        //return CommonMessages.wizardNewTitle_SecurityRole;
    }

    @Override
    protected String getWizardPageTitle() {
        return "New Security Role Mapping";
        //return CommonMessages.wizardNewTitle_SecurityRole;
    }

    @Override
    protected String getWizardPageDescription() {
        return "Provide details for this role mapping";
        //return CommonMessages.wizardPageDescription_SecurityRole;
    }
}
