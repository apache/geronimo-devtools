/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openejb.xml.ns.openejb.jar.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.openejb.xml.ns.openejb.jar.CmpFieldMappingType;
import org.openejb.xml.ns.openejb.jar.JarPackage;

/**
 * This is the item provider adapter for a {@link org.openejb.xml.ns.openejb.jar.CmpFieldMappingType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CmpFieldMappingTypeItemProvider
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
  public CmpFieldMappingTypeItemProvider(AdapterFactory adapterFactory)
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

      addCmpFieldNamePropertyDescriptor(object);
      addCmpFieldClassPropertyDescriptor(object);
      addTableColumnPropertyDescriptor(object);
      addSqlTypePropertyDescriptor(object);
      addTypeConverterPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Cmp Field Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addCmpFieldNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_CmpFieldMappingType_cmpFieldName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_CmpFieldMappingType_cmpFieldName_feature", "_UI_CmpFieldMappingType_type"),
         JarPackage.eINSTANCE.getCmpFieldMappingType_CmpFieldName(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Cmp Field Class feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addCmpFieldClassPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_CmpFieldMappingType_cmpFieldClass_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_CmpFieldMappingType_cmpFieldClass_feature", "_UI_CmpFieldMappingType_type"),
         JarPackage.eINSTANCE.getCmpFieldMappingType_CmpFieldClass(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Table Column feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTableColumnPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_CmpFieldMappingType_tableColumn_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_CmpFieldMappingType_tableColumn_feature", "_UI_CmpFieldMappingType_type"),
         JarPackage.eINSTANCE.getCmpFieldMappingType_TableColumn(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Sql Type feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSqlTypePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_CmpFieldMappingType_sqlType_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_CmpFieldMappingType_sqlType_feature", "_UI_CmpFieldMappingType_type"),
         JarPackage.eINSTANCE.getCmpFieldMappingType_SqlType(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Type Converter feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addTypeConverterPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_CmpFieldMappingType_typeConverter_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_CmpFieldMappingType_typeConverter_feature", "_UI_CmpFieldMappingType_type"),
         JarPackage.eINSTANCE.getCmpFieldMappingType_TypeConverter(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This returns CmpFieldMappingType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/CmpFieldMappingType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((CmpFieldMappingType)object).getCmpFieldName();
    return label == null || label.length() == 0 ?
      getString("_UI_CmpFieldMappingType_type") :
      getString("_UI_CmpFieldMappingType_type") + " " + label;
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

    switch (notification.getFeatureID(CmpFieldMappingType.class))
    {
      case JarPackage.CMP_FIELD_MAPPING_TYPE__CMP_FIELD_NAME:
      case JarPackage.CMP_FIELD_MAPPING_TYPE__CMP_FIELD_CLASS:
      case JarPackage.CMP_FIELD_MAPPING_TYPE__TABLE_COLUMN:
      case JarPackage.CMP_FIELD_MAPPING_TYPE__SQL_TYPE:
      case JarPackage.CMP_FIELD_MAPPING_TYPE__TYPE_CONVERTER:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
