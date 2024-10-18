import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.text.NumberFormat;

public class TablePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    // Toggle buttons for filtering
    private JToggleButton totalToggle1, totalToggle2, totalToggle3;
    private JToggleButton dailyToggle1, dailyToggle2;

    private JLabel rowCountLabel;
    private double minTotalStreams = 0;
    private double maxTotalStreams = Double.MAX_VALUE;
    private double minDailyStreams = 0;
    private double maxDailyStreams = Double.MAX_VALUE;

    private ArrayList<DataModel> originalData;

    public TablePanel(ArrayList<DataModel> data) {
        this.originalData = data;
        setLayout(new BorderLayout());

        initializeTable();
        initializeFilterPanel();
        initializeRowCountLabel();

        populateTable(originalData);
    }

    // Initializes the JTable and sorter for the table
    private void initializeTable() {
        String[] columnNames = {"Artist and Title", "Artist", "Total Streams", "Daily Streams", "Year", "Main Genre"};

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Initializes the filter panel with toggle buttons
    private void initializeFilterPanel() {
        JPanel filterPanel = new JPanel(new GridLayout(2, 3, 10, 10));

        // Total Streams toggle buttons
        totalToggle1 = createToggleButton("Total Streams <= 150,000,000", filterPanel, true);
        totalToggle2 = createToggleButton("Total Streams >= 1,000,000,000", filterPanel, true);
        totalToggle3 = createToggleButton("Total Streams >= 2,000,000,000", filterPanel, true);

        // Daily Streams toggle buttons
        dailyToggle1 = createToggleButton("Daily Streams <= 50,000", filterPanel, false);
        dailyToggle2 = createToggleButton("Daily Streams >= 50,000", filterPanel, false);

        add(filterPanel, BorderLayout.NORTH);
    }

    // Helper method to create a toggle button
    private JToggleButton createToggleButton(String label, JPanel parentPanel, boolean isTotal) {
        JToggleButton toggleButton = new JToggleButton(label);
        toggleButton.setPreferredSize(new Dimension(200, 40));
        toggleButton.addActionListener(e -> applyOrRemoveFilter(isTotal, toggleButton));
        parentPanel.add(toggleButton);
        return toggleButton;
    }

    // Initializes the row count label
    private void initializeRowCountLabel() {
        rowCountLabel = new JLabel("Rows: " + tableModel.getRowCount());
        rowCountLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(rowCountLabel, BorderLayout.SOUTH);
    }

    // Populates the table with data
    private void populateTable(ArrayList<DataModel> data) {
        tableModel.setRowCount(0); // Clear the table

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        for (DataModel model : data) {
            Object[] rowData = {
                    model.getArtist_and_title(),
                    model.getArtist(),
                    numberFormat.format(model.getTotal_streams()),
                    model.getDaily_streams(),
                    model.getYear(),
                    model.getMain_genre(),
            };
            tableModel.addRow(rowData);
        }
    }

    // Applies or removes the filter based on toggle button state
    private void applyOrRemoveFilter(boolean isTotal, JToggleButton toggleButton) {
        if (toggleButton.isSelected()) {
            setFilterRange(isTotal, toggleButton);
            deselectConflictingToggles(isTotal, toggleButton);
        } else {
            resetFilterRange(isTotal);
        }

        applyRowFilter();
        updateRowCountLabel();
    }

    // Sets the filter range based on the selected toggle button
    private void setFilterRange(boolean isTotal, JToggleButton selectedToggle) {
        if (isTotal) {
            if (selectedToggle == totalToggle1) {
                minTotalStreams = 0;
                maxTotalStreams = 150_000_000;
            } else if (selectedToggle == totalToggle2) {
                minTotalStreams = 1_000_000_000;
                maxTotalStreams = Double.MAX_VALUE;
            } else if (selectedToggle == totalToggle3) {
                minTotalStreams = 2_000_000_000;
                maxTotalStreams = Double.MAX_VALUE;
            }
        } else {
            if (selectedToggle == dailyToggle1) {
                minDailyStreams = 0;
                maxDailyStreams = 50_000;
            } else if (selectedToggle == dailyToggle2) {
                minDailyStreams = 50_000;
                maxDailyStreams = Double.MAX_VALUE;
            }
        }
    }

    // Deselects conflicting toggle buttons
    private void deselectConflictingToggles(boolean isTotal, JToggleButton selectedToggle) {
        if (isTotal) {
            if (selectedToggle == totalToggle1) {
                totalToggle2.setSelected(false);
                totalToggle3.setSelected(false);
            } else if (selectedToggle == totalToggle2) {
                totalToggle1.setSelected(false);
                totalToggle3.setSelected(false);
            } else if (selectedToggle == totalToggle3) {
                totalToggle1.setSelected(false);
                totalToggle2.setSelected(false);
            }
        } else {
            if (selectedToggle == dailyToggle1) {
                dailyToggle2.setSelected(false);
            } else if (selectedToggle == dailyToggle2) {
                dailyToggle1.setSelected(false);
            }
        }
    }

    // Resets the filter range to default values
    private void resetFilterRange(boolean isTotal) {
        if (isTotal) {
            minTotalStreams = 0;
            maxTotalStreams = Double.MAX_VALUE;
        } else {
            minDailyStreams = 0;
            maxDailyStreams = Double.MAX_VALUE;
        }
    }

    // Applies the custom row filter to the table
    private void applyRowFilter() {
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                double totalStreams = Double.parseDouble(entry.getStringValue(2).replace(",", ""));
                double dailyStreams = Double.parseDouble(entry.getStringValue(3));

                return totalStreams >= minTotalStreams && totalStreams <= maxTotalStreams &&
                        dailyStreams >= minDailyStreams && dailyStreams <= maxDailyStreams;
            }
        });
    }

    // Updates the row count label after applying or removing filters
    private void updateRowCountLabel() {
        rowCountLabel.setText("Rows: " + sorter.getViewRowCount());
    }
}
