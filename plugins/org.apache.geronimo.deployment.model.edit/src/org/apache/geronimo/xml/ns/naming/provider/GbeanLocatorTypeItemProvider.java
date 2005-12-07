/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.naming.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.naming.GbeanLocatorType;
import org.apache.geronimo.xml.ns.naming.NamingPackage;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.naming.GbeanLocatorType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class GbeanLocatorTypeItemProvider
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
  public GbeanLocatorTypeItemProvider(AdapterFactory adapterFactory)
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
      addGbeanLinkPropertyDescriptor(object);
      addTargetNamePropertyDescriptor(object);
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
         getString("_UI_GbeanLocatorType_domain_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_domain_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_Domain(),
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
         getString("_UI_GbeanLocatorType_server_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_server_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_Server(),
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
         getString("_UI_GbeanLocatorType_application_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_application_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_Application(),
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
         getString("_UI_GbeanLocatorType_module_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_module_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_Module(),
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
         getString("_UI_GbeanLocatorType_type_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_type_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_Type(),
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
         getString("_UI_GbeanLocatorType_name_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_name_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_Name(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Gbean Link feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGbeanLinkPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_GbeanLocatorType_gbeanLink_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_gbeanLink_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_GbeanLink(),
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
         getString("_UI_GbeanLocatorType_targetName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanLocatorType_targetName_feature", "_UI_GbeanLocatorType_type"),
         NamingPackage.eINSTANCE.getGbeanLocatorType_TargetName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This returns GbeanLocatorType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/GbeanLocatorType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((GbeanLocatorType)object).getName();
    return label == null || label.length() == 0 ?
      getString("_UI_GbeanLocatorType_type") :
      getString("_UI_GbeanLocatorType_type") + " " + label;
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

    switch (notification.getFeatureID(GbeanLocatorType.class))
    {
      case NamingPackage.GBEAN_LOCATOR_TYPE__DOMAIN:
      case NamingPackage.GBEAN_LOCATOR_TYPE__SERVER:
      case NamingPackage.GBEAN_LOCATOR_TYPE__APPLICATION:
      case NamingPackage.GBEAN_LOCATOR_TYPE__MODULE:
      case NamingPackage.GBEAN_LOCATOR_TYPE__TYPE:
      case NamingPackage.GBEAN_LOCATOR_TYPE__NAME:
      case NamingPackage.GBEAN_LOCATOR_TYPE__GBEAN_LINK:
      case NamingPackage.GBEAN_LOCATOR_TYPE__TARGET_NAME:
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
