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

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.Platform;
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
    
            Trace.tracePoint("Exit", "AriesHelper.isAriesInstalled", true);
            return true;
        }
    
        Trace.tracePoint("Exit", "AriesHelper.isAriesInstalled", false);
        return false;
    }
}
