package com.sma1lboy.ArenaForFight;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//SQL for store win round points
public class MySQL {

    private String host = "localhost";
    private String port = "3306";
    private String database = "practice";
    private String user = "root";
    private String password = "";

    private Connection connection;

    public boolean isConnected() {
        return this.connection != null;
    }
    public void connect() throws ClassNotFoundException, SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://" +
                host + ":" + port + "/" + database + "?useSSL=false", user, password);
    }
    public void disconnect() {
        if (isConnected()) {
            try {
                this.connection.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

}
