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
    private JComboBox<String> filterComboBox;
    private ArrayList<DataModel> originalData;

    public TablePanel(ArrayList<DataModel> data) {
        setLayout(new BorderLayout());
        this.originalData = data; // Store the original data for filtering

        // Define column names
        String[] columnNames = {"Artist and Title", "Artist", "Total Streams", "Daily Streams", "Year", "Main Genre"};

        // Create table model with column names
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        populateTable(data);

        // Create JTable with the table model
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        // Add the table to a scroll pane and add the scroll pane to the panel
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Method to populate the table model with data
    private void populateTable(ArrayList<DataModel> data) {
        // Clear the table before repopulating it
        tableModel.setRowCount(0);

        // Create a NumberFormat to format the streams without scientific notation
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true); // This will add commas for thousands

        // Add rows to the table model
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
}
