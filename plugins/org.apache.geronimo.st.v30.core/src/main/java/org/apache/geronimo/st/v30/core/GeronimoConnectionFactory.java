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
package org.apache.geronimo.st.v30.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;
import javax.naming.Context;

import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServer;

/**
 * @version $Rev$ $Date$
 */
public class GeronimoConnectionFactory {

    private Map<String, DeploymentManager> connections = 
        Collections.synchronizedMap(new HashMap<String, DeploymentManager>());

    private static GeronimoConnectionFactory instance = new GeronimoConnectionFactory();

    private GeronimoConnectionFactory() {
        super();
    }

    public static GeronimoConnectionFactory getInstance() {
        return instance;
    }

    public DeploymentManager getDeploymentManager(IServer server) throws DeploymentManagerCreationException {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
        DeploymentManager dm = (DeploymentManager) connections.get(server.getId());
        IGeronimoServer geronimoServer = getGeronimoServer(server);
        if (dm == null) {
            DeploymentFactory factory = geronimoServer.getDeploymentFactory();
            String deployerURL = geronimoServer.getDeployerURL();
            Trace.trace(Trace.INFO, "DeployerURL: " + deployerURL, Activator.traceCore);
            String user = geronimoServer.getAdminID();
            String pw = geronimoServer.getAdminPassword();
            dm = factory.getDeploymentManager(deployerURL, user, pw);
            connections.put(server.getId(), dm);
        }
        geronimoServer.configureDeploymentManager(dm);

        return dm;
    }

    private IGeronimoServer getGeronimoServer(IServer server) {
        IGeronimoServer gServer = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
        if (gServer == null) {
            gServer = (IGeronimoServer) server.loadAdapter(IGeronimoServer.class, new NullProgressMonitor());
        }
        return gServer;
    }
 
    public void destroy(IServer server) {
        Trace.tracePoint("Entry", Activator.traceCore, "GeronimoConnectionFactory.destroy");

        DeploymentManager manager = connections.remove(server.getId());        
        if (manager != null) {
            // TODO: need to do reference counting or something else before releasing the connection
            // manager.release();
        }
        
        Trace.tracePoint("Exit", Activator.traceCore, "GeronimoConnectionFactory.destroy");
    }
}
