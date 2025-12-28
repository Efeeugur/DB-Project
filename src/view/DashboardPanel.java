package view;

import controller.*;
import util.SwingUtils;
import javax.swing.*;
import java.awt.*;

/**
 * Dashboard panel showing system statistics.
 */
public class DashboardPanel extends JPanel {
    
    private final StudentController studentController;
    private final InstructorController instructorController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    
    private JPanel statsPanel;
    
    public DashboardPanel(StudentController studentController,
                          InstructorController instructorController,
                          CourseController courseController,
                          EnrollmentController enrollmentController) {
        this.studentController = studentController;
        this.instructorController = instructorController;
        this.courseController = courseController;
        this.enrollmentController = enrollmentController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("📊 Dashboard");
        add(title, BorderLayout.NORTH);
        
        // Stats cards
        statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        statsPanel.setOpaque(false);
        add(statsPanel, BorderLayout.CENTER);
        
        // Welcome message
        JPanel welcomePanel = SwingUtils.createCardPanel();
        welcomePanel.setLayout(new BorderLayout(10, 10));
        
        JLabel welcomeTitle = SwingUtils.createHeaderLabel("Welcome to Art School Management System");
        JLabel welcomeText = SwingUtils.createLabel(
            "<html>This system helps you manage students, instructors, courses, enrollments, " +
            "attendance, and payments for your art school.<br><br>" +
            "Use the navigation bar above to access different modules.</html>");
        
        welcomePanel.add(welcomeTitle, BorderLayout.NORTH);
        welcomePanel.add(welcomeText, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(welcomePanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);
        
        refresh();
    }
    
    public void refresh() {
        statsPanel.removeAll();
        
        // Create stat cards
        statsPanel.add(SwingUtils.createStatCard(
            "Total Students",
            String.valueOf(studentController.getStudentCount()),
            SwingUtils.PRIMARY_COLOR
        ));
        
        statsPanel.add(SwingUtils.createStatCard(
            "Total Instructors",
            String.valueOf(instructorController.getInstructorCount()),
            SwingUtils.SUCCESS_COLOR
        ));
        
        statsPanel.add(SwingUtils.createStatCard(
            "Total Courses",
            String.valueOf(courseController.getCourseCount()),
            SwingUtils.WARNING_COLOR
        ));
        
        statsPanel.add(SwingUtils.createStatCard(
            "Active Enrollments",
            String.valueOf(enrollmentController.getActiveEnrollments().size()),
            SwingUtils.SECONDARY_COLOR
        ));
        
        statsPanel.add(SwingUtils.createStatCard(
            "Pending Payments",
            String.valueOf(enrollmentController.getPendingPayments().size()),
            SwingUtils.DANGER_COLOR
        ));
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
}
