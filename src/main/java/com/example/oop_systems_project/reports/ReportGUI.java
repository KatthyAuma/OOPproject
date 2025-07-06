package com.example.oop_systems_project.reports;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ReportGUI extends Application {

    private final ReportService reportService = new DatabaseReportService();
    private final TextArea reportArea = new TextArea();
    private final TextField thresholdInput = new TextField();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📊 Reports Dashboard");

        Label header = new Label("📈 Business Reports");
        header.setStyle("""
            -fx-font-size: 26px;
            -fx-font-weight: bold;
            -fx-text-fill: white;
        """);

        Button customersBtn = createStyledButton("👥 Customers with Orders");
        Button ordersBtn = createStyledButton("📦 Orders by Branch");
        Button salesBtn = createStyledButton("💰 Sales by Branch");
        Button totalSalesBtn = createStyledButton("🧾 Total Sales");
        Button lowStockBtn = createStyledButton("⚠️ Low Stock Alert");

        
        customersBtn.setOnAction(e -> {
            try {
                reportArea.setText(reportService.CustomerwithOrdersReport());
            } catch (SQLException ex) {
                showErrorAlert("Database Error", "Failed to fetch customer orders report");
                reportArea.setText("Error loading customer orders report. Please try again.");
                ex.printStackTrace();
            }
        });

        ordersBtn.setOnAction(e -> {
            try {
                reportArea.setText(reportService.OrdersByBranchReport());
            } catch (SQLException ex) {
                showErrorAlert("Database Error", "Failed to fetch orders by branch report");
                reportArea.setText("Error loading orders by branch report. Please try again.");
                ex.printStackTrace();
            }
        });

        salesBtn.setOnAction(e -> {
            try {
                reportArea.setText(reportService.SalesByBranchReport());
            } catch (SQLException ex) {
                showErrorAlert("Database Error", "Failed to fetch sales by branch report");
                reportArea.setText("Error loading sales by branch report. Please try again.");
                ex.printStackTrace();
            }
        });

        totalSalesBtn.setOnAction(e -> {
            try {
                reportArea.setText(reportService.TotalSalesReport());
            } catch (SQLException ex) {
                showErrorAlert("Database Error", "Failed to fetch total sales report");
                reportArea.setText("Error loading total sales report. Please try again.");
                ex.printStackTrace();
            }
        });

        lowStockBtn.setOnAction(e -> {
            try {
                if (thresholdInput.getText().isEmpty()) {
                    showErrorAlert("Input Error", "Please enter a threshold value");
                    return;
                }
                int threshold = Integer.parseInt(thresholdInput.getText());
                reportArea.setText(reportService.LowStockReport(threshold));
            } catch (NumberFormatException ex) {
                showErrorAlert("Input Error", "Please enter a valid number for threshold");
                reportArea.setText("⚠️ Please enter a valid number for threshold.");
            } catch (SQLException ex) {
                showErrorAlert("Database Error", "Failed to fetch low stock report");
                reportArea.setText("Error loading low stock report. Please try again.");
                ex.printStackTrace();
            }
        });

        HBox buttonRow = new HBox(12, customersBtn, ordersBtn, salesBtn, totalSalesBtn);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(10));

        thresholdInput.setPromptText("e.g., 5");
        thresholdInput.setPrefWidth(80);
        thresholdInput.setStyle("""
            -fx-background-radius: 8;
            -fx-border-radius: 8;
            -fx-padding: 5 10;
            -fx-border-color: #ffffff;
            -fx-background-color: rgba(255, 255, 255, 0.3);
            -fx-text-fill: black;
        """);

        Label thresholdLabel = new Label("Threshold:");
        thresholdLabel.setStyle("-fx-text-fill: white;");

        HBox lowStockBox = new HBox(10, thresholdLabel, thresholdInput, lowStockBtn);
        lowStockBox.setAlignment(Pos.CENTER);
        lowStockBox.setPadding(new Insets(10));

        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefHeight(400);
        reportArea.setStyle("""
            -fx-font-family: 'Outfit';
            -fx-font-size: 14px;
            -fx-font-weight: bold;
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-control-inner-background: rgba(255, 255, 255, 0.1);
            -fx-background-insets: 0;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: rgba(255, 255, 255, 0.3);
            -fx-border-width: 1;
            -fx-text-fill: black;
            -fx-highlight-fill: rgba(255, 255, 255, 0.3);
            -fx-highlight-text-fill: black;
        """);
        reportArea.setText("📃 Select a report to view data from the database");

        VBox card = new VBox(15, header, buttonRow, lowStockBox, reportArea);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("""
            -fx-background-color: transparent;
            -fx-background-radius: 20;
            -fx-border-radius: 20;
            -fx-border-color: rgba(255, 255, 255, 0.3);
            -fx-border-width: 1.5;
        """);
        card.setEffect(new DropShadow(30, Color.rgb(0, 0, 0, 0.25)));

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("file:C:/Users/Elavaza Sandra/Downloads/Reports.jpeg", 800, 600, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );

        StackPane root = new StackPane(card);
        root.setBackground(new Background(backgroundImage));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("""
            -fx-background-color: linear-gradient(to right, #43CEA2, #185A9D);
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-padding: 8 15;
            -fx-background-radius: 10;
            -fx-cursor: hand;
        """);
        return btn;
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}