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
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.TargetException;

import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.commands.TargetModuleIdNotFoundException;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.osgi.AriesHelper;
import org.apache.geronimo.st.v30.core.osgi.OsgiConstants;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.eclipse.wst.server.core.model.IModuleResource;
import org.eclipse.wst.server.core.util.ProjectModule;
import org.eclipse.wst.server.core.model.IModuleResourceDelta;
import org.eclipse.wst.server.core.model.IModuleFile;
import org.eclipse.wst.server.core.model.IModuleFolder;

/**
 * @version $Rev$ $Date$
 */
public class DeploymentUtils {

    public static final IPath STATE_LOC = Activator.getDefault().getStateLocation();

    private DeploymentUtils() {
        Trace.tracePoint("Entry/Exit", "DeploymentUtils.DeploymentUtils"); 
    }
    
    
    public static IPath generateExplodedConfiguration(IModule module, IPath outputPath) {
        Trace.tracePoint("Entry", "DeploymentUtils.generateExplodedConfiguration", module, outputPath);
        
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
        
        Trace.tracePoint("Exit ", "DeploymentUtils.generateExplodedConfiguration", output);
        return output;
    }
    
    
    public static IModuleResource[] getModuleResources(IModule module) throws CoreException {
        Trace.tracePoint("Entry", "DeploymentUtils.getModuleResources", module);
    
        ProjectModule pm = (ProjectModule)module.loadAdapter(ProjectModule.class, null);
        if (pm != null) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleResources", pm.members());
            return pm.members();
        }
    
