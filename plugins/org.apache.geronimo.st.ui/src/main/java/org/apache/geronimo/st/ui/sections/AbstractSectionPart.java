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
package org.apache.geronimo.st.ui.sections;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractSectionPart extends SectionPart {

	protected FormToolkit toolkit;

	private JAXBElement plan;

	/**
	 * @param section
	 */
	public AbstractSectionPart(Section section) {
		super(section);
	}

	/**
	 * @param parent
	 * @param toolkit
	 * @param style
	 * @param plan
	 */
	public AbstractSectionPart(Composite parent, FormToolkit toolkit,
			int style, JAXBElement plan) {
		super(parent, toolkit, style);
		this.toolkit = toolkit;
		this.plan = plan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 * 
	 * Overriding this method as a workaround as switching tabs on a dirty
	 * editor commits the page and marks the part as not dirty.
	 */
	public void commit(boolean onSave) {
		boolean currentDirtyState = isDirty();
		super.commit(onSave);
		if (!onSave && currentDirtyState) {
			markDirty();
		}
	}

	public JAXBElement getPlan() {
		return plan;
	}

	public FormToolkit getToolkit() {
		return toolkit;
	}

}
