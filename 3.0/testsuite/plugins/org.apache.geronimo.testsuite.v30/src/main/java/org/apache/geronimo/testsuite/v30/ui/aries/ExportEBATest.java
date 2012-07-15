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
import java.io.FileInputStream;

import org.apache.geronimo.testsuite.common.selenium.EclipseSelenium;
import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.apache.geronimo.testsuite.common.ui.Constants;
import org.apache.geronimo.testsuite.common.ui.ProjectTasks;
import org.apache.geronimo.testsuite.common.ui.ServerTasks;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.WorkbenchUtilities;

/*
 * Export eba test.
 * @version $Rev$ $Date$
 */
public class ExportEBATest extends WorkbenchTestCase {
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
	public void testEBAExport() {
		assertTrue(installServer());
		assertTrue(createEBA());
		assertTrue(createBundle());
		assertTrue(copyCodeToBundle());
		assertTrue(exportEBA());
		assertTrue(validateEBA());
		assertTrue(runEBA());
		assertTrue(deleteBundle());
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
	
	private boolean createEBA() {
		boolean success = false;
		try {
			Shell wizardShell = abbotHelper.clickMenuItem(workbenchShell, 
		                        new String[]{"&File", "&New\tShift+Alt+N", "&Other...\tCtrl+N"}, "New");
			abbotHelper.clickTreeItem(wizardShell, new String[]{"OSGi", "OSGi Application Project"});
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.setCursorText("OSGiAppSample");
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
			abbotHelper.waitForDialogDisposal(wizardShell);
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
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
			//This is a web bundle.
            abbotHelper.setTextField(wizardShell, "Default Configuration for Apache Geronimo v3.0",
                                     "OSGi Web Configuration");
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
            IProject aProject = aWSRoot.getProject("OSGiWebBundleSample");
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/aries/export/eba";
            IFile aFile = aProject.getFile("WebContent/index.jsp");
            aFile.create(new FileInputStream(fileDir + "/index.jsp"), true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            success = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return success;
	}
	
	private boolean exportEBA() {
		boolean success = false;
		try {
			Shell wizardShell = abbotHelper.rightClickItem(workbenchShell, "OSGiAppSample", 
					                                       new String[]{"E&xport", "Exp&ort..."}, "Export");
			abbotHelper.clickTreeItem(wizardShell, new String[]{"OSGi", "OSGi Application(EBA)"});
			abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
			abbotHelper.setTextField(wizardShell, "", getExportLocation());
			abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
			success = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private String getExportLocation() {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		String location = ws.getRoot().getLocation().toOSString();
		String fileName = "OSGiAppSample.eba";
		int index = location.lastIndexOf(File.separatorChar);
		if(index > -1) {
			location = location.substring(0, index);
		}
		index = location.lastIndexOf(File.separatorChar);
		if(index > -1) {
			location = location.substring(0, index);
		}
		index = location.lastIndexOf(File.separatorChar);
		if(index > -1) {
			location = location.substring(0, index);
		}
		location = location + File.separatorChar + "results" + File.separatorChar + fileName;
		return location;
	}
	
	/*
     * Validate the eba through deploying it to server. 
     * If there is no exception during deployment, then the eba is valid.
     */
	private boolean validateEBA() {
	    boolean success = false;
	    try {
	        String serverBinDir = getServerInstallDir() + File.separatorChar + "bin";
	        String fileLocation = getExportLocation();
	        //This is the deploy command.
	        String deployCommand = serverBinDir + File.separatorChar +
	                               "deploy --user system --password manager deploy " + 
	                               fileLocation;
	        //Deploy exported eba to server to validate it.
	        Process proc = Runtime.getRuntime().exec(deployCommand);
	        //Wait for deployment finished.
	        int exitVal = proc.exitValue();
	        //According to jdk documentation, by convention, the value 0 indicates normal termination.
	        if(exitVal!=0) {
	            return false;
	        }
	        abbotHelper.waitTime(AbbotHelper.WAIT_LONG);
	        success = true;
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
            selenium.open("http://localhost:8080/OSGiBundleSample/index.jsp");
            selenium.waitForPageToLoad("60000");
            success = (selenium.getHtmlSource().indexOf("Test OSGi bundle") > 0);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            selenium.stop();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return success;
    }
	
	private String getServerInstallDir() {
	    IWorkspace ws = ResourcesPlugin.getWorkspace();
        String location = ws.getRoot().getLocation().toOSString();
        int index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
        }
        index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
        }
        index = location.lastIndexOf(File.separatorChar);
        if (index > -1) {
            location = location.substring(0,index);
        }
        location = location + File.separatorChar + Constants.getConstant(Constants.SERVER_V30, Constants.SERVERPATH);
        return location;
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
	
	private boolean deleteEBA() {
		boolean success = false;
		try {
			ProjectTasks projectTasks = new ProjectTasks(workbenchShell, abbotHelper);
			success = projectTasks.deleteProject("OSGiAppSample");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return success;
	}
}
