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

import org.apache.geronimo.st.core.operations.IGeronimoServerPluginManager;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class ServerCustomAssemblyWizard extends AbstractWizard {

    private Table pluginTable;
    
    protected Text group;
    protected Text artifact;
    protected Text version;
    protected Text type;
    protected Text serverPath;

    protected IGeronimoServerPluginManager customAssembly;

    public ServerCustomAssemblyWizard(IGeronimoServerPluginManager customAssembly) {
        super();
        this.customAssembly = customAssembly;
    }

    public void addPages() {
        addPage(new ServerCustomAssemblyWizardPage("page0"));
    }

    public class ServerCustomAssemblyWizardPage extends AbstractWizardPage {

        public ServerCustomAssemblyWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData());
            Composite composite = createComposite(parent);

            createLabel(composite, CommonMessages.groupId);
            group = createTextField(composite, "");
            createLabel(composite, CommonMessages.artifactId);
            artifact = createTextField(composite, "");
            createLabel(composite, CommonMessages.version);
            version = createTextField(composite, "1.0");
            createLabel(composite, CommonMessages.type);
            type = createTextField(composite, "tar.gz");
            createLabel(composite, CommonMessages.path);
            serverPath = createTextField(composite, "var/temp/assembly");
            createTable(composite);
            populateTable();

            group.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					ServerCustomAssemblyWizard.this.getContainer().updateButtons();
				}
            });
            artifact.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					ServerCustomAssemblyWizard.this.getContainer().updateButtons();
				}
            });
            version.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					ServerCustomAssemblyWizard.this.getContainer().updateButtons();
				}
            });
            type.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					ServerCustomAssemblyWizard.this.getContainer().updateButtons();
				}
            });
            serverPath.addModifyListener(new ModifyListener(){
				public void modifyText(ModifyEvent arg0) {
					ServerCustomAssemblyWizard.this.getContainer().updateButtons();
				}
            });
            pluginTable.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent arg0) {
					ServerCustomAssemblyWizard.this.getContainer().updateButtons();
				}
            });
            
            setControl(composite);
        }

        public GridData createGridData() {
            GridData data = new GridData();
            data.verticalAlignment = GridData.FILL;
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessVerticalSpace = true;
            data.grabExcessHorizontalSpace = true;
            data.heightHint = 400;
            data.widthHint = 300;
            return data;
        }

        private void createTable(Composite composite) {
            int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

            pluginTable = new Table(composite, style);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            data.horizontalSpan = 2;
            data.horizontalAlignment = GridData.FILL;
            data.heightHint = 250;
            data.widthHint = 350;
            pluginTable.setLayoutData(data);
            pluginTable.setLinesVisible(false);
            pluginTable.setHeaderVisible(true);

            final TableColumn[] column = new TableColumn[1];
            column[0] = new TableColumn(pluginTable, SWT.LEFT, 0);
            column[0].setText(CommonMessages.plugin);
            column[0].setWidth(400);
        }

        public void populateTable() {
            List<String> pluginList = customAssembly.getPluginList();
             
            for (int i = 0; i < pluginList.size(); ++i) {
                TableItem tableItem = new TableItem(pluginTable, SWT.NONE);
                String tableEntry = pluginList.get(i);
                tableItem.setData(tableEntry);
                tableItem.setText(new String[] {tableEntry});
            }
        }

        @Override
        protected String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_ServerCustomAssembly;
        }

        @Override
        protected String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_ServerCustomAssembly;
        }
        
        
    }

    public boolean performFinish() {
        if (isEmpty(group.getText()) || isEmpty(artifact.getText()) ||
            isEmpty(version.getText()) || isEmpty(type.getText()) ||
            isEmpty(serverPath.getText()) || pluginTable.getSelectionCount() == 0) {
            return false;
        }
        customAssembly.assembleServer(group.getText(), artifact.getText(), version.getText(), type.getText(), 
                serverPath.getText(), pluginTable.getSelectionIndices());
        return true;
    }

    @Override
    protected String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_ServerCustomAssembly;
    }

    @Override
    protected String getEditWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_ServerCustomAssembly;
    }
    
    public boolean canFinish(){
    	 if (isEmpty(group.getText()) || isEmpty(artifact.getText()) ||
    	            isEmpty(version.getText()) || isEmpty(type.getText()) ||
    	            isEmpty(serverPath.getText()) || pluginTable.getSelectionCount() == 0) {
    		 return false;
    	 }else return true;
    }
}
