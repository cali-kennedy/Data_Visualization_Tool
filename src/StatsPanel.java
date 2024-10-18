import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class StatsPanel extends JPanel {

    // UI Components
    private JLabel averageDailyStreamsLabel;
    private JTextArea topArtistsTextArea;
    private JTextArea yearlyGrowthTextArea;
    private JTextArea totalStreamsByYearTextArea;

    // Constructor to initialize the panel with data and display the statistics
    public StatsPanel(ArrayList<DataModel> data) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Vertical layout
        setBackground(new Color(245, 245, 245)); // Light gray background for the entire panel

        // Initialize and style UI components
        initializeComponents();

        // Populate the panel with the UI components
        addComponentsToPanel();

        // Update and display the statistics using the provided data
        updateStatistics(data);
    }

    // Initializes all UI components (labels and text areas)
    private void initializeComponents() {
        averageDailyStreamsLabel = createStyledLabel("Average Daily Streams", 18, Font.BOLD, Color.DARK_GRAY);

        topArtistsTextArea = createStyledTextArea();
        topArtistsTextArea.setBorder(BorderFactory.createTitledBorder("Top 5 Artists by Total Streams"));

        yearlyGrowthTextArea = createStyledTextArea();
        yearlyGrowthTextArea.setBorder(BorderFactory.createTitledBorder("Yearly Growth Rates"));

        totalStreamsByYearTextArea = createStyledTextArea();
        totalStreamsByYearTextArea.setBorder(BorderFactory.createTitledBorder("Total Streams by Year"));
    }

    // Adds the components to the panel with consistent padding
    private void addComponentsToPanel() {
        add(createSectionPanel(averageDailyStreamsLabel));
        add(createSectionPanel(new JScrollPane(topArtistsTextArea)));
        add(createSectionPanel(new JScrollPane(yearlyGrowthTextArea)));
        add(createSectionPanel(new JScrollPane(totalStreamsByYearTextArea)));
    }

    // Updates the panel with the calculated statistics
    private void updateStatistics(ArrayList<DataModel> data) {
        // Update average daily streams
        double averageDailyStreams = StatsCalculator.calculateAverageDailyStreams(data);
        averageDailyStreamsLabel.setText(String.format("Average Daily Streams: %.2f", averageDailyStreams));

        // Update top artists section
        updateTopArtists(data);

        // Update yearly growth rate section
        yearlyGrowthTextArea.setText(StatsCalculator.calculateYearlyGrowthRate(data));

        // Update total streams by year section
        totalStreamsByYearTextArea.setText(StatsCalculator.calculateTotalStreamsByYear(data));
    }

    // Updates the top artists text area by formatting the top 5 artists with total streams
    private void updateTopArtists(ArrayList<DataModel> data) {
        StringBuilder topArtistsText = new StringBuilder("Top 5 Artists by Total Streams:\n");
        int rank = 1;

        for (Map.Entry<String, Double> entry : StatsCalculator.getTopArtists(data, 5)) {
            topArtistsText.append(String.format("%d. %s: %.2f total streams\n", rank++, entry.getKey(), entry.getValue()));
        }

        topArtistsTextArea.setText(topArtistsText.toString());
    }

    // Helper method to create a styled label with custom font, size, and color
    private JLabel createStyledLabel(String text, int fontSize, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", style, fontSize));
        label.setForeground(color); // Custom text color
        label.setAlignmentX(CENTER_ALIGNMENT); // Center-align the label
        return label;
    }

    // Helper method to create a styled JTextArea with default styling
    private JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // Prevent editing
        textArea.setLineWrap(true); // Wrap text lines
        textArea.setWrapStyleWord(true); // Wrap whole words
        textArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font size and style
        textArea.setBackground(Color.WHITE); // White background for readability
        return textArea;
    }

    // Helper method to create a section panel with padding and background color
    private JPanel createSectionPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the component
        panel.setBackground(Color.LIGHT_GRAY); // Set background color for the section
        panel.add(component); // Add the component to the panel
        return panel;
    }
}
