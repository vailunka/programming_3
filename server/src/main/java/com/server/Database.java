package com.server;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {
    private Connection dbConnection = null;
    private static Database dbInstance = null;

    public static synchronized Database getInstance(){
        if(dbInstance == null){
            dbInstance = new Database();
        }
        return dbInstance;
    }

    private Database(){
        try{
            init();
        }
        catch(SQLException e){

        }
    }

    private void init() throws SQLException{

        String dbName = "DB";

            String database = "jdbc:sqlite:" + dbName;
            dbConnection = DriverManager.getConnection(database);

            if (null != dbConnection) {
                String createUserTable = "create table users (username varchar(50) NOT NULL, password varchar(50) NOT NULL, email varchar(50), primary key(username))";
                Statement createStatement = dbConnection.createStatement();
                createStatement.executeUpdate(createUserTable);
                createStatement.close();

            }
    }

    public void closeDB() throws SQLException {
		if (null != dbConnection) {
			dbConnection.close();
            System.out.println("closing db connection");
			dbConnection = null;
		}
    }
}
