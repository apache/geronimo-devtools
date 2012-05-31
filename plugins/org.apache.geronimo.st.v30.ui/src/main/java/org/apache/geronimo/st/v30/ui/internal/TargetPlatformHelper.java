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
package org.apache.geronimo.st.v30.ui.internal;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.regex.Matcher;

import org.apache.geronimo.st.v30.ui.Activator;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.pde.internal.core.PDECore;
import org.osgi.framework.Bundle;

/**
 * The TargetPlatformHelper uses Eclipse Target Platform API to query and set the default target platform in Eclipse.
 * 
 * TargetPlatformHelper uses reflection API to interact with the Eclipse Target Platform API since the Eclipse API have
 * into different packages starting in Eclipse 3.8. Using reflection allows us to have one version of the plugin that 
 * works with multiple Eclipse versions and makes building the code a bit easier.
 * 
 * @version $Rev$ $Date$
 */
public class TargetPlatformHelper  {

    private static final String LOCAL_TARGET_DIRECTORY = ".metadata/.plugins/org.eclipse.pde.core/.local_targets/";
    
    private static final String TARGET_FILE_EXTENSION = ".target";
    
    private static final String TARGET_FILE_LOCATIONS_PLACEHOLDER = "@locations_placeholder@";
    
    private static final String[] bundleDirectories = { "repository/org/apache/geronimo/specs",
        "repository/org/apache/geronimo/javamail/geronimo-javamail_1.4_mail",
        "repository/org/apache/geronimo/bundles/jaxb-impl", "repository/org/apache/geronimo/bundles/jstl",
        "repository/org/apache/geronimo/bundles/myfaces-bundle",
        "repository/org/apache/geronimo/framework/geronimo-jdbc",
        "repository/org/eclipse/osgi",
        "repository/org/osgi/org.osgi.compendium"};
    
    private final String LOCATION_LINE_TEMPLATE = "<location path=\"${server_location}GERONIMO_BUNDLE_FOLDER_NAME\" type=\"Directory\"/>";
    
    private final Method getNameMethod;
    private final Method getTargetDefinitionMethod;
    private final Method getWorkspaceTargetHandleMethod;
    private final Method getTargetsMethod;
    private final Method loadMethod;
    
    private Object targetPlatformService;

    private TargetPlatformHelper(String basePackage) throws Exception {
        Class<?> targetDefinitionClass = Class.forName(basePackage + ".ITargetDefinition");
        getNameMethod = targetDefinitionClass.getMethod("getName");
        
        Class<?> targetHandleClass = Class.forName(basePackage + ".ITargetHandle");
        getTargetDefinitionMethod = targetHandleClass.getMethod("getTargetDefinition");
        
        Class<?> targetPlatformServiceClass = Class.forName(basePackage + ".ITargetPlatformService");
        getWorkspaceTargetHandleMethod = targetPlatformServiceClass.getMethod("getWorkspaceTargetHandle");
        getTargetsMethod = targetPlatformServiceClass.getMethod("getTargets", IProgressMonitor.class);
        
        Class<?> loadTargetDefinitionClass =  Class.forName(basePackage + ".LoadTargetDefinitionJob");
        loadMethod = loadTargetDefinitionClass.getMethod("load", targetDefinitionClass);
        
        targetPlatformService = PDECore.getDefault().acquireService(basePackage + ".ITargetPlatformService");
        
        if (targetPlatformService == null) {
            throw new Exception("No TargetPlatformService");
        }
    }
    
    public static TargetPlatformHelper getTargetPlatformHelper() throws Exception {
        try {
            Class.forName("org.eclipse.pde.internal.core.target.provisional.ITargetDefinition");
            return new TargetPlatformHelper("org.eclipse.pde.internal.core.target.provisional");
        } catch (ClassNotFoundException e) {
            try {
                Class.forName("org.eclipse.pde.core.target.ITargetDefinition");
                return new TargetPlatformHelper("org.eclipse.pde.core.target");
            } catch (ClassNotFoundException ee) {
                throw new RuntimeException("Target Platform API are not available");
            }
        }
    }
    
    public Object findTargetHandle(String name) throws CoreException {
        Object[] targetHandles = getTargetHandles(targetPlatformService);
        if (targetHandles != null) {
            for (Object targetHandle : targetHandles) {
                Object targetDefinition = getTargetDefinition(targetHandle);
                if (targetDefinition != null) {
                    String targetName = getTargetDefinitionName(targetDefinition);
                    if (name.equals(targetName)) {
                        return targetHandle;
                    }
                }
            }
        }
        return null;
    }

