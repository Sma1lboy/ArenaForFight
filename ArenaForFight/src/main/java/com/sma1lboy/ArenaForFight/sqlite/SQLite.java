package com.sma1lboy.ArenaForFight.sqlite;

import java.sql.*;

public class SQLite {


    private String url = "jdbc:sqlite:";
    public Connection connection;

    public SQLite(String url) {
        this.url += url;
        connect();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(this.url);
        } catch (SQLException e) {
            //TODO error manager
            e.printStackTrace();
        }
    }
    public void disconnect() {
        if(isConnected()) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                //TODO error manager
                e.printStackTrace();
            }
        }
    }



    public Connection getConnection() {
        return this.connection;
    }

    /**
     * @return true if the connection is connected, otherwise false
     */
    public boolean isConnected() {
        return this.connection != null;
    }

    public void insertTestExample() {
        try {
            String sql = "INSERT INTO users(uuid, points) VALUES(1, 1)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
