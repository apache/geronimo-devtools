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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.util.SocketUtil;


/**
 * @version $Rev$ $Date$
 */
public class ModuleArtifactMapper {

    private static ModuleArtifactMapper instance = new ModuleArtifactMapper();
    
    private static final String FILE_NAME = "servermodule.info";    
    private static final String BUNDLE_FILE_NAME = "serverbundle.info";
    
    private ServerEntries serverArtifactEntries = null;    
    private ServerBundleEntries serverBundleEntries = null;
    
    private ModuleArtifactMapper() {
        if (serverArtifactEntries == null) {
            serverArtifactEntries = new ServerEntries();
        }
        if(serverBundleEntries == null) {
        	serverBundleEntries = new ServerBundleEntries();
        }
        load();
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
        Map<String, String> artifactEntries = getServerArtifactsMap(server);
        if (artifactEntries != null) {
            artifactEntries.put(getId(module), configId); 
        }       
    }
    synchronized public void removeArtifactEntry(IServer server, IModule module) {
        Map<String, String> artifactEntries = getServerArtifactsMap(server);
        if (artifactEntries != null) {
            artifactEntries.remove(getId(module)); 
        }       
    }
    
    synchronized public void addBundleEntry(IServer server, IModule module, String symName, String version, long bundleId) {
        Map<String, String[]> bundleEntries = getServerBundlesMap(server);
        if (bundleEntries != null && module != null) {
            bundleEntries.put(module.getProject().getName(), new String[] {symName, version, new Long(bundleId).toString(), "false"}); 
        } 
    }
    synchronized public String resolveBundleByModule(IServer server, IModule module) {// return the bundle symbolic name
    	if(module != null && module.getProject() != null) {
            Map<String, String[]> bundleEntries = this.serverBundleEntries.get(server.getRuntime().getLocation().toFile());
            if (bundleEntries != null) {
                String[] vs = bundleEntries.get(module.getProject().getName());
                return vs == null ? null : vs[0];
            }
    	}
    	return null;
    }
    synchronized public String resolveBundleBySymbolicName(IServer server, String symName, String version) {// return the project name
    	if(symName == null || version == null) return null;
    	Map<String, String[]> bundleEntries = this.serverBundleEntries.get(server.getRuntime().getLocation().toFile());
        if (bundleEntries != null) {
        	Iterator<Map.Entry<String, String[]>> iter = bundleEntries.entrySet().iterator();
        	while(iter.hasNext()) {
        		Map.Entry<String, String[]> entry = iter.next();
        		if(symName.equals(entry.getValue()[0]) && version.equals(entry.getValue()[1])) return entry.getKey();
        	}
        }
    	return null;
    }
    synchronized public boolean checkBundleDirty(IServer server, IModule module) {
        boolean isDirty = false;
        if(module != null && module.getProject() != null) {
            Map<String, String[]> bundleEntries = this.serverBundleEntries.get(server.getRuntime().getLocation().toFile());
            if (bundleEntries != null) {
                String[] vs = bundleEntries.get(module.getProject().getName());
                if(vs == null) isDirty = false;
                else isDirty = Boolean.parseBoolean(vs[3]);
            }
        }
        return isDirty;
    }
    @SuppressWarnings("finally")
	synchronized public String resolveBundleById(IServer server, int bundleId) {// return the project name
    	String pName = null;
    	try {
    		String idStr = new Integer(bundleId).toString();
        	Map<String, String[]> bundleEntries = this.serverBundleEntries.get(server.getRuntime().getLocation().toFile());
            if (bundleEntries != null) {
            	Iterator<Map.Entry<String, String[]>> iter = bundleEntries.entrySet().iterator();
            	while(iter.hasNext()) {
            		Map.Entry<String, String[]> entry = iter.next();
            		if(idStr.equals(entry.getValue()[2])) return entry.getKey();
            	}
            }
    	} catch(Exception e) {
    		Trace.trace(Trace.ERROR, e.getMessage(), e, Activator.logCore);
    	} finally {
    		return pName;
    	}
    }
    synchronized public void removeBundle(IServer server, IModule module) {
    	if(module != null && module.getProject() != null) {
    		Map<String, String[]> bundleEntries = this.serverBundleEntries.get(server.getRuntime().getLocation().toFile());
    		if(bundleEntries != null) {
    			bundleEntries.remove(module.getProject().getName());
    		}
    	}
    }
    
