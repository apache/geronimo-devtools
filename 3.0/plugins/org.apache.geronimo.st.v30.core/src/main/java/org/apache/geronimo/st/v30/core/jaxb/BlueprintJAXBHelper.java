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


package org.apache.geronimo.st.v30.core.jaxb;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.osgi.blueprint.ObjectFactory;
import org.apache.geronimo.osgi.blueprint.Tactivation;
import org.apache.geronimo.osgi.blueprint.TautoExportModes;
import org.apache.geronimo.osgi.blueprint.Tavailability;
import org.apache.geronimo.osgi.blueprint.Tdescription;
import org.apache.geronimo.osgi.blueprint.TserviceUse;
import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;

public class BlueprintJAXBHelper {

    private static Hashtable<Class, List<SubClazzWrapper>> subTypesCache = new Hashtable<Class, List<SubClazzWrapper>>();
    private static Hashtable<Class, List<String>> propertiesCache = new Hashtable<Class, List<String>>();
    private static Hashtable<Class, List<Field>> attrNameCache = new Hashtable<Class, List<Field>>();

    public static class SubClazzWrapper {

        private Class clazz;

        private Field field;

        public SubClazzWrapper(Class subClazz, Field clazzField) {
            clazz = subClazz;
            field = clazzField;
        }

        public Class getClazz() {
            return clazz;
        }

        public Field getField() {
            return field;
        }

        public void setClazz(Class subClazz) {
            clazz = subClazz;
        }

        public void setField(Field clazzField) {
            field = clazzField;
        }

    }

