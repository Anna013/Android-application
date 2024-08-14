package com.example.myapplication;


import java.util.List;
import java.util.Objects;

public class Movie {

    private String title, image,  vote_average, overview, year;
    private int id;
    private List<String> genres;


    public Movie(String title, String image, String vote, String year,
                 String desc, List<String> genres, int id) {
        this.title = title;
        this.image = image;
        this.vote_average = vote;
        this.year = year;
        this.overview = desc;
        this.genres = genres;
        this.id = id;
    }




    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public int getID() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getImage() {
        return image;
    }

    public String getYear() {
        return year;
    }
    public String getVote_average() {
        return vote_average;
    }

    @Override
    public String toString() {
        return "movie: " + this.title + "\n";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return id == movie.id && Objects.equals(getTitle(), movie.getTitle()) && Objects.equals(getImage(), movie.getImage()) && Objects.equals(getVote_average(), movie.getVote_average()) && Objects.equals(getOverview(), movie.getOverview()) && Objects.equals(getYear(), movie.getYear()) && Objects.equals(getGenres(), movie.getGenres());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getImage(), getVote_average(), getOverview(), getYear(), id, getGenres());
    }
}
