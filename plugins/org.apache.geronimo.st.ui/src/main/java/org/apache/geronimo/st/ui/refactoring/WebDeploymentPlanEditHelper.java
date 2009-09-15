package org.apache.geronimo.st.ui.refactoring;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

public class WebDeploymentPlanEditHelper {
	private String fileName;
	private WebDeploymentPlanHandler handler;
	
	public WebDeploymentPlanEditHelper(String fileName){
		this.fileName=fileName;
		this.handler=new WebDeploymentPlanHandler(fileName);
		init();
	}
	
	private void init(){
		SAXParserFactory factory=SAXParserFactory.newInstance();
		try {
			factory.setFeature("http://xml.org/sax/features/namespaces", true);
			SAXParser p = factory.newSAXParser();
			File file =new File(fileName);
			p.parse(file, handler);
		} catch (SAXNotRecognizedException e) {
			e.printStackTrace();
		} catch (SAXNotSupportedException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public int getContextRootOffset(){
		if(handler.getContextRootTextNode()!=null){
			return handler.getContextRootTextNode().getOffset();
		}
		return -1;
	}
	
	public String getContextRootValue(){
		if(handler.getContextRootTextNode()!=null){
			return handler.getContextRootTextNode().getValue();
		}
		return null;
	}
	
	public int getArtifactIdOffset() {
		if(handler.getArtifactIdTextNode()!=null){
			return handler.getArtifactIdTextNode().getOffset();
		}
		return -1;
	}
	
	public String getArtifactIdValue(){
		if(handler.getArtifactIdTextNode()!=null){
			return handler.getArtifactIdTextNode().getValue();
		}
		return null;
	}
}
