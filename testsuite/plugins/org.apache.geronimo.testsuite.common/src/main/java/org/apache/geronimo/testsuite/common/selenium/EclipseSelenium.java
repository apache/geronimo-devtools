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

package org.apache.geronimo.testsuite.common.selenium;

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.eclipse.swt.widgets.Shell;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * EclipseSelenium
 *
 * @version $Rev$ $Date$
 */
public class EclipseSelenium extends DefaultSelenium {

	private static String serverHost = "localhost";
	private static int serverPort = 4444;
	private static String browserStartCommand = "*EclipseBrowser";
	private static String browserURL = "http://localhost:4444";
	
	public EclipseSelenium() {
		super(serverHost, serverPort, browserStartCommand, browserURL);
	}

}
