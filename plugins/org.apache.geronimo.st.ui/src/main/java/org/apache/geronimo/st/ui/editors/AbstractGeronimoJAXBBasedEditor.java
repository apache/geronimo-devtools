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
package org.apache.geronimo.st.ui.editors;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.core.operations.ImportDeploymentPlanDataModelProvider;
import org.apache.geronimo.st.core.operations.ImportDeploymentPlanOperation;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.internal.Messages;
import org.apache.geronimo.st.ui.internal.Trace;
import org.apache.geronimo.st.ui.pages.AbstractGeronimoFormPage;
import org.apache.geronimo.st.ui.pages.DeploymentPlanSourcePage;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetProjectCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * @version $Rev: 939152 $ $Date: 2010-04-29 08:57:12 +0800 (Thu, 29 Apr 2010) $
 */
public abstract class AbstractGeronimoJAXBBasedEditor extends FormEditor {

    protected JAXBElement rootJAXBElement;

    public AbstractGeronimoJAXBBasedEditor() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void doSave(IProgressMonitor monitor) {
        InputStream is = null;
        try {
            IEditorInput input = getEditorInput();
            if (input instanceof IFileEditorInput) {
                IFileEditorInput fei = (IFileEditorInput) input;
                if (rootJAXBElement != null) {
                    saveFile(fei.getFile());
                    commitFormPages(true);
                }

                if (getActiveEditor() == null) {
                    editorDirtyStateChanged();
                } else {
                    getActiveEditor().doSave(monitor);
                    if (rootJAXBElement != null) {
//                        if (deploymentPlan.eResource() != null) {
//                            deploymentPlan.eResource().unload();
//                        }
                        // TODO not sure if this is the best way to refresh
                        // model
                        rootJAXBElement = loadFile(fei.getFile());
                    }
                }
            }
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Error saving", e, Activator.logEditors);
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error saving", e.getMessage());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, "Error saving", e, Activator.logEditors);
                MessageDialog.openError(Display.getCurrent().getActiveShell(), "Error saving", e.getMessage());
            }
        }
    }

//    private void saveEditors(IFile file) throws IOException, JAXBException {
//        JAXBContext jb = JAXBContext.newInstance( "org.apache.geronimo.xml.ns.j2ee.web_2_0:org.apache.geronimo.xml.ns.j2ee.application_2:org.apache.geronimo.xml.ns.deployment_1:org.apache.geronimo.xml.ns.naming_1", Activator.class.getClassLoader() );
//        jb.createMarshaller().marshal( deploymentPlan, new File( file.getLocationURI().toURL().getFile()) );
//        commitFormPages(true);
//    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    protected void addPages() {
        try {
            doAddPages();
        } catch (PartInitException e1) {
            e1.printStackTrace();
        }
    }

    abstract public void doAddPages() throws PartInitException;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    public final void doSaveAs() {
        // do nothing
    }

    protected void addSourcePage() throws PartInitException {
        DeploymentPlanSourcePage source = new DeploymentPlanSourcePage(this);
        int index = addPage(source, getEditorInput());
        setPageText(index, Messages.editorTabSource);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    public boolean isSaveAsAllowed() {
        return false;
    }

    public void commitFormPages(boolean onSave) {
        IFormPage[] pages = getPages();
        for (int i = 0; i < pages.length; i++) {
            IFormPage page = pages[i];
            IManagedForm mform = page.getManagedForm();
            if (mform != null && mform.isDirty())
                mform.commit(true);
        }
    }

    public IFormPage[] getPages() {
        ArrayList formPages = new ArrayList();
        for (int i = 0; i < pages.size(); i++) {
            Object page = pages.get(i);
            if (page instanceof IFormPage)
                formPages.add(page);
        }
        return (IFormPage[]) formPages.toArray(new IFormPage[formPages.size()]);
    }

    public JAXBElement getRootElement() {
        return rootJAXBElement;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite,
     *      org.eclipse.ui.IEditorInput)
     */
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        if (input instanceof IFileEditorInput) {
            IFileEditorInput fei = (IFileEditorInput) input;
            try {
                rootJAXBElement = loadFile(fei.getFile());
            } catch (Exception e) {
                // if catching an exception , it will try to correct the plan 
                // or open the plan with default editor
                Trace.trace(Trace.WARNING, "Error loading deployment plan", e, Activator.logEditors);
            }
            
            boolean fix = false;
            if(rootJAXBElement == null) {
                fix = MessageDialog.openQuestion(Display.getDefault().getActiveShell(), Messages.errorOpenDialog, Messages.editorCorrect);
            }
            
            if(fix) {
                boolean converted = false;
                
                IProject project = fei.getFile().getProject();
                IDataModel model = DataModelFactory.createDataModel(new ImportDeploymentPlanDataModelProvider());
                model.setProperty(IFacetDataModelProperties.FACET_PROJECT_NAME, project.getName());
                try {
                    IFacetedProject facetedProject = ProjectFacetsManager.create(project);
                    model.setProperty(IFacetProjectCreationDataModelProperties.FACET_RUNTIME, facetedProject.getPrimaryRuntime());
                    IDataModelOperation op = new ImportDeploymentPlanOperation(model, fei.getFile());
                    op.execute(new NullProgressMonitor(), null);
                    converted = true;
                } catch (Exception e) {
                    // conversion failed somehow
                    Trace.trace(Trace.WARNING, "Error converting deployment plan", e, Activator.logEditors);
                }
                
                if (converted) {
                    try {
                        rootJAXBElement = loadFile(fei.getFile());
                    } catch (Exception e) {
                        // still can't load it
                        Trace.trace(Trace.WARNING, "Error loading converted deployment plan", e, Activator.logEditors);
                    }
                }
                
                if (rootJAXBElement == null) {    
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), Messages.errorOpenDialog, Messages.editorDefault);
                }
            }
        }
    }

    public void reloadFile() throws Exception {
        IEditorInput input = getEditorInput();
        if (input instanceof IFileEditorInput) {
            IFileEditorInput fei = (IFileEditorInput) input;
            if (rootJAXBElement != null) {
                rootJAXBElement = loadFile(fei.getFile());
                IFormPage[] pages = getPages();
                for (int i = 0; i < pages.length; i++) {
                    IFormPage page = pages[i];
                    IManagedForm mform = page.getManagedForm();
                    if (mform != null) {
                        if (page instanceof AbstractGeronimoFormPage) {
                            AbstractGeronimoFormPage geronimoPage = (AbstractGeronimoFormPage)page;
                            geronimoPage.refresh();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void pageChange(int newPageIndex) {
        if (isDirty()) {
            IFormPage[] pages = getPages();
            IFormPage active = getActivePageInstance();
            int curr = getCurrentPage();
            if (getCurrentPage() == newPageIndex) {
                return;
            }
            // if the old or the new page are the source page, don't allow the page change 
            if (newPageIndex == pages.length || getCurrentPage() == pages.length) {
                setActivePage(curr);
                MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), Messages.savePageTitle, Messages.savePageMessage);
                return;
            }
        }
        super.pageChange(newPageIndex);
    }

    abstract public JAXBElement loadFile(IFile file) throws Exception;
    abstract public void saveFile(IFile file) throws Exception;

}
