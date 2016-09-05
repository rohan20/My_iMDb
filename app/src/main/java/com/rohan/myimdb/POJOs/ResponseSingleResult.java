package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseSingleResult {

    @SerializedName("adult")
    @Expose
    private Boolean adult;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("budget")
    @Expose
    private Long budget;
    @SerializedName("genres")
    @Expose
    private List<ResponseSingleGenre> genres = new ArrayList<ResponseSingleGenre>();
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("revenue")
    @Expose
    private Long revenue;
    @SerializedName("runtime")
    @Expose
    private Long runtime;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Long voteCount;

    /**
     *
     * @return
     * The adult
     */
    public Boolean getAdult() {
        return adult;
    }

    /**
     *
     * @param adult
     * The adult
     */
    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    /**
     *
     * @return
     * The backdropPath
     */
    public String getBackdropPath() {
        return backdropPath;
    }

    /**
     *
     * @param backdropPath
     * The backdrop_path
     */
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    /**
     *
     * @return
     * The budget
     */
    public Long getBudget() {
        return budget;
    }

    /**
     *
     * @param budget
     * The budget
     */
    public void setBudget(Long budget) {
        this.budget = budget;
    }

    /**
     *
     * @return
     * The genres
     */
    public List<ResponseSingleGenre> getGenres() {
        return genres;
    }

    /**
     *
     * @param genres
     * The genres
     */
    public void setGenres(List<ResponseSingleGenre> genres) {
        this.genres = genres;
    }

    /**
     *
     * @return
     * The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     *
     * @param homepage
     * The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The originalTitle
     */
    public String getOriginalTitle() {
        return originalTitle;
    }

    /**
     *
     * @param originalTitle
     * The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     *
     * @return
     * The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     *
     * @param overview
     * The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     *
     * @return
     * The popularity
     */
    public Double getPopularity() {
        return popularity;
    }

    /**
     *
     * @param popularity
     * The popularity
     */
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    /**
     *
     * @return
     * The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     *
     * @param posterPath
     * The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     *
     * @return
     * The releaseDate
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     *
     * @param releaseDate
     * The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     *
     * @return
     * The revenue
     */
    public Long getRevenue() {
        return revenue;
    }

    /**
     *
     * @param revenue
     * The revenue
     */
    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    /**
     *
     * @return
     * The runtime
     */
    public Long getRuntime() {
        return runtime;
    }

    /**
     *
     * @param runtime
     * The runtime
     */
    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    /**
     *
     * @return
     * The tagline
     */
    public String getTagline() {
        return tagline;
    }

    /**
     *
     * @param tagline
     * The tagline
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The voteAverage
     */
    public Double getVoteAverage() {
        return voteAverage;
    }

    /**
     *
     * @param voteAverage
     * The vote_average
     */
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     *
     * @return
     * The voteCount
     */
    public Long getVoteCount() {
        return voteCount;
    }

    /**
     *
     * @param voteCount
     * The vote_count
     */
    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

}