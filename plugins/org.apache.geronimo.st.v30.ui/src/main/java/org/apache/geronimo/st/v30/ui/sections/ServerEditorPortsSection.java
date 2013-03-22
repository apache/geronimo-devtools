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
package org.apache.geronimo.st.v30.ui.sections;

import org.apache.geronimo.st.v30.ui.commands.TextSetPropertyCommand;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wst.server.core.IServerWorkingCopy;

/**
 * @version $Rev$ $Date$
 */
public class ServerEditorPortsSection extends AbstractServerEditorSection {
    
    public ServerEditorPortsSection() {
        super();
    }

    public void createSection(Composite parent) {
        super.createSection(parent);

        FormToolkit toolkit = getFormToolkit(parent.getDisplay());

        Section section = toolkit.createSection(parent,
                ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
                        | ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
                        | ExpandableComposite.FOCUS_TITLE);

        section.setText(Messages.editorSectionPortsTitle);
        section.setDescription(Messages.editorSectionPortsDescription);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Composite composite = toolkit.createComposite(section);
        
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);
        
        PortEditor editor = new ServerEditorPortEditor(toolkit, server);
        editor.init(composite);
    }

    private class ServerEditorPortEditor extends PortEditor {

        FormToolkit toolkit;
        
        public ServerEditorPortEditor(FormToolkit toolkit, IServerWorkingCopy server) {
            super(server);
            this.toolkit = toolkit;
        }
        
        @Override
        protected Text createText(Composite parent, String value, int style) {
            return toolkit.createText(parent, value, style);
        }
        
        @Override
        protected Label createLabel(Composite parent, String value) {
            return ServerEditorPortsSection.this.createLabel(parent, value, toolkit);
        }
        
        @Override
        protected void setPortOffset(Text portOffset) {
            int value = Integer.parseInt(portOffset.getText());
            execute(new TextSetPropertyCommand(server, "PortOffset", int.class, value, portOffset));
        }

        @Override
        protected void setRmiPort(Text rmiPort) {
            String value = rmiPort.getText();
            execute(new TextSetPropertyCommand(server, "RMINamingPort", String.class, value, rmiPort));
        }

        @Override
        protected void setHttpPort(Text httpPort) {
            String value = httpPort.getText();
            execute(new TextSetPropertyCommand(server, "HTTPPort", String.class, value, httpPort));
        }
    }

}
