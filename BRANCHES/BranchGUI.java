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

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.example.BRANCHES.OrderHistoryGUI;

public class BranchGUI extends Application {

    @Override
    //start() - entry point for a JavaFX app, primaryStage - main window
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane(); //Tabbed interface

        tabPane.getTabs().addAll(
                createBranchTab("HQ Nairobi"),
                createBranchTab("Nakuru"),
                createBranchTab("Mombasa"),
                createBranchTab("Kisumu")
        );

        StackPane root = new StackPane(); // layout - nodes are stacked on top of each other
        root.getChildren().add(tabPane);

        BackgroundImage bgImage = new BackgroundImage(
                new Image("file:C:/Users/Elavaza Sandra/Downloads/background3.jpg", 750, 550, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
        root.setBackground(new Background(bgImage));

        Scene scene = new Scene(root, 750, 550); // Creates new scene with the root node
        primaryStage.setTitle("🍹 Order drinks - Branches");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private Tab createBranchTab(String branchName) { // creates a tab for each branch
        Tab tab = new Tab(branchName);
        tab.setClosable(false); //makes it non-closable

        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.25); -fx-background-radius: 10;");

        Label title = new Label("Welcome to " + branchName + " Branch");
        title.setFont(Font.font("Outfit", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: black;");

        GridPane form = new GridPane(); //organise them in a grid
        form.setHgap(10);
        form.setVgap(12);
        form.setAlignment(Pos.CENTER);

        Map<String, Double> drinkPrices = new HashMap<>();
        drinkPrices.put("Coke", 50.0);
        drinkPrices.put("Fanta", 45.0);
        drinkPrices.put("Sprite", 45.0);
        drinkPrices.put("Pepsi", 50.0);
        drinkPrices.put("Water", 30.0);


        Label customerLabel = new Label("Customer Name:");
        TextField customerField = new TextField();
        customerLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 15));
        customerLabel.setStyle("-fx-text-fill: black;");

        Label contactLabel = new Label("Contact Info:");
        contactLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 15));;
        contactLabel.setStyle("-fx-text-fill: black;");
        TextField contactField = new TextField();

