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

import org.apache.geronimo.jee.naming.PersistenceContextRef;
import org.apache.geronimo.jee.naming.PersistenceContextType;
import org.apache.geronimo.jee.naming.ObjectFactory;
import org.apache.geronimo.jee.naming.Pattern;
import org.apache.geronimo.jee.naming.Property;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class PersContextRefWizard extends AbstractTreeWizard {

    private final int CONTEXT = 0;
    private final int PROPERTY = 1;
    
    private final String[] CONTEXT_TYPES = {
            "Extended", "Transactional" };
    protected Combo contextType;
    protected Button specifyUnit;
    protected Button specifyPattern;
    
    public PersContextRefWizard(AbstractTreeSection section) {
        super(section, 2, 8);
        elementTypes[CONTEXT] = "Persistence Context";
        elementTypes[PROPERTY] = "Property";
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_PersContextRef;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_PersContextRef;
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(new PersContextRefWizardPage("Page0"));
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class PersContextRefWizardPage extends AbstractTreeWizardPage {
        public PersContextRefWizardPage(String pageName) {
            super(pageName);
        }

        @Override
        public void createControl(Composite parent) {
            Label label;
            Text text;
            Composite composite = createComposite(parent);
            createLabel(composite, CommonMessages.element);
            element = createCombo(composite, elementTypes, false);
            for (int i = 0; i < maxTextFields; i++) {
                label = createLabel(composite, "");
                labelList.add(label);
                if (i == 1) {
                    contextType = createCombo(composite, CONTEXT_TYPES, false);
                    textList.add (null);
                    specifyUnit = createButton(composite, CommonMessages.useUnitName);
                }
                else {
                    text = createTextField(composite, "");
                    textList.add(text);
                    if (i == 2) {
                        specifyPattern = createButton(composite, CommonMessages.usePattern);
                    }
                }
            }
            element.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent arg0) {
                    toggleFields(true);
                }
            });
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
            if (eObject == null) {
                element.select(CONTEXT);
                PersistenceContextRef contextRef = (PersistenceContextRef)section.getSelectedObject();
                if (contextRef == null) {
                    element.setEnabled(false);
                }
            }
            else {
                if (JAXBElement.class.isInstance(eObject)) {
                    eObject = ((JAXBElement)eObject).getValue();
                    PersistenceContextRef contextRef = (PersistenceContextRef)eObject;
                    textList.get(0).setText(contextRef .getPersistenceContextRefName());
                    contextType.setText(contextRef .getPersistenceContextType().value());
                    if (contextRef .getPersistenceUnitName() != null)
                        textList.get(2).setText(contextRef .getPersistenceUnitName());
                    if (contextRef .getPattern() != null) {
                        specifyPattern.setSelection(true);
                        textList.get(3).setText(contextRef.getPattern().getName());
                        if (contextRef .getPattern().getGroupId() != null)
                            textList.get(4).setText(contextRef .getPattern().getGroupId());
                        if (contextRef .getPattern().getArtifactId() != null)
                            textList.get(5).setText(contextRef .getPattern().getArtifactId());
                        if (contextRef .getPattern().getVersion() != null)
                            textList.get(6).setText(contextRef .getPattern().getVersion());
                        if (contextRef .getPattern().getModule() != null)
                            textList.get(7).setText(contextRef .getPattern().getModule());
                    }
                    element.select(CONTEXT);
                }
                else if (Property.class.isInstance(eObject)) {
                    textList.get(0).setText(((Property)eObject).getKey());
                    textList.get(2).setText(((Property)eObject).getValue());
                    element.select(PROPERTY);
                }
                element.setEnabled(false);
            }
        }
        
        protected void toggleFields (boolean clearFields) {
            if (element.getText().equals(elementTypes[CONTEXT])) {
                labelList.get(0).setText(CommonMessages.contextName);
                labelList.get(2).setText(CommonMessages.unitName);
                textList.get(2).setEnabled(specifyUnit.getSelection());
                labelList.get(1).setVisible(true);
                contextType.setVisible(true);    
                specifyUnit.setVisible(true);
                specifyPattern.setVisible(true);
            }
            else {
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(2).setText(CommonMessages.value);
                textList.get(2).setEnabled(true);
                labelList.get(1).setVisible(false);
                contextType.setVisible(false);
                specifyUnit.setVisible(false);
                specifyPattern.setVisible(false);
            }
            labelList.get(1).setText(CommonMessages.type);
            labelList.get(3).setText(CommonMessages.name);
            labelList.get(4).setText(CommonMessages.groupId);
            labelList.get(5).setText(CommonMessages.artifactId);
            labelList.get(6).setText(CommonMessages.version);
            labelList.get(7).setText(CommonMessages.moduleId);
            for (int i = 3; i < maxTextFields; i++) {
                labelList.get(i).setVisible(element.getText().equals(elementTypes[CONTEXT]));
                textList.get(i).setVisible(element.getText().equals(elementTypes[CONTEXT]));
                textList.get(i).setEnabled(specifyPattern.getSelection());
                if (clearFields == true) {
                    textList.get(i).setText("");
                }
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_PersContextRef;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_PersContextRef;
        }
    }

    @Override
    public boolean performFinish() {
        PersistenceContextRef contextRef;
        if (element.getText().equals(elementTypes[CONTEXT])) {
            if (isEmpty(textList.get(0).getText()) ||
                isEmpty(textList.get(2).getText()) && isEmpty(textList.get(3).getText())) {
                return false;
            }
            contextRef = (PersistenceContextRef)eObject;
            ObjectFactory objectFactory = new ObjectFactory();
            if (contextRef == null) {
                contextRef = (PersistenceContextRef)getEFactory().create(PersistenceContextRef.class);
                JAXBElement plan = section.getPlan();

                // add the JAXBElement of the PersistenceContextRef
                JAXBModelUtils.getGbeanRefs(plan).add(objectFactory.createPersistenceContextRef(contextRef));
                section.getObjectContainer().add(objectFactory.createPersistenceContextRef(contextRef));
            }
            contextRef.setPersistenceContextRefName(textList.get(0).getText());
            contextRef.setPersistenceContextType(PersistenceContextType.fromValue(contextType.getText()));
            if (specifyUnit.getSelection() == true) {
                contextRef.setPersistenceUnitName(textList.get(2).getText());
                contextRef.setPattern(null);
            }
            else {
                contextRef.setPersistenceUnitName(null);
                Pattern pattern = contextRef.getPattern();
                if (pattern == null) {
                    pattern = objectFactory.createPattern();
                }
                pattern.setName(textList.get(3).getText());
                pattern.setGroupId(textList.get(4).getText());
                pattern.setArtifactId(textList.get(5).getText());
                pattern.setVersion(textList.get(6).getText());
                pattern.setModule(textList.get(7).getText());
                contextRef.setPattern(pattern);
            }
        }
        else if (element.getText().equals(elementTypes[PROPERTY])) { 
            if (isEmpty(textList.get(0).getText())) {
                return false;
            }
            Property property = (Property)eObject;
            if (property == null) {
                property = (Property)getEFactory().create(Property.class);
                contextRef = (PersistenceContextRef)section.getSelectedObject();
                contextRef.getProperty().add(property);
            }
            property.setKey(textList.get(0).getText());
            property.setValue(textList.get(2).getText());
        }
        return true;
    }
}
