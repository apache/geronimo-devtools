package org.apache.geronimo.st.core.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

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
          userProperties.load(new FileInputStream(new File(securityPath,userPropertiesFile)));
          groupProperties.load(new FileInputStream(new File(securityPath,groupPropertiesFile)));
      } catch (FileNotFoundException e) {
          Trace.trace(Trace.SEVERE, e.getMessage());
          throw e;
      } catch (IOException e) {
          Trace.trace(Trace.SEVERE, e.getMessage());
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
            groupProperties.store(new FileOutputStream(new File(securityPath,groupPropertiesFile)), "");
            userProperties.store(new FileOutputStream(new File(securityPath,userPropertiesFile)), "");
        } catch (FileNotFoundException e) {
            Trace.trace(Trace.SEVERE, e.getMessage());
            throw e;
        } catch (IOException e) {
            Trace.trace(Trace.SEVERE, e.getMessage());
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
