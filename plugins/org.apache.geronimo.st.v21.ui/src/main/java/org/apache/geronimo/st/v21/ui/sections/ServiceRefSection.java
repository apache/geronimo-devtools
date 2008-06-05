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

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.providers.AdapterFactory;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.wizards.ServiceRefWizard;
import org.apache.geronimo.jee.naming.ServiceRef;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ServiceRefSection extends AbstractTableSection {

    public ServiceRefSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List serviceRefs) {
        super(plan, parent, toolkit, style);
        this.objectContainer = serviceRefs;
        COLUMN_NAMES = new String[] { CommonMessages.editorServiceRefName };
        createClient();
    }

    public String getTitle() {
        return CommonMessages.editorServiceRefTitle;
    }

    public String getDescription() {
        return CommonMessages.editorServiceRefDescription;
    }

    public Wizard getWizard() {
        return new ServiceRefWizard(this);
    }

    public Class getTableEntryObjectType() {
        return ServiceRef.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.ui.sections.AbstractTableSection#getAdapterFactory()
     */
    public AdapterFactory getAdapterFactory() {
        return new AdapterFactory() {
            public Object[] getElements(Object inputElement) {
                if (!JAXBElement.class.isInstance(inputElement)) {
                    return new String[] { "" };
                }
                return getObjectContainer().toArray();
            }

            public String getColumnText(Object element, int columnIndex) {
                if (ServiceRef.class.isInstance(element)) {
                    ServiceRef serviceRef = (ServiceRef)element;
                    switch (columnIndex) {
                    case 0: return serviceRef.getServiceRefName();
                    }
                }
                return null;
            }
        };
    }
}