    public long getBundleId(IServer server, IModule module) {
    	int id = -1;
    	if(module != null && module.getProject() != null) {
    		String projectName = module.getProject().getName();
            Map<String, String[]> bundleEntries = this.serverBundleEntries.get(server.getRuntime().getLocation().toFile());
            if (bundleEntries != null) {
            	String[] strs = bundleEntries.get(projectName);
                if(strs != null) return new Long(strs[2]);
            }
    	}
    	return id;
    }
    
    synchronized public String resolveArtifact(IServer server, IModule module) {  
        if (module != null) {
            Map<String, String> artifactEntries = getServerArtifactsMap(server);
            if (artifactEntries != null) {
                return artifactEntries.get(getId(module));
            }
        }
        return null;
     }
          
    synchronized public Map<String, String> getServerArtifactsMap(IServer server) {
        if (!SocketUtil.isLocalhost(server.getHost())) {
            return null;
        }            
        
        File runtimeLoc = server.getRuntime().getLocation().toFile();  
        Map<String, String> artifactEntries = serverArtifactEntries.get(runtimeLoc);
        if (artifactEntries == null) {
            artifactEntries = new HashMap<String, String>();
            serverArtifactEntries.put(runtimeLoc, artifactEntries);
        }
        
        return artifactEntries;        
    }
    synchronized public Map<String, String[]> getServerBundlesMap(IServer server) {
        if (!SocketUtil.isLocalhost(server.getHost())) {
            return null;
        }            
        
        File runtimeLoc = server.getRuntime().getLocation().toFile();  
        Map<String, String[]> bundleEntries = serverBundleEntries.get(runtimeLoc);
        if (bundleEntries == null) {
            bundleEntries = new HashMap<String, String[]>();
            serverBundleEntries.put(runtimeLoc, bundleEntries);
        }
        
        return bundleEntries;        
    }
    private void save(IServerEntries entries, String fileName) {
        ObjectOutput output = null;
        try {
            IPath dest = Activator.getDefault().getStateLocation().append(fileName);
            OutputStream fos = new FileOutputStream(dest.toFile());
            OutputStream buffer = new BufferedOutputStream(fos);
            output = new ObjectOutputStream(buffer);
            String xml = entries.toXML();
            output.writeObject(xml);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    synchronized public void save() {
        save(serverArtifactEntries, FILE_NAME);
        save(serverBundleEntries, BUNDLE_FILE_NAME);
    }

    private void load(IServerEntries entries, String fileName) {
        ObjectInput input = null;
        try {
            IPath dest = Activator.getDefault().getStateLocation().append(fileName);
            if (dest.toFile().exists()) {
                InputStream file = new FileInputStream(dest.toFile());
                InputStream buffer = new BufferedInputStream(file);
                input = new ObjectInputStream(buffer);
                String xml = (String) input.readObject();
                entries.loadXML(xml);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    synchronized private void load() {
        load(serverArtifactEntries, FILE_NAME);
        load(serverBundleEntries, BUNDLE_FILE_NAME);
        addListener();
    }
    

	protected interface IServerEntries {
        public  void loadXML (String xml);
        public String toXML ();        
    }
    
    // This Inner class is the result of removing XStream.  ModuleArtifactMapper
    // was the only class using it so it seemed silly to have two extra jar
    // files (xpp3.jar and xstream.jar) just for one class.
    // this class is a HashMap
    // keys are the files
    // entries are the Maps of artifact entries
    // this is all saved/loaded to .plugins/org.apache.geronimo.st.v30.core/servermodule.info
    protected class ServerEntries extends HashMap<File, Map<String, String>> implements IServerEntries{
        static final long serialVersionUID = 0;

        public void loadXML (String xml) {
            if (xml == null || xml.length() == 0)
                return;

            String projectName, configId;
            int fileEndPos, nomapStartPos, mapStartPos, mapEndPos, stringStartPos, stringEndPos;
            int fileStartPos = xml.indexOf("<file>", 0);
            Map<String, String> artifactEntries;
            while (fileStartPos > -1) {
                fileEndPos = xml.indexOf("</file>", fileStartPos);
                File runtimeLoc = new File(xml.substring(fileStartPos + 6, fileEndPos));

                nomapStartPos = xml.indexOf("<map/>", fileEndPos);
                mapStartPos = xml.indexOf("<map>", fileEndPos);
                artifactEntries = new HashMap<String, String>();
                // have projects on the server
                if ((nomapStartPos == -1) || (nomapStartPos > mapStartPos)) {
                    mapEndPos = xml.indexOf("</map>", mapStartPos);
                    stringStartPos = xml.indexOf("<string>", mapStartPos);
                    while ((stringStartPos > -1) && (stringStartPos < mapEndPos)) {
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        projectName = xml.substring(stringStartPos + 8, stringEndPos);
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        configId = xml.substring(stringStartPos + 8, stringEndPos);
                        artifactEntries.put(projectName, configId);
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                    }
                }
                // if no projects on the server, it is ok to put an empty HashMap
                this.put (runtimeLoc, artifactEntries);

                fileStartPos = xml.indexOf("<file>", fileEndPos);
            }
        }

        public String toXML () {
            String xmlString = "";
            if (!isEmpty()) {
                xmlString = "<map>\n  <entry>\n";

                Object[] serverKeySet = keySet().toArray();
                for (int i = 0; i < serverKeySet.length; i++) {
                    xmlString += "    <file>" + serverKeySet[i] + "</file>\n";
                    Map<String, String> projectMap = (Map<String, String>)get(serverKeySet[i]);
                    if (projectMap == null || projectMap.size() == 0) {
                        xmlString += "    <map/>\n";
                    }
                    else {
                        xmlString += "    <map>\n";
                        Object[] projectKeySet = projectMap.keySet().toArray();
                        for (int j = 0; j < projectKeySet.length; j++)
                        {
                             xmlString += "      <entry>\n";
                             xmlString += "        <string>" + projectKeySet[j] + "</string>\n";
                             xmlString += "        <string>" + projectMap.get(projectKeySet[j]) + "</string>\n";
                             xmlString += "      </entry>\n";
                        }
                        xmlString += "    </map>\n";
                    }
                }
                xmlString += "  </entry>\n</map>";
            }
            return xmlString;
        }
    }
    
    protected class ServerBundleEntries extends HashMap<File, Map<String, String[]>> implements IServerEntries{
        static final long serialVersionUID = 0;

        public void loadXML (String xml) {
            if (xml == null || xml.length() == 0)
                return;

            String projectName, bundleSymName, bundleVersion, bundleId, removed;
            int fileEndPos, nomapStartPos, mapStartPos, mapEndPos, stringStartPos, stringEndPos;
            int fileStartPos = xml.indexOf("<file>", 0);
            Map<String, String[]> artifactEntries;
            while (fileStartPos > -1) {
                fileEndPos = xml.indexOf("</file>", fileStartPos);
                File runtimeLoc = new File(xml.substring(fileStartPos + 6, fileEndPos));

                nomapStartPos = xml.indexOf("<map/>", fileEndPos);
                mapStartPos = xml.indexOf("<map>", fileEndPos);
                artifactEntries = new HashMap<String, String[]>();
                // have projects on the server
                if ((nomapStartPos == -1) || (nomapStartPos > mapStartPos)) {
                    mapEndPos = xml.indexOf("</map>", mapStartPos);
                    stringStartPos = xml.indexOf("<string>", mapStartPos);
                    while ((stringStartPos > -1) && (stringStartPos < mapEndPos)) {
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        projectName = xml.substring(stringStartPos + 8, stringEndPos);// load project name
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        bundleSymName = xml.substring(stringStartPos + 8, stringEndPos);// load bundle symbolic name
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        bundleVersion = xml.substring(stringStartPos + 8, stringEndPos);// load bundle version
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        bundleId = xml.substring(stringStartPos + 8, stringEndPos);// load bundle id
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        removed = xml.substring(stringStartPos + 8, stringEndPos);// load bundle removed
                        
                        artifactEntries.put(projectName, new String []{bundleSymName, bundleVersion, bundleId, removed});
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        
                    }
                }
                // if no projects on the server, it is ok to put an empty HashMap
                this.put (runtimeLoc, artifactEntries);

                fileStartPos = xml.indexOf("<file>", fileEndPos);
            }
        }

        public String toXML () {
            String xmlString = "";
            if (!isEmpty()) {
                xmlString = "<map>\n  <entry>\n";

                Object[] serverKeySet = keySet().toArray();
                for (int i = 0; i < serverKeySet.length; i++) {
                    xmlString += "    <file>" + serverKeySet[i] + "</file>\n";
                    Map<String, String[]> projectMap = (Map<String, String[]>)get(serverKeySet[i]);
                    if (projectMap == null || projectMap.size() == 0) {
                        xmlString += "    <map/>\n";
                    }
                    else {
                        xmlString += "    <map>\n";
                        Object[] projectKeySet = projectMap.keySet().toArray();
                        for (int j = 0; j < projectKeySet.length; j++)
                        {
                             xmlString += "      <entry>\n";
                             xmlString += "        <string>" + (String)projectKeySet[j] + "</string>\n";
                             xmlString += "        <string>" + (String)projectMap.get(projectKeySet[j])[0] + "</string>\n";
                             xmlString += "        <string>" + (String)projectMap.get(projectKeySet[j])[1] + "</string>\n";
                             xmlString += "        <string>" + (String)projectMap.get(projectKeySet[j])[2] + "</string>\n";
                             xmlString += "        <string>" + (String)projectMap.get(projectKeySet[j])[3] + "</string>\n";
                             xmlString += "      </entry>\n";
                        }
                        xmlString += "    </map>\n";
                    }
                }
                xmlString += "  </entry>\n</map>";
            }
            return xmlString;
        }
    }
    // private methods
    private void addListener() {
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
                                } else if(delta.getKind() == IResourceDelta.CHANGED) {
                                    return processResourceChanged(delta.getResource());
                                }
                                return false;
                            }
                            
                            private boolean processResourceRemoved(IResource res) {
                                try {
                                    if(res instanceof IProject) {
                                        IProject p = (IProject) res;
                                        String projectName = p.getName();
                                        
                                        Iterator<Map.Entry<File, Map<String, String[]>>> iter = serverBundleEntries.entrySet().iterator();
                                        while(iter.hasNext()) {
                                            Map.Entry<File, Map<String, String[]>> serverEntry = iter.next();
                                            Map<String, String[]> bundleMap = serverEntry.getValue();
                                            String[] value = bundleMap.get(projectName);
                                            if(value != null) {
                                                value[3] = "true";// set removed flag to true
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                            
                            private boolean processResourceChanged(IResource res) {
                                try {
                                    if(res.getFullPath().toString().endsWith(".MF")) {// the project's menifest file is changed
                                      BufferedReader br = new BufferedReader(new FileReader(res.getLocation().toFile()));
                                      String projectName = res.getFullPath().toString().split("/")[1];
                                      String fc = br.readLine();
                                      String symbolicName = null, version = null;
                                      boolean bss = false, bvs = false;
                                      while(fc != null && (! bss || ! bvs)) {
                                          if(fc.startsWith("Bundle-SymbolicName")) {
                                              int index = fc.indexOf(":");
                                              symbolicName = fc.substring(index+1).trim();bss = true;
                                          } else if (fc.startsWith("Bundle-Version")) {
                                              int index = fc.indexOf(":");
                                              version = fc.substring(index+1).trim();bvs = true;
                                          }
                                          fc = br.readLine();
                                      }
                                      Iterator<Map.Entry<File, Map<String, String[]>>> iter = serverBundleEntries.entrySet().iterator();
                                      while(iter.hasNext()) {
                                          Map.Entry<File, Map<String, String[]>> serverEntry = iter.next();
                                          Map<String, String[]> bundleMap = serverEntry.getValue();
                                          
                                          String[] value = bundleMap.get(projectName);
                                          if(value != null) {
                                              if(value[0].equals(symbolicName) && value[1].equals(version)) break;
                                              value[0] = symbolicName; value[1] = version;
                                          } 
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
    

        
}
