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

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.finder.generic.MultipleFoundException;
import abbot.swt.finder.generic.NotFoundException;

public class ServerTasks {
    Shell workbenchShell;
    AbbotHelper aHelper;
    String serverVersion;

    public ServerTasks (Shell shell, AbbotHelper helper, String serverVersion) {
        workbenchShell = shell;
        aHelper = helper;
        this.serverVersion = serverVersion;
    }
    
    // just getting through this with no exceptions is success.
    public void createServer() throws MultipleFoundException, NotFoundException {
        // Launch the New Project wizard and aHelper.wait for it to open.
        Shell wizardShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                "New");

        // Having 2 tree items with the same name is not very good design
        aHelper.clickTreeItem (wizardShell, 
                new String[] {"Server", "Server"});
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);

        // Select the proper Server. why do we have this in there twice?
        aHelper.clickItem (wizardShell, Constants.getConstant(serverVersion, Constants.SERVERNAME));
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);

        //TODO would be nice to install the server instead of have to know where it is in advance
        aHelper.setTextField (wizardShell, "", getServerInstallDirectory());

        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
    }

    public void startServer () throws MultipleFoundException, NotFoundException {
    	String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);
    	aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "Show &View", "Servers"});
        
        aHelper.rightClickItem (workbenchShell, serverDisplay,
                new String[] {"&Start"});
        aHelper.waitForServerStatus (workbenchShell, serverDisplay, "Started");
        
        aHelper.waitTime( 1500 );
    }
    
    public void stopServer () throws MultipleFoundException, NotFoundException {
    	String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);
        aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "Show &View", "Servers"});

        aHelper.rightClickItem (workbenchShell, serverDisplay,
                new String[] {"S&top"});
        aHelper.waitForServerStatus (workbenchShell, serverDisplay, "Stopped");
        
        aHelper.waitTime( 1500 );
    }

    
    // remove the server so that the test will be reset back to its original state.
    public void removeServer () throws MultipleFoundException, NotFoundException {
        Shell preferenceShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "&Preferences"},
                "Preferences");

        aHelper.clickTreeItem (preferenceShell,
                new String[] {"Server", "Runtime Environments"});
        aHelper.clickItem (preferenceShell, Constants.getConstant(serverVersion, Constants.SERVERRUNTIME));
        Shell questionShell = aHelper.clickButton (preferenceShell, "&Remove", "Server");
        
        aHelper.clickButton (questionShell, IDialogConstants.OK_LABEL);
        aHelper.clickButton (preferenceShell, IDialogConstants.OK_LABEL);
    }

    public void publishAllProjects () throws MultipleFoundException, NotFoundException {
        String serverDisplay = Constants.getConstant(serverVersion, Constants.SERVERDISPLAY);
        
        aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Window", "Show &View", "Servers"});
        Shell deployShell = aHelper.rightClickItem (workbenchShell, serverDisplay,
                 new String[] {"Add and Remove &Projects..."}, "Add and Remove Projects");
        aHelper.clickButton (deployShell, "Add A&ll >>");
        
        aHelper.clickButton (deployShell, IDialogConstants.FINISH_LABEL);
        aHelper.waitForDialogDisposal (deployShell);
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
