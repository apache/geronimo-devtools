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

import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.status.ProgressObject;

import org.eclipse.core.runtime.IStatus;

public class DeploymentCmdStatus implements IStatus {

	private IStatus status;

	private ProgressObject po;

	public DeploymentCmdStatus(IStatus status, ProgressObject po) {
		super();
		this.status = status;
		this.po = po;
	}

	public ProgressObject getProgressObject() {
		return po;
	}

	public TargetModuleID[] getResultTargetModuleIDs() {
		if (po != null) {
			return po.getResultTargetModuleIDs();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#getChildren()
	 */
	public IStatus[] getChildren() {
		return status.getChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#getCode()
	 */
	public int getCode() {
		return status.getCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#getException()
	 */
	public Throwable getException() {
		return status.getException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#getMessage()
	 */
	public String getMessage() {
		return status.getMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#getPlugin()
	 */
	public String getPlugin() {
		return status.getPlugin();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#getSeverity()
	 */
	public int getSeverity() {
		return status.getSeverity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#isMultiStatus()
	 */
	public boolean isMultiStatus() {
		return status.isMultiStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#isOK()
	 */
	public boolean isOK() {
		return status.isOK();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#matches(int)
	 */
	public boolean matches(int severityMask) {
		return status.matches(severityMask);
	}

}