    /*
     * return all the inherited class of given class
     */
    public static List<Class> getAllInheritedType(Class clazz) {
        if (clazz == null)
            return null;
        ArrayList<Class> inheritedClass = new ArrayList<Class>();
        while (clazz != null && !clazz.equals(Object.class)) {
            inheritedClass.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return inheritedClass;
    }

    /*
     * get PropOrders of given class, this class must be annotated by @XmlType;didn't include prop in inherited class
     */
    private static List<String> getPropOrdersForClass(Class clazz) {

        if (clazz == null)
            return null;

        if (!propertiesCache.containsKey(clazz)) {
            XmlType xmlType = (XmlType) clazz.getAnnotation(javax.xml.bind.annotation.XmlType.class);
            String[] propOrders = xmlType.propOrder();
            propertiesCache.put(clazz, Arrays.asList(propOrders));
        }
        return propertiesCache.get(clazz);
    }

    public static List<SubClazzWrapper> getAllSubTypes(Class clazz){
        return getAllSubTypes(clazz,null);
    }
    /*
     * get sub class types of given class, it includes all class types from all inherited classes
     */
    public static List<SubClazzWrapper> getAllSubTypes(Class clazz, List<Field> excludedFields) {

        if (!subTypesCache.contains(clazz)) {
            visit(clazz);
        }
        List<SubClazzWrapper> subTypes = subTypesCache.get(clazz);
        
        if (excludedFields!=null&&excludedFields.size()>0) {
            Iterator<SubClazzWrapper> iter = subTypes.iterator();
            while (iter.hasNext()){
                SubClazzWrapper wrapper = iter.next();
                if (excludedFields.contains(wrapper.getField()))
                    iter.remove();
            }
        }
        
        return subTypes;

    }

    private static void visit(Class clazz) {
        ArrayList<SubClazzWrapper> subTypes = new ArrayList<SubClazzWrapper>();
        ArrayList<String> propList = new ArrayList<String>();

        // get all inherited class
        List<Class> inheritedClass = getAllInheritedType(clazz);

        Iterator<Class> iter = inheritedClass.iterator();
        while (iter.hasNext()) {
            Class aClass = iter.next();

            List<String> propOrders = getPropOrdersForClass(aClass);
            if (propOrders == null)
                continue;
            propList.addAll(propOrders);

            // find corresponding field for props
            Iterator<String> propIter = propOrders.iterator();
            while (propIter.hasNext()) {
                String prop = propIter.next();
                try {
                    Field field = aClass.getDeclaredField(prop);
                    List<SubClazzWrapper> classList = getJAXBTypesByField(field);
                    
                    //We don't support Tdescription editing
                    if (classList.size()==1 && classList.get(0).getClazz().equals(Tdescription.class)) continue;
                    else  subTypes.addAll(classList);

                } catch (SecurityException e) {
                    continue;
                } catch (NoSuchFieldException e) {
                    continue;
                }

            }
        }

        subTypesCache.put(clazz, subTypes);
        propertiesCache.put(clazz, propList);
    }

    /*
     * get jaxb class type of given field in a type
     */
    public static List<SubClazzWrapper> getJAXBTypesByField(Field field) {
        ArrayList<SubClazzWrapper> typeList = new ArrayList<SubClazzWrapper>();
        // if it's in format like *Or*Or (always annotated with @XmlElementRefs),
        // it will be separated into several types

        // 1. check if it's annotated with @XmlElementRefs
        if (field.isAnnotationPresent(javax.xml.bind.annotation.XmlElementRefs.class)) {
            XmlElementRefs eRefs = field.getAnnotation(javax.xml.bind.annotation.XmlElementRefs.class);
            XmlElementRef[] refs = eRefs.value();
            for (XmlElementRef ref : refs) {
                String typeName = ref.type().getSimpleName();
                Class clazz;
                try {
                    clazz = Activator.class.getClassLoader()
                            .loadClass("org.apache.geronimo.osgi.blueprint." + typeName);

                    SubClazzWrapper wrapper = new SubClazzWrapper(clazz, field);

                    typeList.add(wrapper);
                } catch (ClassNotFoundException e) {
                    Trace.trace(Trace.ERROR, "can't find type " + typeName + " in blueprint classes", e, Activator.logJaxb );
                }

            }
        } else {
            // 2. check if it's list type
            Class fieldType = field.getType();
            if (fieldType.equals(List.class)) {
                Type type = field.getGenericType();
                if (type instanceof ParameterizedType) {
                    Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                    if (types.length != 0) {
                        Class genericType = types[0].getClass();
                        if (genericType.equals(JAXBElement.class)) {
                            // It's Tinterfaces type, only return String.class
                            typeList.add(new SubClazzWrapper(String.class, field));
                        }
                    }
                }
               
            } else {
                // 3. normal field, only return type of the field
                typeList.add(new SubClazzWrapper(fieldType, field));
            }
        }
        return typeList;
    }

    public static List<Field> getAllAttributeFields(Class clazz) {
        ArrayList<Field> attrList = null;

        if (!attrNameCache.contains(clazz)) {
            attrList = new ArrayList<Field>();

            // get all inherited class
            List<Class> inheritedClass = getAllInheritedType(clazz);

            Iterator<Class> iter = inheritedClass.iterator();
            while (iter.hasNext()) {
                Class aClass = iter.next();

                Field[] fieldArray = aClass.getDeclaredFields();

                // find corresponding field of attribute
                for (Field field : fieldArray) {
                    if (field.isAnnotationPresent(javax.xml.bind.annotation.XmlAttribute.class)) {
                        // it's an attribute
                        XmlAttribute attr = field.getAnnotation(javax.xml.bind.annotation.XmlAttribute.class);
                        attrList.add(field);
                    }

                }
            }

            attrNameCache.put(clazz, attrList);
        }

        return attrNameCache.get(clazz);
    }

    public static void setAttributeValue(Object selectedObject, HashMap<Field, String> attrMap)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (selectedObject == null)
            return;

        Set<Entry<Field, String>> entrySet = attrMap.entrySet();
        Iterator<Entry<Field, String>> iter = entrySet.iterator();
        while (iter.hasNext()) {
            Entry<Field, String> entry = iter.next();
            Field field = entry.getKey();
            Object value = entry.getValue();

            // invoke setter method
            // if it's a list type, we need to get the list first
            if (field.getType().equals(List.class)) {
                // invoke method
                Method method = getMethodByField(selectedObject.getClass(),field,false);
                Object resultList = method.invoke(selectedObject);

                // 2. add strings into list
                String[] values = ((String)value).split(",|;| ");
                ((List) resultList).addAll(Arrays.asList(values));
                continue;
            } else {
                // if it's not a list type, just invoke setter method
                Method method = getMethodByField(selectedObject.getClass(),field,true);
                if (field.getType().isEnum()) {
                    // It's a enum
                    value = Enum.valueOf(getEnumClass(field.getType()), (String) value);
                }
                method.invoke(selectedObject, value);
            }
        }

    }

    private static Class getEnumClass(Class type) {
        if (type.equals(Tactivation.class)) {
            return Tactivation.class;
        } else if (type.equals(TautoExportModes.class)) {
            return TautoExportModes.class;
        } else if (type.equals(Tavailability.class)) {
            return Tavailability.class;
        } else if (type.equals(TserviceUse.class)) {
            // TserviceUse
            return TserviceUse.class;
        }

        return null;
    }

    public static void attachToParent(Object parentObject, Object newObject, Field field) throws SecurityException,
            NoSuchFieldException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (parentObject == null || newObject == null)
            return;

        Class clazz = parentObject.getClass();

