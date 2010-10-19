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

import java.io.FileInputStream;

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
import org.junit.Before;
import org.junit.Test;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.WorkbenchUtilities;

/*
 * New osgi bundle test
 * @version $Rev$ $Date$
 */
public class NewOSGiBundleTest extends WorkbenchTestCase {
	Shell workbenchShell;
	AbbotHelper abbotHelper;
	ServerTasks serverTasks;
	WorkbenchTasks workbenchTasks;

	@Before()
	public void setUp() {
		workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
		abbotHelper = new AbbotHelper(workbenchShell);
		serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V30 );
	}
	
	@Test()
	public void testNewOSGiBundle() {
		assertTrue(installServer());
		assertTrue(createBundle());
		assertTrue(copyCodeToBundle());
		assertTrue(deployBundle());
		assertTrue(runBundle());
		assertTrue(undeployBundle());
		assertTrue(deleteBundle());
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
	
	private boolean createBundle() {
		boolean success = false;
		try {
			Shell wizardShell = abbotHelper.clickMenuItem(workbenchShell, 
					            new String[]{"&File", "&New\tShift+Alt+N", "&Other...\tCtrl+N"}, "New");
			abbotHelper.clickTreeItem(wizardShell, new String[]{"OSGi", "OSGi Bundle Project"});
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.setCursorText("OSGiBundleSample");
			//Create a simple web bundle here.
			abbotHelper.setTextField(wizardShell, 
			                         "Default Configuration for Apache Geronimo v3.0", 
			                         "OSGi Web Configuration");
			//Don't need to add this bundle to any osgi application, so uncheck it.
			abbotHelper.clickButton(wizardShell, "Add bundle to application");
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
			abbotHelper.waitForDialogDisposal(wizardShell);
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean copyCodeToBundle() {
		boolean success = false;
		try {
		    IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
		    IProject aProject = aWSRoot.getProject("OSGiBundleSample");
		    String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/aries/create/wab";
		    //The web bundle only contains a jsp file.
		    IFile aFile = aProject.getFile("WebContent/index.jsp");
		    aFile.create(new FileInputStream(fileDir + "/index.jsp"), true, null);
		    abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean deployBundle() {
		boolean success = false;
		try {
			success = serverTasks.publishAllProjects();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean runBundle() {
	    boolean success = false;
	    try {
	        EclipseSelenium selenium = new EclipseSelenium();
	        selenium.start();
	        selenium.open("http://localhost:8080/OSGiBundleSample/index.jsp");
	        selenium.waitForPageToLoad("60000");
	        success = (selenium.getHtmlSource().indexOf("Test OSGi bundle!") > 0);
	        abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
	        selenium.stop();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    return success;
	}
	
	private boolean undeployBundle() {
		boolean success = false;
		try {
			success = serverTasks.removeAllProjects();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean deleteBundle() {
		boolean success = false;
		try {
			ProjectTasks projectTasks = new ProjectTasks(workbenchShell, abbotHelper);
			success = projectTasks.deleteProject("OSGiBundleSample");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
}
