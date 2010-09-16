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
package org.apache.geronimo.testsuite.v21.ui;

import org.apache.geronimo.testsuite.common.ui.AbbotHelper;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import abbot.swt.eclipse.junit.extensions.WorkbenchTestCase;
import abbot.swt.eclipse.utils.Preferences;
import abbot.swt.eclipse.utils.Preferences.Mode;

/*
 * @version $Rev$ $Date$ 
 */
public class EclipseUITest extends WorkbenchTestCase {
    Shell aShell;
    AbbotHelper aHelper;

    public EclipseUITest(){
    	super();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        Preferences.setPerspectiveSwitchPrompt(Mode.Always);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test()
    public void testEclipseUI()
    {
        boolean success = false;
        try {
            success = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        assertTrue (success);
    }
}
