package com.server;

import java.security.Key;
import java.sql.SQLException;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAuthenticator extends com.sun.net.httpserver.BasicAuthenticator {
    private static ArrayList<User> users = null;
    private static Database db = null;
    
    public UserAuthenticator(String realm){
        super(realm);
        users = new ArrayList<User>();
        db = Database.getInstance();  
    }
    
	@Override
	public boolean checkCredentials(String username, String password) {
		// Checking crederntials if username matches and password matched grants passed
        boolean isValidUser;
        try {
            isValidUser = db.authenticateUser(username, password);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("checking credentials failed");
            return false;
        }
        return isValidUser;
        
	}

    public static boolean addUser(String userName, String password, String email) throws JSONException, SQLException {
        boolean result = db.setUser(new JSONObject().put("username", userName).put("password", password).put("email", email));  
        if(!result){
            System.out.println("cant register user");
            return false;
        }  
        System.out.println(userName + "registered completed");
        return true;
}
}

