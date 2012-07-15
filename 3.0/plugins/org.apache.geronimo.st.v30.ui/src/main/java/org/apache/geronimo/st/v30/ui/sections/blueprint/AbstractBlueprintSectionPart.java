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
package org.apache.geronimo.st.v30.ui.sections.blueprint;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.osgi.blueprint.ObjectFactory;
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
public abstract class AbstractBlueprintSectionPart extends SectionPart {

    protected FormToolkit toolkit;

    protected JAXBElement blueprint;
    
    protected ObjectFactory blueprintObjectFactory = null;


    public AbstractBlueprintSectionPart(Section section) {
        super(section);
    }

    public AbstractBlueprintSectionPart(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style);
        this.toolkit = toolkit;
        this.blueprint = plan;
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
        return blueprint;
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
