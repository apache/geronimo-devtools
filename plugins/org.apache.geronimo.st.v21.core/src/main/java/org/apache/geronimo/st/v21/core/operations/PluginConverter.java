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
package org.apache.geronimo.st.v21.core.operations;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.system.plugin.model.ArtifactType;
import org.apache.geronimo.system.plugin.model.AttributeType;
import org.apache.geronimo.system.plugin.model.ConfigXmlContentType;
import org.apache.geronimo.system.plugin.model.CopyFileType;
import org.apache.geronimo.system.plugin.model.DependencyType;
import org.apache.geronimo.system.plugin.model.GbeanType;
import org.apache.geronimo.system.plugin.model.HashType;
import org.apache.geronimo.system.plugin.model.ObjectFactory;
import org.apache.geronimo.system.plugin.model.LicenseType;
import org.apache.geronimo.system.plugin.model.PluginListType;
import org.apache.geronimo.system.plugin.model.PluginType;
import org.apache.geronimo.system.plugin.model.PluginArtifactType;
import org.apache.geronimo.system.plugin.model.PrerequisiteType;
import org.apache.geronimo.system.plugin.model.PropertyType;
import org.apache.geronimo.system.plugin.model.ReferenceType;

/**
 * @version $Rev$ $Date$
 */
public class PluginConverter {

    static ObjectFactory modelFactory;
    static org.apache.geronimo.jee.plugin.ObjectFactory jeeFactory;
    
    public static PluginListType jeeToModelPluginListType (org.apache.geronimo.jee.plugin.PluginListType oldPluginList) {
        modelFactory = new ObjectFactory();
        if (oldPluginList == null) return null;
        PluginListType newPluginList = modelFactory.createPluginListType();
        for (int i = 0; i < oldPluginList.getPlugin().size(); i++) {
            newPluginList.getPlugin().add(jeeToModelPluginType(oldPluginList.getPlugin().get(i)));
        }        
        newPluginList.getDefaultRepository().addAll(oldPluginList.getDefaultRepository());
        return newPluginList;
    }
    
    public static PluginType jeeToModelPluginType (org.apache.geronimo.jee.plugin.PluginType oldPlugin) {
        modelFactory = new ObjectFactory();
        if (oldPlugin == null) return null;
        PluginType newPlugin = modelFactory.createPluginType();
        newPlugin.setName(oldPlugin.getName());
        newPlugin.setCategory(oldPlugin.getCategory());
        newPlugin.setDescription(oldPlugin.getDescription());
        newPlugin.setUrl(oldPlugin.getUrl());
        newPlugin.setAuthor(oldPlugin.getAuthor());
        for (int i = 0; i < oldPlugin.getLicense().size(); i++) {
            newPlugin.getLicense().add(jeeToModelLicenseType(oldPlugin.getLicense().get(i)));
        }
        for (int i = 0; i < oldPlugin.getPluginArtifact().size(); i++) {
            newPlugin.getPluginArtifact().add(jeeToModelPluginArtifactType(oldPlugin.getPluginArtifact().get(i)));
        }
        return newPlugin;
    }

