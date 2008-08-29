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
package org.apache.geronimo.st.ui.sections;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.core.descriptor.AbstractDeploymentDescriptor;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.wizards.AbstractWizard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractListSection extends AbstractSectionPart {

    protected ColumnViewer viewer;

    protected Button addButton;

    protected Button editButton;

    protected Button removeButton;

    protected List objectContainer;

    public AbstractListSection(Section section) {
        super(section);
    }

    /**
     * Subclasses should call createClient() in constructor
     */
    public AbstractListSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style, plan);
    }

    public AbstractListSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan,
            AbstractDeploymentDescriptor descriptor) {
        super(parent, toolkit, style, plan, descriptor);
    }

    public void createClient() {
        getSection().setText(getTitle());
        getSection().setDescription(getDescription());
        getSection().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        getSection().setExpanded(false);
        Composite clientComposite = createComposite(getSection(), 2);
        getSection().setClient(clientComposite);

        createViewer(clientComposite);
        viewer.setContentProvider(getContentProvider());
        viewer.setLabelProvider(getLabelProvider());
        viewer.setInput(getInput());

        Composite buttonComposite = createButtonComposite(clientComposite);
        createAddButton(toolkit, buttonComposite);
        createRemoveButton(toolkit, buttonComposite);
        createEditButton(toolkit, buttonComposite);
        activateButtons();

        if (isRequiredSyncToolbarAction()) {
            addSyncToolbarAction();
        }
    }

    protected boolean isRequiredSyncToolbarAction() {
        return false;
    }

    protected void addSyncToolbarAction() {
        ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
        ToolBar toolbar = toolBarManager.createControl(getSection());
        final Cursor handCursor = new Cursor(Display.getCurrent(), SWT.CURSOR_HAND);
        toolbar.setCursor(handCursor);
        // Cursor needs to be explicitly disposed
        toolbar.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if ((handCursor != null) && (handCursor.isDisposed() == false)) {
                    handCursor.dispose();
                }
            }
        });
        toolBarManager.add(getSyncAction());
        toolBarManager.update(true);
        getSection().setTextClient(toolbar);
    }

    protected class SyncAction extends Action {
        public SyncAction(String tooltipText) {
            super(tooltipText, IAction.AS_PUSH_BUTTON);
            setToolTipText(tooltipText);
            setImageDescriptor(Activator.imageDescriptorFromPlugin("org.apache.geronimo.st.ui",
                    "icons/obj16/synced.gif"));
        }

        @Override
        public void run() {
        }
    }

    protected IAction getSyncAction() {
        return new SyncAction("Sync Deployment Descriptor and Deployemnt Plan");
    }

    protected Composite createComposite(Composite parent, int numColumns) {
        Composite composite = toolkit.createComposite(parent);
        GridLayout layout = new GridLayout();
        layout.numColumns = numColumns;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        return composite;
    }

    protected abstract void createViewer(Composite composite);

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
        buttonComp.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        return buttonComp;
    }

    protected void createRemoveButton(FormToolkit toolkit, Composite buttonComp) {
        removeButton = toolkit.createButton(buttonComp, CommonMessages.remove, SWT.NONE);
        removeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleDelete();
                getViewer().refresh();
                markDirty();
                activateButtons();
            }
        });
        removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    protected abstract void handleDelete();

    protected void createAddButton(FormToolkit toolkit, Composite buttonComp) {
        addButton = toolkit.createButton(buttonComp, CommonMessages.add, SWT.NONE);
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Wizard wizard = getWizard();
                if (wizard != null) {
                    WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                    dialog.open();
                    if (dialog.getReturnCode() == Dialog.OK) {
                        getViewer().refresh();
                        markDirty();
                    }
                }
            }
        });
        addButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    protected void createEditButton(FormToolkit toolkit, Composite buttonComp) {
        editButton = toolkit.createButton(buttonComp, CommonMessages.edit, SWT.NONE);
        editButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Object selectedObject = ((StructuredSelection) getViewer().getSelection()).getFirstElement();
                if (selectedObject != null) {
                    Wizard wizard = getWizard();
                    if (wizard != null) {
                        if (wizard instanceof AbstractWizard) {
                            ((AbstractWizard) wizard).setEObject(selectedObject);
                        }
                        WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
                        dialog.open();
                        if (dialog.getReturnCode() == Dialog.OK) {
                            getViewer().refresh();
                            markDirty();
                        }
                    }
                }
                activateButtons();
            }
        });
        editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    }

    protected void activateButtons() {
        activateAddButton();
        activateRemoveButton();
        activateEditButton();
    }

    protected void activateAddButton() {
        addButton.setEnabled(true);
    }

    protected void activateEditButton() {
        activateButton(editButton);
    }

    protected void activateRemoveButton() {
        activateButton(removeButton);
    }

    protected abstract void activateButton(Button button);

    abstract public String getTitle();

    abstract public String getDescription();

    abstract protected Wizard getWizard();

    abstract public Class getTableEntryObjectType();

    protected String[] COLUMN_NAMES = new String[] {};

    public String[] getTableColumnNames() {
        return COLUMN_NAMES;
    }

    public ColumnViewer getViewer() {
        return viewer;
    }

    public List getObjectContainer() {
        if (objectContainer == null) {
            objectContainer = new ArrayList();
        }
        return objectContainer;
    }

    public Object getInput() {
        return getPlan();
    }

    public class ContentProvider implements IStructuredContentProvider, ITreeContentProvider {
        public Object[] getElements(Object inputElement) {
            if (!JAXBElement.class.isInstance(inputElement)) {
                return new String[] { "" };
            }
            return getObjectContainer().toArray();
        }

        public void dispose() {
        }

        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        }

        public Object[] getChildren(Object arg0) {
            return null;
        }

        public Object getParent(Object arg0) {
            return null;
        }

        public boolean hasChildren(Object parentElement) {
            return getChildren(parentElement).length > 0;
        }
    }

    public IContentProvider getContentProvider() {
        return new ContentProvider();
    }

    public class LabelProvider implements ITableLabelProvider, ILabelProvider {
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            return "";
        }

        public void addListener(ILabelProviderListener arg0) {
        }

        public void dispose() {
        }

        public boolean isLabelProperty(Object arg0, String arg1) {
            return false;
        }

        public void removeListener(ILabelProviderListener arg0) {
        }

        public Image getImage(Object arg0) {
            return null;
        }

        public String getText(Object arg0) {
            return "";
        }
    }

    public IBaseLabelProvider getLabelProvider() {
        return new LabelProvider();
    }
}
