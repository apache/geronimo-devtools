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

import org.apache.geronimo.jee.openejb.OpenejbJar;
import org.apache.geronimo.jee.openejb.ObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class OpenEjbJarGeneralSection extends CommonGeneralSection {

    protected Text ejbqlCompilerFactory;

    protected Text dbSyntaxFactory;

    protected Button enforceForeignKeyConstraints;
    
    OpenejbJar plan;

    public OpenEjbJarGeneralSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style, plan);
        this.plan = (OpenejbJar) plan.getValue();
        createClient();
    }

    protected void createClient() {
        super.createClient();
        Composite composite = (Composite) getSection().getClient();

        createLabel(composite, CommonMessages.editorejbqlCompilerFactory);

        ejbqlCompilerFactory = toolkit.createText(composite, plan.getEjbQlCompilerFactory(), SWT.BORDER);
        ejbqlCompilerFactory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        ejbqlCompilerFactory.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                plan.setEjbQlCompilerFactory(ejbqlCompilerFactory.getText());
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.editordbSyntaxFactory);

        dbSyntaxFactory = toolkit.createText(composite, plan.getDbSyntaxFactory(), SWT.BORDER);
        dbSyntaxFactory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        dbSyntaxFactory.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                plan.setDbSyntaxFactory(dbSyntaxFactory.getText());
                markDirty();
            }
        });

        enforceForeignKeyConstraints = toolkit.createButton(composite, CommonMessages.enforceForeignKeyConstraints, SWT.CHECK);
        enforceForeignKeyConstraints.setSelection(plan.getEnforceForeignKeyConstraints() != null);
        GridData data = new GridData();
        data.horizontalSpan = 2;
        enforceForeignKeyConstraints.setLayoutData(data);

        enforceForeignKeyConstraints.addSelectionListener(new SelectionListener() {
            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
            	if (enforceForeignKeyConstraints.getSelection()) {
            		ObjectFactory objFactory = new ObjectFactory();
            		plan.setEnforceForeignKeyConstraints(objFactory.createEmpty());
            	}
            	else {
            		plan.setEnforceForeignKeyConstraints(null);
            	}
                markDirty();
            }
        });
    }
}
