/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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

	/* (non-Javadoc)
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
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

	/* (non-Javadoc)
	 * @see javax.enterprise.deploy.spi.status.ProgressListener#handleProgressEvent(javax.enterprise.deploy.spi.status.ProgressEvent)
	 */
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

	/* (non-Javadoc)
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#getCommandType()
	 */
	public CommandType getCommandType() {
		return command.getCommandType();
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#getModule()
	 */
	public IModule getModule() {
		return command.getModule();
	}

}
