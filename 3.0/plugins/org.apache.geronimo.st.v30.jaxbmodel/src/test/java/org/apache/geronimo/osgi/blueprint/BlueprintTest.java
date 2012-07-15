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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.custommonkey.xmlunit.Diff;


/**
 * <strong>BlueprintTest</strong> is used to test various JAXB 
 * operations on the following OSGi-specific XML file supported by the GEP:
 * 
 * <ul>
 *      <li>blueprint.xml
 * </ul>
 * 
 * <p>The following JAXB operations are performed: 
 * <ol>
 *      <li>Create XML with all fields
 * </ol>
 * 
 */
public class BlueprintTest extends TestCase {

    /*----------------------------------------------------------------------------------------------------------------*\
    |                                                                                                                  |
    |  Testcase(s)                                                                                                     | 
    |                                                                                                                  |
    \*----------------------------------------------------------------------------------------------------------------*/
    public void testCompleteXML() throws Exception {
        buildFullXMLFromScratch("blueprint/blueprint-expected.xml");
    }


    /*----------------------------------------------------------------------------------------------------------------*\
    |                                                                                                                  |
    |  Private method(s)                                                                                               | 
    |                                                                                                                  |
    \*----------------------------------------------------------------------------------------------------------------*/
    private void buildFullXMLFromScratch (String fileExpected) throws Exception {

        // 
        // Create the blueprint factory
        // 
        ObjectFactory blueprintFactory = new ObjectFactory();
        Tblueprint blueprint = blueprintFactory.createTblueprint();

        // 
        // Create numerous objects so they can be used throughout
        // 
        Targument argument = blueprintFactory.createTargument();
        Tbean bean = blueprintFactory.createTbean();
        Tcollection array = blueprintFactory.createTcollection();
        Tcollection list = blueprintFactory.createTcollection();
        Tcollection set = blueprintFactory.createTcollection();
        Tdescription anyDescription = blueprintFactory.createTdescription();
        Tdescription description = blueprintFactory.createTdescription();
        TinlinedBean inlinedBean = blueprintFactory.createTinlinedBean();
        TinlinedReference inlinedReference = blueprintFactory.createTinlinedReference();
        TinlinedReferenceList inlinedReferenceList = blueprintFactory.createTinlinedReferenceList();
        TinlinedService inlinedService = blueprintFactory.createTinlinedService();
        Tinterfaces anyInterfaces = blueprintFactory.createTinterfaces();
        Tinterfaces interfaces = blueprintFactory.createTinterfaces();
        Tkey key = blueprintFactory.createTkey();
        Tmap map = blueprintFactory.createTmap();
        TmapEntry mapEntry = blueprintFactory.createTmapEntry();
        Tnull _null = blueprintFactory.createTnull();
        Tprop prop = blueprintFactory.createTprop();
        Tproperty property = blueprintFactory.createTproperty();
        Tprops props = blueprintFactory.createTprops();
        Tref anyRef = blueprintFactory.createTref();
        Tref idref = blueprintFactory.createTref();
        Tref ref = blueprintFactory.createTref();
        Treference reference = blueprintFactory.createTreference();
        TreferenceList referenceList = blueprintFactory.createTreferenceList();
        TreferenceListener referenceListener = blueprintFactory.createTreferenceListener();
        TregistrationListener registrationListener = blueprintFactory.createTregistrationListener();
        Tservice service = blueprintFactory.createTservice();
        TserviceProperties serviceProperties = blueprintFactory.createTserviceProperties();
        TservicePropertyEntry servicePropertyEntry = blueprintFactory.createTservicePropertyEntry();
        Tvalue value = blueprintFactory.createTvalue();

        // 
        // Targument
        // 
        argument.setAny(anyRef);
        argument.setArray(array);
        argument.setBean(inlinedBean);
        argument.setDescription(description);
        argument.setIdref(idref);
        argument.setIndex(new BigInteger("1111111111"));
        argument.setList(list);
        argument.setMap(map);  
        argument.setNull(_null);
        argument.setProps(props);
        argument.setRef(ref);
        argument.setRefAttribute("ARGUMENT_REF_ATTRIBUTE");
        argument.setReference(inlinedReference);
        argument.setReferenceList(inlinedReferenceList);
        argument.setService(inlinedService);
        argument.setSet(set);   
        argument.setType("ARGUMENT_TYPE");
        argument.setValue(value);
        argument.setValueAttribute("ARGUMENT_VALUE_ATTRIBUTE");

        // 
        // Tbean
        // 
        bean.setActivation(Tactivation.LAZY);
        bean.setClazz("BEAN_CLASS");
        bean.setDescription(description);
        bean.setDestroyMethod("BEAN_DESTROY_METHOD");
        bean.setFactoryMethod("BEAN_FACTORY_METHOD");
        bean.setFactoryRef("BEAN_FACTORY_REFERENCE");
        bean.setId("BEAN_ID");
        bean.setInitMethod("BEAN_INIT_METHOD");
        bean.setScope("BEAN_SCOPE");
        bean.getArgumentOrPropertyOrAny().add(argument);
        bean.getArgumentOrPropertyOrAny().add(property);
        bean.getDependsOn().add("BEAN_DEPENDS_ON_1");
        bean.getDependsOn().add("BEAN_DEPENDS_ON_2");
        bean.getDependsOn().add("BEAN_DEPENDS_ON_3");

        // 
        // Tcollection: array
        // 
        array.setValueType("ARRAY_VALUE_TYPE");
        array.getGvalue().add(ref);
        array.getGvalue().add(idref);
        array.getGvalue().add(description);
        array.getGvalue().add(_null);

        // 
        // Tcollection: list
        // 
        list.setValueType("LIST_VALUE_TYPE");
        list.getGvalue().add(ref);
        list.getGvalue().add(idref);
        list.getGvalue().add(description);
        list.getGvalue().add(_null);

        // 
        // Tcollection: set
        // 
        set.setValueType("SET_VALUE_TYPE");
        set.getGvalue().add(ref);
        set.getGvalue().add(idref);
        set.getGvalue().add(description);
        set.getGvalue().add(_null);

        // 
        // Tdescription
        // 
        description.getContent().add("DESCRIPTION_PART1");
        description.getContent().add("DESCRIPTION_PART2");
        description.getContent().add("DESCRIPTION_PART3");
        anyDescription.getContent().add("ANY_DESCRIPTION_PART1");
        anyDescription.getContent().add("ANY_DESCRIPTION_PART2");

        // 
        // Tinlinedbean
        // 
        inlinedBean.setActivation(Tactivation.EAGER);
        inlinedBean.setClazz("INLINED_BEAN_CLASS");
        inlinedBean.setDescription(description);
        inlinedBean.setDestroyMethod("INLINED_BEAN_DESTROY_METHOD");
        inlinedBean.setFactoryMethod("INLINED_BEAN_FACTORY_METHOD");
        inlinedBean.setFactoryRef("INLINED_BEAN_FACTORY_REFERENCE");
        inlinedBean.setId("INLINED_BEAN_ID");
        inlinedBean.setInitMethod("INLINED_BEAN_INIT_METHOD");
        inlinedBean.setScope("INLINED_BEAN_SCOPE");
        inlinedBean.getArgumentOrPropertyOrAny().add(null);
        inlinedBean.getArgumentOrPropertyOrAny().add(null);
        inlinedBean.getDependsOn().add("INLINED_BEAN_DEPENDS_ON_1");
        inlinedBean.getDependsOn().add("INLINED_BEAN_DEPENDS_ON_2");
        inlinedBean.getDependsOn().add("INLINED_BEAN_DEPENDS_ON_3");

        // 
        // TinlinedReference
        // 
        inlinedReference.setActivation(Tactivation.EAGER);
        inlinedReference.setAvailability(Tavailability.OPTIONAL);
        inlinedReference.setComponentName("INLINED_REFERENCE_COMPONENT_NAME");
        inlinedReference.setDescription(description);
        inlinedReference.setFilter("INLINED_REFERENCE_FILTER");
        inlinedReference.setId("INLINED_REFERENCE_ID");
        inlinedReference.setInterface("INLINED_REFERENCE_INTERFACE");
        inlinedReference.setTimeout(new BigInteger("2222222222"));
        inlinedReference.getAny().add(anyDescription);
        inlinedReference.getDependsOn().add("INLINED_REFERENCE_DEPENDS_ON_1");
        inlinedReference.getDependsOn().add("INLINED_REFERENCE_DEPENDS_ON_2");
        inlinedReference.getDependsOn().add("INLINED_REFERENCE_DEPENDS_ON_3");
        inlinedReference.getReferenceListener().add(referenceListener);

        // 
        // TinlinedReferenceList
        // 
        inlinedReferenceList.setActivation(Tactivation.LAZY);
        inlinedReferenceList.setAvailability(Tavailability.MANDATORY);
        inlinedReferenceList.setComponentName("INLINED_REFERENCE_LIST_COMPONENT_NAME");
        inlinedReferenceList.setDescription(description);
        inlinedReferenceList.setFilter("INLINED_REFERENCE_LIST_FILTER");
        inlinedReferenceList.setId("INLINED_REFERENCE_LIST_ID");
        inlinedReferenceList.setInterface("INLINED_REFERENCE_LIST_INTERFACE");
        inlinedReferenceList.setMemberType(TserviceUse.SERVICE_OBJECT);
        inlinedReferenceList.getAny().add(anyInterfaces);
        inlinedReferenceList.getDependsOn().add("INLINED_REFERENCE_LIST_DEPENDS_ON_1");
        inlinedReferenceList.getDependsOn().add("INLINED_REFERENCE_LIST_DEPENDS_ON_2");
        inlinedReferenceList.getDependsOn().add("INLINED_REFERENCE_LIST_DEPENDS_ON_3");
        inlinedReferenceList.getReferenceListener().add(referenceListener);

        // 
        // TinlinedService
        // 
        inlinedService.setActivation(Tactivation.EAGER);
        inlinedService.setAny(anyRef);
        inlinedService.setAutoExport(TautoExportModes.DISABLED);
        inlinedService.setBean(inlinedBean);
        inlinedService.setDescription(description);
        inlinedService.setId("INLINED_SERVICE_ID");
        inlinedService.setInterface("INLINED_SERVICE_INTERFACE");
        inlinedService.setInterfaces(interfaces);
        inlinedService.setRanking(999);
        inlinedService.setRef(ref);
        inlinedService.setRefAttribute("INLINED_SERVICE_REF_ATTRIBUTE");
        inlinedService.setReference(inlinedReference);
        inlinedService.setServiceProperties(null);
        inlinedService.getDependsOn().add("INLINED_SERVICE_DEPENDS_ON_1");
        inlinedService.getDependsOn().add("INLINED_SERVICE_DEPENDS_ON_2");
        inlinedService.getDependsOn().add("INLINED_SERVICE_DEPENDS_ON_3");
        inlinedService.getRegistrationListener().add(registrationListener);
 
        // 
        // Tinterfaces
        // 
        anyInterfaces.getValue().add(blueprintFactory.createTinterfacesValue("ANY_INTERFACES_VALUE"));
        interfaces.getValue().add(blueprintFactory.createTinterfacesValue("INTERFACES_VALUE_1"));
        interfaces.getValue().add(blueprintFactory.createTinterfacesValue("INTERFACES_VALUE_2"));
 
        // 
        // Tkey
        // 
        key.setAny(anyDescription);
        key.setArray(array);
        key.setBean(inlinedBean);
        key.setIdref(idref);
        key.setList(list);
        key.setMap(null);
        key.setProps(props);
        key.setRef(ref);
        key.setReference(inlinedReference);
        key.setReferenceList(inlinedReferenceList);
        key.setService(inlinedService);
        key.setSet(set);
        key.setValue(value);

        // 
        // Tmap
        // 
        map.setKeyType("MAP_KEY_TYPE");
        map.setValueType("MAP_VALUE_TYPE");
        map.getEntry().add(mapEntry);

        // 
        // TmapEntry
        // 
        mapEntry.setAny(anyInterfaces);
        mapEntry.setArray(array);
        mapEntry.setBean(inlinedBean);
        mapEntry.setIdref(idref);
        mapEntry.setKey("MAP_ENTRY_KEY");
        mapEntry.setKeyAttribute(key);
        mapEntry.setKeyRef("MAP_ENTRY_KEY_REFERENCE");
        mapEntry.setList(list);
        mapEntry.setMap(null);
        mapEntry.setNull(_null);
        mapEntry.setProps(props);
        mapEntry.setRef(ref);
        mapEntry.setReference(inlinedReference);
        mapEntry.setReferenceList(inlinedReferenceList);
        mapEntry.setService(inlinedService);
        mapEntry.setSet(set);
        mapEntry.setValue(value);
        mapEntry.setValueAttribute("MAP_ENTRY_VALUE_ATTRIBUTE");
        mapEntry.setValueRef("MAP_ENTRY_VALUE_REFERENCE");

        // 
        // Tproperty
        // 
        property.setAny(anyRef);
        property.setArray(array);
        property.setBean(inlinedBean);
        property.setDescription(description);
        property.setIdref(idref);
        property.setList(list);
        property.setMap(map);
        property.setName("PROPERTY_NAME");
        property.setNull(_null);
        property.setProps(props);
        property.setRef(ref);
        property.setRefAttribute("PROPERTY_REF_ATTRIBUTE");
        property.setReference(inlinedReference);
        property.setReferenceList(inlinedReferenceList);
        property.setService(inlinedService);
        property.setSet(set);
        property.setValue(value);
        property.setValueAttribute("PROPERTY_VALUE_ATTRIBUTE");

        // 
        // Tprop
        // 
        prop.setContent("PROP_CONTENT");
        prop.setKey("PROP_KEY");
        prop.setValue("PROP_VALUE");

        // 
        // Tprops
        // 
        props.getProp().add(prop);

        // 
        // Tref
        // 
        anyRef.setComponentId("ANY_REFERENCE_COMPONENT_ID");
        idref.setComponentId("ID_REFERENCE_COMPONENT_ID");
        ref.setComponentId("REFERENCE_COMPONENT_ID");

        // 
        // Treference
        // 
        reference.setActivation(Tactivation.EAGER);
        reference.setAvailability(Tavailability.OPTIONAL);
        reference.setComponentName("REFERENCE_COMPONENT_NAME");
        reference.setDescription(description);
        reference.setFilter("REFERENCE_FILTER");
        reference.setId("REFERENCE_ID");
        reference.setInterface("REFERENCE_INTERFACE");
        reference.setTimeout(new BigInteger("3333333333"));
        reference.getAny().add(anyDescription);
        reference.getDependsOn().add("REFERENCE_DEPENDS_ON_1");
        reference.getDependsOn().add("REFERENCE_DEPENDS_ON_2");
        reference.getDependsOn().add("REFERENCE_DEPENDS_ON_3");
        reference.getReferenceListener().add(referenceListener);

        // 
        // TreferenceList
        // 
        referenceList.setActivation(Tactivation.LAZY);
        referenceList.setAvailability(Tavailability.MANDATORY);
        referenceList.setComponentName("REFERENCE_LIST_COMPONENT_NAME");
        referenceList.setDescription(description);
        referenceList.setFilter("REFERENCE_LIST_FILTER");
        referenceList.setId("REFERENCE_LIST_ID");
        referenceList.setInterface("REFERENCE_LIST_INTERFACE");
        referenceList.setMemberType(TserviceUse.SERVICE_REFERENCE);
        referenceList.getAny().add(anyInterfaces);
        referenceList.getDependsOn().add("REFERENCE_LIST_DEPENDS_ON_1");
        referenceList.getDependsOn().add("REFERENCE_LIST_DEPENDS_ON_2");
        referenceList.getDependsOn().add("REFERENCE_LIST_DEPENDS_ON_3");
        referenceList.getReferenceListener().add(referenceListener);

        // 
        // TreferenceListener
        // 
        referenceListener.setAny(anyRef);
        referenceListener.setBean(inlinedBean);
        referenceListener.setBindMethod("REFERENCE_LISTENER_BIND_METHOD");
        referenceListener.setRef(ref);
        referenceListener.setRefAttribute("REFERENCE_LISTENER_REF_ATTRIBUTE");
        referenceListener.setReference(null);
        referenceListener.setUnbindMethod("REFERENCE_LISTENER_UNBIND_METHOD");

        // 
        // TregistrationListener
        // 
        registrationListener.setAny(anyDescription);
        registrationListener.setBean(inlinedBean);
        registrationListener.setRef(ref);
        registrationListener.setRefAttribute("REGISTRATION_LISTENER_REF_ATTRIBUTE");
        registrationListener.setReference(inlinedReference);
        registrationListener.setRegistrationMethod("REGISTRATION_LISTENER_REGISTRATION_METHOD");
        registrationListener.setUnregistrationMethod("REGISTRATION_LISTENER_UNREGISTRATION_METHOD");

        // 
        // Tservice
        // 
        service.setActivation(Tactivation.EAGER);
        service.setAny(anyInterfaces);
        service.setAutoExport(TautoExportModes.DISABLED);
        service.setBean(inlinedBean);
        service.setDescription(description);
        service.setId("SERVICE_ID");
        service.setInterface("SERVICE_INTERFACE");
        service.setInterfaces(interfaces);
        service.setRanking(888);
        service.setRef(ref);
        service.setRefAttribute("SERVICE_REF_ATTRIBUTE");
        service.setReference(inlinedReference);
        service.setServiceProperties(serviceProperties);
        service.getDependsOn().add("SERVICE_DEPENDS_ON_1");
        service.getDependsOn().add("SERVICE_DEPENDS_ON_2");
        service.getDependsOn().add("SERVICE_DEPENDS_ON_3");
        service.getRegistrationListener().add(registrationListener);

        // 
        // TserviceProperties
        // 
        serviceProperties.getAny().add(anyRef);
        serviceProperties.getEntry().add(servicePropertyEntry);

        // 
        // TservicePropertyEntry
        // 
        servicePropertyEntry.setAny(anyDescription);
        servicePropertyEntry.setArray(array);
        servicePropertyEntry.setBean(inlinedBean);
        servicePropertyEntry.setIdref(idref);
        servicePropertyEntry.setKey("SERVICE_PROPERTY_ENTRY_KEY");
        servicePropertyEntry.setList(list);
        servicePropertyEntry.setMap(map);
        servicePropertyEntry.setNull(_null);
        servicePropertyEntry.setProps(props);
        servicePropertyEntry.setRef(ref);
        servicePropertyEntry.setReference(inlinedReference);
        servicePropertyEntry.setReferenceList(inlinedReferenceList);
        servicePropertyEntry.setService(inlinedService);
        servicePropertyEntry.setSet(set);
        servicePropertyEntry.setValue(value);
        servicePropertyEntry.setValueAttribute("SERVICE_PROPERTY_ENTRY_KEY_VALUE_ATTRIBUTE");

        // 
        // Tvalue
        // 
        value.setContent("VALUE_CONTENT");
        value.setType("VALUE_TYPE");
 
        // 
        // Set the blueprint description
        // 
        blueprint.setDescription(description);

        // 
        // Set the blueprint defaultActivation
        // 
        blueprint.setDefaultActivation(Tactivation.EAGER);

        // 
        // Set the blueprint defaultAvailability
        // 
        blueprint.setDefaultAvailability(Tavailability.OPTIONAL);

        // 
        // Set the blueprint defaultTimeout
        // 
        blueprint.setDefaultTimeout(new BigInteger("4444444444"));

        // 
        // Set the blueprint type-converters
        // 
        TtypeConverters typeConverters = blueprintFactory.createTtypeConverters();
        typeConverters.getBeanOrReferenceOrRef().add(bean);
        typeConverters.getBeanOrReferenceOrRef().add(reference);
        typeConverters.getBeanOrReferenceOrRef().add(referenceList);
        blueprint.setTypeConverters(typeConverters);

        // 
        // Set the blueprint serviceOrReferenceListOrBean
        // 
        blueprint.getServiceOrReferenceListOrBean().add(bean);
        blueprint.getServiceOrReferenceListOrBean().add(reference);
        blueprint.getServiceOrReferenceListOrBean().add(referenceList);
        blueprint.getServiceOrReferenceListOrBean().add(service);

        //
        // Set the blueprint otherAttributes
        // 
        blueprint.getOtherAttributes().put(new QName("OTHER_QNAME_1"),  "OTHER_ATTRIBUTE_1");
        blueprint.getOtherAttributes().put(new QName("OTHER_QNAME_2"),  "OTHER_ATTRIBUTE_2");
        blueprint.getOtherAttributes().put(new QName("OTHER_QNAME_3"),  "OTHER_ATTRIBUTE_3");
        blueprint.getOtherAttributes().put(new QName("OTHER_QNAME_4"),  "OTHER_ATTRIBUTE_4");
        blueprint.getOtherAttributes().put(new QName("OTHER_QNAME_5"),  "OTHER_ATTRIBUTE_5");
 
        //
        // Finally, create the blueprint XML
        // 
        JAXBElement<Tblueprint> jaxbElement = blueprintFactory.createBlueprint(blueprint);
        
        //
        // Marshall the Tblueprint so that it can be compared with the expected file
        // 
        JAXBContext jaxbContext = JAXBContext.newInstance( 
                                    "org.apache.geronimo.osgi.blueprint", getClass().getClassLoader() );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(jaxbElement, baos);
        String actual = new String(baos.toByteArray());

        InputStream expectedInputStream = this.getClass().getClassLoader().getResourceAsStream(fileExpected);
        String expected = readContent(expectedInputStream);

        try {
            Diff myDiff = new Diff(expected, actual);
            assertTrue("Files are similar " + myDiff, myDiff.similar());
        }
        catch (AssertionFailedError e) {
            System.out.println("[Actual XML] " + '\n' + actual + '\n');
            System.out.println("[Expected XML: " + fileExpected + "]\n" + expected + '\n');
            throw e;            
        }  
    }


    private String readContent(InputStream in) throws IOException {
        StringBuffer sb = new StringBuffer();
        in = new BufferedInputStream(in);
        int i = in.read();
        while (i != -1) {
            sb.append((char) i);
            i = in.read();
        }
        String content = sb.toString();
        return content;
    }
}