        Trace.tracePoint("Exit ", "DeploymentUtils.getModuleResources", null);
        return null;
    }
    
    
    private static String getModuleExtension(IModule module) {
        Trace.tracePoint("Entry", "DeploymentUtils.getModuleExtension", module);
    
        if (GeronimoUtils.isEarModule(module)) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", ".ear");
            return ".ear";
        }
        else if (GeronimoUtils.isWebModule(module)) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", ".war");
            return ".war";
        }
        else if (GeronimoUtils.isRARModule(module)) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", ".rar");
            return ".rar";
        }
        else if (GeronimoUtils.isAppClientModule(module)) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", ".car");
            return ".car";
        }
        else if (GeronimoUtils.isEBAModule(module)) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", OsgiConstants.APPLICATION_EXTENSION);
            return OsgiConstants.APPLICATION_EXTENSION;
        }
        else if (GeronimoUtils.isCBAModule(module)) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", OsgiConstants.COMPOSITE_BUNDLE_EXTENSION);
            return OsgiConstants.COMPOSITE_BUNDLE_EXTENSION;
        }
        else if (GeronimoUtils.isBundleModule(module)) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", OsgiConstants.BUNDLE_EXTENSION);
            return OsgiConstants.BUNDLE_EXTENSION;
        }

        Trace.tracePoint("Exit ", "DeploymentUtils.getModuleExtension", ".jar");
        return ".jar";
    }


    public static File createJarFile(IModule module, IPath outputPath) {
        Trace.tracePoint("Entry", "DeploymentUtils.createJarFile", module, outputPath);

        IDataModel model = getExportDataModel(module);

        if (model != null) {

            IVirtualComponent comp = ComponentCore.createComponent(module.getProject());

            //Here, specific extension name should be got, in case module has no standard JEE descriptor file included
            String extensionName = getModuleExtension(module);
            
            // TODO: Need to determine what properties to set for OSGi applications
            model.setProperty(J2EEComponentExportDataModelProvider.PROJECT_NAME, module.getProject());
            model.setProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION, outputPath.append(module.getName())
                    + extensionName);

            model.setProperty(J2EEComponentExportDataModelProvider.COMPONENT, comp);
            model.setBooleanProperty(J2EEComponentExportDataModelProvider.OVERWRITE_EXISTING, true);
            model.setBooleanProperty(J2EEComponentExportDataModelProvider.RUN_BUILD, false);

            if (model != null) {
                try {
                    model.getDefaultOperation().execute(null, null);
                    Trace.tracePoint("Exit ", "DeploymentUtils.createJarFile",new File(model.getStringProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION)));
                    return new File(model.getStringProperty(J2EEComponentExportDataModelProvider.ARCHIVE_DESTINATION));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

        Trace.tracePoint("Exit ", "DeploymentUtils.createJarFile", null);
        return null;
    }


    public static IDataModel getExportDataModel(IModule module) {
        Trace.tracePoint("Entry", "DeploymentUtils.getExportDataModel", module);

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
        }

        Trace.tracePoint("Exit ", "DeploymentUtils.getExportDataModel", null);
        return null;
    }
    
    
    public static TargetModuleID getTargetModuleID(IServer server, IModule module) throws TargetModuleIdNotFoundException {
        Trace.tracePoint("Entry", "DeploymentUtils.getTargetModuleID", module);
    
        String configId = ModuleArtifactMapper.getInstance().resolve(server, module);
        if(configId == null) {
            throw new TargetModuleIdNotFoundException("Could not do a local TargetModuleID lookup for module " + module.getName());
        }
        
        IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
    
        Trace.tracePoint("Exit ", "DeploymentUtils.generateExplodedConfiguration",gs.getVersionHandler().createTargetModuleId(configId));
        return gs.getVersionHandler().createTargetModuleId(configId);
    }


    public static TargetModuleID getTargetModuleID(DeploymentManager dm, String configId) throws TargetModuleIdNotFoundException {
        Trace.tracePoint("Entry", "DeploymentUtils.getTargetModuleID", dm, configId);

        try {
            TargetModuleID id = isInstalledModule(dm,configId);
            if (id!=null) {
                Trace.tracePoint("Exit ", "DeploymentUtils.getTargetModuleID", id);
                return id;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (TargetException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
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
        Trace.tracePoint("Entry", "DeploymentUtils.getLastKnownConfigurationId", module, server);
        
        IGeronimoServer gs = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
        String currentId = gs.getVersionHandler().getConfigID(module);
        String publishedId = ModuleArtifactMapper.getInstance().resolve(server, module);
        String query = publishedId != null ? publishedId : currentId;
        
        Trace.trace(Trace.INFO, "currentConfigId = " + currentId + " previousConfigId = " + publishedId);
        
        DeploymentManager dm = DeploymentCommandFactory.getDeploymentManager(server);
        
        try {
            getTargetModuleID(dm, query);
            Trace.tracePoint("Exit ", "DeploymentUtils.getLastKnownConfigurationId", query);
            return query;
        } catch (TargetModuleIdNotFoundException e) {
            Trace.trace(Trace.INFO, e.getMessage());
        }
        
        if(query != currentId) {
            try {
                getTargetModuleID(dm, currentId);
                Trace.tracePoint("Exit ", "DeploymentUtils.getLastKnownConfigurationId", currentId);
                return currentId;
            } catch (TargetModuleIdNotFoundException e) {
                Trace.trace(Trace.INFO, e.getMessage());
            }
        }
        
        Trace.tracePoint("Exit ", "DeploymentUtils.getLastKnownConfigurationId", null);
        return null;
    }
    
    
    public static List<IModuleResourceDelta> getAffectedJSPFiles(IModuleResourceDelta delta) {
        Trace.tracePoint("Entry", "DeploymentUtils.getAffectedJSPFiles", delta);

        if (delta == null) {
            Trace.tracePoint("Exit ", "DeploymentUtils.getAffectedJSPFiles", null);
            return null;
        }

        IModuleResource resource = delta.getModuleResource();
        List<IModuleResourceDelta> fileList = new ArrayList<IModuleResourceDelta>();

        if (resource instanceof IModuleFile) {
            IModuleFile moduleFile = (IModuleFile)resource;
            if (moduleFile.getName().endsWith(".jsp")) {
                fileList.add(delta);
            }
            else {
                Trace.tracePoint("Exit ", "DeploymentUtils.getAffectedJSPFiles", null);
                return null;   //not only jsp changed
            }
        }
        else if (resource instanceof IModuleFolder) {
            IModuleResourceDelta[] deltaArray = delta.getAffectedChildren();
            for (IModuleResourceDelta childDelta : deltaArray) {
                List<IModuleResourceDelta> deltaChildren = getAffectedJSPFiles(childDelta);
                if (deltaChildren != null) fileList.addAll(deltaChildren);
                else {
                    Trace.tracePoint("Exit ", "DeploymentUtils.getAffectedJSPFiles", null);
                    return null;
                }
            }
        }

        Trace.tracePoint("Exit ", "DeploymentUtils.getAffectedJSPFiles", fileList);
        return fileList;
    }
    
    
    public static boolean isInstalledModule(IServer server, String configId) {
        Trace.tracePoint("Entry", "DeploymentUtils.isInstalledModule", server, configId);
    
        DeploymentManager dm;
        try {
            dm = DeploymentCommandFactory.getDeploymentManager(server);
            TargetModuleID id=isInstalledModule(dm,configId);
            if (id==null) {
                Trace.tracePoint("Exit ", "DeploymentUtils.isInstalledModule", false);
                return false;
            } 
            else {
                Trace.tracePoint("Exit ", "DeploymentUtils.isInstalledModule", true);
                return true;
            }
        } catch (CoreException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (TargetException e) {
            e.printStackTrace();
            return false;
        }
        
    }
    
    
    private static TargetModuleID isInstalledModule(DeploymentManager dm, String configId) throws CoreException, IllegalStateException, TargetException{
        Trace.tracePoint("Entry", "DeploymentUtils.isInstalledModule", dm, configId);
        
        TargetModuleID[] ids = dm.getAvailableModules(null, dm.getTargets());
        if(ids == null) {
            Trace.tracePoint("Exit ", "DeploymentUtils.isInstalledModule", null);
            return null;
        }
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                if (ids[i].getModuleID().equals(configId)) {
                    Trace.trace(Trace.INFO, "Found configuration " + configId +  " on server.");
                    Trace.tracePoint("Exit ", "DeploymentUtils.isInstalledModule", ids[i]);
                    return ids[i];
                }
            }
        }
        
        Trace.tracePoint("Exit ", "DeploymentUtils.isInstalledModule", null);
        return null;
    }
}
