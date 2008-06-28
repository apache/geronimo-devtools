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
package org.apache.geronimo.st.v21.ui.sections;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.jee.applicationclient.ApplicationClient;
import org.apache.geronimo.jee.deployment.Environment;
import org.apache.geronimo.st.ui.CommonMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class AppClientClientGeneralSection extends CommonGeneralSection {

    protected Text callbackHandler;
    protected Text realmName;
    ApplicationClient plan;

    public AppClientClientGeneralSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style, plan);
        this.plan = (ApplicationClient) plan.getValue();
        createClient();
    }

    protected void createClient() {
        super.createClient();
        Composite composite = (Composite) getSection().getClient();

        createLabel(composite, CommonMessages.editorCallbackHandler);

        callbackHandler = toolkit.createText(composite, plan.getCallbackHandler(), SWT.BORDER);
        callbackHandler.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        callbackHandler.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                plan.setCallbackHandler(callbackHandler.getText());
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.editorRealmName);

        realmName = toolkit.createText(composite, plan.getRealmName(), SWT.BORDER);
        realmName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        realmName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                plan.setRealmName(realmName.getText());
                markDirty();
            }
        });
    }

    protected Environment getEnvironment(boolean create) {
        Environment type = null;
        Object plan = getPlan().getValue();
        if (ApplicationClient.class.isInstance(plan)) {
            type = ((ApplicationClient) plan).getClientEnvironment();
            if (type == null && create) {
                type = getDeploymentObjectFactory().createEnvironment();
                ((ApplicationClient) plan).setClientEnvironment(type);
            }
        }

        return type;
    }

    protected String getSectionGeneralTitle() {
        return CommonMessages.editorSectionClientTitle;
    }

    protected String getSectionGeneralDescription() {
        return CommonMessages.editorSectionClientDescription;
    }
}
