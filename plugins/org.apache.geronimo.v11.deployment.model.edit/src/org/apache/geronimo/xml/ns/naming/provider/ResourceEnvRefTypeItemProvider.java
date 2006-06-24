/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.naming.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.v11.deployment.model.edit.GeronimoEMFEditPlugin;

import org.apache.geronimo.xml.ns.naming.NamingFactory;
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
    IItemPropertySource,
    ITableItemLabelProvider
{
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
      addMessageDestinationLinkPropertyDescriptor(object);
      addAdminObjectModulePropertyDescriptor(object);
      addAdminObjectLinkPropertyDescriptor(object);
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
         NamingPackage.Literals.RESOURCE_ENV_REF_TYPE__REF_NAME,
         true,
         false,
         false,
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
         NamingPackage.Literals.RESOURCE_ENV_REF_TYPE__MESSAGE_DESTINATION_LINK,
         true,
         false,
         false,
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
         NamingPackage.Literals.RESOURCE_ENV_REF_TYPE__ADMIN_OBJECT_MODULE,
         true,
         false,
         false,
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
         NamingPackage.Literals.RESOURCE_ENV_REF_TYPE__ADMIN_OBJECT_LINK,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Collection getChildrenFeatures(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(NamingPackage.Literals.RESOURCE_ENV_REF_TYPE__PATTERN);
    }
    return childrenFeatures;
  }

  /**
   * This returns ResourceEnvRefType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/ResourceEnvRefType"));
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((ResourceEnvRefType)object).getRefName();
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
      case NamingPackage.RESOURCE_ENV_REF_TYPE__MESSAGE_DESTINATION_LINK:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__ADMIN_OBJECT_MODULE:
      case NamingPackage.RESOURCE_ENV_REF_TYPE__ADMIN_OBJECT_LINK:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case NamingPackage.RESOURCE_ENV_REF_TYPE__PATTERN:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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

    newChildDescriptors.add
      (createChildParameter
        (NamingPackage.Literals.RESOURCE_ENV_REF_TYPE__PATTERN,
         NamingFactory.eINSTANCE.createPatternType()));
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

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.emf.edit.provider.ITableItemLabelProvider#getColumnText(java.lang.Object,
   *      int)
   */
  public String getColumnText(Object object, int columnIndex) {
	  ResourceEnvRefType o = (ResourceEnvRefType) object;
	  switch (columnIndex) {
	  case 0:
		  if (o.eIsSet(NamingPackage.eINSTANCE.getResourceEnvRefType_RefName())) {
			  return o.getRefName();
		  }
		  break;
	  case 1:
		  if (o.eIsSet(NamingPackage.eINSTANCE.getResourceEnvRefType_MessageDestinationLink())) {
			  return o.getMessageDestinationLink();
		  }
		  break;
	  }
	  return "";
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.emf.edit.provider.ITableItemLabelProvider#getColumnImage(java.lang.Object,
   *      int)
   */
  public Object getColumnImage(Object object, int columnIndex) {
	  if (columnIndex == 0) {
		  return getImage(object);
	  }
	  return null;
  }

}
