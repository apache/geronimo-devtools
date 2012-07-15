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
package org.apache.geronimo.st.v30.ui.pages;

import org.apache.geronimo.st.v30.core.DeploymentDescriptorUtils;
import org.apache.geronimo.st.v30.core.GeronimoServerInfo;
import org.apache.geronimo.st.v30.core.descriptor.AbstractDeploymentDescriptor;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractDeploymentPlanFormPage extends AbstractGeronimoFormPage {

    
    AbstractDeploymentDescriptor deploymentDescriptor;

    /**
     * @param editor
     * @param id
     * @param title
     */
    public AbstractDeploymentPlanFormPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /**
     * @param id
     * @param title
     */
    public AbstractDeploymentPlanFormPage(String id, String title) {
        super(id, title);
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
        deploymentDescriptor = (AbstractDeploymentDescriptor) DeploymentDescriptorUtils
        .getDeploymentDescriptor(getProject());
        
        super.createFormContent(managedForm);
     
    }

    protected void triggerGeronimoServerInfoUpdate(){
        GeronimoServerInfo.getInstance().updateInfo();
    }

    protected String getHelpResource() {
        return "http://geronimo.apache.org/development-tools.html";
    }


    public AbstractDeploymentDescriptor getDeploymentDescriptor() {
        return deploymentDescriptor;
    }

}
