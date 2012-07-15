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

package org.apache.geronimo.jee.plugins;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.xml.ns.plugins_1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GeronimoPlugin_QNAME = new QName("http://geronimo.apache.org/xml/ns/plugins-1.3", "geronimo-plugin");
    private final static QName _GeronimoPluginList_QNAME = new QName("http://geronimo.apache.org/xml/ns/plugins-1.3", "geronimo-plugin-list");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.xml.ns.plugins_1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PluginArtifact }
     * 
     */
    public PluginArtifact createPluginArtifact() {
        return new PluginArtifact();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link Hash }
     * 
     */
    public Hash createHash() {
        return new Hash();
    }

    /**
     * Create an instance of {@link Plugin }
     * 
     */
    public Plugin createPlugin() {
        return new Plugin();
    }

    /**
     * Create an instance of {@link ConfigXmlContent }
     * 
     */
    public ConfigXmlContent createConfigXmlContent() {
        return new ConfigXmlContent();
    }

    /**
     * Create an instance of {@link Dependency }
     * 
     */
    public Dependency createDependency() {
        return new Dependency();
    }

    /**
     * Create an instance of {@link PluginList }
     * 
     */
    public PluginList createPluginList() {
        return new PluginList();
    }

    /**
     * Create an instance of {@link Prerequisite }
     * 
     */
    public Prerequisite createPrerequisite() {
        return new Prerequisite();
    }

    /**
     * Create an instance of {@link License }
     * 
     */
    public License createLicense() {
        return new License();
    }

    /**
     * Create an instance of {@link Artifact }
     * 
     */
    public Artifact createArtifact() {
        return new Artifact();
    }

    /**
     * Create an instance of {@link CopyFile }
     * 
     */
    public CopyFile createCopyFile() {
        return new CopyFile();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Plugin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/plugins-1.3", name = "geronimo-plugin")
    public JAXBElement<Plugin> createGeronimoPlugin(Plugin value) {
        return new JAXBElement<Plugin>(_GeronimoPlugin_QNAME, Plugin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PluginList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://geronimo.apache.org/xml/ns/plugins-1.3", name = "geronimo-plugin-list")
    public JAXBElement<PluginList> createGeronimoPluginList(PluginList value) {
        return new JAXBElement<PluginList>(_GeronimoPluginList_QNAME, PluginList.class, null, value);
    }

}
