import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StatsPanel extends JPanel {
    private JLabel averageDailyStreamsLabel;
    private JTextArea topArtistsTextArea;
    private JTextArea yearlyGrowthTextArea;
    private JTextArea totalStreamsByYearTextArea;
    private List<DataModel> data;

    public StatsPanel(ArrayList<DataModel> data) {
        this.data = data;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        initializeComponents();
        addComponentsToPanel();
        updateStatistics(data);
    }

    private void initializeComponents() {
        averageDailyStreamsLabel = createStyledLabel("Average Daily Streams", 18, Font.BOLD, Color.DARK_GRAY);

        topArtistsTextArea = createStyledTextArea();
        topArtistsTextArea.setBorder(BorderFactory.createTitledBorder("Top 5 Artists by Total Streams"));

        yearlyGrowthTextArea = createStyledTextArea();
        yearlyGrowthTextArea.setBorder(BorderFactory.createTitledBorder("Yearly Growth Rates"));

        totalStreamsByYearTextArea = createStyledTextArea();
        totalStreamsByYearTextArea.setBorder(BorderFactory.createTitledBorder("Total Streams by Year"));
    }

    private void addComponentsToPanel() {
        add(createSectionPanel(averageDailyStreamsLabel));
        add(createSectionPanel(new JScrollPane(topArtistsTextArea)));
        add(createSectionPanel(new JScrollPane(yearlyGrowthTextArea)));
        add(createSectionPanel(new JScrollPane(totalStreamsByYearTextArea)));

        // Create a new panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));  // Horizontal button layout with padding

        // Add Bar Chart Button
        JButton showBarChartButton = new JButton("Show Bar Chart");
        showBarChartButton.addActionListener(e -> showBarChart());
        buttonPanel.add(showBarChartButton);  // Add bar chart button to the button panel

        // Add Pie Chart Button
        JButton showPieChartButton = new JButton("Show Pie Chart");
        showPieChartButton.addActionListener(e -> showPieChart());
        buttonPanel.add(showPieChartButton);  // Add pie chart button to the button panel

        add(buttonPanel);  // Add the button panel to the StatsPanel
    }

    private void updateStatistics(ArrayList<DataModel> data) {
        double averageDailyStreams = StatsCalculator.calculateAverageDailyStreams(data);
        averageDailyStreamsLabel.setText(String.format("Average Daily Streams: %.2f", averageDailyStreams));

        updateTopArtists(data);
        yearlyGrowthTextArea.setText(StatsCalculator.calculateYearlyGrowthRate(data));
        totalStreamsByYearTextArea.setText(StatsCalculator.calculateTotalStreamsByYear(data));
    }

    private void updateTopArtists(ArrayList<DataModel> data) {
        StringBuilder topArtistsText = new StringBuilder();
        int rank = 1;

        for (var entry : StatsCalculator.getTopArtists(data, 5)) {
            topArtistsText.append(String.format("%d. %s: %.2f total streams\n", rank++, entry.getKey(), entry.getValue()));
        }

        topArtistsTextArea.setText(topArtistsText.toString());
    }

    private JLabel createStyledLabel(String text, int fontSize, int style, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", style, fontSize));
        label.setForeground(color);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBackground(Color.WHITE);
        return textArea;
    }

    private JPanel createSectionPanel(JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.add(component);
        return panel;
    }

    private void showPieChart() {
        JFrame pieChartFrame = new JFrame("Top 5 Artists by Total Streams");
        PieChartPanel pieChartPanel = new PieChartPanel();
        pieChartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Get the top 5 artists from StatsCalculator
        List<DataModel> topArtists = StatsCalculator.getTopArtists((ArrayList<DataModel>) data, 5).stream()
                .map(entry -> data.stream().filter(model -> model.getArtist().equals(entry.getKey())).findFirst().orElse(null))
                .toList();

        // Update the pie chart with the top 5 artists
        pieChartPanel.updatePieChart(topArtists);

        pieChartFrame.add(pieChartPanel);
        pieChartFrame.pack();
        pieChartFrame.setLocationRelativeTo(null);
        pieChartFrame.setVisible(true);
    }

    private void showBarChart() {
        // Create a TreeMap of total streams by year
        TreeMap<Integer, Double> totalStreamsByYear = StatsCalculator.getTotalStreamsByYear((ArrayList<DataModel>) data);

        // Display the bar chart in a new JFrame
        InternalBarChartPanel.visualizeTotalStreamsByYear(totalStreamsByYear);
    }
}

