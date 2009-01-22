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

package org.apache.geronimo.testsuite.common.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

public class WorkbenchTasks {
    Shell workbenchShell;
    AbbotHelper abbotHelper;

    public WorkbenchTasks (Shell shell, AbbotHelper helper) {
        workbenchShell = shell;
        abbotHelper = helper;
    }
    
    public boolean showJEEPerspective() {
        boolean success = true;
        try {
            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "&Close Perspective"});
        
            Shell perspectiveShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "&Open Perspective", "&Other..." },
                    "Open Perspective");
            abbotHelper.clickItem (perspectiveShell, "Java EE (default)");
            abbotHelper.clickButton (perspectiveShell, IDialogConstants.OK_LABEL);    
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
    
    public boolean checkValidInstallation (String version) {
        boolean success = true;
        try {
            // About Eclipse Test Case. There are three Shell newShell, nextShell and nextNextShell 
            //used because we need to save the states for newShell and nextShell
            //newShell->nextShell->nextNextShell this is the order in which shell comes up
            Shell newShell = abbotHelper.clickMenuItem(workbenchShell, new String[]{"&Help","&About Eclipse Platform"},"About Eclipse Platform");
            Shell nextShell = abbotHelper.clickImageButton(newShell, "Apache.org - Geronimo " + version + " Server Tools Core Plug-in","About Eclipse Platform Features");
            Shell nextNextShell = abbotHelper.clickButton(nextShell, "&Plug-in Details", "Feature Plug-ins");
            abbotHelper.clickButton(nextNextShell, IDialogConstants.OK_LABEL);
            abbotHelper.clickButton(nextShell, IDialogConstants.OK_LABEL);
            abbotHelper.clickButton(newShell, IDialogConstants.OK_LABEL);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
    
    public boolean openInternalBrowser () {
        boolean success = true;
        try {
            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Web Browser", "&0 Internal Web Browser"});
            Shell openShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Show &View", "&Other..."}, "Show View");
            abbotHelper.clickTreeItem (openShell,
                    new String[] {"General", "Internal Web Browser"});
            abbotHelper.clickButton (openShell, IDialogConstants.OK_LABEL);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
