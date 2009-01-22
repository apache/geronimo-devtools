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

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.apache.geronimo.testsuite.common.ui.Constants;
import org.apache.geronimo.testsuite.common.ui.ProjectTasks;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.apache.geronimo.testsuite.common.ui.WorkbenchTasks;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.Preferences;
import abbot.swt.eclipse.utils.WorkbenchUtilities;
import abbot.swt.eclipse.utils.Preferences.Mode;

/**
 * Tutorial5MinuteTest
 *
 * @version $Rev$ $Date$
 */
public class Tutorial5MinuteTest extends WorkbenchTestCase {
    Shell workbenchShell;
    AbbotHelper aHelper;
    
    protected void setUp() throws Exception {
        super.setUp();
        Preferences.setPerspectiveSwitchPrompt(Mode.Always);
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
            
            ServerTasks serverTasks = new ServerTasks(workbenchShell, aHelper, Constants.SERVER_V21 );
            WorkbenchTasks workbenchTasks = new WorkbenchTasks(workbenchShell, aHelper);
            ProjectTasks projectTasks = new ProjectTasks(workbenchShell, aHelper);
            
            // so we are sure that we are looking in the desired perspective
            workbenchTasks.showJEEPerspective();

            // create server from an installed instance
            serverTasks.createServer();

            projectTasks.createProjects ();

            serverTasks.publishAllProjects();
            serverTasks.startServer();

            projectTasks.webTesting();

            serverTasks.stopServer();

            // delete the projects that have been created
            // reverse alphabetical is a little smoother
            projectTasks.deleteProject ("SampleWAR");
            projectTasks.deleteProject ("SampleEJB");
            projectTasks.deleteProject ("SampleEAR");

            // remove the server 
            serverTasks.removeServer();

            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue (success);
    }
}
