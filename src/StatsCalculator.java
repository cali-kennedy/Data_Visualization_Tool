import java.util.*;
import java.util.stream.Collectors;

public class StatsCalculator {

    // Method to calculate the average daily streams
    public static double calculateAverageDailyStreams(ArrayList<DataModel> data) {
        // Sum the daily streams and calculate the average
        double totalDailyStreams = data.stream()
                .mapToDouble(DataModel::getDaily_streams)
                .sum();

        // Return the average or 0 if there's no data
        return data.isEmpty() ? 0 : totalDailyStreams / data.size();
    }

    // Method to get the top artists by total streams, limited to a specified number
    public static List<Map.Entry<String, Double>> getTopArtists(ArrayList<DataModel> data, int limit) {
        // Create a map to store total streams by artist
        Map<String, Double> artistStreamMap = new HashMap<>();

        // Sum the total streams for each artist
        for (DataModel model : data) {
            String artist = model.getArtist();
            double totalStreams = model.getTotal_streams();
            artistStreamMap.put(artist, artistStreamMap.getOrDefault(artist, 0.0) + totalStreams);
        }

        // Sort artists by total streams in descending order and return the top artists
        return artistStreamMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit) // Limit to the specified number of top artists
                .collect(Collectors.toList());
    }

    // Method to calculate the yearly growth rate of total streams
    public static String calculateYearlyGrowthRate(ArrayList<DataModel> data) {
        // TreeMap to store the total streams per year (sorted by year)
        TreeMap<Integer, Double> yearlyStreamsMap = new TreeMap<>();

        // Sum the total streams for each year
        for (DataModel model : data) {
            int year = (int) model.getYear();
            yearlyStreamsMap.put(year, yearlyStreamsMap.getOrDefault(year, 0.0) + model.getTotal_streams());
        }

        // Build the yearly growth rate output
        StringBuilder growthRates = new StringBuilder("Yearly Growth Rates:\n");
        Integer previousYear = null;
        Double previousYearStreams = null;

        // Calculate the percentage growth for each consecutive year
        for (Map.Entry<Integer, Double> entry : yearlyStreamsMap.entrySet()) {
            if (previousYear != null && previousYearStreams != null) {
                double currentYearStreams = entry.getValue();
                double growthRate = ((currentYearStreams - previousYearStreams) / previousYearStreams) * 100;
                growthRates.append(String.format("From %d to %d: %.2f%%\n", previousYear, entry.getKey(), growthRate));
            }

            // Update previous year and stream values for the next comparison
            previousYear = entry.getKey();
            previousYearStreams = entry.getValue();
        }

        return growthRates.toString();
    }

    // Method to calculate the total streams by year
    public static String calculateTotalStreamsByYear(ArrayList<DataModel> data) {
        // TreeMap to store the total streams per year (sorted by year)
        TreeMap<Integer, Double> totalStreamsByYearMap = new TreeMap<>();

        // Sum the total streams for each year
        for (DataModel model : data) {
            int year = (int) model.getYear();
            totalStreamsByYearMap.put(year, totalStreamsByYearMap.getOrDefault(year, 0.0) + model.getTotal_streams());
        }

        // Build the output showing the total streams for each year
        StringBuilder totalStreamsOutput = new StringBuilder("Total Streams by Year:\n");
        totalStreamsByYearMap.forEach((year, streams) ->
                totalStreamsOutput.append(String.format("%d: %.2f total streams\n", year, streams))
        );

        return totalStreamsOutput.toString();
    }
}
