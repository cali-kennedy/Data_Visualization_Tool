import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class TablePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private final DataManager dataManager;
    private final ArrayList<DataModel> originalData;
    private DataSorter sorter; // Current sorting strategy

    public TablePanel(ArrayList<DataModel> data, DataManager dataManager) {
        this.originalData = data;
        this.dataManager = dataManager;
        this.sorter = new SortByTotalStreams(); // Default sorting strategy

        setLayout(new BorderLayout());
        initializeTable();
        initializeSortingControls();
        populateTable(originalData);
    }

    private void initializeTable() {
        String[] columnNames = {"Artist and Title", "Artist", "Total Streams", "Daily Streams", "Year", "Main Genre"};

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(20);
        table.setAutoscrolls(true);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelIndex = table.convertRowIndexToModel(selectedRow);
                    DataModel selectedItem = originalData.get(modelIndex);
                    dataManager.setSelectedData(selectedItem); // Notify DataManager of selection
                }
            }
        });
    }

    private void initializeSortingControls() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> sortingOptions = new JComboBox<>(new String[]{"Sort by Total Streams", "Sort by Year"});
        sortingOptions.addActionListener(e -> {
            String selectedOption = (String) sortingOptions.getSelectedItem();
            if ("Sort by Total Streams".equals(selectedOption)) {
                sorter = new SortByTotalStreams();
            } else if ("Sort by Year".equals(selectedOption)) {
                sorter = new SortByYear();
            }
            applySorting();
        });
        controlPanel.add(sortingOptions);
        add(controlPanel, BorderLayout.NORTH);
    }

    private void applySorting() {
        sorter.sort(originalData);
        populateTable(originalData);
    }

    private void populateTable(ArrayList<DataModel> data) {
        tableModel.setRowCount(0);

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        for (DataModel model : data) {
            Object[] rowData = {
                    model.getArtist_and_title(),
                    model.getArtist(),
                    numberFormat.format(model.getTotal_streams()),
                    model.getDaily_streams(),
                    model.getYear(),
                    model.getMain_genre()
            };
            tableModel.addRow(rowData);
        }
    }
}
