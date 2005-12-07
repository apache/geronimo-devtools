/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.naming.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.naming.NamingPackage;
import org.apache.geronimo.xml.ns.naming.ResourceLocatorType;

import org.apache.geronimo.xml.ns.web.provider.GeronimowebEditPlugin;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.naming.ResourceLocatorType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ResourceLocatorTypeItemProvider
  extends ItemProviderAdapter
  implements	
    IEditingDomainItemProvider,	
    IStructuredItemContentProvider,	
    ITreeItemContentProvider,	
    IItemLabelProvider,	
    IItemPropertySource		
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
  public ResourceLocatorTypeItemProvider(AdapterFactory adapterFactory)
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
         getString("_UI_ResourceLocatorType_domain_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_domain_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_Domain(),
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
         getString("_UI_ResourceLocatorType_server_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_server_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_Server(),
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
         getString("_UI_ResourceLocatorType_application_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_application_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_Application(),
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
         getString("_UI_ResourceLocatorType_module_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_module_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_Module(),
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
         getString("_UI_ResourceLocatorType_type_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_type_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_Type(),
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
         getString("_UI_ResourceLocatorType_name_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_name_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_Name(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Resource Link feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addResourceLinkPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceLocatorType_resourceLink_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_resourceLink_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_ResourceLink(),
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
         getString("_UI_ResourceLocatorType_targetName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_targetName_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_TargetName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Url feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addUrlPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ResourceLocatorType_url_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ResourceLocatorType_url_feature", "_UI_ResourceLocatorType_type"),
         NamingPackage.eINSTANCE.getResourceLocatorType_Url(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This returns ResourceLocatorType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/ResourceLocatorType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((ResourceLocatorType)object).getName();
    return label == null || label.length() == 0 ?
      getString("_UI_ResourceLocatorType_type") :
      getString("_UI_ResourceLocatorType_type") + " " + label;
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

    switch (notification.getFeatureID(ResourceLocatorType.class))
    {
      case NamingPackage.RESOURCE_LOCATOR_TYPE__DOMAIN:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__SERVER:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__APPLICATION:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__MODULE:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__TYPE:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__NAME:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__RESOURCE_LINK:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__TARGET_NAME:
      case NamingPackage.RESOURCE_LOCATOR_TYPE__URL:
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
    return GeronimowebEditPlugin.INSTANCE;
  }

}
