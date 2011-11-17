///*
// * Licensed to the Apache Software Foundation (ASF) under one or more
// * contributor license agreements.  See the NOTICE file distributed with
// * this work for additional information regarding copyright ownership.
// * The ASF licenses this file to You under the Apache License, Version 2.0
// * (the "License"); you may not use this file except in compliance with
// * the License.  You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.apache.geronimo.st.v11.ui.sections;
//
//import java.util.ArrayList;
//import java.util.Hashtable;
//import java.util.List;
//
//import javax.xml.bind.JAXBElement;
//
//import org.apache.geronimo.st.ui.CommonMessages;
//import org.apache.geronimo.st.ui.sections.AbstractTableSection;
//import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
//import org.apache.geronimo.st.v11.ui.wizards.SecurityRunAsSubjectWizard;
//import org.apache.geronimo.xml.ns.deployment_1.PatternType;
//import org.apache.geronimo.xml.ns.security_1.RoleMappingsType;
//import org.apache.geronimo.xml.ns.security_1.RoleType;
//import org.apache.geronimo.xml.ns.security_1.SecurityType;
//import org.eclipse.jface.dialogs.Dialog;
//import org.eclipse.jface.viewers.IBaseLabelProvider;
//import org.eclipse.jface.viewers.IContentProvider;
//import org.eclipse.jface.viewers.Viewer;
//import org.eclipse.jface.wizard.Wizard;
//import org.eclipse.jface.wizard.WizardDialog;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.ModifyEvent;
//import org.eclipse.swt.events.ModifyListener;
//import org.eclipse.swt.events.SelectionAdapter;
//import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Combo;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.forms.widgets.FormToolkit;
//import org.eclipse.ui.forms.widgets.Section;
//
///**
// * @version $Rev$ $Date$
// */
//public class SecurityAdvancedSection extends AbstractTableSection {
//
//    private static final String SPECIFY_CREDENTIAL_STORE_MANUALLY = "<specify manually>";
//
//    protected Text credentialStoreRef;
//    protected Button specifyCredentialStoreRefButton;
//    protected Button deleteCredentialStoreRefButton;
//
//    private Hashtable<String, Pattern> credentialStoreList = new Hashtable<String, Pattern>();
//
//    protected Combo defaultSubjectRealmName;
//
//    protected Combo defaultSubjectId;
//
//    protected Button doAsCurrentCaller;
//
//    protected Button useContextHandler;
//
//    public SecurityAdvancedSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style) {
//        super(plan, parent, toolkit, style);
//        createClient();
//    }
//
//    public void createClient() {
//        Section section = getSection();
//        section.setText(getTitle());
//        section.setDescription(getDescription());
//        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
//        Composite clientComposite = createComposite(getSection(), 3);
//        section.setClient(clientComposite);
//
//        Composite composite1 = createComposite(clientComposite, 2);
//        composite1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
//
//        doAsCurrentCaller = toolkit.createButton(composite1, CommonMessages.securityDoasCurrentCaller,
//                SWT.CHECK);
//        doAsCurrentCaller.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//        doAsCurrentCaller.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent e) {
//                setDoAsCurrentCaller();
//                markDirty();
//            }
//        });
//        doAsCurrentCaller.setSelection(isDoasCurrentCaller());
//
//        useContextHandler = toolkit.createButton(composite1, CommonMessages.securityUseContextHandler,
//                SWT.CHECK);
//        useContextHandler.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
//        useContextHandler.addSelectionListener(new SelectionAdapter() {
//            public void widgetSelected(SelectionEvent e) {
//                setUseContextHandler();
//                markDirty();
//            }
//        });
//        useContextHandler.setSelection(isUseContextHandler());
//
//        createLabel(clientComposite, CommonMessages.securityCredentialStore);
//        credentialStoreRef = new Text(clientComposite, SWT.READ_ONLY| SWT.BORDER);
//        credentialStoreRef.setText("<credential store ref name will go here>");
//        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
//        gridData.widthHint = 300;
//        credentialStoreRef.setLayoutData(gridData);
//
//        Composite composite2 = toolkit.createComposite(clientComposite);
//        GridLayout layout = new GridLayout();
//        layout.numColumns = 2;
//        layout.marginHeight = 5;
//        layout.marginWidth = 0;
//        layout.verticalSpacing = 5;
//        layout.horizontalSpacing = 5;
//        composite2.setLayout(layout);
//        composite2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
//        specifyCredentialStoreRefButton = toolkit.createButton(composite2, CommonMessages.edit, SWT.NONE);
//        specifyCredentialStoreRefButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
//        specifyCredentialStoreRefButton.addSelectionListener(new SelectionAdapter() {
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//                Wizard wizard = getCredentialStoreRefWizard();
//                WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
//                dialog.open();
//                if (dialog.getReturnCode() == Dialog.OK) {
//                    setCredentialStoreRef();
//                    toggleAdvancedSettings();
//                    markDirty();
//                }
//            }
//        });
//        deleteCredentialStoreRefButton = toolkit.createButton(composite2, CommonMessages.remove, SWT.NONE);
//        deleteCredentialStoreRefButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
//        deleteCredentialStoreRefButton.addSelectionListener(new SelectionAdapter(){
//            @Override
//            public void widgetSelected(SelectionEvent e) {
//            }
//        });
//
//        createLabel(clientComposite, CommonMessages.securityDefaultSubject);
//        createLabel(clientComposite, "");
//        createLabel(clientComposite, "");
//        //group = createGroup(clientComposite, CommonMessages.securityDefaultSubject);
//
//        //createLabel(clientComposite, "");
//        //Composite composite3 = createComposite(clientComposite, 2);
//        //composite3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
//        createLabel(clientComposite, CommonMessages.securityDefaultSubjectRealmName).setLayoutData(
//                new GridData(SWT.RIGHT, SWT.CENTER, false, false));
//        defaultSubjectRealmName = new Combo(clientComposite, SWT.SINGLE | SWT.DROP_DOWN);
//        defaultSubjectRealmName.add(getDefaultSubjectRealmName());
//        defaultSubjectRealmName.select(0);
//        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
//        gridData.widthHint = 100;
//        defaultSubjectRealmName.setLayoutData(gridData);
//        defaultSubjectRealmName.addModifyListener(new ModifyListener() {
//            public void modifyText(ModifyEvent e) {
//                setDefaultSubject();
//                markDirty();
//            }
//        });
//        defaultSubjectRealmName.pack();
//
//        //createLabel(clientComposite, "");
//        //Composite composite4 = createComposite(clientComposite, 2);
//        //composite4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
//        createLabel(clientComposite, CommonMessages.securityDefaultSubjectId).setLayoutData(
//                new GridData(SWT.RIGHT, SWT.CENTER, false, false));
//        defaultSubjectId = new Combo(clientComposite, SWT.SINGLE | SWT.DROP_DOWN);
//        defaultSubjectId.add(getDefaultSubjectId());
//        defaultSubjectId.select(0);
//        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
//        gridData.widthHint = 100;
//        defaultSubjectId.setLayoutData(gridData);
//        defaultSubjectId.addModifyListener(new ModifyListener() {
//            public void modifyText(ModifyEvent e) {
//                setDefaultSubject();
//                markDirty();
//            }
//        });
//        defaultSubjectId.pack();
//
//        //group = createGroup(clientComposite, CommonMessages.securityRunAsSubjects);
//        createLabel(clientComposite, "").setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
//        createLabel(clientComposite, CommonMessages.securityRunAsSubjects).setLayoutData(
//                new GridData(SWT.LEFT, SWT.TOP, false, false));
//        createViewer(clientComposite);
//        viewer.setContentProvider(getContentProvider());
//        viewer.setLabelProvider(getLabelProvider());
//        viewer.setInput(getInput());
//
//        Composite buttonComposite = createButtonComposite(clientComposite);
//        createAddButton(buttonComposite);
//        createRemoveButton(buttonComposite);
//        createEditButton(buttonComposite);
//        activateButtons();
//
//        section.setExpanded(false);
//        toggleAdvancedSettings();
//    }
//
//    private Wizard getCredentialStoreRefWizard() {
//        return null;
//    }
//
//    private void toggleAdvancedSettings() {
//        boolean enable = false;
//        if (getSecurity() != null && getSecurity().getCredentialStoreRef() != null) {
//            enable = true;
//        }
//        defaultSubjectRealmName.setEnabled(enable);
//        defaultSubjectId.setEnabled(enable);
//        table.setEnabled(enable);
//        activateAddButton();
//    }
//
//    @Override
//    public Object getInput() {
//        return SecurityRoleMappingSection.getRoleMappings(getPlan(), false);
//    }
//
//    @Override
//    public IContentProvider getContentProvider() {
//        return new ContentProvider() {
//            @Override
//            public Object[] getElements(Object inputElement) {
//                if (inputElement instanceof RoleMappingsType) {
//                    return ((RoleMappingsType) inputElement).getRole().toArray();
//                }
//                return super.getElements(inputElement);
//            }
//        };
//    }
//
//    @Override
//    protected boolean filter(Viewer viewer, Object parentElement, Object element) {
//        if (element instanceof RoleType) {
//            return ((RoleType) element).getRunAsSubject() != null;
//        }
//        return false;
//    }
//
//    @Override
//    public IBaseLabelProvider getLabelProvider() {
//        return new LabelProvider() {
//            @Override
//            public String getColumnText(Object element, int columnIndex) {
//                if (element instanceof RoleType) {
//                	RoleType role = (RoleType) element;
//                    switch (columnIndex) {
//                    case 0:
//                        return role.getRoleName();
//                    case 1:
//                        return role.getRunAsSubject().getRealm();
//                    case 2:
//                        return role.getRunAsSubject().getId();
//                    }
//                }
//                return "";
//            }
//        };
//    }
//
//    @Override
//    protected void removeItem(Object selectedItem) {
//        if (selectedItem instanceof RoleType) {
//            ((RoleType) selectedItem).setRunAsSubject(null);
//            return;
//        }
//        super.removeItem(selectedItem);
//    }
//
//    @Override
//    protected Wizard getWizard() {
//        return new SecurityRunAsSubjectWizard(this);
//    }
//
//    @Override
//    public void activateAddButton() {
//        if (getSecurity() != null && getSecurity().getCredentialStoreRef() != null
//                && getRolesWithoutRunAsSubject().length > 0) {
//            addButton.setEnabled(true);
//        } else {
//            addButton.setEnabled(false);
//        }
//    }
//
//    public RoleType getRole(String roleName) {
//        List<RoleType> roles = SecurityRoleMappingSection.getRoles(getPlan(), false);
//        for (int i = 0; i < roles.size(); i++) {
//        	RoleType role = roles.get(i);
//            if (role.getRoleName().equals(roleName)) {
//                return role;
//            }
//        }
//        return null;
//    }
//
//    public String[] getRolesWithoutRunAsSubject() {
//        List<RoleType> roles = SecurityRoleMappingSection.getRoles(getPlan(), false);
//        List<RoleType> rolesWithoutRunAsSubject = new ArrayList<RoleType>();
//        for (int i = 0; i < roles.size(); i++) {
//        	RoleType role = roles.get(i);
//            if(role.getRunAsSubject() == null) {
//                rolesWithoutRunAsSubject.add(role);
//            }
//        }
//        String[] roleNames = new String[rolesWithoutRunAsSubject.size()];
//        for (int i = 0; i < rolesWithoutRunAsSubject.size(); i++) {
//            roleNames[i] = rolesWithoutRunAsSubject.get(i).getRoleName();
//        }
//        return roleNames;
//    }
//
//    /*private void populateCredentialStores() {
//        String credentialStoreRefName = getCredentialStoreRefName();
//        if (credentialStoreRefName.length() > 0) {
//            credentialStoreRef.add(""); //users will select this empty string to unset credentialStoreRef
//        }
//        credentialStoreRef.add(credentialStoreRefName);
//        List<Pattern> deployedCredentialStores = GeronimoServerInfo.getInstance().getDeployedCredentialStores();
//        Pattern pattern = new Pattern();
//        pattern.setCustomFoo(credentialStoreRefName);
//        if (deployedCredentialStores.contains(pattern)) {
//            deployedCredentialStores.remove(pattern);
//        }
//        for (int i = 0; i < deployedCredentialStores.size(); i++) {
//            credentialStoreRef.add(deployedCredentialStores.get(i).toString());
//        }
//        credentialStoreRef.add(SPECIFY_CREDENTIAL_STORE_MANUALLY);
//    }*/
//
//    @Override
//    public List getObjectContainer() {
//        return getSecurity().getRoleMappings().getRole();
//    }
//
//    @Override
//    public Class getTableEntryObjectType() {
//        return Role.class;
//    }
//
//    @Override
//    public String[] getTableColumnNames() {
//        return new String[] { CommonMessages.securityRunAsSubjectRole, CommonMessages.securityRunAsSubjectRealm,
//                CommonMessages.securityRunAsSubjectId };
//    }
//
//    @Override
//    public String getTitle() {
//        return CommonMessages.editorSectionSecurityAdvancedTitle;
//    }
//
//    @Override
//    public String getDescription() {
//        return CommonMessages.editorSectionSecurityAdvancedDescription;
//    }
//
//    private SecurityType getSecurity() {
//        return JAXBModelUtils.getSecurity(getPlan());
//    }
//
//    private String getCredentialStoreRefName() {
//        if (getSecurity() != null) {
//            PatternType credentialStoreRef = getSecurity().getCredentialStoreRef();
//            if (credentialStoreRef != null) {
//                return credentialStoreRef.getCustomFoo();
//            }
//        }
//        return "";
//    }
//
//    private String getDefaultSubjectRealmName() {
//        if (getSecurity() != null) {
//            SubjectInfo subjectInfo = getSecurity().getDefaultSubject();
//            if (subjectInfo != null) {
//                return subjectInfo.getRealm();
//            }
//        }
//        return "";
//    }
//
//    private String getDefaultSubjectId() {
//        if (getSecurity() != null) {
//            SubjectInfo subjectInfo = getSecurity().getDefaultSubject();
//            if (subjectInfo != null) {
//                return subjectInfo.getId();
//            }
//        }
//        return "";
//    }
//
//    private boolean isDoasCurrentCaller() {
//        if (getSecurity() != null) {
//            return getSecurity().isDoasCurrentCaller();
//        }
//        return false;
//    }
//
//    private boolean isUseContextHandler() {
//        if (getSecurity() != null) {
//            return getSecurity().isUseContextHandler();
//        }
//        return false;
//    }
//
//    private void setCredentialStoreRef() {
//        if (getSecurity() != null) {
//            String credentialStoreRefName = credentialStoreRef.getText();
//            if (credentialStoreRefName.trim().length() > 0) {
//            	PatternType credentialStoreRef = new PatternType();
//                // credentialStoreRef.setGroupId("org.apache.geronimo.framework");
//                // credentialStoreRef.setArtifactId("server-security-config");
//                //credentialStoreRef.setCustomFoo(credentialStoreRef.getText());
//                getSecurity().setCredentialStoreRef(credentialStoreRef);
//            } else {
//                getSecurity().setCredentialStoreRef(null);
//            }
//        }
//    }
//
//    private void setDefaultSubject() {
//        if (getSecurity() != null) {
//            String realmName = defaultSubjectRealmName.getText();
//            String realmId = defaultSubjectId.getText();
//            if (realmName.trim().length() > 0 && realmId.trim().length() > 0) {
//                SubjectInfo defaultSubject = new SubjectInfo();
//                defaultSubject.setRealm(realmName);
//                defaultSubject.setId(realmId);
//                getSecurity().setDefaultSubject(defaultSubject);
//            } else {
//                getSecurity().setDefaultSubject(null);
//            }
//        }
//    }
//
//    private void setDoAsCurrentCaller() {
//        if (getSecurity() != null) {
//            getSecurity().setDoasCurrentCaller(doAsCurrentCaller.getSelection());
//        }
//    }
//
//    private void setUseContextHandler() {
//        if (getSecurity() != null) {
//            getSecurity().setUseContextHandler(useContextHandler.getSelection());
//        }
//    }
//}
