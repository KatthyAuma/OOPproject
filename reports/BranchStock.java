package com.example.oop_systems_project.reports;

public class BranchStock {
    private int branchId;
    private String drinkName;
    private int stock;

    public BranchStock(int branchId, String drinkName, int stock) {
        this.branchId = branchId;
        this.drinkName = drinkName;
        this.stock = stock;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
