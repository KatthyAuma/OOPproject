package shared;
import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private String customerName;
    private String branchName;
    private List<Product> products;

    public Order(String customerName, String branchName, List<Product> products) {
        this.customerName = customerName;
        this.branchName = branchName;
        this.products = products;
    }

    // Getters
    public String getCustomerName() { return customerName; }
    public String getBranchName() { return branchName; }
    public List<Product> getProducts() { return products; }
}
