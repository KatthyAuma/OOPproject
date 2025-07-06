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
import java.sql.SQLException;

public class SignUp extends JFrame {
    public SignUp() {
        setTitle("Sign Up");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center window on screen


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));  // Light grey background


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Space around components
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

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


        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(120, 30));
        signUpButton.setBackground(new Color(60, 179, 113));  // Medium sea green
        signUpButton.setForeground(Color.WHITE);  // White text
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(signUpButton, gbc);


        signUpButton.addActionListener((ActionEvent e) -> {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "INSERT INTO tbl_students (username, password) VALUES (?, ?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, usernameField.getText());
                statement.setString(2, new String(passwordField.getPassword()));
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Sign-up successful!");


                new Login().setVisible(true);
                dispose();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error during sign-up. Please try again.");
            }
        });

        add(panel);
    }
}
