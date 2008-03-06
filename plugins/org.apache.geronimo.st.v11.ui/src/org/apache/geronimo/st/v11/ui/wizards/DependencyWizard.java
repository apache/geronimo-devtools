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
package org.apache.geronimo.st.v11.ui.wizards;

import javax.enterprise.deploy.spi.factories.DeploymentFactory;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.xml.ns.deployment_1.ArtifactType;
import org.apache.geronimo.xml.ns.deployment_1.DependenciesType;
import org.apache.geronimo.xml.ns.deployment_1.EnvironmentType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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
	
	protected Label typeLabel;

	protected Text groupIdText;

	protected Text artifactIdText;

	protected Text versionText;
	
	protected Text typeText;

	/**
	 * @param section
	 */
	public DependencyWizard(AbstractTableSection section) {
		super(section);
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getEFactory()
//	 */
//	public EFactory getEFactory() {
//		return DeploymentFactory.eINSTANCE;
//	}
//
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getTableColumnEAttributes()
	 */
	public String[] getTableColumnEAttributes() {
		return new String[] {};
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
		dt.setType(typeText.getText());
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

			groupIdLabel = new Label(composite, SWT.LEFT);
			groupIdLabel.setText(CommonMessages.groupId);
			groupIdLabel.setLayoutData(createLabelGridData());

			groupIdText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			groupIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			artifactIdLabel = new Label(composite, SWT.LEFT);
			artifactIdLabel.setText(CommonMessages.artifactId);
			artifactIdLabel.setLayoutData(createLabelGridData());

			artifactIdText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			artifactIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			versionLabel = new Label(composite, SWT.LEFT);
			versionLabel.setText(CommonMessages.version);
			versionLabel.setLayoutData(createLabelGridData());

			versionText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			
			typeLabel = new Label(composite, SWT.LEFT);
			typeLabel.setText(CommonMessages.type);
			typeLabel.setLayoutData(createLabelGridData());

			typeText = new Text(composite, SWT.SINGLE | SWT.BORDER);
			typeText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

//			if (eObject != null) {
//				if (eObject.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_ArtifactId())) {
//					artifactIdText.setText(eObject.eGet(DeploymentPackage.eINSTANCE.getArtifactType_ArtifactId()).toString());
//				}
//				if (eObject.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_GroupId())) {
//					groupIdText.setText(eObject.eGet(DeploymentPackage.eINSTANCE.getArtifactType_GroupId()).toString());
//				}
//				if (eObject.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_Version())) {
//					versionText.setText(eObject.eGet(DeploymentPackage.eINSTANCE.getArtifactType_Version()).toString());
//				}
//				if (eObject.eIsSet(DeploymentPackage.eINSTANCE.getArtifactType_Type())) {
//					typeText.setText(eObject.eGet(DeploymentPackage.eINSTANCE.getArtifactType_Type()).toString());
//				}
//			}

			setControl(composite);

		}
	}

	public GridData createLabelGridData() {
		return new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	}
	
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.ui.wizards.AbstractTableWizard#performFinish()
	 */
	public boolean performFinish() {

//		if (eObject == null) {
//			eObject = getEFactory().create(section.getTableEntryObjectType());
//			EObject plan = section.getPlan();
//			DependenciesType dependenciesType = ((EnvironmentType) plan.eGet(section.getEReference())).getDependencies();
//			if(dependenciesType == null) {
//				dependenciesType = DeploymentFactory.eINSTANCE.createDependenciesType();
//				((EnvironmentType) plan.eGet(section.getEReference())).setDependencies(dependenciesType);
//			}
//			dependenciesType.getDependency().add(eObject);
//		}

		processEAttributes(getPages()[0]);
		
		if (section.getTableViewer().getInput() == null) {
			section.getTableViewer().setInput(section.getInput());
		}

		return true;
	}
}
