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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;

import org.apache.geronimo.kernel.util.SelectorUtils;
import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.commands.TargetModuleIdNotFoundException;
import org.apache.geronimo.st.v30.core.internal.Messages;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.osgi.AriesHelper;
import org.apache.geronimo.st.v30.core.osgi.OsgiConstants;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.application.internal.operations.AppClientComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.application.internal.operations.EARComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.application.internal.operations.J2EEComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.EJBComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.jca.operations.ConnectorComponentExportDataModelProvider;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebComponentExportDataModelProvider;
import org.eclipse.jst.server.core.IEnterpriseApplication;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.jst.server.core.PublishUtil;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleFolder;
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.eclipse.wst.server.core.util.ProjectModule;
import org.eclipse.wst.server.core.util.PublishHelper;

/**
 * @version $Rev$ $Date$
 */
public class DeploymentUtils {

    public static final IPath STATE_LOC = Activator.getDefault().getStateLocation();

    private DeploymentUtils() {
        Trace.tracePoint("Entry/Exit", "DeploymentUtils.DeploymentUtils", Activator.traceCore); 
    }
    
    
    public static IPath generateExplodedConfiguration(IModule module, IPath outputPath) {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.generateExplodedConfiguration", module, outputPath);
        
        IPath output = outputPath.append(module.getName() + getModuleExtension(module));
        try {
            IModuleResource[] resources = getModuleResources(module);
            PublishUtil.publishSmart(resources, output, new NullProgressMonitor());
            if(GeronimoUtils.isEarModule(module)) {
                IEnterpriseApplication application = (IEnterpriseApplication) module.loadAdapter(IEnterpriseApplication.class, null);
                if( application != null ){
                    IModule[] children = application.getModules();
                     for (int i = 0; i < children.length; i++) {
                            IModule child = children[i];
                            IPath childPath = output.append(child.getName() + getModuleExtension(child));
                            IModuleResource[] childResources = getModuleResources(child);
                            PublishUtil.publishSmart(childResources, childPath, new NullProgressMonitor());
                            if(GeronimoUtils.isWebModule(child)) {
                                IWebModule webModule = (IWebModule) module.loadAdapter(IWebModule.class, null);
                                IModule[] libs = webModule.getModules();
                                IPath webLibPath = childPath.append("WEB-INF").append("lib");
                                for(int j = 0; j < libs.length; j++) {
                                    IModule lib = libs[j];
                                    IModuleResource[] libResources = getModuleResources(lib);
                                    PublishUtil.publishSmart(libResources, webLibPath.append(lib.getName() + getModuleExtension(lib)), new NullProgressMonitor());
                                }
                            }
                     }
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.generateExplodedConfiguration", output);
        return output;
    }
    
    
    public static IModuleResource[] getModuleResources(IModule module) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.getModuleResources", module);
    
        ProjectModule pm = (ProjectModule)module.loadAdapter(ProjectModule.class, null);
        if (pm != null) {
            Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getModuleResources", pm.members());
            return pm.members();
        }
    
        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getModuleResources", null);
        return null;
    }
    
    public static File getTargetFile(IServer server, IModule module) throws CoreException {        
        File file = null;
        IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
        if (gs.isRunFromWorkspace()) {
            //TODO Re-enable after DeployableModule supported in G
            //file = generateRunFromWorkspaceConfig(getModule());
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Run from workspace is unsupported."));
        } else {
            IPath outputDir = getServerDirectory(server);
            file = DeploymentUtils.createJarFile(module, outputDir);
        }
        return file;
    }
    
    public static IPath getServerDirectory(IServer server) {
        IPath outputDir = DeploymentUtils.STATE_LOC.append("server_" + server.getId());
        outputDir.toFile().mkdirs();
        return outputDir;
    }
    
    public static int getModuleState(IServer server, IModule module) {
        return server.getModuleState(new IModule[] {module});
    }
    
    private static String getModuleExtension(IModule module) {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.getModuleExtension", module);
    
        String extension = null;
        
        if (GeronimoUtils.isEarModule(module)) {
            extension = ".ear";
        }
        else if (GeronimoUtils.isWebModule(module)) {
            extension = ".war";
        }
        else if (GeronimoUtils.isRARModule(module)) {
            extension = ".rar";
        }
        else if (GeronimoUtils.isAppClientModule(module)) {
            extension = ".car";
        }
        else if (GeronimoUtils.isEBAModule(module)) {
            extension = OsgiConstants.APPLICATION_EXTENSION;
        }
        else if (GeronimoUtils.isCBAModule(module)) {
            extension = OsgiConstants.COMPOSITE_BUNDLE_EXTENSION;
        }
        else if (GeronimoUtils.isBundleModule(module)) {
            extension = OsgiConstants.BUNDLE_EXTENSION;
        }
        else if (GeronimoUtils.isFragmentBundleModule(module)) {
            extension = OsgiConstants.FRAGMENT_BUNDLE_EXTENSION;
        }
        else {
            extension = ".jar";
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getModuleExtension", extension);
        return extension;
    }


    public static File createJarFile(IModule module, IPath outputPath) throws CoreException {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.createJarFile", module, outputPath);

        IDataModel model = getExportDataModel(module);
        if (model == null) {
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "No export DataModel for " + module.getName(), null));
        }
        
        IVirtualComponent comp = ComponentCore.createComponent(module.getProject());

        //Here, specific extension name should be got, in case module has no standard JEE descriptor file included
        String extensionName = getModuleExtension(module);
            
        // TODO: Need to determine what properties to set for OSGi applications
        model.setProperty(J2EEComponentExportDataModelProvider.PROJECT_NAME, module.getProject().getName());
        model.setProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION, outputPath.append(module.getName()) + extensionName);

        model.setProperty(J2EEComponentExportDataModelProvider.COMPONENT, comp);
        model.setBooleanProperty(J2EEComponentExportDataModelProvider.OVERWRITE_EXISTING, true);
        model.setBooleanProperty(J2EEComponentExportDataModelProvider.RUN_BUILD, false);

        try {
            IStatus status = model.getDefaultOperation().execute(null, null);
            if (status != null && !status.isOK()) {
                throw new CoreException(status);
            }
        } catch (ExecutionException e) {
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.bind(Messages.moduleExportError, module.getName()), e)); 
        } 
        
        File exportedFile = new File(model.getStringProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION));
        if (exportedFile == null || !exportedFile.exists()) {
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.bind(Messages.moduleExportError, module.getName()))); 
        }
                      
        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.createJarFile", exportedFile);
        return exportedFile;
    }

    public static IDataModel getExportDataModel(IModule module) {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.getExportDataModel", module);

        String type = module.getModuleType().getId();
        if (IModuleConstants.JST_WEB_MODULE.equals(type)) {
            return DataModelFactory.createDataModel(new WebComponentExportDataModelProvider());
        } else if (IModuleConstants.JST_EJB_MODULE.equals(type)) {
            return DataModelFactory.createDataModel(new EJBComponentExportDataModelProvider());
        } else if (IModuleConstants.JST_EAR_MODULE.equals(type)) {
            return DataModelFactory.createDataModel(new EARComponentExportDataModelProvider());
        } else if (IModuleConstants.JST_CONNECTOR_MODULE.equals(type)) {
            return DataModelFactory.createDataModel(new ConnectorComponentExportDataModelProvider());
        } else if (IModuleConstants.JST_APPCLIENT_MODULE.equals(type)) {
            return DataModelFactory.createDataModel(new AppClientComponentExportDataModelProvider());
        }

        if (AriesHelper.isAriesInstalled()) {
            if (OsgiConstants.APPLICATION.equals(type)) {
                return DataModelFactory.createDataModel(OsgiConstants.APPLICATION_DATAMODEL_PROVIDER_ID);
            }
            else if (OsgiConstants.COMPOSITE_BUNDLE.equals(type)) {
                return DataModelFactory.createDataModel(OsgiConstants.COMPOSITE_BUNDLE_DATAMODEL_PROVIDER_ID);
            }
            else if (OsgiConstants.BUNDLE.equals(type)) {
                return DataModelFactory.createDataModel(OsgiConstants.BUNDLE_DATAMODEL_PROVIDER_ID);
            }
            else if (OsgiConstants.FRAGMENT_BUNDLE.equals(type)) {
                return DataModelFactory.createDataModel(OsgiConstants.FRAGMENT_BUNDLE_DATAMODEL_PROVIDER_ID);
            }
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getExportDataModel", (Object) null);
        return null;
    }
    
    
    public static TargetModuleID getTargetModuleID(IServer server, IModule module) throws TargetModuleIdNotFoundException {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.getTargetModuleID", module);
    
        String configId = ModuleArtifactMapper.getInstance().resolveArtifact(server, module);
        if(configId == null) {
            throw new TargetModuleIdNotFoundException("Could not do a local TargetModuleID lookup for module " + module.getName());
        }
        
        IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
        TargetModuleID moduleId = gs.getVersionHandler().createTargetModuleId(configId);
        Trace.tracePoint("Exit", Activator.traceCore, "DeploymentUtils.getTargetModuleID", moduleId);
        return moduleId;
    }


    public static TargetModuleID getTargetModuleID(DeploymentManager dm, String configId) throws TargetModuleIdNotFoundException {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.getTargetModuleID", dm, configId);

        try {
            TargetModuleID id = isInstalledModule(dm,configId);
            if (id != null) {
                Trace.tracePoint("Exit", Activator.traceCore, "DeploymentUtils.getTargetModuleID", id);
                return id;
            }
        } catch (Exception e) {
            Trace.trace(Trace.INFO, "DeploymentUtils.getTargetModuleID", e, Activator.logCore);
        }

        throw new TargetModuleIdNotFoundException("Could not find TargetModuleID with configId " + configId);
    }


    /**
     * This method determines the last known config id for an IModule that has been deployed to the server.  The
     * configId from the plan cannot be used as the project may have been previously deployed with a different
     * configId.  In which case the lookup is done through the ModuleArtifactMapper first.
     * 
     * @param module
     * @param server
     * @return For a given module associated with a given server, this method returns the last known configuration id.
     * @throws Exception 
     */
    public static String getLastKnownConfigurationId(IModule module, IServer server) throws Exception {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.getLastKnownConfigurationId", module, server);
        IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
        String currentId = gs.getVersionHandler().getConfigID(module);
        String publishedId = ModuleArtifactMapper.getInstance().resolveArtifact(server, module);
        String query = publishedId != null ? publishedId : currentId;
        
        Trace.trace(Trace.INFO, "currentConfigId = " + currentId + " previousConfigId = " + publishedId, Activator.traceCore);
        
        DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(server);        
        TargetModuleID[] ids = null;
        try {
            ids = dm.getAvailableModules(null, dm.getTargets());  
            
            if (getModuleId(ids, query) != null) {
                Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getLastKnownConfigurationId", query);
                return query;
            }
            
            if (query != currentId && getModuleId(ids, currentId) != null) {
                Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getLastKnownConfigurationId", currentId);
                return currentId;                
            }
        } catch (Exception e) {
            Trace.trace(Trace.INFO, "DeploymentUtils.getLastKnownConfigurationId", e, Activator.logCore);
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getLastKnownConfigurationId", (Object) null);
        return null;
    }
    
    public static boolean isMatchingDelta(IModuleResourceDelta delta, List<String> includes, List<String> excludes) {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.isMatchingDelta", delta, includes, excludes);
        
        if (delta == null) {
            Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.isMatchingDelta", delta, false);
            return false;
        } 
        
        IModuleResource resource = delta.getModuleResource();
        if (resource instanceof IModuleFile) {
            IModuleFile moduleFile = (IModuleFile) resource;
            String name = moduleFile.getName();
            IPath relativePath = moduleFile.getModuleRelativePath();
            if (relativePath != null && relativePath.toOSString().length() > 0) {
                name = relativePath.toOSString() + File.separator + moduleFile.getName();
            }
            if (hasMatch(name, excludes)) {
                Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.isMatchingDelta", "Excluded", name);
                return false;
            }
            if (hasMatch(name, includes)) {
                // we have a match - keep checking
            } else {
                Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.isMatchingDelta", "Not included", name);
                return false;
            }
        } else if (resource instanceof IModuleFolder) {
            IModuleResourceDelta[] deltaArray = delta.getAffectedChildren();
            for (IModuleResourceDelta childDelta : deltaArray) {
                if (isMatchingDelta(childDelta, includes, excludes)) {
                    // we have a match - keep checking
                } else {
                    Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.isMatchingDelta", delta, false);
                    return false;
                }
            }
        }

        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.isMatchingDelta", delta, true);
        return true;
    }
    
    private static boolean hasMatch(String name, List<String> patterns) {
        for (String pattern : patterns) {
            if (SelectorUtils.matchPath(pattern, name)) {
                return true;
            }
        }
        return false;
    }     
    
    public static void publishDelta(IModuleResourceDelta delta, IPath publishPath, List<IStatus> statusList) {
        IModuleResource resource = delta.getModuleResource();
        int kind = delta.getKind();

        if (resource instanceof IModuleFile) {
            // it's a file        
            IPath filePath = publishPath.append(resource.getModuleRelativePath()).append(resource.getName());
            File file = filePath.toFile();
            if (kind == IModuleResourceDelta.REMOVED) {
                if (file.exists() && !file.delete()) {
                    statusList.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "Unable to delete file: " + file.getAbsolutePath(), null));
                }
            } else {
                File parentFile = file.getParentFile();
                if (parentFile != null && !parentFile.exists()) {
                    if (!parentFile.mkdirs()) {
                        statusList.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "Unable to create parent directory: " + parentFile.getAbsolutePath(), null));
                        return;
                    }
                }
                
                IModuleFile moduleFile = (IModuleFile) resource;
                try {
                    copyFile(moduleFile, file);
                } catch (CoreException e) {
                    statusList.add(e.getStatus());
                } catch (IOException e) {
                    statusList.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error copying file " + file.getAbsolutePath(), e));
                }
            }
        } else if (resource instanceof IModuleFolder) {
            // it's directory
            if (kind == IModuleResourceDelta.ADDED) {
                IPath filePath = publishPath.append(resource.getModuleRelativePath()).append(resource.getName());
                File file = filePath.toFile();
                if (!file.exists() && !file.mkdirs()) {
                    statusList.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "Unable to create directory: " + file.getAbsolutePath(), null));
                    return;
                }
            }

            IModuleResourceDelta[] childDeltas = delta.getAffectedChildren();
            if (childDeltas != null) {
                for (IModuleResourceDelta childDelta : childDeltas) {
                    publishDelta(childDelta, publishPath, statusList);
                }
            }

            if (kind == IModuleResourceDelta.REMOVED) {
                IPath filePath = publishPath.append(resource.getModuleRelativePath()).append(resource.getName());
                File file = filePath.toFile();
                if (file.exists() && !file.delete()) {
                    statusList.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "Unable to delete directory: " + file.getAbsolutePath(), null));
                }
            }
        }
    }
    
    private static void copyFile(IModuleFile moduleFile, File target) throws CoreException, IOException {
        byte[] buffer = new byte[10 * 1024];
        int bytesRead;    
        InputStream in = null;
        FileOutputStream out = null;
        try {
            IFile srcIFile = (IFile) moduleFile.getAdapter(IFile.class);
            if (srcIFile != null) {
                in = srcIFile.getContents();
            } else {
                File srcFile = (File) moduleFile.getAdapter(File.class);
                in = new FileInputStream(srcFile);
            }
        
            out = new FileOutputStream(target);
            
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            if (in != null) {
                try { in.close(); } catch (IOException ignore) {}
            }
            if (out != null) {
                try { out.close(); } catch (IOException ignore) {}
            }
        }
    }
    
    public static boolean isInstalledModule(IServer server, String configId) {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.isInstalledModule", server, configId);
        boolean isInstalled = false;
        DeploymentManager dm;
        try {
            dm = DeploymentCommandFactory.getDeploymentManager(server);
            TargetModuleID id = isInstalledModule(dm, configId);
            isInstalled = (id != null);
        } catch (Exception e) {
            Trace.trace(Trace.INFO, "DeploymentUtils.isInstalledModule", e, Activator.logCore);
            isInstalled = false;
        }
        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.isInstalledModule", isInstalled);
        return isInstalled;
    }
        
    public static TargetModuleID isInstalledModule(DeploymentManager dm, String configId) throws TargetException {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.isInstalledModule", dm, configId);
        TargetModuleID[] ids = dm.getAvailableModules(null, dm.getTargets());
        TargetModuleID moduleId = getModuleId(ids, configId);
        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.isInstalledModule", moduleId);
        return moduleId;
    }
    
    private static TargetModuleID getModuleId(TargetModuleID[] ids, String configId) {
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                if (ids[i].getModuleID().equals(configId)) {
                    return ids[i];
                }
            }
        }
        return null;
    }
    
    public static String getConfigId(IServer server, IModule module) throws CoreException {
        String configId = ModuleArtifactMapper.getInstance().resolveArtifact(server, module);

        if (configId == null) {
            IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
            try {
                configId = gs.getVersionHandler().getConfigID(module);
            } catch (Exception e) {
                throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Unable to determine configId for module: " + module.getName(), e));
            }
            
            if (configId == null) {
                throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Unable to determine configId for module: " + module.getName()));
            }
        }
        
        return configId;
    }
        
    public static IModuleResource[] getChangedClassResources(IModuleResourceDelta[] deltaArray) {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.getChangedClassResources", deltaArray);
        List<IModuleResource> changedClassResources = new ArrayList<IModuleResource>();
        // collect only changed classes resources
        if (collectChangedClassResources(deltaArray, changedClassResources) && !changedClassResources.isEmpty()) {
            // modified class resources were only found
            IModuleResource[] resources = new IModuleResource[changedClassResources.size()];
            changedClassResources.toArray(resources);
            Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getChangedClassResources", resources);
            return resources;
        } else {
            // added or removed resources or non-class resources were found
            Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.getChangedClassResources", "Added or removed resources or non-class resources were found");
            return null;
        }
    }
    
    private static boolean collectChangedClassResources(IModuleResourceDelta[] deltaArray, List<IModuleResource> list) {
        for (IModuleResourceDelta delta : deltaArray) {
            int kind = delta.getKind();        
            if (kind == IModuleResourceDelta.ADDED || kind == IModuleResourceDelta.REMOVED) {
                return false;
            }
            IModuleResource resource = delta.getModuleResource();
            if (resource instanceof IModuleFile) {
                String name = resource.getName();
                if (!name.endsWith(".class")) {
                    return false;
                }
                if (kind == IModuleResourceDelta.CHANGED) {
                    list.add(resource);
                }
            } else if (resource instanceof IModuleFolder) {
                IModuleResourceDelta[] childDeltaArray = delta.getAffectedChildren();
                if (!collectChangedClassResources(childDeltaArray, list)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /**
     * Returns true only if the delta contains at least one CHANGED class resource. 
     */
    public static boolean containsChangedClassResources(IModuleResourceDelta[] deltaArray) {
        for (IModuleResourceDelta delta : deltaArray) {
            int kind = delta.getKind();                    
            IModuleResource resource = delta.getModuleResource();
            if (resource instanceof IModuleFile) {
                String name = resource.getName();
                if (name.endsWith(".class") && kind == IModuleResourceDelta.CHANGED) {
                    return true;
                }
            } else if (resource instanceof IModuleFolder) {
                IModuleResourceDelta[] childDeltaArray = delta.getAffectedChildren();
                if (containsChangedClassResources(childDeltaArray)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static File createChangeSetFile(IModuleResource[] resources) {
        Trace.tracePoint("Entry", Activator.traceCore, "DeploymentUtils.createChangeSetFile", resources);
        
        File tmpDir = STATE_LOC.toFile();
        
        File file = null;
        try {
            file = File.createTempFile("changeset", ".jar", tmpDir);
        } catch (IOException e) {
            Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.createChangeSetFile", e);
            return null;
        }
        
        PublishHelper publishHelper = new PublishHelper(tmpDir);
        IStatus[] statusArray = publishHelper.publishZip(resources, new Path(file.getAbsolutePath()), null);
        if (statusArray != null) {
            for (IStatus status : statusArray) {
                if (!status.isOK()) {
                    file.delete();
                    Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.createChangeSetFile", status);
                    return null;
                }
            }
        }
        
        Trace.tracePoint("Exit ", Activator.traceCore, "DeploymentUtils.createChangeSetFile", file);
        return file;
    }

}
