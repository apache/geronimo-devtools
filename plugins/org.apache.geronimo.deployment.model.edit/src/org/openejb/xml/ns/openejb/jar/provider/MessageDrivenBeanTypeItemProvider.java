/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openejb.xml.ns.openejb.jar.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.naming.NamingFactory;

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

import org.openejb.xml.ns.openejb.jar.JarFactory;
import org.openejb.xml.ns.openejb.jar.JarPackage;
import org.openejb.xml.ns.openejb.jar.MessageDrivenBeanType;

/**
 * This is the item provider adapter for a {@link org.openejb.xml.ns.openejb.jar.MessageDrivenBeanType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MessageDrivenBeanTypeItemProvider
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
  public MessageDrivenBeanTypeItemProvider(AdapterFactory adapterFactory)
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

      addEjbNamePropertyDescriptor(object);
      addIdPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Ejb Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addEjbNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_MessageDrivenBeanType_ejbName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_MessageDrivenBeanType_ejbName_feature", "_UI_MessageDrivenBeanType_type"),
         JarPackage.eINSTANCE.getMessageDrivenBeanType_EjbName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Id feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIdPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_MessageDrivenBeanType_id_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_MessageDrivenBeanType_id_feature", "_UI_MessageDrivenBeanType_type"),
         JarPackage.eINSTANCE.getMessageDrivenBeanType_Id(),
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
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_ResourceAdapter());
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_ActivationConfig());
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_GbeanRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_EjbRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_EjbLocalRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_ServiceRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_ResourceRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getMessageDrivenBeanType_ResourceEnvRef());
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
   * This returns MessageDrivenBeanType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/MessageDrivenBeanType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((MessageDrivenBeanType)object).getEjbName();
    return label == null || label.length() == 0 ?
      getString("_UI_MessageDrivenBeanType_type") :
      getString("_UI_MessageDrivenBeanType_type") + " " + label;
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

    switch (notification.getFeatureID(MessageDrivenBeanType.class))
    {
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__EJB_NAME:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__RESOURCE_ADAPTER:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__ACTIVATION_CONFIG:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__GBEAN_REF:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__EJB_REF:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__EJB_LOCAL_REF:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__SERVICE_REF:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__RESOURCE_REF:
      case JarPackage.MESSAGE_DRIVEN_BEAN_TYPE__RESOURCE_ENV_REF:
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
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_ResourceAdapter(),
         NamingFactory.eINSTANCE.createResourceLocatorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_ActivationConfig(),
         JarFactory.eINSTANCE.createActivationConfigType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_GbeanRef(),
         NamingFactory.eINSTANCE.createGbeanRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_EjbRef(),
         NamingFactory.eINSTANCE.createEjbRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_EjbLocalRef(),
         NamingFactory.eINSTANCE.createEjbLocalRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_ServiceRef(),
         NamingFactory.eINSTANCE.createServiceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_ResourceRef(),
         NamingFactory.eINSTANCE.createResourceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getMessageDrivenBeanType_ResourceEnvRef(),
         NamingFactory.eINSTANCE.createResourceEnvRefType()));
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResourceLocator getResourceLocator()
  {
    return OpenejbjarEditPlugin.INSTANCE;
  }

}
