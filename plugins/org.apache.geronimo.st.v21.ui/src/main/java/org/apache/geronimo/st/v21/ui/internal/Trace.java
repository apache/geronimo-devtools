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
package org.apache.geronimo.st.v21.ui.internal;

import org.apache.geronimo.runtime.common.log.Logger;
import org.apache.geronimo.st.v21.ui.Activator;

/**
 * Helper class to route trace output.
 *
 * @version $Rev$ $Date$
 */
public class Trace {

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
    
    private static Logger log;
    static {
    	log = Logger.getInstance();
    }
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
        if (Activator.getDefault() == null || !Activator.getDefault().isDebugging())
            return;
        if(opt) {
        	log.trace(level, Activator.PLUGIN_ID, s, t);
        }
        if(Activator.console) {
            System.out.println(Activator.PLUGIN_ID + ":  " + s);
            if (t != null)
                t.printStackTrace();
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
     * @param parm1,2,3,4,5
     *            Method parameters if the trace point is an "Entry"
     *            or
     *            Return value if the trace point is an "Exit"
     */
    public static void trace(String tracePoint, String classDotMethod, boolean opt) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "()", opt);
    }   
    public static void trace(String tracePoint, boolean opt, String classDotMethod, Object parm1) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "( parm1=[" + (parm1 == null ? null : parm1.toString()) + "] )" , opt);
    }

    public static void trace(String tracePoint, boolean opt, String classDotMethod, Object parm1, Object parm2) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "( parm1=[" + (parm1 == null ? null : parm1.toString()) + "], " +
                                                                 "parm2=[" + (parm2 == null ? null : parm2.toString()) + "] )" , opt );
    }
    public static void trace(String tracePoint, boolean opt, String classDotMethod, Object parm1, Object parm2, Object parm3) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "( parm1=[" + (parm1 == null ? null : parm1.toString()) + "], " +
                                                                 "parm2=[" + (parm2 == null ? null : parm2.toString()) + "], " +
                                                                 "parm3=[" + (parm3 == null ? null : parm3.toString()) + "] )" , opt );
    }
    public static void trace(String tracePoint, boolean opt, String classDotMethod, Object parm1, Object parm2, Object parm3, Object parm4) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "( parm1=[" + (parm1 == null ? null : parm1.toString()) + "], " +
                                                                 "parm2=[" + (parm2 == null ? null : parm2.toString()) + "], " +
                                                                 "parm3=[" + (parm3 == null ? null : parm3.toString()) + "], " +
                                                                 "parm4=[" + (parm4 == null ? null : parm4.toString()) + "] )" , opt );
    }
    public static void trace(String tracePoint, boolean opt, String classDotMethod, Object parm1, Object parm2, Object parm3, Object parm4, Object parm5) {
        trace(Trace.INFO, tracePoint + ": " + classDotMethod + "( parm1=[" + (parm1 == null ? null : parm1.toString()) + "], " +
                                                                 "parm2=[" + (parm2 == null ? null : parm2.toString()) + "], " +
                                                                 "parm3=[" + (parm3 == null ? null : parm3.toString()) + "], " +
                                                                 "parm4=[" + (parm4 == null ? null : parm4.toString()) + "], " +
                                                                 "parm5=[" + (parm5 == null ? null : parm5.toString()) + "] )" , opt );
    }
}