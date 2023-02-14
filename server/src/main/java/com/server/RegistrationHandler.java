package com.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RegistrationHandler implements HttpHandler{
    public UserAuthenticator registerUserAuthenticator;

    public RegistrationHandler(UserAuthenticator userAuthenticator){
        registerUserAuthenticator = userAuthenticator;
    }

    
    @Override
    public void handle(HttpExchange r) throws IOException {
        // TODO Auto-generated method stub
        if (r.getRequestMethod().equalsIgnoreCase("POST")) {
            
            // Handle POST requests here (users send this for sending messages)
            String text = Response.postHandle(r);
            String [] register = text.split(":");
            if(register.length != 2){
                Response.responseHandlerPost("Need User or password", 401, r);
            }
            
            if(UserAuthenticator.addUser(register[0], register[1])){
                
                Response.responseHandlerPost("Registeration completed", 200, r);
            }
       
            else{
                Response.responseHandlerPost("User already exist", 401, r);
            }
            }
        else {
            Response.responseHandlerPost("Only post allowed", 403, r);
        // Inform user here that only POST functions are supported and send an error code
        }
        
    }

    
    
}
