package org.apache.geronimo.st.ui.sections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public abstract class AbstractSectionPart extends SectionPart {

	protected FormToolkit toolkit;

	private EObject plan;

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
			int style, EObject plan) {
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

	public EObject getPlan() {
		return plan;
	}

	public FormToolkit getToolkit() {
		return toolkit;
	}

}
