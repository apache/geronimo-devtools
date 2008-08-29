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

import java.util.List;

import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.st.core.jaxb.JAXBUtils;
import org.apache.geronimo.st.ui.sections.AbstractListSection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractTableWizard extends AbstractWizard {

    public AbstractTableWizard(AbstractListSection section) {
        super(section);
    }

    public abstract JAXBObjectFactory getEFactory();

    public abstract String[] getTableColumnEAttributes();

    public class DynamicWizardPage extends AbstractWizardPage {
        protected Text[] textEntries = new Text[getTableColumnEAttributes().length];

        public DynamicWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            createEditFields(composite);
            doCustom(composite);
            setControl(composite);
            textEntries[0].setFocus();
        }

        public void createEditFields(Composite composite) {
            for (int i = 0; i < section.getTableColumnNames().length; i++) {
                createLabel(composite, section.getTableColumnNames()[i]);
                String initialValue = "";
                if (eObject != null) {
                    initialValue = (String) JAXBUtils.getValue(eObject, getTableColumnEAttributes()[i]);
                }
                textEntries[i] = createTextField(composite, initialValue);
            }
        }

        public void doCustom(Composite parent) {
        }

        public Text getTextEntry(int object) {
            return textEntries[object];
        }
    }

    @Override
    public void addPages() {
        addPage(new DynamicWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        if (eObject == null) {
            eObject = getEFactory().create(section.getTableEntryObjectType());
            List objectContainer = section.getObjectContainer();
            objectContainer.add(eObject);
        }
        processEAttributes(getPages()[0]);
        if (section.getViewer().getInput() == null) {
            section.getViewer().setInput(section.getInput());
        }
        return true;
    }

    public void processEAttributes(IWizardPage page) {
        if (page instanceof DynamicWizardPage) {
            for (int i = 0; i < getTableColumnEAttributes().length; i++) {
                String value = ((DynamicWizardPage) page).textEntries[i].getText();
                String attribute = getTableColumnEAttributes()[i];
                JAXBUtils.setValue(eObject, attribute, value);
            }
        }
    }

    @Override
    protected String getWizardPageTitle() {
        return getWizardFirstPageTitle();
    }

    @Override
    protected String getWizardPageDescription() {
        return getWizardFirstPageDescription();
    }

    public abstract String getWizardFirstPageTitle();

    public abstract String getWizardFirstPageDescription();
}
