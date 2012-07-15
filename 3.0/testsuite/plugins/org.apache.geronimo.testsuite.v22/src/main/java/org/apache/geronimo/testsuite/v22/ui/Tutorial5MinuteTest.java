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

import java.io.FileInputStream;

import org.apache.geronimo.testsuite.common.selenium.EclipseSelenium;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

/**
 * Tutorial5MinuteTest
 *
 * @version $Rev$ $Date$
 */
public class Tutorial5MinuteTest extends AbstractTestCase {

    @Override
    public boolean buildTestCase() {
        boolean success = createProjects ();
        if (success == true) {
            success = copyCode();
        }

        return success;
    }

    @Override
    public boolean runTestCase() {
        boolean success = serverTasks.publishAllProjects();
        if (success == true) {
            success = webTesting();
        }

        return success;
    }

    @Override
    public boolean cleanupTestCase() {
        boolean success = serverTasks.removeAllProjects();
        if (success == true) {
            success =  projectTasks.deleteProject ("SampleWAR");
        }
        if (success == true) {
            success = projectTasks.deleteProject ("SampleEJB");
        }
        if (success == true) {
            success = projectTasks.deleteProject ("SampleEAR");
        }

        return success;
    }

    public boolean createProjects() {
        boolean success = createEARProject ();
        if (success == true) {
            success = createEJBProject ();
        }
        if (success == true) {
            success = createWARProject ();
        }

        return success;
    }

    private boolean createEARProject() {
        boolean success = true;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                    "New");

            abbotHelper.clickTreeItem (wizardShell,
                    new String[] {"Java EE", "Enterprise Application Project"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell, "", "SampleEAR");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton (wizardShell, "Generate Deployment Descriptor");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell, "default", "sampleear");
            abbotHelper.setTextField(wizardShell, "", "sample-ear");
            abbotHelper.setTextField(wizardShell, "car", "ear");
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);

            abbotHelper.waitForDialogDisposal (wizardShell);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    private boolean createEJBProject() {
        boolean success = true;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                    "New");

            abbotHelper.clickTreeItem (wizardShell,
                    new String[] {"EJB", "EJB Project"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell, "", "SampleEJB");
            abbotHelper.clickButton (wizardShell, "Add &project to an EAR");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton (wizardShell, "Create an EJB Client JAR module to hold the client interfaces and classes.");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell, "default", "sampleear");
            abbotHelper.setTextField(wizardShell, "", "sample-ejb");
            abbotHelper.setTextField(wizardShell, "car", "ejb");
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);

            abbotHelper.waitForDialogDisposal (wizardShell);

            abbotHelper.doubleClickItem(workbenchShell, "SampleEJB");
            abbotHelper.doubleClickItem(workbenchShell, "ejbModule");
            abbotHelper.doubleClickItem(workbenchShell, "META-INF");
            Shell questionShell = abbotHelper.rightClickItem(workbenchShell, "openejb-jar.xml",
                    new String[] {"&Delete"}, "Delete Resources");
            abbotHelper.clickButton (questionShell, IDialogConstants.OK_LABEL);
            abbotHelper.waitForDialogDisposal (questionShell);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    private boolean createWARProject() {
        boolean success = true;
        try {
            Shell wizardShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                    "New");

            abbotHelper.clickTreeItem (wizardShell,
                    new String[] {"Web", "Dynamic Web Project"});
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell, "", "SampleWAR");
            abbotHelper.clickButton (wizardShell, "Add &project to an EAR");
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.setTextField(wizardShell, "default", "sampleear");
            abbotHelper.setTextField(wizardShell, "", "sample-war");
            abbotHelper.setTextField(wizardShell, "car", "war");
            abbotHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
        
            abbotHelper.waitForDialogDisposal (wizardShell);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
    
    private boolean copyCode ()  {
        boolean success = true;
        try {
            abbotHelper.clickItem (workbenchShell, "SampleWAR");
            Shell propShell = abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&Project", "&Properties"},
                    "Properties for SampleWAR");
            abbotHelper.clickItem (propShell, "Java EE Module Dependencies");
            abbotHelper.clickButton (propShell, "Select &All");
            abbotHelper.clickButton (propShell, "&Apply");
            abbotHelper.clickButton (propShell, IDialogConstants.OK_LABEL);

            IWorkspaceRoot aWSRoot = ResourcesPlugin.getWorkspace().getRoot();
            IProject aProject = aWSRoot.getProject ("SampleEJB");
            IFolder aFolder = aProject.getFolder("ejbModule/sampleear");
            aFolder.create (false, true, null);
            String fileDir = aWSRoot.getLocation().toOSString() + "/src/main/resources/ui-tutorial";
            IFile aFile = aProject.getFile("ejbModule/sampleear/RemoteBusinessInterface.java");
            aFile.create(new FileInputStream (fileDir + "/RemoteBusinessInterface.java"), true, null);
            aFile = aProject.getFile("ejbModule/sampleear/MyStatelessSessionBean.java");
            aFile.create(new FileInputStream (fileDir + "/MyStatelessSessionBean.java"), true, null);

            aProject = aWSRoot.getProject ("SampleWAR");
            aFolder = aProject.getFolder("src/sampleear");
            aFolder.create (false, true, null);
            aFile = aProject.getFile("WebContent/index.jsp");
            aFile.create(new FileInputStream (fileDir + "/index.jsp"), true, null);
            // have to go through Servlet Creation dialog to make the web.xml correct
            Shell servletShell = abbotHelper.rightClickItem(workbenchShell, "SampleWAR",
                    new String [] {"&New", "Servlet"}, "Create Servlet");
            abbotHelper.setCursorText("MyServlet");
            abbotHelper.setTextField(servletShell, "", "sampleear");
            abbotHelper.clickButton (servletShell, IDialogConstants.NEXT_LABEL);
            abbotHelper.clickItem(servletShell, "/MyServlet");
            Shell urlShell = abbotHelper.clickEnabledButton (servletShell, "Edit...", "URL Mappings");
            abbotHelper.setTextField(urlShell, "/MyServlet", "/sayHello");
            abbotHelper.clickButton (urlShell, IDialogConstants.OK_LABEL);
            abbotHelper.clickButton (servletShell, IDialogConstants.FINISH_LABEL);

            // copy over the correct contents
            aFile = aProject.getFile("src/sampleear/MyServlet.java");
            aFile.setContents(new FileInputStream (fileDir + "/MyServlet.java"), true, true, null);

            // close the open file
            abbotHelper.clickMenuItem (workbenchShell,
                    new String[] {"&File", "&Close"});
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    private boolean webTesting () {
        boolean success = true;
        try {
            EclipseSelenium selenium = new EclipseSelenium();
            selenium.start();
            selenium.open ("http://localhost:8080/SampleWAR/");
            selenium.type ("name", "Tom");
            selenium.click ("submit");
            selenium.waitForPageToLoad ("3000");
            success = (selenium.getHtmlSource().indexOf ("says hello to") > 0);

            selenium.stop();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
