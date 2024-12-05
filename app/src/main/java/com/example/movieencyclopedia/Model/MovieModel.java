package com.example.movieencyclopedia.Model;

import android.media.Image;

import java.lang.reflect.Array;
import java.util.List;

public class MovieModel {

    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;
    private String Plot;
    private String Rating;



    public MovieModel(String Title, String Year, String imdbID, String Type, String Poster, String Plot, String Rating) {
        this.Title = Title;
        this.Year = Year;
        this.imdbID = imdbID;
        this.Type = Type;
        this.Poster = Poster;
        this.Plot = Plot;
        this.Rating = Rating;
    }
    public MovieModel() {
    }

    public String getTitle() {return Title;}
    public void setTitle(String Title){this.Title = Title;}
    public String getYear() {return Year;}
    public void setYear(String Year){this.Year = Year;}
    public String getImdbID() {return imdbID;}
    public void setImdbID(String imdbID){this.imdbID = imdbID;}
    public String getType() {return Type;}
    public void setType(String Type){this.Type = Type;}
    public String getPoster() {return Poster;}
    public void setPoster(String Poster){this.Poster = Poster;}
    public String getPlot() {return Plot;}
    public void setPlot(String Plot){this.Plot = Plot;}
    public String getRating() {return Rating;}
    public void setRating(String Rating){this.Rating = Rating;}

}
