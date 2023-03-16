package com.server;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Base64;
import java.util.concurrent.Executors;
import java.security.SecureRandom;
import org.apache.commons.codec.digest.Crypt;

public class Database {
    private Connection dbConnection = null;
    private static Database dbInstance = null;
    private String databaseName = "MyDatabase.db";
    private SecureRandom strongRNG;

    public static synchronized Database getInstance(){
        if(dbInstance == null){
            dbInstance = new Database();
        }
        return dbInstance;
    }

    private Database(){
        try{
            open(databaseName);
            System.out.println("database open");
        }
        catch(SQLException e){
            System.out.println("cant open database");
        }
    }

    public void open(String dbName) throws SQLException{
       
        File tempFile = new File(dbName);
        boolean dataBaseExist = tempFile.exists();
        String dataBaseName = "jdbc:sqlite:" + dbName;
        dbConnection = DriverManager.getConnection(dataBaseName);
        if(!dataBaseExist){
            init(dbConnection);
        }
    }

    private void init(Connection dbconnection) throws SQLException{
        if (null != dbConnection) {
            String createUserTable = "create table users (username varchar(50) NOT NULL, password varchar(50) NOT NULL, email varchar(50), primary key(username))";
            String createMessageTable = "create table messages(nickname varchar(50) NOT NULL, dangertype VARCHAR(50) NOT NULL, longitude DOUBLE(4,20) NOT NULL, latitude DOUBLE(4,20) NOT NULL, sent INTERGER(20) NOT NULL), areacode VARCHAR(10), phonenumber VARCHAR(20)";
            Statement createStatement = dbConnection.createStatement();
            createStatement.executeUpdate(createUserTable);
            createStatement.executeUpdate(createMessageTable);
            createStatement.close();
            System.out.println("database created123");
            
        }
    }

    public void closeDB() throws SQLException {
		if (null != dbConnection) {
			dbConnection.close();
            System.out.println("closing db connection");
			dbConnection = null;
		}
    }
    public boolean setUser(JSONObject user) throws SQLException {
        
        byte bytes[] = new byte[13];
        strongRNG = new SecureRandom();
        strongRNG.nextBytes(bytes);
        String saltedB = new String(Base64.getEncoder().encode(bytes));
        String salt = "$6$" + saltedB; 
        String hashedPassword = Crypt.crypt(user.getString("password"), salt);
        System.out.println("checking if user exist");
        
        if(checkIfUserExist(user.getString("username"))){
            System.out.println("setting user failed");
            return false;
        }

        String SetUserString = "INSERT INTO users VALUES(?,?,?)";
        PreparedStatement insertStatement = dbConnection.prepareStatement(SetUserString);
        insertStatement.setString(1, user.getString("username"));
        insertStatement.setString(2, hashedPassword);
        insertStatement.setString(3, user.getString("email"));
        insertStatement.executeUpdate();
        insertStatement.close();
        System.out.println("user inserted  db");
        return true;
        }


    
    

    public void setMessage(WarningMessage message) throws SQLException {
        System.out.println("starting insert message");
        String SetMessageString = "INSERT INTO messages VALUES(?,?,?,?,?,?,?)";
        PreparedStatement insertStatement = dbConnection.prepareStatement(SetMessageString);

        insertStatement.setString(1, message.getNickname());
        insertStatement.setString(2, message.getDangertype());
        insertStatement.setDouble(3, message.getLongitude());
        insertStatement.setDouble(4, message.getLatitude());
        insertStatement.setLong(5, message.dateAsInt());
        insertStatement.setString(6, message.getAreacode());
        insertStatement.setString(7, message.getPhonenumber());
        
		insertStatement.executeUpdate();
		insertStatement.close();
       
    }

    public String getMessages() throws SQLException {
        System.out.println("get messages");
        Statement queryStatement = null;
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        String getMessagesString = "select nickname, dangertype, longitude, latitude, sent, area, phonenumber from messages";

        queryStatement = dbConnection.createStatement();
		ResultSet rs = queryStatement.executeQuery(getMessagesString);

        while (rs.next()) {
            obj.put("nickname", rs.getString("nickname"));
            obj.put("dangertype", rs.getString("dangertype"));
            obj.put("longitude", rs.getDouble("longitude"));
            obj.put("latitude", rs.getDouble("latitude"));
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(rs.getLong("sent")), ZoneOffset.UTC);
            obj.put("sent", time);
            obj.put("areacode", rs.getString("areacode"));
            obj.put("phonenumber", rs.getString("phonenumber"));
            
            array.put(obj);
		}

        return array.toString(1);

    }
    public boolean checkIfUserExist(String user) throws SQLException{
        Statement queryStatement = null;
        String checkUser = "select username from users where username = '" + user + "'";
        queryStatement = dbConnection.createStatement();
		ResultSet rs = queryStatement.executeQuery(checkUser);
        if(rs.next()){
            System.out.println("users already exist");
            return true;
        }
        else{
            return false;
        }    
    }
    public boolean authenticateUser(String username, String password) throws SQLException{
        String getUser = "select username, password from users where username = '" + username + "'";
        Statement queryStatement = dbConnection.createStatement();
        ResultSet rs = queryStatement.executeQuery(getUser);

        if(rs.next() == false){
            System.out.println("no such user");
            return false;
        }
        else{
            String pass = rs.getString("password");
            if(pass.equals(Crypt.crypt(password, pass))){
                System.out.println("access granted");
                return true;
                
            }
            else{
                System.out.println("access denied");
                return false;
            }
            
        }
    }


}

