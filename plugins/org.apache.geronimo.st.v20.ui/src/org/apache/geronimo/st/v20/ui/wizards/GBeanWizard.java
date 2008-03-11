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
package org.apache.geronimo.st.v20.ui.wizards;

import org.apache.geronimo.st.core.jaxb.JAXBObjectFactory;
import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.ui.wizards.AbstractTableWizard;
import org.apache.geronimo.st.v20.core.jaxb.JAXBObjectFactoryImpl;

public class GBeanWizard extends AbstractTableWizard {

	/**
	 * @param section
	 */
	public GBeanWizard(AbstractTableSection section) {
		super(section);
	}

	public JAXBObjectFactory getEFactory() {
		return JAXBObjectFactoryImpl.getInstance();
	}
	
	public String[] getTableColumnEAttributes() {
		return new String[] { "Name", "Clazz" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getAddWizardWindowTitle()
	 */
	public String getAddWizardWindowTitle() {
		return CommonMessages.wizardNewTitle_GBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getEditWizardWindowTitle()
	 */
	public String getEditWizardWindowTitle() {
		return CommonMessages.wizardEditTitle_GBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getWizardFirstPageTitle()
	 */
	public String getWizardFirstPageTitle() {
		return CommonMessages.wizardEditTitle_GBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.wizards.DynamicAddEditWizard#getWizardFirstPageDescription()
	 */
	public String getWizardFirstPageDescription() {
		return CommonMessages.wizardPageTitle_GBean;
	}

}
