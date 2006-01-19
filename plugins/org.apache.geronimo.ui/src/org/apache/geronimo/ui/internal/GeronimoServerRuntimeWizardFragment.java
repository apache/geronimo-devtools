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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jst.server.generic.core.internal.GenericServerRuntime;
import org.eclipse.jst.server.generic.servertype.definition.Property;
import org.eclipse.jst.server.generic.servertype.definition.ServerRuntime;
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

	private Button tomcat;
	private Button jetty;

	private Group group;

	public GeronimoServerRuntimeWizardFragment() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.ui.internal.ServerDefinitionTypeAwareWizardFragment#description()
	 */
	public String description() {
		return Messages.serverWizardDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.ui.internal.ServerDefinitionTypeAwareWizardFragment#title()
	 */
	public String title() {
		return GenericServerUIMessages.bind(
				GenericServerUIMessages.runtimeWizardTitle, getRuntimeName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jst.server.generic.ui.internal.ServerDefinitionTypeAwareWizardFragment#createContent(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.wst.server.ui.wizard.IWizardHandle)
	 */
	public void createContent(Composite parent, IWizardHandle handle) {

		fDecorators = new GenericServerCompositeDecorator[1];
		fDecorators[0] = new GeronimoJRESelectDecorator(getRuntimeDelegate());
		GenericServerComposite composite = new GenericServerComposite(parent,
				fDecorators);

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
				getRuntimeDelegate().getRuntimeWorkingCopy().setLocation(
						new Path(installDir.getText()));
				validate();
			}
		});

		final Composite browseComp = composite;
		Button browse = SWTUtil.createButton(composite, Messages.browse);
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				DirectoryDialog dialog = new DirectoryDialog(browseComp
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

		final IInstallableRuntime gWithTomcat = ServerPlugin
				.findInstallableRuntime(G_WITH_TOMCAT_ID);
		final IInstallableRuntime gWithJetty = ServerPlugin
				.findInstallableRuntime(G_WITH_JETTY_ID);

		if (gWithTomcat != null && gWithJetty != null) {
			group = new Group(composite, SWT.NONE);
			group.setText(Messages.downloadOptions);
			group.setLayoutData(data);
			group.setLayout(composite.getLayout());
			group.setEnabled(false);

			Label webContainer = new Label(group, SWT.NONE);
			webContainer.setText(Messages.chooseWebContainer);
			data = new GridData();
			data.horizontalSpan = 3;
			webContainer.setLayoutData(data);

			tomcat = new Button(group, SWT.RADIO);
			tomcat.setSelection(true);
			tomcat.setText(Messages.gWithTomcat);
			data = new GridData();
			data.horizontalSpan = 3;
			tomcat.setLayoutData(data);

			jetty = new Button(group, SWT.RADIO);
			jetty.setText(Messages.gWithJetty);
			data = new GridData();
			data.horizontalSpan = 3;
			jetty.setLayoutData(data);

			Button install = SWTUtil.createButton(group, Messages.install);
			data = new GridData();
			data.horizontalSpan = 3;
			install.setLayoutData(data);

			install.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent se) {
					if (installDir != null && isValidLocation()) {
						try {
							Path installPath = new Path(installDir.getText());
							if (tomcat.getSelection()) {
								gWithTomcat.install(installPath,
										new NullProgressMonitor());
							} else {
								gWithJetty.install(installPath,
										new NullProgressMonitor());
							}
							updateInstallDir(installPath);
						} catch (Exception e) {
							Trace.trace(Trace.SEVERE,
									"Error installing runtime", e);
						}
					}
				}

				boolean isValidLocation() {
					return true;
				}

				void updateInstallDir(IPath installPath) {
					installPath = installPath.append("geronimo-1.0");
					installDir.setText(installPath.toOSString());
				}
			});
		}

	}

	protected void validate() {

		IRuntime runtime = getRuntimeDelegate().getRuntime();

		if (runtime == null) {
			getWizard().setMessage("", IMessageProvider.ERROR);
			return;
		}

		IRuntimeWorkingCopy runtimeWC = getRuntimeDelegate()
				.getRuntimeWorkingCopy();
		getRuntimeDelegate().setServerDefinitionId(
				runtime.getRuntimeType().getId());
		getRuntimeDelegate().setServerInstanceProperties(getValues());

		IStatus status = runtimeWC.validate(null);
		if (status == null || status.isOK()) {
			getWizard().setMessage(null, IMessageProvider.NONE);
			group.setEnabled(false);
		} else {
			getWizard().setMessage(status.getMessage(), IMessageProvider.ERROR);
			group.setEnabled(true);
			return;
		}

		validateDecorators();
	}

	private void validateDecorators() {
		for (int i = 0; i < fDecorators.length; i++) {
			if (fDecorators[i].validate())
				return;
		}
	}

	private Map getValues() {
		Map propertyMap = new HashMap();
		if (getRuntimeDelegate() != null) {
			ServerRuntime definition = getServerTypeDefinition(
					getServerDefinitionId(), getRuntimeDelegate()
							.getServerInstanceProperties());
			if (definition != null) {
				List properties = definition.getProperty();
				for (int i = 0; i < properties.size(); i++) {
					Property property = (Property) properties.get(i);
					if (Property.CONTEXT_RUNTIME.equals(property.getContext())) {
						if (Property.TYPE_DIRECTORY.equals(property.getType())) {
							propertyMap.put(property.getId(), installDir
									.getText());
						}
					}
				}
			}
		}
		return propertyMap;
	}

	private String getServerDefinitionId() {
		String currentDefinition = null;
		if (getRuntimeDelegate() != null)
			currentDefinition = getRuntimeDelegate().getRuntime()
					.getRuntimeType().getId();
		if (currentDefinition != null && currentDefinition.length() > 0) {
			return currentDefinition;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.wizard.WizardFragment#enter()
	 */
	public void enter() {
		if (getRuntimeDelegate() != null)
			getRuntimeDelegate().getRuntimeWorkingCopy().setName(createName());
		validate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.wizard.WizardFragment#exit()
	 */
	public void exit() {
		// validate to save latest values
		validateDecorators();
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

	private class GeronimoJRESelectDecorator extends JRESelectDecorator {
		public GeronimoJRESelectDecorator(GenericServerRuntime runtime) {
			super(runtime);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jst.server.generic.ui.internal.GenericServerCompositeDecorator#validate()
		 */
		public boolean validate() {
			if (isValidVM()) {
				getWizard().setMessage(Messages.jvmWarning,
						IMessageProvider.WARNING);
				return true;
			}
			getWizard().setMessage(null, IMessageProvider.NONE);
			return false;
		}

		private boolean isValidVM() {
			String vmId = getRuntimeDelegate().getVMInstallId();
			return vmId == null || !vmId.startsWith("1.4");
		}
	}

}
