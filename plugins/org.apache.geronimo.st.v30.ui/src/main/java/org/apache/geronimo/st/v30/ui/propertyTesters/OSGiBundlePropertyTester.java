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

/**
 * @version $Rev$ $Date$
 */
package org.apache.geronimo.st.v30.ui.propertyTesters;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.ui.IServerModule;

public class OSGiBundlePropertyTester extends PropertyTester{
    
    public OSGiBundlePropertyTester() {
    }

    @Override
    public boolean test(Object target, String properties, Object[] args, Object value) {        
      
        if (target != null && "isOSGiBundle".equals(properties)) {
            if (target instanceof IServerModule) {
                IServerModule ms = (IServerModule) target;
                IModule[] modules =ms.getModule();
                if (modules == null || modules.length == 0) {
                    return false;
                }
                
                if (modules.length == 1) {
                    IModule m = modules[0];
                    boolean isOSGiBundle = "osgi.bundle".equals(m.getModuleType().getId());
                    if (value == null) {
                        value = Boolean.TRUE;
                    }
                    if ((Boolean)value) {
                        return isOSGiBundle;
                    } else {
                        return !isOSGiBundle;
                    }
                }
            }
        } 
        return false;
    }
}
