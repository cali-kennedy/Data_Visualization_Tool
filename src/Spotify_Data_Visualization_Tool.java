import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Spotify_Data_Visualization_Tool extends JFrame {
    public static void main(String[] args) throws IOException {
        DataLoader data_loader = new DataLoader();
        ArrayList<DataModel> journalData = data_loader.loadCSVData("spotify.csv");

        DataManager dataManager = new DataManager(journalData);

        JFrame frame = new JFrame("Spotify Streaming Analytics Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());

        TablePanel tablePanel = new TablePanel(journalData, dataManager);
        frame.add(tablePanel, BorderLayout.CENTER);

        DetailsPanel detailsPanel = new DetailsPanel(dataManager);
        detailsPanel.setPreferredSize(new Dimension(300, 600));
        frame.add(detailsPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }
}
