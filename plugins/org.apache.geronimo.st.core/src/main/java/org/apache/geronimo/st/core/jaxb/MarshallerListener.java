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
package org.apache.geronimo.st.core.jaxb;

import javax.xml.bind.Marshaller;

import org.apache.geronimo.jee.security.Security;

public class MarshallerListener extends Marshaller.Listener{

	@Override
	public void beforeMarshal(Object source) {
		if (source instanceof Security) {
			Security security = (Security)source;
			if (security.getRoleMappings() != null && security.getRoleMappings().getRole().size() == 0) {
				security.setRoleMappings(null);
			}
		}
	}
}
