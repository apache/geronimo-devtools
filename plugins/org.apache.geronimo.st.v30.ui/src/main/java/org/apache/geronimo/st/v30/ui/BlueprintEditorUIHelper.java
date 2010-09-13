package org.apache.geronimo.st.v30.ui;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.osgi.blueprint.ObjectFactory;
import org.apache.geronimo.osgi.blueprint.Tblueprint;

public class BlueprintEditorUIHelper {
    
    private static  ObjectFactory blueprintObjectFactory ;
    
    public static Tblueprint getBlueprint(JAXBElement root ){
        
        if (root!=null){
            Object rootObject = root.getValue();
            if (Tblueprint.class.isInstance(rootObject)){
                return (Tblueprint)rootObject;
            }
            
        }
        return null;
        //Otherwise, create a new root element
//        Tblueprint tbluePrint = getBlueprintObjectFactory().createTblueprint();
//        blueprint = getBlueprintObjectFactory().createBlueprint(tbluePrint);
//        markDirty();
//        return tbluePrint;
    }
    
    public static ObjectFactory getBlueprintObjectFactory() {
        if ( blueprintObjectFactory == null ) {
            blueprintObjectFactory = new ObjectFactory();
        }
        return blueprintObjectFactory;
    }
    
}
