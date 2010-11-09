/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.st.v11.core;

import javax.enterprise.deploy.shared.ActionType;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.spi.status.DeploymentStatus;

/**
 * @version $Rev: 471551 $ $Date: 2006-11-06 06:47:11 +0800 (Mon, 06 Nov 2006) $
 */
public class DeploymentStatusMessage {

	private DeploymentStatus status;
	private ActionType action;
	private CommandType command;
	private String message;

	public DeploymentStatusMessage(DeploymentStatus status) {
		this.status = status;
		this.action = status.getAction();
		this.command = status.getCommand();
		this.message = status.getMessage();
	}

	public ActionType getAction() {
		return action;
	}

	public CommandType getCommand() {
		return command;
	}

	public String getMessage() {
		return message;
	}

	public DeploymentStatus getStatus() {
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * [ActionType CommandType] message
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[").append(action).append(" ").append(command).append("] ").append(message);
		return buffer.toString();
	}

}
