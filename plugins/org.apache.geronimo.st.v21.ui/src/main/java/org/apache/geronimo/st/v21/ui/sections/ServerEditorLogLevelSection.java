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

import org.apache.geronimo.st.v21.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v21.ui.commands.SetConsoleLogLevelCommand;
import org.apache.geronimo.st.ui.internal.Messages;
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

/**
 * @version $Rev: 732518 $ $Date: 2009-01-08 05:50:54 +0800 (Thu, 08 Jan 2009) $
 */
public class ServerEditorLogLevelSection extends AbstractServerEditorSection {

	Button info;

	Button debug;

	public ServerEditorLogLevelSection() {
		super();
	}

	public void createSection(Composite parent) {
		super.createSection(parent);

		FormToolkit toolkit = getFormToolkit(parent.getDisplay());

		Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE
				| ExpandableComposite.EXPANDED
				| ExpandableComposite.TITLE_BAR
				| Section.DESCRIPTION | ExpandableComposite.FOCUS_TITLE);

		section.setText(Messages.editorSectionLogLevelTitle);
		section.setDescription(Messages.editorSectionLogLevelDescription);
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

		info = toolkit.createButton(composite, Messages.info, SWT.RADIO);
		debug = toolkit.createButton(composite, Messages.debug, SWT.RADIO);
		
		String currentValue = ((GeronimoServerDelegate)server.getAdapter(GeronimoServerDelegate.class)).getConsoleLogLevel();
		if(GeronimoServerDelegate.CONSOLE_DEBUG.equals(currentValue)) {
			debug.setSelection(true);
		} else {
			info.setSelection(true);
		}

		info.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (info.getSelection()) {
					execute(new SetConsoleLogLevelCommand(server, GeronimoServerDelegate.CONSOLE_INFO));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		debug.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (debug.getSelection()) {
					execute(new SetConsoleLogLevelCommand(server, GeronimoServerDelegate.CONSOLE_DEBUG));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
}
