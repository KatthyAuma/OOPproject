// Leon Mark Munene Ngatia
// Informatics and Computer Science
// 190140
//15th November 2024


package com.ngatia.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class MainApp extends JFrame {

    private DefaultListModel<String> commentListModel;
    private JList<String> commentList;
    private ArrayList<Integer> commentIds;

    public MainApp() {
        setTitle("SU FEEDS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 240, 240));  // Light grey background


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;


        JTextField studentNameField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JTextField yearOfStudyField = new JTextField(20);
        JTextField classNameField = new JTextField(20);
        JTextArea commentsArea = new JTextArea(5, 20);
        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);

        JScrollPane commentsScroll = new JScrollPane(commentsArea);


        commentListModel = new DefaultListModel<>();
        commentList = new JList<>(commentListModel);
        commentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane commentListScroll = new JScrollPane(commentList);
        commentListScroll.setPreferredSize(new Dimension(300, 150));


        JButton addButton = createStyledButton("Add Comment", new Color(60, 179, 113));  // Medium sea green
        JButton viewMyCommentsButton = createStyledButton("View My Comments", new Color(70, 130, 180));  // Steel blue
        JButton viewAllCommentsButton = createStyledButton("View All Comments", new Color(70, 130, 180));  // Steel blue
        JButton updateButton = createStyledButton("Update Selected Comment", new Color(70, 130, 180));  // Steel blue
        JButton deleteButton = createStyledButton("Delete Selected Comment", new Color(70, 130, 180));  // Steel blue


        addButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "INSERT INTO tbl_topics (student_name, course, year_of_study, class_name, comment) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, studentNameField.getText());
                statement.setString(2, courseField.getText());
                statement.setInt(3, Integer.parseInt(yearOfStudyField.getText()));
                statement.setString(4, classNameField.getText());
                statement.setString(5, commentsArea.getText());
                statement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Comment added!");
                viewMyCommentsButton.doClick();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });


        viewMyCommentsButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "SELECT id, course, year_of_study, class_name, comment FROM tbl_topics WHERE student_name = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, studentNameField.getText());
                ResultSet rs = statement.executeQuery();


                commentListModel.clear();
                commentIds = new ArrayList<>();


                while (rs.next()) {
                    int id = rs.getInt("id");
                    String course = rs.getString("course");
                    int yearOfStudy = rs.getInt("year_of_study");
                    String className = rs.getString("class_name");
                    String comment = rs.getString("comment");

                    commentListModel.addElement(course + " (" + className + ") - " + comment);
                    commentIds.add(id);  // Store comment ID to map with selection
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });


        updateButton.addActionListener(e -> {
            int selectedIndex = commentList.getSelectedIndex();
            if (selectedIndex != -1) {
                int selectedCommentId = commentIds.get(selectedIndex);
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "UPDATE tbl_topics SET comment = ?, course = ?, year_of_study = ?, class_name = ? WHERE id = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, commentsArea.getText());
                    statement.setString(2, courseField.getText());
                    statement.setInt(3, Integer.parseInt(yearOfStudyField.getText()));
                    statement.setString(4, classNameField.getText());
                    statement.setInt(5, selectedCommentId);

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Comment updated!");
                        viewMyCommentsButton.doClick();  // Refresh comment list
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a comment to update.");
            }
        });


        viewAllCommentsButton.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.connect()) {
                String sql = "SELECT id, student_name, course, year_of_study, class_name, comment FROM tbl_topics";
                PreparedStatement statement = conn.prepareStatement(sql);
                ResultSet rs = statement.executeQuery();


                commentListModel.clear();
                commentIds = new ArrayList<>();


                while (rs.next()) {
                    int id = rs.getInt("id");
                    String studentName = rs.getString("student_name");
                    String course = rs.getString("course");
                    int yearOfStudy = rs.getInt("year_of_study");
                    String className = rs.getString("class_name");
                    String comment = rs.getString("comment");


                    commentListModel.addElement(studentName + " - " + course + " (" + className + ") - " + comment);
                    commentIds.add(id);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });



        deleteButton.addActionListener(e -> {
            int selectedIndex = commentList.getSelectedIndex();
            if (selectedIndex != -1) {
                int selectedCommentId = commentIds.get(selectedIndex);
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "DELETE FROM tbl_topics WHERE id = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, selectedCommentId);

                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Comment deleted!");
                        viewMyCommentsButton.doClick();  // Refresh comment list
                    } else {
                        JOptionPane.showMessageDialog(null, "Deletion failed.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a comment to delete.");
            }
        });


        commentList.addListSelectionListener(e -> {
            int selectedIndex = commentList.getSelectedIndex();
            if (selectedIndex != -1) {
                int selectedCommentId = commentIds.get(selectedIndex);
                try (Connection conn = DatabaseConnection.connect()) {
                    String sql = "SELECT course, year_of_study, class_name, comment FROM tbl_topics WHERE id = ?";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setInt(1, selectedCommentId);
                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        courseField.setText(rs.getString("course"));
                        yearOfStudyField.setText(String.valueOf(rs.getInt("year_of_study")));
                        classNameField.setText(rs.getString("class_name"));
                        commentsArea.setText(rs.getString("comment"));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        panel.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        panel.add(courseField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Year of Study:"), gbc);
        gbc.gridx = 1;
        panel.add(yearOfStudyField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Class Name:"), gbc);
        gbc.gridx = 1;
        panel.add(classNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Comments:"), gbc);
        gbc.gridx = 1;
        panel.add(commentsScroll, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        panel.add(viewMyCommentsButton, gbc);

        gbc.gridx = 2;
        panel.add(viewAllCommentsButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(updateButton, gbc);

        gbc.gridx = 1;
        panel.add(deleteButton, gbc);

        gbc.gridx = 2;
        panel.add(commentListScroll, gbc);


        add(panel);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(160, 40));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));
    }
}


