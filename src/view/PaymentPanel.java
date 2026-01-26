package view;

import controller.EnrollmentController;
import controller.StudentController;
import controller.CourseController;
import model.entity.Payment;
import model.entity.Student;
import model.entity.Enrollment;
import model.entity.Course;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

/**
 * Panel for payment management - student-based view.
 */
public class PaymentPanel extends JPanel {
    
    private final EnrollmentController enrollmentController;
    private final StudentController studentController;
    private final CourseController courseController;
    
    private JList<String> studentList;
    private DefaultListModel<String> studentListModel;
    private JTextField txtSearch; // Search field
    private Timer searchTimer;    // Timer for debounce
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblSelectedStudent;
    private JLabel lblSummary;
    
    private int selectedStudentId = -1;
    
    public PaymentPanel(EnrollmentController enrollmentController,
                        StudentController studentController,
                        CourseController courseController) {
        this.enrollmentController = enrollmentController;
        this.studentController = studentController;
        this.courseController = courseController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("💰 Payment Management");
        add(title, BorderLayout.NORTH);
        
        // Timer for search debounce
        searchTimer = new Timer(300, e -> filterStudentList());
        searchTimer.setRepeats(false);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setOpaque(false);
        
        // Left: Student List
        mainPanel.add(createStudentListPanel(), BorderLayout.WEST);
        
        // Center: Payment Table
        mainPanel.add(createPaymentTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        refreshStudentList();
    }
    
    private JPanel createStudentListPanel() {
        JPanel listCard = SwingUtils.createCardPanel();
        listCard.setLayout(new BorderLayout(0, 10));
        listCard.setPreferredSize(new Dimension(250, 0));
        
        JLabel listTitle = SwingUtils.createHeaderLabel("👤 Select Student");
        
        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchPanel.add(listTitle, BorderLayout.NORTH);
        
        txtSearch = new JTextField();
        txtSearch.putClientProperty("JTextField.placeholderText", "Search student...");
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchTimer.restart(); }
        });
        searchPanel.add(txtSearch, BorderLayout.CENTER);
        
        listCard.add(searchPanel, BorderLayout.NORTH);
        
        // Student list
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        studentList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.setFixedCellHeight(35);
        
        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStudentPayments();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(studentList);
        listCard.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        btnRefresh.addActionListener(e -> refreshStudentList());
        bottomPanel.add(btnRefresh);
        listCard.add(bottomPanel, BorderLayout.SOUTH);
        
        return listCard;
    }
    
    private JPanel createPaymentTablePanel() {
        JPanel tableCard = SwingUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));
        
        // Header with selected student name
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        lblSelectedStudent = SwingUtils.createHeaderLabel("📊 Select a student to view payments");
        headerPanel.add(lblSelectedStudent, BorderLayout.WEST);
        
        tableCard.add(headerPanel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Course", "Amount", "Status", "Method", "Date", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Action column
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        
        // Status color renderer
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && value != null) {
                    String status = value.toString();
                    if (status.equals("COMPLETED")) {
                        c.setBackground(new Color(200, 255, 200));
                        c.setForeground(new Color(0, 100, 0));
                    } else if (status.equals("PENDING")) {
                        c.setBackground(new Color(255, 255, 200));
                        c.setForeground(new Color(150, 100, 0));
                    } else {
                        c.setBackground(new Color(255, 200, 200));
                        c.setForeground(new Color(150, 0, 0));
                    }
                }
                return c;
            }
        });
        
        // Button renderer and editor for Action column
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor());
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setOpaque(false);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        lblSummary = new JLabel("");
        lblSummary.setFont(new Font("Segoe UI", Font.BOLD, 13));
        summaryPanel.add(lblSummary, BorderLayout.WEST);
        
        tableCard.add(summaryPanel, BorderLayout.SOUTH);
        
        return tableCard;
    }
    
    private void refreshStudentList() {
        filterStudentList();
    }
    
    private void filterStudentList() {
        studentListModel.clear();
        String query = txtSearch.getText().toLowerCase();
        
        List<Student> students = studentController.getAllStudents();
        for (Student s : students) {
            String displayText = s.getId() + " - " + s.getFullName();
            
            // Search filter
            if (!query.isEmpty() && !displayText.toLowerCase().contains(query)) {
                continue;
            }
            
            // Check if student has any enrollments (and thus payments)
            List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(s.getId());
            if (!enrollments.isEmpty()) {
                studentListModel.addElement(displayText);
            }
        }
    }
    
    private void loadSelectedStudentPayments() {
        String selected = studentList.getSelectedValue();
        if (selected == null) return;
        
        selectedStudentId = Integer.parseInt(selected.split(" - ")[0]);
        String studentName = selected.substring(selected.indexOf(" - ") + 3);
        
        lblSelectedStudent.setText("📊 Payments for: " + studentName);
        
        tableModel.setRowCount(0);
        
        // Get all enrollments for this student
        List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(selectedStudentId);
        
        double totalAmount = 0;
        double pendingAmount = 0;
        int completedCount = 0;
        int pendingCount = 0;
        
        for (Enrollment e : enrollments) {
            Payment payment = enrollmentController.getPaymentForEnrollment(e.getId());
            if (payment != null) {
                String courseName = courseController.getCourseById(e.getCourseId())
                    .map(Course::getName).orElse("Unknown Course");
                
                String actionButton = payment.getStatus() == Payment.PaymentStatus.PENDING ? "Pay" : "-";
                
                tableModel.addRow(new Object[]{
                    payment.getId(),
                    courseName,
                    "$" + payment.getAmount(),
                    payment.getStatus(),
                    payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "-",
                    payment.getPaymentDate().toLocalDate(),
                    actionButton
                });
                
                totalAmount += payment.getAmount().doubleValue();
                if (payment.getStatus() == Payment.PaymentStatus.PENDING) {
                    pendingAmount += payment.getAmount().doubleValue();
                    pendingCount++;
                } else if (payment.getStatus() == Payment.PaymentStatus.COMPLETED) {
                    completedCount++;
                }
            }
        }
        
        // Update summary
        lblSummary.setText(String.format("📈 Total: $%.2f | ✓ Completed: %d | ⏳ Pending: %d ($%.2f)",
            totalAmount, completedCount, pendingCount, pendingAmount));
    }
    
    private void processPayment(int paymentId) {
        // Find payment and enrollment
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((int) tableModel.getValueAt(i, 0) == paymentId) {
                String status = tableModel.getValueAt(i, 3).toString();
                if (!status.equals("PENDING")) {
                    SwingUtils.showInfo(this, "This payment is already " + status);
                    return;
                }
                
                String amount = tableModel.getValueAt(i, 2).toString();
                String course = tableModel.getValueAt(i, 1).toString();
                
                // Get enrollment ID for this payment
                List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(selectedStudentId);
                int enrollmentId = -1;
                for (Enrollment e : enrollments) {
                    Payment p = enrollmentController.getPaymentForEnrollment(e.getId());
                    if (p != null && p.getId() == paymentId) {
                        enrollmentId = e.getId();
                        break;
                    }
                }
                
                if (enrollmentId < 0) {
                    SwingUtils.showError(this, "Could not find enrollment for this payment.");
                    return;
                }
                
                // Show payment dialog
                String[] methods = {"Cash", "Credit Card", "Debit Card", "Bank Transfer"};
                String method = (String) JOptionPane.showInputDialog(
                    this,
                    "Process payment for:\n\nCourse: " + course + "\nAmount: " + amount + "\n\nSelect payment method:",
                    "💰 Process Payment",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    methods,
                    methods[0]
                );
                
                if (method != null) {
                    try {
                        enrollmentController.processPayment(enrollmentId, method);
                        SwingUtils.showSuccess(this, "Payment successful!\n\nAmount: " + amount + "\nMethod: " + method);
                        loadSelectedStudentPayments(); // Refresh table
                    } catch (Exception ex) {
                        SwingUtils.showError(this, "Payment failed: " + ex.getMessage());
                    }
                }
                return;
            }
        }
    }
    
    public void refresh() {
        refreshStudentList();
        if (selectedStudentId > 0) {
            loadSelectedStudentPayments();
        }
    }
    
    // Custom button renderer
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            String text = value != null ? value.toString() : "";
            setText(text);
            
            if (text.equals("Pay")) {
                setBackground(new Color(76, 175, 80));
                setForeground(Color.WHITE);
                setEnabled(true);
            } else {
                setBackground(Color.LIGHT_GRAY);
                setForeground(Color.GRAY);
                setText("-");
                setEnabled(false);
            }
            
            return this;
        }
    }
    
    // Custom button editor
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int paymentId;
        
        public ButtonEditor() {
            super(new JCheckBox());
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                fireEditingStopped();
                if (paymentId > 0) {
                    processPayment(paymentId);
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            String text = value != null ? value.toString() : "";
            button.setText(text);
            
            if (text.equals("Pay")) {
                paymentId = (int) tableModel.getValueAt(row, 0);
                button.setBackground(new Color(76, 175, 80));
                button.setForeground(Color.WHITE);
            } else {
                paymentId = -1;
                button.setBackground(Color.LIGHT_GRAY);
            }
            
            return button;
        }
    }
}
