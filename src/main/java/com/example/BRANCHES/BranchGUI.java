package com.example.BRANCHES;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class BranchGUI extends Application {

    private static final Map<Integer, String> BRANCH_MAP = Map.of(
            1, "Nairobi Headquarters",
            2, "Nakuru",
            3, "Mombasa",
            4, "Kisumu"
    );

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        // Create tabs for each branch using the correct IDs
        tabPane.getTabs().addAll(
                createBranchTab(1, "Nairobi Headquarters"),
                createBranchTab(2, "Nakuru"),
                createBranchTab(3, "Mombasa"),
                createBranchTab(4, "Kisumu")
        );

        StackPane root = new StackPane(tabPane);
        Scene scene = new Scene(root, 750, 550);
        primaryStage.setTitle("🍹 Order drinks - Branches");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Tab createBranchTab(int branchId, String branchName) {
        Tab tab = new Tab(branchName);
        tab.setClosable(false);

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.25); -fx-background-radius: 10;");

        Label title = new Label("Welcome to " + branchName);
        title.setFont(Font.font("Outfit", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: black;");

        // Form setup
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(12);
        form.setAlignment(Pos.CENTER);

        // Drink prices
        Map<String, Double> drinkPrices = new HashMap<>();
        drinkPrices.put("Coke", 50.0);
        drinkPrices.put("Fanta", 45.0);
        drinkPrices.put("Sprite", 45.0);
        drinkPrices.put("Pepsi", 50.0);
        drinkPrices.put("Water", 30.0);

        // Form controls
        TextField customerField = new TextField();
        TextField contactField = new TextField();
        ComboBox<String> drinkComboBox = new ComboBox<>();
        drinkComboBox.getItems().addAll(drinkPrices.keySet());

        // Changed spinner range to allow more than 100 units
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 1000, 1);
        quantitySpinner.setEditable(true);

        ComboBox<String> paymentComboBox = new ComboBox<>();
        paymentComboBox.getItems().addAll("M-Pesa", "Credit Card");
        Label statusLabel = new Label();

        // Price calculation
        Runnable updateTotal = () -> {
            String selectedDrink = drinkComboBox.getValue();
            if (selectedDrink != null) {
                double price = drinkPrices.get(selectedDrink);
                double total = price * quantitySpinner.getValue();
                statusLabel.setText(String.format("Total: KES %.2f", total));
            }
        };

        drinkComboBox.setOnAction(e -> updateTotal.run());
        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTotal.run());

        // Order button action
        Button payButton = new Button("Place Order");
        payButton.setOnAction(e -> {
            String customer = customerField.getText().trim();
            String contact = contactField.getText().trim();
            String drink = drinkComboBox.getValue();
            int quantity = quantitySpinner.getValue();
            String paymentMethod = paymentComboBox.getValue();

            if (customer.isEmpty() || contact.isEmpty() || drink == null || paymentMethod == null) {
                showAlert("Error", "Please fill all fields");
                return;
            }

            try {
                if (!checkStock(branchId, drink, quantity)) {
                    statusLabel.setText("Error: Not enough stock available");
                    statusLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                if (placeOrder(branchId, customer, contact, drink, quantity, paymentMethod, drinkPrices.get(drink))) {
                    statusLabel.setText("Order placed successfully!");
                    statusLabel.setStyle("-fx-text-fill: green;");
                    // Clear form
                    customerField.clear();
                    contactField.clear();
                    drinkComboBox.setValue(null);
                    quantitySpinner.getValueFactory().setValue(1);
                    paymentComboBox.setValue(null);
                }
            } catch (SQLException ex) {
                statusLabel.setText("Error processing order");
                statusLabel.setStyle("-fx-text-fill: red;");
                ex.printStackTrace();
            }
        });

        // Restored Order History button
        Button historyButton = new Button("📜 Order History");
        historyButton.setStyle("-fx-background-color: #6a5acd; -fx-text-fill: white; -fx-font-weight: bold;");
        historyButton.setOnAction(e -> {
            try {
                OrderHistoryGUI historyGUI = new OrderHistoryGUI();
                Stage historyStage = new Stage();
                historyGUI.start(historyStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add controls to form
        form.add(new Label("Customer Name:"), 0, 0);
        form.add(customerField, 1, 0);
        form.add(new Label("Contact Info:"), 0, 1);
        form.add(contactField, 1, 1);
        form.add(new Label("Select Drink:"), 0, 2);
        form.add(drinkComboBox, 1, 2);
        form.add(new Label("Quantity:"), 0, 3);
        form.add(quantitySpinner, 1, 3);
        form.add(new Label("Payment Method:"), 0, 4);
        form.add(paymentComboBox, 1, 4);
        form.add(payButton, 1, 5);
        form.add(statusLabel, 1, 6);

        container.getChildren().addAll(
                title,
                form,
                historyButton  // Added history button back
        );
        tab.setContent(container);
        return tab;
    }

    private boolean checkStock(int branchId, String drinkName, int quantity) throws SQLException {
        String sql = "SELECT quantity FROM stock WHERE branch_id = ? AND drink_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            stmt.setString(2, drinkName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantity") >= quantity;
            }
            return false; // Drink not found in stock
        }
    }

    private boolean placeOrder(int branchId, String customer, String contact,
                               String drink, int quantity, String paymentMethod,
                               double price) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Check and reduce stock
            String stockSql = "UPDATE stock SET quantity = quantity - ? " +
                    "WHERE branch_id = ? AND drink_name = ? AND quantity >= ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
                stockStmt.setInt(1, quantity);
                stockStmt.setInt(2, branchId);
                stockStmt.setString(3, drink);
                stockStmt.setInt(4, quantity);

                int rowsUpdated = stockStmt.executeUpdate();
                if (rowsUpdated == 0) {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Create order
            String orderSql = "INSERT INTO orders (customer_name, contact_info, drink_name, " +
                    "quantity, branch_name, payment_method, ordervalue) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {
                orderStmt.setString(1, customer);
                orderStmt.setString(2, contact);
                orderStmt.setString(3, drink);
                orderStmt.setInt(4, quantity);
                orderStmt.setString(5, BRANCH_MAP.get(branchId));
                orderStmt.setString(6, paymentMethod);
                orderStmt.setDouble(7, price * quantity);
                orderStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}