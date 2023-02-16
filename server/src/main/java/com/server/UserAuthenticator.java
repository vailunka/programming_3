package com.server;

import java.security.Key;
import java.util.*;

public class UserAuthenticator extends com.sun.net.httpserver.BasicAuthenticator {
    private static ArrayList<User> users = null;
    
    public UserAuthenticator(String realm){
        super(realm);
        users = new ArrayList<User>();
        
        
    }
    
	@Override
	public boolean checkCredentials(String username, String password) {
		// Checking crederntials if username matches and password matched grants passed
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getUserName().equals(username) && users.get(i).getPassword().equals(password)){
                return true;
            }
        }
        
        return false;
	}

    public static boolean addUser(String userName, String password, String email) {
        
        //looking if user exist if not adding it to arraylist users
       for(int j = 0; j < users.size(); j++){
            if(users.get(j).getUserName().equals(userName)){
                return false;
            }
        }
            User newUser = new User(userName, password, email);

            users.add(newUser);
            return true;
       
       
}
}

