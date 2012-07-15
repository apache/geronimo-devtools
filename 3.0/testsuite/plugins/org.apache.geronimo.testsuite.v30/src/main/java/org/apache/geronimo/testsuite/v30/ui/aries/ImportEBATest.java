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

import java.io.File;

import org.apache.geronimo.testsuite.common.selenium.EclipseSelenium;
import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.apache.geronimo.testsuite.common.ui.Constants;
import org.apache.geronimo.testsuite.common.ui.ProjectTasks;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.apache.geronimo.testsuite.common.ui.WorkbenchTasks;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.WorkbenchUtilities;

/*
 * Import eba test
 * @version $Rev$ $Date$
 */
public class ImportEBATest extends WorkbenchTestCase {
	Shell workbenchShell;
	AbbotHelper abbotHelper;
	ServerTasks serverTasks;
	WorkbenchTasks workbenchTasks;
	ProjectTasks projectTasks;

	@Before()
	public void setUp() {
		workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
		abbotHelper = new AbbotHelper(workbenchShell);
		serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V30);
		projectTasks = new ProjectTasks(workbenchShell, abbotHelper);
	}
	
	@Test()
	public void testEBAImport() {
		assertTrue(installServer());
		assertTrue(importEBA());
		assertTrue(deployEBA());
		assertTrue(runEBA());
		assertTrue(undeployEBA());
		assertTrue(deleteEBA());
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
	
	private boolean importEBA() {
		boolean success = false;
		try {
			Shell wizardShell = abbotHelper.clickMenuItem(workbenchShell, 
					            new String[]{"&File", "&Import..."}, "Import");
			abbotHelper.clickTreeItem(wizardShell, new String[]{"OSGi", "OSGi Application(EBA)"});
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.setCursorText(getEBALocation());
			//Need some time to calculate the bundles that are included in the eba.
			abbotHelper.waitTime(5000);
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
			abbotHelper.waitForDialogDisposal(wizardShell);
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private String getEBALocation() {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		String location = ws.getRoot().getLocation().toOSString();
		String fileName = "com.example.helloworld.eba-0.1-incubating.eba";
		//The location is a bit complicated.
		location = location + File.separatorChar + 
                   "src" + File.separatorChar + 
                   "main" + File.separatorChar + 
                   "resources" + File.separatorChar +
                   "aries" + File.separatorChar +
                   "import" + File.separatorChar + 
                   "eba" + File.separatorChar + fileName;
		return location;
	}
	
	private boolean deployEBA() {
		boolean success = false;
		try {
			success = serverTasks.publishAllProjects();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean runEBA() {
	    boolean success = false;
	    try {
	        EclipseSelenium selenium = new EclipseSelenium();
	        selenium.start();
	        selenium.open("http://localhost:8080/helloworld/sayhello");
	        selenium.waitForPageToLoad("60000");
	        success = (selenium.getHtmlSource().indexOf("Hello Aries World!") > 0);
	        abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
	        selenium.stop();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
		return success;
	}
	
	private boolean undeployEBA() {
		boolean success = false;
		try {
			success = serverTasks.removeAllProjects();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean deleteEBA() {
		boolean success = false;
		try {
		    //The order of bundles that are deleted is arbitrary.
			success = projectTasks.deleteProject("com.example.helloworld.web");
			if(success == true) {
				success = projectTasks.deleteProject("com.example.helloworld.biz");
			}
			if(success == true) {
				success = projectTasks.deleteProject("com.example.helloworld.api");
			}
			if(success == true) {
				success = projectTasks.deleteProject("com.example.helloworld.eba");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
}
