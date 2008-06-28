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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.deployment.Gbean;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.providers.AdapterFactory;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.v21.ui.wizards.GBeanWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class GBeanSection extends AbstractTableSection {

    /**
     * @param plan
     * @param parent
     * @param toolkit
     * @param style
     */
    public GBeanSection(JAXBElement plan, List gbeans, Composite parent, FormToolkit toolkit, int style) {
        super(plan, parent, toolkit, style);
        this.objectContainer = gbeans;
        this.COLUMN_NAMES = new String[] {
                CommonMessages.name, CommonMessages.className };
        createClient();
    }

    public String getTitle() {
        return CommonMessages.editorSectionGBeanTitle;
    }

    public String getDescription() {
        return CommonMessages.editorSectionGBeanDescription;
    }

    public List getObjectContainer() {
        //NOTE this is a list of JAXBElements of GBeans, not just GBeans
        if (objectContainer == null) {
            objectContainer = (List)JAXBObjectFactoryImpl.getInstance().create(List.class);
        }
        return objectContainer;
    }

    public Wizard getWizard() {
        return new GBeanWizard(this);
    }

    public Class getTableEntryObjectType() {
        return Gbean.class;
    }

    public Object getInput() {
        if (objectContainer != null) {
            return objectContainer;
        }
        return super.getInput();
    }

    // overwrite this method.
    // The item is a GBean and the list is a list of JAXBElements of GBean 
    public void removeItem (Object anItem) {
        JAXBElement element;
        Gbean gbean, removeItem;
        removeItem = (Gbean)anItem;
        for (int i = 0; i < objectContainer.size(); i++)
        {
            element = (JAXBElement)objectContainer.get(i);
            gbean = (Gbean)element.getValue();
            if (removeItem.getName().equals(gbean.getName()) && 
                removeItem.getClazz().equals(gbean.getClazz()))
            {
                getObjectContainer().remove(i);
                return;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.st.ui.sections.AbstractTableSection#getAdapterFactory()
     */
    public AdapterFactory getAdapterFactory() {
        return new AdapterFactory() {
            public Object[] getElements(Object inputElement) {
                if (!List.class.isInstance(inputElement)) {
                    return new String[] { "" };
                }
                // convert the list of JAXBElements into regular GBeans
                List elementList = (List)inputElement;
                ArrayList gbeanList = new ArrayList();
                JAXBElement element;
                Gbean gbean;
                for (int i = 0; i < elementList.size(); i++) {
                    element = (JAXBElement)elementList.get(i);
                    gbean = (Gbean)element.getValue();
                    gbeanList.add(gbean);
                }
                return gbeanList.toArray();
            }

            public String getColumnText(Object element, int columnIndex) {
                if (Gbean.class.isInstance(element)) {
                    Gbean gbean = (Gbean)element;
                    switch (columnIndex) {
                    case 0: return gbean.getName();
                    case 1: return gbean.getClazz();
                    }
                }
                return null;
            }
        };
    }
}
