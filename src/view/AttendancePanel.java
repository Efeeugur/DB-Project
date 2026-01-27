package view;

import controller.AttendanceController;
import controller.CourseController;
import controller.EnrollmentController;
import controller.StudentController;
import model.entity.Attendance;
import model.entity.Course;
import model.entity.Enrollment;
import model.entity.Session;
import model.entity.Student;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel for attendance management with batch attendance feature.
 */
public class AttendancePanel extends JPanel {
    
    private final AttendanceController attendanceController;
    private final EnrollmentController enrollmentController;
    private final CourseController courseController;
    private final StudentController studentController;
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JComboBox<String> cmbCourse;
    private JComboBox<String> cmbSession;
    
    public AttendancePanel(AttendanceController attendanceController,
                           EnrollmentController enrollmentController,
                           CourseController courseController,
                           StudentController studentController) {
        this.attendanceController = attendanceController;
        this.enrollmentController = enrollmentController;
        this.courseController = courseController;
        this.studentController = studentController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("📋 Attendance Management");
        add(title, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setOpaque(false);
        
        // Left: Selection Panel
        mainPanel.add(createSelectionPanel(), BorderLayout.WEST);
        
        // Center: Attendance Records Table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createSelectionPanel() {
        JPanel formCard = SwingUtils.createCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(380, 0));
        
        JLabel formTitle = SwingUtils.createHeaderLabel("Take Attendance");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(20));
        
        // Step 1: Select Course
        JLabel step1 = new JLabel("1️⃣ Select Course:");
        step1.setFont(SwingUtils.LABEL_FONT);
        formCard.add(step1);
        formCard.add(Box.createVerticalStrut(5));
        
        cmbCourse = new JComboBox<>();
        cmbCourse.setFont(SwingUtils.LABEL_FONT);
        cmbCourse.setMaximumSize(new Dimension(350, 35));
        cmbCourse.setAlignmentX(Component.LEFT_ALIGNMENT);
        refreshCourseComboBox();
        formCard.add(cmbCourse);
        
        formCard.add(Box.createVerticalStrut(15));
        
        // Step 2: Select Session
        JLabel step2 = new JLabel("2️⃣ Select Session:");
        step2.setFont(SwingUtils.LABEL_FONT);
        formCard.add(step2);
        formCard.add(Box.createVerticalStrut(5));
        
        cmbSession = new JComboBox<>();
        cmbSession.setFont(SwingUtils.LABEL_FONT);
        cmbSession.setMaximumSize(new Dimension(350, 35));
        cmbSession.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(cmbSession);
        
        // Auto-load sessions when course selected
        cmbCourse.addActionListener(e -> {
            if (cmbCourse.getSelectedIndex() > 0) {
                loadSessionsForCourse();
            }
        });
        
        // Auto-load attendance records when session selected
        cmbSession.addActionListener(e -> {
            if (cmbSession.getSelectedIndex() > 0) {
                refreshTable();
            }
        });
        
        formCard.add(Box.createVerticalStrut(25));
        
        // Step 3: Take Attendance Button
        JLabel step3 = new JLabel("3️⃣ Open Attendance Sheet:");
        step3.setFont(SwingUtils.LABEL_FONT);
        formCard.add(step3);
        formCard.add(Box.createVerticalStrut(10));
        
        JButton btnTakeAttendance = SwingUtils.createSuccessButton("📋 Take Batch Attendance");
        btnTakeAttendance.setMaximumSize(new Dimension(350, 45));
        btnTakeAttendance.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnTakeAttendance.addActionListener(e -> openBatchAttendanceDialog());
        formCard.add(btnTakeAttendance);
        
        formCard.add(Box.createVerticalStrut(30));
        
        // Info
        JLabel infoLabel = new JLabel("<html><i>Select a course and session, then<br>take attendance for all students at once!</i></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(Color.GRAY);
        formCard.add(infoLabel);
        
        formCard.add(Box.createVerticalGlue());
        
        return formCard;
    }
    
