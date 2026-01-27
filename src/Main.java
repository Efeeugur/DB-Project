import controller.*;
import model.dao.*;
import model.dao.postgresql.*;
import view.MainFrame;
import util.GlobalExceptionHandler;
import util.DatabaseConnection;
import util.AppLogger;
import javax.swing.*;
import java.awt.*;
import com.formdev.flatlaf.FlatIntelliJLaf;

/**
 * Main entry point for the Art School Management System.
 * Uses PostgreSQL database for data persistence.
 */
public class Main {
    
    public static void main(String[] args) {
        // Install global exception handler
        GlobalExceptionHandler.install();
        
        // Configure FlatLaf modern theme BEFORE any UI creation
        configureFlatLafTheme();
        
        // Test database connection
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        if (!dbConnection.testConnection()) {
            JOptionPane.showMessageDialog(null,
                "Veritabanı bağlantısı başarısız!\n" +
                "Lütfen PostgreSQL'in çalıştığından ve yapılandırmanın doğru olduğundan emin olun.\n\n" +
                "Detaylar için log dosyasını kontrol edin.",
                "Veritabanı Hatası",
                JOptionPane.ERROR_MESSAGE);
            
            System.err.println("Database connection failed. Please check:");
            System.err.println("1. PostgreSQL is running");
            System.err.println("2. Database 'DB-Project' exists");
            System.err.println("3. User 'art_school_user' has proper permissions");
            System.err.println("4. application.properties is configured correctly");
            System.exit(1);
        }
        
        AppLogger.info("=== Art School Management System ===");
        AppLogger.info("Database connection established successfully");
        
        // Initialize DAOs (PostgreSQL implementation)
        StudentDAO studentDAO = new StudentDAOPostgreSQL();
        InstructorDAO instructorDAO = new InstructorDAOPostgreSQL();
        CourseDAO courseDAO = new CourseDAOPostgreSQL();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAOPostgreSQL();
        SessionDAO sessionDAO = new SessionDAOPostgreSQL();
        AttendanceDAO attendanceDAO = new AttendanceDAOPostgreSQL();
        PaymentDAO paymentDAO = new PaymentDAOPostgreSQL();
        SkillTestDAO skillTestDAO = new SkillTestDAOPostgreSQL();
        
        AppLogger.info("All DAOs initialized with PostgreSQL");
        
        // Initialize Controllers
        StudentController studentController = new StudentController(studentDAO, skillTestDAO);
        InstructorController instructorController = new InstructorController(instructorDAO);
        CourseController courseController = new CourseController(courseDAO, instructorDAO, sessionDAO);
        courseController.setEnrollmentDAO(enrollmentDAO); // For capacity validation
        EnrollmentController enrollmentController = new EnrollmentController(
            enrollmentDAO, studentDAO, courseDAO, paymentDAO);
        AttendanceController attendanceController = new AttendanceController(
            attendanceDAO, enrollmentDAO, sessionDAO);
        
        AppLogger.info("All controllers initialized");
        
        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(
                studentController,
                instructorController,
                courseController,
                enrollmentController,
                attendanceController
            );
            mainFrame.setVisible(true);
            AppLogger.info("Application GUI started");
        });
    }
    
    /**
     * Configures FlatLaf with modern LIGHT theme and custom styling.
     */
    private static void configureFlatLafTheme() {
        try {
            // Modern accent color (vibrant blue)
            Color accentColor = new Color(0, 120, 215);
            Color accentHover = new Color(30, 140, 230);
            
            // Light mode background layers
            Color bgLight = new Color(255, 255, 255);       // White - main
            Color bgMedium = new Color(245, 245, 245);      // Light gray - cards
            Color bgDark = new Color(235, 235, 235);        // Darker gray - headers
            Color bgInteractive = new Color(250, 250, 250); // Input fields
            
            // Set FlatLaf properties BEFORE installing the Look and Feel
            UIManager.put("@accentColor", accentColor);
            
            // Install FlatIntelliJLaf (modern light theme)
            FlatIntelliJLaf.setup();
            
            // === DEPTH & LAYERS ===
            UIManager.put("Panel.background", bgMedium);
            UIManager.put("Component.arc", 10);
            UIManager.put("Button.arc", 10);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("Component.focusWidth", 2);
            UIManager.put("Component.innerFocusWidth", 1);
            UIManager.put("Component.focusColor", accentColor);
            
            // === BUTTON STYLING ===
            UIManager.put("Button.background", bgLight);
            UIManager.put("Button.hoverBackground", new Color(230, 230, 230));
            UIManager.put("Button.pressedBackground", new Color(220, 220, 220));
            UIManager.put("Button.default.background", accentColor);
            UIManager.put("Button.default.foreground", Color.WHITE);
            UIManager.put("Button.default.hoverBackground", accentHover);
            UIManager.put("Button.default.focusedBackground", accentColor);
            UIManager.put("Button.default.boldText", true);
            
            // === INPUT FIELDS ===
            UIManager.put("TextField.background", bgLight);
            UIManager.put("TextField.focusedBackground", Color.WHITE);
            UIManager.put("TextField.placeholderForeground", new Color(150, 150, 150));
            UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            
            UIManager.put("ComboBox.background", bgLight);
            UIManager.put("ComboBox.buttonStyle", "button");
            UIManager.put("ComboBox.buttonBackground", new Color(240, 240, 240));
            UIManager.put("ComboBox.buttonArrowColor", new Color(100, 100, 100));
            
            // === TABLE STYLING ===
            UIManager.put("Table.background", bgLight);
            UIManager.put("Table.alternateRowColor", new Color(248, 248, 250));
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false);
            UIManager.put("Table.intercellSpacing", new Dimension(0, 1));
            UIManager.put("Table.selectionBackground", accentColor);
            UIManager.put("Table.selectionForeground", Color.WHITE);
            UIManager.put("Table.gridColor", new Color(230, 230, 230));
            
            UIManager.put("TableHeader.background", bgDark);
            UIManager.put("TableHeader.foreground", new Color(60, 60, 60));
            UIManager.put("TableHeader.separatorColor", new Color(220, 220, 220));
            UIManager.put("TableHeader.bottomSeparatorColor", accentColor);
            
            // === SCROLLPANE & SCROLLBAR ===
            UIManager.put("ScrollPane.background", bgMedium);
            UIManager.put("ScrollBar.width", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
            UIManager.put("ScrollBar.track", bgMedium);
            UIManager.put("ScrollBar.thumb", new Color(200, 200, 200));
            UIManager.put("ScrollBar.hoverThumbColor", new Color(180, 180, 180));
            
            // === TABBED PANE ===
            UIManager.put("TabbedPane.selectedBackground", bgLight);
            UIManager.put("TabbedPane.focusColor", accentColor);
            UIManager.put("TabbedPane.underlineColor", accentColor);
            UIManager.put("TabbedPane.hoverColor", new Color(240, 240, 240));
            UIManager.put("TabbedPane.tabHeight", 36);
            UIManager.put("TabbedPane.tabInsets", new Insets(8, 16, 8, 16));
            
            // === LABELS & TEXT ===
            UIManager.put("Label.foreground", new Color(50, 50, 50));
            UIManager.put("h1.font", new Font("Segoe UI", Font.BOLD, 24));
            UIManager.put("h2.font", new Font("Segoe UI", Font.BOLD, 18));
            UIManager.put("h3.font", new Font("Segoe UI", Font.BOLD, 14));
            
            // === SEPARATOR ===
            UIManager.put("Separator.foreground", new Color(220, 220, 220));
            
            // === TOOLTIP ===
            UIManager.put("ToolTip.background", new Color(60, 60, 60));
            UIManager.put("ToolTip.foreground", Color.WHITE);
            UIManager.put("ToolTip.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentColor, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));
            
            // === OPTION PANE ===
            UIManager.put("OptionPane.background", bgMedium);
            UIManager.put("OptionPane.messageForeground", new Color(50, 50, 50));
            
            // === PROGRESS BAR ===
            UIManager.put("ProgressBar.arc", 999);
            UIManager.put("ProgressBar.foreground", accentColor);
            UIManager.put("ProgressBar.background", bgDark);
            
            AppLogger.info("FlatLaf IntelliJ Light theme initialized");
            
        } catch (Exception ex) {
            System.err.println("FlatLaf theme could not be loaded: " + ex.getMessage());
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default
            }
        }
    }
}
