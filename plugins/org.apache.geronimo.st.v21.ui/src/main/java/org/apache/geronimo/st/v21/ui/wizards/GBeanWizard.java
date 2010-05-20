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

import org.apache.geronimo.jee.deployment.Attribute;
import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.deployment.ObjectFactory;
import org.apache.geronimo.jee.deployment.Pattern;
import org.apache.geronimo.jee.deployment.Reference;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;

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
                if (Gbean.class.isInstance(eObject)) {
                    textList.get(0).setText(((Gbean)eObject).getName());
                    textList.get(1).setText(((Gbean)eObject).getClazz());
                    element.select(GBEAN);
                }
                else if (Attribute.class.isInstance(eObject)) {
                    textList.get(0).setText(((Attribute)eObject).getName());
                    textList.get(1).setText(((Attribute)eObject).getType());
                    textList.get(2).setText(((Attribute)eObject).getValue());
                    element.select(ATTRIBUTE);
                }
                else if (Pattern.class.isInstance(eObject)) {
                    textList.get(0).setText(((Pattern)eObject).getGroupId());
                    textList.get(1).setText(((Pattern)eObject).getArtifactId());
                    textList.get(2).setText(((Pattern)eObject).getVersion());
                    textList.get(3).setText(((Pattern)eObject).getModule());
                    textList.get(4).setText(((Pattern)eObject).getType());
                    textList.get(5).setText(((Pattern)eObject).getCustomFoo());
                    element.select(DEPENDENCY);
                }
                else if (Reference.class.isInstance(eObject)) {
                    textList.get(0).setText(((Reference)eObject).getName());
                    textList.get(1).setText(((Reference)eObject).getGroupId());
                    textList.get(2).setText(((Reference)eObject).getArtifactId());
                    textList.get(3).setText(((Reference)eObject).getVersion());
                    textList.get(4).setText(((Reference)eObject).getModule());
                    textList.get(5).setText(((Reference)eObject).getType());
                    textList.get(6).setText(((Reference)eObject).getCustomFoo());
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
        Gbean gbean;
        switch (element.getSelectionIndex()) {
        case GBEAN:
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            gbean = (Gbean)eObject;
            if (gbean == null) {
                gbean = (Gbean)getEFactory().create(Gbean.class);
                JAXBElement plan = section.getPlan();
                
                // add the JAXBElement of a GBean, not the GBean
                ObjectFactory objectFactory = new ObjectFactory();
                JAXBModelUtils.getGbeans(plan).add(objectFactory.createGbean((Gbean)gbean));
            }
            gbean.setName(textList.get(0).getText());
            gbean.setClazz(textList.get(1).getText());
            break;

        case ATTRIBUTE:
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            Attribute attribute = (Attribute)eObject;
            if (attribute == null) {
                attribute = (Attribute)getEFactory().create(Attribute.class);
                gbean = (Gbean)section.getSelectedObject();
                
                // add the JAXBElement of an Attribute, not the Attribute
                ObjectFactory objectFactory = new ObjectFactory();
                gbean.getAttributeOrXmlAttributeOrReference().add(objectFactory.createGbeanAttribute(attribute));
            }
            attribute.setName(textList.get(0).getText());
            attribute.setType(textList.get(1).getText());
            attribute.setValue(textList.get(2).getText());
            break;

        case DEPENDENCY:
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            Pattern dependency = (Pattern)eObject;
            if (dependency == null) {
                dependency = (Pattern)getEFactory().create(Pattern.class);
                gbean = (Gbean)section.getSelectedObject();

                // add the JAXBElement of a Dependency, not the Dependency
                ObjectFactory objectFactory = new ObjectFactory();
                gbean.getAttributeOrXmlAttributeOrReference().add(objectFactory.createGbeanDependency(dependency));
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
            Reference reference = (Reference)eObject;
            if (reference == null) {
                reference = (Reference)getEFactory().create(Reference.class);
                gbean = (Gbean)section.getSelectedObject();

                // add the JAXBElement of a Dependency, not the Dependency
                ObjectFactory objectFactory = new ObjectFactory();
                gbean.getAttributeOrXmlAttributeOrReference().add(objectFactory.createGbeanReference(reference));
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
