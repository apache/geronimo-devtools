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

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.osgi.blueprint.Tdescription;
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
public class BlueprintDescriptionSection extends AbstractBlueprintSectionPart {
    
    protected Text blueprintDescription;

    public BlueprintDescriptionSection(Composite parent, FormToolkit toolkit, int style, JAXBElement bluerpint) {
        super(parent, toolkit, style, bluerpint);
        
        createClient();
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.wst.server.ui.editor.ServerEditorSection#createSection(org.eclipse.swt.widgets.Composite)
     */
    public void createClient() {
        Section section = getSection();

        section.setText(getSectionDescriptionTitle());
        section.setDescription(getSectionDescriptionDescription());
        section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

        Composite composite = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 5;
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        layout.horizontalSpacing = 15;
        composite.setLayout(layout);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        section.setClient(composite);
   
       
        blueprintDescription = toolkit.createText(composite, "", SWT.BORDER|SWT.WRAP|SWT.MULTI);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 75;
        gd.widthHint = 100;
        blueprintDescription.setLayoutData(gd);
        blueprintDescription.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Tdescription description = new Tdescription();
                List<String> descriptionContent = description.getContent();
                descriptionContent.clear();
                descriptionContent.add(blueprintDescription.getText());
                BlueprintEditorUIHelper.getBlueprint(getRootElement()).setDescription(description);
                markDirty();
            }
        });
    }
    
    private String getSectionDescriptionDescription() {
        return CommonMessages.blueprintDescriptionSectionTitle;
    }

    private String getSectionDescriptionTitle() {
        return CommonMessages.blueprintDescriptionSectionDescription;
    }

    
}
