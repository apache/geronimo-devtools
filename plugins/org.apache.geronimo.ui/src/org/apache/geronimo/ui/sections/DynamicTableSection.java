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

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.ui.internal.GeronimoUIPlugin;
import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.internal.Trace;
import org.apache.geronimo.ui.wizards.DynamicAddEditWizard;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public abstract class DynamicTableSection extends AbstractSectionPart {

	private Image image;

	protected Table table;

	protected TableViewer tableViewer;

	private ImageDescriptor defaultImgDesc = GeronimoUIPlugin
			.imageDescriptorFromPlugin("org.apache.geronimo.ui",
					"icons/obj16/geronimo.gif");

	Button addButton;

	Button editButton;

	Button removeButton;

	public DynamicTableSection(Section section) {
		super(section);
	}

	/**
	 * @param plan
	 * @param parent
	 * @param toolkit
	 * @param style
	 * 
	 * Subclasses should call create() in constructor
	 */
	public DynamicTableSection(EObject plan, Composite parent,
			FormToolkit toolkit, int style) {
		super(parent, toolkit, style, plan);
	}

	public void create() {
		if (isValid()) {
			createClient();
		} else {
			Trace.trace(Trace.SEVERE, "Could not create client, "
					+ getClass().getName() + ".isValid() == false");
		}

	}

	private boolean isValid() {
		return getTableEntryObjectType() != null
				&& getTableColumnNames() != null && !getFactories().isEmpty();
	}

	public void createClient() {

		getSection().setText(getTitle());
		getSection().setDescription(getDescription());
		getSection().setLayoutData(getSectionLayoutData());
		Composite composite = createTableComposite(getSection());
		getSection().setClient(composite);
		createTable(composite);

		ComposedAdapterFactory caf = new ComposedAdapterFactory(getFactories());

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new AdapterFactoryContentProvider(caf));
		tableViewer.setLabelProvider(new AdapterFactoryLabelProvider(caf));
		tableViewer.setInput(plan);

		tableViewer.addFilter(new ViewerFilter() {
			public boolean select(Viewer viewer, Object parentElement,
					Object element) {
				return getTableEntryObjectType().isInstance(element);
			}
		});

		if (getTableColumnNames().length > 0) {
			tableViewer.setColumnProperties(getTableColumnNames());
		}

		Composite buttonComp = createButtonComposite(composite);
		createAddButton(toolkit, buttonComp);
		createRemoveButton(toolkit, buttonComp);
		createEditButton(toolkit, buttonComp);

	}

	abstract public List getFactories();

	abstract public EClass getTableEntryObjectType();

	protected Composite createTableComposite(Composite parent) {
		Composite composite = toolkit.createComposite(parent);
		composite.setLayout(getSectionCompositeLayout());
		composite.setLayoutData(getTableCompositeLayoutData());
		return composite;
	}

	protected GridData getSectionLayoutData() {
		return new GridData(SWT.FILL, SWT.FILL, false, false);
	}

	protected GridData getTableCompositeLayoutData() {
		return new GridData(SWT.FILL, SWT.FILL, false, false);
	}

	protected GridLayout getSectionCompositeLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 15;
		return layout;
	}

	protected void createTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION
				| SWT.V_SCROLL | SWT.SINGLE);
		if (isHeaderVisible()) {
			table.setHeaderVisible(true);
		}

		GridData data = new GridData(SWT.FILL, SWT.FILL, false, false);
		data.heightHint = 60;
		data.widthHint = 400;
		table.setLayoutData(data);

		TableLayout tableLayout = new TableLayout();
		table.setLayout(tableLayout);

		for (int i = 0; i < getTableColumnNames().length; i++) {
			tableLayout.addColumnData(new ColumnWeightData(35));
			TableColumn tableColumn = new TableColumn(table, SWT.NONE);
			tableColumn.setText(getTableColumnNames()[i]);
		}

	}

	protected Composite createButtonComposite(Composite parent) {
		Composite buttonComp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 2;
		layout.verticalSpacing = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 1;
		buttonComp.setLayout(layout);
		buttonComp.setBackground(toolkit.getColors().getBackground());
		buttonComp
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		return buttonComp;
	}

	protected void createRemoveButton(FormToolkit toolkit, Composite buttonComp) {
		removeButton = toolkit.createButton(buttonComp, Messages.remove,
				SWT.NONE);
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int[] selectedIndices = table.getSelectionIndices();
				for (int i = 0; i < selectedIndices.length; i++) {
					TableItem tableItem = table.getItem(selectedIndices[i]);
					EObject type = (EObject) (tableItem.getData());
					table.remove(selectedIndices[i]);
					EcoreUtil.remove(type);
					markDirty();
				}
			}
		});
		removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
	}

	protected void createAddButton(FormToolkit toolkit, Composite buttonComp) {
		addButton = toolkit.createButton(buttonComp, Messages.add, SWT.NONE);

		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Wizard wizard = getWizard();
				if (wizard != null) {
					WizardDialog dialog = new WizardDialog(Display.getCurrent()
							.getActiveShell(), wizard);

					dialog.open();

					if (dialog.getReturnCode() == Dialog.OK) {
						markDirty();
					}
				}
			}
		});

		addButton
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	protected void createEditButton(FormToolkit toolkit, Composite buttonComp) {
		editButton = toolkit.createButton(buttonComp, Messages.edit, SWT.NONE);

		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Object o = ((StructuredSelection) getTableViewer()
						.getSelection()).getFirstElement();
				if (o != null) {
					Wizard wizard = getWizard();
					if (wizard != null) {
						if (wizard instanceof DynamicAddEditWizard) {
							((DynamicAddEditWizard) wizard)
									.setEObject((EObject) o);
						}
						WizardDialog dialog = new WizardDialog(Display
								.getCurrent().getActiveShell(), wizard);
						dialog.open();
						if (dialog.getReturnCode() == Dialog.OK) {
							markDirty();
							// TODO notify listeners
						}
					}
				}
			}
		});

		editButton
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

	public TableViewer getTableViewer() {
		return tableViewer;
	}

	public EObject getPlan() {
		return plan;
	}

	public boolean isHeaderVisible() {
		return true;
	}

	/**
	 * @return
	 */
	abstract public String getTitle();

	/**
	 * @return
	 */
	abstract public String getDescription();

	/**
	 * @deprecated
	 * @return
	 */
	abstract public EReference getEReference();

	/**
	 * @return
	 */
	abstract public String[] getTableColumnNames();

	/**
	 * @return
	 */
	abstract public Wizard getWizard();

}
