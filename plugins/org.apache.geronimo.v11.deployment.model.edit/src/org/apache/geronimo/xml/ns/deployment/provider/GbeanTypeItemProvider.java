/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.xml.ns.deployment.provider;


import java.util.Collection;
import java.util.List;

import org.apache.geronimo.v11.deployment.model.edit.GeronimoEMFEditPlugin;
import org.apache.geronimo.xml.ns.deployment.DeploymentFactory;
import org.apache.geronimo.xml.ns.deployment.DeploymentPackage;
import org.apache.geronimo.xml.ns.deployment.GbeanType;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link org.apache.geronimo.xml.ns.deployment.GbeanType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated NOT
 *
 * $Rev$ $Date$
 */
public class GbeanTypeItemProvider
  extends ItemProviderAdapter
  implements	
    IEditingDomainItemProvider,	
    IStructuredItemContentProvider,	
    ITreeItemContentProvider,	
    IItemLabelProvider,	
    IItemPropertySource,
    ITableItemLabelProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GbeanTypeItemProvider(AdapterFactory adapterFactory)
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

      addClassPropertyDescriptor(object);
      addNamePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Class feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addClassPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_GbeanType_class_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanType_class_feature", "_UI_GbeanType_type"),
         DeploymentPackage.Literals.GBEAN_TYPE__CLASS,
         true,
         false,
         false,
         ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
         null,
         null));
  }

  /**
   * This adds a property descriptor for the Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add
      (createItemPropertyDescriptor
        (((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
         getResourceLocator(),
         getString("_UI_GbeanType_name_feature"),
         getString("_UI_PropertyDescriptor_description", "_UI_GbeanType_name_feature", "_UI_GbeanType_type"),
         DeploymentPackage.Literals.GBEAN_TYPE__NAME,
         true,
         false,
         false,
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
      childrenFeatures.add(DeploymentPackage.Literals.GBEAN_TYPE__GROUP);
    }
    return childrenFeatures;
  }

  /**
   * This returns GbeanType.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/GbeanType"));
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getText(Object object)
  {
    String label = ((GbeanType)object).getName();
    return label == null || label.length() == 0 ?
      getString("_UI_GbeanType_type") :
      getString("_UI_GbeanType_type") + " " + label;
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

    switch (notification.getFeatureID(GbeanType.class))
    {
      case DeploymentPackage.GBEAN_TYPE__CLASS:
      case DeploymentPackage.GBEAN_TYPE__NAME:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case DeploymentPackage.GBEAN_TYPE__GROUP:
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
        (DeploymentPackage.Literals.GBEAN_TYPE__GROUP,
         FeatureMapUtil.createEntry
          (DeploymentPackage.Literals.GBEAN_TYPE__ATTRIBUTE,
           DeploymentFactory.eINSTANCE.createAttributeType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.Literals.GBEAN_TYPE__GROUP,
         FeatureMapUtil.createEntry
          (DeploymentPackage.Literals.GBEAN_TYPE__XML_ATTRIBUTE,
           DeploymentFactory.eINSTANCE.createXmlAttributeType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.Literals.GBEAN_TYPE__GROUP,
         FeatureMapUtil.createEntry
          (DeploymentPackage.Literals.GBEAN_TYPE__REFERENCE,
           DeploymentFactory.eINSTANCE.createReferenceType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.Literals.GBEAN_TYPE__GROUP,
         FeatureMapUtil.createEntry
          (DeploymentPackage.Literals.GBEAN_TYPE__REFERENCES,
           DeploymentFactory.eINSTANCE.createReferencesType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.Literals.GBEAN_TYPE__GROUP,
         FeatureMapUtil.createEntry
          (DeploymentPackage.Literals.GBEAN_TYPE__XML_REFERENCE,
           DeploymentFactory.eINSTANCE.createXmlAttributeType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.Literals.GBEAN_TYPE__GROUP,
         FeatureMapUtil.createEntry
          (DeploymentPackage.Literals.GBEAN_TYPE__DEPENDENCY,
           DeploymentFactory.eINSTANCE.createPatternType())));

    newChildDescriptors.add
      (createChildParameter
        (DeploymentPackage.Literals.GBEAN_TYPE__GROUP,
         FeatureMapUtil.createEntry
          (DeploymentPackage.Literals.GBEAN_TYPE__DEPENDENCY,
           DeploymentFactory.eINSTANCE.createReferenceType())));
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
      childFeature == DeploymentPackage.Literals.GBEAN_TYPE__XML_ATTRIBUTE ||
      childFeature == DeploymentPackage.Literals.GBEAN_TYPE__XML_REFERENCE ||
      childFeature == DeploymentPackage.Literals.GBEAN_TYPE__REFERENCE ||
      childFeature == DeploymentPackage.Literals.GBEAN_TYPE__DEPENDENCY;

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
    return GeronimoEMFEditPlugin.INSTANCE;
  }
  
  public String getColumnText(Object object, int columnIndex) {
	  GbeanType o = (GbeanType) object;
	  switch (columnIndex) {
		case 0:
			return o.getName();
		case 1:
			return o.getClass_();
	  }
	  return "";
  }

  public Object getColumnImage(Object object, int columnIndex) {
	  if (columnIndex == 0)
		  return getImage(object);
	  return null;
  }

}
