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
package org.apache.geronimo.st.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.jee.deployment.Dependency;
import org.apache.geronimo.jee.naming.Pattern;

/**
 * @version $Rev$ $Date$
 */
public class ServerInfo {
	private static final String HOST = "localhost"; // Default host
	private static final int WEBSERVER_PORT = 8080; // Default port
	private static final String USERNAME = "system"; // Default user
	private static final String PASSWORD = "manager"; // Default password
	//TODO above four parameters must be dynamically obtained

	private static final String SERVER_INFO_SERVLET_WAR = "plan-creator";
	private static final String SERVER_INFO_SERVLET_NAME = "ServerInfoServlet";

	private static final String REQUESTED_INFO = "requestedInfo";
	private static final String DEPLOYED_EJBS = "deployedEJBs";
	private static final String JMS_CONNECTION_FACTORIES = "jmsConnectionFactories";
	private static final String JMS_DESTINATIONS = "jmsDestinations";
	private static final String JDBC_CONNECTION_POOLS = "jdbcConnectionPools";
	private static final String JAVA_MAIL_SESSIONS = "javaMailSessions";
	private static final String DEPLOLYED_CREDENTIAL_STORES = "deployedCredentialStores";
	private static final String COMMON_LIBS = "commonLibs";
	private static final String DEPLOYED_SECURITY_REALMS = "deployedSecurityRealms";

	private ServerInfo() {
		//singleton class
		Authenticator.setDefault(new CollectorToolAuthenticator());
	}
	private static ServerInfo instance = new ServerInfo();
	public static ServerInfo getInstance() {
		return instance;
	}

	public List<Pattern> getDeployedEJBs() {
		return createPatternList(getDynamicInfo(DEPLOYED_EJBS));
	}

	public List<Pattern> getJMSConnectionFactories() {
		return createPatternList(getDynamicInfo(JMS_CONNECTION_FACTORIES));
	}

	public List<Pattern> getJMSDestinations() {
		return createPatternList(getDynamicInfo(JMS_DESTINATIONS));
	}

	public List<Pattern> getJDBCConnectionPools() {
		return createPatternList(getDynamicInfo(JDBC_CONNECTION_POOLS));
	}

	public List<Pattern> getJavaMailSessions() {
		return createPatternList(getDynamicInfo(JAVA_MAIL_SESSIONS));
	}

	public List<Pattern> getDeployedCredentialStores() {
		return createPatternList(getDynamicInfo(DEPLOLYED_CREDENTIAL_STORES));
	}

	public List<Dependency> getCommonLibs() {
		return createDependencyList(getDynamicInfo(COMMON_LIBS));
	}

	public List<String> getDeployedSecurityRealms() {
		return getDynamicInfo(DEPLOYED_SECURITY_REALMS);
	}

	private List<String> getDynamicInfo(String requestedInfo) {
		List<String> list = new ArrayList<String>();
		try {
			String urlPrefix = "http://" + HOST + ":" + WEBSERVER_PORT + "/"
					+ SERVER_INFO_SERVLET_WAR + "/" + SERVER_INFO_SERVLET_NAME
					+ "?" + REQUESTED_INFO + "=";
			URL url = new URL(urlPrefix + requestedInfo);
			InputStream inp = url.openStream();
			ObjectInputStream oi = new ObjectInputStream(inp);
			for(;;) {
				String str = oi.readObject().toString();
				list.add(str);
			}
		} catch (MalformedURLException e) {
			// Should not occur
			e.printStackTrace();
		} catch (IOException e) {
			// No more info objects.  Marks completion.
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Pattern> createPatternList(List<String> list) {
		List<Pattern> newList = new ArrayList<Pattern>();
		for (int i = 0; i < list.size(); i++) {
			newList.add(createPattern(list.get(i)));
		}
		return newList;
	}

	public static List<Dependency> createDependencyList(List<String> list) {
		List<Dependency> newList = new ArrayList<Dependency>();
		for (int i = 0; i < list.size(); i++) {
			newList.add(createDependency(list.get(i)));
		}
		return newList;
	}

	public static Pattern createPattern(String patternString) {
		Pattern pattern = new Pattern();
		String[] elements = patternString.split("/", 6);
		if (!isEmpty(elements[0])) {
			pattern.setGroupId(elements[0]);
		}
		if (!isEmpty(elements[1])) {
			pattern.setArtifactId(elements[1]);
		}
		if (!isEmpty(elements[2])) {
			pattern.setVersion(elements[2]);
		}
		if (!isEmpty(elements[3])) {
			// do nothing;
		}
		if (!isEmpty(elements[4])) {
			pattern.setModule(elements[4]);
		}
		if (!isEmpty(elements[5])) {
			pattern.setName(elements[5]);
		}
		return pattern;
	}

	public static Dependency createDependency(String depString) {
		Dependency dependency = new Dependency();
		String[] elements = depString.split("/", 4);
		if (!isEmpty(elements[0])) {
			dependency.setGroupId(elements[0]);
		}
		if (!isEmpty(elements[1])) {
			dependency.setArtifactId(elements[1]);
		}
		if (!isEmpty(elements[2])) {
			dependency.setVersion(elements[2]);
		}
		if (!isEmpty(elements[3])) {
			dependency.setType(elements[3]);
		}
		return dependency;
	}

	public static boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	public void testServerInfo() {
		System.out.println("Deployed EJBs:");
		writePatternListToConsole(getDeployedEJBs());
		System.out.println("\nJMS Connection Factories:");
		writePatternListToConsole(getJMSConnectionFactories());
		System.out.println("\nJMS Destinations:");
		writePatternListToConsole(getJMSDestinations());
		System.out.println("\nJDBC Connection Pools:");
		writePatternListToConsole(getJDBCConnectionPools());
		System.out.println("\nJava Mail Sessions:");
		writePatternListToConsole(getJavaMailSessions());
		System.out.println("\nDeployed Credential Stores:");
		writePatternListToConsole(getDeployedCredentialStores());
		System.out.println("\nCommon Libs:");
		writeDependencyListToConsole(getCommonLibs());
		System.out.println("\nDeployed Security Realms:");
		writeStringListToConsole(getDeployedSecurityRealms());
	}

	public static void writePatternListToConsole(List<Pattern> list) {
		for (int i = 0; i < list.size(); i++) {
			Pattern pattern = list.get(i);
			System.out.println(pattern.getName() + "(" + pattern.getGroupId()
					+ "/" + pattern.getArtifactId() + "/"
					+ pattern.getVersion() + "/" + pattern.getModule() + ")");
		}
	}

	public static void writeDependencyListToConsole(List<Dependency> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	public static void writeStringListToConsole(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

	class CollectorToolAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(USERNAME, PASSWORD.toCharArray());
		}
	}
}
