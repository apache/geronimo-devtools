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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.osgi.blueprint.Targument;
import org.apache.geronimo.osgi.blueprint.Tbean;
import org.apache.geronimo.osgi.blueprint.Tcomponent;
import org.apache.geronimo.osgi.blueprint.Tproperty;
import org.apache.geronimo.osgi.blueprint.Tref;
import org.apache.geronimo.osgi.blueprint.Treference;
import org.apache.geronimo.osgi.blueprint.TtypeConverters;
import org.apache.geronimo.st.v30.core.jaxb.BlueprintJAXBHelper;
import org.apache.geronimo.st.v30.ui.BlueprintEditorUIHelper;
import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.apache.geronimo.st.v30.ui.wizards.blueprint.BlueprintElementWizardProxy;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev: 939152 $ $Date: 2010-04-29 08:57:12 +0800 (Thu, 29 Apr 2010) $
 */
public class TypeConverterSection extends AbstractBlueprintTreeSectionPart{
    
    private  TtypeConverters typeConverters;

    public TypeConverterSection(Composite parent, FormToolkit toolkit, int style,JAXBElement blueprint) {
       super(blueprint, parent, toolkit, style);
       typeConverters = getBlueprint().getTypeConverters();
       
       if (typeConverters!=null)
           this.objectContainer = typeConverters.getBeanOrReferenceOrRef();
       else {
           typeConverters = BlueprintEditorUIHelper.getBlueprintObjectFactory().createTtypeConverters();
           getBlueprint().setTypeConverters(typeConverters);
           this.objectContainer = new ArrayList();
       }
       createClient();
      
    }
    
    @Override
    public void createClient(){
        super.createClient();
        
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace=true;
        gridData.grabExcessVerticalSpace=true;        
        this.getSection().setLayoutData(gridData);
    }

    @Override
    public String getTitle() {
        return CommonMessages.blueprintTypeConverterSectionTitle;
    }

    @Override
    public String getDescription() {
        return CommonMessages.blueprintTypeConverterSectionDescription;
    }

    @Override
    public Wizard getWizard() {
        return new BlueprintElementWizardProxy(this).getWizard();
    }

    @Override
    public Class getTableEntryObjectType() {
        //In fact, only Tbean, Treference,Tref are allowed. 
        //Maybe we can declare a common super classes for these three types
        return Tcomponent.class;
    }

    @Override
    public Object getInput() {
        if (objectContainer != null) {
            return objectContainer;
        }
        return super.getInput();
    }

    @Override
    protected void activateAddButton() {
        Object object = getSelectedObject();
        if (!BlueprintJAXBHelper.hasChildElements(object.getClass(),getExcludedFieldsOfClazz(object.getClass()))) {
            addButton.setEnabled(false);
        }else{
            addButton.setEnabled(true);
        }
    }

    public Object getSelectedObject() {
        if (tree.getSelection().length == 0) {
            return typeConverters;
        }
        Object object;
        if (tree.getSelection()[0].getParentItem() == null) {
            object = tree.getSelection()[0].getData();
        } else {
            object = tree.getSelection()[0].getParentItem().getData();
        }
        if (isBeanOrReferenceOrRef(object)) {
            return object;
        } else {
            return ((JAXBElement) object).getValue();
        }
    }

    @Override
    public void removeItem(Object anItem) {
        List childList = this.getBlueprint().getTypeConverters().getBeanOrReferenceOrRef();
        if (isBeanOrReferenceOrRef(anItem) && childList.contains(anItem)) {
            getObjectContainer().remove(anItem);
            this.getBlueprint().getTypeConverters().getBeanOrReferenceOrRef().remove(anItem);
        } else if (Targument.class.isInstance(anItem) || Tproperty.class.isInstance(anItem)) {            
            Object object = tree.getSelection()[0].getParentItem().getData();
            //it must be Tbean 
            Tbean bean = (Tbean)object;
            bean.getArgumentOrPropertyOrAny().remove(anItem);          
        } 
    }

    
    
   
    
    private boolean isBeanOrReferenceOrRef(Object object){
        return Tbean.class.isInstance(object)||Treference.class.isInstance(object)||Tref.class.isInstance(object);
        
    }

    @Override
    protected Object getSectionRootElement() {
        return  typeConverters;
    }

    @Override
    public List<Field> getExcludedFieldsOfClazz(Class clazz) {
        return null;
    }


  
}
