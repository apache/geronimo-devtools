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

package org.apache.geronimo.testsuite.common.ui;

import java.util.Hashtable;
import java.util.Map;

public final class Constants {
    public final static String SERVER_V30 = "v30";
    public final static String SERVER_V22 = "v22";
    public final static String SERVER_V21 = "v21";
    public final static String SERVER_V20 = "v20";
    
    public final static String SERVERNAME = "key_1";
    public final static String SERVERDISPLAY = "key_2";
    public final static String SERVERRUNTIME = "key_3";
    public final static String SERVERPATH = "key_4";
    
    
    private final static Map constants;
    static {
        constants = new Hashtable();
        addConstant( SERVER_V30, SERVERNAME, "Apache Geronimo v3.0 Server" );
        addConstant( SERVER_V22, SERVERNAME, "Apache Geronimo v2.2 Server" );
        addConstant( SERVER_V21, SERVERNAME, "Apache Geronimo v2.1 Server" );
        addConstant( SERVER_V20, SERVERNAME, "Apache Geronimo v2.0 Server" );
        
        addConstant( SERVER_V30, SERVERDISPLAY, "Apache Geronimo v3.0 Server at localhost" );
        addConstant( SERVER_V22, SERVERDISPLAY, "Apache Geronimo v2.2 Server at localhost" );
        addConstant( SERVER_V21, SERVERDISPLAY, "Apache Geronimo v2.1 Server at localhost" );
        addConstant( SERVER_V20, SERVERDISPLAY, "Apache Geronimo v2.0 Server at localhost" );
        
        addConstant( SERVER_V30, SERVERRUNTIME, "Apache Geronimo v3.0" );
        addConstant( SERVER_V22, SERVERRUNTIME, "Apache Geronimo v2.2" );
        addConstant( SERVER_V21, SERVERRUNTIME, "Apache Geronimo v2.1" );
        addConstant( SERVER_V20, SERVERRUNTIME, "Apache Geronimo v2.0" );
        
        addConstant( SERVER_V30, SERVERPATH, "server_v3.0" );
        addConstant( SERVER_V22, SERVERPATH, "server_v2.2" );
        addConstant( SERVER_V21, SERVERPATH, "server_v2.1" );
        addConstant( SERVER_V20, SERVERPATH, "server_v2.0" );
    }
    
    public static String getConstant( String version, String key ) {
        return (String)constants.get(version + key);
    }
    
    private static void addConstant( String version, String key, String value ) {
        constants.put(version + key, value);
    }
}
