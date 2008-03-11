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
package org.apache.geronimo.st.ui.wizards;

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.st.core.jaxb.JAXBUtils;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractTableWizard extends Wizard implements TableWizard {

	protected AbstractTableSection section;

	protected Object eObject;

	protected ImageDescriptor descriptor = Activator.imageDescriptorFromPlugin("org.apache.geronimo.ui", "icons/bigG.gif");

	/**
	 * 
	 */
	public AbstractTableWizard(AbstractTableSection section) {
		super();
		this.section = section;
		setWindowTitle(getAddWizardWindowTitle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {

		if (eObject == null) {
			eObject = getEFactory().create(section.getTableEntryObjectType());
			List container = section.getObjectContainer();
			container.add(eObject);
			System.out.println( "-==--=-=" + container );
		}

		processEAttributes(getPages()[0]);

		return true;
	}

	public void processEAttributes(IWizardPage page) {
		if (page instanceof DynamicWizardPage) {
			for (int i = 0; i < getTableColumnEAttributes().length; i++) {
				String value = ((DynamicWizardPage) page).textEntries[i].getText();
				String attribute = getTableColumnEAttributes()[i];
				JAXBUtils.setValue(eObject, attribute, value);
//				if (attribute.getEContainingClass().equals(eObject.eClass())) {
//					if (value != null && value.trim().length() != 0) {
//						if(attribute.isMany()) {
//							((EList) eObject.eGet(attribute)).add(value);
//						} else {
//							eObject.eSet(attribute, value);
//						}
//					}
//				} else {
//					// TODO
//				}
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
	public void setSection(AbstractTableSection section) {
		this.section = section;
	}

	/**
	 * @param object
	 */
	public void setEObject(Object object) {
		eObject = object;
	}

	public class DynamicWizardPage extends WizardPage {

		Text[] textEntries = new Text[getTableColumnEAttributes().length];

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
					String value = (String) JAXBUtils.getValue(eObject,getTableColumnEAttributes()[i]);
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
	
	public String[] getTableColumnEAttributes() {
		return new String[] { "", "" };
	}

	//todo: should be removed
	public JAXBObjectFactory getEFactory() {
		return null;
	}
	
}
