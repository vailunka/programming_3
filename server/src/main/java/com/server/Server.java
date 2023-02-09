package com.server;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsConfigurator;
import java.io.*;
import com.sun.net.httpserver.HttpsParameters;




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

    private static SSLContext serverSSLContext() throws Exception{
        char[] passphrase = "123456".toCharArray();
   KeyStore ks = KeyStore.getInstance("JKS");
   ks.load(new FileInputStream("keystore.jks"), passphrase);

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
        HttpsServer server = HttpsServer.create(new InetSocketAddress(8001),0);
        SSLContext sslContext = serverSSLContext();

        server.setHttpsConfigurator (new HttpsConfigurator(sslContext) {
            public void configure (HttpsParameters params) {
            InetSocketAddress remote = params.getClientAddress();
            SSLContext c = getSSLContext();
            SSLParameters sslparams = c.getDefaultSSLParameters();
            params.setSSLParameters(sslparams);
            }
           });
        //create context that defines path for the resource, in this case a "help"
        server.createContext("/warning", new Server());
        // creates a default executor
        server.setExecutor(null); 
        server.start(); 
    }
}