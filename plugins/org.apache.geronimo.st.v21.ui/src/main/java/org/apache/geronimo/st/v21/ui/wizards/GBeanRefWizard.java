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
import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;
import org.eclipse.jface.wizard.IWizardPage;

public class GBeanRefWizard extends AbstractTableWizard {

    public GBeanRefWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
         return new String[] { "RefName", "RefType" };
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_GBeanRef;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_GBeanRef;
    }

    public String getWizardFirstPageTitle() {
        return CommonMessages.wizardPageTitle_GBeanRef;
    }

    public String getWizardFirstPageDescription() {
        return CommonMessages.wizardPageDescription_GBeanRef;
    }

    public boolean performFinish() {
        DynamicWizardPage page = (DynamicWizardPage) getPages()[0];

        if (eObject == null) {
            eObject = getEFactory().create(GbeanRef.class);
            JAXBElement plan = section.getPlan();

            // add the JAXBElement of a GBean, not the GBean
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBModelUtils.getGbeanRefs(plan).add(objectFactory.createGbeanRef((GbeanRef)eObject));
        }

        processEAttributes (page);

        if (section.getTableViewer().getInput() == null) {
            section.getTableViewer().setInput(section.getInput());
        }

        return true;
    }

    public void processEAttributes(IWizardPage page) {
        if (page instanceof DynamicWizardPage) {
            DynamicWizardPage dynamicPage = (DynamicWizardPage)page;
            GbeanRef gbeanRef = (GbeanRef)eObject;
            String value = dynamicPage.getTextEntry(0).getText();
            gbeanRef.setRefName(value);

            value = dynamicPage.getTextEntry(1).getText();
            gbeanRef.getRefType().add(value);
        }
    }
}