    private JPanel createTablePanel() {
        JPanel tableCard = SwingUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));
        
        JLabel tableTitle = SwingUtils.createHeaderLabel("Attendance Records");
        tableCard.add(tableTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Student", "Course", "Session", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        
        // Color code status column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && value != null) {
                    String status = value.toString();
                    if (status.equals("PRESENT")) {
                        c.setBackground(new Color(200, 255, 200));
                    } else if (status.equals("ABSENT")) {
                        c.setBackground(new Color(255, 200, 200));
                    } else if (status.equals("LATE")) {
                        c.setBackground(new Color(255, 255, 200));
                    } else {
                        c.setBackground(new Color(220, 220, 255));
                    }
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        btnRefresh.addActionListener(e -> refreshTable());
        bottomPanel.add(btnRefresh);
        
        tableCard.add(bottomPanel, BorderLayout.SOUTH);
        
        return tableCard;
    }
    
    private void refreshCourseComboBox() {
        cmbCourse.removeAllItems();
        cmbCourse.addItem("-- Select Course --");
        
        List<Course> courses = courseController.getAllCourses();
        for (Course c : courses) {
            cmbCourse.addItem(c.getId() + " - " + c.getName());
        }
    }
    
    private void loadSessionsForCourse() {
        String selected = (String) cmbCourse.getSelectedItem();
        if (selected == null || selected.startsWith("--")) return;
        
        int courseId = Integer.parseInt(selected.split(" - ")[0]);
        
        cmbSession.removeAllItems();
        cmbSession.addItem("-- Select Session --");
        
        List<Session> sessions = courseController.getSessionsByCourse(courseId);
        for (Session s : sessions) {
            cmbSession.addItem(s.getId() + " - " + s.getSessionDate() + " (" + s.getTopic() + ")");
        }
    }
    
    private void openBatchAttendanceDialog() {
        String courseSelected = (String) cmbCourse.getSelectedItem();
        String sessionSelected = (String) cmbSession.getSelectedItem();
        
        if (courseSelected == null || courseSelected.startsWith("--")) {
            SwingUtils.showWarning(this, "Please select a course first.");
            return;
        }
        if (sessionSelected == null || sessionSelected.startsWith("--")) {
            SwingUtils.showWarning(this, "Please select a session first.");
            return;
        }
        
        int courseId = Integer.parseInt(courseSelected.split(" - ")[0]);
        int sessionId = Integer.parseInt(sessionSelected.split(" - ")[0]);
        
        // Get all enrollments for this course
        List<Enrollment> enrollments = enrollmentController.getCourseEnrollments(courseId);
        
        if (enrollments.isEmpty()) {
            SwingUtils.showWarning(this, "No students enrolled in this course.");
            return;
        }
        
        // Create batch attendance dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "📋 Take Attendance - " + sessionSelected.split(" - ")[1], true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        // Header
        JLabel header = new JLabel("Mark attendance for each student:");
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        dialog.add(header, BorderLayout.NORTH);
        
        // Student list with checkboxes
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
        studentPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        Map<Integer, JComboBox<Attendance.AttendanceStatus>> statusMap = new HashMap<>();
        
        for (Enrollment e : enrollments) {
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            // Student name
            String studentName = studentController.getStudentById(e.getStudentId())
                .map(Student::getFullName).orElse("Student #" + e.getStudentId());
            JLabel nameLabel = new JLabel(studentName);
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            nameLabel.setPreferredSize(new Dimension(200, 30));
            row.add(nameLabel, BorderLayout.WEST);
            
            // Status dropdown
            JComboBox<Attendance.AttendanceStatus> statusCombo = new JComboBox<>(Attendance.AttendanceStatus.values());
            statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            statusCombo.setPreferredSize(new Dimension(120, 30));
            statusMap.put(e.getId(), statusCombo);
            row.add(statusCombo, BorderLayout.EAST);
            
            studentPanel.add(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(studentPanel);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnMarkAllPresent = new JButton("✓ Mark All Present");
        btnMarkAllPresent.addActionListener(e -> {
            for (JComboBox<Attendance.AttendanceStatus> combo : statusMap.values()) {
                combo.setSelectedItem(Attendance.AttendanceStatus.PRESENT);
            }
        });
        
        JButton btnSaveAll = SwingUtils.createSuccessButton("💾 Save All");
        btnSaveAll.addActionListener(e -> {
            int saved = 0;
            for (Map.Entry<Integer, JComboBox<Attendance.AttendanceStatus>> entry : statusMap.entrySet()) {
                int enrollmentId = entry.getKey();
                Attendance.AttendanceStatus status = (Attendance.AttendanceStatus) entry.getValue().getSelectedItem();
                
                try {
                    attendanceController.recordAttendance(enrollmentId, sessionId, status, "");
                    saved++;
                } catch (Exception ex) {
                    // Already recorded or error - skip
                }
            }
            
            SwingUtils.showSuccess(this, "Saved attendance for " + saved + " students!");
            dialog.dispose();
            refreshTable();
        });
        
        JButton btnCancel = SwingUtils.createButton("Cancel");
        btnCancel.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnMarkAllPresent);
        buttonPanel.add(btnSaveAll);
        buttonPanel.add(btnCancel);
        
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    public void refresh() {
        refreshCourseComboBox();
        refreshTable();
    }
    
    private void refreshTable() {
        tableModel.setRowCount(0);
        
        // Check both course and session are selected
        String courseSelected = (String) cmbCourse.getSelectedItem();
        String sessionSelected = (String) cmbSession.getSelectedItem();
        
        if (courseSelected == null || courseSelected.startsWith("--")) {
            return;
        }
        if (sessionSelected == null || sessionSelected.startsWith("--")) {
            return;
        }
        
        int courseId = Integer.parseInt(courseSelected.split(" - ")[0]);
        int sessionId = Integer.parseInt(sessionSelected.split(" - ")[0]);
        
        // Get session info
        String sessionTopic = sessionSelected.contains("(") ? 
            sessionSelected.substring(sessionSelected.indexOf("(") + 1, sessionSelected.length() - 1) : "Session";
        String sessionDate = sessionSelected.split(" - ")[1].split(" ")[0];
        String courseName = courseController.getCourseById(courseId)
            .map(Course::getName).orElse("Unknown");
        
        // Get attendance records for this specific session
        List<Attendance> attendances = attendanceController.getSessionAttendance(sessionId);
        
        for (Attendance a : attendances) {
            // Get student name from enrollment
            String studentName = "Unknown Student";
            List<Enrollment> enrollments = enrollmentController.getCourseEnrollments(courseId);
            for (Enrollment e : enrollments) {
                if (e.getId() == a.getEnrollmentId()) {
                    studentName = studentController.getStudentById(e.getStudentId())
                        .map(Student::getFullName).orElse("Unknown");
                    break;
                }
            }
            
            tableModel.addRow(new Object[]{
                a.getId(),
                studentName,
                courseName,
                sessionTopic,
                sessionDate,
                a.getStatus()
            });
        }
    }
}
