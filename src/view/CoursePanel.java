package view;

import controller.CourseController;
import controller.InstructorController;
import model.entity.Course;
import model.entity.Instructor;
import model.entity.Student;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Panel for course management.
 */
public class CoursePanel extends JPanel {
    
    private final CourseController courseController;
    private final InstructorController instructorController;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Form fields
    private JTextField txtName;
    private JTextField txtDescription;
    private JComboBox<Course.Term> cmbTerm;
    private JComboBox<Student.SkillLevel> cmbSkillLevel;
    private JComboBox<String> cmbInstructor;
    private JTextField txtMaxCapacity;
    private JTextField txtFee;
    private JTextField txtStartDate;
    private JTextField txtEndDate;
    
    private int selectedCourseId = -1;
    
    public CoursePanel(CourseController courseController, InstructorController instructorController) {
        this.courseController = courseController;
        this.instructorController = instructorController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("📚 Course Management");
        add(title, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setOpaque(false);
        
        // Left: Form
        mainPanel.add(createFormPanel(), BorderLayout.WEST);
        
        // Center: Table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        refreshTable();
    }
    
    private JPanel createFormPanel() {
        JPanel formCard = SwingUtils.createCardPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(380, 0));
        
        JLabel formTitle = SwingUtils.createHeaderLabel("Course Form");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(15));
        
        // Form fields
        txtName = SwingUtils.createTextField();
        txtDescription = SwingUtils.createTextField();
        cmbTerm = SwingUtils.createComboBox(Course.Term.values());
        cmbSkillLevel = SwingUtils.createComboBox(Student.SkillLevel.values());
        cmbInstructor = new JComboBox<>();
        cmbInstructor.setFont(SwingUtils.LABEL_FONT);
        cmbInstructor.setPreferredSize(new Dimension(200, 30));
        refreshInstructorComboBox();
        
        txtMaxCapacity = SwingUtils.createTextField();
        txtMaxCapacity.setText("20");
        txtFee = SwingUtils.createTextField();
        txtStartDate = SwingUtils.createTextField();
        txtEndDate = SwingUtils.createTextField();
        
        formCard.add(SwingUtils.createFormRow("Name:", txtName));
        formCard.add(SwingUtils.createFormRow("Description:", txtDescription));
        formCard.add(SwingUtils.createFormRow("Term:", cmbTerm));
        formCard.add(SwingUtils.createFormRow("Skill Level:", cmbSkillLevel));
        formCard.add(SwingUtils.createFormRow("Instructor:", cmbInstructor));
        formCard.add(SwingUtils.createFormRow("Max Capacity:", txtMaxCapacity));
        formCard.add(SwingUtils.createFormRow("Fee:", txtFee));
        formCard.add(SwingUtils.createFormRow("Start Date:", txtStartDate));
        formCard.add(SwingUtils.createFormRow("End Date:", txtEndDate));
        
        // Date hint
        JLabel dateHint = new JLabel("  (Format: YYYY-MM-DD)");
        dateHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        dateHint.setForeground(Color.GRAY);
        formCard.add(dateHint);
        
