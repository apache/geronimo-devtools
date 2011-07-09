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
import java.util.Set;


import org.apache.geronimo.deployment.plugin.jmx.RemoteDeploymentManager;
import org.apache.geronimo.st.v30.core.GeronimoUtils;
import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.osgi.framework.Version;

public class OSGIBundleHelper {

    public static void addBundleToCache(IServer server, IModule module) throws Exception {
        if(! isBundle(module)) return;
        try {
            String[] strArray = getBundleSymbolicNameAndVersion(module.getProject());
            OSGIBundleCache.getInstance().addBundleEntry(server, module, strArray[0], strArray[1]);
        } catch (Exception e) {
            throw e;
        }
    }
    
    public static boolean checkBundleInCache(IServer server, IModule module) {
        OSGIBundleCache.BundleInfo bundle = OSGIBundleCache.getInstance().getBundleByModule(server, module);
        return bundle == null ? false : true;
    }
    
    public static void removeBundleFromCache(IServer server, IModule module) {
        OSGIBundleCache.getInstance().removeBundleEntry(server, module);
    }
    
    public static boolean isBundle(IModule module) throws Exception {
        if(module != null && module.getProject() != null) {
            return isBundle(module.getProject());
        }
        return false;
    }
    
    public static boolean isBundle(IProject project) throws Exception {
        boolean ret = false;
        if(AriesHelper.isAriesInstalled()) {
            Class<?> ariesUtilsClass = Class.forName("com.ibm.etools.aries.internal.core.utils.AriesUtils");
            Method method = ariesUtilsClass.getMethod("isOSGIBundle", IProject.class);
            ret = (Boolean) method.invoke(null, project);
        }
        return ret;
    }
    
    public static Set<OSGIBundleCache.BundleInfo> getSpecificServerBundles(IServer server) {
        return OSGIBundleCache.getInstance().getServerBundles(server);
    }
    
    public static boolean checkBundleDirty(IServer server, IModule module) {
        OSGIBundleCache.BundleInfo bInfo = OSGIBundleCache.getInstance().getBundleByModule(server, module);
        if(bInfo == null) return false;
        return bInfo.isDirty();
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
    
    public static long getOSGIBundleIdFromServer(IServer server, IModule module) throws Exception {
        if(module != null && module.getProject() != null) {
            RemoteDeploymentManager rDm = (RemoteDeploymentManager)DeploymentCommandFactory.getDeploymentManager(server);
            OSGIBundleCache.BundleInfo bif = OSGIBundleCache.getInstance().getBundleByModule(server, module);
            
            return rDm.getBundleId(bif.getBundleSymbolicName(), bif.getBundleVersion());
        }
        return -1;
    }
    
    public static long getOSGIBundleIdFromServer(IServer server, OSGIBundleCache.BundleInfo bundleInfo) throws Exception {
        RemoteDeploymentManager rDm = (RemoteDeploymentManager)DeploymentCommandFactory.getDeploymentManager(server);
        return rDm.getBundleId(bundleInfo.getBundleSymbolicName(), bundleInfo.getBundleVersion());
    }
    
    public static String getBundleSymbolicNameAndVersionString(IProject project) throws Exception {
        String[] strs = getBundleSymbolicNameAndVersion(project);
        return strs[0] + ":" + strs[1];
    }
}


