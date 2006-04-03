/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.eclipse.devtools;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal createdeployable
 */
public class DeployableDistributionMojo extends PackagingMojo {

	/**
	 * @parameter
	 */
	private List explodedBundles;

	/**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
	 * @required
	 * @readonly
	 */
	protected ArchiverManager archiverManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.eclipse.devtools.PackagingMojo#processPlugin(org.apache.maven.artifact.Artifact)
	 */
	void processPlugin(Artifact artifact) throws Exception {
		if (explodedBundles.contains(artifact.getArtifactId())) {
			UnArchiver unArchiver = archiverManager.getUnArchiver(FileUtils.getExtension(artifact.getFile().getName()));
			unArchiver.setSourceFile(artifact.getFile());
			unArchiver.setDestDirectory((getExpandedBundleDirectory(artifact, getPluginsDestination())));
			unArchiver.extract();
		} else {
			FileUtils.copyFile(artifact.getFile(), getDestinationFile(artifact, getPluginsDestination()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.eclipse.devtools.PackagingMojo#processFeature(org.apache.maven.artifact.Artifact)
	 */
	void processFeature(Artifact artifact) throws Exception {
		UnArchiver unArchiver = archiverManager.getUnArchiver(FileUtils.getExtension(artifact.getFile().getName()));
		unArchiver.setSourceFile(artifact.getFile());
		unArchiver.setDestDirectory(getExpandedBundleDirectory(artifact, getFeaturesDestination()));
		unArchiver.extract();
	}
	
	public static File getExpandedBundleDirectory(Artifact artifact, File destinationRoot) {
		String fileName = artifact.getFile().getName().replaceFirst("-", "_");
		String dirName = fileName.substring(0, fileName.indexOf(FileUtils.getExtension(fileName))-1);
		File dest = new File(destinationRoot + File.separator + dirName);
		dest.mkdirs();
		return dest;
	}

}
