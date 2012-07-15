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
package org.apache.geronimo.st.v30.ui.sections.blueprint;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.osgi.blueprint.Tactivation;
import org.apache.geronimo.osgi.blueprint.Tavailability;
import org.apache.geronimo.st.v30.ui.BlueprintEditorUIHelper;
import org.apache.geronimo.st.v30.ui.CommonMessages;
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
public class BlueprintGeneralSection extends AbstractBlueprintSectionPart {

    protected Text defaultActiviation;

    protected Text defaultTimeout;

    protected Text defaultAvailability;


    public BlueprintGeneralSection(Composite parent, FormToolkit toolkit, int style, JAXBElement bluerpint) {
        super(parent, toolkit, style, bluerpint);
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

        createLabel(composite, CommonMessages.defaultActivation);

        
        defaultActiviation = toolkit.createText(composite, getDefaultActiviation(), SWT.BORDER);
        defaultActiviation.setLayoutData(createTextFieldGridData());
        defaultActiviation.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                BlueprintEditorUIHelper.getBlueprint(getRootElement()).setDefaultActivation(Tactivation.fromValue(defaultActiviation.getText()));
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.defaultTimeout);

        defaultTimeout = toolkit.createText(composite, getDefaultTimeout(), SWT.BORDER);
        defaultTimeout.setLayoutData(createTextFieldGridData());
        defaultTimeout.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                BlueprintEditorUIHelper.getBlueprint(getRootElement()).setDefaultTimeout(BigInteger.valueOf(Long.valueOf(defaultTimeout.getText())));
                markDirty();
            }
        });

        createLabel(composite, CommonMessages.defaultAvailability);

        defaultAvailability = toolkit.createText(composite, getDefaultAvailability(), SWT.BORDER);
        defaultAvailability.setLayoutData(createTextFieldGridData());
        defaultAvailability.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                BlueprintEditorUIHelper.getBlueprint(getRootElement()).setDefaultAvailability(Tavailability.fromValue(defaultAvailability.getText()));
                markDirty();
            }
        });

    }
    
   

    private String getDefaultAvailability() {
        Tavailability defaultAvailability = BlueprintEditorUIHelper.getBlueprint(getRootElement()).getDefaultAvailability();
        String value; 
        if (defaultAvailability==null) value = "";
        else value = defaultAvailability.value();
        return value;
    }

    private String getDefaultTimeout() {
        BigInteger defaultTimeout = BlueprintEditorUIHelper.getBlueprint(getRootElement()).getDefaultTimeout();
        String value; 
        if (defaultTimeout==null) value = "";
        else value = String.valueOf(defaultTimeout.longValue());
        return value;
    }

    private String getDefaultActiviation() {
        Tactivation defaultActivation = BlueprintEditorUIHelper.getBlueprint(getRootElement()).getDefaultActivation();
        String value; 
        if (defaultActivation==null) value = "";
        else value = defaultActivation.value();
        return value;
    }

    protected String getSectionGeneralTitle() {
        return CommonMessages.blueprintAttributeSectionTitle;
    }

    protected String getSectionGeneralDescription() {
        return CommonMessages.blueprintAttributeSectionDescription;
    }
    
   
    
    
}
