package util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Utility class for Swing GUI operations.
 */
public class SwingUtils {

    // Colors
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    public static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    public static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    public static final Color DANGER_COLOR = new Color(192, 57, 43);
    public static final Color WARNING_COLOR = new Color(243, 156, 18);
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    public static final Color CARD_COLOR = Color.WHITE;

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    /**
     * Creates a styled button.
     */
    public static JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    /**
     * Creates a primary styled button.
     */
    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY_COLOR);
    }

    /**
     * Creates a success styled button.
     */
    public static JButton createSuccessButton(String text) {
        return createButton(text, SUCCESS_COLOR);
    }

    /**
     * Creates a danger styled button.
     */
    public static JButton createDangerButton(String text) {
        return createButton(text, DANGER_COLOR);
    }

    /**
     * Creates a styled text field.
     */
    public static JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(LABEL_FONT);
        textField.setPreferredSize(new Dimension(200, 30));
        return textField;
    }

    /**
     * Creates a styled label.
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }

    /**
     * Creates a styled title label.
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(SECONDARY_COLOR);
        return label;
    }

    /**
     * Creates a styled header label.
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(SECONDARY_COLOR);
        return label;
    }

    /**
     * Creates a styled combo box.
     */
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(LABEL_FONT);
        comboBox.setPreferredSize(new Dimension(200, 30));
        return comboBox;
    }

    /**
     * Creates a styled table.
     */
    public static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(LABEL_FONT);
        table.setRowHeight(30);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        table.setShowGrid(true);

        // Header styling with custom renderer for visibility
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        // Custom header renderer to ensure text is visible
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                label.setBackground(SECONDARY_COLOR);
                label.setForeground(Color.WHITE);
                label.setFont(HEADER_FONT);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(100, 100, 100)));
                label.setOpaque(true);
                return label;
            }
        });

        // Center alignment for cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        return table;
    }

    /**
     * Creates a panel with card styling.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        return panel;
    }

    /**
     * Shows info dialog.
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows success dialog.
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows error dialog.
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows warning dialog.
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows confirmation dialog.
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Shows input dialog.
     */
    public static String showInput(Component parent, String message) {
        return JOptionPane.showInputDialog(parent, message);
    }

    /**
     * Creates a form row with label and component.
     */
    public static JPanel createFormRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setOpaque(false);

        JLabel label = createLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25));

        row.add(label);
        row.add(component);

        return row;
    }

    /**
     * Creates a stat card for dashboard.
     */
    public static JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, color),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));
        card.setPreferredSize(new Dimension(180, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(LABEL_FONT);
        titleLabel.setForeground(Color.GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(TITLE_FONT);
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Creates a navigation button with auto-sized width based on text.
     * 
     * @param text    Button text
     * @param bgColor Background color
     * @return Styled JButton with auto-calculated width
     */
    public static JButton createAutoSizeNavButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Auto-calculate width based on text
        FontMetrics fm = button.getFontMetrics(button.getFont());
        int textWidth = fm.stringWidth(text);
        int buttonWidth = Math.max(85, textWidth + 24);
        button.setPreferredSize(new Dimension(buttonWidth, 30));
        button.setMinimumSize(new Dimension(buttonWidth, 30));
        button.setMaximumSize(new Dimension(buttonWidth, 30));

        return button;
    }
}
