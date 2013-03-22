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
package org.apache.geronimo.st.v30.ui.internal;

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.sections.PortEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServerWizardFragment extends WizardFragment {

    protected Text hostName;

    protected Text adminId;

    protected Text password;

    /* (non-Javadoc)
     * @see org.eclipse.wst.server.ui.wizard.WizardFragment#hasComposite()
     */
    public boolean hasComposite() {
        return true;
    }

    public Composite createComposite(Composite parent, IWizardHandle handle) {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout grid = new GridLayout(2, false);
        grid.marginWidth = 0;
        container.setLayout(grid);
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        handle.setImageDescriptor(Activator.getImageDescriptor(Activator.IMG_WIZ_GERONIMO));
        handle.setTitle(Messages.bind(Messages.newServerWizardTitle, getServerName()));
        handle.setDescription(Messages.bind(Messages.newServerWizardTitle, getServerName()));
        createContent(container, handle);
        return container;
    }

    public void createContent(Composite parent, IWizardHandle handle) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(Messages.hostName);
        label.setLayoutData(new GridData());

        hostName = new Text(parent, SWT.BORDER);
        hostName.setLayoutData(createTextGridData());
        hostName.setText(getServer().getHost());

        hostName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getServer().setHost(hostName.getText());
            }
        });

        label = new Label(parent, SWT.NONE);
        label.setText(Messages.adminId);
        label.setLayoutData(new GridData());

        adminId = new Text(parent, SWT.BORDER);
        adminId.setLayoutData(createTextGridData());
        adminId.setText(getGeronimoServer().getAdminID());

        adminId.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getGeronimoServer().setAdminID(adminId.getText());
            }
        });

        label = new Label(parent, SWT.NONE);
        label.setText(Messages.adminPassword);
        label.setLayoutData(new GridData());

        password = new Text(parent, SWT.BORDER | SWT.PASSWORD);
        password.setLayoutData(createTextGridData());
        password.setText(getGeronimoServer().getAdminPassword());

        password.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getGeronimoServer().setAdminPassword(password.getText());
            }
        });

		Group portsGroup = new Group(parent, SWT.SHADOW_IN);
		portsGroup.setText(Messages.specifyPorts);
		
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        portsGroup.setLayout(layout);
        
        GridData gd = new GridData();
        gd.verticalIndent = 15;
        gd.horizontalAlignment = GridData.FILL;
        gd.horizontalSpan = 2;
        portsGroup.setLayoutData(gd);

		PortEditor editor = new ServerWizardPortEditor(getServer());
		editor.init(portsGroup);
    }

    class ServerWizardPortEditor extends PortEditor {

        public ServerWizardPortEditor(IServerWorkingCopy server) {
            super(server);
        }

        @Override
        protected void setPortOffset(Text portOffset) {
            getGeronimoServer().setPortOffset(Integer.parseInt(portOffset.getText()));
        }

        @Override
        protected void setRmiPort(Text rmiPort) {
            getGeronimoServer().setRMINamingPort(rmiPort.getText());
        }

        @Override
        protected void setHttpPort(Text httpPort) {
            getGeronimoServer().setHTTPPort(httpPort.getText());
        }

        @Override
        protected Label createLabel(Composite parent, String text) {
            Label label = new Label(parent, SWT.NONE);
            label.setText(text);
            return label;
        }

        @Override
        protected Text createText(Composite parent, String value, int style) {
            Text text = new Text(parent, style);
            text.setText(value);
            return text;
        }
        
    }

    private String getServerName() {
        if (getServer() != null && getServer().getRuntime() != null)
            return getServer().getRuntime().getRuntimeType().getName();
        return null;
    }

    private IServerWorkingCopy getServer() {
        return (IServerWorkingCopy) getTaskModel().getObject(TaskModel.TASK_SERVER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.ui.wizard.WizardFragment#isComplete()
     */
    public boolean isComplete() {
        //TODO
        return true;
    }

    private GeronimoServerDelegate getGeronimoServer() {
        GeronimoServerDelegate gs = (GeronimoServerDelegate) getServer().getAdapter(GeronimoServerDelegate.class);
        if (gs == null)
            gs = (GeronimoServerDelegate) getServer().loadAdapter(GeronimoServerDelegate.class, null);
        return gs;
    }

    private GridData createTextGridData() {
        return new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
    }
}
