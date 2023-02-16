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
        JSONObject objUser = null;

        if (r.getRequestMethod().equalsIgnoreCase("POST")) {
            if(headers.containsKey("Content-Type")){
                contentType = headers.get("Content-Type").get(0);
            }
            else{
                Response.responseHandlerPost("no content", 411, r);
            }
            if(contentType.equalsIgnoreCase("application/json")){
                String newUser = Response.postHandle(r);
                
                if(newUser == null || newUser.length() == 0){
                    //if new user is null sending 412 which means precondition failed
                    Response.responseHandlerPost("new user needs username or password, cant be zero", 412, r);
                }
                else{
                    //gettinn objuser if failer then failer and loser lol
                    try{
                        objUser = new JSONObject(newUser);
                    }catch(JSONException e){
                        System.out.println("JSon parse failed");
                    }

                    if(objUser.getString("username").length() == 0  || objUser.getString("password").length() == 0){
                        Response.responseHandlerPost("not proper credentials", 413, r);
                    }
                    else{
                        if(!UserAuthenticator.addUser(objUser.getString("username"),
                        objUser.getString("password"), objUser.getString("email"))){
                            Response.responseHandlerPost("user exist", 405, r);
                        }
                        else{
                            Response.responseHandlerPost("register completed", 200, r);
                        }
                    }
                }
        
            }
            else{
                //type not json
                Response.responseHandlerPost("type not json", 407, r);
            }

        }
        else{
            // only post allowerd
            Response.responseHandlerPost("Only post method allowed", 401, r);
        }
    
    }
}
