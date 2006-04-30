package org.apache.geronimo.st.v11.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.geronimo.deployment.plugin.GeronimoDeploymentManager;
import org.apache.geronimo.deployment.plugin.TargetModuleIDImpl;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.st.jmxagent.Activator;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;

public class ConfigStoreInstaller implements IServerListener {

	public void serverChanged(ServerEvent event) {
		Trace.trace(Trace.INFO, "--> ConfigStoreInstaller.serverChanged()");
		if (event.getKind() == (ServerEvent.SERVER_CHANGE | ServerEvent.STATE_CHANGE)) {
			if (event.getServer().getServerState() == IServer.STATE_STARTED) {
				install(event.getServer());
			}
		}
		Trace.trace(Trace.INFO, "<-- ConfigStoreInstaller().serverChanged()");
	}

	private void install(IServer server) {
		Trace.trace(Trace.INFO, "--> ConfigStoreInstaller.install()");
		try {

			GeronimoDeploymentManager dm = (GeronimoDeploymentManager) GeronimoConnectionFactory
					.getInstance().getDeploymentManager(server);

			Target target = dm.getTargets()[0];
			Trace.trace(Trace.INFO, "target name: " + target.getName());

			File jar = new File(resolveFromBundle(
					"/lib/config-store-service-1.0-SNAPSHOT.jar").getFile());
			File plan = new File(resolveFromBundle("/plan.xml").getFile());

			ProgressObject po = dm.distribute(new Target[] { target }, jar,
					plan);
			waitForCompletion(po);

			TargetModuleID id = new TargetModuleIDImpl(target,
					Activator.SERVICE_ID);

			po = dm.start(new TargetModuleID[] { id });
			waitForCompletion(po);

		} catch (DeploymentManagerCreationException e) {
			e.printStackTrace();
		}
		Trace.trace(Trace.INFO, "--> ConfigStoreInstaller.install()");
	}

	private URL resolveFromBundle(String path) {
		try {
			return Platform.resolve(Activator.getDefault().getBundle()
					.getEntry(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void waitForCompletion(ProgressObject po) {
		while (po.getDeploymentStatus().isRunning()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
	}
}
