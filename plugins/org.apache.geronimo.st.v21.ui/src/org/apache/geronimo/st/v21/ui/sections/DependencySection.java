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

import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.st.ui.CommonMessages;
import org.apache.geronimo.st.ui.providers.AdapterFactory;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.apache.geronimo.st.v21.ui.Activator;
import org.apache.geronimo.st.v21.ui.wizards.DependencyWizard;
import org.apache.geronimo.xml.ns.deployment_1.DependenciesType;
import org.apache.geronimo.xml.ns.deployment_1.DependencyType;
import org.apache.geronimo.xml.ns.deployment_1.EnvironmentType;
import org.apache.geronimo.xml.ns.j2ee.web_2_0.WebAppType;
import org.apache.geronimo.xml.ns.naming_1.ResourceRefType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DependencySection extends AbstractTableSection {

	private EnvironmentType environment;

	/**
	 * @param plan
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
	public DependencySection(JAXBElement plan, EnvironmentType environment, Composite parent, FormToolkit toolkit, int style) {
		super(plan, parent, toolkit, style);
		this.environment = environment;
		createClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTitle()
	 */
	public String getTitle() {
		return CommonMessages.editorSectionDependenciesTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getDescription()
	 */
	public String getDescription() {
		return CommonMessages.editorSectionDependenciesDescription;
	}

	public List getObjectContainer() {
		return environment.getDependencies().getDependency();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getWizard()
	 */
	public Wizard getWizard() {
		return new DependencyWizard(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#isHeaderVisible()
	 */
	public boolean isHeaderVisible() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return Activator.imageDescriptorFromPlugin("org.eclipse.jdt.ui", "icons/full/obj16/jar_obj.gif");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.sections.AbstractTableSection#getTableEntryObjectType()
	 */
	public Class getTableEntryObjectType() {
		return DependencyType.class;
	}

	
	public Object getInput() {
		if (environment != null) {
			return environment.getDependencies();
		}
		return super.getInput();
	}
	
	public AdapterFactory getAdapterFactory() {
		return new AdapterFactory() {
			public Object[] getElements(Object inputElement) {
				if (!DependenciesType.class.isInstance(inputElement)) {
					return new String[] { "" };
				}
				DependenciesType plan = (DependenciesType)inputElement;
				return plan.getDependency().toArray();
			}
			public String getColumnText(Object element, int columnIndex) {
				if (DependencyType.class.isInstance(element)) {
					DependencyType dependency = (DependencyType)element;
					switch (columnIndex) {
					case 0: return dependency.getGroupId();
					case 1: return dependency.getArtifactId();
					}
				}
				return null;
			}
		};
	}
}
