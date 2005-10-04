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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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
        return Messages.wizardPageDescription_Dependency;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getWizardFirstPageDescription()
     */
    public String getWizardFirstPageDescription() {
        return Messages.wizardNewTitle_Dependency;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish() {
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

            Button button = new Button(group, SWT.LEFT | SWT.RADIO);
            button.setSelection(true);
            button.setText(Messages.serverRepos);
            button.setLayoutData(data);

            Label label = new Label(group, SWT.LEFT);
            label.setText(Messages.uri);
            GridData labelData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
            labelData.grabExcessHorizontalSpace = true;
            labelData.horizontalIndent = 20;
            label.setLayoutData(labelData);

            Text text = new Text(group, SWT.SINGLE | SWT.BORDER);
            text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            Button button2 = new Button(group, SWT.LEFT | SWT.RADIO);
            button2.setSelection(false);
            button2.setText(Messages.mavenArtifact);
            button2.setLayoutData(data);

            Label groupIdLabel = new Label(group, SWT.LEFT);
            groupIdLabel.setText(Messages.groupId);
            groupIdLabel.setLayoutData(labelData);

            Text groupIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
            groupIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            Label artifactIdLabel = new Label(group, SWT.LEFT);
            artifactIdLabel.setText(Messages.artifactId);
            artifactIdLabel.setLayoutData(labelData);

            Text artifactIdText = new Text(group, SWT.SINGLE | SWT.BORDER);
            artifactIdText
                    .setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            Label versionLabel = new Label(group, SWT.LEFT);
            versionLabel.setText(Messages.version);
            versionLabel.setLayoutData(labelData);

            Text versionText = new Text(group, SWT.SINGLE | SWT.BORDER);
            versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

            setControl(composite);

        }
    }

}
