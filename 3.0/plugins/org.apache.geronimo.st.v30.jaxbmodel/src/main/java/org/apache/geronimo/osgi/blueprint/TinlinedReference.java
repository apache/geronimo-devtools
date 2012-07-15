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
 *                 The Tinlined-reference type is used for inlined (i.e. non top level)
 *                 <reference> elements.
 *                 
 *             
 * 
 * <p>Java class for Tinlined-reference complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tinlined-reference">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Treference">
 *       &lt;sequence>
 *         &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}GserviceReferenceElements"/>
 *         &lt;any/>
 *       &lt;/sequence>
 *       &lt;attribute name="availability" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tavailability" />
 *       &lt;attribute name="component-name" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="depends-on" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TdependsOn" />
 *       &lt;attribute name="filter" type="{http://www.w3.org/2001/XMLSchema}normalizedString" />
 *       &lt;attribute name="interface" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tclass" />
 *       &lt;attribute name="timeout" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Ttimeout" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tinlined-reference")
@XmlRootElement(name = "inlinedReference")
public class TinlinedReference extends Treference {
}
