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

import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.j2ee.jaxbmodel.operations.JAXBObjectFactoryImpl;

/**
 * @version $Rev$ $Date$
 */
public class EjbRefWizard extends AbstractTableWizard {

    public EjbRefWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
        return new String[] { "RefName", "EjbLink" };
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_EjbRef;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_EjbRef;
    }
    
    public void addPages() {
        addPage(new EjbRefWizardPage("Page0"));
    }

    public class EjbRefWizardPage extends AbstractTableWizardPage {
        public EjbRefWizardPage(String pageName) {
            super(pageName);
        }

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_EjbRef;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_EjbRef;
        }
    }
}
