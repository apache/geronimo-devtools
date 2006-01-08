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
package org.apache.geronimo.ui.sections;

import org.apache.geronimo.ui.internal.GeronimoUIPlugin;
import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.wizards.SecurityRoleWizard;
import org.apache.geronimo.xml.ns.security.DescriptionType;
import org.apache.geronimo.xml.ns.security.RoleType;
import org.apache.geronimo.xml.ns.security.SecurityPackage;
import org.apache.geronimo.xml.ns.security.SecurityType;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SecuritySection extends AbstractTableSection {

	public EReference securityERef;

	Text roleNameText;

	Text roleDescriptionText;
	
	static {
		COLUMN_NAMES = new String[] { Messages.name };
	}

	/**
	 * @param plan
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
	public SecuritySection(EObject plan, Composite parent, FormToolkit toolkit,
			int style, EReference securityERef) {
		super(plan, parent, toolkit, style);
		this.securityERef = securityERef;
		createClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
	 */
	public String getTitle() {
		return Messages.editorSectionSecurityRolesTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
	 */
	public String getDescription() {
		return Messages.editorSectionSecurityRolesDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getEReference()
	 */
	public EReference getEReference() {
		return SecurityPackage.eINSTANCE.getRoleMappingsType_Role();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getWizard()
	 */
	public Wizard getWizard() {
		return new SecurityRoleWizard(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableEntryObjectType()
	 */
	public EClass getTableEntryObjectType() {
		return SecurityPackage.eINSTANCE.getRoleType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#isHeaderVisible()
	 */
	public boolean isHeaderVisible() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#createClient()
	 */
	public void createClient() {

		super.createClient();

		Composite detail = toolkit.createComposite(getTable().getParent());
		GridLayout gl = new GridLayout();
		gl.marginWidth = 4;
		gl.marginHeight = 8;
		gl.numColumns = 2;
		detail.setLayout(gl);
		detail.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		Label roleNameLabel = toolkit.createLabel(detail, Messages.name + ":");
		roleNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		roleNameLabel.setEnabled(true);

		roleNameText = toolkit.createText(detail, "", SWT.BORDER);
		roleNameText
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		roleNameText.setEnabled(true);

		Label roleDescriptionLabel = toolkit.createLabel(detail,
				Messages.description + ":");
		roleDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false));
		roleDescriptionLabel.setEnabled(true);

		roleDescriptionText = toolkit.createText(detail, "", SWT.MULTI
				| SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.heightHint = 50;
		roleDescriptionText.setLayoutData(data);
		roleDescriptionText.setEnabled(true);

		getTable().addSelectionListener(new TableSelectionListener());

		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				roleNameText.setText("");
				roleDescriptionText.setText("");
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getInput()
	 */
	protected Object getInput() {
		SecurityType secType = (SecurityType) getPlan().eGet(securityERef);
		if (secType != null) {
			return secType.getRoleMappings();
		}
		return super.getInput();
	}

	public ImageDescriptor getImageDescriptor() {
		return GeronimoUIPlugin.imageDescriptorFromPlugin(
				"org.eclipse.jst.j2ee", "icons/full/obj16/security_role.gif");
	}

	class TableSelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			TableItem item = (TableItem) e.item;
			RoleType roleType = (RoleType) item.getData();
			roleNameText.setText(roleType.getRoleName());

			if (!roleType.getDescription().isEmpty()) {
				roleDescriptionText.setText(((DescriptionType) roleType
						.getDescription().get(0)).getValue());
			} else {
				roleDescriptionText.setText("");
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// do nothing
		}

	}

}
