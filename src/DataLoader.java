import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.*;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataLoader {

    public ArrayList<DataModel> loadCSVData(String fileName) throws IOException {

        String contents = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(contents);
        ArrayList<DataModel> journalData = new ArrayList<>();

        scanner.nextLine();
        scanner.useDelimiter(",");


        while (scanner.hasNextLine()) {
            String current_line = scanner.nextLine();
            String[] fields = current_line.split(",");

            // Ensure there are at least two fields (adjust this if your file has more)
            if (fields.length >= 2) {
                DataModel current_journalData = new DataModel();
                current_journalData.setArtist_and_title(fields[1]);
                current_journalData.setArtist(fields[2]);
                journalData.add(current_journalData);
                current_journalData.setTotal_streams(Double.parseDouble(fields[3]));
            }

        }
        return journalData;
    }
}