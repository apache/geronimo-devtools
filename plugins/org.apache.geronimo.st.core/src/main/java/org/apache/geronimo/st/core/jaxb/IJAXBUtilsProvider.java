package org.apache.geronimo.st.core.jaxb;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import org.eclipse.core.resources.IFile;

public interface IJAXBUtilsProvider {
	 public JAXBContext getJAXBContext();
	 public void marshalDeploymentPlan(JAXBElement jaxbElement, IFile file) throws Exception;
	 public void marshalPlugin(JAXBElement jaxbElement, OutputStream outputStream) throws Exception;
	 public JAXBElement unmarshalFilterDeploymentPlan(IFile file) throws Exception;
	 public JAXBElement unmarshalPlugin(InputStream inputStream);
}
