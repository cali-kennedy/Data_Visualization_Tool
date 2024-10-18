import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatsPanel extends JPanel {

    private JLabel averageDailyStreamsLabel;
    private JPanel statsContainer;

    public StatsPanel(ArrayList<DataModel> data) {
        setLayout(new BorderLayout());

        // Initialize a container panel with GridLayout for the statistics
        statsContainer = new JPanel(new GridLayout(4, 1, 10, 10)); // 4 rows, 1 column, with spacing
        statsContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Create and set up labels for statistics
        averageDailyStreamsLabel = new JLabel();

        // Add the labels to the container panel
        statsContainer.add(averageDailyStreamsLabel);

        // Add the stats container to the main panel
        add(statsContainer, BorderLayout.CENTER);

        // Add a titled border around the stats container
        statsContainer.setBorder(BorderFactory.createTitledBorder("Aggregate Statistics"));

        // Calculate and display statistics
        calculateAndDisplayAverageDailyStreams(data);

        // (More aggregate statistic methods can be added here)
    }

    // Method to calculate and display average daily streams
    private void calculateAndDisplayAverageDailyStreams(ArrayList<DataModel> data) {
        double totalDailyStreams = 0;
        int count = data.size();

        // Calculate the total daily streams
        for (DataModel model : data) {
            totalDailyStreams += model.getDaily_streams();
        }

        // Calculate the average
        double averageDailyStreams = count > 0 ? totalDailyStreams / count : 0;

        // Set font and padding for the label
        averageDailyStreamsLabel.setText("Average Daily Streams : " + String.format("%.2f", averageDailyStreams));
        averageDailyStreamsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        averageDailyStreamsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }


    private void calculateAndDisplayTopArtist(ArrayList<DataModel> data) {

    }
}
