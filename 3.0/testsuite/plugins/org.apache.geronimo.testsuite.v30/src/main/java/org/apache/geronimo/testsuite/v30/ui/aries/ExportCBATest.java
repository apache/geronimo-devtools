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

package org.apache.geronimo.testsuite.v30.ui.aries;

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.apache.geronimo.testsuite.common.ui.Constants;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.WorkbenchUtilities;

/*
 * Export cba test
 * @version $Rev$ $Date$
 */
public class ExportCBATest extends WorkbenchTestCase {
	Shell workbenchShell;
	AbbotHelper abbotHelper;
	ServerTasks serverTasks;

	@Before()
	public void setUp() throws Exception {
		workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
		abbotHelper = new AbbotHelper(workbenchShell);
		serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V30);
	}

	@Test()
	public void testCBAExport() {
		assertTrue(installServer());
		//TODO create and export cba
		assertTrue(uninstallServer());
	}
	
	private boolean installServer() {
		boolean success = serverTasks.createServer();
		if(success == true) {
			success = serverTasks.startServer(false);
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
