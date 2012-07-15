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
package org.apache.geronimo.st.core.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Translated messages.
 *
 * @version $Rev$ $Date$
 */
public class Messages extends NLS {

    static {
        NLS.initializeMessages("org.apache.geronimo.st.core.internal.Messages", Messages.class);
    }

    public static String DISTRIBUTE_FAIL;
    public static String START_FAIL;
    public static String STOP_FAIL;
    public static String UNDEPLOY_FAIL;
    public static String REDEPLOY_FAIL;
    public static String DM_CONNECTION_FAIL;

    public static String errorJRE;

    public static String target11runtime;
    public static String target20runtime;
    public static String target21runtime;
    public static String target22runtime;

    public static String incorrectVersion;
    public static String noVersion;
    public static String missingContent;
    public static String errorPortInUse;
    public static String missingServer;

    public static String errorNoProfiler;

    // errors for GeronimoServerPluginManager
    public static String badConfigId;
    public static String errorDeletePlugin;
    public static String errorMovePlugin;
    public static String badPlugin;
    public static String errorCreateMetadata;
    public static String errorUpdateMetadata;
    public static String errorReadConfig;
    public static String errorReadSerFile;
    public static String errorNoSerFile;
    public static String errorLoadClass;
    public static String requires;
    public static String installed;
    public static String configSizeMismatch;
    public static String noDefaultServer;
}
