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
package org.apache.geronimo.st.v11.ui.sections;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.xml.ns.j2ee.web.WebAppType;
import org.apache.geronimo.xml.ns.j2ee.web.WebPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class WebGeneralSection extends CommonGeneralSection {

	protected Text contextRoot;

	WebAppType plan;

	public WebGeneralSection(Composite parent, FormToolkit toolkit, int style, EObject plan) {
		super(parent, toolkit, style, plan);
		this.plan = (WebAppType) plan;
		createClient();
	}

	protected void createClient() {
		super.createClient();
		Composite composite = (Composite) getSection().getClient();

		createLabel(composite, CommonMessages.editorContextRoot, toolkit);

		contextRoot = toolkit.createText(composite, plan.getContextRoot(), SWT.BORDER);
		contextRoot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		contextRoot.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				plan.setContextRoot(contextRoot.getText());
				markDirty();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.v11.ui.sections.CommonGeneralSection#getEnvironmentEReference()
	 */
	protected EReference getEnvironmentEReference() {
		return WebPackage.eINSTANCE.getWebAppType_Environment();
	}
}
