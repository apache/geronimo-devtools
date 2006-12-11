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
import java.util.jar.JarFile;

import org.apache.geronimo.common.DeploymentException;
import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoBuilder;
import org.apache.geronimo.xbeans.eclipse.deployment.ModuleDocument;
import org.apache.geronimo.xbeans.eclipse.deployment.ClassesDocument.Classes;
import org.apache.geronimo.xbeans.eclipse.deployment.ModuleDocument.Module;
import org.apache.geronimo.xbeans.eclipse.deployment.ResourcesDocument.Resources;

/**
 * @version $Rev$ $Date$
 */
public class EclipseDeployableModule implements DeployableModule {

	private Module module = null;

	private File root;

	private String uri;

	private File[] classesFolders = null;

	private File[] resourcesFolders = null;

	private DeployableModule[] children = null;

	private boolean archived = false;

	public EclipseDeployableModule() {
	}

	public EclipseDeployableModule(File config) throws DeploymentException {
		try {
			module = ModuleDocument.Factory.parse(config).getModule();
			init();
		} catch (Exception e) {
			throw new DeploymentException(e);
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
		for (int i = 0; i < resourcesFolders.length; i++) {
			File file = new File(resources[i].getPath());
			resourcesFolders[i] = file;
		}

		return resourcesFolders;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getModules()
	 */
	public DeployableModule[] getModules() {
		if (children != null) {
			return children;
		}

		if (module.getChildren() != null) {
			Module[] childModules = module.getChildren().getModuleArray();
			children = new DeployableModule[childModules.length];
			for (int i = 0; i < childModules.length; i++) {
				Module child = childModules[i];
				children[i] = new EclipseDeployableModule(child);
			}
		} else {
			children = new EclipseDeployableModule[]{};
		}

		return children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getRoot()
	 */
	public File getRoot() {
		if (root != null) {
			return root;
		}
		if (module.getPath() != null) {
			root = new File(module.getPath());
		} else {
			root = new File(module.getResourcesArray(0).getPath());
		}
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getURI()
	 */
	public String getURI() {
		if (uri != null) {
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
		System.out.println("Resolving: " + path);
		File[] search = getModuleContextResources();
		for (int i = 0; i < search.length; i++) {
			File file = new File(search[i].getAbsolutePath(), path);
			if (file.exists()) {
				System.out.println("Resolved to: " + file);
				return file.toURL();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#resolveModule(java.lang.String)
	 */
	public DeployableModule resolveModule(String uri) throws IOException {
		DeployableModule[] children = getModules();
		for (int i = 0; i < children.length; i++) {
			if (children[i].getURI().equals(uri)) {
				return children[i];
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.deployment.DeployableModule#getArchive()
	 */
	public JarFile getArchive() {
		return null;
	}

	private void init() {
		archived = getRoot().isFile();
	}

	public static final GBeanInfo GBEAN_INFO;

	static {
		GBeanInfoBuilder infoBuilder = GBeanInfoBuilder.createStatic(
				EclipseDeployableModule.class, "DeployableModule");
		infoBuilder.addInterface(DeployableModule.class);
		GBEAN_INFO = infoBuilder.getBeanInfo();
	}

	public static GBeanInfo getGBeanInfo() {
		return GBEAN_INFO;
	}

}
