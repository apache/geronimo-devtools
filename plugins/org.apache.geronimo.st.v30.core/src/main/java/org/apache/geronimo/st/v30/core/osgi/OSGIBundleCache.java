package org.apache.geronimo.st.v30.core.osgi;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.util.SocketUtil;

@SuppressWarnings("serial")
public class OSGIBundleCache extends HashMap<File, Set<OSGIBundleCache.BundleInfo>> {
    private static OSGIBundleCache instance = new OSGIBundleCache();
    
    private OSGIBundleCache(){
        addProjectListener();
    };
    
    public synchronized static OSGIBundleCache getInstance() {
        if(instance == null) instance = new OSGIBundleCache();
        return instance;
    }
    /**
     * Add bundle info into the cache
     * @param server
     * @param module
     * @param symName
     * @param version
     * @param id
     */
    public synchronized void addBundleEntry(IServer server, IModule module, String symName, String version) {
        Set<BundleInfo> bundles = this.getServerBundles(server);
        if(module != null && module.getProject() != null) {
            BundleInfo bInfo = new BundleInfo(module.getProject().getName(), symName, version, false);
            bundles.add(bInfo);
        }
    }
    /**
     * Remove the bundle info from the cache
     * @param server
     * @param module
     */
    public synchronized void removeBundleEntry(IServer server, IModule module) {
        if(module != null && module.getProject() != null) {
            Set<BundleInfo> bundles = this.getServerBundles(server);
            BundleInfo bi = this.getBundleByArrtibutesFromSet(bundles, new String[] {"bundleProjectName"}, new Object[] {module.getProject().getName()});
            if(bi != null) bundles.remove(bi);
        }
    }
    /**
     * Get bundle info by module info
     * @param server
     * @param module
     * @return
     */
    public synchronized BundleInfo getBundleByModule(IServer server, IModule module) {
        if(module != null && module.getProject() != null) {
            Set<BundleInfo> bundles = this.getServerBundles(server);
            return this.getBundleByArrtibutesFromSet(bundles, new String[]{"bundleProjectName"}, new Object[] {module.getProject().getName()});
        }
        return null;
    }
    /**
     * Get bundle info by bundle symbolic name and version
     * @param server
     * @param symName
     * @param version
     * @return
     */
    public synchronized BundleInfo getBundleBySymblicNameAndVersion(IServer server, String symName, String version) {
        Set<BundleInfo> bundles = this.getServerBundles(server);
        return this.getBundleByArrtibutesFromSet(bundles, new String[]{"bundleSymbolicName", "bundleVersion"}, 
                new Object[] {symName, version});
    }
    /**
     * Get bundle info by bundle id
     * @param server
     * @param bundleId
     * @return
     */
    public synchronized BundleInfo getBundleById(IServer server, long bundleId) {
        Set<BundleInfo> bundles = this.getServerBundles(server);
        return this.getBundleByArrtibutesFromSet(bundles, new String[]{"bundleId"}, new Object[] {bundleId});
    }

    public synchronized Set<BundleInfo> getServerBundles(IServer server) {
        if (server == null || !SocketUtil.isLocalhost(server.getHost())) {
            return null;
        }            
        
        File runtimeLoc = server.getRuntime().getLocation().toFile();
        Set<BundleInfo> retSet = this.get(runtimeLoc);
        if(retSet == null) {
            retSet = new HashSet<BundleInfo>();
            this.put(runtimeLoc, retSet);
        }
        return retSet;
    }
    
