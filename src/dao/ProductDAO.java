package dao;

import model.Product;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class ProductDAO {

    public void addProduct(Product product) {

        String sql =
                "INSERT INTO products " +
                "(name, category, base_price, stock) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection connection = DBConnection.getConnection();

                PreparedStatement ps =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setDouble(3, product.getBasePrice());
            ps.setInt(4, product.getStock());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {

                try (ResultSet rs = ps.getGeneratedKeys()) {

                    if (rs.next()) {

                        int generatedId = rs.getInt(1);

                        product.setProductid(generatedId);

                        System.out.println(
                                "Product inserted successfully."
                        );

                        System.out.println(
                                "Generated Product ID: " + generatedId
                        );
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println("Failed to insert product.");
            e.printStackTrace();
        }
    }
 // Add these to your existing imports at the top of the file
    // import java.util.ArrayList;
    // import java.util.List;

    public Product getProductById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        Product product = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            
            ps.setInt(1, productId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setProductid(rs.getInt("product_id")); // Adjust to match your setter name
                    product.setName(rs.getString("name"));
                    product.setCategory(rs.getString("category"));
                    product.setBasePrice(rs.getDouble("base_price"));
                    product.setStock(rs.getInt("stock"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch product with ID: " + productId);
            e.printStackTrace();
        }
        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection connection = DBConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product();
                product.setProductid(rs.getInt("product_id")); // Adjust to match your setter name
                product.setName(rs.getString("name"));
                product.setCategory(rs.getString("category"));
                product.setBasePrice(rs.getDouble("base_price"));
                product.setStock(rs.getInt("stock"));
                
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Failed to fetch all products.");
            e.printStackTrace();
        }
        return products;
    }
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, category = ?, base_price = ?, stock = ? WHERE product_id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setString(1, product.getName());
            ps.setString(2, product.getCategory());
            ps.setDouble(3, product.getBasePrice());
            ps.setInt(4, product.getStock());
            ps.setInt(5, product.getProductId()); // Adjust to match your getter name
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Failed to update product.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
             
            ps.setInt(1, productId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.out.println("Failed to delete product. Make sure it isn't linked to an existing order!");
            e.printStackTrace();
            return false;
        }
    }
    
}