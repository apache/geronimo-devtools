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

import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.DependencyWizard;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class DependencySection extends AbstractTableSection {

    boolean isServerEnvironment;
    protected Environment environment;
    private String runtimeVersionNumber;

    public DependencySection(JAXBElement plan, Environment environment, Composite parent, FormToolkit toolkit, int style,String runtimeVersion) {
        this(plan, environment, parent, toolkit, style, true, runtimeVersion);
    }

    public DependencySection(JAXBElement plan, Environment environment, Composite parent, FormToolkit toolkit, int style, boolean isServerEnvironment,String runtimeVersion) {
        super(plan, parent, toolkit, style);
        this.environment = environment;
        this.isServerEnvironment = isServerEnvironment; 
        this.COLUMN_NAMES = new String[] {
                CommonMessages.groupId, CommonMessages.artifactId, CommonMessages.version, CommonMessages.artifactType
        };
        
        runtimeVersionNumber = runtimeVersion;
        createClient();
        getSection().setExpanded(false);
    }

    public String getTitle() {
        if (isServerEnvironment) {
            return CommonMessages.editorSectionDependenciesTitle;
        } else {
            return CommonMessages.editorSectionClientDependenciesTitle;
        }
    }

    public String getDescription() {
        if (isServerEnvironment) {
            return CommonMessages.editorSectionDependenciesDescription;
        } else {
            return CommonMessages.editorSectionClientDependenciesDescription;
        }
    }

    public List getObjectContainer() {
        return getDependencies().getDependency();
    }

    private Dependencies getDependencies() {
        if (environment == null) {
            environment = new Environment();
        }
        if (environment.getDependencies() == null) {
            environment.setDependencies(new Dependencies());
        }
        return environment.getDependencies();
    }

    @Override
    public Wizard getWizard() {
        return new DependencyWizard(this);
    }

    public ImageDescriptor getImageDescriptor() {
        return Activator.imageDescriptorFromPlugin("org.eclipse.jdt.ui", "icons/full/obj16/jar_obj.gif");
    }

    public Class getTableEntryObjectType() {
        return Dependency.class;
    }

    public Object getInput() {
        return getDependencies();
    }

    @Override
    public IContentProvider getContentProvider() {
        return new ContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                if (!Dependencies.class.isInstance(inputElement)) {
                    return new String[] { "" };
                }
                return ((Dependencies) inputElement).getDependency().toArray();
            }
        };
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (Dependency.class.isInstance(element)) {
                    Dependency dependency = (Dependency) element;
                    switch (columnIndex) {
                    case 0:
                        return dependency.getGroupId();
                    case 1:
                        return dependency.getArtifactId();
                    case 2:
                        return dependency.getVersion();
                    case 3:
                        return dependency.getType();
                    }
                }
                return "";
            }
        };
    }
    
    public String getRuntimeVersion(){
        return runtimeVersionNumber;
    }
}
