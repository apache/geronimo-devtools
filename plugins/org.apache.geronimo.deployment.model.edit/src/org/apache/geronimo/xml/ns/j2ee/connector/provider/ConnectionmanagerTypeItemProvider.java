/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.j2ee.connector.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.j2ee.connector.ConnectionmanagerType;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorFactory;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.j2ee.connector.ConnectionmanagerType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ConnectionmanagerTypeItemProvider
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
  public ConnectionmanagerTypeItemProvider(AdapterFactory adapterFactory)
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

    }
    return itemPropertyDescriptors;
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
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_ContainerManagedSecurity());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_NoTransaction());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_LocalTransaction());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_XaTransaction());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_TransactionLog());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_NoPool());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_SinglePool());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getConnectionmanagerType_PartitionedPool());
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
   * This returns ConnectionmanagerType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/ConnectionmanagerType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    return getString("_UI_ConnectionmanagerType_type");
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

    switch (notification.getFeatureID(ConnectionmanagerType.class))
    {
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__CONTAINER_MANAGED_SECURITY:
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__NO_TRANSACTION:
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__LOCAL_TRANSACTION:
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__XA_TRANSACTION:
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__TRANSACTION_LOG:
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__NO_POOL:
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__SINGLE_POOL:
      case ConnectorPackage.CONNECTIONMANAGER_TYPE__PARTITIONED_POOL:
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
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_ContainerManagedSecurity(),
         ConnectorFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_NoTransaction(),
         ConnectorFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_LocalTransaction(),
         ConnectorFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_XaTransaction(),
         ConnectorFactory.eINSTANCE.createXatransactionType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_TransactionLog(),
         ConnectorFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_NoPool(),
         ConnectorFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_SinglePool(),
         ConnectorFactory.eINSTANCE.createSinglepoolType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_SinglePool(),
         ConnectorFactory.eINSTANCE.createPartitionedpoolType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getConnectionmanagerType_PartitionedPool(),
         ConnectorFactory.eINSTANCE.createPartitionedpoolType()));
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
      childFeature == ConnectorPackage.eINSTANCE.getConnectionmanagerType_ContainerManagedSecurity() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectionmanagerType_NoTransaction() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectionmanagerType_LocalTransaction() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectionmanagerType_TransactionLog() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectionmanagerType_NoPool() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectionmanagerType_SinglePool() ||
      childFeature == ConnectorPackage.eINSTANCE.getConnectionmanagerType_PartitionedPool();

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
