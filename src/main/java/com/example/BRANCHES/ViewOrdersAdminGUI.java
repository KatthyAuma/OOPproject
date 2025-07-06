package com.example.BRANCHES;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewOrdersAdminGUI extends Application {
    private TableView<Order> table = new TableView<>();

    @Override
    public void start(Stage stage) {
        stage.setTitle("📊 Admin: All Orders");

        Image bgImage = new Image("file:C:/Users/Elavaza Sandra/Downloads/AdminOrderss.jpeg");
        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(false);
        bgView.setFitWidth(900);
        bgView.setFitHeight(650);
        bgView.setSmooth(true);
        bgView.setCache(true);

        table.getColumns().addAll(OrderColumns.getColumns());
        loadAllOrders();

        Label header = new Label("All Orders:");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        VBox content = new VBox(10, header, table);
        content.setPadding(new Insets(15));
        content.setStyle("""
                -fx-background-color: rgba(255, 255, 255, 0.3);
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 4);
                """);

        table.setStyle("""
                -fx-background-color: rgba(245, 245, 245, 0.3);
                -fx-background-radius: 8;
                -fx-control-inner-background: rgba(245, 245, 245, 0.5);
                -fx-text-fill: black;
                -fx-table-cell-border-color: transparent;
                -fx-table-header-border-color: transparent;
                """);
        header.setStyle("-fx-text-fill: black; -fx-font-size: 18px; -fx-font-weight: bold;");

        StackPane root = new StackPane(bgView, content);
        Scene scene = new Scene(root, 750, 450);
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    private void loadAllOrders() {
        table.getItems().clear();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM orders";
            PreparedStatement stmt = conn.prepareStatement(sql);
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

    public static void main(String[] args) {
        launch(args);
    }
}