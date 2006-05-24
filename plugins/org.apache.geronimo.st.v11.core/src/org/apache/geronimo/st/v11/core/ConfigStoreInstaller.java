package org.apache.geronimo.st.v11.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.enterprise.deploy.spi.Target;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.geronimo.deployment.plugin.TargetModuleIDImpl;
import org.apache.geronimo.deployment.plugin.jmx.JMXDeploymentManager;
import org.apache.geronimo.gbean.AbstractName;
import org.apache.geronimo.kernel.GBeanNotFoundException;
import org.apache.geronimo.kernel.config.Configuration;
import org.apache.geronimo.kernel.config.IOUtil;
import org.apache.geronimo.kernel.management.State;
import org.apache.geronimo.kernel.repository.Artifact;
import org.apache.geronimo.st.core.GeronimoConnectionFactory;
import org.apache.geronimo.st.jmxagent.Activator;
import org.apache.geronimo.st.v11.core.internal.Trace;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;

public class ConfigStoreInstaller implements IServerListener {

	public void serverChanged(ServerEvent event) {
		if (event.getKind() == (ServerEvent.SERVER_CHANGE | ServerEvent.STATE_CHANGE)) {
			if (event.getServer().getServerState() == IServer.STATE_STARTED) {
				install(event.getServer());
				event.getServer().removeServerListener(this);
			}
		}
	}

	private void install(IServer server) {
		Trace.trace(Trace.INFO, "--> ConfigStoreInstaller.install()");
		try {
			
			copyToRepository(server);
			server.getRuntime().getLocation().append("eclipse-repository").toFile().mkdir();

			JMXDeploymentManager dm = (JMXDeploymentManager) GeronimoConnectionFactory.getInstance().getDeploymentManager(server);
			dm.setInPlace(false);

			Target target = dm.getTargets()[0];
			Trace.trace(Trace.INFO, "target name: " + target.getName());

			GeronimoServerBehaviour gsb = (GeronimoServerBehaviour) server.loadAdapter(GeronimoServerBehaviour.class, null);
			Artifact artifact = Artifact.create(Activator.SERVICE_ID);
			AbstractName name = Configuration.getConfigurationAbstractName(artifact);
			
			State state = null;
			try {
				state = State.fromInt(gsb.getKernel().getGBeanState(name));
			} catch (GBeanNotFoundException e) {
				Trace.trace(Trace.INFO, Activator.SERVICE_ID + " not installed");
				//store not installed
			} 
			
			if(state == null) {
				File jar = new File(resolveFromBundle("/lib/config-store-service-1.0.jar").getFile());
				File plan = new File(resolveFromBundle("/plan.xml").getFile());
				Trace.trace(Trace.INFO, "installing " + Activator.SERVICE_ID);
				ProgressObject po = dm.distribute(new Target[] { target }, jar, plan);
				waitForCompletion(po);
			}
			
			if(!gsb.getKernel().isRunning(name)) {
				Trace.trace(Trace.INFO, "starting " + Activator.SERVICE_ID);
				TargetModuleID id = new TargetModuleIDImpl(target, Activator.SERVICE_ID);
				ProgressObject po = dm.start(new TargetModuleID[] { id });
				waitForCompletion(po);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Trace.trace(Trace.INFO, "<-- ConfigStoreInstaller.install()");
		}
	}

	private URL resolveFromBundle(String path) {
		try {
			return Platform.resolve(Activator.getDefault().getBundle().getEntry(path));
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

	private void copyToRepository(IServer server) {
		IPath repo = server.getRuntime().getLocation().append("repository");

		IPath path = repo.append("/hessian/hessian/3.0.8/hessian-3.0.8.jar");
		copyFile(getFileFromBundle("lib/hessian-3.0.8.jar"), path.toFile());

		path = repo.append("/mx4j/mx4j-tools/3.0.1/mx4j-tools-3.0.1.jar");
		copyFile(getFileFromBundle("lib/mx4j-tools-3.0.1.jar"), path.toFile());
	}
	

	private static void copyFile(File src, File dest) {
		try {
			if (!dest.exists()) {
				Trace.trace(Trace.INFO, "adding " + dest);
				IOUtil.copyFile(src, dest);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private File getFileFromBundle(String path) {
		try {
			URL url = Platform.resolve(Activator.getDefault().getBundle().getEntry(path));
			return new File(url.getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
