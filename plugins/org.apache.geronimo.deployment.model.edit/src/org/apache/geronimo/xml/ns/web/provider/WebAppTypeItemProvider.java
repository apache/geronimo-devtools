/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.web.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;

import org.apache.geronimo.xml.ns.naming.NamingFactory;

import org.apache.geronimo.xml.ns.security.SecurityFactory;

import org.apache.geronimo.xml.ns.web.WebAppType;
import org.apache.geronimo.xml.ns.web.WebFactory;
import org.apache.geronimo.xml.ns.web.WebPackage;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.web.WebAppType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class WebAppTypeItemProvider
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
  public WebAppTypeItemProvider(AdapterFactory adapterFactory)
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

      addContextRootPropertyDescriptor(object);
      addContextPriorityClassloaderPropertyDescriptor(object);
      addSecurityRealmNamePropertyDescriptor(object);
      addConfigIdPropertyDescriptor(object);
      addParentIdPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Context Root feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addContextRootPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_WebAppType_contextRoot_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_WebAppType_contextRoot_feature", "_UI_WebAppType_type"),
         WebPackage.eINSTANCE.getWebAppType_ContextRoot(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Context Priority Classloader feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addContextPriorityClassloaderPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_WebAppType_contextPriorityClassloader_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_WebAppType_contextPriorityClassloader_feature", "_UI_WebAppType_type"),
         WebPackage.eINSTANCE.getWebAppType_ContextPriorityClassloader(),
         true,
         ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Security Realm Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSecurityRealmNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_WebAppType_securityRealmName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_WebAppType_securityRealmName_feature", "_UI_WebAppType_type"),
         WebPackage.eINSTANCE.getWebAppType_SecurityRealmName(),
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
         getString("_UI_WebAppType_configId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_WebAppType_configId_feature", "_UI_WebAppType_type"),
         WebPackage.eINSTANCE.getWebAppType_ConfigId(),
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
         getString("_UI_WebAppType_parentId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_WebAppType_parentId_feature", "_UI_WebAppType_type"),
         WebPackage.eINSTANCE.getWebAppType_ParentId(),
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
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_Import());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_Dependency());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_HiddenClasses());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_NonOverridableClasses());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_ContainerConfig());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_GbeanRef());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_EjbRef());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_EjbLocalRef());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_ServiceRef());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_ResourceRef());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_ResourceEnvRef());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_MessageDestination());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_Security());
      childrenFeatures.add(WebPackage.eINSTANCE.getWebAppType_Gbean());
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
   * This returns WebAppType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/WebAppType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((WebAppType)object).getSecurityRealmName();
    return label == null || label.length() == 0 ?
      getString("_UI_WebAppType_type") :
      getString("_UI_WebAppType_type") + " " + label;
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

    switch (notification.getFeatureID(WebAppType.class))
    {
      case WebPackage.WEB_APP_TYPE__CONTEXT_ROOT:
      case WebPackage.WEB_APP_TYPE__CONTEXT_PRIORITY_CLASSLOADER:
      case WebPackage.WEB_APP_TYPE__SECURITY_REALM_NAME:
      case WebPackage.WEB_APP_TYPE__CONFIG_ID:
      case WebPackage.WEB_APP_TYPE__PARENT_ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case WebPackage.WEB_APP_TYPE__IMPORT:
      case WebPackage.WEB_APP_TYPE__DEPENDENCY:
      case WebPackage.WEB_APP_TYPE__HIDDEN_CLASSES:
      case WebPackage.WEB_APP_TYPE__NON_OVERRIDABLE_CLASSES:
      case WebPackage.WEB_APP_TYPE__CONTAINER_CONFIG:
      case WebPackage.WEB_APP_TYPE__GBEAN_REF:
      case WebPackage.WEB_APP_TYPE__EJB_REF:
      case WebPackage.WEB_APP_TYPE__EJB_LOCAL_REF:
      case WebPackage.WEB_APP_TYPE__SERVICE_REF:
      case WebPackage.WEB_APP_TYPE__RESOURCE_REF:
      case WebPackage.WEB_APP_TYPE__RESOURCE_ENV_REF:
      case WebPackage.WEB_APP_TYPE__MESSAGE_DESTINATION:
      case WebPackage.WEB_APP_TYPE__SECURITY:
      case WebPackage.WEB_APP_TYPE__GBEAN:
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
        (WebPackage.eINSTANCE.getWebAppType_Import(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_Dependency(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_HiddenClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_NonOverridableClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_ContainerConfig(),
         WebFactory.eINSTANCE.createContainerConfigType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_GbeanRef(),
         NamingFactory.eINSTANCE.createGbeanRefType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_EjbRef(),
         NamingFactory.eINSTANCE.createEjbRefType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_EjbLocalRef(),
         NamingFactory.eINSTANCE.createEjbLocalRefType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_ServiceRef(),
         NamingFactory.eINSTANCE.createServiceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_ResourceRef(),
         NamingFactory.eINSTANCE.createResourceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_ResourceEnvRef(),
         NamingFactory.eINSTANCE.createResourceEnvRefType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_MessageDestination(),
         NamingFactory.eINSTANCE.createMessageDestinationType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_Security(),
         SecurityFactory.eINSTANCE.createSecurityType()));

    newChildDescriptors.add
      (createChildParameter
        (WebPackage.eINSTANCE.getWebAppType_Gbean(),
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
      childFeature == WebPackage.eINSTANCE.getWebAppType_Import() ||
      childFeature == WebPackage.eINSTANCE.getWebAppType_Dependency() ||
      childFeature == WebPackage.eINSTANCE.getWebAppType_HiddenClasses() ||
      childFeature == WebPackage.eINSTANCE.getWebAppType_NonOverridableClasses();

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
