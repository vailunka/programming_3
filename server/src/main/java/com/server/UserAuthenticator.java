package com.server;

import java.security.Key;
import java.util.*;

public class UserAuthenticator extends com.sun.net.httpserver.BasicAuthenticator {
    private static Map<String,String> users = null;
    
    public UserAuthenticator(String realm){
        super(realm);
        users = new Hashtable<String, String>();
        users.put("dummy", "passwd");
        
    }
    
	@Override
	public boolean checkCredentials(String username, String password) {
		// TODO Auto-generated method stubs
        
        if(users.containsKey(username) && password.equals(users.get(username))){
            return true;
        }
        return false;
	}

    public static boolean addUser(String userName, String password) {
        // TODO implement this by adding user to the Hashtable
        
        if(!users.containsKey(userName)){
            users.put(userName, password);
            return true;
        }
        return false;
       
}
}

