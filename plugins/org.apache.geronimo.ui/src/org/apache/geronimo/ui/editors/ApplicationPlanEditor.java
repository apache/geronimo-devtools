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
package org.apache.geronimo.ui.editors;

import org.apache.geronimo.core.internal.GeronimoUtils;
import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.pages.AppGeneralPage;
import org.apache.geronimo.ui.pages.DeploymentPage;
import org.apache.geronimo.ui.pages.SecurityPage;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationFactory;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationPackage;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormPage;

public class ApplicationPlanEditor extends AbstractGeronimoDeploymentPlanEditor {

	public ApplicationPlanEditor() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.editors.AbstractGeronimoDeploymentPlanEditor#doAddPages()
	 */
	public void doAddPages() throws PartInitException {
		if (getDeploymentPlan() != null) {
			addPage(new AppGeneralPage(this, "appgeneralpage",
					Messages.editorTabGeneral));
			addPage(new SecurityPage(this, "securitypage",
					Messages.editorTabSecurity, ApplicationPackage.eINSTANCE
							.getApplicationType_Security()));
			addPage(getDeploymentPage());
		}
		addSourcePage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.editors.AbstractGeronimoDeploymentPlanEditor#loadDeploymentPlan(org.eclipse.core.resources.IFile)
	 */
	public EObject loadDeploymentPlan(IFile file) {
		return GeronimoUtils.getApplicationDeploymentPlan(file);
	}

	private FormPage getDeploymentPage() {
		DeploymentPage formPage = new DeploymentPage(this, "deploymentpage",
				Messages.editorTabDeployment);
		ApplicationPackage pkg = ApplicationFactory.eINSTANCE
				.getApplicationPackage();
		formPage.dependencies = pkg.getApplicationType_Dependency();
		formPage.imports = pkg.getApplicationType_Import();
		formPage.gbeans = pkg.getApplicationType_Gbean();
		return formPage;
	}

}
