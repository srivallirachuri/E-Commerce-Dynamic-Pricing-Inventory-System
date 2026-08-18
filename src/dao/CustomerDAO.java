package dao;

import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // CREATE: Adds a new customer to the database
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, membership) VALUES (?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getMembership()); // e.g., 'Regular', 'Silver', 'Gold'

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        customer.setCustomerId(generatedId);
                        System.out.println("Customer added successfully with ID: " + generatedId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to insert customer.");
            e.printStackTrace();
        }
    }

    // READ: Fetch a specific customer to check their membership for discounts
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        Customer customer = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setName(rs.getString("name"));
                    customer.setMembership(rs.getString("membership"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch customer with ID: " + customerId);
            e.printStackTrace();
        }
        return customer;
    }

    // READ: Fetch all customers for the Admin Console
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setMembership(rs.getString("membership"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch all customers.");
            e.printStackTrace();
        }
        return customers;
    }
}