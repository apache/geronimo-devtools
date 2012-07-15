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
package org.apache.geronimo.st.v21.ui.sections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.deployment.Pattern;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.RoleMappings;
import org.apache.geronimo.jee.security.Security;
import org.apache.geronimo.jee.security.SubjectInfo;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.core.GeronimoServerInfoManager;
import org.apache.geronimo.st.v21.ui.wizards.SecurityRunAsSubjectWizard;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public class SecurityAdvancedSection extends AbstractTableSection {

    //private static final String SPECIFY_CREDENTIAL_STORE_MANUALLY = "<specify manually>";

    protected Combo credentialStoreRef;
//    protected Button specifyCredentialStoreRefButton;
//    protected Button deleteCredentialStoreRefButton;

    private Hashtable<String, Pattern> credentialStoreList = new Hashtable<String, Pattern>();

    protected Combo defaultSubjectRealmName;

    protected Combo defaultSubjectId;

    protected Button doAsCurrentCaller;

    protected Button useContextHandler;
    
    protected static String runtimeVersionNumber;
    
    private HashMap<Pattern,HashMap<String,ArrayList<String>>> credentialStoreAttributes;

    public SecurityAdvancedSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, String runtimeVersion) {
        super(plan, parent, toolkit, style);
        runtimeVersionNumber=runtimeVersion;
        createClient();
    }

    public void createClient() {
        Section section = getSection();
        section.setText(getTitle());
        section.setDescription(getDescription());
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        Composite clientComposite = createComposite(getSection(), 3);
        section.setClient(clientComposite);

        Composite composite1 = createComposite(clientComposite, 2);
        composite1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

        doAsCurrentCaller = toolkit.createButton(composite1, CommonMessages.securityDoasCurrentCaller,
                SWT.CHECK);
        doAsCurrentCaller.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        doAsCurrentCaller.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                setDoAsCurrentCaller();
                markDirty();
            }
        });
        doAsCurrentCaller.setSelection(isDoasCurrentCaller());

        useContextHandler = toolkit.createButton(composite1, CommonMessages.securityUseContextHandler,
                SWT.CHECK);
        useContextHandler.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        useContextHandler.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                setUseContextHandler();
                markDirty();
            }
        });
        useContextHandler.setSelection(isUseContextHandler());

        createLabel(clientComposite, CommonMessages.securityCredentialStore);
        credentialStoreRef = new Combo(clientComposite, SWT.READ_ONLY| SWT.BORDER);
   //   credentialStoreRef.setText("<credential store ref name will go here>");
        GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false, false);
        gridData.widthHint = 300;
        credentialStoreRef.setLayoutData(gridData);
        populateCredentialStores();
        credentialStoreRef.addSelectionListener(new SelectionAdapter(){

			public void widgetSelected(SelectionEvent arg0) {
				setCredentialStoreRef();
				populateDefaultSubjectRealmName();
        		populateDefaultSubjectId();
				toggleAdvancedSettings();
				markDirty();
			}
        	
        });

        Composite composite2 = toolkit.createComposite(clientComposite);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 5;
        layout.marginWidth = 0;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 5;
        composite2.setLayout(layout);
        composite2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    /*    specifyCredentialStoreRefButton = toolkit.createButton(composite2, CommonMessages.edit, SWT.NONE);
        specifyCredentialStoreRefButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        specifyCredentialStoreRefButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Wizard wizard = getCredentialStoreRefWizard();
                WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                dialog.open();
                if (dialog.getReturnCode() == Dialog.OK) {
                    setCredentialStoreRef();
                    toggleAdvancedSettings();
                    markDirty();
                }
            }
        });
        deleteCredentialStoreRefButton = toolkit.createButton(composite2, CommonMessages.remove, SWT.NONE);
        deleteCredentialStoreRefButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        deleteCredentialStoreRefButton.addSelectionListener(new SelectionAdapter(){
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });*/

        createLabel(clientComposite, CommonMessages.securityDefaultSubject);
        createLabel(clientComposite, "");
        createLabel(clientComposite, "");
        //group = createGroup(clientComposite, CommonMessages.securityDefaultSubject);

        //createLabel(clientComposite, "");
        //Composite composite3 = createComposite(clientComposite, 2);
        //composite3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        createLabel(clientComposite, CommonMessages.securityDefaultSubjectRealmName).setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        defaultSubjectRealmName = new Combo(clientComposite, SWT.SINGLE | SWT.DROP_DOWN |SWT.READ_ONLY);
        populateDefaultSubjectRealmName();
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gridData.widthHint = 100;
        defaultSubjectRealmName.setLayoutData(gridData);
        defaultSubjectRealmName.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent arg0) {
        		populateDefaultSubjectId();
                markDirty();
            }
        });
        defaultSubjectRealmName.pack();

        //createLabel(clientComposite, "");
        //Composite composite4 = createComposite(clientComposite, 2);
        //composite4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        createLabel(clientComposite, CommonMessages.securityDefaultSubjectId).setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false));
        defaultSubjectId = new Combo(clientComposite, SWT.SINGLE | SWT.DROP_DOWN |SWT.READ_ONLY);
        defaultSubjectId.add(getDefaultSubjectId());
        gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
        gridData.widthHint = 100;
        defaultSubjectId.setLayoutData(gridData);
        populateDefaultSubjectId();
        defaultSubjectId.addSelectionListener(new SelectionAdapter() {
        	public void widgetSelected(SelectionEvent arg0) {
                setDefaultSubject();
                markDirty();
            }
        });
        defaultSubjectId.pack();

        //group = createGroup(clientComposite, CommonMessages.securityRunAsSubjects);
        createLabel(clientComposite, "").setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        createLabel(clientComposite, CommonMessages.securityRunAsSubjects).setLayoutData(
                new GridData(SWT.LEFT, SWT.TOP, false, false));
        createViewer(clientComposite);
        viewer.setContentProvider(getContentProvider());
        viewer.setLabelProvider(getLabelProvider());
        viewer.setInput(getInput());

        Composite buttonComposite = createButtonComposite(clientComposite);
        createAddButton(buttonComposite);
        createRemoveButton(buttonComposite);
        createEditButton(buttonComposite);
        activateButtons();

        section.setExpanded(false);
        toggleAdvancedSettings();
    }

    private void populateDefaultSubjectRealmName() {
    	 defaultSubjectRealmName.removeAll();
    	
    	 String realmName = getDefaultSubjectRealmName();
    	 
    	 defaultSubjectRealmName.add(realmName);
         if (realmName.length() > 0) {
        	 defaultSubjectRealmName.add("");
         }
        
         String credentialStoreName = credentialStoreRef.getText();
         if (credentialStoreName!=null && credentialStoreName.length()!=0) {
        	 Map<String,ArrayList<String>> realmNameMap = credentialStoreAttributes.get(credentialStoreList.get(credentialStoreName));
        	 if (realmNameMap!=null) {
        		 Set<String> nameSet = realmNameMap.keySet();
        		 for (String name: nameSet){
        			 if (!name.equals(realmName))
        				 defaultSubjectRealmName.add(name);
        		 }
        	 }
         }   
         defaultSubjectRealmName.select(0);
	}
    
    private void populateDefaultSubjectId() {
	   	 defaultSubjectId.removeAll();
	   	
	   	 String subjectId = getDefaultSubjectId();
	   	 defaultSubjectId.add(subjectId);
	     if (subjectId.length() > 0) {
	       	defaultSubjectId.add(""); 
	     }
	        
	     String credentialStoreName = credentialStoreRef.getText();
	     String defaultRealmName = defaultSubjectRealmName.getText();
	     if (credentialStoreName!=null && credentialStoreName.length()!=0 && defaultRealmName!=null && defaultRealmName.length()!=0) {
	    	 Map<String,ArrayList<String>> realmNameMap = credentialStoreAttributes.get(credentialStoreList.get(credentialStoreName));
	    	 if (realmNameMap!=null) {
	    		 ArrayList<String> ids = realmNameMap.get(defaultRealmName);
	    		 for (String id: ids){
	    			 if (!id.equals(subjectId))   				 
	    				 defaultSubjectId.add(id);
	    		 }
	    	 }
	     }   
	     defaultSubjectId.select(0);
    }

