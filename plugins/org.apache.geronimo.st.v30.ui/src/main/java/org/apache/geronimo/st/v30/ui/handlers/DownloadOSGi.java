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
package org.apache.geronimo.st.v30.ui.handlers;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.geronimo.st.v30.ui.internal.Messages;
import org.apache.geronimo.st.v30.ui.internal.Trace;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.internal.browser.WorkbenchBrowserSupport;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.RepositoryTracker;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

/**
 * Handler to download the OSGi Application Development tools (i.e., Aries free tooling)
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 * 
 * @version $Rev$ $Date$
 */
public class DownloadOSGi extends AbstractHandler  {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        Trace.tracePoint("Entry", "DownloadOSGi.execute", event);

        String url = Messages.DownloadOSGiURL;

            int style = IWorkbenchBrowserSupport.AS_EXTERNAL | IWorkbenchBrowserSupport.STATUS;
//            IWebBrowser browser = WorkbenchBrowserSupport.getInstance().createBrowser(style, "", "", "");
//            browser.openURL(new URL(url));
            ProvisioningUI provisioningUI = ProvisioningUI.getDefaultUI();
            RepositoryTracker tracker = provisioningUI.getRepositoryTracker();
            URI[] uri=new URI[1];
            try {
                uri[0]=new URI("http://public.dhe.ibm.com/ibmdl/export/pub/software/rational/OSGiAppTools");
            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tracker.addRepository(uri[0], null, provisioningUI.getSession());
           // InstallOperation installOperation = provisioningUI.getInstallOperation(null,uri);
            provisioningUI.openInstallWizard(null, null, null);
            try {
                provisioningUI.loadMetadataRepository(uri[0],true,null);
            } catch (ProvisionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           // tracker.refreshRepositories(arg0, provisioningUI., arg2);
//        catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        catch (PartInitException e) {
//            e.printStackTrace();
//        }

        Trace.tracePoint("Exit", "DownloadOSGi.execute");
        return null;
    }

}
