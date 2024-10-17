import java.util.ArrayList;
import java.util.Arrays;

public class DataModel {
    private String artist_and_title;
    private String artist;
    private double total_streams;
    private double daily_streams;
    private double year;
    private String main_genre;
    private ArrayList<String> genres = new ArrayList<>();
    private String genreList;
    private String first_genre;
    private String second_genre;
    private String third_genre;

    public DataModel() {
        artist_and_title = "x";
        total_streams = 0;
        daily_streams = 0;
        year = 0;
        main_genre = "x";
        this.genres.add("x");
        this.first_genre = "x";
        this.second_genre = "x";
        this.third_genre = "x";
    }

    // Getter and Setter methods
    public String getArtist_and_title() {
        return artist_and_title;
    }

    public void setArtist_and_title(String artist_and_title) {
        this.artist_and_title = artist_and_title;
    }

    public String getArtist(){
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public double getTotal_streams() {
        return total_streams;
    }

    public void setTotal_streams(double total_streams) {
        this.total_streams = total_streams;
    }

    public double getDaily_streams() {
        return daily_streams;
    }

    public void setDaily_streams(double daily_streams) {
        this.daily_streams = daily_streams;
    }

    public double getYear() {
        return year;
    }

    public void setYear(double year){
        this.year = year;
    }

    public String getMain_genre() {
        return main_genre;
    }

    public void setMain_genre(String main_genre){
        this.main_genre = main_genre;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(String genreString) {
        // Remove square brackets, single quotes, and any extra double quotes
        genreString = genreString.replace("[", "").replace("]", "").replace("'", "").replace("\"", "").trim();

        // Now split by commas and trim each genre
        this.genres = new ArrayList<>(Arrays.asList(genreString.split("\\s*,\\s*")));

        // Store a clean string for the genreList
        this.genreList = String.join(", ", genres);
    }

    public String getFirst_genre() {
        return first_genre;
    }

    public void setFirst_genre(String first_genre) {
        this.first_genre = first_genre;
    }

    public String getSecond_genre() {
        return second_genre;
    }

    public void setSecond_genre(String second_genre) {
        this.second_genre = second_genre;
    }

    public String getThird_genre() {
        return third_genre;
    }

    public void setThird_genre(String third_genre) {
        this.third_genre = third_genre;
    }

    @Override
    public String toString() {
        return String.format("Artist and Title: %s | Artist: %s | Total Streams: %,.0f | Daily Streams : %.1f | Year: %,.0f | Genres: %s | First Genre: %s | Second Genre: %s | Third Genre: %s",
                artist_and_title, artist, total_streams, daily_streams, year, genreList, first_genre, second_genre, third_genre);
    }
}
