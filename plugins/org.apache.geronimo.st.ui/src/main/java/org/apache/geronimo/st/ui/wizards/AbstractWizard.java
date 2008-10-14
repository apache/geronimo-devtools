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
import org.apache.geronimo.st.ui.sections.AbstractListSection;
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
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractWizard extends Wizard {

    protected AbstractListSection section;

    protected Object eObject;

    public AbstractWizard(AbstractListSection section) {
        super();
        this.section = section;
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

        protected Label createLabel(Composite composite, String labelString) {
            Label label = new Label(composite, SWT.LEFT);
            if (!labelString.endsWith(":")) {
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
    }

    protected abstract String getWizardPageTitle();

    protected abstract String getWizardPageDescription();

    protected abstract String getAddWizardWindowTitle();

    protected abstract String getEditWizardWindowTitle();
    
    protected ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.apache.geronimo.ui", "icons/bigG.gif");
    }

    protected boolean isEmpty(String text) {
        return text == null || text.trim().length() <= 0;
    }
}
