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
package org.apache.geronimo.st.v1.ui.pages;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.pages.AbstractGeronimoFormPage;
import org.apache.geronimo.st.v1.ui.sections.DependencySection;
import org.apache.geronimo.st.v1.ui.sections.GBeanSection;
import org.apache.geronimo.st.v1.ui.sections.ImportSection;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * @version $Rev$ $Date$
 */
public class DeploymentPage extends AbstractGeronimoFormPage {

	public EReference dependencies;

	public EReference imports;

	public EReference gbeans;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public DeploymentPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#fillBody(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void fillBody(IManagedForm managedForm) {
		managedForm.addPart(new DependencySection(getDeploymentPlan(), dependencies, body, toolkit, getStyle()));
		managedForm.addPart(new ImportSection(getDeploymentPlan(), imports, body, toolkit, getStyle()));
		managedForm.addPart(new GBeanSection(getDeploymentPlan(), gbeans, body, toolkit, getStyle()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#getFormTitle()
	 */
	public String getFormTitle() {
		return CommonMessages.deploymentPageTitle;
	}

}
