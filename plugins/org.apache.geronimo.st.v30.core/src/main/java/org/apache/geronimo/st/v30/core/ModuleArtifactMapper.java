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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.geronimo.deployment.plugin.jmx.ExtendedDeploymentManager;
import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.v30.core.commands.DeploymentCommandFactory;
import org.apache.geronimo.st.v30.core.osgi.AriesHelper;
import org.eclipse.core.resources.IProject;
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
    private static final String FILE_NAME2 = "serverbundle.info";

    ServerEntries serverArtifactEntries = null;    
    ServerEntries2 serverBundleEntries = null;

    private ModuleArtifactMapper() {
        if (serverArtifactEntries == null)
            serverArtifactEntries = new ServerEntries();
        if (serverBundleEntries == null) 
            serverBundleEntries = new ServerEntries2();
        load();
    }

    public static ModuleArtifactMapper getInstance() {
        return instance;
    }

    synchronized public void addArtifactEntry(IServer server, IModule module, String configId) {

        if (!SocketUtil.isLocalhost(server.getHost()))
            return;

        File runtimeLoc = server.getRuntime().getLocation().toFile();
        Map artifactEntries = (Map) serverArtifactEntries.get(runtimeLoc);
        if (artifactEntries == null) {
            artifactEntries = new HashMap();
            serverArtifactEntries.put(runtimeLoc, artifactEntries);
        }

        artifactEntries.put(module.getProject().getName(), configId);       
    }
    
    synchronized public void addBundleEntry(IServer server, IModule eba) {
        
        if (!GeronimoUtils.isEBAModule(eba))
            return;        
        if (!SocketUtil.isLocalhost(server.getHost()))
            return;

        File runtimeLoc = server.getRuntime().getLocation().toFile();
        
        Map bundleEntries = (Map) serverBundleEntries.get(runtimeLoc);
        if (bundleEntries == null) {
            bundleEntries = new HashMap();
            serverBundleEntries.put(runtimeLoc, bundleEntries);
        }
        
        // first clean up the previous bundle entries for this eba if such entries exist
        removeBundleEntry(bundleEntries, eba);
        
        try {
            String configId = this.resolveArtifact(server, eba);
            IModule[] bundleModules = AriesHelper.getChildModules(eba);            
            ExtendedDeploymentManager dm = (ExtendedDeploymentManager) DeploymentCommandFactory.getDeploymentManager(server);
            AbstractName abstractName = dm.getApplicationGBeanName(Artifact.create(configId));
            long[] bundleIds = dm.getEBAContentBundleIds(abstractName);

            for (long bundleId : bundleIds) {
                String symbolicName = dm.getEBAContentBundleSymbolicName(abstractName, bundleId);
                if (symbolicName != null) {
                    for (IModule bundleModule : bundleModules) {
                        if (symbolicName.equals(AriesHelper.getSymbolicName(bundleModule))) {
                            EBABundle ebaBundle = new EBABundle(eba, bundleModule);
                            bundleEntries.put(ebaBundle, Long.toString(bundleId));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }     
        
    }
      
    synchronized public void removeBundleEntry(IServer server, IModule eba) {

        if (!GeronimoUtils.isEBAModule(eba))
            return;
        if (!SocketUtil.isLocalhost(server.getHost()))
            return;

        File runtimeLoc = server.getRuntime().getLocation().toFile();
        Map bundleEntries = (Map) serverBundleEntries.get(runtimeLoc);    
        removeBundleEntry(bundleEntries, eba);
    }
    
    private void removeBundleEntry(Map map, IModule eba) {
        
        if (map == null)
            return;
        
        Iterator<EBABundle> iterator = map.keySet().iterator();
        ArrayList<EBABundle> removes = new ArrayList<EBABundle>();
        for ( ; iterator.hasNext();) {
            EBABundle ebaBundle = iterator.next();
            if (ebaBundle.ebaName != null && ebaBundle.ebaName.equals(eba.getProject().getName())) {
                removes.add(ebaBundle);
            }
        }            
        if (removes.size()!=0) {
            for (EBABundle remove : removes) {
                map.remove(remove);
            }
        }
    }
    
    synchronized public void removeArtifactBundleEntry(IServer server, IModule module) {

        if (!SocketUtil.isLocalhost(server.getHost()))
            return;

        File runtimeLoc = server.getRuntime().getLocation().toFile();
        Map artifactEntries = (Map) serverArtifactEntries.get(runtimeLoc);
        if (artifactEntries != null) {
            artifactEntries.remove(module.getProject().getName());
        }
        
        if (GeronimoUtils.isEBAModule(module)) {
            removeBundleEntry(server, module);
        }
    }

    synchronized public String resolveArtifact(IServer server, IModule module) {        
        if (module != null ) {
            return resolveArtifact(server, module.getProject());
        }
        return null;
    }
    
    synchronized public String resolveArtifact(IServer server, IProject project) {
        Map artifactEntries = (Map) serverArtifactEntries.get(server.getRuntime().getLocation().toFile());
        if (artifactEntries != null && project != null) {
            return (String) artifactEntries.get(project.getName());
        }
        return null;
    }
    
    synchronized public String resolveBundle(IServer server, IModule eba, IModule bundle) {
        Map bundleEntries = (Map) serverBundleEntries.get(server.getRuntime().getLocation().toFile());
        if (bundleEntries != null && eba != null && bundle!=null) {
            return (String) bundleEntries.get(new EBABundle(eba, bundle));            
        }
        return null;
    }
    
    synchronized public HashMap getServerArtifactsMap(IServer server) {
        if (!SocketUtil.isLocalhost(server.getHost())) {
            return null;
        }            
        
        File runtimeLoc = server.getRuntime().getLocation().toFile();  
        HashMap artifactEntries = (HashMap) serverArtifactEntries.get(runtimeLoc);
        if (artifactEntries == null) {
            artifactEntries = new HashMap();
            serverArtifactEntries.put(runtimeLoc, artifactEntries);
        }
        
        return artifactEntries;        
    }

    private void save(IServerEntries entries, String fileName) {
        ObjectOutput output = null;
        try {
            IPath dest = Activator.getDefault().getStateLocation().append(fileName);
            OutputStream file = new FileOutputStream(dest.toFile());
            OutputStream buffer = new BufferedOutputStream(file);
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
        save(serverBundleEntries, FILE_NAME2);
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
        load(serverBundleEntries, FILE_NAME2);
    }
    
    protected class EBABundle {
        String ebaName;
        String bundleName;

        public EBABundle(IModule eba, IModule bundle) {
                this.ebaName = eba.getProject().getName();
                this.bundleName = bundle.getProject().getName();
        }
        
        public EBABundle(String ebaName, String bundleName) {
            this.ebaName = ebaName;
            this.bundleName = bundleName;
        }

        public boolean equals(Object bundle) {
            if (bundle instanceof EBABundle) {
                return bundleName.equals(((EBABundle) bundle).bundleName)
                        && ebaName.equals(((EBABundle) bundle).ebaName);
            }
            return false;
        }
        
        public int hashCode() {
            return ebaName.hashCode() + bundleName.hashCode();
        }
    }

    protected interface IServerEntries{
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
    protected class ServerEntries extends HashMap implements IServerEntries{
        static final long serialVersionUID = 0;

        public void loadXML (String xml) {
            if (xml == null || xml.length() == 0)
                return;

            String projectName, configId;
            int fileEndPos, nomapStartPos, mapStartPos, mapEndPos, stringStartPos, stringEndPos;
            int fileStartPos = xml.indexOf("<file>", 0);
            Map artifactEntries;
            while (fileStartPos > -1) {
                fileEndPos = xml.indexOf("</file>", fileStartPos);
                File runtimeLoc = new File(xml.substring(fileStartPos + 6, fileEndPos));

                nomapStartPos = xml.indexOf("<map/>", fileEndPos);
                mapStartPos = xml.indexOf("<map>", fileEndPos);
                artifactEntries = new HashMap();
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
                    Map projectMap = (Map)get(serverKeySet[i]);
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
                             xmlString += "        <string>" + (String)projectMap.get(projectKeySet[j]) + "</string>\n";
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
    
    // This Inner class is the result of removing XStream.  ModuleArtifactMapper
    // was the only class using it so it seemed silly to have two extra jar
    // files (xpp3.jar and xstream.jar) just for one class.
    // this class is a HashMap
    // keys are the files
    // entries are the Maps of bundle entries
    // this is all saved/loaded to .plugins/org.apache.geronimo.st.v30.core/serverbundle.info
    protected class ServerEntries2 extends HashMap implements IServerEntries{
        static final long serialVersionUID = 0;

        public void loadXML (String xml) {
            if (xml == null || xml.length() == 0)
                return;

            String ebaProjectName, bundleProjectName, bundleId;
            int fileEndPos, nomapStartPos, mapStartPos, mapEndPos, stringStartPos, stringEndPos;
            int fileStartPos = xml.indexOf("<file>", 0);
            Map bundleEntries;
            while (fileStartPos > -1) {
                fileEndPos = xml.indexOf("</file>", fileStartPos);
                File runtimeLoc = new File(xml.substring(fileStartPos + 6, fileEndPos));

                nomapStartPos = xml.indexOf("<map/>", fileEndPos);
                mapStartPos = xml.indexOf("<map>", fileEndPos);
                bundleEntries = new HashMap();
                // have EBAs on the server
                if ((nomapStartPos == -1) || (nomapStartPos > mapStartPos)) {
                    mapEndPos = xml.indexOf("</map>", mapStartPos);
                    stringStartPos = xml.indexOf("<string>", mapStartPos);
                    while ((stringStartPos > -1) && (stringStartPos < mapEndPos)) {
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        ebaProjectName = xml.substring(stringStartPos + 8, stringEndPos);
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        bundleProjectName = xml.substring(stringStartPos + 8, stringEndPos);
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                        stringEndPos = xml.indexOf("</string>", stringStartPos);
                        bundleId = xml.substring(stringStartPos + 8, stringEndPos);
                        bundleEntries.put(new EBABundle(ebaProjectName, bundleProjectName), bundleId);
                        stringStartPos = xml.indexOf("<string>", stringEndPos);
                    }
                }
                // if no projects on the server, it is ok to put an empty HashMap
                this.put (runtimeLoc, bundleEntries);

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
                    Map bundleMap = (Map)get(serverKeySet[i]);
                    if (bundleMap == null || bundleMap.size() == 0) {
                        xmlString += "    <map/>\n";
                    }
                    else {
                        xmlString += "    <map>\n";
                        Object[] bundleKeySet = bundleMap.keySet().toArray();
                        for (int j = 0; j < bundleKeySet.length; j++)
                        {
                             xmlString += "      <entry>\n";
                             xmlString += "        <string>" + ((EBABundle)bundleKeySet[j]).ebaName + "</string>\n";
                             xmlString += "        <string>" + ((EBABundle)bundleKeySet[j]).bundleName + "</string>\n";
                             xmlString += "        <string>" + (String)bundleMap.get(bundleKeySet[j]) + "</string>\n";
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
