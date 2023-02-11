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
            String text = new BufferedReader(new InputStreamReader(r.getRequestBody(),
            StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
            String [] register = text.split(":");
            if(register.length != 2){
                String noResponse = "asd";
                byte [] bytes = noResponse.getBytes("UTF-8"); 
                r.sendResponseHeaders(401, bytes.length);
                OutputStream outputStream = r.getResponseBody();
                outputStream.write(noResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            
            if(UserAuthenticator.addUser(register[0], register[1])){
                String noResponse = "register completed";
                byte [] bytes = noResponse.getBytes("UTF-8"); 
                r.sendResponseHeaders(200, bytes.length);
                OutputStream outputStream = r.getResponseBody();
                outputStream.write(noResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            }
       
            else{
                
                String noResponse = "user already exist";
                byte [] bytes = noResponse.getBytes("UTF-8"); 
                r.sendResponseHeaders(401, bytes.length);
                OutputStream outputStream = r.getResponseBody();
                outputStream.write(noResponse.getBytes());
                outputStream.flush();
                outputStream.close();
            }
            }
        else {
            
            String noResponse = "not allowed";
            byte [] bytes = noResponse.getBytes("UTF-8"); 
            r.sendResponseHeaders(403, bytes.length);
            OutputStream outputStream = r.getResponseBody();
            outputStream.write(noResponse.getBytes());
            outputStream.flush();
            outputStream.close();
        // Inform user here that only POST and GET functions are supported and send an error code
        
        }
        
    }

    
    
}
