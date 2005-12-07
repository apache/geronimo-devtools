/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openejb.xml.ns.pkgen.provider;


import java.util.Collection;
import java.util.List;

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

import org.openejb.xml.ns.openejb.jar.provider.OpenejbjarEditPlugin;

import org.openejb.xml.ns.pkgen.KeyGeneratorType;
import org.openejb.xml.ns.pkgen.PkgenFactory;
import org.openejb.xml.ns.pkgen.PkgenPackage;

/**
 * This is the item provider adapter for a {@link org.openejb.xml.ns.pkgen.KeyGeneratorType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class KeyGeneratorTypeItemProvider
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
  public KeyGeneratorTypeItemProvider(AdapterFactory adapterFactory)
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
      childrenFeatures.add(PkgenPackage.eINSTANCE.getKeyGeneratorType_SequenceTable());
      childrenFeatures.add(PkgenPackage.eINSTANCE.getKeyGeneratorType_AutoIncrementTable());
      childrenFeatures.add(PkgenPackage.eINSTANCE.getKeyGeneratorType_SqlGenerator());
      childrenFeatures.add(PkgenPackage.eINSTANCE.getKeyGeneratorType_CustomGenerator());
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
   * This returns KeyGeneratorType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/KeyGeneratorType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    return getString("_UI_KeyGeneratorType_type");
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

    switch (notification.getFeatureID(KeyGeneratorType.class))
    {
      case PkgenPackage.KEY_GENERATOR_TYPE__SEQUENCE_TABLE:
      case PkgenPackage.KEY_GENERATOR_TYPE__AUTO_INCREMENT_TABLE:
      case PkgenPackage.KEY_GENERATOR_TYPE__SQL_GENERATOR:
      case PkgenPackage.KEY_GENERATOR_TYPE__CUSTOM_GENERATOR:
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
        (PkgenPackage.eINSTANCE.getKeyGeneratorType_SequenceTable(),
         PkgenFactory.eINSTANCE.createSequenceTableType()));

    newChildDescriptors.add
      (createChildParameter
        (PkgenPackage.eINSTANCE.getKeyGeneratorType_AutoIncrementTable(),
         PkgenFactory.eINSTANCE.createAutoIncrementTableType()));

    newChildDescriptors.add
      (createChildParameter
        (PkgenPackage.eINSTANCE.getKeyGeneratorType_SqlGenerator(),
         PkgenFactory.eINSTANCE.createSqlGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (PkgenPackage.eINSTANCE.getKeyGeneratorType_CustomGenerator(),
         PkgenFactory.eINSTANCE.createCustomGeneratorType()));
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
