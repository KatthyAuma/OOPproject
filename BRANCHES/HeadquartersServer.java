package server;
import java.io.*;
import java.net.*;
import shared.Order;

public class HeadquartersServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("HQ Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connected to: " + clientSocket.getInetAddress());

                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket socket) {
        try (
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
        ) {
            Order order = (Order) in.readObject();
            System.out.println("Received order from branch: " + order.getBranchName());
            System.out.println("Customer: " + order.getCustomerName());

            out.writeObject("Order received and is being processed at HQ.");
        } catch (Exception e) {
            System.err.println("Client communication error: " + e.getMessage());
        }
    }
}
