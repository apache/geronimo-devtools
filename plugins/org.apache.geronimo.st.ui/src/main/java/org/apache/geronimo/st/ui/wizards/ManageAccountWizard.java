/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.ui.wizards;

import org.apache.geronimo.st.core.operations.GeronimoAccountManager;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ManageAccountWizard extends AbstractWizard {

    protected Button addUser;
    protected Button delUser;
    protected Button editUser;
    
    protected Label userName;
    protected Label groupName;
    protected Label newPassword;
    protected Label confirmPassword;
    
    protected Composite actioncomposite;
    protected Text userNameText;
    protected Combo userNameComobox;
    protected Combo groupComobox;
    protected Text newPasswordText;
    protected Text confirmPasswordText;
    
    private String[] nameList;
    private String[] groupList;
    
    private  GeronimoAccountManager manager;
  
    
    public ManageAccountWizard(GeronimoAccountManager mgr) {
        super();
        manager = mgr;
        nameList = manager.getUserList();
        groupList = manager.getGroupList();
    }
    
    
    public class ManageAccountWizardPage extends AbstractWizardPage {

        public ManageAccountWizardPage(String pageName) {
            super(pageName);
        }


        protected Composite createComposite(Composite parent) {
           Composite composite =super.createComposite(parent);
           GridLayout layout = new GridLayout();
           layout.numColumns = 3;
         //  layout.makeColumnsEqualWidth=true;
           composite.setLayout(layout);
           return composite;
        }
        
        @Override
        protected String getWizardPageDescription() {
           return CommonMessages.manageAccountDescription;
        }

        @Override
        protected String getWizardPageTitle() {
           return CommonMessages.manageAccount;
        }

        public void createControl(final Composite parent) {
            final Composite composite = createComposite(parent);
            addUser = this.createButton(composite, CommonMessages.addUser);
            GridData data = new GridData();
            data.horizontalSpan = 1;
            data.grabExcessHorizontalSpace=true;
            data.horizontalAlignment = GridData.CENTER;
            addUser.setLayoutData(data);
            addUser.addSelectionListener(new SelectionAdapter(){
                public void widgetSelected(SelectionEvent arg0) {
                   updateUIByAction(composite);
                }
            });
            
            delUser = this.createButton(composite, CommonMessages.delUser);
            data = new GridData();
            data.horizontalSpan = 1;
            data.grabExcessHorizontalSpace=true;
            data.horizontalAlignment = GridData.CENTER;
            delUser.setLayoutData(data);
            delUser.addSelectionListener(new SelectionAdapter(){
               public void widgetSelected(SelectionEvent arg0) {
                   updateUIByAction(composite);
               }
            });
            
            editUser = this.createButton(composite, CommonMessages.editUser);
            data = new GridData();
            data.horizontalSpan = 1;
            data.grabExcessHorizontalSpace=true;
            data.horizontalAlignment = GridData.CENTER;
            editUser.setLayoutData(data);
            editUser.addSelectionListener(new SelectionAdapter(){
                public void widgetSelected(SelectionEvent arg0) {
                    updateUIByAction(composite);
                }
             });            
       
         //   updateUIByAction(composite);
            
            setControl(composite);
        }
        
        private void updateUIByAction(Composite composite){
            if (actioncomposite!=null) actioncomposite.dispose();   
            
            actioncomposite = this.createComposite(composite);
            GridLayout layout = new GridLayout();
            layout.numColumns=2;
            actioncomposite.setLayout(layout);
            GridData data = new GridData(GridData.FILL, GridData.FILL,true,true);
            data.horizontalSpan =3;
            actioncomposite.setLayoutData(data);
            
            if (addUser.getSelection()){
                userName = this.createLabel(actioncomposite, CommonMessages.username);
                userNameText = this.createTextField(actioncomposite, "",false);
                
                groupName = this.createLabel(actioncomposite, CommonMessages.groupName);
                groupComobox = this.createCombo(actioncomposite, groupList, false);
                groupComobox.select(0);
                
                newPassword = this.createLabel(actioncomposite, CommonMessages.newPassword);
                newPasswordText = this.createTextField(actioncomposite, "",true);
                
                confirmPassword = this.createLabel(actioncomposite, CommonMessages.confirmPassword);            
                confirmPasswordText = this.createTextField(actioncomposite,"",true);
            }else if (delUser.getSelection()){
                userName = this.createLabel(actioncomposite, CommonMessages.username);
                userNameComobox = this.createCombo(actioncomposite, nameList, false);
                userNameComobox.select(0);
                
            }else{
                userName = this.createLabel(actioncomposite, CommonMessages.username);
                userNameComobox = this.createCombo(actioncomposite, nameList, true);
                userNameComobox.select(0);
                
                newPassword = this.createLabel(actioncomposite, CommonMessages.newPassword);
                newPasswordText = this.createTextField(actioncomposite, "",true);
                
                confirmPassword = this.createLabel(actioncomposite, CommonMessages.confirmPassword);            
                confirmPasswordText = this.createTextField(actioncomposite,"",true);
            }
           
            
            composite.layout();
        }
        
    }
    
   

    @Override
    protected String getAddWizardWindowTitle() {
        return Messages.wizardTitle_ManageAccount;
    }

    @Override
    protected String getEditWizardWindowTitle() {
        return Messages.wizardTitle_ManageAccount;
    }

    @Override
    public boolean performFinish() {
        if (addUser.getSelection()){
            String newPwd = newPasswordText.getText();
            String confirmPwd = confirmPasswordText.getText();
            if (!newPwd.equals(confirmPwd)){
                MessageDialog.openError(Display.getDefault().getActiveShell(), CommonMessages.addUser , CommonMessages.passwordNotEqual);
                return false;
            }
            
            String name = userNameText.getText();
            String groupName = groupComobox.getItem(groupComobox.getSelectionIndex());
            manager.addUser(name, groupName, newPwd);
        }else if (delUser.getSelection()){
            String name = userNameComobox.getItem(userNameComobox.getSelectionIndex());
            manager.delUser(name);
        }else{
            String newPwd = newPasswordText.getText();
            String confirmPwd = confirmPasswordText.getText();
            if (!newPwd.equals(confirmPwd)){
                MessageDialog.openError(Display.getDefault().getActiveShell(), CommonMessages.editUser , CommonMessages.passwordNotEqual);
                return false;
            }
            String groupName = groupComobox.getItem(groupComobox.getSelectionIndex());
            String newName = userNameText.getText();
            String oldName = userNameComobox.getItem(userNameComobox.getSelectionIndex());
            
            manager.modifyUser(oldName, newName, groupName, newPwd);
        }
        
        try {
            manager.persist();
        } catch (Exception e) {
            MessageDialog.openError(Display.getDefault().getActiveShell(), CommonMessages.manageAccount , CommonMessages.cannotSave);
            return false;
        }
        return true;
    }
    
    public void addPages() {
        addPage(new ManageAccountWizardPage(CommonMessages.manageAccount));
    }
    
}
