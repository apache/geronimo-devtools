package org.apache.geronimo.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import javax.enterprise.deploy.shared.factories.DeploymentFactoryManager;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;

import org.apache.geronimo.core.internal.GeronimoServer;
import org.apache.geronimo.core.internal.Trace;
import org.apache.geronimo.deployment.plugin.factories.DeploymentFactoryImpl;
import org.apache.geronimo.deployment.plugin.jmx.JMXDeploymentManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.server.core.IServer;

public class GeronimoConnectionFactory {

	// private final static String DEFAULT_URI =
	// "deployer:geronimo:jmx:rmi://localhost/jndi/rmi:/JMXConnector";

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
		DeploymentManager dm = (DeploymentManager) connections.get(server
				.getId());

		if (dm == null) {
			DeploymentFactoryManager mgr = DeploymentFactoryManager
					.getInstance();
			DeploymentFactory factory = new DeploymentFactoryImpl();
			mgr.registerDeploymentFactory(factory);
			String deployerURL = getDeployerURL(server);
			Trace.trace(Trace.INFO, "DeployerURL: " + deployerURL);
			dm = mgr.getDeploymentManager(deployerURL, getUserName(server),
					getPassword(server));
			if (dm instanceof JMXDeploymentManager) {
				((JMXDeploymentManager) dm).setLogConfiguration(true, true);
			}
			connections.put(server.getId(), dm);
		}

		return dm;
	}

	public void destroy(IServer server) {
		connections.remove(server.getId());
	}

	private String getDeployerURL(IServer server) {
		// return "deployer:geronimo:jmx:rmi://" + server.getHost() +
		// "/jndi/rmi://" + server.getHost() + ":" + getRMINamingPort(server) +
		// "/JMXConnector";
		return "deployer:geronimo:jmx://" + server.getHost() + ":"
				+ getRMINamingPort(server);
	}

	private DeploymentFactory discoverDeploymentFactory(IServer server) {

		try {

			IPath path = server.getRuntime().getLocation()
					.removeLastSegments(1).append(
							"/lib/geronimo-deploy-jsr88-1.0.jar");

			JarFile deployerJar = new JarFile(path.toFile());
			java.util.jar.Manifest manifestFile = deployerJar.getManifest();
			Attributes attributes = manifestFile.getMainAttributes();
			String key = "J2EE-DeploymentFactory-Implementation-Class";
			String className = attributes.getValue(key);
			Class deploymentFactory = Class.forName(className);
			DeploymentFactory df = (DeploymentFactory) deploymentFactory
					.newInstance();
			return df;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getUserName(IServer server) {
		GeronimoServer gServer = getGeronimoServer(server);
		return gServer.getAdminID();
	}

	public String getPassword(IServer server) {
		GeronimoServer gServer = getGeronimoServer(server);
		return gServer.getAdminPassword();
	}

	public String getRMINamingPort(IServer server) {
		GeronimoServer gServer = getGeronimoServer(server);
		return gServer.getRMINamingPort();
	}
	
	private GeronimoServer getGeronimoServer(IServer server) {
		GeronimoServer gServer = (GeronimoServer) server
				.getAdapter(GeronimoServer.class);
		if (gServer == null) {
			gServer = (GeronimoServer) server.loadAdapter(GeronimoServer.class,
					new NullProgressMonitor());
		}
		return gServer;
	}

}
