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
package org.apache.geronimo.st.v21.core.internal;

import org.apache.geronimo.st.v21.core.Activator;
import org.eclipse.core.runtime.IStatus;

/**
 * Helper class to route trace output.
 *
 * @version $Rev: 589873 $ $Date: 2007-10-29 17:48:35 -0400 (Mon, 29 Oct 2007) $
 */
public class Trace {

    /**
     * Finest trace event.
     */
    public static byte INFO = 0;

    /**
     * Warning trace event.
     */
    public static byte WARNING = 1;

    /**
     * Severe trace event.
     */
    public static byte SEVERE = 2;

    /**
     * Trace constructor comment.
     */
    private Trace() {
        super();
    }

    /**
     * Trace the given text.
     * 
     * @param level
     *            the trace level
     * @param s
     *            a message
     */
    public static void trace(byte level, String s) {
        trace(level, s, null);
    }

    /**
     * Trace the given message and exception.
     * 
     * @param level
     *            the trace level
     * @param s
     *            a message
     * @param t
     *            a throwable
     */
    public static void trace(byte level, String s, Throwable t) {
        if (!Activator.getDefault().isDebugging())
            return;

        System.out.println(Activator.PLUGIN_ID + ":  " + s);
        if (t != null)
            t.printStackTrace();
        
    }

    /**
     * Trace the given message 
     * 
     * @param tracePoint
     *            The trace point (e.g., "Exit", "Entry", "Constructor", etc....
     *            
     * @param classDotMethod
     *            The class name + method name (e.g., "Class.method()")
     *            
     * @param parms
     *            Method parameter(s) if the trace point is an "Entry"
     *            or
     *            Return value if the trace point is an "Exit"
     */
    public static void tracePoint(String tracePoint, String classDotMethod) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "()" );
    }   
    public static void tracePoint(String tracePoint, String classDotMethod, Object... parms) {
        if ( parms == null ) {
            trace(Trace.INFO, tracePoint + ": " + classDotMethod + "( null )" );
        }
        else {
            trace(Trace.INFO, tracePoint + ": " + classDotMethod + "(" );
            for ( int ii=0; ii<parms.length; ii++) {
                Object parm = parms[ii];
                trace(Trace.INFO, "    parm" + (ii+1) + "=[" + (parm == null ? null : parm.toString()) + "]" );
            }
            trace(Trace.INFO, ")" );
        }
    }   
}
