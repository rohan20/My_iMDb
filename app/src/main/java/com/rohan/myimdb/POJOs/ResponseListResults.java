package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListResults {

    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("genre_ids")
    @Expose
    private List<Long> genreIds = new ArrayList<Long>();
    @SerializedName("id")
    @Expose
    private Long id;
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
    private Double popularity;
    @SerializedName("vote_count")
    @Expose
    private Long voteCount;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;

    /**
     * @return The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * @return The adult
     */
    public Boolean getAdult() {
        return adult;
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
    public List<Long> getGenreIds() {
        return genreIds;
    }

    /**
     * @return The id
     */
    public Long getId() {
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
    public Double getPopularity() {
        return popularity;
    }

    /**
     * @return The voteCount
     */
    public Long getVoteCount() {
        return voteCount;
    }

    /**
     * @return The video
     */
    public Boolean getVideo() {
        return video;
    }

    /**
     * @return The voteAverage
     */
    public Double getVoteAverage() {
        return voteAverage;
    }
}