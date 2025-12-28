package util;

/**
 * Application constants and configuration values.
 */
public final class AppConstants {
    
    private AppConstants() {} // Prevent instantiation
    
    // Application Info
    public static final String APP_NAME = "Art School Management System";
    public static final String APP_VERSION = "1.0.0";
    public static final String APP_AUTHOR = "Art School Project Team";
    
    // Window Settings
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;
    public static final int MIN_WINDOW_WIDTH = 1000;
    public static final int MIN_WINDOW_HEIGHT = 700;
    
    // Table Settings
    public static final int TABLE_ROW_HEIGHT = 30;
    public static final int TABLE_HEADER_HEIGHT = 40;
    
    // Form Settings
    public static final int FORM_FIELD_WIDTH = 200;
    public static final int FORM_FIELD_HEIGHT = 30;
    public static final int FORM_LABEL_WIDTH = 120;
    
    // Skill Test Thresholds
    public static final int SKILL_BEGINNER_MAX = 40;
    public static final int SKILL_INTERMEDIATE_MAX = 70;
    public static final int SKILL_ADVANCED_MIN = 71;
    
    // Validation
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PHONE_LENGTH = 15;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String PHONE_REGEX = "^[0-9+\\-\\s()]{7,15}$";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    // Messages - Success
    public static final String MSG_SAVE_SUCCESS = "Record saved successfully!";
    public static final String MSG_UPDATE_SUCCESS = "Record updated successfully!";
    public static final String MSG_DELETE_SUCCESS = "Record deleted successfully!";
    public static final String MSG_OPERATION_SUCCESS = "Operation completed successfully!";
    
    // Messages - Error
    public static final String MSG_SAVE_ERROR = "Failed to save record.";
    public static final String MSG_UPDATE_ERROR = "Failed to update record.";
    public static final String MSG_DELETE_ERROR = "Failed to delete record.";
    public static final String MSG_NOT_FOUND = "Record not found.";
    public static final String MSG_VALIDATION_ERROR = "Please check your input.";
    
    // Messages - Validation
    public static final String MSG_REQUIRED_FIELD = "This field is required.";
    public static final String MSG_INVALID_EMAIL = "Please enter a valid email address.";
    public static final String MSG_INVALID_PHONE = "Please enter a valid phone number.";
    public static final String MSG_INVALID_DATE = "Please enter a valid date (YYYY-MM-DD).";
    public static final String MSG_NAME_TOO_SHORT = "Name must be at least " + MIN_NAME_LENGTH + " characters.";
    public static final String MSG_NAME_TOO_LONG = "Name must not exceed " + MAX_NAME_LENGTH + " characters.";
    
    // Messages - Confirmation
    public static final String MSG_CONFIRM_DELETE = "Are you sure you want to delete this record?";
    public static final String MSG_CONFIRM_EXIT = "Are you sure you want to exit?";
    
    // Database (for future PostgreSQL integration)
    public static final String DB_DEFAULT_HOST = "localhost";
    public static final int DB_DEFAULT_PORT = 5432;
    public static final String DB_DEFAULT_NAME = "artschool";
    public static final String DB_DEFAULT_USER = "postgres";
    
    // File paths
    public static final String CONFIG_FILE = "config.properties";
    public static final String LOG_FILE = "app.log";
    public static final String ERROR_LOG_FILE = "error_log.txt";
}
