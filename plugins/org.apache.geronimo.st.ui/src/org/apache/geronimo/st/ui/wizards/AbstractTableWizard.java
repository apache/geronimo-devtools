package org.apache.geronimo.st.ui.wizards;

import org.apache.geronimo.st.ui.Activator;
import org.apache.geronimo.st.ui.sections.AbstractTableSection;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public abstract class AbstractTableWizard extends Wizard implements TableWizard {

	protected AbstractTableSection section;

	protected EObject eObject;

	protected ImageDescriptor descriptor = Activator.imageDescriptorFromPlugin("org.apache.geronimo.ui", "icons/bigG.gif");

	/**
	 * 
	 */
	public AbstractTableWizard(AbstractTableSection section) {
		super();
		this.section = section;
		setWindowTitle(getAddWizardWindowTitle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	public boolean performFinish() {

		if (eObject == null) {
			eObject = getEFactory().create(section.getTableEntryObjectType());
			EObject plan = section.getPlan();
			((EList) plan.eGet(section.getEReference())).add(eObject);
		}

		processEAttributes(getPages()[0]);

		return true;
	}

	public void processEAttributes(IWizardPage page) {
		if (page instanceof DynamicWizardPage) {
			for (int i = 0; i < getTableColumnEAttributes().length; i++) {
				String value = ((DynamicWizardPage) page).textEntries[i].getText();
				EAttribute attribute = getTableColumnEAttributes()[i];
				if (attribute.getEContainingClass().equals(eObject.eClass())) {
					if (value != null && value.trim().length() != 0)
						eObject.eSet(attribute, value);
				} else {
					// TODO
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages() {
		WizardPage page = new DynamicWizardPage("Page0");
		page.setImageDescriptor(descriptor);
		addPage(page);
	}

	/**
	 * @param section
	 */
	public void setSection(AbstractTableSection section) {
		this.section = section;
	}

	/**
	 * @param object
	 */
	public void setEObject(EObject object) {
		eObject = object;
	}

	public class DynamicWizardPage extends WizardPage {

		Text[] textEntries = new Text[getTableColumnEAttributes().length];

		public DynamicWizardPage(String pageName) {
			super(pageName);
			setTitle(getWizardFirstPageTitle());
			setDescription(getWizardFirstPageDescription());
		}

		public DynamicWizardPage(String pageName, String title,
				ImageDescriptor titleImage) {
			super(pageName, title, titleImage);
		}

		public void createControl(Composite parent) {
			Composite composite = createComposite(parent);
			for (int i = 0; i < section.getTableColumnNames().length; i++) {
				Label label = new Label(composite, SWT.LEFT);
				String columnName = section.getTableColumnNames()[i];
				if (!columnName.endsWith(":"))
					columnName = columnName.concat(":");
				label.setText(columnName);
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				label.setLayoutData(data);

				Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
				data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_FILL);
				data.grabExcessHorizontalSpace = true;
				data.widthHint = 100;
				text.setLayoutData(data);

				if (eObject != null) {
					String value = (String) eObject.eGet(getTableColumnEAttributes()[i]);
					if (value != null) {
						text.setText(value);
					}
				}
				textEntries[i] = text;
			}

			doCustom(composite);
			setControl(composite);
			textEntries[0].setFocus();
		}

		public Composite createComposite(Composite parent) {
			Composite composite = new Composite(parent, SWT.NULL);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			composite.setLayout(layout);
			GridData data = new GridData();
			data.verticalAlignment = GridData.FILL;
			data.horizontalAlignment = GridData.FILL;
			data.widthHint = 300;
			composite.setLayoutData(data);
			return composite;
		}

		public void doCustom(Composite parent) {

		}

	}

}
