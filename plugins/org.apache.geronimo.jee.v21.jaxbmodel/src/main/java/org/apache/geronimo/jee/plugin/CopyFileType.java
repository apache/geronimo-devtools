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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 *
 *                 The name of a file in the plugin archive that should be copied into
 *                 the server installation tree somewhere when the plugin is installed.
 *                 There may be a path component (relative to the root of the plugin
 *                 archive), though that will not be used to construct the destination
 *                 location. For example:
 *
 *
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;copy-file xmlns:atts="http://geronimo.apache.org/xml/ns/attributes-1.2" xmlns:jaxb="http://java.sun.com/xml/ns/jaxb" xmlns:list="http://geronimo.apache.org/xml/ns/plugins-1.3" xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc" xmlns:xs="http://www.w3.org/2001/XMLSchema" dest-dir="var/security/keystores" relative-to="server"&gt; *                     resources/keystores/my-keystore *                 &lt;/copy-file&gt;
 * </pre>
 *
 *
 *                 This will copy the file resources/keystores/my-keystore to e.g.
 *                 var/security/keystores/my-keystore
 *
 *
 * <p>Java class for copy-fileType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="copy-fileType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="dest-dir" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="relative-to" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *             &lt;enumeration value="geronimo"/>
 *             &lt;enumeration value="server"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *  @version $Rev$ $Date$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "copy-fileType", propOrder = {
    "value"
})
public class CopyFileType
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlValue
    protected String value;
    @XmlAttribute(name = "dest-dir", required = true)
    protected String destDir;
    @XmlAttribute(name = "relative-to", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String relativeTo;

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the destDir property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDestDir() {
        return destDir;
    }

    /**
     * Sets the value of the destDir property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDestDir(String value) {
        this.destDir = value;
    }

    /**
     * Gets the value of the relativeTo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getRelativeTo() {
        return relativeTo;
    }

    /**
     * Sets the value of the relativeTo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setRelativeTo(String value) {
        this.relativeTo = value;
    }

}
