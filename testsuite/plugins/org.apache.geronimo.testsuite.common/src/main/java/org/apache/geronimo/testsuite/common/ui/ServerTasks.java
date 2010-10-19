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

import java.io.File;

import org.apache.geronimo.testsuite.common.selenium.EclipseSelenium;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

public class ServerTasks {
    Shell workbenchShell;
    AbbotHelper abbotHelper;
    String serverVersion;

    public ServerTasks (Shell shell, AbbotHelper helper, String serverVersion) {
        workbenchShell = shell;
        abbotHelper = helper;
        this.serverVersion = serverVersion;
    }

    // just getting through this with no exceptions is success.
    public boolean createServer(){
        boolean success = true;
        try {
            // Launch the New Project wizard and aHelper.wait for it to open.
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                    "New");
          
            // Having 2 tree items with the same name is not very good design
            abbotHelper.clickTreeItem (wizardShell,
                    new String[] {"Server", "Server"});
         //   abbotHelper.clickTreeItem(wizardShell,"Server");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);

            
            // Select the proper Server. why do we have this in there twice?
            abbotHelper.clickTreeItem(wizardShell,Constants.getConstant(serverVersion, Constants.SERVERNAME));
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);

            // TODO would be nice to install the server instead of have to know where it is in advance
            abbotHelper.setTextField (wizardShell, "", getServerInstallDirectory());

            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean showServerOverview() {
        boolean success = true;
        try {
            String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);
            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Show &View", "Servers"});
            abbotHelper.doubleClickItem(workbenchShell, serverDisplay);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean startServer () {
        return startServer (false);
    }

    public boolean startServer (boolean restart) {
        boolean success = true;
        try {
            String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);
            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Show View", "Servers"});

            if (restart == true) {
                abbotHelper.rightClickItem (workbenchShell, serverDisplay,
                        new String[] {"&Restart"});
            } else {
                abbotHelper.rightClickItem (workbenchShell, serverDisplay,
                        new String[] {"&Start"});
            }
            abbotHelper.waitForServerStatus (workbenchShell, serverDisplay, "Started");

            abbotHelper.waitTime (AbbotHelper.WAIT_STANDARD);

            // if starting the server for the first time, do a quick sanity check
            if (restart == false) {
                EclipseSelenium selenium = new EclipseSelenium();
                selenium.start();

                selenium.open ("http://localhost:8080/console/");
                selenium.waitForPageToLoad ("2000");
                selenium.type ("j_username", "system");
                selenium.type ("j_password", "manager");
                selenium.click ("submit");

                selenium.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean stopServer () {
        boolean success = true;
        try {
            String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);
            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Show &View", "Servers"});

            abbotHelper.rightClickItem (workbenchShell, serverDisplay,
                    new String[] {"S&top"});
            abbotHelper.waitForServerStatus (workbenchShell, serverDisplay, "Stopped");

            abbotHelper.waitTime (AbbotHelper.WAIT_STANDARD);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }


    // remove the server so that the test will be reset back to its original state.
    public boolean removeServer () {
        boolean success = true;
        try {
            Shell preferenceShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "&Preferences"},
                    "Preferences");

            abbotHelper.clickTreeItem (preferenceShell,
                    new String[] {"Server", "Runtime Environments"});
            abbotHelper.clickItem (preferenceShell, Constants.getConstant(serverVersion, Constants.SERVERRUNTIME));
            Shell questionShell = abbotHelper.clickButton (preferenceShell, "&Remove", "Server");

            abbotHelper.clickButton (questionShell, IDialogConstants.OK_LABEL);
            abbotHelper.clickButton (preferenceShell, IDialogConstants.OK_LABEL);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean publishAllProjects () {
        boolean success = true;
        try {
            String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);

            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Show &View", "Servers"});
            Shell deployShell = abbotHelper.rightClickItem (workbenchShell, serverDisplay,
                    new String[] {"Add and Remove &Projects..."}, "Add and Remove Projects");
            abbotHelper.clickButton (deployShell, "Add A&ll >>");

            abbotHelper.clickButton (deployShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal (deployShell);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean removeAllProjects () {
        boolean success = true;
        try {
            String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);

            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Window", "Show &View", "Servers"});
            Shell deployShell = abbotHelper.rightClickItem (workbenchShell, serverDisplay,
                    new String[] {"Add and Remove &Projects..."}, "Add and Remove Projects");
            abbotHelper.clickButton (deployShell, "<< Re&move All");

            abbotHelper.clickButton (deployShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal (deployShell);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    private String getServerInstallDirectory() {
        IWorkspace ws = ResourcesPlugin.getWorkspace();
        String location = ws.getRoot().getLocation().toOSString();
        int index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
        }
        index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
        }
        index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
        }
        location = location + File.separatorChar + Constants.getConstant(serverVersion, Constants.SERVERPATH); ;
        return location;
    }
}
