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
import org.apache.geronimo.st.v21.core.jaxb.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.DependencyWizard;
import org.apache.geronimo.jee.deployment.Dependencies;
import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.deployment.Environment;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DependencySection extends AbstractTableSection {

    protected Environment environment;

    /**
     * @param plan
     * @param parent
     * @param toolkit
     * @param style
     */
    public DependencySection(JAXBElement plan, Environment environment, Composite parent, FormToolkit toolkit, int style) {
        super(plan, parent, toolkit, style);
        this.environment = environment;
        this.COLUMN_NAMES = new String[] {
                CommonMessages.artifactId, CommonMessages.groupId, CommonMessages.version, CommonMessages.type
        };
        createClient();
    }

    public String getTitle() {
        return CommonMessages.editorSectionDependenciesTitle;
    }

    public String getDescription() {
        return CommonMessages.editorSectionDependenciesDescription;
    }

    public List getObjectContainer() {
        if (environment == null) {
            environment = (Environment)JAXBObjectFactoryImpl.getInstance().create(Environment.class);
        }
        
        if ( environment.getDependencies() == null ) {
            Dependencies dependencies = (Dependencies)JAXBObjectFactoryImpl.getInstance().create(Dependencies.class);
            environment.setDependencies(dependencies);
        }
        return environment.getDependencies().getDependency();
    }

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
        if (environment != null) {
            return environment.getDependencies();
        }
        return super.getInput();
    }
    
    public AdapterFactory getAdapterFactory() {
        return new AdapterFactory() {
            public Object[] getElements(Object inputElement) {
                if (!Dependencies.class.isInstance(inputElement)) {
                    return new String[] { "" };
                }
                Dependencies plan = (Dependencies)inputElement;
                return plan.getDependency().toArray();
            }
            public String getColumnText(Object element, int columnIndex) {
                if (Dependency.class.isInstance(element)) {
                    Dependency dependency = (Dependency)element;
                    switch (columnIndex) {
                    case 0: return dependency.getGroupId();
                    case 1: return dependency.getArtifactId();
                    case 2: return dependency.getVersion();
                    case 3: return dependency.getType();
                    }
                }
                return null;
            }
        };
    }
}
