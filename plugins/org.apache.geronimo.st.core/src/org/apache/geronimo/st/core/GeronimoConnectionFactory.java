package org.apache.geronimo.st.core;

import java.util.HashMap;
import java.util.Iterator;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;
import javax.naming.Context;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

public class GeronimoConnectionFactory {

	private HashMap connections = new HashMap();

	private static GeronimoConnectionFactory instance;

	private GeronimoConnectionFactory() {
		super();
	}

	public static GeronimoConnectionFactory getInstance() {
		if (instance == null) {
			instance = new GeronimoConnectionFactory();
		}
		return instance;
	}

	public DeploymentManager getDeploymentManager(IServer server)
			throws DeploymentManagerCreationException {

		System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
		DeploymentManager dm = (DeploymentManager) connections.get(server.getId());

		if (dm == null) {
			DeploymentFactory factory = getGeronimoServer(server).getDeploymentFactory();
			String deployerURL = getGeronimoServer(server).getDeployerURL();
			Trace.trace(Trace.INFO, "DeployerURL: " + deployerURL);
			String user = getGeronimoServer(server).getAdminID();
			String pw = getGeronimoServer(server).getAdminPassword();
			dm = factory.getDeploymentManager(deployerURL, user, pw);
			connections.put(server.getId(), dm);
		}
		
		getGeronimoServer(server).configureDeploymentManager(dm);
		
		return dm;
	}

	private IGeronimoServer getGeronimoServer(IServer server) {
		IGeronimoServer gServer = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
		if (gServer == null) {
			gServer = (IGeronimoServer) server.loadAdapter(IGeronimoServer.class, new NullProgressMonitor());
		}
		return gServer;
	}

	public IGeronimoServer getGeronimoServer(DeploymentManager dm) {
		if (dm != null && connections.containsValue(dm)) {
			Iterator i = connections.keySet().iterator();
			while (i.hasNext()) {
				String serverId = (String) i.next();
				Object o = connections.get(serverId);
				if (dm.equals(o)) {
					IServer server = ServerCore.findServer(serverId);
					if (server != null)
						return getGeronimoServer(server);
				}
			}
		}
		return null;
	}

	public void destroy(IServer server) {
		Trace.trace(Trace.INFO, "deploymentManager destroy");
		connections.remove(server.getId());
	}
}
