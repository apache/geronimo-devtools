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
package org.apache.geronimo.st.v30.ui.sections;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.v30.core.descriptor.AbstractDeploymentDescriptor;
import org.apache.geronimo.st.v30.ui.SortListener;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractTableSection extends AbstractListSection {

    protected Table table;

    protected String[] COLUMN_NAMES = new String[] {};

    public Listener sortListener = null;
    
    public AbstractTableSection(Section section) {
        super(section);
    }

    /**
     * 
     * Subclasses should call createClient() in constructor
     */
    public AbstractTableSection(JAXBElement plan, Composite parent,
            FormToolkit toolkit, int style) {
        super(parent, toolkit, style, plan);
    }

    public AbstractTableSection(JAXBElement plan, AbstractDeploymentDescriptor descriptor,
            Composite parent, FormToolkit toolkit, int style) {
        super(parent, toolkit, style, plan, descriptor);
    }

    public void createViewer(Composite composite) {
        table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.MULTI);
        if (isHeaderVisible()) {
            table.setHeaderVisible(true);
        }

        GridData data = new GridData(SWT.FILL, SWT.FILL, false, true);
        data.heightHint = 60;
        data.widthHint = 660;
        //data.grabExcessVerticalSpace = true;
        table.setLayoutData(data);

        TableLayout tableLayout = new TableLayout();
        table.setLayout(tableLayout);

        sortListener = new SortListener(table, COLUMN_NAMES);
        for (int i = 0; i < getTableColumnNames().length; i++) {
            tableLayout.addColumnData(new ColumnWeightData(35));
            TableColumn tableColumn = new TableColumn(table, SWT.NONE);
            tableColumn.setText(getTableColumnNames()[i]);
            tableColumn.addListener(SWT.Selection, sortListener);
        }

        table.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                activateButtons();
            }
        });

        viewer = new TableViewer(table);
        viewer.addFilter(new ViewerFilter() {
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                return AbstractTableSection.this.filter(viewer, parentElement, element);
            }
        });
        if (getTableColumnNames().length > 0) {
            viewer.setColumnProperties(getTableColumnNames());
        }
    }

    protected boolean isHeaderVisible() {
        return true;
    }

    protected boolean filter(Viewer viewer, Object parentElement, Object element) {
        return getTableEntryObjectType().isInstance(element);
    }

    public void handleDelete() {
        TableItem[] selectedItems = table.getSelection();
        for (int i = 0; i < selectedItems.length; i++) {
            TableItem tableItem = selectedItems[i];
            removeItem(tableItem.getData());
        }
    }

    protected void removeItem (Object selectedItem) {
        getObjectContainer().remove(selectedItem);
    }

    protected void activateButton(Button button) {
        boolean selected = table.getSelectionCount() > 0;
        button.setEnabled(selected);
    }

    public String[] getTableColumnNames() {
        return COLUMN_NAMES;
    }
}
