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
package org.apache.geronimo.st.v11.ui.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.provider.DeploymentItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.j2ee.application.client.provider.ClientItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.j2ee.application.provider.ApplicationItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.j2ee.connector.provider.ConnectorItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.j2ee.web.provider.WebItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.naming.provider.NamingItemProviderAdapterFactory;
import org.apache.geronimo.xml.ns.security.provider.SecurityItemProviderAdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.openejb.xml.ns.openejb.jar.provider.JarItemProviderAdapterFactory;
import org.openejb.xml.ns.pkgen.provider.PkgenItemProviderAdapterFactory;

public class EMFEditorContext {
	
	private static ComposedAdapterFactory factory;

	static {
		List<AdapterFactoryImpl> factories = new ArrayList<AdapterFactoryImpl>();
		factories.add(new DeploymentItemProviderAdapterFactory());
		factories.add(new ClientItemProviderAdapterFactory());
		factories.add(new ApplicationItemProviderAdapterFactory());
		factories.add(new ConnectorItemProviderAdapterFactory());
		factories.add(new WebItemProviderAdapterFactory());
		factories.add(new NamingItemProviderAdapterFactory());
		factories.add(new SecurityItemProviderAdapterFactory());
		factories.add(new JarItemProviderAdapterFactory());
		factories.add(new PkgenItemProviderAdapterFactory());
		factory = new ComposedAdapterFactory(factories);
	}

	public static ComposedAdapterFactory getFactory() {
		return factory;
	}

}