    private static PluginArtifactType jeeToModelPluginArtifactType (org.apache.geronimo.jee.plugin.PluginArtifactType oldPluginArtifact) {
        if (oldPluginArtifact==null) return null;
        PluginArtifactType newPluginArtifact = modelFactory.createPluginArtifactType();
        newPluginArtifact.setModuleId(jeeToModelArtifactType(oldPluginArtifact.getModuleId()));
        newPluginArtifact.setHash(jeeToModelHashType(oldPluginArtifact.getHash()));
        newPluginArtifact.getGeronimoVersion().addAll(oldPluginArtifact.getGeronimoVersion());
        newPluginArtifact.getJvmVersion().addAll(oldPluginArtifact.getJvmVersion());
        for (int i = 0; i < oldPluginArtifact.getPrerequisite().size(); i++) {
            newPluginArtifact.getPrerequisite().add(jeeToModelPrerequisiteType(oldPluginArtifact.getPrerequisite().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getDependency().size(); i++) {
            newPluginArtifact.getDependency().add(jeeToModelDependencyType(oldPluginArtifact.getDependency().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getObsoletes().size(); i++) {
            newPluginArtifact.getObsoletes().add(jeeToModelArtifactType(oldPluginArtifact.getObsoletes().get(i)));
        }
        newPluginArtifact.getSourceRepository().addAll(oldPluginArtifact.getSourceRepository());
        for (int i = 0; i < oldPluginArtifact.getCopyFile().size(); i++) {
            newPluginArtifact.getCopyFile().add(jeeToModelCopyFileType(oldPluginArtifact.getCopyFile().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getConfigXmlContent().size(); i++) {
            newPluginArtifact.getConfigXmlContent().add(jeeToModelConfigXmlContentType(oldPluginArtifact.getConfigXmlContent().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getArtifactAlias().size(); i++) {
            newPluginArtifact.getArtifactAlias().add(jeeToModelPropertyType(oldPluginArtifact.getArtifactAlias().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getConfigSubstitution().size(); i++) {
            newPluginArtifact.getConfigSubstitution().add(jeeToModelPropertyType(oldPluginArtifact.getConfigSubstitution().get(i)));
        }
        return newPluginArtifact;
    }

    private static ConfigXmlContentType jeeToModelConfigXmlContentType (org.apache.geronimo.jee.plugin.ConfigXmlContentType oldContent) {
        if (oldContent == null) return null;
        ConfigXmlContentType newContent = modelFactory.createConfigXmlContentType();
        newContent.setComment(oldContent.getComment());
        for (int i = 0; i < oldContent.getGbean().size(); i++) {
            newContent.getGbean().add(jeeToModelGbeanType(oldContent.getGbean().get(i)));
        }
        newContent.setCondition(oldContent.getCondition());
        newContent.setLoad(oldContent.isLoad());
        newContent.setServer(oldContent.getServer());
        return newContent;
    }

    private static GbeanType jeeToModelGbeanType (org.apache.geronimo.jee.plugin.GbeanType oldGbean) {
        if (oldGbean == null) return null;
        GbeanType newGbean = modelFactory.createGbeanType();
        newGbean.setComment(oldGbean.getComment());
        for (int i = 0; i < oldGbean.getAttributeOrReference().size(); i++) {
            if (org.apache.geronimo.jee.plugin.AttributeType.class.isInstance(oldGbean.getAttributeOrReference().get(i))) {
                newGbean.getAttributeOrReference().add(jeeToModelAttributeType((org.apache.geronimo.jee.plugin.AttributeType)oldGbean.getAttributeOrReference().get(i)));
            } else if (org.apache.geronimo.jee.plugin.ReferenceType.class.isInstance(oldGbean.getAttributeOrReference().get(i))) {
                newGbean.getAttributeOrReference().add(jeeToModelReferenceType((org.apache.geronimo.jee.plugin.ReferenceType)oldGbean.getAttributeOrReference().get(i)));
            }
        }
        newGbean.setGbeanInfo(oldGbean.getGbeanInfo());
        newGbean.setLoad(oldGbean.isLoad());
        newGbean.setName(oldGbean.getName());
        return newGbean;
    }
    
    private static PrerequisiteType jeeToModelPrerequisiteType (org.apache.geronimo.jee.plugin.PrerequisiteType oldPrerequisite) {
        if (oldPrerequisite == null) return null;
        PrerequisiteType newPrerequisite = modelFactory.createPrerequisiteType();
        newPrerequisite.setId(jeeToModelArtifactType(oldPrerequisite.getId()));
        newPrerequisite.setResourceType(oldPrerequisite.getResourceType());
        newPrerequisite.setDescription(oldPrerequisite.getDescription());
        return newPrerequisite;
    }

    private static ArtifactType jeeToModelArtifactType (org.apache.geronimo.jee.plugin.ArtifactType oldArtifact) {
        if (oldArtifact == null) return null;
        ArtifactType newArtifact = modelFactory.createArtifactType();
        newArtifact.setGroupId(oldArtifact.getGroupId());
        newArtifact.setArtifactId(oldArtifact.getArtifactId());
        newArtifact.setVersion(oldArtifact.getVersion());
        newArtifact.setType(oldArtifact.getType());
        return newArtifact;
    }

    private static DependencyType jeeToModelDependencyType (org.apache.geronimo.jee.plugin.DependencyType oldDependency) {
        if (oldDependency == null) return null;
        DependencyType newDependency = modelFactory.createDependencyType();
        newDependency.setGroupId(oldDependency.getGroupId());
        newDependency.setArtifactId(oldDependency.getArtifactId());
        newDependency.setVersion(oldDependency.getVersion());
        newDependency.setType(oldDependency.getType());
        newDependency.setStart(oldDependency.isStart());
        return newDependency;
    }

    private static ReferenceType jeeToModelReferenceType (org.apache.geronimo.jee.plugin.ReferenceType oldReference) {
        if (oldReference == null) return null;
        ReferenceType newReference = modelFactory.createReferenceType();
        for (int i = 0; i < oldReference.getPattern().size(); i++) {
            newReference.getPattern().add(jeeToModelReferencePatternType(oldReference.getPattern().get(i)));
        }
        newReference.setName(oldReference.getName());
        return newReference;
    }

    private static ReferenceType.Pattern jeeToModelReferencePatternType (org.apache.geronimo.jee.plugin.ReferenceType.Pattern oldPattern) {
        if (oldPattern == null) return null;
        ReferenceType.Pattern newPattern = modelFactory.createReferenceTypePattern();
        newPattern.setGroupId(oldPattern.getGroupId());
        newPattern.setArtifactId(oldPattern.getArtifactId());
        newPattern.setVersion(oldPattern.getVersion());
        newPattern.setType(oldPattern.getType());
        newPattern.setModule(oldPattern.getModule());
        newPattern.setName(oldPattern.getName());
        return newPattern;
    }

    private static AttributeType jeeToModelAttributeType (org.apache.geronimo.jee.plugin.AttributeType oldAttribute) {
        if (oldAttribute == null) return null;
        AttributeType newAttribute = modelFactory.createAttributeType();
        newAttribute.getContent().addAll(oldAttribute.getContent());
        newAttribute.setName(oldAttribute.getName());
        newAttribute.setNull(oldAttribute.isNull());
        newAttribute.setPropertyEditor(oldAttribute.getPropertyEditor());
        return newAttribute;
    }

    private static CopyFileType jeeToModelCopyFileType (org.apache.geronimo.jee.plugin.CopyFileType oldCopyFile) {
        if (oldCopyFile == null) return null;
        CopyFileType newCopyFile = modelFactory.createCopyFileType();
        newCopyFile.setValue(oldCopyFile.getValue());
        newCopyFile.setDestDir(oldCopyFile.getDestDir());
        newCopyFile.setRelativeTo(oldCopyFile.getRelativeTo());
        return newCopyFile;
    }

    private static PropertyType jeeToModelPropertyType (org.apache.geronimo.jee.plugin.PropertyType oldProperty) {
        if (oldProperty == null) return null;
        PropertyType newProperty = modelFactory.createPropertyType();
        newProperty.setValue(oldProperty.getValue());
        newProperty.setKey(oldProperty.getKey());
        newProperty.setServer(oldProperty.getServer());
        return newProperty;
    }

    private static LicenseType jeeToModelLicenseType (org.apache.geronimo.jee.plugin.LicenseType oldLicense) {
        if (oldLicense == null) return null;
        LicenseType newLicense = modelFactory.createLicenseType();
        newLicense.setValue(oldLicense.getValue());
        newLicense.setOsiApproved(oldLicense.isOsiApproved());
        return newLicense;
    }

    private static HashType jeeToModelHashType (org.apache.geronimo.jee.plugin.HashType oldHash) {
        if (oldHash == null) return null;
        HashType newHash = modelFactory.createHashType();
        newHash.setValue(oldHash.getValue());
        newHash.setType(oldHash.getType());
        return newHash;
    }

    public static JAXBElement pluginTypeToJAXB (org.apache.geronimo.jee.plugin.PluginType plugin) {
        return jeeFactory.createGeronimoPlugin(plugin);
    }

    public static JAXBElement pluginListTypeToJAXB (org.apache.geronimo.jee.plugin.PluginListType pluginList) {
        return jeeFactory.createGeronimoPluginList(pluginList);
    }
    
    public static org.apache.geronimo.jee.plugin.PluginListType modelToJeePluginListType (PluginListType oldPluginList) {
        jeeFactory = new org.apache.geronimo.jee.plugin.ObjectFactory();
        if (oldPluginList == null) return null;
        org.apache.geronimo.jee.plugin.PluginListType newPluginList = jeeFactory.createPluginListType();
        for (int i = 0; i < oldPluginList.getPlugin().size(); i++) {
            newPluginList.getPlugin().add(modelToJeePluginType(oldPluginList.getPlugin().get(i)));
        }        
        newPluginList.getDefaultRepository().addAll(oldPluginList.getDefaultRepository());
        return newPluginList;
    }

    public static org.apache.geronimo.jee.plugin.PluginType modelToJeePluginType (PluginType oldPlugin) {
        jeeFactory = new org.apache.geronimo.jee.plugin.ObjectFactory();
        if (oldPlugin == null) return null;
        org.apache.geronimo.jee.plugin.PluginType newPlugin = jeeFactory.createPluginType();
        newPlugin.setName(oldPlugin.getName());
        newPlugin.setCategory(oldPlugin.getCategory());
        newPlugin.setDescription(oldPlugin.getDescription());
        newPlugin.setUrl(oldPlugin.getUrl());
        newPlugin.setAuthor(oldPlugin.getAuthor());
        for (int i = 0; i < oldPlugin.getLicense().size(); i++) {
            newPlugin.getLicense().add(modelToJeeLicenseType(oldPlugin.getLicense().get(i)));
        }
        for (int i = 0; i < oldPlugin.getPluginArtifact().size(); i++) {
            newPlugin.getPluginArtifact().add(modelToJeePluginArtifactType(oldPlugin.getPluginArtifact().get(i)));
        }
        return newPlugin;
    }
    
    private static org.apache.geronimo.jee.plugin.PluginArtifactType modelToJeePluginArtifactType (PluginArtifactType oldPluginArtifact) {
        if (oldPluginArtifact==null) return null;
        org.apache.geronimo.jee.plugin.PluginArtifactType newPluginArtifact = jeeFactory.createPluginArtifactType();
        newPluginArtifact.setModuleId(modelToJeeArtifactType(oldPluginArtifact.getModuleId()));
        newPluginArtifact.setHash(modelToJeeHashType(oldPluginArtifact.getHash()));
        newPluginArtifact.getGeronimoVersion().addAll(oldPluginArtifact.getGeronimoVersion());
        newPluginArtifact.getJvmVersion().addAll(oldPluginArtifact.getJvmVersion());
        for (int i = 0; i < oldPluginArtifact.getPrerequisite().size(); i++) {
            newPluginArtifact.getPrerequisite().add(modelToJeePrerequisiteType(oldPluginArtifact.getPrerequisite().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getDependency().size(); i++) {
            newPluginArtifact.getDependency().add(modelToJeeDependencyType(oldPluginArtifact.getDependency().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getObsoletes().size(); i++) {
            newPluginArtifact.getObsoletes().add(modelToJeeArtifactType(oldPluginArtifact.getObsoletes().get(i)));
        }
        newPluginArtifact.getSourceRepository().addAll(oldPluginArtifact.getSourceRepository());
        for (int i = 0; i < oldPluginArtifact.getCopyFile().size(); i++) {
            newPluginArtifact.getCopyFile().add(modelToJeeCopyFileType(oldPluginArtifact.getCopyFile().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getConfigXmlContent().size(); i++) {
            newPluginArtifact.getConfigXmlContent().add(modelToJeeConfigXmlContentType(oldPluginArtifact.getConfigXmlContent().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getArtifactAlias().size(); i++) {
            newPluginArtifact.getArtifactAlias().add(modelToJeePropertyType(oldPluginArtifact.getArtifactAlias().get(i)));
        }
        for (int i = 0; i < oldPluginArtifact.getConfigSubstitution().size(); i++) {
            newPluginArtifact.getConfigSubstitution().add(modelToJeePropertyType(oldPluginArtifact.getConfigSubstitution().get(i)));
        }
        return newPluginArtifact;
    }

    private static org.apache.geronimo.jee.plugin.ConfigXmlContentType modelToJeeConfigXmlContentType (ConfigXmlContentType oldContent) {
        if (oldContent == null) return null;
        org.apache.geronimo.jee.plugin.ConfigXmlContentType newContent = jeeFactory.createConfigXmlContentType();
        newContent.setComment(oldContent.getComment());
        for (int i = 0; i < oldContent.getGbean().size(); i++) {
            newContent.getGbean().add(modelToJeeGbeanType(oldContent.getGbean().get(i)));
        }
        newContent.setCondition(oldContent.getCondition());
        newContent.setLoad(oldContent.isLoad());
        newContent.setServer(oldContent.getServer());
        return newContent;
    }

    private static org.apache.geronimo.jee.plugin.GbeanType modelToJeeGbeanType (GbeanType oldGbean) {
        if (oldGbean == null) return null;
        org.apache.geronimo.jee.plugin.GbeanType newGbean = jeeFactory.createGbeanType();
        newGbean.setComment(oldGbean.getComment());
        for (int i = 0; i < oldGbean.getAttributeOrReference().size(); i++) {
            if (AttributeType.class.isInstance(oldGbean.getAttributeOrReference().get(i))) {
                newGbean.getAttributeOrReference().add(modelToJeeAttributeType((AttributeType)oldGbean.getAttributeOrReference().get(i)));
            } else if (ReferenceType.class.isInstance(oldGbean.getAttributeOrReference().get(i))) {
                newGbean.getAttributeOrReference().add(modelToJeeReferenceType((ReferenceType)oldGbean.getAttributeOrReference().get(i)));
            }
        }
        newGbean.setGbeanInfo(oldGbean.getGbeanInfo());
        newGbean.setLoad(oldGbean.isLoad());
        newGbean.setName(oldGbean.getName());
        return newGbean;
    }
    
    private static org.apache.geronimo.jee.plugin.PrerequisiteType modelToJeePrerequisiteType (PrerequisiteType oldPrerequisite) {
        if (oldPrerequisite == null) return null;
        org.apache.geronimo.jee.plugin.PrerequisiteType newPrerequisite = jeeFactory.createPrerequisiteType();
        newPrerequisite.setId(modelToJeeArtifactType(oldPrerequisite.getId()));
        newPrerequisite.setResourceType(oldPrerequisite.getResourceType());
        newPrerequisite.setDescription(oldPrerequisite.getDescription());
        return newPrerequisite;
    }

    private static org.apache.geronimo.jee.plugin.ArtifactType modelToJeeArtifactType (ArtifactType oldArtifact) {
        if (oldArtifact == null) return null;
        org.apache.geronimo.jee.plugin.ArtifactType newArtifact = jeeFactory.createArtifactType();
        newArtifact.setGroupId(oldArtifact.getGroupId());
        newArtifact.setArtifactId(oldArtifact.getArtifactId());
        newArtifact.setVersion(oldArtifact.getVersion());
        newArtifact.setType(oldArtifact.getType());
        return newArtifact;
    }

    private static org.apache.geronimo.jee.plugin.DependencyType modelToJeeDependencyType (DependencyType oldDependency) {
        if (oldDependency == null) return null;
        org.apache.geronimo.jee.plugin.DependencyType newDependency = jeeFactory.createDependencyType();
        newDependency.setGroupId(oldDependency.getGroupId());
        newDependency.setArtifactId(oldDependency.getArtifactId());
        newDependency.setVersion(oldDependency.getVersion());
        newDependency.setType(oldDependency.getType());
        newDependency.setStart(oldDependency.isStart());
        return newDependency;
    }

    private static org.apache.geronimo.jee.plugin.ReferenceType modelToJeeReferenceType (ReferenceType oldReference) {
        if (oldReference == null) return null;
        org.apache.geronimo.jee.plugin.ReferenceType newReference = jeeFactory.createReferenceType();
        for (int i = 0; i < oldReference.getPattern().size(); i++) {
            newReference.getPattern().add(modelToJeeReferencePatternType(oldReference.getPattern().get(i)));
        }
        newReference.setName(oldReference.getName());
        return newReference;
    }

    private static org.apache.geronimo.jee.plugin.ReferenceType.Pattern modelToJeeReferencePatternType (ReferenceType.Pattern oldPattern) {
        if (oldPattern == null) return null;
        org.apache.geronimo.jee.plugin.ReferenceType.Pattern newPattern = jeeFactory.createReferenceTypePattern();
        newPattern.setGroupId(oldPattern.getGroupId());
        newPattern.setArtifactId(oldPattern.getArtifactId());
        newPattern.setVersion(oldPattern.getVersion());
        newPattern.setType(oldPattern.getType());
        newPattern.setModule(oldPattern.getModule());
        newPattern.setName(oldPattern.getName());
        return newPattern;
    }

    private static org.apache.geronimo.jee.plugin.AttributeType modelToJeeAttributeType (AttributeType oldAttribute) {
        if (oldAttribute == null) return null;
        org.apache.geronimo.jee.plugin.AttributeType newAttribute = jeeFactory.createAttributeType();
        newAttribute.getContent().addAll(oldAttribute.getContent());
        newAttribute.setName(oldAttribute.getName());
        newAttribute.setNull(oldAttribute.isNull());
        newAttribute.setPropertyEditor(oldAttribute.getPropertyEditor());
        return newAttribute;
    }

    private static org.apache.geronimo.jee.plugin.CopyFileType modelToJeeCopyFileType (CopyFileType oldCopyFile) {
        if (oldCopyFile == null) return null;
        org.apache.geronimo.jee.plugin.CopyFileType newCopyFile = jeeFactory.createCopyFileType();
        newCopyFile.setValue(oldCopyFile.getValue());
        newCopyFile.setDestDir(oldCopyFile.getDestDir());
        newCopyFile.setRelativeTo(oldCopyFile.getRelativeTo());
        return newCopyFile;
    }

    private static org.apache.geronimo.jee.plugin.PropertyType modelToJeePropertyType (PropertyType oldProperty) {
        if (oldProperty == null) return null;
        org.apache.geronimo.jee.plugin.PropertyType newProperty = jeeFactory.createPropertyType();
        newProperty.setValue(oldProperty.getValue());
        newProperty.setKey(oldProperty.getKey());
        newProperty.setServer(oldProperty.getServer());
        return newProperty;
    }

    private static org.apache.geronimo.jee.plugin.LicenseType modelToJeeLicenseType (LicenseType oldLicense) {
        if (oldLicense == null) return null;
        org.apache.geronimo.jee.plugin.LicenseType newLicense = jeeFactory.createLicenseType();
        newLicense.setValue(oldLicense.getValue());
        newLicense.setOsiApproved(oldLicense.isOsiApproved());
        return newLicense;
    }

    private static org.apache.geronimo.jee.plugin.HashType modelToJeeHashType (HashType oldHash) {
        if (oldHash == null) return null;
        org.apache.geronimo.jee.plugin.HashType newHash = jeeFactory.createHashType();
        newHash.setValue(oldHash.getValue());
        newHash.setType(oldHash.getType());
        return newHash;
    }
//jeeFactory = new org.apache.geronimo.jee.plugin.ObjectFactory()
}
