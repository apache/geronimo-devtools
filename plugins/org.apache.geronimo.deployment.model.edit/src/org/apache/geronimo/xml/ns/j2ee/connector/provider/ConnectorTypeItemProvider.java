/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.j2ee.connector.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;

import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorFactory;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorPackage;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorType;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.j2ee.connector.ConnectorType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ConnectorTypeItemProvider
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
  public ConnectorTypeItemProvider(AdapterFactory adapterFactory)
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
      addInverseClassloadingPropertyDescriptor(object);
      addParentIdPropertyDescriptor(object);
      addSuppressDefaultParentIdPropertyDescriptor(object);
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
         getString("_UI_ConnectorType_configId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConnectorType_configId_feature", "_UI_ConnectorType_type"),
         ConnectorPackage.eINSTANCE.getConnectorType_ConfigId(),
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
         getString("_UI_ConnectorType_inverseClassloading_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConnectorType_inverseClassloading_feature", "_UI_ConnectorType_type"),
         ConnectorPackage.eINSTANCE.getConnectorType_InverseClassloading(),
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
         getString("_UI_ConnectorType_parentId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConnectorType_parentId_feature", "_UI_ConnectorType_type"),
         ConnectorPackage.eINSTANCE.getConnectorType_ParentId(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Suppress Default Parent Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSuppressDefaultParentIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_ConnectorType_suppressDefaultParentId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_ConnectorType_suppressDefaultParentId_feature", "_UI_ConnectorType_type"),
         ConnectorPackage.eINSTANCE.getConnectorType_SuppressDefaultParentId(),
         true,
         ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectorType_Import());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectorType_Dependency());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectorType_HiddenClasses());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectorType_NonOverridableClasses());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectorType_Resourceadapter());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectorType_Adminobject());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectorType_Gbean());
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
   * This returns ConnectorType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/ConnectorType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((ConnectorType)object).getConfigId();
    return label == null || label.length() == 0 ?
      getString("_UI_ConnectorType_type") :
      getString("_UI_ConnectorType_type") + " " + label;
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

    switch (notification.getFeatureID(ConnectorType.class))
    {
      case ConnectorPackage.CONNECTOR_TYPE__CONFIG_ID:
      case ConnectorPackage.CONNECTOR_TYPE__INVERSE_CLASSLOADING:
      case ConnectorPackage.CONNECTOR_TYPE__PARENT_ID:
      case ConnectorPackage.CONNECTOR_TYPE__SUPPRESS_DEFAULT_PARENT_ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case ConnectorPackage.CONNECTOR_TYPE__IMPORT:
      case ConnectorPackage.CONNECTOR_TYPE__DEPENDENCY:
      case ConnectorPackage.CONNECTOR_TYPE__HIDDEN_CLASSES:
      case ConnectorPackage.CONNECTOR_TYPE__NON_OVERRIDABLE_CLASSES:
      case ConnectorPackage.CONNECTOR_TYPE__RESOURCEADAPTER:
      case ConnectorPackage.CONNECTOR_TYPE__ADMINOBJECT:
      case ConnectorPackage.CONNECTOR_TYPE__GBEAN:
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
        (ConnectorPackage.eINSTANCE.getConnectorType_Import(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectorType_Dependency(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectorType_HiddenClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectorType_NonOverridableClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectorType_Resourceadapter(),
         ConnectorFactory.eINSTANCE.createResourceadapterType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectorType_Adminobject(),
         ConnectorFactory.eINSTANCE.createAdminobjectType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectorType_Gbean(),
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
      childFeature == ConnectorPackage.eINSTANCE.getConnectorType_Import() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectorType_Dependency() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectorType_HiddenClasses() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectorType_NonOverridableClasses();

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
    return GeronimoconnectorEditPlugin.INSTANCE;
  }

}
