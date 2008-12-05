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
package org.apache.geronimo.st.ui.wizards;

import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.SortListener;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractWizard extends Wizard {

    protected Object eObject;

    public AbstractWizard() {
        super();
        setWindowTitle(getAddWizardWindowTitle());
    }

    public void setEObject(Object object) {
        eObject = object;
        setWindowTitle(getEditWizardWindowTitle());
    }

    public abstract class AbstractWizardPage extends WizardPage {
        public AbstractWizardPage(String pageName) {
            super(pageName);
            setTitle(getWizardPageTitle());
            setDescription(getWizardPageDescription());
            setImageDescriptor(getImageDescriptor());
        }

        protected Composite createComposite(Composite parent) {
            Composite composite = new Composite(parent, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            composite.setLayout(layout);
            GridData data = new GridData();
            data.verticalAlignment = GridData.FILL;
            data.horizontalAlignment = GridData.FILL;
            data.heightHint = 230;
            data.widthHint = 300;
            composite.setLayoutData(data);
            return composite;
        }

        public GridData createGridData() {
            return createGridData (400, 300);
        }

        public GridData createGridData(int heightHint, int widthHint) {
            GridData data = new GridData();
            data.verticalAlignment = GridData.FILL;
            data.horizontalAlignment = GridData.FILL;
            data.grabExcessVerticalSpace = true;
            data.grabExcessHorizontalSpace = true;
            data.heightHint = heightHint;
            data.widthHint = widthHint;
            return data;
        }

        protected Label createLabel(Composite composite, String labelString) {
            Label label = new Label(composite, SWT.LEFT);
            if (!labelString.endsWith(":") && labelString.length() > 0) {
                labelString = labelString.concat(":");
            }
            label.setText(labelString);
            GridData data = new GridData();
            data.horizontalAlignment = GridData.FILL;
            label.setLayoutData(data);
            return label;
        }

        protected Text createTextField(Composite composite, String initialValue) {
            Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
            data.grabExcessHorizontalSpace = true;
            data.widthHint = 100;
            text.setLayoutData(data);
            if (initialValue != null) {
                text.setText(initialValue);
            }
            return text;
        }

        protected Text createMultiTextField(Composite composite, String[] initialValue) {
            Text text = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            data.widthHint = 100;
            text.setLayoutData(data);
            if (initialValue != null) {
                String temp = "";
                for (int i = 0; i < initialValue.length; i++) {
                    temp += initialValue[i] + "\n";
                }
                text.setText(temp);
            }
            return text;
        }

        protected Combo createCombo(Composite composite, String[] items, boolean editable) {
            int style = SWT.NONE;
            if (!editable) {
                style = SWT.READ_ONLY;
            }
            Combo combo = new Combo(composite, style);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
            data.grabExcessHorizontalSpace = true;
            data.widthHint = 100;
            combo.setLayoutData(data);
            combo.setItems(items);
            return combo;
        }

        protected Button createButton(Composite composite, String buttonString) {
            Button button = new Button(composite, SWT.RADIO);
            button.setText(buttonString);
            GridData data = new GridData();
            data.horizontalSpan = 2;
            button.setLayoutData(data);
            return button;
        }

        protected Button createPushButton(Composite composite, String buttonString) {
            Button button = new Button(composite, SWT.PUSH);
            button.setText(buttonString);
            GridData data = new GridData();
            data.horizontalSpan = 1;
            button.setLayoutData(data);
            return button;
        }

        protected Table createTable(Composite composite, String[] columnNames, int[] columnWidths) {
            int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

            Table table = new Table(composite, style);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            data.horizontalSpan = 2;
            data.horizontalAlignment = GridData.FILL;
            data.heightHint = 250;
            data.widthHint = 350;
            table.setLayoutData(data);
            table.setLinesVisible(false);
            table.setHeaderVisible(true);

            final TableColumn[] column = new TableColumn[columnNames.length];
            Listener sortListener = new SortListener(table, columnNames);
            for (int i = 0; i < columnNames.length; ++i) {
                column[i] = new TableColumn(table, SWT.LEFT, i);
                column[i].setText(columnNames[i]);
                column[i].setWidth(columnWidths[i]);
                column[i].addListener(SWT.Selection, sortListener);
            }
            return table;
        }

        protected Table createEditableTable(Composite composite, String[] columnNames, int[] columnWidths) {
            int style = SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;

            Table table = new Table(composite, style);
            GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
            data.grabExcessHorizontalSpace = true;
            data.grabExcessVerticalSpace = true;
            data.horizontalSpan = 1;
            data.horizontalAlignment = GridData.FILL;
            data.heightHint = 250;
            data.widthHint = 350;
            table.setLayoutData(data);
            table.setLinesVisible(false);
            table.setHeaderVisible(true);

            final TableColumn[] column = new TableColumn[columnNames.length];
            Listener sortListener = new SortListener(table, columnNames);
            for (int i = 0; i < columnNames.length; ++i) {
                column[i] = new TableColumn(table, SWT.LEFT, i);
                column[i].setText(columnNames[i]);
                column[i].setWidth(columnWidths[i]);
                column[i].addListener(SWT.Selection, sortListener);
            }
            return table;
        }
        
        protected abstract String getWizardPageTitle();

        protected abstract String getWizardPageDescription();
    }

    protected abstract String getAddWizardWindowTitle();

    protected abstract String getEditWizardWindowTitle();
    
    protected ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.apache.geronimo.ui", "icons/bigG.gif");
    }

    protected boolean isEmpty(String text) {
        return text == null || text.trim().length() <= 0;
    }
}
