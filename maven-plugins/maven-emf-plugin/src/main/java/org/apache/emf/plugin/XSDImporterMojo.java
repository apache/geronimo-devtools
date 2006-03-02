package org.apache.emf.plugin;
/**
 * Copyright 2004, 2005 The Apache Software Foundation or its licensors, as applicable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
import java.io.File;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.eclipse.xsd.ecore.importer.XSDImporterApplication;

/**
 * @goal xsd2Java
 */
public class XSDImporterMojo extends AbstractMojo {

	/**
	 * @parameter
	 */
	protected Map packageMap;

	/**
	 * @paramter
	 * @required
	 */
	protected File genModel;

	/**
	 * @paramter
	 * @required
	 */
	protected File project;

	/**
	 * @paramter
	 * @required
	 */
	protected String projectId;

	/**
	 * @paramter
	 * @required
	 */
	protected String type;

	XSDImporterApplication application;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

	}

}
