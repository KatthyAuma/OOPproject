// Leon Mark Munene Ngatia
// Informatics and Computer Science
// 190140
//15th November 2024
package com.ngatia.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    public Login() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));  // Light grey background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Margin around components
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(usernameField, gbc);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(passwordField, gbc);


        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 30));
        loginButton.setBackground(new Color(60, 179, 113));  // Medium sea green
        loginButton.setForeground(Color.WHITE);  // White text
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);


        loginButton.addActionListener((ActionEvent e) -> {
            try (Connection conn = DatabaseConnection.connect()) {

                String sql = "SELECT * FROM tbl_students WHERE username=? AND password=?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, usernameField.getText());
                statement.setString(2, new String(passwordField.getPassword()));
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    new MainApp().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid login. Please try again.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        add(panel);
    }
}
