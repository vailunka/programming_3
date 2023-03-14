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


public class Database {
    private Connection dbConnection = null;
    private static Database dbInstance = null;
    private String databaseName = "MyDatabase.db";

    public static synchronized Database getInstance(){
        if(dbInstance == null){
            dbInstance = new Database();
        }
        return dbInstance;
    }

    private Database(){
        try{
            open(databaseName);
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
            String createMessageTable = "create table messages(nickname varchar(50) NOT NULL, dangertype VARCHAR(50) NOT NULL, longitude DOUBLE(4,20) NOT NULL, latitude DOUBLE(4,20) NOT NULL, sent INTERGER(20) NOT NULL)";
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
    public void setUser(JSONObject message) throws SQLException {
		String setMessageString = "insert into users " +
					"VALUES('" + message.getString("username") + "','" + message.getString("password") + "','" + message.getString("email") + "')"; 
		Statement createStatement;
		createStatement = dbConnection.createStatement();
		createStatement.executeUpdate(setMessageString);
		createStatement.close();
    }
    

    public void setMessage(WarningMessage message) throws SQLException {
        System.out.println("starting insert message");
        /* 
		String setMessageString = "insert into messages " +
					"VALUES('" + message.getNickname() + "','" + message.getDangertype()
                     + "','" + message.getLongitude() + "','" + message.getLatitude() +
                       "','" + message.dateAsInt() + "')"; 

                       */


        String SetMessageString = "INSERT INTO messages VALUES(?,?,?,?,?)";
        PreparedStatement insertStatement = dbConnection.prepareStatement(SetMessageString);

        insertStatement.setString(1, message.getNickname());
        insertStatement.setString(2, message.getDangertype());
        insertStatement.setDouble(3, message.getLongitude());
        insertStatement.setDouble(4, message.getLatitude());
        insertStatement.setLong(5, message.dateAsInt());
        System.out.println("1");    
          

		
        System.out.println("2");
		//insertStatement = dbConnection.createStatement();
        System.out.println("3");
		insertStatement.executeUpdate();
        System.out.println("4");
		insertStatement.close();
        System.out.println("message inserted db");
    }

    public String getMessages() throws SQLException {
        System.out.println("get messages");
        Statement queryStatement = null;
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        String getMessagesString = "select nickname, dangertype, longitude, latitude, sent from messages";

        queryStatement = dbConnection.createStatement();
		ResultSet rs = queryStatement.executeQuery(getMessagesString);

        while (rs.next()) {
            obj.put("nickname", rs.getString("nickname"));
            obj.put("dangertype", rs.getString("dangertype"));
            obj.put("longitude", rs.getDouble("longitude"));
            obj.put("latitude", rs.getDouble("latitude"));
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(rs.getLong("sent")), ZoneOffset.UTC);
            obj.put("sent", time);
            array.put(obj);
		}

        return array.toString(1);

    }
}

