package com.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class RegistrationHandler implements HttpHandler{
    public UserAuthenticator registerUserAuthenticator;

    public RegistrationHandler(UserAuthenticator userAuthenticator){
        registerUserAuthenticator = userAuthenticator;
    }

    
    @Override
    public void handle(HttpExchange r) throws IOException {
        // TODO Auto-generated method stub
        Headers headers = r.getRequestHeaders();
        String contentType = "";
        if (r.getRequestMethod().equalsIgnoreCase("POST")) {
            if(headers.containsKey("Content-Type")){
                contentType = headers.get("Content-Type").get(0);
            }
            else{
                Response.responseHandlerPost("no content", 411, r);
            }
            
        
    }

}
    
}
