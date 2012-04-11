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
import java.util.HashMap;
import java.util.Map;

import org.apache.geronimo.st.v30.core.base.Artifact;
import org.apache.geronimo.st.v30.core.base.Bundle;
import org.apache.geronimo.st.v30.core.base.ModuleSet;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.apache.geronimo.st.v30.core.osgi.AriesHelper;
import org.apache.geronimo.st.v30.core.persist.PersistenceManager;
import org.apache.geronimo.st.v30.core.persist.factory.PersistenceManagerFactory;
import org.apache.geronimo.st.v30.core.persist.factory.impl.FilePersistenceManagerFactory;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.util.SocketUtil;
import org.osgi.framework.Version;


/**
 * @version $Rev$ $Date$
 */
public class ModuleArtifactMapper {
    private static PersistenceManagerFactory<File, File> factory = FilePersistenceManagerFactory.getInstance();
    private static ModuleArtifactMapper instance = new ModuleArtifactMapper();
    private static final String ARTIFACT_FILE_NAME = "serverArtifacts.info";   
    private static final String BUNDLE_FILE_NAME = "serverBundles.info";
    
    private Map<File, ModuleSet<Artifact>> serverArtifacts = null;
    private Map<File, ModuleSet<Bundle>> serverBundles = null;
    private PersistenceManager<File, File> persistenceMgr;
    
    private ModuleArtifactMapper() {
        try {
            if(this.serverArtifacts == null) this.serverArtifacts = new HashMap<File, ModuleSet<Artifact>>();
            if(this.serverBundles == null) this.serverBundles = new HashMap<File, ModuleSet<Bundle>>();
            if(this.persistenceMgr == null) {
                if(factory == null) factory = FilePersistenceManagerFactory.getInstance();
                this.persistenceMgr = factory.create(ARTIFACT_FILE_NAME, ARTIFACT_FILE_NAME);
            }
    
            load();
        } catch(Exception e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
        }
    }

    public static ModuleArtifactMapper getInstance() {
        return instance;
    }

    private static String getId(IModule module) {
        String id = module.getId();
        if (id == null || id.length() == 0) {
            throw new IllegalStateException("Module has no id: " + module);
        }
        return id;
    }
    
    synchronized public void addArtifactEntry(IServer server, IModule module, String configId) {
        ModuleSet<Artifact> artifacts = getServerArtifacts(server);
        if (artifacts != null) {
            artifacts.add(new Artifact(getId(module), configId)); 
        }       
    }
    
    synchronized public void addBundleEntry(IServer server, IModule module, long bundleId, int bundleStartLevel) {
        ModuleSet<Bundle> bundles = getServerBundles(server);
        if(bundles != null) {
            String symbolicName = AriesHelper.getSymbolicName(module);
            Version version = AriesHelper.getVersion(module);
            Bundle bundle = new Bundle(getId(module), symbolicName, version, bundleId, bundleStartLevel);
            bundles.remove(bundle);
            bundles.add(bundle);
        }
    }
    
    synchronized public void removeArtifactEntry(IServer server, IModule module) {
        ModuleSet<Artifact> artifacts = getServerArtifacts(server);
        if (artifacts != null) {
            try {
                artifacts.remove("projectName", getId(module));
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
            } 
        }       
    }
    
    synchronized public boolean removeBundleEntry(IServer server, IModule module) {
        ModuleSet<Bundle> bundles = getServerBundles(server);
        if(bundles != null) {
            try {
                return bundles.remove("projectName", getId(module));
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
            }
        }
        return false;
    }
    synchronized public boolean removeBundleEntryBySymbolicNameAndVersion(IServer server, String symbolicName, Version version) {
        String[] fieldNames = {"symbolicName", "version"};
        Object[] values = {symbolicName, version};
        ModuleSet<Bundle> bundles = getServerBundles(server);
        if(bundles != null) {
            try {
                return bundles.remove(fieldNames, values);
            } catch (Exception e) {
                Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
            }
        }
        return false;
    }
    
