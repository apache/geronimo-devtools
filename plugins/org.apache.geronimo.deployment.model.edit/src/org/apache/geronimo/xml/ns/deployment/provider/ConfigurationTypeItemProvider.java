/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.deployment.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.ConfigurationType;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.deployment.ConfigurationType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigurationTypeItemProvider
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
  public ConfigurationTypeItemProvider(AdapterFactory adapterFactory)
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

      addConfigIdPropertyDescriptor(object);
      addDomainPropertyDescriptor(object);
      addInverseClassloadingPropertyDescriptor(object);
      addParentIdPropertyDescriptor(object);
      addServerPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
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
         getString("_UI_ConfigurationType_configId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationType_configId_feature", "_UI_ConfigurationType_type"),
         DeploymentPackage.eINSTANCE.getConfigurationType_ConfigId(),
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
         getString("_UI_ConfigurationType_domain_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationType_domain_feature", "_UI_ConfigurationType_type"),
         DeploymentPackage.eINSTANCE.getConfigurationType_Domain(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Inverse Classloading feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addInverseClassloadingPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ConfigurationType_inverseClassloading_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationType_inverseClassloading_feature", "_UI_ConfigurationType_type"),
         DeploymentPackage.eINSTANCE.getConfigurationType_InverseClassloading(),
         true,
         ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
         getString("_UI_ConfigurationType_parentId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationType_parentId_feature", "_UI_ConfigurationType_type"),
         DeploymentPackage.eINSTANCE.getConfigurationType_ParentId(),
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
         getString("_UI_ConfigurationType_server_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConfigurationType_server_feature", "_UI_ConfigurationType_type"),
         DeploymentPackage.eINSTANCE.getConfigurationType_Server(),
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
      childrenFeatures.add(DeploymentPackage.eINSTANCE.getConfigurationType_Import());
      childrenFeatures.add(DeploymentPackage.eINSTANCE.getConfigurationType_Include());
      childrenFeatures.add(DeploymentPackage.eINSTANCE.getConfigurationType_Dependency());
      childrenFeatures.add(DeploymentPackage.eINSTANCE.getConfigurationType_HiddenClasses());
      childrenFeatures.add(DeploymentPackage.eINSTANCE.getConfigurationType_NonOverridableClasses());
      childrenFeatures.add(DeploymentPackage.eINSTANCE.getConfigurationType_Gbean());
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
   * This returns ConfigurationType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/ConfigurationType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((ConfigurationType)object).getConfigId();
    return label == null || label.length() == 0 ?
      getString("_UI_ConfigurationType_type") :
      getString("_UI_ConfigurationType_type") + " " + label;
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

    switch (notification.getFeatureID(ConfigurationType.class))
    {
      case DeploymentPackage.CONFIGURATION_TYPE__CONFIG_ID:
      case DeploymentPackage.CONFIGURATION_TYPE__DOMAIN:
      case DeploymentPackage.CONFIGURATION_TYPE__INVERSE_CLASSLOADING:
      case DeploymentPackage.CONFIGURATION_TYPE__PARENT_ID:
      case DeploymentPackage.CONFIGURATION_TYPE__SERVER:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case DeploymentPackage.CONFIGURATION_TYPE__IMPORT:
      case DeploymentPackage.CONFIGURATION_TYPE__INCLUDE:
      case DeploymentPackage.CONFIGURATION_TYPE__DEPENDENCY:
      case DeploymentPackage.CONFIGURATION_TYPE__HIDDEN_CLASSES:
      case DeploymentPackage.CONFIGURATION_TYPE__NON_OVERRIDABLE_CLASSES:
      case DeploymentPackage.CONFIGURATION_TYPE__GBEAN:
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
        (DeploymentPackage.eINSTANCE.getConfigurationType_Import(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getConfigurationType_Include(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getConfigurationType_Dependency(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getConfigurationType_HiddenClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getConfigurationType_NonOverridableClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getConfigurationType_Gbean(),
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
      childFeature == DeploymentPackage.eINSTANCE.getConfigurationType_Import() ||
      childFeature == DeploymentPackage.eINSTANCE.getConfigurationType_Include() ||
      childFeature == DeploymentPackage.eINSTANCE.getConfigurationType_Dependency() ||
      childFeature == DeploymentPackage.eINSTANCE.getConfigurationType_HiddenClasses() ||
      childFeature == DeploymentPackage.eINSTANCE.getConfigurationType_NonOverridableClasses();

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
