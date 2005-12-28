package org.apache.geronimo.core.commands;

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressEvent;
import javax.enterprise.deploy.spi.status.ProgressListener;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.geronimo.core.internal.DeploymentStatusMessageTranslator;
import org.apache.geronimo.core.internal.GeronimoPlugin;
import org.apache.geronimo.core.internal.Trace;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;

public class SynchronizedDeploymentOp implements ProgressListener,
		IDeploymentCommand {

	private static final long TIMEOUT = 10000;

	private IDeploymentCommand command;

	private Thread waitThread;

	private IStatus status = null;

	private IProgressMonitor _monitor = null;

	public SynchronizedDeploymentOp(IDeploymentCommand command) {
		super();
		this.command = command;
	}

	public IStatus execute(IProgressMonitor monitor) {

		_monitor = monitor;

		if (_monitor == null) {
			_monitor = new NullProgressMonitor();
		}

		waitThread = new WaitForNotificationThread();
		waitThread.start();

		IStatus ds = command.execute(_monitor);
		
		ProgressObject po = null;

		if (ds instanceof DeploymentCmdStatus) {
			
			po = ((DeploymentCmdStatus) ds).getProgressObject();

			po.addProgressListener(this);

			try {
				waitThread.join();
			} catch (InterruptedException e) {
			} finally {
				po.removeProgressListener(this);
			}

		}

		return new DeploymentCmdStatus(status, po);

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
					.getTranslatedMessage(event, command.getModule()
							.getProject());
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

	public CommandType getCommandType() {
		return command.getCommandType();
	}

	public IModule getModule() {
		return command.getModule();
	}

}
