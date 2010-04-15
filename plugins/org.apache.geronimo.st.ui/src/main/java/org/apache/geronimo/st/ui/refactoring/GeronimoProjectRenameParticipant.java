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
package org.apache.geronimo.st.ui.refactoring;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jst.j2ee.project.JavaEEProjectUtilities;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class GeronimoProjectRenameParticipant extends RenameParticipant {
	protected IProject project;
	protected IFile deploymentPlanFile;
	protected String deploymentPlanName;

	public RefactoringStatus checkConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	protected boolean initialize(Object element) {
		// if not a project, then stop this refactoring
		if (!(element instanceof IProject)) {
			return false;
		}

		project = (IProject) element;

		// if not a javaee object, then stop this refactoring
		if (JavaEEProjectUtilities.isDynamicWebProject(project)) {
			deploymentPlanName = "WEB-INF/geronimo-web.xml";
		} else if (JavaEEProjectUtilities.isEJBProject(project)) {
			deploymentPlanName = "META-INF/openejb-jar.xml";
		} else if (JavaEEProjectUtilities.isJCAProject(project)) {
			deploymentPlanName = "META-INF/geronimo-ra.xml";
		} else if (JavaEEProjectUtilities.isEARProject(project)) {
			deploymentPlanName = "META-INF/geronimo-application.xml";
		} else {
			return false;
		}

		// sure that project is DynamicWebProject
		deploymentPlanFile = getDeploymentPlanFile(project);

		// if no deployment plan file, then stop this refactoring
		if (deploymentPlanFile == null) {
			return false;
		}

		return true;
	}

	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		CompositeChange result = new CompositeChange(getName());
		try {
			pm
					.beginTask(
							"beging create change for context-root and artifactId",
							100);
			String oldName = project.getName();
			String underProjectFilePath = getDeploymentPlanFileUnderProjectPath(project);
			String projectRelativeFilePath = deploymentPlanFile.getFullPath()
					.toString();
			String absoluteWorkspacePath = project.getParent().getLocation()
					.toString();
			String absoluteFilePath = absoluteWorkspacePath
					+ projectRelativeFilePath;

			DeploymentPlanEditHelper editHelper = new DeploymentPlanEditHelper(
					new DeploymentPlanHandler(absoluteFilePath));

			// Before this change being applied, the project has been
			// renamed. So, use a MovedTextFileChange instead of TextFileChange
			String newName = this.getArguments().getNewName();
			IProject newProject = ((IWorkspaceRoot) deploymentPlanFile
					.getProject().getParent()).getProject(newName);
			IFile newFile = newProject.getFile(underProjectFilePath);

			// if a web project, should create a context-root change if any
			if (JavaEEProjectUtilities.isDynamicWebProject(project)) {
				// create change for context-root
				String oldName1 = editHelper
						.getNodeValue(DeploymentPlanTextNode.CONTEXT_ROOT);
				int offset1 = editHelper
						.getNodeOffset(DeploymentPlanTextNode.CONTEXT_ROOT);
				if (offset1 != -1 && oldName1 != null
						&& oldName1.substring(1).equals(oldName)) {
					MovedTextFileChange change1 = new MovedTextFileChange(
							"Rename artifactId's value", newFile,
							deploymentPlanFile);
					MultiTextEdit rootEdit1 = new MultiTextEdit();
					ReplaceEdit edit1 = new ReplaceEdit(offset1, oldName1
							.length(), "/" + newName);
					rootEdit1.addChild(edit1);
					change1.setEdit(rootEdit1);
					result.add(change1);
				}
			}

			// create change for artifactId
			String oldName2 = editHelper
					.getNodeValue(DeploymentPlanTextNode.ARTIFACT_ID);
			int offset2 = editHelper
					.getNodeOffset(DeploymentPlanTextNode.ARTIFACT_ID);
			if (offset2 != -1 && oldName2 != null && oldName2.equals(oldName)) {
				MovedTextFileChange change2 = new MovedTextFileChange(
						"Rename context-root's value", newFile,
						deploymentPlanFile);
				MultiTextEdit rootEdit2 = new MultiTextEdit();
				ReplaceEdit edit2 = new ReplaceEdit(offset2, oldName2.length(),
						newName);
				rootEdit2.addChild(edit2);
				change2.setEdit(rootEdit2);
				result.add(change2);
			}

		} finally {
			pm.done();
		}
		if (result.getChildren().length == 0)
			return null;

		return result;
	}

	public String getName() {
		return "Rename project name related info in geronimo deployment plan";
	}

	protected IFile getDeploymentPlanFile(IProject project) {
		IVirtualComponent comp = ComponentCore.createComponent(project);
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
				.getProjectRelativePath().append(deploymentPlanName);
		return project.getFile(deployPlanPath);
	}

	protected String getDeploymentPlanFileUnderProjectPath(IProject project) {
		IVirtualComponent comp = ComponentCore.createComponent(project);
		IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
				.getProjectRelativePath().append(deploymentPlanName);
		return deployPlanPath.toString();
	}
}
