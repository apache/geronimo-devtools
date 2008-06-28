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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.naming.GbeanRef;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.st.core.jaxb.JAXBUtils;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
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
 
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        GbeanRefWizardPage page = new GbeanRefWizardPage("Page0");
        page.setImageDescriptor(descriptor);
        addPage(page);
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class GbeanRefWizardPage extends DynamicWizardPage {
        public GbeanRefWizardPage(String pageName) {
            super(pageName);
        }

        public void createControl(Composite parent) {
            Composite composite = createComposite(parent);
            for (int i = 0; i < section.getTableColumnNames().length; i++) {
                Label label = new Label(composite, SWT.LEFT);
                String columnName = section.getTableColumnNames()[i];
                if (!columnName.endsWith(":"))
                    columnName = columnName.concat(":");
                label.setText(columnName);
                GridData data = new GridData();
                data.horizontalAlignment = GridData.FILL;
                label.setLayoutData(data);

                Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
                data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                        | GridData.VERTICAL_ALIGN_FILL);
                data.grabExcessHorizontalSpace = true;
                data.widthHint = 100;
                text.setLayoutData(data);
                if (eObject != null) {
                    if (i == 1) {
                        // get the description
                        GbeanRef gbeanRef = (GbeanRef) eObject;
                        String value = gbeanRef.getRefType().get(0);
                        if (value != null) {
                            text.setText(value);
                        }                        
                    }
                    else
                    {
                        String value = (String) JAXBUtils.getValue(eObject,getTableColumnEAttributes()[i]);
                        if (value != null) {
                            text.setText(value);
                        }
                    }
                }
                textEntries[i] = text;
            }

            doCustom(composite);
            setControl(composite);
            textEntries[0].setFocus();
        }
    }
    
    public boolean performFinish() {
        DynamicWizardPage page = (DynamicWizardPage) getPages()[0];

        if (eObject == null) {
            eObject = getEFactory().create(GbeanRef.class);
            JAXBElement plan = section.getPlan();

            List gbeanRefList = JAXBModelUtils.getGbeanRefs(plan); 
            if (gbeanRefList == null) {
                gbeanRefList = (List)getEFactory().create(GbeanRef.class);
            }
            gbeanRefList.add(eObject);
        }

        // NOTE!! this replaces the call to processEAttributes (page);
        String value = page.getTextEntry(0).getText();
        String attribute = getTableColumnEAttributes()[0];
        JAXBUtils.setValue(eObject, attribute, value);

        Description type = null;
        GbeanRef gbeanRef = (GbeanRef) eObject;
        gbeanRef.getRefType().add(page.getTextEntry(1).getText());

        if (section.getTableViewer().getInput() == section.getPlan()) {
            section.getTableViewer().setInput(section.getInput());
        }

        return true;
    }
}
