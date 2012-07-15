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

import org.apache.geronimo.j2ee.naming.ResourceRefType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v11.ui.Activator;
import org.apache.geronimo.st.v11.ui.wizards.ResourceRefWizard;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class ResourceRefSection extends AbstractTableSection {

    public ResourceRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List resourceRefs) {
        super(plan, parent, toolkit, style);
        this.objectContainer = resourceRefs;
        COLUMN_NAMES = new String[] {
                CommonMessages.editorResRefNameTitle, CommonMessages.editorResRefLinkTitle};
        createClient();
        getSection().setExpanded(false);
    }

    public String getTitle() {
        return CommonMessages.editorResourceRefTitle;
    }

    public String getDescription() {
        return CommonMessages.editorResourceRefDescription;
    }

    public Wizard getWizard() {
        return new ResourceRefWizard(this);
    }

    public ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee", "icons/full/obj16/resourceRef_obj.gif");
    }

    public Class getTableEntryObjectType() {
        return ResourceRefType.class;
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (ResourceRefType.class.isInstance(element)) {
                	ResourceRefType resourceRef = (ResourceRefType) element;
                    switch (columnIndex) {
                    case 0:
                        return resourceRef.getRefName();
                    case 1:
                        return resourceRef.getResourceLink();
                    }
                }
                return null;
            }
        };
    }
}
