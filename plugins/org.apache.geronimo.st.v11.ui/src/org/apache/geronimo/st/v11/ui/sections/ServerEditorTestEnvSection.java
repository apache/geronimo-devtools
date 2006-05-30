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

import org.apache.geronimo.st.v11.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.ui.editor.ServerEditorSection;

public class ServerEditorTestEnvSection extends ServerEditorSection {

	private Button runFromProject;

	private Button inPlace;

	private Button persistant;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.editor.ServerEditorSection#createSection(org.eclipse.swt.widgets.Composite)
	 */
	public void createSection(Composite parent) {
		super.createSection(parent);

		FormToolkit toolkit = getFormToolkit(parent.getDisplay());

		Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE
				| ExpandableComposite.EXPANDED
				| ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION | ExpandableComposite.FOCUS_TITLE);

		section.setText(Messages.editorSectionTestEnvTitle);
		section.setDescription(Messages.editorSectionTestEnvDescription);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Composite composite = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		section.setClient(composite);

		persistant = toolkit.createButton(composite, Messages.editorSectionSetPersistant, SWT.CHECK);
		inPlace = toolkit.createButton(composite, Messages.editorSectionEnableInPlace, SWT.CHECK);
		runFromProject = toolkit.createButton(composite, Messages.editorSectionRunFromProject, SWT.CHECK);
		
		GridData data = new GridData();
		data.horizontalIndent = 20;
		runFromProject.setLayoutData(data);
		runFromProject.setEnabled(inPlace.getSelection());

		persistant.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (persistant.getSelection()) {
					// TODO implementMe
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		inPlace.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				runFromProject.setEnabled(inPlace.getSelection());
				// TODO implementMe
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		runFromProject.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (runFromProject.getSelection()) {
					// TODO implementMe
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
	}
}
