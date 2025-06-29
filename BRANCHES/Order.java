package com.example.BRANCHES;

public class Order {
    private String customerName, contactInfo, drinkName, branchName, paymentMethod;
    private int quantity;
    private double orderValue;
    private String orderTime;

    public Order(String customerName, String contactInfo, String drinkName, int quantity, String branchName, String paymentMethod, double orderValue, String orderTime) {
        this.customerName = customerName;
        this.contactInfo = contactInfo;
        this.drinkName = drinkName;
        this.quantity = quantity;
        this.branchName = branchName;
        this.paymentMethod = paymentMethod;
        this.orderValue = orderValue;
        this.orderTime = orderTime;
    }

    // Getters for TableView
    public String getCustomerName() { return customerName; }
    public String getContactInfo() { return contactInfo; }
    public String getDrinkName() { return drinkName; }
    public int getQuantity() { return quantity; }
    public String getBranchName() { return branchName; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getOrderValue() { return orderValue; }
    public String getOrderTime() { return orderTime; }
}
