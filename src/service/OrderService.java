package service;

import dao.CustomerDAO;
import dao.ProductDAO;
import model.Customer;
import model.Order;
import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;

public class OrderService {

    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;
    private final InventoryService inventoryService;
    private final PricingEngine pricingEngine;

    // Inject all dependencies through the constructor
    public OrderService(ProductDAO productDAO, CustomerDAO customerDAO, 
                        InventoryService inventoryService, PricingEngine pricingEngine) {
        this.productDAO = productDAO;
        this.customerDAO = customerDAO;
        this.inventoryService = inventoryService;
        this.pricingEngine = pricingEngine;
    }

    /**
     * Processes an order with full Transaction Management (Rollback on failure)
     */
    public void placeOrder(int customerId, int productId, int quantity) {
        
        // 1. Fetch entities
        Customer customer = customerDAO.getCustomerById(customerId);
        Product product = productDAO.getProductById(productId);

        if (customer == null || product == null) {
            System.out.println("Order failed: Invalid Customer ID or Product ID.");
            return;
        }

        // 2. Business Logic: Check stock
        if (!inventoryService.isStockAvailable(productId, quantity)) {
            System.out.println("Order failed: Insufficient stock for " + product.getName());
            return;
        }

        // 3. Business Logic: Calculate dynamic price
        double finalUnitPrice = pricingEngine.calculateFinalUnitPrice(product, customer, quantity);
        double totalPrice = finalUnitPrice * quantity;

        // 4. TRANSACTION MANAGEMENT BLOCK
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            
            // Turn off auto-commit to start the transaction
            conn.setAutoCommit(false); 

            // Step A: Insert into Orders table
            String insertOrderSql = "INSERT INTO orders (customer_id, product_id, quantity, unit_price, total_price, order_date) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, customerId);
                orderStmt.setInt(2, productId);
                orderStmt.setInt(3, quantity);
                orderStmt.setDouble(4, finalUnitPrice);
                orderStmt.setDouble(5, totalPrice);
                orderStmt.setDate(6, Date.valueOf(LocalDate.now()));
                
                orderStmt.executeUpdate();
                
                // Print the generated Order ID
                var rs = orderStmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("Order Created! ID: " + rs.getInt(1));
                }
            }

            // Step B: Update Inventory in Products table
            String updateStockSql = "UPDATE products SET stock = stock - ? WHERE product_id = ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                stockStmt.setInt(1, quantity);
                stockStmt.setInt(2, productId);
                stockStmt.executeUpdate();
            }

            // Step C: If both succeeded, COMMIT the transaction!
            conn.commit();
            System.out.println("Transaction Committed: Successfully purchased " + quantity + "x " + product.getName() + " for $" + totalPrice);

        } catch (SQLException e) {
            System.out.println("A database error occurred! Rolling back the transaction...");
            try {
                if (conn != null) {
                    // UNDO EVERYTHING if anything failed
                    conn.rollback(); 
                    System.out.println("Rollback successful. No phantom data was saved.");
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Clean up: Reset auto-commit and close connection
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}