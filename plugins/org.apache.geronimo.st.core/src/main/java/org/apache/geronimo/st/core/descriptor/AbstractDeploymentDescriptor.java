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
package org.apache.geronimo.st.core.descriptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version $Rev$ $Date$
 */
public class AbstractDeploymentDescriptor {

    Object obj;

    public AbstractDeploymentDescriptor(Object obj) {
        this.obj = obj;
    }

    protected List<String> getDeploymentDescriptorInfo(Map<String, String> input) {
        ArrayList<String> result = new ArrayList<String>();
        List info = null;
        try {
            Class clazz = null, infoClazz = null;
            Method infoGetter = null, nameGetter = null;
            clazz = Class.forName(input.get("class"));
            infoGetter = clazz.getMethod(input.get("infoGetter"), null);
            info = (List) infoGetter.invoke(obj, null);
            infoClazz = Class.forName(input.get("implClass"));
            nameGetter = infoClazz.getMethod(input.get("nameGetter"), null);
            for (int i = 0; i < info.size(); i++) {
                result.add((String) nameGetter.invoke(info.get(i), null));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

}