        Label drinkLabel = new Label("Select Drink:");
        drinkLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 15));
        drinkLabel.setStyle("-fx-text-fill: black;");
        ComboBox<String> drinkComboBox = new ComboBox<>();
        drinkComboBox.getItems().addAll("Coke", "Fanta", "Sprite", "Pepsi", "Water");

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.setFont(Font.font("Outfit", FontWeight.MEDIUM, 15));
        quantityLabel.setStyle("-fx-text-fill: black;");
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 1000, 1);

        Label priceLabel = new Label("Price per item: -");
        priceLabel.setFont(Font.font("Outfit", FontWeight.NORMAL, 15));
        priceLabel.setStyle("-fx-text-fill: black");

        Label totalLabel = new Label("Total: -");
        totalLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 15));
        totalLabel.setStyle("-fx-text-fill: black");

        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setFont(Font.font("Outfit", FontWeight.NORMAL, 15));
        paymentLabel.setStyle("-fx-text-fill: black");
        ComboBox<String> paymentComboBox = new ComboBox<>();
        paymentComboBox.getItems().addAll("M-Pesa", "Credit Card");

        Button payButton = new Button("Place Order");
        Label statusLabel = new Label();

        //Update price and total
        Runnable updateTotal = () -> {
            String selectedDrink = drinkComboBox.getValue();
            Integer quantity = quantitySpinner.getValue();

            if(selectedDrink != null && quantity != null){
                double price = drinkPrices.get(selectedDrink);
                double total = price * quantity;
                priceLabel.setText("Price per item: KES " + price);
                totalLabel.setText("Total: KES "+ total);
            } else {
                priceLabel.setText("Price per item: -");
                totalLabel.setText("Total: -");
            }
        };

        drinkComboBox.setOnAction(e -> updateTotal.run());
        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> updateTotal.run());

        payButton.setOnAction(e -> { //Defines what happens when order button is clicked
            String customer = customerField.getText();
            String drink = drinkComboBox.getValue();
            int quantity = quantitySpinner.getValue();
            String paymentMethod = paymentComboBox.getValue();
            String contact = contactField.getText();

            if (customer.isEmpty() || drink == null || paymentMethod == null) {
                statusLabel.setText("⚠️ Please fill all fields.");
                statusLabel.setFont(Font.font("Outfit", FontWeight.BOLD,16));
                statusLabel.setStyle("-fx-text-fill: red; -fx-background-color: rgba(0,255,0,0.1); -fx-padding: 5px; -fx-background-radius: 8;");
            } else { //connects to the database
                try (Connection conn = DBConnection.getConnection()) {
                    boolean paymentSuccess = true;

                    if (paymentSuccess){
                        // Execute INSERT sql query to save the order to the table
                        double price = drinkPrices.get(drink);
                        double totalValue = price * quantity;

                        String sql = "INSERT INTO orders (customer_name, contact_info, drink_name, quantity, branch_name, payment_method, ordervalue, order_time) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
                        var pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, customer);
                        pstmt.setString(2, contact);
                        pstmt.setString(3, drink);
                        pstmt.setInt(4, quantity);
                        pstmt.setString(5, branchName);
                        pstmt.setString(6, paymentMethod);
                        pstmt.setDouble(7, totalValue);
                        pstmt.executeUpdate();


                        statusLabel.setText("✅ Order placed, thank you for shopping with us.");
                        statusLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 16));
                        statusLabel.setStyle("-fx-text-fill: green; -fx-background-color: rgba(0,255,0,0.1); -fx-padding: 8px; -fx-background-radius: 8;");
                        // Clear the form fields
                        customerField.clear();
                        contactField.clear();
                        drinkComboBox.setValue(null);
                        quantitySpinner.getValueFactory().setValue(1);
                        paymentComboBox.setValue(null);
                        priceLabel.setText("Price per item: -");
                        totalLabel.setText("Total: -");
                    } else {
                        statusLabel.setText("💳Payment failed. Please try again.");
                        statusLabel.setFont(Font.font("Outfit",FontWeight.BOLD, 16));
                        statusLabel.setStyle("-fx-text-fill: red; -fx-background-color: rgba(0,255,0,0.1); -fx-padding: 8px; -fx-background-radius: 8");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("🥲 Sorry, couldn't place order.");
                    statusLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 16));
                    statusLabel.setStyle("-fx-text-fill: red; -fx-background-color: rgba(0,255,0,0.1); -fx-padding: 8px; -fx-background-radius: 8");
                }
            }
        });

        Button historyButton = new Button("📜 Order History");
        historyButton.setStyle("-fx-background-color: #6a5acd; -fx-text-fill: white; -fx-font-weight: bold;");
        historyButton.setOnAction(e -> {
            try {
                // Open Customer Order History GUI in new window
                OrderHistoryGUI historyGUI = new OrderHistoryGUI();
                Stage historyStage = new Stage();
                historyGUI.start(historyStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        form.add(customerLabel, 0, 0);
        form.add(customerField, 1, 0);

        form.add(contactLabel, 0,1);
        form.add(contactField,1,1);

        form.add(drinkLabel, 0, 2);
        form.add(drinkComboBox, 1, 2);

        form.add(quantityLabel, 0, 3);
        form.add(quantitySpinner, 1, 3);

        form.add(priceLabel, 1, 4);
        form.add(totalLabel, 1, 5);

        form.add(paymentLabel, 0, 6);
        form.add(paymentComboBox, 1, 6);

        form.add(payButton, 1, 7);
        form.add(statusLabel, 1, 8);

        container.getChildren().addAll(title, form, historyButton);
        tab.setContent(container);

        return tab;
    }

    public static void main(String[] args) {
        launch(args); //Triggers start() above
    }
}
