package org.apache.geronimo.st.v30.ui.sections.blueprint;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.geronimo.st.v30.core.jaxb.BlueprintJAXBHelper.SubClazzWrapper;

public interface IBlueprintJAXBManipulator {
    public void setNewObjectValue(Object parentObject,SubClazzWrapper clazz, HashMap<Field,String> attrMap);
    
    public void setObjectValue(Object selectedObject, HashMap<Field,String> attrMap);
}
