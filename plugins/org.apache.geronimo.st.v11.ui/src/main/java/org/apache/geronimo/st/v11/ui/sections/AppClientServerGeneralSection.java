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
package org.apache.geronimo.st.v11.ui.sections;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.j2ee.applicationclient.ApplicationClientType;
import org.apache.geronimo.j2ee.deployment.EnvironmentType;
import org.apache.geronimo.st.ui.CommonMessages;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @version $Rev$ $Date$
 */
public class AppClientServerGeneralSection extends CommonGeneralSection {

	ApplicationClientType plan;

	public AppClientServerGeneralSection(Composite parent, FormToolkit toolkit, int style, JAXBElement plan) {
		super(parent, toolkit, style, plan);
		this.plan = (ApplicationClientType) plan.getValue();
		createClient();
	}

    protected EnvironmentType getEnvironment(boolean create) {
    	EnvironmentType type = null;
        Object plan = getPlan().getValue();
        if (ApplicationClientType.class.isInstance(plan)) {
            type = ((ApplicationClientType) plan).getServerEnvironment();
            if (type == null && create) {
                type = getDeploymentObjectFactory().createEnvironmentType();
                ((ApplicationClientType) plan).setServerEnvironment(type);
            }
        }

        return type;
    }

    protected String getSectionGeneralTitle() {
        return CommonMessages.editorSectionServerTitle;
    }

    protected String getSectionGeneralDescription() {
        return CommonMessages.editorSectionServerDescription;
    }
}
