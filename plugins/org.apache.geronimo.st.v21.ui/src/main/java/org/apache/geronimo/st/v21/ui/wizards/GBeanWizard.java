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

import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.jee.deployment.ObjectFactory;
import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;

/**
 * @version $Rev$ $Date$
 */
public class GBeanWizard extends AbstractTableWizard {

    public GBeanWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }
    
    public String[] getTableColumnEAttributes() {
        return new String[] { "Name", "Clazz" };
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_GBean;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_GBean;
    }

    public String getWizardFirstPageTitle() {
        return CommonMessages.wizardEditTitle_GBean;
    }

    public String getWizardFirstPageDescription() {
        return CommonMessages.wizardPageTitle_GBean;
    }

    public boolean performFinish() {
        DynamicWizardPage page = (DynamicWizardPage) getPages()[0];

        if (eObject == null) {
            eObject = getEFactory().create(Gbean.class);
            JAXBElement plan = section.getPlan();

            // add the JAXBElement of a GBean, not the GBean
            ObjectFactory objectFactory = new ObjectFactory();
            JAXBModelUtils.getGbeans(plan).add(objectFactory.createGbean((Gbean)eObject));
        }

        processEAttributes (page);

        if (section.getViewer().getInput() == null) {
            section.getViewer().setInput(section.getInput());
        }

        return true;
    }
}
