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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.WorkbenchUtilities;

/*
 * Aries tool install test
 * @version $Rev$ $Date$
 */
public class AriesToolInstallTest extends WorkbenchTestCase {
	Shell workbenchShell;
	AbbotHelper abbotHelper;

	@Before()
	public void setUp() throws Exception {
		workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
		abbotHelper = new AbbotHelper(workbenchShell);
	}

	@After()
	public void tearDown() throws Exception {
	}
	
	@Test()
	public void testAriesToolInstall() {
		assertTrue(installAriesTool());
		//TODO check installation
	}
	
	private boolean installAriesTool() {
		boolean success = true;
		try {
			Shell newShell = abbotHelper.clickMenuItem(workbenchShell, new String[]{"&Help", "Install New &Software..."}, "Install");
			Shell nextShell = abbotHelper.clickButton(newShell, "&Add...", "Add Repository");
			abbotHelper.setTextField(nextShell, "http://", "http://public.dhe.ibm.com/ibmdl/export/pub/software/rational/OSGiAppTools");
			abbotHelper.clickButton(nextShell, IDialogConstants.OK_LABEL);
			//Need time to retrieve the tree.
			abbotHelper.waitTime(30000);
			abbotHelper.clickButton(newShell, "&Select All");
			abbotHelper.clickButton(newShell, IDialogConstants.NEXT_LABEL);
			//Need time to calculate the dependencies of the plugin.
			abbotHelper.waitTime(30000);
			abbotHelper.clickButton(newShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.clickButton(newShell, "I &accept the terms of the license agreement");
			nextShell = abbotHelper.clickButton(newShell, IDialogConstants.FINISH_LABEL, "Software Update");
			abbotHelper.clickButton(nextShell, "&Not Now");
		} catch(Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

}
