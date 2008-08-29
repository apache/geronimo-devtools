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

import org.apache.geronimo.jee.application.ExtModule;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.wizards.ExtModuleWizard;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/*
 * @version $Rev$ $Date$
 */
public class ExtModuleSection extends AbstractTableSection {

    public ExtModuleSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List extModules) {
        super(plan, parent, toolkit, style);
        this.objectContainer = extModules;
        COLUMN_NAMES = new String[] {
                CommonMessages.moduleType, CommonMessages.path, CommonMessages.internalPath,
                CommonMessages.groupId, CommonMessages.artifactId, CommonMessages.version, CommonMessages.artifactType };
        createClient();
    }

    public String getTitle() {
        return CommonMessages.editorSectionExtModuleTitle;
    }

    public String getDescription() {
        return CommonMessages.editorSectionExtModuleDescription;
    }

    public Wizard getWizard() {
        return new ExtModuleWizard(this);
    }

    public Class getTableEntryObjectType() {
        return ExtModule.class;
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (ExtModule.class.isInstance(element)) {
                    ExtModule extModule = (ExtModule) element;
                    switch (columnIndex) {
                    case 0:
                        if (extModule.getConnector() != null) {
                            return "connector";
                        } else if (extModule.getEjb() != null) {
                            return "ejb";
                        } else if (extModule.getJava() != null) {
                            return "java";
                        } else if (extModule.getWeb() != null) {
                            return "web";
                        }
                        return "";
                    case 1:
                        if (extModule.getConnector() != null) {
                            return extModule.getConnector().getValue();
                        } else if (extModule.getEjb() != null) {
                            return extModule.getEjb().getValue();
                        } else if (extModule.getJava() != null) {
                            return extModule.getJava().getValue();
                        } else if (extModule.getWeb() != null) {
                            return extModule.getWeb().getValue();
                        }
                        return "";
                    case 2:
                        if (extModule.getInternalPath() != null) {
                            return extModule.getInternalPath();
                        }
                        return "";
                    case 3:
                        if (extModule.getExternalPath() != null) {
                            return extModule.getExternalPath().getGroupId();
                        }
                        return "";
                    case 4:
                        if (extModule.getExternalPath() != null) {
                            return extModule.getExternalPath().getArtifactId();
                        }
                        return "";
                    case 5:
                        if (extModule.getExternalPath() != null) {
                            return extModule.getExternalPath().getVersion();
                        }
                        return "";
                    case 6:
                        if (extModule.getExternalPath() != null) {
                            return extModule.getExternalPath().getType();
                        }
                        return "";
                    }
                }
                return null;
            }
        };
    }
}
