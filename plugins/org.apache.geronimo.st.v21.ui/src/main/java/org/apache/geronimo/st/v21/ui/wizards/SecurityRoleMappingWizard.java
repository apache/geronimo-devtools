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

import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.apache.geronimo.jee.security.DistinguishedName;
import org.apache.geronimo.jee.security.LoginDomainPrincipal;
import org.apache.geronimo.jee.security.Principal;
import org.apache.geronimo.jee.security.RealmPrincipal;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.apache.geronimo.st.v21.core.GeronimoServerInfoManager;
import org.apache.geronimo.st.v21.ui.sections.SecurityRoleMappingSection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 *  @version $Rev$ $Date$
 */
public class SecurityRoleMappingWizard extends AbstractWizard {
    public static final String[] PRINCIPAL_CLASSES = {
            "org.apache.geronimo.security.realm.providers.GeronimoGroupPrincipal",
            "org.apache.geronimo.security.realm.providers.GeronimoUserPrincipal" };

    private final int DISTINGUISHED_NAME = 0;
    private final int PRINCIPAL = 1;
    private final int LOGIN_DOMAIN_PRINCIPAL = 2;
    private final int REALM_PRINCIPAL = 3;
    private final String[] ROLE_MAPPING_TYPES = {
            "Distinguished Name", "Principal", "Login Domain Principal", "Realm Principal" };

    protected AbstractTreeSection section;
    protected Combo type;
    protected Text name;
    protected Combo clazz;
    protected Text domainName;
    protected Combo realmName;
    
    public SecurityRoleMappingWizard(AbstractTreeSection section) {
        super();
        this.section = section;
    }

    protected String[] getSecurityRealms() {
        String runtimeVersion  = ((SecurityRoleMappingSection)section).getRuntimeVersion();
        List<String> securityRealms =  GeronimoServerInfoManager.getProvider(runtimeVersion).getSecurityRealms();
        return securityRealms.toArray(new String[securityRealms.size()]);
    }

    public class SecurityRoleMappingsWizardPage extends AbstractWizardPage {
        protected Label nameLabel, clazzLabel, domainNameLabel, realmNameLabel;

        public SecurityRoleMappingsWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel(composite, CommonMessages.type);
            type = createCombo(composite, ROLE_MAPPING_TYPES, false);
            nameLabel = createLabel(composite, CommonMessages.name);
            name = createTextField(composite, "");
            name.setFocus();
            clazzLabel = createLabel(composite, CommonMessages.className);
            clazz = createCombo(composite, PRINCIPAL_CLASSES, true);
            clazz.select(0);
            domainNameLabel = createLabel(composite, CommonMessages.editorDomainName);
            domainName = createTextField(composite, "");
            realmNameLabel = createLabel(composite, CommonMessages.editorRealmName);
            realmName = createCombo(composite, getSecurityRealms(), true);

            type.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    toggleFields();
                }
            });
            if (eObject == null) {
                type.select(DISTINGUISHED_NAME);
            }
            else {
                // set the fields to the values in eObject and select the correct type
                if (DistinguishedName.class.isInstance(eObject)) {
                    name.setText(((DistinguishedName)eObject).getName());
                    type.select(DISTINGUISHED_NAME);
                }
                else {
                    name.setText(((Principal)eObject).getName());
                    clazz.setText(((Principal)eObject).getClazz());
                    if (LoginDomainPrincipal.class.isInstance(eObject)) {
                        domainName.setText(((LoginDomainPrincipal)eObject).getDomainName());
                        if (RealmPrincipal.class.isInstance(eObject)) {
                            realmName.setText(((RealmPrincipal)eObject).getRealmName());
                            type.select(REALM_PRINCIPAL);
                        }
                        else {
                            type.select(LOGIN_DOMAIN_PRINCIPAL);
                        }
                    }
                    else {
                        type.select(PRINCIPAL);
                    }
                }
                type.setEnabled(false);
            }
            toggleFields();
            setControl(composite);
        }

        private void toggleFields() {
            int selection = type.getSelectionIndex();
            clazzLabel.setVisible (selection >= PRINCIPAL ? true : false);
            clazz.setVisible (selection >= PRINCIPAL ? true : false);
            domainNameLabel.setVisible (selection >= LOGIN_DOMAIN_PRINCIPAL ? true : false);
            domainName.setVisible (selection >= LOGIN_DOMAIN_PRINCIPAL ? true : false);
            realmNameLabel.setVisible (selection == REALM_PRINCIPAL ? true : false);
            realmName.setVisible (selection == REALM_PRINCIPAL ? true : false);
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_RoleMapping;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_RoleMapping;
        }
    }

    @Override
    public void addPages() {
        addPage(new SecurityRoleMappingsWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        switch (type.getSelectionIndex()) {
        case DISTINGUISHED_NAME:
            if (isEmpty(name.getText())) {
                return false;
            }
            DistinguishedName distinguishedName = (DistinguishedName)eObject;
            if (distinguishedName == null) {
                distinguishedName = (DistinguishedName)getEFactory().create(DistinguishedName.class);
                Role role = (Role) section.getSelectedObject();
                role.getDistinguishedName().add(distinguishedName);
            }
            distinguishedName.setName(name.getText());
            break;

        case PRINCIPAL:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText())) {
                return false;
            }
            Principal principal = (Principal)eObject;
            if (principal == null) {
                principal = (Principal)getEFactory().create(Principal.class);
                Role role = (Role) section.getSelectedObject();
                role.getPrincipal().add(principal);
            }
            principal.setName(name.getText());
            principal.setClazz(clazz.getText());
            break;

        case LOGIN_DOMAIN_PRINCIPAL:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText()) || isEmpty(domainName.getText())) {
                return false;
            }
            LoginDomainPrincipal loginDomainPrincipal = (LoginDomainPrincipal)eObject;
            if (loginDomainPrincipal == null) {
                loginDomainPrincipal = (LoginDomainPrincipal)getEFactory().create(LoginDomainPrincipal.class);
                Role role = (Role) section.getSelectedObject();
                role.getLoginDomainPrincipal().add(loginDomainPrincipal);
            }
            loginDomainPrincipal.setName(name.getText());
            loginDomainPrincipal.setClazz(clazz.getText());
            loginDomainPrincipal.setDomainName(domainName.getText()); 
            break;

        case REALM_PRINCIPAL:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText()) || isEmpty(domainName.getText())
                    || isEmpty(realmName.getText())) {
                return false;
            }
            RealmPrincipal realmPrincipal = (RealmPrincipal)eObject;
            if (realmPrincipal == null) {
                realmPrincipal = (RealmPrincipal)getEFactory().create(RealmPrincipal.class);
                Role role = (Role) section.getSelectedObject();
                role.getRealmPrincipal().add(realmPrincipal);
            }
            realmPrincipal.setName(name.getText());
            realmPrincipal.setClazz(clazz.getText());
            realmPrincipal.setDomainName(domainName.getText());
            realmPrincipal.setRealmName(realmName.getText());
            break;
        }
        return true;
    }
    
    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    @Override
    protected String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_RoleMapping;
    }

    @Override
    protected String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_RoleMapping;
    }
}
