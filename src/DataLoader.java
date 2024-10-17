import java.io.BufferedReader;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    public ArrayList<DataModel> loadCSVData(String fileName) throws IOException {

        // Use InputStream to open a file and BufferedReader to read characters
        Path path = Paths.get(fileName);
        InputStream inputStream = Files.newInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String currentLine;
        ArrayList<DataModel> journalData = new ArrayList<>();

        // Skip the header
        reader.readLine();

        // Read the file line by line
        while ((currentLine = reader.readLine()) != null) {
            String[] fields = parseCSVLine(currentLine);  // Using the parseCSVLine method for safe parsing

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
