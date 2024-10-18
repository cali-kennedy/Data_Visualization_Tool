import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.text.NumberFormat;

public class TablePanel extends JPanel {
    // Constants for Stream Ranges
    private static final double MIN_STREAMS_DEFAULT = 0;
    private static final double MAX_STREAMS_DEFAULT = Double.MAX_VALUE;
    private static final double MAX_TOTAL_STREAMS_LOW = 150_000_000;
    private static final double MIN_TOTAL_STREAMS_MEDIUM = 1_000_000_000;
    private static final double MIN_TOTAL_STREAMS_HIGH = 2_000_000_000;
    private static final double MAX_DAILY_STREAMS_LOW = 50_000;
    private static final double MIN_DAILY_STREAMS_HIGH = 50_000;

    private double minTotalStreams = MIN_STREAMS_DEFAULT;
    private double maxTotalStreams = MAX_STREAMS_DEFAULT;
    private double minDailyStreams = MIN_STREAMS_DEFAULT;
    private double maxDailyStreams = MAX_STREAMS_DEFAULT;

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    // Toggle buttons for filtering
    private JToggleButton lowTotalStreamsToggle, mediumTotalStreamsToggle, highTotalStreamsToggle;
    private JToggleButton lowDailyStreamsToggle, highDailyStreamsToggle;

    private JLabel rowCountLabel;
    private ArrayList<DataModel> originalData;

    public TablePanel(ArrayList<DataModel> data) {
        this.originalData = data;
        setLayout(new BorderLayout());

        initializeTable();
        initializeFilterPanel();
        initializeFooterPanel();
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
        lowTotalStreamsToggle = createToggleButton("Total Streams <= 150,000,000", filterPanel, true);
        mediumTotalStreamsToggle = createToggleButton("Total Streams >= 1,000,000,000", filterPanel, true);
        highTotalStreamsToggle = createToggleButton("Total Streams >= 2,000,000,000", filterPanel, true);

        // Daily Streams toggle buttons
        lowDailyStreamsToggle = createToggleButton("Daily Streams <= 50,000", filterPanel, false);
        highDailyStreamsToggle = createToggleButton("Daily Streams >= 50,000", filterPanel, false);

        add(filterPanel, BorderLayout.NORTH);
    }

    // Method to show the aggregate statistics panel
    private void showStatisticsPanel() {
        // Create a new frame to display the statistics
        JFrame statisticsFrame = new JFrame("Aggregate Statistics");
        statisticsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statisticsFrame.setSize(400, 200);

        // Get the filtered data from the table
        ArrayList<DataModel> filteredData = getFilteredData();

        // Create a new StatsPanel and pass the filtered data
        StatsPanel statisticsPanel = new StatsPanel(filteredData);
        statisticsFrame.add(statisticsPanel);

        // Make the frame visible
        statisticsFrame.setVisible(true);
    }

    // Helper method to create a toggle button
    private JToggleButton createToggleButton(String label, JPanel parentPanel, boolean isTotal) {
        JToggleButton toggleButton = new JToggleButton(label);
        toggleButton.setPreferredSize(new Dimension(200, 40));
        toggleButton.addActionListener(e -> applyOrRemoveFilter(isTotal, toggleButton));
        parentPanel.add(toggleButton);
        return toggleButton;
    }

    // Create a new panel to hold both the row count label and the show statistics button
    private void initializeFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Create a panel to hold the label and button
        rowCountLabel = new JLabel("Rows: " + tableModel.getRowCount());
        rowCountLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Add row count label to the footer panel
        footerPanel.add(rowCountLabel);

        // Add a button to show aggregate statistics
        JButton showStatisticsButton = new JButton("Show Aggregate Statistics");
        showStatisticsButton.addActionListener(e -> showStatisticsPanel());

        // Add the show statistics button to the footer panel
        footerPanel.add(showStatisticsButton);

