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
 *                 The Tinlined-bean type is used for inlined (i.e. non top level)
 *                 <bean> elements.  Those elements have some restrictions on
 *                 the attributes that can be used to define them.
 *                 
 *             
 * 
 * <p>Java class for Tinlined-bean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Tinlined-bean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tbean">
 *       &lt;group ref="{http://www.osgi.org/xmlns/blueprint/v1.0.0}GbeanElements"/>
 *       &lt;attribute name="class" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tclass" />
 *       &lt;attribute name="depends-on" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}TdependsOn" />
 *       &lt;attribute name="factory-method" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tmethod" />
 *       &lt;attribute name="factory-ref" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tidref" />
 *       &lt;attribute name="init-method" type="{http://www.osgi.org/xmlns/blueprint/v1.0.0}Tmethod" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tinlined-bean")
@XmlRootElement(name = "inlineBean")
public class TinlinedBean extends Tbean {
}
