package com.rohan.myimdb.POJOs;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseListTrailer {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("results")
    @Expose
    private List<ResponseSingleTrailer> results = new ArrayList<>();

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
     * The results
     */
    public List<ResponseSingleTrailer> getTrailers() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<ResponseSingleTrailer> results) {
        this.results = results;
    }

}