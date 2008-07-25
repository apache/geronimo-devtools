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

package org.apache.geronimo.testsuite.common.ui;

import java.io.FileInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;

import abbot.swt.finder.generic.MultipleFoundException;
import abbot.swt.finder.generic.NotFoundException;

public class ProjectTasks {
    Shell workbenchShell;
    AbbotHelper aHelper;
    String serverVersion;

    public ProjectTasks (Shell shell, AbbotHelper helper, String serverVersion) {
        workbenchShell = shell;
        aHelper = helper;
        this.serverVersion = serverVersion;
    }
    
    public void createProjects() throws Exception {
        createEARProject ();
        createEJBProject ();
        createWARProject ();
        copyCode ();
    }

    private void createEARProject() throws MultipleFoundException, NotFoundException {
        Shell wizardShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                "New");

        aHelper.clickTreeItem (wizardShell,
                new String[] {"Java EE", "Enterprise Application Project"});
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.setTextField(wizardShell, "", "SampleEAR");
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, "Generate Deployment Descriptor");
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.setTextField(wizardShell, "default", "sampleear");
        aHelper.setTextField(wizardShell, "", "sample-ear");
        aHelper.setTextField(wizardShell, "car", "ear");
        aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);

        aHelper.waitForDialogDisposal (wizardShell);
    }

    private void createEJBProject() throws MultipleFoundException, NotFoundException {
        Shell wizardShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                "New");

        aHelper.clickTreeItem (wizardShell,
                new String[] {"EJB", "EJB Project"});
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.setTextField(wizardShell, "", "SampleEJB");
        aHelper.clickButton (wizardShell, "Add project to an EAR");
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, "Create an EJB Client JAR module to hold the client interfaces and classes.");
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.setTextField(wizardShell, "default", "sampleear");
        aHelper.setTextField(wizardShell, "", "sample-ejb");
        aHelper.setTextField(wizardShell, "car", "ejb");
        aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
        
        aHelper.waitForDialogDisposal (wizardShell);
        
        aHelper.doubleClickItem(workbenchShell, "ejbModule");
        aHelper.doubleClickItem(workbenchShell, "META-INF");
        Shell questionShell = aHelper.rightClickItem(workbenchShell, "openejb-jar.xml",
                new String[] {"&Delete"}, "Delete Resources");
        aHelper.clickButton (questionShell, IDialogConstants.OK_LABEL);
        aHelper.waitForDialogDisposal (questionShell);
    }

    private void createWARProject() throws MultipleFoundException, NotFoundException {
        Shell wizardShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&File", "&New\tAlt+Shift+N", "&Other..."},
                "New");

        aHelper.clickTreeItem (wizardShell,
                new String[] {"Web", "Dynamic Web Project"});
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.setTextField(wizardShell, "", "SampleWAR");
        aHelper.clickButton (wizardShell, "Add project to an EAR");
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickButton (wizardShell, IDialogConstants.NEXT_LABEL);
        aHelper.setTextField(wizardShell, "default", "sampleear");
        aHelper.setTextField(wizardShell, "", "sample-war");
        aHelper.setTextField(wizardShell, "car", "war");
        aHelper.clickButton (wizardShell, IDialogConstants.FINISH_LABEL);
        
        aHelper.waitForDialogDisposal (wizardShell);
    }
    
    private void copyCode () throws Exception {
        aHelper.clickItem (workbenchShell, "SampleWAR");
        Shell propShell = aHelper.clickMenuItem (workbenchShell,
                new String[] {"&Project", "&Properties"},
                "Properties for SampleWAR");
        aHelper.clickItem (propShell, "Java EE Module Dependencies");
        aHelper.clickButton (propShell, "Select &All");
        aHelper.clickButton (propShell, "&Apply");
        aHelper.clickButton (propShell, IDialogConstants.OK_LABEL);

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
        Shell servletShell = aHelper.rightClickItem(workbenchShell, "SampleWAR",
                new String [] {"&New", "Servlet"}, "Create Servlet");
        aHelper.setCursorText("MyServlet");
        aHelper.setTextField(servletShell, "", "sampleear");
        aHelper.clickButton (servletShell, IDialogConstants.NEXT_LABEL);
        aHelper.clickItem(servletShell, "/MyServlet");
        Shell urlShell = aHelper.clickEnabledButton (servletShell, "Edit...", "URL Mappings");
        aHelper.setTextField(urlShell, "/MyServlet", "/sayHello");
        aHelper.clickButton (urlShell, IDialogConstants.OK_LABEL);
        aHelper.clickButton (servletShell, IDialogConstants.FINISH_LABEL);

        // copy over the correct contents
        aFile = aProject.getFile("src/sampleear/MyServlet.java");
        aFile.setContents(new FileInputStream (fileDir + "/MyServlet.java"), true, true, null);
    }
    
    public void deleteProject (String projectName) throws MultipleFoundException, NotFoundException {
        Shell questionShell = aHelper.rightClickItem (workbenchShell, projectName, 
                new String[] {"&Delete"}, "Delete Resources");

        // use .* to signify use of a wildcard
        aHelper.clickButton (questionShell, "&Delete project contents.*");
        aHelper.clickButton (questionShell, IDialogConstants.OK_LABEL);
        aHelper.waitForDialogDisposal (questionShell);
    }

}
