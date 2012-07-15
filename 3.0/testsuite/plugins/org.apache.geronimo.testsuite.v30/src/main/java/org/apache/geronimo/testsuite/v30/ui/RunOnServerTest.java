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

package org.apache.geronimo.testsuite.v30.ui;

import java.io.FileInputStream;

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

/*
 * @version $Rev$ $Date$
 */
public class RunOnServerTest extends AbstractTestCase {

    public RunOnServerTest() {
        super ();
    }

    @Override
    public boolean buildTestCase() {
        boolean success = true;
        try {
            // create the project
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                                                           new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                                                           "New");
            abbotHelper.clickTreeItem (wizardShell,
                                       new String[] {"Web", "Dynamic Web Project"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell,"", "DynamicWebProject");
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);

            // copy the code to the project
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject ("DynamicWebProject");
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/run-on-server";
            IFile aFile = aProject.getFile("WebContent/index.jsp");
            aFile.create(new FileInputStream (fileDir + "/index.jsp"), true, null);
            abbotHelper.waitTime (AbbotHelper.WAIT_STANDARD);
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean runTestCase() {
        boolean success = true;
        try {
            // deploy the project
            Shell deployShell = abbotHelper.rightClickItem(workbenchShell, "DynamicWebProject",
                                                           new String [] {"&Run As", "&1 Run on Server\tAlt+Shift+X, R"}, 
                                                           "Run On Server");
            abbotHelper.clickButton (deployShell, IDialogConstants.FINISH_LABEL);
            // sometimes publish takes a while
            abbotHelper.waitTime (AbbotHelper.WAIT_LONG + AbbotHelper.WAIT_LONG);
            abbotHelper.clickCombo (workbenchShell, "http://localhost:8080/DynamicWebProject/");
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean cleanupTestCase() {
        boolean success = true;
        try {
            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&File", "&Close"});
            success = serverTasks.removeAllProjects();
            if (success == true) {
                success = projectTasks.deleteProject ("DynamicWebProject");
            }
        } catch (Exception e) {
            success = false;
        }
        return success;
    }
}
