package client;
import java.io.*;
import java.net.*;
import java.util.*;
import shared.Order;
import shared.Product;

public class BranchClient {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 5000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            List<Product> productList = new ArrayList<>();
            productList.add(new Product("Coca-Cola", 2, 120.0));
            productList.add(new Product("Pepsi", 1, 110.0));

            Order order = new Order("Kathy", "MOMBASA", productList);

            out.writeObject(order);
            out.flush();

            String response = (String) in.readObject();
            System.out.println("HQ Response: " + response);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with server: " + e.getMessage());
        }
    }
}
