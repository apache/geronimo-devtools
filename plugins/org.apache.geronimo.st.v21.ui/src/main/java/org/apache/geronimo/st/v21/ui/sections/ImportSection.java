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
package org.apache.geronimo.st.v21.ui.sections;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.v21.ui.wizards.ImportWizard;
import org.apache.geronimo.jee.deployment.Environment;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ImportSection extends DependencySection {

	public ImportSection(JAXBElement plan, Environment environment, Composite parent, FormToolkit toolkit, int style) {
		super(plan, environment, parent, toolkit, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
	 */
	public String getTitle() {
		return CommonMessages.editorSectionImportTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
	 */
	public String getDescription() {
		return CommonMessages.editorSectionImportDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getWizard()
	 */
	public Wizard getWizard() {
		return new ImportWizard(this);
	}

}
