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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractSectionPart extends SectionPart {

    protected FormToolkit toolkit;

    private JAXBElement root;

    private AbstractDeploymentDescriptor descriptor;

    public AbstractSectionPart(Section section) {
        super(section);
    }

    public AbstractSectionPart(Composite parent, FormToolkit toolkit, int style, JAXBElement rootElement) {
        super(parent, toolkit, style);
        this.toolkit = toolkit;
        this.root = rootElement;
    }

    public AbstractSectionPart(Composite parent, FormToolkit toolkit, int style, JAXBElement rootElement,
            AbstractDeploymentDescriptor descriptor) {
        super(parent, toolkit, style);
        this.toolkit = toolkit;
        this.root = rootElement;
        this.descriptor = descriptor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
     * 
     * Overriding this method as a workaround as switching tabs on a dirty
     * editor commits the page and marks the part as not dirty.
     */
    public void commit(boolean onSave) {
        boolean currentDirtyState = isDirty();
        super.commit(onSave);
        if (!onSave && currentDirtyState) {
            markDirty();
        }
    }

    public FormToolkit getToolkit() {
        return toolkit;
    }

    public JAXBElement getRootElement() {
        return root;
    }

    public AbstractDeploymentDescriptor getDescriptor() {
        return descriptor;
    }

    protected Label createLabel(Composite parent, String text) {
        Label label = toolkit.createLabel(parent, text);
        label.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        return label;
    }

    protected GridData createTextFieldGridData() {
        GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
        data.widthHint = 150;
        return data;
    }

}
