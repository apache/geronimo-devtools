package org.apache.geronimo.core.operations;

import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressEvent;
import javax.enterprise.deploy.spi.status.ProgressListener;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.geronimo.core.commands.IDeploymentCommand;
import org.apache.geronimo.core.internal.DeploymentStatusMessageTranslator;
import org.apache.geronimo.core.internal.GeronimoPlugin;
import org.apache.geronimo.core.internal.Trace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;

public class SynchronizedDeploymentOp implements ProgressListener {

	private static final long TIMEOUT = 10000;

	private IDeploymentCommand command;

	private Thread waitThread;

	private IStatus status = null;

	private TargetModuleID[] result;

	private IProgressMonitor _monitor = null;

	public SynchronizedDeploymentOp(IDeploymentCommand command) {
		super();
		this.command = command;
	}

	public IStatus run(IProgressMonitor monitor)
			throws CoreException {

		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		_monitor = monitor;

		waitThread = new WaitForNotificationThread();
		waitThread.start();
		ProgressObject po = command.execute();
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
					.getTranslatedMessage(event, command.getModule().getProject());
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
