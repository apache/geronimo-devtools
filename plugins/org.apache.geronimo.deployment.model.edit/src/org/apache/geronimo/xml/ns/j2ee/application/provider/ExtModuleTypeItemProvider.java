/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.j2ee.application.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;

import org.apache.geronimo.xml.ns.j2ee.application.ApplicationFactory;
import org.apache.geronimo.xml.ns.j2ee.application.ApplicationPackage;
import org.apache.geronimo.xml.ns.j2ee.application.ExtModuleType;

import org.apache.geronimo.xml.ns.security.SecurityFactory;
import org.apache.geronimo.xml.ns.security.SecurityPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.util.FeatureMap;
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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.j2ee.application.ExtModuleType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ExtModuleTypeItemProvider
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
  public ExtModuleTypeItemProvider(AdapterFactory adapterFactory)
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

      addInternalPathPropertyDescriptor(object);
      addExternalPathPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Internal Path feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addInternalPathPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ExtModuleType_internalPath_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ExtModuleType_internalPath_feature", "_UI_ExtModuleType_type"),
         ApplicationPackage.eINSTANCE.getExtModuleType_InternalPath(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the External Path feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addExternalPathPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ExtModuleType_externalPath_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ExtModuleType_externalPath_feature", "_UI_ExtModuleType_type"),
         ApplicationPackage.eINSTANCE.getExtModuleType_ExternalPath(),
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
      childrenFeatures.add(ApplicationPackage.eINSTANCE.getExtModuleType_Connector());
      childrenFeatures.add(ApplicationPackage.eINSTANCE.getExtModuleType_Ejb());
      childrenFeatures.add(ApplicationPackage.eINSTANCE.getExtModuleType_Java());
      childrenFeatures.add(ApplicationPackage.eINSTANCE.getExtModuleType_Web());
      childrenFeatures.add(ApplicationPackage.eINSTANCE.getExtModuleType_Any());
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
   * This returns ExtModuleType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/ExtModuleType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((ExtModuleType)object).getInternalPath();
    return label == null || label.length() == 0 ?
      getString("_UI_ExtModuleType_type") :
      getString("_UI_ExtModuleType_type") + " " + label;
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

    switch (notification.getFeatureID(ExtModuleType.class))
    {
      case ApplicationPackage.EXT_MODULE_TYPE__INTERNAL_PATH:
      case ApplicationPackage.EXT_MODULE_TYPE__EXTERNAL_PATH:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case ApplicationPackage.EXT_MODULE_TYPE__CONNECTOR:
      case ApplicationPackage.EXT_MODULE_TYPE__EJB:
      case ApplicationPackage.EXT_MODULE_TYPE__JAVA:
      case ApplicationPackage.EXT_MODULE_TYPE__WEB:
      case ApplicationPackage.EXT_MODULE_TYPE__ANY:
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
        (ApplicationPackage.eINSTANCE.getExtModuleType_Connector(),
         ApplicationFactory.eINSTANCE.createPathType()));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Ejb(),
         ApplicationFactory.eINSTANCE.createPathType()));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Java(),
         ApplicationFactory.eINSTANCE.createPathType()));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Web(),
         ApplicationFactory.eINSTANCE.createPathType()));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (SecurityPackage.eINSTANCE.getDocumentRoot_DefaultPrincipal(),
           SecurityFactory.eINSTANCE.createDefaultPrincipalType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (SecurityPackage.eINSTANCE.getDocumentRoot_Security(),
           SecurityFactory.eINSTANCE.createSecurityType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Configuration(),
           DeploymentFactory.eINSTANCE.createConfigurationType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Dependency(),
           DeploymentFactory.eINSTANCE.createDependencyType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Gbean(),
           DeploymentFactory.eINSTANCE.createGbeanType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_HiddenClasses(),
           DeploymentFactory.eINSTANCE.createClassFilterType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Import(),
           DeploymentFactory.eINSTANCE.createDependencyType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Include(),
           DeploymentFactory.eINSTANCE.createDependencyType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_NonOverridableClasses(),
           DeploymentFactory.eINSTANCE.createClassFilterType())));

    newChildDescriptors.add
      (createChildParameter
        (ApplicationPackage.eINSTANCE.getExtModuleType_Any(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Service(),
           DeploymentFactory.eINSTANCE.createServiceType())));
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

    if (childFeature instanceof EStructuralFeature && FeatureMapUtil.isFeatureMap((EStructuralFeature)childFeature))
    {
      FeatureMap.Entry entry = (FeatureMap.Entry)childObject;
      childFeature = entry.getEStructuralFeature();
      childObject = entry.getValue();
    }

    boolean qualify =
      childFeature == ApplicationPackage.eINSTANCE.getExtModuleType_Connector() ||
      childFeature == ApplicationPackage.eINSTANCE.getExtModuleType_Ejb() ||
      childFeature == ApplicationPackage.eINSTANCE.getExtModuleType_Java() ||
      childFeature == ApplicationPackage.eINSTANCE.getExtModuleType_Web() ||
      childFeature == DeploymentPackage.eINSTANCE.getDocumentRoot_Dependency() ||
      childFeature == DeploymentPackage.eINSTANCE.getDocumentRoot_Import() ||
      childFeature == DeploymentPackage.eINSTANCE.getDocumentRoot_Include() ||
      childFeature == DeploymentPackage.eINSTANCE.getDocumentRoot_HiddenClasses() ||
      childFeature == DeploymentPackage.eINSTANCE.getDocumentRoot_NonOverridableClasses();

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
    return GeronimoapplicationEditPlugin.INSTANCE;
  }

}
