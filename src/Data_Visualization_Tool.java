import java.io.IOException;
import java.util.ArrayList;

public class Data_Visualization_Tool {
    public static void main(String[] args) throws IOException {
        DataLoader data_loader = new DataLoader();
        ArrayList<DataModel> journalData;
        journalData = data_loader.loadCSVData("spotify_short.csv");
        for (DataModel journalEntry : journalData) {
            System.out.println(journalEntry); // This calls the toString() method of DataModel
        }
    }
}