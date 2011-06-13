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
package org.apache.geronimo.st.v30.ui.wizards;

import org.apache.geronimo.st.v30.ui.sections.ListEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public abstract class ListEditorWizard extends AbstractWizard {

    protected ListEditor listEditor;
    
    protected Text value;
    
    public ListEditorWizard(ListEditor listEditor) {
        this.listEditor = listEditor;
    }

    public void addPages() {
        addPage(new MessageDestWizardPage("Page0"));
    }

    public abstract String getWizardPageTitle();   

    public abstract String getWizardPageDescription();   
    
    public abstract String getWizardPageLabel();
    
    public class MessageDestWizardPage extends AbstractWizardPage {
        public MessageDestWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createLabel (composite, getWizardPageLabel(), SWT.LEFT | SWT.WRAP);
            value = createTextField (composite, "");
            
            if (eObject != null) {
                value.setText(eObject.toString());
            }
            
            setControl(composite);
        }

        public String getWizardPageTitle() {
            return ListEditorWizard.this.getWizardPageTitle();
        }

        public String getWizardPageDescription() {
            return ListEditorWizard.this.getWizardPageDescription();
        }
    }
    
    public boolean performFinish() {
        if (eObject == null) {
            listEditor.addItem(value.getText().trim());
        } else {
            listEditor.setSelectedItem(value.getText().trim());
        }
        return true;
    }
    
}
