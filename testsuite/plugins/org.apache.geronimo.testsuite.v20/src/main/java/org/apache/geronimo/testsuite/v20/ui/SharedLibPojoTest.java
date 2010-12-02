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

package org.apache.geronimo.testsuite.v20.ui;

import java.io.FileInputStream;

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.apache.geronimo.testsuite.common.ui.Constants;
import org.apache.geronimo.testsuite.common.ui.ProjectTasks;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.WorkbenchUtilities;
import abbot.swt.finder.generic.MultipleFoundException;
import abbot.swt.finder.generic.NotFoundException;

/*
 * @version $Rev$ $Date$
 */
public class SharedLibPojoTest extends WorkbenchTestCase {

	ServerTasks serverTasks;
    Shell aShell;
    AbbotHelper aHelper;
    boolean success = false;

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        deleteProjects();
        deleteServer();
    }

    public void testSharedLib()
    {
    	createServer();
        createPojoProject();
        copyCodeToPojoProject();
        createHelloWorldProject();
        copyCodeToHelloWorldProject();
        modifyHelloWorldBuildPath();
        startServer();
        addSharedLibSupport();
        displayApplication();    
    }
    public void createServer() {
    	aShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
    	aHelper = new AbbotHelper (aShell);

        serverTasks = new ServerTasks(aShell, aHelper, Constants.SERVER_V20 );
        try {
			serverTasks.createServer();
		} catch (MultipleFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void createPojoProject()
    {
        try {
//            aShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
//            aHelper = new AbbotHelper(aShell);
//            ServerTasks serverTasks = new ServerTasks(aShell, aHelper, Constants.SERVER_V21 );
//            serverTasks.createServer();
            aHelper.clickMenuItem (aShell,new String[] {"&Window", "&Close Perspective"});
            Shell perspectiveShell = aHelper.clickMenuItem (aShell,
                                                            new String[] {"&Window", "&Open Perspective", "&Other..."},
                                                            "Open Perspective");
            aHelper.clickItem (perspectiveShell, "Java");
            aHelper.clickButton (perspectiveShell, IDialogConstants.OK_LABEL);  

            Shell wizardShell = aHelper.clickMenuItem (aShell,
                                                       new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                                       "New");
            aHelper.clickTreeItem (wizardShell,
                                   new String[] {"Java", "Java Project"});
            aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            aHelper.setTextField(wizardShell,"", "CurrencyConverterPojo");
            aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
            aHelper.doubleClickItem(aShell, "CurrencyConverterPojo");

            wizardShell=aHelper.clickMenuItem (aShell,
                                               new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                               "New");
            aHelper.clickTreeItem (wizardShell,
                                   new String[] {"Java", "Package"});
            aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            aHelper.setTextField(wizardShell,"", "myPackage");
            aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);           
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyCodeToPojoProject()
    {
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject ("CurrencyConverterPojo");
            String fileDir =aWSRoot.getLocation().toOSString()+ "/src/main/resources/sharedlib";
            IFile aFile = aProject.getFile("src/myPackage/CurrencyConverter.java");
            aFile.create(new FileInputStream (fileDir + "/CurrencyConverter.java"), true, null);
            aHelper.waitTime(1500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createHelloWorldProject()
    {
        try {
            aHelper.clickMenuItem (aShell,
                                   new String[] {"&Window", "&Close Perspective"});
            Shell perspectiveShell = aHelper.clickMenuItem (aShell,
                                                            new String[] {"&Window", "&Open Perspective", "&Other..."},
                                                            "Open Perspective");
            aHelper.clickItem (perspectiveShell, "Java EE");
            aHelper.clickButton (perspectiveShell, IDialogConstants.OK_LABEL);  
            Shell wizardShell = aHelper.clickMenuItem (aShell,
                                                       new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                                       "New");
            aHelper.clickTreeItem (wizardShell,
                                   new String[] {"Web", "Dynamic Web Project"});
            aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            aHelper.setTextField(wizardShell,"", "HelloWorld");
            aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            aHelper.setTextField(wizardShell, "", "helloworld");
            aHelper.setTextField(wizardShell, "car", "war");
            aHelper.clickButton(wizardShell, "Add a runtime dependency to Geronimo's shared library");
            aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
            aHelper.waitForDialogDisposal(wizardShell);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void copyCodeToHelloWorldProject()
    {
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject ("HelloWorld");
            String fileDir =aWSRoot.getLocation().toOSString()+"/src/main/resources/sharedlib";
            IFile aFile = aProject.getFile("WebContent/index.jsp");
            aFile.create(new FileInputStream (fileDir + "/index.jsp"), true, null);
            aHelper.waitTime(1500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modifyHelloWorldBuildPath()
    {
        try {
            Shell wizardShell=aHelper.clickMenuItem (aShell,
                                                     new String[] {"&Project","&Properties"},
                                                     "Properties for HelloWorld");
            aHelper.clickItem(wizardShell,"Java Build Path");
            aHelper.selectTabItem(wizardShell,"&Projects");
            Shell newShell=aHelper.clickButton(wizardShell, "&Add...","Required Project Selection");
            aHelper.clickButton(newShell, "&Select All");
            aHelper.clickButton(newShell, IDialogConstants.OK_LABEL);
            aHelper.clickButton(wizardShell, IDialogConstants.OK_LABEL);
            newShell=aHelper.clickMenuItem(aShell, new String[]{"&Project","Clea&n..."}, "Clean");
            aHelper.clickButton(newShell, IDialogConstants.OK_LABEL);               
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSharedLibSupport()
    {
        try {
            ServerTasks serverTasks = new ServerTasks(aShell, aHelper, Constants.SERVER_V20 );
            serverTasks.editServer();
            aHelper.clickButton(aShell, "Enable in-place shared library support.");
            aHelper.clickMenuItem(aShell,new String[]{"&File","&Save"});
            aHelper.clickMenuItem(aShell, new String[]{"&File","C&lose All"});
        }
        catch (Exception e) {
            e.printStackTrace();        
        }
    }

    public void startServer()
    {
        try {
            serverTasks.startServer();// start server first!
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayApplication()
    {
        success = false;
        try {
            Shell deployShell = aHelper.rightClickItem(aShell, "HelloWorld",
                                                           new String [] {"&Run As", "&1 Run on Server\tAlt+Shift+X, R"}, 
                                                           "Run On Server");
            aHelper.clickButton (deployShell, IDialogConstants.FINISH_LABEL);
            aHelper.waitTime( 10000 );
            aHelper.clickCombo( aShell, "http://localhost:8080/HelloWorld/");
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }

    public void deleteProjects()
    {
        try {
            ProjectTasks projectTasks = new ProjectTasks(aShell, aHelper, Constants.SERVER_V20 );
            // delete the projects that have been created
            // reverse alphabetical is a little smoother
            projectTasks.deleteProject ("HelloWorld");
            projectTasks.deleteProject ("CurrencyConverterPojo");
            success=true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(success);
    }

    public void deleteServer()
    {
        try {
            serverTasks = new ServerTasks(aShell, aHelper, Constants.SERVER_V20 );
            // stop the server 
            serverTasks.stopServer();
            // remove the server 
            serverTasks.removeServer();
            success=true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(success);
    }

}
