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

import org.apache.geronimo.jaxbmodel.common.operations.JAXBObjectFactory;
import org.apache.geronimo.jaxbmodel.common.operations.JAXBUtils;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBModelUtils;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.jee.naming.Pattern;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public class MessageDestWizard extends AbstractTableWizard {

    public MessageDestWizard(AbstractTableSection section) {
        super(section);
    }

    public JAXBObjectFactory getEFactory() {
        return JAXBObjectFactoryImpl.getInstance();
    }

    public String[] getTableColumnEAttributes() {
        return new String[] { "MessageDestinationName", "AdminObjectModule", "AdminObjectLink",
                "GroupId", "ArtifactId", "Version", "Module", "Name"};
    }

    public String getAddWizardWindowTitle() {
        return CommonMessages.wizardNewTitle_MessageDest;
    }

    public String getEditWizardWindowTitle() {
        return CommonMessages.wizardEditTitle_MessageDest;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        addPage(new MessageDestWizardPage("Page0"));
    }

    // need to extend the DynamicWizardPage only so that when the Edit dialog is shown
    // the values are brought in properly.
    public class MessageDestWizardPage extends AbstractTableWizardPage {
        public MessageDestWizardPage(String pageName) {
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
                    if (i > 2) {
                        // get the pattern value
                        Pattern pattern = ((MessageDestination) eObject).getPattern();
                        String value = null;
                        try {
                            value = (String) JAXBUtils.getValue(pattern,getTableColumnEAttributes()[i]);
                        } catch (Exception e) {
                            MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.error, e.getMessage());
                        }
                        if (value != null) {
                            text.setText(value);
                        }                        
                    }
                    else
                    {
                        String value = null;
                        try {
                            value = (String) JAXBUtils.getValue(eObject,getTableColumnEAttributes()[i]);
                        } catch (Exception e) {
                            MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.error, e.getMessage());
                        }
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

        public String getWizardPageTitle() {
            return CommonMessages.wizardPageTitle_MessageDest;
        }

        public String getWizardPageDescription() {
            return CommonMessages.wizardPageDescription_MessageDest;
        }
    }
    
    public boolean performFinish() {
        AbstractTableWizardPage page = (AbstractTableWizardPage) getPages()[0];
        Pattern msgPattern;
        MessageDestination messageDest;

        if (eObject == null) {
            eObject = getEFactory().create(MessageDestination.class);
            JAXBElement plan = section.getPlan();

            messageDest = (MessageDestination)eObject;
            msgPattern = (Pattern)getEFactory().create(Pattern.class);
            messageDest.setPattern(msgPattern);

            List msgDestList = JAXBModelUtils.getMessageDestinations(plan); 
            if (msgDestList == null) {
                msgDestList = (List)getEFactory().create(MessageDestination.class);
            }
            msgDestList.add(eObject);
        }

        // NOTE!! this replaces the call to processEAttributes (page);
        messageDest =(MessageDestination) eObject; 
        msgPattern = messageDest.getPattern();
        for (int i = 0; i < 8; i++) {
            String value = page.getTextEntry(i).getText();
            String attribute = getTableColumnEAttributes()[i];
            if (i < 3)
                try {
                    JAXBUtils.setValue(eObject, attribute, value);
                } catch (Exception e) {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.error, e.getMessage());
                }
            else
                try {
                    JAXBUtils.setValue(msgPattern, attribute, value);
                } catch (Exception e) {
                    MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.error, e.getMessage());
                }
        }
        
        if (section.getViewer().getInput() == section.getPlan()) {
            section.getViewer().setInput(section.getInput());
        }

        return true;
    }
}
