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
            String[] fields = parseCSVLine(current_line);  // Using the parseCSVLine method for safe parsing

            // Ensure there are at least the required fields
            if (fields.length >= 8) {
                DataModel current_journalData = new DataModel();
                current_journalData.setArtist_and_title(fields[1]);
                current_journalData.setArtist(fields[2]);
                current_journalData.setTotal_streams(Double.parseDouble(fields[3]));
                current_journalData.setDaily_streams(Double.parseDouble(fields[4]));
                current_journalData.setYear(Double.parseDouble(fields[5]));
                current_journalData.setMain_genre(fields[6]);

                // Now handle the genres correctly
                current_journalData.setGenres(fields[7]);

                // Set the first, second, and third genres based on the list of genres
                ArrayList<String> genres = current_journalData.getGenres();
                if (genres.size() > 0) current_journalData.setFirst_genre(genres.get(0));
                if (genres.size() > 1) current_journalData.setSecond_genre(genres.get(1));
                if (genres.size() > 2) current_journalData.setThird_genre(genres.get(2));

                journalData.add(current_journalData);
            }
        }
        return journalData;
    }

    // Method to handle lines with quoted fields
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // toggle the state of being inside/outside quotes
            } else if (c == ',' && !inQuotes) {
                // If not inside quotes, this comma marks the end of a field
                result.add(currentField.toString().trim());
                currentField.setLength(0); // reset StringBuilder
            } else {
                currentField.append(c); // Add character to the current field
            }
        }
        // Add the last field
        result.add(currentField.toString().trim());

        return result.toArray(new String[0]);
    }
}
