package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListResults {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("genre_ids")
    @Expose
    private List<String> genreIds = new ArrayList<>();
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("original_language")
    @Expose
    private String originalLanguage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("popularity")
    @Expose
    private String popularity;
    @SerializedName("vote_count")
    @Expose
    private String voteCount;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("vote_average")
    @Expose
    private String voteAverage;

    /**
     * @return The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }


    /**
     * @return The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @return The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * @return The genreIds
     */
    public List<String> getGenreIds() {
        return genreIds;
    }

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @return The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     * @return The originalLanguage
     */
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The backdropPath
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     * @return The popularity
     */
    public String getPopularity() {
        return popularity;
    }

    /**
     * @return The voteCount
     */
    public String getVoteCount() {
        return voteCount;
    }

    /**
     * @return The video
     */
    public String getVideo() {
        return video;
    }

    /**
     * @return The voteAverage
     */
    public String getVoteAverage() {
        return voteAverage;
    }
}