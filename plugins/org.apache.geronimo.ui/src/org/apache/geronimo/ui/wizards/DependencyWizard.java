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
import org.apache.geronimo.ui.sections.DynamicTableSection;
import org.apache.geronimo.xml.ns.deployment.DependencyType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
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

public class DependencyWizard extends DynamicAddEditWizard {

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
    public DependencyWizard(DynamicTableSection section) {
        super(section);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish() {
        if (eObject == null) {
            eObject = DeploymentFactory.eINSTANCE.createDependencyType();
            ((EList) section.getPlan().eGet(section.getEReference())).add(eObject);
        }
        
        DependencyType dt = (DependencyType) eObject;
        
        if (uriButton.getSelection()) {
            dt.setUri(uriText.getText());
            dt.eUnset(DeploymentPackage.eINSTANCE.getDependencyType_ArtifactId());
            dt.eUnset(DeploymentPackage.eINSTANCE.getDependencyType_GroupId());
            dt.eUnset(DeploymentPackage.eINSTANCE.getDependencyType_Version());
        } else {
            dt.setArtifactId(artifactIdText.getText());
            dt.setGroupId(groupIdText.getText());
            dt.setVersion(versionText.getText());
            dt.eUnset(DeploymentPackage.eINSTANCE.getDependencyType_Uri());
        }

        return true;
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

        Text[] textEntries = new Text[section.getTableColumnEAttributes().length];

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
            uriButton.setSelection(true);
            uriButton.setText(Messages.serverRepos);
            uriButton.setLayoutData(data);

            uriLabel = new Label(group, SWT.LEFT);
            uriLabel.setText(Messages.uri);
            GridData labelData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
            labelData.horizontalIndent = 20;
            labelData.widthHint = 50;
            uriLabel.setLayoutData(labelData);

            uriText = new Text(group, SWT.SINGLE | SWT.BORDER);
            uriText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            mavenButton = new Button(group, SWT.LEFT | SWT.RADIO);
            mavenButton.setSelection(false);
            mavenButton.setText(Messages.mavenArtifact);
            mavenButton.setLayoutData(data);

            groupIdLabel = new Label(group, SWT.LEFT);
            groupIdLabel.setText(Messages.groupId);
            groupIdLabel.setLayoutData(labelData);

            groupIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
            groupIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            artifactIdLabel = new Label(group, SWT.LEFT);
            artifactIdLabel.setText(Messages.artifactId);
            artifactIdLabel.setLayoutData(labelData);

            artifactIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
            artifactIdText
                    .setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            versionLabel = new Label(group, SWT.LEFT);
            versionLabel.setText(Messages.version);
            versionLabel.setLayoutData(labelData);

            versionText = new Text(group, SWT.SINGLE | SWT.BORDER);
            versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            groupIdLabel.setEnabled(false);
            groupIdText.setEnabled(false);
            artifactIdLabel.setEnabled(false);
            artifactIdText.setEnabled(false);
            versionLabel.setEnabled(false);
            versionText.setEnabled(false);

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

            setControl(composite);

        }
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
