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
package org.apache.geronimo.ui.sections;

import org.apache.geronimo.core.internal.GeronimoServer;
import org.apache.geronimo.ui.commands.SetHTTPPortCommand;
import org.apache.geronimo.ui.commands.SetRMIPortCommand;
import org.apache.geronimo.ui.internal.Messages;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.ui.editor.ServerEditorSection;

/**
 * @author sppatel
 * 
 */
public class ServerPortsEditorSection extends ServerEditorSection {

	Text httpPort;

	Text rmiPort;

	GeronimoServer gs;

	public ServerPortsEditorSection() {
		super();
	}

	public void createSection(Composite parent) {
		super.createSection(parent);

		FormToolkit toolkit = getFormToolkit(parent.getDisplay());

		Section section = toolkit.createSection(parent,
				ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
						| ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
						| ExpandableComposite.FOCUS_TITLE);

		section.setText(Messages.editorSectionPortsTitle);
		section.setDescription(Messages.editorSectionPortsDescription);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		Composite composite = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		section.setClient(composite);

		// ------- Label and text field for the http port -------
		createLabel(composite, Messages.httpPort, toolkit);

		httpPort = toolkit.createText(composite, getHTTPPort(), SWT.BORDER);
		httpPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		httpPort.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				execute(new SetHTTPPortCommand(server, httpPort.getText()));
			}
		});

		// ------- Label and text field for the rmi port -------
		createLabel(composite, Messages.rmiPort, toolkit);

		rmiPort = toolkit.createText(composite, getRMIPort(), SWT.BORDER);
		rmiPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		rmiPort.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				execute(new SetRMIPortCommand(server, rmiPort.getText()));
			}
		});
	}

	protected Label createLabel(Composite parent, String text,
			FormToolkit toolkit) {
		Label label = toolkit.createLabel(parent, text);
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.editor.ServerEditorSection#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		super.init(site, input);
		gs = (GeronimoServer) server.getAdapter(GeronimoServer.class);
		if (gs == null) {
			gs = (GeronimoServer) server.loadAdapter(GeronimoServer.class,
					new NullProgressMonitor());
		}
	}

	private String getHTTPPort() {
		if (gs != null) {
			return gs.getHTTPPort();
		}
		return "";
	}

	private String getRMIPort() {
		if (gs != null) {
			return gs.getRMINamingPort();
		}
		return "";
	}

}
