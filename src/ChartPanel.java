import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class ChartPanel extends JPanel {
    private final TreeMap<Integer, Double> data;  // Year as key, Total Streams as value

    public ChartPanel(TreeMap<Integer, Double> data) {
        this.data = data;
        setPreferredSize(new Dimension(800, 400));  // Set panel size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Define chart dimensions and margins
        int chartWidth = 700;
        int chartHeight = 300;
        int xOffset = 80;
        int yOffset = 50;
        int barWidth = chartWidth / data.size();

        // Set a fixed max value for Y-axis scaling (e.g., 500 billion) to give enough space
        double maxTotalStreams = 500_000_000_000.0;

        // Define tick marks (Y-axis intervals) with a fixed interval (e.g., 100 billion)
        int numTicks = 5;
        double tickInterval = maxTotalStreams / numTicks;

        // Set font for labels
        g.setFont(new Font("Arial", Font.BOLD, 12));

        // Draw X and Y axes
        g.drawLine(xOffset, yOffset, xOffset, chartHeight + yOffset); // Y-axis
        g.drawLine(xOffset, chartHeight + yOffset, chartWidth + xOffset, chartHeight + yOffset); // X-axis

        // Labels for X-axis (Year)
        g.drawString("Year", chartWidth / 2 + xOffset, chartHeight + yOffset + 40);

        // Draw Y-axis tick marks, labels, and grid lines
        for (int i = 0; i <= numTicks; i++) {
            int yPosition = (int) (chartHeight + yOffset - (i * chartHeight / numTicks));

            // Draw tick marks and labels
            g.drawLine(xOffset - 5, yPosition, xOffset, yPosition);  // Tick marks

            // Draw tick labels in bold with commas (increment by 100 billion)
            String tickLabel = String.format("%,.0fB", (i * tickInterval) / 1_000_000_000);  // Display in billions (B)
            g.drawString(tickLabel, xOffset - 55, yPosition + 5);  // Adjust spacing for readability

            // Draw horizontal grid lines for better readability
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(xOffset, yPosition, chartWidth + xOffset, yPosition);
            g.setColor(Color.BLACK);  // Reset color to default for text and bars
        }

        // Draw bars and year labels
        int i = 0;
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            int year = entry.getKey();
            double totalStreams = entry.getValue();

            // Calculate height of the bar based on the total streams
            int barHeight = (int) ((totalStreams / maxTotalStreams) * chartHeight);

            // Draw the bar
            g.setColor(Color.BLUE);
            g.fillRect(xOffset + i * barWidth, (chartHeight + yOffset) - barHeight, barWidth - 10, barHeight);

            // Draw year label under each bar
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(year), xOffset + i * barWidth + barWidth / 4, chartHeight + yOffset + 20);

            i++;
        }
    }

    // Method to display the bar chart with tick marks
    public static void visualizeTotalStreamsByYear(TreeMap<Integer, Double> data) {
        JFrame frame = new JFrame("Total Streams by Year - Bar Chart with Tick Marks");
        ChartPanel chartPanel = new ChartPanel(data);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
