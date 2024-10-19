import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PieChartPanel extends JPanel {
    private JFreeChart pieChart;

    public PieChartPanel() {
        setPreferredSize(new Dimension(600, 400));
    }

    // Updates the pie chart with top artists' data
    public void updatePieChart(List<DataModel> topArtists) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (DataModel artist : topArtists) {
            dataset.setValue(artist.getArtist(), artist.getTotal_streams());
        }

        pieChart = ChartFactory.createPieChart(
                "Top 5 Artists by Total Streams", // chart title
                dataset, // data
                true, // include legend
                true,
                false
        );

        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 14));
        pieChart.getLegend().setItemFont(new Font("Arial", Font.BOLD, 18));  // Increase legend font size

        plot.setNoDataMessage("No data available");
        plot.setCircular(true);
        plot.setLabelGap(0.02);
        // Remove existing chart panel and add the new one
        removeAll();
        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setPreferredSize(new Dimension(500, 300));
        add(chartPanel);

        revalidate();
        repaint();
    }
}
