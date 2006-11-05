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
package org.apache.geronimo.deployment;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.geronimo.xbeans.eclipse.deployment.ModuleDocument;
import org.apache.geronimo.xbeans.eclipse.deployment.ClassesDocument.Classes;
import org.apache.geronimo.xbeans.eclipse.deployment.ModuleDocument.Module;
import org.apache.geronimo.xbeans.eclipse.deployment.ResourcesDocument.Resources;
import org.apache.xmlbeans.XmlException;

/**
 * @version $Rev$ $Date$
 */
public class EclipseDeployableModule /*implements DeployableModule*/ {

	private Module module = null;

	private File root;
	
	private String uri;

	private File[] classesFolders = null;

	private File[] resourcesFolders = null;

	//private DeployableModule[] children = null;
	
	private boolean archived = false;
	
	public EclipseDeployableModule(File config) {
		try {
			module = ModuleDocument.Factory.parse(config).getModule();
			init();
		} catch (XmlException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param module
	 */
	private EclipseDeployableModule(Module module) {
		this.module = module;
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#cleanup()
	 */
	public void cleanup() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getClassesFolders()
	 */
	public File[] getClassesFolders() {
		if (classesFolders != null) {
			return classesFolders;
		}

		Classes[] classes = module.getClassesArray();
		classesFolders = new File[classes.length];
		for (int i = 0; i < classesFolders.length; i++) {
			File file = new File(classes[i].getPath());
			if (file.exists()) {
				classesFolders[i] = file;
			} else {

			}
		}
		return classesFolders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getModuleContextResources()
	 */
	public File[] getModuleContextResources() {
		if (resourcesFolders != null) {
			return resourcesFolders;
		}

		Resources[] resources = module.getResourcesArray();
		resourcesFolders = new File[resources.length];
		for (int i = 0; i < classesFolders.length; i++) {
			File file = new File(resources[i].getPath());
			if (file.exists()) {
				resourcesFolders[i] = file;
			} else {

			}
		}

		return resourcesFolders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getModules()
	 */
	/*public DeployableModule[] getModules() {
		if (children != null) {
			return children;
		}

		Module[] childModules = module.getChildren().getModuleArray();
		children = new DeployableModule[childModules.length];
		for (int i = 0; i < childModules.length; i++) {
			Module child = childModules[i];
			children[i] = new EclipseDeployableModule(child);
		}

		return children;
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getRoot()
	 */
	public File getRoot() {
		if (root != null) {
			return root;
		}
		root = new File(module.getPath());
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getURI()
	 */
	public String getURI() {
		if(uri != null) {
			return uri;
		}
		uri = module.getName();
		return uri;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#isArchived()
	 */
	public boolean isArchived() {
		return archived;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#resolve(java.lang.String)
	 */
	public URL resolve(String path) throws IOException {
		File[] search = getModuleContextResources();
		for(int i = 0; i <  search.length; i++) {
			String findPath = search[i].getAbsolutePath().concat(path);
			File file = new File(findPath);
			if(file.exists()) 
				return file.toURL();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#resolveModule(java.lang.String)
	 */
/*	public DeployableModule resolveModule(String uri) throws IOException {
		DeployableModule[] children = getModules();
		for(int i = 0; i < children.length; i++) {
			if(children[i].getURI().equals(uri)) {
				return children[i];
			}
		}
		return null;
	}*/
	
	private void init() {
		archived = getRoot().isFile();
	}
}
