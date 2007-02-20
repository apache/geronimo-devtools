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
package org.apache.geronimo.st.ui.internal;

import org.apache.geronimo.st.core.GeronimoRuntimeDelegate;
import org.apache.geronimo.st.ui.Activator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IRuntimeWorkingCopy;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.internal.SWTUtil;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoRuntimeSourceWizardFragment extends WizardFragment {
	
	private IWizardHandle fWizard;
	
	protected Text srcLoc;

	public GeronimoRuntimeSourceWizardFragment() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.wizard.WizardFragment#hasComposite()
	 */
	public boolean hasComposite() {
		return true;
	}
	
	public Composite createComposite(Composite parent, IWizardHandle handle) {
		this.fWizard = handle;
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout grid = new GridLayout(1, false);
		grid.marginWidth = 0;
		container.setLayout(grid);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		handle.setImageDescriptor(Activator.getImageDescriptor((Activator.IMG_WIZ_GERONIMO)));
		handle.setTitle(Messages.sourceLocWizTitle);
		handle.setDescription(Messages.sourceLocWizDescription);
		createContent(container, handle);
		return container;
	}

	public void createContent(Composite parent, IWizardHandle handle) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		addInstallDirSection(composite);
	}
	
	protected void addInstallDirSection(Composite composite) {
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.sourceZipFile);
		GridData data = new GridData();
		data.horizontalSpan = 3;
		label.setLayoutData(data);
		label.setToolTipText(Messages.tooltipLoc);

		srcLoc = new Text(composite, SWT.BORDER);

		IPath currentLocation = getRuntimeDelegate().getRuntimeSourceLocation();
		if (currentLocation != null) {
			srcLoc.setText(currentLocation.toOSString());
		}

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		srcLoc.setLayoutData(data);
		srcLoc.setToolTipText(Messages.tooltipLoc);
		srcLoc.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				getRuntimeDelegate().setRuntimeSourceLocation(srcLoc.getText());
			}
		});

		final Composite browseComp = composite;
		Button browse = SWTUtil.createButton(composite, Messages.browse);
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent se) {
				FileDialog dialog = new FileDialog(browseComp.getShell());
				dialog.setText(Messages.browseSrcDialog);
				dialog.setFilterPath(srcLoc.getText());
				String selected = dialog.open();
				if (selected != null)
					srcLoc.setText(selected);
			}
		});
	}
	
	private GeronimoRuntimeDelegate getRuntimeDelegate() {
		IRuntimeWorkingCopy wc = (IRuntimeWorkingCopy) getTaskModel().getObject(TaskModel.TASK_RUNTIME);
		if (wc == null)
			return null;
		return (GeronimoRuntimeDelegate) wc.loadAdapter(GeronimoRuntimeDelegate.class, new NullProgressMonitor());
	}

}
