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
package org.apache.geronimo.st.core.commands;

import java.io.File;

import org.apache.geronimo.st.core.DeploymentUtils;
import org.apache.geronimo.st.core.IGeronimoServer;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
abstract public class DeployCommand extends AbstractDeploymentCommand {

	/**
	 * @param server
	 * @param module
	 */
	public DeployCommand(IServer server, IModule module) {
		super(server, module);
	}

	public File getTargetFile() {
		File file = null;
		IGeronimoServer gs = getGeronimoServer();
		if (gs.isRunFromWorkspace()) {
		    //TODO Re-enable after DeployableModule supported in G
			//file = generateRunFromWorkspaceConfig(getModule());
		} else {
			IPath outputDir = DeploymentUtils.STATE_LOC.append("server_" + getServer().getId());
			outputDir.toFile().mkdirs();
			file = DeploymentUtils.createJarFile(getModule(), outputDir);
		}
		Trace.trace(Trace.INFO, "File: " + file.getAbsolutePath());
		return file;
	}

	/*protected File generateRunFromWorkspaceConfig(IModule module) {
		IPath configDir = Activator.getDefault().getStateLocation().append("looseconfig").append("server_" + getServer().getId());
		configDir.toFile().mkdirs();

		ModuleDocument doc = ModuleDocument.Factory.newInstance();
		Module deployable = doc.addNewModule();
		processModuleConfig(deployable, module);

		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		File file = configDir.append(module.getName()).addFileExtension("xml").toFile();
		Trace.trace(Trace.INFO,doc.xmlText(options));
		try {
			doc.save(file, options);
			return file;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void processModuleConfig(Module deployable, IModule serverModule) {
		deployable.setName(serverModule.getName());
		J2EEFlexProjDeployable j2eeModule = (J2EEFlexProjDeployable) serverModule.loadAdapter(J2EEFlexProjDeployable.class, null);
		if (j2eeModule != null) {

			if(j2eeModule.isBinary()) {
				deployable.setPath(serverModule.getName());
				return;
			}

			IContainer[] containers = j2eeModule.getResourceFolders();
			for (int i = 0; i < containers.length; i++) {
				deployable.addNewResources().setPath(containers[i].getLocation().toOSString());
			}
			containers = j2eeModule.getJavaOutputFolders();
			for (int i = 0; i < containers.length; i++) {
				deployable.addNewClasses().setPath(containers[i].getLocation().toOSString());
			}
		}

		IModule[] children = j2eeModule.getChildModules();		
		if (children.length > 0) {
			Children modChild = deployable.addNewChildren();
			for (int i = 0; i < children.length; i++) {
				processModuleConfig(modChild.addNewModule(), children[i]);
			}
		}

		IVirtualComponent vc = ComponentCore.createComponent(serverModule.getProject());
		IVirtualReference[] refs = vc.getReferences();
		for(int i = 0; i< refs.length; i++) {
			IVirtualComponent refComp = refs[i].getReferencedComponent();
			if(refComp.isBinary()) {
				Children modChild = deployable.getChildren() == null ? deployable.addNewChildren() : deployable.getChildren();
				Module binaryModule = modChild.addNewModule();
				VirtualArchiveComponent archiveComp = (VirtualArchiveComponent) refComp;
				binaryModule.setName(archiveComp.getUnderlyingDiskFile().getName());
				binaryModule.setPath(archiveComp.getUnderlyingDiskFile().getAbsolutePath());
			}
		}
	}*/

}
