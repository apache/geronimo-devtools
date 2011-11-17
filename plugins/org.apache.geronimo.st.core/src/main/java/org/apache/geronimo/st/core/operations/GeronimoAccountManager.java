/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.geronimo.st.core.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.geronimo.st.core.Activator;
import org.apache.geronimo.st.core.internal.Trace;
import org.eclipse.wst.server.core.IRuntime;

//currently, only geronimo-admin with properties file realm is supported
public class GeronimoAccountManager {
    private Properties userProperties;
    private Properties groupProperties;
    
    private String securityPath;
    private static final String userPropertiesFile = "users.properties";
    private static final String groupPropertiesFile = "groups.properties";
    
    public GeronimoAccountManager(IRuntime runtime){
      if (runtime!=null){
          securityPath = runtime.getLocation().toOSString()+"/var/security";
      }
    }
    
    public void init() throws Exception{
        userProperties = new Properties();
        groupProperties= new Properties();
        try {
          FileInputStream fis = new FileInputStream(new File(securityPath,userPropertiesFile));
          userProperties.load(fis);
          fis.close();
          
          fis = new FileInputStream(new File(securityPath,groupPropertiesFile));
          groupProperties.load(fis);
          fis.close();
      } catch (FileNotFoundException e) {
          Trace.trace(Trace.ERROR, e.getMessage(), Activator.logOperations);
          throw e;
      } catch (IOException e) {
          Trace.trace(Trace.ERROR, e.getMessage(), Activator.logOperations);
          throw e;
      }
    }
    
    public String[] getUserList(){
        return userProperties.keySet().toArray(new String[0]);
    }
    
    public String[] getGroupList(){
        return groupProperties.keySet().toArray(new String[0]);
    }
    
    public boolean modifyUser(String oldName, String newName,String newGroup, String passwd){
        boolean operationResult = false;
        
        operationResult = true;
        return operationResult;
    }
    
    private void addUserIntoGroup(String groupName,String userName){
        String userList = groupProperties.getProperty(groupName);
        int index = getIndexOfUser(userName,userList);
        if (index!=-1){
            //user already exists
            return;
        }else {
            userList = userList.concat(",").concat(userName);
        }
        groupProperties.setProperty(groupName, userList);
    }
    
    private void delUserFromGroup(String groupName,String userName){
        String userList = groupProperties.getProperty(groupName);
        int index = getIndexOfUser(userName,userList);
        if (index == -1) return;
        else{
            userList = removeUser(userList,index);
        }
        groupProperties.setProperty(groupName, userList);
    }
    
    private String removeUser(String userList, int index) {
        String[] users = userList.split(",");
        StringBuilder usersStr = new StringBuilder();
        for (int i=0;i<users.length;i++){
           if (i!=index) {
               usersStr.append(users[i].concat(","));
           }
        }
        //delete last comma
        usersStr.deleteCharAt(usersStr.length()-1);
        
        return usersStr.toString();
    }

    public void delUser(String userName){
        String password = userProperties.getProperty(userName);
        if (password!=null) {
            userProperties.remove(userName);
        }
        
       Set<Object> groupSet = groupProperties.keySet();
       for (Object group:groupSet){
           delUserFromGroup((String)group,userName);
       }
    }
    
    
    public void addUser(String userName,String groupName,String password){
        userProperties.setProperty(userName, password);
        addUserIntoGroup(groupName,userName);
    }
    
    public void persist() throws Exception, IOException{
        try{
        	FileOutputStream fos = new FileOutputStream(new File(securityPath,groupPropertiesFile));
            groupProperties.store(fos, "");
            fos.close();
            
            fos = new FileOutputStream(new File(securityPath,userPropertiesFile));
            userProperties.store(fos, "");
            fos.close();
        } catch (FileNotFoundException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), Activator.logOperations);
            throw e;
        } catch (IOException e) {
            Trace.trace(Trace.ERROR, e.getMessage(), Activator.logOperations);
            throw e;
        }
    }
    
    private int getIndexOfUser(String username,String userList){
        String[] users = userList.split(",");
        for (int i=0;i<users.length;i++){
            if (users[i].equalsIgnoreCase(username))
                return i;
        }
            return -1;
    }
    
}
