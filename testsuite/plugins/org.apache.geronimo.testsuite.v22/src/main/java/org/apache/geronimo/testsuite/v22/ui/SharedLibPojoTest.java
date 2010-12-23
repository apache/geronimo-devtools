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

package org.apache.geronimo.testsuite.v22.ui;

import java.io.FileInputStream;

import org.apache.geronimo.testsuite.common.selenium.EclipseSelenium;
import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.eclipse.utils.WorkbenchUtilities;

/*
 * @version $Rev$ $Date$
 */
public class SharedLibPojoTest extends AbstractTestCase {
    
    @Override
    public boolean buildTestCase() {
        boolean success = createPojoProject();
        if (success == true) {
            success = copyCodeToPojoProject();
        }
        if (success == true) {
            success = createHelloWorldProject();
        }
        if (success == true) {
            success = copyCodeToHelloWorldProject();
        }
        if (success == true) {
            success = modifyHelloWorldBuildPath();
        }
        return success;
    }

    @Override
    public boolean runTestCase() {
        boolean success = deployHelloWorldProject();
        if (success == true) {
            success = toggleSharedLibSupport();
        }
        if (success == true) {
            success = displayApplication();
        }
        return success;
    }

    @Override
    public boolean cleanupTestCase() {
        boolean success = serverTasks.removeAllProjects();
        if (success == true) {
            success = projectTasks.deleteProject ("HelloWorld");
        }
        if (success == true) {
            success = projectTasks.deleteProject ("CurrencyConverterPojo");
        }
        if (success == true) {
            success = toggleSharedLibSupport();
        }

        return success;
    }

    public boolean createPojoProject() {
        boolean success = true;
        try {
            workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
            abbotHelper = new AbbotHelper(workbenchShell);

            abbotHelper.clickMenuItem (workbenchShell,new String[] {"&Window", "&Close Perspective"});
            Shell perspectiveShell = abbotHelper.clickMenuItem (workbenchShell,
                                                            new String[] {"&Window", "&Open Perspective", "&Other..."},
                                                            "Open Perspective");
            abbotHelper.clickItem (perspectiveShell, "Java");
            abbotHelper.clickButton (perspectiveShell, IDialogConstants.OK_LABEL);  

            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                                                       new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                                       "New");
            abbotHelper.clickTreeItem (wizardShell,
                                   new String[] {"Java", "Java Project"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell,"", "CurrencyConverterPojo");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.doubleClickItem(workbenchShell, "CurrencyConverterPojo");

            wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                                               new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                               "New");
            abbotHelper.clickTreeItem (wizardShell,
                                   new String[] {"Java", "Package"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell,"", "myPackage");
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);           
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean copyCodeToPojoProject() {
        boolean success = true;
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject ("CurrencyConverterPojo");
            String fileDir = aWSRoot.getLocation().toOSString()+ "/src/main/resources/sharedlib";
            IFile aFile = aProject.getFile("src/myPackage/CurrencyConverter.java");
            aFile.create(new FileInputStream (fileDir + "/CurrencyConverter.java"), true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean createHelloWorldProject() {
        boolean success = true;
        try {
            abbotHelper.clickMenuItem (workbenchShell,
                                   new String[] {"&Window", "&Close Perspective"});
            Shell perspectiveShell = abbotHelper.clickMenuItem (workbenchShell,
                                                            new String[] {"&Window", "&Open Perspective", "&Other..."},
                                                            "Open Perspective");
            abbotHelper.clickItem (perspectiveShell, "Java EE");
            abbotHelper.clickButton (perspectiveShell, IDialogConstants.OK_LABEL);  
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                                                       new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                                       "New");
            abbotHelper.clickTreeItem (wizardShell,
                                   new String[] {"Web", "Dynamic Web Project"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell,"", "HelloWorld");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell, "", "helloworld");
            abbotHelper.setTextField(wizardShell, "car", "war");
            abbotHelper.clickButton(wizardShell, "Add a runtime dependency to Geronimo's shared library");
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean copyCodeToHelloWorldProject() {
        boolean success = true;
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject ("HelloWorld");
            String fileDir =aWSRoot.getLocation().toOSString()+"/src/main/resources/sharedlib";
            IFile aFile = aProject.getFile("WebContent/index.jsp");
            aFile.create(new FileInputStream (fileDir + "/index.jsp"), true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean modifyHelloWorldBuildPath() {
        boolean success = true;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                                                     new String[] {"&Project","&Properties"},
                                                     "Properties for HelloWorld");
            abbotHelper.clickItem(wizardShell,"Java Build Path");
            abbotHelper.selectTabItem(wizardShell,"&Projects");
            Shell newShell=abbotHelper.clickButton(wizardShell, "&Add...","Required Project Selection");
            abbotHelper.clickButton(newShell, "&Select All");
            abbotHelper.clickButton(newShell, IDialogConstants.OK_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.OK_LABEL);
            newShell = abbotHelper.clickMenuItem(workbenchShell, new String[]{"&Project","Clea&n..."}, "Clean");
            abbotHelper.clickButton(newShell, IDialogConstants.OK_LABEL);               
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean toggleSharedLibSupport() {
        boolean success = true;
        try {
            serverTasks.showServerOverview();
            abbotHelper.clickButton(workbenchShell, "Enable in-place shared library support.");
            abbotHelper.clickMenuItem(workbenchShell,new String[]{"&File","&Save"});
            abbotHelper.clickMenuItem(workbenchShell, new String[]{"&File","C&lose All"});
            // restart the server to pick up the change
            serverTasks.startServer (true);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean deployHelloWorldProject() {
        boolean success = true;
        try {
            serverTasks.publishAllProjects();   
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public boolean displayApplication() {
    	
        boolean success = false;
        try {
            Shell deployShell = abbotHelper.rightClickItem(workbenchShell, "HelloWorld",
                                                           new String [] {"&Run As", "&1 Run on Server\tAlt+Shift+X, R"}, 
                                                           "Run On Server");
            abbotHelper.clickButton (deployShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitTime( 10000 );
            success = true;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        return success;
    }
}
