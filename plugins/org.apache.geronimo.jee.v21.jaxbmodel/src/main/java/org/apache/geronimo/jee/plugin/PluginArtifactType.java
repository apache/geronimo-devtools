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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.geronimo.jee.plugin.ArtifactType;
import org.apache.geronimo.jee.plugin.ConfigXmlContentType;
import org.apache.geronimo.jee.plugin.CopyFileType;
import org.apache.geronimo.jee.plugin.DependencyType;
import org.apache.geronimo.jee.plugin.HashType;
import org.apache.geronimo.jee.plugin.PrerequisiteType;
import org.apache.geronimo.jee.plugin.PropertyType;


/**
 * <p>Java class for pluginArtifactType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pluginArtifactType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="module-id" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}artifactType"/>
 *         &lt;element name="hash" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}hashType" minOccurs="0"/>
 *         &lt;element name="geronimo-version" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="jvm-version" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="prerequisite" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}prerequisiteType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dependency" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}dependencyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="obsoletes" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}artifactType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="source-repository" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="copy-file" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}copy-fileType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="config-xml-content" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}config-xml-contentType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="artifact-alias" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}propertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="config-substitution" type="{http://geronimo.apache.org/xml/ns/plugins-1.3}propertyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 *  @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pluginArtifactType", propOrder = {
    "moduleId",
    "hash",
    "geronimoVersion",
    "jvmVersion",
    "prerequisite",
    "dependency",
    "obsoletes",
    "sourceRepository",
    "copyFile",
    "configXmlContent",
    "artifactAlias",
    "configSubstitution"
})
public class PluginArtifactType
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "module-id", required = true)
    protected ArtifactType moduleId;
    protected HashType hash;
    @XmlElement(name = "geronimo-version")
    protected List<String> geronimoVersion;
    @XmlElement(name = "jvm-version")
    protected List<String> jvmVersion;
    protected List<PrerequisiteType> prerequisite;
    protected List<DependencyType> dependency;
    protected List<ArtifactType> obsoletes;
    @XmlElement(name = "source-repository")
    protected List<String> sourceRepository;
    @XmlElement(name = "copy-file")
    protected List<CopyFileType> copyFile;
    @XmlElement(name = "config-xml-content")
    protected List<ConfigXmlContentType> configXmlContent;
    @XmlElement(name = "artifact-alias")
    protected List<PropertyType> artifactAlias;
    @XmlElement(name = "config-substitution")
    protected List<PropertyType> configSubstitution;

    /**
     * Gets the value of the moduleId property.
     * 
     * @return
     *     possible object is
     *     {@link ArtifactType }
     *     
     */
    public ArtifactType getModuleId() {
        return moduleId;
    }

    /**
     * Sets the value of the moduleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArtifactType }
     *     
     */
    public void setModuleId(ArtifactType value) {
        this.moduleId = value;
    }

    /**
     * Gets the value of the hash property.
     * 
     * @return
     *     possible object is
     *     {@link HashType }
     *     
     */
    public HashType getHash() {
        return hash;
    }

    /**
     * Sets the value of the hash property.
     * 
     * @param value
     *     allowed object is
     *     {@link HashType }
     *     
     */
    public void setHash(HashType value) {
        this.hash = value;
    }

    /**
     * Gets the value of the geronimoVersion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the geronimoVersion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGeronimoVersion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getGeronimoVersion() {
        if (geronimoVersion == null) {
            geronimoVersion = new ArrayList<String>();
        }
        return this.geronimoVersion;
    }

    /**
     * Gets the value of the jvmVersion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the jvmVersion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJvmVersion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getJvmVersion() {
        if (jvmVersion == null) {
            jvmVersion = new ArrayList<String>();
        }
        return this.jvmVersion;
    }

    /**
     * Gets the value of the prerequisite property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the prerequisite property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrerequisite().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PrerequisiteType }
     * 
     * 
     */
    public List<PrerequisiteType> getPrerequisite() {
        if (prerequisite == null) {
            prerequisite = new ArrayList<PrerequisiteType>();
        }
        return this.prerequisite;
    }

    /**
     * Gets the value of the dependency property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dependency property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDependency().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DependencyType }
     * 
     * 
     */
    public List<DependencyType> getDependency() {
        if (dependency == null) {
            dependency = new ArrayList<DependencyType>();
        }
        return this.dependency;
    }

    /**
     * Gets the value of the obsoletes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the obsoletes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getObsoletes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArtifactType }
     * 
     * 
     */
    public List<ArtifactType> getObsoletes() {
        if (obsoletes == null) {
            obsoletes = new ArrayList<ArtifactType>();
        }
        return this.obsoletes;
    }

    /**
     * Gets the value of the sourceRepository property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceRepository property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceRepository().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSourceRepository() {
        if (sourceRepository == null) {
            sourceRepository = new ArrayList<String>();
        }
        return this.sourceRepository;
    }

    /**
     * Gets the value of the copyFile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the copyFile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCopyFile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CopyFileType }
     * 
     * 
     */
    public List<CopyFileType> getCopyFile() {
        if (copyFile == null) {
            copyFile = new ArrayList<CopyFileType>();
        }
        return this.copyFile;
    }

    /**
     * Gets the value of the configXmlContent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configXmlContent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigXmlContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConfigXmlContentType }
     * 
     * 
     */
    public List<ConfigXmlContentType> getConfigXmlContent() {
        if (configXmlContent == null) {
            configXmlContent = new ArrayList<ConfigXmlContentType>();
        }
        return this.configXmlContent;
    }

    /**
     * Gets the value of the artifactAlias property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the artifactAlias property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArtifactAlias().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     * 
     * 
     */
    public List<PropertyType> getArtifactAlias() {
        if (artifactAlias == null) {
            artifactAlias = new ArrayList<PropertyType>();
        }
        return this.artifactAlias;
    }

    /**
     * Gets the value of the configSubstitution property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the configSubstitution property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfigSubstitution().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     * 
     * 
     */
    public List<PropertyType> getConfigSubstitution() {
        if (configSubstitution == null) {
            configSubstitution = new ArrayList<PropertyType>();
        }
        return this.configSubstitution;
    }

}
