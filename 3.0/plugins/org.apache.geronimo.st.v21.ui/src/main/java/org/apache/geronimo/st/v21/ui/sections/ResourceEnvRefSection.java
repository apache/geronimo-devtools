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
package org.apache.geronimo.st.v21.ui.sections;

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.naming.ResourceEnvRef;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.ResourceEnvRefWizard;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class ResourceEnvRefSection extends AbstractTableSection {


    public ResourceEnvRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List resourceEnvRefs) {
        super(plan, parent, toolkit, style);
        this.objectContainer = resourceEnvRefs;
        COLUMN_NAMES = new String[] {
                CommonMessages.editorResEnvRefNameTitle, CommonMessages.editorResEnvRefMsgDestTitle };
        createClient();
    }

    public String getTitle() {
        return CommonMessages.editorResourceEnvRefTitle;
    }

    public String getDescription() {
        return CommonMessages.editorResourceEnvRefDescription;
    }

    public Wizard getWizard() {
        return new ResourceEnvRefWizard(this);
    }

    public ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee", "icons/full/obj16/res_env_ref_obj.gif");
    }

    public Class getTableEntryObjectType() {
        return ResourceEnvRef.class;
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (ResourceEnvRef.class.isInstance(element)) {
                    ResourceEnvRef resourceEnvRef = (ResourceEnvRef) element;
                    switch (columnIndex) {
                    case 0:
                        return resourceEnvRef.getRefName();
                    case 1:
                        return resourceEnvRef.getMessageDestinationLink();
                    }
                }
                return null;
            }
        };
    }
}
