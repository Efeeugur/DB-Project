package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection manager for PostgreSQL.
 * Reads configuration from application.properties.
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private String url;
    private String user;
    private String password;
    
    private DatabaseConnection() {
        loadConfiguration();
    }
    
    /**
     * Loads database configuration from application.properties.
     */
    private void loadConfiguration() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            if (input == null) {
                System.err.println("Unable to find application.properties, using defaults");
                useDefaults();
                return;
            }
            
            props.load(input);
            
            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "5432");
            String dbName = props.getProperty("db.name", "DB-Project");
            this.user = props.getProperty("db.username", "art_school_user");
            this.password = props.getProperty("db.password", "ArtSchool2024!");
            
            this.url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            
            AppLogger.info("Database configuration loaded successfully");
            AppLogger.info("Database URL: " + url);
            AppLogger.info("Database User: " + user);
            
        } catch (IOException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            useDefaults();
        }
    }
    
    /**
     * Uses default configuration values.
     */
    private void useDefaults() {
        this.url = "jdbc:postgresql://localhost:5432/DB-Project";
        this.user = "art_school_user";
        this.password = "ArtSchool2024!";
        AppLogger.warning("Using default database configuration");
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
     * Configures database connection parameters manually.
     */
    public void configure(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        AppLogger.info("Database configuration updated manually");
    }
    
    /**
     * Gets a new database connection.
     * Note: Caller is responsible for closing the connection.
     */
    public Connection getConnection() throws SQLException {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
        
        Connection conn = DriverManager.getConnection(url, user, password);
        AppLogger.debug("New database connection established");
        return conn;
    }
    
    /**
     * Tests database connection.
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean valid = conn.isValid(5);
            if (valid) {
                AppLogger.info("Database connection test: SUCCESS");
            } else {
                AppLogger.error("Database connection test: FAILED (invalid)");
            }
            return valid;
        } catch (SQLException e) {
            AppLogger.error("Database connection test FAILED: " + e.getMessage());
            System.err.println("Database connection failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Initializes database schema (creates tables if they don't exist).
     */
    public void initializeSchema() throws SQLException {
        // Schema is already created via schema.sql
        // This method can be used for migrations in the future
        AppLogger.info("Database schema already initialized");
    }
    
    // Getters
    public String getUrl() { return url; }
    public String getUser() { return user; }
}
