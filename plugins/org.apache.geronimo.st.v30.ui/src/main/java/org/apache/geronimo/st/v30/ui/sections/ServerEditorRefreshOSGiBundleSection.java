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

import org.apache.geronimo.st.v30.core.GeronimoServerDelegate;
import org.apache.geronimo.st.v30.ui.commands.SetCleanOSGiBundleCacheCommand;
import org.apache.geronimo.st.v30.ui.commands.SetRefreshOSGiBundleCommand;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public class ServerEditorRefreshOSGiBundleSection extends AbstractServerEditorSection {

    Button refreshOSGiBundle;
    
    public ServerEditorRefreshOSGiBundleSection() {
        super();
    }
    
    public void createSection(Composite parent) {
        super.createSection(parent);

        FormToolkit toolkit = getFormToolkit(parent.getDisplay());

        Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE
                | ExpandableComposite.EXPANDED
                | ExpandableComposite.TITLE_BAR
                | Section.DESCRIPTION | ExpandableComposite.FOCUS_TITLE);
        
        section.setText(Messages.editorSectionRefreshOSGiBundleTitle);
        section.setDescription(Messages.editorSectionRefreshOSGiBundleDescription);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        
        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);
        
        refreshOSGiBundle = toolkit.createButton(composite, Messages.refreshOSGiBundle, SWT.CHECK);
        GeronimoServerDelegate gsd = (GeronimoServerDelegate) server.getAdapter(GeronimoServerDelegate.class);
        boolean refreshEnabled = gsd.isRefreshOSGiBundle();
        refreshOSGiBundle.setSelection(refreshEnabled);
        
        refreshOSGiBundle.addSelectionListener( new SelectionListener(){
            public void widgetSelected(SelectionEvent e) {
                execute(new SetRefreshOSGiBundleCommand(server, refreshOSGiBundle.getSelection()));
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }
            
        });
        
    }
}
