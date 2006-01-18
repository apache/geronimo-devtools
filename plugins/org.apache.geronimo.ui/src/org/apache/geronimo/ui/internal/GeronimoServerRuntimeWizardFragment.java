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
package org.apache.geronimo.ui.internal;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.server.generic.core.internal.GenericServerRuntime;
import org.eclipse.jst.server.generic.ui.internal.GenericServerComposite;
import org.eclipse.jst.server.generic.ui.internal.GenericServerCompositeDecorator;
import org.eclipse.jst.server.generic.ui.internal.GenericServerUIMessages;
import org.eclipse.jst.server.generic.ui.internal.JRESelectDecorator;
import org.eclipse.jst.server.generic.ui.internal.SWTUtil;
import org.eclipse.jst.server.generic.ui.internal.ServerDefinitionTypeAwareWizardFragment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IRuntimeType;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.core.internal.IInstallableRuntime;
import org.eclipse.wst.server.core.internal.ServerPlugin;
import org.eclipse.wst.server.core.model.RuntimeDelegate;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;

public class GeronimoServerRuntimeWizardFragment extends
		ServerDefinitionTypeAwareWizardFragment {

	private static final String G_WITH_TOMCAT_ID = "org.apache.geronimo.runtime.tomcat.10";
	private static final String G_WITH_JETTY_ID = "org.apache.geronimo.runtime.jetty.10";

	private GenericServerCompositeDecorator[] fDecorators;
	protected Text installDir;

	public GeronimoServerRuntimeWizardFragment() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.ui.internal.ServerDefinitionTypeAwareWizardFragment#description()
	 */
	public String description() {
		String rName = getRuntimeName();
		if (rName == null || rName.length() < 1)
			rName = "Generic";
		return GenericServerUIMessages.bind(
				GenericServerUIMessages.runtimeWizardDescription, rName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.ui.internal.ServerDefinitionTypeAwareWizardFragment#title()
	 */
	public String title() {
		String rName = getRuntimeName();
		if (rName == null || rName.length() < 1)
			rName = "Generic";
		return GenericServerUIMessages.bind(
				GenericServerUIMessages.runtimeWizardTitle, rName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.ui.internal.ServerDefinitionTypeAwareWizardFragment#createContent(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.wst.server.ui.wizard.IWizardHandle)
	 */
	public void createContent(Composite parent, IWizardHandle handle) {

		fDecorators = new GenericServerCompositeDecorator[1];
		fDecorators[0] = new JRESelectDecorator(getRuntimeDelegate());
		GenericServerComposite composite = new GenericServerComposite(parent,
				fDecorators);

		IInstallableRuntime gWithTomcat = ServerPlugin
				.findInstallableRuntime(G_WITH_TOMCAT_ID);
		IInstallableRuntime gWithJetty = ServerPlugin
				.findInstallableRuntime(G_WITH_JETTY_ID);

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.installDir);
		GridData data = new GridData();
		data.horizontalSpan = 3;
		label.setLayoutData(data);

		installDir = new Text(composite, SWT.BORDER);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		installDir.setLayoutData(data);
		installDir.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				// runtimeWC.setLocation(new Path(installDir.getText()));
				// validate();
			}
		});

		final Composite parentComp = composite;
		Button browse = SWTUtil.createButton(composite, Messages.browse);
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				DirectoryDialog dialog = new DirectoryDialog(parentComp
						.getShell());
				dialog.setMessage(Messages.installDir);
				dialog.setFilterPath(installDir.getText());
				String selectedDirectory = dialog.open();
				if (selectedDirectory != null)
					installDir.setText(selectedDirectory);
			}
		});

		data = new GridData();
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;

		Group group = new Group(composite, SWT.NONE);
		group.setText(Messages.downloadOptions);
		group.setLayoutData(data);
		group.setLayout(composite.getLayout());

		Label webContainer = new Label(group, SWT.NONE);
		webContainer.setText(Messages.chooseWebContainer);
		data = new GridData();
		data.horizontalSpan = 3;
		webContainer.setLayoutData(data);

		Button tomcat = new Button(group, SWT.RADIO);
		tomcat.setSelection(true);
		tomcat.setText(Messages.gWithTomcat);
		data = new GridData();
		data.horizontalSpan = 3;
		tomcat.setLayoutData(data);

		Button jetty = new Button(group, SWT.RADIO);
		jetty.setText(Messages.gWithJetty);
		data = new GridData();
		data.horizontalSpan = 3;
		jetty.setLayoutData(data);

		Button install = SWTUtil.createButton(group, Messages.install);
		data = new GridData();
		data.horizontalSpan = 3;
		install.setLayoutData(data);

	}

	public void enter() {
		if (getRuntimeDelegate() != null)
			getRuntimeDelegate().getRuntimeWorkingCopy().setName(createName());

		for (int i = 0; i < fDecorators.length; i++) {
			if (fDecorators[i].validate())
				return;
		}
	}

	public void exit() {
		// validate to save latest values
		for (int i = 0; i < fDecorators.length; i++) {
			if (fDecorators[i].validate())
				return;
		}
	}

	private GenericServerRuntime getRuntimeDelegate() {
		IRuntimeWorkingCopy wc = (IRuntimeWorkingCopy) getTaskModel()
				.getObject(TaskModel.TASK_RUNTIME);
		if (wc == null)
			return null;
		return (GenericServerRuntime) wc.loadAdapter(
				GenericServerRuntime.class, new NullProgressMonitor());
	}

	private String createName() {
		RuntimeDelegate dl = getRuntimeDelegate();
		IRuntimeType runtimeType = dl.getRuntime().getRuntimeType();
		String name = GenericServerUIMessages.bind(
				GenericServerUIMessages.runtimeName, runtimeType.getName());
		IRuntime[] list = ServerCore.getRuntimes();
		int suffix = 1;
		String suffixName = name;
		for (int i = 0; i < list.length; i++) {
			if ((list[i].getName().equals(name) || list[i].getName().equals(
					suffixName))
					&& !list[i].equals(dl.getRuntime()))
				suffix++;
			suffixName = name + " " + suffix;
		}

		if (suffix > 1)
			return suffixName;
		return name;
	}

	private String getRuntimeName() {
		if (getRuntimeDelegate() != null
				&& getRuntimeDelegate().getRuntime() != null)
			return getRuntimeDelegate().getRuntime().getName();
		return null;
	}

}
