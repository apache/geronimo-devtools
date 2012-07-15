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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.deployment.AttributeType;
import org.apache.geronimo.j2ee.deployment.GbeanType;
import org.apache.geronimo.j2ee.deployment.ObjectFactory;
import org.apache.geronimo.j2ee.deployment.PatternType;
import org.apache.geronimo.j2ee.deployment.ReferenceType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBObjectFactoryImpl;

/**
 * @version $Rev$ $Date$
 */
public class GBeanWizard extends AbstractTreeWizard {

    private final int GBEAN = 0;
    private final int ATTRIBUTE = 1;
    private final int DEPENDENCY = 2;
    private final int REFERENCE = 3;

    public GBeanWizard(AbstractTreeSection section) {
        super(section, 4, 7);
        elementTypes[GBEAN] = "GBean";
        elementTypes[ATTRIBUTE] = "Attribute";
        elementTypes[DEPENDENCY] = "Dependency";
        elementTypes[REFERENCE] = "Reference";
    }

    public class GBeanWizardPage extends AbstractTreeWizardPage {

        public GBeanWizardPage(String pageName) {
            super(pageName);
        }

        protected void initControl() {
            if (eObject == null) {
                element.select(GBEAN);
                if (section.getSelectedObject() == null) {
                    element.setEnabled(false);
                }
            }
            else {
                // change eObject to be the value of the JAXBElement
                eObject = ((JAXBElement)eObject).getValue();
                if (GbeanType.class.isInstance(eObject)) {
                    textList.get(0).setText(((GbeanType)eObject).getName());
                    textList.get(1).setText(((GbeanType)eObject).getClazz());
                    element.select(GBEAN);
                }
                else if (AttributeType.class.isInstance(eObject)) {
                    textList.get(0).setText(((AttributeType)eObject).getName());
                    textList.get(1).setText(((AttributeType)eObject).getType());
                    textList.get(2).setText(((AttributeType)eObject).getValue());
                    element.select(ATTRIBUTE);
                }
                else if (PatternType.class.isInstance(eObject)) {
                    textList.get(0).setText(((PatternType)eObject).getGroupId());
                    textList.get(1).setText(((PatternType)eObject).getArtifactId());
                    textList.get(2).setText(((PatternType)eObject).getVersion());
                    textList.get(3).setText(((PatternType)eObject).getModule());
                    textList.get(4).setText(((PatternType)eObject).getType());
                    textList.get(5).setText(((PatternType)eObject).getCustomFoo());
                    element.select(DEPENDENCY);
                }
                else if (ReferenceType.class.isInstance(eObject)) {
                    textList.get(0).setText(((ReferenceType)eObject).getName());
                    textList.get(1).setText(((ReferenceType)eObject).getGroupId());
                    textList.get(2).setText(((ReferenceType)eObject).getArtifactId());
                    textList.get(3).setText(((ReferenceType)eObject).getVersion());
                    textList.get(4).setText(((ReferenceType)eObject).getModule());
                    textList.get(5).setText(((ReferenceType)eObject).getType());
                    textList.get(6).setText(((ReferenceType)eObject).getCustomFoo());
                    element.select(REFERENCE);
                }
                element.setEnabled(false);
            }
        }
        
