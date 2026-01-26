package view;

import controller.CourseController;
import controller.EnrollmentController;
import controller.StudentController;
import model.entity.Course;
import model.entity.Enrollment;
import model.entity.Student;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for enrollment management.
 */
public class EnrollmentPanel extends JPanel {
    
    private final EnrollmentController enrollmentController;
    private final StudentController studentController;
    private final CourseController courseController;
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private JComboBox<String> cmbStudent;
    private JComboBox<String> cmbCourse;
    
    private int selectedEnrollmentId = -1;
    
    public EnrollmentPanel(EnrollmentController enrollmentController, 
                           StudentController studentController,
                           CourseController courseController) {
        this.enrollmentController = enrollmentController;
        this.studentController = studentController;
        this.courseController = courseController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("📝 Enrollment Management");
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
        formCard.setPreferredSize(new Dimension(350, 0));
        
        JLabel formTitle = SwingUtils.createHeaderLabel("New Enrollment");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(20));
        
        // Combo boxes
        cmbStudent = new JComboBox<>();
        cmbStudent.setFont(SwingUtils.LABEL_FONT);
        cmbStudent.setPreferredSize(new Dimension(200, 30));
        
        cmbCourse = new JComboBox<>();
        cmbCourse.setFont(SwingUtils.LABEL_FONT);
        cmbCourse.setPreferredSize(new Dimension(200, 30));
        
        refreshComboBoxes();
        
        formCard.add(SwingUtils.createFormRow("Student:", cmbStudent));
        formCard.add(SwingUtils.createFormRow("Course:", cmbCourse));
        
        formCard.add(Box.createVerticalStrut(20));
        
        JButton btnEnroll = SwingUtils.createSuccessButton("Enroll Student");
        btnEnroll.addActionListener(e -> enrollStudent());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnEnroll);
        
        formCard.add(btnPanel);
        
        // Actions section
        formCard.add(Box.createVerticalStrut(30));
        JLabel actionsTitle = SwingUtils.createHeaderLabel("Actions");
        actionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(actionsTitle);
        formCard.add(Box.createVerticalStrut(10));
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actionPanel.setOpaque(false);
        
        JButton btnDrop = SwingUtils.createDangerButton("Drop");
        JButton btnComplete = SwingUtils.createPrimaryButton("Complete");
        
        btnDrop.addActionListener(e -> dropEnrollment());
        btnComplete.addActionListener(e -> completeEnrollment());
        
        actionPanel.add(btnDrop);
        actionPanel.add(btnComplete);
        
        formCard.add(actionPanel);
        
        // Info box
        formCard.add(Box.createVerticalStrut(20));
        JLabel infoLabel = new JLabel("<html><i>Note: Students can only enroll in courses<br>matching their skill level.<br><br>💡 Use Payment menu to process payments.</i></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(Color.GRAY);
        formCard.add(infoLabel);
        
        return formCard;
    }
    
    private JPanel createTablePanel() {
        JPanel tableCard = SwingUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));
        
        JLabel tableTitle = SwingUtils.createHeaderLabel("Enrollments List");
        tableCard.add(tableTitle, BorderLayout.NORTH);
        
        String[] columns = {"ID", "Student", "Course", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        
        // Selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                selectedEnrollmentId = (int) table.getValueAt(table.getSelectedRow(), 0);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        btnRefresh.addActionListener(e -> refreshTable());
        bottomPanel.add(btnRefresh);
        
        tableCard.add(bottomPanel, BorderLayout.SOUTH);
        
        return tableCard;
    }
    
    private void refreshComboBoxes() {
        cmbStudent.removeAllItems();
        cmbStudent.addItem("-- Select Student --");
        List<Student> students = studentController.getAllStudents();
        for (Student s : students) {
            cmbStudent.addItem(s.getId() + " - " + s.getFullName() + " (" + s.getSkillLevel() + ")");
        }
        
        cmbCourse.removeAllItems();
        cmbCourse.addItem("-- Select Course --");
        List<Course> courses = courseController.getAllCourses();
        for (Course c : courses) {
            cmbCourse.addItem(c.getId() + " - " + c.getName() + " (" + c.getSkillLevel() + ")");
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();
        
        for (Enrollment e : enrollments) {
            String studentName = studentController.getStudentById(e.getStudentId())
                .map(Student::getFullName).orElse("Unknown");
            
            String courseName = courseController.getCourseById(e.getCourseId())
                .map(Course::getName).orElse("Unknown");
            
            tableModel.addRow(new Object[]{
                e.getId(),
                studentName,
                courseName,
                e.getEnrollmentDate(),
                e.getStatus()
            });
        }
        
        refreshComboBoxes();
    }
    
    private void enrollStudent() {
        if (cmbStudent.getSelectedIndex() <= 0 || cmbCourse.getSelectedIndex() <= 0) {
            SwingUtils.showWarning(this, "Please select both student and course.");
            return;
        }
        
        try {
            int studentId = Integer.parseInt(((String)cmbStudent.getSelectedItem()).split(" - ")[0]);
            int courseId = Integer.parseInt(((String)cmbCourse.getSelectedItem()).split(" - ")[0]);
            
            enrollmentController.enrollStudent(studentId, courseId);
            SwingUtils.showSuccess(this, "Student enrolled successfully!");
            refreshTable();
        } catch (Exception e) {
            SwingUtils.showError(this, e.getMessage());
        }
    }
    
    private void dropEnrollment() {
        if (selectedEnrollmentId < 0) {
            SwingUtils.showWarning(this, "Please select an enrollment from the table.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to drop this enrollment?", 
            "Confirm Drop", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                enrollmentController.dropEnrollment(selectedEnrollmentId);
                SwingUtils.showSuccess(this, "Enrollment dropped successfully.");
                refreshTable();
            } catch (Exception e) {
                SwingUtils.showError(this, "Failed to drop enrollment.");
            }
        }
    }
    
    private void completeEnrollment() {
        if (selectedEnrollmentId < 0) {
            SwingUtils.showWarning(this, "Please select an enrollment from the table.");
            return;
        }
        
        try {
            enrollmentController.completeEnrollment(selectedEnrollmentId);
            SwingUtils.showSuccess(this, "Enrollment marked as completed.");
            refreshTable();
        } catch (Exception e) {
            SwingUtils.showError(this, "Failed to complete enrollment.");
        }
    }
}
