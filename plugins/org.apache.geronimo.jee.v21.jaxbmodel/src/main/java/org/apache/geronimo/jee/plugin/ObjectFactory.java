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
package org.apache.geronimo.jee.plugin;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import org.apache.geronimo.jee.plugin.ArtifactType;
import org.apache.geronimo.jee.plugin.AttributeType;
import org.apache.geronimo.jee.plugin.AttributesType;
import org.apache.geronimo.jee.plugin.ConfigXmlContentType;
import org.apache.geronimo.jee.plugin.CopyFileType;
import org.apache.geronimo.jee.plugin.DependencyType;
import org.apache.geronimo.jee.plugin.GbeanType;
import org.apache.geronimo.jee.plugin.HashType;
import org.apache.geronimo.jee.plugin.LicenseType;
import org.apache.geronimo.jee.plugin.ModuleType;
import org.apache.geronimo.jee.plugin.PluginArtifactType;
import org.apache.geronimo.jee.plugin.PluginListType;
import org.apache.geronimo.jee.plugin.PluginType;
import org.apache.geronimo.jee.plugin.PrerequisiteType;
import org.apache.geronimo.jee.plugin.PropertyType;
import org.apache.geronimo.jee.plugin.ReferenceType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.system.plugin.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 *  @version $Rev$ $Date$
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Comment_QNAME = new QName("http://geronimo.apache.org/xml/ns/attributes-1.2", "comment");
    private final static QName _GeronimoPlugin_QNAME = new QName("http://geronimo.apache.org/xml/ns/plugins-1.3", "geronimo-plugin");
    private final static QName _GeronimoPluginList_QNAME = new QName("http://geronimo.apache.org/xml/ns/plugins-1.3", "geronimo-plugin-list");
    private final static QName _Gbean_QNAME = new QName("http://geronimo.apache.org/xml/ns/attributes-1.2", "gbean");
    private final static QName _Attribute_QNAME = new QName("http://geronimo.apache.org/xml/ns/attributes-1.2", "attribute");
    private final static QName _Attributes_QNAME = new QName("http://geronimo.apache.org/xml/ns/attributes-1.2", "attributes");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.system.plugin.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LicenseType }
     * 
     */
    public LicenseType createLicenseType() {
        return new LicenseType();
    }

    /**
     * Create an instance of {@link GbeanType }
     * 
     */
    public GbeanType createGbeanType() {
        return new GbeanType();
    }

    /**
     * Create an instance of {@link ReferenceType }
     * 
     */
    public ReferenceType createReferenceType() {
        return new ReferenceType();
    }

    /**
     * Create an instance of {@link ReferenceType.Pattern }
     * 
     */
    public ReferenceType.Pattern createReferenceTypePattern() {
        return new ReferenceType.Pattern();
    }

    /**
     * Create an instance of {@link ConfigXmlContentType }
     * 
     */
    public ConfigXmlContentType createConfigXmlContentType() {
        return new ConfigXmlContentType();
    }

    /**
     * Create an instance of {@link PluginType }
     * 
     */
    public PluginType createPluginType() {
        return new PluginType();
    }

    /**
     * Create an instance of {@link PropertyType }
     * 
     */
    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    /**
     * Create an instance of {@link ModuleType }
     * 
     */
    public ModuleType createModuleType() {
        return new ModuleType();
    }

    /**
     * Create an instance of {@link PrerequisiteType }
     * 
     */
    public PrerequisiteType createPrerequisiteType() {
        return new PrerequisiteType();
    }

    /**
     * Create an instance of {@link DependencyType }
     * 
     */
    public DependencyType createDependencyType() {
        return new DependencyType();
    }

    /**
     * Create an instance of {@link HashType }
     * 
     */
    public HashType createHashType() {
        return new HashType();
    }

    /**
     * Create an instance of {@link AttributesType }
     * 
     */
    public AttributesType createAttributesType() {
        return new AttributesType();
    }

    /**
     * Create an instance of {@link PluginListType }
     * 
     */
    public PluginListType createPluginListType() {
        return new PluginListType();
    }

    /**
     * Create an instance of {@link CopyFileType }
     * 
     */
    public CopyFileType createCopyFileType() {
        return new CopyFileType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link ArtifactType }
     * 
     */
    public ArtifactType createArtifactType() {
        return new ArtifactType();
    }

    /**
     * Create an instance of {@link PluginArtifactType }
     * 
     */
    public PluginArtifactType createPluginArtifactType() {
        return new PluginArtifactType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/attributes-1.2", name = "comment")
    public JAXBElement<String> createComment(String value) {
        return new JAXBElement<String>(_Comment_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PluginType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/plugins-1.3", name = "geronimo-plugin")
    public JAXBElement<PluginType> createGeronimoPlugin(PluginType value) {
        return new JAXBElement<PluginType>(_GeronimoPlugin_QNAME, PluginType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GbeanType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/attributes-1.2", name = "gbean")
    public JAXBElement<GbeanType> createGbean(GbeanType value) {
        return new JAXBElement<GbeanType>(_Gbean_QNAME, GbeanType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PluginListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/plugins-1.3", name = "geronimo-plugin-list")
    public JAXBElement<PluginListType> createGeronimoPluginList(PluginListType value) {
        return new JAXBElement<PluginListType>(_GeronimoPluginList_QNAME, PluginListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/attributes-1.2", name = "attribute")
    public JAXBElement<AttributeType> createAttribute(AttributeType value) {
        return new JAXBElement<AttributeType>(_Attribute_QNAME, AttributeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributesType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/attributes-1.2", name = "attributes")
    public JAXBElement<AttributesType> createAttributes(AttributesType value) {
        return new JAXBElement<AttributesType>(_Attributes_QNAME, AttributesType.class, null, value);
    }

}
