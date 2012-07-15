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
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.geronimo.osgi.blueprint.Tblueprint;
import org.apache.geronimo.st.ui.internal.Trace;
import org.apache.geronimo.st.v30.core.jaxb.BlueprintJAXBHelper;
import org.apache.geronimo.st.v30.core.jaxb.BlueprintJAXBHelper.SubClazzWrapper;
import org.apache.geronimo.st.v30.core.jaxb.JAXBObjectFactoryImpl;
import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.BlueprintEditorUIHelper;
import org.apache.geronimo.st.v30.ui.sections.AbstractTreeSection;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class AbstractBlueprintTreeSectionPart extends AbstractTreeSection implements
        IBlueprintJAXBManipulator {
    
    protected HashMap<Object, Field> treeFieldMap = new HashMap<Object,Field>();

    public AbstractBlueprintTreeSectionPart(JAXBElement blueprint, Composite parent, FormToolkit toolkit, int style) {
        super(blueprint, parent, toolkit, style);
    }
    
    @Override
    public void createClient(){
        super.createClient();
        
        this.getSection().addExpansionListener(new IExpansionListener(){

            @Override
            public void expansionStateChanged(ExpansionEvent arg0) {
                tree.deselectAll();
                
            }

            @Override
            public void expansionStateChanging(ExpansionEvent arg0) {
                //do nothing
            }
            
        });
    }

    @Override
    public void setObjectValue(Object selectedObject, HashMap<Field, String> attrMap) {
        try {
            BlueprintJAXBHelper.setAttributeValue(selectedObject,attrMap);
        } catch (SecurityException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+selectedObject, e, Activator.logBlueprint);
        } catch (IllegalArgumentException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+selectedObject, e, Activator.logBlueprint);
        } catch (NoSuchMethodException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+selectedObject, e, Activator.logBlueprint);
        } catch (IllegalAccessException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+selectedObject, e, Activator.logBlueprint);
        } catch (InvocationTargetException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+selectedObject, e, Activator.logBlueprint);
        }
        
    }

    @Override
    public void setNewObjectValue(Object parentObject,SubClazzWrapper clazzWrapper, HashMap<Field, String> attrMap) {
         Object newObject = JAXBObjectFactoryImpl.getInstance().createBlueprintElement(clazzWrapper.getClazz());
         try {
            BlueprintJAXBHelper.setAttributeValue(newObject,attrMap);
        } catch (SecurityException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+newObject, e, Activator.logBlueprint);
        } catch (IllegalArgumentException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+newObject, e, Activator.logBlueprint);
        } catch (NoSuchMethodException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+newObject, e, Activator.logBlueprint);
        } catch (IllegalAccessException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+newObject, e, Activator.logBlueprint);
        } catch (InvocationTargetException e) {
            Trace.trace(Trace.ERROR, "can't set attribute value for object "+newObject, e, Activator.logBlueprint);
        }
         try {
            BlueprintJAXBHelper.attachToParent(parentObject,newObject,clazzWrapper.getField());
        } catch (SecurityException e) {
            Trace.trace(Trace.ERROR, "can't add new object "+newObject, e, Activator.logBlueprint);
        } catch (IllegalArgumentException e) {
            Trace.trace(Trace.ERROR, "can't add new object "+newObject, e, Activator.logBlueprint);
        } catch (NoSuchFieldException e) {
            Trace.trace(Trace.ERROR, "can't add new object "+newObject, e, Activator.logBlueprint);
        } catch (NoSuchMethodException e) {
            Trace.trace(Trace.ERROR, "can't add new object "+newObject, e, Activator.logBlueprint);
        } catch (IllegalAccessException e) {
            Trace.trace(Trace.ERROR, "can't add new object "+newObject, e, Activator.logBlueprint);
        } catch (InvocationTargetException e) {
            Trace.trace(Trace.ERROR, "can't add new object "+newObject, e, Activator.logBlueprint);
        }
        
        treeFieldMap.put(newObject, clazzWrapper.getField());
    }
    
    protected Tblueprint getBlueprint(){
        return BlueprintEditorUIHelper.getBlueprint(getRootElement());
    }
    
    protected abstract Object getSectionRootElement();
    
    public abstract List<Field> getExcludedFieldsOfClazz(Class clazz);

    @Override
    public ITreeContentProvider getContentProvider() {
        return new ContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return getChildren(inputElement);
            }

            @Override
            public Object[] getChildren(Object parentElement) {
                if (List.class.isInstance(parentElement)) {
                    return ((List) parentElement).toArray();
                }
                if (JAXBElement.class.isInstance(parentElement)) {
                    parentElement = ((JAXBElement) parentElement).getValue();
                }
                
                if (BlueprintJAXBHelper.hasChildElements(parentElement.getClass(),getExcludedFieldsOfClazz(parentElement.getClass()))){
                    try {
                        List allSubElements = BlueprintJAXBHelper.getAllSubElements(parentElement);
                        return allSubElements.toArray(new Object[0]);
                    } catch (SecurityException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (IllegalArgumentException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (NoSuchMethodException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (IllegalAccessException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (InvocationTargetException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    }
                    
                }
                    
                return new String[] {};
            }
        };
    }

    @Override
    public ILabelProvider getLabelProvider() {
        return new LabelProvider() {
            @Override
            public String getText(Object element) {
                if (JAXBElement.class.isInstance(element)) {
                    element = ((JAXBElement) element).getValue();
                }
                
                try {
                        HashMap<String,String> attrMap = BlueprintJAXBHelper.getAllAttributes(element);
                        if (attrMap.size()>0) {
                            StringBuilder sb = new StringBuilder();
                            
                            //element name
                            Class clazz = element.getClass();
                            XmlRootElement root = (XmlRootElement) clazz.getAnnotation(javax.xml.bind.annotation.XmlRootElement.class);
                            sb.append(root.name()+": ");
                            
                            Set<Entry<String, String>> entrySet = attrMap.entrySet();
                            Iterator<Entry<String,String>> iter = entrySet.iterator();
                            while (iter.hasNext()) {
                                Entry<String, String> entry = iter.next();
                                String field = entry.getKey();
                                Object value = entry.getValue();
                                
                                sb.append(field+"="+value+" ");
                            }
                            return sb.toString();
                        }
                        
                    } catch (SecurityException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (IllegalArgumentException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (NoSuchMethodException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (IllegalAccessException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    } catch (InvocationTargetException e) {
                        Trace.trace(Trace.ERROR, "get child elements error! ", e, Activator.logBlueprint);
                    }
                    

                return null;
            }

            @Override
            public Image getImage(Object arg0) {
                return Activator.imageDescriptorFromPlugin("org.eclipse.jst.j2ee",
                        "icons/full/obj16/module_web_obj.gif").createImage();
            }
        };
    }
    
    public Object getParentElement(){
        if (isEditing()){
             TreeItem item = tree.getSelection()[0].getParentItem();
             if (item==null) return getSectionRootElement();
             else return item.getData();
        }else{
            return getSelectedObject();
        }
    }
    
    public void addItemFieldMap(Object data, Field field){
        treeFieldMap.put(data, field);
    }
}
