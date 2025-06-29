package com.example.oop_systems_project.reports;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;
import java.util.stream.Collectors;

public class StockManagerGUI extends Application {

    private final ObservableList<BranchStock> allStockData = FXCollections.observableArrayList(
            MockBranchStockData.getMockStock()
    );

    private final Map<Integer, String> branchMap = Map.of(
            001, "Nairobi HQ",
            002, "Kisumu",
            003, "Eldoret",
            004, "Mombasa"
    );

    private int currentBranchId = -1;

    @Override
    public void start(Stage stage) {
        TextInputDialog branchPrompt = new TextInputDialog();
        branchPrompt.setTitle("Branch Login");
        branchPrompt.setHeaderText("Enter your Branch ID to continue:");
        branchPrompt.setContentText("Branch ID:");

        Optional<String> result = branchPrompt.showAndWait();
        if (result.isEmpty()) {
            System.exit(0);
        }

        try {
            currentBranchId = Integer.parseInt(result.get());
            if (!branchMap.containsKey(currentBranchId)) {
                showAlert("Invalid Branch", "Branch ID not recognized.");
                System.exit(0);
            }
        } catch (NumberFormatException ex) {
            showAlert("Input Error", "Please enter a valid number.");
            System.exit(0);
        }

        stage.setTitle("Stock Manager - " + branchMap.get(currentBranchId));

        Label title = new Label("Stock Management - " + branchMap.get(currentBranchId));
        title.setFont(Font.font("Arial", 24));
        title.setPadding(new Insets(10, 0, 20, 0));

        TableView<BranchStock> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(250);
        table.setStyle("""
                -fx-background-color: rgba(245, 245, 245, 0.3);
                -fx-background-radius: 8;
                -fx-control-inner-background: rgba(245, 245, 245, 0.5);
                -fx-text-fill: black;
                -fx-table-cell-border-color: transparent;
                -fx-table-header-border-color: transparent;
                """);

        TableColumn<BranchStock, String> branchNameCol = new TableColumn<>("Branch Name");
        branchNameCol.setCellValueFactory(data -> {
            int branchId = data.getValue().getBranchId();
            String name = branchMap.getOrDefault(branchId, "Unknown");
            return new ReadOnlyStringWrapper(name);
        });

        TableColumn<BranchStock, String> drinkNameCol = new TableColumn<>("Drink Name");
        drinkNameCol.setCellValueFactory(new PropertyValueFactory<>("drinkName"));

        TableColumn<BranchStock, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        table.getColumns().addAll(branchNameCol, drinkNameCol, stockCol);

        ObservableList<BranchStock> stockData;
        if (currentBranchId == 001) {
            stockData = allStockData;
        } else {
            stockData = FXCollections.observableArrayList(
                    allStockData.stream()
                            .filter(bs -> bs.getBranchId() == currentBranchId)
                            .collect(Collectors.toList())
            );
        }

        table.setItems(stockData);

        TextField drinkField = new TextField();
        drinkField.setPromptText("Drink Name");
        drinkField.setMaxWidth(150);

        TextField stockField = new TextField();
        stockField.setPromptText("Stock");
        stockField.setMaxWidth(150);

        Button addBtn = new Button("Add Stock");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        addBtn.setOnAction(e -> {
            try {
                String drinkName = drinkField.getText();
                int stock = Integer.parseInt(stockField.getText());
                BranchStock newStock = new BranchStock(currentBranchId, drinkName, stock);
                allStockData.add(newStock);

                if (currentBranchId == 001 || newStock.getBranchId() == currentBranchId) {
                    stockData.add(newStock);
                }

                drinkField.clear();
                stockField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Stock must be a number.");
            }
        });

        deleteBtn.setOnAction(e -> {
            BranchStock selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                allStockData.remove(selected);
                stockData.remove(selected);
            } else {
                showAlert("Selection Error", "Please select an item to delete.");
            }
        });

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10));
        inputGrid.add(new Label("Drink Name:"), 0, 0);
        inputGrid.add(drinkField, 1, 0);
        inputGrid.add(new Label("Stock:"), 0, 1);
        inputGrid.add(stockField, 1, 1);
        inputGrid.add(addBtn, 0, 2);
        inputGrid.add(deleteBtn, 1, 2);

        VBox layout = new VBox(15, title, table, inputGrid);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        Image backgroundImage = new Image(getClass().getResource("/Stock5.jpeg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setPreserveRatio(false);
        //backgroundView.setOpacity(0.25);

        StackPane root = new StackPane(backgroundView, layout);
        Scene scene = new Scene(root, 800, 500);
        backgroundView.fitWidthProperty().bind(scene.widthProperty());
        backgroundView.fitHeightProperty().bind(scene.heightProperty());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
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
