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
import org.apache.geronimo.xml.ns.naming.ResourceEnvRefType;
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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.naming.ResourceEnvRefType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated NOT
 */
public class ResourceEnvRefTypeItemProvider
  extends ItemProviderAdapter
  implements	
    IEditingDomainItemProvider,	
    IStructuredItemContentProvider,	
    ITreeItemContentProvider,	
    IItemLabelProvider,	
    IItemPropertySource	,
    ITableItemLabelProvider
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String copyright = "";

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResourceEnvRefTypeItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      super.getPropertyDescriptors(object);

      addRefNamePropertyDescriptor(object);
      addDomainPropertyDescriptor(object);
      addServerPropertyDescriptor(object);
      addApplicationPropertyDescriptor(object);
      addModulePropertyDescriptor(object);
      addTypePropertyDescriptor(object);
      addNamePropertyDescriptor(object);
      addMessageDestinationLinkPropertyDescriptor(object);
      addAdminObjectModulePropertyDescriptor(object);
      addAdminObjectLinkPropertyDescriptor(object);
      addTargetNamePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Ref Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRefNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_refName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_refName_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_RefName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Domain feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addDomainPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_domain_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_domain_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_Domain(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Server feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addServerPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_server_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_server_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_Server(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Application feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addApplicationPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_application_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_application_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_Application(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Module feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addModulePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_module_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_module_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_Module(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_type_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_type_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_Type(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_name_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_name_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_Name(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Message Destination Link feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMessageDestinationLinkPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_messageDestinationLink_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_messageDestinationLink_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_MessageDestinationLink(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Admin Object Module feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addAdminObjectModulePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_adminObjectModule_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_adminObjectModule_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_AdminObjectModule(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Admin Object Link feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addAdminObjectLinkPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_adminObjectLink_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_adminObjectLink_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_AdminObjectLink(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Target Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTargetNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceEnvRefType_targetName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceEnvRefType_targetName_feature", "_UI_ResourceEnvRefType_type"),
         NamingPackage.eINSTANCE.getResourceEnvRefType_TargetName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This returns ResourceEnvRefType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/ResourceEnvRefType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((ResourceEnvRefType)object).getName();
    return label == null || label.length() == 0 ?
      getString("_UI_ResourceEnvRefType_type") :
      getString("_UI_ResourceEnvRefType_type") + " " + label;
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(ResourceEnvRefType.class))
    {
      case NamingPackage.RESOURCE_ENV_REF_TYPE__REF_NAME:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__DOMAIN:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__SERVER:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__APPLICATION:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__MODULE:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__TYPE:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__NAME:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__MESSAGE_DESTINATION_LINK:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__ADMIN_OBJECT_MODULE:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__ADMIN_OBJECT_LINK:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__TARGET_NAME:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s
   * describing all of the children that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResourceLocator getResourceLocator()
  {
    return GeronimoEMFEditPlugin.INSTANCE;
  }
  
  /**
	 * This does the same thing as ITableLabelProvider.getColumnText.
	 */
	public String getColumnText(Object object, int columnIndex) {
		ResourceEnvRefType o = (ResourceEnvRefType) object;
		switch(columnIndex) {
		case 0:
			if(o.eIsSet(NamingPackage.eINSTANCE.getResourceEnvRefType_RefName())) {
				return o.getRefName();
			}
			break;
		case 1:
			if(o.eIsSet(NamingPackage.eINSTANCE.getResourceEnvRefType_MessageDestinationLink())) {
				return o.getMessageDestinationLink();
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
			return getResourceLocator().getImage("full/obj16/ResourceEnvRefType");
		}
		return null;
	}

}