    /**
     * 
     * @param bundles
     * @param fieldNames
     * @param values
     * @return
     */
    private BundleInfo getBundleByArrtibutesFromSet(Set<BundleInfo> bundles, String[] fieldNames, Object[] values)  {
        Iterator<BundleInfo> iter = bundles.iterator();
        try {
            while(iter.hasNext()) {
                BundleInfo b = iter.next();
                boolean fullMatched = true;
                boolean partialMatched = false;
                boolean isCompared = false;
                for(int i=0; i<fieldNames.length; ++i) {
                    isCompared = true;
                    partialMatched = false;
                    Field f = b.getClass().getDeclaredField(fieldNames[i]);
                    f.setAccessible(true);
                    if(f.get(b).equals(values[i])) {
                        partialMatched = true;
                    }
                    fullMatched &= partialMatched;
                }
                if(isCompared && fullMatched) return b;
            }
        } catch(SecurityException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logOsgi);
        } catch(NoSuchFieldException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logOsgi);
        } catch (IllegalArgumentException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logOsgi);
        } catch (IllegalAccessException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logOsgi);
        }
        return null;
    }
    
    private void addProjectListener() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
            @Override
            public void resourceChanged(IResourceChangeEvent event) {
                if(event.getType() == IResourceChangeEvent.POST_CHANGE) {// just listen to the project's change event
                    try {
                        event.getDelta().accept(new IResourceDeltaVisitor() {
                            
                            @Override
                            public boolean visit(IResourceDelta delta) throws CoreException {
                                if(delta.getKind() == IResourceDelta.REMOVED) {
                                    return processResourceRemoved(delta.getResource());
                                } 
                                else if(delta.getKind() == IResourceDelta.CHANGED) {
                                    return true;
                                }
                                return false;
                            }
                            
                            private boolean processResourceRemoved(IResource res) {
                                try {
                                    if(res instanceof IProject) {
                                        IProject p = (IProject) res;
                                        String projectName = p.getName();
                                        Iterator<Map.Entry<File, Set<BundleInfo>>> iter = OSGIBundleCache.this.entrySet().iterator();
                                        while(iter.hasNext()) {
                                            Map.Entry<File, Set<BundleInfo>> serverEntry = iter.next();
                                            BundleInfo bInfo = OSGIBundleCache.this.getBundleByArrtibutesFromSet(serverEntry.getValue(), new String[] {"bundleProjectName"}, 
                                                    new Object[] {projectName});
                                            if(bInfo != null) bInfo.setDirty(true);
                                            
                                            
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                                                        
                        });
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
                
            }
        }, IResourceChangeEvent.POST_CHANGE);
    }
    
    public static class BundleInfo {
        private String bundleProjectName;
        private String bundleSymbolicName;
        private String bundleVersion;
        private boolean isDirty;
        
        public BundleInfo(String bundleProjectName, String bundleSymbolicName, String bundleVersion,
                boolean isDirty) {
            super();
            this.bundleProjectName = bundleProjectName;
            this.bundleSymbolicName = bundleSymbolicName;
            this.bundleVersion = bundleVersion;
            this.isDirty = isDirty;
        }
        
        public String getBundleProjectName() {
            return bundleProjectName;
        }
        public void setBundleProjectName(String bundleProjectName) {
            this.bundleProjectName = bundleProjectName;
        }
        public String getBundleSymbolicName() {
            return bundleSymbolicName;
        }
        public void setBundleSymbolicName(String bundleSymbolicName) {
            this.bundleSymbolicName = bundleSymbolicName;
        }
        public String getBundleVersion() {
            return bundleVersion;
        }
        public void setBundleVersion(String bundleVersion) {
            this.bundleVersion = bundleVersion;
        }
        public boolean isDirty() {
            return isDirty;
        }
        public void setDirty(boolean isDirty) {
            this.isDirty = isDirty;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((bundleProjectName == null) ? 0 : bundleProjectName.hashCode());
            result = prime * result + ((bundleSymbolicName == null) ? 0 : bundleSymbolicName.hashCode());
            result = prime * result + ((bundleVersion == null) ? 0 : bundleVersion.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            BundleInfo other = (BundleInfo) obj;
            if (bundleProjectName == null) {
                if (other.bundleProjectName != null)
                    return false;
            } else if (!bundleProjectName.equals(other.bundleProjectName))
                return false;
            if (bundleSymbolicName == null) {
                if (other.bundleSymbolicName != null)
                    return false;
            } else if (!bundleSymbolicName.equals(other.bundleSymbolicName))
                return false;
            if (bundleVersion == null) {
                if (other.bundleVersion != null)
                    return false;
            } else if (!bundleVersion.equals(other.bundleVersion))
                return false;
            return true;
        }
        
    }
}
