package com.ngatia.sufeeds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection connect() {
        Connection connection = null;
        try {
            // Load UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // Path to your Access database file
            String dbPath = "C:/Users/ngati/Desktop/ICS II/OOP II/SUFeeds.accdb"; // Use forward slashes or double backslashes
            String url = "jdbc:ucanaccess://" + dbPath;

            connection = DriverManager.getConnection(url);
            System.out.println("Connected to Access DB!");

        } catch (ClassNotFoundException e) {
            System.err.println("UCanAccess driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to Access DB.");
            e.printStackTrace();
        }

        return connection;
    }
}
