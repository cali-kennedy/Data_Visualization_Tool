import java.util.*;
import java.util.stream.Collectors;

public class StatsCalculator {

    // Method to calculate average daily streams
    public static double calculateAverageDailyStreams(ArrayList<DataModel> data) {
        double totalDailyStreams = data.stream()
                .mapToDouble(DataModel::getDaily_streams)
                .sum();
        return data.size() > 0 ? totalDailyStreams / data.size() : 0;
    }

    // Method to get the top 5 artists by total streams
    public static List<Map.Entry<String, Double>> getTopArtists(ArrayList<DataModel> data, int limit) {
        Map<String, Double> artistStreamMap = new HashMap<>();

        for (DataModel model : data) {
            artistStreamMap.put(model.getArtist(), artistStreamMap.getOrDefault(model.getArtist(), 0.0) + model.getTotal_streams());
        }

        return artistStreamMap.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    // Method to calculate the top 5 artists' share of streams for a given year
    public static List<DataModel> getTopArtistsByYear(List<DataModel> data, int year) {
        // Filter the data by the given year
        List<DataModel> filteredData = data.stream()
                .filter(model -> model.getYear() == year)
                .sorted(Comparator.comparingDouble(DataModel::getTotal_streams).reversed())
                .limit(5) // Get the top 5 artists
                .collect(Collectors.toList());

        // Return the top 5 artists
        return filteredData;
    }

    // Calculate the total streams for the given year to get a percentage share
    public static double getTotalStreamsForYear(List<DataModel> data, int year) {
        return data.stream()
                .filter(model -> model.getYear() == year)
                .mapToDouble(DataModel::getTotal_streams)
                .sum();
    }


    // Method to calculate yearly growth rate as string
    public static String calculateYearlyGrowthRate(ArrayList<DataModel> data) {
        TreeMap<Integer, Double> yearlyStreamsMap = new TreeMap<>();
        for (DataModel model : data) {
            int year = (int) model.getYear();
            yearlyStreamsMap.put(year, yearlyStreamsMap.getOrDefault(year, 0.0) + model.getTotal_streams());
        }

        StringBuilder growthRates = new StringBuilder("Yearly Growth Rates:\n");
        Integer previousYear = null;
        Double previousYearStreams = null;

        for (Map.Entry<Integer, Double> entry : yearlyStreamsMap.entrySet()) {
            if (previousYear != null) {
                double growthRate = ((entry.getValue() - previousYearStreams) / previousYearStreams) * 100;
                growthRates.append(String.format("From %d to %d: %.2f%%\n", previousYear, entry.getKey(), growthRate));
            }
            previousYear = entry.getKey();
            previousYearStreams = entry.getValue();
        }

        return growthRates.toString();
    }

    // Method to get data for the line chart
    public static TreeMap<Integer, Double> getYearlyGrowthData(ArrayList<DataModel> data) {
        TreeMap<Integer, Double> yearlyGrowthMap = new TreeMap<>();
        TreeMap<Integer, Double> yearlyStreamsMap = new TreeMap<>();

        // Populate yearlyStreamsMap with total streams per year
        for (DataModel model : data) {
            int year = (int) model.getYear();
            yearlyStreamsMap.put(year, yearlyStreamsMap.getOrDefault(year, 0.0) + model.getTotal_streams());
        }

        Integer previousYear = null;
        Double previousYearStreams = null;

        for (Map.Entry<Integer, Double> entry : yearlyStreamsMap.entrySet()) {
            if (previousYear != null) {
                double growthRate = ((entry.getValue() - previousYearStreams) / previousYearStreams) * 100;
                yearlyGrowthMap.put(entry.getKey(), growthRate);
            }
            previousYear = entry.getKey();
            previousYearStreams = entry.getValue();
        }

        return yearlyGrowthMap;
    }

    // Method to calculate total streams by year as a TreeMap
    public static TreeMap<Integer, Double> getTotalStreamsByYear(ArrayList<DataModel> data) {
        TreeMap<Integer, Double> totalStreamsByYearMap = new TreeMap<>();
        for (DataModel model : data) {
            int year = (int) model.getYear();
            totalStreamsByYearMap.put(year, totalStreamsByYearMap.getOrDefault(year, 0.0) + model.getTotal_streams());
        }

        return totalStreamsByYearMap;
    }

    // Method to calculate total streams by year as a string (for text display)
    public static String calculateTotalStreamsByYear(ArrayList<DataModel> data) {
        TreeMap<Integer, Double> totalStreamsByYearMap = getTotalStreamsByYear(data);
        StringBuilder totalStreams = new StringBuilder("Total Streams by Year:\n");
        totalStreamsByYearMap.forEach((year, streams) -> totalStreams.append(String.format("%d: %.2f total streams\n", year, streams)));
        return totalStreams.toString();
    }
}
