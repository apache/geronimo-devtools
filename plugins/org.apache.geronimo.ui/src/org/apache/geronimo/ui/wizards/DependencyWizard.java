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

import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.sections.AbstractTableSection;
import org.apache.geronimo.xml.ns.deployment.DependencyType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DependencyWizard extends AbstractTableWizard {

	public static String wizardNewTitle_Dependency;

	public static String wizardEditTitle_Dependency;

	public static String wizardPageTitle_Dependency;

	public static String wizardPageDescription_Dependency;

	protected Button uriButton;

	protected Button mavenButton;

	protected Label uriLabel;

	protected Label groupIdLabel;

	protected Label artifactIdLabel;

	protected Label versionLabel;

	protected Text uriText;

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
		return Messages.wizardNewTitle_Dependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getEditWizardWindowTitle()
	 */
	public String getEditWizardWindowTitle() {
		return Messages.wizardEditTitle_Dependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getWizardFirstPageTitle()
	 */
	public String getWizardFirstPageTitle() {
		return Messages.wizardPageTitle_Dependency;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getWizardFirstPageDescription()
	 */
	public String getWizardFirstPageDescription() {
		return Messages.wizardPageDescription_Dependency;
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.ui.wizards.AbstractTableWizard#processEAttributes(org.eclipse.jface.wizard.IWizardPage)
	 */
	public void processEAttributes(IWizardPage page) {
		DependencyType dt = (DependencyType) eObject;

		if (uriButton.getSelection()) {
			dt.setUri(uriText.getText());
			dt.eUnset(DeploymentPackage.eINSTANCE
					.getDependencyType_ArtifactId());
			dt.eUnset(DeploymentPackage.eINSTANCE.getDependencyType_GroupId());
			dt.eUnset(DeploymentPackage.eINSTANCE.getDependencyType_Version());
		} else {
			dt.setArtifactId(artifactIdText.getText());
			dt.setGroupId(groupIdText.getText());
			dt.setVersion(versionText.getText());
			dt.eUnset(DeploymentPackage.eINSTANCE.getDependencyType_Uri());
		}
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

		public DependencyWizardPage(String pageName, String title,
				ImageDescriptor titleImage) {
			super(pageName, title, titleImage);
		}

		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NULL);

			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.horizontalSpacing = 15;
			composite.setLayout(layout);
			composite
					.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

			GridData data = new GridData();
			data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;

			Group group = new Group(composite, SWT.NONE);
			group.setText(Messages.dependencyGroupLabel);
			group.setLayoutData(data);
			group.setLayout(layout);

			uriButton = new Button(group, SWT.LEFT | SWT.RADIO);
			uriButton.setText(Messages.serverRepos);
			uriButton.setLayoutData(data);

			uriLabel = new Label(group, SWT.LEFT);
			uriLabel.setText(Messages.uri);
			uriLabel.setLayoutData(createLabelGridData());

			uriText = new Text(group, SWT.SINGLE | SWT.BORDER);
			uriText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			mavenButton = new Button(group, SWT.LEFT | SWT.RADIO);
			mavenButton.setText(Messages.mavenArtifact);
			mavenButton.setLayoutData(data);

			groupIdLabel = new Label(group, SWT.LEFT);
			groupIdLabel.setText(Messages.groupId);
			groupIdLabel.setLayoutData(createLabelGridData());

			groupIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
			groupIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			artifactIdLabel = new Label(group, SWT.LEFT);
			artifactIdLabel.setText(Messages.artifactId);
			artifactIdLabel.setLayoutData(createLabelGridData());

			artifactIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
			artifactIdText
					.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			versionLabel = new Label(group, SWT.LEFT);
			versionLabel.setText(Messages.version);
			versionLabel.setLayoutData(createLabelGridData());

			versionText = new Text(group, SWT.SINGLE | SWT.BORDER);
			versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			uriButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (uriButton.getSelection()) {
						toggle();
					}
				}
			});

			mavenButton.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					if (mavenButton.getSelection()) {
						toggle();
					}
				}
			});

			if (eObject != null) {
				if (eObject.eIsSet(DeploymentPackage.eINSTANCE
						.getDependencyType_Uri())) {
					uriButton.setSelection(true);
					uriText.setText(eObject
							.eGet(
									DeploymentPackage.eINSTANCE
											.getDependencyType_Uri())
							.toString());
				} else {
					mavenButton.setSelection(true);
					if (eObject.eIsSet(DeploymentPackage.eINSTANCE
							.getDependencyType_ArtifactId())) {
						artifactIdText.setText(eObject.eGet(
								DeploymentPackage.eINSTANCE
										.getDependencyType_ArtifactId())
								.toString());
					}
					if (eObject.eIsSet(DeploymentPackage.eINSTANCE
							.getDependencyType_GroupId())) {
						groupIdText.setText(eObject.eGet(
								DeploymentPackage.eINSTANCE
										.getDependencyType_GroupId())
								.toString());
					}
					if (eObject.eIsSet(DeploymentPackage.eINSTANCE
							.getDependencyType_Version())) {
						versionText.setText(eObject.eGet(
								DeploymentPackage.eINSTANCE
										.getDependencyType_Version())
								.toString());
					}
				}
			} else {
				uriButton.setSelection(true);
				uriLabel.setEnabled(true);
				uriText.setEnabled(true);

				mavenButton.setSelection(false);
				groupIdLabel.setEnabled(false);
				groupIdText.setEnabled(false);
				artifactIdLabel.setEnabled(false);
				artifactIdText.setEnabled(false);
				versionLabel.setEnabled(false);
				versionText.setEnabled(false);
			}

			setControl(composite);

		}
	}

	public GridData createLabelGridData() {
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalIndent = 20;
		return gd;
	}

	public void toggle() {
		uriLabel.setEnabled(!uriLabel.isEnabled());
		groupIdLabel.setEnabled(!groupIdLabel.isEnabled());
		artifactIdLabel.setEnabled(!artifactIdLabel.isEnabled());
		versionLabel.setEnabled(!versionLabel.isEnabled());
		uriText.setEnabled(!uriText.isEnabled());
		groupIdText.setEnabled(!groupIdText.isEnabled());
		artifactIdText.setEnabled(!artifactIdText.isEnabled());
		versionText.setEnabled(!versionText.isEnabled());
	}

}
