package util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Utility class for Swing GUI operations.
 * Uses FlatLaf LIGHT theme with visual depth and hierarchy.
 */
public class SwingUtils {

    // Modern accent colors (lighter variants for light mode)
    public static final Color PRIMARY_COLOR = new Color(0, 120, 215);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color SECONDARY_COLOR = new Color(108, 117, 125);

    // Light mode background layers
    public static final Color BG_WHITE = new Color(255, 255, 255);
    public static final Color BG_LIGHT = new Color(248, 249, 250);
    public static final Color BG_MEDIUM = new Color(240, 240, 240);
    public static final Color BG_DARK = new Color(230, 230, 230);

    // Theme-aware fallbacks
    public static final Color BACKGROUND_COLOR = UIManager.getColor("Panel.background");
    public static final Color CARD_COLOR = UIManager.getColor("Panel.background");
    public static final Font LABEL_FONT = UIManager.getFont("Label.font");

    /**
     * Creates a standard button.
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        return button;
    }

    /**
     * Creates a primary styled button (accent color).
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a success styled button (green).
     */
    public static JButton createSuccessButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(Color.WHITE);
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a danger styled button (red).
     */
    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.WHITE);
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a standard text field with enhanced styling.
     */
    public static JTextField createTextField() {
        JTextField textField = new JTextField(25);
        textField.setPreferredSize(new Dimension(220, 32));
        textField.setMinimumSize(new Dimension(180, 32));
        return textField;
    }

    /**
     * Creates a standard label.
     */
    public static JLabel createLabel(String text) {
        return new JLabel(text);
    }

    /**
     * Creates a title label with larger font (FlatLaf h1 style).
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.putClientProperty("FlatLaf.styleClass", "h1");
        label.setForeground(new Color(40, 40, 40));
        return label;
    }

    /**
     * Creates a header label (FlatLaf h3 style).
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.putClientProperty("FlatLaf.styleClass", "h3");
        label.setForeground(PRIMARY_COLOR);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    /**
     * Creates a standard combo box.
     */
    public static <T> JComboBox<T> createComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return comboBox;
    }

    /**
     * Creates a styled table with modern appearance and alternating rows.
     */
    public static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(36);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(230, 230, 230));

        return table;
    }

    /**
     * Creates a card panel with light shadow effect.
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_WHITE);

        // Light shadow border
        Border shadow = BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(220, 220, 220));
        Border line = BorderFactory.createLineBorder(new Color(230, 230, 230), 1);
        Border padding = BorderFactory.createEmptyBorder(16, 16, 16, 16);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(shadow, line),
                padding));

        return panel;
    }

    /**
     * Creates an elevated panel for interactive sections.
     */
    public static JPanel createElevatedPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        return panel;
    }

    /**
     * Shows info dialog.
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Bilgi",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows success dialog.
     */
    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Basarili",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows error dialog.
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Hata",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows warning dialog.
     */
    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Uyari",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows confirmation dialog.
     */
    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Onayla",
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
     * Creates a form row with label and component - enhanced spacing.
     */
    public static JPanel createFormRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 32));
        label.setForeground(new Color(80, 80, 80));

        // Ensure component has proper size
        if (component instanceof JTextField) {
            component.setPreferredSize(new Dimension(220, 32));
        } else if (component instanceof JComboBox) {
            component.setPreferredSize(new Dimension(220, 32));
        }

        row.add(label, BorderLayout.WEST);
        row.add(component, BorderLayout.CENTER);
        return row;
    }

    /**
     * Creates a stat card for dashboard with accent color and depth.
     */
    public static JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(BG_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, color),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                        BorderFactory.createEmptyBorder(16, 20, 16, 20))));
        card.setPreferredSize(new Dimension(180, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(100, 100, 100));

        JLabel valueLabel = new JLabel(value);
        valueLabel.putClientProperty("FlatLaf.styleClass", "h1");
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * Creates a navigation button.
     */
    public static JButton createAutoSizeNavButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a section separator with title.
     */
    public static JPanel createSectionSeparator(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel label = new JLabel(title);
        label.setForeground(PRIMARY_COLOR);
        label.putClientProperty("FlatLaf.styleClass", "h3");

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));

        panel.add(label, BorderLayout.WEST);
        panel.add(separator, BorderLayout.CENTER);

        return panel;
    }
}
