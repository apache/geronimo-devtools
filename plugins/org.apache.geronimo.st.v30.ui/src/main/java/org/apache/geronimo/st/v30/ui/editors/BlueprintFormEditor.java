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
package org.apache.geronimo.st.v30.ui.editors;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.ui.editors.AbstractGeronimoJAXBBasedEditor;
import org.apache.geronimo.st.v30.core.jaxb.JAXBUtils;
import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.apache.geronimo.st.v30.ui.pages.blueprint.BlueprintPage;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.PartInitException;

/**
 * @version $Rev$ $Date$
 */
public class BlueprintFormEditor extends AbstractGeronimoJAXBBasedEditor {
    
    private static String BLUEPRINT_NAME = "blueprint.xml";
    
    private JAXBElement blueprint ;

    @Override
    public void doAddPages() throws PartInitException {
       
        addFormPages();
        
        //add source page
        addSourcePage();
        
    }

    private void addFormPages() throws PartInitException {
      //add General page
      this.addPage(new BlueprintPage(this, "blueprintgeneralpage", CommonMessages.editorTabGeneral));
        
    }


    @Override
    public JAXBElement loadFile(IFile file) throws Exception {
        if (file.getName().equals(BLUEPRINT_NAME) && file.exists()) {
            blueprint =  JAXBUtils.unmarshalFilterDeploymentPlan(file);
            return blueprint;
        }else return null;
    }

    @Override
    public void saveFile(IFile file) throws Exception {
        JAXBUtils.marshalDeploymentPlan(blueprint, file);
        
    }
    
    

    
}