    synchronized public boolean removeBundleEntryById(IServer server, long bundleId) {
        if(bundleId != -1) {
            ModuleSet<Bundle> bundles = getServerBundles(server);
            if(bundles != null) {
                try {
                    return bundles.remove("id", bundleId);
                } catch (Exception e) {
                    Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
                }
            }
        }
        return false;
    }
    
    synchronized public String resolveArtifact(IServer server, IModule module) {  
        if (module != null) {
            ModuleSet<Artifact> artifacts = getServerArtifacts(server);
            if (artifacts != null) {
                try {
                    Artifact artifact = artifacts.query("projectName", getId(module));
                    if(artifact != null) return artifact.getConfigId();
                } catch (Exception e) {
                    Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
                }
            }
        }
        return null;
     }
          
    synchronized public Bundle resolveBundle(IServer server, IModule module) {
        if(module != null) {
            ModuleSet<Bundle> bundles = getServerBundles(server);
            if(bundles != null) {
                try {
                    return bundles.query("projectName", getId(module));
                } catch (Exception e) {
                    Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
                }
            }
        }
        return null;
    }
    
    synchronized public Bundle resolveBundleById(IServer server, long bundleId) {
        if(bundleId != -1) {
            ModuleSet<Bundle> bundles = getServerBundles(server);
            if(bundles != null) {
                try {
                    return bundles.query("id", bundleId);
                } catch (Exception e) {
                    Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
                }
            }
        }
        return null;
    }
    
    synchronized public ModuleSet<Artifact> getServerArtifacts(IServer server) {
        if (!SocketUtil.isLocalhost(server.getHost())) {
            return null;
        }            
        
        File runtimeLoc = server.getRuntime().getLocation().toFile();
        ModuleSet<Artifact> artifacts = this.serverArtifacts.get(runtimeLoc);
        if (artifacts == null) {
            artifacts = new ModuleSet<Artifact>();
            this.serverArtifacts.put(runtimeLoc, artifacts);
        }
        
        return artifacts;        
    }
    
    synchronized public ModuleSet<Bundle> getServerBundles(IServer server) {
        if (!SocketUtil.isLocalhost(server.getHost())) {
            return null;
        }            
        
        File runtimeLoc = server.getRuntime().getLocation().toFile();
        ModuleSet<Bundle> bundles = this.serverBundles.get(runtimeLoc);
        if (bundles == null) {
            bundles = new ModuleSet<Bundle>();
            this.serverBundles.put(runtimeLoc, bundles);
        }
        
        return bundles;  
    }
    
    synchronized public void save() throws Exception {
        /* save serverArifacts in the format of object file */
        this.persistenceMgr.setDestination(ARTIFACT_FILE_NAME);
        this.persistenceMgr.save(this.serverArtifacts);
        /* save serverBundles in the format of object file */
        this.persistenceMgr.setDestination(BUNDLE_FILE_NAME);
        this.persistenceMgr.save(this.serverBundles);
    }
    
    synchronized public int getServerBundleDefaultStartLevel(IServer server) {
        return this.getServerBundles(server).getDefaultModuleStartLevel();
    }
    
    synchronized public void saveDefaultBundleStartLevel(IServerWorkingCopy server, int startLevel) {
        this.serverBundles.get(server.getRuntime().getLocation().toFile()).setDefaultStartLevel(startLevel);
    }
    
    synchronized private void load() throws Exception {
        /* load artifacts */
        this.persistenceMgr.setSource(ARTIFACT_FILE_NAME);
        serverArtifacts = this.persistenceMgr.load(null, serverArtifacts);
        /* load bundles */
        this.persistenceMgr.setSource(BUNDLE_FILE_NAME);
        serverBundles =  this.persistenceMgr.load(null, serverBundles);
    }

    
}
