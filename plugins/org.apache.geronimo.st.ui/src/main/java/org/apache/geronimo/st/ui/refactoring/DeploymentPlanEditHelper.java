package org.apache.geronimo.st.ui.refactoring;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * 
 */
public class DeploymentPlanEditHelper {
	private String fileName;
	private DeploymentPlanHandler handler;
	
	public DeploymentPlanEditHelper(String fileName){
		this.fileName=fileName;
		this.handler=new DeploymentPlanHandler(fileName);
		init();
	}
	
	public DeploymentPlanEditHelper(DeploymentPlanHandler deploymentPlanHandler){
		this.fileName=deploymentPlanHandler.file;
		this.handler=deploymentPlanHandler;
		//parse the deployment plan, generate info
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
	
	public int getNodeOffset(String nodeName){
		return handler.getNodeOffset(nodeName);
	}
	
	public String getNodeValue(String nodeName){
		return handler.getNodeValue(nodeName);
	}
	
}
