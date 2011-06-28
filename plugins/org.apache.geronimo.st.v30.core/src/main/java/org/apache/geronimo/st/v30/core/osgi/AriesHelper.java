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
package org.apache.geronimo.st.v30.core.osgi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.server.core.IModule;
import org.osgi.framework.Bundle;

/**
 * <b>AriesHelper</b> is a static helper class used to encapsulate the functions related to the installation of the 
 * Aries OSGi tooling plugins (i.e., whether they are installed or not). They are optional and have to be manually 
 * installed by the GEP user(s).
 */
public final class AriesHelper {

    private AriesHelper() {
    }
    
    
    /**
     * Determine if the Aries OSGi tooling plugins are installed. They are optional and have to be manually installed 
     * by the GEP user(s).
     *
     * @return true or false
     */
    public static boolean isAriesInstalled() {
        Trace.tracePoint("Entry", Activator.traceOsgi, "AriesHelper.isAriesInstalled");

        Bundle ariesCore = Platform.getBundle("com.ibm.etools.aries.core");
        Bundle ariesUI   = Platform.getBundle("com.ibm.etools.aries.ui");

        if (ariesCore != null && ariesCore.getState() != Bundle.UNINSTALLED &&
            ariesUI != null && ariesUI.getState() != Bundle.UNINSTALLED) {
    
            Trace.tracePoint("Exit", Activator.traceOsgi, "AriesHelper.isAriesInstalled", true);
            return true;
        }
    
        Trace.tracePoint("Exit", Activator.traceOsgi, "AriesHelper.isAriesInstalled", false);
        return false;
    }
    
    public static IModule[] getChildModules(IModule ebaModule) {
        if (AriesHelper.isAriesInstalled()) {
            try {
                Class<?> class1 = Class.forName("com.ibm.etools.aries.internal.core.modules.AriesModuleDelegate");
                Method method = class1.getMethod("getChildModules");
                Constructor<?> constructor = class1.getConstructor(IProject.class);
                Object object = constructor.newInstance(ebaModule.getProject());
                return (IModule[]) method.invoke(object);                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }        
        return new IModule[0];
    }
    
    public static String getSymbolicName(IModule bundleModule) {
        if (AriesHelper.isAriesInstalled()) {
            try {
                Class<?> class1 = Class.forName("com.ibm.etools.aries.internal.core.utils.AriesUtils");
                Method method = class1.getMethod("getBundleSymbolicName", IProject.class);
                return (String) method.invoke(null, bundleModule.getProject());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
