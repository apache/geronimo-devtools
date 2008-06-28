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
import org.apache.geronimo.jee.security.Description;
import org.apache.geronimo.jee.security.ObjectFactory;
import org.apache.geronimo.jee.security.SubjectInfo;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractSectionPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public class AppClientSecuritySection extends AbstractSectionPart {

    protected Text realm;

    protected Text subjectId;

    protected Text description;
    
	ObjectFactory securityFactory;
    
    public AppClientSecuritySection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
        super(parent, toolkit, style, plan);
        securityFactory = new ObjectFactory();
        createClient();
    }

    protected void createClient() {

        Section section = getSection();

        section.setText(getSectionGeneralTitle());
        section.setDescription(getSectionGeneralDescription());
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);

        createLabel(composite, CommonMessages.editorSubjectId);

        subjectId = toolkit.createText(composite, getDefaultSubject().getId(), SWT.BORDER);
        subjectId.setLayoutData(createTextFieldGridData());
        subjectId.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getDefaultSubject().setId(subjectId.getText());
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.editorRealmName);

        realm = toolkit.createText(composite, getDefaultSubject().getRealm(), SWT.BORDER);
        realm.setLayoutData(createTextFieldGridData());
        realm.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                getDefaultSubject().setRealm(realm.getText());
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.description);

        if (getDefaultSubject().getDescription().size() > 0)
            description = toolkit.createText(composite, getDefaultSubject().getDescription().get(0).getValue(), SWT.BORDER);
        else
            description = toolkit.createText(composite, "", SWT.BORDER);
        description.setLayoutData(createTextFieldGridData());
        description.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
            	Description descText = securityFactory.createDescription();
            	descText.setValue(description.getText());
            	getDefaultSubject().getDescription().clear();
                getDefaultSubject().getDescription().add(descText);
                markDirty();
            }
        });
    }

    private SubjectInfo getDefaultSubject () {
    	ApplicationClient client = (ApplicationClient)getPlan().getValue();
    	SubjectInfo subjectInfo = client.getDefaultSubject();
    	if (subjectInfo == null) {
    	    subjectInfo = securityFactory.createSubjectInfo();
    	    client.setDefaultSubject(subjectInfo);
    	}
    	return subjectInfo;
    }

    protected String getSectionGeneralTitle() {
        return CommonMessages.editorSectionSecurityTitle;
    }

    protected String getSectionGeneralDescription() {
        return CommonMessages.editorSectionSecurityDescription;
    }
}
