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
package org.apache.geronimo.st.v11.ui.wizards;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.xml.ns.deployment.ArtifactType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DependencyWizard extends AbstractTableWizard {

	public static String wizardNewTitle_Dependency;

	public static String wizardEditTitle_Dependency;

	public static String wizardPageTitle_Dependency;

	public static String wizardPageDescription_Dependency;

	protected Label groupIdLabel;

	protected Label artifactIdLabel;

	protected Label versionLabel;

	protected Text groupIdText;

	protected Text artifactIdText;

	protected Text versionText;

	/**
	 * @param section
	 */
	public DependencyWizard(AbstractTableSection section) {
		super(section);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getEFactory()
	 */
	public EFactory getEFactory() {
		return DeploymentFactory.eINSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getTableColumnEAttributes()
	 */
	public EAttribute[] getTableColumnEAttributes() {
		return new EAttribute[] {};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getAddWizardWindowTitle()
	 */
	public String getAddWizardWindowTitle() {
		return CommonMessages.wizardNewTitle_Dependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getEditWizardWindowTitle()
	 */
	public String getEditWizardWindowTitle() {
		return CommonMessages.wizardEditTitle_Dependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getWizardFirstPageTitle()
	 */
	public String getWizardFirstPageTitle() {
		return CommonMessages.wizardPageTitle_Dependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getWizardFirstPageDescription()
	 */
	public String getWizardFirstPageDescription() {
		return CommonMessages.wizardPageDescription_Dependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.AbstractTableWizard#processEAttributes(org.eclipse.jface.wizard.IWizardPage)
	 */
	public void processEAttributes(IWizardPage page) {
		ArtifactType dt = (ArtifactType) eObject;
		dt.setArtifactId(artifactIdText.getText());
		dt.setGroupId(groupIdText.getText());
		dt.setVersion(versionText.getText());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		WizardPage page = new DependencyWizardPage("Page0");
		addPage(page);
	}

	public class DependencyWizardPage extends WizardPage {

		public DependencyWizardPage(String pageName) {
			super(pageName);
			setTitle(getWizardFirstPageTitle());
			setDescription(getWizardFirstPageDescription());
		}

		public DependencyWizardPage(String pageName, String title, ImageDescriptor titleImage) {
			super(pageName, title, titleImage);
		}

		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NULL);

			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.horizontalSpacing = 15;
			composite.setLayout(layout);
			composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

			GridData data = new GridData();
			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;

			Group group = new Group(composite, SWT.NONE);
			group.setText(CommonMessages.dependencyGroupLabel);
			group.setLayoutData(data);
			group.setLayout(layout);

			groupIdLabel = new Label(group, SWT.LEFT);
			groupIdLabel.setText(CommonMessages.groupId);
			groupIdLabel.setLayoutData(createLabelGridData());

			groupIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
			groupIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			artifactIdLabel = new Label(group, SWT.LEFT);
			artifactIdLabel.setText(CommonMessages.artifactId);
			artifactIdLabel.setLayoutData(createLabelGridData());

			artifactIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
			artifactIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			versionLabel = new Label(group, SWT.LEFT);
			versionLabel.setText(CommonMessages.version);
			versionLabel.setLayoutData(createLabelGridData());

			versionText = new Text(group, SWT.SINGLE | SWT.BORDER);
			versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			if (eObject.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_ArtifactId())) {
				artifactIdText.setText(eObject.eGet(DeploymentPackage.eINSTANCE.getArtifactType_ArtifactId()).toString());
			}
			if (eObject.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_GroupId())) {
				groupIdText.setText(eObject.eGet(DeploymentPackage.eINSTANCE.getArtifactType_GroupId()).toString());
			}
			if (eObject.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_Version())) {
				versionText.setText(eObject.eGet(DeploymentPackage.eINSTANCE.getArtifactType_Version()).toString());
			}

			setControl(composite);

		}
	}

	public GridData createLabelGridData() {
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalIndent = 20;
		return gd;
	}
}
