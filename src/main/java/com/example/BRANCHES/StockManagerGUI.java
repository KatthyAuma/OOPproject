package com.example.BRANCHES;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Optional;

public class StockManagerGUI extends Application {

    private ObservableList<BranchStock> stockData = FXCollections.observableArrayList();
    private TableView<BranchStock> table = new TableView<>();
    private int currentBranchId = -1;
    private String currentBranchName = "";

    @Override
    public void start(Stage stage) {

        TextInputDialog branchPrompt = new TextInputDialog();
        branchPrompt.setTitle("Branch Login");
        branchPrompt.setHeaderText("Enter your Branch ID to continue:");


        Optional<String> result = branchPrompt.showAndWait();
        if (!result.isPresent()) {
            System.exit(0);
        }

        try {
            currentBranchId = Integer.parseInt(result.get());
            switch (currentBranchId) {
                case 1: currentBranchName = "Nairobi Headquarters"; break;
                case 2: currentBranchName = "Nakuru"; break;
                case 3: currentBranchName = "Mombasa"; break;
                case 4: currentBranchName = "Kisumu"; break;
                default:
                    showAlert("Invalid Branch", "Branch ID must be between 1 and 4.");
                    System.exit(0);
            }
        } catch (NumberFormatException ex) {
            showAlert("Input Error", "Please enter a valid number.");
            System.exit(0);
        }

        stage.setTitle("Stock Manager - " + currentBranchName);
        initializeUI(stage);
    }

    private void initializeUI(Stage stage) {
        loadStockData();

        Label title = new Label("Stock Management - " + currentBranchName);
        title.setFont(Font.font("Arial", 24));
        title.setPadding(new Insets(10, 0, 20, 0));

        setupTable();

        // UI controls setup
        TextField drinkField = new TextField();
        drinkField.setPromptText("Drink Name");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Button addBtn = createButton("Add Stock", "#4CAF50");
        Button deleteBtn = createButton("Delete Selected", "#f44336");
        Button refreshBtn = createButton("Refresh", "#2196F3");

        // Button actions
        addBtn.setOnAction(e -> handleAddStock(drinkField, quantityField));
        deleteBtn.setOnAction(e -> handleDeleteStock());
        refreshBtn.setOnAction(e -> loadStockData());

        // Layout setup
        GridPane inputGrid = createInputGrid(drinkField, quantityField, addBtn, deleteBtn, refreshBtn);
        VBox layout = new VBox(15, title, table, inputGrid);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Scene scene = createScene(layout);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void setupTable() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(250);

        TableColumn<BranchStock, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(currentBranchName));

        TableColumn<BranchStock, String> drinkCol = new TableColumn<>("Drink");
        drinkCol.setCellValueFactory(new PropertyValueFactory<>("drinkName"));

        TableColumn<BranchStock, Integer> stockCol = new TableColumn<>("Quantity");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().setAll(branchCol, drinkCol, stockCol);
        table.setItems(stockData);
    }

    private void loadStockData() {
        stockData.clear();
        String sql = "SELECT * FROM stock WHERE branch_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, currentBranchId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                stockData.add(new BranchStock(
                        rs.getInt("branch_id"),
                        rs.getString("drink_name"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load stock data: " + e.getMessage());
        }
    }

    private void handleAddStock(TextField drinkField, TextField quantityField) {
        try {
            String drinkName = drinkField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (drinkName.isEmpty()) {
                showAlert("Input Error", "Please enter a drink name.");
                return;
            }

            addStockToDatabase(currentBranchId, drinkName, quantity);
            loadStockData();

            drinkField.clear();
            quantityField.clear();
        } catch (NumberFormatException ex) {
            showAlert("Input Error", "Quantity must be a valid number.");
        }
    }

    private void addStockToDatabase(int branchId, String drinkName, int quantity) {
        String checkSql = "SELECT quantity FROM stock WHERE branch_id = ? AND drink_name = ?";
        String updateSql = "UPDATE stock SET quantity = quantity + ? WHERE branch_id = ? AND drink_name = ?";
        String insertSql = "INSERT INTO stock (branch_id, drink_name, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, branchId);
            checkStmt.setString(2, drinkName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, quantity);
                    updateStmt.setInt(2, branchId);
                    updateStmt.setString(3, drinkName);
                    updateStmt.executeUpdate();
                }
            } else {
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, branchId);
                    insertStmt.setString(2, drinkName);
                    insertStmt.setInt(3, quantity);
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to add stock: " + e.getMessage());
        }
    }

    private void handleDeleteStock() {
        BranchStock selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            deleteStockFromDatabase(selected);
            loadStockData();
        } else {
            showAlert("Selection Error", "Please select an item to delete.");
        }
    }

    private void deleteStockFromDatabase(BranchStock stock) {
        String sql = "DELETE FROM stock WHERE branch_id = ? AND drink_name = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, stock.getBranchId());
            stmt.setString(2, stock.getDrinkName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete stock: " + e.getMessage());
        }
    }


    private Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        return btn;
    }

    private GridPane createInputGrid(TextField drinkField, TextField quantityField, Button... buttons) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));
        grid.add(new Label("Drink Name:"), 0, 0);
        grid.add(drinkField, 1, 0);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);

        for (int i = 0; i < buttons.length; i++) {
            grid.add(buttons[i], i, 2);
        }
        return grid;
    }

    private Scene createScene(VBox layout) {
        Image backgroundImage = new Image("file:background.jpg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        backgroundView.setOpacity(0.3);

        StackPane root = new StackPane(backgroundView, layout);
        Scene scene = new Scene(root, 800, 500);
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());
        return scene;
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