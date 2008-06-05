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

import org.apache.geronimo.jee.security.Role;
import org.apache.geronimo.jee.security.RoleMappings;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.providers.AdapterFactory;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.SecurityRoleWizard;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SecuritySection extends AbstractTableSection {

    public RoleMappings roleMappings;

    /**
     * @param plan
     * @param parent
     * @param toolkit
     * @param style
     */
    public SecuritySection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, RoleMappings roleMappings) {
        super(plan, parent, toolkit, style);
        this.roleMappings = roleMappings;
        this.COLUMN_NAMES = new String[] { CommonMessages.name, CommonMessages.description };
        createClient();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
     */
    public String getTitle() {
        return CommonMessages.editorSectionSecurityRolesTitle;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
     */
    public String getDescription() {
        return CommonMessages.editorSectionSecurityRolesDescription;
    }

    public List getObjectContainer() {
        return roleMappings.getRole();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.AbstractTableSection#getWizard()
     */
    public Wizard getWizard() {
        return new SecurityRoleWizard(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableEntryObjectType()
     */
    public Class getTableEntryObjectType() {
        return Role.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.sections.AbstractTableSection#getInput()
     */
    public Object getInput() {
        if (roleMappings != null) {
            return roleMappings;
        }
        return super.getInput();
    }

    public ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee", "icons/full/obj16/security_role.gif");
    }

    public AdapterFactory getAdapterFactory() {
        return new AdapterFactory() {
            public Object[] getElements(Object inputElement) {
                if (!RoleMappings.class.isInstance(inputElement)) {
                    return new String[] { "" };
                }
                RoleMappings plan = (RoleMappings)inputElement;
                return plan.getRole().toArray();
            }
            public String getColumnText(Object element, int columnIndex) {
                if (Role.class.isInstance(element)) {
                    Role role = (Role)element;
                    switch (columnIndex) {
                    case 0: return role.getRoleName();
                    case 1: return role.getDescription().get(0).getValue();
                    }
                }
                return null;
            }
        };
    }
}
