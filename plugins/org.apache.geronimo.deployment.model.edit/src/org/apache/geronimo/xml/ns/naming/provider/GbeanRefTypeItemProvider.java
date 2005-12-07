/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.naming.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.naming.GbeanRefType;
import org.apache.geronimo.xml.ns.naming.NamingPackage;

import org.apache.geronimo.xml.ns.web.provider.GeronimowebEditPlugin;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.util.FeatureMapUtil;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.naming.GbeanRefType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class GbeanRefTypeItemProvider
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
  public GbeanRefTypeItemProvider(AdapterFactory adapterFactory)
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
      addRefTypePropertyDescriptor(object);
      addProxyTypePropertyDescriptor(object);
      addDomainPropertyDescriptor(object);
      addServerPropertyDescriptor(object);
      addApplicationPropertyDescriptor(object);
      addModulePropertyDescriptor(object);
      addTypePropertyDescriptor(object);
      addNamePropertyDescriptor(object);
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
         getString("_UI_GbeanRefType_refName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_refName_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_RefName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Ref Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRefTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_GbeanRefType_refType_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_refType_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_RefType(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Proxy Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addProxyTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_GbeanRefType_proxyType_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_proxyType_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_ProxyType(),
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
         getString("_UI_GbeanRefType_domain_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_domain_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_Domain(),
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
         getString("_UI_GbeanRefType_server_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_server_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_Server(),
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
         getString("_UI_GbeanRefType_application_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_application_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_Application(),
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
         getString("_UI_GbeanRefType_module_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_module_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_Module(),
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
         getString("_UI_GbeanRefType_type_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_type_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_Type(),
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
         getString("_UI_GbeanRefType_name_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_name_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_Name(),
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
         getString("_UI_GbeanRefType_targetName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanRefType_targetName_feature", "_UI_GbeanRefType_type"),
         NamingPackage.eINSTANCE.getGbeanRefType_TargetName(),
         true,
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
      childrenFeatures.add(NamingPackage.eINSTANCE.getGbeanRefType_Group());
    }
    return childrenFeatures;
  }

  /**
   * This returns GbeanRefType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/GbeanRefType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((GbeanRefType)object).getRefName();
    return label == null || label.length() == 0 ?
      getString("_UI_GbeanRefType_type") :
      getString("_UI_GbeanRefType_type") + " " + label;
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

    switch (notification.getFeatureID(GbeanRefType.class))
    {
      case NamingPackage.GBEAN_REF_TYPE__REF_NAME:
      case NamingPackage.GBEAN_REF_TYPE__REF_TYPE:
      case NamingPackage.GBEAN_REF_TYPE__PROXY_TYPE:
      case NamingPackage.GBEAN_REF_TYPE__DOMAIN:
      case NamingPackage.GBEAN_REF_TYPE__SERVER:
      case NamingPackage.GBEAN_REF_TYPE__APPLICATION:
      case NamingPackage.GBEAN_REF_TYPE__MODULE:
      case NamingPackage.GBEAN_REF_TYPE__TYPE:
      case NamingPackage.GBEAN_REF_TYPE__NAME:
      case NamingPackage.GBEAN_REF_TYPE__TARGET_NAME:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case NamingPackage.GBEAN_REF_TYPE__GROUP:
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
        (NamingPackage.eINSTANCE.getGbeanRefType_Group(),
         FeatureMapUtil.createEntry
          (NamingPackage.eINSTANCE.getGbeanRefType_Domain(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (NamingPackage.eINSTANCE.getGbeanRefType_Group(),
         FeatureMapUtil.createEntry
          (NamingPackage.eINSTANCE.getGbeanRefType_Server(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (NamingPackage.eINSTANCE.getGbeanRefType_Group(),
         FeatureMapUtil.createEntry
          (NamingPackage.eINSTANCE.getGbeanRefType_Application(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (NamingPackage.eINSTANCE.getGbeanRefType_Group(),
         FeatureMapUtil.createEntry
          (NamingPackage.eINSTANCE.getGbeanRefType_Module(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (NamingPackage.eINSTANCE.getGbeanRefType_Group(),
         FeatureMapUtil.createEntry
          (NamingPackage.eINSTANCE.getGbeanRefType_Type(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (NamingPackage.eINSTANCE.getGbeanRefType_Group(),
         FeatureMapUtil.createEntry
          (NamingPackage.eINSTANCE.getGbeanRefType_Name(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (NamingPackage.eINSTANCE.getGbeanRefType_Group(),
         FeatureMapUtil.createEntry
          (NamingPackage.eINSTANCE.getGbeanRefType_TargetName(),
           "")));
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
