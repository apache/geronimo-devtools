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

package org.apache.geronimo.testsuite.v20;

import org.apache.geronimo.testsuite.common.selenium.EclipseSeleniumServer;
import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleContext;

import abbot.swt.eclipse.utils.WorkbenchUtilities;

/**
 * The activator class controls the plug-in life cycle
 *
 * @version $Rev: 677978 $ $Date: 2008-07-18 10:34:28 -0700 (Fri, 18 Jul 2008) $
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.apache.geronimo.st.v20.core";

	// The shared instance
	private static Activator plugin;

	private EclipseSeleniumServer seleniumServer;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	    Shell workbenchShell = WorkbenchUtilities.getWorkbenchWindow().getShell();
	    AbbotHelper aHelper = new AbbotHelper (workbenchShell);
    	seleniumServer = new EclipseSeleniumServer(aHelper, workbenchShell);
		try {
			SafeRunner.run( seleniumServer );
			Thread.sleep(5000);
//			new Exception("sdsfsdf").printStackTrace();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		seleniumServer.stop();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public static void log(int severity, String message, Throwable throwable) {
		plugin.getLog().log(new Status(severity, PLUGIN_ID, 0, message, throwable));
	}
}
