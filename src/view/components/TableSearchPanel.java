package view.components;

import util.SwingUtils;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

/**
 * Reusable search panel component for JTable filtering.
 */
public class TableSearchPanel extends JPanel {
    
    private final JTextField searchField;
    private final JComboBox<String> columnSelector;
    private final JButton clearButton;
    private final JTable table;
    private TableRowSorter<DefaultTableModel> sorter;
    
    public TableSearchPanel(JTable table) {
        this.table = table;
        
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        setOpaque(false);
        
        // Search icon label
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(SwingUtils.LABEL_FONT);
        add(searchLabel);
        
        // Search field
        searchField = new JTextField(20);
        searchField.setFont(SwingUtils.LABEL_FONT);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setToolTipText("Type to search...");
        add(searchField);
        
        // Column selector
        JLabel inLabel = new JLabel("in:");
        inLabel.setFont(SwingUtils.LABEL_FONT);
        add(inLabel);
        
        columnSelector = new JComboBox<>();
        columnSelector.setFont(SwingUtils.LABEL_FONT);
        columnSelector.setPreferredSize(new Dimension(150, 30));
        columnSelector.addItem("All Columns");
        
        // Add table columns to selector
        if (table.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                columnSelector.addItem(model.getColumnName(i));
            }
        }
        add(columnSelector);
        
        // Clear button
        clearButton = SwingUtils.createButton("Clear", Color.GRAY);
        clearButton.setPreferredSize(new Dimension(80, 30));
        clearButton.addActionListener(e -> clearSearch());
        add(clearButton);
        
        // Setup row sorter
        setupRowSorter();
        
        // Add search listener
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
        
        // Column change listener
        columnSelector.addActionListener(e -> filterTable());
    }
    
    /**
     * Sets up the row sorter for the table.
     */
    private void setupRowSorter() {
        if (table.getModel() instanceof DefaultTableModel) {
            sorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(sorter);
        }
    }
    
    /**
     * Refreshes the sorter when table model changes.
     */
    public void refreshSorter() {
        setupRowSorter();
        // Refresh column selector
        columnSelector.removeAllItems();
        columnSelector.addItem("All Columns");
        if (table.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                columnSelector.addItem(model.getColumnName(i));
            }
        }
    }
    
    /**
     * Filters the table based on search text.
     */
    private void filterTable() {
        if (sorter == null) return;
        
        String searchText = searchField.getText().trim();
        int selectedColumn = columnSelector.getSelectedIndex();
        
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        
        try {
            // Case-insensitive search
            RowFilter<DefaultTableModel, Object> filter;
            
            if (selectedColumn == 0) {
                // Search all columns
                filter = RowFilter.regexFilter("(?i)" + escapeRegex(searchText));
            } else {
                // Search specific column (index - 1 because "All Columns" is at 0)
                filter = RowFilter.regexFilter("(?i)" + escapeRegex(searchText), selectedColumn - 1);
            }
            
            sorter.setRowFilter(filter);
        } catch (Exception e) {
            // Invalid regex, clear filter
            sorter.setRowFilter(null);
        }
    }
    
    /**
     * Escapes special regex characters.
     */
    private String escapeRegex(String text) {
        return text.replaceAll("[\\\\^$.|?*+()\\[\\]{}]", "\\\\$0");
    }
    
    /**
     * Clears the search field and filter.
     */
    public void clearSearch() {
        searchField.setText("");
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }
    
    /**
     * Gets the current search text.
     */
    public String getSearchText() {
        return searchField.getText();
    }
    
    /**
     * Sets focus to the search field.
     */
    public void focusSearch() {
        searchField.requestFocusInWindow();
    }
}
