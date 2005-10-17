/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.ui.sections;

import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.xml.ns.web.WebAppType;
import org.apache.geronimo.xml.ns.web.WebPackage;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class WebGeneralSection extends CommonGeneralSection {

    protected Text contextRoot;

    protected Text securityRealm;

    protected Button cpClassLoaderWebAppFirst;

    protected Button cpClassLoaderServerFirst;

    WebAppType plan;

    public WebGeneralSection(Composite parent, FormToolkit toolkit, int style,
            EObject plan) {
        super(parent, toolkit, style, plan);
        this.plan = (WebAppType) plan;
        createClient();
    }   

    protected void createClient() {
        
        super.createClient();
        
        Composite composite = (Composite) getSection().getClient();

        // ------- Label and text field for the context root -------
        createLabel(composite, Messages.editorContextRoot, toolkit);

        contextRoot = toolkit.createText(composite, plan.getContextRoot(),
                SWT.BORDER);
        contextRoot.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false));
        contextRoot.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                plan.setContextRoot(contextRoot.getText());
                markDirty();
            }
        });

        // ------- Label and text field for the security realm -------
        createLabel(composite, Messages.securityRealmName, toolkit);

        securityRealm = toolkit.createText(composite, plan
                .getSecurityRealmName(), SWT.BORDER);
        securityRealm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
                false));
        securityRealm.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                plan.setSecurityRealmName(securityRealm.getText());
                markDirty();
            }
        });

        // ------- Label and check box for the Context Priority Classloader
        // -------
        Label classLoaderLabel = toolkit.createLabel(composite,
                Messages.editorClassloader);
        GridData data = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        data.horizontalSpan = 2;
        classLoaderLabel.setLayoutData(data);

        cpClassLoaderServerFirst = toolkit.createButton(composite,
                Messages.editorClassloaderServer, SWT.RADIO);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false);
        data.horizontalSpan = 2;
        cpClassLoaderServerFirst.setLayoutData(data);
        cpClassLoaderServerFirst.setSelection(!plan
                .isContextPriorityClassloader());

        cpClassLoaderWebAppFirst = toolkit.createButton(composite,
                Messages.editorClassloaderWebApp, SWT.RADIO);
        data = new GridData(SWT.FILL, SWT.CENTER, false, false);
        data.horizontalSpan = 2;
        cpClassLoaderWebAppFirst.setLayoutData(data);
        cpClassLoaderWebAppFirst.setSelection(plan
                .isContextPriorityClassloader());

        cpClassLoaderWebAppFirst.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent e) {
                plan.setContextPriorityClassloader(cpClassLoaderWebAppFirst
                        .getSelection());
                markDirty();
            }

            public void widgetDefaultSelected(SelectionEvent e) {
                // Not invoked
            }
        });

    }

    /* (non-Javadoc)
     * @see org.apache.geronimo.ui.sections.CommonGeneralSection#getConfigIdEAttribute()
     */
    protected EAttribute getConfigIdEAttribute() {
        return WebPackage.eINSTANCE.getWebAppType_ConfigId();
    }

    /* (non-Javadoc)
     * @see org.apache.geronimo.ui.sections.CommonGeneralSection#getParentIdEAttribute()
     */
    protected EAttribute getParentIdEAttribute() {
        return WebPackage.eINSTANCE.getWebAppType_ParentId();
    }
}
