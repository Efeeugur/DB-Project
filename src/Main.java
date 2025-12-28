import controller.*;
import model.dao.*;
import model.dao.impl.*;
import view.MainFrame;
import javax.swing.*;

/**
 * Main entry point for the Art School Management System.
 * Now uses Java Swing GUI instead of console.
 */
public class Main {
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default Look and Feel
        }
        
        // Initialize DAOs (In-Memory implementation)
        StudentDAOImpl studentDAO = new StudentDAOImpl();
        InstructorDAOImpl instructorDAO = new InstructorDAOImpl();
        CourseDAOImpl courseDAO = new CourseDAOImpl();
        EnrollmentDAOImpl enrollmentDAO = new EnrollmentDAOImpl();
        SessionDAOImpl sessionDAO = new SessionDAOImpl();
        AttendanceDAOImpl attendanceDAO = new AttendanceDAOImpl();
        PaymentDAOImpl paymentDAO = new PaymentDAOImpl();
        SkillTestDAOImpl skillTestDAO = new SkillTestDAOImpl();
        
        // Link DAOs for relationship queries
        courseDAO.setEnrollmentDAO(enrollmentDAO);
        
        // Initialize Controllers
        StudentController studentController = new StudentController(studentDAO, skillTestDAO);
        InstructorController instructorController = new InstructorController(instructorDAO);
        CourseController courseController = new CourseController(courseDAO, instructorDAO, sessionDAO);
        EnrollmentController enrollmentController = new EnrollmentController(
            enrollmentDAO, studentDAO, courseDAO, paymentDAO);
        AttendanceController attendanceController = new AttendanceController(
            attendanceDAO, enrollmentDAO, sessionDAO);
        
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
        });
    }
}
