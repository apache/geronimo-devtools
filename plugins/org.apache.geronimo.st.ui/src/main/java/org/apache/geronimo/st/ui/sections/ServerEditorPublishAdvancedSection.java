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
package org.apache.geronimo.st.ui.sections;

import org.apache.geronimo.st.core.GeronimoServerDelegate;
import org.apache.geronimo.st.ui.commands.SetPublishTimeoutCommand;
import org.apache.geronimo.st.ui.internal.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public class ServerEditorPublishAdvancedSection extends AbstractServerEditorSection {

    protected Spinner publishTimeout;

    public ServerEditorPublishAdvancedSection() {
        super();
    }

    public void createSection(Composite parent) {
        super.createSection(parent);

        FormToolkit toolkit = getFormToolkit(parent.getDisplay());

        Section section = toolkit.createSection(parent,
                ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
                        | ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
                        | ExpandableComposite.FOCUS_TITLE);

        section.setText(Messages.editorSectionPublishAdvancedTitle);
        section.setDescription(Messages.editorSectionPublishAdvancedDescription);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);

        // ------- Label and text field for the http port -------
        createLabel(composite, Messages.publishingTimeout, toolkit);

        publishTimeout = new Spinner(composite, SWT.BORDER);
        publishTimeout.setMinimum(0);
        publishTimeout.setIncrement(5);
        publishTimeout.setMaximum(900000);
        publishTimeout.setSelection(getPublishTimeout());

        publishTimeout.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                execute(new SetPublishTimeoutCommand(server, publishTimeout.getSelection()));
            }
        });
    }

    private int getPublishTimeout() {
        if (gs != null) {
            return (int)gs.getPublishTimeout();
        }
        return 0;
    }
}
