/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.j2ee.connector.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorFactory;
import org.apache.geronimo.xml.ns.j2ee.connector.ConnectorPackage;
import org.apache.geronimo.xml.ns.j2ee.connector.SinglepoolType;

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
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.j2ee.connector.SinglepoolType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SinglepoolTypeItemProvider
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
  public SinglepoolTypeItemProvider(AdapterFactory adapterFactory)
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

      addMaxSizePropertyDescriptor(object);
      addMinSizePropertyDescriptor(object);
      addBlockingTimeoutMillisecondsPropertyDescriptor(object);
      addIdleTimeoutMinutesPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Max Size feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMaxSizePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SinglepoolType_maxSize_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SinglepoolType_maxSize_feature", "_UI_SinglepoolType_type"),
         ConnectorPackage.eINSTANCE.getSinglepoolType_MaxSize(),
         true,
         ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Min Size feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addMinSizePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SinglepoolType_minSize_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SinglepoolType_minSize_feature", "_UI_SinglepoolType_type"),
         ConnectorPackage.eINSTANCE.getSinglepoolType_MinSize(),
         true,
         ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Blocking Timeout Milliseconds feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addBlockingTimeoutMillisecondsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SinglepoolType_blockingTimeoutMilliseconds_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SinglepoolType_blockingTimeoutMilliseconds_feature", "_UI_SinglepoolType_type"),
         ConnectorPackage.eINSTANCE.getSinglepoolType_BlockingTimeoutMilliseconds(),
         true,
         ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Idle Timeout Minutes feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIdleTimeoutMinutesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_SinglepoolType_idleTimeoutMinutes_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_SinglepoolType_idleTimeoutMinutes_feature", "_UI_SinglepoolType_type"),
         ConnectorPackage.eINSTANCE.getSinglepoolType_IdleTimeoutMinutes(),
         true,
         ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getSinglepoolType_MatchOne());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getSinglepoolType_MatchAll());
      childrenFeatures.add(ConnectorPackage.eINSTANCE.getSinglepoolType_SelectOneAssumeMatch());
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
   * This returns SinglepoolType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/SinglepoolType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    SinglepoolType singlepoolType = (SinglepoolType)object;
    return getString("_UI_SinglepoolType_type") + " " + singlepoolType.getMaxSize();
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

    switch (notification.getFeatureID(SinglepoolType.class))
    {
      case ConnectorPackage.SINGLEPOOL_TYPE__MAX_SIZE:
      case ConnectorPackage.SINGLEPOOL_TYPE__MIN_SIZE:
      case ConnectorPackage.SINGLEPOOL_TYPE__BLOCKING_TIMEOUT_MILLISECONDS:
      case ConnectorPackage.SINGLEPOOL_TYPE__IDLE_TIMEOUT_MINUTES:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case ConnectorPackage.SINGLEPOOL_TYPE__MATCH_ONE:
      case ConnectorPackage.SINGLEPOOL_TYPE__MATCH_ALL:
      case ConnectorPackage.SINGLEPOOL_TYPE__SELECT_ONE_ASSUME_MATCH:
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
        (ConnectorPackage.eINSTANCE.getSinglepoolType_MatchOne(),
         ConnectorFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getSinglepoolType_MatchAll(),
         ConnectorFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (ConnectorPackage.eINSTANCE.getSinglepoolType_SelectOneAssumeMatch(),
         ConnectorFactory.eINSTANCE.createEmptyType()));
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
      childFeature == ConnectorPackage.eINSTANCE.getSinglepoolType_MatchOne() ||
      childFeature == ConnectorPackage.eINSTANCE.getSinglepoolType_MatchAll() ||
      childFeature == ConnectorPackage.eINSTANCE.getSinglepoolType_SelectOneAssumeMatch();

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
