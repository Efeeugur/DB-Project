package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection manager for PostgreSQL.
 * This class will be used when PostgreSQL is integrated.
 */
public class DatabaseConnection {
    
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/artschool";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "postgres";
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private String url;
    private String user;
    private String password;
    
    private DatabaseConnection() {
        this.url = DEFAULT_URL;
        this.user = DEFAULT_USER;
        this.password = DEFAULT_PASSWORD;
    }
    
    /**
     * Gets the singleton instance.
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Configures database connection parameters.
     */
    public void configure(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
    
    /**
     * Gets database connection.
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
    
    /**
     * Tests database connection.
     */
    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean valid = conn.isValid(5);
            return valid;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Closes database connection.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    // Getters
    public String getUrl() { return url; }
    public String getUser() { return user; }
}
