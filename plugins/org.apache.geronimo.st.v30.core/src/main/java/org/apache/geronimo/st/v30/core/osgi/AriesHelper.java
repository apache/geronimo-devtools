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

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.server.core.IModule;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

/**
 * <b>AriesHelper</b> is a static helper class used to encapsulate the functions related to the installation of the 
 * Aries OSGi tooling plugins (i.e., whether they are installed or not). They are optional and have to be manually 
 * installed by the GEP user(s).
 */
public final class AriesHelper {

    private static Method GET_BUNDLE_MANIFEST_METHOD;
    private static Method GET_APPLICATION_MANIFEST_METHOD;
    
    private static Method GET_BUNDLE_SYMBOLIC_NAME_METHOD;
    private static Method GET_BUNDLE_VERSION_METHOD;
    
    private static Method GET_APPLICATION_SYMBOLIC_NAME_METHOD;
    private static Method GET_APPLICATION_VERSION_METHOD;
    
    private static Method REMOVE_MODEL_METHOD;
    private static Method GET_MANIFEST_FILE_METHOD;
    
    static {
        initialize();
    }
    
    private static void initialize() {
        try {
            Class<?> ariesUtilsClass = Class.forName("com.ibm.etools.aries.internal.core.utils.AriesUtils");
            GET_BUNDLE_MANIFEST_METHOD = ariesUtilsClass.getMethod("getBlueprintBundleManifest", IProject.class);
            GET_APPLICATION_MANIFEST_METHOD = ariesUtilsClass.getMethod("getApplicationManifest", IProject.class);
        } catch (ClassNotFoundException e) {
            Trace.trace(Trace.WARNING, "AriesUtils class is not available", e, Activator.traceOsgi);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not initialize AriesUtils methods", e, Activator.traceOsgi);
        }
        
        try {
            Class<?> bundleManifestClass = Class.forName("com.ibm.etools.aries.core.models.BundleManifest");
            GET_BUNDLE_SYMBOLIC_NAME_METHOD = bundleManifestClass.getMethod("getBundleSymbolicName");
            GET_BUNDLE_VERSION_METHOD = bundleManifestClass.getMethod("getBundleVersion");
        } catch (ClassNotFoundException e) {
            Trace.trace(Trace.WARNING, "BundleManifest class is not available", e, Activator.traceOsgi);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not initialize BundleManifest methods", e, Activator.traceOsgi);
        }
        
        try {
            Class<?> appManifestClass = Class.forName("com.ibm.etools.aries.core.models.ApplicationManifest");
            GET_APPLICATION_SYMBOLIC_NAME_METHOD = appManifestClass.getMethod("getApplicationSymbolicName");
            GET_APPLICATION_VERSION_METHOD= appManifestClass.getMethod("getApplicationVersion");
        } catch (ClassNotFoundException e) {
            Trace.trace(Trace.WARNING, "ApplicationManifest class is not available", e, Activator.traceOsgi);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not initialize ApplicationManifest methods", e, Activator.traceOsgi);
        }
        
        try {
            Class<?> manifestFactoryClass = Class.forName("com.ibm.etools.aries.core.models.ManifestModelsFactory");
            Class<?> manifestClass = Class.forName("com.ibm.etools.aries.core.models.Manifest");
            REMOVE_MODEL_METHOD = manifestFactoryClass.getMethod("removeModel", new Class [] { IProject.class, manifestClass });
        } catch (ClassNotFoundException e) {
            Trace.trace(Trace.WARNING, "ManifestModelsFactory or Manifest class is not available", e, Activator.traceOsgi);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not initialize ManifestModelsFactory methods", e, Activator.traceOsgi);
        }
        
        try {
            Class<?> manifestUtilsClass = Class.forName("com.ibm.etools.aries.internal.core.utils.ManifestUtils");
            GET_MANIFEST_FILE_METHOD = manifestUtilsClass.getMethod("getManifestFile", IProject.class);
        } catch (ClassNotFoundException e) {
            Trace.trace(Trace.WARNING, "ManifestUtils class is not available", e, Activator.traceOsgi);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not initialize ManifestUtils methods", e, Activator.traceOsgi);
        }
    }
    
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
    
