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
package org.apache.geronimo.st.v21.ui.internal;

import org.apache.geronimo.st.v21.core.GeronimoServerDelegate;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.internal.Messages;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.TaskModel;
import org.eclipse.wst.server.ui.wizard.IWizardHandle;
import org.eclipse.wst.server.ui.wizard.WizardFragment;

/**
 * @version $Rev: 471551 $ $Date: 2006-11-06 06:47:11 +0800 (Mon, 06 Nov 2006) $
 */
public class GeronimoServerWizardFragment extends WizardFragment {

	protected Text hostName;

	protected Text adminId;

	protected Text password;

	protected Table ports;

	protected TableViewer viewer;

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

		label = new Label(parent, SWT.NONE);
		label.setText(Messages.specifyPorts);
		GridData data = createTextGridData();
		data.verticalIndent = 15;
		data.horizontalSpan = 2;
		label.setLayoutData(data);

		// ports
		ports = new Table(parent, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.FULL_SELECTION);
		ports.setHeaderVisible(true);
		ports.setLinesVisible(false);

		TableColumn col = new TableColumn(ports, SWT.NONE);
		col.setText(Messages.portName);
		col.setResizable(false);
		ColumnWeightData colData = new ColumnWeightData(15, 150, false);
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(colData);

		col = new TableColumn(ports, SWT.NONE);
		col.setText(Messages.portValue);
		col.setResizable(false);
		colData = new ColumnWeightData(8, 80, false);
		tableLayout.addColumnData(colData);

		data = new GridData(GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_FILL);
		data.horizontalSpan = 2;
		data.heightHint = 100;
		ports.setLayoutData(data);
		ports.setLayout(tableLayout);

		viewer = new TableViewer(ports);
		viewer.setColumnProperties(new String[] { "name", "port" });

		fillTable(ports);
		addCellEditor(ports);
	}

	private void addCellEditor(Table ports) {
		viewer.setCellEditors(new CellEditor[] { null,
				new TextCellEditor(ports) });

		ICellModifier cellModifier = new ICellModifier() {
			public Object getValue(Object element, String property) {
				ServerPort sp = (ServerPort) element;
				return sp.getPort() + "";
			}

			public boolean canModify(Object element, String property) {
				return "port".equals(property);
			}

			public void modify(Object element, String property, Object value) {
				Item item = (Item) element;
				ServerPort sp = (ServerPort) item.getData();
				GeronimoServerDelegate gs = getGeronimoServer();
				gs.setInstanceProperty(sp.getId(), (String) value);
				changePortNumber(sp.getId(), Integer.parseInt((String) value));
			}
		};

		viewer.setCellModifier(cellModifier);
	}

	private void fillTable(Table ports) {
		ServerPort[] serverPorts = getServer().getServerPorts(null);
		if (serverPorts != null) {
			for (int i = 0; i < serverPorts.length; i++) {
				ServerPort port = serverPorts[i];
				TableItem item = new TableItem(ports, SWT.NONE);
				String[] s = new String[] { port.getName(),
						Integer.toString(port.getPort()) };
				item.setText(s);
				item.setImage(Activator.getImage(Activator.IMG_PORT));
				item.setData(port);
			}
		}
	}

	protected void changePortNumber(String id, int port) {
		TableItem[] items = ports.getItems();
		int size = items.length;
		for (int i = 0; i < size; i++) {
			ServerPort sp = (ServerPort) items[i].getData();
			if (sp.getId().equals(id)) {
				items[i].setData(new ServerPort(id, sp.getName(), port, sp.getProtocol()));
				items[i].setText(1, port + "");
				return;
			}
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
