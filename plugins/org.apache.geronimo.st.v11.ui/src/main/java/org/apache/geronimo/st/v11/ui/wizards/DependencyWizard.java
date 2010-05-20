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

import org.apache.geronimo.j2ee.deployment.DependencyType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.st.v11.core.GeronimoServerInfo;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @version $Rev$ $Date$
 */
public class DependencyWizard extends AbstractTableWizard {

    private Table dependencyTable;
    private TabFolder tabFolder;

    public DependencyWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
        return new String[] { "GroupId", "ArtifactId", "Version", "Type" };
    }

    protected String[] dependencyToStringArray(DependencyType dependency) {
        String[] stringArray = new String[getTableColumnEAttributes().length];
        stringArray[0] = dependency.getGroupId();
        stringArray[1] = dependency.getArtifactId();
        stringArray[2] = dependency.getVersion();
        stringArray[3] = dependency.getType();
        return stringArray;
    }

    public void addPages() {
        addPage(new DependencyWizardPage("AddDependencyPage"));
    }

    public class DependencyWizardPage extends AbstractTableWizardPage {

        public DependencyWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            parent.setLayoutData(createGridData());

            Composite composite = createComposite(parent);
            tabFolder = new TabFolder(composite, SWT.NONE);
            tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

            TabItem item1 = new TabItem(tabFolder, SWT.NONE);
            item1.setText(CommonMessages.wizardTabManual_Dependency);
            Composite addManuallyComposite = createComposite(tabFolder);
            createEditFields(addManuallyComposite);
            item1.setControl(addManuallyComposite);

            if (eObject == null) {
                TabItem item2 = new TabItem(tabFolder, SWT.NONE);
                item2.setText(CommonMessages.wizardTabServer_Dependency);
                int columnWidths[] = { 80, 220, 50, 65 };
                dependencyTable = createTable(tabFolder, section.getTableColumnNames(), columnWidths);
                populateTable();
                item2.setControl(dependencyTable);
            }
            tabFolder.pack();
            doCustom(composite);
            setControl(composite);
        }

        public GridData createGridData() {
            GridData data = new GridData();
            data.verticalAlignment = GridData.FILL;
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessVerticalSpace = true;
            data.grabExcessHorizontalSpace = true;
            data.heightHint = 230;
            data.widthHint = 300;
            return data;
        }

        public void populateTable() {
            List<DependencyType> serverList = GeronimoServerInfo.getInstance().getCommonLibs();
            List<DependencyType> alreadyAddedDependencies = (List<DependencyType>) section.getObjectContainer();
            serverList.removeAll(alreadyAddedDependencies);
            for (int i = 0; i < serverList.size(); ++i) {
                TableItem tabItem = new TableItem(dependencyTable, SWT.NONE);
                DependencyType dependency = serverList.get(i);
                tabItem.setData(dependency);
                tabItem.setText(dependencyToStringArray(dependency));
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_Dependency;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_Dependency;
        }
    }

    public boolean performFinish() {
        switch (tabFolder.getSelectionIndex()) {
        case -1:
            // no item selected!
            return false;
        case 0:
            // Adding Manually
            if (eObject == null) {
                eObject = new DependencyType();
                section.getObjectContainer().add(eObject);
            }
            processEAttributes(getPages()[0]);
            break;
        case 1:
            // Adding from Server
            TableItem[] selectedItems = dependencyTable.getSelection();
            for (int i = 0; i < selectedItems.length; i++) {
                TableItem selectedItem = selectedItems[i];
                DependencyType dependency = (DependencyType) selectedItem.getData();
                section.getObjectContainer().add(dependency);
            }
            break;
        }
        return true;
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_Dependency;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_Dependency;
    }
}
