package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database Configuration
    private static final String URL =
            "jdbc:mysql://localhost:3306/ecommerce_inventory_db";

    private static final String USER = "root";

    private static final String PASSWORD = "srivalli_16";

    // Private constructor prevents object creation
    private DBConnection() {
    }

    // Returns a database connection
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }
}