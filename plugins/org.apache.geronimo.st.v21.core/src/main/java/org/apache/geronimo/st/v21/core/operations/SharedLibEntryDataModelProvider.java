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
package org.apache.geronimo.st.v21.core.operations;

import java.util.Set;

import org.apache.geronimo.st.core.operations.ISharedLibEntryCreationDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

/**
 * @version $Rev: 471551 $ $Date: 2006-11-06 06:47:11 +0800 (Mon, 06 Nov 2006) $
 */
public class SharedLibEntryDataModelProvider extends AbstractDataModelProvider implements ISharedLibEntryCreationDataModelProperties {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider#getPropertyNames()
	 */
	public Set getPropertyNames() {
		Set names = super.getPropertyNames();
		names.add(MODULES);
		names.add(SERVER);
		return names;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelProvider#getDefaultOperation()
	 */
	public IDataModelOperation getDefaultOperation() {
		return new SharedLibEntryCreationOperation(model);
	}
}
