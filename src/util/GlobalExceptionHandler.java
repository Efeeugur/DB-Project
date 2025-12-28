package util;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Global exception handler for the application.
 * Catches all uncaught exceptions and displays user-friendly error messages.
 */
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    
    private static final String LOG_FILE = "error_log.txt";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // Log the exception
        logException(t, e);
        
        // Show user-friendly error dialog
        showErrorDialog(e);
    }
    
    /**
     * Logs exception details to a file.
     */
    private void logException(Thread t, Throwable e) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            pw.println("=".repeat(60));
            pw.println("Timestamp: " + LocalDateTime.now().format(DATE_FORMAT));
            pw.println("Thread: " + t.getName());
            pw.println("Exception: " + e.getClass().getName());
            pw.println("Message: " + e.getMessage());
            pw.println("Stack Trace:");
            e.printStackTrace(pw);
            pw.println();
            
        } catch (IOException ioException) {
            System.err.println("Failed to write to log file: " + ioException.getMessage());
        }
        
        // Also print to console
        System.err.println("[ERROR] Uncaught exception in thread: " + t.getName());
        e.printStackTrace();
    }
    
    /**
     * Shows a user-friendly error dialog.
     */
    private void showErrorDialog(Throwable e) {
        SwingUtilities.invokeLater(() -> {
            String title = "Application Error";
            String message = getErrorMessage(e);
            
            // Create custom error panel
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            
            JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.errorIcon"));
            panel.add(iconLabel, BorderLayout.WEST);
            
            JPanel messagePanel = new JPanel();
            messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
            
            JLabel mainMessage = new JLabel("<html><b>An unexpected error occurred.</b></html>");
            messagePanel.add(mainMessage);
            messagePanel.add(Box.createVerticalStrut(10));
            
            JLabel errorDetail = new JLabel("<html><i>" + escapeHtml(message) + "</i></html>");
            errorDetail.setForeground(Color.GRAY);
            messagePanel.add(errorDetail);
            
            panel.add(messagePanel, BorderLayout.CENTER);
            
            // Show details button
            String stackTrace = getStackTraceString(e);
            
            Object[] options = {"OK", "Show Details"};
            int result = JOptionPane.showOptionDialog(
                null,
                panel,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
            );
            
            if (result == 1) {
                showStackTraceDialog(stackTrace);
            }
        });
    }
    
    /**
     * Shows a dialog with the full stack trace.
     */
    private void showStackTraceDialog(String stackTrace) {
        JTextArea textArea = new JTextArea(stackTrace);
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(
            null,
            scrollPane,
            "Error Details",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Gets a user-friendly error message.
     */
    private String getErrorMessage(Throwable e) {
        if (e.getMessage() != null && !e.getMessage().isEmpty()) {
            return e.getMessage();
        }
        return e.getClass().getSimpleName();
    }
    
    /**
     * Converts stack trace to string.
     */
    private String getStackTraceString(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    
    /**
     * Escapes HTML special characters.
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
    }
    
    /**
     * Installs the global exception handler.
     */
    public static void install() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        
        // Set as default handler for all threads
        Thread.setDefaultUncaughtExceptionHandler(handler);
        
        // Also handle EDT exceptions
        System.setProperty("sun.awt.exception.handler", GlobalExceptionHandler.class.getName());
        
        // For older Java versions, also set EDT handler
        SwingUtilities.invokeLater(() -> {
            Thread.currentThread().setUncaughtExceptionHandler(handler);
        });
        
        System.out.println("[INFO] Global exception handler installed.");
    }
    
    /**
     * Required for sun.awt.exception.handler property.
     */
    public void handle(Throwable e) {
        uncaughtException(Thread.currentThread(), e);
    }
}
