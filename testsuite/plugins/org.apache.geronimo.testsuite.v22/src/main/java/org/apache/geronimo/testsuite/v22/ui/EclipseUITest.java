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

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.apache.geronimo.testsuite.common.ui.Constants;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.apache.geronimo.testsuite.common.ui.WorkbenchTasks;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.Preferences;
import abbot.swt.eclipse.utils.WorkbenchUtilities;
import abbot.swt.eclipse.utils.Preferences.Mode;

/*
 * @version $Rev$ $Date$
 */
public class EclipseUITest extends WorkbenchTestCase {
    Shell workbenchShell;
    AbbotHelper abbotHelper;
    ServerTasks serverTasks;
    WorkbenchTasks workbenchTasks;

    protected void setUp() throws Exception {
        super.setUp();
        Preferences.setPerspectiveSwitchPrompt(Mode.Always);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEclipseUI()
    {
        workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
        abbotHelper = new AbbotHelper(workbenchShell);
        serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V22 );
        workbenchTasks = new WorkbenchTasks(workbenchShell, abbotHelper);

        System.out.println("initial validation and set up");
        // run simple test case to make sure that Geronimo was intalled
        // correctly and switch to the JEE Perspective
        assertTrue (workbenchTasks.checkValidInstallation(Constants.SERVER_V22));
        assertTrue (workbenchTasks.showJEEPerspective());

        // install and start the server
        assertTrue (installServer());

        // get the list of extensions and run each test case
        try {
            IExtensionRegistry er = Platform.getExtensionRegistry();
            IConfigurationElement[] ces = er.getConfigurationElementsFor("org.apache.geronimo.testsuite.v22.testCases");
            for (int j = 0; j < ces.length; j++) {
                Object o = ces[j].createExecutableExtension("class");
                if (o instanceof AbstractTestCase) {
                    System.out.println("running test case: " + ces[j].getAttribute("class"));
                    AbstractTestCase gst = (AbstractTestCase)o;
                    gst.setHelpers (workbenchShell, abbotHelper, Constants.SERVER_V22);
                    assertTrue (gst.buildTestCase());
                    assertTrue (gst.runTestCase());
                    assertTrue (gst.cleanupTestCase());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("uninstall the server");
        // stop and uninstall the server
        assertTrue (uninstallServer());
    }
  
    private boolean installServer() {
        boolean success = serverTasks.createServer();
        if (success == true) {
            success = serverTasks.startServer (false);
        }
        return success;
    }

    private boolean uninstallServer() {
        boolean success = serverTasks.stopServer();
        if (success == true) {
            success = serverTasks.removeServer();
        }
        return success;
    }
}
