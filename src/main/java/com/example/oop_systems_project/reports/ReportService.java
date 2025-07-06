package com.example.oop_systems_project.reports;

import java.sql.SQLException;

public interface ReportService {
    String CustomerwithOrdersReport() throws SQLException;
    String OrdersByBranchReport() throws SQLException;
    String SalesByBranchReport() throws SQLException;
    String LowStockReport(int threshold) throws SQLException;
    String TotalSalesReport() throws SQLException;
}