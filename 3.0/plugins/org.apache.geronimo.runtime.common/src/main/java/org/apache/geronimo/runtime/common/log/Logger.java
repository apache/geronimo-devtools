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


package org.apache.geronimo.runtime.common.log;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class Logger extends Plugin{
	private static ILog log;
	public static int visitied;
	private static Logger self = new Logger();
	@Override
	public boolean isDebugging() {
		// TODO Auto-generated method stub
		return super.isDebugging();
	}

	@Override
	public void setDebugging(boolean value) {
		// TODO Auto-generated method stub
		super.setDebugging(value);
	}


	@Override
	public void start(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.start(context);
		if(log == null) log = this.getLog();
		System.out.println(context.getBundle().getSymbolicName() + ": started");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		super.stop(context);
	}
	
	public static Logger getInstance() {
		++ visitied;
		return self;
	}
	
	public void trace(int level, String pluginId, String msg, Throwable e) {
		if(log == null) log = this.getLog();
		log.log(new Status(level, pluginId, msg, e));
	}

}
