package edu.weber.favmovies.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Movie {

    @NonNull
    @PrimaryKey
    private String imdbID;
    @ColumnInfo(name = "Title")
    private String Title;
    @ColumnInfo(name = "Year")
    private String Year;
    @ColumnInfo(name = "imdbRating")
    private double imdbRating;
    @ColumnInfo(name = "imdbVotes")
    private String imdbVotes;
    @ColumnInfo(name = "Released")
    private String Released;
    @ColumnInfo(name = "Runtime")
    private String Runtime;
    @ColumnInfo(name = "Genre")
    private String Genre;
    @ColumnInfo(name = "Director")
    private String Director;
    @ColumnInfo(name = "Writer")
    private String Writer;
    @ColumnInfo(name = "Actors")
    private String Actors;
    @ColumnInfo(name = "Country")
    private String Country;
    @ColumnInfo(name = "Plot")
    private String Plot;
    @ColumnInfo(name = "Language")
    private String Language;
    @ColumnInfo(name = "Type")
    private String Type;
    @ColumnInfo(name = "Poster")
    private String Poster;

    public Movie(String imdbID, String title, String year,
                 double imdbRating, String imdbVotes, String released,
                 String runtime, String genre, String director, String writer,
                 String actors, String country, String plot, String language,
                 String type, String poster) {
        this.imdbID = imdbID;
        Title = title;
        Year = year;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        Released = released;
        Runtime = runtime;
        Genre = genre;
        Director = director;
        Writer = writer;
        Actors = actors;
        Country = country;
        Plot = plot;
        Language = language;
        Type = type;
        Poster = poster;
    }

    public Movie() {

    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getReleased() {
        return Released;
    }

    public void setReleased(String released) {
        this.Released = released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public void setRuntime(String runtime) {
        this.Runtime = runtime;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        this.Genre = genre;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        this.Director = director;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        this.Writer = writer;
    }

    public String getActors() {
        return Actors;
    }

    public void setActors(String actors) {
        this.Actors = actors;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        this.Plot = plot;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        this.Language = language;
    }

    public double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(double imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public void setImdbVotes(String imdbVotes) {
        this.imdbVotes = imdbVotes;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        this.Poster = poster;
    }

    @NonNull
    @Override
    public String toString() {
        return "Movie{" +
                "Title='" + Title + '\'' +
                ", Year='" + Year + '\'' +
                ", Released='" + Released + '\'' +
                ", Runtime='" + Runtime + '\'' +
                ", Genre='" + Genre + '\'' +
                ", Director='" + Director + '\'' +
                ", Writer='" + Writer + '\'' +
                ", Actors='" + Actors + '\'' +
                ", Plot='" + Plot + '\'' +
                ", Language='" + Language + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                ", imdbVotes='" + imdbVotes + '\'' +
                ", Type='" + Type + '\'' +
                ", Poster='" + Poster + '\'' +
                ", Country='" + Country + '\'' +
                ", imdbID='" + imdbID + '\'' +
                '}';
    }
}
