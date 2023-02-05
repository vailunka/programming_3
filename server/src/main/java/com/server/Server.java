package com.server;

import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.*;



public class Server implements HttpHandler {
    ArrayList<String> messages = new ArrayList<>();
    private Server() {
    }

    @Override
    public void handle(HttpExchange t) throws IOException {

    //implement GET and POST handling 
    if (t.getRequestMethod().equalsIgnoreCase("POST")) {
        
        // Handle POST requests here (users send this for sending messages)
        String text = new BufferedReader(new InputStreamReader(t.getRequestBody(),
        StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        System.out.println(text);
        messages.add(text);
        t.sendResponseHeaders(200, -1);
        t.close();
        } else if (t.getRequestMethod().equalsIgnoreCase("GET")) {
            
            String responseString = "";

            for(int i=0; i<messages.size();i++){
                responseString = responseString + messages.get(i).toString() + " ";     
            }
            System.out.println(responseString);
            if(messages.size() == 0){
                responseString = "No messages";
            }
            byte [] bytes = responseString.getBytes("UTF-8"); 
            t.sendResponseHeaders(200, bytes.length);
            OutputStream outputStream = t.getResponseBody();
            outputStream.write(responseString.getBytes());
            outputStream.flush();
            outputStream.close();
        // Handle GET requests here (users use this to get messages)
        } else {

            String noResponse = "Not supported";
            byte [] bytes = noResponse.getBytes("UTF-8"); 
            t.sendResponseHeaders(400, bytes.length);
            OutputStream outputStream = t.getResponseBody();
            outputStream.write(noResponse.getBytes());
            outputStream.flush();
            outputStream.close();
        // Inform user here that only POST and GET functions are supported and send an error code
        
        }
    }


    public static void main(String[] args) throws Exception {
        //create the http server to port 8001 with default logger
        HttpServer server = HttpServer.create(new InetSocketAddress(8001),0);
        //create context that defines path for the resource, in this case a "help"
        server.createContext("/warning", new Server());
        // creates a default executor
        server.setExecutor(null); 
        server.start(); 
    }
}