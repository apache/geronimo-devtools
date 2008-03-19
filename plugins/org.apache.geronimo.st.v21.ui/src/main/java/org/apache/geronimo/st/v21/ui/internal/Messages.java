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
package org.apache.geronimo.st.v21.ui.internal;

import org.apache.geronimo.st.v21.ui.Activator;
import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	static {
		NLS.initializeMessages(Activator.PLUGIN_ID + ".internal.Messages", Messages.class);
	}
	
	public static String artifactId;
	public static String gBeanLink;
	public static String groupId;
	public static String moduleId;
	public static String name;
	public static String seeRestrictions;
	
	public static String useGBeanLink;
	public static String useGBeanPattern;
	public static String artifactType;
	public static String inverseClassloading;
	public static String supressDefaultEnv;
	public static String sharedLibDepends;
	
	public static String addSharedLib;
	public static String version;
	public static String webContainerSection;
	public static String webContainerSectionDescription;

}
