package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListBackdropsAndPosters {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("backdrops")
    @Expose
    private List<ResponseSingleBackdrop> backdrops = new ArrayList<ResponseSingleBackdrop>();
    @SerializedName("posters")
    @Expose
    private List<ResponseSinglePoster> posters = new ArrayList<ResponseSinglePoster>();

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
     * The backdrops
     */
    public List<ResponseSingleBackdrop> getBackdrops() {
        return backdrops;
    }

    /**
     *
     * @param backdrops
     * The backdrops
     */
    public void setBackdrops(List<ResponseSingleBackdrop> backdrops) {
        this.backdrops = backdrops;
    }

    /**
     *
     * @return
     * The posters
     */
    public List<ResponseSinglePoster> getPosters() {
        return posters;
    }

    /**
     *
     * @param posters
     * The posters
     */
    public void setPosters(List<ResponseSinglePoster> posters) {
        this.posters = posters;
    }

}
