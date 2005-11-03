/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.ui.wizards;

import org.apache.geronimo.ui.internal.GeronimoUIPlugin;
import org.apache.geronimo.ui.sections.DynamicTableSection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public abstract class DynamicAddEditWizard extends Wizard {

    DynamicTableSection section;

    EObject eObject;
    
    ImageDescriptor descriptor = GeronimoUIPlugin.imageDescriptorFromPlugin(
            "org.apache.geronimo.ui", "icons/bigG.gif");

    /**
     * 
     */
    public DynamicAddEditWizard(DynamicTableSection section) {
        super();
        this.section = section;
        setWindowTitle(getAddWizardWindowTitle());      
    }

    /**
     * @return
     */
    abstract public String getAddWizardWindowTitle();

    /**
     * @return
     */
    abstract public String getEditWizardWindowTitle();

    /**
     * @return
     */
    abstract public String getWizardFirstPageTitle();

    /**
     * @return
     */
    abstract public String getWizardFirstPageDescription();

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish() {
        DynamicWizardPage page = (DynamicWizardPage) getPages()[0];

        boolean isNew = false;

        if (eObject == null) {
            eObject = section.getEFactory().create(
                    section.getTableColumnEAttributes()[0]
                            .getEContainingClass());
            EObject plan = section.getPlan();           
            
            ((EList) plan.eGet(section.getEReference())).add(eObject);
            isNew = true;
        }
        
        processEAttributes(page);

        String[] tableText = section.getTableText(eObject);

        if (isNew) {
            TableItem item = new TableItem(section.getTableViewer().getTable(),
                    SWT.NONE);
            item.setImage(section.getImage());
            item.setData(eObject);
            item.setText(tableText);
        } else {
            int index = section.getTableViewer().getTable().getSelectionIndex();
            if (index != -1) {
                TableItem item = section.getTableViewer().getTable().getItem(
                        index);
                item.setText(tableText);
            }
        }

        return true;
    }

	public void processEAttributes(DynamicWizardPage page) {
		for (int i = 0; i < section.getTableColumnEAttributes().length; i++) {
            String value = page.textEntries[i].getText();
            EAttribute attribute = section.getTableColumnEAttributes()[i];
            if (attribute.getEContainingClass().equals(eObject.eClass())) {
                eObject.eSet(attribute, value);
            } else {
            	//TODO
            }
        }
	}

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#addPages()
     */
    public void addPages() {
        WizardPage page = new DynamicWizardPage("Page0");
        page.setImageDescriptor(descriptor);
        addPage(page);
    }

    /**
     * @param section
     */
    public void setSection(DynamicTableSection section) {
        this.section = section;
    }

    /**
     * @param object
     */
    public void setEObject(EObject object) {
        eObject = object;
    }

    public class DynamicWizardPage extends WizardPage {

        Text[] textEntries = new Text[section.getTableColumnEAttributes().length];

        public DynamicWizardPage(String pageName) {
            super(pageName);
            setTitle(getWizardFirstPageTitle());
            setDescription(getWizardFirstPageDescription());
        }

        public DynamicWizardPage(String pageName, String title,
                ImageDescriptor titleImage) {
            super(pageName, title, titleImage);
        }

        public void createControl(Composite parent) {
			Composite composite = createComposite(parent);
            for (int i = 0; i < section.getTableColumnNames().length; i++) {
                Label label = new Label(composite, SWT.LEFT);
                String columnName = section.getTableColumnNames()[i];
                if(!columnName.endsWith(":"))
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
                    String value = (String) eObject.eGet(section
                            .getTableColumnEAttributes()[i]);
                    if (value != null) {
                        text.setText(value);
                    }
                }
                textEntries[i] = text;
            }
            
            doCustom(composite);
            setControl(composite);                                    
            textEntries[0].setFocus();
        }

		public Composite createComposite(Composite parent) {
			Composite composite = new Composite(parent, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.numColumns = 2;
            composite.setLayout(layout);
            GridData data = new GridData();
            data.verticalAlignment = GridData.FILL;
            data.horizontalAlignment = GridData.FILL;
            data.widthHint = 300;
            composite.setLayoutData(data);
			return composite;
		}
        
        public void doCustom(Composite parent) {
        	
        }

    }

}
