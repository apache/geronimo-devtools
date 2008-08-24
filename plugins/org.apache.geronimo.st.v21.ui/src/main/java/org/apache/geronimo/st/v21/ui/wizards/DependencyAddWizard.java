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

import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.SortListener;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.core.GeronimoServerInfo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @version $Rev$ $Date$
 */
public class DependencyAddWizard extends DependencyWizard {
    public Table dependencyTable;
    private TabFolder tabFolder;

    public DependencyAddWizard(AbstractTableSection section) {
        super(section, true);
    }

    public void addPages() {
        addPage(new DependencyAddWizardPage("AddDependencyPage"));
    }

    public class DependencyAddWizardPage extends DynamicWizardPage {

        public DependencyAddWizardPage(String pageName) {
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

            TabItem item2 = new TabItem(tabFolder, SWT.NONE);
            item2.setText(CommonMessages.wizardTabServer_Dependency);
            createTable(tabFolder);
            populateTable();
            item2.setControl(dependencyTable);

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

        private void createTable(Composite composite) {
            int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

            dependencyTable = new Table(composite, style);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            data.horizontalSpan = 2;
            data.horizontalAlignment = GridData.FILL;
            data.heightHint = 60;
            data.widthHint = 400;
            dependencyTable.setLayoutData(data);
            dependencyTable.setLinesVisible(false);
            dependencyTable.setHeaderVisible(true);

            String[] columnNames = section.getTableColumnNames();
            final TableColumn[] column = new TableColumn[columnNames.length];
            int columnWidth[] = { 80, 220, 50, 65 };
            Listener sortListener = new SortListener(dependencyTable, columnNames);
            for (int i = 0; i < columnNames.length; ++i) {
                column[i] = new TableColumn(dependencyTable, SWT.LEFT, i);
                column[i].setText(columnNames[i]);
                column[i].setWidth(columnWidth[i]);
                column[i].addListener(SWT.Selection, sortListener);
            }
        }

        public void populateTable() {
            List<Dependency> serverList = GeronimoServerInfo.getInstance().getCommonLibs();
            List<Dependency> alreadyAddedDependencies = (List<Dependency>) section.getObjectContainer();
            serverList.removeAll(alreadyAddedDependencies);
            for (int i = 0; i < serverList.size(); ++i) {
                TableItem tabItem = new TableItem(dependencyTable, SWT.NONE);
                Dependency dependency = serverList.get(i);
                tabItem.setData(dependency);
                tabItem.setText(dependencyToStringArray(dependency));
            }
        }
    }

    public boolean performFinish() {
        switch (tabFolder.getSelectionIndex()) {
        case -1:
            // no item selected!
            return false;
        case 0:
            // Adding Manually
            eObject = new Dependency();
            section.getObjectContainer().add(eObject);
            processEAttributes(getPages()[0]);
            break;
        case 1:
            // Adding from Server
            TableItem[] selectedItems = dependencyTable.getSelection();
            for (int i = 0; i < selectedItems.length; i++) {
                TableItem selectedItem = selectedItems[i];
                Dependency dependency = (Dependency) selectedItem.getData();
                section.getObjectContainer().add(dependency);
            }
            break;
        }
        return true;
    }
}
