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

import org.apache.geronimo.j2ee.application.ModuleType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v11.ui.wizards.ModuleWizard;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/*
 * @version $Rev$ $Date$
 */
public class ModuleSection extends AbstractTableSection {

    public ModuleSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List modules) {
        super(plan, parent, toolkit, style);
        this.objectContainer = modules;
        COLUMN_NAMES = new String[] {
                CommonMessages.moduleType, CommonMessages.path, CommonMessages.altDD };
        createClient();
    }

    public String getTitle() {
        return CommonMessages.editorSectionModuleTitle;
    }

    public String getDescription() {
        return CommonMessages.editorSectionModuleDescription;
    }

    public Wizard getWizard() {
        return new ModuleWizard(this);
    }

    public Class getTableEntryObjectType() {
        return ModuleType.class;
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (ModuleType.class.isInstance(element)) {
                	ModuleType module = (ModuleType) element;
                    switch (columnIndex) {
                    case 0:
                        if (module.getConnector() != null) {
                            return Messages.connector;
                        } else if (module.getEjb() != null) {
                            return Messages.ejb;
                        } else if (module.getJava() != null) {
                            return Messages.java;
                        } else if (module.getWeb() != null) {
                            return Messages.web;
                        }
                        return "";
                    case 1:
                        if (module.getConnector() != null) {
                            return module.getConnector().getValue();
                        } else if (module.getEjb() != null) {
                            return module.getEjb().getValue();
                        } else if (module.getJava() != null) {
                            return module.getJava().getValue();
                        } else if (module.getWeb() != null) {
                            return module.getWeb().getValue();
                        }
                        return "";
                    case 2:
                        if (module.getAltDd() != null) {
                            return module.getAltDd().getValue();
                        }
                        return "";
                    }
                }
                return null;
            }
        };
    }
}
