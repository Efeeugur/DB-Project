package view;

import controller.AttendanceController;
import controller.CourseController;
import controller.EnrollmentController;
import model.entity.Attendance;
import model.entity.Course;
import model.entity.Enrollment;
import model.entity.Session;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for attendance management.
 */
public class AttendancePanel extends JPanel {
    
    private final AttendanceController attendanceController;
    private final EnrollmentController enrollmentController;
    private final CourseController courseController;
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JComboBox<String> cmbEnrollment;
    private JComboBox<String> cmbSession;
    private JComboBox<Attendance.AttendanceStatus> cmbStatus;
    private JTextField txtNotes;
    
    public AttendancePanel(AttendanceController attendanceController,
                           EnrollmentController enrollmentController,
                           CourseController courseController) {
        this.attendanceController = attendanceController;
        this.enrollmentController = enrollmentController;
        this.courseController = courseController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("✓ Attendance Management");
        add(title, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setOpaque(false);
        
        // Left: Form
        mainPanel.add(createFormPanel(), BorderLayout.WEST);
        
        // Center: Table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createFormPanel() {
        JPanel formCard = SwingUtils.createCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(380, 0));
        
        JLabel formTitle = SwingUtils.createHeaderLabel("Record Attendance");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(20));
        
        // Combo boxes
        cmbEnrollment = new JComboBox<>();
        cmbEnrollment.setFont(SwingUtils.LABEL_FONT);
        cmbEnrollment.setPreferredSize(new Dimension(220, 30));
        
        cmbSession = new JComboBox<>();
        cmbSession.setFont(SwingUtils.LABEL_FONT);
        cmbSession.setPreferredSize(new Dimension(220, 30));
        
        cmbStatus = SwingUtils.createComboBox(Attendance.AttendanceStatus.values());
        txtNotes = SwingUtils.createTextField();
        
        refreshEnrollmentComboBox();
        
        formCard.add(SwingUtils.createFormRow("Enrollment:", cmbEnrollment));
        formCard.add(SwingUtils.createFormRow("Session:", cmbSession));
        formCard.add(SwingUtils.createFormRow("Status:", cmbStatus));
        formCard.add(SwingUtils.createFormRow("Notes:", txtNotes));
        
        formCard.add(Box.createVerticalStrut(20));
        
        // Record button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        
        JButton btnRecord = SwingUtils.createSuccessButton("Record");
        JButton btnLoadSessions = SwingUtils.createPrimaryButton("Load Sessions");
        
        btnRecord.addActionListener(e -> recordAttendance());
        btnLoadSessions.addActionListener(e -> loadSessions());
        
        btnPanel.add(btnRecord);
        btnPanel.add(btnLoadSessions);
        
        formCard.add(btnPanel);
        
        // Attendance percentage section
        formCard.add(Box.createVerticalStrut(30));
        JLabel calcTitle = SwingUtils.createHeaderLabel("Calculate Attendance");
        calcTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(calcTitle);
        formCard.add(Box.createVerticalStrut(10));
        
        JButton btnCalc = SwingUtils.createPrimaryButton("Calculate %");
        btnCalc.addActionListener(e -> calculateAttendance());
        
        JPanel calcPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        calcPanel.setOpaque(false);
        calcPanel.add(btnCalc);
        formCard.add(calcPanel);
        
        return formCard;
    }
    
    private JPanel createTablePanel() {
        JPanel tableCard = SwingUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));
        
        JLabel tableTitle = SwingUtils.createHeaderLabel("Attendance Records");
        tableCard.add(tableTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Enrollment ID", "Session ID", "Status", "Notes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        JButton btnViewByEnrollment = SwingUtils.createPrimaryButton("View Selected");
        btnViewByEnrollment.addActionListener(e -> viewAttendanceByEnrollment());
        bottomPanel.add(btnViewByEnrollment);
        
        tableCard.add(bottomPanel, BorderLayout.SOUTH);
        
        return tableCard;
    }
    
    private void refreshEnrollmentComboBox() {
        cmbEnrollment.removeAllItems();
        cmbEnrollment.addItem("-- Select Enrollment --");
        
        List<Enrollment> enrollments = enrollmentController.getActiveEnrollments();
        for (Enrollment e : enrollments) {
            String courseName = courseController.getCourseById(e.getCourseId())
                .map(Course::getName).orElse("Unknown");
            cmbEnrollment.addItem(e.getId() + " - " + courseName + " (Student: " + e.getStudentId() + ")");
        }
    }
    
    private void loadSessions() {
        String selected = (String) cmbEnrollment.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            SwingUtils.showWarning(this, "Please select an enrollment first.");
            return;
        }
        
        int enrollmentId = Integer.parseInt(selected.split(" - ")[0]);
        
        // Get course ID from enrollment
        enrollmentController.getActiveEnrollments().stream()
            .filter(e -> e.getId() == enrollmentId)
            .findFirst()
            .ifPresent(enrollment -> {
                cmbSession.removeAllItems();
                cmbSession.addItem("-- Select Session --");
                
                List<Session> sessions = courseController.getSessionsByCourse(enrollment.getCourseId());
                if (sessions.isEmpty()) {
                    SwingUtils.showInfo(this, "No sessions found for this course.\nPlease create sessions first.");
                } else {
                    for (Session s : sessions) {
                        cmbSession.addItem(s.getId() + " - " + s.getSessionDate() + " - " + s.getTopic());
                    }
                }
            });
    }
    
    private int getSelectedEnrollmentId() {
        String selected = (String) cmbEnrollment.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            return -1;
        }
        return Integer.parseInt(selected.split(" - ")[0]);
    }
    
    private int getSelectedSessionId() {
        String selected = (String) cmbSession.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            return -1;
        }
        return Integer.parseInt(selected.split(" - ")[0]);
    }
    
    private void recordAttendance() {
        int enrollmentId = getSelectedEnrollmentId();
        int sessionId = getSelectedSessionId();
        
        if (enrollmentId < 0) {
            SwingUtils.showWarning(this, "Please select an enrollment.");
            return;
        }
        if (sessionId < 0) {
            SwingUtils.showWarning(this, "Please select a session.");
            return;
        }
        
        try {
            Attendance attendance = attendanceController.recordAttendance(
                enrollmentId,
                sessionId,
                (Attendance.AttendanceStatus) cmbStatus.getSelectedItem(),
                txtNotes.getText()
            );
            SwingUtils.showSuccess(this, "Attendance recorded! ID: " + attendance.getId());
            viewAttendanceByEnrollment();
        } catch (IllegalArgumentException e) {
            SwingUtils.showError(this, e.getMessage());
        }
    }
    
    private void viewAttendanceByEnrollment() {
        int enrollmentId = getSelectedEnrollmentId();
        if (enrollmentId < 0) {
            SwingUtils.showWarning(this, "Please select an enrollment.");
            return;
        }
        
        tableModel.setRowCount(0);
        List<Attendance> attendances = attendanceController.getEnrollmentAttendance(enrollmentId);
        
        for (Attendance a : attendances) {
            tableModel.addRow(new Object[]{
                a.getId(),
                a.getEnrollmentId(),
                a.getSessionId(),
                a.getStatus(),
                a.getNotes()
            });
        }
    }
    
    private void calculateAttendance() {
        int enrollmentId = getSelectedEnrollmentId();
        if (enrollmentId < 0) {
            SwingUtils.showWarning(this, "Please select an enrollment.");
            return;
        }
        
        double percentage = attendanceController.calculateAttendancePercentage(enrollmentId);
        SwingUtils.showInfo(this, String.format("Attendance Percentage: %.1f%%", percentage));
    }
}