        formCard.add(Box.createVerticalStrut(15));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        
        JButton btnAdd = SwingUtils.createSuccessButton("Add");
        JButton btnUpdate = SwingUtils.createPrimaryButton("Update");
        JButton btnClear = SwingUtils.createButton("Clear", Color.GRAY);
        
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnClear.addActionListener(e -> clearForm());
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnClear);
        
        formCard.add(btnPanel);
        
        return formCard;
    }
    
    private JPanel createTablePanel() {
        JPanel tableCard = SwingUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));
        
        JLabel tableTitle = SwingUtils.createHeaderLabel("Course List");
        tableCard.add(tableTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Name", "Term", "Level", "Instructor ID", "Capacity", "Fee"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCourse();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        JButton btnDelete = SwingUtils.createDangerButton("Delete");
        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        
        btnDelete.addActionListener(e -> deleteCourse());
        btnRefresh.addActionListener(e -> refreshTable());
        
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnDelete);
        tableCard.add(bottomPanel, BorderLayout.SOUTH);
        
        return tableCard;
    }
    
    private void refreshInstructorComboBox() {
        cmbInstructor.removeAllItems();
        cmbInstructor.addItem("-- Select Instructor --");
        List<Instructor> instructors = instructorController.getAllInstructors();
        for (Instructor i : instructors) {
            cmbInstructor.addItem(i.getId() + " - " + i.getFullName());
        }
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        refreshInstructorComboBox();
        List<Course> courses = courseController.getAllCourses();
        
        for (Course c : courses) {
            tableModel.addRow(new Object[]{
                c.getId(),
                c.getName(),
                c.getTerm(),
                c.getSkillLevel(),
                c.getInstructorId(),
                c.getMaxCapacity(),
                "$" + c.getFee()
            });
        }
    }
    
    private void loadSelectedCourse() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedCourseId = (int) tableModel.getValueAt(row, 0);
            
            courseController.getCourseById(selectedCourseId).ifPresent(c -> {
                txtName.setText(c.getName());
                txtDescription.setText(c.getDescription());
                cmbTerm.setSelectedItem(c.getTerm());
                cmbSkillLevel.setSelectedItem(c.getSkillLevel());
                txtMaxCapacity.setText(String.valueOf(c.getMaxCapacity()));
                txtFee.setText(c.getFee().toString());
                txtStartDate.setText(c.getStartDate() != null ? c.getStartDate().toString() : "");
                txtEndDate.setText(c.getEndDate() != null ? c.getEndDate().toString() : "");
                
                // Select instructor
                for (int i = 0; i < cmbInstructor.getItemCount(); i++) {
                    String item = (String) cmbInstructor.getItemAt(i);
                    if (item.startsWith(c.getInstructorId() + " -")) {
                        cmbInstructor.setSelectedIndex(i);
                        break;
                    }
                }
            });
        }
    }
    
    private int getSelectedInstructorId() {
        String selected = (String) cmbInstructor.getSelectedItem();
        if (selected == null || selected.startsWith("--")) {
            return -1;
        }
        return Integer.parseInt(selected.split(" - ")[0]);
    }
    
    private void addCourse() {
        try {
            int instructorId = getSelectedInstructorId();
            if (instructorId < 0) {
                SwingUtils.showWarning(this, "Please select an instructor.");
                return;
            }
            
            Course course = courseController.createCourse(
                txtName.getText(),
                txtDescription.getText(),
                (Course.Term) cmbTerm.getSelectedItem(),
                (Student.SkillLevel) cmbSkillLevel.getSelectedItem(),
                instructorId,
                Integer.parseInt(txtMaxCapacity.getText()),
                new BigDecimal(txtFee.getText()),
                LocalDate.parse(txtStartDate.getText()),
                LocalDate.parse(txtEndDate.getText())
            );
            SwingUtils.showSuccess(this, "Course added successfully! ID: " + course.getId());
            clearForm();
            refreshTable();
        } catch (NumberFormatException e) {
            SwingUtils.showError(this, "Please enter valid numbers for capacity and fee.");
        } catch (DateTimeParseException e) {
            SwingUtils.showError(this, "Please enter dates in YYYY-MM-DD format.");
        } catch (IllegalArgumentException e) {
            SwingUtils.showError(this, e.getMessage());
        }
    }
    
    private void updateCourse() {
        if (selectedCourseId < 0) {
            SwingUtils.showWarning(this, "Please select a course first.");
            return;
        }
        
        try {
            courseController.getCourseById(selectedCourseId).ifPresent(course -> {
                course.setName(txtName.getText());
                course.setDescription(txtDescription.getText());
                course.setTerm((Course.Term) cmbTerm.getSelectedItem());
                course.setSkillLevel((Student.SkillLevel) cmbSkillLevel.getSelectedItem());
                course.setInstructorId(getSelectedInstructorId());
                course.setMaxCapacity(Integer.parseInt(txtMaxCapacity.getText()));
                course.setFee(new BigDecimal(txtFee.getText()));
                course.setStartDate(LocalDate.parse(txtStartDate.getText()));
                course.setEndDate(LocalDate.parse(txtEndDate.getText()));
                
                courseController.updateCourse(course);
                SwingUtils.showSuccess(this, "Course updated successfully!");
                refreshTable();
            });
        } catch (Exception e) {
            SwingUtils.showError(this, "Error updating course: " + e.getMessage());
        }
    }
    
    private void deleteCourse() {
        if (selectedCourseId < 0) {
            SwingUtils.showWarning(this, "Please select a course first.");
            return;
        }
        
        if (SwingUtils.showConfirm(this, "Are you sure you want to delete this course?")) {
            if (courseController.deleteCourse(selectedCourseId)) {
                SwingUtils.showSuccess(this, "Course deleted successfully!");
                clearForm();
                refreshTable();
            } else {
                SwingUtils.showError(this, "Failed to delete course.");
            }
        }
    }
    
    private void clearForm() {
        selectedCourseId = -1;
        txtName.setText("");
        txtDescription.setText("");
        cmbTerm.setSelectedIndex(0);
        cmbSkillLevel.setSelectedIndex(0);
        cmbInstructor.setSelectedIndex(0);
        txtMaxCapacity.setText("20");
        txtFee.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        table.clearSelection();
    }
}