/*    private Wizard getCredentialStoreRefWizard() {
        return null;
    }*/

    private void toggleAdvancedSettings() {
        boolean enable = false;
        
        if (getSecurity() != null && getSecurity().getCredentialStoreRef() != null 
        		&& (defaultSubjectRealmName.getItemCount() > 0 || defaultSubjectId.getItemCount() >0)) {
            enable = true;
        }
        
        defaultSubjectRealmName.setEnabled(enable);
        defaultSubjectId.setEnabled(enable);
        table.setEnabled(enable);        
        activateAddButton();
    }

    @Override
    public Object getInput() {
        return SecurityRoleMappingSection.getRoleMappings(getPlan(), false);
    }

    @Override
    public IContentProvider getContentProvider() {
        return new ContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof RoleMappings) {
                    return ((RoleMappings) inputElement).getRole().toArray();
                }
                return super.getElements(inputElement);
            }
        };
    }

    @Override
    protected boolean filter(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof Role) {
            return ((Role) element).getRunAsSubject() != null;
        }
        return false;
    }

    @Override
    public IBaseLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (element instanceof Role) {
                    Role role = (Role) element;
                    switch (columnIndex) {
                    case 0:
                        return role.getRoleName();
                    case 1:
                        return role.getRunAsSubject().getRealm();
                    case 2:
                        return role.getRunAsSubject().getId();
                    }
                }
                return "";
            }
        };
    }

    @Override
    protected void removeItem(Object selectedItem) {
        if (selectedItem instanceof Role) {
            ((Role) selectedItem).setRunAsSubject(null);
            return;
        }
        super.removeItem(selectedItem);
    }

    @Override
    protected Wizard getWizard() {
        return new SecurityRunAsSubjectWizard(this);
    }

    @Override
    public void activateAddButton() {
        if (getSecurity() != null && getSecurity().getCredentialStoreRef() != null
                && getRolesWithoutRunAsSubject().length > 0) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    public Role getRole(String roleName) {
        List<Role> roles = SecurityRoleMappingSection.getRoles(getPlan(), false);
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        return null;
    }

    public String[] getRolesWithoutRunAsSubject() {
        List<Role> roles = SecurityRoleMappingSection.getRoles(getPlan(), false);
        List<Role> rolesWithoutRunAsSubject = new ArrayList<Role>();
        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            if(role.getRunAsSubject() == null) {
                rolesWithoutRunAsSubject.add(role);
            }
        }
        String[] roleNames = new String[rolesWithoutRunAsSubject.size()];
        for (int i = 0; i < rolesWithoutRunAsSubject.size(); i++) {
            roleNames[i] = rolesWithoutRunAsSubject.get(i).getRoleName();
        }
        return roleNames;
    }

    private void populateCredentialStores() {
        String credentialStoreRefName = getCredentialStoreRefName();
        credentialStoreRef.add(credentialStoreRefName);
        if (credentialStoreRefName.length() > 0) {
            credentialStoreRef.add(""); //users will select this empty string to unset credentialStoreRef
        }
        
        List<Pattern> deployedCredentialStores = GeronimoServerInfoManager.getProvider(runtimeVersionNumber).getDeployedCredentialStores();
        Pattern pattern = new Pattern();
        pattern.setCustomFoo(credentialStoreRefName);
        if (deployedCredentialStores.contains(pattern)) {
            deployedCredentialStores.remove(pattern);
        }
        for (int i = 0; i < deployedCredentialStores.size(); i++) {
        	String credentialStoreName = deployedCredentialStores.get(i).toString();
        	//in case that module is null, replace the ending string of pattern.toString() with type 
        	credentialStoreName = credentialStoreName.substring(0, credentialStoreName.lastIndexOf("/")+1).concat(deployedCredentialStores.get(i).getType()+")");
            credentialStoreRef.add(credentialStoreName);
            credentialStoreList.put(credentialStoreName, deployedCredentialStores.get(i));
        }
        credentialStoreAttributes = GeronimoServerInfoManager.getProvider(runtimeVersionNumber).getDeployedCredentialStoreAttributes();
        credentialStoreRef.select(0);
    }

    @Override
    public List getObjectContainer() {
        return getSecurity().getRoleMappings().getRole();
    }

    @Override
    public Class getTableEntryObjectType() {
        return Role.class;
    }

    @Override
    public String[] getTableColumnNames() {
        return new String[] { CommonMessages.securityRunAsSubjectRole, CommonMessages.securityRunAsSubjectRealm,
                CommonMessages.securityRunAsSubjectId };
    }

    @Override
    public String getTitle() {
        return CommonMessages.editorSectionSecurityAdvancedTitle;
    }

    @Override
    public String getDescription() {
        return CommonMessages.editorSectionSecurityAdvancedDescription;
    }

    private Security getSecurity() {
        return JAXBModelUtils.getSecurity(getPlan());
    }

    private String getCredentialStoreRefName() {
        if (getSecurity() != null) {
            Pattern credentialStoreRef = getSecurity().getCredentialStoreRef();
            if (credentialStoreRef != null) {
                return credentialStoreRef.getCustomFoo();
            }
        }
        return "";
    }

    private String getDefaultSubjectRealmName() {
        if (getSecurity() != null) {
            SubjectInfo subjectInfo = getSecurity().getDefaultSubject();
            if (subjectInfo != null) {
                return subjectInfo.getRealm();
            }
        }
        return "";
    }

    private String getDefaultSubjectId() {
        if (getSecurity() != null) {
            SubjectInfo subjectInfo = getSecurity().getDefaultSubject();
            if (subjectInfo != null) {
                return subjectInfo.getId();
            }
        }
        return "";
    }

    private boolean isDoasCurrentCaller() {
        if (getSecurity() != null) {
            return getSecurity().isDoasCurrentCaller();
        }
        return false;
    }

    private boolean isUseContextHandler() {
        if (getSecurity() != null) {
            return getSecurity().isUseContextHandler();
        }
        return false;
    }

    private void setCredentialStoreRef() {
        if (getSecurity() != null) {
            String credentialStoreRefName = credentialStoreRef.getText();
            if (credentialStoreRefName.trim().length() > 0) {
                Pattern credentialStoreRef = credentialStoreList.get(credentialStoreRefName);
                getSecurity().setCredentialStoreRef(credentialStoreRef);
            } else {
                getSecurity().setCredentialStoreRef(null);
            }
        }
    }

    private void setDefaultSubject() {
        if (getSecurity() != null) {
            String realmName = defaultSubjectRealmName.getText();
            String realmId = defaultSubjectId.getText();
            if (realmName.trim().length() > 0 && realmId.trim().length() > 0) {
                SubjectInfo defaultSubject = new SubjectInfo();
                defaultSubject.setRealm(realmName);
                defaultSubject.setId(realmId);
                getSecurity().setDefaultSubject(defaultSubject);
            } else {
                getSecurity().setDefaultSubject(null);
            }
        }
    }

    private void setDoAsCurrentCaller() {
        if (getSecurity() != null) {
            getSecurity().setDoasCurrentCaller(doAsCurrentCaller.getSelection());
        }
    }

    private void setUseContextHandler() {
        if (getSecurity() != null) {
            getSecurity().setUseContextHandler(useContextHandler.getSelection());
        }
    }

	public static String getRuntimeVersionNumber() {
		return runtimeVersionNumber;
	}

	public static void setRuntimeVersionNumber(String runtimeVersionNumber) {
		SecurityAdvancedSection.runtimeVersionNumber = runtimeVersionNumber;
	}
    
}
