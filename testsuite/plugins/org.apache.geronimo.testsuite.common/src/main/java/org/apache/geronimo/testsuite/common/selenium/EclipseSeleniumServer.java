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
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.swt.widgets.Shell;
import org.openqa.selenium.server.SeleniumServer;
import org.openqa.selenium.server.browserlaunchers.BrowserLauncherFactory;
import org.openqa.selenium.server.browserlaunchers.MockBrowserLauncher;

/**
 * EclipseSeleniumServer
 *
 * @version $Rev$ $Date$
 */
public class EclipseSeleniumServer implements ISafeRunnable{

    public static EclipseSeleniumServer INSTANCE;
    private SeleniumServer server;
    private AbbotHelper aHelper;
    private Shell shell;

    public EclipseSeleniumServer( AbbotHelper aHelper, Shell shell ) {
        this.aHelper = aHelper;
        this.shell = shell;
        INSTANCE = this;
    }

    public void stop() {
        server.stop();
    }

    public AbbotHelper getAHelper() {
        return aHelper;
    }

    public Shell getShell() {
        return shell;
    }

    public void handleException(Throwable arg0) {

    }

    public void run() throws Exception {
        try {
            BrowserLauncherFactory.addBrowserLauncher("MockBrowser", MockBrowserLauncher.class);
            //BrowserLauncherFactory.addBrowserLauncher("Firefox Browser", Firefox3Launcher.class);
            //SeleniumServer.setDebugMode (true);
            server = new SeleniumServer();
            server.start();
        } catch (Throwable th) {
            th.printStackTrace();
            Exception e = new Exception("unable to start Selenium server");
            e.initCause(th);
            throw (e);
        }
    }

}
