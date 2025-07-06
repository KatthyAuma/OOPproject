package com.example.BRANCHES;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderHistoryGUI extends Application {
    private TableView<Order> table = new TableView<>();
    private TextField contactField = new TextField();

    @Override
    public void start(Stage stage) {
        stage.setTitle("🧾 Your Order History");


        Image bgImage = new Image("file:C:/Users/Elavaza Sandra/Downloads/Orderview2.jpeg");
        ImageView bgView = new ImageView(bgImage);
        bgView.setFitWidth(900);
        bgView.setFitHeight(650);
        bgView.setPreserveRatio(false);
        bgView.setSmooth(true);


        Label label = new Label("Enter your contact:");
        label.setFont(Font.font("Outfit", FontWeight.BOLD, 12));
        Button viewBtn = new Button("View Orders");
        viewBtn.setOnAction(e -> loadOrders(contactField.getText().trim()));

        HBox inputBox = new HBox(10, label, contactField, viewBtn);
        inputBox.setPadding(new Insets(10));

        table.getColumns().addAll(OrderColumns.getColumns());
        table.setStyle("""
                -fx-control-inner-background: rgba(245, 245, 245, 0.5);
                -fx-background-color: transparent;
                -fx-table-cell-border-color: transparent;
                -fx-table-header-border-color: transparent;
                -fx-text-fill: black;
                """);


        VBox content = new VBox(10, inputBox, table);
        content.setPadding(new Insets(15));
        content.setStyle("""
                -fx-background-color: rgba(255, 255, 255, 0.3);
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.0.2), 8, 0, 0, 4);
                """);

        StackPane root = new StackPane(bgView, content);
        Scene scene = new Scene(root, 800, 400);
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();

    }

    private void loadOrders(String contact) {
        table.getItems().clear();
        if (contact.isEmpty()) return;

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM orders WHERE contact_info = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, contact);
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