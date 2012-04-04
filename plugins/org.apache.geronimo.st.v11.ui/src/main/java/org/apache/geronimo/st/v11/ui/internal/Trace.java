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
package org.apache.geronimo.st.v11.ui.internal;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.geronimo.runtime.common.log.Logger;
import org.apache.geronimo.st.v11.ui.Activator;

/**
 * Helper class to route trace output.
 *
 * @version $Rev$ $Date$
 */
public class Trace {
    
    private static final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm.ss.SSS");

    /**
     * Finest trace event.
     */
    public static int INFO = 1;

    /**
     * Warning trace event.
     */
    public static int WARNING = 2;

    /**
     * error trace event.
     */
    public static int ERROR = 4;
    /**
     * cancel trace event.
     */
    public static int CANCEL = 8;

    /**
     * Trace constructor comment.
     */
    private static Logger log;
    
    static {
        log = Logger.getInstance();
    }
    
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
    public static void trace(int level, String s, boolean opt) {
        trace(level, s, null, opt);
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
    public static void trace(int level, String s, Throwable t, boolean opt) {
        if (Activator.getDefault() == null || !Activator.getDefault().isDebugging()) {
            return;
        }
        if (opt) {
            log.trace(level, Activator.PLUGIN_ID, s, t);
        }
        if (Activator.console) {
            System.out.println(buildMessage(s));
            if (t != null) {
                t.printStackTrace(System.out);
            }
        }
    }

    private static String buildMessage(String msg) {
        StringBuilder builder = new StringBuilder(50);
        builder.append(formateCurrnetTime());
        builder.append(" [").append(Activator.PLUGIN_ID).append("] ");
        builder.append(msg);
        return builder.toString();
    }
    
    private static String formateCurrnetTime() {
        synchronized (df) {
            return df.format(new Date());
        }
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
    public static void tracePoint(String tracePoint, String classDotMethod, boolean opt) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "()", opt);
    }   
    
    public static void tracePoint(String tracePoint, boolean opt, String classDotMethod, Object... parms) {
        if (parms == null || parms.length == 0) {
            trace(Trace.INFO, tracePoint + ": " + classDotMethod + "()" , opt);
        } else {
            trace(Trace.INFO, tracePoint + ": " + classDotMethod + "(" , opt);
            for ( int ii=0; ii<parms.length; ii++) {
                Object parm = parms[ii];
                trace(Trace.INFO, "    parm" + (ii+1) + "=[" + (parm == null ? null : parm.toString()) + "]" , opt);
            }
            trace(Trace.INFO, ")" ,opt);
        }
    }    
    
    public static void trace(String tracePoint, String classDotMethod, boolean opt) {
        tracePoint(tracePoint, classDotMethod, opt);
    }

    public static void trace(String tracePoint, boolean opt, String classDotMethod, Object... parms) {
        tracePoint(tracePoint, opt, classDotMethod, parms);
    }

    public static void traceEntry(boolean opt, String classDotMethod, Object... parms) {
        tracePoint("Entry", opt, classDotMethod, parms);
    }
    
    public static void traceExit(boolean opt, String classDotMethod, Object... parms) {
        tracePoint("Exit", opt, classDotMethod, parms);
    }
}
