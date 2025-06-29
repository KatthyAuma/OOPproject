package com.example.oop_systems_project.reports;

public interface ReportService {
    String CustomerwithOrdersReport();
    String OrdersByBranchReport();
    String SalesByBranchReport();
    String LowStockReport(int threshold);
    String TotalSalesReport();
}
