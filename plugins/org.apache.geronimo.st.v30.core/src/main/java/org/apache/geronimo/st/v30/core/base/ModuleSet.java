/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v30.core.base;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.geronimo.st.v30.core.osgi.OsgiConstants;
import org.apache.geronimo.st.v30.core.util.Utils;

/**
 * 
 *
 * @version $Rev$ $Date$
 */
public class ModuleSet<E> extends HashSet<E> {
    private static final long serialVersionUID = 6614548315200004801L;
    protected int defaultStartLevel;
    
    public ModuleSet() {
        this(OsgiConstants.BUNDLE_DEFAULT_START_LEVEL);
    }
    public ModuleSet(int defaultStartLevel) {
        super();
        this.defaultStartLevel = defaultStartLevel;
    }
    
    public E query(String fieldName, Object value) throws Exception {
        E element = null;
        
        Iterator<E> iter = this.iterator();
        while(iter.hasNext()) {
            E b = iter.next();
            if(value.equals(Utils.getValueByFieldName(fieldName, b))) {
                element = b;
                break;
            }
        }
        return element;
    }
    
    public boolean remove(String fieldName, Object value) throws Exception {
        return this.remove(this.query(fieldName, value));
    }

    public int getDefaultModuleStartLevel() {
        return defaultStartLevel;
    }

    public void setDefaultStartLevel(int defaultStartLevel) {
        this.defaultStartLevel = defaultStartLevel;
    }
}
