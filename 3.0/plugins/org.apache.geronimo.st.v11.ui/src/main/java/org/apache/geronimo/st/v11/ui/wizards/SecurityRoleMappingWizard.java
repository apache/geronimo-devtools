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

import org.apache.geronimo.j2ee.security.DistinguishedNameType;
import org.apache.geronimo.j2ee.security.LoginDomainPrincipalType;
import org.apache.geronimo.j2ee.security.PrincipalType;
import org.apache.geronimo.j2ee.security.RealmPrincipalType;
import org.apache.geronimo.j2ee.security.RoleType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.apache.geronimo.st.v11.core.GeronimoServerInfo;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBObjectFactoryImpl;
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

    protected static String[] getSecurityRealms() {
        List<String> securityRealms = GeronimoServerInfo.getInstance().getSecurityRealms();
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
                if (DistinguishedNameType.class.isInstance(eObject)) {
                    name.setText(((DistinguishedNameType)eObject).getName());
                    type.select(DISTINGUISHED_NAME);
                }
                else {
                    name.setText(((PrincipalType)eObject).getName());
                    clazz.setText(((PrincipalType)eObject).getClazz());
                    if (LoginDomainPrincipalType.class.isInstance(eObject)) {
                        domainName.setText(((LoginDomainPrincipalType)eObject).getDomainName());
                        if (RealmPrincipalType.class.isInstance(eObject)) {
                            realmName.setText(((RealmPrincipalType)eObject).getRealmName());
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
            DistinguishedNameType distinguishedName = (DistinguishedNameType)eObject;
            if (distinguishedName == null) {
                distinguishedName = (DistinguishedNameType)getEFactory().create(DistinguishedNameType.class);
                RoleType role = (RoleType) section.getSelectedObject();
                role.getDistinguishedName().add(distinguishedName);
            }
            distinguishedName.setName(name.getText());
            break;

        case PRINCIPAL:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText())) {
                return false;
            }
            PrincipalType principal = (PrincipalType)eObject;
            if (principal == null) {
                principal = (PrincipalType)getEFactory().create(PrincipalType.class);
                RoleType role = (RoleType) section.getSelectedObject();
                role.getPrincipal().add(principal);
            }
            principal.setName(name.getText());
            principal.setClazz(clazz.getText());
            break;

        case LOGIN_DOMAIN_PRINCIPAL:
            if (isEmpty(name.getText()) || isEmpty(clazz.getText()) || isEmpty(domainName.getText())) {
                return false;
            }
            LoginDomainPrincipalType loginDomainPrincipal = (LoginDomainPrincipalType)eObject;
            if (loginDomainPrincipal == null) {
                loginDomainPrincipal = (LoginDomainPrincipalType)getEFactory().create(LoginDomainPrincipalType.class);
                RoleType role = (RoleType) section.getSelectedObject();
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
            RealmPrincipalType realmPrincipal = (RealmPrincipalType)eObject;
            if (realmPrincipal == null) {
                realmPrincipal = (RealmPrincipalType)getEFactory().create(RealmPrincipalType.class);
                RoleType role = (RoleType) section.getSelectedObject();
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
