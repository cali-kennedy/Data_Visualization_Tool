import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class InternalBarChartPanel extends JPanel {
    private final TreeMap<Integer, Double> data;  // Year as key, Total Streams as value
    private final int chartWidth = 700;
    private final int chartHeight = 300;
    private final int xOffset = 80;
    private final int yOffset = 50;
    private final double maxTotalStreams = 500_000_000_000.0;  // Fixed max for Y-axis scaling
    private final int numTicks = 5;  // Number of tick marks on Y-axis
    private final double tickInterval = maxTotalStreams / numTicks;

    public InternalBarChartPanel(TreeMap<Integer, Double> data) {
        this.data = data;
        setPreferredSize(new Dimension(800, 400));  // Set panel size
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int barWidth = chartWidth / data.size();

        // Draw X and Y axes
        g2d.drawLine(xOffset, yOffset, xOffset, chartHeight + yOffset);  // Y-axis
        g2d.drawLine(xOffset, chartHeight + yOffset, chartWidth + xOffset, chartHeight + yOffset);  // X-axis

        // Draw Y-axis tick marks and grid lines
        for (int i = 0; i <= numTicks; i++) {
            int yPosition = chartHeight + yOffset - (i * chartHeight / numTicks);
            g2d.drawLine(xOffset - 5, yPosition, xOffset, yPosition);  // Tick marks
            g2d.drawString(String.format("%,.0fB", (i * tickInterval) / 1_000_000_000), xOffset - 55, yPosition + 5);  // Tick labels
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(xOffset, yPosition, chartWidth + xOffset, yPosition);  // Grid lines
            g2d.setColor(Color.BLACK);  // Reset color for bars and text
        }

        // Draw bars and X-axis labels (Years)
        int i = 0;
        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            int year = entry.getKey();
            double totalStreams = entry.getValue();
            int barHeight = (int) ((totalStreams / maxTotalStreams) * chartHeight);

            g2d.setColor(Color.BLUE);
            g2d.fillRect(xOffset + i * barWidth, chartHeight + yOffset - barHeight, barWidth - 10, barHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf(year), xOffset + i * barWidth + barWidth / 4, chartHeight + yOffset + 20);
            i++;
        }
    }

    // Method to visualize the bar chart
    public static void visualizeTotalStreamsByYear(TreeMap<Integer, Double> data) {
        JFrame frame = new JFrame("Total Streams by Year - Bar Chart");
        InternalBarChartPanel chartPanel = new InternalBarChartPanel(data);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
