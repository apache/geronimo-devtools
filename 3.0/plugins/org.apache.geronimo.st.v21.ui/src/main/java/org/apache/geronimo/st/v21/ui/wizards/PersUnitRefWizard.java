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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.naming.PersistenceUnitRef;
import org.apache.geronimo.jee.naming.ObjectFactory;
import org.apache.geronimo.jee.naming.Pattern;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class PersUnitRefWizard extends AbstractTreeWizard {

    protected Button specifyUnit;
    protected Button specifyPattern;
    
    public PersUnitRefWizard(AbstractTreeSection section) {
        super(section, 1, 7);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_PersUnitRef;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_PersUnitRef;
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(new PersUnitRefWizardPage("Page0"));
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class PersUnitRefWizardPage extends AbstractTreeWizardPage {
        public PersUnitRefWizardPage(String pageName) {
            super(pageName);
        }

        @Override
        public void createControl(Composite parent) {
            Label label;
            Text text;
            Composite composite = createComposite(parent);
            for (int i = 0; i < maxTextFields; i++) {
                label = createLabel(composite, "");
                labelList.add(label);
                text = createTextField(composite, "");
                textList.add(text);
                if (i == 0) {
                    specifyUnit = createButton(composite, CommonMessages.useUnitName);
                }
                if (i == 1) {
                    specifyPattern = createButton(composite, CommonMessages.usePattern);
                }
            }
            specifyUnit.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    toggleFields(false);
                }
            });
            specifyPattern.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    toggleFields(false);
                }
            });
            
            initControl();
            toggleFields(false);
            setControl(composite);
        }

        protected void initControl () {
            if (eObject != null) {
                if (JAXBElement.class.isInstance(eObject)) {
                    eObject = ((JAXBElement)eObject).getValue();
                    PersistenceUnitRef unitRef = (PersistenceUnitRef)eObject;
                    textList.get(0).setText(unitRef.getPersistenceUnitRefName());
                    if (unitRef.getPersistenceUnitName() != null)
                        textList.get(1).setText(unitRef.getPersistenceUnitName());
                    if (unitRef.getPattern() != null) {
                        specifyPattern.setSelection(true);
                        textList.get(2).setText(unitRef.getPattern().getName());
                        if (unitRef.getPattern().getGroupId() != null)
                            textList.get(3).setText(unitRef.getPattern().getGroupId());
                        if (unitRef.getPattern().getArtifactId() != null)
                            textList.get(4).setText(unitRef.getPattern().getArtifactId());
                        if (unitRef.getPattern().getVersion() != null)
                            textList.get(5).setText(unitRef.getPattern().getVersion());
                        if (unitRef.getPattern().getModule() != null)
                            textList.get(6).setText(unitRef.getPattern().getModule());
                    }
                }
            }
        }
        
        protected void toggleFields (boolean clearFields) {
            labelList.get(0).setText(CommonMessages.contextName);
            labelList.get(1).setText(CommonMessages.unitName);
            textList.get(1).setEnabled(specifyUnit.getSelection());

            labelList.get(2).setText(CommonMessages.name);
            labelList.get(3).setText(CommonMessages.groupId);
            labelList.get(4).setText(CommonMessages.artifactId);
            labelList.get(5).setText(CommonMessages.version);
            labelList.get(6).setText(CommonMessages.moduleId);
            for (int i = 2; i < maxTextFields; i++) {
                textList.get(i).setEnabled(specifyPattern.getSelection());
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_PersUnitRef;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_PersUnitRef;
        }
    }

    @Override
    public boolean performFinish() {
        PersistenceUnitRef unitRef;
        if (isEmpty(textList.get(0).getText()) ||
            isEmpty(textList.get(1).getText()) && isEmpty(textList.get(2).getText())) {
            return false;
        }
        unitRef = (PersistenceUnitRef)eObject;
        ObjectFactory objectFactory = new ObjectFactory();
        if (unitRef == null) {
            unitRef = (PersistenceUnitRef)getEFactory().create(PersistenceUnitRef.class);
            JAXBElement plan = section.getPlan();

            // add the JAXBElement of the PersistenceContextRef
            JAXBModelUtils.getGbeanRefs(plan).add(objectFactory.createPersistenceUnitRef(unitRef));
            section.getObjectContainer().add(objectFactory.createPersistenceUnitRef(unitRef));
        }
        unitRef.setPersistenceUnitRefName(textList.get(0).getText());
        if (specifyUnit.getSelection() == true) {
            unitRef.setPersistenceUnitName(textList.get(1).getText());
            unitRef.setPattern(null);
        }
        else {
            unitRef.setPersistenceUnitName(null);
            Pattern pattern = unitRef.getPattern();
            if (pattern == null) {
                pattern = objectFactory.createPattern();
            }
            pattern.setName(textList.get(2).getText());
            pattern.setGroupId(textList.get(3).getText());
            pattern.setArtifactId(textList.get(4).getText());
            pattern.setVersion(textList.get(5).getText());
            pattern.setModule(textList.get(6).getText());
            unitRef.setPattern(pattern);
        }

        return true;
    }
}
