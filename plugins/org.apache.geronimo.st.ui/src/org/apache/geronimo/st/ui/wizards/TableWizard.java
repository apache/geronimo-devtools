package org.apache.geronimo.st.ui.wizards;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EFactory;

public interface TableWizard {

	public EFactory getEFactory();

	public EAttribute[] getTableColumnEAttributes();

	public String getAddWizardWindowTitle();

	public String getEditWizardWindowTitle();

	public String getWizardFirstPageTitle();

	public String getWizardFirstPageDescription();

}
