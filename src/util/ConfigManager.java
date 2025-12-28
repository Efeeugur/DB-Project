package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration manager for loading and accessing application settings.
 */
public class ConfigManager {
    
    private static ConfigManager instance;
    private final Properties properties;
    private boolean loaded = false;
    
    private ConfigManager() {
        properties = new Properties();
        loadConfiguration();
    }
    
    /**
     * Gets the singleton instance.
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    /**
     * Loads configuration from properties file.
     */
    private void loadConfiguration() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            
            if (input != null) {
                properties.load(input);
                loaded = true;
                AppLogger.info("Configuration loaded successfully");
            } else {
                AppLogger.warning("config.properties not found, using defaults");
                setDefaults();
            }
        } catch (IOException e) {
            AppLogger.error("Error loading configuration", e);
            setDefaults();
        }
    }
    
    /**
     * Sets default configuration values.
     */
    private void setDefaults() {
        properties.setProperty("app.name", AppConstants.APP_NAME);
        properties.setProperty("app.version", AppConstants.APP_VERSION);
        properties.setProperty("window.width", String.valueOf(AppConstants.WINDOW_WIDTH));
        properties.setProperty("window.height", String.valueOf(AppConstants.WINDOW_HEIGHT));
        properties.setProperty("db.host", AppConstants.DB_DEFAULT_HOST);
        properties.setProperty("db.port", String.valueOf(AppConstants.DB_DEFAULT_PORT));
        properties.setProperty("db.name", AppConstants.DB_DEFAULT_NAME);
        properties.setProperty("default.course.capacity", "20");
    }
    
    /**
     * Gets a string property.
     */
    public String getString(String key) {
        return properties.getProperty(key, "");
    }
    
    /**
     * Gets a string property with default value.
     */
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets an integer property.
     */
    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Gets a boolean property.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Checks if configuration was loaded successfully.
     */
    public boolean isLoaded() {
        return loaded;
    }
    
    // Convenience methods
    public String getAppName() {
        return getString("app.name", AppConstants.APP_NAME);
    }
    
    public String getAppVersion() {
        return getString("app.version", AppConstants.APP_VERSION);
    }
    
    public int getWindowWidth() {
        return getInt("window.width", AppConstants.WINDOW_WIDTH);
    }
    
    public int getWindowHeight() {
        return getInt("window.height", AppConstants.WINDOW_HEIGHT);
    }
    
    public String getDbHost() {
        return getString("db.host", AppConstants.DB_DEFAULT_HOST);
    }
    
    public int getDbPort() {
        return getInt("db.port", AppConstants.DB_DEFAULT_PORT);
    }
    
    public String getDbName() {
        return getString("db.name", AppConstants.DB_DEFAULT_NAME);
    }
    
    public int getDefaultCourseCapacity() {
        return getInt("default.course.capacity", 20);
    }
}
