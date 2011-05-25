package org.apache.geronimo.st.v30.ui.sections.blueprint;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.geronimo.osgi.blueprint.Tbean;
import org.apache.geronimo.osgi.blueprint.Tblueprint;
import org.apache.geronimo.osgi.blueprint.Tcomponent;
import org.apache.geronimo.osgi.blueprint.Treference;
import org.apache.geronimo.osgi.blueprint.TreferenceList;
import org.apache.geronimo.osgi.blueprint.Tservice;
import org.apache.geronimo.st.ui.internal.Trace;
import org.apache.geronimo.st.v30.core.jaxb.BlueprintJAXBHelper;
import org.apache.geronimo.st.v30.ui.Activator;
import org.apache.geronimo.st.v30.ui.CommonMessages;
import org.apache.geronimo.st.v30.ui.wizards.blueprint.BlueprintElementWizardProxy;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class OtherElementsSection extends AbstractBlueprintTreeSectionPart {


    public OtherElementsSection(Composite parent, FormToolkit toolkit, int style,JAXBElement blueprint) {
       super(blueprint, parent, toolkit, style);
       
       this.objectContainer = getBlueprint().getServiceOrReferenceListOrBean();
       createClient();
      
    }
    
    @Override
    public void createClient(){
        super.createClient();
        
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace=true;
        gridData.grabExcessVerticalSpace=true;
        this.getSection().setLayoutData(gridData);
    }

    @Override
    public String getTitle() {
        return CommonMessages.blueprintOtherElementsSection;
    }

    @Override
    public String getDescription() {
        return CommonMessages.blueprintOtherElementsSectionDescription;
    }

    @Override
    public Wizard getWizard() {
        return new BlueprintElementWizardProxy(this).getWizard();
    }

    @Override
    public Class getTableEntryObjectType() {
        //In fact, only Tbean, Treference,Tref are allowed. 
        //Maybe we can declare a common super classes for these three types
        return Tcomponent.class;
    }

    @Override
    public Object getInput() {
        if (objectContainer != null) {
            return objectContainer;
        }
        return super.getInput();
    }

    @Override
    protected void activateAddButton() {
        Object object = getSelectedObject();
        if (!BlueprintJAXBHelper.hasChildElements(object.getClass(),getExcludedFieldsOfClazz(object.getClass()))) {
            addButton.setEnabled(false);
        }else{
            addButton.setEnabled(true);
        }
    }

    public Object getSelectedObject() {
        if (tree.getSelection().length == 0) {
            return getBlueprint();
        }
        Object object;
        if (tree.getSelection()[0].getParentItem() == null) {
            object = tree.getSelection()[0].getData();
        } else {
            object = tree.getSelection()[0].getParentItem().getData();
        }
        if (isSubElements(object)) {
            return object;
        } else {
            return ((JAXBElement) object).getValue();
        }
    }

    @Override
    public void removeItem(Object anItem) {
        List childList = this.getBlueprint().getServiceOrReferenceListOrBean();
        if (isSubElements(anItem) && childList.contains(anItem)) {
            //remove direct children
            getObjectContainer().remove(anItem);
            this.getBlueprint().getServiceOrReferenceListOrBean().remove(anItem);
        } else {            
            Object object = tree.getSelection()[0].getParentItem().getData();
            //TODO:detach anItem from object 
            Field field = treeFieldMap.get(anItem);
            try {
                BlueprintJAXBHelper.detachFromParent(object, anItem, field);
            } catch (IllegalArgumentException e) {
                Trace.trace(Trace.ERROR, "can't remove object "+ anItem, e, Activator.logBlueprint);
            } catch (IllegalAccessException e) {
                Trace.trace(Trace.ERROR, "can't remove object "+ anItem, e, Activator.logBlueprint);
            } catch (InvocationTargetException e) {
                Trace.trace(Trace.ERROR, "can't remove object "+ anItem, e, Activator.logBlueprint);
            }
        } 
    }

    
    
   
    
    private boolean isSubElements(Object object){
        return Tbean.class.isInstance(object)||TreferenceList.class.isInstance(object)||Treference.class.isInstance(object)||Tservice.class.isInstance(object);
        
    }

    @Override
    protected Object getSectionRootElement() {
        return  getBlueprint();
    }

    @Override
    public List<Field> getExcludedFieldsOfClazz(Class clazz) {
        if (clazz.equals(Tblueprint.class)){
            //exclude Tdescription and Ttypeconverters
            List<Field> excludedList = new ArrayList<Field>();
            try {
                excludedList.add(getBlueprint().getClass().getDeclaredField("description"));
                excludedList.add(getBlueprint().getClass().getDeclaredField("typeConverters"));
            } catch (SecurityException e) {
                Trace.trace(Trace.ERROR, "Can't find field! ", e, Activator.logBlueprint);
            } catch (NoSuchFieldException e) {
                Trace.trace(Trace.ERROR, "Can't find field! ", e, Activator.logBlueprint);
            }
            return excludedList;
        }
        return null;
    }
}
