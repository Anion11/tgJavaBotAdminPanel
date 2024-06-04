package com.example.adminpanel.DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB extends config {
    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            connection = null;
        }
        return connection;
    }
}