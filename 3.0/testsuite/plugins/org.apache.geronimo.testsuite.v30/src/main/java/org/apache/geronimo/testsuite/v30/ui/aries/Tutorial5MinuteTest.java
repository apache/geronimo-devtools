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
import org.eclipse.core.resources.IFolder;
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
 * Tutorial 5 minutes test with a bit complicated osgi application.
 * @version $Rev$ $Date$
 */
public class Tutorial5MinuteTest extends WorkbenchTestCase {
    Shell workbenchShell;
    AbbotHelper abbotHelper;
    ServerTasks serverTasks;
    WorkbenchTasks workbenchTasks;

    @Before()
    public void setUp() {
        workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
        abbotHelper = new AbbotHelper(workbenchShell);
        serverTasks = new ServerTasks(workbenchShell, abbotHelper, Constants.SERVER_V30);
    }

    @Test()
    public void test() {
        assertTrue(installServer());
        assertTrue(createOSGiApp());
        assertTrue(createServiceBundle());
        assertTrue(copyCodeToServiceBundle());
        assertTrue(createServiceImplBundle());
        assertTrue(copyCodeToServiceImplBundle());
        assertTrue(createBlueprintFile());
        assertTrue(copyCodeToBlueprintFile());
        assertTrue(createWebBundle());
        assertTrue(copyCodeToWebBundle());
        assertTrue(deployOSGiApp());
        assertTrue(runOSGiApp());
        assertTrue(undeployOSGiApp());
        assertTrue(deleteOSGiApp());
        assertTrue(uninstallServer());
    }

    /*
     * Create and install server
     */
    private boolean installServer() {
        boolean success = serverTasks.createServer();
        if (success == true) {
            success = serverTasks.startServer(false);
        }
        return success;
    }

    /*
     * Stop and uninstall server
     */
    private boolean uninstallServer() {
        boolean success = serverTasks.stopServer();
        if (success == true) {
            success = serverTasks.removeServer();
        }
        return success;
    }

