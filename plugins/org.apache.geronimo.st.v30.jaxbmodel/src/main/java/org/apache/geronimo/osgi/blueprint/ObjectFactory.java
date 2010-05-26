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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.apache.geronimo.osgi.blueprint package. 
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

    private final static QName _Blueprint_QNAME               = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "blueprint");
    private final static QName _TbeanArgument_QNAME           = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "argument");
    private final static QName _TbeanProperty_QNAME           = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "property");
    private final static QName _TblueprintBean_QNAME          = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "bean");
    private final static QName _TblueprintService_QNAME       = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "service");
    private final static QName _TblueprintReferenceList_QNAME = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "reference-list");
    private final static QName _TblueprintReference_QNAME     = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "reference");
    private final static QName _TcollectionMap_QNAME          = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "map");
    private final static QName _TcollectionIdref_QNAME        = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "idref");
    private final static QName _TcollectionArray_QNAME        = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "array");
    private final static QName _TcollectionProps_QNAME        = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "props");
    private final static QName _TcollectionNull_QNAME         = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "null");
    private final static QName _TcollectionValue_QNAME        = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "value");
    private final static QName _TcollectionSet_QNAME          = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "set");
    private final static QName _TcollectionList_QNAME         = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "list");
    private final static QName _TcollectionRef_QNAME          = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "ref");
    private final static QName _TinterfacesValue_QNAME        = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "value");
    private final static QName _TtypeConvertersRef_QNAME      = new QName("http://www.osgi.org/xmlns/blueprint/v1.0.0", "ref");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.geronimo.osgi.blueprint
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Tservice }
     * 
     */
    public Tservice createTservice() {
        return new Tservice();
    }

    /**
     * Create an instance of {@link Tblueprint }
     * 
     */
    public Tblueprint createTblueprint() {
        return new Tblueprint();
    }

    /**
     * Create an instance of {@link TserviceReference }
     * 
     */
    public TserviceReference createTserviceReference() {
        return new TserviceReference();
    }

    /**
     * Create an instance of {@link TreferenceListener }
     * 
     */
    public TreferenceListener createTreferenceListener() {
        return new TreferenceListener();
    }

    /**
     * Create an instance of {@link TinlinedReference }
     * 
     */
    public TinlinedReference createTinlinedReference() {
        return new TinlinedReference();
    }

    /**
     * Create an instance of {@link TserviceProperties }
     * 
     */
    public TserviceProperties createTserviceProperties() {
        return new TserviceProperties();
    }

    /**
     * Create an instance of {@link Tdescription }
     * 
     */
    public Tdescription createTdescription() {
        return new Tdescription();
    }

    /**
     * Create an instance of {@link TregistrationListener }
     * 
     */
    public TregistrationListener createTregistrationListener() {
        return new TregistrationListener();
    }

    /**
     * Create an instance of {@link Treference }
     * 
     */
    public Treference createTreference() {
        return new Treference();
    }

    /**
     * Create an instance of {@link Tmap }
     * 
     */
    public Tmap createTmap() {
        return new Tmap();
    }

    /**
     * Create an instance of {@link TinlinedService }
     * 
     */
    public TinlinedService createTinlinedService() {
        return new TinlinedService();
    }

    /**
     * Create an instance of {@link Tkey }
     * 
     */
    public Tkey createTkey() {
        return new Tkey();
    }

    /**
     * Create an instance of {@link Tbean }
     * 
     */
    public Tbean createTbean() {
        return new Tbean();
    }

    /**
     * Create an instance of {@link Tref }
     * 
     */
    public Tref createTref() {
        return new Tref();
    }

    /**
     * Create an instance of {@link Tnull }
     * 
     */
    public Tnull createTnull() {
        return new Tnull();
    }

    /**
     * Create an instance of {@link Tproperty }
     * 
     */
    public Tproperty createTproperty() {
        return new Tproperty();
    }

    /**
     * Create an instance of {@link Tprop }
     * 
     */
    public Tprop createTprop() {
        return new Tprop();
    }

    /**
     * Create an instance of {@link Tcollection }
     * 
     */
    public Tcollection createTcollection() {
        return new Tcollection();
    }

    /**
     * Create an instance of {@link Tinterfaces }
     * 
     */
    public Tinterfaces createTinterfaces() {
        return new Tinterfaces();
    }

    /**
     * Create an instance of {@link TtypeConverters }
     * 
     */
    public TtypeConverters createTtypeConverters() {
        return new TtypeConverters();
    }

    /**
     * Create an instance of {@link TmapEntry }
     * 
     */
    public TmapEntry createTmapEntry() {
        return new TmapEntry();
    }

    /**
     * Create an instance of {@link TinlinedBean }
     * 
     */
    public TinlinedBean createTinlinedBean() {
        return new TinlinedBean();
    }

    /**
     * Create an instance of {@link Targument }
     * 
     */
    public Targument createTargument() {
        return new Targument();
    }

    /**
     * Create an instance of {@link Tprops }
     * 
     */
    public Tprops createTprops() {
        return new Tprops();
    }

    /**
     * Create an instance of {@link TservicePropertyEntry }
     * 
     */
    public TservicePropertyEntry createTservicePropertyEntry() {
        return new TservicePropertyEntry();
    }

    /**
     * Create an instance of {@link TinlinedReferenceList }
     * 
     */
    public TinlinedReferenceList createTinlinedReferenceList() {
        return new TinlinedReferenceList();
    }

    /**
     * Create an instance of {@link TtypedCollection }
     * 
     */
    public TtypedCollection createTtypedCollection() {
        return new TtypedCollection();
    }

    /**
     * Create an instance of {@link TreferenceList }
     * 
     */
    public TreferenceList createTreferenceList() {
        return new TreferenceList();
    }

    /**
     * Create an instance of {@link Tvalue }
     * 
     */
    public Tvalue createTvalue() {
        return new Tvalue();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Targument }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "argument", scope = Tbean.class)
    public JAXBElement<Targument> createTbeanArgument(Targument value) {
        return new JAXBElement<Targument>(_TbeanArgument_QNAME, Targument.class, Tbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tproperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "property", scope = Tbean.class)
    public JAXBElement<Tproperty> createTbeanProperty(Tproperty value) {
        return new JAXBElement<Tproperty>(_TbeanProperty_QNAME, Tproperty.class, Tbean.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tblueprint }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "blueprint")
    public JAXBElement<Tblueprint> createBlueprint(Tblueprint value) {
        return new JAXBElement<Tblueprint>(_Blueprint_QNAME, Tblueprint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tbean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "bean", scope = Tblueprint.class)
    public JAXBElement<Tbean> createTblueprintBean(Tbean value) {
        return new JAXBElement<Tbean>(_TblueprintBean_QNAME, Tbean.class, Tblueprint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tservice }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "service", scope = Tblueprint.class)
    public JAXBElement<Tservice> createTblueprintService(Tservice value) {
        return new JAXBElement<Tservice>(_TblueprintService_QNAME, Tservice.class, Tblueprint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TreferenceList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "reference-list", scope = Tblueprint.class)
    public JAXBElement<TreferenceList> createTblueprintReferenceList(TreferenceList value) {
        return new JAXBElement<TreferenceList>(_TblueprintReferenceList_QNAME, TreferenceList.class, Tblueprint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Treference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "reference", scope = Tblueprint.class)
    public JAXBElement<Treference> createTblueprintReference(Treference value) {
        return new JAXBElement<Treference>(_TblueprintReference_QNAME, Treference.class, Tblueprint.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tmap }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "map", scope = Tcollection.class)

    public JAXBElement<Tmap> createTcollectionMap(Tmap value) {
        return new JAXBElement<Tmap>(_TcollectionMap_QNAME, Tmap.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TinlinedService }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "service", scope = Tcollection.class)
    public JAXBElement<TinlinedService> createTcollectionService(TinlinedService value) {
        return new JAXBElement<TinlinedService>(_TblueprintService_QNAME, TinlinedService.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tref }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "idref", scope = Tcollection.class)
    public JAXBElement<Tref> createTcollectionIdref(Tref value) {
        return new JAXBElement<Tref>(_TcollectionIdref_QNAME, Tref.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tcollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "array", scope = Tcollection.class)
    public JAXBElement<Tcollection> createTcollectionArray(Tcollection value) {
        return new JAXBElement<Tcollection>(_TcollectionArray_QNAME, Tcollection.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tprops }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "props", scope = Tcollection.class)
    public JAXBElement<Tprops> createTcollectionProps(Tprops value) {
        return new JAXBElement<Tprops>(_TcollectionProps_QNAME, Tprops.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tnull }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "null", scope = Tcollection.class)
    public JAXBElement<Tnull> createTcollectionNull(Tnull value) {
        return new JAXBElement<Tnull>(_TcollectionNull_QNAME, Tnull.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tvalue }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "value", scope = Tcollection.class)
    public JAXBElement<Tvalue> createTcollectionValue(Tvalue value) {
        return new JAXBElement<Tvalue>(_TcollectionValue_QNAME, Tvalue.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TinlinedBean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "bean", scope = Tcollection.class)
    public JAXBElement<TinlinedBean> createTcollectionBean(TinlinedBean value) {
        return new JAXBElement<TinlinedBean>(_TblueprintBean_QNAME, TinlinedBean.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tcollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "set", scope = Tcollection.class)
    public JAXBElement<Tcollection> createTcollectionSet(Tcollection value) {
        return new JAXBElement<Tcollection>(_TcollectionSet_QNAME, Tcollection.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tcollection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "list", scope = Tcollection.class)
    public JAXBElement<Tcollection> createTcollectionList(Tcollection value) {
        return new JAXBElement<Tcollection>(_TcollectionList_QNAME, Tcollection.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TinlinedReferenceList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "reference-list", scope = Tcollection.class)
    public JAXBElement<TinlinedReferenceList> createTcollectionReferenceList(TinlinedReferenceList value) {
        return new JAXBElement<TinlinedReferenceList>(_TblueprintReferenceList_QNAME, TinlinedReferenceList.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tref }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "ref", scope = Tcollection.class)
    public JAXBElement<Tref> createTcollectionRef(Tref value) {
        return new JAXBElement<Tref>(_TcollectionRef_QNAME, Tref.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TinlinedReference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "reference", scope = Tcollection.class)
    public JAXBElement<TinlinedReference> createTcollectionReference(TinlinedReference value) {
        return new JAXBElement<TinlinedReference>(_TblueprintReference_QNAME, TinlinedReference.class, Tcollection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "value", scope = Tinterfaces.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTinterfacesValue(String value) {
        return new JAXBElement<String>(_TinterfacesValue_QNAME, String.class, Tinterfaces.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tref }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "ref", scope = TtypeConverters.class)
    public JAXBElement<Tref> createTtypeConvertersRef(Tref value) {
        return new JAXBElement<Tref>(_TtypeConvertersRef_QNAME, Tref.class, TtypeConverters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tbean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "bean", scope = TtypeConverters.class)
    public JAXBElement<Tbean> createTtypeConvertersBean(Tbean value) {
        return new JAXBElement<Tbean>(_TblueprintBean_QNAME, Tbean.class, TtypeConverters.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Treference }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.osgi.org/xmlns/blueprint/v1.0.0", name = "reference", scope = TtypeConverters.class)
    public JAXBElement<Treference> createTtypeConvertersReference(Treference value) {
        return new JAXBElement<Treference>(_TblueprintReference_QNAME, Treference.class, TtypeConverters.class, value);
    }

}
