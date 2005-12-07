/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.j2ee.application.client.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;

import org.apache.geronimo.xml.ns.j2ee.application.client.ApplicationClientType;
import org.apache.geronimo.xml.ns.j2ee.application.client.ClientFactory;
import org.apache.geronimo.xml.ns.j2ee.application.client.ClientPackage;

import org.apache.geronimo.xml.ns.naming.NamingFactory;

import org.apache.geronimo.xml.ns.security.SecurityFactory;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.j2ee.application.client.ApplicationClientType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ApplicationClientTypeItemProvider
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
  public ApplicationClientTypeItemProvider(AdapterFactory adapterFactory)
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

      addRealmNamePropertyDescriptor(object);
      addCallbackHandlerPropertyDescriptor(object);
      addClientConfigIdPropertyDescriptor(object);
      addClientParentIdPropertyDescriptor(object);
      addConfigIdPropertyDescriptor(object);
      addParentIdPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Realm Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRealmNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ApplicationClientType_realmName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ApplicationClientType_realmName_feature", "_UI_ApplicationClientType_type"),
         ClientPackage.eINSTANCE.getApplicationClientType_RealmName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Callback Handler feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addCallbackHandlerPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ApplicationClientType_callbackHandler_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ApplicationClientType_callbackHandler_feature", "_UI_ApplicationClientType_type"),
         ClientPackage.eINSTANCE.getApplicationClientType_CallbackHandler(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Client Config Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addClientConfigIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ApplicationClientType_clientConfigId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ApplicationClientType_clientConfigId_feature", "_UI_ApplicationClientType_type"),
         ClientPackage.eINSTANCE.getApplicationClientType_ClientConfigId(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Client Parent Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addClientParentIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ApplicationClientType_clientParentId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ApplicationClientType_clientParentId_feature", "_UI_ApplicationClientType_type"),
         ClientPackage.eINSTANCE.getApplicationClientType_ClientParentId(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Config Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addConfigIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ApplicationClientType_configId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ApplicationClientType_configId_feature", "_UI_ApplicationClientType_type"),
         ClientPackage.eINSTANCE.getApplicationClientType_ConfigId(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Parent Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addParentIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ApplicationClientType_parentId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ApplicationClientType_parentId_feature", "_UI_ApplicationClientType_type"),
         ClientPackage.eINSTANCE.getApplicationClientType_ParentId(),
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
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_Import());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_Include());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_Dependency());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_HiddenClasses());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_NonOverridableClasses());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_EjbRef());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_ServiceRef());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_ResourceRef());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_ResourceEnvRef());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_MessageDestination());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_DefaultPrincipal());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_Resource());
      childrenFeatures.add(ClientPackage.eINSTANCE.getApplicationClientType_Gbean());
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
   * This returns ApplicationClientType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/ApplicationClientType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((ApplicationClientType)object).getRealmName();
    return label == null || label.length() == 0 ?
      getString("_UI_ApplicationClientType_type") :
      getString("_UI_ApplicationClientType_type") + " " + label;
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

    switch (notification.getFeatureID(ApplicationClientType.class))
    {
      case ClientPackage.APPLICATION_CLIENT_TYPE__REALM_NAME:
      case ClientPackage.APPLICATION_CLIENT_TYPE__CALLBACK_HANDLER:
      case ClientPackage.APPLICATION_CLIENT_TYPE__CLIENT_CONFIG_ID:
      case ClientPackage.APPLICATION_CLIENT_TYPE__CLIENT_PARENT_ID:
      case ClientPackage.APPLICATION_CLIENT_TYPE__CONFIG_ID:
      case ClientPackage.APPLICATION_CLIENT_TYPE__PARENT_ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case ClientPackage.APPLICATION_CLIENT_TYPE__IMPORT:
      case ClientPackage.APPLICATION_CLIENT_TYPE__INCLUDE:
      case ClientPackage.APPLICATION_CLIENT_TYPE__DEPENDENCY:
      case ClientPackage.APPLICATION_CLIENT_TYPE__HIDDEN_CLASSES:
      case ClientPackage.APPLICATION_CLIENT_TYPE__NON_OVERRIDABLE_CLASSES:
      case ClientPackage.APPLICATION_CLIENT_TYPE__EJB_REF:
      case ClientPackage.APPLICATION_CLIENT_TYPE__SERVICE_REF:
      case ClientPackage.APPLICATION_CLIENT_TYPE__RESOURCE_REF:
      case ClientPackage.APPLICATION_CLIENT_TYPE__RESOURCE_ENV_REF:
      case ClientPackage.APPLICATION_CLIENT_TYPE__MESSAGE_DESTINATION:
      case ClientPackage.APPLICATION_CLIENT_TYPE__DEFAULT_PRINCIPAL:
      case ClientPackage.APPLICATION_CLIENT_TYPE__RESOURCE:
      case ClientPackage.APPLICATION_CLIENT_TYPE__GBEAN:
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
        (ClientPackage.eINSTANCE.getApplicationClientType_Import(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_Include(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_Dependency(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_HiddenClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_NonOverridableClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_EjbRef(),
         NamingFactory.eINSTANCE.createEjbRefType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_ServiceRef(),
         NamingFactory.eINSTANCE.createServiceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_ResourceRef(),
         NamingFactory.eINSTANCE.createResourceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_ResourceEnvRef(),
         NamingFactory.eINSTANCE.createResourceEnvRefType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_MessageDestination(),
         NamingFactory.eINSTANCE.createMessageDestinationType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_DefaultPrincipal(),
         SecurityFactory.eINSTANCE.createDefaultPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_Resource(),
         ClientFactory.eINSTANCE.createResourceType()));

    newChildDescriptors.add
      (createChildParameter
        (ClientPackage.eINSTANCE.getApplicationClientType_Gbean(),
         DeploymentFactory.eINSTANCE.createGbeanType()));
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
      childFeature == ClientPackage.eINSTANCE.getApplicationClientType_Import() ||
      childFeature == ClientPackage.eINSTANCE.getApplicationClientType_Include() ||
      childFeature == ClientPackage.eINSTANCE.getApplicationClientType_Dependency() ||
      childFeature == ClientPackage.eINSTANCE.getApplicationClientType_HiddenClasses() ||
      childFeature == ClientPackage.eINSTANCE.getApplicationClientType_NonOverridableClasses();

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
    return GeronimoapplicationclientEditPlugin.INSTANCE;
  }

}
