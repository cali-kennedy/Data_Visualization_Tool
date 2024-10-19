import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.List;

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

    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    // Toggle buttons for filtering
    private JToggleButton lowTotalStreamsToggle, mediumTotalStreamsToggle, highTotalStreamsToggle;
    private JToggleButton lowDailyStreamsToggle, highDailyStreamsToggle;
    private JToggleButton showPieChartToggle;

    private DetailsPanel detailsPanel;  // Panel for showing details when a row is selected
    private JLabel rowCountLabel;  // Displays the number of rows currently shown
    private ArrayList<DataModel> originalData;  // Original list of data for the table

    // TablePanel Constructor
    public TablePanel(ArrayList<DataModel> data) {
        this.originalData = data;
        setLayout(new BorderLayout());

        initializeTable();  // Sets up the JTable and its data model
        initializeFilterPanel();  // Adds filter toggle buttons to filter data
        initializeDetailsPanel();  // Adds a panel to show details of the selected row
        initializeFooterPanel();  // Footer panel with row count and statistics buttons
        populateTable(originalData);  // Populates the table with the data
    }

    // Initializes the JTable and sorter for the table
    private void initializeTable() {
        String[] columnNames = {"Artist and Title", "Artist", "Total Streams", "Daily Streams", "Year", "Main Genre"};

        tableModel = new DefaultTableModel(columnNames, 0);  // Create the table model with column names
        table = new JTable(tableModel);  // Create the JTable
        table.setRowHeight(20);  // Set row height
        table.setAutoscrolls(true);

        sorter = new TableRowSorter<>(tableModel);  // Enable sorting for table columns
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);  // Add table to the center of the panel

        // Listener to update the DetailsPanel when a row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelIndex = table.convertRowIndexToModel(selectedRow);
                    DataModel selectedItem = originalData.get(modelIndex);
                    detailsPanel.updateDetails(selectedItem);  // Update details panel with selected item
                }
            }
        });
    }

    // Initializes the DetailsPanel and adds it to the right side of the table
    private void initializeDetailsPanel() {
        detailsPanel = new DetailsPanel();  // Create the DetailsPanel
        detailsPanel.setPreferredSize(new Dimension(300, 300));  // Set the preferred size
        add(detailsPanel, BorderLayout.EAST);  // Add the details panel to the right side
    }

    // Initializes the filter panel with toggle buttons
    private void initializeFilterPanel() {
        JPanel filterPanel = new JPanel(new GridLayout(2, 3, 10, 10));  // Grid layout for toggle buttons

        // Create toggle buttons for total streams filters
        lowTotalStreamsToggle = createToggleButton("Total Streams <= 150,000,000", filterPanel, true);
        mediumTotalStreamsToggle = createToggleButton("Total Streams >= 1,000,000,000", filterPanel, true);
        highTotalStreamsToggle = createToggleButton("Total Streams >= 2,000,000,000", filterPanel, true);

        // Create toggle buttons for daily streams filters
        lowDailyStreamsToggle = createToggleButton("Daily Streams <= 50,000", filterPanel, false);
        highDailyStreamsToggle = createToggleButton("Daily Streams >= 50,000", filterPanel, false);

        // Toggle button for showing pie chart
        showPieChartToggle = createToggleButton("Show Pie Chart", filterPanel, false);

        add(filterPanel, BorderLayout.NORTH);  // Add filter panel to the top of the panel
    }

    // Method to show the aggregate statistics panel in a new window
    private void showStatisticsPanel() {
        JFrame statisticsFrame = new JFrame("Aggregate Statistics");
        statisticsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statisticsFrame.setSize(550, 400);

        // Get the filtered data from the table and pass to StatsPanel
        ArrayList<DataModel> filteredData = getFilteredData();
        StatsPanel statisticsPanel = new StatsPanel(filteredData);
        statisticsFrame.add(statisticsPanel);

        statisticsFrame.setVisible(true);  // Show the statistics window
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
        footerPanel.add(showPieChartToggle);
        showPieChartToggle.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        showPieChartToggle.setPreferredSize(new Dimension(200,30));
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
            } else if (selectedToggle == showPieChartToggle) {
                // make a new method that will show pie chart panel should have it already in stats panel
                showPieChart();
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

    private void showPieChart() {
        JFrame pieChartFrame = new JFrame("Top 5 Artists by Total Streams");
        PieChartPanel pieChartPanel = new PieChartPanel();
        pieChartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Get the top 5 artists from StatsCalculator
        List<DataModel> topArtists = StatsCalculator.getTopArtists((getFilteredData()), 5).stream()
                .map(entry -> getFilteredData().stream().filter(model -> model.getArtist().equals(entry.getKey())).findFirst().orElse(null))
                .toList();

        // Update the pie chart with the top 5 artists
        pieChartPanel.updatePieChart(topArtists);

        pieChartFrame.add(pieChartPanel);
        pieChartFrame.pack();
        pieChartFrame.setLocationRelativeTo(null);
        pieChartFrame.setVisible(true);
    }

    // Updates the row count label after applying or removing filters
    private void updateRowCountLabel() {
        rowCountLabel.setText("Rows: " + sorter.getViewRowCount());
    }
}
