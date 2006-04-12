package org.apache.geronimo.st.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.enterprise.deploy.shared.factories.DeploymentFactoryManager;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;

import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.IPath;
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

		DeploymentManager dm = (DeploymentManager) connections.get(server.getId());

		if (dm == null) {
			DeploymentFactoryManager mgr = DeploymentFactoryManager.getInstance();
			DeploymentFactory factory = discoverDeploymentFactory(server);
			mgr.registerDeploymentFactory(factory);
			String deployerURL = getGeronimoServer(server).getDeployerURL();
			Trace.trace(Trace.INFO, "DeployerURL: " + deployerURL);
			String user = getGeronimoServer(server).getAdminID();
			String pw = getGeronimoServer(server).getAdminPassword();
			dm = mgr.getDeploymentManager(deployerURL, user, pw);
			getGeronimoServer(server).configureDeploymentManager(dm);
			connections.put(server.getId(), dm);
		}
		return dm;
	}

	private DeploymentFactory discoverDeploymentFactory(IServer server) {
		try {
			IPath path = getGeronimoServer(server).getJSR88DeployerJar();
			JarFile deployerJar = new JarFile(path.toFile());
			Manifest manifestFile = deployerJar.getManifest();
			Attributes attributes = manifestFile.getMainAttributes();
			String className = attributes.getValue("J2EE-DeploymentFactory-Implementation-Class");
			Class deploymentFactory = Class.forName(className);
			return (DeploymentFactory) deploymentFactory.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private IGeronimoServer getGeronimoServer(IServer server) {
		IGeronimoServer gServer = (IGeronimoServer) server.getAdapter(IGeronimoServer.class);
		if (gServer == null) {
			gServer = (IGeronimoServer) server.loadAdapter(IGeronimoServer.class, new NullProgressMonitor());
		}
		return gServer;
	}

	protected IGeronimoServer getGeronimoServer(DeploymentManager dm) {
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
		connections.remove(server.getId());
	}
}
