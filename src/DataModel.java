import java.util.ArrayList;

public class DataModel {
    private String artist_and_title;
    private String artist;
    private double total_streams;
    private double daily_streams;
    private int year;
    private String main_genre;
    private ArrayList<String> genres = new ArrayList<>();
    private String first_genre;
    private String second_genre;
    private String third_genre;

    public DataModel(String artist_and_title, String artist, double total_streams, double daily_streams, int year,String main_genre, ArrayList<String> genres, String first_genre,String second_genre, String third_genre){
        this.artist_and_title = artist_and_title;
        this.artist = artist;
        this.total_streams = total_streams;
        this.daily_streams = daily_streams;
        this.year = year;
        this.main_genre = main_genre;
        this.genres = genres;
        this.first_genre = first_genre;
        this.second_genre = second_genre;
        this.third_genre = third_genre;
    }

    public DataModel(){
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

    public String getArtist_and_title() {
        return artist_and_title;
    }

    public void setArtist_and_title(String artist_and_title) {
        this.artist_and_title = artist_and_title;
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

    public int getYear() {
        return year;
    }

    public String getMain_genre() {
        return main_genre;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public String getFirst_genre() {
        return first_genre;
    }

    public String getSecond_genre() {
        return second_genre;
    }

    public String getThird_genre() {
        return third_genre;
    }

    public String toString() {
        return "Artist and Title: " + artist_and_title + " | " + "Artist:" + artist + " | " + "Total Streams:" + total_streams;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
