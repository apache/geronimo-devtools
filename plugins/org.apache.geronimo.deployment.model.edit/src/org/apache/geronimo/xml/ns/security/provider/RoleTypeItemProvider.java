/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.security.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.security.RoleType;
import org.apache.geronimo.xml.ns.security.SecurityFactory;
import org.apache.geronimo.xml.ns.security.SecurityPackage;

import org.apache.geronimo.xml.ns.web.provider.GeronimowebEditPlugin;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.security.RoleType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RoleTypeItemProvider
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
  public RoleTypeItemProvider(AdapterFactory adapterFactory)
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

      addRoleNamePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Role Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRoleNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_RoleType_roleName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_RoleType_roleName_feature", "_UI_RoleType_type"),
         SecurityPackage.eINSTANCE.getRoleType_RoleName(),
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
      childrenFeatures.add(SecurityPackage.eINSTANCE.getRoleType_Description());
      childrenFeatures.add(SecurityPackage.eINSTANCE.getRoleType_RealmPrincipal());
      childrenFeatures.add(SecurityPackage.eINSTANCE.getRoleType_LoginDomainPrincipal());
      childrenFeatures.add(SecurityPackage.eINSTANCE.getRoleType_Principal());
      childrenFeatures.add(SecurityPackage.eINSTANCE.getRoleType_DistinguishedName());
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EStructuralFeature getChildFeature(Object object, Object child)
  {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    return super.getChildFeature(object, child);
  }

  /**
   * This returns RoleType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/RoleType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((RoleType)object).getRoleName();
    return label == null || label.length() == 0 ?
      getString("_UI_RoleType_type") :
      getString("_UI_RoleType_type") + " " + label;
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

    switch (notification.getFeatureID(RoleType.class))
    {
      case SecurityPackage.ROLE_TYPE__ROLE_NAME:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case SecurityPackage.ROLE_TYPE__DESCRIPTION:
      case SecurityPackage.ROLE_TYPE__REALM_PRINCIPAL:
      case SecurityPackage.ROLE_TYPE__LOGIN_DOMAIN_PRINCIPAL:
      case SecurityPackage.ROLE_TYPE__PRINCIPAL:
      case SecurityPackage.ROLE_TYPE__DISTINGUISHED_NAME:
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
        (SecurityPackage.eINSTANCE.getRoleType_Description(),
         SecurityFactory.eINSTANCE.createDescriptionType()));

    newChildDescriptors.add
      (createChildParameter
        (SecurityPackage.eINSTANCE.getRoleType_RealmPrincipal(),
         SecurityFactory.eINSTANCE.createRealmPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (SecurityPackage.eINSTANCE.getRoleType_LoginDomainPrincipal(),
         SecurityFactory.eINSTANCE.createLoginDomainPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (SecurityPackage.eINSTANCE.getRoleType_LoginDomainPrincipal(),
         SecurityFactory.eINSTANCE.createRealmPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (SecurityPackage.eINSTANCE.getRoleType_Principal(),
         SecurityFactory.eINSTANCE.createPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (SecurityPackage.eINSTANCE.getRoleType_Principal(),
         SecurityFactory.eINSTANCE.createLoginDomainPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (SecurityPackage.eINSTANCE.getRoleType_Principal(),
         SecurityFactory.eINSTANCE.createRealmPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (SecurityPackage.eINSTANCE.getRoleType_DistinguishedName(),
         SecurityFactory.eINSTANCE.createDistinguishedNameType()));
  }

  /**
   * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getCreateChildText(Object owner, Object feature, Object child, Collection selection)
  {
    Object childFeature = feature;
    Object childObject = child;

    boolean qualify =
      childFeature == SecurityPackage.eINSTANCE.getRoleType_RealmPrincipal() ||
      childFeature == SecurityPackage.eINSTANCE.getRoleType_LoginDomainPrincipal() ||
      childFeature == SecurityPackage.eINSTANCE.getRoleType_Principal();

    if (qualify)
    {
      return getString
        ("_UI_CreateChild_text2",
         new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
    }
    return super.getCreateChildText(owner, feature, child, selection);
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
