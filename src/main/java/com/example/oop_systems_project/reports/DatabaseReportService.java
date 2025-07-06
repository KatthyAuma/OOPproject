package com.example.oop_systems_project.reports;

import com.example.BRANCHES.DBConnection;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Date;

public class DatabaseReportService implements ReportService {

    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("KES #,##0.00");

    @Override
    public String CustomerwithOrdersReport() throws SQLException {
        StringBuilder report = new StringBuilder();
        report.append("=== CUSTOMERS WITH ORDERS ===\n");
        report.append("Generated: ").append(new Date()).append("\n\n");

        String sql = "SELECT DISTINCT customer_name, contact_info, COUNT(*) as order_count " +
                "FROM orders GROUP BY customer_name, contact_info ORDER BY customer_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            report.append(String.format("%-20s %-15s %-10s\n", "CUSTOMER", "CONTACT", "ORDERS"));
            report.append("----------------------------------------\n");

            while (rs.next()) {
                report.append(String.format("%-20s %-15s %3d\n",
                        rs.getString("customer_name"),
                        rs.getString("contact_info"),
                        rs.getInt("order_count")));
            }

            if (report.length() < 100) {
                report.append("\nNo customer orders found\n");
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to generate customer report: " + e.getMessage());
        }

        return report.toString();
    }

    @Override
    public String OrdersByBranchReport() throws SQLException {
        StringBuilder report = new StringBuilder();
        report.append("=== ORDERS BY BRANCH ===\n");
        report.append("Generated: ").append(new Date()).append("\n\n");

        String sql = "SELECT branch_name, COUNT(*) as order_count, SUM(ordervalue) as total_sales " +
                "FROM orders GROUP BY branch_name ORDER BY order_count DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            report.append(String.format("%-15s %-10s %-15s\n", "BRANCH", "ORDERS", "TOTAL SALES"));
            report.append("----------------------------------\n");

            while (rs.next()) {
                report.append(String.format("%-15s %5d %15s\n",
                        rs.getString("branch_name"),
                        rs.getInt("order_count"),
                        CURRENCY_FORMAT.format(rs.getDouble("total_sales"))));
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to generate branch orders report: " + e.getMessage());
        }

        return report.toString();
    }

    @Override
    public String SalesByBranchReport() throws SQLException {
        StringBuilder report = new StringBuilder();
        report.append("=== SALES BY BRANCH ===\n");
        report.append("Generated: ").append(new Date()).append("\n\n");

        String sql = "SELECT branch_name, SUM(ordervalue) as total_sales " +
                "FROM orders GROUP BY branch_name ORDER BY total_sales DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            report.append(String.format("%-15s %-15s\n", "BRANCH", "TOTAL SALES"));
            report.append("---------------------------\n");

            while (rs.next()) {
                report.append(String.format("%-15s %15s\n",
                        rs.getString("branch_name"),
                        CURRENCY_FORMAT.format(rs.getDouble("total_sales"))));
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to generate sales report: " + e.getMessage());
        }

        return report.toString();
    }

    @Override
    public String LowStockReport(int threshold) throws SQLException {
        StringBuilder report = new StringBuilder();
        report.append("=== LOW STOCK ALERT ===\n");
        report.append("Threshold: ").append(threshold).append(" units\n");
        report.append("Generated: ").append(new Date()).append("\n\n");

        String sql = "SELECT b.branch_name, s.drink_name, s.quantity " +
                "FROM stock s JOIN branches b ON s.branch_id = b.branch_id " +
                "WHERE s.quantity < ? ORDER BY s.quantity ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, threshold);
            ResultSet rs = stmt.executeQuery();

            report.append(String.format("%-15s %-15s %-10s\n", "BRANCH", "DRINK", "QUANTITY"));
            report.append("----------------------------------\n");

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                report.append(String.format("%-15s %-15s %3d units\n",
                        rs.getString("branch_name"),
                        rs.getString("drink_name"),
                        rs.getInt("quantity")));
            }

            if (!hasResults) {
                report.append("\nAll items are above stock threshold\n");
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to generate low stock report: " + e.getMessage());
        }

        return report.toString();
    }

    @Override
    public String TotalSalesReport() throws SQLException {
        StringBuilder report = new StringBuilder();
        report.append("=== TOTAL SALES REPORT ===\n");
        report.append("Generated: ").append(new Date()).append("\n\n");

        String sql = "SELECT SUM(ordervalue) as total_sales, COUNT(*) as total_orders " +
                "FROM orders";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                double totalSales = rs.getDouble("total_sales");
                int totalOrders = rs.getInt("total_orders");

                report.append(String.format("%-20s %15s\n", "Total Orders:", totalOrders));
                report.append(String.format("%-20s %15s\n", "Total Sales:",
                        CURRENCY_FORMAT.format(totalSales)));
            } else {
                report.append("No sales data available\n");
            }

        } catch (SQLException e) {
            throw new SQLException("Failed to generate total sales report: " + e.getMessage());
        }

        return report.toString();
    }
}