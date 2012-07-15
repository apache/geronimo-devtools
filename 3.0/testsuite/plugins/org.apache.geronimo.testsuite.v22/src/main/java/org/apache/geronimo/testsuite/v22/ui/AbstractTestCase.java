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
import org.apache.geronimo.testsuite.common.ui.ProjectTasks;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.apache.geronimo.testsuite.common.ui.WorkbenchTasks;
import org.eclipse.swt.widgets.Shell;

/*
 * This is the abstract base class of all the GUI test cases that use Abbot.
 * Each test case must know how to build itself, run, and then clean up after itself.
 *
 * It is safe to assume the following
 * 1) Geronimo validation has taken place
 * 2) Eclipse is in the J2EE perspective
 * 3) The server will be installed and started before any test case is run
 * 4) The server will be stopped and uninstalled after all test cases have finished
 *
 * @version $Rev$ $Date$
 */
public abstract class AbstractTestCase {

    protected Shell workbenchShell;
    protected AbbotHelper abbotHelper;
    protected ServerTasks serverTasks;
    protected ProjectTasks projectTasks;
    protected WorkbenchTasks workbenchTasks;
    
    public AbstractTestCase () {
    }

    public void setHelpers (Shell aShell, AbbotHelper aHelper, String serverVersion) {
        workbenchShell = aShell;
        abbotHelper = aHelper;

        // instantiate the server and project tasks while we're in here
        serverTasks = new ServerTasks (workbenchShell, abbotHelper, serverVersion);
        projectTasks = new ProjectTasks(workbenchShell, abbotHelper);
        workbenchTasks = new WorkbenchTasks(workbenchShell, abbotHelper);
    }

    // abstract methods return true if everything was successful 
    abstract public boolean buildTestCase ();
    abstract public boolean runTestCase ();
    abstract public boolean cleanupTestCase ();
}
