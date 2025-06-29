package com.example.BRANCHES;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BranchOrderViewerGUI extends Application {
    private TableView<Order> table = new TableView<>();
    private static String currentBranchName = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🔐 Branch Login");

        VBox loginPane = new VBox(10);
        loginPane.setPadding(new Insets(20));
        loginPane.setStyle("-fx-background-color: #f0f8ff; -fx-background-radius: 12;");

        Label loginLabel = new Label("Enter Branch ID to View Orders:");
        loginLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 16));

        TextField branchField = new TextField();
        branchField.setPromptText("e.g., 001 for Nairobi");

        Button loginButton = new Button("View Orders");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String enteredID = branchField.getText().trim();
            String branch = getBranchFromID(enteredID);

            if (branch != null) {
                currentBranchName = branch;
                showOrders(primaryStage);
            } else {
                messageLabel.setText("❌ Invalid Branch ID.");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        loginPane.getChildren().addAll(loginLabel, branchField, loginButton, messageLabel);
        loginPane.setPadding(new Insets(50));
        loginPane.setAlignment(javafx.geometry.Pos.CENTER);

        Scene loginScene = new Scene(loginPane, 400, 250);
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void showOrders(Stage stage) {
        table.getColumns().addAll(OrderColumns.getColumns());

        Image bgImage = new Image("file:C:/Users/Elavaza Sandra/Downloads/Orderview2.jpeg");
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(900);
        bgView.setFitHeight(600);

        Label header = new Label(currentBranchName.equals("HQ Nairobi") ?
                "📋 All Orders (HQ Access)" :
                "📋 Orders for " + currentBranchName);
        header.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        header.setStyle("-fx-text-fill: black;");

        VBox layout = new VBox(10, header, table);
        layout.setPadding(new Insets(20));
        layout.setStyle("""
                -fx-background-color: rgba(255, 255, 255, 0.3);
                -fx-background-radius: 10;
                """);
        table.setStyle("""
                -fx-background-color: rgba(245, 245, 245, 0.3);
                -fx-background-radius: 8;
                -fx-control-inner-background: rgba(245, 245, 245, 0.5);
                -fx-text-fill: black;
                -fx-table-cell-border-color: transparent;
                -fx-table-header-border-color: transparent;
                """);

        StackPane root = new StackPane(bgView, layout);
        Scene scene = new Scene(root, 800, 450);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("📦 Branch Order View");
        stage.show();

        loadOrders();
    }

    private void loadOrders() {
        table.getItems().clear();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = currentBranchName.equals("HQ Nairobi")
                    ? "SELECT * FROM orders"
                    : "SELECT * FROM orders WHERE branch_name = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            if (!currentBranchName.equals("HQ Nairobi")) {
                stmt.setString(1, currentBranchName);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order(
                        rs.getString("customer_name"),
                        rs.getString("contact_info"),
                        rs.getString("drink_name"),
                        rs.getInt("quantity"),
                        rs.getString("branch_name"),
                        rs.getString("payment_method"),
                        rs.getDouble("ordervalue"),
                        rs.getString("order_time")
                );
                table.getItems().add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getBranchFromID(String id) {
        return switch (id) {
            case "001" -> "HQ Nairobi";
            case "002" -> "Nakuru";
            case "003" -> "Mombasa";
            case "004" -> "Kisumu";
            default -> null;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