    public static BundleInfo getBundleInfo(IModule module) {
        BundleInfo info = null;
        try {
            info = getBundleInfo(module.getProject());
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not get BundleInfo", e, Activator.traceOsgi);
        }
        return info;
    }
    
    public static BundleInfo getBundleInfo(IProject project) throws Exception {
        if (GET_BUNDLE_MANIFEST_METHOD != null) {
            Object bundleManifest = GET_BUNDLE_MANIFEST_METHOD.invoke(null, project);
            String symbolicName = (String) GET_BUNDLE_SYMBOLIC_NAME_METHOD.invoke(bundleManifest);
            String version = (String) GET_BUNDLE_VERSION_METHOD.invoke(bundleManifest);
            if (symbolicName == null && version == null && REMOVE_MODEL_METHOD != null) {
                // try to reset the manifest cache in case and lookup the values again
                REMOVE_MODEL_METHOD.invoke(null, project, bundleManifest);
                bundleManifest = GET_BUNDLE_MANIFEST_METHOD.invoke(null, project);
                symbolicName = (String) GET_BUNDLE_SYMBOLIC_NAME_METHOD.invoke(bundleManifest);
                version = (String) GET_BUNDLE_VERSION_METHOD.invoke(bundleManifest);
            }
            if (symbolicName != null) {
                return new BundleInfo(symbolicName, Version.parseVersion(version));
            }
        }
        return null;
    }
        
    public static BundleInfo getApplicationBundleInfo(IProject project) throws Exception {
        if (GET_APPLICATION_MANIFEST_METHOD != null) {
            Object appManifest = GET_APPLICATION_MANIFEST_METHOD.invoke(null, project);
            String symbolicName = (String) GET_APPLICATION_SYMBOLIC_NAME_METHOD.invoke(appManifest);
            String version = (String) GET_APPLICATION_VERSION_METHOD.invoke(appManifest);
            if (symbolicName == null && version == null && REMOVE_MODEL_METHOD != null) {
                // try to reset the manifest cache in case and lookup the values again
                REMOVE_MODEL_METHOD.invoke(null, project, appManifest);
                appManifest = GET_APPLICATION_MANIFEST_METHOD.invoke(null, project);
                symbolicName = (String) GET_APPLICATION_SYMBOLIC_NAME_METHOD.invoke(appManifest);
                version = (String) GET_APPLICATION_VERSION_METHOD.invoke(appManifest);
            }
            if (symbolicName != null) {
                return new BundleInfo(symbolicName, Version.parseVersion(version));
            }
        }
        return null;
    }

    public static IFile getManifestFile(IProject project) {
        if (GET_MANIFEST_FILE_METHOD != null) {
            try {
                return (IFile) GET_MANIFEST_FILE_METHOD.invoke(null, project);
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, "Could not get manifest file", e, Activator.traceOsgi);
            }
        }
        return null;
    }
    
    // copied from org.apache.geronimo.aries.builder.ApplicationInstaller.getVersion(Version)
    public static String toMvnVersion(Version version) {
        String str = version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
        String qualifier = version.getQualifier();
        if (qualifier != null && qualifier.trim().length() > 0) {
            str += "-" + version.getQualifier().trim();
        }
        return str;
    }
    
    public static class BundleInfo {
        
        private final String symbolicName;
        private final Version version;
        

		public BundleInfo(String symbolicName, Version version) {
            super();
            this.symbolicName = symbolicName;
            this.version = version;
        }

        public String getSymbolicName() {
            return symbolicName;
        }
        
        public Version getVersion() {
            return version;
        }
            
        public String getMvnVersion() {
            return toMvnVersion(version);
        }
        
    }
}
