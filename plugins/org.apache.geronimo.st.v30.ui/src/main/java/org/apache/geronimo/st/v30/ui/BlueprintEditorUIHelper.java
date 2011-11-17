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
