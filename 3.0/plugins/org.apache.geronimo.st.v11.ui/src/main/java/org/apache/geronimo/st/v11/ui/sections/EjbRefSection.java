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
package org.apache.geronimo.st.v11.ui.sections;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.naming.EjbRefType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.EjbRefWizard;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class EjbRefSection extends AbstractTableSection {

    /**
     * @param plan
     * @param parent
     * @param toolkit
     * @param style
     */
    public EjbRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List ejbRefs) {
        super(plan, parent, toolkit, style);
        this.objectContainer = ejbRefs;
        COLUMN_NAMES = new String[] {
                CommonMessages.name, CommonMessages.editorEjbRefEjbLink };
        createClient();
        getSection().setExpanded(false);
    }

    public String getTitle() {
        return CommonMessages.editorEjbRefTitle;
    }

    public String getDescription() {
        return CommonMessages.editorEjbRefDescription;
    }

    public Wizard getWizard() {
        return new EjbRefWizard(this);
    }

    public ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee", "icons/full/obj16/ejbRef_obj.gif");
    }

    public Class getTableEntryObjectType() {
        return EjbRefType.class;
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (EjbRefType.class.isInstance(element)) {
                	EjbRefType ejbRef = (EjbRefType) element;
                    switch (columnIndex) {
                    case 0:
                        return ejbRef.getRefName();
                    case 1:
                        return ejbRef.getEjbLink();
                    }
                }
                return null;
            }
        };
    }
}
