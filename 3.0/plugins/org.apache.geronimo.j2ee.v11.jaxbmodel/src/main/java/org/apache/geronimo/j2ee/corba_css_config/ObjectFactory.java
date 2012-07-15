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

package org.apache.geronimo.j2ee.corba_css_config;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openejb.xml.ns.corba_css_config_2 package. 
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

    private final static QName _Css_QNAME = new QName("http://www.openejb.org/xml/ns/corba-css-config-2.0", "css");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openejb.xml.ns.corba_css_config_2
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SSLType.TrustList }
     * 
     */
    public SSLType.TrustList createSSLTypeTrustList() {
        return new SSLType.TrustList();
    }

    /**
     * Create an instance of {@link ITTPrincipalNameStaticType }
     * 
     */
    public ITTPrincipalNameStaticType createITTPrincipalNameStaticType() {
        return new ITTPrincipalNameStaticType();
    }

    /**
     * Create an instance of {@link GSSUPStaticType }
     * 
     */
    public GSSUPStaticType createGSSUPStaticType() {
        return new GSSUPStaticType();
    }

    /**
     * Create an instance of {@link GSSUPDynamicType }
     * 
     */
    public GSSUPDynamicType createGSSUPDynamicType() {
        return new GSSUPDynamicType();
    }

    /**
     * Create an instance of {@link CssType.CompoundSecMechTypeList }
     * 
     */
    public CssType.CompoundSecMechTypeList createCssTypeCompoundSecMechTypeList() {
        return new CssType.CompoundSecMechTypeList();
    }

    /**
     * Create an instance of {@link SECIOPType }
     * 
     */
    public SECIOPType createSECIOPType() {
        return new SECIOPType();
    }

    /**
     * Create an instance of {@link TrustEveryoneType }
     * 
     */
    public TrustEveryoneType createTrustEveryoneType() {
        return new TrustEveryoneType();
    }

    /**
     * Create an instance of {@link SSLType }
     * 
     */
    public SSLType createSSLType() {
        return new SSLType();
    }

    /**
     * Create an instance of {@link EntityType }
     * 
     */
    public EntityType createEntityType() {
        return new EntityType();
    }

    /**
     * Create an instance of {@link CssType }
     * 
     */
    public CssType createCssType() {
        return new CssType();
    }

    /**
     * Create an instance of {@link ITTAbsentType }
     * 
     */
    public ITTAbsentType createITTAbsentType() {
        return new ITTAbsentType();
    }

    /**
     * Create an instance of {@link DescriptionType }
     * 
     */
    public DescriptionType createDescriptionType() {
        return new DescriptionType();
    }

    /**
     * Create an instance of {@link ITTAnonymousType }
     * 
     */
    public ITTAnonymousType createITTAnonymousType() {
        return new ITTAnonymousType();
    }

    /**
     * Create an instance of {@link CompoundSecMechType }
     * 
     */
    public CompoundSecMechType createCompoundSecMechType() {
        return new CompoundSecMechType();
    }

    /**
     * Create an instance of {@link SasMechType }
     * 
     */
    public SasMechType createSasMechType() {
        return new SasMechType();
    }

    /**
     * Create an instance of {@link ITTPrincipalNameDynamicType }
     * 
     */
    public ITTPrincipalNameDynamicType createITTPrincipalNameDynamicType() {
        return new ITTPrincipalNameDynamicType();
    }

    /**
     * Create an instance of {@link TrustNooneType }
     * 
     */
    public TrustNooneType createTrustNooneType() {
        return new TrustNooneType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CssType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.openejb.org/xml/ns/corba-css-config-2.0", name = "css")
    public JAXBElement<CssType> createCss(CssType value) {
        return new JAXBElement<CssType>(_Css_QNAME, CssType.class, null, value);
    }

}
