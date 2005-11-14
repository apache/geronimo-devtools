package org.apache.geronimo.core;

import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressEvent;
import javax.enterprise.deploy.spi.status.ProgressListener;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.geronimo.core.commands.AbstractDeploymentCommand;
import org.apache.geronimo.core.commands.IDeploymentCommand;
import org.apache.geronimo.core.internal.DeploymentStatusMessageTranslator;
import org.apache.geronimo.core.internal.GeronimoPlugin;
import org.apache.geronimo.core.internal.Messages;
import org.apache.geronimo.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;

public class CommandLauncher implements ProgressListener {

	private static final long TIMEOUT = 10000;

	private IDeploymentCommand command;

	private IModule module;

	private IServer server;

	private Thread waitThread;

	private IStatus status = null;

	private TargetModuleID[] result;

	private IProgressMonitor _monitor = null;

	public CommandLauncher(IDeploymentCommand command, IModule module,
			IServer server) {
		super();
		this.command = command;
		this.module = module;
		this.server = server;
	}

	public IStatus executeCommand(IProgressMonitor monitor)
			throws CoreException {

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		_monitor = monitor;

		try {
			setDeploymentManager();
		} catch (DeploymentManagerCreationException e) {
			throw new CoreException(
					new Status(IStatus.ERROR, GeronimoPlugin.PLUGIN_ID, 0,
							Messages.DM_CONNECTION_FAIL, e));
		}

		waitThread = new WaitForNotificationThread();
		waitThread.start();
		ProgressObject po = command.execute(module);
		po.addProgressListener(this);

		try {
			waitThread.join();
		} catch (InterruptedException e) {
		} finally {
			po.removeProgressListener(this);
		}

		result = po.getResultTargetModuleIDs();

		return status;
	}

	private void setDeploymentManager()
			throws DeploymentManagerCreationException {
		DeploymentManager mgr = GeronimoConnectionFactory.getInstance()
				.getDeploymentManager(server);
		((AbstractDeploymentCommand) command).setDeploymentManager(mgr);
	}

	class WaitForNotificationThread extends Thread {
		public void run() {
			try {
				sleep(TIMEOUT);
				Trace.trace(Trace.INFO, "Wait thread TIMEOUT!");
			} catch (InterruptedException e) {
				Trace.trace(Trace.INFO, "Wait thread interrupted");
			}
		}
	}

	public void handleProgressEvent(ProgressEvent event) {
		DeploymentStatus deploymentStatus = event.getDeploymentStatus();
		if (deploymentStatus != null) {
			String msg = DeploymentStatusMessageTranslator
					.getTranslatedMessage(event, module.getProject());
			Trace.trace(Trace.INFO, msg);
			_monitor.subTask(msg);
			if (command.getCommandType() == deploymentStatus.getCommand()) {
				if (deploymentStatus.isCompleted()) {
					status = new Status(IStatus.OK, GeronimoPlugin.PLUGIN_ID,
							0, msg, null);
					waitThread.interrupt();
				} else if (deploymentStatus.isFailed()) {
					status = new Status(IStatus.ERROR,
							GeronimoPlugin.PLUGIN_ID, 0, msg, null);
					waitThread.interrupt();
				}
			}
		}
	}

	public TargetModuleID[] getResultTargetModuleIDs() {
		return result;
	}

}
