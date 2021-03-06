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
package org.apache.geronimo.st.v30.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.geronimo.st.v30.core.internal.Messages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.server.core.model.RuntimeDelegate;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoRuntimeDelegate extends RuntimeDelegate implements IGeronimoRuntime {

    private static final String PROP_VM_INSTALL_TYPE_ID = "vm-install-type-id";

    private static final String PROP_VM_INSTALL_ID = "vm-install-id";

    public static final String RUNTIME_SOURCE= "runtime.source";

    public static final int NO_IMAGE = 0;

    public static final int INCORRECT_VERSION = 1;

    public static final int PARTIAL_IMAGE = 2;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jst.server.core.IJavaRuntime#getVMInstall()
     */
    public IVMInstall getVMInstall() {
        if (getVMInstallTypeId() == null)
            return JavaRuntime.getDefaultVMInstall();
        try {
            IVMInstallType vmInstallType = JavaRuntime.getVMInstallType(getVMInstallTypeId());
            IVMInstall[] vmInstalls = vmInstallType.getVMInstalls();
            int size = vmInstalls.length;
            String id = getVMInstallId();
            for (int i = 0; i < size; i++) {
                if (id.equals(vmInstalls[i].getId()))
                    return vmInstalls[i];
            }
        }
        catch (Exception e) {
            // ignore
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.RuntimeDelegate#validate()
     */
    public IStatus validate() {
        IStatus status = super.validate();

        if (!status.isOK()) {
            return status;
        }

        if (getVMInstall() == null) {
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, Messages.errorJRE, null);
        }
        
        IPath runtimeLoc = getRuntime().getLocation();

        // check for server file structure
        int limit = 2;
        int count = 0;
        if (runtimeLoc.append("lib").toFile().exists()) {
            count++;
        }
        if (runtimeLoc.append("repository").toFile().exists()) {
            count++;
        }
	              
        if (count == 0) {
            return new Status(IStatus.ERROR, Activator.PLUGIN_ID, NO_IMAGE, "", null);
        }
        
        if (count < limit) {        	
			// part of a server image was found, don't let install happen
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					PARTIAL_IMAGE, Messages.bind(Messages.missingContent,
							getRuntime().getName()), null);
        }
        
        String detectedVersion = detectVersion();
        if (detectedVersion == null) {
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID,
					INCORRECT_VERSION, Messages.bind(Messages.noVersion,
							getRuntime().getName()), null);
		}

        if (!detectedVersion.startsWith(getRuntime().getRuntimeType().getVersion())) {
        	String runtimeVersion = getRuntime().getRuntimeType().getVersion();
			String message = NLS.bind(Messages.incorrectVersion,
					new String[] { getRuntime().getName(),
					        runtimeVersion,
							detectedVersion });
            // GD332 allow version > if it's a SNAPSHOT
            int severity = IStatus.ERROR;
            if (detectedVersion.endsWith("-SNAPSHOT")
                    && detectedVersion.compareTo(runtimeVersion) >= 0) {
                severity = IStatus.WARNING;
            }
            return new Status(severity, Activator.PLUGIN_ID, INCORRECT_VERSION,
                    message, null);
		}

        return Status.OK_STATUS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.wst.server.core.model.RuntimeDelegate#setDefaults(org.eclipse.core.runtime.IProgressMonitor)
     */
    public void setDefaults(IProgressMonitor monitor) {
        IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
        setVMInstall(vmInstall.getVMInstallType().getId(), vmInstall.getId());
    }

    /* (non-Javadoc)
     * @see org.apache.geronimo.st.v30.core.IGeronimoRuntime#getRuntimeSourceLocation()
     */
    public IPath getRuntimeSourceLocation() {
        String source = getAttribute(RUNTIME_SOURCE, (String) null);
        return (source != null) ? new Path(source) : null;
    }

    public void setRuntimeSourceLocation(String path) {
        setAttribute(RUNTIME_SOURCE, path);
    }

    /**
     * @return
     */
    public String detectVersion() {

        URL systemjarURL = null;

        //
        // Check lib directory first
        //
        File libDir = getRuntime().getLocation().append("lib").toFile();
        if (libDir.exists()) {
            File[] libs = libDir.listFiles();
            for (int i = 0; i < libs.length; i++) {
                if (libs[i].getName().startsWith("geronimo-system")) {
                    try {
                        systemjarURL = libs[i].toURL();
                        break;
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // 
        // Check pre-2.1 repository if necessary
        //
        if (systemjarURL == null) {
            File systemDir = getRuntime().getLocation().append("repository/org/apache/geronimo/modules/geronimo-system").toFile();
            if (systemDir.exists() && systemDir.isDirectory() && systemDir.canRead()) {
                List<File> dirFiles = scanDirectory(systemDir);
                for (File jarFile : dirFiles) {
                    if (jarFile.getName().startsWith("geronimo-system") && jarFile.getName().endsWith("jar")) {
                        try {
                            systemjarURL = jarFile.toURL();
                            break;
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // 
        // Check 2.1 repository if necessary
        //
        if (systemjarURL == null) {
            File systemDir = getRuntime().getLocation().append("repository/org/apache/geronimo/framework/geronimo-system").toFile();
            if (systemDir.exists() && systemDir.isDirectory() && systemDir.canRead()) {
                List<File> dirFiles = scanDirectory(systemDir);
                for (File jarFile : dirFiles) {
                    if (jarFile.getName().startsWith("geronimo-system") && jarFile.getName().endsWith("jar")) {
                        try {
                            systemjarURL = jarFile.toURL();
                            break;
                        }
                        catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        if (systemjarURL != null) {
        	try {
				String version = null;
				JarFile jar = new JarFile(systemjarURL.getFile());
				Enumeration<JarEntry> entries = jar.entries();
				while(entries.hasMoreElements()){
					JarEntry entry = entries.nextElement();
					if (entry.getName().indexOf("geronimo-version.properties")!=-1 ||
							entry.getName().indexOf("server-version.properties")!=-1 ){
						InputStream is = jar.getInputStream(entry);
						Properties properties = new Properties();
						properties.load(is);
						 version = properties.getProperty("version");
						 is.close();
					}
				}
				jar.close();
				return version;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return null;
    }

    /**
     * @return
     */
    public String getInstallableTomcatRuntimeId() {
        return "org.apache.geronimo.runtime.tomcat." + getRuntime().getRuntimeType().getVersion().replaceAll("\\.", "");
    }

    /**
     * @return
     */
    public String getInstallableJettyRuntimeId() {
        return "org.apache.geronimo.runtime.jetty." + getRuntime().getRuntimeType().getVersion().replaceAll("\\.", "");
    }

    /**
     * @param vmInstall
     */
    public void setVMInstall(IVMInstall vmInstall) {
        if (vmInstall == null) {
            setVMInstall(null, null);
        }
        else
            setVMInstall(vmInstall.getVMInstallType().getId(), vmInstall.getId());
    }

    /**
     * @param typeId
     * @param id
     */
    public void setVMInstall(String typeId, String id) {
        if (typeId == null)
            setAttribute(PROP_VM_INSTALL_TYPE_ID, (String) null);
        else
            setAttribute(PROP_VM_INSTALL_TYPE_ID, typeId);

        if (id == null)
            setAttribute(PROP_VM_INSTALL_ID, (String) null);
        else
            setAttribute(PROP_VM_INSTALL_ID, id);
    }

    /**
     * @return
     */
    public String getVMInstallTypeId() {
        return getAttribute(PROP_VM_INSTALL_TYPE_ID, (String) null);
    }

    /**
     * @return
     */
    public String getVMInstallId() {
        return getAttribute(PROP_VM_INSTALL_ID, (String) null);
    }

    /**
     * @return
     */
    public boolean isUsingDefaultJRE() {
        return getVMInstallTypeId() == null;
    }

    private static List<File> scanDirectory(File dir) {
        List<File> dirFiles = new ArrayList<File>();
        scanDirectory(dir, dirFiles);
        return dirFiles;
    }

    private static void scanDirectory(File dir, List<File> dirFiles) {
        File[] files = dir.listFiles();
        for (int ii = 0; ii < files.length; ii++) {
            if (files[ii].isDirectory()) {
                scanDirectory(files[ii], dirFiles);
            }
            else {
                dirFiles.add(files[ii]);    
            }
        }
    }

}