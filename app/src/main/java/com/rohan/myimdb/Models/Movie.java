package com.rohan.myimdb.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohan on 18-Jul-16.
 */
public class Movie {

    private String posterPath;
    private String overview;
    private String releaseDate;
    private List<Long> genreIds = new ArrayList<>();
    private Long id;
    private String title;
    private String backdropPath;
    private Double voteAverage;

    private boolean isFavourite;

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getPosterPath() {

        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public boolean isFavourite() {
        return isFavourite;
    }
}