        Class fieldType = field.getType();
        if (fieldType.equals(List.class)) {
            // it's list type
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
                if (types.length != 0) {
                    Class generic = types[0].getClass();
    
                    if (generic.equals(JAXBElement.class)) {
                        // It must be Tinterfaces
                        if (newObject.getClass().equals(String.class))
                            newObject = new ObjectFactory().createTinterfacesValue((String) newObject);
                    }
    
                    // add the new object into list
                    // 1. get List first
                    // invoke method
                    Method method = getMethodByField(parentObject.getClass(),field,false);
                    Object resultList = method.invoke(parentObject);
    
                    // 2. add new object into list
                    ((List) resultList).add(newObject);
                 }
            }
        } else {
            // get method name for setter
            // invoke method
            Method method = getMethodByField(parentObject.getClass(),field,true);
            method.invoke(parentObject,newObject);

        }
    }
    
    public static void detachFromParent(Object parentObject, Object newObject, Field field) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        if (parentObject == null || newObject == null)
            return;

        Class clazz = parentObject.getClass();

        Class fieldType = field.getType();
        if (fieldType.equals(List.class)) {
            // it's list type
            Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
                if (types.length != 0) {
                    Class generic = types[0].getClass();
    
                    if (generic.equals(JAXBElement.class)) {
                        // It must be Tinterfaces
                        if (newObject.getClass().equals(String.class))
                            newObject = new ObjectFactory().createTinterfacesValue((String) newObject);
                    }
    
                    // add the new object into list
                    // 1. get List first
                    // invoke method
                    Method method = getMethodByField(parentObject.getClass(),field,false);
                    Object resultList = method.invoke(parentObject);
    
                    // 2. remove new object from list
                    ((List) resultList).remove(newObject);
                 }
            }
        } else {
            // get method name for setter
            // invoke method
            Method method = getMethodByField(parentObject.getClass(),field,true);
            method.invoke(parentObject,null);

        }
    }
    
    public static boolean hasChildElements(Class clazz, List<Field> excludedFields){
        List<SubClazzWrapper> wrapperList=getAllSubTypes(clazz,excludedFields);
        return wrapperList!=null && wrapperList.size()>0;
    }
    
    public static List getAllSubElements(Object object) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        List subElements = new ArrayList();
        
        List<SubClazzWrapper> wrapperList = getAllSubTypes(object.getClass());
        
        Iterator<SubClazzWrapper> iter = wrapperList.iterator();
        
        while(iter.hasNext()){
            SubClazzWrapper wrapper = iter.next();
            
            Field field = wrapper.getField();
            Class fieldType = field.getType(); 
            
            if (fieldType.equals(List.class)) {
                //get all objects in the list first

                // 1. get List first
                // invoke method
                Method method = getMethodByField(object.getClass(),field,false);
                Object resultList = method.invoke(object);

                // 2. add new object into list                
                
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType) {
                    Type[] types = ((ParameterizedType) genericType).getActualTypeArguments();
                    if (types.length != 0) {
                        Class generic = types[0].getClass();
        
                        if (generic.equals(JAXBElement.class)) {
                            Iterator<JAXBElement> jaxbElementIter = ((List)resultList).iterator();
                            while (jaxbElementIter.hasNext()){
                                JAXBElement element = jaxbElementIter.next();
                                ((List)resultList).add(element.getValue());
                            }
                            continue ;
                        }    
                     }
                }
                
                subElements.addAll((List)resultList);
                
                
            } else {
                

                // invoke method
                Method method = getMethodByField(object.getClass(),field,true);
                Object result = method.invoke(object);
                
                subElements.add(result);
            }
        }
        return subElements;
        
    }
    
    public static HashMap<String,String> getAllAttributes(Object object) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        HashMap<String,String> attrMap = new HashMap<String,String>();
        
        List<Field> fieldList = getAllAttributeFields(object.getClass());
        
        Iterator<Field> iter = fieldList.iterator();
        
        while(iter.hasNext()){
            Field field = iter.next();
            
            // invoke setter method
            // if it's a list type, we need to get the list first
            if (field.getType().equals(List.class)) {
                // It's a list
                // invoke method
                Method method = getMethodByField(object.getClass(),field,false);
                Object resultList = method.invoke(object);

                // 2. merge strings for the list
                Iterator attrIter = ((List)resultList).iterator();
                StringBuilder attrValue = new StringBuilder();
                while (attrIter.hasNext()){
                    Object attr = attrIter.next();
                    attrValue.append(attr).append(";");
                }
                attrMap.put(field.getName(), attrValue.toString());
            } else {

                // if it's not a list type, just invoke getter method
                Method method = getMethodByField(object.getClass(),field,false);
                Object value = method.invoke(object);
                attrMap.put(field.getName(), String.valueOf(value));
            }
        }
        
        return attrMap;
    }
    
    private static Method getMethodByField(Class clazz, Field field,boolean isSetter){
        // get method name 
        String methodPrefix = isSetter ? "set":"get";
        StringBuilder sb = new StringBuilder(methodPrefix);
        String fieldName = field.getName();
        String prefix = fieldName.substring(0, 1);
        if (prefix.equals("_")) {
            //there may be a "_" in front of field name, such as _interface
            fieldName = fieldName.substring(1);
            prefix = fieldName.substring(0, 1);;
        }
        sb.append(fieldName.replaceFirst(prefix, prefix.toUpperCase()));

        
        try {
            if (isSetter) return clazz.getMethod(sb.toString(),field.getType());
            else return clazz.getMethod(sb.toString());
        } catch (SecurityException e) {
           Trace.trace(Trace.ERROR, "can't get method by field", e, Activator.logJaxb);
        } catch (NoSuchMethodException e) {
           Trace.trace(Trace.ERROR, "can't get method by field", e, Activator.logJaxb);
        }
        
        return null;
    }

}