        // Add the footer panel to the bottom of the main panel
        add(footerPanel, BorderLayout.SOUTH);
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
            if (selectedToggle == lowTotalStreamsToggle) {
                minTotalStreams = MIN_STREAMS_DEFAULT;
                maxTotalStreams = MAX_TOTAL_STREAMS_LOW;
            } else if (selectedToggle == mediumTotalStreamsToggle) {
                minTotalStreams = MIN_TOTAL_STREAMS_MEDIUM;
                maxTotalStreams = MAX_STREAMS_DEFAULT;
            } else if (selectedToggle == highTotalStreamsToggle) {
                minTotalStreams = MIN_TOTAL_STREAMS_HIGH;
                maxTotalStreams = MAX_STREAMS_DEFAULT;
            }
        } else {
            if (selectedToggle == lowDailyStreamsToggle) {
                minDailyStreams = MIN_STREAMS_DEFAULT;
                maxDailyStreams = MAX_DAILY_STREAMS_LOW;
            } else if (selectedToggle == highDailyStreamsToggle) {
                minDailyStreams = MIN_DAILY_STREAMS_HIGH;
                maxDailyStreams = MAX_STREAMS_DEFAULT;
            }
        }
    }

    // Deselects conflicting toggle buttons
    private void deselectConflictingToggles(boolean isTotal, JToggleButton selectedToggle) {
        JToggleButton[] totalToggles = {lowTotalStreamsToggle, mediumTotalStreamsToggle, highTotalStreamsToggle};
        JToggleButton[] dailyToggles = {lowDailyStreamsToggle, highDailyStreamsToggle};

        if (isTotal) {
            deselectOthers(selectedToggle, totalToggles);
        } else {
            deselectOthers(selectedToggle, dailyToggles);
        }
    }

    // Helper method to deselect all toggles except the selected one
    private void deselectOthers(JToggleButton selectedToggle, JToggleButton[] toggleButtons) {
        for (JToggleButton toggle : toggleButtons) {
            if (toggle != selectedToggle) {
                toggle.setSelected(false);
            }
        }
    }

    // Resets the filter range to default values
    private void resetFilterRange(boolean isTotal) {
        if (isTotal) {
            minTotalStreams = MIN_STREAMS_DEFAULT;
            maxTotalStreams = MAX_STREAMS_DEFAULT;
        } else {
            minDailyStreams = MIN_STREAMS_DEFAULT;
            maxDailyStreams = MAX_STREAMS_DEFAULT;
        }
    }


     //Applies a row filter to the table based on the selected total streams and daily streams filter ranges.
    private void applyRowFilter() {
        // Set a custom row filter for the table sorter.
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {

            /**
             * Determines whether a table row should be included based on the filter criteria.
             * @param entry - The table row entry being evaluated.
             * @return boolean - Returns true if the row meets the filter criteria; otherwise, false.
             */
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                // Parse the total streams value from the 3rd column (index 2), removing commas for proper parsing.
                double totalStreams = Double.parseDouble(entry.getStringValue(2).replace(",", ""));

                // Parse the daily streams value from the 4th column (index 3).
                double dailyStreams = Double.parseDouble(entry.getStringValue(3));

                // Check if the row's total streams and daily streams values fall within the filter's range.
                return totalStreams >= minTotalStreams && totalStreams <= maxTotalStreams &&
                        dailyStreams >= minDailyStreams && dailyStreams <= maxDailyStreams;
            }
        });
    }
    // Method to get the currently filtered data from the table
    private ArrayList<DataModel> getFilteredData() {
        ArrayList<DataModel> filteredData = new ArrayList<>();

        // Loop through the visible rows after filtering
        for (int i = 0; i < table.getRowCount(); i++) {
            int modelIndex = table.convertRowIndexToModel(i); // Get the model index from the view index

            String artistAndTitle = (String) tableModel.getValueAt(modelIndex, 0);
            String artist = (String) tableModel.getValueAt(modelIndex, 1);
            double totalStreams = Double.parseDouble(tableModel.getValueAt(modelIndex, 2).toString().replace(",", ""));
            double dailyStreams = Double.parseDouble(tableModel.getValueAt(modelIndex, 3).toString());
            double year = Double.parseDouble(tableModel.getValueAt(modelIndex, 4).toString());
            String mainGenre = (String) tableModel.getValueAt(modelIndex, 5);

            // Create a new DataModel for the current row
            DataModel model = new DataModel();
            model.setArtist_and_title(artistAndTitle);
            model.setArtist(artist);
            model.setTotal_streams(totalStreams);
            model.setDaily_streams(dailyStreams);
            model.setYear(year);
            model.setMain_genre(mainGenre);

            filteredData.add(model);  // Add the model to the filtered data list
        }
        return filteredData;
    }


    // Updates the row count label after applying or removing filters
    private void updateRowCountLabel() {
        rowCountLabel.setText("Rows: " + sorter.getViewRowCount());
    }
}
