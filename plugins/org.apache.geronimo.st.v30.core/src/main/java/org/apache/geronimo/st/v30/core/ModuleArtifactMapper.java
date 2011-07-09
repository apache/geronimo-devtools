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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
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
    
    private ServerEntries serverArtifactEntries = null;    
    
    private ModuleArtifactMapper() {
        if (serverArtifactEntries == null) {
            serverArtifactEntries = new ServerEntries();
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
    
}
