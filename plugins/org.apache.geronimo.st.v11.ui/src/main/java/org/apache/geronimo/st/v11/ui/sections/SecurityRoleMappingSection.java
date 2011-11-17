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
package org.apache.geronimo.st.v11.ui.sections;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.j2ee.security.DistinguishedNameType;
import org.apache.geronimo.j2ee.security.LoginDomainPrincipalType;
import org.apache.geronimo.j2ee.security.PrincipalType;
import org.apache.geronimo.j2ee.security.RealmPrincipalType;
import org.apache.geronimo.j2ee.security.RoleMappingsType;
import org.apache.geronimo.j2ee.security.RoleType;
import org.apache.geronimo.j2ee.security.SecurityType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils;
import org.apache.geronimo.st.core.descriptor.AbstractDeploymentDescriptor;
import org.apache.geronimo.st.core.descriptor.ApplicationDeploymentDescriptor;
import org.apache.geronimo.st.core.descriptor.EjbDeploymentDescriptor;
import org.apache.geronimo.st.core.descriptor.WebDeploymentDescriptor;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.SecurityRoleMappingWizard;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class SecurityRoleMappingSection extends AbstractTreeSection {

    public SecurityRoleMappingSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style) {
        super(plan, parent, toolkit, style);
        createClient();
    }

    public SecurityRoleMappingSection(JAXBElement plan, AbstractDeploymentDescriptor descriptor, Composite parent,
            FormToolkit toolkit, int style) {
        super(plan, descriptor, parent, toolkit, style);
        createClient();
    }

    @Override
    protected boolean isRequiredSyncToolbarAction() {
        return true;
    }

    @Override
    protected IAction getSyncAction() {
        return new SyncAction(CommonMessages.securityRefreshRoles) {
            @Override
            public void run() {
                if( addRolesFromDeploymentDescriptor() ) {
                    markDirty();
                }
            }
        };
    }

    @Override
    protected void activateRemoveButton() {
        if (tree.getSelectionCount() > 0 && tree.getSelection()[0].getParentItem() != null) {
            removeButton.setEnabled(true);
        } else {
            removeButton.setEnabled(false);
        }
    }

    @Override
    protected void activateEditButton() {
        if (tree.getSelectionCount() > 0 && tree.getSelection()[0].getParentItem() != null) {
            editButton.setEnabled(true);
        } else {
            editButton.setEnabled(false);
        }
    }

    @Override
    protected void activateAddButton() {
        if (tree.getSelectionCount() > 0 && tree.getSelection()[0].getParentItem() == null) {
            addButton.setEnabled(true);
        } else {
            addButton.setEnabled(false);
        }
    }

    @Override
    public void handleDelete() {
        TreeItem selectedItem = tree.getSelection()[0];
        Object selectedObject = selectedItem.getData();
        RoleType role = (RoleType) selectedItem.getParentItem().getData();
        try {
			((ArrayList) JAXBUtils.getValue(role, selectedObject.getClass().getSimpleName())).remove(selectedObject);
		} catch (Exception e) {
            MessageDialog.openError(Display.getCurrent().getActiveShell(),Messages.error, e.getMessage());
		}
    }

    public RoleType getSelectedObject() {
        return (RoleType) tree.getSelection()[0].getData();
    }

    @Override
    public String getTitle() {
        return CommonMessages.editorSectionSecurityRolesTitle;
    }

    @Override
    public String getDescription() {
        return CommonMessages.editorSectionSecurityRolesDescription;
    }

    @Override
    public Wizard getWizard() {
        return new SecurityRoleMappingWizard(this);
    }

    @Override
    public Class getTableEntryObjectType() {
        return RoleType.class;
    }

    @Override
    public List getObjectContainer() {
        return getRoles(getPlan(), false);
    }

    public static List<RoleType> getRoles(JAXBElement plan, boolean create) {
    	RoleMappingsType roleMappings = getRoleMappings(plan, create);
        if (roleMappings != null) {
            return roleMappings.getRole();
        }
        return null;
    }

    public static RoleMappingsType getRoleMappings(JAXBElement plan, boolean create) {
    	SecurityType security = JAXBModelUtils.getSecurity(plan);
        if (security == null && create) {
            security = new SecurityType();
            JAXBModelUtils.setSecurity(plan, security);
        }
        if (security != null) {
        	RoleMappingsType roleMappings = security.getRoleMappings();
            if (roleMappings == null && create) {
                roleMappings = new RoleMappingsType();
                security.setRoleMappings(roleMappings);
            }
            return roleMappings;
        }
        return null;
    }

    protected boolean addRolesFromDeploymentDescriptor() {
        List<String> declaredRoleNames = null;
        if (WebDeploymentDescriptor.class.isInstance(getDescriptor())) {
            declaredRoleNames = ((WebDeploymentDescriptor)getDescriptor()).getSecurityRoles();
        }
        else if (ApplicationDeploymentDescriptor.class.isInstance(getDescriptor())) {
            declaredRoleNames = ((ApplicationDeploymentDescriptor)getDescriptor()).getSecurityRoles();
        }
        else if (EjbDeploymentDescriptor.class.isInstance(getDescriptor())) {
            declaredRoleNames = ((EjbDeploymentDescriptor)getDescriptor()).getSecurityRoles();
        }
        
        if (declaredRoleNames == null || declaredRoleNames.size() <= 0) {
            return false;
        }
        List<RoleType> definedRoles = getRoles(getPlan(), true);
        List<RoleType> newRoles = new ArrayList<RoleType>();
        for (int i = 0; i < declaredRoleNames.size(); i++) {
            String roleName = declaredRoleNames.get(i);
            boolean roleExists = false;
            for (int j = 0; j < definedRoles.size(); j++) {
                if (definedRoles.get(j).getRoleName().equals(roleName)) {
                    roleExists = true;
                    break;
                }
            }
            if (!roleExists) {
            	RoleType newRole = new RoleType();
                newRole.setRoleName(roleName);
                newRoles.add(newRole);
            }
        }
        definedRoles.addAll(newRoles);
        return newRoles.size() > 0;
    }

    @Override
    public Object getInput() {
        addRolesFromDeploymentDescriptor();
        return getRoleMappings(getPlan(), false);
    }

    @Override
    public ITreeContentProvider getContentProvider() {
        return new ContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return getChildren(inputElement);
            }

            @Override
            public Object[] getChildren(Object parentElement) {
                if (RoleMappingsType.class.isInstance(parentElement)) {
                    return ((RoleMappingsType) parentElement).getRole().toArray();
                }
                if (parentElement instanceof RoleType) {
                	RoleType role = (RoleType) parentElement;
                    return concat(role.getDistinguishedName().toArray(), role.getPrincipal().toArray(), 
                            role.getLoginDomainPrincipal().toArray(), role.getRealmPrincipal().toArray());
                }
                return new String[] {};
            }

            private Object[] concat(Object[] arr1, Object[] arr2, Object[] arr3, Object[] arr4) {
                Object[] all = new Object[arr1.length + arr2.length + arr3.length + arr4.length];
                System.arraycopy(arr1, 0, all, 0, arr1.length);
                System.arraycopy(arr2, 0, all, arr1.length, arr2.length);
                System.arraycopy(arr3, 0, all, arr1.length + arr2.length, arr3.length);
                System.arraycopy(arr4, 0, all, arr1.length + arr2.length + arr3.length, arr4.length);
                return all;
            }
        };
    }

    @Override
    public ILabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getText(Object element) {
                if (RoleType.class.isInstance(element)) {
                	RoleType role = (RoleType) element;
                    return Messages.roleName + ": \"" + role.getRoleName() + "\"";
                }
                else if (element instanceof DistinguishedNameType) {
                	DistinguishedNameType object = (DistinguishedNameType) element;
                    return Messages.distinguishedName + ": \"" + object.getName() + "\"";
                }
                else if (element instanceof RealmPrincipalType) {
                	RealmPrincipalType object = (RealmPrincipalType) element;
                    return Messages.realmPrincipal + ": \"" + object.getName() +
                    "\", " + Messages.clazz + " = \"" + object.getClazz() +
                    "\", " + Messages.domainName + " = \"" + object.getDomainName() +
                    "\", " + Messages.realmName + " = \"" + object.getRealmName() +"\"";
                    
                }
                else if (element instanceof LoginDomainPrincipalType) {
                	LoginDomainPrincipalType object = (LoginDomainPrincipalType) element;
                    return Messages.loginDomainPrincipal + ": = \"" + object.getName() +
                    "\", " + Messages.clazz + " = \"" + object.getClazz() +
                    "\", " + Messages.domainName + " = \"" + object.getDomainName() +"\"";
                }
                else if (element instanceof PrincipalType) {
                	PrincipalType object = (PrincipalType) element;
                    return Messages.principal + " = \"" + object.getName() +
                    "\", " + Messages.clazz + " = \"" + object.getClazz() + "\"";
                }

                return null;
            }

            @Override
            public Image getImage(Object arg0) {
                return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee",
                        "icons/full/obj16/security_role.gif").createImage();
            }
        };
    }
}
