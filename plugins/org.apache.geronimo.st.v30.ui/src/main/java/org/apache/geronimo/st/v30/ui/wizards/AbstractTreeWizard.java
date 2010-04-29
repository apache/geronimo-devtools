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

import java.util.ArrayList;

import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.apache.geronimo.st.v30.ui.sections.AbstractTreeSection;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractTreeWizard extends AbstractWizard {

    protected AbstractTreeSection section;
    
    protected String[] elementTypes = null;
    protected int maxTextFields;

    protected Combo element;
    protected ArrayList<Text> textList;
    
    /*
     * AbstractTreeWizard takes 3 parameters
     * The Tree Section that is being added/edited
     * elementTypeCount for how many different elements could be handled
     * maxTextFields for the maximum number of fields an element may have.
     * 
     * For the most part, the user will be given an opportunity to add any kind of\
     * element by using a combo.  When the combo changes, the fields need to change
     * to allow the appropriate fields to be used.
     */
    public AbstractTreeWizard(AbstractTreeSection section, int elementTypeCount, int maxTextFields) {
        super();
        this.section = section;
        elementTypes = new String[elementTypeCount];
        this.maxTextFields = maxTextFields;
        textList = new ArrayList<Text>(maxTextFields);
    }

    public abstract class AbstractTreeWizardPage extends AbstractWizardPage {
        protected ArrayList<Label> labelList;

        public AbstractTreeWizardPage(String pageName) {
            super(pageName);
            labelList = new ArrayList<Label>(maxTextFields);
        }

        public void createControl(Composite parent) {
            Label label;
            Text text;
            Composite composite = createComposite(parent);
            createLabel(composite, CommonMessages.element);
            element = createCombo(composite, elementTypes, false);
            for (int i = 0; i < maxTextFields; i++) {
                label = createLabel(composite, "");
                labelList.add(label);
                text = createTextField(composite, "");
                textList.add(text);
            }
            element.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    toggleFields(true);
                }
            });
            
            initControl();
            toggleFields(false);
            setControl(composite);
        }

        protected abstract void initControl();

        protected abstract void toggleFields(boolean clearFields);
    }

    protected abstract String getAddWizardWindowTitle();

    protected abstract String getEditWizardWindowTitle();
}
