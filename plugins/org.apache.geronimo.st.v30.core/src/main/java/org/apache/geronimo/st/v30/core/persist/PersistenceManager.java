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
package org.apache.geronimo.st.v30.core.persist;


/**
 * 
 *
 * @version $Rev$ $Date$
 */
public interface PersistenceManager<S, D> {
    /**
     * Load the value by key, if find nothing, the default Value will return;
     * @param <K>
     * @param <V>
     * @param key
     * @param defaultValue
     * @return
     * @throws Exception
     */
    <K, V> V load(K key, V defaultValue) throws Exception;
    
    /**
     * Load the value which is type of V by key
     * @param <K>
     * @param <V>
     * @param key
     * @param clazz
     * @return
     * @throws Exception
     */
    <K, V> V load(K key, Class<V> clazz) throws Exception;
    /**
     * Save the value V to the destination
     * @param <V>
     * @param value
     * @throws Exception
     */
    <V> void save(V value) throws Exception;
    
    S getSource();
    void setSource(S src);
    void setSource(String srcPath);
    D getDestination();
    void setDestination(D dst);
    void setDestination(String dstPath);
}