        protected void toggleFields (boolean clearFields) {
            int selection = element.getSelectionIndex();
            switch (selection) {
            case GBEAN:
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 2 ? true : false);
                    textList.get(i).setVisible(i < 2 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.className);
                // if we are doing an add, then we need to make sure that the longest
                // text can be handled
                labelList.get(2).setText(CommonMessages.groupId);
                labelList.get(3).setText(CommonMessages.artifactId);
                labelList.get(4).setText(CommonMessages.moduleId);
                labelList.get(5).setText(CommonMessages.artifactType);
                labelList.get(6).setText(CommonMessages.customName);
                break;
            case ATTRIBUTE:
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 3 ? true : false);
                    textList.get(i).setVisible(i < 3 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.type);
                labelList.get(2).setText(CommonMessages.value);
                break;
            case DEPENDENCY:
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 6 ? true : false);
                    textList.get(i).setVisible(i < 6 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.groupId);
                labelList.get(1).setText(CommonMessages.artifactId);
                labelList.get(2).setText(CommonMessages.version);
                labelList.get(3).setText(CommonMessages.moduleId);
                labelList.get(4).setText(CommonMessages.artifactType);
                labelList.get(5).setText(CommonMessages.customName);
                break;
            case REFERENCE:
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(true);
                    textList.get(i).setVisible(true);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.groupId);
                labelList.get(2).setText(CommonMessages.artifactId);
                labelList.get(3).setText(CommonMessages.version);
                labelList.get(4).setText(CommonMessages.moduleId);
                labelList.get(5).setText(CommonMessages.artifactType);
                labelList.get(6).setText(CommonMessages.customName);
                break;
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardEditTitle_GBean;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageTitle_GBean;
        }
    }

    @Override
    public void addPages() {
        addPage(new GBeanWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        GbeanType gbean;
        switch (element.getSelectionIndex()) {
        case GBEAN:
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            gbean = (GbeanType)eObject;
            if (gbean == null) {
                gbean = (GbeanType)getEFactory().create(GbeanType.class);
                JAXBElement plan = section.getPlan();
                
                // add the JAXBElement of a GBean, not the GBean
                ObjectFactory objectFactory = new ObjectFactory();
                JAXBModelUtils.getGbeans(plan).add(objectFactory.createGbean((GbeanType)gbean));
            }
            gbean.setName(textList.get(0).getText());
            gbean.setClazz(textList.get(1).getText());
            break;

        case ATTRIBUTE:
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            AttributeType attribute = (AttributeType)eObject;
            if (attribute == null) {
                attribute = (AttributeType)getEFactory().create(AttributeType.class);
                gbean = (GbeanType)section.getSelectedObject();
                
                // add the JAXBElement of an Attribute, not the Attribute
                ObjectFactory objectFactory = new ObjectFactory();
                gbean.getAttributeOrXmlAttributeOrReference().add(objectFactory.createGbeanTypeAttribute(attribute));
            }
            attribute.setName(textList.get(0).getText());
            attribute.setType(textList.get(1).getText());
            attribute.setValue(textList.get(2).getText());
            break;

        case DEPENDENCY:
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            PatternType dependency = (PatternType)eObject;
            if (dependency == null) {
                dependency = (PatternType)getEFactory().create(PatternType.class);
                gbean = (GbeanType)section.getSelectedObject();

                // add the JAXBElement of a Dependency, not the Dependency
                ObjectFactory objectFactory = new ObjectFactory();
                gbean.getAttributeOrXmlAttributeOrReference().add(objectFactory.createGbeanTypeDependency(dependency));
            }
            dependency.setGroupId(textList.get(0).getText());
            dependency.setArtifactId(textList.get(1).getText());
            dependency.setVersion(textList.get(2).getText());
            dependency.setModule(textList.get(3).getText());
            dependency.setType(textList.get(4).getText());
            dependency.setCustomFoo(textList.get(5).getText());
            break;

        case REFERENCE:
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText()) ||
                    isEmpty(textList.get(2).getText())) {
                return false;
            }
            ReferenceType reference = (ReferenceType)eObject;
            if (reference == null) {
                reference = (ReferenceType)getEFactory().create(ReferenceType.class);
                gbean = (GbeanType)section.getSelectedObject();

                // add the JAXBElement of a Dependency, not the Dependency
                ObjectFactory objectFactory = new ObjectFactory();
                gbean.getAttributeOrXmlAttributeOrReference().add(objectFactory.createGbeanTypeReference(reference));
            }
            reference.setName(textList.get(0).getText());
            reference.setGroupId(textList.get(1).getText());
            reference.setArtifactId(textList.get(2).getText());
            reference.setVersion(textList.get(3).getText());
            reference.setModule(textList.get(4).getText());
            reference.setType(textList.get(5).getText());
            reference.setCustomFoo(textList.get(6).getText());
            break;
        }
        return true;
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_GBean;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_GBean;
    }
}
