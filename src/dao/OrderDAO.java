package dao;

import model.Customer;
import model.Order;
import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // CREATE: Insert a new order into the database
    public void addOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, product_id, quantity, unit_price, total_price, order_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // FIX: Extract the integer IDs from the Customer and Product objects
            ps.setInt(1, order.getCustomer().getCustomerId()); // Use the correct ID getter for Customer
            ps.setInt(2, order.getProduct().getProductId());   // Use the correct ID getter for Product
            ps.setInt(3, order.getQuantity());
            ps.setDouble(4, order.getUnitPrice());
            ps.setDouble(5, order.getTotalPrice());
            
            // Convert java.time.LocalDate to java.sql.Date
            ps.setDate(6, Date.valueOf(order.getOrderDate())); 

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        order.setOrderId(generatedId);
                        System.out.println("Order processed successfully. Order ID: " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert order.");
            e.printStackTrace();
        }
    }

    // READ: Fetch all orders for a specific customer (Order History)
    public List<Order> getOrdersByCustomerId(int customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = extractOrderFromResultSet(rs);
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch orders for customer ID: " + customerId);
            e.printStackTrace();
        }
        return orders;
    }

    // READ: Fetch all orders for the Admin Dashboard (Total Revenue / Total Orders)
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = extractOrderFromResultSet(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch all orders.");
            e.printStackTrace();
        }
        return orders;
    }

    // Helper method to keep code DRY (Don't Repeat Yourself)
 // Helper method to keep code DRY (Don't Repeat Yourself)
    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        
        // 1. Create a Customer object and set its ID
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        order.setCustomer(customer);
        
        // 2. Create a Product object and set its ID
        Product product = new Product();
        product.setProductid(rs.getInt("product_id")); // match your Product.java setter name
        order.setProduct(product);
        
        order.setQuantity(rs.getInt("quantity"));
        order.setUnitPrice(rs.getDouble("unit_price"));
        order.setTotalPrice(rs.getDouble("total_price"));
        order.setOrderDate(rs.getDate("order_date").toLocalDate()); 
        
        return order;
    }
}