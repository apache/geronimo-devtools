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

import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.naming.ObjectFactory;
import org.apache.geronimo.jee.naming.Pattern;
import org.apache.geronimo.jee.web.WebApp;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;

/**
 * @version $Rev$ $Date$
 */
public class GBeanRefWizard extends AbstractTreeWizard {

    private final int GBEAN_REF = 0;
    private final int GBEAN_TYPE = 1;
    private final int PATTERN = 2;
    
    public GBeanRefWizard(AbstractTreeSection section) {
        super(section, 3, 5);
        elementTypes[GBEAN_REF] = "GBean Reference";
        elementTypes[GBEAN_TYPE] = "Gbean type";
        elementTypes[PATTERN] = "Pattern";
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_GBeanRef;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_GBeanRef;
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(new GbeanRefWizardPage("Page0"));
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class GbeanRefWizardPage extends AbstractTreeWizardPage {
        public GbeanRefWizardPage(String pageName) {
            super(pageName);
        }

        protected void initControl () {
            if (eObject == null) {
                element.select(GBEAN_REF);
                GbeanRef gbeanRef = (GbeanRef)section.getSelectedObject();
                if (gbeanRef == null) {
                    element.setEnabled(false);
                }
            }
            else {
                if (JAXBElement.class.isInstance(eObject)) {
                    eObject = ((JAXBElement)eObject).getValue();
                }
                if (GbeanRef.class.isInstance(eObject)) {
                    textList.get(0).setText(((GbeanRef)eObject).getRefName());
                    element.select(GBEAN_REF);
                }
                else if (String.class.isInstance(eObject)) {
                    textList.get(0).setText(((String)eObject));
                    element.select(GBEAN_TYPE);
                }
                else if (Pattern.class.isInstance(eObject)) {
                    textList.get(0).setText(((Pattern)eObject).getName());
                    textList.get(1).setText(((Pattern)eObject).getGroupId());
                    textList.get(2).setText(((Pattern)eObject).getArtifactId());
                    textList.get(3).setText(((Pattern)eObject).getVersion());
                    textList.get(4).setText(((Pattern)eObject).getModule());
                    element.select(PATTERN);
                }
                element.setEnabled(false);
            }
        }
        
        protected void toggleFields (boolean clearFields) {
            for (int i = 0; i < maxTextFields; i++) {
                labelList.get(i).setVisible(i < 1 ? true : false);
                textList.get(i).setVisible(i < 1 ? true : false);
                if (clearFields == true) {
                    textList.get(i).setText("");
                }
            }
            if (element.getText().equals(elementTypes[GBEAN_TYPE])) {
                labelList.get(0).setText(CommonMessages.type);
            }
            else {
                labelList.get(0).setText(CommonMessages.name);
            }
            labelList.get(1).setText(CommonMessages.groupId);
            labelList.get(2).setText(CommonMessages.artifactId);
            labelList.get(3).setText(CommonMessages.version);
            labelList.get(4).setText(CommonMessages.moduleId);
            for (int i = 1; i < maxTextFields; i++) {
                labelList.get(i).setVisible(element.getText().equals(elementTypes[PATTERN]));
                textList.get(i).setVisible(element.getText().equals(elementTypes[PATTERN]));
                if (clearFields == true) {
                    textList.get(i).setText("");
                }
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_GBeanRef;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_GBeanRef;
        }
    }

    @Override
    public boolean performFinish() {
        GbeanRef gbeanRef;
        if (element.getText().equals(elementTypes[GBEAN_REF])) {
            if (isEmpty(textList.get(0).getText())) {
                return false;
            }
            gbeanRef = (GbeanRef)eObject;
            if (gbeanRef == null) {
                gbeanRef = (GbeanRef)getEFactory().create(GbeanRef.class);
                JAXBElement plan = section.getPlan();

                // if we have a WebApp, add the JAXBElement of the GBeanRef, not the GBeanRef
                if (WebApp.class.isInstance(plan.getValue())) {
                    ObjectFactory objectFactory = new ObjectFactory();
                    JAXBModelUtils.getGbeanRefs(plan).add(objectFactory.createGbeanRef(gbeanRef));
                    section.getObjectContainer().add(objectFactory.createGbeanRef(gbeanRef));
                }
                else {
                    JAXBModelUtils.getGbeanRefs(plan).add(gbeanRef);
                    section.getObjectContainer().add(gbeanRef);
                }
            }
            gbeanRef.setRefName(textList.get(0).getText());
        }
        else if (element.getText().equals(elementTypes[GBEAN_TYPE])) {
            if (isEmpty(textList.get(0).getText())) {
                return false;
            }
            String type = (String)eObject;
            gbeanRef = (GbeanRef)section.getSelectedObject();
            if (type == null) {
                gbeanRef.getRefType().add(textList.get(0).getText());
            }
            else {
                gbeanRef.getRefType().set(gbeanRef.getRefType().indexOf(type), textList.get(0).getText());
            }
        }
        else if (element.getText().equals(elementTypes[PATTERN])) { 
            if (isEmpty(textList.get(0).getText())) {
                return false;
            }
            Pattern pattern = (Pattern)eObject;
            if (pattern == null) {
                pattern = (Pattern)getEFactory().create(Pattern.class);
                gbeanRef = (GbeanRef)section.getSelectedObject();
                gbeanRef.getPattern().add(pattern);
            }
            pattern.setName(textList.get(0).getText());
            pattern.setGroupId(textList.get(1).getText());
            pattern.setArtifactId(textList.get(2).getText());
            pattern.setVersion(textList.get(3).getText());
            pattern.setModule(textList.get(4).getText());
        }
        return true;
    }
}
