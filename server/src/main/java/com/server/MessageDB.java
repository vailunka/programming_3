package com.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageDB {
    private  Connection dataBaseConnection = null;
    

    public void open(String path, String dbName) throws SQLException{
       
        File tempFile = new File(path + dbName);
        boolean dataBaseExist = tempFile.exists();
        String dataBaseName = "jdbc:sqlite:" + path  + dbName;
        dataBaseConnection = DriverManager.getConnection(dataBaseName);
       
        if(!dataBaseExist){
            init(dbName);
        } 
}

    

    private boolean init(String name) throws SQLException{

        String dbName = name;

        String database = "jdbc:sqlite:" + dbName;
        dataBaseConnection = DriverManager.getConnection(database);

        if (null != dataBaseConnection) {
            String createBasicDB = "create table data (user varchar(50) NOT NULL, usermessage varchar(500) NOT NULL)";
            Statement createStatement = dataBaseConnection.createStatement();
            createStatement.executeUpdate(createBasicDB);
            createStatement.close();
            System.out.println("DB successfully created");

            return true;
        }

        System.out.println("DB creation failed");
        return false;

    }


    public void closeDB() throws SQLException {
		if (null != dataBaseConnection) {
			dataBaseConnection.close();
            System.out.println("closing db connection");
			dataBaseConnection = null;
		}
    }
}
