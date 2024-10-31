import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public class Spotify_Data_Visualization_Tool extends JFrame{
    public static void main(String[] args) throws IOException {
        DataLoader data_loader = new DataLoader();
        ArrayList<DataModel> journalData;
        journalData = data_loader.loadCSVData("spotify.csv");
        for (DataModel journalEntry : journalData) {
            System.out.println(journalEntry); // This calls the toString() method of DataModel
        }

        JFrame frame = new JFrame("Spotify Streaming Analytics Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Create and add the TablePanel
        TablePanel tablePanel = new TablePanel(journalData);
        frame.add(tablePanel);
        frame.setVisible(true);


    }
}