    public void createTargetPlatform(String geronimoServerLocation) throws CoreException {
        // add or update string substitution variable named ${geronimo_30_server_location} 
        IStringVariableManager varManager = VariablesPlugin.getDefault().getStringVariableManager();
        IValueVariable v = varManager.getValueVariable(getServerLocationVariableName());
        if (v == null) {
            v = varManager.newValueVariable(getServerLocationVariableName(),
                                            getServerLocationVariableDescription(), 
                                            false, 
                                            geronimoServerLocation);
            varManager.addVariables(new IValueVariable[] { v });
        } else {
            v.setValue(geronimoServerLocation);
        }
                    
        // copy the target file for target platform into workspace
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IPath rootLocation = root.getLocation();
        String timestamp = Long.toString(System.currentTimeMillis());           
        String append = LOCAL_TARGET_DIRECTORY + timestamp + TARGET_FILE_EXTENSION;
        IPath destination = rootLocation.append(append);            
        
        URL template = getTargetTempateFile();
        
        copyFile(template, destination, getLocationEntryLists(geronimoServerLocation));
    }
    
    private void copyFile(URL source, IPath dest, String replacement) throws CoreException {
        Reader reader = null;
        Writer writer = null;
        
        try {
            try {
                reader = new InputStreamReader(source.openStream());
                
                StringBuilder builder = new StringBuilder();
                char [] buf = new char [2048];
                int count = 0;
                
                while ( (count = reader.read(buf)) >= 0) {
                    builder.append(buf, 0, count);
                }
                
                String template = builder.toString();
                String text = template.replace(TARGET_FILE_LOCATIONS_PLACEHOLDER, replacement);
                
                dest.toFile().getParentFile().mkdirs();
                writer = new FileWriter(dest.toFile());
                
                writer.write(text);
                
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (IOException ex) {
            throw new CoreException(new Status(IStatus.WARNING, Activator.PLUGIN_ID, Messages.copyTargetFileFailed, ex));
        }
    }
    
    private String getLocationEntryLists(String serverLocation) {
        StringBuffer buf = new StringBuffer(1024);
        buf.append(System.getProperty("line.separator"));
        for (String bundleDir : bundleDirectories) {
            File directory = new File(serverLocation, bundleDir);
            listFile(directory, buf, serverLocation);
        }
        return buf.toString();
    }

    private void listFile(File file, StringBuffer buf, String serverLocation) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subfile : files) {
                listFile(subfile, buf, serverLocation);
            }
        } else {
            String filename = file.getAbsolutePath();
            if (filename.endsWith(".jar")) {
                String parentPath = file.getParent();
                String bundleFolder = parentPath.substring(serverLocation.length());
                buf.append(getLocationLineTemplate().replaceAll("GERONIMO_BUNDLE_FOLDER_NAME", 
                                                                Matcher.quoteReplacement(bundleFolder)));
                buf.append(System.getProperty("line.separator"));
            }
        }
    }
    
    private String getLocationLineTemplate() {
        return LOCATION_LINE_TEMPLATE.replaceAll("server_location", getServerLocationVariableName());
    }
    
    private String getTargetTemplateFileName() {
        return "targets/Geronimo30.target";
    }
    
    private String getServerLocationVariableName() {
        return "geronimo30_server_location";
    }
    
    private String getServerLocationVariableDescription(){
        return Messages.serverLocationVariableDescription;
    }
    
    private URL getTargetTempateFile() {
        Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
        return bundle.getEntry(getTargetTemplateFileName());
    }
    
    public void switchTargetPlatform(Object targetHandle) throws CoreException {
        Object activeTargetHandle = getWorkspaceTargetHandle();
        if (activeTargetHandle == null || !activeTargetHandle.equals(targetHandle)) {
            Object targetDefinition = getTargetDefinition(targetHandle);
            if (targetDefinition != null) {
                setTargetPlatform(targetDefinition);
            }
        }
    }  
    
    // Key Target Platform reflection calls 

    private String getTargetDefinitionName(Object targetDefinition) {
        try {
            return (String) getNameMethod.invoke(targetDefinition);           
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not get target definition name", e, Activator.traceInternal);
            return null;
        } 
    }
    
    private Object getTargetDefinition(Object targetHandle) throws CoreException {
        try {
            return getTargetDefinitionMethod.invoke(targetHandle);    
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof CoreException) {
                throw (CoreException) cause;
            }
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, cause.getMessage(), cause));
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not get target definition", e, Activator.traceInternal);
            return null;
        } 
    }
            
    private Object[] getTargetHandles(Object targetPlatformService) {
        try {
            return (Object[]) getTargetsMethod.invoke(targetPlatformService, new Object[] { null });           
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not get platform targets", e, Activator.traceInternal);
            return null;
        } 
    }
    
    private Object getWorkspaceTargetHandle() throws CoreException {
        try {
            return getWorkspaceTargetHandleMethod.invoke(targetPlatformService);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof CoreException) {
                throw (CoreException) cause;
            }
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, cause.getMessage(), cause));        
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not get workspace target handle", e, Activator.traceInternal);
            return null;
        } 
    }
    
    private void setTargetPlatform(Object targetDefinition) {
        try {
            loadMethod.invoke(null, targetDefinition);
        } catch (Exception e) {
            Trace.trace(Trace.ERROR, "Could not set target platform", e, Activator.traceInternal);
        }        
    }
}
