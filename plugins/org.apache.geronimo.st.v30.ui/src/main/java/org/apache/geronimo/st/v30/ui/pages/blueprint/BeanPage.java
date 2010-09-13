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
package org.apache.geronimo.st.v30.ui.pages.blueprint;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.osgi.blueprint.Tblueprint;
import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.apache.geronimo.st.v30.ui.sections.blueprint.BlueprintDescriptionSection;
import org.apache.geronimo.st.v30.ui.sections.blueprint.BlueprintGeneralSection;
import org.apache.geronimo.st.v30.ui.sections.blueprint.TypeConverterSection;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * @version $Rev: 939152 $ $Date: 2010-04-29 08:57:12 +0800 (Thu, 29 Apr 2010) $
 */
public class BeanPage extends AbstractBlueprintFormPage {

    public BeanPage(FormEditor editor, String id, String title) {
        super(editor, id, title);
    }

    @Override
    protected void triggerGeronimoServerInfoUpdate() {
         //do nothing
    }

    @Override
    protected void fillBody(IManagedForm managedForm) {
        JAXBElement blueprint = getRootElement();
        if (blueprint!=null && Tblueprint.class.isInstance(blueprint.getValue()))        
            managedForm.addPart(new BlueprintGeneralSection(body, toolkit, getStyle(), blueprint));
            managedForm.addPart(new BlueprintDescriptionSection(body, toolkit, getStyle(), blueprint));
            managedForm.addPart(new TypeConverterSection(body, toolkit, getStyle(),blueprint));
       
    }
    
    public String getFormTitle() {
        return CommonMessages.blueprintGeneralPageTitle;
    }


}
