package com.server;

import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.ArrayList;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsConfigurator;
import java.io.*;
import com.sun.net.httpserver.HttpsParameters;
//todot


public class Server implements HttpHandler {
    
    private Server() {
    }
    private static ArrayList<WarningMessage> warningmessages = new ArrayList<WarningMessage>();
    @Override
    public void handle(HttpExchange t) throws IOException {
        Headers headers = t.getRequestHeaders();
        String contentType = "";
        JSONObject objmessage = null;

    //implement GET and POST handling 
        if (t.getRequestMethod().equalsIgnoreCase("POST")) {
        // Handle POST requests here (users send this for sending messages)
        if(headers.containsKey("Content-Type")){
            contentType = headers.get("Content-Type").get(0);
        }
        else{
            Response.responseHandlerPost("no content", 411, t);
        }
        if(contentType.equalsIgnoreCase("application/json")){
            String newMessage = Response.postHandle(t);
            System.out.println("new message" + newMessage);
            try{
                objmessage = new JSONObject(newMessage);
            }catch(JSONException e){
                System.out.println("JSon parse failed");
            }
            System.out.println("testings1");
            String nickname = objmessage.getString("nickname");
            String latitude = objmessage.getString("latitude");
            String longitude = objmessage.getString("longitude");
            String dangertype = objmessage.getString("dangertype");
            
            System.out.println(nickname + latitude + longitude + dangertype);


            WarningMessage message = new WarningMessage(nickname, latitude, longitude, dangertype);
            System.out.println("what");
            warningmessages.add(message);
            System.out.println("testing");
        }
        else{
            //type not json
            Response.responseHandlerPost("type not json", 407, t);
        }
            t.sendResponseHeaders(200, -1);
            t.close();
        
        
        
        
        
        } 
        else if (t.getRequestMethod().equalsIgnoreCase("GET")) {
            if(warningmessages.isEmpty()){
                Response.responseHandlerPost("no messages", 204, t);
            }
            else{
                JSONArray array = new JSONArray();
                for(int i = 0; i < warningmessages.size(); i++){
                    JSONObject addmessage = new JSONObject();
                    addmessage.put("nickname", warningmessages.get(i).getNickname())
                        .put("latitude", warningmessages.get(i).getLatitude())
                        .put("longitude", warningmessages.get(i).getLongitude())
                        .put("dangertype", warningmessages.get(i).getDangertype());
                    array.put(addmessage);
                    System.out.println(array);
                    System.out.println(addmessage);
                }
                String messages = array.toString(1);
                System.out.println(messages);
                Response.responseHandlerPost(messages, 200, t);
            }
            
        // Handle GET requests here (users use this to get messages)
        } else {

            Response.responseHandlerPost("not support", 400, t);
        // Inform user here that only POST and GET functions are supported and send an error code
        
        }
    }

    private static SSLContext serverSSLContext(String args, String args1) throws Exception{
    //private static SSLContext serverSSLContext() throws Exception{
        char[] passphrase = args1.toCharArray();
        //char[] passphrase = "123456".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(args), passphrase);
        //ks.load(new FileInputStream("C:/Users/ailun/programming3/group-0047-project/server/keystore.jks"), passphrase);
        //ks.load(new FileInputStream("C:/Users/ailun/keystore/keystore1.jks"), passphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return ssl;
    }



    public static void main(String[] args) throws Exception {
        //create the http server to port 8001 with default logger
        try{
            UserAuthenticator userAuthenticator = new UserAuthenticator("get");
            HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);
            HttpContext HttpContext = server.createContext("/warning", new Server());
            HttpContext.setAuthenticator(userAuthenticator);
            server.createContext("/registration", new RegistrationHandler(userAuthenticator));
            SSLContext sslContext = serverSSLContext(args[0], args[1]);
            //SSLContext sslContext = serverSSLContext();
            server.setHttpsConfigurator (new HttpsConfigurator(sslContext) {
                public void configure (HttpsParameters params) {
                    InetSocketAddress remote = params.getClientAddress();
                    SSLContext c = getSSLContext();
                    SSLParameters sslparams = c.getDefaultSSLParameters();
                    params.setSSLParameters(sslparams);
            }
            });
            
            //create context that defines path for the resource, in this case a "help"
            
            // creates a default executor
            server.setExecutor(null); 
            server.start(); 
        }catch(FileNotFoundException e){
        System.out.println("File not found");
         }
        catch(Exception e){
        e.printStackTrace();
        }
    
    }
}

    