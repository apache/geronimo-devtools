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
package org.apache.geronimo.st.v21.ui.pages;

import org.apache.geronimo.st.ui.internal.Messages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.wst.server.ui.editor.ServerEditorPart;
import org.eclipse.wst.server.ui.internal.ImageResource;

/**
 * Server advanced editor page.
 *
 * @version $Rev$ $Date$
 */
public class ServerPluginPage extends ServerEditorPart {
    protected ManagedForm managedForm;

    /**
     * ServerAdvancedPage constructor comment.
     */
    public ServerPluginPage() {
        super();
    }

    /**
     * Creates the SWT controls for this workbench part.
     *
     * @param parent the parent control
     */
    public final void createPartControl(final Composite parent) {
        managedForm = new ManagedForm(parent);
        setManagedForm(managedForm);
        ScrolledForm form = managedForm.getForm();
        FormToolkit toolkit = managedForm.getToolkit();
        toolkit.decorateFormHeading(form.getForm());
        form.setText(Messages.plugin);
        form.setImage(ImageResource.getImage(ImageResource.IMG_SERVER));
        form.getBody().setLayout(new GridLayout());

        insertSections(form.getBody(), "org.eclipse.wst.server.editor.plugins");

        form.reflow(true);
    }

    public void dispose() {
        super.dispose();

        if (managedForm != null) {
            managedForm.dispose();
            managedForm = null;
        }
    }

    /* (non-Javadoc)
     * Initializes the editor part with a site and input.
     */
    public void init(IEditorSite site, IEditorInput input) {
        super.init(site, input);
    }

    protected void updateDecoration(ControlDecoration decoration, IStatus status) {
        if (status != null) {
            Image newImage = null;
            FieldDecorationRegistry registry = FieldDecorationRegistry.getDefault();
            switch (status.getSeverity()) {
                case IStatus.INFO:
                    newImage = registry.getFieldDecoration(FieldDecorationRegistry.DEC_REQUIRED).getImage();
                    break;
                case IStatus.WARNING:
                    newImage = registry.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
                    break;
                case IStatus.ERROR:
                    newImage = registry.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
            }
            decoration.setDescriptionText(status.getMessage());
            decoration.setImage(newImage);
            decoration.show();
        } else {
            decoration.setDescriptionText(null);
            decoration.hide();
        }
    }

    /*
     * @see IWorkbenchPart#setFocus()
     */
    public void setFocus() {
    }
}
