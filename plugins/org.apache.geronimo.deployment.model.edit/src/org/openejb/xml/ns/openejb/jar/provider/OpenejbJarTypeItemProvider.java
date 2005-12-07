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
import org.openejb.xml.ns.openejb.jar.OpenejbJarType;

/**
 * This is the item provider adapter for a {@link org.openejb.xml.ns.openejb.jar.OpenejbJarType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OpenejbJarTypeItemProvider
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
  public OpenejbJarTypeItemProvider(AdapterFactory adapterFactory)
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

      addEjbQlCompilerFactoryPropertyDescriptor(object);
      addDbSyntaxFactoryPropertyDescriptor(object);
      addConfigIdPropertyDescriptor(object);
      addInverseClassloadingPropertyDescriptor(object);
      addParentIdPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Ejb Ql Compiler Factory feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addEjbQlCompilerFactoryPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_OpenejbJarType_ejbQlCompilerFactory_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_OpenejbJarType_ejbQlCompilerFactory_feature", "_UI_OpenejbJarType_type"),
         JarPackage.eINSTANCE.getOpenejbJarType_EjbQlCompilerFactory(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Db Syntax Factory feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addDbSyntaxFactoryPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_OpenejbJarType_dbSyntaxFactory_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_OpenejbJarType_dbSyntaxFactory_feature", "_UI_OpenejbJarType_type"),
         JarPackage.eINSTANCE.getOpenejbJarType_DbSyntaxFactory(),
         true,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
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
         getString("_UI_OpenejbJarType_configId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_OpenejbJarType_configId_feature", "_UI_OpenejbJarType_type"),
         JarPackage.eINSTANCE.getOpenejbJarType_ConfigId(),
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
         getString("_UI_OpenejbJarType_inverseClassloading_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_OpenejbJarType_inverseClassloading_feature", "_UI_OpenejbJarType_type"),
         JarPackage.eINSTANCE.getOpenejbJarType_InverseClassloading(),
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
         getString("_UI_OpenejbJarType_parentId_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_OpenejbJarType_parentId_feature", "_UI_OpenejbJarType_type"),
         JarPackage.eINSTANCE.getOpenejbJarType_ParentId(),
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
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_Import());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_Dependency());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_HiddenClasses());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_NonOverridableClasses());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_CmpConnectionFactory());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_EnforceForeignKeyConstraints());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_EnterpriseBeans());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_Relationships());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_MessageDestination());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_Security());
      childrenFeatures.add(JarPackage.eINSTANCE.getOpenejbJarType_Gbean());
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
   * This returns OpenejbJarType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return getResourceLocator().getImage("full/obj16/OpenejbJarType");
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((OpenejbJarType)object).getEjbQlCompilerFactory();
    return label == null || label.length() == 0 ?
      getString("_UI_OpenejbJarType_type") :
      getString("_UI_OpenejbJarType_type") + " " + label;
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

    switch (notification.getFeatureID(OpenejbJarType.class))
    {
      case JarPackage.OPENEJB_JAR_TYPE__EJB_QL_COMPILER_FACTORY:
      case JarPackage.OPENEJB_JAR_TYPE__DB_SYNTAX_FACTORY:
      case JarPackage.OPENEJB_JAR_TYPE__CONFIG_ID:
      case JarPackage.OPENEJB_JAR_TYPE__INVERSE_CLASSLOADING:
      case JarPackage.OPENEJB_JAR_TYPE__PARENT_ID:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case JarPackage.OPENEJB_JAR_TYPE__IMPORT:
      case JarPackage.OPENEJB_JAR_TYPE__DEPENDENCY:
      case JarPackage.OPENEJB_JAR_TYPE__HIDDEN_CLASSES:
      case JarPackage.OPENEJB_JAR_TYPE__NON_OVERRIDABLE_CLASSES:
      case JarPackage.OPENEJB_JAR_TYPE__CMP_CONNECTION_FACTORY:
      case JarPackage.OPENEJB_JAR_TYPE__ENFORCE_FOREIGN_KEY_CONSTRAINTS:
      case JarPackage.OPENEJB_JAR_TYPE__ENTERPRISE_BEANS:
      case JarPackage.OPENEJB_JAR_TYPE__RELATIONSHIPS:
      case JarPackage.OPENEJB_JAR_TYPE__MESSAGE_DESTINATION:
      case JarPackage.OPENEJB_JAR_TYPE__SECURITY:
      case JarPackage.OPENEJB_JAR_TYPE__GBEAN:
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
        (JarPackage.eINSTANCE.getOpenejbJarType_Import(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_Dependency(),
         DeploymentFactory.eINSTANCE.createDependencyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_HiddenClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_NonOverridableClasses(),
         DeploymentFactory.eINSTANCE.createClassFilterType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_CmpConnectionFactory(),
         NamingFactory.eINSTANCE.createResourceLocatorType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_EnforceForeignKeyConstraints(),
         JarFactory.eINSTANCE.createEmptyType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_EnterpriseBeans(),
         JarFactory.eINSTANCE.createEnterpriseBeansType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_Relationships(),
         JarFactory.eINSTANCE.createRelationshipsType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_MessageDestination(),
         NamingFactory.eINSTANCE.createMessageDestinationType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_Security(),
         SecurityFactory.eINSTANCE.createSecurityType()));

    newChildDescriptors.add
      (createChildParameter
        (JarPackage.eINSTANCE.getOpenejbJarType_Gbean(),
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
      childFeature == JarPackage.eINSTANCE.getOpenejbJarType_Import() ||
      childFeature == JarPackage.eINSTANCE.getOpenejbJarType_Dependency() ||
      childFeature == JarPackage.eINSTANCE.getOpenejbJarType_HiddenClasses() ||
      childFeature == JarPackage.eINSTANCE.getOpenejbJarType_NonOverridableClasses();

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
    return OpenejbjarEditPlugin.INSTANCE;
  }

}
