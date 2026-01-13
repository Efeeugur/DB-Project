import controller.*;
import model.dao.*;
import model.dao.postgresql.*;
import view.MainFrame;
import util.GlobalExceptionHandler;
import util.DatabaseConnection;
import util.AppLogger;
import javax.swing.*;

/**
 * Main entry point for the Art School Management System.
 * Uses PostgreSQL database for data persistence.
 */
public class Main {
    
    public static void main(String[] args) {
        // Install global exception handler
        GlobalExceptionHandler.install();
        
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
            System.err.println("2. Database 'BD-Project' exists");
            System.err.println("3. User 'art_school_user' has proper permissions");
            System.err.println("4. application.properties is configured correctly");
            System.exit(1);
        }
        
        AppLogger.info("=== Art School Management System ===");
        AppLogger.info("Database connection established successfully");
        
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default Look and Feel
        }
        
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
}

