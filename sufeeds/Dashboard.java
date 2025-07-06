// Leon Mark Munene Ngatia
// Informatics and Computer Science
// 190140
//15th November 2024


package com.ngatia.sufeeds;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Welcome to SU Feeds!");
        setSize(400, 300);  // Larger size to provide more space
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center the frame on the screen


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));  // Light grey background for modern feel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Spacing around components
        gbc.fill = GridBagConstraints.CENTER;


        JLabel welcomeLabel = new JLabel("Welcome to SU Feeds!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));  // Larger, bold font for title
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(welcomeLabel, gbc);


        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 30));  // Standard button size
        loginButton.setBackground(new Color(100, 149, 237));  // Cornflower blue color
        loginButton.setForeground(Color.WHITE);  // White text color
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(loginButton, gbc);


        JButton signupButton = new JButton("Sign Up");
        signupButton.setPreferredSize(new Dimension(100, 30));
        signupButton.setBackground(new Color(60, 179, 113));  // Medium sea green color
        signupButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(signupButton, gbc);


        loginButton.addActionListener(e -> {
            new Login().setVisible(true);
            dispose();
        });

        signupButton.addActionListener(e -> {
            new SignUp().setVisible(true);
            dispose();
        });

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
