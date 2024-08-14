package com.example.myapplication;


import java.util.Objects;

public class MovieItem {

    private String image, title, date;

    public MovieItem(){

    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieItem)) return false;
        MovieItem movieItem = (MovieItem) o;
        return Objects.equals(getTitle(), movieItem.getTitle()) && Objects.equals(getDate(), movieItem.getDate());
    }

    @Override
    public String toString() {
        return "MovieItem{" +
                "image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDate());
    }

}
