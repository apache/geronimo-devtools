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
package org.apache.geronimo.xml.ns.naming.provider;

import java.util.Collection;
import java.util.List;

import org.apache.geronimo.deployment.model.edit.GeronimoEMFEditPlugin;
import org.apache.geronimo.xml.ns.naming.EjbRefType;
import org.apache.geronimo.xml.ns.naming.NamingFactory;
import org.apache.geronimo.xml.ns.naming.NamingPackage;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a
 * {@link org.apache.geronimo.xml.ns.naming.EjbRefType} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated NOT
 */
public class EjbRefTypeItemProvider extends ItemProviderAdapter implements
		IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource,
		ITableItemLabelProvider {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String copyright = "";

	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EjbRefTypeItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addRefNamePropertyDescriptor(object);
			addDomainPropertyDescriptor(object);
			addServerPropertyDescriptor(object);
			addApplicationPropertyDescriptor(object);
			addModulePropertyDescriptor(object);
			addTypePropertyDescriptor(object);
			addNamePropertyDescriptor(object);
			addNsCorbalocPropertyDescriptor(object);
			addName1PropertyDescriptor(object);
			addCssLinkPropertyDescriptor(object);
			addCssNamePropertyDescriptor(object);
			addEjbLinkPropertyDescriptor(object);
			addTargetNamePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Ref Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addRefNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_EjbRefType_refName_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_EjbRefType_refName_feature",
								"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
								.getEjbRefType_RefName(), true,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Domain feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDomainPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_EjbRefType_domain_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_EjbRefType_domain_feature",
								"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
								.getEjbRefType_Domain(), true,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Server feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addServerPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_EjbRefType_server_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_EjbRefType_server_feature",
								"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
								.getEjbRefType_Server(), true,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Application feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addApplicationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_EjbRefType_application_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EjbRefType_application_feature",
						"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
						.getEjbRefType_Application(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Module feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addModulePropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_EjbRefType_module_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_EjbRefType_module_feature",
								"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
								.getEjbRefType_Module(), true,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Type feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_EjbRefType_type_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EjbRefType_type_feature", "_UI_EjbRefType_type"),
				NamingPackage.eINSTANCE.getEjbRefType_Type(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Name feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_EjbRefType_name_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EjbRefType_name_feature", "_UI_EjbRefType_type"),
				NamingPackage.eINSTANCE.getEjbRefType_Name(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Ns Corbaloc feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addNsCorbalocPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_EjbRefType_nsCorbaloc_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EjbRefType_nsCorbaloc_feature",
						"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
						.getEjbRefType_NsCorbaloc(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Name1 feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addName1PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_EjbRefType_name1_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EjbRefType_name1_feature", "_UI_EjbRefType_type"),
				NamingPackage.eINSTANCE.getEjbRefType_Name1(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Css Link feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addCssLinkPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_EjbRefType_cssLink_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_EjbRefType_cssLink_feature",
								"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
								.getEjbRefType_CssLink(), true,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Css Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addCssNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_EjbRefType_cssName_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_EjbRefType_cssName_feature",
								"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
								.getEjbRefType_CssName(), true,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Ejb Link feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addEjbLinkPropertyDescriptor(Object object) {
		itemPropertyDescriptors
				.add(createItemPropertyDescriptor(
						((ComposeableAdapterFactory) adapterFactory)
								.getRootAdapterFactory(), getResourceLocator(),
						getString("_UI_EjbRefType_ejbLink_feature"), getString(
								"_UI_PropertyDescriptor_description",
								"_UI_EjbRefType_ejbLink_feature",
								"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
								.getEjbRefType_EjbLink(), true,
						ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Target Name feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addTargetNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_EjbRefType_targetName_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_EjbRefType_targetName_feature",
						"_UI_EjbRefType_type"), NamingPackage.eINSTANCE
						.getEjbRefType_TargetName(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to
	 * deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand},
	 * {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in
	 * {@link #createCommand}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(NamingPackage.eINSTANCE.getEjbRefType_Css());
		}
		return childrenFeatures;
	}

	/**
	 * This returns EjbRefType.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	public Object getImage(Object object) {
		return getResourceLocator().getImage("full/obj16/EjbRefType");
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText(Object object) {
		String label = ((EjbRefType) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_EjbRefType_type")
				: getString("_UI_EjbRefType_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(EjbRefType.class)) {
		case NamingPackage.EJB_REF_TYPE__REF_NAME:
		case NamingPackage.EJB_REF_TYPE__DOMAIN:
		case NamingPackage.EJB_REF_TYPE__SERVER:
		case NamingPackage.EJB_REF_TYPE__APPLICATION:
		case NamingPackage.EJB_REF_TYPE__MODULE:
		case NamingPackage.EJB_REF_TYPE__TYPE:
		case NamingPackage.EJB_REF_TYPE__NAME:
		case NamingPackage.EJB_REF_TYPE__NS_CORBALOC:
		case NamingPackage.EJB_REF_TYPE__NAME1:
		case NamingPackage.EJB_REF_TYPE__CSS_LINK:
		case NamingPackage.EJB_REF_TYPE__CSS_NAME:
		case NamingPackage.EJB_REF_TYPE__EJB_LINK:
		case NamingPackage.EJB_REF_TYPE__TARGET_NAME:
			fireNotifyChanged(new ViewerNotification(notification, notification
					.getNotifier(), false, true));
			return;
		case NamingPackage.EJB_REF_TYPE__CSS:
			fireNotifyChanged(new ViewerNotification(notification, notification
					.getNotifier(), true, false));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of
	 * {@link org.eclipse.emf.edit.command.CommandParameter}s describing all of
	 * the children that can be created under this object. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void collectNewChildDescriptors(Collection newChildDescriptors,
			Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add(createChildParameter(NamingPackage.eINSTANCE
				.getEjbRefType_Css(), NamingFactory.eINSTANCE.createCssType()));
	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return GeronimoEMFEditPlugin.INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.edit.provider.ITableItemLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
	public String getColumnText(Object object, int columnIndex) {
		EjbRefType o = (EjbRefType) object;
		switch (columnIndex) {
		case 0:
			if (o.eIsSet(NamingPackage.eINSTANCE.getEjbRefType_TargetName())) {
				return o.getTargetName();
			}
			break;
		case 1:
			if (o.eIsSet(NamingPackage.eINSTANCE.getEjbRefType_EjbLink())) {
				return o.getEjbLink();
			}
			break;
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.edit.provider.ITableItemLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Object getColumnImage(Object object, int columnIndex) {
		if (columnIndex == 0) {
			return getImage(object);
		}
		return null;
	}

}
