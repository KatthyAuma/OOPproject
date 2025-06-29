package com.example.BRANCHES;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class OrderColumns {
    public static List<TableColumn<Order, ?>> getColumns() {
        TableColumn<Order, String> nameCol = new TableColumn<>("Customer");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<Order, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));

        TableColumn<Order, String> drinkCol = new TableColumn<>("Drink");
        drinkCol.setCellValueFactory(new PropertyValueFactory<>("drinkName"));

        TableColumn<Order, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Order, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));

        TableColumn<Order, String> payCol = new TableColumn<>("Payment");
        payCol.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));

        TableColumn<Order, Double> valueCol = new TableColumn<>("Order Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("orderValue"));

        TableColumn<Order, String> timeCol = new TableColumn<>("Order Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("orderTime"));

        return FXCollections.observableArrayList(nameCol, contactCol, drinkCol, qtyCol, branchCol, payCol, valueCol, timeCol);
    }
}
