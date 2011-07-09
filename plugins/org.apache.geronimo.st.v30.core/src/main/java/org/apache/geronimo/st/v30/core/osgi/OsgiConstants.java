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
package org.apache.geronimo.st.v30.core.osgi;

public final class OsgiConstants {

    public final static String APPLICATION = "osgi.app"; 
    public final static String APPLICATION_EXTENSION = ".eba"; 
    public final static String APPLICATION_DATAMODEL_PROVIDER_ID = "osgi.application.datamodelprovider";
    public final static String JAR_TYPE = "jar";

    public final static String BUNDLE = "osgi.bundle"; 
    public final static String BUNDLE_GROUP = "recorded-bundles";
    public final static String BUNDLE_EXTENSION = ".jar";
    public final static String BUNDLE_DATAMODEL_PROVIDER_ID = "osgi.bundle.datamodelprovider";
    public final static int BUNDLE_DEFAULT_START_LEVLE = 60;
    
    public final static String FRAGMENT_BUNDLE = "osgi.fragment"; 
    public final static String FRAGMENT_BUNDLE_EXTENSION = ".jar"; 
    public final static String FRAGMENT_BUNDLE_DATAMODEL_PROVIDER_ID = "osgi.fragment.datamodelprovider";

    public final static String COMPOSITE_BUNDLE = "osgi.comp"; 
    public final static String COMPOSITE_BUNDLE_EXTENSION = ".cba";  
    public final static String COMPOSITE_BUNDLE_DATAMODEL_PROVIDER_ID = "osgi.composite.datamodelprovider";
    
    public final static String ARTIFACT_GROUP = "application";    
    public final static String ARTIFACT_TYPE = "eba";
    
}
