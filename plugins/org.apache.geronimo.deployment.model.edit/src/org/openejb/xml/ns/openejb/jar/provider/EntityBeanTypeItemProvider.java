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

import org.openejb.xml.ns.openejb.jar.EntityBeanType;
import org.openejb.xml.ns.openejb.jar.JarFactory;
import org.openejb.xml.ns.openejb.jar.JarPackage;

import org.openejb.xml.ns.pkgen.PkgenFactory;

/**
 * This is the item provider adapter for a {@link org.openejb.xml.ns.openejb.jar.EntityBeanType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EntityBeanTypeItemProvider
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
  public EntityBeanTypeItemProvider(AdapterFactory adapterFactory)
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
      addJndiNamePropertyDescriptor(object);
      addLocalJndiNamePropertyDescriptor(object);
      addTssTargetNamePropertyDescriptor(object);
      addTssLinkPropertyDescriptor(object);
      addTableNamePropertyDescriptor(object);
      addPrimkeyFieldPropertyDescriptor(object);
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
         getString("_UI_EntityBeanType_ejbName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_ejbName_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_EjbName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Jndi Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addJndiNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_EntityBeanType_jndiName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_jndiName_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_JndiName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Local Jndi Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLocalJndiNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_EntityBeanType_localJndiName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_localJndiName_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_LocalJndiName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Tss Target Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTssTargetNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_EntityBeanType_tssTargetName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_tssTargetName_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_TssTargetName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Tss Link feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTssLinkPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_EntityBeanType_tssLink_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_tssLink_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_TssLink(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Table Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTableNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_EntityBeanType_tableName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_tableName_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_TableName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Primkey Field feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPrimkeyFieldPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_EntityBeanType_primkeyField_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_primkeyField_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_PrimkeyField(),
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
         getString("_UI_EntityBeanType_id_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EntityBeanType_id_feature", "_UI_EntityBeanType_type"),
         JarPackage.eINSTANCE.getEntityBeanType_Id(),
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
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_Tss());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_CmpFieldMapping());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_KeyGenerator());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_PrefetchGroup());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_Cache());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_GbeanRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_EjbRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_EjbLocalRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_ServiceRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_ResourceRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_ResourceEnvRef());
      childrenFeatures.add(JarPackage.eINSTANCE.getEntityBeanType_Query());
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
   * This returns EntityBeanType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/EntityBeanType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((EntityBeanType)object).getEjbName();
    return label == null || label.length() == 0 ?
      getString("_UI_EntityBeanType_type") :
      getString("_UI_EntityBeanType_type") + " " + label;
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

    switch (notification.getFeatureID(EntityBeanType.class))
    {
      case JarPackage.ENTITY_BEAN_TYPE__EJB_NAME:
      case JarPackage.ENTITY_BEAN_TYPE__JNDI_NAME:
      case JarPackage.ENTITY_BEAN_TYPE__LOCAL_JNDI_NAME:
      case JarPackage.ENTITY_BEAN_TYPE__TSS_TARGET_NAME:
      case JarPackage.ENTITY_BEAN_TYPE__TSS_LINK:
      case JarPackage.ENTITY_BEAN_TYPE__TABLE_NAME:
      case JarPackage.ENTITY_BEAN_TYPE__PRIMKEY_FIELD:
      case JarPackage.ENTITY_BEAN_TYPE__ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case JarPackage.ENTITY_BEAN_TYPE__TSS:
      case JarPackage.ENTITY_BEAN_TYPE__CMP_FIELD_MAPPING:
      case JarPackage.ENTITY_BEAN_TYPE__KEY_GENERATOR:
      case JarPackage.ENTITY_BEAN_TYPE__PREFETCH_GROUP:
      case JarPackage.ENTITY_BEAN_TYPE__CACHE:
      case JarPackage.ENTITY_BEAN_TYPE__GBEAN_REF:
      case JarPackage.ENTITY_BEAN_TYPE__EJB_REF:
      case JarPackage.ENTITY_BEAN_TYPE__EJB_LOCAL_REF:
      case JarPackage.ENTITY_BEAN_TYPE__SERVICE_REF:
      case JarPackage.ENTITY_BEAN_TYPE__RESOURCE_REF:
      case JarPackage.ENTITY_BEAN_TYPE__RESOURCE_ENV_REF:
      case JarPackage.ENTITY_BEAN_TYPE__QUERY:
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
        (JarPackage.eINSTANCE.getEntityBeanType_Tss(),
         JarFactory.eINSTANCE.createTssType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_CmpFieldMapping(),
         JarFactory.eINSTANCE.createCmpFieldMappingType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_KeyGenerator(),
         PkgenFactory.eINSTANCE.createKeyGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_PrefetchGroup(),
         JarFactory.eINSTANCE.createPrefetchGroupType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_Cache(),
         JarFactory.eINSTANCE.createCacheType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_GbeanRef(),
         NamingFactory.eINSTANCE.createGbeanRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_EjbRef(),
         NamingFactory.eINSTANCE.createEjbRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_EjbLocalRef(),
         NamingFactory.eINSTANCE.createEjbLocalRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_ServiceRef(),
         NamingFactory.eINSTANCE.createServiceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_ResourceRef(),
         NamingFactory.eINSTANCE.createResourceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_ResourceEnvRef(),
         NamingFactory.eINSTANCE.createResourceEnvRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEntityBeanType_Query(),
         JarFactory.eINSTANCE.createQueryType()));
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
