package view;

import controller.EnrollmentController;
import model.entity.Payment;
import util.SwingUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for payment management.
 */
public class PaymentPanel extends JPanel {
    
    private final EnrollmentController enrollmentController;
    
    private JTable table;
    private DefaultTableModel tableModel;
    
    private int selectedEnrollmentId = -1;
    
    public PaymentPanel(EnrollmentController enrollmentController) {
        this.enrollmentController = enrollmentController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("💰 Payment Management");
        add(title, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setOpaque(false);
        
        // Left: Actions
        mainPanel.add(createActionsPanel(), BorderLayout.WEST);
        
        // Center: Table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        refreshTable();
    }
    
    private JPanel createActionsPanel() {
        JPanel actionsCard = SwingUtils.createCardPanel();
        actionsCard.setLayout(new BoxLayout(actionsCard, BoxLayout.Y_AXIS));
        actionsCard.setPreferredSize(new Dimension(300, 0));
        
        JLabel formTitle = SwingUtils.createHeaderLabel("Process Payment");
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsCard.add(formTitle);
        actionsCard.add(Box.createVerticalStrut(20));
        
        JLabel instructions = new JLabel("<html>1. Select a pending payment from the table<br>" +
            "2. Click 'Process Payment'<br>" +
            "3. Enter payment method</html>");
        instructions.setFont(SwingUtils.LABEL_FONT);
        actionsCard.add(instructions);
        
        actionsCard.add(Box.createVerticalStrut(30));
        
        // Payment methods
        String[] methods = {"Cash", "Credit Card", "Debit Card", "Bank Transfer"};
        JComboBox<String> cmbMethod = SwingUtils.createComboBox(methods);
        actionsCard.add(SwingUtils.createFormRow("Method:", cmbMethod));
        
        actionsCard.add(Box.createVerticalStrut(20));
        
        JButton btnProcess = SwingUtils.createSuccessButton("Process");
        btnProcess.setPreferredSize(new Dimension(200, 40));
        btnProcess.addActionListener(e -> {
            if (selectedEnrollmentId < 0) {
                SwingUtils.showWarning(this, "Please select a pending payment first.");
                return;
            }
            
            try {
                Payment payment = enrollmentController.processPayment(
                    selectedEnrollmentId, (String) cmbMethod.getSelectedItem());
                SwingUtils.showSuccess(this, 
                    "Payment processed successfully!\nAmount: $" + payment.getAmount());
                selectedEnrollmentId = -1;
                refreshTable();
            } catch (IllegalArgumentException ex) {
                SwingUtils.showError(this, ex.getMessage());
            }
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setOpaque(false);
        btnPanel.add(btnProcess);
        actionsCard.add(btnPanel);
        
        // Stats
        actionsCard.add(Box.createVerticalStrut(40));
        actionsCard.add(Box.createVerticalGlue());
        
        return actionsCard;
    }
    
    private JPanel createTablePanel() {
        JPanel tableCard = SwingUtils.createCardPanel();
        tableCard.setLayout(new BorderLayout(0, 10));
        
        JLabel tableTitle = SwingUtils.createHeaderLabel("Pending Payments");
        tableCard.add(tableTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Enrollment ID", "Amount", "Status", "Method", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = SwingUtils.createTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    selectedEnrollmentId = (int) tableModel.getValueAt(row, 1);
                }
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
    
    public void refreshTable() {
        tableModel.setRowCount(0);
        
        List<Payment> payments = enrollmentController.getPendingPayments();
        
        for (Payment p : payments) {
            tableModel.addRow(new Object[]{
                p.getId(),
                p.getEnrollmentId(),
                "$" + p.getAmount(),
                p.getStatus(),
                p.getPaymentMethod() != null ? p.getPaymentMethod() : "-",
                p.getPaymentDate().toLocalDate()
            });
        }
    }
}
