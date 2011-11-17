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

import org.apache.geronimo.j2ee.deployment.ClassFilterType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.v11.ui.sections.ClassFilterSection;

/**
 * @version $Rev$ $Date$
 */
public class ClassFilterWizard extends AbstractTableWizard {

    protected boolean isServerEnvironment;
    
    public ClassFilterWizard(AbstractTableSection section, boolean isServerEnvironment) {
        super(section);
        this.isServerEnvironment = isServerEnvironment;
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
        return new String[] { "Value" };
    }

    public void addPages() {
        addPage(new ClassFilterWizardPage("Page0"));
    }
    
    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_Dependency;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_Dependency;
    }

    public class ClassFilterWizardPage extends AbstractTableWizardPage {
        public ClassFilterWizardPage(String pageName) {
            super(pageName);
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_Dependency;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_Dependency;
        }
    }
    
    /* (non-Javadoc)
     * @see org.apache.geronimo.st.ui.wizards.AbstractTableWizard#performFinish()
     */
    public boolean performFinish() {
        AbstractTableWizardPage page = (AbstractTableWizardPage) getPages()[0];

        if (eObject == null) {
            eObject = new String();
            JAXBElement plan = section.getPlan();

            EnvironmentType environment = null;
          	environment = JAXBModelUtils.getEnvironment(plan, isServerEnvironment);
            if (environment == null) {
                environment = (EnvironmentType)getEFactory().create(EnvironmentType.class);
                JAXBModelUtils.setEnvironment (plan, environment, isServerEnvironment);
            }

            ClassFilterType filter = ((ClassFilterSection)section).getClassFilter();
            if (filter == null) {
                filter = (ClassFilterType)getEFactory().create(ClassFilterType.class);
                ((ClassFilterSection)section).setClassFilter (filter);
            }
            filter.getFilter().add(page.getTextEntry(0).getText());
        }
        else {
        	ClassFilterType filter = ((ClassFilterSection)section).getClassFilter();
            filter.getFilter().set(filter.getFilter().indexOf(eObject), page.getTextEntry(0).getText());
        }

        if (section.getViewer().getInput() == null) {
            section.getViewer().setInput(section.getInput());
        }

        return true;
    }
}
