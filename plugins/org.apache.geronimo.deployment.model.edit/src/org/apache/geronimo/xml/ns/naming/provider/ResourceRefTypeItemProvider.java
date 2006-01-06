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
import org.apache.geronimo.xml.ns.naming.NamingPackage;
import org.apache.geronimo.xml.ns.naming.ResourceRefType;
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
 * {@link org.apache.geronimo.xml.ns.naming.ResourceRefType} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated NOT
 */
public class ResourceRefTypeItemProvider extends ItemProviderAdapter implements
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
	public ResourceRefTypeItemProvider(AdapterFactory adapterFactory) {
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
			addResourceLinkPropertyDescriptor(object);
			addTargetNamePropertyDescriptor(object);
			addUrlPropertyDescriptor(object);
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
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ResourceRefType_refName_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_refName_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_RefName(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Domain feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDomainPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ResourceRefType_domain_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_domain_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_Domain(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Server feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addServerPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ResourceRefType_server_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_server_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_Server(), true,
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
				getString("_UI_ResourceRefType_application_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_application_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_Application(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Module feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addModulePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ResourceRefType_module_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_module_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_Module(), true,
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
				getString("_UI_ResourceRefType_type_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_type_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_Type(), true,
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
				getString("_UI_ResourceRefType_name_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_name_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_Name(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Resource Link feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addResourceLinkPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ResourceRefType_resourceLink_feature"),
				getString("_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_resourceLink_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_ResourceLink(), true,
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
				getString("_UI_ResourceRefType_targetName_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_targetName_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_TargetName(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Url feature. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addUrlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				((ComposeableAdapterFactory) adapterFactory)
						.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ResourceRefType_url_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_ResourceRefType_url_feature",
						"_UI_ResourceRefType_type"), NamingPackage.eINSTANCE
						.getResourceRefType_Url(), true,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This returns ResourceRefType.gif. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getImage(Object object) {
		return getResourceLocator().getImage("full/obj16/ResourceRefType");
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getText(Object object) {
		String label = ((ResourceRefType) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_ResourceRefType_type")
				: getString("_UI_ResourceRefType_type") + " " + label;
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

		switch (notification.getFeatureID(ResourceRefType.class)) {
		case NamingPackage.RESOURCE_REF_TYPE__REF_NAME:
		case NamingPackage.RESOURCE_REF_TYPE__DOMAIN:
		case NamingPackage.RESOURCE_REF_TYPE__SERVER:
		case NamingPackage.RESOURCE_REF_TYPE__APPLICATION:
		case NamingPackage.RESOURCE_REF_TYPE__MODULE:
		case NamingPackage.RESOURCE_REF_TYPE__TYPE:
		case NamingPackage.RESOURCE_REF_TYPE__NAME:
		case NamingPackage.RESOURCE_REF_TYPE__RESOURCE_LINK:
		case NamingPackage.RESOURCE_REF_TYPE__TARGET_NAME:
		case NamingPackage.RESOURCE_REF_TYPE__URL:
			fireNotifyChanged(new ViewerNotification(notification, notification
					.getNotifier(), false, true));
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

	/**
	 * This does the same thing as ITableLabelProvider.getColumnText.
	 */
	public String getColumnText(Object object, int columnIndex) {
		ResourceRefType o = (ResourceRefType) object;
		switch(columnIndex) {
		case 0:
			return o.getRefName();
		case 1:
			if(o.eIsSet(NamingPackage.eINSTANCE.getResourceRefType_ResourceLink())) {
				return o.getResourceLink();
			}
			break;
		case 2:
			if(o.eIsSet(NamingPackage.eINSTANCE.getResourceRefType_TargetName())) {
				return o.getTargetName();
			}
			break;
		}
		return "";
	}

	/**
	 * This does the same thing as ITableLabelProvider.getColumnImage.
	 */
	public Object getColumnImage(Object object, int columnIndex) {
		if (columnIndex == 0) {
			return getResourceLocator().getImage("full/obj16/ResourceRefType");
		}
		return null;
	}

}
