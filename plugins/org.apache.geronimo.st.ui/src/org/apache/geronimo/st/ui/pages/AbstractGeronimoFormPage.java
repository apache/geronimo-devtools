package org.apache.geronimo.st.ui.pages;

import org.apache.geronimo.st.ui.editors.AbstractGeronimoDeploymentPlanEditor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public abstract class AbstractGeronimoFormPage extends FormPage {

	EObject deploymentPlan;

	protected FormToolkit toolkit;

	protected Composite body;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public AbstractGeronimoFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	/**
	 * @param id
	 * @param title
	 */
	public AbstractGeronimoFormPage(String id, String title) {
		super(id, title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	protected void createFormContent(IManagedForm managedForm) {
		deploymentPlan = ((AbstractGeronimoDeploymentPlanEditor) getEditor()).getDeploymentPlan();
		body = managedForm.getForm().getBody();
		toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText(getFormTitle());
		// managedForm.addPart(new BannerPart(form.getBody(), toolkit,
		// SWT.NONE));
		form.getBody().setLayout(getLayout());
		fillBody(managedForm);
		form.reflow(true);
	}

	protected GridLayout getLayout() {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 20;
		layout.makeColumnsEqualWidth = true;
		return layout;
	}

	protected int getStyle() {
		return ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED
				| ExpandableComposite.TITLE_BAR | Section.DESCRIPTION
				| ExpandableComposite.FOCUS_TITLE;
	}

	abstract protected void fillBody(IManagedForm managedForm);

	public EObject getDeploymentPlan() {
		return deploymentPlan;
	}

	public String getFormTitle() {
		return getTitle();
	}

}
