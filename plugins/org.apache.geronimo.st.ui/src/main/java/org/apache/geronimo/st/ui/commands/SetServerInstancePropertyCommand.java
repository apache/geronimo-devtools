/**
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
package org.apache.geronimo.st.ui.commands;

import java.lang.reflect.Method;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.wst.server.core.IServerWorkingCopy;

/**
 * @version $Rev$ $Date$
 */
public class SetServerInstancePropertyCommand extends ServerCommand {
	
	Object value;
	Object oldValue;
	Class valueType;
	
	String setterName;
	String getterName;
	
	Class adapterClass;
	Object adaptedClass;

	public SetServerInstancePropertyCommand(IServerWorkingCopy server, Object value, String setterName, Class valueType, Class adapterClass) {
		super(server, value.toString());
		this.value = value;
		this.setterName = setterName;
		this.getterName = "get" + setterName.split("set")[1];
		this.valueType = valueType;
		this.adapterClass = adapterClass;
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.ui.commands.ServerCommand#execute()
	 */
	public void execute() {
		adaptedClass = server.getAdapter(adapterClass);
		if (adaptedClass == null) {
			adaptedClass = server.loadAdapter(adapterClass, new NullProgressMonitor());
		}
		try {
			Method getterMethod = adaptedClass.getClass().getMethod(getterName, new Class[]{});
			oldValue = getterMethod.invoke(adaptedClass, new Object[]{});

			Method setterMethod = adaptedClass.getClass().getMethod(setterName, new Class[]{valueType});
			setterMethod.invoke(adaptedClass, new Object[]{value});
		} catch (Exception e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/* (non-Javadoc)
	 * @see org.apache.geronimo.st.ui.commands.ServerCommand#undo()
	 */
	public void undo() {
		if(adaptedClass != null) {
			try {
				Method setterMethod = adaptedClass.getClass().getMethod(setterName, new Class[]{String.class});
				setterMethod.invoke(adaptedClass, new Object[]{oldValue});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

}
