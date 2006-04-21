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
package org.apache.geronimo.st.v11.core;

import org.apache.geronimo.st.core.IGeronimoVersionHandler;
import org.eclipse.wst.server.core.IModule;

public class GeronimoV11VersionHandler implements IGeronimoVersionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.st.core.IGeronimoVersionHandler#getConfigID(org.eclipse.wst.server.core.IModule)
	 */
	public String getConfigID(IModule module) {
		return GeronimoV11Utils.getConfigId(module);
	}
}
