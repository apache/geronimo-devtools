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
package org.apache.geronimo.st.v30.ui.sections;

import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.apache.geronimo.st.v30.ui.wizards.AbstractWizard;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * @version $Rev$ $Date$
 */
public class ListEditor {

    private String title;
    private String[] defaultValues;
    
    protected FormToolkit toolkit;
    
    protected Button addButton;
    protected Button editButton;
    protected Button removeButton;
    protected List listWidget;
    
    private Composite clientComposite;
    
    public ListEditor(FormToolkit toolkit) { 
        this.toolkit = toolkit;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setDefaultValues(java.util.List<String> defaultValues) {
        String[] values = new String[defaultValues.size()];
        this.defaultValues = defaultValues.toArray(values);
    }
    
    public void setDefaultValues(String [] defaultValues) {
        this.defaultValues = defaultValues;
    }
    
    public void setItems(String[] items) {
        listWidget.setItems(items);
    }
    
    public void setEnabled(boolean enabled) {
        listWidget.setEnabled(enabled);
        if (enabled) {
            activateButtons();
        } else {
            addButton.setEnabled(false);
            editButton.setEnabled(false);
            removeButton.setEnabled(false);
        }       
    }
    
    public Composite getComposite() {
        return clientComposite;
    }
    
    public void createClient(Composite parent) {
        clientComposite = createComposite(parent, 3);
                
        Label titleLabel = createLabel(clientComposite, title);
        titleLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        
        Hyperlink restoreDefaults = new Hyperlink(clientComposite, SWT.NULL);
        restoreDefaults.setText(CommonMessages.restoreDefaults);
        restoreDefaults.setForeground(parent.getShell().getDisplay().getSystemColor(SWT.COLOR_BLUE));
        restoreDefaults.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        restoreDefaults.addHyperlinkListener(new IHyperlinkListener() {
            public void linkActivated(HyperlinkEvent arg0) {
                listWidget.setItems(defaultValues);
                modified();
                activateButtons();
            }
            public void linkEntered(HyperlinkEvent arg0) {}
            public void linkExited(HyperlinkEvent arg0) {}
        });
        Label emptyLabel = createLabel(clientComposite, "");
        emptyLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));

        listWidget = new List (clientComposite, SWT.BORDER | SWT.V_SCROLL);
        listWidget.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 5));
        listWidget.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                activateButtons();                
            }
        });
        createAddButton(clientComposite);
        createRemoveButton(clientComposite);
        createEditButton(clientComposite);
        
        activateButtons();
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
        return composite;
    }

    protected Label createLabel(Composite parent, String text) {
        Label label = toolkit.createLabel(parent, text);
        label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        return label;
    }

    protected void createRemoveButton(Composite buttonComp) {
        removeButton = toolkit.createButton(buttonComp, CommonMessages.remove, SWT.NONE);
        removeButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                deleteSelectedItem();
                modified();
                activateButtons();
            }
        });
        removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    }

    protected void createAddButton(Composite buttonComp) {
        addButton = toolkit.createButton(buttonComp, CommonMessages.add, SWT.NONE);
        addButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Wizard wizard = getWizard();
                if (wizard != null) {
                    WizardDialog dialog = createWizardDialog(Display.getCurrent().getActiveShell(), wizard);
                    dialog.open();
                    if (dialog.getReturnCode() == Dialog.OK) {
                        modified();
                        activateButtons();
                    }
                }
            }
        });
        addButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    }

    protected void createEditButton(Composite buttonComp) {
        editButton = toolkit.createButton(buttonComp, CommonMessages.edit, SWT.NONE);
        editButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                int index = listWidget.getSelectionIndex();
                if (index > -1) {
                    Wizard wizard = getWizard();
                    if (wizard != null) {
                        if (wizard instanceof AbstractWizard) {
                            ((AbstractWizard) wizard).setEObject(listWidget.getItem(index));
                        }
                        WizardDialog dialog = createWizardDialog(Display.getCurrent().getActiveShell(), wizard);
                        dialog.open();
                        if (dialog.getReturnCode() == Dialog.OK) {
                            modified();
                        }
                    }
                }                
            }
        });
        editButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    }
          
    protected WizardDialog createWizardDialog(Shell parentShell, IWizard newWizard) {
        return new WizardDialog(parentShell, newWizard);
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
   
    public void addItem(String value) {
        listWidget.add(value);
    }
    
    public void setSelectedItem(String value) {
        int index = listWidget.getSelectionIndex();
        if (index > -1) {
            listWidget.setItem(index, value);
        }
    }
    
    private void deleteSelectedItem() {
        int index = listWidget.getSelectionIndex();
        if (index > -1) {
            listWidget.remove(index);
        }
    }

    protected void activateButton(Button button) {
        if (listWidget.getItems().length == 0) {
            button.setEnabled(false);
        } else {
            button.setEnabled(listWidget.getSelectionIndex() > -1);
        }        
    }

    protected Wizard getWizard() {
        return null;
    }
    
    public String[] getItems() {
        return listWidget.getItems();
    }
    
    public void modified() {
    }

}
