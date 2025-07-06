package com.example.BRANCHES;

public class BranchStock {
    private final int branchId;
    private final String drinkName;
    private int stock;

    public BranchStock(int branchId, String drinkName, int stock) {
        this.branchId = branchId;
        this.drinkName = drinkName;
        this.stock = stock;
    }

    public int getBranchId() { return branchId; }
    public String getDrinkName() { return drinkName; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}