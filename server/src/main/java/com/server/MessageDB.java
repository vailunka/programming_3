package com.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MessageDB {
    private Connection dataBaseConnection = null;
    private boolean dataBaseExist;

    public void open(String dbName) throws SQLException{
       

        File tempFile = new File(dbName);
        boolean dataBaseExist = tempFile.exists();
        String dataBaseName = "jdbc:sqlite:" + dbName;
        if(dataBaseExist){
        dataBaseConnection = DriverManager.getConnection(dataBaseName);
        }
        else{
            String createUserTable = "create table users (username varchar(50) NOT NULL, password varchar(50) NOT NULL, email varchar(50), primary key(username))";
            Statement createStatement = dataBaseConnection.createStatement();
            createStatement.executeUpdate(createUserTable);
            createStatement.close();

        }

    }
}
