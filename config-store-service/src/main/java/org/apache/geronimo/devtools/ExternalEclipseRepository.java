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
package org.apache.geronimo.devtools;

import java.io.File;
import java.net.URI;

import org.apache.geronimo.gbean.GBeanInfo;
import org.apache.geronimo.gbean.GBeanInfoBuilder;
import org.apache.geronimo.system.repository.Maven2Repository;
import org.apache.geronimo.system.serverinfo.ServerInfo;

public class ExternalEclipseRepository extends Maven2Repository {

	public ExternalEclipseRepository(File arg0) {
		super(arg0);
	}

	public ExternalEclipseRepository(URI arg0, ServerInfo arg1) {
		super(arg0, arg1);
	}
	
    public static final GBeanInfo GBEAN_INFO;

    static {
        GBeanInfoBuilder infoFactory = GBeanInfoBuilder.createStatic(ExternalEclipseRepository.class, "Repository");
        infoFactory.addAttribute("root", URI.class, true);
        infoFactory.addReference("ServerInfo", ServerInfo.class, "GBean");
        infoFactory.addInterface(ExternalEclipseRepository.class);
        infoFactory.setConstructor(new String[]{"root", "ServerInfo"});
        GBEAN_INFO = infoFactory.getBeanInfo();
    }

    public static GBeanInfo getGBeanInfo() {
        return GBEAN_INFO;
    }

}
