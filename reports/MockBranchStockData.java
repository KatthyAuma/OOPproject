package com.example.oop_systems_project.reports;

import java.util.Arrays;
import java.util.List;

public class MockBranchStockData {
    public static List<BranchStock> getMockStock() {
        return Arrays.asList(
                new BranchStock(001, "Coca-Cola", 50),    // HQ Nairobi
                new BranchStock(001, "Fanta", 30),
                new BranchStock(002, "Sprite", 20),       // Kisumu
                new BranchStock(002, "Pepsi", 25),
                new BranchStock(003, "Coca-Cola", 15),    // Eldoret
                new BranchStock(003, "Krest", 10),
                new BranchStock(004, "Fanta", 40),        // Mombasa
                new BranchStock(004, "Pepsi", 35)
        );
    }
}