package org.apache.geronimo.ui.pages;

import org.apache.geronimo.ui.internal.Messages;
import org.apache.geronimo.ui.sections.SecurityRootSection;
import org.apache.geronimo.ui.sections.SecuritySection;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

public class SecurityPage extends AbstractGeronimoFormPage {

	public EReference securityERef;

	public SecurityPage(FormEditor editor, String id, String title,
			EReference securityERef) {
		super(editor, id, title);
		this.securityERef = securityERef;
	}

	public SecurityPage(String id, String title) {
		super(id, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#fillBody(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void fillBody(IManagedForm managedForm) {
		managedForm.addPart(new SecurityRootSection(body, toolkit, getStyle(),
				getDeploymentPlan(), securityERef));
		managedForm.addPart(new SecuritySection(getDeploymentPlan(), body,
				toolkit, getStyle(), securityERef));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#getLayout()
	 */
	protected GridLayout getLayout() {
		GridLayout layout = super.getLayout();
		layout.numColumns = 1;
		return layout;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.geronimo.ui.pages.AbstractGeronimoFormPage#getFormTitle()
	 */
	public String getFormTitle() {
		return Messages.securityPageTitle;
	}

}
