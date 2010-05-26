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
package org.apache.geronimo.osgi.blueprint;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *                 
 *                 The Tinlined-service type is used for inlined (i.e. non top level)
 *                 <service> elements.
 *                 
 *             
 * 
 * <p>Java class for Tinlined-service complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tinlined-service">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tservice">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}GserviceElements"/>
 *       &lt;/sequence>
 *       &lt;attribute name="auto-export" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TautoExportModes" default="disabled" />
 *       &lt;attribute name="depends-on" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TdependsOn" />
 *       &lt;attribute name="interface" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tclass" />
 *       &lt;attribute name="ranking" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *       &lt;attribute name="ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tinlined-service")
@XmlRootElement(name = "inlinedService")
public class TinlinedService extends Tservice {
}
