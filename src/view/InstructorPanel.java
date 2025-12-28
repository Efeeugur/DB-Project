package view;

import controller.InstructorController;
import model.entity.Instructor;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for instructor management.
 */
public class InstructorPanel extends JPanel {
    
    private final InstructorController instructorController;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Form fields
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtSpecialization;
    
    private int selectedInstructorId = -1;
    
    public InstructorPanel(InstructorController instructorController) {
        this.instructorController = instructorController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("👨‍🏫 Instructor Management");
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
        
        JLabel formTitle = SwingUtils.createHeaderLabel("Instructor Form");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createVerticalStrut(20));
        
        // Form fields
        txtFirstName = SwingUtils.createTextField();
        txtLastName = SwingUtils.createTextField();
        txtEmail = SwingUtils.createTextField();
        txtPhone = SwingUtils.createTextField();
        txtSpecialization = SwingUtils.createTextField();
        
        formCard.add(SwingUtils.createFormRow("First Name:", txtFirstName));
        formCard.add(SwingUtils.createFormRow("Last Name:", txtLastName));
        formCard.add(SwingUtils.createFormRow("Email:", txtEmail));
        formCard.add(SwingUtils.createFormRow("Phone:", txtPhone));
        formCard.add(SwingUtils.createFormRow("Specialization:", txtSpecialization));
        
        formCard.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        
        JButton btnAdd = SwingUtils.createSuccessButton("Add");
        JButton btnUpdate = SwingUtils.createPrimaryButton("Update");
        JButton btnClear = SwingUtils.createButton("Clear", Color.GRAY);
        
        btnAdd.addActionListener(e -> addInstructor());
        btnUpdate.addActionListener(e -> updateInstructor());
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
        
        JLabel tableTitle = SwingUtils.createHeaderLabel("Instructor List");
        tableCard.add(tableTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "First Name", "Last Name", "Email", "Phone", "Specialization"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedInstructor();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        JButton btnDelete = SwingUtils.createDangerButton("Delete");
        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        
        btnDelete.addActionListener(e -> deleteInstructor());
        btnRefresh.addActionListener(e -> refreshTable());
        
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnDelete);
        tableCard.add(bottomPanel, BorderLayout.SOUTH);
        
        return tableCard;
    }
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Instructor> instructors = instructorController.getAllInstructors();
        
        for (Instructor i : instructors) {
            tableModel.addRow(new Object[]{
                i.getId(),
                i.getFirstName(),
                i.getLastName(),
                i.getEmail(),
                i.getPhone(),
                i.getSpecialization()
            });
        }
    }
    
    private void loadSelectedInstructor() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedInstructorId = (int) tableModel.getValueAt(row, 0);
            txtFirstName.setText((String) tableModel.getValueAt(row, 1));
            txtLastName.setText((String) tableModel.getValueAt(row, 2));
            txtEmail.setText((String) tableModel.getValueAt(row, 3));
            txtPhone.setText((String) tableModel.getValueAt(row, 4));
            txtSpecialization.setText((String) tableModel.getValueAt(row, 5));
        }
    }
    
    private void addInstructor() {
        try {
            Instructor instructor = instructorController.registerInstructor(
                txtFirstName.getText(),
                txtLastName.getText(),
                txtEmail.getText(),
                txtPhone.getText(),
                txtSpecialization.getText()
            );
            SwingUtils.showSuccess(this, "Instructor added successfully! ID: " + instructor.getId());
            clearForm();
            refreshTable();
        } catch (IllegalArgumentException e) {
            SwingUtils.showError(this, e.getMessage());
        }
    }
    
    private void updateInstructor() {
        if (selectedInstructorId < 0) {
            SwingUtils.showWarning(this, "Please select an instructor first.");
            return;
        }
        
        instructorController.getInstructorById(selectedInstructorId).ifPresent(instructor -> {
            instructor.setFirstName(txtFirstName.getText());
            instructor.setLastName(txtLastName.getText());
            instructor.setEmail(txtEmail.getText());
            instructor.setPhone(txtPhone.getText());
            instructor.setSpecialization(txtSpecialization.getText());
            
            instructorController.updateInstructor(instructor);
            SwingUtils.showSuccess(this, "Instructor updated successfully!");
            refreshTable();
        });
    }
    
    private void deleteInstructor() {
        if (selectedInstructorId < 0) {
            SwingUtils.showWarning(this, "Please select an instructor first.");
            return;
        }
        
        if (SwingUtils.showConfirm(this, "Are you sure you want to delete this instructor?")) {
            if (instructorController.deleteInstructor(selectedInstructorId)) {
                SwingUtils.showSuccess(this, "Instructor deleted successfully!");
                clearForm();
                refreshTable();
            } else {
                SwingUtils.showError(this, "Failed to delete instructor.");
            }
        }
    }
    
    private void clearForm() {
        selectedInstructorId = -1;
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtSpecialization.setText("");
        table.clearSelection();
    }
}
