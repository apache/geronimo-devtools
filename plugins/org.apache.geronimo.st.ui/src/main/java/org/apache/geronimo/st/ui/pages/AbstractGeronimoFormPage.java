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
package org.apache.geronimo.st.ui.pages;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.core.DeploymentDescriptorUtils;
import org.apache.geronimo.st.core.descriptor.AbstractDeploymentDescriptor;
import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.editors.AbstractGeronimoJAXBBasedEditor;
import org.apache.geronimo.st.ui.editors.Refreshable;
import org.apache.geronimo.st.ui.editors.SharedDeploymentPlanEditor;
import org.apache.geronimo.st.ui.internal.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractGeronimoFormPage extends FormPage implements Refreshable {

    JAXBElement deploymentPlan;
    
    AbstractDeploymentDescriptor deploymentDescriptor;

    //server runtime version
    String runtimeVersion ;

    protected FormToolkit toolkit;

    protected Composite body;

    /**
     * @param editor
     * @param id
     * @param title
     */
    public AbstractGeronimoFormPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
        runtimeVersion = ((SharedDeploymentPlanEditor)editor).getRuntimeVersion();
    }

    @Override
    public void refresh() {
        // clear the old composite and tool bar
        Control[] controls = body.getChildren();
        for (int i = 0; i < controls.length; i++) {
            controls[i].dispose();
        }
        getManagedForm().getForm().getToolBarManager().removeAll();
        createFormContent(getManagedForm());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    protected void createFormContent(IManagedForm managedForm) {
        deploymentPlan = ((AbstractGeronimoJAXBBasedEditor) getEditor()).getRootElement();
        deploymentDescriptor = (AbstractDeploymentDescriptor) DeploymentDescriptorUtils
                .getDeploymentDescriptor(getProject());
        body = managedForm.getForm().getBody();
        toolkit = managedForm.getToolkit();
        final ScrolledForm form = managedForm.getForm();
        form.setText(getFormTitle());
        // managedForm.addPart(new BannerPart(form.getBody(), toolkit,
        // SWT.NONE));
        form.getBody().setLayout(getLayout());
        fillBody(managedForm);

        // header with help button
        toolkit.decorateFormHeading(form.getForm());
        IToolBarManager manager = form.getToolBarManager();

        Action serverInfoRefresh = new Action(Messages.formServerInfo) {
            public void run() {
                BusyIndicator.showWhile(form.getDisplay(), new Runnable() {
                    public void run() {
                        triggerGeronimoServerInfoUpdate();
                    }
                });
            }
        };
        serverInfoRefresh.setToolTipText(Messages.triggerUpdateServerInfo);
        serverInfoRefresh.setImageDescriptor(Activator.imageDescriptorFromPlugin("org.apache.geronimo.st.ui",
                "icons/obj16/update.gif"));
        manager.add(serverInfoRefresh);

        Action helpAction = new Action(Messages.formHelp) {
            public void run() {
                BusyIndicator.showWhile(form.getDisplay(), new Runnable() {
                    public void run() {
                        PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(getHelpResource());
                    }
                });
            }
        };
        helpAction.setToolTipText(Messages.formHelp);
        helpAction.setImageDescriptor(Activator.imageDescriptorFromPlugin("org.apache.geronimo.st.ui",
                "icons/obj16/help.gif"));
        manager.add(helpAction);

        manager.update(true);
        form.reflow(true);
    }

    protected abstract void triggerGeronimoServerInfoUpdate();

    protected String getHelpResource() {
        return "http://geronimo.apache.org/development-tools.html";
    }

    protected GridLayout getLayout() {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.horizontalSpacing = 20;
        layout.makeColumnsEqualWidth = true;
        return layout;
    }

    protected int getStyle() {
        return ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
                | ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
                | ExpandableComposite.FOCUS_TITLE;
    }

    abstract protected void fillBody(IManagedForm managedForm);

    public JAXBElement getDeploymentPlan() {
        return deploymentPlan;
    }

    protected IProject getProject() {
        IEditorInput editorInput = getEditorInput();
        if (editorInput instanceof IFileEditorInput) {
            return ((IFileEditorInput) editorInput).getFile().getProject();
        }
        return null;
    }

    public AbstractDeploymentDescriptor getDeploymentDescriptor() {
        return deploymentDescriptor;
    }

    public String getFormTitle() {
        return getTitle();
    }
    
    protected String getRuntimeVersion(){
        return runtimeVersion;
    }
}
