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

import java.util.List;

import org.apache.geronimo.jee.connector.Adminobject;
import org.apache.geronimo.jee.connector.AdminobjectInstance;
import org.apache.geronimo.jee.connector.ConfigPropertySetting;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTreeSection;
import org.apache.geronimo.st.ui.wizards.AbstractTreeWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.v21.ui.sections.AdminObjectSection;

/**
 * @version $Rev$ $Date$
 */
public class AdminObjectWizard extends AbstractTreeWizard {

    private final int ADMIN_OBJECT = 0;
    private final int ADMIN_OBJECT_INSTANCE = 1;
    private final int CONFIG_PROPERTY_SETTING = 2;

    public AdminObjectWizard(AbstractTreeSection section) {
        super(section, 3, 2);
        elementTypes[ADMIN_OBJECT] = "Admin Object";
        elementTypes[ADMIN_OBJECT_INSTANCE] = "Admin Object Instance";
        elementTypes[CONFIG_PROPERTY_SETTING] = "Config Property";
    }

    public class EjbRelationWizardPage extends AbstractTreeWizardPage {

        public EjbRelationWizardPage(String pageName) {
            super(pageName);
        }

        protected void initControl () {
            if (eObject == null) {
                element.select(ADMIN_OBJECT);
                if (Adminobject.class.isInstance(((AdminObjectSection)section).getSelectedObject())) {
                    element.remove(elementTypes[CONFIG_PROPERTY_SETTING]);
                }
                else if (AdminobjectInstance.class.isInstance(((AdminObjectSection)section).getSelectedObject())) {
                    element.select(CONFIG_PROPERTY_SETTING);
                    element.setEnabled(false);
                }
                else {
                    element.setEnabled(false);
                }
            }
            else {
                if (Adminobject.class.isInstance(eObject)) {
                    textList.get(0).setText(((Adminobject)eObject).getAdminobjectInterface());
                    textList.get(1).setText(((Adminobject)eObject).getAdminobjectClass());
                    element.select(ADMIN_OBJECT);
                }
                else if (AdminobjectInstance.class.isInstance(eObject)) {
                    textList.get(0).setText(((AdminobjectInstance)eObject).getMessageDestinationName());
                    element.select(ADMIN_OBJECT_INSTANCE);
                }
                else if (ConfigPropertySetting.class.isInstance(eObject)) {
                    textList.get(0).setText(((ConfigPropertySetting)eObject).getName());
                    textList.get(1).setText(((ConfigPropertySetting)eObject).getValue());
                    element.select(CONFIG_PROPERTY_SETTING);
                }
                element.setEnabled(false);
            }
        }
        
        protected void toggleFields (boolean clearFields) {
            if (element.getText().equals(elementTypes[ADMIN_OBJECT])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(true);
                    textList.get(i).setVisible(true);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.interfaceName);
                labelList.get(1).setText(CommonMessages.className);
            }
            else if (element.getText().equals(elementTypes[ADMIN_OBJECT_INSTANCE])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(i < 1 ? true : false);
                    textList.get(i).setVisible(i < 1 ? true : false);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.messageDestinationName);
            }
            else if (element.getText().equals(elementTypes[CONFIG_PROPERTY_SETTING])) {
                for (int i = 0; i < maxTextFields; i++) {
                    labelList.get(i).setVisible(true);
                    textList.get(i).setVisible(true);
                    if (clearFields == true) {
                        textList.get(i).setText("");
                    }
                }
                labelList.get(0).setText(CommonMessages.name);
                labelList.get(1).setText(CommonMessages.value);
            }
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_AdminObject;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_AdminObject;
        }
    }

    @Override
    public void addPages() {
        addPage(new EjbRelationWizardPage("Page0"));
    }

    @Override
    public boolean performFinish() {
        Adminobject admin;
        if (element.getText().equals(elementTypes[ADMIN_OBJECT])) {
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            admin = (Adminobject)eObject;
            if (admin == null) {
                admin = (Adminobject)getEFactory().create(Adminobject.class);
                List objectList = (List)section.getInput();
                objectList.add (admin);
            }
            admin.setAdminobjectInterface(textList.get(0).getText());
            admin.setAdminobjectClass(textList.get(1).getText());
        }
        else if (element.getText().equals(elementTypes[ADMIN_OBJECT_INSTANCE])) {
            if (isEmpty(textList.get(0).getText())) {
                return false;
            }
            AdminobjectInstance aoInstance = (AdminobjectInstance)eObject;
            if (aoInstance == null) {
                aoInstance = (AdminobjectInstance)getEFactory().create(AdminobjectInstance.class);
                admin = (Adminobject)((AdminObjectSection)section).getSelectedObject();
                admin.getAdminobjectInstance().add(aoInstance);
            }
            aoInstance.setMessageDestinationName(textList.get(0).getText());
        }
        else if (element.getText().equals(elementTypes[CONFIG_PROPERTY_SETTING])) { 
            if (isEmpty(textList.get(0).getText()) || isEmpty(textList.get(1).getText())) {
                return false;
            }
            ConfigPropertySetting property = (ConfigPropertySetting)eObject;
            if (property == null) {
                property = (ConfigPropertySetting)getEFactory().create(ConfigPropertySetting.class);
                AdminobjectInstance aoInstance = (AdminobjectInstance)((AdminObjectSection)section).getSelectedObject();
                aoInstance.getConfigPropertySetting().add(property);
            }
            property.setName(textList.get(0).getText());
            property.setValue(textList.get(1).getText());
        }
        return true;
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_AdminObject;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_AdminObject;
    }
}
