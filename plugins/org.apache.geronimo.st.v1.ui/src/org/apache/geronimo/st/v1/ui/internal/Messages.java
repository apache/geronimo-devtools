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
package org.apache.geronimo.st.v1.ui.internal;

import org.apache.geronimo.st.v1.ui.Activator;
import org.eclipse.osgi.util.NLS;

/**
 * Translated messages.
 *
 * @version $Rev$ $Date$
 */
public class Messages extends NLS {

	public static String editorConfigId;
	public static String editorParentId;
	public static String editorContextRoot;
	public static String editorClassloader;
	public static String editorClassloaderServer;
	public static String editorClassloaderWebApp;
	public static String securityRealmName;

	public static String dependencyGroupLabel;
	public static String serverRepos;
	public static String uri;
	public static String mavenArtifact;

	static {
		NLS.initializeMessages(Activator.PLUGIN_ID + ".internal.Messages", Messages.class);
	}
}