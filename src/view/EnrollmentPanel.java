package view;

import controller.CourseController;
import controller.EnrollmentController;
import controller.StudentController;
import model.entity.Course;
import model.entity.Enrollment;
import model.entity.Session;
import model.entity.Student;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for enrollment management - Course-based card view.
 */
public class EnrollmentPanel extends JPanel {

    private final EnrollmentController enrollmentController;
    private final StudentController studentController;
    private final CourseController courseController;

    private JPanel coursesContainer;

    public EnrollmentPanel(EnrollmentController enrollmentController,
            StudentController studentController,
            CourseController courseController) {
        this.enrollmentController = enrollmentController;
        this.studentController = studentController;
        this.courseController = courseController;

        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel title = SwingUtils.createTitleLabel("Course Enrollments");
        headerPanel.add(title, BorderLayout.WEST);

        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        btnRefresh.addActionListener(e -> refreshCourseCards());
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Courses container with scroll
        coursesContainer = new JPanel();
        coursesContainer.setLayout(new GridLayout(0, 3, 20, 20));
        coursesContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(coursesContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);

        refreshCourseCards();
    }

    public void refreshTable() {
        refreshCourseCards();
    }

    private void refreshCourseCards() {
        coursesContainer.removeAll();

        List<Course> courses = courseController.getAllCourses();

        for (Course course : courses) {
            coursesContainer.add(createCourseCard(course));
        }

        int remainder = courses.size() % 3;
        if (remainder != 0) {
            for (int i = 0; i < 3 - remainder; i++) {
                JPanel filler = new JPanel();
                filler.setOpaque(false);
                coursesContainer.add(filler);
            }
        }

        coursesContainer.revalidate();
        coursesContainer.repaint();
    }

    private JPanel createCourseCard(Course course) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setBackground(SwingUtils.BG_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(300, 180));

        List<Session> sessions = courseController.getSessionsByCourse(course.getId());
        List<Enrollment> enrollments = enrollmentController.getCourseEnrollments(course.getId());
        int enrolledCount = (int) enrollments.stream()
                .filter(e -> e.getStatus() == Enrollment.Status.ACTIVE)
                .count();

        JLabel nameLabel = new JLabel(course.getName());
        nameLabel.putClientProperty("FlatLaf.styleClass", "h3");
        nameLabel.setForeground(SwingUtils.PRIMARY_COLOR);
        card.add(nameLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        infoPanel.setOpaque(false);

        JLabel instructorLabel = new JLabel("Instructor #" + course.getInstructorId());
        instructorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel levelLabel = new JLabel("Level: " + course.getSkillLevel());
        levelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel sessionLabel = new JLabel("Sessions: " + sessions.size());
        sessionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        String capacityText = enrolledCount + " / " + course.getMaxCapacity() + " students";
        JLabel capacityLabel = new JLabel(capacityText);
        capacityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        if (enrolledCount >= course.getMaxCapacity()) {
            capacityLabel.setForeground(SwingUtils.DANGER_COLOR);
        } else if (enrolledCount >= course.getMaxCapacity() * 0.8) {
            capacityLabel.setForeground(SwingUtils.WARNING_COLOR);
        } else {
            capacityLabel.setForeground(SwingUtils.SUCCESS_COLOR);
        }

        infoPanel.add(instructorLabel);
        infoPanel.add(levelLabel);
        infoPanel.add(sessionLabel);
        infoPanel.add(capacityLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        JLabel termLabel = new JLabel(course.getTerm().toString());
        termLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        termLabel.setForeground(new Color(120, 120, 120));

        JLabel feeLabel = new JLabel("₺" + course.getFee().toString());
        feeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        feeLabel.setForeground(new Color(80, 80, 80));

        bottomPanel.add(termLabel, BorderLayout.WEST);
        bottomPanel.add(feeLabel, BorderLayout.EAST);

        card.add(bottomPanel, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showSessionsDialog(course);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(245, 248, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(SwingUtils.PRIMARY_COLOR, 2),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(SwingUtils.BG_WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(16, 16, 16, 16)));
            }
        });

