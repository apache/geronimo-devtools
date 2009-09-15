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
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

public class GeronimoWebProjectRenameParticipant extends
	GeronimoProjectRenameParticipant {
    private IFile geronimoWebDeploymentPlanFile;

    @Override
    protected boolean initialize(Object element) {
	// if not a project, then stop this refactoring
	if (!(element instanceof IProject)) {
	    return false;
	}

	project = (IProject) element;

	// if not a DynamicWebProject,then stop this refactoring
	if (!JavaEEProjectUtilities.isDynamicWebProject(project)) {
	    return false;
	}

	// sure that project is DynamicWebProject
	geronimoWebDeploymentPlanFile = getWebDeploymentPlanFile(project);

	// if no geronimo-web.xml, then stop this refactoring
	if (geronimoWebDeploymentPlanFile == null) {
	    return false;
	}

	return true;
    }

    @Override
    public Change createChange(IProgressMonitor pm) throws CoreException,
	    OperationCanceledException {
	CompositeChange result = new CompositeChange(getName());
	try {
	    pm
		    .beginTask(
			    "beging create change for context-root and artifactId",
			    100);
	    String oldName=project.getName();
	    String underProjectFilePath = getWebDeploymentPlanFileUnderProjectPath(project);
	    String projectRelativeFilePath = geronimoWebDeploymentPlanFile
		    .getFullPath().toString();
	    String absoluteWorkspacePath = project.getParent().getLocation()
		    .toString();
	    String absoluteFilePath = absoluteWorkspacePath
		    + projectRelativeFilePath;

	    WebDeploymentPlanEditHelper editHelper = new WebDeploymentPlanEditHelper(
		    absoluteFilePath);

	    // Before this change being applied, the project has been
	    // renamed. So, use a MovedTextFileChange instead of TextFileChange
	    String newName = this.getArguments().getNewName();
	    IProject newProject = ((IWorkspaceRoot) geronimoWebDeploymentPlanFile
		    .getProject().getParent()).getProject(newName);
	    IFile newFile = newProject.getFile(underProjectFilePath);

	    // create change for context-root
	    String oldName1 = editHelper.getContextRootValue();
	    int offset1 = editHelper.getContextRootOffset();
	    if (offset1 != -1 && oldName1.substring(1).equals(oldName)) {
		MovedTextFileChange change1 = new MovedTextFileChange(
			"Rename artifact-id's value", newFile,
			geronimoWebDeploymentPlanFile);
		MultiTextEdit rootEdit1 = new MultiTextEdit();
		ReplaceEdit edit1 = new ReplaceEdit(offset1, oldName1.length(),
			"/" + newName);
		rootEdit1.addChild(edit1);
		change1.setEdit(rootEdit1);
		result.add(change1);
	    }
	    // create change for artifactId
	    String oldName2 = editHelper.getArtifactIdValue();
	    int offset2 = editHelper.getArtifactIdOffset();
	    if (offset2 != -1 && oldName2.equals(oldName)) {
		MovedTextFileChange change2 = new MovedTextFileChange(
			"Rename context-root's value", newFile,
			geronimoWebDeploymentPlanFile);
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
	if(result.getChildren().length==0)
	    return null;
	return result;
    }

    @Override
    public String getName() {
	return "Rename artifact-id and context-root in geronimo-web.xml";
    }

    private IFile getWebDeploymentPlanFile(IProject project) {
	IVirtualComponent comp = ComponentCore.createComponent(project);
	IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
		.getProjectRelativePath().append("WEB-INF").append(
			"geronimo-web.xml");
	return project.getFile(deployPlanPath);
    }

    private String getWebDeploymentPlanFileUnderProjectPath(IProject project) {
	IVirtualComponent comp = ComponentCore.createComponent(project);
	IPath deployPlanPath = comp.getRootFolder().getUnderlyingFolder()
		.getProjectRelativePath().append("WEB-INF").append(
			"geronimo-web.xml");
	return deployPlanPath.toString();
    }
}
