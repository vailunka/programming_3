package com.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import com.sun.net.httpserver.*;
public class Response {



    public static String postHandle( HttpExchange httpExchange){
        String text = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody(),
        StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        return text;
    }

    public static void responseHandlerPost(String response, int responseCode, HttpExchange httpExchange) throws IOException{
        byte [] bytes = response.getBytes("UTF-8"); 
        httpExchange.sendResponseHeaders(responseCode, bytes.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}

    