        return card;
    }

    private void showSessionsDialog(Course course) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Sessions - " + course.getName(), true);
        dialog.setSize(700, 550);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(SwingUtils.BG_LIGHT);

        JLabel headerLabel = SwingUtils.createHeaderLabel(course.getName());
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Sessions table
        String[] sessionColumns = { "ID", "Date", "Start", "End", "Topic" };
        DefaultTableModel sessionModel = new DefaultTableModel(sessionColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Session> sessions = courseController.getSessionsByCourse(course.getId());
        for (Session s : sessions) {
            sessionModel.addRow(new Object[] {
                    s.getId(), s.getSessionDate(), s.getStartTime(), s.getEndTime(), s.getTopic()
            });
        }

        JTable sessionTable = SwingUtils.createTable(sessionModel);

        // Enrolled table - DEFINE BEFORE enroll button
        String[] enrolledColumns = { "ID", "Student", "Date", "Status" };
        DefaultTableModel enrolledModel = new DefaultTableModel(enrolledColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Enrollment> enrollments = enrollmentController.getCourseEnrollments(course.getId());
        for (Enrollment en : enrollments) {
            // Only show ACTIVE enrollments
            if (en.getStatus() == Enrollment.Status.ACTIVE) {
                String studentName = studentController.getStudentById(en.getStudentId())
                        .map(Student::getFullName).orElse("Unknown");
                enrolledModel.addRow(new Object[] {
                        en.getId(), studentName, en.getEnrollmentDate(), en.getStatus()
                });
            }
        }

        JTable enrolledTable = SwingUtils.createTable(enrolledModel);

        // Student combo
        JComboBox<String> cmbStudent = new JComboBox<>();
        cmbStudent.addItem("-- Select Student --");
        List<Student> students = studentController.getAllStudents();
        for (Student s : students) {
            if (s.getSkillLevel().ordinal() >= course.getSkillLevel().ordinal()) {
                cmbStudent.addItem(s.getId() + " - " + s.getFullName() + " (" + s.getSkillLevel() + ")");
            }
        }
        cmbStudent.setPreferredSize(new Dimension(280, 32));

        // Enroll button
        JButton btnEnroll = SwingUtils.createSuccessButton("Enroll");
        btnEnroll.addActionListener(e -> {
            if (cmbStudent.getSelectedIndex() <= 0) {
                SwingUtils.showWarning(dialog, "Please select a student.");
                return;
            }
            try {
                int studentId = Integer.parseInt(((String) cmbStudent.getSelectedItem()).split(" - ")[0]);
                enrollmentController.enrollStudent(studentId, course.getId());
                SwingUtils.showSuccess(dialog, "Student enrolled!");

                // Refresh enrolled table (only ACTIVE)
                enrolledModel.setRowCount(0);
                List<Enrollment> updated = enrollmentController.getCourseEnrollments(course.getId());
                for (Enrollment en : updated) {
                    if (en.getStatus() == Enrollment.Status.ACTIVE) {
                        String name = studentController.getStudentById(en.getStudentId())
                                .map(Student::getFullName).orElse("Unknown");
                        enrolledModel.addRow(new Object[] { en.getId(), name, en.getEnrollmentDate(), en.getStatus() });
                    }
                }

                SwingUtilities.invokeLater(() -> refreshCourseCards());
            } catch (Exception ex) {
                SwingUtils.showError(dialog, ex.getMessage());
            }
        });

        // Drop button
        JButton btnDrop = SwingUtils.createDangerButton("Drop");
        btnDrop.addActionListener(e -> {
            int row = enrolledTable.getSelectedRow();
            if (row < 0) {
                SwingUtils.showWarning(dialog, "Select an enrollment.");
                return;
            }
            int enrollmentId = (int) enrolledModel.getValueAt(row, 0);
            if (SwingUtils.showConfirm(dialog, "Drop this enrollment?")) {
                try {
                    enrollmentController.dropEnrollment(enrollmentId);
                    enrolledModel.removeRow(row);
                    SwingUtilities.invokeLater(() -> refreshCourseCards());
                } catch (Exception ex) {
                    SwingUtils.showError(dialog, "Failed.");
                }
            }
        });

        // Layout
        JPanel sessionsPanel = new JPanel(new BorderLayout(5, 5));
        sessionsPanel.setOpaque(false);
        sessionsPanel.add(new JLabel("Sessions:"), BorderLayout.NORTH);
        sessionsPanel.add(new JScrollPane(sessionTable), BorderLayout.CENTER);

        JPanel enrolledPanel = new JPanel(new BorderLayout(5, 5));
        enrolledPanel.setOpaque(false);
        enrolledPanel.add(new JLabel("Enrolled:"), BorderLayout.NORTH);
        enrolledPanel.add(new JScrollPane(enrolledTable), BorderLayout.CENTER);
        enrolledPanel.add(btnDrop, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sessionsPanel, enrolledPanel);
        splitPane.setResizeWeight(0.35);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        southPanel.setOpaque(false);
        southPanel.add(new JLabel("Add:"));
        southPanel.add(cmbStudent);
        southPanel.add(btnEnroll);

        JButton btnClose = SwingUtils.createButton("Close");
        btnClose.addActionListener(e -> dialog.dispose());
        southPanel.add(Box.createHorizontalStrut(20));
        southPanel.add(btnClose);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}
