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

import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 *
 * <strong>GeronimoServerConsoleHelper</strong> is a helper class with that can 
 * be used to augment the contents of the Geronimo server console view. The idea
 * is to consolidate errors that might show up only in the Eclipse log in the
 * server's console view for easier diagnostics.
 *
 * @version $Rev$ $Date$ 
 */
public class GeronimoServerConsoleHelper {

    private static IOConsole geronimoServerConsole = null;
    private static IOConsoleOutputStream out = null;


    /**
     * Default constructor -- force consumers to use one of the static write methods
     */
    private GeronimoServerConsoleHelper() {
    }


    /**
     * Write the message to the Geronimo console. If a IOException results, assume
     * that the server is writing to it and try one more time (only).
     * 
     * @param severity -- Severity string to demarcate the messages from the standard server output
     * @param message -- Message to write to the Geronimo server console
     */
    public static void write(String severity, String message) {
        // 
        // See if the Geronimo console is visible yet
        // 
        if (geronimoServerConsole == null) {
            findGeronimoConsole();
        }
        if (geronimoServerConsole == null)
            return;

        try {
            out.write("\n-----> " + severity + " " + message + " <-----");
        }
        catch (IOException ioe) {
            try {
                out.write("\n-----> " + severity + " " + message + " <-----");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Write the message and exception to the Geronimo console. If a IOException results, assume
     * that the server is writing to it and try one more time (only).
     * 
     * @param severity -- Severity string to demarcate the messages from the standard server output
     * @param message -- Message to write to the Geronimo server console
     * @param throwable -- Exception
     */
    public static void write(String severity, String message, Throwable throwable) {
        // 
        // See if the Geronimo console is visible yet
        // 
        if (geronimoServerConsole == null) {
            findGeronimoConsole();
        }
        if (geronimoServerConsole == null)
            return;

        StackTraceElement elements[] = throwable.getStackTrace();

        try {
            out.write("\n-----> " + severity + " " + message + " " + throwable.getMessage());
            for (StackTraceElement element : elements) {
                out.write("\n" + element.toString());
            }
            out.write(" <-----");
        }
        catch (IOException ioe1) {
            try {
                out.write("\n-----> " + severity + " " + message + " " + throwable.getMessage());
                for (StackTraceElement element : elements) {
                    out.write("\n" + element.toString());
                }
                out.write(" <-----");
            }
            catch (IOException ioe2) {
                ioe2.printStackTrace();
            }
        }
    }


    /**
     * Write the exception to the Geronimo console. If a IOException results, assume
     * that the server is writing to it and try one more time (only).
     * 
     * @param severity -- Severity string to demarcate the messages from the standard server output
     * @param throwable -- Exception
     */
    public static void write(String severity, Throwable throwable) {
        write(severity, "", throwable);
    }

    /**
     * Find the Geronimo server console if it exists
     */
    private static void findGeronimoConsole() {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager conMan = plugin.getConsoleManager();
        IConsole[] existing = conMan.getConsoles();
        for (int i = 0; i < existing.length; i++) {
            if (existing[i].getName().contains("[Apache Geronimo]") &&
                existing[i] instanceof IOConsole) {
                geronimoServerConsole = (IOConsole)existing[i];
                out = geronimoServerConsole.newOutputStream();
                break;
            }
        } 
    }
}
