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

import java.util.List;

import org.apache.geronimo.st.v30.core.ClasspathContainersHelper;
import org.apache.geronimo.st.v30.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.v30.ui.commands.SetClasspathContainersCommand;
import org.apache.geronimo.st.v30.ui.commands.SetInPlaceSharedLibCommand;
import org.apache.geronimo.st.v30.ui.commands.SetNotRedeployJSPFilesCommand;
import org.apache.geronimo.st.v30.ui.commands.SetRunFromWorkspaceCommand;
import org.apache.geronimo.st.v30.ui.commands.SetSelectClasspathContainersCommand;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.apache.geronimo.st.v30.ui.internal.Trace;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class ServerEditorTestEnvSection extends AbstractServerEditorSection {

    // SWT widget(s)
    private Button runFromWorkspace;
    private Button noRedeployJSPFiles;
    private Button inPlaceSharedLib;
    private Button selectClasspathContainers = null;
    private Composite composite = null;

    // Form widget(s)
    private FormToolkit toolkit;

    // JFace viewer(s)
    private CheckboxTableViewer checkbox;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.ui.editor.ServerEditorSection#createSection(org.eclipse.swt.widgets.Composite)
     */
    public void createSection(Composite parent) {
        super.createSection(parent);

        Trace.tracePoint("ENTRY", "ServerEditorTestEnvSection.createSection", parent);

        toolkit = getFormToolkit(parent.getDisplay());

        Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE
                                                | ExpandableComposite.EXPANDED
                                                | ExpandableComposite.TITLE_BAR
                                                | Section.DESCRIPTION | ExpandableComposite.FOCUS_TITLE);

        section.setText(Messages.editorSectionTestEnvTitle);
        section.setDescription(Messages.editorSectionTestEnvDescription);
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);

        //
        // inPlaceSharedLib Button
        //
        inPlaceSharedLib = toolkit.createButton(composite, Messages.editorSectionSharedLibrariesInPlace, SWT.CHECK);
        inPlaceSharedLib.setSelection(gs.isInPlaceSharedLib());
        inPlaceSharedLib.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                execute(new SetInPlaceSharedLibCommand(server, inPlaceSharedLib.getSelection()));
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }

        });
        
        //
        // Don't redeploy JSP files Button
        //
        noRedeployJSPFiles = toolkit.createButton(composite, Messages.editorSectionNotRedeployJSPFiles, SWT.CHECK);
        noRedeployJSPFiles.setSelection(gs.isNotRedeployJSPFiles());
        
        noRedeployJSPFiles.setEnabled(!(server.getServerType().supportsRemoteHosts()
                && !SocketUtil.isLocalhost(server.getHost()))&&gs.getServer().getServerState()==IServer.STATE_STOPPED); 
        noRedeployJSPFiles.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                execute(new SetNotRedeployJSPFilesCommand(server, noRedeployJSPFiles.getSelection()));
                
                if (noRedeployJSPFiles.getSelection()) {
                    MessageDialog.openInformation(Display.getCurrent().getActiveShell(), 
                        Messages.notRedeployJSPFilesReminder, Messages.notRedeployJSPFilesInformation);
                }
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }

        });

        //
        // runFromWorkspace Button
        //
        runFromWorkspace = toolkit.createButton(composite, Messages.editorSectionRunFromWorkspace, SWT.CHECK);
        runFromWorkspace.setSelection(gs.isRunFromWorkspace());
        runFromWorkspace.setEnabled(false); //FIXME disable support until supported
        runFromWorkspace.addSelectionListener(new SelectionListener() {

            public void widgetSelected(SelectionEvent e) {
                execute(new SetRunFromWorkspaceCommand(server, runFromWorkspace.getSelection()));
            }

            public void widgetDefaultSelected(SelectionEvent e) {
            }

        });

        //
        // selectClasspathContainers Button
        //
        selectClasspathContainers = toolkit.createButton(composite, Messages.editorSectionSelectClasspathContainers, SWT.CHECK); 
        selectClasspathContainers.setSelection(gs.isSelectClasspathContainers()); 
        selectClasspathContainers.addSelectionListener(new SelectionListener() { 

            public void widgetSelected(SelectionEvent e) { 
                execute(new SetSelectClasspathContainersCommand(server, selectClasspathContainers.getSelection())); 

                createCheckbox();

                //
                // For any selection change the checkbox will be populated from the workspace. 
                // Changes to individual elements in the checkbox will be handled with the 
                // CheckStateListener below.
                //
                List<String> containers = ClasspathContainersHelper.queryWorkspace();
                checkbox.setInput( containers );
                checkbox.setAllChecked( false );
                if ( selectClasspathContainers.getSelection() ) {
                    checkbox.getTable().setEnabled( true );
                }
                else {
                    checkbox.getTable().setEnabled( false );
                    // Clear any previously selected classpath containers
                    execute(new SetClasspathContainersCommand(server, new Object[] {} ));
                }
            } 

            public void widgetDefaultSelected(SelectionEvent e) { 
            } 
        }); 

        //
        // checkbox CheckboxTableViewer
        //
        createCheckbox();

        //
        // Populate the checkbox from the list of classpath containers in the workspace. If 
        // workspace classpath containers had been previously selected then use the list from the
        // server's instance properties to populate the checkbox. One advantage of this approach
        // is that it will handle cases where new classpath containers are added in the workspace
        // or existing containers are deleted from the workspace. 
        //
        List<String> containers = ClasspathContainersHelper.queryWorkspace();
        checkbox.setInput( containers );

        if ( selectClasspathContainers.getSelection() ) {
            checkbox.getTable().setEnabled( true );
            List<String> checkedContainers = gs.getClasspathContainers();
            for (String container: checkedContainers) {
                checkbox.setChecked( container, true );
            }
        }
        else {
            checkbox.getTable().setEnabled( false );
        }

        Trace.tracePoint("EXIT", "ServerEditorTestEnvSection.createSection");
    }

    //
    // CheckboxTableViewer: checkbox
    //
    public void createCheckbox() {
        Trace.tracePoint("ENTRY", "ServerEditorTestEnvSection.createCheckbox");
    
        if ( checkbox == null ) {
                    
            checkbox = CheckboxTableViewer.newCheckList( composite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
                    
            GridData gridData = new GridData(GridData.FILL_BOTH);
            gridData.heightHint = 250;
            gridData.widthHint = 100;
            checkbox.getTable().setLayoutData( gridData );
                    
            checkbox.addCheckStateListener( new ICheckStateListener() {

                public void checkStateChanged(CheckStateChangedEvent event) {
                    execute(new SetClasspathContainersCommand(server, checkbox.getCheckedElements()));
                }
            }); 

            checkbox.setLabelProvider( new LabelProvider() {
            });

            checkbox.setContentProvider( new ArrayContentProvider() {
            });
        }

        Trace.tracePoint("EXIT", "ServerEditorTestEnvSection.createCheckbox");
    }
}
