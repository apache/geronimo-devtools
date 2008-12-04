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

import java.io.FileInputStream;

import org.apache.geronimo.testsuite.common.AssertUtil;
import org.apache.geronimo.testsuite.common.selenium.EclipseSelenium;
import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.apache.geronimo.testsuite.common.ui.Constants;
import org.apache.geronimo.testsuite.common.ui.ProjectTasks;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.apache.geronimo.testsuite.common.ui.WorkbenchTasks;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.WorkbenchUtilities;

/*
 * @version $Rev$ $Date$
 */
public class RunOnServerTest extends WorkbenchTestCase {

    Shell workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
    AbbotHelper abbotHelper = new AbbotHelper(workbenchShell);
    boolean success = false;


    protected void setUp() throws Exception {
        super.setUp();
    }


    protected void tearDown() throws Exception {
        super.tearDown();
        deleteProject();
        deleteServer();
    }


    public void testRunOnServer() {
        createServer();
        startServer();
        createProject();
        copyCodeToProject();
        deployProject();
    }


    private void createServer() {
        success = false;
        try {
            WorkbenchTasks workbenchTasks = new WorkbenchTasks(workbenchShell, abbotHelper);
            // so we are sure that we are looking in the desired perspective
            workbenchTasks.showJEEPerspective();
            ServerTasks serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V21 );
            serverTasks.createServer();
            serverTasks.startServer();
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }


    private void startServer() {
        success = false;
        try {
            ServerTasks serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V21 );
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }


    private void createProject() {
        success = false;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                                                           new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                                           "New");
            abbotHelper.clickTreeItem (wizardShell,
                                       new String[] {"Web", "Dynamic Web Project"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell,"", "DynamicWebProject");
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }


    private void copyCodeToProject() {
        success = false;
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject ("DynamicWebProject");
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/run-on-server";
            IFile aFile = aProject.getFile("WebContent/index.jsp");
            aFile.create(new FileInputStream (fileDir + "/index.jsp"), true, null);
            abbotHelper.waitTime( 1500 );
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }


    private void deployProject() {
        success = false;
        try {
            Shell deployShell = abbotHelper.rightClickItem(workbenchShell, "DynamicWebProject",
                                                           new String [] {"&Run As", "&1 Run on Server\tAlt+Shift+X, R"}, 
                                                           "Run On Server");
            abbotHelper.clickButton (deployShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitTime( 10000 );
            abbotHelper.clickCombo( workbenchShell, "http://localhost:8080/DynamicWebProject/");
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }


    private void deleteProject() {
        success = false;
        try {
            ProjectTasks projectTasks = new ProjectTasks(workbenchShell, abbotHelper, Constants.SERVER_V21 );
            projectTasks.deleteProject ("DynamicWebProject");
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }


    private void deleteServer() {
        success = false;
        try {
            ServerTasks serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V21 );
            serverTasks.stopServer();
            serverTasks.removeServer();
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue( success );
    }

}
