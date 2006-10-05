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
package org.apache.geronimo.st.core.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.status.DeploymentStatus;
import javax.enterprise.deploy.spi.status.ProgressEvent;
import javax.enterprise.deploy.spi.status.ProgressListener;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.apache.geronimo.st.core.Activator;
import org.apache.geronimo.st.core.DeploymentStatusMessage;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.server.core.IModule;

/**
 * This class is a wrapper IDeploymentCommand that when executed blocks the
 * callee's thread until completed, either when the waiting thread times out, or
 * a failed or completed notification is recieved from the DeploymentManager.
 */
public class SynchronizedDeploymentOp implements ProgressListener,
		IDeploymentCommand {

	private static final long TIMEOUT = 900000;

	private IDeploymentCommand command;

	private MultiStatus status = null;

	private IProgressMonitor _monitor = null;

	private boolean timedOut = true;

	public SynchronizedDeploymentOp(IDeploymentCommand command) {
		super();
		this.command = command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#execute(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public IStatus execute(IProgressMonitor monitor) throws Exception {

		_monitor = monitor;

		if (_monitor == null) {
			_monitor = new NullProgressMonitor();
		}

		ProgressObject po = run();

		return new DeploymentCmdStatus(status, po);
	}

	private synchronized ProgressObject run() throws Exception {
		Trace.trace(Trace.INFO, "--> run()");
		
		IStatus ds = command.execute(_monitor);

		ProgressObject po = null;

		if (ds instanceof DeploymentCmdStatus) {
			po = ((DeploymentCmdStatus) ds).getProgressObject();
			po.addProgressListener(this);

			try {
				wait(TIMEOUT);
			} catch (InterruptedException e) {
			}

			po.removeProgressListener(this);
			if (timedOut) {
				Trace.trace(Trace.SEVERE, "Command Timed Out!");
				status = new MultiStatus(Activator.PLUGIN_ID, 0, "", null);
				status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, command.getCommandType() + " timed out.", null));
			}
		}

		Trace.trace(Trace.INFO, "<-- run()");
		return po;
	}

	private synchronized void sendNotification() {
		timedOut = false;
		Trace.trace(Trace.INFO, "notifyAll()");
		notifyAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.enterprise.deploy.spi.status.ProgressListener#handleProgressEvent(javax.enterprise.deploy.spi.status.ProgressEvent)
	 */
	public void handleProgressEvent(ProgressEvent event) {
		DeploymentStatus deploymentStatus = event.getDeploymentStatus();
		if (deploymentStatus != null) {
			DeploymentStatusMessage dsm = new DeploymentStatusMessage(deploymentStatus);
			Trace.trace(Trace.INFO, dsm.toString());
			_monitor.subTask(dsm.toString());
			if (command.getCommandType() == deploymentStatus.getCommand()) {
				if (deploymentStatus.isCompleted()) {
					messageToStatus(IStatus.OK, dsm.getMessage(), false);
					sendNotification();
				} else if (deploymentStatus.isFailed()) {
					messageToStatus(IStatus.ERROR, dsm.getMessage(), true);
					sendNotification();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#getCommandType()
	 */
	public CommandType getCommandType() {
		return command.getCommandType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.core.commands.IDeploymentCommand#getModule()
	 */
	public IModule getModule() {
		return command.getModule();
	}
	
    public void messageToStatus(int severity, String source, boolean error) {
    	status = new MultiStatus(Activator.PLUGIN_ID, 0, "", null);
		try {
			BufferedReader in = new BufferedReader(new StringReader(source));
			String line;
			while ((line = in.readLine()) != null) {
				status.add(new Status(severity, Activator.PLUGIN_ID, 0,line, null));
			}
		} catch (IOException e) {

		}
    }
}
