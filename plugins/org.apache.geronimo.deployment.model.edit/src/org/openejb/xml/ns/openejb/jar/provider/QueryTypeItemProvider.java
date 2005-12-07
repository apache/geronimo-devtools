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

import org.openejb.xml.ns.openejb.jar.JarFactory;
import org.openejb.xml.ns.openejb.jar.JarPackage;
import org.openejb.xml.ns.openejb.jar.QueryType;

import org.openejb.xml.ns.pkgen.PkgenFactory;

/**
 * This is the item provider adapter for a {@link org.openejb.xml.ns.openejb.jar.QueryType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class QueryTypeItemProvider
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
  public QueryTypeItemProvider(AdapterFactory adapterFactory)
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

      addResultTypeMappingPropertyDescriptor(object);
      addEjbQlPropertyDescriptor(object);
      addGroupNamePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Result Type Mapping feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addResultTypeMappingPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_QueryType_resultTypeMapping_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_QueryType_resultTypeMapping_feature", "_UI_QueryType_type"),
         JarPackage.eINSTANCE.getQueryType_ResultTypeMapping(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Ejb Ql feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addEjbQlPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_QueryType_ejbQl_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_QueryType_ejbQl_feature", "_UI_QueryType_type"),
         JarPackage.eINSTANCE.getQueryType_EjbQl(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Group Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGroupNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_QueryType_groupName_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_QueryType_groupName_feature", "_UI_QueryType_type"),
         JarPackage.eINSTANCE.getQueryType_GroupName(),
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
      childrenFeatures.add(JarPackage.eINSTANCE.getQueryType_QueryMethod());
      childrenFeatures.add(JarPackage.eINSTANCE.getQueryType_NoCacheFlush());
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
   * This returns QueryType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/QueryType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((QueryType)object).getGroupName();
    return label == null || label.length() == 0 ?
      getString("_UI_QueryType_type") :
      getString("_UI_QueryType_type") + " " + label;
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

    switch (notification.getFeatureID(QueryType.class))
    {
      case JarPackage.QUERY_TYPE__RESULT_TYPE_MAPPING:
      case JarPackage.QUERY_TYPE__EJB_QL:
      case JarPackage.QUERY_TYPE__GROUP_NAME:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case JarPackage.QUERY_TYPE__QUERY_METHOD:
      case JarPackage.QUERY_TYPE__NO_CACHE_FLUSH:
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
        (JarPackage.eINSTANCE.getQueryType_QueryMethod(),
         JarFactory.eINSTANCE.createQueryMethodType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createActivationConfigPropertyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createActivationConfigType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createCmpFieldGroupMappingType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createCmrFieldGroupMappingType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createEjbRelationshipRoleType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createEjbRelationType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createEntityBeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createEntityGroupMappingType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createGroupType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createMessageDrivenBeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createOpenejbJarType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createQueryType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createRelationshipsType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createSessionBeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createTssType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         JarFactory.eINSTANCE.createWebServiceSecurityType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         PkgenFactory.eINSTANCE.createAutoIncrementTableType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         PkgenFactory.eINSTANCE.createCustomGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         PkgenFactory.eINSTANCE.createDatabaseGeneratedType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         PkgenFactory.eINSTANCE.createKeyGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         PkgenFactory.eINSTANCE.createSequenceTableType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         PkgenFactory.eINSTANCE.createSqlGeneratorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createCssType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createEjbLocalRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createEjbRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createGbeanLocatorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createGbeanRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createMessageDestinationType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createPortCompletionType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createPortType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createResourceEnvRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createResourceLocatorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createResourceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createServiceCompletionType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         NamingFactory.eINSTANCE.createServiceRefType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createDefaultPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createDescriptionType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createDistinguishedNameType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createLoginDomainPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createNamedUsernamePasswordCredentialType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createRealmPrincipalType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createRoleMappingsType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createRoleType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         SecurityFactory.eINSTANCE.createSecurityType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createAttributeType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createConfigurationType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createGbeanType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createPatternType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createReferencesType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createReferenceType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createServiceType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getQueryType_NoCacheFlush(),
         DeploymentFactory.eINSTANCE.createXmlAttributeType()));
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
