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
package org.apache.geronimo.st.v21.ui.pages;

import org.apache.geronimo.jee.security.Security;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.pages.AbstractGeronimoFormPage;
import org.apache.geronimo.st.v21.core.jaxb.JAXBModelUtils;
import org.apache.geronimo.st.v21.ui.sections.SecurityRootSection;
import org.apache.geronimo.st.v21.ui.sections.SecuritySection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

public class SecurityPage extends AbstractGeronimoFormPage {

    public SecurityPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#fillBody(org.eclipse.ui.forms.IManagedForm)
     */
    protected void fillBody(IManagedForm managedForm) {
        Security security = (Security)JAXBModelUtils.getSecurity(getDeploymentPlan());
        managedForm.addPart(new SecurityRootSection(body, toolkit, getStyle(), getDeploymentPlan(), security));
        managedForm.addPart(new SecuritySection(getDeploymentPlan(), body, toolkit, getStyle(), security.getRoleMappings()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#getLayout()
     */
    protected GridLayout getLayout() {
        GridLayout layout = super.getLayout();
        layout.numColumns = 1;
        return layout;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#getFormTitle()
     */
    public String getFormTitle() {
        return CommonMessages.securityPageTitle;
    }

}
