/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.apache.geronimo.xml.ns.deployment.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.apache.geronimo.xml.ns.deployment.DocumentRoot;

import org.apache.geronimo.xml.ns.web.provider.GeronimowebEditPlugin;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.deployment.DocumentRoot} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DocumentRootItemProvider
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
  public DocumentRootItemProvider(AdapterFactory adapterFactory)
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
      childrenFeatures.add(DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed());
    }
    return childrenFeatures;
  }

  /**
   * This returns DocumentRoot.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/DocumentRoot");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    return getString("_UI_DocumentRoot_type");
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

    switch (notification.getFeatureID(DocumentRoot.class))
    {
      case DeploymentPackage.DOCUMENT_ROOT__MIXED:
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
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Comment(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (XMLTypePackage.eINSTANCE.getXMLTypeDocumentRoot_Text(),
           "")));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Configuration(),
           DeploymentFactory.eINSTANCE.createConfigurationType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Dependency(),
           DeploymentFactory.eINSTANCE.createDependencyType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Gbean(),
           DeploymentFactory.eINSTANCE.createGbeanType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_HiddenClasses(),
           DeploymentFactory.eINSTANCE.createClassFilterType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Import(),
           DeploymentFactory.eINSTANCE.createDependencyType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_Include(),
           DeploymentFactory.eINSTANCE.createDependencyType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
         FeatureMapUtil.createEntry
          (DeploymentPackage.eINSTANCE.getDocumentRoot_NonOverridableClasses(),
           DeploymentFactory.eINSTANCE.createClassFilterType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.eINSTANCE.getDocumentRoot_Mixed(),
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
    return GeronimowebEditPlugin.INSTANCE;
  }

}
