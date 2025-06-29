package com.example.oop_systems_project.reports;

public class MockReportService implements ReportService {

    @Override
    public String CustomerwithOrdersReport() {
        return """
                Customers who made Orders:
                - Sandra Elavaza (0111820845)
                - Kate Elavaza (0726784363)
                - Mercy Too (0114165346)
                """;
    }

    @Override
    public String OrdersByBranchReport() {
        return """
                The Orders by each Branch:

                - Nairobi: 15 orders
                - Kisumu: 10 orders
                - Eldoret: 5 orders
                """;
    }

    @Override
    public String SalesByBranchReport() {
        return """
                Sales by Branch:

                - Nairobi: KES 12,500
                - Kisumu: KES 8,000
                - Eldoret: KES 3,000
                """;
    }

    @Override
    public String LowStockReport(int threshold) {
        return """
                Low Stock Alerts (Below """ + threshold + " units):\n\n" +
                "- Nairobi | Coca Cola → 2 units\n" +
                "- Kisumu | Fanta → 3 units";
    }

    @Override
    public String TotalSalesReport() {
        return "Total Business Sales:\n\nKES 23,500";
    }
}
