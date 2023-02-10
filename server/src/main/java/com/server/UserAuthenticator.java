package com.server;

import java.security.Key;
import java.util.*;

public class UserAuthenticator extends com.sun.net.httpserver.BasicAuthenticator {
    private Map<String,String> users = null;

    public UserAuthenticator(String realm){
        super(realm);
        users = new Hashtable<String, String>();
        users.put("dummy", "passwd");
    }
    
	@Override
	public boolean checkCredentials(String username, String password) {
		// TODO Auto-generated method stub
        if(username.equals("dummy") && password.equals("passwd")){
            return true;
        }
		return false;
	}
}
