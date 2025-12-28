package view;

import controller.StudentController;
import model.entity.Student;
import model.entity.SkillTest;
import util.SwingUtils;
import util.InputValidator;
import util.ValidationResult;
import view.components.TableSearchPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for student management.
 */
public class StudentPanel extends JPanel {
    
    private final StudentController studentController;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableSearchPanel searchPanel;
    
    // Form fields
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtDateOfBirth;
    private JComboBox<Student.SkillLevel> cmbSkillLevel;
    
    private int selectedStudentId = -1;
    
    public StudentPanel(StudentController studentController) {
        this.studentController = studentController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("👨‍🎓 Student Management");
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
        formCard.setPreferredSize(new Dimension(350, 0));
        
        JLabel formTitle = SwingUtils.createHeaderLabel("Student Form");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(20));
        
        // Form fields
        txtFirstName = SwingUtils.createTextField();
        txtLastName = SwingUtils.createTextField();
        txtEmail = SwingUtils.createTextField();
        txtPhone = SwingUtils.createTextField();
        txtDateOfBirth = SwingUtils.createTextField();
        cmbSkillLevel = SwingUtils.createComboBox(Student.SkillLevel.values());
        
        formCard.add(SwingUtils.createFormRow("First Name:", txtFirstName));
        formCard.add(SwingUtils.createFormRow("Last Name:", txtLastName));
        formCard.add(SwingUtils.createFormRow("Email:", txtEmail));
        formCard.add(SwingUtils.createFormRow("Phone:", txtPhone));
        formCard.add(SwingUtils.createFormRow("Date of Birth:", txtDateOfBirth));
        formCard.add(SwingUtils.createFormRow("Skill Level:", cmbSkillLevel));
        
        formCard.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        
        JButton btnAdd = SwingUtils.createSuccessButton("Add");
        JButton btnUpdate = SwingUtils.createPrimaryButton("Update");
        JButton btnClear = SwingUtils.createButton("Clear", Color.GRAY);
        
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnClear.addActionListener(e -> clearForm());
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnClear);
        
        formCard.add(btnPanel);
        formCard.add(Box.createVerticalStrut(20));
        
        // Skill Test Section
        JLabel testTitle = SwingUtils.createHeaderLabel("Skill Test");
        testTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(testTitle);
        formCard.add(Box.createVerticalStrut(10));
        
        JButton btnSkillTest = SwingUtils.createPrimaryButton("Conduct Test");
        btnSkillTest.addActionListener(e -> conductSkillTest());
        
        JPanel testPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        testPanel.setOpaque(false);
        testPanel.add(btnSkillTest);
        formCard.add(testPanel);
        
        return formCard;
    }
    
    private JPanel createTablePanel() {
        JPanel tableCard = SwingUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));
        
        // Header with title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel tableTitle = SwingUtils.createHeaderLabel("Student List");
        headerPanel.add(tableTitle, BorderLayout.WEST);
        tableCard.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone", "Skill Level"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStudent();
            }
        });
        
        // Add search panel
        searchPanel = new TableSearchPanel(table);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tableCard.add(centerPanel, BorderLayout.CENTER);
        
        // Delete button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        JButton btnDelete = SwingUtils.createDangerButton("Delete");
        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        
        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> refreshTable());
        
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnDelete);
        tableCard.add(bottomPanel, BorderLayout.SOUTH);
        
        return tableCard;
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = studentController.getAllStudents();
        
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getId(),
                s.getFirstName(),
                s.getLastName(),
                s.getEmail(),
                s.getPhone(),
                s.getSkillLevel()
            });
        }
    }
    
    private void loadSelectedStudent() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedStudentId = (int) tableModel.getValueAt(row, 0);
            txtFirstName.setText((String) tableModel.getValueAt(row, 1));
            txtLastName.setText((String) tableModel.getValueAt(row, 2));
            txtEmail.setText((String) tableModel.getValueAt(row, 3));
            txtPhone.setText((String) tableModel.getValueAt(row, 4));
            cmbSkillLevel.setSelectedItem(tableModel.getValueAt(row, 5));
            
            studentController.getStudentById(selectedStudentId).ifPresent(s -> {
                txtDateOfBirth.setText(s.getDateOfBirth() != null ? s.getDateOfBirth() : "");
            });
        }
    }
    
    private void addStudent() {
        // Validate input
        ValidationResult validation = InputValidator.validateStudent(
            txtFirstName.getText(),
            txtLastName.getText(),
            txtEmail.getText(),
            txtPhone.getText(),
            txtDateOfBirth.getText()
        );
        
        if (!validation.isValid()) {
            SwingUtils.showError(this, validation.getErrorsAsHtml());
            return;
        }
        
        try {
            Student student = studentController.registerStudent(
                txtFirstName.getText(),
                txtLastName.getText(),
                txtEmail.getText(),
                txtPhone.getText(),
                txtDateOfBirth.getText()
            );
            SwingUtils.showSuccess(this, "Student added successfully! ID: " + student.getId());
            clearForm();
            refreshTable();
            if (searchPanel != null) searchPanel.refreshSorter();
        } catch (IllegalArgumentException e) {
            SwingUtils.showError(this, e.getMessage());
        }
    }
    
    private void updateStudent() {
        if (selectedStudentId < 0) {
            SwingUtils.showWarning(this, "Please select a student first.");
            return;
        }
        
        studentController.getStudentById(selectedStudentId).ifPresent(student -> {
            student.setFirstName(txtFirstName.getText());
            student.setLastName(txtLastName.getText());
            student.setEmail(txtEmail.getText());
            student.setPhone(txtPhone.getText());
            student.setDateOfBirth(txtDateOfBirth.getText());
            student.setSkillLevel((Student.SkillLevel) cmbSkillLevel.getSelectedItem());
            
            studentController.updateStudent(student);
            SwingUtils.showSuccess(this, "Student updated successfully!");
            refreshTable();
        });
    }
    
    private void deleteStudent() {
        if (selectedStudentId < 0) {
            SwingUtils.showWarning(this, "Please select a student first.");
            return;
        }
        
        if (SwingUtils.showConfirm(this, "Are you sure you want to delete this student?")) {
            if (studentController.deleteStudent(selectedStudentId)) {
                SwingUtils.showSuccess(this, "Student deleted successfully!");
                clearForm();
                refreshTable();
            } else {
                SwingUtils.showError(this, "Failed to delete student.");
            }
        }
    }
    
    private void conductSkillTest() {
        if (selectedStudentId < 0) {
            SwingUtils.showWarning(this, "Please select a student first.");
            return;
        }
        
        String scoreStr = SwingUtils.showInput(this, "Enter test score (0-100):");
        if (scoreStr == null) return;
        
        try {
            int score = Integer.parseInt(scoreStr);
            if (score < 0 || score > 100) {
                SwingUtils.showError(this, "Score must be between 0 and 100.");
                return;
            }
            
            String notes = SwingUtils.showInput(this, "Enter notes (optional):");
            SkillTest test = studentController.conductSkillTest(selectedStudentId, score, notes);
            
            SwingUtils.showSuccess(this, 
                "Skill test completed!\nScore: " + score + "\nAssigned Level: " + test.getAssignedLevel());
            refreshTable();
            loadSelectedStudent();
        } catch (NumberFormatException e) {
            SwingUtils.showError(this, "Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            SwingUtils.showError(this, e.getMessage());
        }
    }
    
    private void clearForm() {
        selectedStudentId = -1;
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtDateOfBirth.setText("");
        cmbSkillLevel.setSelectedIndex(0);
        table.clearSelection();
    }
}
