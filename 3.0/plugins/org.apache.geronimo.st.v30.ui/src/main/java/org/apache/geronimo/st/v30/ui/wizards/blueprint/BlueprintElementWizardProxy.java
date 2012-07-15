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

package org.apache.geronimo.st.v30.ui.wizards.blueprint;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.apache.geronimo.st.ui.internal.Trace;
import org.apache.geronimo.st.v30.core.jaxb.BlueprintJAXBHelper;
import org.apache.geronimo.st.v30.core.jaxb.BlueprintJAXBHelper.SubClazzWrapper;
import org.apache.geronimo.st.v30.ui.sections.blueprint.AbstractBlueprintTreeSectionPart;
import org.apache.geronimo.st.v30.ui.wizards.AbstractTreeWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Text;

public class BlueprintElementWizardProxy{
    
    private AbstractTreeWizard wizard ;
    
    private Object parentElement;
    
    private SubClazzWrapper childElementClazz;  
    
    private List<SubClazzWrapper> childrenClazz;
    
    private AbstractBlueprintTreeSectionPart section;
   
         
    private class BlueprintWizard extends AbstractTreeWizard{
        
        
        private class BlueprintWizardPage extends AbstractTreeWizardPage{

            public BlueprintWizardPage(AbstractTreeWizard abstractTreeWizard, String pageName) {
                abstractTreeWizard.super(pageName);
                
                //init combo
                
                Iterator<SubClazzWrapper> iter = childrenClazz.iterator();
                int count = 0;
                while(iter.hasNext()){
                    SubClazzWrapper wrapper = iter.next();
                    Class clazz = wrapper.getClazz();
                    XmlRootElement element = (XmlRootElement) clazz.getAnnotation(javax.xml.bind.annotation.XmlRootElement.class);
                    elementTypes[count++]=element.name();
                }
            }

            @Override
            protected void initControl() {
                if (eObject!=null){
                    //it's edit mode
                    XmlRootElement rootElement = (XmlRootElement) eObject.getClass().getAnnotation(javax.xml.bind.annotation.XmlRootElement.class);
                    element.setItem(0, rootElement.name());
                    element.select(0);
                    element.setEnabled(false);
                    childElementClazz = new SubClazzWrapper(eObject.getClass(),null);
                    
                    //set value for text field
                    try {
                        HashMap<String,String> attrMap = BlueprintJAXBHelper.getAllAttributes(eObject);
                        Collection<String> values = attrMap.values();
                        
                        int count =0;
                        Iterator<String> iter = values.iterator();
                        while (iter.hasNext()){
                            String value = iter.next();
                            if (count<textList.size()){ 
                                Text text = textList.get(count);
                                text.setText(value);
                            }else {
                                //there must be an error
                                break;
                            }
                            count++;
                        }
                    } catch (SecurityException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logWizards);
                    } catch (IllegalArgumentException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logWizards);
                    } catch (NoSuchMethodException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logWizards);
                    } catch (IllegalAccessException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logWizards);
                    } catch (InvocationTargetException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logWizards);
                    }
                    
                }else{
                    //it's add mode
                    element.select(0);
                    element.setEnabled(true);
                }
                
            }
            
           

            @Override
            protected void toggleFields(boolean clearFields) {
                if (element.isEnabled()) {
                    int index = element.getSelectionIndex();
                    childElementClazz = childrenClazz.get(index);
                }
                
                List<Field> attrList = BlueprintJAXBHelper.getAllAttributeFields(childElementClazz.getClazz());
                
                Iterator<Field> iter = attrList.iterator();
                int count = 0;
                while (iter.hasNext()){
                    Field attr = iter.next();
                    XmlAttribute attrAnnotation = attr.getAnnotation(javax.xml.bind.annotation.XmlAttribute.class);
                    String attrName = attrAnnotation.name();
                    if (attrName.contains("#")) {
                        //name property is not set in the annotation
                        attrName = attr.getName();
                    }
                    labelList.get(count).setText(attrName);
                    labelList.get(count).setVisible(true);
                    textList.get(count).setVisible(true);
                    if (clearFields) textList.get(count).setText("");
                    count++;
                }
                
                for (int i=count;i<labelList.size();i++){
                    labelList.get(i).setVisible(false);
                    textList.get(i).setVisible(false);
                }
                
            }

            @Override
            protected String getWizardPageTitle() {
            	return Messages.blueprintConfiguration;
            }

            @Override
            protected String getWizardPageDescription() {
            	return Messages.blueprintAddOrEditElementInSchema;
            }
            
            public HashMap<Field,String> getResult(){
              //init attribute map
                HashMap<Field,String> attrMap = new HashMap<Field,String>();
                
                List<Field> attrList = BlueprintJAXBHelper.getAllAttributeFields(childElementClazz.getClazz());
                Iterator<Text> iter = textList.iterator();
                int count=0;
                while (iter.hasNext()){
                    Text text = iter.next();
                    String value = text.getText();
                    if (text.isVisible()&&value!=null&&!value.isEmpty()) {
                        Field attrField = attrList.get(count);
                        attrMap.put(attrField, value);
                    }
                    count++;
                }
                
                return attrMap;
            }
            
        }
        
        private BlueprintWizardPage page;
        
        public BlueprintWizard(int maxTextField){
            super(BlueprintElementWizardProxy.this.section,childrenClazz.size(),maxTextField);
           
        }
        
        @Override
        public void addPages() {
            page = new BlueprintWizardPage(wizard, parentElement.getClass().getSimpleName());
            addPage(page);
        }

        @Override
        protected String getAddWizardWindowTitle() {
        	return Messages.blueprintEditorAdding;
        }

        @Override
        protected String getEditWizardWindowTitle() {
        	return Messages.blueprintEditorEditing;
        }

        @Override
        public boolean performFinish() {
            HashMap<Field,String> attrMap = page.getResult();
            
            if (element.isEnabled()){
                //add mode
                BlueprintElementWizardProxy.this.section.setNewObjectValue(parentElement,childElementClazz, attrMap);                
            }else{
                //edit mode
                BlueprintElementWizardProxy.this.section.setObjectValue(section.getSelectedObject(), attrMap);
            }
            
            return true;
        }
        
    }
    

    public BlueprintElementWizardProxy(AbstractBlueprintTreeSectionPart  blueprintSection) {
        section = blueprintSection;
        
        parentElement= blueprintSection.getParentElement();
        
        childrenClazz = BlueprintJAXBHelper.getAllSubTypes(parentElement.getClass(),blueprintSection.getExcludedFieldsOfClazz(parentElement.getClass()));
        
        //init attribute map
        Iterator<SubClazzWrapper> iter = childrenClazz.iterator();
        int maxTextField = 0;
        while (iter.hasNext()){
            SubClazzWrapper wrapper = (SubClazzWrapper)iter.next();
            Class clazz = wrapper.getClazz();
            List<Field> attributeFields = BlueprintJAXBHelper.getAllAttributeFields(clazz);
            if (maxTextField < attributeFields.size()) maxTextField = attributeFields.size();
        }
        
        initWizard(maxTextField);
    }
    
    

    private void initWizard(int maxTextField) {
        
        wizard = new BlueprintWizard(maxTextField);
        
        
    }
    
    public Wizard getWizard(){
        return wizard;
    }

    
}
