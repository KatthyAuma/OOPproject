package com.example.BRANCHES;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/drink_orders", "root", "");
            System.out.println("✅ Connected!");
        } catch (Exception e) {
            e.printStackTrace(); // Copy what it prints here
        }
    }
}
