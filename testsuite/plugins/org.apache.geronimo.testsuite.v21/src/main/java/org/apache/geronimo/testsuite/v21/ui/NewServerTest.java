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

package org.apache.geronimo.testsuite.v21.ui;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.Preferences;
import abbot.swt.eclipse.utils.PreferenceUtils;
import abbot.swt.eclipse.utils.Preferences.Mode;
import abbot.swt.eclipse.utils.WorkbenchUtilities;
import abbot.swt.finder.generic.MultipleFoundException;
import abbot.swt.finder.generic.NotFoundException;

/**
 * @version $Rev$ $Date$
 */
public class NewServerTest extends WorkbenchTestCase {
    Shell workbenchShell;
    AbbotHelper aHelper;
    
    protected void setUp() throws Exception {
        super.setUp();
        Preferences.setPerspectiveSwitchPrompt(Mode.Always);
        PreferenceUtils.setWelcomeOnStartup(false, PlatformUI.getPreferenceStore()); 
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    // We only want to install/uninstall the server a single time so 
    // we will roll all testcases into one large one that is controlled
    // in the testAllGeronimoGUI method.
    public void testAllGeronimoGUI() {
        boolean success = false;
        try {
            workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
            aHelper = new AbbotHelper (workbenchShell);
            
            // so we are sure that we are looking in the desired perspective
            showJEEPerspective();

            // create server from an installed instance
            server21Create();

            serverTesting();

            // remove the server 
            server21Remove();

            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue (success);
    }

    private void showJEEPerspective() throws MultipleFoundException, NotFoundException {

    	aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "&Close Perspective"});
    	
    	Shell perspectiveShell = aHelper.clickMenuItem (workbenchShell,
              new String[] {"&Window", "&Open Perspective", "&Other..." },
              "Open Perspective");
      aHelper.clickItem (perspectiveShell, "Java EE (default)");
      aHelper.clickButton (perspectiveShell, IDialogConstants.OK_LABEL);        
    }
    
    // just getting through this with no exceptions is success.
    private void server21Create() throws MultipleFoundException, NotFoundException {
        // Launch the New Project wizard and aHelper.wait for it to open.
        Shell wizardShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                "New");

        // Having 2 tree items with the same name is not very good design
        aHelper.clickTreeItem (wizardShell, 
                new String[] {"Server", "Server"});
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);

        // Select the proper Server. why do we have this in there twice?
        aHelper.clickItem (wizardShell, "Apache Geronimo v2.1 Server");
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);

        //TODO would be nice to install the server instead of have to know where it is in advance
        aHelper.setTextField (wizardShell, "", getServerInstallDirectory());

        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
    }

    // TODO only want to add the SampleEAR project and then test that
    // that will include 3 different types of projects
    private void serverTesting () throws MultipleFoundException, NotFoundException {
        
    	String serverDisplay = "Apache Geronimo v2.1 Server at localhost";
    	aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "Show &View", "Servers"});
        
        aHelper.rightClickItem (workbenchShell, serverDisplay,
                new String[] {"&Start"});
        aHelper.waitForServerStatus (workbenchShell, serverDisplay, "Started");
        
        aHelper.waitTime( 1000 );
        
        aHelper.rightClickItem (workbenchShell, serverDisplay,
                new String[] {"S&top"});
        aHelper.waitForServerStatus (workbenchShell, serverDisplay, "Stopped");
        
        aHelper.waitTime( 1000 );
        
    }
    
    // remove the server so that the test will be reset back to its original state.
    private void server21Remove () throws MultipleFoundException, NotFoundException {
        Shell preferenceShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "&Preferences"},
                "Preferences");

        aHelper.clickTreeItem (preferenceShell,
                new String[] {"Server", "Runtime Environments"});
        aHelper.clickItem (preferenceShell, "Apache Geronimo v2.1");
        Shell questionShell = aHelper.clickButton (preferenceShell, "&Remove", "Server");
        
        aHelper.clickButton (questionShell, IDialogConstants.OK_LABEL);
        aHelper.clickButton (preferenceShell, IDialogConstants.OK_LABEL);
    }

    private String getServerInstallDirectory() {
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        String location = ws.getRoot().getLocation().toOSString();
        int index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
            System.out.println(location);           
        }
        index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
            System.out.println(location);
        }
        index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
            System.out.println(location);
        }
        location = location + File.separatorChar + "server" ;
        return location;
    }  
}
