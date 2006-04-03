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

import org.apache.maven.artifact.Artifact;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal updatesite
 */
public class UpdateDistribtuionMojo extends PackagingMojo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.eclipse.devtools.PackagingMojo#processPlugin(org.apache.maven.artifact.Artifact)
	 */
	void processPlugin(Artifact artifact) throws Exception {
		FileUtils.copyFile(artifact.getFile(), getDestinationFile(artifact, getPluginsDestination()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.eclipse.devtools.PackagingMojo#processFeature(org.apache.maven.artifact.Artifact)
	 */
	void processFeature(Artifact artifact) throws Exception {
		FileUtils.copyFile(artifact.getFile(), getDestinationFile(artifact, getFeaturesDestination()));
	}
}
