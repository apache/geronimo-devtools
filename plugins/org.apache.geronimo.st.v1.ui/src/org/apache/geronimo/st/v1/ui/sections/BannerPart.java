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
package org.apache.geronimo.st.v1.ui.sections;

import org.apache.geronimo.st.v1.ui.Activator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class BannerPart extends SectionPart {

	FormToolkit toolkit;

	ImageDescriptor defaultImgDesc = Activator.imageDescriptorFromPlugin("org.apache.geronimo.st.ui", "icons/Geronimo_Welcome4.gif");

	public BannerPart(Composite parent, FormToolkit toolkit, int style) {
		super(parent, toolkit, style);
		this.toolkit = toolkit;
		create();
	}

	private void create() {
		Section section = getSection();
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
		section.setText("");

		Composite composite = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		layout.verticalSpacing = 5;
		layout.horizontalSpacing = 10;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		section.setClient(composite);

		Label label = toolkit.createLabel(composite, "");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		label.setImage(defaultImgDesc.createImage());
	}

}
