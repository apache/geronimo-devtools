/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.openejb.xml.ns.openejb.jar.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;

import org.apache.geronimo.xml.ns.naming.NamingFactory;

import org.apache.geronimo.xml.ns.security.SecurityFactory;

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

import org.openejb.xml.ns.openejb.jar.EjbRelationshipRoleType;
import org.openejb.xml.ns.openejb.jar.JarFactory;
import org.openejb.xml.ns.openejb.jar.JarPackage;

import org.openejb.xml.ns.pkgen.PkgenFactory;

/**
 * This is the item provider adapter for a {@link org.openejb.xml.ns.openejb.jar.EjbRelationshipRoleType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EjbRelationshipRoleTypeItemProvider
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
  public EjbRelationshipRoleTypeItemProvider(AdapterFactory adapterFactory)
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

      addEjbRelationshipRoleNamePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Ejb Relationship Role Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addEjbRelationshipRoleNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_EjbRelationshipRoleType_ejbRelationshipRoleName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_EjbRelationshipRoleType_ejbRelationshipRoleName_feature", "_UI_EjbRelationshipRoleType_type"),
         JarPackage.eINSTANCE.getEjbRelationshipRoleType_EjbRelationshipRoleName(),
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
      childrenFeatures.add(JarPackage.eINSTANCE.getEjbRelationshipRoleType_RelationshipRoleSource());
      childrenFeatures.add(JarPackage.eINSTANCE.getEjbRelationshipRoleType_CmrField());
      childrenFeatures.add(JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource());
      childrenFeatures.add(JarPackage.eINSTANCE.getEjbRelationshipRoleType_RoleMapping());
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
   * This returns EjbRelationshipRoleType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/EjbRelationshipRoleType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((EjbRelationshipRoleType)object).getEjbRelationshipRoleName();
    return label == null || label.length() == 0 ?
      getString("_UI_EjbRelationshipRoleType_type") :
      getString("_UI_EjbRelationshipRoleType_type") + " " + label;
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

    switch (notification.getFeatureID(EjbRelationshipRoleType.class))
    {
      case JarPackage.EJB_RELATIONSHIP_ROLE_TYPE__EJB_RELATIONSHIP_ROLE_NAME:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case JarPackage.EJB_RELATIONSHIP_ROLE_TYPE__RELATIONSHIP_ROLE_SOURCE:
      case JarPackage.EJB_RELATIONSHIP_ROLE_TYPE__CMR_FIELD:
      case JarPackage.EJB_RELATIONSHIP_ROLE_TYPE__FOREIGN_KEY_COLUMN_ON_SOURCE:
      case JarPackage.EJB_RELATIONSHIP_ROLE_TYPE__ROLE_MAPPING:
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
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_RelationshipRoleSource(),
         JarFactory.eINSTANCE.createRelationshipRoleSourceType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_CmrField(),
         JarFactory.eINSTANCE.createCmrFieldType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createActivationConfigPropertyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createActivationConfigType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createCmpFieldGroupMappingType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createCmrFieldGroupMappingType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createEjbRelationshipRoleType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createEjbRelationType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createEntityBeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createEntityGroupMappingType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createGroupType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createMessageDrivenBeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createOpenejbJarType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createQueryType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createRelationshipsType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createSessionBeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createTssType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         JarFactory.eINSTANCE.createWebServiceSecurityType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         PkgenFactory.eINSTANCE.createAutoIncrementTableType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         PkgenFactory.eINSTANCE.createCustomGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         PkgenFactory.eINSTANCE.createDatabaseGeneratedType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         PkgenFactory.eINSTANCE.createKeyGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         PkgenFactory.eINSTANCE.createSequenceTableType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         PkgenFactory.eINSTANCE.createSqlGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createCssType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createEjbLocalRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createEjbRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createGbeanLocatorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createGbeanRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createMessageDestinationType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createPortCompletionType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createPortType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createResourceEnvRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createResourceLocatorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createResourceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createServiceCompletionType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         NamingFactory.eINSTANCE.createServiceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createDefaultPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createDescriptionType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createDistinguishedNameType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createLoginDomainPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createNamedUsernamePasswordCredentialType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createRealmPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createRoleMappingsType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createRoleType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         SecurityFactory.eINSTANCE.createSecurityType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createAttributeType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createConfigurationType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createGbeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createPatternType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createReferencesType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createReferenceType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createServiceType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_ForeignKeyColumnOnSource(),
         DeploymentFactory.eINSTANCE.createXmlAttributeType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getEjbRelationshipRoleType_RoleMapping(),
         JarFactory.eINSTANCE.createRoleMappingType()));
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
