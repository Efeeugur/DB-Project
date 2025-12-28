package view;

import controller.*;
import util.SwingUtils;
import javax.swing.*;
import java.awt.*;

/**
 * Main application frame for Art School Management System.
 */
public class MainFrame extends JFrame {
    
    private final CardLayout cardLayout;
    private final JPanel contentPanel;
    private final JLabel statusLabel;
    
    private final StudentController studentController;
    private final InstructorController instructorController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    private final AttendanceController attendanceController;
    
    // Panels
    private DashboardPanel dashboardPanel;
    private StudentPanel studentPanel;
    private InstructorPanel instructorPanel;
    private CoursePanel coursePanel;
    private EnrollmentPanel enrollmentPanel;
    private AttendancePanel attendancePanel;
    private PaymentPanel paymentPanel;
    private ReportPanel reportPanel;
    
    public MainFrame(StudentController studentController,
                     InstructorController instructorController,
                     CourseController courseController,
                     EnrollmentController enrollmentController,
                     AttendanceController attendanceController) {
        this.studentController = studentController;
        this.instructorController = instructorController;
        this.courseController = courseController;
        this.enrollmentController = enrollmentController;
        this.attendanceController = attendanceController;
        
        setTitle("Art School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
        
        // Main layout
        setLayout(new BorderLayout());
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Content with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(SwingUtils.BACKGROUND_COLOR);
        
        initializePanels();
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Status bar
        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(SwingUtils.LABEL_FONT);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(statusLabel, BorderLayout.SOUTH);
        
        // Show dashboard by default
        showPanel("dashboard");
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SwingUtils.SECONDARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Title
        JLabel titleLabel = new JLabel("🎨 Art School Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);
        
        // Navigation buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        navPanel.setOpaque(false);
        
        String[] navItems = {"Dashboard", "Students", "Instructors", "Courses", 
                            "Enrollments", "Attendance", "Payments", "Reports"};
        String[] panelNames = {"dashboard", "students", "instructors", "courses",
                              "enrollments", "attendance", "payments", "reports"};
        
        for (int i = 0; i < navItems.length; i++) {
            JButton btn = createNavButton(navItems[i]);
            final String panelName = panelNames[i];
            btn.addActionListener(e -> showPanel(panelName));
            navPanel.add(btn);
        }
        
        header.add(navPanel, BorderLayout.CENTER);
        
        return header;
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SwingUtils.SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 30));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SwingUtils.PRIMARY_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SwingUtils.SECONDARY_COLOR);
            }
        });
        
        return button;
    }
    
    private void initializePanels() {
        dashboardPanel = new DashboardPanel(studentController, instructorController, 
                                           courseController, enrollmentController);
        studentPanel = new StudentPanel(studentController);
        instructorPanel = new InstructorPanel(instructorController);
        coursePanel = new CoursePanel(courseController, instructorController);
        enrollmentPanel = new EnrollmentPanel(enrollmentController, studentController, courseController);
        attendancePanel = new AttendancePanel(attendanceController, enrollmentController, courseController);
        paymentPanel = new PaymentPanel(enrollmentController);
        reportPanel = new ReportPanel(studentController, instructorController, 
                                      courseController, enrollmentController);
        
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(studentPanel, "students");
        contentPanel.add(instructorPanel, "instructors");
        contentPanel.add(coursePanel, "courses");
        contentPanel.add(enrollmentPanel, "enrollments");
        contentPanel.add(attendancePanel, "attendance");
        contentPanel.add(paymentPanel, "payments");
        contentPanel.add(reportPanel, "reports");
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
        setStatus("Viewing: " + panelName.substring(0, 1).toUpperCase() + panelName.substring(1));
        
        // Refresh data when panel is shown
        switch (panelName) {
            case "dashboard" -> dashboardPanel.refresh();
            case "students" -> studentPanel.refreshTable();
            case "instructors" -> instructorPanel.refreshTable();
            case "courses" -> coursePanel.refreshTable();
            case "enrollments" -> enrollmentPanel.refreshTable();
            case "payments" -> paymentPanel.refreshTable();
            case "reports" -> reportPanel.refresh();
        }
    }
    
    public void setStatus(String status) {
        statusLabel.setText(" " + status);
    }
}
