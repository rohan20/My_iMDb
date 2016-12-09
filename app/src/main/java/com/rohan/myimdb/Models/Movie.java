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
public class Movie implements Parcelable{

    private String posterPath;
    private String overview;
    private String releaseDate;
    private List<String> genreIds = new ArrayList<>();
    private String id;
    private String title;
    private String backdropPath;
    private String voteAverage;
    private String runtime;

    public Movie() {
    }

    public Movie(String posterPath, String overview, String releaseDate, List<String> genreIds, String id, String title, String backdropPath, String voteAverage, String runtime) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.runtime = runtime;
    }

    public Movie(String posterPath, String overview, String releaseDate, List<String> genreIds, String id, String title, String backdropPath, String voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        genreIds = in.createStringArrayList();
        id = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        voteAverage = in.readString();
        runtime = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
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

    public List<String> getGenreIds() {
        return genreIds;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getRuntime() {
        return runtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(posterPath);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeStringList(genreIds);
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(backdropPath);
        parcel.writeString(voteAverage);
        parcel.writeString(runtime);
    }
}