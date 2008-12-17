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
package org.apache.geronimo.st.v21.core.operations;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.List;
import java.util.zip.ZipEntry;

import javax.management.MBeanServerConnection;
import javax.xml.bind.JAXBElement;

import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.deployment.plugin.jmx.RemoteDeploymentManager;
import org.apache.geronimo.kernel.Kernel;
import org.apache.geronimo.kernel.config.ConfigurationData;
import org.apache.geronimo.kernel.config.ConfigurationInfo;
import org.apache.geronimo.kernel.config.ConfigurationManager;
import org.apache.geronimo.kernel.config.ConfigurationUtil;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.kernel.repository.Dependency;
import org.apache.geronimo.kernel.repository.ImportType;
import org.apache.geronimo.kernel.repository.Version;
import org.apache.geronimo.st.core.CommonMessages;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.st.core.GeronimoRuntimeDelegate;
import org.apache.geronimo.st.core.GeronimoServerBehaviourDelegate;
import org.apache.geronimo.st.core.jaxb.JAXBUtils;
import org.apache.geronimo.st.v21.core.internal.Trace;
import org.apache.geronimo.system.jmx.KernelDelegate;
import org.apache.geronimo.system.plugin.SourceRepository;
import org.apache.geronimo.system.plugin.SourceRepositoryFactory;
import org.apache.geronimo.system.plugin.model.ArtifactType;
import org.apache.geronimo.system.plugin.model.DependencyType;
import org.apache.geronimo.system.plugin.model.ObjectFactory;
import org.apache.geronimo.system.plugin.model.PluginArtifactType;
import org.apache.geronimo.system.plugin.model.PluginListType;
import org.apache.geronimo.system.plugin.model.PluginType;
import org.apache.geronimo.system.plugin.model.PrerequisiteType;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoServerPluginManager {

    private IServer server;
    private RemoteDeploymentManager remoteDM;
    private PluginListType data;
    private List<String> pluginList;
    private Kernel kernel;

    public GeronimoServerPluginManager () {
        kernel = null;
        Trace.tracePoint("Constructor", "GeronimoServerPluginManager");
    }

    public boolean serverChanged (Object aServer, String serverPrefix) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.serverChanged", aServer, serverPrefix);
        server = (IServer)aServer;

        boolean enabled = server != null &&
                server.getServerType().getId().startsWith(serverPrefix) &&
                server.getServerState() == IServer.STATE_STARTED;

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.serverChanged", enabled);
        return enabled;
    }

    // mimics org.apache.geronimo.console.car.AssemblyListHandler.renderView
    public List<String> getPluginList () {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.getPluginList");

        String name;
        boolean added;
        try {
            GeronimoConnectionFactory gcFactory = GeronimoConnectionFactory.getInstance();
            remoteDM = (RemoteDeploymentManager)gcFactory.getDeploymentManager(server);
            data = remoteDM.createPluginListForRepositories(null);
            List<PluginType> aList = data.getPlugin();
            pluginList = new ArrayList<String>(aList.size());
            for (int i = 0; i < aList.size(); i++) {
                name = aList.get(i).getName();
                added = false;
                for (int j = 0; j < pluginList.size() && added == false; j++) {
                    if (name.compareTo(pluginList.get(j)) < 0) {
                        pluginList.add(j, name);
                        added = true;
                    }
                }
                if (added == false) {
                    pluginList.add(name);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.getPluginList", pluginList);
        return pluginList;
    }

    // mimics org.apache.geronimo.console.car.AssemblyViewHandler.actionAfterView
    public void assembleServer (String group, String artifact, String version,
                        String format, String relativeServerPath, int[] selected) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.assembleServer",
                group, artifact, version, format);

        PluginListType selectedPlugins = new PluginListType();
        String name;
        boolean found;

        for (int i = 0; i < selected.length; i++) {
            name = pluginList.get(selected[i]);
            found = false;
            for (int j = 0 ; j < data.getPlugin().size() && found == false; j++) {
                if (name.equals(data.getPlugin().get(j).getName())) {
                    selectedPlugins.getPlugin().add(data.getPlugin().get(j));
                    found = true;
                }
            }
        }

        try {
            remoteDM.installPluginList("repository", relativeServerPath, selectedPlugins);
            remoteDM.archive(relativeServerPath, "var/temp", new Artifact(group, artifact, (String)version, format));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Trace.tracePoint("Exit", "GeronimoServerPluginManager.assembleServer");
    }

    // mimics org.apache.geronimo.console.util.KernelManagementHelper.getConfigurations()
    public List<String> getConfigurationList () {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.getConfigurationList");

        ConfigurationManager mgr = getConfigurationManager();
        List<AbstractName> stores = mgr.listStores();
        List<String> results = new ArrayList<String>();
        for (AbstractName storeName : stores) {
            try {
                List<ConfigurationInfo> infos = mgr.listConfigurations(storeName);
                for (ConfigurationInfo info : infos) {
                    results.add(info.getConfigID().toString());
                }
            } catch (Exception e) {
                throw new RuntimeException(CommonMessages.badConfigId, e);
            }
        }
        Collections.sort(results);

        Trace.tracePoint("Entry", "GeronimoServerPluginManager.getConfigurationList", results);
        return results;
    }

    public PluginType getPluginMetadata (String configId) {
        Artifact artifact = Artifact.create(configId);
        File dir = new File (addFilename(getArtifactLocation(artifact), artifact));
        PluginType metadata = extractPluginMetadata (dir);
        if (metadata == null) {
            metadata = createDefaultMetadata (artifact);
        }
        return metadata;
    }

    // mimics org.apache.geronimo.system.plugin.PluginInstallerGBean.updatePluginMetadata
    public void savePluginXML (String configId, PluginType metadata) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.savePluginXML", configId, metadata);

        Artifact artifact = Artifact.create(configId);
        File dir = new File (getArtifactLocation(artifact));

        if (!dir.isDirectory()) { // must be a packed (JAR-formatted) plugin
            try {
                File temp = new File(dir.getParentFile(), dir.getName() + ".temp");
                JarFile input = new JarFile(dir);
                Manifest manifest = input.getManifest();
                JarOutputStream out = manifest == null ? new JarOutputStream(
                        new BufferedOutputStream(new FileOutputStream(temp)))
                        : new JarOutputStream(new BufferedOutputStream(new FileOutputStream(temp)), manifest);
                Enumeration en = input.entries();
                byte[] buf = new byte[4096];
                int count;
                while (en.hasMoreElements()) {
                    JarEntry entry = (JarEntry) en.nextElement();
                    if (entry.getName().equals("META-INF/geronimo-plugin.xml")) {
                        entry = new JarEntry(entry.getName());
                        out.putNextEntry(entry);
                        writePluginMetadata(metadata, out);
                    } else if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                        // do nothing, already passed in a manifest
                    } else {
                        out.putNextEntry(entry);
                        InputStream in = input.getInputStream(entry);
                        while ((count = in.read(buf)) > -1) {
                            out.write(buf, 0, count);
                        }
                        in.close();
                        out.closeEntry();
                    }
                }
                out.flush();
                out.close();
                input.close();
                if (!dir.delete()) {
                    String message = CommonMessages.bind(CommonMessages.errorDeletePlugin, dir.getAbsolutePath());
                    Trace.tracePoint("Throw", "GeronimoServerPluginManager.savePluginXML", message);
                    throw new Exception(message);
                }
                if (!temp.renameTo(dir)) {
                    String message = CommonMessages.bind(CommonMessages.errorMovePlugin, temp.getAbsolutePath(), dir.getAbsolutePath());
                    Trace.tracePoint("Throw", "GeronimoServerPluginManager.savePluginXML", message);
                    throw new Exception(message);
                }
            } catch (Exception e) {
                throw new RuntimeException(CommonMessages.errorUpdateMetadata, e);
            }
        } else {
            File meta = new File(addFilename(dir.getAbsolutePath(), artifact), "META-INF");
            if (!meta.isDirectory() || !meta.canRead()) {
                String message = CommonMessages.bind(CommonMessages.badPlugin, artifact);
                Trace.tracePoint("Throw", "GeronimoServerPluginManager.savePluginXML", message);
                throw new IllegalArgumentException(message);
            }
            File xml = new File(meta, "geronimo-plugin.xml");
            FileOutputStream fos = null;
            try {
                if (!xml.isFile()) {
                    if (!xml.createNewFile()) {
                        String message = CommonMessages.bind(CommonMessages.errorCreateMetadata, artifact);
                        Trace.tracePoint("Throw", "GeronimoServerPluginManager.savePluginXML", message);
                        throw new RuntimeException(message);
                    }
                }
                fos = new FileOutputStream(xml);
                writePluginMetadata(metadata, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                }
            }
        }
        Trace.tracePoint("Exit", "GeronimoServerPluginManager.savePluginXML");
    }

    // mimics org.apache.geronimo.system.configuration.RepositoryConfigurationStore.exportConfiguration
    public void exportCAR (String localRepoDir, String configId) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.exportCAR", localRepoDir, configId);

        Artifact artifact = Artifact.create(configId);
        String filename = createDirectoryStructure (localRepoDir, artifact);
        File outputDir = new File (filename);

        File serverArtifact = new File(getArtifactLocation (artifact));
        writeToDirectory(serverArtifact, outputDir);

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.exportCAR");
    }

    private void writeToDirectory(File inputDir, File outputDir) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.writeToZip", inputDir);

        outputDir.mkdirs();
        File[] all = inputDir.listFiles();
        for (File file : all) {
            if (file.isDirectory()) {
                String oDir = outputDir.getAbsolutePath() + File.separator + file.getName();
                File temp = new File (oDir);
                writeToDirectory(file, temp);
            } else {
                File entry = new File(outputDir + File.separator + file.getName());
                FileOutputStream out = new FileOutputStream (entry);
                FileInputStream in = new FileInputStream(file);
                byte[] buf = new byte[10240];
                int count;
                try {
                    while ((count = in.read(buf, 0, buf.length)) > -1) {
                        out.write(buf, 0, count);
                    }
                } finally {
                    in.close();
                    out.flush();
                    out.close();
                }
            }
        }
        Trace.tracePoint("Exit", "GeronimoServerPluginManager.writeToZip");
    }

    public void updatePluginList (String localRepoDir, PluginType metadata) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.updatePluginList", localRepoDir, metadata);

        PluginListType pluginList = readPluginList(localRepoDir);
        File listFile = new File (localRepoDir, "geronimo-plugins.xml");

        // if this plugin exists, remove it from the list
        PluginType plugin;
        for (int i = 0; i < pluginList.getPlugin().size(); i++) {
            plugin = pluginList.getPlugin().get(i);
            if (metadata.getName().equals(plugin.getName()) &&
                metadata.getCategory().equals(plugin.getCategory())) {
                pluginList.getPlugin().remove(i);
                break;
            }
        }

        // add the current plugin to the list
        pluginList.getPlugin().add(metadata);

        // write the file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(listFile);
            writePluginList(pluginList, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        Trace.tracePoint("Exit", "GeronimoServerPluginManager.updatePluginList");
    }

    public PluginListType readPluginList (String localRepoDir) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.readPluginList", localRepoDir);

        PluginListType pluginList = null;
        File listFile = new File (localRepoDir, "geronimo-plugins.xml");
        if (listFile.exists() && listFile.exists()) {
            InputStream in = null;
            try {
                in = new FileInputStream(listFile);
                pluginList = loadPluginList(in);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {}
                }
            }
        }
        if (pluginList == null) {
            ObjectFactory factory = new ObjectFactory();
            pluginList = factory.createPluginListType();
        }

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.readPluginList", pluginList);
        return pluginList;
    }

    // mimics org.apache.geronimo.system.plugin.GeronimoSourceRepository.extractPluginMetadata
    private PluginType extractPluginMetadata (File dir) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.extractPluginMetadata", dir);

        try {
            if (dir.isDirectory()) {
                File meta = new File(dir, "META-INF");
                if (!meta.isDirectory() || !meta.canRead()) {
                    return null;
                }
                File xml = new File(meta, "geronimo-plugin.xml");
                if (!xml.isFile() || !xml.canRead() || xml.length() == 0) {
                    return null;
                }
                InputStream in = new FileInputStream(xml);
                try {
                    return loadPluginMetadata(in);
                } finally {
                    in.close();
                }
            } else {
                if (!dir.isFile() || !dir.canRead()) {
                    throw new IllegalStateException(CommonMessages.bind(CommonMessages.errorReadConfig, dir.getAbsolutePath()));
                }
                JarFile jar = new JarFile(dir);
                try {
                    ZipEntry entry = jar.getEntry("META-INF/geronimo-plugin.xml");
                    if (entry == null) {
                        return null;
                    }
                    InputStream in = jar.getInputStream(entry);
                    try {
                        return loadPluginMetadata(in);
                    } finally {
                        in.close();
                    }
                } finally {
                    jar.close();
                }
            }
        } catch (Exception e) {
            //ignore
        }
        Trace.tracePoint("Exit", "GeronimoServerPluginManager.extractPluginMetadata", dir);
        return null;
    }

    // mimics org.apache.geronimo.system.plugin.PluginInstallerGBean.createDefaultMetadata
    private PluginType createDefaultMetadata (Artifact moduleId) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.createDefaultMetadata", moduleId);

        ConfigurationData data = null;
        try {
            data = this.loadConfiguration(moduleId);
        }
        catch (Exception e) {
        }

        PluginType metadata = new PluginType();
        PluginArtifactType instance = new PluginArtifactType();
        metadata.getPluginArtifact().add(instance);
        metadata.setName(toArtifactType(moduleId).getArtifactId());
        instance.setModuleId(toArtifactType(moduleId));
        metadata.setCategory("Unknown");
        GeronimoRuntimeDelegate rd = (GeronimoRuntimeDelegate)server.getRuntime().getAdapter(GeronimoRuntimeDelegate.class);
        if (rd == null)
            rd = (GeronimoRuntimeDelegate) server.getRuntime().loadAdapter(GeronimoRuntimeDelegate.class, new NullProgressMonitor());
        if (rd != null) {
            instance.getGeronimoVersion().add(rd.detectVersion());
        }
        instance.getObsoletes().add(toArtifactType(new Artifact(moduleId.getGroupId(),
                moduleId.getArtifactId(),
                (Version) null,
                moduleId.getType())));
        List<DependencyType> deps = instance.getDependency();
        addGeronimoDependencies(data, deps, true);

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.createDefaultMetadata", metadata);
        return metadata;
    }

    // mimics org.apache.geronimo.system.configuration.RepositoryConfigurationStore.loadConfiguration
    private ConfigurationData loadConfiguration (Artifact artifact) throws Exception {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.loadConfiguration", artifact);

        String temp = getArtifactLocation(artifact);
        File location = new File (this.addFilename(temp, artifact));

        ConfigurationData configurationData;
        try {
            if (location.isDirectory()) {
                File serFile = new File(location, "META-INF");
                serFile = new File(serFile, "config.ser");

                if (!serFile.exists()) {
                    throw new Exception(CommonMessages.bind(CommonMessages.errorNoSerFile, serFile));
                } else if (!serFile.canRead()) {
                    throw new Exception(CommonMessages.bind(CommonMessages.errorReadSerFile, serFile));
                }

                //ConfigurationStoreUtil.verifyChecksum(serFile);

                InputStream in = new FileInputStream(serFile);
                try {
                    configurationData = ConfigurationUtil.readConfigurationData(in);
                } finally {
                    in.close();
                }
            } else {
                JarFile jarFile = new JarFile(location);
                InputStream in = null;
                try {
                    ZipEntry entry = jarFile.getEntry("META-INF/config.ser");
                    in = jarFile.getInputStream(entry);
                    configurationData = ConfigurationUtil.readConfigurationData(in);
                } finally {
                    in.close();
                    jarFile.close();
                }
            }
        } catch (ClassNotFoundException e) {
            String message = CommonMessages.bind(CommonMessages.errorLoadClass, artifact);
            Trace.tracePoint("Throw", "GeronimoServerPluginManager.loadConfiguration", message);
            throw new Exception(message, e);
        }

        configurationData.setConfigurationDir(location);
//        if (kernel != null) {
//            configurationData.setNaming(kernel.getNaming());
//        }


        Trace.tracePoint("Exit", "GeronimoServerPluginManager.loadConfiguration", configurationData);
        return configurationData;
    }

    public PluginListType loadPluginList (InputStream in) {
        try {
            JAXBElement pluginListElement = JAXBUtils.unmarshalPlugin(in);
            return (PluginListType)pluginListElement.getValue();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writePluginList (PluginListType pluginList, OutputStream out) {
        try {
            ObjectFactory jeeFactory = new ObjectFactory();
            JAXBElement element = jeeFactory.createGeronimoPluginList(pluginList);
            JAXBUtils.marshalPlugin(element, out);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    //mimic org.apache.geronimo.system.plugin.PluginXmlUtil.loadPluginMetadata(in);
    private PluginType loadPluginMetadata (InputStream in) {
        try {
            JAXBElement pluginElement = JAXBUtils.unmarshalPlugin(in);
            return (PluginType)pluginElement.getValue();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writePluginMetadata (PluginType metadata, OutputStream out) {
        try {
            ObjectFactory jeeFactory = new ObjectFactory();
            JAXBElement element = jeeFactory.createGeronimoPlugin(metadata);
            JAXBUtils.marshalPlugin(element, out);
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private String getArtifactLocation (Artifact artifact) {
        String ch = File.separator;
        String temp = server.getRuntime().getLocation().toOSString() + ch + "repository" + ch;
        String group = artifact.getGroupId();
		int pos = group.indexOf(".");
		while (pos > -1) {
		    group = group.substring(0, pos) + ch + group.substring(pos + 1);
		    pos = group.indexOf(".");
        }
        temp += group + ch + artifact.getArtifactId() + ch + artifact.getVersion() + ch;
        return temp;
    }

    private String addFilename (String path, Artifact artifact) {
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path += "/";
        }
        return path + artifact.getArtifactId() + "-" + artifact.getVersion() + "." + artifact.getType();
    }

    private String createDirectoryStructure (String rootPath, Artifact artifact) {
        String fileName = rootPath;
        if (!fileName.endsWith("/") && !fileName.endsWith("\\")) {
            fileName += "/";
        }

        String group = artifact.getGroupId();
        int pos = group.indexOf(".");
        while (pos > -1) {
            group = group.substring(0, pos) + File.separator + group.substring(pos + 1);
            pos = group.indexOf(".");
        }
        fileName += group + "/" + artifact.getArtifactId() + "/" + artifact.getVersion() + "/";
        File temp = new File (fileName);
        if (!temp.exists()) {
            temp.mkdirs();
        }

        return fileName;
    }

    private ConfigurationManager getConfigurationManager () {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.getConfigurationManager");

        if (kernel == null) {
            try {
                GeronimoServerBehaviourDelegate delegate = (GeronimoServerBehaviourDelegate) server
                    .getAdapter(GeronimoServerBehaviourDelegate.class);
                if (delegate != null) {
                    MBeanServerConnection connection = delegate
                        .getServerConnection();
                    if (connection != null) {
                        kernel = new KernelDelegate(connection);
                    }
                }
            } catch (Exception e) {
                Trace.trace(Trace.WARNING, "Kernel connection failed. "
                    + e.getMessage());
            }
        }
        if (kernel != null) {
            Trace.tracePoint("Exit", "GeronimoServerPluginManager.getConfigurationManager");
            return ConfigurationUtil.getEditableConfigurationManager(kernel);
        }

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.getConfigurationManager");
        return null;
    }

    public ArtifactType toArtifactType(String configId) {
        return toArtifactType (Artifact.create(configId));
    }

    public ArtifactType toArtifactType(Artifact id) {
        ArtifactType artifact = new ArtifactType();
        artifact.setGroupId(id.getGroupId());
        artifact.setArtifactId(id.getArtifactId());
        artifact.setVersion(id.getVersion() == null ? null : id.getVersion().toString());
        artifact.setType(id.getType());
        return artifact;
    }

    public Artifact toArtifact(ArtifactType id) {
        return new Artifact (id.getGroupId(), id.getArtifactId(), id.getVersion(), id.getType());
    }

    public void addGeronimoDependencies(ConfigurationData data, List<DependencyType> deps, boolean includeVersion) {
        processDependencyList(data.getEnvironment().getDependencies(), deps, includeVersion);
        Map<String, ConfigurationData> children = data.getChildConfigurations();
        for (ConfigurationData child : children.values()) {
            processDependencyList(child.getEnvironment().getDependencies(), deps, includeVersion);
        }
    }

    private void processDependencyList(List<Dependency> real, List<DependencyType> deps, boolean includeVersion) {
        for (Dependency dep : real) {
            DependencyType dependency = toDependencyType(dep, includeVersion);
            if (!deps.contains(dependency)) {
                deps.add(dependency);
            }
        }
    }

    public DependencyType toDependencyType(String configId) {
        return toDependencyType(new Dependency(Artifact.create(configId), ImportType.ALL), true);
    }

    public DependencyType toDependencyType(Dependency dep, boolean includeVersion) {
        Artifact id = dep.getArtifact();
        DependencyType dependency = new DependencyType();
        dependency.setGroupId(id.getGroupId());
        dependency.setArtifactId(id.getArtifactId());
        if (includeVersion) {
            dependency.setVersion(id.getVersion() == null ? null : id.getVersion().toString());
        }
        dependency.setType(id.getType());
        return dependency;
    }

    // mimics org.apache.geronimo.system.plugin.PluginInstallerGbean.validatePlugin
    public boolean validatePlugin (PluginType plugin) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.validatePlugin", plugin);

        boolean valid = true;
        String serverVersion = null;
        GeronimoRuntimeDelegate rd = (GeronimoRuntimeDelegate)server.getRuntime().getAdapter(GeronimoRuntimeDelegate.class);
        if (rd == null)
            rd = (GeronimoRuntimeDelegate) server.getRuntime().loadAdapter(GeronimoRuntimeDelegate.class, new NullProgressMonitor());
        if (rd != null) {
            serverVersion = rd.detectVersion();
        }

        // check the Geronimo and JVM versions for validity
        PluginArtifactType metadata = plugin.getPluginArtifact().get(0);
        if (metadata.getGeronimoVersion().size() > 0) {
            valid = false;
            for (String gerVersion : metadata.getGeronimoVersion()) {
                valid = gerVersion.equals(serverVersion);
                if (valid) {
                    break;
                }
            }
        }
        if (valid == false) {
            Trace.tracePoint("Exit", "GeronimoServerPluginManager.validatePlugin", valid);
            return valid;
        }

        String jvmSystem = System.getProperty("java.version");
        if (metadata.getJvmVersion().size() > 0) {
            valid = false;
            for (String jvmVersion : metadata.getJvmVersion()) {
                valid = jvmSystem.startsWith(jvmVersion);
                if (valid) {
                    break;
                }
            }
        }
        if (valid == false) {
            Trace.tracePoint("Exit", "GeronimoServerPluginManager.validatePlugin", valid);
            return valid;
        }

        // check that it's not already installed
        if (metadata.getModuleId() != null) {
            Artifact artifact = toArtifact (metadata.getModuleId());
            if (getConfigurationManager().isInstalled(artifact)) {
                valid = false;
                for (ArtifactType obsolete : metadata.getObsoletes()) {
                    Artifact test = toArtifact(obsolete);
                    if (test.matches(artifact)) {
                        valid = true;
                        break;
                    }
                }
            }
        }

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.validatePlugin", valid);
        return valid;
    }

    // mimics org.apache.geronimo.system.plugin.PluginInstallerGbean.install
    public ArrayList<String> installPlugins (String localRepoDir, List<PluginType> pluginList) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.installPlugins", localRepoDir, pluginList);
        ArrayList<String> eventLog = new ArrayList<String>();

        //List<Artifact> downloadedArtifacts = new ArrayList<Artifact>();
        try {
            ConfigurationManager configManager = getConfigurationManager();
            Map<Artifact, PluginType> metaMap = new HashMap<Artifact, PluginType>();
            // Step 1: validate everything
            List<PluginType> toInstall = new ArrayList<PluginType>();
            for (PluginType metadata : pluginList) {
                try {
                    //validatePlugin(metadata);
                    verifyPrerequisites(metadata);

                    PluginArtifactType instance = metadata.getPluginArtifact().get(0);

                    if (instance.getModuleId() != null) {
                        metaMap.put(toArtifact(instance.getModuleId()), metadata);
                    }
                    toInstall.add(metadata);
                } catch (Exception e) {
                }
            }

            // Step 2: everything is valid, do the installation
            for (PluginType metadata : toInstall) {
                // 2. Unload obsoleted configurations
                PluginArtifactType instance = metadata.getPluginArtifact().get(0);
                List<Artifact> obsoletes = new ArrayList<Artifact>();
                for (ArtifactType obs : instance.getObsoletes()) {
                    Artifact obsolete = toArtifact(obs);
                    if (configManager.isLoaded(obsolete)) {
                        if (configManager.isRunning(obsolete)) {
                            configManager.stopConfiguration(obsolete);
                            eventLog.add(obsolete.toString() + " stopped");
                        }
                        configManager.unloadConfiguration(obsolete);
                        obsoletes.add(obsolete);
                    }
                }

                // 4. Uninstall obsolete configurations
                for (Artifact artifact : obsoletes) {
                    configManager.uninstallConfiguration(artifact);
                }
            }

            // Step 3: Start anything that's marked accordingly
            if (configManager.isOnline()) {
                for (int i = 0; i < toInstall.size(); i++) {
                    Artifact artifact = toArtifact(toInstall.get(i).getPluginArtifact().get(0).getModuleId());
                    if (!configManager.isRunning(artifact)) {
                        if (!configManager.isLoaded(artifact)) {
                            File serverArtifact = new File(getArtifactLocation (artifact));
                            File localDir = new File (createDirectoryStructure(localRepoDir, artifact));
                            writeToDirectory(localDir, serverArtifact);
                            configManager.loadConfiguration(artifact);
                        }
                        configManager.startConfiguration(artifact);
                        eventLog.add(artifact.toString() + " started");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.installPlugins", eventLog.toString());
        return eventLog;
    }

    // mimics org.apache.geronimo.system.plugin.PluginInstallerGbean.verifyPrerequisistes
    private void verifyPrerequisites(PluginType plugin) throws Exception {
        Trace.tracePoint("Exit", "GeronimoServerPluginManager.verifyPrerequisites", plugin);
        List<Dependency> missingPrereqs = getMissingPrerequisites(plugin);
        if (!missingPrereqs.isEmpty()) {
            PluginArtifactType metadata = plugin.getPluginArtifact().get(0);
            Artifact moduleId = toArtifact(metadata.getModuleId());
            StringBuffer buf = new StringBuffer();
            buf.append(moduleId.toString()).append(CommonMessages.requires);
            Iterator<Dependency> iter = missingPrereqs.iterator();
            while (iter.hasNext()) {
                buf.append(iter.next().getArtifact().toString());
                if (iter.hasNext()) {
                    buf.append(", ");
                }
            }
            buf.append(CommonMessages.installed);
            Trace.tracePoint("Throw", "GeronimoServerPluginManager.verifyPrerequisites", buf.toString());
            throw new Exception(buf.toString());
        }

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.verifyPrerequisites");
    }

    // mimics org.apache.geronimo.system.plugin.PluginInstallerGbean.getMissingPrerequisistes
    private List<Dependency> getMissingPrerequisites(PluginType plugin) {
        Trace.tracePoint("Entry", "GeronimoServerPluginManager.getMissingPrerequisites", plugin);

        if (plugin.getPluginArtifact().size() != 1) {
            String message = CommonMessages.bind(CommonMessages.configSizeMismatch, plugin.getPluginArtifact().size());
            Trace.tracePoint("Throw", "GeronimoServerPluginManager.getMissingPrerequisites", message);
            throw new IllegalArgumentException(message);
        }

        PluginArtifactType metadata = plugin.getPluginArtifact().get(0);
        List<PrerequisiteType> prereqs = metadata.getPrerequisite();

        ArrayList<Dependency> missingPrereqs = new ArrayList<Dependency>();
        for (PrerequisiteType prereq : prereqs) {
            Artifact artifact = toArtifact(prereq.getId());
            try {
                if (getConfigurationManager().getArtifactResolver().queryArtifacts(artifact).length == 0) {
                    missingPrereqs.add(new Dependency(artifact, ImportType.ALL));
                }
            } catch (Exception e) {
                Trace.tracePoint("Throw", "GeronimoServerPluginManager.getMissingPrerequisites", CommonMessages.noDefaultServer);
                throw new RuntimeException(CommonMessages.noDefaultServer);
            }
        }

        Trace.tracePoint("Exit", "GeronimoServerPluginManager.getMissingPrerequisites", missingPrereqs);
        return missingPrereqs;
    }

    // mimics org.apache.geronimo.system.plugin.PluginInstallerGbean.getRepos
    private List<SourceRepository> getRepos(PluginListType pluginsToInstall, SourceRepository defaultRepository, boolean restrictToDefaultRepository, PluginArtifactType instance) {
        List<SourceRepository> repos = new ArrayList<SourceRepository>();
        if (defaultRepository != null) {
            repos.add(defaultRepository);
        }
        if (!restrictToDefaultRepository) {
            List<String> repoLocations;
            if (!instance.getSourceRepository().isEmpty()) {
                repoLocations = instance.getSourceRepository();
            } else {
                repoLocations = pluginsToInstall.getDefaultRepository();
            }
            for (String repoLocation : repoLocations) {
                SourceRepository repo = SourceRepositoryFactory.getSourceRepository(repoLocation);
                repos.add(repo);
            }
        }
        return repos;
    }

}
