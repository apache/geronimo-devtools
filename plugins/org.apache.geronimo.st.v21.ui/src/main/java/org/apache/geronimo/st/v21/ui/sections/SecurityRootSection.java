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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractSectionPart;
import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;
import org.apache.geronimo.jee.security.Security;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public class SecurityRootSection extends AbstractSectionPart {

	Security secERef;

	Text defaultRole;

	Button doas;

	Button useCtxHdl;

	/**
	 * @param section
	 */
	public SecurityRootSection(Section section) {
		super(section);
	}

	/**
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
	public SecurityRootSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan, Security secERef) {
		super(parent, toolkit, style, plan);
		this.secERef = secERef;
		createClient();
	}

	protected void createClient() {
		Section section = getSection();

		section.setText(CommonMessages.editorSectionGeneralTitle);
		section.setDescription(CommonMessages.editorSectionGeneralDescription);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Composite composite = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		section.setClient(composite);

		createLabel(composite, CommonMessages.defaultRole, toolkit);
		defaultRole = toolkit.createText(composite, getDefaultRole(), SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = 150;
		defaultRole.setLayoutData(gd);
		defaultRole.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getSecurity().setDefaultRole(defaultRole.getText());
				markDirty();
			}
		});

		doas = toolkit.createButton(composite, CommonMessages.doasCurrentCaller, SWT.CHECK);
		doas.setLayoutData(createGridData());
		doas.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				getSecurity().setDoasCurrentCaller(doas.getSelection());
				markDirty();
			}
		});
		doas.setSelection(getSecurity().isDoasCurrentCaller());

		useCtxHdl = toolkit.createButton(composite, CommonMessages.useContextHandler, SWT.CHECK);
		useCtxHdl.setLayoutData(createGridData());
		useCtxHdl.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				getSecurity().setUseContextHandler(useCtxHdl.getSelection());
				markDirty();
			}
		});
		useCtxHdl.setSelection(getSecurity().isUseContextHandler());

	}

	protected GridData createGridData() {
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.horizontalSpan = 2;
		return gd;
	}

	protected Label createLabel(Composite parent, String text, FormToolkit toolkit) {
		Label label = toolkit.createLabel(parent, text);
		label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		return label;
	}

	private String getDefaultRole() {
		Security security = JAXBModelUtils.getSecurity(getPlan());
		if (security != null
				&& security.getDefaultRole() != null ) {
			return security.getDefaultRole();
		}
		return "";
	}

	private Security getSecurity() {
		Security security = JAXBModelUtils.getSecurity(getPlan());
		if (security == null) {
			security = (Security)JAXBObjectFactoryImpl.getInstance().create( Security.class );
			JAXBModelUtils.setSecurity(getPlan(),security);
		}
		return security;
	}

}
