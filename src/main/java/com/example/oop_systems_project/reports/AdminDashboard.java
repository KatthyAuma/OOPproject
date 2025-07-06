package com.example.oop_systems_project.reports;

import com.example.BRANCHES.StockManagerGUI;
import com.example.BRANCHES.ViewOrdersAdminGUI;
import com.example.BRANCHES.BranchOrderViewerGUI;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AdminDashboard extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Drink Sales Automation - Admin Panel");

        Label title = new Label("Welcome to Admin Panel");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-text-fill: black;");

        Button customerOrdersBtn = new Button("Customer Orders");
        Button ordersByBranchBtn = new Button("Orders by Branch");
        Button salesReportBtn = new Button("Sales Report");
        Button lowStockBtn = new Button("Stock Manager");

        for (Button btn : new Button[]{customerOrdersBtn, ordersByBranchBtn, salesReportBtn, lowStockBtn}) {
            btn.setMinWidth(180);
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom right, #f6d365, #fda085);" +
                            "-fx-text-fill: #333333;" +
                            "-fx-font-size: 14px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 10 20 10 20;" +
                            "-fx-font-weight: bold;"
            );

        }

        VBox menu = new VBox(20, customerOrdersBtn, ordersByBranchBtn, salesReportBtn, lowStockBtn);
        menu.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        menu.setPadding(new javafx.geometry.Insets(40, 20, 20, 20));
        menu.setAlignment(Pos.TOP_LEFT);
        menu.setMinWidth(250);

        VBox content = new VBox(title);
        content.setAlignment(Pos.TOP_CENTER);
        content.setSpacing(20);
        content.setPadding(new javafx.geometry.Insets(50, 50, 50, 50));

        Image bgImage = new Image(getClass().getResource("/DashboardBackground.jpg").toExternalForm());
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(1000);
        bgView.setPreserveRatio(false);


        StackPane overlay = new StackPane(bgView, new HBox(menu, content));
        Scene scene = new Scene(overlay, 1000, 600);

        // Button Actions
        customerOrdersBtn.setOnAction(e -> new ViewOrdersAdminGUI().start(new Stage()));
        ordersByBranchBtn.setOnAction(e -> new BranchOrderViewerGUI().start(new Stage()));
        salesReportBtn.setOnAction(e -> new ReportGUI().start(new Stage()));
        lowStockBtn.setOnAction(e -> new StockManagerGUI().start(new Stage()));

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}