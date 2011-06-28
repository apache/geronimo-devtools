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

import java.lang.reflect.Method;

import org.apache.geronimo.st.v30.core.GeronimoUtils;
import org.apache.geronimo.st.v30.core.ModuleArtifactMapper;
import org.apache.geronimo.st.v30.core.internal.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.osgi.framework.Version;

public class OSGIBundleHelper {
    public static void addBundleToPublishedMap(IServer server, IModule module, long bundleId) throws Exception {
        if(! GeronimoUtils.isBundleModule(module)) return;
        try {
            String[] strArray = getBundleSymbolicNameAndVersion(module.getProject());
            ModuleArtifactMapper.getInstance().addBundleEntry(server, module, strArray[0], strArray[1], bundleId);
        } catch (Exception e) {
            throw e;
        }
    }
    public static boolean checkBundleInPublishedMap(IServer server, IModule module) {
        String symName = ModuleArtifactMapper.getInstance().resolveBundleByModule(server, module);
        return symName == null ? false : true;
    }
    
    public static void removeBundleFromPublishedMap(IServer server, IModule module) {
        ModuleArtifactMapper.getInstance().removeBundle(server, module);
    }
    
    public static boolean isBundle(IModule module) {
        return GeronimoUtils.isBundleModule(module);
    }
    public static boolean isBundle(IProject project) throws Exception {
        boolean ret = false;
        if(AriesHelper.isAriesInstalled()) {
            Class<?> ariesUtilsClass = Class.forName("com.ibm.etools.aries.internal.core.utils.AriesUtils");
            Method method = ariesUtilsClass.getMethod("isOSGIBundle", IProject.class);
            ret = (Boolean) method.invoke(null, project);
        } else {
        	throw new Exception(Messages.OSGI_ARIES_NOT_INSTALLED);
        }
        return ret;
    }
    
    public static boolean checkBundleDirty(IServer server, IModule module) {
        return ModuleArtifactMapper.getInstance().checkBundleDirty(server, module);
    }
    
    public static String[] getBundleSymbolicNameAndVersion(IProject project) throws Exception {
        String[] strs = null;
        if(AriesHelper.isAriesInstalled()) {
            if(isBundle(project)) {
                Class<?> ariesUtilsClass = Class.forName("com.ibm.etools.aries.internal.core.utils.AriesUtils");
                Method method = ariesUtilsClass.getMethod("getBlueprintBundleManifest", IProject.class);
                Object object = method.invoke(null, project);
                
                Class<?> bundleManifest = Class.forName("com.ibm.etools.aries.core.models.BundleManifest");
                method = bundleManifest.getMethod("getBundleSymbolicName");
                String bundleSymName = (String) method.invoke(object); 
                
                method = bundleManifest.getMethod("getBundleVersion"); 
                String versionStr = (String) method.invoke(object);
                Version version = Version.parseVersion(versionStr);
                String newVersionStr = GeronimoUtils.getVersion(version);                    
                
                if (bundleSymName != null && newVersionStr != null) {
                    strs = new String[2];
                    strs[0] = bundleSymName;
                    strs[1] = newVersionStr;
                }    
            }
        }
        return strs;

    }
    
    public static long getOSGIBundleId(IServer server, IModule module) {
        return ModuleArtifactMapper.getInstance().getBundleId(server, module);
    }
    public static String getBundleSymbolicNameAndVersionString(IProject project) throws Exception {
        String[] strs = getBundleSymbolicNameAndVersion(project);
        return strs[0] + ":" + strs[1];
    }
}


