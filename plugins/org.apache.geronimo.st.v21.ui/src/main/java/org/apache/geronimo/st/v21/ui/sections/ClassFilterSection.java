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

import org.apache.geronimo.jee.deployment.ClassFilter;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.jee.jaxbmodel.operations.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.ClassFilterWizard;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class ClassFilterSection extends AbstractTableSection {

    protected Environment environment;

    protected boolean isServerEnvironment;

    // isHiddenClasses = true for hidden classes or false for non overridable classes
    protected boolean isHiddenClasses;

    /**
     * @param plan
     * @param parent
     * @param toolkit
     * @param style
     * @param envType
     * @param classFilterType
     */
    public ClassFilterSection(JAXBElement plan, Environment environment, Composite parent, FormToolkit toolkit, int style, boolean isServerEnvironment, boolean isHiddenClasses) {
        super(plan, parent, toolkit, style);
        this.environment = environment;
        this.isServerEnvironment = isServerEnvironment;
        this.isHiddenClasses = isHiddenClasses; 
        this.COLUMN_NAMES = new String[] {
                CommonMessages.className
        };
        createClient();
    }

    public String getTitle() {
        if (isServerEnvironment && isHiddenClasses)
            return CommonMessages.editorSectionHiddenClassesTitle;
        else if (!isServerEnvironment && isHiddenClasses)
            return CommonMessages.editorSectionClientHiddenClassesTitle;
        else if (isServerEnvironment && !isHiddenClasses)
            return CommonMessages.editorSectionNonOverridableTitle;
        else
            return CommonMessages.editorSectionClientNonOverridableTitle;
    }

    public String getDescription() {
        if (isServerEnvironment && isHiddenClasses)
            return CommonMessages.editorSectionHiddenClassesDescription;
        else if (!isServerEnvironment && isHiddenClasses)
            return CommonMessages.editorSectionClientHiddenClassesDescription;
        else if (isServerEnvironment && !isHiddenClasses)
            return CommonMessages.editorSectionNonOverridableDescription;
        else
            return CommonMessages.editorSectionClientNonOverridableDescription;
    }

    public List getObjectContainer() {
        if (environment == null) {
            environment = (Environment)JAXBObjectFactoryImpl.getInstance().create(Environment.class);
        }

        if (getClassFilter() == null) {
            ClassFilter filter = (ClassFilter)JAXBObjectFactoryImpl.getInstance().create(ClassFilter.class);
            setClassFilter (filter);
        }
        return getClassFilter().getFilter();
    }

    public Wizard getWizard() {
        return new ClassFilterWizard(this, isServerEnvironment);
    }

    public ClassFilter getClassFilter() {
        if (isHiddenClasses == true)
            return environment.getHiddenClasses();
        else
            return environment.getNonOverridableClasses();
    }

    public void setClassFilter (ClassFilter filter) {
        if (isHiddenClasses == true)
            environment.setHiddenClasses (filter);
        else
            environment.setNonOverridableClasses (filter);
    }
    
    public ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.eclipse.jdt.ui", "icons/full/obj16/jar_obj.gif");
    }

    public Class getTableEntryObjectType() {
        return String.class;
    }

    public Object getInput() {
        if (environment != null) {
            return getClassFilter();
        }
        return super.getInput();
    }
    
    @Override
    public IContentProvider getContentProvider() {
        return new ContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                if (!ClassFilter.class.isInstance(inputElement)) {
                    return new String[] { "" };
                }
                return ((ClassFilter) inputElement).getFilter().toArray();
            }
        };
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (String.class.isInstance(element)) {
                    switch (columnIndex) {
                    case 0:
                        return (String) element;
                    }
                }
                return null;
            }
        };
    }
}
