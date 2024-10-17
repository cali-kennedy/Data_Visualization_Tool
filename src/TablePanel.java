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

    public TablePanel(ArrayList<DataModel> data) {
        setLayout(new BorderLayout());

        // Define column names
        String[] columnNames = {"Artist and Title", "Artist", "Total Streams", "Daily Streams", "Year", "Main Genre"};

        // Create table model with column names
        tableModel = new DefaultTableModel(columnNames, 0);

        // Create a NumberFormat to format the streams without scientific notation
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true); // This will add commas for thousands

        // Populate the table model with data from the ArrayList
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

        // Create JTable with the table model
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        // Add the table to a scroll pane and add the scroll pane to the panel
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}
