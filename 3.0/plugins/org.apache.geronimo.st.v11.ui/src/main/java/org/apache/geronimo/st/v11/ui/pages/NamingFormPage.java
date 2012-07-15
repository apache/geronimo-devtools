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
package org.apache.geronimo.st.v11.ui.pages;

import org.apache.geronimo.j2ee.applicationclient.ApplicationClientType;
import org.apache.geronimo.j2ee.openejb_jar.OpenejbJarType;
import org.apache.geronimo.j2ee.web.WebAppType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.editors.AbstractGeronimoJAXBBasedEditor;
import org.apache.geronimo.st.ui.pages.AbstractGeronimoFormPage;
import org.apache.geronimo.st.v11.core.GeronimoServerInfo;
import org.apache.geronimo.st.v11.ui.sections.EjbLocalRefSection;
import org.apache.geronimo.st.v11.ui.sections.EjbRefSection;
import org.apache.geronimo.st.v11.ui.sections.EjbRelationSection;
import org.apache.geronimo.st.v11.ui.sections.GBeanRefSection;
import org.apache.geronimo.st.v11.ui.sections.MessageDestSection;
import org.apache.geronimo.st.v11.ui.sections.ResourceEnvRefSection;
import org.apache.geronimo.st.v11.ui.sections.ResourceRefSection;
import org.apache.geronimo.st.v11.ui.sections.ServiceRefSection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * @version $Rev$ $Date$
 */
public class NamingFormPage extends AbstractGeronimoFormPage {

    public NamingFormPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#fillBody(org.eclipse.ui.forms.IManagedForm)
     */
    protected void fillBody(IManagedForm managedForm) {
        if (WebAppType.class.isInstance (getDeploymentPlan().getValue())) {
            WebAppType webapp = (WebAppType)((AbstractGeronimoJAXBBasedEditor) getEditor()).getRootElement().getValue();
            managedForm.addPart(new EjbRefSection(getDeploymentPlan(), body, toolkit, getStyle(), webapp.getEjbRef()));
            managedForm.addPart(new ResourceRefSection(getDeploymentPlan(), body, toolkit, getStyle(), webapp.getResourceRef()));
            managedForm.addPart(new ServiceRefSection(getDeploymentPlan(), body, toolkit, getStyle(), webapp.getServiceRef()));
            managedForm.addPart(new ResourceEnvRefSection(getDeploymentPlan(), body, toolkit, getStyle(), webapp.getResourceEnvRef()));
            managedForm.addPart(new EjbLocalRefSection(getDeploymentPlan(), body, toolkit, getStyle(), webapp.getEjbLocalRef()));
            managedForm.addPart(new GBeanRefSection(getDeploymentPlan(), body, toolkit, getStyle(), webapp.getGbeanRef()));
            managedForm.addPart(new MessageDestSection(getDeploymentPlan(), body, toolkit, getStyle(), webapp.getMessageDestination()));
        }
        else if (ApplicationClientType.class.isInstance (getDeploymentPlan().getValue())){
        	ApplicationClientType appClient = (ApplicationClientType)((AbstractGeronimoJAXBBasedEditor) getEditor()).getRootElement().getValue();
            managedForm.addPart(new EjbRefSection(getDeploymentPlan(), body, toolkit, getStyle(), appClient.getEjbRef()));
            managedForm.addPart(new ResourceRefSection(getDeploymentPlan(), body, toolkit, getStyle(), appClient.getResourceRef()));
            managedForm.addPart(new ServiceRefSection(getDeploymentPlan(), body, toolkit, getStyle(), appClient.getServiceRef()));
            managedForm.addPart(new ResourceEnvRefSection(getDeploymentPlan(), body, toolkit, getStyle(), appClient.getResourceEnvRef()));
            managedForm.addPart(new GBeanRefSection(getDeploymentPlan(), body, toolkit, getStyle(), appClient.getGbeanRef()));
            managedForm.addPart(new MessageDestSection(getDeploymentPlan(), body, toolkit, getStyle(), appClient.getMessageDestination()));
        }
        else if (OpenejbJarType.class.isInstance (getDeploymentPlan().getValue())){
        	OpenejbJarType ejbJar = (OpenejbJarType)((AbstractGeronimoJAXBBasedEditor) getEditor()).getRootElement().getValue();
            managedForm.addPart(new EjbRelationSection(getDeploymentPlan(), body, toolkit, getStyle(), ejbJar.getRelationships()));
            managedForm.addPart(new MessageDestSection(getDeploymentPlan(), body, toolkit, getStyle(), ejbJar.getMessageDestination()));
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
        return CommonMessages.namingFormPageTitle;
    }

    @Override
    protected void triggerGeronimoServerInfoUpdate() {
        GeronimoServerInfo.getInstance().updateInfo();
    }
}