    /*
     * Create osgi application that is used to include bundles.
     */
    private boolean createOSGiApp() {
        boolean success = false;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem(workbenchShell, new String[] { "&File", "&New\tShift+Alt+N",
                    "&Other...\tCtrl+N" }, "New");
            abbotHelper.clickTreeItem(wizardShell, new String[] { "OSGi", "OSGi Application Project" });
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setCursorText("OSGiAppSample");
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Create service bundle.
     */
    private boolean createServiceBundle() {
        boolean success = false;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem(workbenchShell, new String[] { "&File", "&New\tShift+Alt+N",
                    "&Other...\tCtrl+N" }, "New");
            abbotHelper.clickTreeItem(wizardShell, new String[] { "OSGi", "OSGi Bundle Project" });
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setCursorText("OSGiServiceBundleSample");
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Copy code to service bundle. This bundle provides an interface that contains a simple method.
     */
    private boolean copyCodeToServiceBundle() {
        boolean success = false;
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject("OSGiServiceBundleSample");
            IFolder aFolder = aProject.getFolder("src/com.example.helloworld.api");
            aFolder.create(false, true, null);
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/aries/create/tutorial/api";
            IFile aFile = aProject.getFile("src/com.example.helloworld.api/HelloworldService.java");
            aFile.create(new FileInputStream(fileDir + "/HelloworldService.java"), true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            aFile = aProject.getFile("BundleContent/META-INF/MANIFEST.MF");
            aFile.setContents(new FileInputStream(fileDir + "/MANIFEST.MF"), true, true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Create service implementation bundle.
     */
    private boolean createServiceImplBundle() {
        boolean success = false;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem(workbenchShell, new String[] { "&File", "&New\tShift+Alt+N",
                    "&Other...\tCtrl+N" }, "New");
            abbotHelper.clickTreeItem(wizardShell, new String[] { "OSGi", "OSGi Bundle Project" });
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setCursorText("OSGiServiceImplBundleSample");
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Copy code to service implementation bundle. This bundle implements the service interface.
     */
    private boolean copyCodeToServiceImplBundle() {
        boolean success = false;
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject("OSGiServiceImplBundleSample");
            IFolder aFolder = aProject.getFolder("src/com.example.helloworld.biz");
            aFolder.create(false, true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/aries/create/tutorial/biz";
            IFile aFile = aProject.getFile("BundleContent/META-INF/MANIFEST.MF");
            aFile.setContents(new FileInputStream(fileDir + "/MANIFEST.MF"), true, true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            aFile = aProject.getFile("src/com.example.helloworld.biz/HelloworldServiceImpl.java");
            aFile.create(new FileInputStream(fileDir + "/HelloworldServiceImpl.java"), true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Create blueprint file for service implementation bundle.
     */
    private boolean createBlueprintFile() {
        boolean success = false;
        try {
            Shell wizardShell = abbotHelper.rightClickItem(workbenchShell, "OSGiServiceImplBundleSample", new String[] {
                    "&New", "&Other...\tCtrl+N" }, "New");
            abbotHelper.clickTreeItem(wizardShell, new String[] { "OSGi", "Blueprint File" });
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Set the content of the blueprint file.
     */
    private boolean copyCodeToBlueprintFile() {
        boolean success = false;
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject("OSGiServiceImplBundleSample");
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/aries/create/tutorial/biz";
            IFile aFile = aProject.getFile("BundleContent/OSGI-INF/blueprint/blueprint.xml");
            aFile.setContents(new FileInputStream(fileDir + "/blueprint.xml"), true, true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Create web bundle.
     */
    private boolean createWebBundle() {
        boolean success = true;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem(workbenchShell, new String[] { "&File", "&New\tShift+Alt+N",
                                                          "&Other...\tCtrl+N" }, "New");
            abbotHelper.clickTreeItem(wizardShell, new String[] { "OSGi", "OSGi Bundle Project" });
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setCursorText("OSGiWebBundleSample");
            //This is a web bundle.
            abbotHelper.setTextField(wizardShell, "Default Configuration for Apache Geronimo v3.0",
                                     "OSGi Web Configuration");
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton(wizardShell, IDialogConstants.FINISH_LABEL);
            abbotHelper.waitForDialogDisposal(wizardShell);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
    
    /*
     * Copy code to web bundle. This bundle provides a single servlet and a single jsp file.
     */
    private boolean copyCodeToWebBundle() {
        boolean success = false;
        try {
            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject("OSGiWebBundleSample");
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/aries/create/tutorial/web";
            IFile aFile = aProject.getFile("WebContent/META-INF/MANIFEST.MF");
            aFile.setContents(new FileInputStream(fileDir + "/MANIFEST.MF"), true, true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            aFile = aProject.getFile("WebContent/WEB-INF/web.xml");
            aFile.setContents(new FileInputStream(fileDir + "/web.xml"), true, true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            aFile = aProject.getFile("WebContent/index.jsp");
            aFile.create(new FileInputStream(fileDir + "/index.jsp"), true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            aFile = aProject.getFile("JavaResources:src/com.example.helloworld.web/HelloWorld.java");
            aFile.create(new FileInputStream(fileDir + "/HelloWorld.java"), true, null);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            success = true;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Deploy the osgi application to server.
     */
    private boolean deployOSGiApp() {
        boolean success = false;
        try {
            success = serverTasks.publishAllProjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Run the osgi application on server.
     */
    private boolean runOSGiApp() {
        boolean success = false;
        try {
            EclipseSelenium selenium = new EclipseSelenium();
            selenium.start();
            selenium.open("http://localhost:8080/OSGiWebBundleSample/sayhello");
            selenium.waitForPageToLoad("60000");
            success = (selenium.getHtmlSource().indexOf("Hello Aries World!") > 0);
            abbotHelper.waitTime(AbbotHelper.WAIT_STANDARD);
            selenium.stop();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Undeploy the osgi application.
     */
    private boolean undeployOSGiApp() {
        boolean success = false;
        try {
            success = serverTasks.removeAllProjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /*
     * Delete the osgi application.
     */
    private boolean deleteOSGiApp() {
        boolean success = false;
        try {
            ProjectTasks projectTasks = new ProjectTasks(workbenchShell, abbotHelper);
            //The order of bundles that are deleted is arbitrary.
            success = projectTasks.deleteProject("OSGiWebBundleSample");
            if(success == true) {
                success = projectTasks.deleteProject("OSGiServiceImplBundleSample");
            }
            if(success == true) {
                success = projectTasks.deleteProject("OSGiServiceBundleSample");
            }
            if(success == true) {
                success = projectTasks.deleteProject("OSGiAppSample");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
