/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.st.v30.core.util;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.geronimo.st.v30.core.Activator;
import org.apache.geronimo.st.v30.core.internal.Trace;
import org.eclipse.wst.server.core.util.SocketUtil;

/**
 * @version $Rev$ $Date$
 */
public class Utils {
    
    public static Object getValueByFieldName(String fieldName, Object instance) throws Exception {
        Field f = instance.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(instance);
    } 
    
    public static boolean isPortInUse(String host, int port, int count) {
        List<InetAddress> addresses = new ArrayList<InetAddress>();
        // check any/all address
        addresses.add(null);
        try {
            // check specific address
            addresses.add(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            Trace.trace(Trace.WARNING, "Host lookup error for " + host, e, Activator.traceCore);
        }
        return isPortInUse(addresses, port, count);
    }
    
    public static boolean isPortInUse(List<InetAddress> addresses, int port, int count) {
        boolean inUse = isPortInUse(addresses, port);
        while (inUse && count > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // ignore
            }
            inUse = isPortInUse(addresses, port);
            count--;
        }
        return inUse;
    }
    
    public static boolean isPortInUse(List<InetAddress> addresses, int port) {
        for (InetAddress address : addresses) {
            if (SocketUtil.isPortInUse(address, port)) {
                return true;
            }
        }
        return false;
    }
}
