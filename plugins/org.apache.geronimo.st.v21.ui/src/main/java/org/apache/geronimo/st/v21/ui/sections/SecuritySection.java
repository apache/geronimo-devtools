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
package org.apache.geronimo.st.v21.ui.sections;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.providers.AdapterFactory;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.SecurityRoleWizard;
import org.apache.geronimo.jee.naming.ResourceRef;
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.RoleMappings;
import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.Security;
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

	public RoleMappings roleMappings;

	Text roleNameText;

	Text roleDescriptionText;

	private static final String[] COLUMN_NAMES = new String[] { CommonMessages.name };

	/**
	 * @param plan
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
	public SecuritySection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, RoleMappings roleMappings) {
		super(plan, parent, toolkit, style);
		this.roleMappings = roleMappings;
		createClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
	 */
	public String getTitle() {
		return CommonMessages.editorSectionSecurityRolesTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
	 */
	public String getDescription() {
		return CommonMessages.editorSectionSecurityRolesDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableColumnNames()
	 */
	public String[] getTableColumnNames() {
		return COLUMN_NAMES;
	}

	public List getObjectContainer() {
		return roleMappings.getRole();
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
	public Class getTableEntryObjectType() {
		return Role.class;
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

		Label roleNameLabel = toolkit.createLabel(detail, CommonMessages.name
				+ ":");
		roleNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		roleNameLabel.setEnabled(true);

		roleNameText = toolkit.createText(detail, "", SWT.BORDER);
		roleNameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		roleNameText.setEnabled(true);

		Label roleDescriptionLabel = toolkit.createLabel(detail, CommonMessages.description
				+ ":");
		roleDescriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
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
	public Object getInput() {
		Security sec = JAXBModelUtils.getSecurity(getPlan());//.eGet(securityERef);
		if (sec != null) {
			return sec.getRoleMappings();
		}
		return super.getInput();
	}

	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee", "icons/full/obj16/security_role.gif");
	}

	class TableSelectionListener implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			TableItem item = (TableItem) e.item;
			Role role = (Role) item.getData();
			roleNameText.setText(role.getRoleName());

			if (!role.getDescription().isEmpty()) {
				roleDescriptionText.setText(((Description) role.getDescription().get(0)).getValue());
			} else {
				roleDescriptionText.setText("");
			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// do nothing
		}

	}

	public AdapterFactory getAdapterFactory() {
		return new AdapterFactory() {
			public Object[] getElements(Object inputElement) {
				if (!RoleMappings.class.isInstance(inputElement)) {
					return new String[] { "" };
				}
				RoleMappings plan = (RoleMappings)inputElement;
				return plan.getRole().toArray();
			}
			public String getColumnText(Object element, int columnIndex) {
				if (Role.class.isInstance(element)) {
					Role role = (Role)element;
					switch (columnIndex) {
					case 0: return role.getRoleName();
					}
				}
				return null;
			}
		};
	}
}
