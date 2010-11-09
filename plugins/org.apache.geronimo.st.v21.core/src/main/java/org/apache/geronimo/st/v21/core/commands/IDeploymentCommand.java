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

package org.apache.geronimo.st.v21.core.commands;

import javax.enterprise.deploy.shared.CommandType;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.server.core.IModule;

/**
 * @version $Rev: 568527 $ $Date: 2007-08-22 17:21:59 +0800 (Wed, 22 Aug 2007) $
 */
public interface IDeploymentCommand {

	public IStatus execute(IProgressMonitor monitor) throws Exception;

	public CommandType getCommandType();

	public IModule getModule();
	
	public long getTimeout();

}
