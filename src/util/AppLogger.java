package util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 * Application logger for centralized logging.
 */
public class AppLogger {
    
    private static final Logger logger = Logger.getLogger(AppConstants.APP_NAME);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static boolean initialized = false;
    
    private AppLogger() {} // Prevent instantiation
    
    /**
     * Initializes the logger with file and console handlers.
     */
    public static synchronized void initialize() {
        if (initialized) return;
        
        try {
            // Remove default handlers
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }
            
            // Console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new CustomFormatter());
            logger.addHandler(consoleHandler);
            
            // File handler
            try {
                FileHandler fileHandler = new FileHandler(AppConstants.LOG_FILE, true);
                fileHandler.setLevel(Level.ALL);
                fileHandler.setFormatter(new CustomFormatter());
                logger.addHandler(fileHandler);
            } catch (IOException e) {
                System.err.println("Could not create log file: " + e.getMessage());
            }
            
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            
            initialized = true;
            info("Logger initialized successfully");
            
        } catch (Exception e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }
    
    /**
     * Logs an info message.
     */
    public static void info(String message) {
        ensureInitialized();
        logger.info(message);
    }
    
    /**
     * Logs a warning message.
     */
    public static void warning(String message) {
        ensureInitialized();
        logger.warning(message);
    }
    
    /**
     * Logs an error message.
     */
    public static void error(String message) {
        ensureInitialized();
        logger.severe(message);
    }
    
    /**
     * Logs an error message with exception.
     */
    public static void error(String message, Throwable throwable) {
        ensureInitialized();
        logger.log(Level.SEVERE, message, throwable);
    }
    
    /**
     * Logs a debug message.
     */
    public static void debug(String message) {
        ensureInitialized();
        logger.fine(message);
    }
    
    /**
     * Logs method entry.
     */
    public static void entering(String className, String methodName) {
        ensureInitialized();
        logger.entering(className, methodName);
    }
    
    /**
     * Logs method exit.
     */
    public static void exiting(String className, String methodName) {
        ensureInitialized();
        logger.exiting(className, methodName);
    }
    
    private static void ensureInitialized() {
        if (!initialized) {
            initialize();
        }
    }
    
    /**
     * Custom log formatter.
     */
    private static class CustomFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(LocalDateTime.now().format(DATE_FORMAT)).append("] ");
            sb.append("[").append(record.getLevel().getName()).append("] ");
            sb.append(record.getMessage());
            sb.append("\n");
            
            if (record.getThrown() != null) {
                sb.append("Exception: ").append(record.getThrown().getMessage()).append("\n");
            }
            
            return sb.toString();
        }
    }
}
