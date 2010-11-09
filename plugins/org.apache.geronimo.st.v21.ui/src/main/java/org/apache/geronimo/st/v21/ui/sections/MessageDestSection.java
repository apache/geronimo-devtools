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

import org.apache.geronimo.jee.naming.MessageDestination;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.wizards.MessageDestWizard;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class MessageDestSection extends AbstractTableSection {

    public MessageDestSection(JAXBElement plan, Composite parent, FormToolkit toolkit, int style, List messageDest) {
        super(plan, parent, toolkit, style);
        this.objectContainer = messageDest;
        COLUMN_NAMES = new String[] {
                CommonMessages.messageDestinationName, CommonMessages.adminModule, CommonMessages.adminLink, 
                CommonMessages.groupId, CommonMessages.artifactId, CommonMessages.version,
                CommonMessages.moduleId, CommonMessages.name };
        createClient();
    }

    public String getTitle() {
        return CommonMessages.editorSectionMessageDestTitle;
    }

    public String getDescription() {
        return CommonMessages.editorSectionMessageDestDescription;
    }

    public Wizard getWizard() {
        return new MessageDestWizard(this);
    }

    public Class getTableEntryObjectType() {
        return MessageDestination.class;
    }

    @Override
    public ITableLabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getColumnText(Object element, int columnIndex) {
                if (MessageDestination.class.isInstance(element)) {
                    MessageDestination msgDest = (MessageDestination) element;
                    switch (columnIndex) {
                    case 0:
                        return msgDest.getMessageDestinationName();
                    case 1:
                        return msgDest.getAdminObjectModule();
                    case 2:
                        return msgDest.getAdminObjectLink();
                    case 3:
                        return msgDest.getPattern().getGroupId();
                    case 4:
                        return msgDest.getPattern().getArtifactId();
                    case 5:
                        return msgDest.getPattern().getVersion();
                    case 6:
                        return msgDest.getPattern().getModule();
                    case 7:
                        return msgDest.getPattern().getName();
                    }
                }
                return null;
            }
        };
    }
}
