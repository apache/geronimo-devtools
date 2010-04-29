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

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.v30.core.descriptor.AbstractDeploymentDescriptor;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractTreeSection extends AbstractListSection {

    protected Tree tree;

    public AbstractTreeSection(Section section) {
        super(section);
    }

    /**
     * Subclasses should call createClient() in constructor
     */
    public AbstractTreeSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style) {
        super(parent, toolkit, style, plan);
    }

    public AbstractTreeSection(JAXBElement plan, AbstractDeploymentDescriptor descriptor,
            Composite parent, FormToolkit toolkit, int style) {
        super(parent, toolkit, style, plan, descriptor);
    }

    public void createViewer(Composite composite) {
        tree = new Tree(composite, SWT.BORDER | SWT.V_SCROLL);
        GridData data = new GridData(SWT.FILL, SWT.FILL, false, false);
        data.heightHint = 60;
        data.widthHint = 350;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        tree.setLayoutData(data);
        tree.setLayout(new TableLayout());
        tree.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                activateButtons();
            }
        });
        viewer = new TreeViewer(tree);
    }

    public void handleDelete() {
        TreeItem[] selectedItems = tree.getSelection();
        removeItem(selectedItems[0].getData());
    }

    public void removeItem(Object anItem) {
        getObjectContainer().remove(anItem);
    }

    protected void activateButton(Button button) {
        button.setEnabled(tree.getSelectionCount() > 0);
    }

    abstract public Object getSelectedObject();
}
