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

import org.apache.geronimo.jee.application.Application;
import org.apache.geronimo.jee.applicationclient.ApplicationClient;
import org.apache.geronimo.jee.connector.Connector;
import org.apache.geronimo.st.ui.editors.AbstractGeronimoJAXBBasedEditor;
import org.apache.geronimo.st.v30.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.apache.geronimo.st.v30.ui.sections.AdminObjectSection;
import org.apache.geronimo.st.v30.ui.sections.ClassFilterSection;
import org.apache.geronimo.st.v30.ui.sections.DependencySection;
import org.apache.geronimo.st.v30.ui.sections.ExtModuleSection;
import org.apache.geronimo.st.v30.ui.sections.GBeanSection;
import org.apache.geronimo.st.v30.ui.sections.ModuleSection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/*
 * @version $Rev$ $Date$
 */
public class DeploymentPage extends AbstractDeploymentPlanFormPage {
    
    public DeploymentPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#fillBody(org.eclipse.ui.forms.IManagedForm)
     */
    protected void fillBody(IManagedForm managedForm) {
        managedForm.addPart(new DependencySection(getRootElement(), JAXBModelUtils.getEnvironment(getRootElement()), body, toolkit, getStyle()));
        managedForm.addPart(new GBeanSection(getRootElement(), JAXBModelUtils.getGbeans(getRootElement()), body, toolkit, getStyle()));
        managedForm.addPart(new ClassFilterSection(getRootElement(), JAXBModelUtils.getEnvironment(getRootElement()), body, toolkit, getStyle(), true, true));
        managedForm.addPart(new ClassFilterSection(getRootElement(), JAXBModelUtils.getEnvironment(getRootElement()), body, toolkit, getStyle(), true, false));

        if (Application.class.isInstance(getRootElement().getValue())) {
            Application application = (Application)((AbstractGeronimoJAXBBasedEditor) getEditor()).getRootElement().getValue();
            managedForm.addPart(new ModuleSection(getRootElement(), body, toolkit, getStyle(), application.getModule()));
            managedForm.addPart(new ExtModuleSection(getRootElement(), body, toolkit, getStyle(), application.getExtModule()));
        } 
        if (Connector.class.isInstance(getRootElement().getValue())) {
            Connector connector = (Connector)((AbstractGeronimoJAXBBasedEditor) getEditor()).getRootElement().getValue();
            managedForm.addPart(new AdminObjectSection(getRootElement(), body, toolkit, getStyle(), connector.getAdminobject()));
        }
        if (ApplicationClient.class.isInstance(getRootElement().getValue())) {
            managedForm.addPart(new DependencySection(getRootElement(), JAXBModelUtils.getEnvironment(getRootElement(), false), body, toolkit, getStyle(), false));
            managedForm.addPart(new ClassFilterSection(getRootElement(), JAXBModelUtils.getEnvironment(getRootElement(), false), body, toolkit, getStyle(), false, true));
            managedForm.addPart(new ClassFilterSection(getRootElement(), JAXBModelUtils.getEnvironment(getRootElement(), false), body, toolkit, getStyle(), false, false));
        }
    }
    
    protected GridLayout getLayout() {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.horizontalSpacing = 20;
        return layout;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#getFormTitle()
     */
    public String getFormTitle() {
        return CommonMessages.deploymentPageTitle;
    }


}
